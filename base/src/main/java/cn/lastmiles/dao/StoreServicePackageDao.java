package cn.lastmiles.dao;
/**
 * createDate : 2016年3月10日下午3:29:58
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.StoreServicePackage;
import cn.lastmiles.bean.StoreTerminal;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class StoreServicePackageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save(StoreServicePackage storeServicePackage) {
		JdbcUtils.save(storeServicePackage);
	}

	public int checkServicePackageName(Long storeId, String name) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_store_service_package ssp where ssp.name = ? ");
		parameters.add(name);
		
		if( null != storeId ){
			querySQL.append(" and ssp.storeId = ?");
			parameters.add(storeId);
		}
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}

	public List<StoreServicePackage> getServicePackageSelect(Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select * from t_store_service_package ssp where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and ssp.storeId = ?");
			parameters.add(storeId);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(StoreServicePackage.class, list);
		}
		return null;
	}

	public List<StoreServicePackage> findByStoreId(Long storeId) {
		List<Map<String, Object>> list  = jdbcTemplate.queryForList("select ssp.* from t_store_service_package ssp where ssp.storeId = ?", storeId);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(StoreServicePackage.class, list);			
		}
		return null;
	}

	public StoreServicePackage findById(Long id) {
		String sql = "select * from t_store_service_package where  id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(StoreServicePackage.class, list.get(0));
	}
}