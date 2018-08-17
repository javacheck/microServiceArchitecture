package cn.self.cloud.commonutils.internet.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Response {
    int statuscode;
    Map<String, String> headers;
    String body;

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Response(CloseableHttpResponse w) {
        Header[] headers = w.getAllHeaders();
        this.headers = new HashMap<String, String>();
        for (int i = 0; i < headers.length; i++) {
            this.headers.put(headers[i].getName(), headers[i].getValue());
        }
        this.statuscode = w.getStatusLine().getStatusCode();
        Header lenHeader = w.getFirstHeader("Content-Length");
        try {
            if (lenHeader != null) {
                int length = Integer.parseInt(lenHeader.getValue());
                if (length > 0) {
                    HttpEntity entity = w.getEntity();
                    if (entity == null) {
                        this.body = "";
                        return;
                    }
                    this.body = EntityUtils.toString(entity, "utf-8");
                }
            } else {
                HttpEntity entity = w.getEntity();
                if (entity == null) {
                    this.body = "";
                    return;
                }
                this.body = EntityUtils.toString(entity, "utf-8");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Response{" +
                "statuscode=" + statuscode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
