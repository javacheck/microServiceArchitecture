package cn.self.cloud.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.self.cloud.bean.Order;
import cn.self.cloud.bean.OrderItem;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.self.cloud.commonutils.id.IdService;

@Service
public class OrderServise {
	
	@Autowired
	private IdService IdService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private UserService userService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderItemServise orderItemServise;
	@Autowired
	private ProductStockService productStockService;
	
	public Long save(Order order){
		order.setId(IdService.getId());
		return orderDao.save(order)?order.getId():-100L;
	}
	
	@SuppressWarnings("unchecked")
	public Page list(String beginTime, String endTime, String mobile, String orderId, Page page){
		List<Order> Returnorders = new ArrayList<Order>();
		page = orderDao.list( beginTime, endTime, mobile, orderId, page);
		List<Order> orders= (List<Order>) orderDao.list( beginTime, endTime, mobile, orderId, page).getData();
		for (Order order : orders) {
			Returnorders.add(this.filling(order));
		}
		page.setData(Returnorders);
		return page;
	}
	
	public Double calculatSales(Long accountId,Long storeId,Date beginDate,Date endDate){
		return orderDao.calculatSales(accountId,storeId,beginDate,endDate);
	}
	
	public Order findById(Long id){
		return this.filling(orderDao.findById(id));
	}
	
	/**
	 * 关联表封装
	 * @param order
	 * @return
	 */
	public Order filling(Order order){
		if (order==null) {
			return null;
		}
		order.setAccount(accountService.findById(order.getAccountId()));
		order.setUser(userService.findById(order.getUserId()));
		order.setStore(storeService.findById(order.getStoreId()));
		order.setOrderItems(orderItemServise.findByOrderId(order.getId()));
		return order;
	}
	
	public Long saveOrderAndOrderItem(Order order){
		Long orderId=this.save(order);
		for (OrderItem orderItem : order.getOrderItems()) {
			orderItem.setOrderId(orderId);
			orderItemServise.save(orderItem);
		}
		return orderId;
	}

	public void update(Long orderId, Integer status, Integer paymentMode) {
		orderDao.update(orderId,status,paymentMode);
	}
	
	

}
