package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.OrderPromotion;
import cn.lastmiles.dao.OrderPromotionDao;

@Service
public class OrderPromotionService {
	@Autowired
	private OrderPromotionDao orderPromotionDao;
	
	public void save(OrderPromotion orderPromotion){
		orderPromotionDao.save(orderPromotion);
	}
}
