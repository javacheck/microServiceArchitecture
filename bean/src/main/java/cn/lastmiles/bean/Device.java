package cn.lastmiles.bean;

import java.io.Serializable;

public class Device implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;//ID
	
	private String serialId;//串行ID
	
	private String deviceSn;//终端SN
	
	private String deviceName;//设备名称
	
	private Long storeId;//店铺ID
	
	private String storeName;//店铺名称
	
	private Integer status;//状态
	
	private String factory;//厂商
	
	private String model;
	
	public Device() {
		super();
	}
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getDeviceSn() {
		return deviceSn;
	}


	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}




	public String getSerialId() {
		return serialId;
	}


	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}


	@Override
	public String toString() {
		return "Device [id=" + id + ", serialId=" + serialId + ", deviceSn="
				+ deviceSn + ", deviceName=" + deviceName + ", storeId="
				+ storeId + ", storeName=" + storeName + ", status=" + status
				+ ", factory=" + factory + ", model=" + model + "]";
	}
	
}