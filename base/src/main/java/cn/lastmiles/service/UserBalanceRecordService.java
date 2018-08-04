package cn.lastmiles.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.UserBalanceRecord;
import cn.lastmiles.dao.UserBalanceRecordDao;

@Service
public class UserBalanceRecordService {
	
	@Autowired
	private UserBalanceRecordDao userBalanceRecordDao;
	
	private final static Logger logger = LoggerFactory
			.getLogger(UserBalanceRecordService.class);
	
	public void save(UserBalanceRecord balanceRecord){
		logger.debug("save balanceRecord is :{}"+balanceRecord);
		userBalanceRecordDao.save(balanceRecord);
	}

	public Double sumPrice(Long userId, Integer type,Long orderId,Date beginTime,Date endTime) {
		return userBalanceRecordDao.sumPrice(userId,type,orderId,beginTime,endTime);
	}
	
}
