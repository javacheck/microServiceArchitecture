package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ReportStock;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ReportStockDao {
	private final static Logger logger = LoggerFactory.getLogger(ReportStockDao.class);
	public Page findAllPage(String storeIdString, String name, String barCode,
			Integer sort, Page page) {
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
		if(StringUtils.isNotBlank(name)){
			map.put("name", "%"+name+"%");
		}
		map.put("barCode", barCode);
		map.put("sort", sort);
		Integer total = JdbcUtils.queryForObject("reportStock.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("reportStock.findAllPage", map, ReportStock.class));
		
		return page;
	}

	public ReportStock findAllSum(String storeIdString, String name,
			String barCode) {
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
		if(StringUtils.isNotBlank(name)){
			map.put("name", "%"+name+"%");
		}
		map.put("barCode", barCode);
		
		
		return JdbcUtils.queryForObject("reportStock.findAllSum", map, ReportStock.class);
	}

	public ReportStock findProductSum(String storeIdString, String name,
			String barCode) {
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
		if(StringUtils.isNotBlank(name)){
			map.put("name", "%"+name+"%");
		}
		map.put("barCode", barCode);
		
		
		return JdbcUtils.queryForObject("reportStock.findProductSum", map, ReportStock.class);
	}

	public List<ReportStock> findAll(String storeIdString, String name,
			String barCode, Integer sort) {
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
		if(StringUtils.isNotBlank(name)){
			map.put("name", "%"+name+"%");
		}
		map.put("barCode", barCode);
		map.put("sort", sort);
		

		
		
		return JdbcUtils.queryForList("reportStock.findAll", map, ReportStock.class);
		
	}

}
