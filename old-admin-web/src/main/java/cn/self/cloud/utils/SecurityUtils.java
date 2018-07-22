package cn.self.cloud.utils;

import cn.self.cloud.bean.Account;
import cn.self.cloud.bean.Permission;
import cn.self.cloud.bean.Role;
import cn.self.cloud.constant.Constants;

import java.util.List;

public final class SecurityUtils {
	public static boolean isLogin() {
		return getAccount() != null;
	}

	/**
	 * 是否有权限
	 * 
	 * @param defaultPermission
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasPermission(
			Permission.DefaultPermission defaultPermission) {
		List<Permission> permissons = (List<Permission>) WebUtils
				.getAttributeFromSession(Constants.PERMISSION_SESSION_KEY);
		for (Permission pm : permissons) {
			if (pm.getOperator().equals(defaultPermission.getOperator())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<Permission> getPermissionList() {
		return (List<Permission>) WebUtils
				.getAttributeFromSession(Constants.PERMISSION_SESSION_KEY);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasPermission(String permission) {
		List<Permission> permissons = (List<Permission>) WebUtils
				.getAttributeFromSession(Constants.PERMISSION_SESSION_KEY);
		for (Permission pm : permissons) {
			if (pm.getOperator().equals(permission)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean hasAnyPermission(String... ps) {
		List<Permission> permissons = (List<Permission>) WebUtils
				.getAttributeFromSession(Constants.PERMISSION_SESSION_KEY);
		for (String p : ps) {
			for (Permission pm : permissons) {
				if (pm.getOperator().equals(p)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否是某个默认角色
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isRole(Role.DefaultRole defaultRole) {
		List<Role> list = (List<Role>) WebUtils
				.getAttributeFromSession(Constants.ROLE_SESSION_KEY);
		for (Role role : list) {
			if (role.getValue().equals(defaultRole.getValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当前登录帐号信息
	 * 
	 * @return
	 */
	public static Account getAccount() {
		return (Account) WebUtils
				.getAttributeFromSession(Constants.ACCOUNT_SESSION_KEY);
	}

	/**
	 * 当前登录帐号id
	 * 
	 * @return
	 */
	public static Long getAccountId() {
		return getAccount().getId();
	}

	/**
	 * 当前登录所属store
	 * 
	 * @return
	 */
	public static Long getAccountStoreId() {
		return getAccount().getStoreId();
	}
}
