package cn.self.cloud.commonutils.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPoolEep {
    private static Logger log = LoggerFactory.getLogger(ConnectionPoolEep.class);

    private ConnectionPoolEep() {
        jdbcDriverClassName = ConfigHelper.getInstance().get("jdbcDriverClassName");
        jdbcUrl = ConfigHelper.getInstance().get("jdbcUrl_eep");
        jdbcUsername = ConfigHelper.getInstance().get("jdbcUsername");
        jdbcPassword = ConfigHelper.getInstance().get("jdbcPassword");
    }

    private static ConnectionPoolEep pool = null;

    public synchronized static ConnectionPoolEep getInstance() {
        if (pool == null)
            pool = new ConnectionPoolEep();
        return pool;
    }

    private int poolMaxSize = 20;

    // #数据库配置参数
    private String jdbcDriverClassName = "com.mysql.jdbc.Driver";
    private String jdbcUrl = "jdbc:mysql://192.168.1.123/db_eep?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";
    private String jdbcUsername = "root";
    private String jdbcPassword = "chehai";

    private List<MyConnection> connectionList = new ArrayList<MyConnection>();

    public Connection getConnection() {
        synchronized (connectionList) {
            MyConnection conn = getFreeConnection();
            if (conn != null) {
                conn.setInUse(true);
                try {
                    if (conn.getConnection() == null || conn.getConnection().isClosed()) {
                        Connection cn = getNewConnection();
                        if (cn != null)
                            conn.setConnection(cn);
                        else {
                            connectionList.remove(conn);
                            return null;
                        }
                    }
                    else {
                        Connection con=conn.getConnection();
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;
                        try{
                            preparedStatement=con.prepareStatement("select 1");
                            resultSet=preparedStatement.executeQuery();
                        }catch (SQLException ee){
                            ee.printStackTrace();
                            log.info("数据库连接断开，进行重连......");
                            freeConnection(conn.getConnection());
                            con.close();
                            connectionList.remove(conn);
                            if(connectionList.size()<poolMaxSize){
                                Connection cnn=getNewConnection();
                                if(cnn!=null){
                                    conn= new MyConnection();
                                    conn.setConnection(cnn);
                                    conn.setInUse(true);
                                    connectionList.add(conn);
                                }
                                else{
                                    return null;
                                }
                            }
                            else{
                                return null;
                            }

                        }finally {
                            if(resultSet!=null){
                                try{
                                    resultSet.close();
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }
                            }
                            if(preparedStatement!=null){
                                try{
                                    preparedStatement.close();
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
                return conn.getConnection();
            }
            if (connectionList.size() < poolMaxSize) {
                Connection cn = getNewConnection();
                if (cn != null) {
                    conn = new MyConnection();
                    conn.setConnection(cn);
                    conn.setInUse(true);
                    connectionList.add(conn);
                    return conn.getConnection();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private MyConnection getFreeConnection() {
        for (MyConnection conn : connectionList) {
            if (!conn.isInUse()) {
                //log.info("获取到空闲连接" + conn);
                return conn;
            }
        }
        return null;
    }

    private Connection getNewConnection() {
        Connection conn = null;
        try {
            Class.forName(jdbcDriverClassName);
            conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
//            log.info("获取到新的数据库连接" + conn);
        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
//        log.info("获取数据库连接" + conn);
        return conn;
    }

    public void freeConnection(Connection connection) {
        for (MyConnection conn : connectionList) {
            if (conn.isInUse() && conn.getConnection() == connection) {
//                log.info("释放数据库连接：" + conn.getConnection()+"\n释放完成后连接池："+connectionList);
                conn.setInUse(false);
            }
        }
    }
}
