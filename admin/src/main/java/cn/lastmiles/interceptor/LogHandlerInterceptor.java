package cn.lastmiles.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Log;
import cn.lastmiles.service.LogService;
import cn.lastmiles.utils.SecurityUtils;

public class LogHandlerInterceptor implements HandlerInterceptor {
	private LogService logService;
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	private static final Logger logger = LoggerFactory.getLogger(LogHandlerInterceptor.class);
	
	public LogHandlerInterceptor(LogService logService,
			ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.logService = logService;
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Account account = SecurityUtils.getAccount();
		if (account != null) {
			String url = request.getRequestURI();
			Long accountId = account.getId();
			StringBuilder parameters = new StringBuilder("{");
			Enumeration<String> names = request.getParameterNames();
			int i = 0;
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String value = request.getParameter(name);
				if (i++ > 0) {
					parameters.append(",");	
				}
				parameters.append(name + ":" + value);
			}
			parameters.append("}");
			threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					Log log = new Log();
					log.setAccountId(accountId);
					log.setRequestUrl(url);
					log.setParameters(parameters.toString());
					logService.save(log);
				}
			});
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
