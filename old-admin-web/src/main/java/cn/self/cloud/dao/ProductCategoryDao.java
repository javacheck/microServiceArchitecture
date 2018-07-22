package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.ProductCategory;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 产品类型底层
 * 
 * @author zhangpengcheng
 *
 */

@Repository
public class ProductCategoryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存方法
	 * 
	 * @param productCategory
	 */
	public void save(ProductCategory productCategory) {
		jdbcTemplate
				.update("insert into t_product_category(id,name,parentId,storeId,path) values(?,?,?,?,?)",
						productCategory.getId(), productCategory.getName(),
						productCategory.getParentId(),
						productCategory.getStoreId(), productCategory.getPath());

	}

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
				"select count(1) from t_product_category " + where,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select id,name,parentId ,(SELECT name FROM t_product_category tpc WHERE  t.parentId=tpc.id ) AS parentName from t_product_category t ");
		sql.append(where + " limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(ProductCategory.class, list));

		return page;
	}

	/**
	 * 通过 父类ID 查询
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductCategory> findByParentId(Long parentId) {
		StringBuffer sql = new StringBuffer(
				"select id,name,parentId ,(SELECT name FROM t_product_category tpc WHERE  t.parentId=tpc.id ) AS parentName from t_product_category t where 1 = 1 ");
		List<Map<String, Object>> list = null;
		if (parentId != null) {
			sql.append(" and parentId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), parentId);
		} else {
			sql.append(" and parentId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString());
		}

		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 通过ID 修改数据
	 * 
	 * @param productCategory
	 */
	public void updateByID(ProductCategory productCategory) {
		jdbcTemplate.update(
				"update t_product_category set name = ?  where id = ?",
				productCategory.getName(), productCategory.getId());
	}

	/**
	 * 通过ID查询数据
	 * 
	 * @param id
	 * @return
	 */
	public ProductCategory findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId ,(SELECT name FROM t_product_category tpc WHERE  t.parentId=tpc.id ) AS parentName from t_product_category t where id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}

	public void delete(Long id) {
		jdbcTemplate
				.update("DELETE FROM  t_product_category  where id = ? or path like ? or path like ?",
						id, "%-" + id + "-%", id + "-%");
	}

	/**
	 * 查询表里是否存相同的分类，入参为分类名和店铺
	 * 
	 * @param string
	 * @param accountStoreId
	 * @return
	 */
	public ProductCategory findProductCategoryByName(String string,
			Long accountStoreId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId ,(SELECT name FROM t_product_category tpc WHERE  t.parentId=tpc.id ) AS parentName from t_product_category t where name = ? and storeId=?",
						string, accountStoreId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}

	public List<ProductCategory> findByParentIdAndStoreId(Long parentId,
			Long storeId) {
		StringBuffer sql = new StringBuffer(
				"select t.* ,(SELECT name FROM t_product_category tpc WHERE  t.parentId=tpc.id ) AS parentName from t_product_category t where 1 = 1 and storeId = ?");
		List<Map<String, Object>> list = null;
		if (parentId != null) {
			sql.append(" and parentId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), storeId, parentId);
		} else {
			sql.append(" and parentId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), storeId);
		}

		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 查找父类与其所有子类（递归，包括子类的子类）
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductCategory> findAllChildren(Long parentId) {
		return BeanUtils
				.toList(ProductCategory.class,
						jdbcTemplate
								.queryForList(
										"select id,name,parentId,storeId,path from t_product_category where id = ? or path like ?",
										parentId, parentId + "-%"));
	}
}
