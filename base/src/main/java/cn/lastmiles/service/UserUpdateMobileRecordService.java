package cn.lastmiles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.dao.UserUpdateMobileRecordDao;
import cn.lastmiles.common.jdbc.Page;

@Service
public class UserUpdateMobileRecordService {
	private static  final Logger logger = LoggerFactory.getLogger(UserUpdateMobileRecordService.class);
	@Autowired
	private UserUpdateMobileRecordDao UserUpdateMobileRecordDao;
	
	public Page findAll(String storeIdString, String oldMobile,
			String newMobile, String accountName, String beginTime,
			String endTime, Page page) {
		
		return UserUpdateMobileRecordDao.findAll(storeIdString,oldMobile,newMobile,accountName,beginTime,endTime,page);
	}
	
}
