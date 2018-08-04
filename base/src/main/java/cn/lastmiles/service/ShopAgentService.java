package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Account;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.dao.ShopAgentDao;

@Service
public class ShopAgentService {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private ShopAgentDao shopAgentDao;
	public Page list(String mobile, Page page) {
		
		return shopAgentDao.list(mobile,page);
	}
	public void updateAccountPwd(Account account) {
		String password=null;
		// 如果密码和数据库不相同则密码加密
		if (!account.getPassword().equals(accountService.findById(account.getId()).getPassword())) {
			password=PasswordUtils.encryptPassword(account.getPassword());
		}
		shopAgentDao.updateAccountPwd(account.getId(),password);
		
	}

}
