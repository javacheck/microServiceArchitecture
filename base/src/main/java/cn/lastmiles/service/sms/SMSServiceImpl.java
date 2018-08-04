package cn.lastmiles.service.sms;

import java.util.HashMap;
import java.util.Map;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.util.StringTemplateParser;
import jodd.util.StringTemplateParser.MacroResolver;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.SmsRecord;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.SmsRecordService;

@Service
public class SMSServiceImpl implements SMSService {
	private final static Logger logger = LoggerFactory
			.getLogger(SMSServiceImpl.class);
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private SmsRecordService smsRecordService;
	
	private void send(int port,String template, SMSParameter parameter, String... mobiles){
		StringBuilder content = new StringBuilder();
		if (parameter != null) {
			StringTemplateParser stp = new StringTemplateParser();
			content.append(stp.parse(template, new MacroResolver() {
				public String resolve(String macroName) {
					return parameter.getParameters().get(macroName);
				}
			}));
		}else {
			content.append(template);
		}
		
		if (ConfigUtils.getProperty("redis.ip") != null
				&& !ConfigUtils.getProperty("redis.ip").startsWith("192")) {
			logger.debug("send sms ................{} ",
					StringUtil.join(mobiles, ","));
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					String url = "http://120.25.202.19:8888/sms.aspx";
					String account = "laimai";
					String password = "123123";
					String id = "147";
					
					if (port == 2){
						account = "laimai1";
						id = "802";
					}

					String sends = StringUtil.join(mobiles, ",");
					logger.debug("sms content= {},mobile = {}", content, sends);
					Map<String, Object> forms = new HashMap<String, Object>();
					forms.put("action", "send");
					forms.put("userid", id);
					forms.put("account", account);
					forms.put("password", password);
					forms.put("mobile", sends);
					forms.put("content", content.toString());
					forms.put("sendTime", "");
					forms.put("extno", "");

					HttpResponse httpResponse = HttpRequest.post(url)
							.form(forms).send();

					logger.info(httpResponse.bodyText());
				}
			});
		}
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String token = StringUtils.uuid();
				for (String mobile : mobiles){
					SmsRecord record = new SmsRecord();
					record.setContent(content.toString());
					record.setMobiles(mobile);
					record.setToken(token);
					logger.debug("sms record = {}", record);
					smsRecordService.save(record);
				}
			}
		});
	}

	@Override
	public void send(String template, SMSParameter parameter, String... mobiles) {
		send(1,template, parameter, mobiles);
	}

	@Override
	public void send2(String template, SMSParameter parameter,
			String... mobiles) {
		send(2,template, parameter, mobiles);
	}
}
