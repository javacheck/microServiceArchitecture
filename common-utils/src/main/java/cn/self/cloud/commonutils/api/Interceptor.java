package cn.self.cloud.commonutils.api;

import cn.self.cloud.commonutils.internet.IpUtils;
import cn.self.cloud.commonutils.json.JsonFormatUtil;
import cn.self.cloud.commonutils.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截器
 */
public class Interceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("----------------------------------------------------------------Request Start----------------------------------------------------------------");
        logger.info("请求地址:{}", (request).getRequestURI());
        logger.info("请求IP地址:{}", IpUtils.getIpAddress(request));
        logger.info("客户端信息:{}", request.getHeader("user-agent"));
        logger.info("请求的参数如下:");

        Map<String, String[]> requestData = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : requestData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            //日志中，不能出现明文的密码
            if (key.contains("password") || key.contains("token")) {
                logger.info("{}-->******", key);
            } else {
                logger.info("{}-->{}", key, value);
            }
        }
        logger.info("****************************************************************{}执行SQL****************************************************************", (request).getRequestURI());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        logger.info("<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{}请求成功,返回参数~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", (request).getRequestURI());
        logger.info("\n" + JsonFormatUtil.formatJson(JsonUtils.objectToJson(RequestUtil.getResult())));
        logger.info("----------------------------------------------------------------Request End----------------------------------------------------------------");
    }
}
