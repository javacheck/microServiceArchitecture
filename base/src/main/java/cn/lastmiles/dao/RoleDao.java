package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Role;
import cn.lastmiles.common.utils.BeanUtils;

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
		sql.append("SELECT r.* from t_account_role ar ");
		sql.append("INNER JOIN t_role r on ar.roleId = r.id where ar.accountId =   ? ORDER BY  r.createTime desc ");

		return BeanUtils.toList(Role.class,jdbcTemplate.queryForList(sql.toString(), accountId));
	}

	public void save(Role role) {
		jdbcTemplate.update(
				"insert into t_role(id,name,value,createdId,createTime,type,ownerId) values(?,?,?,?,?,?,?)",
				role.getId(), role.getName(), role.getValue(),
				role.getCreatedId(),new Date(),role.getType(),role.getOwnerId());
	}

	public Role findById(Long id) {
		return BeanUtils.toBean(Role.class, jdbcTemplate.queryForMap(
				"select * from t_role where id = ?", id));
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
		StringBuffer sf =new StringBuffer();
		sf.append(" select id,name,value,createdId from t_role  where 1=1");
		sf.append(" and createdId = ?");
		return BeanUtils.toList(Role.class,jdbcTemplate.queryForList(sf.toString(),accountId));
	}

	public void updateName(Role role) {
		String sql = "update t_role set name = ? ,type = ? ,ownerId = ? where id = ?";
		jdbcTemplate.update(sql, role.getName(),role.getType(),role.getOwnerId(),role.getId());
	}

	public void delete(Long id) {
		String sql = "delete from  t_role  where id = ?";
		jdbcTemplate.update(sql, id);
	}

	public List<Role> findKidsByValue(Integer value) {
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

	public Role findbyName(String roleName, Integer type, Long ownerId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from t_role where 1=1 ");
		if (ownerId!=null) {
			sql.append(" and ownerId = ? ");
			parameters.add(ownerId);
		}else{
			sql.append(" and ownerId is null ");
		}
		if (type!=null) {
			sql.append(" and type = ? ");
			parameters.add(type);
		}
		if (roleName!=null) {
			sql.append(" and name = ? ");
			parameters.add(roleName);
		}
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql.toString(), parameters.toArray());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Role.class, list.get(0));
	}

	public List<Role> findAll() {
		List<Map<String, Object>> list=jdbcTemplate.queryForList("select r.id,r.name,r.value,r.createdId from t_role r ");
		return BeanUtils.toList(Role.class,list );
	}
}
