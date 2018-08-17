package cn.self.cloud.commonutils.zookeeper;

import cn.self.cloud.commonutils.basictype.StringUtils;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于zookeeper的分布式锁，实现原理
 * 每次试图获取分布式锁时，在锁节点下创建临时顺序节点，
 * 如果当前节点是顺序节点中最小的，则获得锁
 * 如果不是，则对比最它小的节点建立监听，当前面的节点被删除时，
 * 当前节点会接收到通知，此时再次尝试获取锁(刷新节点列表，获得锁)
 * Created by cp on 2017/12/1.
 */
public class ZookeeperLock implements Lock {
    //InterProcessMutex 分布式锁

    public static final String LockRoot = "/lock/";
    private ZkClient zkclient;
    private String lockPath;

    private String currentPath;
    private String prePath;

    private CountDownLatch countDownLatch;

    public ZookeeperLock(String zkConfig, String name) {
        this(zkConfig, 5000, Integer.MAX_VALUE, name);
    }

    public ZookeeperLock(String zkConfig, int sessionTimeout, int connectionTimeout, String name) {
        zkclient = new ZkClient(zkConfig, sessionTimeout, connectionTimeout, new SerializableSerializer());
        lockPath = LockRoot + name;
        if (!zkclient.exists(lockPath)) {
            zkclient.createPersistent(lockPath, true);
        }
    }

    private void waitLock() {
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(Thread.currentThread() + "捕获到Data Delete事件");
                if (countDownLatch != null)
                    countDownLatch.countDown();
            }
        };
        zkclient.subscribeDataChanges(prePath, listener);
        System.out.println(Thread.currentThread() + "当前节点" + currentPath + "对" + prePath + "增加了监听事件");
        if (zkclient.exists(prePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkclient.unsubscribeDataChanges(prePath, listener);
    }

    public boolean tryLock() {
        if (StringUtils.isEmpty(currentPath)) {
            currentPath = zkclient.createEphemeralSequential(lockPath + "/", "lock");
            System.out.println(Thread.currentThread() + "创建了临时顺序节点：" + currentPath);
        }
        List<String> nodeList = zkclient.getChildren(lockPath);
        Collections.sort(nodeList);
        System.out.println(Thread.currentThread() + "获得列表" + nodeList);
        System.out.println(Thread.currentThread() + "比对值：" + currentPath + "==" + lockPath + "/" + nodeList.get(0));
        if (currentPath.equals(lockPath + "/" + nodeList.get(0))) {//是最小节点
            return true;
        } else {
            String myNodeName = currentPath.substring(currentPath.lastIndexOf('/') + 1);
            int index = Collections.binarySearch(nodeList, myNodeName);
            prePath = lockPath + "/" + nodeList.get(index - 1);
            return false;
        }
    }


    public void lock() {
        if (tryLock()) {
            System.out.println(Thread.currentThread() + "获得了分布式锁");
            return;
        }
        waitLock();
    }


    public void unlock() {
        System.out.println(Thread.currentThread() + "解锁");
        try {
            zkclient.delete(currentPath);
            zkclient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    ///------------------------ 下面接口先不实现-----------------------------
    public void lockInterruptibly() throws InterruptedException {

    }


    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    public Condition newCondition() {
        return null;
    }
}
