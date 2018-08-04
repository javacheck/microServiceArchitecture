package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserStoreServicePackageRecord;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class UserStoreServicePackageRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<UserStoreServicePackageRecord> findCurrentRecordForPos(Long accountId,
			Date startDate){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_store_service_package_record where accountId = ? and createdTime >= ? and source = 1 and type = 1 and payStatus = 0 ", accountId,startDate);
		return BeanUtils.toList(UserStoreServicePackageRecord.class, list);
	}
	
	public UserStoreServicePackageRecord findLastRechargeRecordForPos(Long storeId , String mobile){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT usspr.*,ssp.`name` as servicePackageName,uc.cardNum as cardNum from t_user_store_service_package_record usspr " +  
				" INNER JOIN t_user_card uc on usspr.userCardId = uc.id " + 
				" INNER JOIN t_store_service_package ssp on ssp.id = usspr.storeServicePackageId " + 
				" where uc.storeId = ? and uc.mobile = ? and usspr.type = 1 and usspr.source = 1 order by id desc limit 1", storeId,mobile);
		
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserStoreServicePackageRecord.class, list.get(0));
		}
		
		return null;
	}

	public UserStoreServicePackageRecord findById(Long recordId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_store_service_package_record where id = ?", recordId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(UserStoreServicePackageRecord.class, list.get(0));
	}
}
