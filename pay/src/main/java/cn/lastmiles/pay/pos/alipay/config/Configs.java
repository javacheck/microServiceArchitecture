package cn.lastmiles.pay.pos.alipay.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by liuyangkly on 15/6/27.
 */
public class Configs {
    private static Log log = LogFactory.getLog(Configs.class);
    private static Configuration configs;
    /**
     * 支付宝openapi域名
     */
    private static String openApiDomain = "https://openapi.alipay.com/gateway.do";
    /**
     * 支付宝mcloudmonitor域名
     */
    private static String mcloudApiDomain = "http://mcloudmonitor.com/gateway.do";  
    /**
     * 商户partner id
     */
    private  String pid;             
    /**
     * 商户应用id
     */
    private  String appid;           
    /**
     *  RSA私钥，用于对商户请求报文加签
     */
    private  String privateKey;
    /**
     *  RSA公钥，仅用于验证开发者网关
     */
    private  String publicKey; 
    /**
     * 支付宝RSA公钥，用于验签支付宝应答
     */
    private static String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";  
    /**
     * 最大查询次数
     */
    private static  int maxQueryRetry = 5;   
    /**
     * 查询间隔（毫秒）
     */
    private static  long queryDuration = 5000;  
    /**
     * 最大撤销次数
     */
    private static  int maxCancelRetry = 3;
    /**
     * 撤销间隔（毫秒）
     */
    private static  long cancelDuration = 2000; 
    /**
     * 交易保障线程第一次调度延迟（秒）
     */
    private static  long heartbeatDelay =5;
    /**
     * 交易保障线程调度间隔（秒）
     */
    private static  long heartbeatDuration =900;

    public Configs() {
        // No Constructor
    }
    
    public  Configs(String pid,String appid,String privateKey,String publicKey) {
    	this.pid=pid;
    	this.appid=appid;
    	this.privateKey=privateKey;
    	this.publicKey=publicKey;
    }
    // 根据文件名读取配置文件，文件后缀名必须为.properties
    public synchronized  void init(String filePath) {
        if (configs != null) {
            return;
        }

        try {
            configs = new PropertiesConfiguration(filePath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        if (configs == null) {
            throw new IllegalStateException("can`t find file by path:" + filePath);
        }

        openApiDomain = configs.getString("open_api_domain");
        mcloudApiDomain = configs.getString("mcloud_api_domain");

        pid = configs.getString("pid");
        appid = configs.getString("appid");

        // RSA
        privateKey = configs.getString("private_key");
        publicKey = configs.getString("public_key");
        alipayPublicKey = configs.getString("alipay_public_key");

        // 查询参数
        maxQueryRetry = configs.getInt("max_query_retry");
        queryDuration = configs.getLong("query_duration");
        maxCancelRetry = configs.getInt("max_cancel_retry");
        cancelDuration = configs.getLong("cancel_duration");

        // 交易保障调度线程
        heartbeatDelay = configs.getLong("heartbeat_delay");
        heartbeatDuration = configs.getLong("heartbeat_duration");

        log.info("配置文件名: " + filePath);
        log.info(description());
    }

    public  String description() {
        StringBuilder sb = new StringBuilder("Configs{");
        sb.append("支付宝openapi网关: ").append(openApiDomain).append("\n");
        if (StringUtils.isNotEmpty(mcloudApiDomain)) {
            sb.append(", 支付宝mcloudapi网关域名: ").append(mcloudApiDomain).append("\n");
        }

        if (StringUtils.isNotEmpty(pid)) {
            sb.append(", pid: ").append(pid).append("\n");
        }
        sb.append(", appid: ").append(appid).append("\n");

        sb.append(", 商户RSA私钥: ").append(getKeyDescription(privateKey)).append("\n");
        sb.append(", 商户RSA公钥: ").append(getKeyDescription(publicKey)).append("\n");
        sb.append(", 支付宝RSA公钥: ").append(getKeyDescription(alipayPublicKey)).append("\n");

        sb.append(", 查询重试次数: ").append(maxQueryRetry).append("\n");
        sb.append(", 查询间隔(毫秒): ").append(queryDuration).append("\n");
        sb.append(", 撤销尝试次数: ").append(maxCancelRetry).append("\n");
        sb.append(", 撤销重试间隔(毫秒): ").append(cancelDuration).append("\n");

        sb.append(", 交易保障调度延迟(秒): ").append(heartbeatDelay).append("\n");
        sb.append(", 交易保障调度间隔(秒): ").append(heartbeatDuration).append("\n");
        sb.append("}");
        return sb.toString();
    }

    public  String getKeyDescription(String key) {
        int showLength = 10;
        if (StringUtils.isNotEmpty(key)) {
            return new StringBuilder(key.substring(0, showLength))
                    .append("******")
                    .append(key.substring(key.length() - showLength))
                    .toString();
        }
        return null;
    }

    public static Configuration getConfigs() {
        return configs;
    }

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		Configs.log = log;
	}

	public static String getOpenApiDomain() {
		return openApiDomain;
	}

	public static void setOpenApiDomain(String openApiDomain) {
		Configs.openApiDomain = openApiDomain;
	}

	public static String getMcloudApiDomain() {
		return mcloudApiDomain;
	}

	public static void setMcloudApiDomain(String mcloudApiDomain) {
		Configs.mcloudApiDomain = mcloudApiDomain;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public static String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public static void setAlipayPublicKey(String alipayPublicKey) {
		Configs.alipayPublicKey = alipayPublicKey;
	}

	public static int getMaxQueryRetry() {
		return maxQueryRetry;
	}

	public static void setMaxQueryRetry(int maxQueryRetry) {
		Configs.maxQueryRetry = maxQueryRetry;
	}

	public static long getQueryDuration() {
		return queryDuration;
	}

	public static void setQueryDuration(long queryDuration) {
		Configs.queryDuration = queryDuration;
	}

	public static int getMaxCancelRetry() {
		return maxCancelRetry;
	}

	public static void setMaxCancelRetry(int maxCancelRetry) {
		Configs.maxCancelRetry = maxCancelRetry;
	}

	public static long getCancelDuration() {
		return cancelDuration;
	}

	public static void setCancelDuration(long cancelDuration) {
		Configs.cancelDuration = cancelDuration;
	}

	public static long getHeartbeatDelay() {
		return heartbeatDelay;
	}

	public static void setHeartbeatDelay(long heartbeatDelay) {
		Configs.heartbeatDelay = heartbeatDelay;
	}

	public static long getHeartbeatDuration() {
		return heartbeatDuration;
	}

	public static void setHeartbeatDuration(long heartbeatDuration) {
		Configs.heartbeatDuration = heartbeatDuration;
	}

	public static void setConfigs(Configuration configs) {
		Configs.configs = configs;
	}

    
}

