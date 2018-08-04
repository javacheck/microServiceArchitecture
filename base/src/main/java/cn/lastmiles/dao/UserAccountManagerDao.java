package cn.lastmiles.dao;
/**
 * createDate : 2016年3月9日上午11:08:45
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.bean.UserStoreServicePackageRecord;
import cn.lastmiles.bean.UserUpdateMobileRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.UserService;

@Repository
public class UserAccountManagerDao {
	private final static Logger logger = LoggerFactory.getLogger(UserAccountManagerDao.class);
	@Autowired
	private UserstoreservicepackageDao userstoreservicepackageDao;
	@Autowired
	private IdService idService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserCardDao userCardDao;
	
	public Page list(String mobile, String cardNum, String storeIdString, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_card uc where 1 = 1 ");
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and uc.mobile = ? ");
			parameters.add(mobile);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(cardNum);
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and uc.storeId in (" + storeIdString + ")");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(),Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0, " select uc.*, (select a.mobile from t_account a where a.id = uc.accountId) as accountName,(select s.name from t_store s where s.id = uc.storeId) as storeName,(select uld.name from t_user_level_definition uld where uld.id = uc.levelId) as userLevelName , (select uld.discount from t_user_level_definition uld where uld.id = uc.levelId) as userLevelDiscount ");
		querySQL.append(" order by uc.id desc limit ?,?");
		logger.debug("querySQL = {}",querySQL);
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserCard> userCardList = BeanUtils.toList(UserCard.class, list);
		for (UserCard userCard : userCardList) {
			StringBuffer serviceNameArray = new StringBuffer();
			List<UserStoreServicePackage> userStoreServicePackageList = userstoreservicepackageDao.findByUserCardId(userCard.getId());
//			userCard.setPoint( (userService.searchOrganization_UserByMobile$StoreId(userCard.getMobile(),userCard.getStoreId() )).getTotalPoint() );
			boolean flag = false;
			for (UserStoreServicePackage userStoreServicePackage : userStoreServicePackageList) {
				if( ObjectUtils.equals(userStoreServicePackage.getTimes(), 0) ){ // 列表不显示已经用完的服务套餐内容
					continue;
				}
				if(flag){
					serviceNameArray.append("  /  ");
				}
				flag = true;
				serviceNameArray.append(userStoreServicePackage.getStoreServicePackage().getName());
			}
			userCard.setServiceNameArray(serviceNameArray.toString());
		}
		page.setData(userCardList);

		return page;
	}
	
	
	public Page list(String mobile, String cardNum, String storeIdString,Integer mode, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_card uc where status = ? ");
		parameters.add(Constants.UserCard.STATUS_ON);
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and uc.mobile = ? ");
			parameters.add(mobile);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(cardNum);
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and uc.storeId in (" + storeIdString + ")");
		}
		
		if( null == mode ){
			querySQL.append(" and uc.upgradedLevelId is not null ");
			// 因新增会员卡时可以选择会员等级，会存在已升级而升级列表仍然有升级数据列表的问题，现修改为数据库中升级标识只有两者不相同时才出现升级列表数据
			// 2016-08-22 
			querySQL.append(" and uc.upgradedLevelId <> uc.levelId ");
		}
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(),Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0, " select uc.*, (select a.mobile from t_account a where a.id = uc.accountId) as accountName,(select s.name from t_store s where s.id = uc.storeId) as storeName,(select uld.name from t_user_level_definition uld where uld.id = uc.levelId) as userLevelName ,(select uld.discount from t_user_level_definition uld where uld.id = uc.levelId) as userLevelDiscount ");
		querySQL.append(" order by uc.createdTime desc limit ?,?");
		logger.debug("querySQL = {}",querySQL);
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserCard> userCardList = BeanUtils.toList(UserCard.class, list);
		for (UserCard userCard : userCardList) {
			StringBuffer serviceNameArray = new StringBuffer();
			List<UserStoreServicePackage> userStoreServicePackageList = userstoreservicepackageDao.findByUserCardId(userCard.getId());
//			userCard.setPoint( (userService.searchOrganization_UserByMobile$StoreId(userCard.getMobile(),userCard.getStoreId() )).getTotalPoint() );
			boolean flag = false;
			for (UserStoreServicePackage userStoreServicePackage : userStoreServicePackageList) {
				if( ObjectUtils.equals(userStoreServicePackage.getTimes(), 0) ){ // 列表不显示已经用完的服务套餐内容
					continue;
				}
				if(flag){
					serviceNameArray.append("  /  ");
				}
				flag = true;
				serviceNameArray.append(userStoreServicePackage.getStoreServicePackage().getName());
			}
			userCard.setServiceNameArray(serviceNameArray.toString());
		}
		page.setData(userCardList);

		return page;
	}
	
	public int checkCardNumBystoreId(Long id , Long storeId, String cardNum) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_user_card uc where 1=1 ");
		if( null != id ){
			querySQL.append(" and uc.id <> ? ");
			parameters.add(id);
		}
		if( null != storeId ){
			querySQL.append(" and uc.storeId = ?");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ?");
			parameters.add(cardNum);
		}
		
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}
	
	public int checkMobileBystoreId(Long id , Long storeId, String mobile,Integer status) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_user_card uc where 1=1 ");
		
		if( null != status ){
			querySQL.append(" and uc.status = ? ");
			parameters.add(status);
		}
		if( null != id ){
			querySQL.append(" and uc.id <> ? ");
			parameters.add(id);
		}
		if( null != storeId ){
			querySQL.append(" and uc.storeId = ?");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and uc.mobile = ?");
			parameters.add(mobile);
		}
		
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}

	private Map<String, Object> getCommonUserCardSaveField(UserCard userCard) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", userCard.getId());
		map.put("cardNum", userCard.getCardNum());
		map.put("mobile", userCard.getMobile());
		map.put("memo", userCard.getMemo());
		map.put("createdTime", new Date());
		map.put("storeId", userCard.getStoreId());
		map.put("accountId", userCard.getAccountId());
		map.put("status", userCard.getStatus());
		map.put("name", userCard.getName());
		map.put("password", userCard.getPassword());
		map.put("userLevelName", userCard.getUserLevelName());
		map.put("userLevelDiscount", userCard.getUserLevelDiscount());
		
		if( null != userCard.getLevelId() ){
			map.put("levelId", userCard.getLevelId());			
		}
		return map;
	}

	public void save_stored_value(UserCard userCard) {
		Map<String, Object> map = getCommonUserCardSaveField(userCard);
		if( userCard.getPayWay() != 0 ){
			map.put("balance", 0D);
		} else {
			map.put("balance", userCard.getBalance());			
		}
		JdbcUtils.save("t_user_card", map);
	}

	public void save_service_Package(UserCard userCard) {
		Map<String, Object> map = getCommonUserCardSaveField(userCard);
		JdbcUtils.save("t_user_card", map);
		
		Map<String, Object> map_service_record = new HashMap<String, Object>();
		map_service_record.put("userCardId", userCard.getId());
		map_service_record.put("storeServicePackageId", userCard.getUserStoreServicePackage().getStoreServicePackageId());
		map_service_record.put("accountId", userCard.getAccountId());
		map_service_record.put("times", userCard.getUserStoreServicePackage().getTimes());
		map_service_record.put("createdTime", new Date());
		map_service_record.put("remainTimes",userCard.getUserStoreServicePackage().getTimes() );
		map_service_record.put("beforeTimes",0 );
		map_service_record.put("type", Constants.UserCardRecord.TYPE_RECHARGE);
		map_service_record.put("mobile", userCard.getMobile());
		map_service_record.put("realityPrice",userCard.getRealityPrice() );
		map_service_record.put("source", userCard.getSource());
		map_service_record.put("id",idService.getId());
		
		map_service_record.put("payWay", userCard.getPayWay());
		
		if( userCard.getPayWay() == 0 ){ // 支付成功
			map_service_record.put("payStatus", 0);
		} else {
			map_service_record.put("payStatus", 1);
		}
		
		JdbcUtils.save("t_user_store_service_package_record", map_service_record);
		
		Map<String, Object> map_service = new HashMap<String, Object>();
		map_service.put("userCardId", userCard.getId());
		map_service.put("storeId", userCard.getStoreId());
		map_service.put("storeServicePackageId", userCard.getUserStoreServicePackage().getStoreServicePackageId());
		map_service.put("createdTime", new Date());
		
		if( userCard.getPayWay() == 0 ){
			map_service.put("times", userCard.getUserStoreServicePackage().getTimes());			
		}else {
			map_service.put("times", 0);
		}
		
		
		JdbcUtils.save("t_user_store_service_package", map_service);
	}

	public void save_discount(UserCard userCard) {
		Map<String, Object> map = getCommonUserCardSaveField(userCard);
		map.put("discountType", userCard.getDiscountType());
		
		if(userCard.getDiscountType().intValue() == 2 ){
			map.put("discount", userCard.getDiscount());
		}
		JdbcUtils.save("t_user_card", map);
	}

	public UserCard findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select uc.*,(select s.name from t_store s where s.id = uc.storeId) as storeName,(select uld.name from t_user_level_definition uld where uld.id = uc.levelId) as userLevelName,(select uld.discount from t_user_level_definition uld where uld.id = uc.levelId) as userLevelDiscount  from t_user_card uc where uc.id = ?", id);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null;
	}
	
	public void update_stored_value(UserCard userCard) {
		UserCard uc = findById(userCard.getId());
		Map<String,Object> updateMap = new HashMap<String, Object>();
		Double balance = 0D;
		if( userCard.getPayWay() == 0 ){
			balance = NumberUtils.add(ObjectUtils.equals(0D, uc.getBalance()) ? 0D : uc.getBalance(), userCard.getBalance());
			updateMap.put("balance", balance);						
		} else {
			balance = uc.getBalance();
			updateMap.put("balance", balance);
		}
		
		updateMap.put("memo", userCard.getMemo());
		if( !ObjectUtils.equals(uc.getStatus(),Constants.UserCard.STATUS_LOSS) ){
			updateMap.put("status", userCard.getStatus());			
		}
		
		Map<String,Object> whereAndMap = new HashMap<String, Object>();
		whereAndMap.put("id", userCard.getId());
		JdbcUtils.updateForAnd("t_user_card", updateMap, whereAndMap);
		
		if (balance > 50){
			userCardDao.updateRemindedSms(userCard.getId(), 0);
		}
	}
	
	public int update_service_Package(UserCard userCard,boolean isSale) {
		logger.debug("update_service_Package --- >> userCard is {},isSale is {}",userCard,isSale);
		Long storeId = userCard.getStoreId();
		Long userCardId = userCard.getId();
		Long storeServicePackageId = userCard.getUserStoreServicePackage().getStoreServicePackageId();
		
		UserStoreServicePackage userStoreServicePackage = userstoreservicepackageDao.findByStoreId$UserCardId$storeServicePackageId(storeId, userCardId, storeServicePackageId);
		if(isSale){
			if( null != userStoreServicePackage.getTimes() ){
				if( userCard.getUserStoreServicePackage().getTimes().intValue() - userStoreServicePackage.getTimes().intValue() > 0){
					return 0; // 消费套餐时,消费次数大于用户所拥有的服务次数
				}			
			} else {
				return 0; // 消费套餐时,消费次数大于用户所拥有的服务次数
			}
		}
		int tempTimes = 0;
		int remainTempTimes = 0;
		if( null == userStoreServicePackage ){ // 新增
			Map<String, Object> map_service = new HashMap<String, Object>();
			map_service.put("userCardId", userCard.getId());
			map_service.put("storeId", userCard.getStoreId());
			map_service.put("storeServicePackageId", userCard.getUserStoreServicePackage().getStoreServicePackageId());
			map_service.put("createdTime", new Date());
			tempTimes = userCard.getUserStoreServicePackage().getTimes();
			if( userCard.getPayWay() == 0 ){
				map_service.put("times", tempTimes);				
			} else {
				map_service.put("times", 0);
			}
			remainTempTimes = tempTimes;
			JdbcUtils.save("t_user_store_service_package", map_service);
		} else { // 修改
			if( null != userStoreServicePackage.getTimes() ){
				tempTimes = userStoreServicePackage.getTimes().intValue();				
			}
			Map<String, Object> updateMap = new HashMap<String, Object>();
			if(isSale){ // 消费时
				remainTempTimes = (tempTimes - userCard.getUserStoreServicePackage().getTimes().intValue());
			} else { // 充值时
				remainTempTimes = (tempTimes + userCard.getUserStoreServicePackage().getTimes().intValue());				
			}
			if( userCard.getPayWay() == 0 ){
				updateMap.put("times", remainTempTimes);
			} else {
				updateMap.put("times", tempTimes);
			}
			Map<String, Object> whereAndMap = new HashMap<String, Object>();
			whereAndMap.put("storeId", storeId);
			whereAndMap.put("userCardId", userCardId);
			whereAndMap.put("storeServicePackageId", storeServicePackageId);
			JdbcUtils.updateForAnd("t_user_store_service_package", updateMap, whereAndMap);
		}
		Map<String, Object> map_service_record = new HashMap<String, Object>();
		map_service_record.put("userCardId", userCard.getId());
		map_service_record.put("storeServicePackageId", userCard.getUserStoreServicePackage().getStoreServicePackageId());
		map_service_record.put("accountId", userCard.getAccountId());
		map_service_record.put("times", userCard.getUserStoreServicePackage().getTimes());
		map_service_record.put("createdTime", new Date());
		if(isSale){
			map_service_record.put("type", Constants.UserCardRecord.TYPE_CONSUMER); // 消费
			map_service_record.put("beforeTimes", tempTimes); 
		} else {
			map_service_record.put("type", Constants.UserCardRecord.TYPE_RECHARGE);	// 充值
			map_service_record.put("beforeTimes", 0); 

		}
		map_service_record.put("mobile", userCard.getMobile());
		
		map_service_record.put("remainTimes", remainTempTimes);
		map_service_record.put("realityPrice",userCard.getRealityPrice() );
		map_service_record.put("source", userCard.getSource());
		map_service_record.put("id", idService.getId());
		
		map_service_record.put("payWay", userCard.getPayWay());
		
		if( userCard.getPayWay() == 0 ){ // 支付成功
			map_service_record.put("payStatus", 0);
		} else {
			map_service_record.put("payStatus", 1);
		}
		
		JdbcUtils.save("t_user_store_service_package_record", map_service_record);
		return -10;
	}

	public void update_discount(UserCard userCard) {
		Map<String,Object> updateMap = new HashMap<String, Object>();
		updateMap.put("discountType", userCard.getDiscountType());
		
		if(userCard.getDiscountType().intValue() == Constants.UserCardRecord.DISCOUNTTYPE_FIXATION ){
			updateMap.put("discount", userCard.getDiscount());
		} else {
			updateMap.put("discount", null);
		}
		updateMap.put("memo", userCard.getMemo());
		updateMap.put("status", userCard.getStatus());
		
		Map<String, Object> whereAndMap = new HashMap<String, Object>();
		whereAndMap.put("id", userCard.getId());
		JdbcUtils.updateForAnd("t_user_card", updateMap,whereAndMap);
	}

	public void updateUserCardStatus(Long id,Integer status,String mobile,String name,String memo) {
		Map<String,Object> updateMap = new HashMap<String, Object>();
		updateMap.put("status", status);
		updateMap.put("mobile", mobile);
		updateMap.put("name", name);
		updateMap.put("memo", memo);
		
		Map<String, Object> whereAndMap = new HashMap<String, Object>();
		whereAndMap.put("id", id);
		JdbcUtils.updateForAnd("t_user_card", updateMap,whereAndMap);
	}

	public Page userCardRecordlist(Long orderId, String mobile, String cardNum, Integer type, String beginTime, String endTime, String storeIdString, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_card_record ucr left join t_user_card uc on ucr.userCardId = uc.id where 1=1 ");
		
		querySQL.append(" and ucr.payStatus != 1");
		
		if( null != orderId ){
			querySQL.append(" and (ucr.orderId = ? or uc.id = ? ) ");
			parameters.add(orderId);
			parameters.add(orderId);
		}
		
		if( null != type ){
			querySQL.append(" and ucr.type = ? ");
			parameters.add(type);
		}
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and ucr.mobile = ? ");
			parameters.add(mobile);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(cardNum);
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and ucr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and ucr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and ucr.storeId in (" + storeIdString + ")");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(),Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0, " select ucr.*,ucr.id as orderId,uc.cardNum as 'userCard.cardNum', (select a.mobile from t_account a where a.id = ucr.accountId) as 'userCard.accountName',(select s.name from t_store s where s.id = ucr.storeId) as 'userCard.storeName' ");
		querySQL.append(" order by ucr.createdTime desc limit ?,?");
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserCardRecord> userCardList = BeanUtils.toList(UserCardRecord.class, list);
		page.setData(userCardList);

		return page;
	}

	public UserCard getByMobile$StoreId(Long id, Long storeId, String mobile) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select * from t_user_card uc where 1=1 ");
		if( null != id ){
			querySQL.append(" and uc.id <> ? ");
			parameters.add(id);
		}
		if( null != storeId ){
			querySQL.append(" and uc.storeId = ?");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and uc.mobile = ?");
			parameters.add(mobile);
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null; 
	}

	public Page userStoreServicePackageRecordlist(Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_store_service_package_record ussp left join t_user_card uc on ussp.userCardId = uc.id where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and uc.storeId = ? ");
			parameters.add(storeId);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(),Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0, " select ussp.userCardId,ussp.storeServicePackageId,ussp.accountId,ussp.times,ussp.createdTime,ussp.type,ussp.mobile,ussp.beforeTimes,ussp.remainTimes,ussp.realityPrice,"
				+ "(select ssp.name from t_store_service_package ssp where ssp.id = ussp.storeServicePackageId )"
				+ " as servicePackageName,if(ussp.orderId is null , ussp.id ,ussp.orderId) as orderId,uc.cardNum as 'userCard.cardNum', "
				+ "(select a.mobile from t_account a where a.id = ussp.accountId) as 'userCard.accountName',(select s.name from t_store s where s.id = uc.storeId) as 'userCard.storeName' ");
		querySQL.append(" order by ussp.createdTime desc limit ?,?");
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserStoreServicePackageRecord> userStoreServicePackageRecordList = BeanUtils.toList(UserStoreServicePackageRecord.class, list);
		page.setData(userStoreServicePackageRecordList);

		return page;
	}
	
	
	public List<UserCardRecord> userCardRecordlist(Long orderId, String mobile, String cardNum, Integer type, String beginTime, String endTime, String storeIdString) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_card_record ucr left join t_user_card uc on ucr.userCardId = uc.id where 1=1 ");
		
		if( null != orderId ){
			querySQL.append(" and ucr.orderId = ? ");
			parameters.add(orderId);
		}
		
		if( null != type ){
			querySQL.append(" and ucr.type = ? ");
			parameters.add(type);
		}
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and ucr.mobile = ? ");
			parameters.add(mobile);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(cardNum);
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and ucr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and ucr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and ucr.storeId in (" + storeIdString + ")");
		}
		
		querySQL.insert(0, " select ucr.*,if(ucr.orderId is null , uc.id ,ucr.orderId) as orderId,uc.cardNum as 'userCard.cardNum', (select a.mobile from t_account a where a.id = ucr.accountId) as 'userCard.accountName',(select s.name from t_store s where s.id = ucr.storeId) as 'userCard.storeName' ");
		querySQL.append(" order by ucr.createdTime desc");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserCardRecord> userCardList = BeanUtils.toList(UserCardRecord.class, list);

		return userCardList;
	}

	public List<UserCard> list(String storeId, String reportMobile, String reportCardNum) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_user_card uc where 1 = 1 ");
		
		if( StringUtils.isNotBlank(reportMobile) ){
			querySQL.append(" and uc.mobile = ? ");
			parameters.add(reportMobile);
		}
		
		if( StringUtils.isNotBlank(reportCardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(reportCardNum);
		}
		
		if( StringUtils.isNotBlank(storeId) ){
			querySQL.append(" and uc.storeId in (" + storeId + ")");
		}
		
		
		querySQL.insert(0, " select uc.*, (select a.mobile from t_account a where a.id = uc.accountId) as accountName,(select s.name from t_store s where s.id = uc.storeId) as storeName ");
		querySQL.append(" order by uc.createdTime desc ");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<UserCard> userCardList = BeanUtils.toList(UserCard.class, list);
		for (UserCard userCard : userCardList) {
			StringBuffer serviceNameArray = new StringBuffer();
			List<UserStoreServicePackage> userStoreServicePackageList = userstoreservicepackageDao.findByUserCardId(userCard.getId());
			boolean flag = false;
			for (UserStoreServicePackage userStoreServicePackage : userStoreServicePackageList) {
				if( ObjectUtils.equals(userStoreServicePackage.getTimes(), 0) ){ // 列表不显示已经用完的服务套餐内容
					continue;
				}
				if(flag){
					serviceNameArray.append("  /  ");
				}
				flag = true;
				serviceNameArray.append(userStoreServicePackage.getStoreServicePackage().getName());
			}
			userCard.setServiceNameArray(serviceNameArray.toString());
		}
		return userCardList;
	}

	public int updateStatus(List<Object[]> batchArr) {
		String sql = " update t_user_card set status = ? where id = ? and (status = 1 or status = 3) ";
		int[] result = jdbcTemplate.batchUpdate(sql, batchArr);
		if( result.length > 0 ){
			return 1;
		}
		return 0;
	}

	public int updateRandomCardNumber(String cardID, String changeCardNumber) {
		String updateSQL = " update t_user_card set cardNum = ?  where id = ? ";
		return jdbcTemplate.update(updateSQL,changeCardNumber,cardID) > 0 ? 1: 0;
	}

	public UserCard getChangeCardInfo(String cardID) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where id = ?",cardID );
		if( list.isEmpty() ){
			return null;
		}
		return BeanUtils.toBean(UserCard.class, list.get(0));
	}


	public List<UserCard> getList(String storeIdString,String mobile,String cardNum) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer("select uc.* from t_user_card uc where status = ? ");
		parameters.add(Constants.UserCard.STATUS_ON);
		
		if( StringUtils.isNotBlank(mobile) ){
			querySQL.append(" and uc.mobile = ? ");
			parameters.add(mobile);
		}
		
		if( StringUtils.isNotBlank(cardNum) ){
			querySQL.append(" and uc.cardNum = ? ");
			parameters.add(cardNum);
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and uc.storeId in (" + storeIdString + ")");
		}
		
		querySQL.append(" and uc.upgradedLevelId is not null");
		
		querySQL.append(" order by uc.createdTime desc");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(UserCard.class, list);
		}
		return null;
	}


	public UserLevelDefinition getLevelDefinitionByID(String userCardID) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_level_definition where id = ( select upgradedLevelId from t_user_card where id = ? )",userCardID );
		if( list.isEmpty() ){
			return null;
		}
		return BeanUtils.toBean(UserLevelDefinition.class, list.get(0));
	}

	public boolean updateLevelAndName(String userCardID, String name, Double discount) {
		return jdbcTemplate.update("update t_user_card set userLevelName = ?,userLevelDiscount = ? where id = ?",name,discount,userCardID) > 0 ? true :false;
	}

	public void clearUpgradedLevelIdById(String userCardID) {
		jdbcTemplate.update("update t_user_card set upgradedLevelId = null where id = ?",userCardID);
	}


	public void updateDetail(User user) {
		logger.debug("updateDetail mobile is {} , name is {}",user.getMobile(),user.getName());
		jdbcTemplate.update("update t_user set mobile = ?,name = ?,sex = ?,birthDay = ? ,identity = ? where id = ?",
				user.getMobile(),user.getName(),
				user.getSex(),user.getBirthDay(),
				user.getIdentity(),user.getId());
	}


	public List<UserCard> getAllUserCardByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId = ? ",storeId );
	
		return BeanUtils.toList(UserCard.class, list);
	}

	public UserCard findByCardNumAndStoreId(String cardNum,Long storeId){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId = ? and cardNum = ? ",storeId ,cardNum);
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null;
	}

	public void updatePasswordByCardNumOrMobile(String cardNum,String mobile,Long storeId,String password){
		String sql = "update t_user_card set password = ? where storeId = ? ";
		List<Object> list = new ArrayList<Object>();
		list.add(password);
		list.add(storeId);
		
		if (StringUtils.isNotBlank(cardNum)){
			sql += " and cardNum = ? " ;
			list.add(cardNum);
		}
		
		if (StringUtils.isNotBlank(mobile)){
			sql += " and mobile = ? " ;
			list.add(mobile);
		}
		
		jdbcTemplate.update(sql, list.toArray());
	}


	public UserCard findByMobileAndStoreId(String mobile, Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId = ? and mobile = ? ",storeId ,mobile);
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null;
	}


	public void updateUserCardMoble(Long id, String mobile) {
		jdbcTemplate.update("update t_user_card set mobile = ? where id = ?",
				mobile,id);
		
	}


	public void saveUserUpdateMobileRecord(UserUpdateMobileRecord uumr) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", uumr.getId());
		map.put("accountStoreId", uumr.getAccountStoreId());
		map.put("storeId", uumr.getStoreId());
		map.put("oldMobile", uumr.getOldMobile());
		map.put("newMobile", uumr.getNewMobile());
		map.put("accountId", uumr.getAccountId());
		map.put("createdTime",uumr.getCreatedTime());
		JdbcUtils.save("t_user_update_mobile_record", map);
		
	}


	public void update(Long userCardId, Double balance) {
		jdbcTemplate.update("update t_user_card set balance = ? where id = ?", balance,userCardId);
	}


	public boolean updateLevel(String userCardID) {
		return jdbcTemplate.update("update t_user_card set levelId = upgradedLevelId where id = ?",userCardID) > 0 ? true :false;
	}


	public void updateUserCardName(Long id, String name) {
		jdbcTemplate.update("update t_user_card set name = ? where id = ?", name, id);
	}


	public List<UserCard> findByStoreStringAndMobile(String storeIdString, String mobile) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId in ("+storeIdString+") and mobile = ? " ,mobile);
		if (!list.isEmpty()){
			return BeanUtils.toList(UserCard.class, list);
		}
		return null;
	}


	public UserCard getByMobileAndCardNum(String cardNum, Long storeId,
			String mobile) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_card where storeId = ? and mobile = ? and cardNum=? ",storeId ,mobile,cardNum);
		if (!list.isEmpty()){
			return BeanUtils.toBean(UserCard.class, list.get(0));
		}
		return null;
	}
}