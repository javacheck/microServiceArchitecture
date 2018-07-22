package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;

import cn.self.cloud.bean.Permission;
import cn.self.cloud.dao.PermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
	@Autowired
	private PermissionDao permissionDao;

	/**
	 * 所有需要权限验证的url
	 * 
	 * @return
	 */
	public List<String> findAllUrl() {
		return permissionDao.findAllUrl();
	}

	/**
	 * 查询accountId拥有的权限
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Permission> findByAccountId(Long accountId) {
		return permissionDao.findByAccountId(accountId);
	}

	public List<Permission> findByRoleId(Long roleId) {
		return permissionDao.findByRoleId(roleId);
	}

	public List<Long> findByRoleIdReturnListLong(Long roleId) {
		List<Long> list = new ArrayList<Long>();
		for (Permission permission : permissionDao.findByRoleId(roleId)) {
			list.add(permission.getId());
		}
		return list;
	}

	public void save(Long roleId, Long[] permissionIds) {
		permissionDao.save(roleId, permissionIds);
	}

	public void updateRolePermission(Long roleId, Long[] permissionIds) {
		permissionDao.deleteRolePermission(roleId);
		permissionDao.save(roleId, permissionIds);
	}

	public void deleteRolePermission(Long roleId) {
		permissionDao.deleteRolePermission(roleId);
	}
}
