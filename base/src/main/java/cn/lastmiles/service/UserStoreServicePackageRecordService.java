package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.UserStoreServicePackageRecord;
import cn.lastmiles.dao.UserStoreServicePackageRecordDao;

@Service
public class UserStoreServicePackageRecordService {
	@Autowired
	private UserStoreServicePackageRecordDao userStoreServicePackageRecordDao;
	
	/**
	 * 最近充值的记录
	 * @param storeId
	 * @param mobile
	 * @return
	 */
	public UserStoreServicePackageRecord findLastRechargeRecordForPos(Long storeId , String mobile){
		return userStoreServicePackageRecordDao.findLastRechargeRecordForPos(storeId, mobile);
	}

	public UserStoreServicePackageRecord findById(Long recordId) {
		return userStoreServicePackageRecordDao.findById(recordId);
	}
}
