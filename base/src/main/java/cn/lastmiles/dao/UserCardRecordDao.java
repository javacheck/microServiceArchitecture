package cn.lastmiles.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * createDate : 2016年3月14日上午11:20:59
 */
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;
@Repository
public class UserCardRecordDao {
	private final static Logger logger = LoggerFactory
			.getLogger(UserCardRecordDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void updateStatusSuccess(Long id ){
		jdbcTemplate.update("update t_user_card_record set payStatus = 0 where id = ? ", id);
	}
	public void updateServiceStatusSuccess(Long id ){
		jdbcTemplate.update("update t_user_store_service_package_record set payStatus = 0 where id = ? ", id);
	}
	
	public UserCardRecord findByOrderId(Long orderId){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card_record where orderId = ?", orderId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(UserCardRecord.class, list.get(0));
	}

	public void save(UserCardRecord userCardRecord) {
		JdbcUtils.save(userCardRecord);
	}

	public Page appList(String userCardId, String startTime, String endTime,Integer type, Page page) {
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_user_card_record ucr where 1 = 1 ");
		if (StringUtils.isNotBlank(userCardId)) {
			sql.append(" and ucr.userCardId= ? ");
			parameters.add(userCardId);
		}
		if (StringUtils.isNotBlank(startTime)) {
			sql.append(" and ucr.createdTime >= ?");
			parameters.add(startTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and ucr.createdTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		if (type!=null) {
			sql.append(" and ucr.type = ? ");
			parameters.add(type);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total.intValue() == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT ucr.* "
								+ ",(SELECT a.mobile   FROM t_account a  WHERE a.id=ucr.accountId ) as accountMobile "
								+ sql + " order by createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(UserCardRecord.class, list));

		return page;
	}
	
	public List<UserCardRecord> findCurrentRecordForPos(Long accountId,Date startDate){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card_record where accountId = ? and createdTime >= ? and source = 1 and type = 1 and payStatus = 0 ", accountId,startDate);
		return BeanUtils.toList(UserCardRecord.class, list);
	}
	
	/**
	 * 最近充值的记录
	 * @param storeId
	 * @param mobile
	 * @return
	 */
	public UserCardRecord findLastRechargeRecordForPos(Long storeId , String mobile){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT ucr.*,uc.cardNum as cardNum from t_user_card_record ucr " + 
				" INNER JOIN t_user_card uc on ucr.userCardId = uc.id " + 
				" where uc.storeId = ? and uc.mobile = ? and ucr.type = 1 and ucr.source = 1 order by id desc limit 1;", storeId,mobile);
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserCardRecord.class, list.get(0));
		}
		return null;
	}

	public UserCardRecord findById(Long recordId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card_record where id = ?", recordId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(UserCardRecord.class, list.get(0));
	}

	public List<UserCard> findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId = ?", storeId);
		
		return BeanUtils.toList(UserCard.class, list);
	}

	public List<UserCardRecord> findByUserCardId(Long userCardId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card_record where userCardId = ?", userCardId);
		
		return BeanUtils.toList(UserCardRecord.class, list);
	}
}
