/**
 * createDate : 2016年7月26日下午3:35:20
 */
package cn.lastmiles.alipay.bean;

public class CreateShopInfo {
	// <必填> 外部门店编号；最长32位字符，该编号将作为收单接口的入参， 请开发者自行确保其唯一性。
		private String storeId;
		
	// <必填> 类目id，请参考商户入驻要求
		private String categoryId;
		
		// <可选> 品牌名，不填写则默认为“其它品牌”。
		private String brandName;
		
		// <可选> 品牌LOGO; 图片ID，不填写则默认为门店首图main_image。
		private String brandLogo;
		
	// <必填> 主门店名 比如：肯德基；主店名里不要包含分店名，如“万塘路店”。主店名长度不能超过20个字符。
		private String mainShopName;
		
		// <可选> 分店名称，比如：万塘路店，与主门店名合并在客户端显示为：肯德基(万塘路店)。
		private String branchShopName;
		
	// <必填> 省份编码，国标码，详见国家统计局数据 点此下载。
		private String provinceCode;
		
	// <必填> 城市编码，国标码，详见国家统计局数据 点此下载。
		private String cityCode;
		
	// <必填> 区县编码，国标码，详见国家统计局数据 点此下载。
		private String districtCode;
		
	// <必填> 门店详细地址，地址字符长度在4-50个字符，注：不含省市区。门店详细地址按规范格式填写地址，以免影响门店搜索及活动报名：例1：道路+门牌号，“人民东路18号”；例2：道路+门牌号+标志性建筑+楼层，“四川北路1552号欢乐广场1楼”。
		private String address;
		
	// <必填> 经度；最长15位字符（包括小数点）， 注：高德坐标系。经纬度是门店搜索和活动推荐的重要参数，录入时请确保经纬度参数准确。
		private Double longitude;
		
	// <必填> 纬度；最长15位字符（包括小数点）， 注：高德坐标系。经纬度是门店搜索和活动推荐的重要参数，录入时请确保经纬度参数准确。
		private Double latitude;
		
	// <必填> 门店电话号码；支持座机和手机，只支持数字和+-号，在客户端对用户展现， 支持多个电话， 以英文逗号分隔。
		private String contactNumber;
		
		// <可选> 门店店长电话号码；用于接收门店状态变更通知，收款成功通知等通知消息， 不在客户端展示。
		private String notifyMobile;
		
	// <必填> 门店首图，非常重要，推荐尺寸2000*1500。
		private String mainImage;
		
		// <可选> 门店审核时需要的图片；至少包含一张门头照片，两张内景照片，必须反映真实的门店情况，审核才能够通过；多个图片之间以英文逗号分隔。
		private String auditImage;
		
		// <可选> 营业时间，支持分段营业时间，以英文逗号分隔。
		private String businessTime;
		
		// <可选> 门店是否支持WIFI，T表示支持，F表示不支持，不传在客户端不作展示。
		private String wifi;
		
		// <可选> 门店是否支持停车，T表示支持，F表示不支持，不传在客户端不作展示。
		private String parking;
		
		// <可选> 门店其他的服务，门店与用户线下兑现。
		private String valueAdded;
		
		// <可选> 人均消费价格，最少1元，最大不超过99999元，请按实际情况填写；单位元，不需填写单位。
		private String avgPrice;
		
	// <必填> ISV返佣id，门店创建、或者门店交易的返佣将通过此账号反给ISV，如果有口碑签订了返佣协议，则该字段作为返佣数据提取的依据。此字段必须是个合法uid，2088开头的16位支付宝会员账号，如果传入错误将无法创建门店。
		private String isvUid;
		
		// <可选> 门店营业执照图片，各行业所需的证照资质参见商户入驻要求。
		private String licence; 
		
		// <可选> 门店营业执照编号，营业执照信息与is_operating_online至少填一项。
		private String licenceCode;
		
		// <可选> 门店营业执照名称。
		private String licenceName;
		
		// <可选> 许可证，各行业所需的证照资质参见商户入驻要求；该字段只能上传一张许可证，一张以外的许可证、除营业执照和许可证之外其他证照请放在其他资质字段上传。
		private String businessCertificate;
		
		// <可选> 许可证有效期，格式：2020-03-20。
		private String businessCertificteExpires;
		
		// <可选> 门店授权函,营业执照与签约账号主体不一致时需要。
		private String authLetter;
		
		// <可选> 是否在其他平台开店，T表示有开店，F表示未开店。
		private String isOperatingOnline;
		
		// <可选> 其他平台开店的店铺链接url，多个url使用英文逗号隔开,isv迁移到新接口使用此字段，与is_operating_online=T配套使用。
		private String onlineUrl;
		
		// <可选> 当商户的门店审核状态发生变化时，会向该地址推送消息。
		private String operateNotifyUrl;
		
		// <可选> 机具号，多个之间以英文逗号分隔。
		private String implementId;
		
		// <可选> 是否有无烟区，T表示有无烟区，F表示没有无烟区，不传在客户端不展示
		private String noSmoking;
		
		// <可选> 门店是否有包厢，T表示有，F表示没有，不传在客户端不作展示。
		private String box;
		
		// <可选> 支持英文字母和数字，由开发者自行定义（不允许重复），在门店notify消息中也会带有该参数，以此标明本次notify消息是对哪个请求的回应。
		private String requestId;
		
		// <可选> 其他资质。用于上传营业证照、许可证照外的其他资质，除已上传许可证外的其他许可证也可以在该字段上传。
		private String otherAuthorization;
		
		// <可选> 营业执照过期时间
		private String licenceExpires;
		
		// <可选> 表示以系统集成商的身份开店，开放平台现在统一传入ISV。
		private String opRole;
		
	// <必填> 店铺接口业务版本号，新接入的ISV，请统一传入2.0。
		private String bizVersion;

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}

		public String getCategoryId() {
			return categoryId;
		}

		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}

		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}

		public String getBrandLogo() {
			return brandLogo;
		}

		public void setBrandLogo(String brandLogo) {
			this.brandLogo = brandLogo;
		}

		public String getMainShopName() {
			return mainShopName;
		}

		public void setMainShopName(String mainShopName) {
			this.mainShopName = mainShopName;
		}

		public String getBranchShopName() {
			return branchShopName;
		}

		public void setBranchShopName(String branchShopName) {
			this.branchShopName = branchShopName;
		}

		public String getProvinceCode() {
			return provinceCode;
		}

		public void setProvinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
		}

		public String getCityCode() {
			return cityCode;
		}

		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}

		public String getDistrictCode() {
			return districtCode;
		}

		public void setDistrictCode(String districtCode) {
			this.districtCode = districtCode;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
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

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getNotifyMobile() {
			return notifyMobile;
		}

		public void setNotifyMobile(String notifyMobile) {
			this.notifyMobile = notifyMobile;
		}

		public String getMainImage() {
			return mainImage;
		}

		public void setMainImage(String mainImage) {
			this.mainImage = mainImage;
		}

		public String getAuditImage() {
			return auditImage;
		}

		public void setAuditImage(String auditImage) {
			this.auditImage = auditImage;
		}

		public String getBusinessTime() {
			return businessTime;
		}

		public void setBusinessTime(String businessTime) {
			this.businessTime = businessTime;
		}

		public String getWifi() {
			return wifi;
		}

		public void setWifi(String wifi) {
			this.wifi = wifi;
		}

		public String getParking() {
			return parking;
		}

		public void setParking(String parking) {
			this.parking = parking;
		}

		public String getValueAdded() {
			return valueAdded;
		}

		public void setValueAdded(String valueAdded) {
			this.valueAdded = valueAdded;
		}

		public String getAvgPrice() {
			return avgPrice;
		}

		public void setAvgPrice(String avgPrice) {
			this.avgPrice = avgPrice;
		}

		public String getIsvUid() {
			return isvUid;
		}

		public void setIsvUid(String isvUid) {
			this.isvUid = isvUid;
		}

		public String getLicence() {
			return licence;
		}

		public void setLicence(String licence) {
			this.licence = licence;
		}

		public String getLicenceCode() {
			return licenceCode;
		}

		public void setLicenceCode(String licenceCode) {
			this.licenceCode = licenceCode;
		}

		public String getLicenceName() {
			return licenceName;
		}

		public void setLicenceName(String licenceName) {
			this.licenceName = licenceName;
		}

		public String getBusinessCertificate() {
			return businessCertificate;
		}

		public void setBusinessCertificate(String businessCertificate) {
			this.businessCertificate = businessCertificate;
		}

		public String getBusinessCertificteExpires() {
			return businessCertificteExpires;
		}

		public void setBusinessCertificteExpires(String businessCertificteExpires) {
			this.businessCertificteExpires = businessCertificteExpires;
		}

		public String getAuthLetter() {
			return authLetter;
		}

		public void setAuthLetter(String authLetter) {
			this.authLetter = authLetter;
		}

		public String getIsOperatingOnline() {
			return isOperatingOnline;
		}

		public void setIsOperatingOnline(String isOperatingOnline) {
			this.isOperatingOnline = isOperatingOnline;
		}

		public String getOnlineUrl() {
			return onlineUrl;
		}

		public void setOnlineUrl(String onlineUrl) {
			this.onlineUrl = onlineUrl;
		}

		public String getOperateNotifyUrl() {
			return operateNotifyUrl;
		}

		public void setOperateNotifyUrl(String operateNotifyUrl) {
			this.operateNotifyUrl = operateNotifyUrl;
		}

		public String getImplementId() {
			return implementId;
		}

		public void setImplementId(String implementId) {
			this.implementId = implementId;
		}

		public String getNoSmoking() {
			return noSmoking;
		}

		public void setNoSmoking(String noSmoking) {
			this.noSmoking = noSmoking;
		}

		public String getBox() {
			return box;
		}

		public void setBox(String box) {
			this.box = box;
		}

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getOtherAuthorization() {
			return otherAuthorization;
		}

		public void setOtherAuthorization(String otherAuthorization) {
			this.otherAuthorization = otherAuthorization;
		}

		public String getLicenceExpires() {
			return licenceExpires;
		}

		public void setLicenceExpires(String licenceExpires) {
			this.licenceExpires = licenceExpires;
		}

		public String getOpRole() {
			return opRole;
		}

		public void setOpRole(String opRole) {
			this.opRole = opRole;
		}

		public String getBizVersion() {
			return bizVersion;
		}

		public void setBizVersion(String bizVersion) {
			this.bizVersion = bizVersion;
		}

		public CreateShopInfo() {
			super();
		}

		public CreateShopInfo(String storeId, String categoryId,
				String brandName, String brandLogo, String mainShopName,
				String branchShopName, String provinceCode, String cityCode,
				String districtCode, String address, Double longitude,
				Double latitude, String contactNumber, String notifyMobile,
				String mainImage, String auditImage, String businessTime,
				String wifi, String parking, String valueAdded,
				String avgPrice, String isvUid, String licence,
				String licenceCode, String licenceName,
				String businessCertificate, String businessCertificteExpires,
				String authLetter, String isOperatingOnline, String onlineUrl,
				String operateNotifyUrl, String implementId, String noSmoking,
				String box, String requestId, String otherAuthorization,
				String licenceExpires, String opRole, String bizVersion) {
			super();
			this.storeId = storeId;
			this.categoryId = categoryId;
			this.brandName = brandName;
			this.brandLogo = brandLogo;
			this.mainShopName = mainShopName;
			this.branchShopName = branchShopName;
			this.provinceCode = provinceCode;
			this.cityCode = cityCode;
			this.districtCode = districtCode;
			this.address = address;
			this.longitude = longitude;
			this.latitude = latitude;
			this.contactNumber = contactNumber;
			this.notifyMobile = notifyMobile;
			this.mainImage = mainImage;
			this.auditImage = auditImage;
			this.businessTime = businessTime;
			this.wifi = wifi;
			this.parking = parking;
			this.valueAdded = valueAdded;
			this.avgPrice = avgPrice;
			this.isvUid = isvUid;
			this.licence = licence;
			this.licenceCode = licenceCode;
			this.licenceName = licenceName;
			this.businessCertificate = businessCertificate;
			this.businessCertificteExpires = businessCertificteExpires;
			this.authLetter = authLetter;
			this.isOperatingOnline = isOperatingOnline;
			this.onlineUrl = onlineUrl;
			this.operateNotifyUrl = operateNotifyUrl;
			this.implementId = implementId;
			this.noSmoking = noSmoking;
			this.box = box;
			this.requestId = requestId;
			this.otherAuthorization = otherAuthorization;
			this.licenceExpires = licenceExpires;
			this.opRole = opRole;
			this.bizVersion = bizVersion;
		}
}