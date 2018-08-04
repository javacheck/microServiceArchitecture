package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ProductAttributeDao {
	private final static Logger logger = LoggerFactory.getLogger(ProductAttributeDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 查询所有商品属性
	 * 
	 * @return
	 */
	public Page findAll(String storeIdString,String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(name)) {
			and.append(" and t.name like ?");
			parameters.add("%" + name + "%");
		}
		
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and tp.storeId in( " +storeIdString+" ) ");
		}
		
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_product_attribute t,t_product_category tp where t.categoryId = tp.id " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.name,t.categoryId, "
				+ " (select name from t_product_category tpc where  t.categoryId=tpc.id ) AS categoryName, "
				+ "tp.storeId,(select name from t_store s where s.id=tp.storeId) as storeName "
				+ " from t_product_attribute t,t_product_category tp where t.categoryId = tp.id ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(ProductAttribute.class, list));
		
		return page;
	}
	public  void save(ProductAttribute productAttribute) {
		jdbcTemplate.update(
				"insert into t_product_attribute(id,name,categoryId) values(?,?,?)", productAttribute.getId(),
				productAttribute.getName(),productAttribute.getCategoryId());
	}	
	public void update(ProductAttribute productAttribute) {
		jdbcTemplate.update("update t_product_attribute set name=? where id=?",
				productAttribute.getName(), productAttribute.getId());
		
	}

	public ProductAttribute findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.id,t.name,t.categoryId,(select name from t_product_category tpc where  t.categoryId=tpc.id ) AS categoryName from t_product_attribute t  where t.id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttribute.class, list.get(0));
	}

	public void deleteById(ProductAttribute productAttribute) {
		jdbcTemplate.update(
				"delete from t_product_attribute  where id = ?", productAttribute.getId());
	}
	
	public ProductAttribute findProductAttribute(ProductAttribute productAttribute) {
		if(productAttribute.getId()==null){
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,categoryId from t_product_attribute where name = ? and categoryId=?", productAttribute.getName(),productAttribute.getCategoryId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductAttribute.class, list.get(0));
		}else{
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,categoryId from t_product_attribute where id<>? and name = ? and categoryId=?",productAttribute.getId(), productAttribute.getName(),productAttribute.getCategoryId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductAttribute.class, list.get(0));
		}
		
	}
	public List<ProductAttribute> productAttributeList(Long categoryId,Long storeId) {
		List<Map<String, Object>> list=null;
		if(storeId==null){
			
			list = jdbcTemplate.queryForList("select * from t_product_attribute where categoryId=? order by id asc", categoryId);
		}else{
			list = jdbcTemplate.queryForList("select a.id,a.name,a.categoryId from t_product_attribute a,t_product_category c where a.categoryId=c.id and a.categoryId=? and c.storeId=?  order by a.id asc", categoryId,storeId);
		}
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	
	public List<ProductAttribute> productAttributeList(Long storeId) {
		List<Map<String, Object>> list=null;
		if(storeId==null){
			list = jdbcTemplate.queryForList("select * from t_product_attribute order by id asc");
		} else{
			list = jdbcTemplate.queryForList("select a.id,a.name,a.categoryId from t_product_attribute a where a.storeId=?  order by a.id asc",storeId);
		}
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	
	
	public ProductAttribute productAttribute(Long attribute) {
		logger.debug("attribute ============== {}",attribute);
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select a.id,a.name,a.categoryId from t_product_attribute a where a.id=?  order by a.id asc",attribute);
		return BeanUtils.toBean(ProductAttribute.class, list.get(0));
	}
	
	public List<ProductAttribute> productAttributeList1(Long categoryId) {
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_product_attribute where categoryId=? order by id asc", categoryId);
		
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	public void deleteByCategoryId(Long categoryId) {
		jdbcTemplate.update(
				"delete from t_product_attribute  where categoryId = ?",categoryId);
	}
	public List<ProductAttribute> findById1(Long productAttributeId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.id,t.name,t.categoryId,(select name from t_product_category tpc where  t.categoryId=tpc.id ) AS categoryName from t_product_attribute t  where t.id = ?", productAttributeId);
		
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	public void deleteById(Long productAttributeId) {
		jdbcTemplate.update(
				"delete from t_product_attribute  where id = ?",productAttributeId);
		
	}
	public ProductAttribute findProductAttribute(Long categoryId, String name) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,name,categoryId from t_product_attribute where name = ? and categoryId=?",name,categoryId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttribute.class, list.get(0));
	}
	public void saveProductAttribute(ProductAttribute pa) {
		jdbcTemplate.update(
				"insert into t_product_attribute(id,name,categoryId) values(?,?,?)", pa.getId(),
				pa.getName(),pa.getCategoryId());
		
	}
	public List<ProductAttribute> findByCategoryId(Long categoryId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select * from t_product_attribute where  categoryId=?",categoryId);
		
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	public List<ProductStockAttributeValue> findByProductStockAttributeValue(Long productId, Long productStockId) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where productStockId = ? and productId = ? ",productStockId,productId);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}
	public List<ProductAttribute> productAttributeList(String idString) {
		String sql = "select * from t_product_attribute where id in ("+idString+")";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	
	public List<ProductStockAttributeValue> findByProductStockAttributeValue(Long productId) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where productId = ? group by number order by number",productId);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}
	
	public List<ProductStockAttributeValue> findProductStockAttributeValue(Long productId, Long attributeId) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where productAttributeId = ? and productId = ? ",attributeId,productId);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}
	
}
