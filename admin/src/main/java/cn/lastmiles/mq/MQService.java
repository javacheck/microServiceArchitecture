package cn.lastmiles.mq;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.cache.CacheService;

@Service
public class MQService {
	@Autowired
	private CacheService cacheService;

	public void sendMessage(String channel,Serializable message){
		System.out.println("sendMessage:" + channel + "," + message);
		cacheService.sendMessage(channel, message);
	}
}
