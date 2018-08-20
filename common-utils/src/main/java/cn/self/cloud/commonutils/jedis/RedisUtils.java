package cn.self.cloud.commonutils.jedis;

import cn.self.cloud.commonutils.jedis.util.Jedis;
import cn.self.cloud.commonutils.jedis.util.JedisPool;
import cn.self.cloud.commonutils.jedis.util.Pipeline;
import com.niuyun.beans.RedisOpration;

import com.niuyun.comm.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Chen Ping on 14-9-22.
 */
public class RedisUtils {
    private static Jedis jedis = null;
    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    private static ServerInfoService serverInfoService = ServerInfoService.getInstance();

    private static void checkAndGetRedis() {
        if (jedis == null || !jedis.isConnected()) {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
            ConfigHelper config = ConfigHelper.getInstance();
            jedis = JedisUtils.getInstance().getJedis(config.get("redisServer"), Integer.parseInt(config.get("redisPort")), config.get("redisPass"));
        }
    }

    public static void newCloseJedis() {
        if (jedis != null) {
            ConfigHelper config = ConfigHelper.getInstance();
            JedisPool jedisPool = JedisUtils.getInstance().getPool(config.get("redisServer"), Integer.parseInt(config.get("redisPort")), config.get("redisPass"));
//        JedisPool jedisPool = new JedisPool(config.get("redisServer"), Integer.parseInt(config.get("redisPort")));
            jedisPool.returnBrokenResource(jedis);
            jedis = null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String key = "sssss";
        String value = "bbbb";
        Jedis jedis = new Jedis("182.92.243.152", 6379);

        long hash = -2130764741L;
        logger.info(jedis.get("hash." + Long.toHexString(hash)));
    }

    public static void clearPreffix(Jedis jedis, String preffix) {
        Set<String> keys = jedis.keys(preffix + "*");
        for (String key : keys) {
            jedis.del(key);
        }
        logger.info("清除" + preffix + "值" + keys.size() + "个");
    }

    private static void clear(Jedis jedis) {
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            jedis.del(key);
        }
        logger.info("清除键值对" + keys.size() + "个");
    }

    private static void clearAlliid(Jedis jedis) {
        jedis.del("kch.iid.all");
    }

    public static void clearHash(Jedis jedis) {
        Set<String> keys = jedis.keys("hash*");
        for (String key : keys) {
            jedis.del(key);
        }
        logger.info("清除hash值" + keys.size() + "个");
    }

    public static void setStringValueWithKey(String key, String value, int seconds) {
        checkAndGetRedis();
        jedis.setex(key, seconds, value);
    }

    /**
     * 检验指定的key是否存在，如果存在，则增加其超时时间，如果不存在，则设置其值
     *
     * @param key
     * @param value
     * @param seconds 超时秒数
     * @return 如果存在则返回其值, 不存在则返回空字符串
     */
    public static String checkAndSetStringValueWithKey(String key, String value, int seconds) throws Exception {
        checkAndGetRedis();
   /*     Transaction transaction = jedis.multi();
        String ret = transaction.get(key).get();
        String old;
        if (StringUtils.isNotEmpty(ret)) {
            old = transaction.get(key).get();
            transaction.set(key, value);
            transaction.expire(key, seconds);
        } else {
            transaction.set(key, value);
            transaction.expire(key, seconds);
            old = "";
        }
        transaction.exec();
        */
        String ret = jedis.get(key);
        if (StringUtils.isEmpty(ret)) {
            jedis.set(key, value);
        }
        jedis.expire(key, seconds);
        return ret;
    }


    public static String getString(String key) {
        checkAndGetRedis();
        String ret = jedis.get(key);
        return ret;
    }

    public static Set<String> getSetMembers(String key) {
        checkAndGetRedis();
        return jedis.smembers(key);
    }

    public static void deleteByKey(String key) {
        checkAndGetRedis();
        jedis.del(key);
    }

    public static void deleteFromZSetByMember(String key, String member) {
        checkAndGetRedis();
        jedis.zrem(key, member);
    }

    public static void setValue(String key, String value, int seconds) {
        checkAndGetRedis();
        jedis.set(key, value);
        jedis.expire(key, seconds);
    }

    public static void addZSetMember(String key, long score, String member) {
        checkAndGetRedis();
        jedis.zadd(key, score, member);
    }

    public static void addSetMember(String key, String member) {
        checkAndGetRedis();
        jedis.sadd(key, member);
    }

    public static void closeJedis() {
        if (jedis != null) {
            jedis.close();
            jedis = null;
        }
    }

    public static void lPushIntoQueue(String key, String member) {
        checkAndGetRedis();
        jedis.lpush(key, member);
    }

    public static void noticeToPush(String key) {
        checkAndGetRedis();
        jedis.publish(key, "完成了一批数据的处理");
    }

    public static void sateToRedis(List<RedisOpration> redisOprations) {
        if (redisOprations == null || redisOprations.isEmpty())
            return;
        Collection<RedisServer> serverList = serverInfoService.getServerList();
        List<Pipeline> pipelist = new ArrayList<Pipeline>();
        List<Jedis> jedisList = new ArrayList<Jedis>();
        List<JedisPool> pool = new ArrayList<JedisPool>();
        try {
            if (serverList != null && !serverList.isEmpty()) {
                for (RedisServer server : serverList) {
                    JedisPool jedisPool = JedisUtils.getPool(server.getIp(), server.getPort(), server.getPassword());
                    Jedis jedis = null;
                    try {
                        jedis = jedisPool.getResource();
                        pool.add(jedisPool);
                        jedisList.add(jedis);
                        Pipeline pipeline = jedis.pipelined();
                        pipelist.add(pipeline);
                    } catch (Exception e) {
                        logger.error(LogUtils.getErrorInfo(e));
                        if (jedis != null) {
                            jedisPool.returnBrokenResource(jedis);
                        }
                    }
                }
            }

            for (Pipeline pipeline : pipelist) {
                for (RedisOpration op : redisOprations) {
                    // 1 set 新增操作  2  删除key  3 push操作
                    switch (op.getType()) {
                        case 1://
                            pipeline.set(op.getKey(), op.getValue());
                            pipeline.expire(op.getKey(), op.getTime());
                            break;
                        case 2:
                            pipeline.del(op.getKey());
                            break;
                        case 3:
                            pipeline.lpush(op.getKey(), op.getValue());
                            break;
                        default:
                            break;
                    }
                }
                pipeline.sync();
                logger.info("对一批数据进行了" + redisOprations.size() + "次redis缓存操作！");
            }

        } finally {
            for (int i = 0; i < pool.size(); i++) {
                if (jedisList.get(i) != null) {
                    pool.get(i).returnBrokenResource(jedisList.get(i));
                }
            }
        }
    }

    /*
    public static void doPipeline(List<CenterRedisOp> centerRedisOpList) {
        if(centerRedisOpList==null || centerRedisOpList.isEmpty())
            return ;
        checkAndGetRedis();
        Pipeline pipeline=jedis.pipelined();
        for(CenterRedisOp op:centerRedisOpList){
            // 1 lPushIntoQueue 操作  2 zset新增操作  3 set 新增操作  4 zset根据key和member(value)进行删除操作 5 删除key
            switch (op.getType()){
                case 1://
                    pipeline.lpush(op.getKey(), op.getValue());
                    break;
                case 2:
                    pipeline.zadd(op.getKey(), op.getScore(), op.getValue());
                    break;
                case 3:
                    pipeline.sadd(op.getKey(),op.getValue());
                    break;
                case 4:
                    pipeline.zrem(op.getKey(), op.getValue());
                    break;
                case 5:
                    pipeline.del(op.getKey());
                    break;
                default:
                    break;
            }
        }
        pipeline.sync();
    }
    */
}
