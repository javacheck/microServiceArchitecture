package cn.lastmiles.dao;
/**
 * updateDate : 2015-07-16 PM 18:09
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cn.lastmiles.bean.SettlementsRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class SettlementsRecordDao {
	
	private final static Logger logger = LoggerFactory.getLogger(SettlementsRecordDao.class) ;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查询账户流水记录(【管理员/代理商/商家 】查询)
	 * @param ownerId 商户id，或者代理商id
	 * @param type 0商户，1代理商
	 * @param orderId 订单ID
	 * @param name 商家名称或者代理商名称
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getSettlementsRecord(Long ownerId,Integer type,Long orderId,String name,String startTime,String endTime,Page page){
		logger.debug("query SettlementsRecord record : ownerId = {} , type = {} , orderId = {}",ownerId,type,orderId);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_settlements_record r ");
		
		// 查询的参数列表
		StringBuffer data = new StringBuffer("r.id,r.orderId, r.ownerId,r.type,r.amount,r.createdTime,r.balance");
		
		if( null != type ){
			if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE)){
				querySQL.append(" INNER JOIN  t_store s on r.ownerId = s.id and r.type = 0 ");
				data.append(",s.name as 'nameS'"); // 与商家表关联得到商家名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and s.name like ? ");
					parameters.add("%" + name + "%");
				}	
				querySQL.append(" and r.type = ?"); // 先关联表再and查询

			} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT)) {
				querySQL.append(" INNER JOIN  t_agent a on r.ownerId = a.id and r.type = 1 ");
				data.append(",a.name as 'nameS'"); // 与代理商表关联得到代理商名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and a.name like ? ");
					parameters.add("%" + name + "%");
				}
				querySQL.append(" and r.type = ?"); // 先关联表再and查询
				
			} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_PLATFORMP) ){
				querySQL.append(" where r.type = ?"); 
				if( StringUtils.isNotBlank(name) ){
					if( !"平台".equals(name) ){
						querySQL.append(" and 1=2 ");
					}
				}
			}
			
			parameters.add(type);
		} else{
			querySQL.append(" where 1=1 "); // 如果没传入type的话
		}
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) { // 开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) { // 结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
					
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() , Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by r.id desc limit ?,?", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		page.setData(BeanUtils.toList(SettlementsRecord.class, list));
		
		return page;
	}
	
	/**
	 * 查询账户流水记录(管理员admin查询)
	 * @param ownerId 商户id，或者代理商id
	 * @param orderId 订单ID
	 * @param name 商家名称或者代理商名称
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getSettlementsRecord(Long ownerId,Long orderId,String name,String startTime,String endTime,Page page){
		logger.debug("admin query SettlementsRecord record :  ownerId = {} , orderId = {}",ownerId,orderId);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
		
		// 关联商家表
		querySQL.append(" select r.*,s.`name` as 'nameS'  from t_settlements_record r  inner join  t_store s on r.ownerId = s.id and r.type = 0 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and s.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		querySQL.append(" union ");
		
		// 关联代理商表
		querySQL.append(" select r.*,a.`name` as 'nameS'  from t_settlements_record r  inner join  t_agent a on r.ownerId = a.id and r.type = 1 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and a.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		querySQL.append(" union ");
		
		// 平台
		querySQL.append(" select r.*,null as 'nameS'  from t_settlements_record r  where r.type = -1 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
			
		if( StringUtils.isNotBlank(name) ){
			if( !"平台".equals(name) ){
				querySQL.append(" and 1=2 ");
			}
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) from (" + querySQL.toString() + ") z" , Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from (" + querySQL.toString() + ") z order by z.id desc limit ?,?", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		page.setData(BeanUtils.toList(SettlementsRecord.class, list));
		
		return page;
	}
	
	/**
	 * API-查询账户流水记录(商家/代理商)
	 * @param ownerId 商户id，或者代理商id
	 * @param type 类型
	 * @param page
	 */
	public Page getSettlementsRecord(Long ownerId,Integer type,Page page){
		logger.debug("API query SettlementsRecord record : ownerId = {} , type = {} ",ownerId,type);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_settlements_record r ");
		
		// 查询的参数列表
		StringBuffer data = new StringBuffer("r.id,r.orderId, r.ownerId,r.type,r.amount,r.createdTime,r.balance");
		
		if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE)){
			querySQL.append(" INNER JOIN  t_store s on r.ownerId = s.id and r.type = 0 ");
			data.append(",s.name as 'nameS'"); // 与商家表关联得到商家名称
		} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT)) {
			querySQL.append(" INNER JOIN  t_agent a on r.ownerId = a.id and r.type = 1 ");
			data.append(",a.name as 'nameS'"); // 与代理商表关联得到代理商名称
		}
		querySQL.append(" and r.type = ?"); // 先关联表再and查询
		parameters.add(type);
		
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
					
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() , Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by r.createdTime desc limit ?,?", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		page.setData(BeanUtils.toList(SettlementsRecord.class, list));
		
		return page;
	}

	public void save(SettlementsRecord settlementsRecord) {
		String sql = "insert into t_settlements_record(id,orderId,ownerId,type,amount,createdTime,balance,status) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql,settlementsRecord.getId(),settlementsRecord.getOrderId(),settlementsRecord.getOwnerId(),settlementsRecord.getType(),settlementsRecord.getAmount(),new Date(),settlementsRecord.getBalance(),settlementsRecord.getStatus());
	}

	public void updateStatus(Long id, Integer status) {
		String sql = "update t_settlements_record set status = ? where id = ?";
		logger.debug("updateStatus ---> sql is {},id is {},status is {},",sql,id,status);
		jdbcTemplate.update(sql,status,id);
	}
	public List<SettlementsRecord> findAllBySearch(Long ownerId, Integer type,
			Long orderId, String name, String startTime, String endTime) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_settlements_record r ");
		
		// 查询的参数列表
		StringBuffer data = new StringBuffer("r.id,r.orderId, r.ownerId,r.type,r.amount,r.createdTime,r.balance");
		
		if( null != type ){
			if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE)){
				querySQL.append(" INNER JOIN  t_store s on r.ownerId = s.id and r.type = 0 ");
				data.append(",s.name as 'nameS'"); // 与商家表关联得到商家名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and s.name like ? ");
					parameters.add("%" + name + "%");
				}	
				querySQL.append(" and r.type = ?"); // 先关联表再and查询

			} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT)) {
				querySQL.append(" INNER JOIN  t_agent a on r.ownerId = a.id and r.type = 1 ");
				data.append(",a.name as 'nameS'"); // 与代理商表关联得到代理商名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and a.name like ? ");
					parameters.add("%" + name + "%");
				}
				querySQL.append(" and r.type = ?"); // 先关联表再and查询
				
			} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_PLATFORMP) ){
				querySQL.append(" where r.type = ?"); 
			}
			
			parameters.add(type);
		} else{
			querySQL.append(" where 1=1 "); // 如果没传入type的话
		}
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) { // 开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) { // 结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
					

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by r.id desc ", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		
		return BeanUtils.toList(SettlementsRecord.class, list);
	}

	public List<SettlementsRecord> findAllBySearch(Long ownerId, Long orderId,
			String name, String startTime, String endTime) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
		
		// 关联商家表
		querySQL.append(" select r.*,s.`name` as 'nameS'  from t_settlements_record r  inner join  t_store s on r.ownerId = s.id and r.type = 0 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and s.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		querySQL.append(" union ");
		
		// 关联代理商表
		querySQL.append(" select r.*,a.`name` as 'nameS'  from t_settlements_record r  inner join  t_agent a on r.ownerId = a.id and r.type = 1 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and a.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		querySQL.append(" union ");
		
		// 平台
		querySQL.append(" select r.*,null as 'nameS'  from t_settlements_record r  where r.type = -1 ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
				
		if( null != orderId ){
			querySQL.append(" and r.orderId like ?");
			parameters.add("%" + orderId + "%");
		}
		
		if (StringUtils.isNotBlank(startTime)) {//开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime +" 00:00:00");
		}
		
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime +" 23:59:59");
		}
		
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from (" + querySQL.toString() + ") z order by z.id desc ", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		
		return BeanUtils.toList(SettlementsRecord.class, list);
	}

	public List<SettlementsRecord> findAllBySearch(Long ownerId, Integer type) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_settlements_record r ");
		
		// 查询的参数列表
		StringBuffer data = new StringBuffer("r.id,r.orderId, r.ownerId,r.type,r.amount,r.createdTime,r.balance");
		
		if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE)){
			querySQL.append(" INNER JOIN  t_store s on r.ownerId = s.id and r.type = 0 ");
			data.append(",s.name as 'nameS'"); // 与商家表关联得到商家名称
		} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT)) {
			querySQL.append(" INNER JOIN  t_agent a on r.ownerId = a.id and r.type = 1 ");
			data.append(",a.name as 'nameS'"); // 与代理商表关联得到代理商名称
		}
		querySQL.append(" and r.type = ?"); // 先关联表再and查询
		parameters.add(type);
		
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
					
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by r.createdTime desc", parameters.toArray());
		for (Map<String, Object> map : list) {
			map.put("amountString", NumberUtils.dobleToString((Double) map.get("amount")));
		}
		
		return BeanUtils.toList(SettlementsRecord.class, list);
	}
}