package cn.lastmiles.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Log;
import cn.lastmiles.dao.LogDao;

@Service
public class LogService {
	@Autowired
	private LogDao logDao;
	
	public void save(Log log){
		log.setCreatedTime(new Date());
		logDao.save(log);
	}
}
