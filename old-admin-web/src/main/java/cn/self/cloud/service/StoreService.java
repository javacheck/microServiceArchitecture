package cn.self.cloud.service;

import java.util.Date;
import java.util.List;

import cn.self.cloud.bean.Store;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.StoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private IdService idService;

	/**
	 * 保存 调用底层保存方法
	 * 
	 * @param store
	 */
	public void save(Store store) {
		store.setId(idService.getId());
		store.setCreatedTime(new Date());
		storeDao.save(store);
	}

	/**
	 * 
	 * @return 参看所有商品分类
	 */
	public Page findAll(String name, Page page, Long accountId) {
		return storeDao.findAll(name, page, accountId);
	}

	/**
	 * 
	 * @return 参看所有商品分类
	 */
	public List<Store> findAll() {
		return storeDao.findAll();
	}

	/**
	 * 通过父类查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Store> findByParentId(Long parentId) {
		return storeDao.findByParentId(parentId);
	}

	/**
	 * 修改方法
	 * 
	 * @param store
	 */
	public void updateByID(Store store) {
		storeDao.updateByID(store);
	}

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Store findById(Long id) {
		return storeDao.findById(id);
	}
	
	public Store findByProduct(Long productId) {
		return storeDao.findByProduct(productId);
	}
	public void deleteById(Long id) {
		storeDao.delete(id);
	}

	/**
	 * 我创建的，或者我属于的商店
	 * 
	 * @param accountId
	 * @param storeId
	 * @return
	 */
	public List<Store> findMyStore(Long accountId, Long storeId) {
		return storeDao.findMyStore(accountId, storeId);
	}

	public List<Store> findByAccount(Long accountId) {
		return storeDao.findByAccount(accountId);
		
	}
}
