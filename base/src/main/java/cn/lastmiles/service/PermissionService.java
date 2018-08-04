package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Permission;
import cn.lastmiles.dao.PermissionDao;

@Service
public class PermissionService {
	
	private final static Logger logger = LoggerFactory
			.getLogger(PermissionService.class);
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
		if (permissionIds!=null&&permissionIds.length>=1) {
			permissionDao.save(roleId, permissionIds);
		}
	}

	public void deleteRolePermission(Long roleId) {
		permissionDao.deleteRolePermission(roleId);
	}
	
	/**
	 * 查询accountId拥有的权限 并分类
	 * 
	 * @param accountId
	 * @return
	 */
	public Map<String, List<Permission>> findByAccountIdGroup(Long accountId) {
		Map<String, List<Permission>> map = new HashMap<String, List<Permission>>();
		List<String> categoryNames= permissionDao.findAllCategoryName();
		logger.debug("findByAccountIdGroup categoryNames is :"+map);
		if (categoryNames!=null&&categoryNames.size()>=1) {
			for (String categoryName : categoryNames) {
				List<Permission> permissions=permissionDao.findByCategoryNameAndAccountId(categoryName, accountId);
				if (permissions!=null&&permissions.size()>=1) {
					map.put(categoryName, permissions);
				}
			}
		}
		logger.debug("findByAccountIdGroup  map is :"+map);
		return map;
	}
	/**
	 * 查询所有权限 并分类
	 * 
	 * @param accountId
	 * @return
	 */
	public Map<String, List<Permission>> findByALLGroup(Long accountId) {
		Map<String, List<Permission>> map = new HashMap<String, List<Permission>>();
		List<String> categoryNames= permissionDao.findAllCategoryName();
		logger.debug("findByAccountIdGroup categoryNames is :"+categoryNames);
		if (categoryNames!=null&&categoryNames.size()>=1) {
			for (String categoryName : categoryNames) {
				List<Permission> permissions=permissionDao.findByCategoryName(categoryName);
				if (permissions!=null&&permissions.size()>=1) {
					map.put(categoryName, permissions);
				}
			}
		}
		logger.debug("findByAccountIdGroup  map is :"+map);
		return map;
	}
}
