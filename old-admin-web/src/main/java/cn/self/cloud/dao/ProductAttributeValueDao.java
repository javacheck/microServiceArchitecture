package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.bean.ProductAttributeValue;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductAttributeValueDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 查询所有商品属性
	 * 
	 * @return
	 */
	public Page findAll(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		String where = "";
		if (StringUtils.isNotBlank(name)) {
			where = " where name like ?";
			parameters.add("%" + name + "%");
		}

		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_product_attribute_value " + where,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select id,value,attributeId,(select name from t_product_attribute pa where pa.id=t.attributeId) as productAttributeName,(select name from t_product where id=(select productId from t_product_attribute pa where pa.id=t.attributeId)) as productName from t_product_attribute_value t ");
		sql.append(where + " limit ?,?");
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
	 * @param productId
	 * @return
	 */
	public List<ProductAttribute> findByProductId(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,name,productId from t_product_attribute where productId=? order by id desc", productId);
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
						"select id,value,attributeId,(select name from t_product_attribute pa where pa.id=t.attributeId) as productAttributeName,(select name from t_product where id=(select productId from t_product_attribute pa where pa.id=t.attributeId)) as productName from t_product_attribute_value t where t.id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
	}

	public void deleteById(ProductAttributeValue productAttributeValue) {
		jdbcTemplate.update(
				"delete from t_product_attribute_value  where id = ?", productAttributeValue.getId());
	}
	
	public ProductAttributeValue findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,value,attributeId from t_product_attribute_value where value = ? and attributeId=?", productAttributeValue.getValue(),productAttributeValue.getAttributeId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
	}

	public Integer findByAttributeId(Long attributeId) {
		String sql="select count(1) from t_product_attribute_value where attributeId=?";
		return jdbcTemplate.queryForObject(sql, Integer.class, attributeId);
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
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
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
}
