/**
 * createDate : 2016年7月25日下午2:57:40
 * 口碑店铺
 */
package cn.lastmiles.alipay.external;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.FileItem;
import com.alipay.api.internal.util.AlipaySignature;

import cn.lastmiles.alipay.bean.CreateShopInfo;
import cn.lastmiles.alipay.config.Configs;
import cn.lastmiles.alipay.model.builder.shop.AlipayCreateShopInfoRequestBuilder;
import cn.lastmiles.alipay.model.builder.shop.AlipayStoresImageUploadRequestBuilder;
import cn.lastmiles.alipay.model.result.shop.AlipayCreateShopInfoResult;
import cn.lastmiles.alipay.model.result.shop.AlipayStoresImageUploadResult;
import cn.lastmiles.alipay.service.abs.AbsAlipayShopService;
import cn.lastmiles.alipay.service.impl.AlipayShopServiceImpl;
import cn.lastmiles.alipay.utils.Utils;

public class AlipayShopInvoke {

	private static AbsAlipayShopService shopService = new AlipayShopServiceImpl.ClientBuilder().build();
	
	public Map<String,Object> storesImageUpload(String appAuthToken,String imageName,File file){
		Map<String,Object> returnMap = new HashMap<String,Object>();

		if( null == file || !file.exists()){
			returnMap.put("errorParameters","file should not be exists!");
			return returnMap;
		}
		if( !Utils.isImage(file) ){
			returnMap.put("errorParameters","file should not be picFile!");
			return returnMap;
		}
		
		String fileName = file.getName();
		String imageType = fileName.substring(fileName.lastIndexOf(".")+1);
		
		AlipayStoresImageUploadRequestBuilder builder = new AlipayStoresImageUploadRequestBuilder();
		builder.setAppAuthToken(appAuthToken);
		builder.setImageName(imageName);
		builder.setImageType(imageType);
		FileItem imageContent = new FileItem(file);
		builder.setImageContent(imageContent);
		
		AlipayStoresImageUploadResult result = shopService.storesImageUpload(builder);
		
		if( null == result.getParametersError()){
			returnMap.put("requestParameters", builder);
			returnMap.put("responseParameters",result);			
		} else {
			returnMap.put("errorParameters",result.getParametersError());
		}
	    return returnMap;
	}
	
	public Map<String,Object> createShopInfo(String appAuthToken,CreateShopInfo createShopInfo){
		
		AlipayCreateShopInfoRequestBuilder builder = new AlipayCreateShopInfoRequestBuilder();
		builder.setStoreId(createShopInfo.getStoreId());
		builder.setCategoryId(createShopInfo.getCategoryId());
		builder.setBrandName(createShopInfo.getBrandName());
		builder.setBrandLogo(createShopInfo.getBrandLogo());
		builder.setMainShopName(createShopInfo.getMainShopName());
		builder.setBranchShopName(createShopInfo.getBranchShopName());
		builder.setProvinceCode(createShopInfo.getProvinceCode());
		builder.setCityCode(createShopInfo.getCityCode());
		builder.setDistrictCode(createShopInfo.getDistrictCode());
		builder.setAddress(createShopInfo.getAddress());
		builder.setLongitude(createShopInfo.getLongitude());
		builder.setLatitude(createShopInfo.getLatitude());
		builder.setContactNumber(createShopInfo.getContactNumber());
		builder.setNotifyMobile(createShopInfo.getNotifyMobile());
		builder.setMainImage(createShopInfo.getMainImage());
		builder.setAuditImage(createShopInfo.getAuditImage());
		builder.setBusinessTime(createShopInfo.getBusinessTime());
		builder.setWifi(createShopInfo.getWifi());
		builder.setParking(createShopInfo.getParking());
		builder.setValueAdded(createShopInfo.getValueAdded());
		builder.setAvgPrice(createShopInfo.getAvgPrice());
		
		builder.setIsvUid(createShopInfo.getIsvUid());
		builder.setLicence(createShopInfo.getLicence());
		builder.setLicenceCode(createShopInfo.getLicenceCode());
		builder.setLicenceName(createShopInfo.getLicenceName());
		builder.setBusinessCertificate(createShopInfo.getBusinessCertificate());
		builder.setBusinessCertificteExpires(createShopInfo.getBusinessCertificteExpires());
		builder.setAuthLetter(createShopInfo.getAuthLetter());
		builder.setIsOperatingOnline(createShopInfo.getIsOperatingOnline());
		builder.setOnlineUrl(createShopInfo.getOnlineUrl());
		builder.setOperateNotifyUrl(createShopInfo.getOperateNotifyUrl());
		builder.setImplementId(createShopInfo.getImplementId());
		builder.setNoSmoking(createShopInfo.getNoSmoking());
		builder.setBox(createShopInfo.getBox());
		builder.setRequestId(createShopInfo.getRequestId());
		builder.setOtherAuthorization(createShopInfo.getOtherAuthorization());
		builder.setLicenceExpires(createShopInfo.getLicenceExpires());
		builder.setOpRole(createShopInfo.getOpRole());
		builder.setBizVersion(createShopInfo.getBizVersion());
		builder.setAppAuthToken(appAuthToken);
		
		AlipayCreateShopInfoResult result = shopService.createShopInfo(builder);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		if( null == result.getParametersError()){
			returnMap.put("requestParameters", builder);
			returnMap.put("responseParameters",result);			
		} else {
			returnMap.put("errorParameters",result.getParametersError());
		}
	    return returnMap;
	}
	
	// 验签
	public boolean rsaCheckV2(Map<String,String> params){
		boolean flag = false;
		try {
			flag = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), Configs.charset);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static void main(String[] args) {
		AlipayShopInvoke asi = new AlipayShopInvoke();
		// 上传门店信息
//		Map<String,Object> upLoad = asi.storesImageUpload("图片测试", new File("F:/picture/39_160426110638_1.jpg"));
//		System.out.println("上传门店信息：错误提示：" + upLoad.get("errorParameters") + "---> 请求参数：" + upLoad.get("requestParameters") + "---> 响应参数：" + upLoad.get("responseParameters"));
		
//		createShopInfoTest();
//		
//		AlipayShopInvoke acf = new AlipayShopInvoke();
//		acf.createShopInfo(c); 
	}

	public static void createShopInfoTest() {
		CreateShopInfo c = new CreateShopInfo();
		c.setStoreId("2000920");
		c.setCategoryId("2015091000058486");
		c.setMainShopName("莱麦便利店");
		c.setProvinceCode("440000");
		c.setCityCode("440101");
		c.setDistrictCode("440106");
		c.setAddress("中山大道建工路15号3-6层503房");
		c.setLongitude(113.375559);
		c.setLatitude(23.155511);
		c.setIsOperatingOnline("F");
		c.setContactNumber("020-28876503");
		c.setMainImage("5WNa4hXKTSC9GuSRhDL7PQAAACMAAQED");
		c.setIsvUid("2088021174383713");
		c.setBizVersion("2.0");
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		c.setRequestId("RK"+sdf.format(new Date()));
		c.setAuditImage("5WNa4hXKTSC9GuSRhDL7PQAAACMAAQED,5WNa4hXKTSC9GuSRhDL7PQAAACMAAQED,5WNa4hXKTSC9GuSRhDL7PQAAACMAAQED");
		c.setLicence("VaGKTpTwQAekjezvxJ5ueQAAACMAAQED");
		c.setLicenceCode("S0612015073445 (1-1)");
		c.setLicenceName("广州市莱麦互联网科技有限公司");
		c.setBusinessCertificate("VaGKTpTwQAekjezvxJ5ueQAAACMAAQED");
	}
 	
}