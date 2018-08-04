package cn.lastmiles.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AllocationConfig;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class AllocationConfigDao {
	

	public AllocationConfig getAllocationConfigByStoreId(Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		AllocationConfig allocationConfig = JdbcUtils.queryForObject("allocation.getAllocationConfigByStoreId", map, AllocationConfig.class);
		return allocationConfig;
	}

	public void save(AllocationConfig allocationConfig) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", allocationConfig.getStoreId());
		map.put("status", allocationConfig.getStatus());
		JdbcUtils.save("t_allocation_config", map);
		
	}

	public void update(AllocationConfig allocationConfig) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", allocationConfig.getStoreId());
		map.put("status", allocationConfig.getStatus());
		int ret = JdbcUtils.update("allocation.updateAllocationConfig", map);
		System.out.println(ret);
		
	}

	public void delAllocationConfig(AllocationConfig allocationConfig) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", allocationConfig.getStoreId());
		int ret = JdbcUtils.update("allocation.delAllocationConfig", map);
		System.out.println(ret);
	}
}
