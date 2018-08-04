package cn.lastmiles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.StorageIntakeLog;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class StorageIntakeLogDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(StorageIntakeLogDao.class);
	
	public void posSave(StorageIntakeLog storageIntakeLog){
		String sql = "insert into t_storage_intake_log(id,productName,productStockId,attributeName"
				+ ",barCode,costPrice,price,intakeStock"
				+ ",totalityStock,createdTime,accountName,accountId,storeId,attributeValuesListJointValue,categoryName,shelves) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("posSave --> sql is {},storageIntakeLog is {}",sql,storageIntakeLog);
		jdbcTemplate.update(sql
				,storageIntakeLog.getId(),storageIntakeLog.getProductName(),storageIntakeLog.getProductStockId(),storageIntakeLog.getAttributeName()
				,storageIntakeLog.getBarCode(),storageIntakeLog.getCostPrice(),storageIntakeLog.getPrice(),storageIntakeLog.getIntakeStock()
				,storageIntakeLog.getTotalityStock(),storageIntakeLog.getCreatedTime(),storageIntakeLog.getAccountName(),storageIntakeLog.getAccountId(),storageIntakeLog.getStoreId()
				,storageIntakeLog.getAttributeValuesListJointValue(),storageIntakeLog.getCategoryName(),storageIntakeLog.getShelves());
	}
	
	public Page posList(Page page, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_storage_intake_log where 1 = 1  ");
		if (storeId!=null) {
			sql.append(" and storeId= ?");
			parameters.add(storeId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("posList  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT *  "
								+ sql + "  order by createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(StorageIntakeLog.class, list));
		return page;
	}

	public Page apiShopList(Long productStockId, Long storeId, Integer date, String operator, Integer number, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_storage_intake_log sil where sil.storeId = ? and sil.productStockId = ? ");
		parameters.add(storeId);
		parameters.add(productStockId);
		
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		GregorianCalendar gc = new GregorianCalendar(); 
		gc.setTime(new Date()); 
		switch (date) { // 日期时间筛选条件
			case 0: break; // 全部商品(不进行任何条件约束)
			case 1: 
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE),gc.get(Calendar.HOUR_OF_DAY),gc.get(Calendar.MINUTE),gc.get(Calendar.SECOND));
				String week = sdf.format(gc.getTime());
				querySQL.append(" and sil.createdTime >= ?");
				parameters.add(week);
				break; // 一天内
			case 2:
				gc.add(4,-1);
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE),gc.get(Calendar.HOUR_OF_DAY),gc.get(Calendar.MINUTE),gc.get(Calendar.SECOND));
				String oneMonth = sdf.format(gc.getTime());
				querySQL.append(" and sil.createdTime >= ?");
				parameters.add(oneMonth);
				break; // 一个星期内
			case 3:
				gc.add(3,-3);
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE),gc.get(Calendar.HOUR_OF_DAY),gc.get(Calendar.MINUTE),gc.get(Calendar.SECOND));
				String threeMonth = sdf.format(gc.getTime());
				querySQL.append(" and sil.createdTime >= ? ");
				parameters.add(threeMonth);
				break; // 一个月内
			default: break; //默认也是全部商品
		}
		
		if( StringUtils.isNotBlank(operator) ){
			querySQL.append(" and sil.accountId = ? ");
			parameters.add(operator);
		}
		
		switch (number) { // 入库数量筛选条件
			case 0: break; // 全部商品(不进行任何条件约束)
			case 1: 
				querySQL.append(" and sil.intakeStock < 10 ");
				break; // 10以内
			case 2:
				querySQL.append(" and sil.intakeStock <= 50 and sil.intakeStock >= 10 ");
				break; // 10-50
			case 3:
				querySQL.append(" and sil.intakeStock > 50 ");
				break; // 50以上
			default: break; //默认也是全部商品
		}
		
		logger.debug("apiShopList 查询语句为：{},参数列表为：{}",querySQL.toString(),parameters.toArray());
		
		Integer total = jdbcTemplate.queryForObject("select count(*) "+querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.insert(0," select DATE_FORMAT(sil.createdTime,'%Y-%m-%d %T') as createdTime,sil.intakeStock,sil.totalityStock,(select a.mobile from t_account a where a.id = sil.accountId ) as mobile,(select a.name from t_account a where a.id = sil.accountId ) as name,sil.costPrice ");

		querySQL.append(" order by sil.id desc ");
		querySQL.append(" limit ?,? ");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		page.setData(list);
		return page;
	}

}
