package cn.self.cloud.commonutils.zother.entity;

/**
 * Created by xiaomi on 2016/6/24 0024.
 */
public class RedisInfo {
    String id;
    int type;  //节点类型， 读(0)/写(1)
    String host;
    int port;
    String auth;  //认证

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "RedisInfo{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", auth='" + auth + '\'' +
                '}';
    }
}
