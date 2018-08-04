package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Partner;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

/**
 * createDate : 2015年12月21日下午4:24:16
 */
@Repository
public class PartnerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page list(String name,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_partner p where 1=1 ");
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and instr(name,?) > 0 ");
			parameters.add(name);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString() , Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.append(" order by p.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());		
		
		querySQL.insert(0,"select p.*,(select name from t_store s where s.id = p.storeId) as storeName ");
		
		page.setData(BeanUtils.toList(Partner.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray())));
		return page;
	}

	public Partner findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.*,(select name from t_store s where s.id = p.storeId) as storeName from t_partner p where p.id = ? ", id);
		return list.isEmpty() ? null : BeanUtils.toBean(Partner.class, list.get(0));
	}

	public boolean save(Partner partner) {
		int temp = jdbcTemplate.update("insert into t_partner(id,appKey,name,storeId,token) values (? ,? ,?,?,?)", partner.getId(),partner.getAppKey(),partner.getName(),partner.getStoreId(),partner.getToken());
		return temp > 0 ? true : false ;
	}

	public boolean update(Partner partner) {
		int temp = jdbcTemplate.update("update t_partner set name = ?,storeId = ?,token = ? where id = ?", partner.getName(),partner.getStoreId(),partner.getToken(),partner.getId());
		return temp > 0 ? true : false ;
	}

	public int delete(Long id) {
		return jdbcTemplate.update("delete from t_partner where id = ?", id);
	}

	public int checkPartnerName(Long id, String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_partner p where p.name = ? ");
		parameters.add(name);
		
		if( null != id ){
			querySQL.append(" and p.id <> ?");
			parameters.add(id);
		}
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}

	public boolean findStoreById(Long id) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(id);
		return jdbcTemplate.queryForObject("select count(*) from t_store s where s.partnerId = ? ", Integer.class,parameters.toArray()) > 0 ? true : false;
	}

	public int checkPartnerToken(Long id, String token) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_partner p where p.token = ? ");
		parameters.add(token);
		
		if( null != id ){
			querySQL.append(" and p.id <> ?");
			parameters.add(id);
		}
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}

	public Partner findByToken(String token) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.* from t_partner p where p.token = ? ", token);
		
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(Partner.class, list.get(0));
		}
		return null;
	}
	
	public Partner findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.* from t_partner p where p.storeId = ? ", storeId);
		
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(Partner.class, list.get(0));
		}
		return null;
	}
}