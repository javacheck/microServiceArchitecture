package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductAttributeDao {
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
				"select count(1) from t_product_attribute " + where,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select id,name,productId,(select name from t_product tpc where  t.productId=tpc.id ) AS productName from t_product_attribute t ");
		sql.append(where + " limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(ProductAttribute.class, list));
		
		return page;
	}
	public  void save(ProductAttribute productAttribute) {
		jdbcTemplate.update(
				"insert into t_product_attribute(id,name,productId) values(?,?,?)", productAttribute.getId(),
				productAttribute.getName(),productAttribute.getProductId());
	}	
	public void update(ProductAttribute productAttribute) {
		jdbcTemplate.update("update t_product_attribute set name=? where id=?",
				productAttribute.getName(), productAttribute.getId());
		
	}

	public ProductAttribute findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,name,productId,(select name from t_product tpc where  t.productId=tpc.id ) AS productName from t_product_attribute t  where t.id = ?", id);
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
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,name,productId from t_product_attribute where name = ? and productId=?", productAttribute.getName(),productAttribute.getProductId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductAttribute.class, list.get(0));
	}
	public List<ProductAttribute> productAttributeList(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,name,productId from t_product_attribute where productId=? order by id asc", productId);
		return BeanUtils.toList(ProductAttribute.class, list);
	}
	
}
