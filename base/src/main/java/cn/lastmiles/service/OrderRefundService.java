package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.OrderRefund;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.OrderRefundDao;

@Service
public class OrderRefundService {
	@Autowired
	private OrderRefundDao orderRefundDao;
	@Autowired
	private IdService idService;

	public void save(OrderRefund orderRefund) {
		//银联需要新的orderId
		if (orderRefund.getId() == null) {
			orderRefund.setId(idService.getOrderId());
		}
		orderRefundDao.save(orderRefund);
	}

	public void updateStatus(Long id, Integer status) {
		orderRefundDao.updateStatus(id, status);
	}

}
