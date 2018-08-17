package cn.self.cloud.commonutils.zother.entity;

/**
 * Created by xiaomi on 2016/6/23 0023.
 */
public class Mysql {
    String id;
    String dbname;
    int type;  //节点类型， 读(0)/写(1)
    String host;
    int port;
    String username;
    String password;
    ConnectionPoolInfo connectionPoolInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }


    public ConnectionPoolInfo getConnectionPoolInfo() {
        return connectionPoolInfo;
    }

    public void setConnectionPoolInfo(ConnectionPoolInfo connectionPoolInfo) {
        this.connectionPoolInfo = connectionPoolInfo;
    }
}


