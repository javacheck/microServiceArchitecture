package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class UserstoreservicepackageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(UserstoreservicepackageDao.class);

	public List<UserStoreServicePackage> findByUserCardId(Long userCardId) {
		String sql = "select ussp.*,( SELECT if(sum(times) is null,0,sum(times)) FROM t_user_store_service_package_record usspr WHERE usspr.storeServicePackageId = ussp.storeServicePackageId and usspr.type = ?  and usspr.userCardId = ? ) as totalSaleTimes,(select ssp.name from t_store_service_package ssp where ssp.id = ussp.storeServicePackageId ) as 'storeServicePackage.name' from t_user_store_service_package ussp where ussp.userCardId = ? order by ussp.times desc";
		return BeanUtils.toList(UserStoreServicePackage.class, jdbcTemplate.queryForList(sql,Constants.UserCardRecord.TYPE_CONSUMER,userCardId,userCardId));
	}
	
	public List<UserStoreServicePackage> findByUserCardId(Long userCardId,Integer remainTimes) {
		logger.debug("findByUserCardId is {} , remainTimes is {}",userCardId,remainTimes);
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select ussp.* ");
		querySQL.append(",( SELECT sum(times) FROM t_user_store_service_package_record usspr WHERE usspr.storeServicePackageId = ussp.storeServicePackageId and usspr.type = ? ) as totalSaleTimes");
		querySQL.append(",(select ssp.name from t_store_service_package ssp where ssp.id = ussp.storeServicePackageId ) as 'storeServicePackage.name' ");
		querySQL.append("from t_user_store_service_package ussp where ussp.userCardId = ?");
		parameters.add(Constants.UserCardRecord.TYPE_CONSUMER);
		parameters.add(userCardId);
		if( null != remainTimes ){
			querySQL.append(" and ussp.times > ? ");
			parameters.add(remainTimes);
		}
		return BeanUtils.toList(UserStoreServicePackage.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray()));
	}
	
	public UserStoreServicePackage findByStoreId$UserCardId$storeServicePackageId(Long storeId,Long userCardId,Long storeServicePackageId){
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select * from t_user_store_service_package where 1=1 ");
		if( null != storeId ){
			querySQL.append(" and storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != userCardId ){
			querySQL.append(" and userCardId = ? ");
			parameters.add(userCardId);
		}
		
		if( null != storeServicePackageId ){
			querySQL.append(" and storeServicePackageId = ? ");
			parameters.add(storeServicePackageId);
		}
		
		List<Map<String, Object>> list =  jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(UserStoreServicePackage.class, list.get(0));
		}
		return null;
	}

	public void update(Long userCardId,Long storeServicePackageId, Integer times) {
		logger.debug("userCardId is {} , update---- times {}",userCardId,times);
		jdbcTemplate.update("update t_user_store_service_package set times = ? where userCardId = ? and storeServicePackageId = ? ", times,userCardId,storeServicePackageId);
	}
	
}
