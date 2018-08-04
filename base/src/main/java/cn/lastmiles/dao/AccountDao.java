package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Account;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class AccountDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(AccountDao.class);

	/**
	 * 根据手机查询
	 * 
	 * @param mobile
	 * @return
	 */
	public Account findByMobile(String mobile) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_account where mobile = ?",
						mobile);
		return list.isEmpty() ? null : BeanUtils.toBean(Account.class,	list.get(0));
	}
	
	public Account findByMobile(String mobile,Integer type) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_account where mobile = ? and type = ?",
						mobile,type);
		return list.isEmpty() ? null : BeanUtils.toBean(Account.class,	list.get(0));
	}

	/**
	 * 分页查找
	 * 
	 * @param mobile
	 * @param type 
	 * @param storeId 
	 * @param agentId 
	 * @param page
	 * @return
	 */
	public Page list(String beginTime,String endTime,String name,Integer sex,String mobile, Integer type, Long agentId, Long storeId, Page page,Long id) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_account where 1 = 1 and id <> "+Constants.Account.SUPER_ACCOUNT_ID);
		if (type != null){
			sql.append(" and type = ? ");
			parameters.add(type);
		}
		
		if (sex!=null&&!sex.equals(-1)) {
			sql.append(" and sex= ?");
			parameters.add(sex);
		}
		if (StringUtils.isNotBlank(name)) {//名称筛选
			sql.append(" and name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {//开始时间筛选
			sql.append(" and createdTime >= ?");
			parameters.add(beginTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			sql.append(" and createdTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		if (StringUtils.isNotBlank(mobile)) {//手机号码筛选
			sql.append(" and mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		//权限初始化
		if (type != null){
			if (type.equals(Constants.Account.ACCOUNT_TYPE_ADMIN)) {
				//管理员
			}else if (type.equals(Constants.Account.ACCOUNT_TYPE_AGENT)) {//代理商
				sql.append(" and agentId = ? ");
				parameters.add(agentId);
			}else if (type.equals(Constants.Account.ACCOUNT_TYPE_STORE)) {//商家
				sql.append(" and storeId = ? ");
				parameters.add(storeId);
			}
		} 
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		
		page.setTotal(total);
		
		if (total.intValue() == 0) {
			return page;
		}
		
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT address,createdTime ,email,id,idCard,mobile,name,parentId,password,path,sex,storeId  "
								+ sql + "  order by createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(Account.class, list));

		return page;
	}

	/**
	 * 更新方法
	 * 
	 * @param account
	 * @return
	 */
	public boolean update(Account account) {
		String sql = "update t_account set address=?," + "email =?,"
				+ "idCard= ?,"  + "mobile= ?," + "name = ?,"
				+ "parentId = ?," + "password = ?, " + "path = ?," + "sex = ?"
				+ " where id = ?";
		int temp = jdbcTemplate.update(sql, account.getAddress(),
				account.getEmail(), account.getIdCard(),
				account.getMobile(), account.getName(), account.getParentId(),
				account.getPassword(), account.getPath(), account.getSex(),
				account.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存方法
	 * 
	 * @param account
	 * @return
	 */
	public boolean save(Account account) {
		String sql = "INSERT INTO t_account(address,createdTime ,email,id,idCard,mobile,name,parentId,password,path,sex,storeId,type,agentId) "
				+ "VALUES (?,now(),?,?,?,?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql, account.getAddress(),
				account.getEmail(), account.getId(), account.getIdCard(),
				account.getMobile(), account.getName(),
				account.getParentId(), account.getPassword(),
				account.getPath(), account.getSex(),account.getStoreId(),account.getType(),account.getAgentId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过ID查找
	 * 
	 * @param id
	 * @return
	 */
	public Account findById(Long id) {
		String sql = "SELECT address,createdTime ,email,id,idCard,storeId,"
				+ "mobile,name,parentId,password,path,sex,agentId from t_account where id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Account.class, list.get(0));
	}

	/**
	 * 通过父类ID查找
	 * 
	 * @param id
	 * @return
	 */
	public Account findByParentId(Long id) {
		String sql = "SELECT address,createdTime ,email,id,idCard,storeId,"
				+ "mobile,name,parentId,password,path,sex from t_account where parentId = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Account.class, list.get(0));
	}
	
	/**
	 * 通过父类ID查找
	 * 
	 * @param id
	 * @return
	 */
	public List<Account> findAllByParentId(Long id) {
		String sql = "SELECT address,createdTime ,email,id,idCard,storeId,"
				+ "mobile,name,parentId,password,path,sex from t_account where path like ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, "%" + id + "-%");
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(Account.class, list);
	}

	/**
	 * 通过ID删除
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		int temp= jdbcTemplate.update("DELETE FROM  t_account  where id = ? ",id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updatePassword(String password, Long accountId) {
		jdbcTemplate.update("update t_account set password = ? where id = ?", password,accountId);
	}

	public List<Account> findByStoreId(Long storeId) {
		String sql = "SELECT * from t_account where storeId = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, storeId);
		return BeanUtils.toList(Account.class, list);
	}

	public List<Map<String, Object>> apiShopfindByStoreId(Long storeId) {
		String sql = "SELECT id,mobile,name from t_account where storeId = ?";
		return jdbcTemplate.queryForList(sql, storeId);
	}

	public void updateMobileByMobile(String mobile, String mobile2) {
		jdbcTemplate.update("update t_account set mobile = ? where mobile = ?", mobile,mobile2);
		
	}
	
	public void updateMobileByMobile(String mobile, String mobile2,Integer type ) {
		jdbcTemplate.update("update t_account set mobile = ? where mobile = ? and type= ?", mobile,mobile2,type);
		
	}

	public void updateCid(Long id, String cid) {
		jdbcTemplate.update("update t_account set cid = ? where id = ?", cid,id);
	}

	public void updatePasswordByMobile(String storeAcountName, String storeAcountPassWord, int accountTypeStore) {
		jdbcTemplate.update("update t_account set password = ? where mobile = ? and type= ?", storeAcountPassWord,storeAcountName,accountTypeStore);
	}
	
	public Account findByStoreIdAndTypeAndMobile(Long storeId, int accountTypeStore, String storeAcountName) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_account where mobile = ? and type = ? and storeId = ? ",
						storeAcountName,accountTypeStore,storeId);
		return list.isEmpty() ? null : BeanUtils.toBean(Account.class,	list.get(0));
	}
}
