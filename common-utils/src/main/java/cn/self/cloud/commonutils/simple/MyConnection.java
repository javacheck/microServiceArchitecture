package cn.self.cloud.commonutils.simple;

import java.sql.Connection;

public class MyConnection {
    private boolean inUse;
    private Connection connection;

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "MyConnection{" +
                "inUse=" + inUse +
                ", connection=" + connection +
                '}';
    }
}
