package cn.lastmiles.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PurchaseAcceptance;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class PurchaseAcceptanceDao {
	private final static Logger logger = LoggerFactory.getLogger(PurchaseAcceptanceDao.class);
	public Page findAllPage(Long storeId, String purchaseNumber, String mobile,
			String beginTime, String endTime, Integer status, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.debug("purchaseNumber={}",purchaseNumber);
		map.put("storeId", storeId);
		if(StringUtils.isNotBlank(purchaseNumber)){
			map.put("purchaseNumber", "%"+purchaseNumber+"%");
		}else{
			map.put("purchaseNumber", "");
		}
		if(StringUtils.isNotBlank(mobile)){
			map.put("mobile", "%"+mobile+"%");
		}else{
			map.put("mobile", "");
		}
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		map.put("status", status);
		Integer total = JdbcUtils.queryForObject("purchaseAcceptance.findTotal", map, Integer.class);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("purchaseAcceptance.findAllPage", map, PurchaseAcceptance.class));
		
		return page;
	}
	public PurchaseAcceptance findById(Long id) {
		return JdbcUtils.findById(PurchaseAcceptance.class, id);
	}
	public void savePurchaseAcceptance(PurchaseAcceptance purchaseAcceptance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", purchaseAcceptance.getId());
		map.put("purchaseNumber", purchaseAcceptance.getPurchaseNumber());
		map.put("accountId", purchaseAcceptance.getAccountId());
		map.put("storageNumber", purchaseAcceptance.getStorageNumber());
		map.put("storeId", purchaseAcceptance.getStoreId());
		map.put("createdTime", new Date());
		map.put("status", purchaseAcceptance.getStatus());
		map.put("memo", purchaseAcceptance.getMemo());
		map.put("imageId", purchaseAcceptance.getImageId());
		JdbcUtils.save("t_purchase_acceptance", map);
		
	}
	public void updatePurchaseAcceptance(PurchaseAcceptance purchaseAcceptance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", purchaseAcceptance.getId());
		map.put("accountId", purchaseAcceptance.getAccountId());
		map.put("storageNumber", purchaseAcceptance.getStorageNumber());
		map.put("storeId", purchaseAcceptance.getStoreId());
		map.put("createdTime", new Date());
		map.put("status", purchaseAcceptance.getStatus());
		map.put("memo", purchaseAcceptance.getMemo());
		map.put("imageId", purchaseAcceptance.getImageId());
		int ret = JdbcUtils.update("purchaseAcceptance.updatePurchaseAcceptance", map);
		System.out.println(ret);
		
	}
	public PurchaseAcceptance findPaExist(Long id, String purchaseNumber,Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("purchaseNumber", purchaseNumber);
		map.put("storeId", storeId);
		PurchaseAcceptance pa = JdbcUtils.queryForObject("purchaseAcceptance.findPaExist", map, PurchaseAcceptance.class);
		return pa;
	}
}
