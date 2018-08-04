package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.OrderItemDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class OrderItemServise {
	private final static Logger logger = LoggerFactory.getLogger(OrderItemServise.class);

	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private ProductStockService productStockService;

	@Autowired
	private IdService idService;

	/**
	 * 通过订单ID查找
	 * 
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> findByOrderId(Long orderId) {
		List<OrderItem> lists = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderItemDao.findByOrderId(orderId)) {
			lists.add(this.filling(orderItem));
		}
		return lists;
	}

	public List<OrderItem> posFindByOrderId(Long orderId) {
		List<OrderItem> lists = new ArrayList<OrderItem>();
		List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
		for (OrderItem orderItem : orderItemList) {
			lists.add(posFilling(orderItem));
		}
		return lists;
	}

	public OrderItem posFilling(OrderItem orderItem) {
		ProductStock productStock = productStockService.posFindByStockId(orderItem.getStockId());
		orderItem.setProductAttributeInfo(productStock.getAttributeValuesListJointValue());
		orderItem.setProductName(productStock.getProductName());
		orderItem.setProductStockImageUrl(FileServiceUtils.getFileUrl(productStock.getImageId()));
		return orderItem;
	}

	/**
	 * 关联表封装
	 * 
	 * @param order
	 * @return
	 */
	public OrderItem filling(OrderItem orderItem) {
		ProductStock productStock = productStockService.findByStockId(orderItem.getStockId());
		if (productStock != null) {
			productStock.setAttributeName(orderItem.getName());
			orderItem.setProductStock(productStock);
		}
		return orderItem;
	}

	public void save(OrderItem orderItem) {
		orderItem.setId(idService.getId());
		if (orderItem.getStatus() == null) {
			orderItem.setStatus(Constants.OrderItem.STATUSNORMAL);
		}
		orderItemDao.save(orderItem);
		productStockService.decreaseStock(orderItem.getStockId(), orderItem.getAmount());
		if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_PRODUCT) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					productStockService.dealSales(orderItem.getStockId(), orderItem.getAmount());
					logger.debug("StockId is {} ,Amount is {}", orderItem.getStockId(), orderItem.getAmount());
				}
			});
		}
	}

	public List<OrderItem> findByNoCategoryId() {
		return orderItemDao.findByNoCategoryId();
	}

	public void updateCategoryId(Long id, Long categoryId) {
		orderItemDao.updateCategoryId(id, categoryId);
	}

	public boolean decreaseStock(List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getType().intValue() == Constants.OrderItem.TYPE_PRODUCT) {
				if (!productStockService.decreaseStock(orderItem.getStockId(), orderItem.getAmount())) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * public List<OrderItem> updateName(List<OrderItem> orderItems) { if
	 * (orderItems!=null) { for (OrderItem orderItem : orderItems) { if
	 * (orderItem.getProductStock()!=null) { String [] attributeCodes
	 * =orderItem.getProductStock().getAttributeCode().split("-"); StringBuffer
	 * sf= new StringBuffer(); for (String attributeCode : attributeCodes) {
	 * ProductAttributeValue productAttributeValue
	 * =attributeValueService.findById(Long.parseLong(attributeCode)); if
	 * (productAttributeValue!=null) {
	 * sf.append(productAttributeValue.getValue()+"|"); } }//for 结束
	 * sf.deleteCharAt(sf.length()-1); orderItem.setProductStock(productStock);
	 * } }
	 * 
	 * } return null; }
	 */
	public void updateDiscount(Long orderId, String orderItemDiscount) {
		List<Map> list = JsonUtils.jsonToList(orderItemDiscount, Map.class);
		if (list != null) {
			for (Map map : list) {
				orderItemDao.updateDiscount(orderId, Long.valueOf(map.get("id").toString()),
						Double.valueOf(map.get("discount").toString()));
			}
		}
	}

	public void updateDiscount(Long orderId, Double discount) {
		orderItemDao.updateCategoryId(orderId, discount);
	}

	public Integer countPromotion(Long promotionCategoryId, Long userId) {
		return orderItemDao.countPromotion(promotionCategoryId, userId);
	}

	public void updateStatus(Long orderId) {
		orderItemDao.updateStatus(orderId);
	}

	public OrderItem findByStockId(Long stockId) {
		return orderItemDao.findByStockId(stockId);
	}

	public OrderItem findByOrderIdAndStockIdAndType(Long orderId, Long stockId, Integer type) {
		return orderItemDao.findByOrderIdAndStockIdAndType(orderId, stockId, type);
	}

	@Transactional
	public void addProduct(List<OrderItem> orderItems) {
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				this.addProduct(orderItem);
			}
		}
	}

	public void addProduct(OrderItem orderItem) {
		OrderItem oldOrderItem = findByOrderIdAndStockIdAndType(orderItem.getOrderId(), orderItem.getStockId(),
				orderItem.getType());
		if (oldOrderItem != null) {// 判断订单里面是否有次商品
			orderItemDao.updateAmount(oldOrderItem.getId(),
					oldOrderItem.getAmount().intValue() + orderItem.getAmount().intValue());
		} else {
			this.save(orderItem);
		}
	}

	public void updateStatus(Long orderId, Integer status) {
		orderItemDao.updateStatus(orderId, status);
	}

	public Boolean updateNumberAndPrice(Long orderId, Long stockId, Integer type, Double updateReturnNumber,
			Double updateReturnPrice) {
		return orderItemDao.updateNumberAndPrice(orderId, stockId, type, updateReturnNumber, updateReturnPrice);
	}

	@SuppressWarnings("unchecked")
	public Page getProductListByOrderId(String orderId, String productName, String barCode, Page page) {
		page = orderItemDao.getProductListByOrderId(orderId, productName, barCode, page);
		List<OrderItem> orderItems = (List<OrderItem>) page.getData();
		if (orderItems == null) {
			return null;
		}
		List<OrderItem> finalItems = new ArrayList<OrderItem>();
		for (OrderItem orderItem : orderItems) {
			orderItem = this.filling(orderItem);
			finalItems.add(orderItem);
		}
		page.setData(finalItems);
		return page;
	}
}
