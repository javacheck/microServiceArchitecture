package cn.self.cloud.commonutils.jedis.util;

import cn.self.cloud.commonutils.jedis.exceptions.*;
import cn.self.cloud.commonutils.simple.util.JedisClusterCRC16;

public abstract class JedisClusterCommand<T> {

    private JedisClusterConnectionHandler connectionHandler;
    private int commandTimeout;
    private int redirections;

    public JedisClusterCommand(JedisClusterConnectionHandler connectionHandler,
	    int timeout, int maxRedirections) {
	this.connectionHandler = connectionHandler;
	this.commandTimeout = timeout;
	this.redirections = maxRedirections;
    }

    public abstract T execute(Jedis connection);

    public T run(String key) {
	if (key == null) {
	    throw new JedisClusterException(
		    "No way to dispatch this command to Redis Cluster.");
	}

	return runWithRetries(key, this.redirections, false, false);
    }

    private T runWithRetries(String key, int redirections,
	    boolean tryRandomNode, boolean asking) {
	if (redirections <= 0) {
	    throw new JedisClusterMaxRedirectionsException(
		    "Too many Cluster redirections?");
	}

	Jedis connection = null;
	try {
	    if (tryRandomNode) {
		connection = connectionHandler.getConnection();
	    } else {
		connection = connectionHandler
			.getConnectionFromSlot(JedisClusterCRC16.getSlot(key));
	    }

	    if (asking) {
		// TODO: Pipeline asking with the original command to make it
		// faster....
		connection.asking();

		// if asking success, reset asking flag
		asking = false;
	    }

	    return execute(connection);
	} catch (JedisConnectionException jce) {
	    if (tryRandomNode) {
		// maybe all connection is down
		throw jce;
	    }
	    
	    releaseConnection(connection, true);
	    connection = null;
	    
	    // retry with random connection
	    return runWithRetries(key, redirections--, true, asking);
	} catch (JedisRedirectionException jre) {
	    if (jre instanceof JedisAskDataException) {
		asking = true;
	    } else if (jre instanceof JedisMovedDataException) {
		// TODO : In antirez's redis-rb-cluster implementation, 
		// it rebuilds cluster's slot and node cache
	    }

	    this.connectionHandler.assignSlotToNode(jre.getSlot(),
		    jre.getTargetNode());

	    releaseConnection(connection, false);
	    connection = null;
	    
	    return runWithRetries(key, redirections - 1, false, asking);
	} finally {
	    releaseConnection(connection, false);
	}

    }
    
    private void releaseConnection(Jedis connection, boolean broken) {
	if (connection != null) {
	    if (broken) {
		connectionHandler.returnBrokenConnection(connection);
	    } else {
		connectionHandler.returnConnection(connection);
	    }
	}
    }

}