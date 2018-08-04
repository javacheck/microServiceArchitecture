package cn.lastmiles.service;

import cn.lastmiles.bean.PayRecord;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.OrderDao;
import cn.lastmiles.dao.PayRecordDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PayRecordService {
	@Autowired
	private PayRecordDao payRecordDao;
	@Autowired
	private OrderDao orderDao;
	
	public PayRecord get(Long orderId) {
		return payRecordDao.get(orderId);
	}

	@Transactional
	public void save(PayRecord pr) {
		if (pr.getChannel() == Constants.PayRecord.CHANNEL_UNIONPAY) {//银联
			orderDao.udpatePaymentMode(Long.valueOf(pr.getOrderId()),
					Constants.Order.PAYMENT_UNION_PAY);
		} else if (pr.getChannel() == Constants.PayRecord.CHANNEL_WEIXIN) {//微信
			orderDao.udpatePaymentMode(Long.valueOf(pr.getOrderId()),
					Constants.Order.PAYMENT_MICRO_CHANNEL_PAY);
		}
		else if (pr.getChannel() == Constants.PayRecord.CHANNEL_ALIPAY) {//支付宝
			orderDao.udpatePaymentMode(Long.valueOf(pr.getOrderId()),
					Constants.Order.PAYMENT_ALIPAY);
		}
		payRecordDao.delete(pr.getOrderId());
		payRecordDao.save(pr);
		if (pr.getStatus() == Constants.PayRecord.STATUS_SUCCESS) {
			orderDao.paySuccess(pr.getOrderId());
		}
	}
}
