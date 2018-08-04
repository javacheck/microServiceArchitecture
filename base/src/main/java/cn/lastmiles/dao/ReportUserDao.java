package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class ReportUserDao {
	private final static Logger logger = LoggerFactory.getLogger(ReportUserDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public ReportUser findReportSalesSum(String storeIdString, Integer type) {
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
		return JdbcUtils.queryForObject("operationSummary.findReportUserSum", map, ReportUser.class);
	}

	public Page findAllPage(String storeIdString, String beginTime,
			String endTime,Integer sort, Page page) {
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
		map.put("sort", sort);
		Integer total = JdbcUtils.queryForObject("reportUser.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("reportUser.findAllPage", map, ReportUser.class));
		
		return page;
	}

	public ReportUser findAllSum(String storeIdString, String beginTime,
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
		map.put("date", endTime);
		
		return JdbcUtils.queryForObject("reportUser.findAllSum", map, ReportUser.class);
	}

	public ReportUser findStoreMunSum(String storeIdString, String beginTime,
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
		
		return JdbcUtils.queryForObject("reportUser.findStoreMunSum", map, ReportUser.class);
	}

	public List<ReportUser> findAll(String storeIdString, String beginTime,
			String endTime,Integer sort) {
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
		map.put("sort", sort);
		return JdbcUtils.queryForList("reportUser.findAll", map, ReportUser.class);
	}

	public Page findStorePage(Long storeId, Integer type, String beginTime,
			String endTime,Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("type", type);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		logger.debug("storeId={}",storeId);
		Integer total = JdbcUtils.queryForObject("reportUser.findStoreTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		if(type.intValue()==0){
			page.setData(JdbcUtils.queryForList("reportUser.findStorePageByDate", map, ReportUser.class));
		}else{
			page.setData(JdbcUtils.queryForList("reportUser.findStorePageByMoon", map, ReportUser.class));
		}
		
		return page;
	}

	public ReportUser findStoreSum(Long storeId, Integer type,
			String beginTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("storeId", storeId);
		map.put("date", endTime);
		
		return JdbcUtils.queryForObject("reportUser.findStoreSum", map, ReportUser.class);
	}
	public ReportUser findStoreSumByTime(Long storeId, Integer type,
			String beginTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("storeId", storeId);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		
		return JdbcUtils.queryForObject("reportUser.findStoreSumByTime", map, ReportUser.class);
	}
	public List<ReportUser> findStore(Long storeId, Integer type,
			String beginTime, String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("storeId", storeId);
		map.put("type", type);
		map.put("beginTime", beginTime);
		if (StringUtils.isNotBlank(endTime)) {
			endTime=endTime + " 23:59:59";
		}
		map.put("endTime", endTime);
		if(type.intValue()==0){
			return JdbcUtils.queryForList("reportUser.findStoreByDate", map, ReportUser.class);
		}else{
			return JdbcUtils.queryForList("reportUser.findStoreByMoon", map, ReportUser.class);
		}
		
	}

	/**
	 * 返回会员统计表中的日期集合
	 */
	public List<ReportUser> findReportUserDateList(){
		StringBuffer querySQL = new StringBuffer(" select s.id,str_to_date(s.reportDate,'%Y-%m-%d')  as reportDate from (");
		querySQL.append(" select distinct(ru.reportDate) as reportDate,ru.id from  ");
		querySQL.append(" ( ");
		querySQL.append(" select  id,(date_format(reportDate,'%Y-%m-%d')) as reportDate  from t_report_user  ");
		querySQL.append(" ) ru ");
		querySQL.append(" order by reportDate desc ");
		querySQL.append(") s");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(ReportUser.class, list);
		}
		return null;
	}
	
	public int insertReportUser(Long id,Long storeId,String date){
		logger.debug("insertReportUser id is {},storeId is {},date is {}",id,storeId,date);
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer insertSQL = new StringBuffer(" insert into t_report_user");
		insertSQL.append(" ( ");
		insertSQL.append(" select ");
		insertSQL.append("?");
		parameters.add(id);
		
		insertSQL.append(",");
		
		insertSQL.append("?");
		parameters.add(storeId);
		
		insertSQL.append(",");
		
		insertSQL.append("( select count(*) from t_user_card where storeId = ? and createdTime <= ? and createdTime >= ?) as userNum ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		parameters.add(date+" 00:00:00");
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and createdTime >= ? and type = ? ) as consumption ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		parameters.add(date+" 00:00:00");
		parameters.add(Constants.UserCardRecord.TYPE_CONSUMER);
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and createdTime >= ? and type = ? ) as recharge ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		parameters.add(date+" 00:00:00");
		parameters.add(Constants.UserCardRecord.TYPE_RECHARGE);
		
		insertSQL.append(",");
		
		insertSQL.append("( select count(*) from t_user_card where storeId = ? and createdTime <= ? ) as totalUserNum ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and type = ? ) as totalConsumption ");
		parameters.add(storeId);
		parameters.add(date + " 23:59:59");
		parameters.add(Constants.UserCardRecord.TYPE_CONSUMER);
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and type = ? ) as totalRecharge ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		parameters.add(Constants.UserCardRecord.TYPE_RECHARGE);
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( SUM(balance) is null , 0 , convert(sum(balance),decimal(65,2)) ) from t_user_card where storeId = ? and createdTime <= ? ) as totalBalance ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		
		insertSQL.append(",");
		
		insertSQL.append("( select if( SUM(point) is null , 0 , truncate(sum(point),2) ) from t_user_card where storeId = ? and createdTime <= ? ) as totalPoint ");
		parameters.add(storeId);
		parameters.add(date+" 23:59:59");
		
		insertSQL.append(",");
		
		insertSQL.append("? as reportDate ");
		parameters.add(date);
		insertSQL.append(" ) ");
		
		logger.debug("insertSQL is {},parameters is {}",insertSQL.toString(),parameters.toArray());
//		return 0;
		return jdbcTemplate.update(insertSQL.toString(),parameters.toArray());
	}

	public int updateReportUser(Long id, Long storeId, String date, String time) {
		logger.debug("id is {},storeId is {},date is {},time is {}",id,storeId,date,time);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer updateSQL = new StringBuffer(" update t_report_user ");
		updateSQL.append(" set ");
		updateSQL.append(" userNum = ( select count(*) from t_user_card where storeId = ? and createdTime <= ? and createdTime >= ?) ");
		parameters.add(storeId);
		parameters.add(time);
		parameters.add(date+" 00:00:00");
		
		updateSQL.append(" , ");
		
		updateSQL.append(" consumption = ( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and createdTime >= ? and type = ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		parameters.add(date+" 00:00:00");
		parameters.add(Constants.UserCardRecord.TYPE_CONSUMER);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" recharge = ( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and createdTime >= ? and type = ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		parameters.add(date+" 00:00:00");
		parameters.add(Constants.UserCardRecord.TYPE_RECHARGE);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" totalUserNum = ( select count(*) from t_user_card where storeId = ? and createdTime <= ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" totalConsumption = ( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and type = ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		parameters.add(Constants.UserCardRecord.TYPE_CONSUMER);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" totalRecharge = ( select if( sum(actualAmount) is null , 0 , convert(sum(actualAmount),decimal(65,2)) ) from t_user_card_record where storeId = ? and createdTime <= ? and type = ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		parameters.add(Constants.UserCardRecord.TYPE_RECHARGE);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" totalBalance = ( select if( SUM(balance) is null , 0 , convert(sum(balance),decimal(65,2)) ) from t_user_card where storeId = ? and createdTime <= ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		
		updateSQL.append(" , ");
		
		updateSQL.append(" totalPoint = ( select if( SUM(point) is null , 0 , truncate(sum(point),2) ) from t_user_card where storeId = ? and createdTime <= ? ) ");
		parameters.add(storeId);
		parameters.add(time);
		
		updateSQL.append(" where id = ? ");
		parameters.add(id);
		
		logger.debug("updateSQL is {},parameters is {}",updateSQL.toString(),parameters.toArray());
//		return 0;
		return jdbcTemplate.update(updateSQL.toString(),parameters.toArray());
	}

	public List<ReportUser> findReportUserStoreIdListByDate(String time) {
		logger.debug("time is {}",time);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select id,storeId  from t_report_user where 1=1 ");
		querySQL.append(" and reportDate = ?");
		parameters.add(time);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(ReportUser.class, list);
		}
		return null;
	}

	public List<ReportUser> findTotalReportUserList(String storeIdString,
			String endTime, Integer sort) {
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
		
		
		map.put("endTime", endTime);
		map.put("sort", sort);
		
		
		
		return JdbcUtils.queryForList("reportUser.findTotalReportUserList", map, ReportUser.class);
	}

	public ReportUser findAllSumByTime(String storeIdString, String beginTime,
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
		
		return JdbcUtils.queryForObject("reportUser.findAllSumByTime", map, ReportUser.class);
	}
	public ReportUser findTotolSumByTime(String storeIdString,
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
		
		if (StringUtils.isNotBlank(endTime)) {
			map.put("endTime", endTime);
		}
		
		return JdbcUtils.queryForObject("reportUser.findTotolSumByTime", map, ReportUser.class);
	}
	public ReportUser findreportUserByLastDate(Long storeId,
			String moonDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("storeId", storeId);
		map.put("moonDate", moonDate);
		
		return JdbcUtils.queryForObject("reportUser.findreportUserByLastDate", map, ReportUser.class);
	}

	public Double findAmountSumByCreatedTime(Long storeId, String tempDate, Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("createdTime", tempDate);
		map.put("type", type);
		return JdbcUtils.queryForObject("reportUser.findAmountSumByCreatedTime", map, Double.class);
	}

	public List<UserCardRecord> findByCreatedTime(Long storeId, String tempDate, Integer type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("createdTime", tempDate);
		map.put("type", type);
		return JdbcUtils.queryForList("reportUser.findByCreatedTime", map, UserCardRecord.class);
	}

	public Order findByOrderId(Long orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		
		return JdbcUtils.queryForObject("reportUser.findByOrderId", map, Order.class);
	}

	public ReportUser findUserCardSumByCreatedTime(Long storeId,
			String tempDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("createdTime", tempDate);
		return JdbcUtils.queryForObject("reportUser.findUserCardSumByCreatedTime", map, ReportUser.class);
	}

	public ReportUser findByReportDate(Long storeId, String tempDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("reportDate", tempDate);
		return JdbcUtils.queryForObject("reportUser.findByReportDate", map, ReportUser.class);
	}

	public void saveReportUser(ReportUser ru) {
		JdbcUtils.save(ru);
	}

	public void updateReportUser(ReportUser ru) {
		JdbcUtils.update(ru);
	}

	
}
