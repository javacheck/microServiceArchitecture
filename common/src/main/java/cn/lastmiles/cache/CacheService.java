package cn.lastmiles.cache;

import java.io.Serializable;

public interface CacheService {
	/**
	 * 保存到缓存
	 * @param key
	 * @param value
	 * @param liveSeconds 秒数
	 */
	public void set(String key, Object value, Long liveSeconds);

	public void set(String key, Object value);

	public Object get(String key);

	public void delete(String key);

	public Long getExpire(String key);

	public Boolean expire(String key, Long liveSeconds);

	public Boolean hasKey(String key);

	/**
	 * 保存到hash表
	 * 
	 * @param key
	 *            redis中key
	 * @param hashKey
	 *            hash表中可以
	 * @param value
	 */
	public void setToHash(String key, Object hashKey, Object value);

	public Object getFromHash(String key, Object hashKey);

	public Boolean hasHashKey(String key, Object hashKey);

	public Long hashSize(String key);

	public void deleteFromHash(String key, Object... hashKeys);

	public Long increment(String key, Long value);

	public Long increment(String key);
	
	public void sendMessage(String channel, Serializable message);
}
