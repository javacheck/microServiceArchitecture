package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AlipayAuthSituation;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

/**
 * 2016/10/13
 * @author shaoyikun
 *
 */
@Repository
public class AlipayAuthSituationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page getResult(String storeId,Long code, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_alipay_auth_get_request qu inner join t_alipay_auth_get_response sp where qu.getAuthID = sp.getAuthID ");

		if (StringUtils.isNotBlank(storeId)) {
			querySQL.append(" and qu.storeId in("+storeId+") ");
		}

		if( null != code ){
			querySQL.append(" and sp.code = ? ");
			parameters.add(code);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(0, "select qu.getAuthID,sp.user_id,qu.grant_type,qu.createTime,sp.code,(select s.name from t_store s where s.id = qu.storeId) storeName ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString() + " order by qu.storeId ,qu.createTime desc limit ?,?", parameters.toArray());

		page.setData(BeanUtils.toList(AlipayAuthSituation.class, list));

		return page;
	}

	public AlipayAuthSituation findDetailByAuthID(Long getAuthID) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select qu.*,sp.*,(select s.name from t_store s where s.id = qu.storeId) storeName from t_alipay_auth_get_request qu inner join t_alipay_auth_get_response sp where qu.getAuthID = sp.getAuthID  and qu.getAuthID = ? ",getAuthID);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(AlipayAuthSituation.class, list.get(0));
		}
		return null;
	}
}