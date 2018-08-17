package cn.self.cloud.commonutils.zother.service.impl;

import cn.self.cloud.commonutils.zookeeper.TreeNode;
import cn.self.cloud.commonutils.zother.entity.RedisInfo;
import com.terjoy.comm.ZooKeeperService;
import com.terjoy.comm.ZookeeperManager;
import org.apache.zookeeper.WatchedEvent;
import org.redkale.convert.json.JsonConvert;
import org.redkale.service.AbstractService;
import org.redkale.service.Local;
import org.redkale.util.AnyValue;
import org.redkale.util.TypeToken;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cp on 2017/7/8.
 */
@Local
public class RedisServiceImpl extends AbstractService {
    @Resource(name = "property.config.mode")
    String configMode;

    @Resource(name = "property.zookeeper.config")
    String zkConfig;

    Map<String, List<RedisInfo>> redisMap = new HashMap<>();

    ZooKeeperService zooKeeperService = null;

    boolean isWatchingThreadRunning = true;

    static Map<String, Map<String, List<JedisPool>>> poolsMap = new HashMap();


    public void init(AnyValue config) {
        if (zooKeeperService == null)
            zooKeeperService = ZookeeperManager.getZookeeperService(zkConfig);
        new Thread(() -> {
            while (isWatchingThreadRunning) {
                CountDownLatch task = new CountDownLatch(1);
                try {
                    refreshRedisMap();
                    List<String> nodePathList = zooKeeperService.getAndWatchChildren(TreeNode.redis,
                            (WatchedEvent event) -> {
                                task.countDown();
                            }
                    );
                    for (String path : nodePathList) {
                        zooKeeperService.getDataAndWatch(TreeNode.redis + "/" + path,
                                (WatchedEvent event) -> {
                                    task.countDown();
                                }
                        );
                    }
                    task.await();
                } catch (Exception e) {

                }
            }
        }, "Redis配置信息监控线程").start();
    }


    public void destroy(AnyValue config) {
        isWatchingThreadRunning = false;
    }

    private void refreshRedisMap() {

        Map<String, List<RedisInfo>> newMap = new HashMap();
        List<String> nodeList = zooKeeperService.getChildren(TreeNode.redis);
        if (nodeList == null || nodeList.isEmpty()) {
            synchronized (this) {
                redisMap.clear();
            }
            return;
        }
        for (String node : nodeList) {
            List<RedisInfo> redisList = JsonConvert.root().convertFrom(new TypeToken<List<RedisInfo>>() {
            }.getType(), new String(zooKeeperService.getData(TreeNode.redis + "/" + node)));
            newMap.put(node, redisList);
        }
        synchronized (this) {
            redisMap = newMap;
        }
    }

    public JedisPool getWriteJedis(String name) throws Exception {
        if (!redisMap.containsKey(name))
            throw new RuntimeException("没有发现名为" + name + "的redis服务配置");
        Map<String, List<JedisPool>> readWritePoolMap = checkAndInitPool(name);
        return get("1", readWritePoolMap);
    }

    private synchronized Map<String, List<JedisPool>> checkAndInitPool(String name) {
        Map<String, List<JedisPool>> readWritePoolMap = poolsMap.get(name);
        if (readWritePoolMap == null) { //为空时进行初始化
            readWritePoolMap = initPoolsMap(name);
            poolsMap.put(name, readWritePoolMap);
        } else { // 不为空时，进行连接的检验 一般情况下，连接池不会自动损坏，所以不用每次都进行连接池的检查
            /*
            clearPoolMap(readWritePoolMap,"0");
            clearPoolMap(readWritePoolMap,"1");
            if (readWritePoolMap.isEmpty()) {
                poolsMap.remove(name);
                readWritePoolMap = initPoolsMap(name);
                poolsMap.put(name, readWritePoolMap);
            }*/
        }
//        System.out.println("完成连接池的检查，redis参数：" + redisMap + ",连接池:" + readWritePoolMap);
        return readWritePoolMap;
    }
    private void clearPoolMap(Map<String, List<JedisPool>> readWritePoolMap,String key){
        List<JedisPool> pools = readWritePoolMap.get(key);
        List<JedisPool> remove = new ArrayList<>();
        for (JedisPool pool : pools) {
            if (pool.isClosed()) {
                remove.add(pool);
            }
        }
        pools.removeAll(remove);
        if (pools.isEmpty()) {
            readWritePoolMap.remove(key);
        }
    }

    private Map<String, List<JedisPool>> initPoolsMap(String name) {
        Map<String, List<JedisPool>> readWritePoolMap = new HashMap<>();
        List<RedisInfo> rediss = redisMap.get(name);
        for (RedisInfo ri : rediss) {
            String key = String.valueOf(ri.getType());
            List<JedisPool> pools = readWritePoolMap.get(key);
            if (pools == null) {
                pools = new ArrayList();
                readWritePoolMap.put(key, pools);
                if (ri.getType() == 1) {
                    readWritePoolMap.put("0", pools);
                }
            }
            pools.add(dial(ri.getHost(), ri.getPort(), ri.getAuth()));
        }
        System.out.println("完成连接池的初始化，redis参数：" + redisMap + ",连接池:" + readWritePoolMap);
        return readWritePoolMap;
    }

    private JedisPool dial(String host, int port, String auth) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        JedisPool pool = new JedisPool(config, host, port, 2000, auth);
        return pool;
    }


    public JedisPool getReadJedis(String name) throws Exception {
        if (!redisMap.containsKey(name))
            throw new RuntimeException("没有发现名为" + name + "的redis服务配置");
        Map<String, List<JedisPool>> readWritePoolMap = checkAndInitPool(name);
        return get("0", readWritePoolMap);
    }

    private JedisPool get(String key, Map<String, List<JedisPool>> readWritePoolMap) throws Exception {
        synchronized (this) {
            List<JedisPool> pools = readWritePoolMap.get(key);
            if (pools == null) {
                throw new Exception("Jedis Pool 为空.");
            }
            return pools.get(Id.random(pools.size()));
        }
    }
}
