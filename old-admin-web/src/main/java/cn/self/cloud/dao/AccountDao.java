package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Account;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 根据手机查询
	 * 
	 * @param mobile
	 * @return
	 */
	public Account findByMobile(String mobile) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,mobile,password,name,parentId,path,storeId from t_account where mobile = ?",
						mobile);
		return list.isEmpty() ? null : BeanUtils.toBean(Account.class,
				list.get(0));
	}

	/**
	 * 分页查找
	 * 
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(String mobile, Page page, Long id) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_account where 1 = 1 ");

		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		parameters.add("%"  + id + "-%");
		parameters.add("%-" + id + "");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql + "and  (path like ? OR path LIKE ? ) ",
				Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT address,createdTime ,email,id,idCard,mobile,name,parentId,password,path,sex,storeId  "
								+ sql + " and  (path like ? OR path LIKE ? ) order by id limit ?,?",
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
				+ "parentId = ?," + "password = ?, " + "path = ?," + "sex = ?,"+ " storeId = ?"
				+ " where id = ?";
		int temp = jdbcTemplate.update(sql, account.getAddress(),
				account.getEmail(), account.getIdCard(),
				account.getMobile(), account.getName(), account.getParentId(),
				account.getPassword(), account.getPath(), account.getSex(),account.getStoreId(),
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
		String sql = "INSERT INTO t_account(address,createdTime ,email,id,idCard,mobile,name,parentId,password,path,sex,storeId) "
				+ "VALUES (?,now(),?,?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql, account.getAddress(),
				account.getEmail(), account.getId(), account.getIdCard(),
				account.getMobile(), account.getName(),
				account.getParentId(), account.getPassword(),
				account.getPath(), account.getSex(),account.getStoreId());
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
				+ "mobile,name,parentId,password,path,sex from t_account where id = ?";
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
}
