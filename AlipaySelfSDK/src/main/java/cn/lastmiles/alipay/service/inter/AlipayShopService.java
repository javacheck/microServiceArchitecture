/**
 * createDate : 2016年7月25日下午3:50:57
 * 口碑店铺
 */
package cn.lastmiles.alipay.service.inter;

import cn.lastmiles.alipay.model.builder.monitor.AlipayBusinessWaterBatchQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.monitor.AlipayQuerySignedCertificationRequestBuilder;
import cn.lastmiles.alipay.model.builder.shop.AlipayCreateShopInfoRequestBuilder;
import cn.lastmiles.alipay.model.builder.shop.AlipayStoresImageUploadRequestBuilder;
import cn.lastmiles.alipay.model.result.monitor.AlipayBusinessWaterBatchQueryResult;
import cn.lastmiles.alipay.model.result.monitor.AlipayQuerySignedCertificationResult;
import cn.lastmiles.alipay.model.result.shop.AlipayCreateShopInfoResult;
import cn.lastmiles.alipay.model.result.shop.AlipayStoresImageUploadResult;

public interface AlipayShopService {
	 /**
	  * *********************************************************************
	  @ 上传门店照片接口
	  * 接口地址：alipay.offline.material.image.upload
	  * *********************************************************************
	  * 系统商需要先将商户需要使用的图片，上传支付宝服务器,生成对应的图片ID后才能够在口碑平台上配置相应图片
	  */
	 public AlipayStoresImageUploadResult storesImageUpload(AlipayStoresImageUploadRequestBuilder builder);
	 
	 /**
	  * 查询签约、认证状态
	  */
	 public AlipayQuerySignedCertificationResult querySignedCertification(AlipayQuerySignedCertificationRequestBuilder builder);
	 
	 /**
	  * *********************************************************************
	  @ 创建门店信息
	  * 接口地址：alipay.offline.market.shop.create
	  * *********************************************************************
	  * 系统商需要通过该接口在口碑平台帮助商户创建门店信息。
	  */
	 public AlipayCreateShopInfoResult createShopInfo(AlipayCreateShopInfoRequestBuilder builder);
	 
	 /**
	  * 业务流水批量查询接口
	  */
	 public AlipayBusinessWaterBatchQueryResult businessWaterBatchQuery(AlipayBusinessWaterBatchQueryRequestBuilder builder);
}