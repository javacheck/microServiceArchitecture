package cn.lastmiles.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.StorageIntakeLog;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.StorageIntakeLogDao;

@Service
public class StorageIntakeLogService {
	@Autowired
	private StorageIntakeLogDao storageIntakeLogDao;
	@Autowired
	private IdService idService;
	
	public void posSave(StorageIntakeLog storageIntakeLog){
		storageIntakeLog.setId(idService.getId());
		storageIntakeLog.setCreatedTime(new Date());
		storageIntakeLogDao.posSave(storageIntakeLog);
	}

	public Page posList(Page page, Long storeId) {
		return storageIntakeLogDao.posList(page,storeId);
	}

	public Page apiShopList(Long productStockId, Long storeId, Integer date, String operator, Integer number, Page page) {
		return storageIntakeLogDao.apiShopList(productStockId,storeId,date,operator,number,page);
	}

}
