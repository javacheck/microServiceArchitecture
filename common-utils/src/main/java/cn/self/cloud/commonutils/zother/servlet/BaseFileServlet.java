package cn.self.cloud.commonutils.zother.servlet;

import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.HttpServlet;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by cp on 2017/12/15.
 */
public class BaseFileServlet extends HttpServlet {

    Logger logger = Logger.getLogger(BaseFileServlet.class.getCanonicalName());

    @Override
    protected void preExecute(HttpRequest request, HttpResponse response) throws IOException {
        response.recycleListener((req, resp) -> {
            String bodyString = "";
            try {
                if (resp.getOutput() instanceof String)
                    bodyString = (String) resp.getOutput();
                else {
                    if (resp.getOutput() != null)
                        bodyString = resp.getOutput().toString();
                    else
                        bodyString = "";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            LogUtils.info(getClass(), "base file servlet", request.getMethod(), request.getRequestURI(), HttpRequestUtils.parseParameters(request),
                    "{file}", response.getStatus(), bodyString, (System.currentTimeMillis() - request.getCreatetime()));
        });
        response.nextEvent();
    }

    @Override
    protected void authenticate(HttpRequest request, HttpResponse response) throws IOException {
        logger.info("***********文件上传请求权限验证通过***********");
        response.nextEvent();
    }
}