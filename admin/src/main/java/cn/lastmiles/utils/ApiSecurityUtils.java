package cn.lastmiles.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.lastmiles.bean.Account;
import cn.lastmiles.cache.CacheService;

@Component
public class ApiSecurityUtils {
	
	@Autowired
	private void setCacheService(CacheService cache) {
		cacheService = cache;
	}
	private static CacheService cacheService;
	
	public static Account getAccount(String token){
		return (Account) cacheService.get(token);
	}
	public static Long getStoreId(String token){
		return getAccount(token).getStoreId();
	}
}
