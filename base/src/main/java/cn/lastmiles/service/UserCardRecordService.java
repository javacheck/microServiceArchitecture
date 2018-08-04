package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * createDate : 2016年3月17日上午10:32:26
 */
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.UserCardRecordDao;

@Service
public class UserCardRecordService {
	@Autowired
	private UserCardRecordDao userCardRecordDao;
	
	public void updateStatusSuccess(Long id ){
		userCardRecordDao.updateStatusSuccess(id);;
	}

	public void updateServiceStatusSuccess(Long id ){
		userCardRecordDao.updateServiceStatusSuccess(id);;
	}
	
	public void save(UserCardRecord userCardRecord) {
		userCardRecordDao.save(userCardRecord);
	}

	public Page appList(String userCardId, String startTime, String endTime,
			Integer type, Page page) {

		return userCardRecordDao.appList(userCardId, startTime, endTime, type,
				page);
	}

	public UserCardRecord findByOrderId(Long orderId) {
		return userCardRecordDao.findByOrderId(orderId);
	}

	/**
	 * 最近充值的记录
	 * @param storeId
	 * @param mobile
	 * @return
	 */
	public UserCardRecord findLastRechargeRecordForPos(Long storeId , String mobile){
		return userCardRecordDao.findLastRechargeRecordForPos(storeId, mobile);
	}

	public UserCardRecord findById(Long recordId) {
		return userCardRecordDao.findById(recordId);
	}

	public List<UserCardRecord> findByUserCardId(Long userCardId) {
		return userCardRecordDao.findByUserCardId(userCardId);
	}
}
