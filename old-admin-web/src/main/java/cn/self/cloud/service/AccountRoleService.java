package cn.self.cloud.service;

import java.util.List;
import cn.self.cloud.bean.AccountRole;
import cn.self.cloud.dao.AccountRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountRoleService {
	
	@Autowired
	public AccountRoleDao accountRoleDao;
	
	/**
	 * 
	 * @param accountRoles
	 * @return
	 */
	public Integer save(List<AccountRole> accountRoles){
		return accountRoleDao.save(accountRoles);
	}
	
	/**
	 * 存储一个
	 * @param accountRole
	 * @return
	 */
	public Integer save(AccountRole accountRole){
		return accountRoleDao.save(accountRole);
	}
	
	/**
	 * 存储一个
	 * @param accountId
	 * @return
	 */
	public Integer save(Long accountId,Long roleId){
		return accountRoleDao.save(accountId, roleId);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param roleIds
	 * @return
	 */
	public Integer save (Long accountId,Long [] roleIds){
		return accountRoleDao.save(accountId, roleIds);
	}
	
	@Transactional
	public void update(Long accountId, Long[] roleIds) {
		accountRoleDao.deleteByAccountId(accountId);
		this.save(accountId, roleIds);
	}
	

}
