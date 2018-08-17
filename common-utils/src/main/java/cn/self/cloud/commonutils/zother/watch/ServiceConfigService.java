package cn.self.cloud.commonutils.zother.watch;


import cn.self.cloud.commonutils.zookeeper.Process;
import cn.self.cloud.commonutils.zother.service.impl.InterfacesRouteServiceImpl;
import cn.self.cloud.commonutils.zookeeper.TreeNode;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


/**
 * 新版的服务注册机制,增加服务器黑名单逻辑
 * Created by cp on 2017/12/26.
 */
@RestService(name = "serviceconfig", catalog = "watch", repair = false)
public class ServiceConfigService extends AbstractWatchService {

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

    AtomicBoolean registSuccess = new AtomicBoolean(false);


    private ZkClient zkClient = null;

    private Lock lock = new ReentrantLock();

    /***
     * 初始化时，注册到配置中心，同时Watch服务列表的变化，为接口调用提供支持，接口调用的机制中，
     * 预留进行负载均衡的接口
     * 疑问：如何记录服务之间的调用轨迹？
     *
     * @param config 配置参数
     */
    @Override
    public void init(AnyValue config) {

        if (server == null) {
            server = initAndGetNodeHttpServer();
        }
        if (modulePort <= 0)
            modulePort = server.getHttpServer().getSocketAddress().getPort();
        if (StringUtils.isEmpty(moduleIp))
            moduleIp = Ip.ipv4();
//        processId=regist();
        logger.info("ZK配置信息："+zkConfig);
        zkClient = new ZkClient(zkConfig);
        zkClient.waitUntilConnected();

        new Thread(() -> {
            while (!registSuccess.get()) {
                int createProcessCount = 1;
                if (StringUtils.isEmpty(processId)) {
                    try {
                        processId = regist();
                        registSuccess.set(true);
                        logger.info("注册服务成功.");
                        createProcessCount = 0;
                    } catch (Exception e) {
                        createProcessCount++;
                        try {
                            Thread.sleep(createProcessCount * 1000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                        logger.info("注册服务失败.");
                    }
                }
            }
        }, "服务注册线程").start();


        refreshProcesses(); //启动的时候，先刷新一下服务列表
        lock.lock();
        try {
            zkClient.subscribeChildChanges(TreeNode.module, (String parentPath, List<String> currentChilds) -> {
                refreshProcesses();
            });
        } finally {
            lock.unlock();
        }


    }


    @Override
    public void destroy(AnyValue config) {
        zkClient.close();
    }

    private void refreshProcesses() {
        List<Process> processList = new ArrayList<>();
        String path = TreeNode.module;
        lock.lock();
        try {
            List<String> list = zkClient.getChildren(path);
            for (String p : list) {
                if (StringUtils.isNotEmpty(p)) {
                    String node = path + "/" + p;
                    try {
                        String json = zkClient.readData(node);
                        Process process = JsonConvert.root().convertFrom(Process.class, json);
                        if (process != null) {
                            processList.add(process);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } finally {
            lock.unlock();
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
        String json = JsonConvert.root().convertTo(process);
        String path = TreeNode.module + "/" + processId;
        lock.lock();
        try {
            List<String> blackList = zkClient.getChildren(TreeNode.black);
            if (blackList.indexOf(processId) >= 0) {// 在黑名单中，不注册服务，直接退出
//                application.sendCommand("SHUTDOWN");
                logger.info("当前服务已经在黑名单中，直接退出...");
                System.exit(0);
                return null;
            }
            if (zkClient.exists(path)) {
                zkClient.delete(path);
//                String v = zkClient.readData(path);
//                if (!v.equals(json))
//                    zkClient.writeData(path, json);
            }
            zkClient.create(path, json, CreateMode.EPHEMERAL);
            IZkDataListener dataListener = new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    //数据被修改，不管
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    //节点被删除,要尝试重新创建
                    lock.lock();
                    try {
                        zkClient.unsubscribeDataChanges(dataPath, this);
                    } finally {
                        lock.unlock();
                    }
                    regist();
                }
            };
            zkClient.subscribeDataChanges(path, dataListener);
            logger.info("注册服务成功");
        } finally {
            lock.unlock();
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
