/**
 * createDate : 2016年4月28日下午5:37:37
 */
package cn.lastmiles.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.OrderReturnGoods;
import cn.lastmiles.bean.OrderReturnGoodsRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.OrderReturnGoodsDao;

@Service
public class OrderReturnGoodsService {
	private static final Logger logger = LoggerFactory.getLogger(OrderReturnGoodsService.class);
	@Autowired
	private OrderReturnGoodsDao orderReturnGoodsDao;
	@Autowired
	private IdService idService;
	
	public void save(OrderReturnGoods orderReturnGoods){
		
		if( null == orderReturnGoods.getId() ){
			orderReturnGoods.setId(idService.getId());
		}
		
		if( null == orderReturnGoods.getCreatedTime() ){
			orderReturnGoods.setCreatedTime(new Date());
		}
		
		logger.debug("save orderReturnGoods is {}",orderReturnGoods);
		orderReturnGoodsDao.save(orderReturnGoods);
	}
	
	public void saveRecord(OrderReturnGoodsRecord orderReturnGoodsRecord){
				
		if( null == orderReturnGoodsRecord.getCreatedTime() ){
			orderReturnGoodsRecord.setCreatedTime(new Date());
		}
		
		logger.debug("saveRecord orderReturnGoodsRecord is {}",orderReturnGoodsRecord);
		orderReturnGoodsDao.saveRecord(orderReturnGoodsRecord);
	}
	
	
	public List<OrderReturnGoods> findByOrderId(Long orderId){
		return orderReturnGoodsDao.findByOrderId(orderId);
	}

	public Page list(String storeId,String orderId, String productName, String barcode, Long categoryId,String beginTime,String endTime, Page page) {
		return orderReturnGoodsDao.list(storeId,orderId,productName,barcode,categoryId,beginTime,endTime,page);
	}
	
	public Page list(String storeId,Integer sort,String productName, String barcode, Long categoryId,Integer dateType,String beginTime,String endTime, Page page) {
		return orderReturnGoodsDao.list(storeId,sort,productName,barcode,categoryId,dateType,beginTime,endTime,page);
	}

	public Page findAll(String storeId, String orderId, String productName,
			String barcode, Long categoryId, String beginTime, String endTime,
			Page page) {
		return orderReturnGoodsDao.findAll(storeId,orderId,productName,barcode,categoryId,beginTime,endTime,page);
	}

	public Page findStatisticalAll(String storeId, Integer sort,
			String productName, String barcode, Long categoryId,
			Integer dateType, String beginTime, String endTime, Page page) {
		return orderReturnGoodsDao.findStatisticalAll(storeId,sort,productName,barcode,categoryId,dateType,beginTime,endTime,page);
	}
}
