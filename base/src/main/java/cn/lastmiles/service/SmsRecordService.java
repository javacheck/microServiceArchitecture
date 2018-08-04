package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.SmsRecord;
import cn.lastmiles.dao.SmsRecordDao;

@Service
public class SmsRecordService {
	@Autowired
	private SmsRecordDao smsRecordDao;
	
	public void save(SmsRecord record){
		smsRecordDao.save(record);
	}
}
