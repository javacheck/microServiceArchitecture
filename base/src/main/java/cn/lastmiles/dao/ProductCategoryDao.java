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
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

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

	private static final Logger logger = LoggerFactory
			.getLogger(ProductCategoryDao.class);

	/**
	 * 检测名称
	 * 
	 * @param name
	 * @param type
	 * @return true表示存在
	 */
	public boolean checkName(String name, Long id, Integer type, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select count(1) from t_product_category where 1 =1 and name = ?";
		parameters.add(name);
		if (id != null) {
			sql += " and id <> ? ";
			parameters.add(id);
		}
		if (type.equals(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM)) {// 自定义分类
			sql += " and (storeId = ?  or type = ? )";
			parameters.add(storeId);// 扫描当前商店
			parameters.add(Constants.ProductCategory.PRODUCT_CATEGORY_SYSTEM);// 扫描系统分类
		}
		if (type.equals(Constants.ProductCategory.PRODUCT_CATEGORY_SYSTEM)) {// 系统分类
			sql += " and type = ? ";
			parameters.add(Constants.ProductCategory.PRODUCT_CATEGORY_SYSTEM);// 扫描系统分类
		}

		logger.debug("checkName sql  is : {}", sql, parameters);
		return jdbcTemplate.queryForObject(sql, Integer.class,
				parameters.toArray()) > 0;
	}
	
	/**
	 * 保存方法
	 * 
	 * @param productCategory
	 */
	public void save(ProductCategory productCategory) {
		jdbcTemplate
				.update("insert into t_product_category(id,name,pId,storeId,path,type,sort) values(?,?,?,?,?,?,?)",
						productCategory.getId(), productCategory.getName(),
						productCategory.getpId(), productCategory.getStoreId(),
						productCategory.getPath(), productCategory.getType(),productCategory.getSort());

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
				"select id,name,pId ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName "
						+ ",(SELECT name FROM t_store s WHERE  s.id=t.storeId ) AS 'storeName'"
						+ "from t_product_category t ");
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
	 * @param pId
	 * @return
	 */
	public List<ProductCategory> findBypId(Long pId, String storeIds) {
		StringBuffer sql = new StringBuffer(
				"select id,name,pId ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName "
						+ ",(SELECT name FROM t_store s WHERE  s.id=t.storeId ) AS 'storeName',t.storeId"
						+ " from t_product_category t" + " where 1 = 1");
		if (null != storeIds && !"".equals(storeIds)) {
			sql.append(" and t.storeId in (" + storeIds + ")");
		}
		List<Map<String, Object>> list = null;
		if (pId != null) {
			sql.append(" and pId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), pId);
		} else {
			sql.append(" and pId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString());
		}

		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 通过ID 修改数据
	 * 
	 * @param productCategory
	 */
	public void updateById(ProductCategory productCategory) {
		jdbcTemplate.update(
				"update t_product_category set name = ? ,sort = ? where id = ?",
				productCategory.getName(),productCategory.getSort(), productCategory.getId());
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
						"select id,name,path,pId ,sort ,type,storeId,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t where id = ?",
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
						"select id,name,path,pId ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t where name = ? and storeId=?",
						string, accountStoreId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}

	public List<ProductCategory> findBypIdAndStoreId(Long pId, Long storeId) {
		StringBuffer sql = new StringBuffer(
				"select t.* ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t where 1 = 1 and storeId = ?");
		List<Map<String, Object>> list = null;
		if (pId != null) {
			sql.append(" and pId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), storeId, pId);
		} else {
			sql.append(" and pId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), storeId);
		}

		return BeanUtils.toList(ProductCategory.class, list);
	}

	public List<ProductCategory> findBypIdAndStoreId(Long pId, String storeIds) {
		StringBuffer sql = new StringBuffer(
				"select t.* ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t where 1 = 1 ");
		if (null != storeIds && !"".equals(storeIds)) {
			sql.append("and storeId in (" + storeIds + ")");
		}
		List<Map<String, Object>> list = null;
		if (pId != null) {
			sql.append(" and pId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), pId);
		} else {
			sql.append(" and pId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString());
		}

		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 查找父类与其所有子类（递归，包括子类的子类）
	 * 
	 * @param pId
	 * @return
	 */
	public List<ProductCategory> findAllChildren(Long pId) {
		return BeanUtils
				.toList(ProductCategory.class,
						jdbcTemplate
								.queryForList(
										"select id,name,pId,storeId,path from t_product_category where id = ? or path like ?",
										pId, pId + "-%"));
	}

	public List<ProductCategory> findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_category where storeId = ? order by sort",
						storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 根据商品分类ID删除商品分类信息
	 * 
	 * @param categoryIDS
	 *            商品分类ID或商品分类集合(格式： ID1,ID2,ID3,ID4....)
	 * @return true 删除成功
	 */
	public Boolean deleteById(Long storeId,String[] categoryID) {
		for (String id : categoryID) {
			int deleteID = jdbcTemplate.update(
					"delete from t_product_category  where id = ? and type = 1 and storeId = ?", id);
			if (deleteID <= 0) {
				logger.debug("商品分类ID {} 删除失败！", id,storeId);
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据商家ID获取商品分类信息
	 * 
	 * @param storeId
	 *            商家ID
	 * @param categoryIDS
	 *            分类ID
	 * @return List<ProductCategory>或者null
	 */
	public List<ProductCategory> getProductCategory(Long storeId,
			Long categoryId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"select id,name,path,pId,storeId,type from t_product_category where storeId = ? or type = 0");

		parameters.add(storeId);

		if (null != categoryId) {
			sql.append(" and id = ?");
			parameters.add(categoryId);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		return BeanUtils.toList(ProductCategory.class, list);
	}

	public List<ProductCategory> findByTypeStoreId(Long storeId, Integer type) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select sort, id,name,path,pId,storeId,type from t_product_category where  type = ? ";
		parameters.add(type);
		if (type.equals(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM)) {// 查询自定义
			sql += " AND storeId= ?";
			parameters.add(storeId);
		}
		sql+=" order by sort  ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		return BeanUtils.toList(ProductCategory.class, list);
	}

	/**
	 * 根据父id查询
	 * @param parentId
	 * @param storeId
	 * @return
	 */
	public List<ProductCategory> findByParent(Long parentId, Long storeId) {
		List<Object> parameter = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name,path,pId from t_product_category where 1 = 1 ");

		if (parentId != null) {
			sql.append(" and pId = ? ");
			parameter.add(parentId);
		} else {
			sql.append(" and pId is null ");
		}

		if (storeId != null) {
			sql.append(" and storeId = ?  ");
			parameter.add(storeId);
		} else {
			sql.append(" and storeId is null ");
		}
		sql.append("order by sort ");
		return BeanUtils.toList(ProductCategory.class,
				jdbcTemplate.queryForList(sql.toString(), parameter.toArray()));
	}

	public List<ProductCategory> appFindProductCategory(Long parentId, Long storeId) {
		List<Object> parameter = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer("select t.* ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t ");
		
		// 因新商品可能上线，故现在修改获取分类接口为只获取第一级分类<为兼容以前的数据,加上级别字段为null的数据>，其他级别分类不获取(2016.08.02)
		sf.append(" where (t.level = 0 or t.level is null) ");
		
		if (storeId!=null) {
			//sf.append(" and t.id  in ( SELECT DISTINCT p.categoryId FROM t_product p WHERE p.storeId = ? ) ");//查询有商品的分类
			sf.append(" and  t.storeId = ?  ");//查询有商品的分类
			parameter.add(storeId);
		}
		if (parentId != null) {
			sf.append(" and t.pId = ? ");
			parameter.add(parentId);
		}else {
			sf.append(" and t.pId is null ");
		}
		
		sf.append(" GROUP BY t.id ");
		sf.append("order by sort ");
		return BeanUtils.toList(ProductCategory.class, jdbcTemplate.queryForList(sf.toString(), parameter.toArray()));
	}

	public List<ProductCategory> posFindProductCategory(Long parentId, Long storeId) {
		List<Object> parameter = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer("select t.* ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t ");
		//sf.append(" inner JOIN t_product p ON t.id = p.categoryId ");
		// 因新商品可能上线，故现在修改获取分类接口为只获取第一级分类<为兼容以前的数据,加上级别字段为null的数据>，其他级别分类不获取(2016.07.10)
		sf.append(" where (t.level = 0 or t.level is null) ");
		
		if (storeId!=null) {
			// sf.append(" and t.id  in ( SELECT DISTINCT p.categoryId FROM t_product p WHERE p.storeId = ? ) ");//查询有商品的分类
			sf.append(" and t.storeId = ?");
			parameter.add(storeId);
		}
		if (parentId != null) {
			sf.append(" and t.pId = ? ");
			parameter.add(parentId);
		}else {
			sf.append(" and t.pId is null ");
		}
		sf.append(" GROUP BY t.id ");
		sf.append("order by sort ");
		
		logger.debug("获取分类是的SQL语句是：{},参数是：{}",sf.toString(),parameter.toArray());
		
		return BeanUtils.toList(ProductCategory.class, jdbcTemplate.queryForList(sf.toString(), parameter.toArray()));
	}

	public void updateSort(Long pId,Long storeId, Integer sortBegin,Integer sortEnd, char c) {
		String sql = "update t_product_category set sort = sort "+c+" 1 where storeId = ? and sort >=? ";
		if (sortEnd != null) {
			sql+=" and sort <= "+sortEnd;
		}
		if (pId != null&&pId!=-1) {
			sql+=" and pId = "+pId;
		}else {
			sql+=" and pId is null ";
		}
		jdbcTemplate.update(sql,storeId,sortBegin);
	}

	public List<ProductCategory> checkName(String name, Long id, Long storeId) {
		if(id==null){
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,categoryId from t_product_category where name = ? and storeId=?", name,storeId);
			
			return BeanUtils.toList(ProductCategory.class, list);
		}else{
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,categoryId from t_product_attribute where id<>? and name = ? and storeId=?", id,name,storeId);
			
			return BeanUtils.toList(ProductCategory.class, list);
		}
	}

	public ProductCategory findProductCategoryByName(Long id, String name,
			Long storeId) {
		
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,pId ,(SELECT name FROM t_product_category tpc WHERE  t.pId=tpc.id ) AS parentName from t_product_category t where id<>? and name = ? and storeId=?",
						id,name, storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}

	public Page findByStoreId(Long storeId,Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (storeId != null) {
			and.append(" and t.storeId =  ? ");
			parameters.add(storeId);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product_category t "
				+ "where 1=1 " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select * from t_product_category t where 1=1 ");
		sql.append(and.toString() + " order by t.id desc,t.sort desc limit ? , ? ");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductCategory.class, list));
		return page;
	}
}