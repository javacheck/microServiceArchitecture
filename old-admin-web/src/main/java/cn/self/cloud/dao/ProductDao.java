package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Product;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询所有商品分类
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
				"select count(1) from t_product " + where, Integer.class,
				parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select id,name,categoryId,accountId,(SELECT name FROM t_product_category tpc WHERE  t.categoryId=tpc.id ) AS categoryName from t_product t ");
		sql.append(where + " limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Product.class, list));

		return page;
	}

	public void save(Product product) {
		jdbcTemplate
				.update("insert into t_product(id,name,categoryId,accountId) values(?,?,?,?)",
						product.getId(), product.getName(),
						product.getCategoryId(), product.getAccountId());
	}

	public void update(Product product) {
		jdbcTemplate.update(
				"update t_product set name=?,categoryId=? where id=?",
				product.getName(), product.getCategoryId(), product.getId());

	}

	public Product findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where id = ?",
						id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	public void deleteById(Product product) {
		jdbcTemplate.update("delete from t_product  where id = ?",
				product.getId());
	}

	public List<Product> findByAccountId(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where accountId = ?",
						product.getAccountId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(Product.class, list);
	}

	public Product findProduct(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where name = ? and categoryId=? and accountId=?",
						product.getName(), product.getCategoryId(),
						product.getAccountId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	/**
	 * 根据category统计数量
	 * @param categoryId
	 * @return
	 */
	public Long countByCategory(Long categoryId) {
		return jdbcTemplate.queryForObject(
				"select count(1) from t_product where categoryId = ?",
				Long.class, categoryId);
	}

	public List<Product> productList(Long categoryId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where categoryId=? order by id desc",
						categoryId);
		return BeanUtils.toList(Product.class, list);
	}
}
