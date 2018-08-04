package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ProductAttributeValueDao {
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
			and.append(" and value like ?");
			parameters.add("%" + name + "%");
		}
		
		if( StringUtils.isNotBlank(storeIdString) ){
			and.append(" and tp.storeId in ("+storeIdString+")");
		}
		
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_product_attribute_value t,t_product_attribute a,t_product_category tp" +
				" where t.attributeId = a.id " +
				" and a.categoryId = tp.id " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.value,t.attributeId,"
				+ "(select name from t_product_attribute pa where pa.id=t.attributeId) as productAttributeName,"
				+ "(select name from t_product_category where id=(select categoryId from t_product_attribute pa where pa.id=t.attributeId)) as categoryName, "
				+ "tp.storeId,(select name from t_store s where s.id=tp.storeId) as storeName "
				+ " from t_product_attribute_value t,t_product_attribute a,t_product_category tp " +
				" where t.attributeId = a.id " + 
				" and a.categoryId = tp.id ");
		sql.append(and.toString() + " order by id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(ProductAttributeValue.class, list));
		
		return page;
	}
	
	/**
	 * 通过 父类ID 查询
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductAttribute> findByCategoryId(Long categoryId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,name,categoryId from t_product_attribute where categoryId=? order by id desc", categoryId);
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	
	public  void save(ProductAttributeValue productAttributeValue) {
		jdbcTemplate.update(
				"insert into t_product_attribute_value(id,value,attributeId) values(?,?,?)", productAttributeValue.getId(),
				productAttributeValue.getValue(),productAttributeValue.getAttributeId());
	}	
	
	public void update(ProductAttributeValue productAttributeValue) {
		jdbcTemplate.update("update t_product_attribute_value set value=? where id=?",
				productAttributeValue.getValue(),productAttributeValue.getId());
		
	}

	public ProductAttributeValue findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select id,value,attributeId,(select name from t_product_attribute pa where pa.id=t.attributeId) as productAttributeName,(select name from t_product_category where id=(select categoryId from t_product_attribute pa where pa.id=t.attributeId)) as categoryName from t_product_attribute_value t where t.id = ? order by t.attributeId asc", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
	}
	public ProductAttributeValue findById1(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select value from t_product_attribute_value   where id = ? order by attributeId asc", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
	}
	public List<ProductAttributeValue> findById2(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select id,value,attributeId,(select name from t_product_attribute pa where pa.id=t.attributeId) as productAttributeName,(select name from t_product_category where id=(select categoryId from t_product_attribute pa where pa.id=t.attributeId)) as categoryName from t_product_attribute_value t where t.id = ? order by t.attributeId asc", id);
		
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}
	public void deleteById(ProductAttributeValue productAttributeValue) {
		jdbcTemplate.update(
				"delete from t_product_attribute_value  where id = ?", productAttributeValue.getId());
	}
	
	public ProductAttributeValue findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		if(productAttributeValue.getId()==null){
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
							"select id,value,attributeId from t_product_attribute_value where value = ? and attributeId=?", productAttributeValue.getValue(),productAttributeValue.getAttributeId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
		}else{
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
							"select id,value,attributeId from t_product_attribute_value where id<>? and value = ? and attributeId=?", productAttributeValue.getId(),productAttributeValue.getValue(),productAttributeValue.getAttributeId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
		}
	}

	public Integer findByAttributeId(Long attributeId) {
		String sql="select count(1) from t_product_attribute_value where attributeId=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, attributeId);
	}
	
	public List<ProductAttributeValue> findByAttributeId1(Long attributeId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select * from t_product_attribute_value where attributeId = ?", attributeId);
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}
	
	public List<ProductAttributeValue> findByAttributeId(ProductAttributeValue productAttributeValue) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,value,attributeId from t_product_attribute_value where attributeId = ?", productAttributeValue.getAttributeId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}
	
	public List<ProductAttributeValue> findByAttributeIdList(Long  attributeId) {
		List<Map<String, Object>> list=jdbcTemplate.queryForList(
				"select * from t_product_attribute_value where attributeId = ?", attributeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}
	
	public List<ProductAttributeValue> productAttributeValueList(
			Long productAttributeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,value,attributeId from t_product_attribute_value where attributeId=? order by id desc", productAttributeId);
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}

	public List<ProductAttributeValue> findAllBrothersById(Long id) {
		String sql = "SELECT	* FROM 	t_product_attribute_value WHERE 	attributeId ="
				+ "( SELECT		attributeId 	FROM 		t_product_attribute_value 	WHERE 		id = ? )";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}

	public int deleteAttributeId(Long attributeId) {
		return jdbcTemplate.update(
				"delete from t_product_attribute_value  where attributeId = ?", attributeId);
	}

	public void deleteById(Long productAttributeValueId) {
		jdbcTemplate.update(
				"delete from t_product_attribute_value  where id = ?",productAttributeValueId);
		
	}
}
