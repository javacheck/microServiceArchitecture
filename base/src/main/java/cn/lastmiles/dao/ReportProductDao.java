package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ReportProduct;
import cn.lastmiles.bean.ReportSum;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ReportProductDao {
	private final static Logger logger = LoggerFactory.getLogger(ReportProductDao.class);
	public Page findAllPage(String storeIdString, String name,Integer type,Integer source,Integer sort,
			String beginTime, String endTime, Page page) {
		logger.debug("dao+storeIdString"+storeIdString);
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
		logger.debug("sort={}",sort);
		map.put("type", type);
		map.put("source", source);
		map.put("sort", sort);
		if(StringUtils.isNotBlank(beginTime)){
			map.put("beginTime", beginTime+":00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime + ":59");
		}
		
		
		
		Integer total = JdbcUtils.queryForObject("reportProduct.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("reportProduct.findAllPage", map, ReportProduct.class));
		
		return page;
	}
	
	

	public List<ReportProduct> findAll(String storeIdString, String name,Integer type,Integer source,Integer sort,
			String beginTime, String endTime) {
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
		map.put("type", type);
		map.put("source", source);
		map.put("sort", sort);
		if(StringUtils.isNotBlank(beginTime)){
			map.put("beginTime", beginTime+":00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime + ":59");
		}
		
		return JdbcUtils.queryForList("reportProduct.findAll", map, ReportProduct.class);
	}



	public ReportProduct findAllSum(String storeIdString, String name,Integer type,Integer source, String beginTime, String endTime) {
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
		map.put("type", type);
		map.put("source", source);
		if(StringUtils.isNotBlank(beginTime)){
			map.put("beginTime", beginTime+":00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime + ":59");
		}
		
		return JdbcUtils.queryForObject("reportProduct.findAllSum", map, ReportProduct.class);
	}



	public ReportProduct findStoreMunSum(String storeIdString, String name,Integer type,Integer source,
			String beginTime, String endTime) {
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
		map.put("type", type);
		map.put("source", source);
		if(StringUtils.isNotBlank(beginTime)){
			map.put("beginTime", beginTime+":00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime + ":59");
		}
		
		return JdbcUtils.queryForObject("reportProduct.findStoreMunSum", map, ReportProduct.class);
	}



	public ReportProduct findStockMunSum(String storeIdString, String name,Integer type,Integer source,
			String beginTime, String endTime) {
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
		map.put("type", type);
		map.put("source", source);
		if(StringUtils.isNotBlank(beginTime)){
			map.put("beginTime", beginTime+":00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime + ":59");
		}
		
		return JdbcUtils.queryForObject("reportProduct.findStockMunSum", map, ReportProduct.class);
	}
}
