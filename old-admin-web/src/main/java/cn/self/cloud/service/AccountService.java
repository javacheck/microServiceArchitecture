package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;

import cn.self.cloud.bean.Account;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.password.PasswordUtils;
import cn.self.cloud.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author zhangpengcheng
 *
 */
@Service
public class AccountService {
	@Autowired
	private AccountDao accountDao;

	@Autowired
	private IdService idService;

	public Account login(String mobile, String password) {
		Account account = accountDao.findByMobile(mobile);
		if (account != null
				&& PasswordUtils.checkPassword(password, account.getPassword())) {
			return account;
		}
		return null;
	}

	public Page list(String mobile, Page page, Long id) {
		return accountDao.list(mobile, page, id);

	}

	public boolean update(Account account) {
		if (account.getParentId() != null) {
			Account parent = accountDao.findById(account.getParentId());
			if (parent != null) {
				account.setPath(parent.getPath() + "-" + account.getId());
			} else {
				account.setParentId(null);
				account.setPath(account.getId().toString());
			}
		} else {
			account.setParentId(null);
			account.setPath(account.getId().toString());
		}
		// 如果密码和数据库不相同则密码加密
		if (!PasswordUtils.checkPassword(this.findById(account.getId())
				.getPassword(), account.getPassword())) {
			account.setPassword(PasswordUtils.encryptPassword(account
					.getPassword()));
		}
		return accountDao.update(account);
	}

	public boolean save(Account account) {
		// 密码转换
		account.setPassword(PasswordUtils.encryptPassword(account.getPassword()));
		account.setId(idService.getId());
		if (account.getParentId() != null) {
			Account parent = accountDao.findById(account.getParentId());
			account.setPath(parent.getPath() + "-" + account.getId());
		} else {
			account.setPath(account.getId().toString());
		}
		return accountDao.save(account);
	}

	public Account findById(Long id) {
		return accountDao.findById(id);
	}

	public List<Account> findAllByParentId(Long id) {
		List<Account> list = new ArrayList<Account>();
		if (accountDao.findById(id) != null) {
			list.add(accountDao.findById(id));
		}
		if (accountDao.findAllByParentId(id) != null) {
			list.addAll(accountDao.findAllByParentId(id));
		}
		return list;
	}

	public Account findByParentId(Long id) {
		return accountDao.findByParentId(id);
	}

	public boolean delete(Long id) {
		return accountDao.delete(id);
	}

	/**
	 * 更新传递的path
	 * 
	 * @param accounts
	 */
	public void UpdatePath(List<Account> accounts) {
		if (null == accounts || accounts.size() <= 0) {
			return;
		}
		for (Account account : accounts) {
			if (null != account) {
				this.update(account);
			}
		}
	}

	public boolean checkMobile(String mobile) {
		return accountDao.findByMobile(mobile) != null ? true : false;
	}

	public void updatePassword(String password, Long accountId) {
		accountDao.updatePassword(password, accountId);
	}
}
