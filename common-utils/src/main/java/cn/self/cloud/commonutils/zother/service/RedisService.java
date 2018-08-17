package cn.self.cloud.commonutils.zother.service;

import cn.self.cloud.commonutils.zother.service.impl.RedisServiceImpl;
import org.redkale.service.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * Created by cp on 2017/7/8.
 */
public class RedisService implements Service {
    @Resource
    RedisServiceImpl impl;

    /**
     * 获取写的jedis客户端
     *
     * @param name
     * @return
     */
    public JedisPool getWriteJedis(String name) throws Exception {
        return impl.getWriteJedis(name);
    }

    /**
     * 获取只读的jedis客户端
     *
     * @param name
     * @return
     */
    public JedisPool getReadJedis(String name) throws Exception {
        return impl.getReadJedis(name);
    }
}
