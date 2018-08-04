/**
 * createDate : 2016年5月11日上午11:00:32
 */
package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.MessageRecharge;
import cn.lastmiles.bean.MessageRechargeRecord;
import cn.lastmiles.bean.MessageSaleRecord;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class MessageRechargeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(String storeName,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_message_recharge mr where 1=1 ");
		
		if( StringUtils.isNotBlank(storeName) ){
			querySQL.append(" and mr.storeId in ( select s.id from t_store s where s.name like ? )");
			parameters.add("%"+storeName+"%");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class,parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select mr.*,(select name from t_store s where s.id = mr.storeId ) as 'storeName' ");
		querySQL.append(" limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(MessageRecharge.class, list));

		return page;
	}

	public void batchSave(List<Object[]> saveBatchArray) {
		String insertSQL = " insert into t_message_recharge(id,storeId,accountId,price,number,updateTime,remainNumber) values(?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSQL, saveBatchArray);
	}

	public void batchUpdate(List<Object[]> updateBatchArray) {
		String updateSQL = " update t_message_recharge set accountId = ? ,price = ? ,number = ? ,updateTime = ? ,remainNumber = ?,beforeRemainNumber = ?  where storeId = ? ";
		jdbcTemplate.batchUpdate(updateSQL, updateBatchArray);
	}

	public void save(MessageRechargeRecord messageRechargeRecord) {
		JdbcUtils.save(messageRechargeRecord);
	}

	public List<MessageRecharge> find() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_message_recharge");
		return BeanUtils.toList(MessageRecharge.class, list);
	}

	public void batchSaveRecord(List<Object[]> batchArray) {
		String insertSQL = " insert into t_message_recharge_record(id,storeId,accountId,price,number,createdTime,remainNumber) values(?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSQL, batchArray);
	}

	public Page findStoreList(String modelStoreName, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s where 1=1 ");
		
		if( StringUtils.isNotBlank(modelStoreName) ){
			querySQL.append(" and s.name like ? ");
			parameters.add("%"+modelStoreName+"%");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class,parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select s.* ");
		querySQL.append(" limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	public Page list(Long storeId, Long accountId, String searchTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_message_recharge_record mrr where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and mrr.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != accountId && !ObjectUtils.equals(-1L, accountId)){
			querySQL.append(" and mrr.accountId = ? ");
			parameters.add(accountId);
		}
		
		if( StringUtils.isNotBlank(searchTime) ){
			querySQL.append(" and DATE_FORMAT(mrr.createdTime,'%Y-%m-%d') = ? ");
			parameters.add(searchTime);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class,parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select mrr.*,(select name from t_store s where s.id = mrr.storeId ) as 'storeName',(select name from t_account a where a.id = mrr.accountId) as operationName ");
		querySQL.append("order by mrr.createdTime desc limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(MessageRechargeRecord.class, list));

		return page;
	}

	public List<Account> findAccountList() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_account where type = ?", Constants.Account.ACCOUNT_TYPE_ADMIN);
		return BeanUtils.toList(Account.class, list);
	}

	public Page list(Long storeId,String userAccount, Integer type, String searchTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_message_sale_record msr where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and msr.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(userAccount) ){
			querySQL.append(" and msr.userAccountMobile = ? ");
			parameters.add(userAccount);
		}
		
		if( null != type && !ObjectUtils.equals(-1, type)){
			querySQL.append(" and msr.type = ? ");
			parameters.add(type);
		}
		
		if( StringUtils.isNotBlank(searchTime) ){
			querySQL.append(" and DATE_FORMAT(msr.createdTime,'%Y-%m-%d') = ? ");
			parameters.add(searchTime);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class,parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select msr.*");
		querySQL.append("order by msr.createdTime desc limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(MessageSaleRecord.class, list));

		return page;
	}

	public List<MessageSaleRecord> list(Long storeId, String userAccount, Integer type, String searchTime) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_message_sale_record msr where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and msr.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(userAccount) ){
			querySQL.append(" and msr.userAccountMobile = ? ");
			parameters.add(userAccount);
		}
		
		if( null != type && !ObjectUtils.equals(-1, type)){
			querySQL.append(" and msr.type = ? ");
			parameters.add(type);
		}
		
		if( StringUtils.isNotBlank(searchTime) ){
			querySQL.append(" and DATE_FORMAT(msr.createdTime,'%Y-%m-%d') = ? ");
			parameters.add(searchTime);
		}
		
		querySQL.insert(0, " select msr.*");
		querySQL.append("order by msr.createdTime desc ");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		return (BeanUtils.toList(MessageSaleRecord.class, list));
	}
}