package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.basictype.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient implements Watcher {

    private static ZooKeeper zookeeper = null;
    private CountDownLatch cdl = new CountDownLatch(1);
    private int SessionTimeOut = 30 * 1000;

    public ZookeeperClient() {
        if (this.zookeeper == null) {
            System.out.println("执行zookeeper client初始化");
            try {
                String zkconfig = System.getenv("zkconfig");
                dial(zkconfig);
            } catch (Exception e) {
                System.out.println("连接ZOOKEEPER异常.");
                System.exit(-1);
            }
        }
    }

    public ZookeeperClient(String zkConfig) {
        if (this.zookeeper == null) {
            System.out.println("执行zookeeper client初始化");
            try {
                dial(zkConfig);
            } catch (Exception e) {
                System.out.println("连接ZOOKEEPER异常.");
                System.exit(-1);
            }
        }
    }

    void dial(String zkconfig) throws Exception {
        if (StringUtils.isEmpty(zkconfig)) {
            throw new RuntimeException("缺少Zookeeper服务器配置信息");
        }
        this.zookeeper = new ZooKeeper(zkconfig, SessionTimeOut, this);
        this.cdl.await();
    }

    public void process(WatchedEvent event) {
        // if (event.getState() == Event.KeeperState.SyncConnected) {
        if (this.zookeeper.getState().isConnected()) {
            cdl.countDown();
        }
    }

    public void Create(String path, byte[] value, boolean persistent) throws Exception {
        CreateMode mode = CreateMode.PERSISTENT;
        if (!persistent) {
            mode = CreateMode.EPHEMERAL;
        }
        this.zookeeper.create(path, value, Ids.OPEN_ACL_UNSAFE, mode);
    }

    /**
     * durable,  true表示持久节点，false表示临时节点
     */
    public void Set(String key, byte[] value, boolean durable) throws Exception {

        CreateMode mode = CreateMode.PERSISTENT;
        if (!durable) {
            mode = CreateMode.EPHEMERAL;
        }

        //
        Stat stat = this.zookeeper.exists(key, null);
        if (stat != null) {
            this.zookeeper.setData(key, value, stat.getVersion());
            return;
        }

        String[] nodes = key.split("/");
        String root = "";
        for (String node : nodes) {
            if (node.equals("")) {
                continue;
            }

            root = root + "/" + node;
            if (!(this.zookeeper.exists(root, null) == null)) {
                continue;
            }

            if (root.equals(key)) {
                this.zookeeper.create(root, value, Ids.OPEN_ACL_UNSAFE, mode);
                continue;
            }

            this.zookeeper.create(root, null, Ids.OPEN_ACL_UNSAFE, mode);
        }

        return;

    }

    /**
     * 获取节点的值
     *
     * @param key
     * @return
     */
    public byte[] Get(String key) {
        try {
            return this.zookeeper.getData(key, false, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "null".getBytes();
        }
    }


    /**
     * 获取子节点
     *
     * @param key
     * @return
     */
    public List<String> Children(String key) {
        List<String> children = new ArrayList<String>();
        try {
            children = this.zookeeper.getChildren(key, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }

    public void ChildrenW(String key, Watcher watcher) throws Exception {
        this.zookeeper.getChildren(key, watcher);
    }

    public List<String> children(String key, Watcher watcher) throws Exception {
        return this.zookeeper.getChildren(key, watcher);
    }

    public byte[] getData(String key, Watcher watcher) throws Exception {
        return this.zookeeper.getData(key, watcher, null);
    }


    /**
     * 删除节点
     *
     * @param key
     */
    public void Delete(String key) {

        List<String> children = Children(key);
        for (String child : children) {
            Delete(key + "/" + child);
        }

        try {
            Transaction t = this.zookeeper.transaction();
            t.delete(key, -1);
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在
     */
    public boolean Exist(String key) throws Exception {
        Stat stat = this.zookeeper.exists(key, null);
        if (stat == null) {
            return false;
        }
        return true;
    }

    public void close() {
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
