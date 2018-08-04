package cn.lastmiles.bean;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商户支付渠道信息
 * 
 * @author hql
 *
 */
@Table(name = "t_pay_channel_info")
public class PayChannelInfo {//商户支付渠道管理
	@Id
	@Column
	private Long id;//支付渠道ID
	
	@Column
	private Long storeId;//商店ID
	
	/**
	 * 微信：微信公众账号ID
	 * 支付宝appid
	 */
	@Column
	private String appId;
	
	/**
	 * 微信：签名算法需要用到的秘钥
	 * 支付私钥
	 */

	@Column
	private String appKey;
	
	/**
	 * 微信：商户ID
	 * 
	 */
	@Column
	private String mchID;
	
	/**
	 * 微信：子商户ID，受理模式必填
	 * 
	 */
	@Column
	private String subMchID;
	
	/**
	 * 微信：证书路径
	 * 
	 */
	@Column
	private String certLocalPath;
	
	/**
	 * 微信：证书流
	 * 
	 */
	@Column
	private String certIo;
	
	/**
	 * 微信：证书密码
	 * 
	 */
	@Column
	private String certPassword;//

	
	@Column
	private Integer type;//0表示支付宝,1表示微信
	
	private String storeName;//商店名称
	
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	

	@Override
	public String toString() {
		return "PayChannelInfo [id=" + id + ", storeId=" + storeId + ", appId="
				+ appId + ", appKey=" + appKey + ", mchID=" + mchID
				+ ", subMchID=" + subMchID + ", certLocalPath=" + certLocalPath
				+ ", certPassword=" + certPassword + ", type=" + type
				+ ", storeName=" + storeName + "]";
	}

	public String getMchID() {
		return mchID;
	}

	public void setMchID(String mchID) {
		this.mchID = mchID;
	}

	public String getSubMchID() {
		return subMchID;
	}

	public void setSubMchID(String subMchID) {
		this.subMchID = subMchID;
	}

	public String getCertLocalPath() {
		return certLocalPath;
	}

	public void setCertLocalPath(String certLocalPath) {
		this.certLocalPath = certLocalPath;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}

	public String getCertIo() {
		return certIo;
	}

	public void setCertIo(String certIo) {
		this.certIo = certIo;
	}
	
}
