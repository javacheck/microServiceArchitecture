package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.StockCheck;
import cn.lastmiles.bean.StockCheckDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class StockCheckDao {
	private final static Logger logger = LoggerFactory.getLogger(StockCheckDao.class);

	public Page findAll(String storeIdString, String checkedName,
			String createdTime, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
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
		if(StringUtils.isNotBlank(checkedName)){
			map.put("checkedName", "%"+checkedName+"%");
		}else{
			map.put("checkedName", "");
		}
		map.put("createdTime", createdTime);
		Integer total = JdbcUtils.queryForObject("stockCheck.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("stockCheck.findAllPage", map, StockCheck.class));
		
		return page;
	}

	public List<ProductStock> findAllBySearch(String storeIdString) {
		Map<String, Object> map = new HashMap<String, Object>();
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
		return JdbcUtils.queryForList("stockCheck.findAllBySearch", map, ProductStock.class);
	}


	public void saveStockCheck(StockCheck sc) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", sc.getId());
		map.put("createdTime", new Date());
		map.put("checkedName", sc.getCheckedName());
		map.put("accountId", sc.getAccountId());
		map.put("storeId", sc.getStoreId());
		JdbcUtils.save("t_stock_check", map);
		
	}
	public void saveStockCheckDetail(StockCheckDetail scd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCheckId", scd.getStockCheckId());
		map.put("storeId", scd.getStoreId());
		map.put("productName", scd.getProductName());
		map.put("barCode", scd.getBarCode());
		map.put("categoryName", scd.getCategoryName());
		map.put("stock", scd.getStock());
		map.put("checkedStock", scd.getCheckedStock());
		map.put("inventoryProfit", scd.getInventoryProfit());
		map.put("inventoryLoss", scd.getInventoryLoss());
		map.put("checkedName", scd.getCheckedName());
		map.put("checkedTime", scd.getCheckedTime());
		JdbcUtils.save("t_stock_check_detail", map);
		
	}

	public Page finddetailsAll(Long stockCheckId, String productName,
			String barCode,Integer sort,Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("stockCheckId", stockCheckId);
		
		if(StringUtils.isNotBlank(productName)){
			map.put("productName", "%"+productName+"%");
		}else{
			map.put("productName", "");
		}
		
		map.put("barCode", barCode);
		
		map.put("sort", sort);
		Integer total = JdbcUtils.queryForObject("stockCheck.findDetailTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("stockCheck.findDetailAllPage", map, StockCheckDetail.class));
		
		return page;
	}

	public List<StockCheckDetail> findDetailsListAllBySearch(Long stockCheckId,
			String productName, String barCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("stockCheckId", stockCheckId);
		
		if(StringUtils.isNotBlank(productName)){
			map.put("productName", "%"+productName+"%");
		}else{
			map.put("productName", "");
		}
		if(StringUtils.isNotBlank(barCode)){
			map.put("barCode", "%"+barCode+"%");
		}else{
			map.put("barCode", "");
		}
		
		
		return JdbcUtils.queryForList("stockCheck.findDetailsListAllBySearch", map, StockCheckDetail.class);
		
	}
}
