package cn.lastmiles.service.sms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class SMSParameter {
	private Map<String, String> parameters;

	public static SMSParameter getInstance(String... nameValues) {
		Assert.notNull(nameValues);
		Assert.notEmpty(nameValues);
		Assert.isTrue(nameValues.length % 2 == 0,"参数必须name value对");

		SMSParameter instance = new SMSParameter();
		instance.parameters = new HashMap<String, String>();

		for (int i = 0; i < nameValues.length; i = i + 2) {
			instance.parameters.put(nameValues[i], nameValues[i + 1]);
		}

		return instance;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
