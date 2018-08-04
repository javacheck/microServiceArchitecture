/**
 * createDate : 2016年7月25日下午4:48:00
 * 口碑店铺
 */
package cn.lastmiles.alipay.service.abs;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOfflineMarketShopCreateRequest;
import com.alipay.api.request.AlipayOfflineMaterialImageUploadRequest;
import com.alipay.api.response.AlipayOfflineMarketShopCreateResponse;
import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;
import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.monitor.AlipayBusinessWaterBatchQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.monitor.AlipayQuerySignedCertificationRequestBuilder;
import cn.lastmiles.alipay.model.builder.shop.AlipayCreateShopInfoRequestBuilder;
import cn.lastmiles.alipay.model.builder.shop.AlipayStoresImageUploadRequestBuilder;
import cn.lastmiles.alipay.model.result.monitor.AlipayBusinessWaterBatchQueryResult;
import cn.lastmiles.alipay.model.result.monitor.AlipayQuerySignedCertificationResult;
import cn.lastmiles.alipay.model.result.shop.AlipayCreateShopInfoResult;
import cn.lastmiles.alipay.model.result.shop.AlipayStoresImageUploadResult;
import cn.lastmiles.alipay.model.status.shop.CreateStatus;
import cn.lastmiles.alipay.model.status.shop.UploadStatus;
import cn.lastmiles.alipay.service.inter.AlipayShopService;

public class AbsAlipayShopService extends AbsAlipayService implements AlipayShopService{
	protected AlipayClient client;
	
	@Override
	public AlipayStoresImageUploadResult storesImageUpload(AlipayStoresImageUploadRequestBuilder builder) {
		logger.info("storesImageUpload --->> builder: {}",builder);
		
		String validate = validateBuilder(builder);
		if(!Constants.VALIDATESUCCESS.equals(validate)){
			return new AlipayStoresImageUploadResult(validate);
		}
		 
		 AlipayOfflineMaterialImageUploadRequest request = new AlipayOfflineMaterialImageUploadRequest();
		 
		 request.setImageName(builder.getImageName());
		 request.setImageType(builder.getImageType());
		 request.setImageContent(builder.getImageContent());
		 
		 AlipayOfflineMaterialImageUploadResponse response = (AlipayOfflineMaterialImageUploadResponse) getResponse(client, request);
		 
		 logger.info("storesImageUpload --->> response: {} ",response.toString());
		 
		 AlipayStoresImageUploadResult result = new AlipayStoresImageUploadResult(response);
		 
		 if (response != null && Constants.SUCCESS.equals(response.getCode())) {
	         result.setUploadStatus(UploadStatus.SUCCESS);
	     } 
		 
        return result;
	}

	@Override
	public AlipayQuerySignedCertificationResult querySignedCertification(AlipayQuerySignedCertificationRequestBuilder builder) {
		return null;
	}

	@Override
	public AlipayCreateShopInfoResult createShopInfo(AlipayCreateShopInfoRequestBuilder builder) {
		logger.info("createShopInfo --->> builder: {}",builder);
		
		String validate = validateBuilder(builder);
		if(!Constants.VALIDATESUCCESS.equals(validate)){
			return new AlipayCreateShopInfoResult(validate);
		}
		 
		AlipayOfflineMarketShopCreateRequest request = new AlipayOfflineMarketShopCreateRequest();
		request.setBizContent(builder.toJsonString());
		 
		logger.info("createShopInfo --->> bizContent: {}",request.getBizContent());
		 
		AlipayOfflineMarketShopCreateResponse response = (AlipayOfflineMarketShopCreateResponse) getResponse(client, request);
		
		logger.info("createShopInfo --->> response: {} ",response.toString());
		
		AlipayCreateShopInfoResult result = new AlipayCreateShopInfoResult(response);
		 
        if ( null != response && Constants.SUCCESS.equals(response.getCode())) {
            result.setCreateStatus(CreateStatus.SUCCESS);
        } 
        return result;
	}

	@Override
	public AlipayBusinessWaterBatchQueryResult businessWaterBatchQuery(AlipayBusinessWaterBatchQueryRequestBuilder builder) {
		return null;
	}

}