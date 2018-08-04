package cn.lastmiles.common.service;

import cn.lastmiles.cache.CacheService;

public class RedisIdService implements IdService {
	private final static String IDKEY = "id:key:";
	private final static String ORDERIDKEY = "orderId:key:";

	private Long initId = 1000000L;
	private Long initOrderId = 10000000L;

	private CacheService cacheService;

	public RedisIdService(CacheService cacheService) {
		super();
		this.cacheService = cacheService;
		if (!cacheService.hasKey(IDKEY)) {
			cacheService.increment(IDKEY, initId);
		}
		if (!cacheService.hasKey(ORDERIDKEY)) {
			cacheService.increment(ORDERIDKEY, initOrderId);
		}
	}

	@Override
	public Long getId() {
		return cacheService.increment(IDKEY);
	}

	@Override
	public Long getOrderId() {
		return cacheService.increment(ORDERIDKEY);
	}

}
