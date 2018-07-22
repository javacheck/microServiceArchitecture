package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;

import cn.self.cloud.bean.Account;
import cn.self.cloud.bean.Role;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.dao.RoleDao;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private IdService idService;

	public void save(Role role) {
		role.setId(idService.getId());
		role.setValue(-1);
		role.setCreatedId(SecurityUtils.getAccountId());
		roleDao.save(role);
	}

	/**
	 * 查询自己创建的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Role> findByCreatedId(Long accountId) {
		return roleDao.findByCreatedId(accountId);
	}

	/**
	 * 查找拥有的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Role> findByAccountId(Long accountId) {
		return roleDao.findByAccountId(accountId);
	}
	
	/**
	 * 查找拥有的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Long> findByAccountIdReturn(Long accountId) {
		List<Long> roleIds = new ArrayList<Long>();
		for (Role role : roleDao.findByAccountId(accountId)) {
			roleIds.add(role.getId());
		}
		return roleIds;
	}

	/**
	 * 查找拥有的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public Role findMaxByAccountId(Long accountId) {
		return roleDao.findMaxByAccountId(accountId);
	}

	/**
	 * 修改角色名称
	 * 
	 * @param role
	 */
	public void updateName(Role role) {
		roleDao.updateName(role);
	}

	public Role findById(Long id) {
		return roleDao.findById(id);
	}

	public void delete(Long id) {
		roleDao.delete(id);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<Role> findKidsByAccountId(Long id) {
		List<Role> list = new ArrayList<Role>();
		list.addAll(this.findByCreatedId(id));//添加当前用户创建的角色
		Role role = this.findMaxByAccountId(id);//查找当前用户创建用户的最高角色
		if (role.getValue() != -1) {//系统默认角色
			list.addAll(roleDao.findKidsByIdAndValue(role.getValue()));//添加系统角色下所有角色
		}else{//用户创建角色
			list.add(role);//添加当前系统角色
		}
		return list;
	}
	
	public List<Role> findKidsByIdAndValue(Integer value){
		return roleDao.findKidsByIdAndValue(value);
	}
	
	public List<Account> findAccountByRoleId(Long roleId){
		return roleDao.findAccountByRoleId(roleId);
	}

	public Role findbyName(String roleName) {
		return roleDao.findbyName(roleName);
	}
}
