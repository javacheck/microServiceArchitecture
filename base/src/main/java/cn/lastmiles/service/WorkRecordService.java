package cn.lastmiles.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.bean.UserStoreServicePackageRecord;
import cn.lastmiles.bean.WorkRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.OrderDao;
import cn.lastmiles.dao.UserCardRecordDao;
import cn.lastmiles.dao.UserStoreServicePackageRecordDao;
import cn.lastmiles.dao.WorkRecordDao;

@Service
public class WorkRecordService {
	private final static Logger logger = LoggerFactory
			.getLogger(WorkRecordService.class);
	@Autowired
	private WorkRecordDao workRecorddao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private IdService idService;
	@Autowired
	private UserCardRecordDao userCardRecordDao;
	@Autowired
	private UserStoreServicePackageRecordDao userStoreServicePackageRecordDao;

	public Page list(String accountName, String accountMobile,
			String startDate, String endDate, Long storeId, Page page) {
		logger.debug(
				"accountName:{},accountMobile:{},startDate:{},endDate:{},storeId:{}",
				accountName, accountMobile, startDate, endDate, storeId);
		return workRecorddao.list(accountName, accountMobile, startDate,
				endDate, storeId, page);
	}

	/**
	 * 当前交接班记录
	 * 
	 * @param accountId
	 * @return
	 */
	@Transactional
	public WorkRecord findCurrent(Long accountId) {
		// 统计订单
		WorkRecord workRecord = this.findByAccountId(accountId);
		logger.debug("workRecord num = {},sales = {}",
				workRecord.getTotalNum(), workRecord.getSales());
		List<Order> list = orderDao.findCurrentOrderRecord(accountId,
				workRecord.getStartDate());
		logger.debug("workRecord size = {},accountId= {},date = {}",
				list.size(), accountId, workRecord.getStartDate());

		workRecord.setOrderNum(list.size());
		workRecord.setTotalNum(list.size() + workRecord.getTotalNum());
		for (Order order : list) {
			if (order.getPaymentMode() != null) {
				Double actualPrice = order.getActualPrice();

				if (order.getRoomId() != null) {
					actualPrice = NumberUtils.subtract(actualPrice,
							order.getRefundPrice());
				}

				Double cashPrice = order.getCashPrice() == null ? 0 : order
						.getCashPrice();

				if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_ALIPAY) {
					workRecord.setAlipayNum(workRecord.getAlipayNum() + 1);
					if (cashPrice > 0) {
						workRecord.setCashPay(NumberUtils.add(cashPrice,
								workRecord.getCashPay()));
						workRecord.setAlipay(NumberUtils.add(
								workRecord.getAlipay(),
								NumberUtils.subtract(actualPrice, cashPrice)));
					} else {
						workRecord.setAlipay(NumberUtils.add(actualPrice,
								workRecord.getAlipay()));
					}
					workRecord.setSales(NumberUtils.add(actualPrice,
							workRecord.getSales()));

					workRecord.setOrderSales(NumberUtils.add(actualPrice,
							workRecord.getOrderSales()));
				} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_MICRO_CHANNEL_PAY) {
					workRecord.setWxPayNum(workRecord.getWxPayNum() + 1);
					if (cashPrice > 0) {
						workRecord.setCashPay(NumberUtils.add(cashPrice,
								workRecord.getCashPay()));
						workRecord.setWxPay(NumberUtils.add(
								workRecord.getWxPay(),
								NumberUtils.subtract(actualPrice, cashPrice)));
					} else {
						workRecord.setWxPay(NumberUtils.add(actualPrice,
								workRecord.getWxPay()));
					}
					workRecord.setSales(NumberUtils.add(actualPrice,
							workRecord.getSales()));
					workRecord.setOrderSales(NumberUtils.add(actualPrice,
							workRecord.getOrderSales()));
				} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_CARD_PAY) {
					workRecord
							.setBankCardPayNum(workRecord.getBankCardPayNum() + 1);
					if (cashPrice > 0) {
						workRecord.setCashPay(NumberUtils.add(cashPrice,
								workRecord.getCashPay()));
						workRecord.setBankCardPay(NumberUtils.add(
								workRecord.getBankCardPay(),
								NumberUtils.subtract(actualPrice, cashPrice)));
					} else {
						workRecord.setBankCardPay(NumberUtils.add(actualPrice,
								workRecord.getBankCardPay()));
					}
					workRecord.setSales(NumberUtils.add(actualPrice,
							workRecord.getSales()));
					workRecord.setOrderSales(NumberUtils.add(actualPrice,
							workRecord.getOrderSales()));
				} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_CASH_PAY) {
					workRecord.setCashPayNum(workRecord.getCashPayNum() + 1);
					workRecord.setCashPay(NumberUtils.add(actualPrice,
							workRecord.getCashPay()));

					workRecord.setSales(NumberUtils.add(actualPrice,
							workRecord.getSales()));
					workRecord.setOrderSales(NumberUtils.add(actualPrice,
							workRecord.getOrderSales()));
				} else if (order.getPaymentMode().intValue() == Constants.Order.PAYMENT_USER_CARD_BALANCE_PAY) {
					if (cashPrice.doubleValue() > 0) {
						// 会员卡加现金的情况
						Double d = NumberUtils.subtract(cashPrice,
								order.getChange());

						workRecord.setSales(NumberUtils.add(d,
								workRecord.getSales()));
						workRecord.setOrderSales(NumberUtils.add(d,
								workRecord.getOrderSales()));
						workRecord.setCashPay(NumberUtils.add(d,
								workRecord.getCashPay()));

						workRecord.setUserCard(NumberUtils.add(
								workRecord.getUserCard(),
								NumberUtils.subtract(actualPrice, d)));
						// workRecord.setSales(d);
						// workRecord.setCashPay(d);
					} else {
						// 全部会员卡支付
						// workRecord.setUserCardNum(workRecord.getUserCardNum()
						// + 1);
						workRecord.setUserCard(NumberUtils.add(
								workRecord.getUserCard(), actualPrice));

						workRecord.setSales(NumberUtils.add(actualPrice,
								workRecord.getSales()));
						workRecord.setOrderSales(NumberUtils.add(actualPrice,
								workRecord.getOrderSales()));
					}
				}
			}
		}

		// 统计会员卡
		List<UserCardRecord> userCardRecordList = userCardRecordDao
				.findCurrentRecordForPos(accountId, workRecord.getStartDate());
		workRecord.setTotalNum(userCardRecordList.size()
				+ workRecord.getTotalNum());
		workRecord.setUserCardRechargeNum(userCardRecordList.size());

		for (UserCardRecord ucr : userCardRecordList) {
			workRecord.setSales(NumberUtils.add(ucr.getAmount(),
					workRecord.getSales()));
			workRecord.setUserCardRechargeSales(NumberUtils.add(
					workRecord.getUserCardRechargeSales(), ucr.getAmount()));
			// 0 现金支付 、1 微信支付、2 银联支付，3支付宝
			if (ucr.getPayWay() == 0) {
				workRecord.setCashPay(NumberUtils.add(workRecord.getCashPay(),
						ucr.getAmount()));
			} else if (ucr.getPayWay() == 1) {
				workRecord.setWxPayNum(workRecord.getWxPayNum() + 1);
				workRecord.setWxPay(NumberUtils.add(workRecord.getWxPay(),
						ucr.getAmount()));
			} else if (ucr.getPayWay() == 2) {
				workRecord
						.setBankCardPayNum(workRecord.getBankCardPayNum() + 1);
				workRecord.setBankCardPay(NumberUtils.add(
						workRecord.getBankCardPay(), ucr.getAmount()));
			} else if (ucr.getPayWay() == 3) {
				workRecord.setAlipayNum(workRecord.getAlipayNum() + 1);
				workRecord.setAlipay(NumberUtils.add(workRecord.getAlipay(),
						ucr.getAmount()));
			}
		}

		// 服务套餐
		List<UserStoreServicePackageRecord> packageRecordList = userStoreServicePackageRecordDao
				.findCurrentRecordForPos(accountId, workRecord.getStartDate());
		workRecord.setTotalNum(packageRecordList.size()
				+ workRecord.getTotalNum());
		workRecord.setUserCardRechargeNum(packageRecordList.size()
				+ workRecord.getUserCardRechargeNum());

		for (UserStoreServicePackageRecord r : packageRecordList) {
			workRecord.setSales(NumberUtils.add(r.getRealityPrice(),
					workRecord.getSales()));
			workRecord
					.setUserCardRechargeSales(NumberUtils.add(
							workRecord.getUserCardRechargeSales(),
							r.getRealityPrice()));
			if (r.getPayWay() == 0) {
				workRecord.setCashPay(NumberUtils.add(workRecord.getCashPay(),
						r.getRealityPrice()));
			} else if (r.getPayWay() == 1) {
				workRecord.setWxPayNum(workRecord.getWxPayNum() + 1);
				workRecord.setWxPay(NumberUtils.add(workRecord.getWxPay(),
						r.getRealityPrice()));
			} else if (r.getPayWay() == 2) {
				workRecord
						.setBankCardPayNum(workRecord.getBankCardPayNum() + 1);
				workRecord.setBankCardPay(NumberUtils.add(
						workRecord.getBankCardPay(), r.getRealityPrice()));
			} else if (r.getPayWay() == 3) {
				workRecord.setAlipayNum(workRecord.getAlipayNum() + 1);
				workRecord.setAlipay(NumberUtils.add(workRecord.getAlipay(),
						r.getRealityPrice()));
			}
			// workRecord.setUserCardNum(workRecord.getUserCardNum() + 1);
			// workRecord.setUserCard(NumberUtils.add(workRecord.getUserCard(),
			// r.getRealityPrice()));
		}

		// JdbcUtils.deleteById(WorkRecord.class, workRecord.getId());
		// workRecord.setUpdateDate(new Date());
		// JdbcUtils.save(workRecord);

		logger.debug("workrecord ======== {}", workRecord);
		return workRecord;
	}

	public Long beginSave(WorkRecord workRecord) {
		workRecord.setId(idService.getId());
		return workRecorddao.beginSave(workRecord);
	}

	public void endUpdate(WorkRecord workRecord) {
		workRecorddao.endUpdate(workRecord);
	}

	public WorkRecord findById(Long id) {
		return workRecorddao.findById(id);
	}

	public WorkRecord findByToken(String token) {
		return workRecorddao.findByToken(token);
	}

	public WorkRecord findByAccountId(Long accountId) {
		return workRecorddao.findByAccountId(accountId);
	}

	public void next(WorkRecord workRecord) {
		JdbcUtils.deleteById(WorkRecord.class, workRecord.getId());
		JdbcUtils.save(workRecord);
	}

	public Page getRecordList(String name, String mobile, String startTime,
			String endTime, Long storeId, Page page) {
		return workRecorddao.list(name, mobile, startTime, endTime, storeId,
				page);
	}
}
