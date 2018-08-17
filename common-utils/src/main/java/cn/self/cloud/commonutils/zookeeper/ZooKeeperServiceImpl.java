package cn.self.cloud.commonutils.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cp on 2017/7/12.
 */
public class ZooKeeperServiceImpl implements ZooKeeperService {

    private ZooKeeper zooKeeper;
    private String zkConfig;
    private int timeout;
    private ConcurrentLinkedDeque<WatcherInfo> watcherInfoQueue = new ConcurrentLinkedDeque<>();
    private AtomicBoolean isWatchThreadRunning = new AtomicBoolean(false);
    private List<WatcherInfo> infoList = new ArrayList<>();

    public ZooKeeperServiceImpl(ZooKeeper zooKeeper, String zkConfig, int timeout) {
        this.zkConfig = zkConfig;
        this.zooKeeper = zooKeeper;
        this.timeout = timeout;
    }

    private static class WatcherInfo {
        public int type;//watcher类型 1 children watcher 2  node watcher
        public String path;
        public Watcher watcher;


        public WatcherInfo(int type, String path, Watcher watcher) {
            this.type = type;
            this.path = path;
            this.watcher = watcher;
        }
    }

    public List<String> getChildren(String path) {
        try {
            return zooKeeper.getChildren(path, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void watchChildren(String path, Watcher watcher) {
        WatcherInfo info = new WatcherInfo(1, path, watcher);
        if (isWatchThreadRunning.get()) {
            watcherInfoQueue.offer(info);
        } else {
            infoList.add(info);
        }
        refreshWatchers();
    }

    private void refreshWatchers() {
        if (isWatchThreadRunning.get())
            return;
        zooKeeper = ZookeeperManager.invalidAndGetZookeeper(zkConfig, timeout);
        new Thread(() -> {
            isWatchThreadRunning.set(true);
            watcherInfoQueue.clear();
            watcherInfoQueue.addAll(infoList);
            while (isWatchThreadRunning.get()) {
                WatcherInfo info = watcherInfoQueue.poll();
                while (info != null) {
                    if (info.type == 1) {
                        try {
                            final WatcherInfo temp = info;
                            zooKeeper.getChildren(info.path, (WatchedEvent event) -> {
                                if (event.getType() == Watcher.Event.EventType.None && event.getState() == Watcher.Event.KeeperState.Disconnected) {
                                    zooKeeper = ZookeeperManager.invalidAndGetZookeeper(zkConfig, timeout);
                                    isWatchThreadRunning.getAndSet(false);
                                } else {
                                    temp.watcher.process(event);
                                    watcherInfoQueue.offer(temp);
                                }
                            });
                        } catch (KeeperException e) {
//                            e.printStackTrace();
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                        }
                    } else if (info.type == 2) {
                        try {
                            final WatcherInfo temp = info;
                            zooKeeper.exists(info.path, (WatchedEvent event) -> {
                                if (event.getType() == Watcher.Event.EventType.None && event.getState() == Watcher.Event.KeeperState.Disconnected) {
                                    zooKeeper = ZookeeperManager.invalidAndGetZookeeper(zkConfig, timeout);
                                    isWatchThreadRunning.getAndSet(false);
                                } else {
                                    temp.watcher.process(event);
                                    watcherInfoQueue.offer(temp);
                                }
                            });
                        } catch (KeeperException e) {
//                            e.printStackTrace();
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                        }
                    }
                    info = watcherInfoQueue.poll();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            refreshWatchers();
        }, "Zookeeper事件监控线程").start();
    }

    public List<String> getAndWatchChildren(String path, Watcher watcher) {
        try {
            List<String> list = zooKeeper.getChildren(path, null);
            watchChildren(path, watcher);
            return list;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(String path, byte[] data, CreateMode createMode) {
        try {
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            System.out.println("创建节点：" + path + ",对象：" + zooKeeper);
            return true;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            System.out.println(" 创建节点失败：" + zooKeeper);
//            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String createAndWatch(String path, byte[] data, CreateMode createMode, Watcher watcher) {
        try {
            String ps = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            existAndWatch(path, watcher);
            return ps;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exist(String path) {
        try {
            Stat stat = zooKeeper.exists(path, null);
            if (stat != null)
                return true;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        return false;
    }

    public boolean existAndWatch(String path, Watcher watcher) {
        try {
            Stat stat = zooKeeper.exists(path, null);
            if (stat != null) {
                WatcherInfo info = new WatcherInfo(2, path, watcher);
                if (isWatchThreadRunning.get()) {
                    watcherInfoQueue.offer(info);
                } else {
                    infoList.add(info);
                }
                refreshWatchers();
                return true;
            }
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] getData(String path) {
        try {
            return zooKeeper.getData(path, null, null);
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getDataAndWatch(String path, Watcher watcher) {
        try {
            byte[] bs = zooKeeper.getData(path, null, null);
            WatcherInfo info = new WatcherInfo(2, path, watcher);
            if (isWatchThreadRunning.get()) {
                watcherInfoQueue.offer(info);
            } else {
                infoList.add(info);
            }
            refreshWatchers();
            return bs;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(String path) {
        try {
            zooKeeper.delete(path, -1);
            return true;
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setData(String path, byte[] bytes) {
        try {
            zooKeeper.setData(path, bytes, -1);
        } catch (KeeperException.SessionExpiredException ex) {
            refreshWatchers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException.NoNodeException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
