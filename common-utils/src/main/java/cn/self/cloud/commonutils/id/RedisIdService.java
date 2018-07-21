package cn.self.cloud.commonutils.id;

import cn.self.cloud.commonutils.cache.CacheService;

public class RedisIdService implements IdService {
    private final static String IDKEY = "模块名:id:key";
    private final static String ORDERIDKEY = "模块名:orderId:key";

    private Long initId = 4000000L;
    private Long initOrderId = 40000000L;

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
