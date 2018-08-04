package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.APIResponse;
import cn.lastmiles.bean.CashGift;
import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.OrderPrePay;
import cn.lastmiles.bean.OrderRefund;
import cn.lastmiles.bean.OrderReturnGoods;
import cn.lastmiles.bean.PaymentModeInfo;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.SettlementsRecord;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.bean.movie.RoomPackage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.AddressDao;
import cn.lastmiles.dao.OrderDao;
import cn.lastmiles.dao.OrderItemDao;
import cn.lastmiles.dao.OrderRefundDao;
import cn.lastmiles.dao.ProductStockDao;
import cn.lastmiles.dao.SalesPromotionDao;
import cn.lastmiles.exception.orderAddException;
import cn.lastmiles.getui.PushService;
import cn.lastmiles.pay.unionpay.UnionPayUtils;
import cn.lastmiles.pay.wx.WXPayBean;
import cn.lastmiles.pay.wx.WXPayUtils;
import cn.lastmiles.service.print.PrintServiceImpl;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class OrderServise {
	private final static Logger logger = LoggerFactory
			.getLogger(OrderServise.class);
	@Autowired
	private OrderRefundDao orderRefundDao;
	@Autowired
	private ProductStockDao productStockDao;
	@Autowired
	private IdService IdService;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private OrderPrePayService orderPrePayService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private UserService userService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderItemServise orderItemServise;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private CashGiftService cashGiftService;
	@Autowired
	private PayAccountService payAccountService;
	@Autowired
	private PrintService printService;
	@Autowired
	private SettlementsRecordService settlementsRecordService;
	@Autowired
	private SalesPromotionService salesPromotionService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomCategoryService roomCategoryService;

	@Autowired
	private SalesPromotionDao salesPromotionDao;
	@Autowired
	private RoomPackageService roomPackageService;
	@Autowired
	private OrderReturnGoodsService orderReturnGoodsService;
	@Autowired
	private UserCardService userCardService;
	public Long save(Order order) {
		if (order.getId() == null) {
			order.setId(IdService.getOrderId());
		}
		return orderDao.save(order) ? order.getId() : -100L;
	}
	
	@Transactional
	public String addProduct(Order order) {
		Double price = 0.0D;
		price = order.getPrice();
		Integer duration = order.getDuration();
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem == null) {
				logger.error("参数错误");
				return "参数错误";
			}
			orderItem.setOrderId(order.getId());
			orderItem.setStatus(Constants.OrderItem.STATUS_NO_PAY);
			if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_PRODUCT) {// 如果是商品
				ProductStock productStock = productStockService.findById(orderItem.getStockId());// 找到库存
				if (productStock == null) {
					logger.error("库存ID未找到");
					return "库存ID未找到";
				}
				if (!productStockService.checkStock(orderItem.getStockId(),
						orderItem.getAmount())) {//检测是否有库存
					logger.error("库存ID未找到或无库存");
					return "库存ID未找到或无库存";
				}
				if(!orderItemServise.decreaseStock(order.getOrderItems())){//处理库存 减少库存
					logger.error("修改库存出现异常");
					return "修改库存出现异常";
				}
				orderItem.setName(productStock.getAttributeName());
				orderItem.setPrice(productStock.getPrice());
			} else if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_ROOM) {
				Room room = roomService.findById(orderItem.getStockId());
				RoomCategory roomCategory = roomCategoryService.findById(room
						.getCategoryId());
				orderItem.setName(room.getNumber() + "房(" + roomCategory.getName() + ")");
				orderItem.setPrice(roomService.reckonRoomPrice(
						order.getCreatedTime(), orderItem.getAmount().intValue(),
						order.getStoreId(), room,order.getUserId().longValue()!=-1L));
				duration = duration+orderItem.getAmount().intValue();
				logger.debug("isUser is :{}",order.getUserId().longValue()!=-1L);
				logger.debug("orderItem price is :{}",roomService.reckonRoomPrice(order.getCreatedTime(), orderItem.getAmount().intValue(),order.getStoreId(), room,false));
				logger.debug("orderItem Userprice is :{}",roomService.reckonRoomPrice(order.getCreatedTime(), orderItem.getAmount().intValue(),order.getStoreId(), room,true));
			} else if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_PACKAGE) {
				RoomPackage roomPackage = roomPackageService.findById(orderItem.getStockId());
				orderItem.setName(roomPackage.getName() + "套餐");
				orderItem.setPrice(roomPackage.getPrice());
				duration = duration+roomPackage.getDuration();
			}
			Double amount = orderItem.getAmount();
			if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_ROOM) {
				amount = 1d;
			}
			price = NumberUtils.add(price, NumberUtils.multiply(
					String.valueOf(orderItem.getPrice()), amount.toString()));
		}
//		order.setPrice(price);
		this.updatePrice(order.getId(), order.getPrice());
		this.updateDuration(order.getId(),duration);
		orderItemServise.addProduct(order.getOrderItems());
		return "000000";
	}
	@SuppressWarnings("unchecked")
	public Page appList(String beginTime, String endTime, Integer paymentMode,
			String mobile, String orderId, String storeIdString,
			Integer source, Integer status,Integer haveReturnGoods,String memo, Page page) {
		List<Order> Returnorders = new ArrayList<Order>();
		page = orderDao.appList(beginTime, endTime, paymentMode, mobile, orderId,
				storeIdString, source, status,haveReturnGoods,memo, page);
		List<Order> orders = (List<Order>) page.getData();
		for (Order order : orders) {
			Returnorders.add(this.filling(order));
		}
		page.setData(Returnorders);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public Page list(String beginTime, String endTime, Integer paymentMode,
			String mobile, String orderId, String storeIdString,
			Integer source, Integer status,Integer haveReturnGoods,String memo, Page page) {
		List<Order> Returnorders = new ArrayList<Order>();
		page = orderDao.list(beginTime, endTime, paymentMode, mobile, orderId,
				storeIdString, source, status,haveReturnGoods,memo, page);
		List<Order> orders = (List<Order>) page.getData();
		for (Order order : orders) {
			order = this.filling(order);
			
			List<OrderItem> orderItemList = order.getOrderItems();
			if( null != orderItemList && orderItemList.size()> 0 ){
				Double sum = 0D;
				for (OrderItem orderItem : orderItemList) {
					if( orderItem.getReturnNumber() > 0 ){ //有退货数量,则必定有退货金额
						sum = NumberUtils.add(sum, orderItem.getReturnPrice());					
					}
				}
				order.setSumReturnGoodsPrice(sum);
			}
			List<OrderReturnGoods> orgList = orderReturnGoodsService.findByOrderId(order.getId());
			if (orgList != null){
				Double sum = 0D;
				for (OrderReturnGoods org : orgList){
					sum = NumberUtils.add(sum, org.getReturnPrice());			
				}
				order.setSumReturnGoodsPrice(sum);
			}
			Returnorders.add(order);
		}
		
		page.setData(Returnorders);
		return page;
	}
	
	public List<Order> findAppBySearch(String beginTime, String endTime,
			String storeIdString, String mobile, Integer paymentMode,
			String orderId, Integer source, Integer status,String memo) {
		List<Order> Returnorders = new ArrayList<Order>();
		List<Order> orders = orderDao.findAppBySearch(beginTime, endTime,
				paymentMode, mobile, orderId, storeIdString, source, status,memo);
		for (Order order : orders) {
			Returnorders.add(this.filling(order));
		}
		return Returnorders;
	}

	@SuppressWarnings("unchecked")
	public Page posList(String beginTime, String endTime, String mobile,
			String orderId, Long storeId, Integer status, Page page) {
		List<Order> Returnorders = new ArrayList<Order>();
		page = orderDao.posList(beginTime, endTime, mobile, orderId, storeId,
				status, page);
		List<Order> orders = (List<Order>) page.getData();
		for (Order order : orders) {
			Returnorders.add(this.filling(order));
		}
		page.setData(Returnorders);
		return page;
	}
	
	public List<Order> findPOSBySearch(String beginTime, String endTime,
			String storeIdString, String mobile, Integer paymentMode,
			String orderId, Integer source, Integer status,String memo,Integer haveReturnGoods) {
		List<Order> Returnorders = new ArrayList<Order>();
		List<Order> orders = orderDao.findPOSBySearch(beginTime, endTime,
				paymentMode, mobile, orderId, storeIdString, source, status,memo,haveReturnGoods);
		for (Order order : orders) {
			Returnorders.add(this.filling(order));
			
			List<OrderReturnGoods> orgList = orderReturnGoodsService.findByOrderId(order.getId());
			if (orgList != null){
				Double sum = 0D;
				for (OrderReturnGoods org : orgList){
					sum = NumberUtils.add(sum, org.getReturnPrice());			
				}
				order.setSumReturnGoodsPrice(sum);
			}
		}
		return Returnorders;
	}
	
	/**
	 * 统计销售额
	 * 
	 * @param accountId
	 * @param storeId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Double calculatSales(Long accountId, Long storeId, Date beginDate,
			Date endDate) {
		return orderDao.calculatSales(accountId, storeId, beginDate, endDate);
	}

	/**
	 * 通过ID查找
	 * 
	 * @param id
	 * @return
	 */
	public Order findById(Long id) {
		return this.filling(orderDao.findById(id));
	}

	public Order findByIdNoRoomId(Long id) {
		return this.filling(orderDao.findByIdNoRoomId(id));
	}
	
	public Order findByOrderId(Long orderId) {
		return orderDao.findById(orderId);
	}

	/**
	 * 查询未支付订单根据ID
	 * 
	 * @param id
	 * @return
	 */
	public Order findUnpaidOrderById(Long id) {
		return this.filling(orderDao.findByIdAndStatus(id, 0));
	}

	/**
	 * 关联表封装
	 * 
	 * @param order
	 * @return
	 */
	public Order filling(Order order) {
		if (order == null) {
			return null;
		}
		order.setAccount(accountService.findById(order.getAccountId()));
		if (order.getUserId() != null && order.getUserId().intValue() != -1){
			order.setUser(userService.findById(order.getUserId()));
			logger.debug("uc={}",userService.findById(order.getUserId()));
		}
		
		Store store = storeService.findById(order.getStoreId());
		
		if (order.getUser() != null){
			logger.debug("uc={}",userCardService.findByMobileAndStoreId(order.getUser().getMobile(), order.getStoreId()));
			order.setUserCard(userCardService.findByMobileAndStoreId(order.getUser().getMobile(), order.getStoreId()));
		}
		
		store.setLogo(FileServiceUtils.getFileUrl(store.getLogo()));
		order.setStore(store);
		order.setOrderItems(orderItemServise.findByOrderId(order.getId()));
		order.setOrderReturnGoods(orderReturnGoodsService.findByOrderId(order.getId()));
		order.setAddress(addressDao.get(order.getAddressId()));
		if (order.getShipAmount() == null) {
			order.setShipAmount(0D);
		}
		order.setSumPrice(NumberUtils.add(order.getPrice(),
				order.getShipAmount()));

		// 2016.03.09 返回给安卓端优惠卷信息(优惠金额)
		order.setCashGift(cashGiftService.findById(order.getCashGiftId()));
		return order;
	}

	/**
	 * 保存 app 订单
	 * 
	 * @param order
	 * @return
	 */
	@Transactional
	public Long saveOrderAndOrderItem(Order order) {
		Long orderId = this.save(order);
		for (OrderItem orderItem : order.getOrderItems()) {
			orderItem.setOrderId(orderId);
			orderItemServise.save(orderItem);
			// 处理库存
			Long stockId = orderItem.getStockId();
			productStockService.decreaseStock(stockId, orderItem.getAmount());

			// 处理促销的
			if (orderItem.getSalesPromotionCategoryId() != null) {
				salesPromotionService.updateSalesNum(orderItem.getAmount().intValue(),
						orderItem.getStockId(),
						orderItem.getSalesPromotionCategoryId());
			}
		}
		// 处理优惠卷
		if (order.getCashGiftId() != null) {
			cashGiftService.useCashGift(order.getCashGiftId(),
					order.getStoreId(), order.getUserId());
		}
		// 处理余额
		if (order.getBalance() != null
				&& order.getBalance().doubleValue() > 0.0) {
			payAccountService.reduceBalance(order.getBalance(),
					order.getUserId(),
					Constants.PayAccount.PAY_ACCOUNT_TYPE_USER, order.getId());
		}
		return orderId;
	}

	/**
	 * 保存
	 * 
	 * @param order
	 * @return
	 */
	@Transactional
	public Order posSaveOrderAndOrderItem(Order order) {
		order.setId(IdService.getOrderId());
		Long orderId = this.save(order);
		for (OrderItem orderItem : order.getOrderItems()) {
			orderItem.setOrderId(orderId);
			orderItemServise.save(orderItem);
		}
		return order;
	}

	/**
	 * 修改订单状态 支付方式 实际支付金额 会员ID 折扣
	 * 
	 * @param orderId
	 * @param status
	 * @param paymentMode
	 * @param price
	 * @param userId
	 * @param cashPrice
	 * @param discountc
	 * @return
	 */
	public boolean update(Long orderId, Integer status, Integer paymentMode,
			Double price, Long userId, Double discount, Double cashPrice,
			Double point,Double change) {
		return orderDao.update(orderId, status, paymentMode, price, userId,
				discount, cashPrice, point,change);
	}
	
	public boolean update(Long orderId, Integer status, Integer paymentMode,
			Double price, Long userId, Double discount, Double cashPrice,
			Double point,Double change,Double fullSubstractAmount ,
			Double promotionAmount,
			Double promotionDiscount,
			Double reductionAmount,
			Double reductionDiscount,String fullSubstractCondition){
		return orderDao.update(orderId, status, paymentMode, price, userId,
				discount, cashPrice, point,change,fullSubstractAmount,promotionAmount,promotionDiscount,reductionAmount,reductionDiscount,fullSubstractCondition);
	}

	/**
	 * 交接班统计
	 * 
	 * @param accountId
	 * @param storeId
	 * @param startDate
	 * @param endTime
	 * @return
	 */
	public List<PaymentModeInfo> StatisticsShiftWorkLog(Long accountId,
			Long storeId, Date startDate, Date endTime) {
		return orderDao.StatisticsShiftWorkLog(accountId, storeId, startDate,
				endTime);
	}

	public Order findByStoreId(Long id) {
		return orderDao.findByStoreId(id);
	}

	public Page AppList(String beginTime, String endTime, String orderId,
			Long userId, Page page, Integer status) {
		page = orderDao.AppList(beginTime, endTime, orderId, userId, page,
				status, Constants.Order.ORDER_SOURCE_APP);

		return page;
	}

	public boolean appUpdate(Long orderId, Integer status, Integer paymentMode,
			Long userId) {
		return orderDao.appUpdate(orderId, status, paymentMode, userId);
	}

	/**
	 * 2用户取消
	 * 
	 * @param userId
	 * @param orderId
	 * @param status
	 */
	public void userCancel(Long userId, Long orderId, Integer status) {
		logger.debug("userCancel userId is：{},orderId is：{},status is：{},",
				userId, orderId, status);
		cancel(userId, orderId, Constants.Order.TYPE_USER_CANCEL);
		// 推送通知
		PushService
				.pushToStore(new Message("取消订单",
						Constants.MessageType.MESSAGE_ORDER_USER_CANCEL_CONTENT
								.replaceAll("#title#", orderId.toString()),
						userId,
						Constants.MessageType.MESSAGE_ORDER_USER_CANCEL,
						orderId, null));
	}

	/**
	 * 取消
	 * 
	 * @param userId
	 * @param orderId
	 * @param status
	 */
	@Transactional
	public void cancel(Long userId, Long orderId, Integer status) {
		Order order = orderDao.findByIdAndUserId(orderId, userId);
		if (order != null) {
			orderDao.updateStatus(userId, orderId, status);
			orderDao.orderHandel(status, orderId);
			// 取消, 恢复库存数量
			List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
			for (OrderItem orderItem : orderItemList) {
				// 处理库存
				Long stockId = orderItem.getStockId();
				productStockService.decreaseStock(stockId,
						-orderItem.getAmount());
				if (orderItem.getSalesPromotionCategoryId() != null) {
					salesPromotionService.updateSalesNum(
							-orderItem.getAmount().intValue(), stockId,
							orderItem.getSalesPromotionCategoryId());
				}
				if (orderItem.getPromotionId() != null) {
					Promotion promotion = promotionService.findById(orderItem
							.getPromotionId());
					if (promotion != null
							&& promotion.getTotal().intValue() != -1) {
						promotionService.lossStock(promotion.getId(),
								orderItem.getAmount().intValue());
					}
				}
			}
			if (order.getCashGiftId() != null) {
				cashGiftService.updateStatus(order.getCashGiftId(), null, null,
						Constants.CashGift.STATUSNORMAL);// 优惠卷改为 未使用
			}
			if (order.getBalance() != null
					&& order.getBalance().doubleValue() > 0d) {
				payAccountService.revertBalance(userId,
						Constants.PayAccount.PAY_ACCOUNT_TYPE_USER,
						order.getBalance(), order.getId());// 退回使用余额
			}
			if (order.getPaymentMode() != null
					&& order.getPaymentMode().intValue() == Constants.Order.PAYMENT_APP_BALANCE_PAY) {
				// 余额支付
				orderDao.orderHandel(Constants.Order.TYPE_REFUND_ING, orderId);
				orderDao.orderHandel(Constants.Order.TYPE_REFUND_SUCCESS,
						orderId);
			} else if (order.getStatus().intValue() != Constants.Order.TYPE_NO_PAY
					&& order.getPaymentMode() != null
					&& order.getPaymentMode().intValue() != Constants.Order.PAYMENT_LINE_PAY) {
				OrderRefund orderRefund = new OrderRefund();
				// 银联需要新的orderId
				orderRefund.setId(IdService.getOrderId());
				orderRefund.setOrderId(orderId);
				orderRefund.setRefundPrice(order.getActualPrice());
				orderRefund.setTotalPrice(order.getActualPrice());
				orderRefund.setStatus(Constants.OrderRefund.STATUS_ING);
				orderRefund.setChannel(order.getPaymentMode());
				orderRefundDao.save(orderRefund);

				// 退款中
				orderDao.updateStatus(userId, orderId,
						Constants.Order.TYPE_REFUND_ING);
			}
			orderItemServise.updateStatus(orderId);// 更改订单详情里面的状态为取消
		}
	}

	/**
	 * 3系统取消
	 * 
	 * @param userId
	 * @param orderId
	 * @param status
	 */
	@Transactional
	public void taskCancel(Long userId, Long orderId, Integer status) {
		cancel(userId, orderId, Constants.Order.TYPE_SYS_CANCEL);
	}

	public void typeChangeByorderId(Long orderId, Integer status) {
		if (status.intValue() == 2) {// 取消
		// if (orderDao.findById(orderId).getStatus().intValue() != 7) {// 线 上
		// orderDao.orderHandel(status, orderId);// 已取消
		// orderDao.typeChangeByorderId(status, orderId);
		// } else {
		// orderDao.orderHandel(status, orderId);// 先取消
		// orderDao.orderHandel(Constants.Order.TYPE_CLOSED, orderId);// 后关闭
		// orderDao.typeChangeByorderId(Constants.Order.TYPE_CLOSED,
		// orderId);
		// }
			throw new RuntimeException("订单取消有问题");
		} else {// 其它状态操作
			if (status.intValue() == Constants.Order.TYPE_WAITING_DELIVER) {// 确认
				orderDao.orderHandel(Constants.Order.TYPE_PAY, orderId);// 改变状态
			} else if (status.intValue() == Constants.Order.TYPE_WAITING_RECEIVING) {// 发货
				orderDao.orderHandel(Constants.Order.TYPE_WAITING_DELIVER,
						orderId);// 改变状态
			}
			orderDao.typeChangeByorderId(status, orderId);
		}

	}

	public void offlinePay(Long orderId, Integer paymentMode) {
		orderDao.offlinePay(orderId, paymentMode);
	}

	public Order posInfo(Long id) {
		return this.filling(orderDao.findById(id));
	}

	public void udpatePaymentMode(Long orderId, Integer paymentMode) {
		orderDao.udpatePaymentMode(orderId, paymentMode);
	}

	/**
	 * 订单评价，订单完成
	 * 
	 * @param orderId
	 * @param evaluate
	 */
	@Transactional
	public void evaluate(Long userId, Long orderId, String evaluate) {
		orderDao.updateStatus(userId, orderId, Constants.Order.TYPE_FINISHED);
		if (StringUtils.isNotBlank(evaluate)) {
			orderDao.evaluate(userId, orderId, evaluate);
		}
		orderDao.orderHandel(Constants.Order.TYPE_WAITING_EVALUATE, orderId);
	}

	/**
	 * 用户签收，状态变成待评价
	 * 
	 * @param userId
	 * @param orderId
	 */
	@Transactional
	public void sign(Long userId, Long orderId) {
		orderDao.updateStatus(userId, orderId,
				Constants.Order.TYPE_WAITING_EVALUATE);
		orderDao.orderHandel(Constants.Order.TYPE_WAITING_RECEIVING, orderId);
	}

	public List<Order> posGetNotPrintedOrder(Long storeId) {
		List<Order> Returnorders = new ArrayList<Order>();
		List<Order> orderList = orderDao.posGetNotPrintedOrder(storeId);
		for (Order order : orderList) {
			Returnorders.add(isCopyFillingMethod(order));
		}
		return Returnorders;
	}

	public Order isCopyFillingMethod(Order order) {
		if (order == null) {
			return null;
		}
		order.setOrderItems(orderItemServise.posFindByOrderId(order.getId()));
		return order;
	}

	public void updatePrinted(Long userId, Long orderId) {
		orderDao.updatePrinted(userId, orderId, Constants.Order.PRINTED_OK);
	}

	public void changePrinted(Long orderId, int printed) {
		orderDao.changePrinted(orderId, printed);

	}

	public void changeWifiPrinted(Long orderId, int printed) {
		orderDao.changeWifiPrinted(orderId, printed);
	}

	@Transactional
	public void dealOrderPercentage(Order order) {
		logger.debug("dealOrderPercentage is deal start order is {}", order);
		if (order == null) {
			logger.error("dealOrderPercentage --> 错误!!!!订单为空");
			return;
		}
		Store store = shopService.findById(order.getStoreId());
		if (store == null) {
			logger.error("dealOrderPercentage --> 错误!!!!商店ID{}错误",
					order.getStoreId());
			return;
		}
		if (order.getPaymentMode() == null) {
			return;
		}
		if (order.isLineOffPayment() && order.getBalance().doubleValue() != 0) {// 如果是
																				// 线下支付
			payAccountService.AddfrozenAmount(store.getId(),
					Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE,
					order.getBalance());// 店铺账户添加金额.
			settlementsRecordService.save(new SettlementsRecord(order.getId(),
					store.getId(), Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE,
					order.getBalance()));// 保存商店记录
			orderDao.updateSettlement(order.getId(),
					Constants.Order.SETTLEMENT_STATUS_DONT);
			return;
		}
		if (order.isLineOnPayment()) {// 如果上线上支付
			Double dealPrice = order.getActualPrice();
			Double balance = order.getBalance();
			// if
			// (order.getShipTypeId().equals(Constants.Order.DELIVERY_ON_THIRD))
			// {//如果送第三方配送 则加上配送费
			// NumberUtils.add(dealPrice, order.getShipAmount());
			// }
			// 计算线上手续费
			Double onLinePrice = store.getOnLineRateType().intValue() == 0// 如果是按照比率分成
			? NumberUtils.multiply(dealPrice, store.getOnLineRate(), 0.01D)// 比率分成
					: store.getOnLineRate();// 每笔分成
			logger.debug(
					"dealPrice is {},OnLineRate is {},onLinePrice is {},balance is {}",
					dealPrice, store.getOnLineRate(), onLinePrice, balance);
			Double agentPrice = NumberUtils.multiply(onLinePrice, 0.25D);// 代理商手续费
			Double platformPrice = NumberUtils
					.subtract(onLinePrice, agentPrice);// 平台手续费
			if (balance.doubleValue() != 0
					&& store.getOnLineRateType().intValue() == 0) {// 使用余额支付
																	// 并且是分成是按照每笔来分成的
				platformPrice = NumberUtils.add(platformPrice, NumberUtils
						.multiply(balance, store.getOnLineRate(), 0.01D));// 加上使用余额的手续费
			}
			Double stopPrice = NumberUtils.subtract(
					NumberUtils.add(dealPrice, balance), agentPrice,
					platformPrice);// 店铺手续费

			// 以下为存储记录过程 不涉及逻辑
			logger.debug(
					"agentPrice is :{},platformPrice is :{},stopPrice is :{}",
					agentPrice, platformPrice, stopPrice);
			payAccountService.AddfrozenAmount(store.getAgentId(),
					Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT, agentPrice);// 代理商账户添加金额
			payAccountService.AddfrozenAmount(store.getId(),
					Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE, stopPrice);// 店铺账户添加金额
			payAccountService.AddfrozenAmount(
					Constants.PayAccount.PAY_ACCOUNT_OWNERID_PLATFORMPRICE,
					Constants.PayAccount.PAY_ACCOUNT_TYPE_PLATFORMP,
					platformPrice);// 平台添加金额
			logger.debug("AgentId is :{},storeId is :{}", store.getAgentId(),
					store.getId());
			SettlementsRecord settlementsRecord = null;
			settlementsRecord = new SettlementsRecord(order.getId(),
					store.getAgentId(),
					Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT, agentPrice);
			settlementsRecordService.save(settlementsRecord);// 保存代理商记录
			settlementsRecord = new SettlementsRecord(order.getId(),
					store.getId(), Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE,
					stopPrice);
			settlementsRecordService.save(settlementsRecord);// 保存商店记录
			settlementsRecord = new SettlementsRecord(order.getId(),
					Constants.PayAccount.PAY_ACCOUNT_OWNERID_PLATFORMPRICE,
					Constants.PayAccount.PAY_ACCOUNT_TYPE_PLATFORMP,
					platformPrice);
			settlementsRecordService.save(settlementsRecord);// 保存平台记录
			orderDao.updateSettlement(order.getId(),
					Constants.Order.SETTLEMENT_STATUS_DONT);
		}
	}

	public Page posGetAppOrder(Long storeId, String orderIdOrProductName,
			Integer status, Page page) {
		List<Order> Returnorders = new ArrayList<Order>();
		page = orderDao.posGetAppOrder(storeId, orderIdOrProductName, status,
				page);
		@SuppressWarnings("unchecked")
		List<Order> orderList = (List<Order>) page.getData();
		for (Order order : orderList) {
			Returnorders.add(isCopyFillingMethod(order));
		}
		page.setData(Returnorders);
		return page;
	}

	public List<Order> findNoPrint() {
		return orderDao.findNoPrint();
	}

	@Transactional
	public void close(Long id) {
		orderDao.close(id);
		orderDao.orderHandel(Constants.Order.TYPE_CLOSED, id);
	}

	// 退款
	@Transactional
	public void refund(Long refundId, int status) {
		OrderRefund refund = orderRefundDao.get(refundId);
		orderDao.updateStatus(refund.getOrderId(), status);
		if (status == Constants.Order.TYPE_REFUND_FAIL) {
			orderDao.orderHandel(Constants.Order.TYPE_REFUND_FAIL,
					refund.getOrderId());
			orderRefundDao.updateStatus(refundId,
					Constants.OrderRefund.STATUS_FAILURE);
		} else {
			orderDao.orderHandel(Constants.Order.TYPE_REFUND_SUCCESS,
					refund.getOrderId());
			orderRefundDao.updateStatus(refundId,
					Constants.OrderRefund.STATUS_SUCESS);
		}
	}

	public List<Order> findWifiNoPrint() {
		return orderDao.findWifiNoPrint();
	}

	public boolean isFirstOrder(Long userId) {
		return orderDao.isFirstOrder(userId);
	}

	public boolean updateStoreDerate(Long orderId, Double price) {
		return orderDao.updateStoreDerate(orderId, price);
	}

	@Transactional
	public APIResponse appAdd(Order order, User user, String ip)
			throws orderAddException {
		order.setId(IdService.getOrderId());
		order.setCreatedTime(new Date());
		order.setSource(Constants.Order.ORDER_SOURCE_APP);// 设置订单来源
		order.setStatus(Constants.Order.TYPE_NO_PAY); // 设置订单状态
		order.setUserId(user.getId());
		logger.debug("orderId is {}", order.getId());
		Double productPrice = 0.0;
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Long> combinations = new HashSet<Long>();
		Set<Long> discounts = new HashSet<Long>();
		for (OrderItem orderItem : order.getOrderItems()) {
			ProductStock productStock = productStockService.findById(orderItem
					.getStockId());// 找到库存
			orderItem.setUserId(order.getUserId());
			if (productStock == null) {
				return new APIResponse("030104", "商品["
						+ orderItem.getProductName() + "]不存在");
			}
			if (!productStockService.checkStock(orderItem.getStockId(),
					orderItem.getAmount())) {// 检测是否有库存
				return new APIResponse("030105", "商品["
						+ productStock.getProductName() + "]库存不足");
			}
			/*
			 * if (orderItem.getCategoryId().longValue() < 0L){ //
			 * 如果productStock是下架状态，检查促销是否有 ，促销存在，就检查促销里面的限制条件 SalesPromotion
			 * promotion =
			 * salesPromotionService.getByStockId(orderItem.getStockId
			 * (),-orderItem.getCategoryId()); if (promotion != null){
			 * orderItem.setPrice(promotion.getPrice());
			 * orderItem.setSalesPromotionCategoryId
			 * (promotion.getSalesPromotionCategoryId()); if
			 * (promotion.getSalesNum() != -1 && promotion.getSalesNum() <
			 * orderItem.getAmount().intValue()){ return new
			 * APIResponse("030105",
			 * "商品["+productStock.getProductName()+"]促销数量不足"); }
			 * 
			 * //判断是否可以购买 SalesPromotionCategory spc =
			 * salesPromotionCategoryService
			 * .findById(promotion.getSalesPromotionCategoryId()); Integer
			 * buyNum = spc.getBuyNum(); if (buyNum.intValue() != -1){ Integer
			 * count = orderItemService.countPromotion(spc.getId(),
			 * SecurityUtils.getUserId());
			 * logger.debug("促销分类 {},buyNum = {},count= {},amount={}"
			 * ,spc.getName(),buyNum,count,orderItem.getAmount()); if (count !=
			 * null){ if ((count.intValue() + orderItem.getAmount().intValue())
			 * > buyNum.intValue()){ return new APIResponse("030105",
			 * "商品["+productStock.getProductName()+"]促销,一个用户只能购买"+buyNum+"件"); }
			 * } } }else { return new APIResponse("030105",
			 * "商品["+productStock.getProductName()+"]已经过促销时间"); }
			 * 
			 * }else { orderItem.setPrice(productStock.getPrice()); }
			 */
			if (orderItem.getPromotionId() != null) {
				Promotion promotion = promotionService.findById(orderItem
						.getPromotionId());
				if (promotion == null) {
					logger.error("折扣分类不存在 折扣ID" + orderItem.getPromotionId());
					return new APIResponse("030106", "折扣分类不存在 折扣ID"
							+ orderItem.getPromotionId());
				}
				if (promotion.getTotal().intValue() != -1) {// 如果限制数量
					if (promotion.getTotal().intValue() < orderItem.getAmount()
							.intValue()) {
						throw new orderAddException(orderItem.getId()
								.toString());
					}
					promotionService.lossStock(promotion.getId(),
							orderItem.getAmount().intValue());
				}
				if (!discounts.contains(promotion.getId())) {
					discounts.add(promotion.getId());
					map.clear();// 折扣统计
					map.put("orderId", order.getId());
					map.put("promotionId", promotion.getId());
					JdbcUtils.save("t_order_promotion", map);
				}
				/*
				 * //productPrice =
				 * NumberUtils.add(productPrice,NumberUtils.dealDecimal
				 * (NumberUtils
				 * .multiply(orderItem.getPrice(),Double.valueOf(orderItem
				 * .getAmount()),promotion.getDiscount(),0.1D), 2) ); }else{
				 * //productPrice =
				 * NumberUtils.add(productPrice,NumberUtils.multiply
				 * (orderItem.getPrice
				 * (),Double.valueOf(orderItem.getAmount())));
				 */
			}
			if (productStock.getPromotionId() != null) {// 组合优惠
				if (!combinations.contains(productStock.getPromotionId())) {
					combinations.add(productStock.getPromotionId());
					map.clear();// 组合统计
					map.put("orderId", order.getId());
					map.put("promotionId", productStock.getPromotionId());
					JdbcUtils.save("t_order_promotion", map);
				}
			}
		}

		if (ObjectUtils.equals(order.getShipTypeId(),
				Constants.Order.DELIVERY_USER)) {
			// 1到店自提 配送费0
			order.setShipAmount(0D);
		} else {
			Store store = storeService.findById(order.getStoreId());
			if (store.getShipAmount() != null
					&& store.getShipAmount().doubleValue() > 0) {
				if (store.getFreeShipAmount() == null
						|| store.getFreeShipAmount().doubleValue() <= order
								.getPrice().doubleValue()) {
					order.setShipAmount(0D);
					order.setFreeShipAmount(store.getShipAmount());
				} else {
					order.setShipAmount(store.getShipAmount());
				}
			} else {
				order.setShipAmount(0D);
			}
		}

		// @ TODO
		// totalPrice = NumberUtils.add(order.getShipAmount(), productPrice);

		Double actualPrice = order.getActualPrice();// 需要支付的金额

		if (order.getFullSubtractId() != null) {
			Promotion promotion = promotionService.findById(order
					.getFullSubtractId());
			if (promotion == null
					|| promotion.getType() != Constants.Promotion.TYPE_FULL_SUBTRACT) {
				logger.error("满减ID未查到 is {}", promotion);
				return new APIResponse("000015", "满减ID未查到");
			}
			map.clear();// 满减统计
			map.put("orderId", order.getId());
			map.put("promotionId", promotion.getId());
			JdbcUtils.save("t_order_promotion", map);
		}

		if (order.getFirstOrderId() != null) {// 用了首单优惠
			if (!this.isFirstOrder(user.getId())) {
				logger.error("用户不具备首单资格用户ID={}", user.getId());
				return new APIResponse("000013", "用户不具备首单资格");
			}
			Promotion promotion = promotionService.findById(order
					.getFirstOrderId());
			if (promotion == null
					|| promotion.getType() != Constants.Promotion.TYPE_FIRST_ORDER) {
				logger.error("首单ID未查到 is {}", promotion);
				return new APIResponse("000014", "首单ID未查到");
			}
			map.clear();// 首单统计
			map.put("orderId", order.getId());
			map.put("promotionId", promotion.getId());
			JdbcUtils.save("t_order_promotion", map);
		}

		// 验证cash gift ,不用支付配送费
		if (order.getCashGiftId() != null) {
			CashGift cashGift = cashGiftService.findOne(order.getCashGiftId(),
					user.getId(), order.getStoreId());
			if (cashGift == null) {
				// 暂时不做校验
				// return new APIResponse("000012", "无效的优惠劵");
			} else {
				if (cashGift.getType().intValue() == Constants.CashGift.TYPECASH) {
					// 现金卷
					// if (cashGift.getAmount().doubleValue() >= actualPrice
					// .doubleValue()) {
					// actualPrice = 0.0;
					// } else {
					// actualPrice = NumberUtils.subtract(actualPrice,
					// cashGift.getAmount());
					// }
					order.setCashGiftDesc(cashGift.getAmount().doubleValue()
							+ "元现金劵");
				} else if (cashGift.getType().intValue() == Constants.CashGift.TYPEDISCOUNT) {
					// 折扣卷
					// actualPrice = NumberUtils.multiply(productPrice,
					// NumberUtils.multiply(0.1, cashGift.getAmount()));
					// actualPrice = NumberUtils.dealDecimal(actualPrice);
					// actualPrice = NumberUtils.add(actualPrice,
					// order.getShipAmount());
					order.setCashGiftDesc(cashGift.getAmount().doubleValue()
							+ "折优惠劵");
				}
			}
		}

		if (order.getBalance() != null && order.getBalance().doubleValue() > 0) {
			Double balance = userService.getBalance(user.getId());
			if (order.getBalance().doubleValue() > balance.doubleValue()) {
				return new APIResponse("000013", "余额不正确");
			}
		}
		/**
		 * if (!ObjectUtils.equals(productPrice,order.getPrice())) {
		 * logger.error("后台计算金额"+productPrice+"传递过来金额"+order.getPrice()); return
		 * new
		 * APIResponse("000014","后台计算金额"+productPrice+"传递过来金额"+order.getPrice
		 * ()); }
		 */
		// order.setPrice(productPrice);
		// order.setActualPrice(actualPrice);
		if (actualPrice.doubleValue() == 0.0) {
			order.setStatus(Constants.Order.TYPE_PAY);
		}
		if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_LINE_PAY) {
			// 线下支付
			order.setStatus(Constants.Order.TYPE_PAY);
		}

		Long orderId = this.saveOrderAndOrderItem(order);

		// 微信支付
		if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_MICRO_CHANNEL_PAY) {
			WXPayBean tn = WXPayUtils.getAppPayTn(orderId.toString(),
					NumberUtils.multiply(order.getActualPrice(), 100)
							.intValue(), "随身社区订单：" + orderId, ip);
			tn.setOrderId(orderId.toString());
			tn.setPaymentMode(order.getPaymentMode().toString());

			OrderPrePay prePay = new OrderPrePay();
			prePay.setChannel(0);
			prePay.setOrderId(orderId);
			prePay.setNoncestr(tn.getNonceStr());
			prePay.setPrepayId(tn.getPrepayId());
			prePay.setTimestamp(tn.getTimeStamp());
			prePay.setSign(tn.getSign());
			orderPrePayService.save(prePay);

			return new APIResponse("000000", "成功", tn);
		} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_UNION_PAY) {
			// 银联
			String ret = UnionPayUtils.getAppPayTn(orderId.toString(),
					NumberUtils.multiply(order.getActualPrice(), 100)
							.intValue());
			logger.debug("ret >>>>>>>>>>>>>>>>>>> {}", ret);
			WXPayBean tn = new WXPayBean();
			tn.setPrice(order.getActualPrice().toString());
			tn.setPrepayId(ret);
			tn.setOrderId(orderId.toString());
			tn.setPaymentMode(order.getPaymentMode().toString());

			return new APIResponse("000000", "成功", tn);
		} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_LINE_PAY
				|| order.getPaymentMode().intValue() == Constants.Order.PAYMENT_APP_BALANCE_PAY) {
			WXPayBean tn = new WXPayBean();
			tn.setOrderId(orderId.toString());
			tn.setPaymentMode(order.getPaymentMode().toString());

			// 线下支付，打印订单
			PrintServiceImpl.print(orderId);

			// 调用商家推送
			PushService.pushToStore(new Message("新订单通知",
					Constants.MessageType.MESSAGE_ORDER_ADD_CONTENT.replaceAll(
							"#title#", orderId.toString()), user.getId(),
					Constants.MessageType.MESSAGE_ORDER_ADD, orderId, order
							.getStoreId()));
			return new APIResponse("000000", "成功", tn);
		}else if (order.getPaymentMode().intValue() == 10){
			// 手环
			WXPayBean tn = new WXPayBean();
			tn.setPrice(order.getActualPrice().toString());
			tn.setPrepayId("1");
			tn.setOrderId(orderId.toString());
			tn.setPaymentMode(order.getPaymentMode().toString());

			return new APIResponse("000000", "成功", tn);
		} else {
			return new APIResponse("000002", "不存在的支付方式", "");
		}

	}

	public boolean updateCashGiftID(Long orderId, Long cashGiftId) {
		return orderDao.updateCashGiftID(orderId, cashGiftId);
	}

	public Order findByRoomId(Long roomId) {
		return  this.filling(orderDao.findByRoomIdAndStatus(roomId,
				Constants.Order.TYPE_NO_PAY));
	}
	public Order findByRoomIdNoFilling(Long roomId) {
		return orderDao.findByRoomIdAndStatus(roomId,
				Constants.Order.TYPE_NO_PAY);
	}

	public void updatePrice(Long id, Double price) {
		orderDao.updatePrice(id, price);
	}

	public void updateStatus(Long id, Integer status) {
		orderDao.updatePrice(id, status);
	}

	public void updateRefundPrice(Long id, Double refundPrice) {
		orderDao.updateRefundPrice(id, refundPrice);
	}

	public void updateDuration(Long id, Integer duration) {
		orderDao.updateDuration(id, duration);
	}

	public void updateHaveReturnGoods(Long orderId, int i) {
		orderDao.updateHaveReturnGoods(orderId,i);
	}
	
	public void updatePoint(Double point ,Double getPoint, Long orderId){
		orderDao.updatePoint(point,getPoint,orderId);
	}
	
	/**
	 * 处理pos端返回未支付的订单
	 * @param orderId
	 */
	public void restoreStock(Long orderId){
		//删除订单
		if (orderDao.deleteRestoreOrder(orderId) > 0){
			//返回库存
			List<OrderItem> itemList = orderItemDao.findByOrderId(orderId);
			for (OrderItem item : itemList){
				productStockDao.updateStockNumberAtReturnGoods(item.getStockId(), item.getAmount().doubleValue());
			}
			orderItemDao.deleteByOrderId(orderId);
		}
	}
	
	public void handlerPayingOrder(Long orderId){
		if (orderDao.deletePayingOrder(orderId) > 0){
			//返回库存
			List<OrderItem> itemList = orderItemDao.findByOrderId(orderId);
			for (OrderItem item : itemList){
				productStockDao.updateStockNumberAtReturnGoods(item.getStockId(), item.getAmount().doubleValue());
			}
			orderItemDao.deleteByOrderId(orderId);
		}
	}

	public void bankCardOrderRevoke(Long orderId) {
		if (orderDao.updateStatus(orderId, Constants.Order.TYPE_BANK_CARD_REVOKE,Constants.Order.TYPE_PAY) > 0){
			//返回库存
			List<OrderItem> itemList = orderItemDao.findByOrderId(orderId);
			for (OrderItem item : itemList){
				productStockDao.updateStockNumberAtReturnGoods(item.getStockId(), item.getAmount().doubleValue());
			}
		}
	}

	public Integer getStatus(Long orderId) {
		return orderDao.getStatus(orderId);
	}

	public void paid(Long orderId, Long storeId) {
		orderDao.updateStatus(orderId, Constants.Order.TYPE_PAY, Constants.Order.TYPE_PAYING,storeId);
	}
	
	@SuppressWarnings("unchecked")
	public Page getOrderListByStoreId(String storeId, String orderId, String beginTime, String endTime, Page page) {
		page = orderDao.getOrderListByStoreId(storeId, orderId, beginTime, endTime, page);
		List<Order> returnorders = new ArrayList<Order>();
		List<Order> orders = (List<Order>) page.getData();
		for (Order order : orders) {
			order = this.filling(order);
			returnorders.add(order);
		}
		page.setData(returnorders);
		return page;
	}
}
