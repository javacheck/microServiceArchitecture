package cn.self.cloud.commonutils.cache;

import java.io.Serializable;
/**
 * 缓存的根接口
 */
public interface CacheService {
	/**
	 * 设置值到缓存中,且指定存活的时间
	 * @param key 标识key
	 * @param value 存储的值
	 * @param liveSeconds 存活的秒数
	 */
	void set(String key, Object value, Long liveSeconds);

	/**
	 * 设置值到缓存中
	 * @param key 标识key
	 * @param value 存储的值
	 */
	void set(String key, Object value);

    /**
     * 从缓存中获取已存储的值
     * @param key 标识key
     * @return 返回null或者设置的存储值
     */
	Object get(String key);

    /**
     * 将缓存中指定的值删除
     * @param key 标识key
     */
	void delete(String key);

    /**
     * 根据标识key获取存储值的存活时间
     * @param key 标识key
     * @return -1或者存储值的存活秒数
     */
	Long getExpire(String key);

    /**
     * 根据标识key设置存储值的存活时间
     * @param key 标识key
     * @param liveSeconds 存活秒数
     * @return 是否设置成功
     */
	Boolean expire(String key, Long liveSeconds);

	Boolean hasKey(String key);

	/**
	 * 保存到hash表
	 * @param key redis中key
	 * @param hashKey hash表中可以
	 * @param value
	 */
	void setToHash(String key, Object hashKey, Object value);

	Object getFromHash(String key, Object hashKey);

	Boolean hasHashKey(String key, Object hashKey);

	Long hashSize(String key);

	void deleteFromHash(String key, Object... hashKeys);

	Long increment(String key, Long value);

	Long increment(String key);
	
	void sendMessage(String channel, Serializable message);
}
