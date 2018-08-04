/**
 * createDate : 2016年7月25日下午4:01:23
 * 口碑店铺
 */
package cn.lastmiles.alipay.model.builder.shop;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.RequestBuilder;
import com.google.gson.annotations.SerializedName;

public class AlipayCreateShopInfoRequestBuilder extends RequestBuilder{

	private BizContent bizContent = new BizContent();
	
	@Override
	public String validate() {
		
		if( StringUtils.isNotBlank(bizContent.isOperatingOnline)){
			if( !"T".equals(bizContent.isOperatingOnline) && !"F".equals(bizContent.isOperatingOnline)){
				bizContent.isOperatingOnline = "";
			}
		}
		
		String nullValue = nullValueValidate(); // 必填非空判断
		if(!Constants.VALIDATESUCCESS.equals(nullValue)){
			return nullValue;
		}
		
		String lengthValue = lengthValidate();
		if(!Constants.VALIDATESUCCESS.equals(lengthValue)){
			return lengthValue;
		}
		
		String ruleValue = ruleValidate();
		if(!Constants.VALIDATESUCCESS.equals(ruleValue)){
			return ruleValue;
		}
		return Constants.VALIDATESUCCESS;
	}

	@Override
	public BizContent getBizContent() {
		return bizContent;
	}
	
	public static class BizContent {
	// <必填> 外部门店编号；最长32位字符，该编号将作为收单接口的入参， 请开发者自行确保其唯一性。
		@SerializedName("store_id")
		private String storeId;
		
	// <必填> 类目id，请参考商户入驻要求
		@SerializedName("category_id")
		private String categoryId;
		
		// <可选> 品牌名，不填写则默认为“其它品牌”。
		@SerializedName("brand_name")
		private String brandName;
		
		// <可选> 品牌LOGO; 图片ID，不填写则默认为门店首图main_image。
		@SerializedName("brand_logo")
		private String brandLogo;
		
	// <必填> 主门店名 比如：肯德基；主店名里不要包含分店名，如“万塘路店”。主店名长度不能超过20个字符。
		@SerializedName("main_shop_name")
		private String mainShopName;
		
		// <可选> 分店名称，比如：万塘路店，与主门店名合并在客户端显示为：肯德基(万塘路店)。
		@SerializedName("branch_shop_name")
		private String branchShopName;
		
	// <必填> 省份编码，国标码，详见国家统计局数据 点此下载。
		@SerializedName("province_code")
		private String provinceCode;
		
	// <必填> 城市编码，国标码，详见国家统计局数据 点此下载。
		@SerializedName("city_code")
		private String cityCode;
		
	// <必填> 区县编码，国标码，详见国家统计局数据 点此下载。
		@SerializedName("district_code")
		private String districtCode;
		
	// <必填> 门店详细地址，地址字符长度在4-50个字符，注：不含省市区。门店详细地址按规范格式填写地址，以免影响门店搜索及活动报名：例1：道路+门牌号，“人民东路18号”；例2：道路+门牌号+标志性建筑+楼层，“四川北路1552号欢乐广场1楼”。
		@SerializedName("address")
		private String address;
		
	// <必填> 经度；最长15位字符（包括小数点）， 注：高德坐标系。经纬度是门店搜索和活动推荐的重要参数，录入时请确保经纬度参数准确。
		@SerializedName("longitude")
		private Double longitude;
		
	// <必填> 纬度；最长15位字符（包括小数点）， 注：高德坐标系。经纬度是门店搜索和活动推荐的重要参数，录入时请确保经纬度参数准确。
		@SerializedName("latitude")
		private Double latitude;
		
	// <必填> 门店电话号码；支持座机和手机，只支持数字和+-号，在客户端对用户展现， 支持多个电话， 以英文逗号分隔。
		@SerializedName("contact_number")
		private String contactNumber;
		
		// <可选> 门店店长电话号码；用于接收门店状态变更通知，收款成功通知等通知消息， 不在客户端展示。
		@SerializedName("notify_mobile")
		private String notifyMobile;
		
	// <必填> 门店首图，非常重要，推荐尺寸2000*1500。
		@SerializedName("main_image")
		private String mainImage;
		
		// <可选> 门店审核时需要的图片；至少包含一张门头照片，两张内景照片，必须反映真实的门店情况，审核才能够通过；多个图片之间以英文逗号分隔。
		@SerializedName("audit_images")
		private String auditImage;
		
		// <可选> 营业时间，支持分段营业时间，以英文逗号分隔。
		@SerializedName("business_time")
		private String businessTime;
		
		// <可选> 门店是否支持WIFI，T表示支持，F表示不支持，不传在客户端不作展示。
		@SerializedName("wifi")
		private String wifi;
		
		// <可选> 门店是否支持停车，T表示支持，F表示不支持，不传在客户端不作展示。
		@SerializedName("parking")
		private String parking;
		
		// <可选> 门店其他的服务，门店与用户线下兑现。
		@SerializedName("value_added")
		private String valueAdded;
		
		// <可选> 人均消费价格，最少1元，最大不超过99999元，请按实际情况填写；单位元，不需填写单位。
		@SerializedName("avg_price")
		private String avgPrice;
		
		// <必填> ISV返佣id，门店创建、或者门店交易的返佣将通过此账号反给ISV，如果有口碑签订了返佣协议，则该字段作为返佣数据提取的依据。此字段必须是个合法uid，2088开头的16位支付宝会员账号，如果传入错误将无法创建门店。
		@SerializedName("isv_uid")
		private String isvUid;
		
		// <可选> 门店营业执照图片，各行业所需的证照资质参见商户入驻要求。
		@SerializedName("licence")
		private String licence; 
		
		// <可选> 门店营业执照编号，营业执照信息与is_operating_online至少填一项。
		@SerializedName("licence_code")
		private String licenceCode;
		
		// <可选> 门店营业执照名称。
		@SerializedName("licence_name")
		private String licenceName;
		
		// <可选> 许可证，各行业所需的证照资质参见商户入驻要求；该字段只能上传一张许可证，一张以外的许可证、除营业执照和许可证之外其他证照请放在其他资质字段上传。
		@SerializedName("business_certificate")
		private String businessCertificate;
		
		// <可选> 许可证有效期，格式：2020-03-20。
		@SerializedName("business_certificate_expires")
		private String businessCertificteExpires;
		
		// <可选> 门店授权函,营业执照与签约账号主体不一致时需要。
		@SerializedName("auth_letter")
		private String authLetter;
		
		// <可选> 是否在其他平台开店，T表示有开店，F表示未开店。
		@SerializedName("is_operating_online")
		private String isOperatingOnline;
		
		// <可选> 其他平台开店的店铺链接url，多个url使用英文逗号隔开,isv迁移到新接口使用此字段，与is_operating_online=T配套使用。
		@SerializedName("online_url")
		private String onlineUrl;
		
		// <可选> 当商户的门店审核状态发生变化时，会向该地址推送消息。
		@SerializedName("operate_notify_url")
		private String operateNotifyUrl;
		
		// <可选> 机具号，多个之间以英文逗号分隔。
		@SerializedName("implement_id")
		private String implementId;
		
		// <可选> 是否有无烟区，T表示有无烟区，F表示没有无烟区，不传在客户端不展示
		@SerializedName("no_smoking")
		private String noSmoking;
		
		// <可选> 门店是否有包厢，T表示有，F表示没有，不传在客户端不作展示。
		@SerializedName("box")
		private String box;
		
		// <可选> 支持英文字母和数字，由开发者自行定义（不允许重复），在门店notify消息中也会带有该参数，以此标明本次notify消息是对哪个请求的回应。
		@SerializedName("request_id")
		private String requestId;
		
		// <可选> 其他资质。用于上传营业证照、许可证照外的其他资质，除已上传许可证外的其他许可证也可以在该字段上传。
		@SerializedName("other_authorization")
		private String otherAuthorization;
		
		// <可选> 营业执照过期时间
		@SerializedName("licence_expires")
		private String licenceExpires;
		
		// <可选> 表示以系统集成商的身份开店，开放平台现在统一传入ISV。
		@SerializedName("op_role")
		private String opRole;
		
		// <必填> 店铺接口业务版本号，新接入的ISV，请统一传入2.0。
		@SerializedName("biz_version")
		private String bizVersion;

		@Override
		public String toString() {
			return "BizContent [storeId=" + storeId + ", categoryId="
					+ categoryId + ", brandName=" + brandName + ", brandLogo="
					+ brandLogo + ", mainShopName=" + mainShopName
					+ ", branchShopName=" + branchShopName + ", provinceCode="
					+ provinceCode + ", cityCode=" + cityCode
					+ ", districtCode=" + districtCode + ", address=" + address
					+ ", longitude=" + longitude + ", latitude=" + latitude
					+ ", contactNumber=" + contactNumber + ", notifyMobile="
					+ notifyMobile + ", mainImage=" + mainImage
					+ ", auditImage=" + auditImage + ", businessTime="
					+ businessTime + ", wifi=" + wifi + ", parking=" + parking
					+ ", valueAdded=" + valueAdded + ", avgPrice=" + avgPrice
					+ ", isvUid=" + isvUid + ", licence=" + licence
					+ ", licenceCode=" + licenceCode + ", licenceName="
					+ licenceName + ", businessCertificate="
					+ businessCertificate + ", businessCertificteExpires="
					+ businessCertificteExpires + ", authLetter=" + authLetter
					+ ", isOperatingOnline=" + isOperatingOnline
					+ ", onlineUrl=" + onlineUrl + ", operateNotifyUrl="
					+ operateNotifyUrl + ", implementId=" + implementId
					+ ", noSmoking=" + noSmoking + ", box=" + box
					+ ", requestId=" + requestId + ", otherAuthorization="
					+ otherAuthorization + ", licenceExpires=" + licenceExpires
					+ ", opRole=" + opRole + ", bizVersion=" + bizVersion + "]";
		}
		
	}
	
	// --------------------------getter ------  setter ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓--------------------------------------
	public String getStoreId() {
		return bizContent.storeId;
	}

	public void setStoreId(String storeId) {
		bizContent.storeId = storeId;
	}

	public String getCategoryId() {
		return bizContent.categoryId;
	}

	public void setCategoryId(String categoryId) {
		bizContent.categoryId = categoryId;
	}

	public String getBrandName() {
		return bizContent.brandName;
	}

	public void setBrandName(String brandName) {
		bizContent.brandName = brandName;
	}

	public String getBrandLogo() {
		return bizContent.brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		bizContent.brandLogo = brandLogo;
	}

	public String getMainShopName() {
		return bizContent.mainShopName;
	}

	public void setMainShopName(String mainShopName) {
		bizContent.mainShopName = mainShopName;
	}

	public String getBranchShopName() {
		return bizContent.branchShopName;
	}

	public void setBranchShopName(String branchShopName) {
		bizContent.branchShopName = branchShopName;
	}

	public String getProvinceCode() {
		return bizContent.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		bizContent.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return bizContent.cityCode;
	}

	public void setCityCode(String cityCode) {
		bizContent.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return bizContent.districtCode;
	}

	public void setDistrictCode(String districtCode) {
		bizContent.districtCode = districtCode;
	}

	public String getAddress() {
		return bizContent.address;
	}

	public void setAddress(String address) {
		bizContent.address = address;
	}

	public Double getLongitude() {
		return bizContent.longitude;
	}

	public void setLongitude(Double longitude) {
		bizContent.longitude = longitude;
	}

	public Double getLatitude() {
		return bizContent.latitude;
	}

	public void setLatitude(Double latitude) {
		bizContent.latitude = latitude;
	}

	public String getContactNumber() {
		return bizContent.contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		bizContent.contactNumber = contactNumber;
	}

	public String getNotifyMobile() {
		return bizContent.notifyMobile;
	}

	public void setNotifyMobile(String notifyMobile) {
		bizContent.notifyMobile = notifyMobile;
	}

	public String getMainImage() {
		return bizContent.mainImage;
	}

	public void setMainImage(String mainImage) {
		bizContent.mainImage = mainImage;
	}

	public String getAuditImage() {
		return bizContent.auditImage;
	}

	public void setAuditImage(String auditImage) {
		bizContent.auditImage = auditImage;
	}

	public String getBusinessTime() {
		return bizContent.businessTime;
	}

	public void setBusinessTime(String businessTime) {
		bizContent.businessTime = businessTime;
	}

	public String getWifi() {
		return bizContent.wifi;
	}

	public void setWifi(String wifi) {
		bizContent.wifi = wifi;
	}

	public String getParking() {
		return bizContent.parking;
	}

	public void setParking(String parking) {
		bizContent.parking = parking;
	}

	public String getValueAdded() {
		return bizContent.valueAdded;
	}

	public void setValueAdded(String valueAdded) {
		bizContent.valueAdded = valueAdded;
	}

	public String getAvgPrice() {
		return bizContent.avgPrice;
	}

	public void setAvgPrice(String avgPrice) {
		bizContent.avgPrice = avgPrice;
	}

	public String getIsvUid() {
		return bizContent.isvUid;
	}

	public void setIsvUid(String isvUid) {
		bizContent.isvUid = isvUid;
	}

	public String getLicence() {
		return bizContent.licence;
	}

	public void setLicence(String licence) {
		bizContent.licence = licence;
	}

	public String getLicenceCode() {
		return bizContent.licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		bizContent.licenceCode = licenceCode;
	}

	public String getLicenceName() {
		return bizContent.licenceName;
	}

	public void setLicenceName(String licenceName) {
		bizContent.licenceName = licenceName;
	}

	public String getBusinessCertificate() {
		return bizContent.businessCertificate;
	}

	public void setBusinessCertificate(String businessCertificate) {
		bizContent.businessCertificate = businessCertificate;
	}

	public String getBusinessCertificteExpires() {
		return bizContent.businessCertificteExpires;
	}

	public void setBusinessCertificteExpires(String businessCertificteExpires) {
		bizContent.businessCertificteExpires = businessCertificteExpires;
	}

	public String getAuthLetter() {
		return bizContent.authLetter;
	}

	public void setAuthLetter(String authLetter) {
		bizContent.authLetter = authLetter;
	}

	public String getIsOperatingOnline() {
		return bizContent.isOperatingOnline;
	}

	public void setIsOperatingOnline(String isOperatingOnline) {
		bizContent.isOperatingOnline = isOperatingOnline;
	}

	public String getOnlineUrl() {
		return bizContent.onlineUrl;
	}

	public void setOnlineUrl(String onlineUrl) {
		bizContent.onlineUrl = onlineUrl;
	}

	public String getOperateNotifyUrl() {
		return bizContent.operateNotifyUrl;
	}

	public void setOperateNotifyUrl(String operateNotifyUrl) {
		bizContent.operateNotifyUrl = operateNotifyUrl;
	}

	public String getImplementId() {
		return bizContent.implementId;
	}

	public void setImplementId(String implementId) {
		bizContent.implementId = implementId;
	}

	public String getNoSmoking() {
		return bizContent.noSmoking;
	}

	public void setNoSmoking(String noSmoking) {
		bizContent.noSmoking = noSmoking;
	}

	public String getBox() {
		return bizContent.box;
	}

	public void setBox(String box) {
		bizContent.box = box;
	}

	public String getRequestId() {
		return bizContent.requestId;
	}

	public void setRequestId(String requestId) {
		bizContent.requestId = requestId;
	}

	public String getOtherAuthorization() {
		return bizContent.otherAuthorization;
	}

	public void setOtherAuthorization(String otherAuthorization) {
		bizContent.otherAuthorization = otherAuthorization;
	}

	public String getLicenceExpires() {
		return bizContent.licenceExpires;
	}

	public void setLicenceExpires(String licenceExpires) {
		bizContent.licenceExpires = licenceExpires;
	}

	public String getOpRole() {
		return bizContent.opRole;
	}

	public void setOpRole(String opRole) {
		bizContent.opRole = opRole;
	}

	public String getBizVersion() {
		return bizContent.bizVersion;
	}

	public void setBizVersion(String bizVersion) {
		bizContent.bizVersion = bizVersion;
	}
	
	// --------------------------getter ------  setter↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ --------------------------------------
	
	public String nullValueValidate() {
		if( StringUtils.isBlank(bizContent.storeId)){
			 return "storeId should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.categoryId) ){
			return "categoryId should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.mainShopName)){
			return "mainShopName should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.provinceCode)){
			return "provinceCode should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.cityCode)){
			return "cityCode should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.districtCode)){
			return "districtCode should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.address)){
			return "address should not be NULL!";
		}
		
		if( null == bizContent.longitude ){
			return "longitude should not be NULL!";
		}
		
		if( null == bizContent.latitude ){
			return "latitude should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.contactNumber)){
			return "contactNumber should not be NULL!";
		}
		
		if( StringUtils.isBlank(bizContent.mainImage)){
			return "mainImage should not be NULL!";
		}

		if( StringUtils.isBlank(bizContent.isvUid)){
			return "isvUid should not be NULL!";
		}
		
	    if (StringUtils.isBlank(bizContent.licenceCode) &&
                StringUtils.isBlank(bizContent.isOperatingOnline)) {
            return "licenceCode and isOperatingOnline can not both be NULL!";
        }
		  
		if( StringUtils.isBlank(bizContent.bizVersion)){
			return "bizVersion should not be NULL!";
		}
		return Constants.VALIDATESUCCESS;
	}
	
	private String lengthValidate() {
		if( StringUtils.isNotBlank(bizContent.storeId) && bizContent.storeId.length() > 32 ){
			return ("storeId is big length , max 32");
		}
		
		if( StringUtils.isNotBlank(bizContent.categoryId) && bizContent.categoryId.length() > 32 ){
			return ("categoryId is big length , max 32");
		}
		
		if( StringUtils.isNotBlank(bizContent.mainShopName) && bizContent.mainShopName.length() > 20 ){
			return ("mainShopName is big length , max 20");
		}
		
		if( StringUtils.isNotBlank(bizContent.branchShopName) && bizContent.branchShopName.length() > 20 ){
			return ("branchShopName is big length , max 20");
		}
		
		if( StringUtils.isNotBlank(bizContent.address) && bizContent.address.length() > 50 ){
			return ("address is big length , max 50");
		}
		
		if( null != bizContent.longitude && (bizContent.longitude+"").length() > 15 ){
			return ("longitude is big length , max 15");
		}
		
		if( null != bizContent.latitude && (bizContent.latitude+"").length() > 15 ){
			return ("latitude is big length , max 15");
		}
		
		if( StringUtils.isNotBlank(bizContent.isvUid) && bizContent.isvUid.length() > 16 ){
			return ("isvUid is big length , max 16");
		}
		
		if( StringUtils.isNotBlank(bizContent.requestId) && bizContent.requestId.length() > 64){
			return ("requestId is big length , max 64");
		}
		
		if( StringUtils.isNotBlank(bizContent.opRole) && bizContent.opRole.length() > 16 ){
			return ("opRole is big length , max 16");
		}
		
		if( StringUtils.isNotBlank(bizContent.bizVersion) && bizContent.bizVersion.length() > 10 ){
			return ("bizVersion is big length , max 10");
		}
		return Constants.VALIDATESUCCESS;
	}
	
	private String ruleValidate() {
		
		if( StringUtils.isNotBlank(bizContent.wifi)){
			if( !"T".equals(bizContent.wifi) && !"F".equals(bizContent.wifi)){
				bizContent.wifi = "";
			}
		}
		
		if( StringUtils.isNotBlank(bizContent.parking)){
			if( !"T".equals(bizContent.parking) && !"F".equals(bizContent.parking)){
				bizContent.parking = "";
			}
		}
		
		if (StringUtils.isNotBlank(bizContent.avgPrice)) {
			if(NumberUtils.isNumber(bizContent.avgPrice)){
				Double avgPrice_ = Double.valueOf(bizContent.avgPrice);
				if( avgPrice_ < 1D || avgPrice_ > 99999D){
					return ("avgPrice data rule error !");
				}
			} else {
				return ("invalid avgPrice !");				
			}
        }
		
		if( StringUtils.isNotBlank(bizContent.noSmoking)){
			if( !"T".equals(bizContent.noSmoking) && !"F".equals(bizContent.noSmoking)){
				bizContent.noSmoking = "";
			}
		}
		
		if( StringUtils.isNotBlank(bizContent.box)){
			if( !"T".equals(bizContent.box) && !"F".equals(bizContent.box)){
				bizContent.box = "";
			}
		}
		
		if( StringUtils.isNotBlank(bizContent.requestId) ){
			if(!Pattern.matches("^[A-Za-z0-9]+$", bizContent.requestId)){
				return ("requestId data rule error !");
			}
	    }
		
		return Constants.VALIDATESUCCESS;
	}
}