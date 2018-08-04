package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ProductUnitDao {

	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	private static final Logger logger = LoggerFactory.getLogger(ProductUnitDao.class);
	
	public Page findAllPage(String storeIdString, String name, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> storeIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(storeIdString)){
			String[] arrStoreId=storeIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				storeIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("storeId", storeIds);
		}else{
			map.put("storeId", null);
		}
		if (StringUtils.isNotBlank(name)) {
			map.put("name", "%"+name+"%");
		}else{
			map.put("name", "");
		}
		Integer total = JdbcUtils.queryForObject("productUnit.findTotal", map, Integer.class);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		map.put("startNumber",page.getStart());
		map.put("pageSize",page.getPageSize());
		
		page.setData(JdbcUtils.queryForList("productUnit.findAllPage", map, ProductUnit.class));
		
		return page;
	}

	public ProductUnit findProductUnitExist(ProductUnit productUnit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", productUnit.getId());
		map.put("name", productUnit.getName());
		map.put("storeId", productUnit.getStoreId());
		ProductUnit pu = JdbcUtils.queryForObject("productUnit.findProductUnitExist",map, ProductUnit.class);
		return pu;
	}
	public ProductUnit findProductUnitById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		ProductUnit pu = JdbcUtils.queryForObject("productUnit.findProductUnitById", map, ProductUnit.class);
		return pu;
	}
	public void save(ProductUnit productUnit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", productUnit.getId());
		map.put("name", productUnit.getName());
		map.put("storeId", productUnit.getStoreId());
		JdbcUtils.save("t_product_unit", map);
	}

	public void update(ProductUnit productUnit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", productUnit.getId());
		map.put("name", productUnit.getName());
		map.put("storeId", productUnit.getStoreId());
		JdbcUtils.update("productUnit.updateProductUnit", map);
	}

	public void deleteById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		JdbcUtils.deleteById(ProductUnit.class, id);
	}

	public Integer findStockCountByUnitId(Long productUnitId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unitId", productUnitId);
		return JdbcUtils.queryForObject("productUnit.findStockTotal", map, Integer.class);
		
	}

	public List<ProductUnit> getProductUnitListByStoreID(Long storeId) {
		logger.debug("获取商品单位时，传入的商家ID是:{}",storeId);
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_unit where storeId = ? ",storeId);
		return BeanUtils.toList(ProductUnit.class, list);
	}

	public void batchSave(List<Object[]> addUnit) {
		StringBuilder insertSQL = new StringBuilder(50);
		
		insertSQL.append("insert into t_product_unit(id,name,storeId)");
		insertSQL.append(" values(?,?,?)");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),addUnit) ;
		logger.debug("批量新增单位信息{}个",result);
	}
		
}