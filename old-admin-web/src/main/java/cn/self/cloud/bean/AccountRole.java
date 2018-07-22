package cn.self.cloud.bean;

/**
 * 账户和角色对应关系
 * @author zhangpengcheng
 *
 */
public class AccountRole {
	
	private Long accountId;
	
	private Long roleId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	} 

}
