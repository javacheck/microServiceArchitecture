package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.AllocationConfig;
import cn.lastmiles.dao.AllocationConfigDao;

@Service
public class AllocationConfigService {
	
	@Autowired
	private AllocationConfigDao AllocationConfigDao;
	
	public AllocationConfig getAllocationConfigByStoreId(Long storeId) {
		
		return AllocationConfigDao.getAllocationConfigByStoreId(storeId);
	}
	

	public void saveAllocationConfig(AllocationConfig allocationConfig) {
		if(allocationConfig.getStatus()!=null){
			AllocationConfigDao.save(allocationConfig);
		}
		
	}

	public void updateAllocationConfig(AllocationConfig allocationConfig) {
		if(allocationConfig.getStatus()!=null){
			AllocationConfigDao.update(allocationConfig);
		}else{
			AllocationConfigDao.delAllocationConfig(allocationConfig);
		}
		
	}
}
