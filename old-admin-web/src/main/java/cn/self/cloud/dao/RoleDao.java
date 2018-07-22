package cn.self.cloud.dao;

import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Account;
import cn.self.cloud.bean.Role;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 角色Dao
 * 
 * @author hql
 *
 */
@Repository
public class RoleDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查找拥有的角色
	 * @param accountId
	 * @return
	 */
	public List<Role> findByAccountId(Long accountId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT r.id,r.name,r.value,r.createdId from t_account_role ar ");
		sql.append("INNER JOIN t_role r on ar.roleId = r.id where ar.accountId =  ?");

		return BeanUtils.toList(Role.class,
				jdbcTemplate.queryForList(sql.toString(), accountId));
	}

	public void save(Role role) {
		jdbcTemplate.update(
				"insert into t_role(id,name,value,createdId) values(?,?,?,?)",
				role.getId(), role.getName(), role.getValue(),
				role.getCreatedId());
	}

	public Role findById(Long id) {
		return BeanUtils.toBean(Role.class, jdbcTemplate.queryForMap(
				"select id,name from t_role where id = ?", id));
	}

	public void edit(Role role) {

	}

	/**
	 * 查询自己创建的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Role> findByCreatedId(Long accountId) {
		return BeanUtils
				.toList(Role.class,	jdbcTemplate.queryForList(
						"select id,name,value,createdId from t_role where createdId = ?",accountId));
	}

	public void updateName(Role role) {
		String sql = "update t_role set name = ? where id = ?";
		jdbcTemplate.update(sql, role.getName(),role.getId());
	}

	public void delete(Long id) {
		String sql = "delete from  t_role  where id = ?";
		jdbcTemplate.update(sql, id);
	}

	public List<Role> findKidsByIdAndValue(Integer value) {
		return BeanUtils
				.toList(Role.class,	jdbcTemplate.queryForList(
						"select id,name,value,createdId from t_role  WHERE value <= ? ",value));
	}

	public Role findMaxByAccountId(Long accountId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT r.id,r.name,r.value,r.createdId from t_account_role ar ");
		sql.append("INNER JOIN t_role r on ar.roleId = r.id where ar.accountId =  ? ORDER BY value DESC LIMIT 0,1");
		
		return BeanUtils.toBean(Role.class,jdbcTemplate.queryForList(sql.toString(), accountId).get(0));
	}

	public List<Account> findAccountByRoleId(Long roleId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select a.id,a.name from t_account_role ar INNER JOIN  t_account a on  ar.accountId=a.id where ar.roleId=?", roleId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(Account.class, list);
	}

	public Role findbyName(String roleName) {
		List<Map<String, Object>> list=jdbcTemplate.queryForList(
				"select * from t_role where name = ?", roleName);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Role.class, list.get(0));
	}
}
