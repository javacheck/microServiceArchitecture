package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AllocationRecord;
import cn.lastmiles.bean.AllocationRecordStock;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class AllocationDao {
	private final static Logger logger = LoggerFactory.getLogger(AllocationDao.class);
	
	public Page findAllPage(String fromStoreIdString, String toStoreIdString,
			String allocationNumber, Integer status, String beginTime,
			String endTime,String storeIdString, Page page) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> fromStoreIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(fromStoreIdString)){
			String[] arrStoreId=fromStoreIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				fromStoreIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("fromStoreId", fromStoreIds);
		}else{
			map.put("fromStoreId", null);
		}
		List<Long> toStoreIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(toStoreIdString)){
			String[] arrStoreId=toStoreIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				toStoreIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("toStoreId", toStoreIds);
		}else{
			map.put("toStoreId", null);
		}
		
		map.put("allocationNumber", "%"+allocationNumber+"%");
		map.put("status", status);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		List<Long> storeIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(storeIdString)){
			String[] arrStoreId=storeIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				storeIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("storeId", storeIds);
		}else{
			map.put("storeId", null);
		}
		
		
		Integer total = JdbcUtils.queryForObject("allocation.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("allocation.findAllPage", map, AllocationRecord.class));
		
		return page;
	}

	public List<ProductStock> findProductStockList(String productStockIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> ids=new ArrayList<Long>();
		String[] arrStoreId=productStockIds.split(",");
		for(int i=0;i<arrStoreId.length;i++){
			ids.add(Long.parseLong(arrStoreId[i]));
		}
		map.put("ids", ids);
		
		return JdbcUtils.queryForList("allocation.findProductStockList", map, ProductStock.class);
	}

	public void save(AllocationRecord ar) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ar.getId());
		map.put("createdTime", new Date());
		map.put("allocationTime", ar.getAllocationTime());
		map.put("memo", ar.getMemo());
		map.put("fromStoreId", ar.getFromStoreId());
		map.put("toStoreId", ar.getToStoreId());
		map.put("status", ar.getStatus());
		map.put("allocationNumber", ar.getAllocationNumber());
		map.put("storeId", ar.getStoreId());
		map.put("accountId", ar.getAccountId());
		JdbcUtils.save("t_allocation_record", map);
	}

	public Integer findTotal(String date,Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		map.put("storeId", storeId);
		Integer total = JdbcUtils.queryForObject("allocation.findTotalSum", map, Integer.class);
		return total;
		
	}
	public void subtractStockAmount(Long stockId, Double amount) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockId", stockId);
		map.put("amount", amount);
		int ret = JdbcUtils.update("allocation.subtractStockAmount", map);
		System.out.println(ret);
		
	}
	public void addStockAmount(Long stockId, Integer amount) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockId", stockId);
		map.put("amount", amount);
		int ret = JdbcUtils.update("allocation.addStockAmount", map);
		System.out.println(ret);
		
	}
	public void saveAllocationRecordStock(AllocationRecordStock ars) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allocationRecordId", ars.getAllocationRecordId());
		map.put("productName", ars.getProductName());
		map.put("barCode", ars.getBarCode());
		map.put("categoryName", ars.getCategoryName());
		map.put("attributeName", ars.getAttributeName());
		map.put("amount", ars.getAmount());
		map.put("stockId", ars.getStockId());
		JdbcUtils.save("t_allocation_record_stock", map);
		
	}


	public List<AllocationRecordStock> findById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("allocationRecordId", id);
		
		return JdbcUtils.queryForList("allocation.findAllocationRecordStockList", map, AllocationRecordStock.class);
	}

	public void typeChangeByallocationId(Long id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		int ret = JdbcUtils.update("allocation.updateAllocationStatus", map);
		System.out.println(ret);
		
	}

	public ProductStock findProductStock(String barCode, Long storeId,String productName,Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("barCode", barCode);
		map.put("productName", productName);
		map.put("type", type);
		return JdbcUtils.queryForObject("allocation.findProductStock", map, ProductStock.class);
	}

	public void addConfirmStockAmount(int amount,String attributeName, String barCode, Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barCode", barCode);
		map.put("amount", amount);
		map.put("storeId", storeId);
		map.put("attributeName", attributeName);
		logger.debug("barCode={},amount={},storeId={},attributeName={}",barCode,amount,storeId,attributeName);
		int ret = JdbcUtils.update("allocation.addConfirmStockAmount", map);
		System.out.println(ret);
		
	}

	public List<AllocationRecord> findAllBySearch(Long fromStoreId,
			Long toStoreId, String allocationNumber, Integer status,
			String beginTime, String endTime, String storeIdString) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromStoreId", fromStoreId);
		map.put("toStoreId", toStoreId);
		map.put("allocationNumber", "%"+allocationNumber+"%");
		map.put("status", status);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		List<Long> storeIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(storeIdString)){
			String[] arrStoreId=storeIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				storeIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("storeId", storeIds);
		}else{
			map.put("storeId", null);
		}
		
		return JdbcUtils.queryForList("allocation.findAll", map, AllocationRecord.class);
	}

	public ProductStock findProductStockById(Long stockId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", stockId);
		
		return JdbcUtils.queryForObject("allocation.findProductStockById", map, ProductStock.class);
	}

	public Product findProductById(Long productId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", productId);
		
		return JdbcUtils.queryForObject("allocation.findProductById", map, Product.class);
	}

	public ProductCategory findProductCategory(String categoryName, Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryName", categoryName);
		map.put("storeId", storeId);
		
		return JdbcUtils.queryForObject("allocation.findProductCategory", map, ProductCategory.class);
	}

	public Product findProduct(Long categoryId, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", categoryId);
		map.put("name", name);
		
		return JdbcUtils.queryForObject("allocation.findProduct", map, Product.class);
	}

	public List<ProductStock> findProductStockList(String barCode,String productName,
			Long storeId, Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barCode", barCode);
		map.put("productName", productName);
		map.put("storeId", storeId);
		map.put("type", type);
		
		return JdbcUtils.queryForList("allocation.findPSList", map, ProductStock.class);
	}

	public void addConfirmStockAmount(Long stockId, int amount) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", stockId);
		map.put("amount", amount);
		int ret = JdbcUtils.update("allocation.addConfirmStockAmountById", map);
		System.out.println(ret);
		
	}

	public void typefinishChangeByallocationId(Long id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		map.put("finishTime", new Date());
		int ret = JdbcUtils.update("allocation.typefinishChangeByallocationId", map);
		System.out.println(ret);
		
	}

	public ProductStock findProductStock(Long stockId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", stockId);
		
		return JdbcUtils.queryForObject("allocation.findProductStockById", map, ProductStock.class);
	}

	public void updateLastAmounts(Long allocationRecordId, Long stockId,
			Double lastAmount) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allocationRecordId", allocationRecordId);
		map.put("stockId", stockId);
		map.put("lastAmount", lastAmount);
		int ret = JdbcUtils.update("allocation.updateLastAmounts", map);
		System.out.println(ret);
		
	}

	
}
