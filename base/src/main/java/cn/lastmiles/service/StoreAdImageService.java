package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.StoreAdImage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.StoreAdImageDao;

/**
 * createDate : 2015年8月25日 下午3:54:56 
 */

@Service
public class StoreAdImageService {

	@Autowired
	private StoreAdImageDao storeAdImageDao;

	public Page list(Long storeId, Integer type, Page page) {
		return storeAdImageDao.list(storeId,type,page);
	}

	public boolean delete(Integer type, Long storeId) {
		return storeAdImageDao.delete(type,storeId);
	}

	public StoreAdImage findByStoreId(Long storeId) {
		return storeAdImageDao.findByStoreId(storeId);
	}

	public boolean save(StoreAdImage storeAdImage) {
		return storeAdImageDao.save(storeAdImage);
	}

	public boolean update(StoreAdImage storeAdImage) {
		return storeAdImageDao.update(storeAdImage);
	}	
}