package cn.self.cloud.commonutils.sms;//package cn.base.code.common.sms.imp;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import jodd.http.HttpRequest;
//import jodd.http.HttpResponse;
//import jodd.util.StringTemplateParser;
//import jodd.util.StringTemplateParser.MacroResolver;
//import jodd.util.StringUtil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.stereotype.Service;
//
//import cn.base.code.base.sms.SMSService;
//
//@Service
//public class SMSServiceImpl implements SMSService {
//	private final static Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);
//	@Autowired
//	private TaskExecutor taskExecutor;
//
//	@Override
//	public void send(String template, Map<String, String> parameter,String... mobiles) {
//		taskExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				StringTemplateParser stp = new StringTemplateParser();
//				String content = stp.parse(template, new MacroResolver() {
//					public String resolve(String macroName) {
//						return parameter.get(macroName);
//					}
//				});
//				logger.info("sms content= {}", content);
//				String url = "";//ConfigUtils.getProperty("sms.url");
//				String account = "";//ConfigUtils.getProperty("sms.account");
//				String password = "";//ConfigUtils.getProperty("sms.password");
//				String id = "";//ConfigUtils.getProperty("sms.id");
//
//				String sends = StringUtil.join(mobiles, ",");
//				Map<String, Object> forms = new HashMap<String, Object>();
//				forms.put("action", "send");
//				forms.put("userid", id);
//				forms.put("account", account);
//				forms.put("password", password);
//				forms.put("mobile", sends);
//				forms.put("content", content);
//				forms.put("sendTime", "");
//				forms.put("extno", "");
//
//				HttpResponse httpResponse = HttpRequest.post(url)
//						.form(forms).send();
//
//				logger.info(httpResponse.bodyText());
//			}
//		});
//	}
//}