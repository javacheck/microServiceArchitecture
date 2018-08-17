package cn.self.cloud.commonutils.internet.http;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OPTIONS 方法比较少见，该方法用于请求服务器告知其支持哪些其他的功能和方法。通过 OPTIONS 方法，可以询问服务器具体支持哪些方法，或者服务器会使用什么样的方法来处理一些特殊资源。可以说这是一个探测性的方法，客户端通过该方法可以在不访问服务器上实际资源的情况下就知道处理该资源的最优方式。
 * 既然比较少见，什么情况下会使用这个方法呢？
 * 客户端发起的这个 OPTIONS 可以说是一个“预请求”，用于探测后续真正需要发起的跨域 POST 请求对于服务器来说是否是安全可接受的，因为跨域提交数据对于服务器来说可能存在很大的安全问题。
 * 请求头 Access-Control-Request-Method 用于提醒服务器在接下来的请求中将会使用什么样的方法来发起请求。
 * Access-Control-Allow-Method 和 Access-Control-Allow-Origin 分别告知客户端，服务器允许客户端用于跨域的方法和域名。
 * 设置响应状态码为 204 是为了告知客户端表示该响应成功了，但是该响应并没有返回任何响应体，
 * 如果状态码为 200，还得携带多余的响应体，在这种场景下是完全多余的，只会浪费流量。
 */
public class OptionsResponse extends HttpServlet {

    @Autowired
    protected void doOptions(HttpServletRequest request, HttpServletResponse response){
        String region = request.getHeader("Origin");
//      response.setHeader("Access-Control-Allow-Origin",region);
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "apptype,versioncode,iversion,timestamp,random,sign,Content-Type,sessionid,versionname");//,sessionid
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setStatus(204);
    }

}
