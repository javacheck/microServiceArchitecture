package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.dao.UserAccountManagerDao;
import cn.lastmiles.dao.UserstoreservicepackageDao;

@Service
public class UserstoreservicepackageService {
	@Autowired
	private UserstoreservicepackageDao userstoreservicepackageDao;
	@Autowired
	private UserAccountManagerDao userAccountManagerDao;
	@Autowired
	private UserAccountManagerService userAccountManagerService;
	
	public List<UserStoreServicePackage> findByUserCardId(Long userCardId){
		return userstoreservicepackageDao.findByUserCardId(userCardId);
	}

	public List<UserStoreServicePackage> findByUserCardId(Long userCardId,Integer remainTimes){
		return userstoreservicepackageDao.findByUserCardId(userCardId,remainTimes);
	}
	
	public UserStoreServicePackage findByStoreId$UserCardId$storeServicePackageId(Long storeId,Long userCardId,Long storeServicePackageId){
		return userstoreservicepackageDao.findByStoreId$UserCardId$storeServicePackageId(storeId,userCardId,storeServicePackageId);
	}

	public void update(Long storeId,Long userCardId,Long storeServicePackageId, Integer times) {
		UserStoreServicePackage ussp = findByStoreId$UserCardId$storeServicePackageId(storeId,userCardId,storeServicePackageId);
		if( null != ussp ){
			times = ussp.getTimes().intValue() + times.intValue();
		}
		userstoreservicepackageDao.update(userCardId,storeServicePackageId,times);
		UserCard uc = userAccountManagerDao.findById(userCardId);
		userAccountManagerService.handlerUserLevel(uc,1);
	}
}
