package cn.lastmiles.dao;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.SmsRecord;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class SmsRecordDao {
	public void save(SmsRecord record){
		JdbcUtils.save(record);
	}
}
