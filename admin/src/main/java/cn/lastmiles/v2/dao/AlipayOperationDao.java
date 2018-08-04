/**
 * createDate : 2016年9月7日下午2:42:55
 */
package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

@Repository
public class AlipayOperationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public boolean getAuthResponseBody(String storeId,Long getAuthID, AlipayOpenAuthTokenAppResponse responseBody) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer insertSQL = new StringBuffer("insert into t_alipay_auth_get_response(");
		insertSQL.append("storeId,getAuthID,code,msg,sub_code,sub_msg,app_auth_token,app_refresh_token,auth_app_id,expires_in,re_expires_in,user_id,createtime) ");
		insertSQL.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		parameters.add(storeId);
		parameters.add(getAuthID);
		parameters.add(responseBody.getCode());
		parameters.add(responseBody.getMsg());
		parameters.add(responseBody.getSubCode());
		parameters.add(responseBody.getSubMsg());
		parameters.add(responseBody.getAppAuthToken());
		parameters.add(responseBody.getAppRefreshToken());
		parameters.add(responseBody.getAuthAppId());
		parameters.add(responseBody.getExpiresIn());
		parameters.add(responseBody.getReExpiresIn());
		parameters.add(responseBody.getUserId());
		parameters.add(new Date());
		return jdbcTemplate.update(insertSQL.toString(),parameters.toArray()) > 0 ? true : false;
	}

	public boolean getAuthRequestBody(String storeId,Long getAuthID, AlipayAuthTokenRequestBuilder builder) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer insertSQL = new StringBuffer("insert into t_alipay_auth_get_request(");
		insertSQL.append("storeId,getAuthID,grant_type,code,refresh_token,createTime) values (?,?,?,?,?,?)");
		
		parameters.add(storeId);
		parameters.add(getAuthID);
		parameters.add(builder.getGrantType());
		parameters.add(builder.getCode());
		parameters.add(builder.getRefreshToken());
		parameters.add(new Date());
		return jdbcTemplate.update(insertSQL.toString(),parameters.toArray()) > 0 ? true : false;
	}

	public void saveAsyncNotice(Long asyncID, Map<String, String> params) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer insertSQL = new StringBuffer("insert into t_alipay_async_notice(");
		insertSQL.append("id,request_id,biz_type,notify_id,notify_time,notify_type,apply_id,audit_status,is_online,is_show,version,sign_type,sign,shop_id,result_code,result_desc,createTime) ");
		insertSQL.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		parameters.add(asyncID);
		parameters.add(params.get("request_id"));
		parameters.add(params.get("biz_type"));
		parameters.add(params.get("notify_id"));
		parameters.add(params.get("notify_time"));
		parameters.add(params.get("notify_type"));
		parameters.add(params.get("apply_id"));
		parameters.add(params.get("audit_status"));
		parameters.add(params.get("is_online"));
		parameters.add(params.get("is_show"));
		parameters.add(params.get("version"));
		parameters.add(params.get("sign_type"));
		parameters.add(params.get("sign"));
		parameters.add(params.get("shop_id"));
		parameters.add(params.get("result_code"));
		parameters.add(params.get("result_desc"));
		parameters.add(new Date());
		
		jdbcTemplate.update(insertSQL.toString(),parameters.toArray());
	}

	public List<Map<String, Object>> findAuthByStoreId(String storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_alipay_auth_get_response where storeId = ? and code = 10000  ",storeId);
		return list;
	}

}