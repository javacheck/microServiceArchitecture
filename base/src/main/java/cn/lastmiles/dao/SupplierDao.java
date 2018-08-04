package cn.lastmiles.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Supplier;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class SupplierDao {

	private final static Logger logger = LoggerFactory.getLogger(SupplierDao.class);
	public Page findAll(Long storeId, String name,String phone, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("name", "%"+name+"%");
		map.put("phone", phone);
		Integer total = JdbcUtils.queryForObject("supplier.findTotal", map, Integer.class);
		logger.debug("total={}",total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",page.getStart(),page.getPageSize());
		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("supplier.findAllPage", map, Supplier.class));
		
		return page;
	}
	public Supplier findSupplier(Supplier supplier) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", supplier.getId());
		map.put("name", supplier.getName());
		map.put("storeId", supplier.getStoreId());
		supplier = JdbcUtils.queryForObject("supplier.findSupplier", map, Supplier.class);
		return supplier;
	}
	public void saveSupplier(Supplier supplier) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", supplier.getId());
		map.put("name",supplier.getName());
		map.put("storeId", supplier.getStoreId());
		map.put("contacts", supplier.getContacts());
		map.put("phone", supplier.getPhone());
		map.put("address", supplier.getAddress());
		map.put("memo", supplier.getMemo());
		map.put("createdTime", new Date());
		JdbcUtils.save("t_supplier", map);
		
	}
	public void updateSupplier(Supplier supplier) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", supplier.getId());
		map.put("name",supplier.getName());
		map.put("storeId", supplier.getStoreId());
		map.put("contacts", supplier.getContacts());
		map.put("phone", supplier.getPhone());
		map.put("address", supplier.getAddress());
		map.put("memo", supplier.getMemo());
		map.put("createdTime", new Date());
		JdbcUtils.update("supplier.updateSupplier", map);
		
	}
	public Supplier findById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Supplier supplier = JdbcUtils.queryForObject("supplier.findById", map, Supplier.class);
		return supplier;
	}
	public void deleteById(Long id) {
		JdbcUtils.deleteById( Supplier.class,id);
	}

}
