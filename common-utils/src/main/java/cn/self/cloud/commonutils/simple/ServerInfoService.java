package cn.self.cloud.commonutils.simple;

import cn.self.cloud.commonutils.jedis.RedisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取服务器信息的服务
 * Created by ChenPing on 15-1-30.
 */
public class ServerInfoService {
    private static Logger logger = LoggerFactory.getLogger(ServerInfoService.class);
    private static ServerInfoService serverInfoService = null;
    private Map<String,RedisServer>  serverMap=new HashMap<String, RedisServer>();
    public static final long ServerListReloadInterval = 600000;

    private ServerInfoService() {

    }

    public static ServerInfoService getInstance() {
        if (serverInfoService == null) {
            serverInfoService = new ServerInfoService();
        }
        return serverInfoService;
    }
    public static final String sqlSelectServerList = "select ip,redisport,id,redispass from tb_server_info where status=1 and isinterfaceserver=1 and addcache=1";
    public static long lastReloadTime = 0;

    private Object lock=new Object();

    private void reloadServerData(){
        Connection connection = ConnectionPoolEep.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sqlSelectServerList);
            resultSet = preparedStatement.executeQuery();
            serverMap.clear();
            while (resultSet.next()) {
                RedisServer redisServer = new RedisServer();
                redisServer.setIp(resultSet.getString(1));
                redisServer.setPort(resultSet.getInt(2));
                redisServer.setId(resultSet.getInt(3));
                redisServer.setPassword(resultSet.getString(4));
                serverMap.put(redisServer.getIp(),redisServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(LogUtils.getErrorInfo(e));
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null) {
                ConnectionPoolEep.getInstance().freeConnection(connection);
            }
        }
    }

    public Collection<RedisServer> getServerList() {

        synchronized (lock) {
            long now = System.currentTimeMillis();
            if (lastReloadTime == 0 || now - lastReloadTime > ServerListReloadInterval) {
                reloadServerData();
                lastReloadTime = now;
            }
        }
        return serverMap.values();

    }

    public RedisServer getRedisServer(String serverip) {
        synchronized (lock){
            return serverMap.get(serverip);
        }
    }
}
