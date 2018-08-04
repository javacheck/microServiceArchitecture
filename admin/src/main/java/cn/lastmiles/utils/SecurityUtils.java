package cn.lastmiles.utils;

import java.util.List;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Permission;
import cn.lastmiles.bean.Role;
import cn.lastmiles.constant.Constants;

public final class SecurityUtils {
	public static boolean isLogin() {
		return getAccount() != null;
	}

	/**
	 * 是否有权限
	 * 
	 * @param permission
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
	 * 当前登录账号信息
	 * 
	 * @return
	 */
	public static Account getAccount() {
		return (Account) WebUtils
				.getAttributeFromSession(Constants.ACCOUNT_SESSION_KEY);
	}

	/**
	 * 当前登录账号id
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

	/**
	 * 当前登录所属agent
	 * 
	 * @return
	 */
	public static Long getAccountAgentId() {
		return getAccount().getAgentId();
	}
	/**
	 * 是否为管理员
	 * @return
	 */
	public static boolean isAdmin() {
		return getAccountStoreId() == null && getAccountAgentId() == null;
	}
	
	/**
	 * 是否为商家
	 * @return
	 */
	public static boolean isStore() {
		return getAccountStoreId() != null;
	}
	
	/**
	 * 是否普通商家
	 * @return
	 */
	public static boolean isNormalStore() {
		return isStore() && !isMainStore();
	}
	
	/**
	 * 是否为代理商
	 * @return
	 */
	public static boolean isAgent() {
		return getAccountAgentId() != null;
	}
	/**
	 * 是否为总店
	 * @return
	 */
	public static boolean isMainStore() {
		Account account =getAccount();
		return account.getStore()!=null&&account.getStore().getIsMain()!=null&&account.getStore().getIsMain().intValue()==Constants.Store.STORE_MAINSHOP;
	}
	
	public static String getStoreName(){
		Account account =getAccount();
		return account.getStore().getName();
	}
	
	/**
	 * 是否为超级管理员
	 * @return
	 */
	public static boolean isSuperAccount() {
		return getAccountId().equals(Constants.Account.SUPER_ACCOUNT_ID) ;
	}
	
	/**
	 * 是否是连锁店
	 * @return
	 */
	public static boolean isChainStore(){
		return getAccount().getStore().getOrganizationId() != null;
	}
}