package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;

import cn.self.cloud.bean.OrderItem;
import cn.self.cloud.dao.OrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServise {
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Autowired
	private ProductStockService productStockService;
	
	/**
	 * 通过订单ID查找
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> findByOrderId(Long orderId){
		List<OrderItem> lists = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderItemDao.findByOrderId(orderId)) {
			lists.add(this.filling(orderItem));
		}
		return lists;
	}
	
	/**
	 * 关联表封装
	 * @param orderItem
	 * @return
	 */
	public OrderItem filling(OrderItem orderItem){
		orderItem.setProductStock(productStockService.findByStockId(orderItem.getStockId()));
		return orderItem;
	}
	
	public void save(OrderItem orderItem){
		orderItemDao.save(orderItem);
	}
	

}
