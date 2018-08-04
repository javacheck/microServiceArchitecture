package cn.lastmiles.service;
/**
 * createDate : 2016年3月9日上午11:03:58
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.OrderReturnGoods;
import cn.lastmiles.bean.PointRule;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.bean.UserUpdateMobileRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PointRuleDao;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserAccountManagerDao;
import cn.lastmiles.dao.UserCardDao;
import cn.lastmiles.dao.UserCardRecordDao;
import cn.lastmiles.dao.UserLevelDefinitionDao;

@Service
public class UserAccountManagerService {
	@Autowired
	private UserAccountManagerDao userAccountManagerDao;
	@Autowired
	private UserCardRecordDao userCardRecordDao;
	@Autowired
	private UserService userService;
	@Autowired
	private IdService idService;
	@Autowired
	private UserLevelDefinitionDao userLevelDefinitionDao;
	@Autowired
	private UserCardDao userCardDao;
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private StoreService storeService;
	@Autowired
	private PointRuleDao pointRuleDao;
	@Autowired
	private UserCardService userCardService;
	
	public Page list(String mobile, String cardNum, String storeIdString, Page page) {
		return userAccountManagerDao.list(mobile,cardNum,storeIdString,page);
	}
	
	public Page list(String mobile, String cardNum, String storeIdString,Integer mode, Page page) {
		return userAccountManagerDao.list(mobile,cardNum,storeIdString,mode,page);
	}

	public int checkCardNumBystoreId(Long id,Long storeId, String cardNum) {
		return userAccountManagerDao.checkCardNumBystoreId(id,storeId,cardNum);
	}
	
	public int checkMobileBystoreId(Long id,Long storeId, String mobile,Integer status) {
		return userAccountManagerDao.checkMobileBystoreId(id,storeId,mobile,status);
	}

	@Transactional
	public void save(UserCard userCard, int fun_Select) {
		if( null == userCard.getId() ){
			userCard.setId(idService.getId());
		}
		if (null == userCard.getCardNum()){
			userCard.setCardNum(userCard.getId().toString());
		}
		if( null == userCard.getCreatedTime() ){
			userCard.setCreatedTime(new Date());
		}
		
		Long storeId = userCard.getStoreId();
		String mobile = userCard.getMobile();
		User user = userService.returnStoreUserByStoreId$Mobile(storeId, mobile);
		String identity = userCard.getIdentity();
		String birthTime = userCard.getBirthTime();
		Integer sex = userCard.getSex();
		if( StringUtils.isNotBlank(identity) || StringUtils.isNotBlank(birthTime) || null != sex ){
			userService.update(identity,birthTime,sex,user.getId());
		}
		
		//会员卡密码
		if (userCard.getSource() == 0){
			//后台添加密码默认888888
			userCard.setPassword(PasswordUtils.encryptPassword("888888"));
		}else {
			if (StringUtils.isNotBlank(userCard.getPassword())){
				userCard.setPassword(PasswordUtils.encryptPassword(userCard.getPassword()));
			}
		}
		
		if(fun_Select == 0 ){ // 储值功能(保存会员卡的同时保存会员卡充值记录)
			createUserCardRecord(userCard, user);
			userAccountManagerDao.save_stored_value(userCard);
		} else if(fun_Select == 1) { // 服务套餐(保存会员卡的同时保存服务套餐记录)
			userAccountManagerDao.save_service_Package(userCard);
		} else if(fun_Select == 2){ // 折扣功能
			userAccountManagerDao.save_discount(userCard);
		}
		
		if( userCard.getPayWay() == 0 ){ // 只有现金支付的时候才进行等级计算
			handlerUserLevel(userCard,fun_Select);			
		}
		
		// 新增时，新增会员卡的名称， 将此名称同步到User表中的name字段(2016.04.28 PM 17:22)
		String name = userCard.getName();
		if( StringUtils.isNotBlank(name) ){
			userService.updateName(user.getId(),name);
			batchUpdateUserCardName(storeId, mobile, name); // 批量修改会员卡名称
		}
	}

	public void batchUpdateUserCardName(Long storeId, String mobile, String name) {
		Store topStore = storeService.findTopStore(storeId);
		List<Store> storeList = storeDao.findAllChildrenStore(topStore.getId());
		StringBuilder ids = new StringBuilder();
		for (Store s : storeList){
			if (ids.length() == 0){
				ids.append(s.getId());
			}else {
				ids.append("," + s.getId());
			}
		}
		logger.debug("获取的ID是：{}",ids);
		List<UserCard> userCardList = userAccountManagerDao.findByStoreStringAndMobile(ids.toString(),mobile);
		if( null != userCardList ){
			for (UserCard userCard2 : userCardList) {
				userAccountManagerDao.updateUserCardName(userCard2.getId(),name);
			}			
		}
	}
	
	private void createUserCardRecord(UserCard userCard, User user) {
		UserCard uc = userAccountManagerDao.findById(userCard.getId());
		
		UserCardRecord userCardRecord = new UserCardRecord();
		userCardRecord.setId(idService.getId());
		userCardRecord.setUserCardId(userCard.getId());
		userCardRecord.setStoreId(userCard.getStoreId());
		userCardRecord.setCreatedTime(new Date());
		userCardRecord.setType(Constants.UserCardRecord.TYPE_RECHARGE); // 充值类型
		userCardRecord.setAccountId(userCard.getAccountId());
		
		userCardRecord.setAmount(userCard.getActualAmount()); // 充值金额
		
		userCardRecord.setActualAmount(userCard.getBalance()); // 可消费金额
		
		userCardRecord.setSource(userCard.getSource());
		
		userCardRecord.setPayWay(userCard.getPayWay());
		if( userCard.getPayWay() == 0 ){ // 支付成功 现金支付方式，直接成功
			userCardRecord.setPayStatus(0);
		} else {
			userCardRecord.setPayStatus(1);
		}
		
		if( null == uc ){ // 以前没有
			userCardRecord.setBalance(userCard.getBalance());			
		} else { // 以前就有
			userCardRecord.setBalance(NumberUtils.add(ObjectUtils.equals(0D, uc.getBalance()) ? 0D : uc.getBalance(), userCard.getBalance()));
		}
		userCardRecord.setPoint(0D);
		if (uc == null){
			userCardRecord.setTotalPoint(0d);
			userCardRecord.setRemainPoint(0d);
		}else {
			userCardRecord.setTotalPoint(uc.getTotalPoint());
			userCardRecord.setRemainPoint(uc.getPoint());
		}
		userCardRecord.setMobile(userCard.getMobile());
		logger.debug("createUserCardRecord source = {}",userCardRecord.getSource());
		userCardRecordDao.save(userCardRecord);
	}

	public UserCard findById(Long id) {
		return userAccountManagerDao.findById(id);
	}

	public int update(UserCard userCard, int fun_Select) {
		if(fun_Select == 0 ){ // 储值功能(保存会员卡的同时保存会员卡充值记录)
			Long storeId = userCard.getStoreId();
			String mobile = userCard.getMobile();
			User user = userService.searchOrganization_UserByMobile$StoreId(mobile, storeId);
			createUserCardRecord(userCard, user);
			userAccountManagerDao.update_stored_value(userCard);
		} else if(fun_Select == 1) { // 服务套餐(保存会员卡的同时保存服务套餐记录)
			int ret =  update_service_Package(userCard,false);
			
			if( userCard.getPayWay() == 0 ){
				handlerUserLevel(userCard, fun_Select);				
			}
			return ret;
		} else if(fun_Select == 2){ // 折扣功能
			userAccountManagerDao.update_discount(userCard);
		}
		return -10;
	}
	public int update_service_Package(UserCard userCard,boolean isSale){
		return userAccountManagerDao.update_service_Package(userCard,isSale); 
	}

	public Page userCardRecordlist(Long orderId, String mobile, String cardNum, Integer type, String beginTime, String endTime, String storeIdString, Page page) {
		if( ObjectUtils.equals(type, 0) ){
			type = null;
		}
		return userAccountManagerDao.userCardRecordlist(orderId,mobile,cardNum,type,beginTime,endTime,storeIdString,page);
	}

	public UserCard getByMobile$StoreId(Long id, Long storeId, String mobile) {
		return userAccountManagerDao.getByMobile$StoreId(id,storeId,mobile);
	}

	public Page userCardRecordlist(Long storeId, Integer fun_Select, Page page) {
		if( fun_Select.intValue() == 0 ){
			return userCardRecordlist(null, null, null, null, null, null, storeId.toString(), page);			
		} 
		return userAccountManagerDao.userStoreServicePackageRecordlist(storeId,page);
	}
	
	public List<UserCardRecord> userCardRecordlist(Long orderId, String mobile, String cardNum, Integer type, String beginTime, String endTime, String storeIdString) {
		return userAccountManagerDao.userCardRecordlist(orderId, mobile, cardNum, type, beginTime, endTime, storeIdString);
	}
	
	private final static Logger logger = LoggerFactory.getLogger(UserAccountManagerService.class);
	
	/**+
	 * 处理等级，积分
	 */
	protected void handlerUserLevel(UserCard uc,int fun_Select){
		logger.debug("handlerUserLevel= {},{}",uc,fun_Select);
		UserLevelDefinition userLevelDef = null;
		
		if (uc.getLevelId() != null){
			userLevelDef = userLevelDefinitionDao.findById(uc.getLevelId());
		}
		
		Integer levelMode = Constants.UserLevelDefinition.AUTOMODE;
		Long levelId = null;
		Integer defType = null;//0表示积分，1表示消费总额
		
		Double point = 0d;
		Double totalPoint = 0d;
		Double consumption = 0d;
		Double discount = 10d;
		
		Store topStore = storeDao.findTopStore(uc.getStoreId());
		
		//处理积分
		List<Store> storeList = storeDao.findAllEntryChildrenStore(topStore.getId());
		for (Store store : storeList){
			if (store.getId().longValue() != uc.getStoreId().longValue()){
				//如果其他分店有会员卡，因为积分共享，则把积分同步过来
				UserCard userCard = userCardDao.findByMobileAndStoreId(uc.getMobile(), store.getId());
				if (userCard != null){
					totalPoint = NumberUtils.add(point,userCard.getTotalPoint());
					point = NumberUtils.add(point,  userCard.getPoint());
					levelId = userCard.getLevelId();
					discount = userCard.getDiscount();
					break;
				}
			}
		}
		
		if (userLevelDef != null){
			levelId = userLevelDef.getId();
			levelMode = userLevelDef.getMode();
			defType = userLevelDef.getType();
			if (userLevelDef.getType().intValue() == 1){
				consumption = userLevelDef.getPoint();
			}else {
				if (totalPoint.doubleValue() < userLevelDef.getPoint().doubleValue()){
					totalPoint = userLevelDef.getPoint();
				}
			}
			levelMode = userLevelDef.getMode();
			discount = userLevelDef.getDiscount();
		}
		
		if (fun_Select == 1){
			Date date = new Date();
			
			Store store = storeDao.findById(uc.getStoreId());
			PointRule pointRule = null;
			if (store.getOrganizationId() != null && store.getUnifiedPointRule().intValue() == Constants.Store.UNIFIEDPOINTRULE) {
				// 如果是连锁商家,设置使用总部规则，找总部设置规则
				pointRule = pointRuleDao
						.getPointRuleByStoreId(topStore.getId());// 查找总部积分规则
			} else if (store.getOrganizationId()  == null){
				pointRule = pointRuleDao.getPointRuleByStoreId(uc
						.getStoreId());// 通过店铺ID查找积分规则
			}
			Double getPoint = 0D;
			if (pointRule != null) {
				if (pointRule.getStatus().intValue() == 1 && pointRule.getRestriction().intValue() == 0) {// 1 积分规则有效  无限制
					if (pointRule.getValidTime() != null) {// 有效时间为表无限
						date = DateUtils.addYear(new Date(),
								pointRule.getValidTime());
					} else {
						date = DateUtils.addYear(new Date(), 100);// 一百表是从开始时间加一百年表无限
					}

					getPoint = NumberUtils.divide(
							uc.getRealityPrice(),
							(NumberUtils.divide(pointRule.getMoney(),
									pointRule.getPoint())));// 积分
				}
			}
			logger.debug("point rule = {},getpoint = {}",pointRule,getPoint);
			totalPoint = NumberUtils.add(getPoint,totalPoint);
			point = NumberUtils.add(point,  getPoint);
			
			if (getPoint.doubleValue() > 0.0){
				UserCardRecord record = new UserCardRecord();
				record.setAccountId(uc.getAccountId());
				record.setActualAmount(uc.getRealityPrice());
				record.setCreatedTime(new Date());
	//			record.setOrderId(order.getId());
				record.setStoreId(uc.getStoreId());
				record.setPoint(getPoint);
				record.setMobile(uc.getMobile());
				record.setTotalPoint(totalPoint);
				record.setRemainPoint(point);
				record.setUserCardId(uc.getId());
				record.setBalance(uc.getBalance());
	
				record.setType(Constants.UserCardRecord.TYPE_SERVICE_PACKAGE);
				record.setValidDate(date);
				record.setId(idService.getId());
				userCardRecordDao.save(record);
			}
			
			consumption = NumberUtils.add(uc.getRealityPrice(),consumption);
		}
		if (levelId == null){
			//处理等级
			List<UserLevelDefinition> defList = userLevelDefinitionDao
					.findByStoreId(uc.getStoreId());
			logger.debug("UserLevelDefinition = {}",defList);
			if (defList != null && !defList.isEmpty()) {
				for (UserLevelDefinition def : defList) {
					// 0表示积分，1表示消费总额
					if (def.getType().intValue() == 0
							&& def.getDiscount() != null) {
						if (totalPoint.doubleValue() >= def.getPoint()
								.doubleValue()) {
							levelMode = def.getMode();
							levelId = def.getId();
							uc.setLevelId(def.getId());
							defType = def.getType();
							discount = def.getDiscount();
							break;
						}
					} else if (def.getType().intValue() == 1
							&& def.getDiscount() != null) {
						if (consumption.doubleValue() >= def.getPoint()
								.doubleValue()) {
							levelMode = def.getMode();
							levelId = def.getId();
							uc.setLevelId(def.getId());
							defType = def.getType();
							discount = def.getDiscount();
							break;
						}
					}
				}
	//			userCardDao.updateLevel(uc.getId(), levelName, discount);
			}
		}
		
		logger.debug("point = {},totalPoint = {},levelId = {}",point,totalPoint,levelId);
		
		if (levelId != null){
			if (ObjectUtils.equals(levelMode, Constants.UserLevelDefinition.AUTOMODE)){
				userCardDao.updatePointAndLevel(uc.getMobile(),
						uc.getStoreId(), point, totalPoint,levelId,
						null,defType);
			}else {
				userCardDao.updatePointAndLevel(uc.getMobile(),
						uc.getStoreId(), point, totalPoint,
						null,levelId,defType);
			}
		}
		
		userCardDao.updateTotalConsumptionById(consumption, uc.getId());
		
		uc.setDiscount(discount);
		uc.setPoint(point);
	}

	public List<UserCard> list(String storeId, String reportMobile, String reportCardNum) {
		return userAccountManagerDao.list(storeId,reportMobile,reportCardNum);
	}

	public boolean updateByReturnGoods(OrderReturnGoods orderReturnGoods,UserCard userCard, User user,Long orderId) {
		
		UserCard uc = userAccountManagerDao.findById(userCard.getId());
		
		Double balance  = 0D;
		if( null == uc ){// 以前没有
			balance = userCard.getBalance();
		} else {	// 以前就有		
			balance = NumberUtils.add(ObjectUtils.equals(0D, uc.getBalance()) ? 0D : uc.getBalance(), userCard.getBalance());
		}
		// 退储值
		if( ObjectUtils.equals(orderReturnGoods.getReturnType(),Constants.OrderReturnGoods.RETURNTYPE_STOREDVALUE) ){
//			UserCardRecord userCardRecord = new UserCardRecord();
//			userCardRecord.setId(idService.getId());
//			userCardRecord.setUserCardId(userCard.getId());
//			userCardRecord.setStoreId(userCard.getStoreId());
//			userCardRecord.setCreatedTime(new Date());
//			userCardRecord.setType(Constants.UserCardRecord.TYPE_RETURNGOODS); // 退换货
//			userCardRecord.setAccountId(userCard.getAccountId());
//			
//			userCardRecord.setAmount(userCard.getActualAmount()); // 退货金额
//			
//			userCardRecord.setActualAmount(userCard.getBalance()); // 可消费金额
//			userCardRecord.setBalance(balance);			
//			userCardRecord.setPoint(userCard.getPoint());
//			userCardRecord.setTotalPoint(user.getTotalPoint());
//			userCardRecord.setMobile(userCard.getMobile());
//			
//			userCardRecordDao.save(userCardRecord);
			
			Map<String,Object> updateMap = new HashMap<String, Object>();
			
			updateMap.put("balance", balance);
			updateMap.put("memo", userCard.getMemo());
			updateMap.put("status", userCard.getStatus());
			
			Map<String,Object> whereAndMap = new HashMap<String, Object>();
			whereAndMap.put("id", userCard.getId());
			JdbcUtils.updateForAnd("t_user_card", updateMap, whereAndMap);
		}
		
		logger.debug("退换货积分计算传值：mobile is {},storeId is {},point is {},balance is {},orderId is {}",userCard.getMobile(),userCard.getStoreId(),userCard.getPoint(),balance,orderId);
		// 退货积分问题
		userCardService.handlePoint(userCard.getMobile(),userCard.getStoreId(),-orderReturnGoods.getReturnPoint().doubleValue(),orderReturnGoods.getReturnPrice(),Constants.UserCardRecord.TYPE_RETURNGOODS,userCard.getAccountId(),orderId,new Date(),orderReturnGoods.getReturnType());

		return true;
	}
	
	public int updateStatusById(Integer status, String[] cacheArray) {
		List<Object[]> batchArr = new ArrayList<Object[]>();
		for (String objects : cacheArray) {
			Object[] o = new Object[2];
			o[0] = status;
			o[1] = objects;
			batchArr.add(o);
		}
		return userAccountManagerDao.updateStatus(batchArr);
	}

	public int updateRandomCardNumber(String cardID, String changeCardNumber) {
		return userAccountManagerDao.updateRandomCardNumber(cardID,changeCardNumber);
	}

	public UserCard getChangeCardInfo(String cardID) {
		return userAccountManagerDao.getChangeCardInfo(cardID);
	}

	@Transactional
	public int upgradeUpdate(String storeIdString,String mobile,String cardNum,String[] cacheArray) {
		List<String> list = new ArrayList<String>();
		if( null == cacheArray || cacheArray.length == 0 ){ // 查询所有的
			List<UserCard> userCardList = userAccountManagerDao.getList(storeIdString,mobile,cardNum);
			if( null != userCardList ){
				for (UserCard userCard : userCardList) {
					list.add(userCard.getId().toString());
				}
			}
		} else {
			for (String string : cacheArray) {
				list.add(string);
			}
		}
		logger.debug("手动升级组拼的ID数组字符串是：{}",list.toArray());
		int countNumber = 0;
		for ( String userCardID : list ) {
			// 根据ID查找等级表中的等级名称和等级折扣
			UserLevelDefinition uld = userAccountManagerDao.getLevelDefinitionByID(userCardID);
			// 根据ID修改会员卡中的等级名称和等级折扣
			if( null == uld){
				logger.debug("根据userCardID查询不到等级信息：{}",userCardID);
				continue;
			}
//			boolean flag = userAccountManagerDao.updateLevelAndName(userCardID,uld.getName(),uld.getDiscount());
			boolean flag = userAccountManagerDao.updateLevel(userCardID);
			if(flag){
				userAccountManagerDao.clearUpgradedLevelIdById(userCardID);
				countNumber++;
			}
		}
		if( list.size() == 0 ){
			logger.debug("没有可以升级的会员!");
			return 2;
		}
		if( list.size() - countNumber == 0){
			logger.debug("升级成功!");
			return 1;
		} 
		logger.debug("查到的数据和升级的数据不相符!,已升级的数据量为：{}",countNumber);
		return 0;
	}

	@Transactional
	public void updateDetail(UserCard userCard, User user,boolean isMain) {
		logger.debug("isMain={}",isMain);
		Long id = userCard.getId();
		Integer status = userCard.getStatus();
		String mobile = userCard.getMobile();
		String name = userCard.getName();
		String memo = userCard.getMemo();
		logger.debug("-------------------------------------------{},{}",user.getName(),name);
		//通过ID查出旧手机号
		UserCard uc=userAccountManagerDao.findById(id);
		logger.debug("uc.getMobile()={},mobile={}",uc.getMobile(),mobile);
		if(mobile.equals(uc.getMobile())){//手机号没变化
			// 修改当前ID的会员卡信息
			userAccountManagerDao.updateUserCardStatus(id,status,mobile,name,memo);
			// 修改当前ID的会员信息
			userAccountManagerDao.updateDetail(user);
			
			
			// 批量修改会员卡姓名
			batchUpdateUserCardName(userCard.getStoreId(),mobile,name);
		}else{
			List<Store> storeList=null;
			//通过当前店查出总店下的所有店铺
			if(isMain){
				storeList = storeService.findAllStoreOnOneOrganizationTree(uc.getStoreId());
			}else{
				Store s=storeService.findTopStore(uc.getStoreId());
				logger.debug("s={}",s);
				storeList = storeService.findAllStoreOnOneOrganizationTree(s.getId());
			}
			logger.debug("storeList={}",storeList);
			List<UserCard> ucList=new ArrayList<UserCard>();
			for(Store s:storeList){
				logger.debug("uc.getMobile()={},s.getId()={}",uc.getMobile(),s.getId());
				//通过店铺ID和手机号去查会员卡信息
				UserCard uCard=userAccountManagerDao.findByMobileAndStoreId(uc.getMobile(), s.getId());
				logger.debug("uCard={}",uCard);
				if(uCard!=null){
					ucList.add(uCard);//会员卡
				}
			}
			
			// 修改当前ID的会员信息
			userAccountManagerDao.updateDetail(user);
			
			// 批量修改会员卡姓名
			batchUpdateUserCardName(userCard.getStoreId(),mobile,name);
						
			logger.debug("ucList.size()={}",ucList.size());
			for(int i=0;i<ucList.size();i++){
				UserUpdateMobileRecord uumr=new UserUpdateMobileRecord();
				uumr.setId(idService.getId());//修改会员手机记录ID
				uumr.setAccountStoreId(userCard.getStoreId());//操作商家ID
				uumr.setStoreId(ucList.get(i).getStoreId());//会员手机关联商家ID
				uumr.setOldMobile(ucList.get(i).getMobile());////会员旧手机号
				uumr.setNewMobile(mobile);////会员新手机号
				uumr.setAccountId(uc.getAccountId());//操作人ID
				uumr.setCreatedTime(new Date());
				userAccountManagerDao.saveUserUpdateMobileRecord(uumr);
				logger.debug("ucList.get(i).getId()={},id={}",ucList.get(i).getId(),id);
				if(ObjectUtils.equals(ucList.get(i).getId(), id)){//如果是当前会员卡
					// 修改当前ID的会员卡信息
					userAccountManagerDao.updateUserCardStatus(id,status,mobile,name,memo);
				}else{
					//修改同一会员的手机号
					userAccountManagerDao.updateUserCardMoble(ucList.get(i).getId(),mobile);
				}
				
			}
			
		}
	}

	public List<UserCard> getAllUserCardByStoreId(Long storeId) {
		return userAccountManagerDao.getAllUserCardByStoreId(storeId);
	}

	@Transactional
	public void export(List<List<String>> lo,Long storeId,Long accountId) {
		for (int i = 1; i < lo.size(); i++){
			List<String> row = lo.get(i);
			String mobile = row.get(0);
			String name = row.get(1);
			String cardNum = row.get(2);
			String memo = row.get(3);
			UserCard uc = new UserCard();
			uc.setMobile(mobile);
			uc.setStoreId(storeId);
			uc.setMemo(memo);
			uc.setName(name);
			uc.setAccountId(accountId);
			if (StringUtils.isBlank(cardNum)){
				uc.setCardNum(idService.getId().toString());
			}else {
				uc.setCardNum(cardNum);
			}
			logger.debug("usercard = {}",uc);
			save(uc,0);
		}
	}
	
	public UserCard findByCardNumAndStoreId(String cardNum,Long storeId){
		return userAccountManagerDao.findByCardNumAndStoreId(cardNum, storeId);
	}
	
	/**
	 * 根据手机或者卡号修改
	 * @param cardNum
	 * @param mobile
	 * @param storeId
	 */
	public void updatePasswordByCardNumOrMobile(String cardNum,String mobile,Long storeId,String password){
		userAccountManagerDao.updatePasswordByCardNumOrMobile(cardNum, mobile, storeId, PasswordUtils.encryptPassword(password));
	}

	public void update(Long userCardId, Double balance) {
		UserCard uc = userAccountManagerDao.findById(userCardId);
		balance = NumberUtils.add( (ObjectUtils.equals(0D, uc.getBalance()) ? 0D : uc.getBalance()), balance);
		userAccountManagerDao.update(userCardId,balance);
		handlerUserLevel(uc,0);
	}

	public String checkConnectedMobile(String mobile, Long storeId,boolean isMain) {
		List<Store> storeList=null;
		//通过当前店查出总店下的所有店铺
		if(isMain){
			storeList = storeService.findAllStoreOnOneOrganizationTree(storeId);
		}else{
			Store s=storeService.findTopStore(storeId);
			logger.debug("s={}",s);
			storeList = storeService.findAllStoreOnOneOrganizationTree(s.getId());
		}
		boolean flag=true;
		for(Store s:storeList){
			//通过店铺ID和手机号去查会员卡信息
			UserCard uCard=userAccountManagerDao.findByMobileAndStoreId(mobile, s.getId());
			logger.debug("uCard={}",uCard);
			if(uCard!=null){
				flag=false;
				break;
			}
		}
		if(flag){
			return "0";
		}else{
			return "1";
		}
	}

	public UserCard getByMobileAndCardNum(String cardNum, Long storeId,
			String oldMobile) {
		
		return userAccountManagerDao.getByMobileAndCardNum(cardNum,storeId,oldMobile);
	}
}