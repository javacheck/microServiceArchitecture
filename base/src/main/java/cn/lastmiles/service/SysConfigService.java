package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.SysConfig;
import cn.lastmiles.dao.SysConfigDao;

@Service
public class SysConfigService {
	@Autowired
	private SysConfigDao sysconfigDao;

	public SysConfig get(String name) {
		return sysconfigDao.get(name);
	}

	public void update(SysConfig sysConfig) {
		sysconfigDao.update(sysConfig);
	}
}
