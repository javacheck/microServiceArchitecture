package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ReportSales;
import cn.lastmiles.bean.ReportSum;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ReportSalesDao {

	public Page findAllPage(String storeIdString, Integer source,
			String beginTime, String endTime,Integer sort, Page page) {
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
		map.put("source", source);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		map.put("sort", sort);
		
		
		Integer total = JdbcUtils.queryForObject("reportSales.findTotal", map, Integer.class);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("reportSales.findAllPage", map, ReportSales.class));
		
		return page;
	}
	
	public ReportSales findAllSum(String storeIdString,
			String beginTime, String endTime){
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
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		
		return JdbcUtils.queryForObject("reportSales.findAllSum", map, ReportSales.class);
		
	}

	public List<ReportSales> findAll(String storeIdString, Integer source,
			String beginTime, String endTime,Integer sort) {
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
		map.put("source", source);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		map.put("sort", sort);
		
		return JdbcUtils.queryForList("reportSales.findAll", map, ReportSales.class);
	}

	public ReportSales findReportSalesSum(String storeIdString, Integer source,Integer type) {
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
		map.put("source", source);
		map.put("type", type);
		if(type.intValue()==0){//当天
			map.put("date", DateUtils.format(new Date(), "yyyy-MM-dd"));
		}else if(type.intValue()==1){//本月
			map.put("date", DateUtils.format(new Date(), "yyyy-MM"));
		}else if(type.intValue()==2){//上月
			map.put("date", DateUtils.format(DateUtils.getLastDate(new Date()), "yyyy-MM"));
		}else if(type.intValue()==3){//今年
			map.put("date", DateUtils.format(DateUtils.getLastDate(new Date()), "yyyy"));
		}else{//昨天
			map.put("date", DateUtils.format(DateUtils.addDay(new Date(), -1), "yyyy-MM-dd"));
		}
		return JdbcUtils.queryForObject("operationSummary.findReportSalesSum", map, ReportSales.class);
	}

	public ReportSales findStoreMunSum(String storeIdString, String beginTime,
			String endTime) {
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
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		
		return JdbcUtils.queryForObject("reportSales.findStoreMunSum", map, ReportSales.class);
	}

	public Page findStorePage(String storeIdString, Integer source, String beginTime,
			String endTime, Integer type, Page page) {
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
		map.put("source", source);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		map.put("type", type);
		
		
		Integer total = JdbcUtils.queryForObject("reportSales.findStoreTotal", map, Integer.class);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("reportSales.findStorePage", map, ReportSales.class));
		
		return page;
	}

	public ReportSales findStoreSum(String storeIdString,
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
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		
		return JdbcUtils.queryForObject("reportSales.findStoreSum", map, ReportSales.class);
	}

	public List<ReportSales> findStore(String storeIdString, Integer source,
			String beginTime, String endTime, Integer type) {
		
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
		map.put("source", source);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		map.put("type", type);
		
		return JdbcUtils.queryForList("reportSales.findStore", map, ReportSales.class);
	}
}
