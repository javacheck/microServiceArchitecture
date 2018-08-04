package cn.lastmiles.dao;
/**
 * updateDate : 2015-07-16 PM 16:22
 */
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
import cn.lastmiles.bean.Withdraw;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class WithdrawDao {

	private static final Logger logger = LoggerFactory.getLogger(WithdrawDao.class); 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	/**
	 * 保存提现流水记录
	 * @param withdraw 提现流水对象
	 * @return 是否保存成功
	 */
	public boolean save(Withdraw withdraw) {
		logger.debug("save withdraw accountId is {} , save ID is {} ",withdraw.getAccountId(),withdraw.getId());
		if( null != withdraw && null == withdraw.getAmount()){
			withdraw.setAmount(0d);
		}
		String sql = "insert into t_withdraw(id,createdTime,type,ownerId,amount,accountId,bankName,bankAccountNumber,bankAccountName,status)"
				   + "values (? ,?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql, 
				 withdraw.getId(),new Date(), withdraw.getType(),withdraw.getOwnerId(),withdraw.getAmount(),withdraw.getAccountId()
				,withdraw.getBankName(),withdraw.getBankAccountNumber(),withdraw.getBankAccountName(),withdraw.getStatus());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 查询提现流水记录(【管理员/代理商/商家 】查询)
	 * @param accountId 代理商或者商家的accountId，如果是用户null值
	 * @param ownerId 商户id，或者代理商id，用户id
	 * @param type 0商户，1代理商,2用户
	 * @param Id 记录ID
	 * @param name 商家名称或者代理商名称
	 * @param amount 金额
	 * @param status 状态 0 处理中 1 成功 2 失败
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getWithdraw(Long accountId,Long ownerId,Integer type,Long Id,String name,Double amount,Integer status,String startTime,String endTime,Page page){
		logger.debug("query Withdraw record : accountId = {} , ownerId = {} , type = {} , id = {}",accountId,ownerId,type,Id);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_withdraw r ");
		
		// 查询的参数列表
		StringBuffer data = new StringBuffer("r.id,r.createdTime,r.type,r.ownerId,r.amount,r.accountId,r.bankName,r.bankAccountNumber,r.bankAccountName,r.status");
		
		if( null != type ){
			
			if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE)){
				querySQL.append(" INNER JOIN  t_store s on r.ownerId = s.id");
				data.append(",s.name as 'nameS'"); // 与商家表关联得到商家名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and s.name like ? ");
					parameters.add("%" + name + "%");
				}
				
			} else if( type.equals(cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT)) {
				querySQL.append(" INNER JOIN  t_agent a on r.ownerId = a.id");
				data.append(",a.name as 'nameS'"); // 与代理商表关联得到代理商名称
				
				if( StringUtils.isNotBlank(name) ){
					querySQL.append(" and a.name like ? ");
					parameters.add("%" + name + "%");
				}
			}
			
			querySQL.append(" and r.type = ?"); // 先关联表再and查询
			parameters.add(type);
		} else {
			querySQL.append(" where 1=1 "); // 如果没传入type的话
		}
		
		if( null != status ){
			querySQL.append(" and r.status = ? ");
			parameters.add(status);
		}
		
		if( null != amount ){
			querySQL.append(" and r.amount = ? ");
			parameters.add(amount);
		}	
		
		if( null != accountId ){
			querySQL.append(" and r.accountId = ?");
			parameters.add(accountId);
		}
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( null != Id ){
			querySQL.append(" and r.Id like ?");
			parameters.add("%" + Id + "%");
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
		
		page.setData(BeanUtils.toList(Withdraw.class, list));
		
		return page;
	}
	
	/**
	 * 查询提现流水记录(管理员admin查询)
	 * @param accountId 代理商或者商家的accountId，如果是用户null值
	 * @param ownerId 商户id，或者代理商id，用户id
	 * @param Id 记录ID
	 * @param name 商家名称或者代理商名称
	 * @param amount 金额
	 * @param status 状态 0 处理中 1 成功 2 失败
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getWithdraw(Long accountId,Long ownerId,Long Id,String name,Double amount,Integer status,String startTime,String endTime,Page page){
		logger.debug("admin query Withdraw record : accountId = {} , ownerId = {} , id = {}",accountId,ownerId,Id);
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
		
		// 关联商家表
		querySQL.append(" select r.*,s.`name` as 'nameS'  from t_withdraw r  inner join  t_store s on r.ownerId = s.id");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and s.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != amount ){
			querySQL.append(" and r.amount = ? ");
			parameters.add(amount);
		}	
		
		if( null != status ){
			querySQL.append(" and r.status = ? ");
			parameters.add(status);
		}
		
		if( null != accountId ){
			querySQL.append(" and r.accountId = ?");
			parameters.add(accountId);
		}
		
		if( null != Id ){
			querySQL.append(" and r.Id like ?");
			parameters.add("%" + Id + "%");
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
		querySQL.append(" select r.*,a.`name` as 'nameS'  from t_withdraw r  inner join  t_agent a on r.ownerId = a.id ");
		
		if( null != ownerId ){
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and a.name like ? ");
			parameters.add("%" + name + "%");
		}
		
		if( null != amount ){
			querySQL.append(" and r.amount = ? ");
			parameters.add(amount);
		}	
		
		if( null != status ){
			querySQL.append(" and r.status = ? ");
			parameters.add(status);
		}
		
		if( null != accountId ){
			querySQL.append(" and r.accountId = ?");
			parameters.add(accountId);
		}
		
		if( null != Id ){
			querySQL.append(" and r.Id like ?");
			parameters.add("%" + Id + "%");
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
		page.setData(BeanUtils.toList(Withdraw.class, list));
		
		return page;
	}

	public boolean updateStatus(Integer status,Long id,Long ownerId) {
		String sql = "update t_withdraw set status = ? WHERE ID = ? and ownerId = ? ";
		int flag = jdbcTemplate.update(sql, status, id,ownerId);
		if (flag > 0) {
			return true;
		}
		return false;
	}

	public Double getBalance(Long id, Integer type, Long ownerId) {
		Map<String, Object> amount = jdbcTemplate.queryForMap("select amount from t_withdraw where ownerId = ? and type = ? and id = ?",ownerId,type,id);
		return (Double) amount.get("amount");
	}
		
	/**
	 * API商家版提现账户流水接口
	 * @param querySign 查询范围 0：全部、1：一天内、2：一周内、3：一个月内
	 * @param querySort 查询排序 0：智能排序(按ID)、1：按时间排序、2：按金额排序
	 * @param ownerId 查询者ID
	 * @param type 商家类型
	 * @param page
	 * @return
	 */
	public Page getWithdraw( int querySign,int querySort,String bank,String keyword,String timeInterval,Long ownerId,Integer type,Page page ){
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_withdraw r where 1=1 ");
		
		if( null != ownerId ){ // 查询者编号
			querySQL.append(" and r.ownerId = ?");
			parameters.add(ownerId);
		}
		
		if( null != type ){ // 商家
			querySQL.append(" and r.type = ?"); 
			parameters.add(type);			
		}
		
		if( StringUtils.isNotBlank(keyword) ){
			querySQL.append(" and r.id = ?");
			parameters.add(keyword);
		}
		
		if( StringUtils.isNotBlank(bank) ){
			querySQL.append(" and instr(r.bankAccountNumber,?) >0 ");
			parameters.add(bank);
		}
		
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = new GregorianCalendar(); 
		gc.setTime(new Date()); 
		// gc.add(field,value);
		// value为正则往后,为负则往前 
		// field取1加1年,取2加半年,取3加一季度,取4加一周,取5加一天....
		switch (querySign) {
			case 0:
				break;
			case 1: // 一天内
				//gc.add(5,-1);
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				String week = sdf.format(gc.getTime());
				querySQL.append(" and r.createdTime >= ?");
				parameters.add(week + " 00:00:00");
				break;
			case 2: // 一周内
				gc.add(4,-1);
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				String oneMonth = sdf.format(gc.getTime());
				querySQL.append(" and r.createdTime >= ?");
				parameters.add(oneMonth + " 00:00:00");
				break;
			case 3: // 一个月内
				gc.add(3,-3);
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				String threeMonth = sdf.format(gc.getTime());
				querySQL.append(" and r.createdTime >= ?");
				parameters.add(threeMonth + " 00:00:00");
				break;
			case 4: // 根据传入的时间区间进行查询
				if( StringUtils.isBlank(timeInterval) ){
					break;
				}
				querySQL.append(" and r.createdTime >= ?");
				parameters.add(timeInterval.indexOf(0, timeInterval.lastIndexOf("/")) + " 00:00:00");
				querySQL.append(" and r.createdTime <= ?");
				parameters.add(timeInterval.indexOf(timeInterval.lastIndexOf("/")+1,timeInterval.length()) + " 59:59:59");
				break;
			default:
				break;
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() , Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		// 排序
		if( querySort == 0 ){
			querySQL.append(" order by r.id desc limit ?,?");			
		} else if( querySort == 1 ){
			querySQL.append(" order by r.createdTime desc limit ?,?");	
		} else {
			querySQL.append(" order by r.amount desc limit ?,?");
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(0,"select r.id,r.createdTime,r.type,r.ownerId,r.amount,r.accountId,r.bankName,r.bankAccountNumber,r.bankAccountName,r.status ");
		
		page.setData(BeanUtils.toList(Withdraw.class, jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray())));
		
		return page;
	}
	
}