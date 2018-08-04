package cn.lastmiles.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Permission;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class PermissionDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 所有需要权限验证的url
	 * @return
	 */
	public List<String> findAllUrl() {
		return jdbcTemplate.queryForList("select url from t_permission",
				String.class);
	}

	/**
	 * 查询accountId拥有的权限
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Permission> findByAccountId(Long accountId) {
		
		if (accountId.equals(Constants.Account.SUPER_ACCOUNT_ID)) {
			return BeanUtils.toList(Permission.class,jdbcTemplate.queryForList("select * from t_permission "));
		}
		StringBuilder sql = new StringBuilder("SELECT distinct p.* from t_role_permission rp ");
		sql.append(" INNER JOIN t_role role on rp.roleId = role.id ");
		sql.append(" INNER JOIN t_permission p on rp.permissionId = p.id ");
		sql.append(" INNER JOIN t_account_role ar on ar.roleId = role.id ");
		sql.append(" INNER JOIN t_account a on ar.accountId = a.id ");
		sql.append(" where a.id = ? ");
		return BeanUtils.toList(Permission.class,jdbcTemplate.queryForList(sql.toString(), accountId));
	}

	/**
	 * 根据角色ID 查找权限
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Permission> findByRoleId(Long roleId) {
		String sql = "SELECT distinct p.* from t_role_permission rp INNER JOIN t_permission p on rp.permissionId = p.id WHERE roleId = ? ";
		return BeanUtils.toList(Permission.class,
				jdbcTemplate.queryForList(sql, roleId));
	}

	public void save(Long roleId, Long[] permissionIds) {
		String sql = "INSERT INTO t_role_permission(roleId,permissionId) VALUES (?,?)";
		for (Long permissionId : permissionIds) {
			jdbcTemplate.update(sql, roleId, permissionId);
		}
	}

	public void deleteRolePermission(Long roleId) {
		String sql = "DELETE FROM t_role_permission WHERE roleId = ?";
		jdbcTemplate.update(sql, roleId);
	}
	/**
	 * 查找所有分类名称
	 * @return
	 */
	public List<String> findAllCategoryName(){
		String sql = "SELECT DISTINCT categoryName FROM t_permission";
		return jdbcTemplate.queryForList(sql,String.class);
	}
	/**
	 * 根据账户拥有的权限和分类分名称查找
	 * @param categoryName
	 * @return
	 */
	public List<Permission> findByCategoryNameAndAccountId(String categoryName,Long accountId){
		StringBuilder sql = new StringBuilder("SELECT distinct p.* from t_role_permission rp ");
		sql.append(" INNER JOIN t_role role on rp.roleId = role.id ");
		sql.append(" INNER JOIN t_permission p on rp.permissionId = p.id ");
		sql.append(" INNER JOIN t_account_role ar on ar.roleId = role.id ");
		sql.append(" INNER JOIN t_account a on ar.accountId = a.id ");
		sql.append(" where a.id = ? AND p.categoryName = ?  ");
		return BeanUtils.toList(Permission.class,jdbcTemplate.queryForList(sql.toString(),accountId,categoryName));
	}

	public List<Permission> findByCategoryName(String categoryName) {
		StringBuilder sql = new StringBuilder("SELECT distinct p.* from t_permission p where  p.categoryName = ? ");
		return BeanUtils.toList(Permission.class,jdbcTemplate.queryForList(sql.toString(),categoryName));
	}
	
}
