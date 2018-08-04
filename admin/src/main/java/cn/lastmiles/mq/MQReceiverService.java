package cn.lastmiles.mq;

import java.io.Serializable;

import cn.lastmiles.bean.User;

public class MQReceiverService {
	public void receiveMessage(Serializable message) {
		System.out.println(message);
	}
}
