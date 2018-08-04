package cn.lastmiles.service;
/**
 * createDate : 2016年3月10日下午3:27:55
 */
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.StoreServicePackage;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.StoreServicePackageDao;

@Service
public class StoreServicePackageService {
	@Autowired
	private StoreServicePackageDao storeServicePackageDao;
	@Autowired
	private IdService idService;
	
	public void save(StoreServicePackage storeServicePackage){
		if( null == storeServicePackage.getId() ){
			storeServicePackage.setId(idService.getId());
		}
		if( null == storeServicePackage.getCreatedTime() ){
			storeServicePackage.setCreatedTime(new Date());
		}
		storeServicePackageDao.save(storeServicePackage);
	}

	public int checkServicePackageName(Long storeId, String name) {
		return storeServicePackageDao.checkServicePackageName(storeId,name);
	}

	public List<StoreServicePackage> getServicePackageSelect(Long storeId) {
		return storeServicePackageDao.getServicePackageSelect(storeId);
	}
	public StoreServicePackage findById(Long id) {
		return storeServicePackageDao.findById(id);
	}

	public List<StoreServicePackage> findByStoreId(Long storeId) {
		return storeServicePackageDao.findByStoreId(storeId);
	}

}