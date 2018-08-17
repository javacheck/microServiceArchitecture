package cn.self.cloud.commonutils.zother;

import cn.self.cloud.commonutils.api.JsonResult;
import cn.self.cloud.commonutils.internet.http.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/***
 * 资源服务器的公共方法
 */
public class ResServiceUtils {

    /****
     * 上传文件到资源服务器
     * @param uri 资源服务器上传url 如：http://localhost:6085/res/files/upload
     * @param file 要上传的文件
     * @return 如果成功返回文件的下载url，如果失败，返回null
     */
    public static String uploadFile(String uri, File file) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(90000)
                .setConnectTimeout(90000)
                .setConnectionRequestTimeout(90000)
                .setStaleConnectionCheckEnabled(true)
                .build();
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        CloseableHttpResponse w = null;
        Response response = null;
        try {
            HttpPost request = new HttpPost();

            RequestConfig config = RequestConfig.custom().setConnectTimeout(30000)
                    .setConnectionRequestTimeout(30000)
                    .setSocketTimeout(30000)
                    .build();
            request.setConfig(config);
            request.setURI(new URI(uri));

            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("file", new FileBody(file));
            request.setEntity(multipartEntity);
            w = client.execute(request);
            response = new Response(w);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        if (response.getStatuscode() != 200)
            return null;
        String json = response.getBody();
        JsonResult<String> jsonResult = new Gson().fromJson(json, new TypeToken<JsonResult<String>>() {
        }.getType());
        if (jsonResult.getCode() == 1) {
            return jsonResult.getData();
        } else {
            return null;
        }
    }
}
