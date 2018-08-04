package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class TransactionAnalysisDao {
	private final static Logger logger = LoggerFactory.getLogger(TransactionAnalysisDao.class);
	public List<Map<String,Object>> findDateAll(String storeIdString, Long stockId,
			Integer analysisType, String dateBeginTime, String dateEndTime,
			Integer dateOrderType,Integer source) {
		logger.debug("storeIdString={}",storeIdString);
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
		map.put("stockId", stockId);
		map.put("source", source);
		map.put("dateOrderType",dateOrderType);
		logger.debug("dateOrderType.intValue()={}",dateOrderType.intValue());
		if(dateOrderType.intValue()==1){
			int dNum=0;
			//开始时间和结束时间之间的天数
			dNum =DateUtils.getDayDiff(DateUtils.parse("yyyy-MM-dd",dateEndTime),DateUtils.parse("yyyy-MM-dd",dateBeginTime));
			dNum=dNum+1;
			logger.debug("dNum={}",dNum);
			map.put("dNum",dNum);
		}
		if (!StringUtils.isNotBlank(dateEndTime)) {
			Date d = DateUtils.addDay(new Date(), -6);
			map.put("dateBeginTime", DateUtils.format(d, "yyyy-MM-dd"));
			map.put("dateEndTime", DateUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59");
			logger.debug("dateBeginTime={},dateEndTime={}",DateUtils.format(d, "yyyy-MM-dd"),DateUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59");
		}else{
			map.put("dateBeginTime", dateBeginTime);
			map.put("dateEndTime", dateEndTime + " 23:59:59");
			logger.debug("dateBeginTime1={},dateEndTime1={}",dateBeginTime,dateEndTime + " 23:59:59");
		}
		if(analysisType==null || analysisType.intValue()==0){//setAnalysisType=0 分析订单数量
			map.put("analysisType", null);
		}else if(analysisType.intValue()==1){
			map.put("analysisType", 1);
		}else{//2
			map.put("analysisType", 2);
		}
		
		return  JdbcUtils.queryForList("reportProduct.findDateSum", map);
	}
	public List<Map<String, Object>> findMonthAll(String storeIdString,
			Long stockId, Integer analysisType, String monthTime,
			 Integer monthOrderType, Integer source) {
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
		String monthBeginTime="";
		String monthEndTime="";
		if (!StringUtils.isNotBlank(monthTime)) {
			monthBeginTime=DateUtils.format(DateUtils.getFirstDate(new Date()),"yyyy-MM-dd");
			monthEndTime=DateUtils.format(DateUtils.getLastDay(new Date()),"yyyy-MM-dd");
		}else{
			if(!DateUtils.format(new Date(), "yyyy-MM").equals(monthTime)){//传进来的当前月
				monthBeginTime=DateUtils.format(DateUtils.getFirstDate(DateUtils.parse("yyyy-MM",monthTime)),"yyyy-MM-dd");
				monthEndTime=DateUtils.format(DateUtils.getLastDay(DateUtils.parse("yyyy-MM",monthTime)),"yyyy-MM-dd");
				
			}else{
				monthBeginTime=DateUtils.format(DateUtils.getFirstDate(new Date()),"yyyy-MM-dd");
				monthEndTime=DateUtils.format(new Date(),"yyyy-MM-dd");
			}
		}
		map.put("monthBeginTime", monthBeginTime);
		map.put("monthEndTime", monthEndTime + " 23:59:59");
		
		
		map.put("stockId", stockId);
		map.put("source", source);
		map.put("monthOrderType",monthOrderType);
		logger.debug("monthBeginTime={},monthEndTime={}",monthBeginTime,monthEndTime);
		if(monthOrderType.intValue()==1){
			int mNum=0;
			//开始时间和结束时间之间的天数
			mNum =DateUtils.getDayDiff(DateUtils.parse("yyyy-MM-dd",monthEndTime),DateUtils.parse( "yyyy-MM-dd",monthBeginTime));
			mNum=mNum+1;
			logger.debug("dNum={}",mNum);
			map.put("mNum",mNum);
		}
		
		if(analysisType==null || analysisType.intValue()==0){//setAnalysisType=0 分析订单数量
			map.put("analysisType", null);
		}else if(analysisType.intValue()==1){
			map.put("analysisType", 1);
		}else{//2
			map.put("analysisType", 2);
		}
		
		return  JdbcUtils.queryForList("reportProduct.findMonthSum", map);
	}
	
	public List<Map<String, Object>> findYearAll(String storeIdString,
			Long stockId, Integer analysisType, String yearTime,
			Integer yearOrderType, Integer source) {
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
		String yearBeginTime="";
		String yearEndTime="";
		logger.debug("yearTime={}",yearTime);
		if (!StringUtils.isNotBlank(yearTime)) {
			yearBeginTime=DateUtils.format(DateUtils.getFirstDate(new Date()),"yyyy")+"-01-01";
			yearEndTime=DateUtils.format(DateUtils.getLastDay(new Date()),"yyyy-MM-dd");
		}else{
			if(DateUtils.format(new Date(), "yyyy-MM").equals(yearTime)){//传进来的当前月
				yearBeginTime=DateUtils.format(DateUtils.getFirstDate(new Date()),"yyyy")+"-01-01";
				yearEndTime=DateUtils.format(DateUtils.getLastDay(new Date()),"yyyy-MM-dd");
			}else{
				yearBeginTime=yearTime+"-01-01";
				yearEndTime=yearTime+"-12-31";
			}
		}
		logger.debug("yearBeginTime={},yearEndTime={}",yearBeginTime,yearEndTime);
		map.put("yearBeginTime", yearBeginTime);
		map.put("yearEndTime", yearEndTime + " 23:59:59");
		
		
		map.put("stockId", stockId);
		logger.debug("stockId={}",stockId);
		map.put("source", source);
		map.put("yearOrderType",yearOrderType);
		if(yearOrderType.intValue()==1){
			
			map.put("yNum",12);
		}
		
		if(analysisType==null || analysisType.intValue()==0){//setAnalysisType=0 分析订单数量
			map.put("analysisType", null);
		}else if(analysisType.intValue()==1){
			map.put("analysisType", 1);
		}else{//2
			map.put("analysisType", 2);
		}
		
		return  JdbcUtils.queryForList("reportProduct.findYearSum", map);
	}
	public List<Map<String, Object>> findMonthAll(String storeIdString,
			Integer analysisType, String beginTime, String endTime,Integer source) {
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
		
		map.put("monthBeginTime", beginTime);
		map.put("monthEndTime", endTime);
		map.put("source", source);
		
		logger.debug("monthBeginTime={},monthEndTime={}",beginTime,endTime);
		
		if(analysisType==null || analysisType.intValue()==0){//setAnalysisType=0 分析订单数量
			map.put("analysisType", null);
		}else if(analysisType.intValue()==1){
			map.put("analysisType", 1);
		}else{//2
			map.put("analysisType", 2);
		}
		
		return  JdbcUtils.queryForList("reportProduct.findAppMonth", map);
	}
}
