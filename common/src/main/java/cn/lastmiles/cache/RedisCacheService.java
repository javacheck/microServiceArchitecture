package cn.lastmiles.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheService implements CacheService {
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void set(final String key, final Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public Object get(final String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void delete(final String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void set(final String key, final Object value, final Long liveSeconds) {
		redisTemplate.opsForValue().set(key, value, liveSeconds,
				TimeUnit.SECONDS);
	}

	@Override
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	@Override
	public Boolean expire(String key, Long liveSeconds) {
		return redisTemplate.expire(key, liveSeconds.longValue(),
				TimeUnit.SECONDS);
	}

	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	@Override
	public void setToHash(String key, Object hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	@Override
	public Object getFromHash(String key, Object hashKey) {
		return redisTemplate.opsForHash().get(key, hashKey);
	}

	@Override
	public Boolean hasHashKey(String key, Object hashKey) {
		return redisTemplate.opsForHash().hasKey(key, hashKey);
	}

	@Override
	public Long hashSize(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	public void deleteFromHash(String key, Object... hashKeys) {
		redisTemplate.opsForHash().delete(key, hashKeys);
	}

	public Long increment(String key, Long value) {
		return redisTemplate.boundValueOps(key).increment(value);
	}

	public Long increment(String key) {
		return redisTemplate.boundValueOps(key).increment(1L);
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void sendMessage(String channel, Serializable message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
