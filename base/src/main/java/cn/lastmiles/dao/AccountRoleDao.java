package cn.lastmiles.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AccountRole;

@Repository
public class AccountRoleDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 存储
	 * @param accountRoles
	 * @return
	 */
	public Integer save(List<AccountRole> accountRoles){
		Integer temp=0;
		for (AccountRole accountRole : accountRoles) {
			temp = temp+this.save(accountRole);
		}
		return temp;
	}
	
	/**
	 * 存储一个
	 * @param accountRole
	 * @return
	 */
	public Integer save(AccountRole accountRole){
		String sql = "insert into t_account_role(accountId,roleId) values(?,?)";
		return jdbcTemplate.update(sql, accountRole.getAccountId(),accountRole.getRoleId());
	}
	
	/**
	 * 存储一个
	 * @param accountRole
	 * @return
	 */
	public Integer save(Long accountId,Long roleId){
		String sql = "insert into t_account_role(accountId,roleId) values(?,?)";
		return jdbcTemplate.update(sql, accountId,roleId);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param roleIds
	 * @return
	 */
	public Integer save (Long accountId,Long [] roleIds){
		Integer temp=0;
		for (Long roleId : roleIds) {
			temp = temp+this.save(accountId,roleId);
		}
		return temp;
	}

	public void deleteByAccountId(Long id) {
		String sql = "delete from t_account_role  where accountId = ?";
		jdbcTemplate.update(sql,id);
	}

}
