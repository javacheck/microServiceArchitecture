package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Account;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.AccountDao;

/**
 * 
 * @author zhangpengcheng
 *
 */
@Service
public class AccountService {
	private final static Logger logger = LoggerFactory
			.getLogger(AccountService.class);
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private StoreService storeService;

	@Autowired
	private IdService idService;
	

	public Account posLogin(String mobile, String password){
		Account account = accountDao.findByMobile(mobile,Constants.Account.ACCOUNT_TYPE_STORE);
		if (account != null
				&& PasswordUtils.checkPassword(password, account.getPassword())) {
			return account;
		}
		return null;
	}
	
	public Account login(String mobile, String password) {
		Account account = accountDao.findByMobile(mobile);
		logger.debug("login account = {} password is {}",account,password);
		if (account != null
				&& PasswordUtils.checkPassword(password, account.getPassword())) {
			return account;
		}
		return null;
	}
	
	public Account login(String mobile,int type) {
		Account account = accountDao.findByMobile(mobile,type);
		if (type==Constants.Account.ACCOUNT_TYPE_STORE&&account!=null&&account.getStoreId()!=null) {
			account.setStore(storeService.findById(account.getStoreId()));
		}
		return account;
	}
	
	public Account findByMobile(String mobile) {
		return accountDao.findByMobile(mobile);
	}

	@SuppressWarnings("unchecked")
	public Page list(String beginTime,String endTime,String name,Integer sex,String mobile, Integer type, Long agentId, Long storeId, Page page, Long id) {
		page = accountDao.list(beginTime,endTime,name,sex,mobile,type,agentId,storeId, page, id);
		page.setData(this.addStore((List<Account>) page.getData()));
		return page;

	}
	
	public Account addStore(Account account){
		return account!=null?account.setStore(storeService.findById(account.getStoreId())):account;
	}
	
	public List<Account> addStore(List<Account> accounts){
		List<Account> list = new ArrayList<Account>();
		if (accounts!=null&&accounts.size()>=1) {
			for (Account account : accounts) {
				list.add(this.addStore(account));
			}
		}
		return list;
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
		if (!account.getPassword().equals(this.findById(account.getId()).getPassword())) {
			account.setPassword(PasswordUtils.encryptPassword(account.getPassword()));
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
	
	public boolean checkMobile(String mobile,Integer type) {
		return accountDao.findByMobile(mobile,type) != null ? true : false;
	}

	public void updatePassword(String password, Long accountId) {
		accountDao.updatePassword(password, accountId);
	}

	public List<Account> findByStoreId(Long id) {
		return accountDao.findByStoreId(id);
	}

	public List<Map<String, Object>> apiShopfindByStoreId(Long storeId) {
		return accountDao.apiShopfindByStoreId(storeId);
	}
	public void updateMobileByMobile(String mobile, String mobile2) {
		accountDao.updateMobileByMobile(mobile,mobile2);
		
	}
	public void updateMobileByMobile(String mobile, String mobile2,Integer type ) {
		accountDao.updateMobileByMobile(mobile, mobile2, type);
	}

	public void updateCid(Long id, String cid) {
		accountDao.updateCid(id,cid);
	}

	public void updatePasswordByMobile(String storeAcountName,
			String storeAcountPassWord, int accountTypeStore) {
		accountDao.updatePasswordByMobile(storeAcountName,storeAcountPassWord,accountTypeStore);
	}

	public Account findByStoreIdAndTypeAndMobile(Long storeId, int accountTypeStore, String storeAcountName) {
		return accountDao.findByStoreIdAndTypeAndMobile(storeId, accountTypeStore, storeAcountName);
	}
}
