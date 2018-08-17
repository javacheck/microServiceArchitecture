package cn.self.cloud.commonutils.zother.watch;


import com.terjoy.comm.ZooKeeperService;
import com.terjoy.comm.ZookeeperManager;
import cn.self.cloud.commonutils.zookeeper.Process;
import cn.self.cloud.commonutils.zother.service.impl.InterfacesRouteServiceImpl;
import cn.self.cloud.commonutils.zookeeper.TreeNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.redkale.boot.Application;
import org.redkale.boot.NodeHttpServer;
import org.redkale.boot.NodeServer;
import org.redkale.boot.watch.AbstractWatchService;
import org.redkale.convert.json.JsonConvert;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.net.http.RestService;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * 旧的服务注册机制 20171226重写，新的机制参考ServiceConfigService
 * Created by cp on 2017/7/4.
 */
@RestService(name = "servermanage", catalog = "watch", repair = false)
public class ServerManageWatchService extends AbstractWatchService {

    Logger logger = Logger.getLogger(getClass().getCanonicalName());
    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    @Resource(name = "property.module.name")
    String moduleName;

    @Resource(name = "property.module.port")
    int modulePort;

    @Resource(name = "property.module.ip")
    String moduleIp;

    private String processId;

    @Resource
    Application application;

    NodeHttpServer server = null;

    boolean isRegistThreadRunning = true;

    public static final int ZooKeeperTimeout = 30000;

    private ZooKeeperService zooKeeperService = null;

    /***
     * 初始化时，注册到配置中心，同时Watch服务列表的变化，为接口调用提供支持，接口调用的机制中，
     * 预留进行负载均衡的接口
     * 疑问：如何记录服务之间的调用轨迹？
     *
     * @param config 配置参数
     */
    public void init_111(AnyValue config) {

        if (server == null) {
            server = initAndGetNodeHttpServer();
        }
        if (modulePort <= 0)
            modulePort = server.getHttpServer().getSocketAddress().getPort();
        if (StringUtils.isEmpty(moduleIp))
            moduleIp = Ip.ipv4();
        if (zooKeeperService == null) {
            zooKeeperService = ZookeeperManager.getZookeeperService(zkConfig, ZooKeeperTimeout);
        }
//        processId=regist();
        new Thread(() -> {
            int createProcessCount = 1;
//            int waitCount=0;
            while (isRegistThreadRunning) {
                if (StringUtils.isNotEmpty(processId)) {
                    if (zooKeeperService.exist(TreeNode.module + "/" + processId)) {
//                        waitCount++;
//                        System.out.println("节点存在，休息10秒.");
                        try {
                            Thread.sleep(10000);//每过10秒检查情况
//                            if(waitCount==2){
//                                waitCount=0;
                            // refreshNode();
//                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        logger.info("节点存在" + processId);
                        continue;
                    } else {
                        try {
                            processId = regist();
                            logger.info("节点不存在，重新注册服务成功." + processId);
                            createProcessCount = 0;
                        } catch (Exception e) {
                            createProcessCount++;
                            try {
                                Thread.sleep(createProcessCount * 1000);
                            } catch (InterruptedException ie) {
                                e.printStackTrace();
                            }
                            logger.info("注册服务失败.");
                            continue;
                        }
                    }
                } else {
                    try {
                        processId = regist();
                        logger.info("注册服务成功.");
                        createProcessCount = 0;
                    } catch (Exception e) {
                        createProcessCount++;
                        try {
                            Thread.sleep(createProcessCount * 1000);
                        } catch (InterruptedException ie) {
                            e.printStackTrace();
                        }
                        logger.info("注册服务失败.");
                        continue;
                    }
                }
            }
        }, "服务注册线程").start();

        refreshProcesses(); //启动的时候，先刷新一下服务列表

        zooKeeperService.watchChildren(TreeNode.module, (WatchedEvent event) -> {
            refreshProcesses();
        });
    }


    @Override
    public void destroy(AnyValue config) {
        isRegistThreadRunning = false;
    }


    private void refreshNode() {
        Process process = new Process();
        process.setModule(moduleName);
        process.setPort(modulePort);
        // process.setHost(InetAddress.getLocalHost().getHostAddress());
        process.setHost(Ip.ipv4());
        process.setTime(System.currentTimeMillis());
        process.setDir(Dir.pwd());  //当前目录
        process.setPid(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        byte[] bytes = Base64.encodeBase64(JsonConvert.root().convertTo(process).getBytes());
        String path = TreeNode.module + "/" + processId;
        zooKeeperService.setData(path, bytes);
        logger.info("更新了节点数据");
    }

    private synchronized void refreshProcesses() {
        List<Process> processList = new ArrayList<>();
        String path = TreeNode.module;
        List<String> list = zooKeeperService.getChildren(path);
        for (String p : list) {
            if (StringUtils.isNotEmpty(p)) {
                String node = path + "/" + p;
                try {
                    byte[] bytes = zooKeeperService.getData(node);
                    Process process = JsonConvert.root().convertFrom(Process.class, new String(bytes));
                    if (process != null) {
                        processList.add(process);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (server == null) {
            server = initAndGetNodeHttpServer();
        }
        InterfacesRouteServiceImpl interfacesRouteManager = server.getResourceFactory().find(InterfacesRouteServiceImpl.class);
        interfacesRouteManager.refreshProcesses(processList);
        logger.info("刷新服务列表：" + processList.size());
    }

    private NodeHttpServer initAndGetNodeHttpServer() {
        NodeHttpServer server = null;
        List<NodeServer> servList = application.getNodeServers();
        for (NodeServer nodeServer : servList) {
            if (!nodeServer.isWATCH() && !nodeServer.isSNCP()) {
                if (modulePort > 0) { //配置了的话，要以配置的服务为当前服务
                    if (nodeServer.getSocketAddress().getPort() == modulePort)
                        server = (NodeHttpServer) nodeServer;
                } else {//没有配置，则为当前服务
                    server = (NodeHttpServer) nodeServer;
                }
            }
        }
        return server;
    }

    private String regist() {
        Process process = new Process();
        process.setModule(moduleName);
        //常用配置
        process.setPort(modulePort);
        process.setHost(moduleIp);

        // process.setHost(InetAddress.getLocalHost().getHostAddress());
        // csb service的专用配置
//        process.setPort(6062);
//        process.setHost("116.62.187.50");
        process.setTime(System.currentTimeMillis());
        process.setDir(Dir.pwd());  //当前目录
        process.setPid(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        String processId = produceProcessId(moduleIp, process.getModule(), process.getPort());
        byte[] bytes = JsonConvert.root().convertTo(process).getBytes();
        String path = TreeNode.module + "/" + processId;
        if (zooKeeperService.exist(path)) {
//            zooKeeperService.delete(path);
            zooKeeperService.setData(path, bytes);
        } else {
            zooKeeperService.create(path, bytes, CreateMode.EPHEMERAL);
        }
        return processId;
    }

    public String produceProcessId(String ip, String host, int port) {
        int hashcode = new String(ip + host + port).hashCode();
        return String.valueOf(hashcode);
    }

    private int heartbeat() {
        return 0;
    }

    //进行服务器关闭操作
    @RestMapping(name = "shutdown")
    public List<String> info(@RestParam(name = "#") int authid) {
        System.out.println("authid参数: " + authid);
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        return list;
    }
}
