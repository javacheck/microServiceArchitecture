package cn.lastmiles.dao;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.OrderPromotion;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class OrderPromotionDao {
	
	public void save(OrderPromotion orderPromotion){
		JdbcUtils.save(orderPromotion);
	}
}
