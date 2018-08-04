/**
 * createDate : 2016年4月11日上午11:46:23
 */
package cn.lastmiles.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.movie.RoomPackage;
import cn.lastmiles.bean.movie.RoomPackageProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.RoomPackageDao;

@Service
public class RoomPackageService {

	private static final Logger logger = LoggerFactory.getLogger(RoomPackageService.class);
	@Autowired
	private RoomPackageDao roomPackageDao;
	@Autowired
	private IdService idService;
	
	@Autowired
	private ProductStockService productStockService;
	public Page list(Long storeId, Page page) {
		return roomPackageDao.list(storeId,page);
	}
	public int deleteById(Long storeId,Long id) {
		return roomPackageDao.deleteById(storeId,id);
	}
	public List<ProductStock> findProductStockList(Long storeId, String stockIdS) {
		List<ProductStock> productStockList = roomPackageDao.findProductStockList(storeId,stockIdS);
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService.getAttributeValuesListJointValue(productStock.getAttributeCode(),"|"));
		}
		return productStockList;
	}
	public Page findProductStockList(String productName, Long storeId, Long productCategoryId, Page page) {
		page = roomPackageDao.findProductStockList(productName,storeId,productCategoryId,page); 
		if( page.getTotal().intValue() == 0 ){
			return page;
		}
		List<ProductStock> productStockList = (List<ProductStock>) page.getData();
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService.getAttributeValuesListJointValue(productStock.getAttributeCode(),"|"));
		}
		page.setData(productStockList);
		return page;
	}
	public boolean checkName(Long id ,Long storeId ,String name) {
		return roomPackageDao.checkName(id,storeId,name);
	}
	public boolean save(RoomPackage roomPackage) {
		String[] roomPackageProductCache = roomPackage.getRoomPackageProductCache(); 
		String[] roomPackageProductCacheAmount = roomPackage.getRoomPackageProductCacheAmount();
		
		logger.debug("保存或者修改时存储的商品列表数：{},各个商品对应的数量是：{}",roomPackageProductCache.length,roomPackageProductCacheAmount.length);
		
		Long roomPackageId = null;
		if( null == roomPackage.getId() ){
			roomPackageId = idService.getId();
		} else {
			roomPackageId = roomPackage.getId();
		}
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (int i = 0; i < roomPackageProductCache.length; i++) {
			Object[] arg = new Object[3];
			arg[0] = roomPackageId;
			arg[1] = roomPackageProductCache[i];
			arg[2] = roomPackageProductCacheAmount[i];
			batchArgs.add(arg);
		}
		
		
		boolean flag = false;
		
		double duration_ = BigDecimal.valueOf(roomPackage.getDuration_Cache()).setScale(1,BigDecimal.ROUND_FLOOR).doubleValue();
		int minute =  (int)(duration_ * 60) ; // 将页面上的小时转化成分钟存入数据库中
		
		logger.debug("小时格式的消费时长是：{}，转化成分钟后的消费时长是：{}",roomPackage.getDuration_Cache(),minute);
		
		roomPackage.setDuration(minute);
		
		
		if( null == roomPackage.getId()){
			roomPackage.setId(roomPackageId);
			flag = roomPackageDao.save(roomPackage);	
		} else {
			flag = roomPackageDao.update(roomPackage);
		}
		roomPackageDao.saveBatch(batchArgs,roomPackageId);	
		return flag;
	}
	public RoomPackage findById(Long id) {
		return roomPackageDao.findById(id);
	}
	public List<RoomPackageProductStock> findProductById(Long id) {
		return roomPackageDao.findProductById(id);
	}
	public List<RoomPackage> findByStoreId(Long storeId) {
		return roomPackageDao.findByStoreId(storeId);
		
	}
	
}
