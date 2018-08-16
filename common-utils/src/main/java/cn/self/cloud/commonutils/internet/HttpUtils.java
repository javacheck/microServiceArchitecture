package cn.self.cloud.commonutils.internet;

import cn.self.cloud.commonutils.validate.ValidateUtils;
import jodd.http.HttpRequest;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具
 */
public final class HttpUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String post(String url, Map<String, Object> parameters) {
        return HttpRequest.post(url).form(parameters).send().bodyText();
    }
    
    /**
     * Http get请求
     *
     * @param url              请求地址
     * @param header           头部信息
     * @param parameterDataMap 请求的参数
     */
    public static String apiGetRequest(String url, Map<String, String> header, Map<String, String> parameterDataMap) {
        if (!ValidateUtils.isEmpty(parameterDataMap)) {
            try {
                URIBuilder uriBuilder = new URIBuilder(url);
                if (!ValidateUtils.isEmpty(parameterDataMap)) {
                    parameterDataMap.entrySet().forEach(e -> {
                        uriBuilder.addParameter(e.getKey(), e.getValue());
                    });
                }
                url = uriBuilder.build().toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException("url错误");
            }
        }
        logger.info("请求的URL：{}", url);

        return httpExecute(new HttpGet(url), header);
    }

    /**
     * Http post请求
     *
     * @param url              请求地址
     * @param header           头部信息
     * @param parameterDataMap 请求的参数
     */
    public static String apiPostRequest(String url, Map<String, String> header, Map<String, String> parameterDataMap) {
        logger.info("请求的URL为：{}", url);

        HttpPost httpPost = new HttpPost(url);
        if (ValidateUtils.isNotEmpty(parameterDataMap)) {
            List<NameValuePair> formParams = new ArrayList<>();
            parameterDataMap.entrySet().forEach(e -> {
                formParams.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            });
            HttpEntity entity;
            try {
                entity = new UrlEncodedFormEntity(formParams);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("参数不支持编码");
            }
            httpPost.setEntity(entity);
            try {
                logger.info("请求的参数为：{}", EntityUtils.toString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return httpExecute(httpPost, header);
    }

    private static String httpExecute(HttpUriRequest request, Map<String, String> header) {
        logger.info("请求方式为：{}", request.getMethod());
        if (ValidateUtils.isNotEmpty(header)) {
            header.entrySet().forEach(e -> {
                request.setHeader(e.getKey(), e.getValue());
            });
        }

        for (Header header1 : request.getAllHeaders()) {
            logger.info("请求的header为：{}", header1);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            for (Header header1 : response.getAllHeaders()) {
                logger.info("返回的header为：{}", header1);
            }
            logger.info("返回的状态码为：{}", response.getStatusLine());
            String result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
            logger.info("返回的数据为：{}", result);
            response.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("访问出错");
        }
    }

    /**
     * 判断是否请求是否来源支付宝
     */
    public static boolean isAlipayRequest(String partner, String notifyId) {
        String url = "https://mapi.alipay.com/gateway.do";
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("service", "notify_verify");
            parameters.put("partner", partner);
            parameters.put("notify_id", notifyId);
            String bodyText = HttpRequest.post(url).form(parameters).send().bodyText();
            if (bodyText.equals("true")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
