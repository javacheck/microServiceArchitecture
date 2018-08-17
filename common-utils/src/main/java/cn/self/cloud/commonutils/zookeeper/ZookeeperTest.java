package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.internet.IpUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.zookeeper.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cp on 2017/7/11.
 */
public class ZookeeperTest {

    public static void main(String[] args) throws Exception {
//        String zkconfig = "120.26.80.24:2181";
//        String zkconfig = "121.41.88.144:2181";
        String zkconfig = "localhost:2181";
//        String zkconfig = "210.43.57.71:2181";
        ZooKeeperService zooKeeperService = ZookeeperManager.getZookeeperService(zkconfig, 30000);
        if (!zooKeeperService.exist(TreeNode.module)) {
            zooKeeperService.create(TreeNode.module, null, CreateMode.PERSISTENT);
        }
        System.out.println("modules:" + zooKeeperService.getAndWatchChildren(TreeNode.module, (WatchedEvent event) -> {
            System.out.println("得到通知事件:" + event);
            System.out.println(zooKeeperService.getChildren(TreeNode.module));
        }));
//        System.out.println(new String(zooKeeperService.getData("/tj/config/redis/uc")));
        init(zooKeeperService);
        int index = 0;
        while (true) {
            Thread.sleep(10000);
//            regist("csb" + index++, 2100, zooKeeperService);
            //zooKeeperService.create("/tj/module/test" + index++, "{tests,test.}".getBytes(), CreateMode.EPHEMERAL);
        }
    }

    public static void init(ZooKeeperService zooKeeperService) {
        String path = TreeNode.root;
        checkAndCreateNode(path, zooKeeperService);
        path = TreeNode.module;
        checkAndCreateNode(path, zooKeeperService);
        path = TreeNode.black;
        checkAndCreateNode(path, zooKeeperService);
        path = TreeNode.config;
        checkAndCreateNode(path, zooKeeperService);
        path = TreeNode.mysql;
        checkAndCreateNode(path, zooKeeperService);
        path = TreeNode.redis;
        checkAndCreateNode(path, zooKeeperService);
        String data = "[{\"dbname\":\"db_comm\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_comm";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_special_union\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_special_union";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_kworld\",\"host\":\"rdscp5h7mz0uj29xeo6uo.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_kworld";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_cht\",\"host\":\"rdscp5h7mz0uj29xeo6uo.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_cht";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_uc\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_uc";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_eep\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_eep";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_circle\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80047023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_circle";
        setNodeData(path, data, zooKeeperService);

        data = "[{\"dbname\":\"db_csb\",\"host\":\"rdscp5h7mz0uj29xeo6u.mysql.rds.aliyuncs.com\",\"id\":\"20cf058b60ad408e80037023d6d04f4d\",\"password\":\"asdf20160225\",\"port\":3306,\"type\":1,\"username\":\"test1\",\"connectionPoolInfo\":{\"maxPoolSize\":10,\"minPoolSize\":2,\"initialPoolSize\":2,\"idleConnectionTestPeriod\":120,\"preferredTestQuery\":\"select 1 \",\"maxIdleTime\":25200,\"testConnectionOnCheckout\":true }}]";
        path = TreeNode.mysql + "/db_csb";
        setNodeData(path, data, zooKeeperService);


        data = "[{\"auth\":\"terjoy2015\",\"host\":\"120.26.80.24\",\"id\":\"c5d1208eb47447889b77190662b7a6a5\",\"port\":6379,\"type\":1}," +
                "{\"auth\":\"terjoy2015\",\"host\":\"120.26.80.24\",\"id\":\"c5d1208eb47447889b77190662b7a6a5\",\"port\":6379,\"type\":0}]";
        path = TreeNode.redis + "/uc";
        setNodeData(path, data, zooKeeperService);
    }

    private static void setNodeData(String path, String data, ZooKeeperService zooKeeperService) {
        zooKeeperService.create(path, data.getBytes(), CreateMode.PERSISTENT);
        System.out.println("初始化节点数据按成：" + path);
    }

    private static void checkAndCreateNode(String path, ZooKeeperService zooKeeperService) {
        if (!zooKeeperService.exist(path)) {
            zooKeeperService.create(path, null, CreateMode.PERSISTENT);
            System.out.println("创建" + path + "完成");
        }
    }


    private static String regist(String moduleName, int modulePort, ZooKeeperService zooKeeperService) {
        Process process = new Process();
        process.setModule(moduleName);
        process.setPort(modulePort);
        // process.setHost(InetAddress.getLocalHost().getHostAddress());
        process.setHost(IpUtils.ipv4());
        process.setTime(System.currentTimeMillis());
        process.setDir(System.getProperty("user.dir"));  //当前目录
        process.setPid(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        String processId = produceProcessId(process.getModule(), process.getPort());
        byte[] bytes = Base64.encodeBase64(process.toString().getBytes());
        System.out.println("创建临时节点的内容长度：" + bytes.length);
        String path = TreeNode.module + "/" + processId;
        zooKeeperService.create(path, bytes, CreateMode.EPHEMERAL);
        return processId;
    }

    public static String produceProcessId(String host, int port) {
        int hashcode = new String(host + port).hashCode();
        return String.valueOf(hashcode);
    }

    public static void test() {

        new Thread(() -> {
            try {
                String zkconfig = "120.26.80.24:2181";
                CountDownLatch latch = new CountDownLatch(1);
                ZooKeeper zookeeper = new ZooKeeper(zkconfig, 20000, (WatchedEvent event) -> {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                });
                latch.await();
                String path = "/tj/module/test";
                zookeeper.create(path, "{test}".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                while ((zookeeper.exists(path, null)) != null) {
                    Thread.sleep(2000);
                    System.out.println("节点存在，休息两秒");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, "注册线程").start();

    }
}
