package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.basictype.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cp on 2017/7/12.
 */
public class ZookeeperManager {
    public static final int ZooKeeperTimeout = 30000;
    /**
     * zookeeper客户端名称->zookeeper客户端对应关系
     */
    private static Map<String, ZooKeeper> clientMap = new HashMap();

    private static ZookeeperManager zookeeperManager = null;

    private ZookeeperManager() {
        if (zookeeperManager == null) {
            zookeeperManager = new ZookeeperManager();
        }
    }

    public synchronized static ZooKeeperService getZookeeperService(String zkConfig, int timeout) {
        if (StringUtils.isEmpty(zkConfig)) {
            throw new RuntimeException("zookeeper 配置信息不能为空");
        }
        String hash = stringHashCode(zkConfig);
        ZooKeeper zooKeeper = clientMap.get(hash);
        if (zooKeeper == null) {
            zooKeeper = connectToZookeeper(zkConfig, timeout);
            clientMap.put(hash, zooKeeper);
        }
        return new ZooKeeperServiceImpl(zooKeeper, zkConfig, timeout);
    }

    public synchronized static ZooKeeper invalidAndGetZookeeper(String zkConfig, int timeout) {
        if (StringUtils.isEmpty(zkConfig))
            return null;
        String key = stringHashCode(zkConfig);
        ZooKeeper zooKeeper = clientMap.get(key);
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
                clientMap.remove(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zooKeeper = connectToZookeeper(zkConfig, timeout);
        if (zooKeeper != null) {
            clientMap.put(key, zooKeeper);
            return zooKeeper;
        }
        return null;
    }

    public synchronized static ZooKeeperService getZookeeperService(String zkConfig) {
        return getZookeeperService(zkConfig, ZooKeeperTimeout);
    }

    private static String stringHashCode(String str) {
        return String.valueOf(str.hashCode());
    }


    private static ZooKeeper connectToZookeeper(String zkConfig, final int timeout) {
        if (StringUtils.isEmpty(zkConfig)) {
            throw new RuntimeException("zookeeper 配置信息为空,无法连接到zookeeper.");
        }
        try {
            int zkTimeout=timeout;
            if (timeout <= 0) {
                zkTimeout = ZooKeeperTimeout;
            }
            CountDownLatch latch = new CountDownLatch(1);
            ZooKeeper client = new ZooKeeper(zkConfig, zkTimeout, (WatchedEvent event) -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
//                else if(event.getState() == Watcher.Event.KeeperState.Expired){
//                    invalidAndGetZookeeper(zkConfig,timeout);
//                }
                System.out.println("接收到节点通知：" + event);
                //else if(event.getType()==Watcher.Event.EventType.None && event.getState()==Watcher.Event.KeeperState.Disconnected){
                //连接超时，或者断开时的通知
                //}

            });
            latch.await();
            return client;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
