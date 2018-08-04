package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.http.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.AppUserCardResult;
import cn.lastmiles.bean.Partner;
import cn.lastmiles.bean.PointRule;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PointRuleDao;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserCardDao;
import cn.lastmiles.dao.UserCardRecordDao;
import cn.lastmiles.dao.UserDao;
import cn.lastmiles.dao.UserLevelDefinitionDao;
import cn.lastmiles.service.sms.SMSParameter;
import cn.lastmiles.service.sms.SMSService;
import cn.lastmiles.service.sms.SMSTemplate;

@Service
public class UserCardService {
	private final static Logger logger = LoggerFactory
			.getLogger(UserCardService.class);
	@Autowired
	private UserCardDao userCardDao;
	@Autowired
	private UserCardRecordDao userCardRecordDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ShopService shopService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserstoreservicepackageService userstoreservicepackageService;
	@Autowired
	private StoreServicePackageService StoreServicePackageService;
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private PointRuleDao pointRuleDao;
	@Autowired
	private IdService idService;
	@Autowired
	private PartnerService partnerService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private UserLevelDefinitionDao userLevelDefinitionDao;
	@Autowired
	private SMSService smsService;
	
	public Page findMyUserCard(String mobile,String storeName,Page page) {
		List<AppUserCardResult> appUserCardResults = new ArrayList<AppUserCardResult>();
		List<UserCard> userCards =userCardDao.findMyUserCard(mobile,storeName);
		logger.debug("userCards is {}",userCards);
		for (UserCard userCard : userCards) {
			if (userCard.getBalance()!=null) {
				//添加储值卡信息
				AppUserCardResult appUserCardResult = new AppUserCardResult();
				appUserCardResult.setId(userCard.getId());
				appUserCardResult.setFirstColumnName("余额");
				appUserCardResult.setFirstColumnContent(NumberUtils.dobleToString(NumberUtils.dealDecimal(userCard.getBalance())));
				appUserCardResult.setSecondColumnName("积分");
				User user = userService.returnStoreUserByStoreId$Mobile(userCard.getStoreId(), mobile);
				appUserCardResult.setSecondColumnContent(String.valueOf(user.getTotalPoint()));
				appUserCardResult.setStoreName(shopService.findById(userCard.getStoreId()).getName());
				appUserCardResult.setTypeDescribe("储值");
				appUserCardResult.setCardNum(userCard.getCardNum());
				appUserCardResult.setCreatedTime(userCard.getCreatedTime());
				appUserCardResults.add(appUserCardResult);
			}
			List<UserStoreServicePackage> userStoreServicePackages =userstoreservicepackageService.findByUserCardId(userCard.getId());
			for (UserStoreServicePackage userStoreServicePackage : userStoreServicePackages) {
				//添加储值卡信息
				AppUserCardResult appUserCardResult = new AppUserCardResult();
				appUserCardResult.setId(userCard.getId());
				appUserCardResult.setFirstColumnName("服务内容");
				appUserCardResult.setFirstColumnContent(StoreServicePackageService.findById(userStoreServicePackage.getStoreServicePackageId()).getName());
				appUserCardResult.setSecondColumnName("可消费次数");
				appUserCardResult.setSecondColumnContent(String.valueOf(userStoreServicePackage.getTimes()));
				appUserCardResult.setStoreName(shopService.findById(userStoreServicePackage.getStoreId()).getName());
				appUserCardResult.setTypeDescribe("服务套餐");
				appUserCardResult.setCardNum(userCard.getCardNum());
				appUserCardResult.setCreatedTime(userCard.getCreatedTime());
				appUserCardResults.add(appUserCardResult);
			}
		}
		logger.debug("appUserCardResults size is :{}",appUserCardResults.size());
		if (appUserCardResults.isEmpty()) {
			return page;
		}
		page.setTotal(appUserCardResults.size());
		page.setData(appUserCardResults);
		page.dealPaging();
		logger.debug("page is :{}",page);
		return page;
	}

	/**
	 * 更新余额
	 * @param userId
	 * @param storeId
	 * @param balance
	 */
	public void updateBalance(String mobile,Long storeId,Double balance,UserCard userCard){
		logger.debug("updateBalance:{},{},{}",mobile,storeId,balance);
		//如果余额小等于50，发送提醒短信
		if (balance.doubleValue() <= 50 && userCard.getRemindedSms().intValue() != 1){
			smsService.send2(SMSTemplate.BALANCEREMINDE, SMSParameter.getInstance("storeName",userCard.getStoreName()), mobile);
			userCardDao.updateRemindedSms(userCard.getId(),1);
		}
		userCardDao.updateBalance(mobile, storeId, balance);
	}
	
	public UserCard findByMobileAndStoreId(String mobile,Long storeId){
		return userCardDao.findByMobileAndStoreId(mobile, storeId);
	}
	
	public List<UserCard> findAllStoreId(){
		return userCardDao.findAllStoreId();
	}

	public List<UserCard> findAllDate() {
		return userCardDao.findAllDate();
	}
	
	/**
	 * 处理积分等级
	 * @param mobile
	 * @param storeId
	 * @param point
	 * @param type
	 */
	@Transactional
	public void handlePoint(String mobile,Long storeId,Double point,Double amount, int type,Long accountId,Long orderId,Date validDate,Integer returnType){
		logger.debug("handlePoint  mobile = {},storeId = {},point= {},amount = {},type = {},accountId = {},orderId = {},validate = {}",
				 mobile, storeId, point, amount,  type, accountId, orderId, validDate);
		UserCard userCard = userCardDao.findByMobileAndStoreId(mobile, storeId);
		if (userCard == null){
			return;
		}
		Double totalPoint = NumberUtils.add(userCard.getTotalPoint(), point);
		Double remainPoint = NumberUtils.add(userCard.getPoint(), point);
		
		Double totalConsumption = NumberUtils.add(userCard.getTotalConsumption(),
				amount);
		
		Store topStore = storeDao.findById(storeId);
		//连锁商家
		if (topStore.getUnifiedPointRule().intValue() == Constants.Store.UNIFIEDPOINTRULE) {
			topStore = storeDao.findTopStore(storeId);
		}
		List<UserLevelDefinition> defList = null;
		// 处理等级
		if (topStore != null) {
			defList = userLevelDefinitionDao
					.findByStoreId(topStore.getId());
		} else {
			defList = userLevelDefinitionDao.findByStoreId(storeId);
		}
		Double discount = 101D;
		Integer levelMode = Constants.UserLevelDefinition.AUTOMODE;
		Long levelId = null;
		Integer defType = null;
		
		if (defList != null && !defList.isEmpty()) {
			for (UserLevelDefinition def : defList) {
				// 0表示积分，1表示消费总额
				if (def.getType().intValue() == 0
						&& def.getDiscount() != null) {
					if (totalPoint.doubleValue() >= def.getPoint()
							.doubleValue()) {
						discount = def.getDiscount();
						levelMode = def.getMode();
						levelId = def.getId();
						defType = def.getType();
						break;
					}
				} else if (def.getType().intValue() == 1
						&& def.getDiscount() != null) {
					if (totalConsumption.doubleValue() >= def.getPoint()
							.doubleValue()) {
						discount = def.getDiscount();
						levelMode = def.getMode();
						levelId = def.getId();
						defType = def.getType();
						break;
					}
				}
			}
		}
		
		// 保存支付记录，更新会员卡的积分、消费金额
		if (discount.doubleValue() == 101D) {
			discount = null;
		}
		
		if (ObjectUtils.equals(levelMode, Constants.UserLevelDefinition.AUTOMODE)){
			userCardDao.updatePointAndLevel(mobile,
					storeId, remainPoint, totalPoint,
					levelId,null,defType);
		}else {
			userCardDao.updatePointAndLevel(mobile,
					storeId, remainPoint, totalPoint,
					null,levelId,defType);
		}
		
		userCardDao.updateTotalConsumptionById(totalConsumption, userCard.getId());
		UserCardRecord record = new UserCardRecord();
		record.setAccountId(accountId);
		/**
		 * 2016.10.18进行修改
		 * 因为目前只有UserAccountManagerService.updateByReturnGoods方法调用了此函数
		 * 又因为WXJ的要求当退货时，退储值，则充值/消费一栏为所退货款的正数，而现金且有积分减扣时，则充值/消费一栏为空
		 * 故加上此次的判断
		 */
		// 退储值
		if( ObjectUtils.equals(returnType,Constants.OrderReturnGoods.RETURNTYPE_STOREDVALUE)){
			record.setActualAmount(Math.abs(amount)); // 必须为正，则取绝对值			
		} else if( ObjectUtils.equals(returnType,Constants.OrderReturnGoods.RETURNTYPE_CASH)){
			record.setActualAmount(null);
		} else {
			record.setActualAmount(amount);
		}
		record.setCreatedTime(new Date());
		record.setOrderId(orderId);
		record.setStoreId(storeId);
		record.setPoint(point);
		record.setMobile(mobile);
		record.setTotalPoint(totalPoint);
		record.setRemainPoint(remainPoint);
		record.setUserCardId(userCard.getId());
		record.setBalance(userCard.getBalance());
		record.setValidDate(validDate);

		record.setType(type);
		record.setId(idService.getId());
		userCardRecordDao.save(record);
	}
	
	/**
	 * 微信公众号积分处理
	 * @param mobile
	 * @param storeId
	 * @param point
	 */
	@Transactional
	public void handleWeiXinPoint(String mobile,Long storeId,Double point){
		UserCard userCard = userCardDao.findByMobileAndStoreId(mobile, storeId);
		if (userCard != null){
			Store store = storeDao.findById(storeId);
			PointRule pointRule = null;
			Store topStore = null;
			if (store.getUnifiedPointRule().intValue() == Constants.Store.UNIFIEDPOINTRULE) {
				// 如果是连锁商家,设置使用总部规则，找总部设置规则
				topStore = storeDao.findTopStore(storeId);
				pointRule = pointRuleDao
						.getPointRuleByStoreId(topStore.getId());// 查找总部积分规则
			} else {
				pointRule = pointRuleDao.getPointRuleByStoreId(storeId);// 通过店铺ID查找积分规则
			}
			Date date = new Date();
			if (pointRule != null && pointRule.getStatus().intValue() == 1 && pointRule.getRestriction().intValue() == 0) {
				// 1 积分规则有效  无限制
				if (pointRule.getValidTime() != null) {// 有效时间
					date = DateUtils.addYear(new Date(),
							pointRule.getValidTime());
				} else {
					date = DateUtils.addYear(new Date(), 100);// 一百表是从开始时间加一百年表无限
				}
				point = 0.0;
			}
			
			User user = userService.findUserForChainStore(mobile, storeId);
			Double totalPoint = user.getTotalPoint();
			Double remainPoint = user.getPoint();
			if (point > 0){
				//累计积分
				totalPoint = NumberUtils.add(user.getTotalPoint(), point);
				//当前剩余积分
				remainPoint = NumberUtils.add(user.getPoint(), point);
				
				userCardDao.updatePoint(mobile, storeId,remainPoint, totalPoint);
				userDao.updatePoint(user.getId(), remainPoint, totalPoint);
			}
			UserCardRecord record = new UserCardRecord();
			record.setAccountId(null);
			record.setActualAmount(null);
			record.setCreatedTime(new Date());
			record.setOrderId(null);
			record.setStoreId(storeId);
			record.setPoint(point);
			record.setMobile(mobile);
			record.setTotalPoint(totalPoint);
			record.setRemainPoint(remainPoint);
			record.setUserCardId(userCard.getId());
			record.setBalance(userCard.getBalance());

			record.setType(Constants.UserCardRecord.TYPE_WEIXIN);
			record.setValidDate(date);
			record.setId(idService.getId());
			userCardRecordDao.save(record);
		}
	}
	
	/**
	 * 发送积分到智慧牛接口
	 * @param point
	 * @param mobile
	 * @param storeId
	 */
	public void sendPoint(Double point,String mobile,Long storeId){
		// TODO 先异步执行，后面改成mq处理
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Partner partner = partnerService.findByStoreId(storeId);
				if (partner != null){
					String token = partner.getToken();
					if (StringUtils.isNotBlank(token)){
						Map<String, String> map = new HashMap<String, String>();
						map.put("total_score", point.toString());
						map.put("tel", mobile);
						map.put("token", token);
						
						String ret = HttpRequest.post("http://wx.cowinmobile.com/api/tp_userinfo.php").body(JsonUtils.objectToJson(map)).send().bodyText();
						logger.debug("sendPoint  ret = {}",ret);
					}
				}
			}
		});
		
	}

	public List<UserCard> findByStoreId(Long storeId) {
		return userCardRecordDao.findByStoreId(storeId);
	}

	public String findFirstDateByStoreId(Long storeId) {
		
		return userCardDao.findFirstDateByStoreId(storeId);
	}
}
