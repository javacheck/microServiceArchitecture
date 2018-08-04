package cn.lastmiles.dao;

/**
 * createDate : 2015-07-10 PM 16:07
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

import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class PayAccountDao {
	private final static Logger logger = LoggerFactory
			.getLogger(PayAccountDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 使用余额
	 * @param balance
	 * @param ownerId
	 * @param type
	 */
	public void reduceBalance(Double balance,Long ownerId,Integer type){
		PayAccount payAccount = this.getByOwnerIdAndType(ownerId, type);
		if (payAccount.getBalance().doubleValue() < balance.doubleValue()){
			throw new RuntimeException("余额不正确");
		}
		jdbcTemplate.update("update t_pay_account set balance = ? where ownerId = ? and type = ?", NumberUtils.subtract(payAccount.getBalance(), balance),ownerId,type);
	}

	/**
	 * 根据条件查询支付账号对象信息
	 * 
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @param status
	 *            支付账号状态
	 * @return 支付账号对象或者null
	 */
	public PayAccount queryHaveData(Long ownerId, Integer type, Integer status) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				"select * from t_pay_account where 1=1 ");

		if (null != ownerId) {
			querySQL.append(" and ownerId = ?");
			parameters.add(ownerId);
		}

		if (null != type) {
			querySQL.append(" and type = ?");
			parameters.add(type);
		}

		if (null != status) {
			querySQL.append(" and status = ?");
			parameters.add(status);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				querySQL.toString(), parameters.toArray());
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(PayAccount.class, list.get(0));
	}

	/**
	 * 根据条件修改支付账号的密码信息
	 * 
	 * @param passWord
	 *            修改后的密码
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @return 是否修改成功
	 */
	public Boolean setPassword(String passWord, Long ownerId, Integer type) {
		logger.debug("pwd = {},ownerId = {},type = {}", passWord, ownerId, type);
		int temp = jdbcTemplate
				.update("update t_pay_account set password = ? where ownerId = ? and type= ?",
						passWord, ownerId, type);
		logger.debug(temp + "");
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存支付账号对象信息
	 * 
	 * @param payaccount
	 *            支付账号对象
	 * @return 是否修改成功
	 */
	public Boolean save(PayAccount payaccount) {
		int temp = jdbcTemplate
				.update("insert into t_pay_account(id,ownerId,type,password,createdTime,status,balance,frozenAmount) values(?,?,?,?,?,?,?,?)",
						payaccount.getId(), payaccount.getOwnerId(),
						payaccount.getType(), payaccount.getPassword(),
						new Date(), payaccount.getStatus(),
						payaccount.getBalance(), payaccount.getFrozenAmount());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据条件查询支付账号对象信息
	 * 
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @return 支付账号对象或者null
	 */
	public PayAccount getByOwnerIdAndType(Long ownerId, Integer type) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_pay_account where ownerId = ? and type = ?",
				ownerId, type);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(PayAccount.class, list.get(0));
	}

	public void updateBalance(Double balance, int type, Long ownerId) {
		logger.debug(
				"updateBalance method : balance is {} , type is {} , ownerId is {}",
				balance, type, ownerId);

		jdbcTemplate
				.update("update t_pay_account set balance = ? where ownerId = ? and type = ?",
						balance, ownerId, type);
	}

	/**
	 * 获取支付密码
	 * @param ownerId
	 * @param type
	 * @return
	 */
	public String getPaypassword(Long ownerId, int type) {
		List<String> list = jdbcTemplate
				.queryForList(
						"select password from t_pay_account where ownerId = ? and type = ?",
						String.class, ownerId, type);
		if (list.isEmpty()){
			return null;
		}else {
			return list.get(0);
		}
	}

	public void updateFrozenAmount(Long ownerId, Integer type, Double frozenAmount) {
		String sql ="update t_pay_account set frozenAmount = ? where ownerId = ? and type = ?";
		jdbcTemplate.update(sql,frozenAmount,ownerId,type);
	}

	public Page list(Page page, Integer type, String mobile, Integer status) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" FROM t_pay_account  pc LEFT JOIN t_store s  ON s.id = pc.ownerId LEFT JOIN t_agent a ON a.id = pc.ownerId LEFT JOIN t_user u ON u.id = pc.ownerId WHERE 1=1 ");
		if (StringUtils.isNotBlank(mobile)) {
			sql.append("AND (u.mobile LIKE ?  OR s.mobile LIKE ? OR a.mobile LIKE ?)");
			parameters.add("%"+mobile+"%");
			parameters.add("%"+mobile+"%");
			parameters.add("%"+mobile+"%");
		}
		if (type!=null) {
			sql.append("AND pc.type=?");
			parameters.add(type);
		}
		if (status!=null) {
			sql.append("AND pc.status=?");
			parameters.add(status);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT  pc.*"
						+ ", CASE "
						+ "WHEN pc.type = '-1' THEN '平台' "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE+"' THEN s.name "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT+"' THEN a.name "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_USER+"' THEN u.realName  "
						+ "ELSE '其他' END AS 'name'  "
						+ ", CASE "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE+"' THEN s.mobile "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT+"' THEN a.mobile "
						+ "WHEN pc.type = '"+Constants.PayAccount.PAY_ACCOUNT_TYPE_USER+"' THEN u.mobile  "
						+ "ELSE '其他' END AS 'mobile'  "
						
								+ sql + "  order by pc.createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(PayAccount.class, list));
		return page;
	}
}