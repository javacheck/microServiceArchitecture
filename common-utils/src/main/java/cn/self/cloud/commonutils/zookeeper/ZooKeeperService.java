package cn.self.cloud.commonutils.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * 进行zookeeper操作的接口
 * Created by cp on 2017/7/12.
 */
public interface ZooKeeperService {

    List<String> getChildren(String path);

    void watchChildren(String path, Watcher watcher);

    List<String> getAndWatchChildren(String path, Watcher watcher);

    boolean create(String path, byte[] data, CreateMode createMode);

    String createAndWatch(String path, byte[] data, CreateMode createMode, Watcher watcher);

    boolean exist(String path);


    boolean existAndWatch(String path, Watcher watcher);

    byte[] getData(String path);

    byte[] getDataAndWatch(String path, Watcher watcher);
    boolean  delete(String path);

    void setData(String path, byte[] bytes);
}
