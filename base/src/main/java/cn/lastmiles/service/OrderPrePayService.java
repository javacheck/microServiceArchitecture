package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.OrderPrePay;
import cn.lastmiles.dao.OrderPrePayDao;

@Service
public class OrderPrePayService {
	@Autowired
	private OrderPrePayDao orderPrePayDao;
	
	public OrderPrePay find(Long orderId) {
		return orderPrePayDao.find(orderId);
	}

	public void save(OrderPrePay prePay) {
		orderPrePayDao.save(prePay);
	}
}
