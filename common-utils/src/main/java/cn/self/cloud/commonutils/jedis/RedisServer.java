package cn.self.cloud.commonutils.jedis;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created by yumei on 15-1-13.
 */
public class RedisServer {
    private int id;
    private String ip;
    private int port;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
