package cn.self.cloud.service;

import java.util.Date;

import cn.self.cloud.bean.User;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private IdService idService;

	public Page list(String mobile, Long storeId, Page page) {
		return userDao.list(mobile,storeId, page);
	}

	public User findByMobile(String mobile) {
		return userDao.findByMobile(mobile);
	}
	
	public User findByMobileAndStoreId(String mobile,Long storeId) {
		return userDao.findByMobileAndStoreId(mobile,storeId);
	}

	public boolean save(User user) {
		user.setId(idService.getId());
		user.setCreatedTime(new Date());
		return userDao.save(user);
	}

	public boolean updateDiscount(Long storeId, Double discount) {
		return userDao.updateDiscount(storeId, discount);
	}

	public boolean update(User user) {
		return userDao.update(user);

	}

	public User findById(Long id) {
		return userDao.findById(id);
	}

	public boolean delete(Long id) {
		return userDao.delete(id);
	}
}