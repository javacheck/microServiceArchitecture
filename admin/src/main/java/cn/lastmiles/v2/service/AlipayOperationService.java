/**
 * createDate : 2016年9月7日下午2:42:33
 */
package cn.lastmiles.v2.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;
import cn.lastmiles.v2.dao.AlipayOperationDao;

@Service
public class AlipayOperationService {
	@Autowired
	private AlipayOperationDao alipayOperationDao;
	
	public boolean getAuthRequestBody(String storeId,Long getAuthID, AlipayAuthTokenRequestBuilder builder){
		return alipayOperationDao.getAuthRequestBody(storeId,getAuthID,builder);
	}
	
	public boolean getAuthResponseBody(String storeId,Long getAuthID, AlipayOpenAuthTokenAppResponse responseBody){
		return alipayOperationDao.getAuthResponseBody(storeId,getAuthID,responseBody);
	}

	public void saveAsyncNotice(Long asyncID, Map<String, String> params) {
		alipayOperationDao.saveAsyncNotice(asyncID,params);
	}

	public List<Map<String, Object>> findAuthByStoreId(String storeId) {
		return alipayOperationDao.findAuthByStoreId(storeId);
	}
}
