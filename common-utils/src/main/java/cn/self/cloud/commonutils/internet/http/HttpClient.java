package cn.self.cloud.commonutils.internet.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class HttpClient {


    public static Response Do(String method, String uri, Map<String , String> header, byte[] body){

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(90000)
                .setConnectTimeout(90000)
                .setConnectionRequestTimeout(90000)
                .setStaleConnectionCheckEnabled(true)
                .build();
//        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        CloseableHttpResponse  w = null;
        Response response = null;
        try {
            HttpUriRequest request = createRequest(method, uri,header, body);
            w = client.execute(request);
            response =  new Response(w);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
            	if (w != null) {
            		w.close();
            	}
            	if (client != null) {
            		client.close();
            	}
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return response;
    }

    private static HttpUriRequest createRequest(String method, String uri, Map<String, String> header, byte[] body) throws Exception {
        method = method.toUpperCase();

        if (method.equals(HttpGet.METHOD_NAME)) {
            return get(uri, header);
        }

        if (method.equals(HttpPost.METHOD_NAME)) {
            return post(uri, header, body);
        }

        if (method.equals(HttpPut.METHOD_NAME)) {
            return put(uri, header, body);
        }

        if (method.equals(HttpDelete.METHOD_NAME)) {
            return delete(uri, header);
        }

        throw new Exception("METHOD NOT SUPPORT");
    }


    private static HttpUriRequest get(String uri, Map<String, String> header) throws Exception {
        HttpGet request = new HttpGet();
        request.setConfig(createConfig());
        setUri(request, uri);
        setHeader(request, header);
        return request;
    }

    private static HttpUriRequest post(String uri, Map<String, String> header, byte[] body) throws Exception {
        HttpPost request = new HttpPost();
        request.setConfig(createConfig());
        setUri(request, uri);
        setHeader(request, header);
        setEntity(request, body);

        return request;
    }

    private static HttpUriRequest put(String uri, Map<String, String> header, byte[] body) throws Exception {
        HttpPut request = new HttpPut();
        request.setConfig(createConfig());
        setUri(request, uri);
        setHeader(request, header);
        setEntity(request, body);
        return request;
    }

    private static HttpUriRequest delete(String uri, Map<String, String> header) throws Exception {
        HttpDelete request = new HttpDelete();
        request.setConfig(createConfig());
        setUri(request, uri);
        setHeader(request, header);
        return request;
    }

    private static void setUri(HttpRequestBase request, String uri) throws Exception {
        if (!uri.contains("http")) {
            uri = "http://" + uri;
        }
        request.setURI(new URI(uri));
    }

    private static void setHeader(HttpRequestBase request, Map<String, String> header) {
        request.setHeader("Content-Type", "application/json;charset=utf-8");
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void setEntity(HttpEntityEnclosingRequestBase request, byte[] body) throws Exception {
        if(body != null){
            request.setEntity(new StringEntity(new String(body),"utf-8"));
        }
    }

    private static CloseableHttpResponse submit(HttpUriRequest request) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    client.close();
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
        }
        return response;
    }

    private static RequestConfig createConfig() {
        return RequestConfig.custom().setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000)
                .build();

    }
}
