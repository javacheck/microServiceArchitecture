package cn.self.cloud.commonutils.sms;

import java.util.Map;

public interface SMSService {
	public void send(String template, Map<String, String> parameter, String... mobiles);
}
