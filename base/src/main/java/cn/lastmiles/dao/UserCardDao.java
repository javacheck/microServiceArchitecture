package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class UserCardDao {
	private final static Logger logger = LoggerFactory.getLogger(UserCardDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StoreDao storeDao;

	public List<UserCard> findByMobile(String mobile) {
		String sql = "select * from t_user_card where mobile = ? ";
		return BeanUtils.toList(UserCard.class, jdbcTemplate.queryForList(sql,mobile)) ;
	}
	public List<UserCard> findMyUserCard(String mobile,String stopName) {
		String sql = "select uc.* from t_user_card uc LEFT JOIN t_store s ON uc.storeId = s.id where uc.mobile = ? and uc.status = 1";
		if (StringUtils.isNotBlank(stopName)) {
			sql+=" AND s.`name` LIKE ? ";
			return BeanUtils.toList(UserCard.class, jdbcTemplate.queryForList(sql,mobile,"%"+stopName+"%")) ;
		}
		return BeanUtils.toList(UserCard.class, jdbcTemplate.queryForList(sql,mobile)) ;
	}
	
	public UserCard findByMobileAndStoreId(String mobile,Long storeId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user_card where mobile = ? and storeId= ?", mobile,storeId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(UserCard.class, list.get(0));
	}
	
	/**
	 * 更新积分、消费金额（包括同一连锁下所有的商家会员卡）
	 * @param mobile
	 * @param storeId
	 * @param point
	 * @param consumption
	 */
	public void updatePointAndLevel(String mobile,Long storeId,Double point,Double totalPoint,Long levelId,Long upgradedLevelId,Integer defType){
		String temp = "";
		if (defType != null && defType.intValue() == 0){//积分
			List<Store> list = storeDao.findAllStoreOnOneOrganizationTree(storeId);
			for (int i = 0; i < list.size(); i++){
				Store s = list.get(i);
				if (i != 0){
					temp += "," + s.getId();
				}else {
					temp += s.getId();
				}
			}
		}else {
			//消费
			temp = storeId.toString();
		}
		logger.debug("user card dao updatePointAndConsumptionAndLevel,mobile={},storeId= {},point = {},temp={},levelId = {}",
				 mobile, storeId, point,  temp,levelId);
		//要不要更新其他店的会员等级呢？TODO
		if (levelId == null){
			jdbcTemplate.update("update t_user_card set point = ?,totalPoint = ? where mobile = ? and storeId in ("+temp+")",
				point,totalPoint,mobile);
		}else {
			jdbcTemplate.update("update t_user_card set point = ?,totalPoint = ?,levelId = ?  where mobile = ? and storeId in ("+temp+")",
					point,totalPoint,levelId,mobile);
		}
		
		if (levelId == null){
			jdbcTemplate.update("update t_user_card set upgradedLevelId = ? where mobile = ? and storeId = ? ",
					upgradedLevelId,mobile,storeId);
		}else {
			jdbcTemplate.update("update t_user_card set levelId = ? where mobile = ? and storeId = ? ",
					levelId,mobile,storeId);
		}
		
	}
	
	/**
	 * 更新累计消费
	 * @param totalConsumption
	 * @param id
	 */
	public void updateTotalConsumptionById(Double totalConsumption,Long id){
		jdbcTemplate.update("update t_user_card set totalConsumption = ? where id = ?",totalConsumption,id);
	}
	
	public void updatePointAndConsumptionAndLevelById(Long id,Double point,Double totalPoint,Double consumption,String levelName,Double discount){
		jdbcTemplate.update("update t_user_card set point = ?,totalPoint = ? ,totalConsumption = ?,userLevelName = ?,userLevelDiscount=?  where id = ?",
				point,totalPoint,consumption,levelName,discount,id);
	}
	
	/**
	 * 更新积分
	 * @param mobile
	 * @param storeId
	 * @param point
	 * @param totalPoint
	 */
	public void updatePoint(String mobile,Long storeId,Double point,Double totalPoint){
		List<Store> list = storeDao.findAllStoreOnOneOrganizationTree(storeId);
		String temp = "";
		for (int i = 0; i < list.size(); i++){
			Store s = list.get(i);
			if (i != 0){
				temp += "," + s.getId();
			}else {
				temp += s.getId();
			}
		}
		
		jdbcTemplate.update("update t_user_card set point = ?,totalPoint = ?  where mobile = ? and storeId in ("+temp+")",
				point,totalPoint,mobile);
	}
	
	public void updateLevel(Long id,String levelName,Double discount){
		jdbcTemplate.update("update t_user_card set userLevelName = ?,userLevelDiscount=?  where id = ?",
				levelName,discount,id);
	}

	/**
	 * 根据用户id，商户id查询用户卡
	 * @param userId
	 * @param storeId
	 * @return
	 */
	public UserCard getByUserIdAndStoreId(Long userId,Long storeId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user_card where mobile = (select mobile from t_user where id = ?) and storeId = ?", userId,storeId);
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null;
	}
	
	/**
	 * 减余额
	 * @param mobile
	 * @param storeId
	 * @param balance
	 */
	public void updateBalance(String mobile,Long storeId,Double balance){
		jdbcTemplate.update("update t_user_card set balance = ? where mobile = ? and storeId = ?", balance,mobile,storeId);
	}
	public List<UserCard> findAllStoreId() {
		StringBuffer querySQL = new StringBuffer(" select DISTINCT(storeId) as storeId from t_user_card ");
		querySQL.append(" order by storeId ");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(UserCard.class, list);
		}
		return null;
	}
	public List<UserCard> findAllDate() {
		/*StringBuffer querySQL = new StringBuffer("select STR_TO_DATE(s.createdTime,'%Y-%m-%d') as createdTime from ( ");
		querySQL.append(" select distinct(uc.createdTime) as createdTime from  ");
		querySQL.append(" ( ");
		querySQL.append(" select  (date_format(createdTime,'%Y-%m-%d')) as createdTime  from t_user_card  ");
		querySQL.append(" ) uc ");
		querySQL.append(" order by createdTime desc ");
		querySQL.append(" ) s");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());*/
		String sql="SELECT DISTINCT date_format(createdTime, '%Y-%m-%d') FROM t_user_card ORDER BY date_format(createdTime, '%Y-%m-%d') DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(UserCard.class, list);
		}
		return null;
	}
	
	/**
	 * 查找这个商家下(包括分店)所有的会员卡
	 * @param storeId
	 * @return
	 */
	public List<UserCard> findByChainStoreId(Long storeId){
		List<Store> storeList = storeDao.findAllChildrenStore(storeId);
		if (storeList.isEmpty()){
			return null;
		}else {
			StringBuilder ids = new StringBuilder();
			for (Store s : storeList){
				if (ids.length() == 0){
					ids.append(s.getId());
				}else {
					ids.append("," + s.getId());
				}
			}
			return BeanUtils.toList(UserCard.class, jdbcTemplate.queryForList("select * from t_user_card where storeId in ("+ids+")"));
		}
	}
	
	public void updateRemindedSms(Long id, int remindedSms) {
		jdbcTemplate.update("update t_user_card set remindedSms = ? where id = ?", remindedSms,id);
	}
	public String findFirstDateByStoreId(Long storeId) {
		return jdbcTemplate
				.queryForObject(
						"SELECT DISTINCT date_format(createdTime, '%Y-%m-%d') FROM t_user_card where storeId=? ORDER BY date_format(createdTime, '%Y-%m-%d')   limit 1",
						String.class,storeId);
	}
}
