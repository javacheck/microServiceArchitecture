package cn.self.cloud.commonutils.zother.service;

import cn.self.cloud.commonutils.zookeeper.Process;
import com.terjoy.comm.ZooKeeperService;
import com.terjoy.comm.ZookeeperManager;
import cn.self.cloud.commonutils.zookeeper.TreeNode;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.redkale.boot.Application;
import org.redkale.boot.NodeHttpServer;
import org.redkale.boot.NodeServer;
import org.redkale.convert.json.JsonConvert;
import org.redkale.net.http.RestMapping;
import org.redkale.net.http.RestParam;
import org.redkale.service.Local;
import org.redkale.util.AnyValue;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cp on 2017/7/17.
 */
@Local
public class ServerManageService { //extends AbstractService {

    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    @Resource(name = "property.module.name")
    String moduleName;

    int modulePort;

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

    public void init(AnyValue config) {

     /*   if (server == null) {
            server = initAndGetNodeHttpServer();
        }
        modulePort = server.getHttpServer().getSocketAddress().getPort();
       */
        modulePort = 6060;
        if (zooKeeperService == null) {
            zooKeeperService = ZookeeperManager.getZookeeperService(zkConfig, ZooKeeperTimeout);
        }
        processId = regist();
        new Thread(() -> {
            int createProcessCount = 1;
            while (isRegistThreadRunning) {
                if (StringUtils.isNotEmpty(processId)) {

                    if (zooKeeperService.exist(TreeNode.module + "/" + processId)) {
                        System.out.println("节点存在，休息3秒.");
                        try {
                            Thread.sleep(3000);//每过10秒检查情况
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    } else {
                        try {
                            processId = regist();
                            System.out.println("节点不存在，重新注册服务成功.");
                            createProcessCount = 0;
                        } catch (Exception e) {
                            createProcessCount++;
                            try {
                                Thread.sleep(createProcessCount * 1000);
                            } catch (InterruptedException ie) {
                                e.printStackTrace();
                            }
                            System.out.println("注册服务失败.");
                            continue;
                        }
                    }
                } else {
                    try {
                        processId = regist();
                        System.out.println("注册服务成功.");
                        createProcessCount = 0;
                    } catch (Exception e) {
                        createProcessCount++;
                        try {
                            Thread.sleep(createProcessCount * 1000);
                        } catch (InterruptedException ie) {
                            e.printStackTrace();
                        }
                        System.out.println("注册服务失败.");
                        continue;
                    }
                }
            }
        }, "服务注册线程").start();

        refreshProcesses();
        zooKeeperService.watchChildren(TreeNode.module, (WatchedEvent event) -> {
            refreshProcesses();
        });
    }


    public void destroy(AnyValue config) {
        isRegistThreadRunning = false;
    }

    private void refreshProcesses() {
        List<Process> processList = new ArrayList<>();
        String path = TreeNode.module;
        List<String> list = zooKeeperService.getChildren(path);
        for (String p : list) {
            if (StringUtils.isNotEmpty(p)) {
                String node = path + "/" + p;
                byte[] bytes = zooKeeperService.getData(node);
                try {
                    Process process = JsonConvert.root().convertFrom(Process.class, new String(bytes));
                    if (process != null) {
                        processList.add(process);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
     /*   if (server == null) {
            server = initAndGetNodeHttpServer();
        }
        InterfacesRouteServiceImpl interfacesRouteManager = server.getResourceFactory().find(InterfacesRouteServiceImpl.class);
        interfacesRouteManager.refreshProcesses(processList);
       */
        System.out.println("刷新服务列表：" + processList.size());

    }

    private NodeHttpServer initAndGetNodeHttpServer() {
        NodeHttpServer server = null;
        List<NodeServer> servList = application.getNodeServers();
        for (NodeServer nodeServer : servList) {
            if (!nodeServer.isWATCH() && !nodeServer.isSNCP()) {
                server = (NodeHttpServer) nodeServer;
            }
        }
        return server;
    }

    private String regist() {
        Process process = new Process();
        process.setModule(moduleName);
        process.setPort(modulePort);
        // process.setHost(InetAddress.getLocalHost().getHostAddress());
        process.setHost(Ip.ipv4());
        process.setTime(System.currentTimeMillis());
        process.setDir(Dir.pwd());  //当前目录
        process.setPid(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        String processId = produceProcessId(process.getModule(), process.getPort());
        byte[] bytes = JsonConvert.root().convertTo(process).getBytes();
        String path = TreeNode.module + "/" + processId;
        zooKeeperService.create(path, bytes, CreateMode.EPHEMERAL);
        return processId;
    }

    public String produceProcessId(String host, int port) {
        int hashcode = new String(host + port).hashCode();
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
