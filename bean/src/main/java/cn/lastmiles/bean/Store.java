package cn.lastmiles.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

/**
 * 商家
 * 
 * @author hql
 *
 */
public class Store implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8346497473762146043L;
	private Long id;
	private String name;
	private Date createdTime;
	private Long accountId;
	// 上一级商家
	private Long parentId;
	private String parentName;

	private String companyName; // 公司名
	private Long shopTypeId; // 商家类型ID
	private Long agentId;// 代理商ID
	private Long areaId;// 区域ID
	private String address;// 地址
	private Date updatedTime;// 修改时间
	private Long updatedAccountId;// 修改帐号ID
	private String logo; // logo图标
	private String description;// 详情
	private Double longitude;// 经度
	private Double latitude;// 纬度
	private Integer payType;// 交易类型 0表示在线支付，1表示货到付款，2表示两种方式都行
	private String merchantName; // post商户名称
	private String merchantNo;// post商户号
	private Double mcc; // 标准签约费率
	private Double mccSign;// 签约费率
	private Double mccCost;// 收单成本费率
	private Integer status;// 营业状态 1表示营业，0表示休息中
	private String posBankDeposit;// 收单开户行
	private String posAccountName;// 收单帐号名
	private String posBankLine;// 收单银行行号
	private String posBankAccount;// 收单银行账户
	private String contact;// 联系人
	private String mobile;// 手机号码
	private String phone;// 联系电话
	private String businessTime;// 营业时间
	private Double minAmount;// 起送金额
	private String shipRange;// 配送范围
	private String shipTime;// 送达时间
	private Double onLineRate; // 线上手续费 // 2015-09-10新增
	private Integer onLineRateType; // 线上手续费 类型 0表示按照比例，1表示固定金额 // 2015-11-17新增

	// --- 2015-07-22-14:48新增start
	private Double shipAmount;// 送货费用，默认为0元
	private String service;// 服务(免费取件及送货上门)
	private String shipType;// 送货方式：0：店铺送货上门(默认)，1：到店自提，2：快递，3：第三方配送
	// --- 2015-07-22-14:48新增end

	private String[] shipTypeDesc;

	private Double distance;// 当前人和商店之间的距离
	private Double ratingStar;// 星级
	private String shopTypeName; // 商家类型名称
	private String agentName; // 代理商名称
	private String logoFileCache; // logo缓存
	private String mobileCache; // 手机缓存

	private String[] terminalIdArray; // 设备号集合

	private Double freeShipAmount; // 订单达到这个金额，不需要配送费，免配送费金额

	// 工作日，节假日，不限
	private String[] shipTimeDesc = new String[] { "工作日", "节假日", "不限" };

	private Date startBusinessDate;
	private Date endBusinessDate;

	private String startBusinessString;
	private String endBusinessString;
	
	private Long partnerId; // 合作者关联ID
	private String partnerName; // 合作者名称
	
	// --2016-01-09 新增start
	private String storeAcountName; // 商家帐号
	private String storeAcountNameCache;
	private String storeAcountPassWord; // 商家密码
	private Integer isMain; // 是否是总店(默认是0=商店，1代表总店)
	private Integer isShareUser; // 是否用户共享
	private String mainShopName; // 总店名称
	// --2016-01-09新增end
	
	// --2016-01-18新增start
	private String posAdminPassword; // pos管理密码
	// --2016-01-18新增end
	
	private Integer unifiedPointRule = Constants.Store.UNIFIEDPOINTRULE;//总部统一积分规则，0表示同意使用总部积分规则，1表示不使用总部积分规则
	
	private Long organizationId; //组织结构id
	
	private List<Promotion> promotions;//显示优惠信息
	
	private Integer posCartAuthority = 0;//0 删除、清空pos端购物车的时候，不需要授权密码，1需要，默认0
	private Integer  receiptReprint =1;//0关闭 1开启
	private Integer receiptPrintAmount=2;//1表示1联 2表示2联
	private String organizationName;//組織結構名稱
	
	private String appAuthToken; // 授权token
	private String alipayStoreId; // 口碑店铺
	
	private Integer printer = 0;//使用的打印机 0 默认打印机，1映美打印机
	
	public String getPosAdminPassword() {
		return posAdminPassword;
	}

	public void setPosAdminPassword(String posAdminPassword) {
		this.posAdminPassword = posAdminPassword;
	}

	public String getStoreAcountNameCache() {
		return storeAcountNameCache;
	}

	public void setStoreAcountNameCache(String storeAcountNameCache) {
		this.storeAcountNameCache = storeAcountNameCache;
	}

	public String getAppAuthToken() {
		return appAuthToken;
	}

	public void setAppAuthToken(String appAuthToken) {
		this.appAuthToken = appAuthToken;
	}

	public String getAlipayStoreId() {
		return alipayStoreId;
	}

	public void setAlipayStoreId(String alipayStoreId) {
		this.alipayStoreId = alipayStoreId;
	}

	

	@Override
	public String toString() {
		return "Store [id=" + id + ", name=" + name + ", createdTime="
				+ createdTime + ", accountId=" + accountId + ", parentId="
				+ parentId + ", parentName=" + parentName + ", companyName="
				+ companyName + ", shopTypeId=" + shopTypeId + ", agentId="
				+ agentId + ", areaId=" + areaId + ", address=" + address
				+ ", updatedTime=" + updatedTime + ", updatedAccountId="
				+ updatedAccountId + ", logo=" + logo + ", description="
				+ description + ", longitude=" + longitude + ", latitude="
				+ latitude + ", payType=" + payType + ", merchantName="
				+ merchantName + ", merchantNo=" + merchantNo + ", mcc=" + mcc
				+ ", mccSign=" + mccSign + ", mccCost=" + mccCost + ", status="
				+ status + ", posBankDeposit=" + posBankDeposit
				+ ", posAccountName=" + posAccountName + ", posBankLine="
				+ posBankLine + ", posBankAccount=" + posBankAccount
				+ ", contact=" + contact + ", mobile=" + mobile + ", phone="
				+ phone + ", businessTime=" + businessTime + ", minAmount="
				+ minAmount + ", shipRange=" + shipRange + ", shipTime="
				+ shipTime + ", onLineRate=" + onLineRate + ", onLineRateType="
				+ onLineRateType + ", shipAmount=" + shipAmount + ", service="
				+ service + ", shipType=" + shipType + ", shipTypeDesc="
				+ Arrays.toString(shipTypeDesc) + ", distance=" + distance
				+ ", ratingStar=" + ratingStar + ", shopTypeName="
				+ shopTypeName + ", agentName=" + agentName
				+ ", logoFileCache=" + logoFileCache + ", mobileCache="
				+ mobileCache + ", terminalIdArray="
				+ Arrays.toString(terminalIdArray) + ", freeShipAmount="
				+ freeShipAmount + ", shipTimeDesc="
				+ Arrays.toString(shipTimeDesc) + ", startBusinessDate="
				+ startBusinessDate + ", endBusinessDate=" + endBusinessDate
				+ ", startBusinessString=" + startBusinessString
				+ ", endBusinessString=" + endBusinessString + ", partnerId="
				+ partnerId + ", partnerName=" + partnerName
				+ ", storeAcountName=" + storeAcountName
				+ ", storeAcountNameCache=" + storeAcountNameCache
				+ ", storeAcountPassWord=" + storeAcountPassWord + ", isMain="
				+ isMain + ", isShareUser=" + isShareUser + ", mainShopName="
				+ mainShopName + ", posAdminPassword=" + posAdminPassword
				+ ", unifiedPointRule=" + unifiedPointRule
				+ ", organizationId=" + organizationId + ", promotions="
				+ promotions + ", posCartAuthority=" + posCartAuthority
				+ ", receiptReprint=" + receiptReprint
				+ ", receiptPrintAmount=" + receiptPrintAmount
				+ ", organizationName=" + organizationName + ", appAuthToken="
				+ appAuthToken + ", alipayStoreId=" + alipayStoreId
				+ ", printer=" + printer + ", salesNumOfMonth="
				+ salesNumOfMonth + ", path=" + path + "]";
	}

	public String getMainShopName() {
		return mainShopName;
	}

	public void setMainShopName(String mainShopName) {
		this.mainShopName = mainShopName;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public Integer getIsShareUser() {
		return isShareUser;
	}

	public void setIsShareUser(Integer isShareUser) {
		this.isShareUser = isShareUser;
	}

	public String getStoreAcountName() {
		return storeAcountName;
	}

	public void setStoreAcountName(String storeAcountName) {
		this.storeAcountName = storeAcountName;
	}

	public String getStoreAcountPassWord() {
		return storeAcountPassWord;
	}

	public void setStoreAcountPassWord(String storeAcountPassWord) {
		this.storeAcountPassWord = storeAcountPassWord;
	}

	//月销售单数
	private int salesNumOfMonth;

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getOnLineRateType() {
		return onLineRateType;
	}

	public void setOnLineRateType(Integer onLineRateType) {
		this.onLineRateType = onLineRateType;
	}

	public Date getStartBusinessDate() {
		return startBusinessDate;
	}

	public void setStartBusinessDate(Date startBusinessDate) {
		this.startBusinessDate = startBusinessDate;
	}

	public Date getEndBusinessDate() {
		return endBusinessDate;
	}

	public void setEndBusinessDate(Date endBusinessDate) {
		this.endBusinessDate = endBusinessDate;
	}

	public String getStartBusinessString() {
		return startBusinessString;
	}

	public void setStartBusinessString(String startBusinessString) {
		this.startBusinessString = startBusinessString;
	}

	public String getEndBusinessString() {
		return endBusinessString;
	}

	public void setEndBusinessString(String endBusinessString) {
		this.endBusinessString = endBusinessString;
	}

	public Double getOnLineRate() {
		return onLineRate;
	}

	public void setOnLineRate(Double onLineRate) {
		this.onLineRate = onLineRate;
	}

	public Store() {
		super();
	}

	public Store(Long id, String name, Date createdTime, Long accountId,
			Long parentId, String parentName, String companyName,
			Long shopTypeId, Long agentId, Long areaId, String address,
			Date updatedTime, Long updatedAccountId, String logo,
			String description, Double longitude, Double latitude,
			Integer payType, String merchantName, String merchantNo,
			Double mcc, Double mccSign, Double mccCost, Integer status,
			String posBankDeposit, String posAccountName, String posBankLine,
			String posBankAccount, String contact, String mobile, String phone,
			String businessTime, Double minAmount, String shipRange,
			String shipTime, Double shipAmount, String service,
			String shipType, Double distance, Double ratingStar,
			String shopTypeName, String agentName, String logoFileCache,
			String mobileCache, String path) {
		super();
		this.id = id;
		this.name = name;
		this.createdTime = createdTime;
		this.accountId = accountId;
		this.parentId = parentId;
		this.parentName = parentName;
		this.companyName = companyName;
		this.shopTypeId = shopTypeId;
		this.agentId = agentId;
		this.areaId = areaId;
		this.address = address;
		this.updatedTime = updatedTime;
		this.updatedAccountId = updatedAccountId;
		this.logo = logo;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.payType = payType;
		this.merchantName = merchantName;
		this.merchantNo = merchantNo;
		this.mcc = mcc;
		this.mccSign = mccSign;
		this.mccCost = mccCost;
		this.status = status;
		this.posBankDeposit = posBankDeposit;
		this.posAccountName = posAccountName;
		this.posBankLine = posBankLine;
		this.posBankAccount = posBankAccount;
		this.contact = contact;
		this.mobile = mobile;
		this.phone = phone;
		this.businessTime = businessTime;
		this.minAmount = minAmount;
		this.shipRange = shipRange;
		this.shipTime = shipTime;
		this.shipAmount = shipAmount;
		this.service = service;
		this.shipType = shipType;
		this.distance = distance;
		this.ratingStar = ratingStar;
		this.shopTypeName = shopTypeName;
		this.agentName = agentName;
		this.logoFileCache = logoFileCache;
		this.mobileCache = mobileCache;
		this.path = path;
	}

	public String[] getTerminalIdArray() {
		return terminalIdArray;
	}

	public void setTerminalIdArray(String[] terminalIdArray) {
		this.terminalIdArray = terminalIdArray;
	}

	public String getMobileCache() {
		return mobileCache;
	}

	public void setMobileCache(String mobileCache) {
		this.mobileCache = mobileCache;
	}

	public String getLogoFileCache() {
		return logoFileCache;
	}

	public void setLogoFileCache(String logoFileCache) {
		this.logoFileCache = logoFileCache;
	}

	public Double getShipAmount() {
		return shipAmount == null ? 0.0 : shipAmount;
	}

	public void setShipAmount(Double shipAmount) {
		this.shipAmount = shipAmount;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getShopTypeName() {
		return shopTypeName;
	}

	public void setShopTypeName(String shopTypeName) {
		this.shopTypeName = shopTypeName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getShopTypeId() {
		return shopTypeId;
	}

	public void setShopTypeId(Long shopTypeId) {
		this.shopTypeId = shopTypeId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getUpdatedAccountId() {
		return updatedAccountId;
	}

	public void setUpdatedAccountId(Long updatedAccountId) {
		this.updatedAccountId = updatedAccountId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public Double getMcc() {
		return mcc;
	}

	public void setMcc(Double mcc) {
		this.mcc = mcc;
	}

	public Double getMccSign() {
		return mccSign;
	}

	public void setMccSign(Double mccSign) {
		this.mccSign = mccSign;
	}

	public Double getMccCost() {
		return mccCost;
	}

	public void setMccCost(Double mccCost) {
		this.mccCost = mccCost;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPosBankDeposit() {
		return posBankDeposit;
	}

	public void setPosBankDeposit(String posBankDeposit) {
		this.posBankDeposit = posBankDeposit;
	}

	public String getPosAccountName() {
		return posAccountName;
	}

	public void setPosAccountName(String posAccountName) {
		this.posAccountName = posAccountName;
	}

	public String getPosBankLine() {
		return posBankLine;
	}

	public void setPosBankLine(String posBankLine) {
		this.posBankLine = posBankLine;
	}

	public String getPosBankAccount() {
		return posBankAccount;
	}

	public void setPosBankAccount(String posBankAccount) {
		this.posBankAccount = posBankAccount;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBusinessTime() {
		return businessTime;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}

	public Double getMinAmount() {
		return minAmount == null ? 0.0 : minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public String getShipRange() {
		return shipRange;
	}

	public void setShipRange(String shipRange) {
		this.shipRange = shipRange;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	/**
	 * 全路径，从根节点开始的id连接 1-10-100
	 */
	private String path;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Double getDistance() {
		// 转成米
		if (distance != null) {
			distance = distance * 1000;
			return distance > 1 ? distance.intValue() : distance;
		}
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getRatingStar() {
		return ratingStar;
	}

	public void setRatingStar(Double ratingStar) {
		this.ratingStar = ratingStar;
	}

	public Double getFreeShipAmount() {
		return freeShipAmount == null ? 0.0 : freeShipAmount;
	}

	public void setFreeShipAmount(Double freeShipAmount) {
		this.freeShipAmount = freeShipAmount;
	}

	public String[] getShipTypeGroup() {
		// 送货方式：0：店铺送货上门(默认)，1：到店自提，2：快递，3：第三方配送
		return getShipTypeGroup(shipType);
	}
	
	public static String[] getShipTypeGroup(String shipType) {
		String[] shipTypeGroup = null;
		if (StringUtils.isNotBlank(shipType)) {
			String[] arr = StringUtils.split(shipType, ",");
			shipTypeGroup = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				if (StringUtils.equals("0", arr[i])) {
					shipTypeGroup[i] = "0|店铺送货上门";
				} else if (StringUtils.equals("1", arr[i])) {
					shipTypeGroup[i] = "1|到店自提";
				} else if (StringUtils.equals("2", arr[i])) {
					shipTypeGroup[i] = "2|快递";
				} else if (StringUtils.equals("3", arr[i])) {
					shipTypeGroup[i] = "3|第三方配送";
				}
			}
		}
		return shipTypeGroup;
	}
	
	public String[] getShipTypeDesc() {
		// 送货方式：0：店铺送货上门(默认)，1：到店自提，2：快递，3：第三方配送
		return getShipTypeDesc(shipType);
	}

	public static String[] getShipTypeDesc(String shipType) {
		String[] shipTypeDesc = null;
		if (StringUtils.isNotBlank(shipType)) {
			String[] arr = StringUtils.split(shipType, ",");
			shipTypeDesc = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				if (StringUtils.equals("0", arr[i])) {
					shipTypeDesc[i] = "店铺送货上门";
				} else if (StringUtils.equals("1", arr[i])) {
					shipTypeDesc[i] = "到店自提";
				} else if (StringUtils.equals("2", arr[i])) {
					shipTypeDesc[i] = "快递";
				} else if (StringUtils.equals("3", arr[i])) {
					shipTypeDesc[i] = "第三方配送";
				}
			}
		}
		return shipTypeDesc;
	}

	/**
	 * 是否支持到店自提
	 * 
	 * @return
	 */
	public boolean getServiceMySelf() {
		return getServiceMySelf(shipType);
	}
	
	public static boolean getServiceMySelf(String shipType){
		if (StringUtils.isNotBlank(shipType)) {
			String[] arr = StringUtils.split(shipType, ",");
			for (String s : arr) {
				if ("1".equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setShipTypeDesc(String[] shipTypeDesc) {
		this.shipTypeDesc = shipTypeDesc;
	}

	public String[] getShipTimeDesc() {
		if (null != shopTypeId && shopTypeId.longValue() != 0L) {
			return shipTimeDesc;
		}
		return new String[] {};
	}

	public void setShipTimeDesc(String[] shipTimeDesc) {
		this.shipTimeDesc = shipTimeDesc;
	}

	public int getSalesNumOfMonth() {
		return salesNumOfMonth;
	}

	public void setSalesNumOfMonth(int salesNumOfMonth) {
		this.salesNumOfMonth = salesNumOfMonth;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public Integer getUnifiedPointRule() {
		//默认
		return unifiedPointRule == null ? Constants.Store.UNIFIEDPOINTRULE : unifiedPointRule;
	}

	public void setUnifiedPointRule(Integer unifiedPointRule) {
		this.unifiedPointRule = unifiedPointRule;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getPosCartAuthority() {
		if (posCartAuthority == null){
			 posCartAuthority = 0;
		}
		return posCartAuthority;
	}

	public void setPosCartAuthority(Integer posCartAuthority) {
		this.posCartAuthority = posCartAuthority;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Integer getReceiptReprint() {
		if (receiptReprint == null){
			receiptReprint =1;
		}
		return receiptReprint;
	}

	public void setReceiptReprint(Integer receiptReprint) {
		this.receiptReprint = receiptReprint;
	}

	public Integer getReceiptPrintAmount() {
		if (receiptPrintAmount == null){
			receiptPrintAmount=2;
		}
		return receiptPrintAmount;
	}

	public void setReceiptPrintAmount(Integer receiptPrintAmount) {
		this.receiptPrintAmount = receiptPrintAmount;
	}

	public Integer getPrinter() {
		return printer;
	}

	public void setPrinter(Integer printer) {
		this.printer = printer;
	}
	
	
}
