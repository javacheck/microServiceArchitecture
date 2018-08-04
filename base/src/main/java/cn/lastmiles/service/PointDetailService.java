package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.PointRule;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PointDetailDao;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserCardDao;
import cn.lastmiles.dao.UserCardRecordDao;
import cn.lastmiles.dao.UserDao;
import cn.lastmiles.dao.UserLevelDefinitionDao;

@Service
public class PointDetailService {
	@Autowired
	private UserCardRecordDao userCardRecordDao;

	@Autowired
	private OrderServise orderService;

	@Autowired
	private IdService idService;

	@Autowired
	private PointRuleService pointRuleService;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private UserLevelDefinitionDao userLevelDefinitionDao;

	@Autowired
	private PointDetailDao pointDetailDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserCardDao userCardDao;
	@Autowired
	private UserCardService userCardService;

	private final static Logger logger = LoggerFactory
			.getLogger(PointDetailService.class);

	public void savePointDetail(Order order) {// 入参订单ID和消费金额
		Store store = storeDao.findById(order.getStoreId());
		PointRule pointRule = null;
		Store topStore = null;
		if (store.getOrganizationId() != null && store.getUnifiedPointRule().intValue() == Constants.Store.UNIFIEDPOINTRULE) {
			// 如果是连锁商家,设置使用总部规则，找总部设置规则
			topStore = storeDao.findTopStore(order.getStoreId());
			pointRule = pointRuleService
					.getPointRuleByStoreId(topStore.getId());// 查找总部积分规则
		} else if (store.getOrganizationId() == null){
			pointRule = pointRuleService.getPointRuleByStoreId(order
					.getStoreId());// 通过店铺ID查找积分规则
		}
		logger.debug("pointRule = {}", pointRule);
		Double discount = 101D;
		User user = userDao.findById(order.getUserId());
		double point = 0;
		Double totalConsumption = 0d;
		String levelName = "";
		Integer levelMode = Constants.UserLevelDefinition.AUTOMODE;
		Long levelId = null;
		Integer defType = null;//0表示积分，1表示消费总额
		
		Double totalPoint = 0d;
		Date date = new Date();
		
		if (store.getUnifiedPointRule().intValue() == Constants.Store.UNIFIEDPOINTRULE) {
			user = userDao.findUserForChainStore(user.getMobile(),
					order.getStoreId());
		} else {
			user = userDao.findUserForStore(user.getMobile(),
					order.getStoreId());
		}

		Double getPoint = 0d;//获取的积分
		if (pointRule != null) {
			if (pointRule.getStatus().intValue() == 1 && pointRule.getRestriction().intValue() == 0) {// 1 积分规则有效  无限制
				if (pointRule.getValidTime() != null) {// 有效时间为表无限
					date = DateUtils.addYear(new Date(),
							pointRule.getValidTime());
				} else {
					date = DateUtils.addYear(new Date(), 100);// 一百表是从开始时间加一百年表无限
				}

				logger.debug("pointRule user = {}", user);
				if (user != null) {
					getPoint = NumberUtils.divideForInt(
							order.getActualPrice(),
							(NumberUtils.divide(pointRule.getMoney(),
									pointRule.getPoint())))*1.00;// 积分
				}
			}
		}
		
		//使用了积分支付
		if (order.getPoint() != null && order.getPoint().doubleValue() > 0){
			point = -order.getPoint();
		}
		
		//
		
		if (user != null) {
			userCardService.sendPoint(point, user.getMobile(), order.getStoreId());
			UserCard userCard = userCardDao.findByMobileAndStoreId(user.getMobile(), order.getStoreId());
			
			totalPoint = NumberUtils.add(userCard.getTotalPoint(), getPoint);
			Double remainPoint = NumberUtils.add(userCard.getPoint(), point);
			remainPoint = NumberUtils.add(remainPoint, getPoint);
			
			logger.debug("getActualPrice:{},getCashPrice:{},getTotalConsumption:{},totalConsumption:{}",order.getActualPrice(),order.getCashPrice(),userCard.getTotalConsumption(),totalConsumption);
			totalConsumption = NumberUtils.add(userCard.getTotalConsumption(),
					order.getActualPrice());
			logger.debug("totalConsumption:{}",totalConsumption);
			totalConsumption = NumberUtils.add(totalConsumption,
					order.getCashPrice());
			logger.debug("totalConsumption  22222222 :{}",totalConsumption);
			
//			if (pointRule != null){
			
				List<UserLevelDefinition> defList = null;
				// 处理等级
				if (topStore != null) {
					defList = userLevelDefinitionDao
							.findByStoreId(topStore.getId());
				} else {
					defList = userLevelDefinitionDao.findByStoreId(store.getId());
				}
	
				if (defList != null && !defList.isEmpty()) {
					for (UserLevelDefinition def : defList) {
						// 0表示积分，1表示消费总额
						if (def.getType().intValue() == 0
								&& def.getDiscount() != null) {
							if (totalPoint.doubleValue() >= def.getPoint()
									.doubleValue()) {
								discount = def.getDiscount();
								levelName = def.getName();
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
								levelName = def.getName();
								levelMode = def.getMode();
								levelId = def.getId();
								defType = def.getType();
								break;
							}
						}
					}
				}
			
//			}
			// 保存支付记录，更新会员卡的积分、消费金额
			if (discount.doubleValue() == 101D) {
				discount = null;
			}
			
			logger.debug("levelName:{},levelMode:{},leveId:{},totalConsumption:{}",levelName,levelMode,levelId,totalConsumption);
			
			if (ObjectUtils.equals(levelMode, Constants.UserLevelDefinition.AUTOMODE)){
				userCardDao.updatePointAndLevel(user.getMobile(),
						order.getStoreId(), remainPoint, totalPoint,
						levelId,null,defType);
			}else {
				userCardDao.updatePointAndLevel(user.getMobile(),
						order.getStoreId(), remainPoint, totalPoint,
						null,levelId,defType);
			}
			
			userCardDao.updateTotalConsumptionById(totalConsumption, userCard.getId());
			if (userCard != null) {
				UserCardRecord record = new UserCardRecord();
				record.setAccountId(order.getAccountId());
				record.setActualAmount(order.getActualPrice());
				record.setCreatedTime(new Date());
				record.setOrderId(order.getId());
				record.setStoreId(order.getStoreId());
//				if (point != 0){
					//如果消耗了积分，只记录消耗的积分
//					record.setPoint(point);
//				}else {
				record.setPoint(getPoint + point);
//				}
				record.setMobile(user.getMobile());
				record.setTotalPoint(totalPoint);
				record.setRemainPoint(remainPoint);
				record.setUserCardId(userCard.getId());
				record.setBalance(userCard.getBalance());
				record.setValidDate(date);

				if (point >= 0){
					record.setType(Constants.UserCardRecord.TYPE_CONSUMER);
					record.setValidDate(date);
				}else {
					record.setType(Constants.UserCardRecord.TYPE_DEDUCTION);
				}
				record.setId(idService.getId());
				userCardRecordDao.save(record);
			}
			order.setGetPoint(getPoint);
			order.setRemainPoint(remainPoint);
			orderService.updatePoint(order.getPoint(), getPoint, order.getId());
		}
	}
}
