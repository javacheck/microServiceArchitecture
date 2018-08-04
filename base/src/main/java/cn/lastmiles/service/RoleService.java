package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Role;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.RoleDao;

@Service
public class RoleService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private IdService idService;

	public void save(Role role) {
		role.setId(idService.getId());
		role.setValue(-1);
		role.setCreatedId(role.getCreatedId());
		role.setCreateTime(new Date());
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
	 * 查找自己可以控制角色
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Role> findMyRoles(Long accountId) {
		List<Role> roles = new ArrayList<Role>();
		roles.addAll(this.findByAccountId(accountId));//自己拥有角色
		roles.addAll(this.findByCreatedId(accountId));//自己创建角色
		return roles;
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
		if (!role.getValue().equals(-1)) {//系统默认角色
			list.addAll(roleDao.findKidsByValue(role.getValue()));//添加系统角色下所有角色
		}else{//用户创建角色
			list.add(role);//添加当前系统角色
		}
		//删除重复数据
		List<Role> listReturn = new ArrayList<Role>();
		Set<Long> sets = new HashSet<Long>();
		for (Role role2 : list) {
			if (!sets.contains(role2.getId())) {
				listReturn.add(role2);
			}
			sets.add(role2.getId());
		}
		Collections.sort(listReturn, new Comparator<Role>() {//排序
			@Override
			public int compare(Role r1, Role r2) {
				return (int) (r1.getId() - r2.getId());
			}
		});
		return listReturn;
	}
	
	public List<Role> findKidsByIdAndValue(Integer value){
		return roleDao.findKidsByValue(value);
	}
	
	public List<Account> findAccountByRoleId(Long roleId){
		return roleDao.findAccountByRoleId(roleId);
	}

	public Role findbyName(String roleName, Integer type, Long ownerId) {
		return roleDao.findbyName(roleName,type,ownerId);
	}

	public List<Role> findAll() {
		return roleDao.findAll();
	}
}
