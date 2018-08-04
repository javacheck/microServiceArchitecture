package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class ProductDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final static Logger logger = LoggerFactory
			.getLogger(ProductDao.class);

	/**
	 * 查询所有商品分类
	 * 
	 * @return
	 */
	public Page findAll(String storeIdString, Long sysCategoryId, Long storeCategoryId,
			String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if(StringUtils.isNotBlank(storeIdString)){
			and.append(" and p.storeId in( " +storeIdString+" ) ");
		}
		if(sysCategoryId!=null && storeCategoryId==null){
			and.append(" and p.categoryId =  ? ");
			parameters.add(sysCategoryId);
		}
		if(storeCategoryId!=null && sysCategoryId==null){
			and.append(" and p.categoryId = ? "); 
			parameters.add(storeCategoryId);
		}
		if(storeCategoryId!=null && sysCategoryId!=null){
			and.append(" and ( p.categoryId =  ? or p.categoryId = ? ) "); 
			parameters.add(sysCategoryId);
			parameters.add(storeCategoryId);
		}
		if (StringUtils.isNotBlank(name)) {
			and.append(" and p.name like ?");
			parameters.add("%" + name + "%");
		}
		
		Integer total =jdbcTemplate.queryForObject(
				"select count(1) from t_product p where 1=1" + and.toString(), Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		StringBuffer sql = new StringBuffer(
				"select p.id,p.name,p.categoryId,(select name from t_product_category c where c.id=p.categoryId) as categoryName, "
				+ " p.accountId,(select name from t_store s where s.id=p.storeId) as storeName, "
				+ "(select storeId from t_product_category c where c.id=p.categoryId) as categoryStoreId,p.storeId,p.type,"
				+ " p.brandId,(select b.`name` from t_brand b where b.id=p.brandId)as brandName  "
				+ " from t_product p left join t_store s on p.storeId=s.id where 1=1 ");
		sql.append(and.toString() + " order by s.isMain desc,p.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		
		page.setData(BeanUtils.toList(Product.class, list));
		return page;
	}

	public void save(Product product) {
		jdbcTemplate
				.update("insert into t_product(id,name,categoryId,accountId,storeId,type,brandId) values(?,?,?,?,?,?,?)",
						product.getId(), product.getName(),
						product.getCategoryId(), product.getAccountId(),product.getStoreId(),product.getType(),product.getBrandId());
	}

	public void update(Product product) {
		jdbcTemplate.update(
				"update t_product set name=?,categoryId=? where id=?",
				product.getName(), product.getCategoryId(), product.getId());

	}

	public Product findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select p.id,p.name,p.categoryId,(select name from t_product_category c where c.id=p.categoryId) as categoryName,"
						+ " p.accountId,p.storeId,(select name from t_store s where s.id=p.storeId) as storeName,"
						+ "(select storeId from t_product_category c where c.id=p.categoryId) as categoryStoreId,type,"
						+ "p.brandId,(select b.`name` from t_brand b where b.id=p.brandId)as brandName "
						+ " from t_product p where id = ?",
						id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	public Product findById(Long categoryId,Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where id = ? and categoryId = ?",
						id,categoryId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}
	
	public void deleteById(Product product) {
		jdbcTemplate.update("delete from t_product  where id = ?",
				product.getId());
	}
	public void deleteById(Long id) {
		jdbcTemplate.update("delete from t_product  where id = ?",
				id);
	}


	public Product findProduct(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where name = ? and categoryId=? and storeId=?",
						product.getName(), product.getCategoryId(),
						product.getStoreId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}
	public Product findProduct1(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select  p.id,s.barCode,p.storeId FROM  t_product_stock s,t_product p WHERE s.productId=p.id  and p.name = ? and p.categoryId=? and p.storeId=?",
						product.getName(), product.getCategoryId(),
						product.getStoreId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}
	public Product findProductByID(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId from t_product where id = ? and categoryId=? ",
						product.getId(), product.getCategoryId());
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

	public List<Product> findByCategoryId(Long categoryId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product where categoryId=? order by id desc",
						categoryId);
		return BeanUtils.toList(Product.class, list);
	}
	public List<Product> findByCategoryIdAndStroeId(Long categoryId,Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,accountId ,storeId, type from t_product where categoryId=? and storeId = ?  order by id desc",
						categoryId,storeId);
		return BeanUtils.toList(Product.class, list);
	}
	
	public Page posFind(Long categoryId,Long storeId,Page page) {
		logger.debug("categoryId=======================：{}",categoryId);
		//------------------------------------
		// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
		List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ",categoryId+"%");
		List<ProductCategory> pcList = BeanUtils.toList(ProductCategory.class, temp);
		StringBuilder sb = new StringBuilder(20);
		boolean flag = false;
		for (ProductCategory productCategory : pcList) {
			if(flag){
				sb.append(",");
			}
			sb.append(productCategory.getId());
			flag = true;
		}
		//------------------------------------
		
		logger.debug("查询的商品分类是：{}",sb.toString());
		
		String sql = "	FROM t_product p INNER JOIN t_product_stock ps on ps.productId = p.id AND (ps.shelves = "+Constants.ProductStock.POS_UP + " or ps.shelves = " + Constants.ProductStock.ALL_UP + ")"; 
		
//		String where = " where p.categoryId=? and p.storeId = ? ";
		
		String where = " where p.categoryId in("+sb.toString()+") and p.storeId = ? ";
		
		Integer total = jdbcTemplate.queryForObject("select count(1) from (select p.id" + sql + where+" group by p.id) abc ", Integer.class, storeId);
		
		page.setTotal(total);
		if (total.intValue() == 0){
			return page;
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT p.id, p. NAME,p.categoryId,p.accountId,p.storeId,p.type,ps.imageId as picUrl  " + sql +
				//" LEFT JOIN t_product_image img on ps.id = img.productStockId  "
				where + " group by p.id order by ps.sort desc LIMIT ?,?",
						storeId,page.getStart(),page.getPageSize());
		
		List<Product> productList = BeanUtils.toList(Product.class, list);
		
		page.setData(productList);
		return page;
	}
	
	public List<Product> posFind(String name,Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new  StringBuffer();
		querySQL.append("SELECT p.id, p.NAME,p.categoryId,p.accountId,p.storeId,p.type FROM	t_product p  where instr(p.name,?)>0 and p.storeId = ? AND (SELECT	COUNT(1) FROM t_product_stock ps WHERE ps.productId = p.id AND ( ps.shelves = ? or ps.shelves = ? ) ) >0");
		parameters.add(name);
		parameters.add(storeId);
		parameters.add(Constants.ProductStock.POS_UP);
		parameters.add(Constants.ProductStock.ALL_UP);
		querySQL.append(" UNION ");
		querySQL.append(" select p.id, p.NAME,p.categoryId,p.accountId,p.storeId,p.type from  t_product p where p.id in  (select ps.productId from t_product_stock ps where ps.storeId = ? and ( ps.shelves = ? or ps.shelves = ? ) and instr(ps.barCode,?) ) ");
		parameters.add(storeId);
		parameters.add(Constants.ProductStock.POS_UP);
		parameters.add(Constants.ProductStock.ALL_UP);
		parameters.add(name);
		
//		String sql = "SELECT p.id, p. NAME,p.categoryId,p.accountId,p.storeId,p.type FROM	t_product p  where p.name like ? and p.storeId = ? "
//				+ "AND (SELECT	COUNT(1) FROM t_product_stock ps	WHERE	ps.productId = p.id AND ps.shelves = "+Constants.ProductStock.STORE_SALEING+" ) >0  order by id desc";
//		logger.debug("posFind  sqlis {}"+sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,"%"+name+"%",storeId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		logger.debug("posFind  list {}"+list.size());
		return BeanUtils.toList(Product.class, list);
	}
	
	
	public Product findByStockId(Long stockId) {
		StringBuffer sql  =new StringBuffer();
		sql.append(" SELECT * FROM t_product p");
		sql.append(" INNER JOIN t_product_stock psk");
		sql.append(" ON  p.id=psk.productId");
		sql.append(" WHERE psk.id=?");
		List<Map<String, Object>> list= jdbcTemplate.queryForList(sql.toString(),stockId);
		return list.isEmpty() ? null : BeanUtils.toBean(Product.class,	list.get(0));
	}
	
	/**
	 * 通过姓名模糊查找
	 * @param getByName
	 * @param storeId 
	 * @return
	 */
	public List<Product> findByName(String getByName, Long storeId) {
		String sql = "SELECT	p.* FROM 	t_product p   WHERE  p.name like ? and p.storeId = ? order by p.id desc";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql,"%"+getByName+"%",storeId);
		return BeanUtils.toList(Product.class, list);
	}

	public List<Product> findByNameAndStoreId(String name, Long storeId) {
		String sql = "SELECT p.* FROM  t_product p INNER JOIN t_product_stock pc ON p.id = pc.productId  WHERE  p.name = ? and pc.storeId = ? order by p.id desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,name,storeId);
		return BeanUtils.toList(Product.class, list);
	}

	/**
	 * 获取商品列表
	 * @param page page对象
	 * @param userId 用户ID
	 * @param productId 商品ID  可为空，(为空查询此用户的所有商品)
	 * @param storeId 商家ID 可为空，(为空查询此用户的所有商铺商品)
	 * @param shelves 上架状态(0未上架1上架中) 可为空，(为空或非0,非1则查询缺货库存)
	 * @return page(ProductStock)
	 */
	public Page getProductInformation(Page page,  Long storeId, Integer shelves) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
				
		String data = "s.id,p.name as productName,p.accountId,"
					+ "s.stock,s.alarmValue,s.productId,s.storeId,s.price,s.barCode,s.createTime,"
				    + "s.remarks,s.sales,s.hits,s.updateTime,s.recommended,s.shelves,(select id from t_product_image img where img.productStockId = s.id limit 1) as imageId";
		querySQL.append(" from t_product p,t_product_stock s where p.id = s.productId and p.type = 1 ");
		
		// 商家
		if( null != storeId ){
			querySQL.append(" and s.storeId = ?");
			parameters.add(storeId);
		}
		
		// 上架/不查询上架情况，则查询缺货情况
		if(null != shelves && (shelves.equals(0) || shelves.equals(1)) ){
			querySQL.append(" and s.shelves = ?");
			parameters.add(shelves);
		} else {
			querySQL.append(" and s.stock = 0 ");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() , Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by s.accountId,s.productId desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}

	public Boolean update(Product product, boolean b) {
		int updateRow = jdbcTemplate.update(
				"update t_product set name=?,categoryId=? where id=?",
				product.getName(), product.getCategoryId(), product.getId());
		if(updateRow > 0){
			return true;
		}
		return false;
	}

	public Page AppFindList(Page page, Long categoryId, String name, Long storeId) {
		
		logger.debug("categoryId=======================：{}",categoryId);
		
		String sql = " from t_product p INNER JOIN t_product_stock ps on ps.productId = p.id AND (ps.shelves = "+Constants.ProductStock.APP_UP + " or ps.shelves = " + Constants.ProductStock.ALL_UP + ")";
		List<Object> parameters = new ArrayList<Object>();
		sql += " where p.storeId = ? ";
		parameters.add(storeId);
		if (categoryId!=null) {
			//------------------------------------
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.08.04)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ",categoryId+"%");
			List<ProductCategory> pcList = BeanUtils.toList(ProductCategory.class, temp);
			StringBuilder sb = new StringBuilder(20);
			boolean flag = false;
			for (ProductCategory productCategory : pcList) {
				if(flag){
					sb.append(",");
				}
				sb.append(productCategory.getId());
				flag = true;
			}
			//------------------------------------
			
			logger.debug("查询的商品分类是：{}",sb.toString());
			
			sql+=" and p.categoryId in ("+sb.toString()+")";
		}
		if (StringUtils.isNotBlank(name)) {
			sql+=" and p.name like ?";
			parameters.add("%"+name+"%");
		}
		logger.debug("AppFindList sql is {},parameters is {}",sql,parameters);
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql , Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.* "+sql+" group by p.id order by ps.sort desc LIMIT ?,?",parameters.toArray());
		page.setData(BeanUtils.toList(Product.class, list));
		return page;
	}

	public List<Product> findProductType(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.* from t_product_stock s,t_product t where s.productId=t.id and  t.name= ? and t.storeId = ? and t.categoryId=? and t.type=? order by id desc",
						product.getName(),product.getStoreId(),product.getCategoryId(),1);
		return BeanUtils.toList(Product.class, list);
	}
	
	public ProductStock getProductInformation(Long stockId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
				
		String data = "p.id,p.name as productName,p.accountId,"
					+ "s.stock,s.alarmValue,s.productId,s.storeId,s.price,s.barCode,s.createTime,"
				    + "s.remarks,s.sales,s.hits,s.updateTime,s.recommended,s.shelves";
		
		querySQL.append(" from t_product p,t_product_stock s where p.id = s.productId and s.type = 1 ");
		
		// 商家
		if( null != storeId ){
			querySQL.append(" and s.storeId = ?");
			parameters.add(storeId);
		}
		
		// 库存ID
		if( null != stockId ){
			querySQL.append(" and s.id = ?");
			parameters.add(stockId);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by s.accountId,s.productId desc limit ?,?", parameters.toArray());
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public void changeShelves(Long productId,int i) {
		jdbcTemplate.update(
				"update t_product_stock set shelves=? where productId=?",
				i, productId);
		
	}

	public void deleteByproductStockId(long productStockId) {
		jdbcTemplate.update("delete from t_product_stock  where id = ?",
				productStockId);
		
	}

	/**
	 * POS端入库管理修改商品名称(2015.12.15)
	 * @param productID
	 * @param productName
	 * @return
	 */
	public boolean updateProductName(Long productID,String productName) {
		int i = jdbcTemplate.update( "update t_product set name=? where id=?",productName, productID);
		if( i > 0 ){
			return true; 
		} else {
			return false;
		}
	}

	public List<Product> findProductType1(Product product,Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.* from t_product_stock s,t_product t where s.productId=t.id "
						+ " and s.id<>? and  t.name= ? and t.storeId = ? and t.categoryId=? and t.type=? order by id desc",
						id,product.getName(),product.getStoreId(),product.getCategoryId(),1);
		return BeanUtils.toList(Product.class, list);
	}

	public List<Product> posFindByProductId(Long id, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new  StringBuffer();
		querySQL.append("SELECT p.id, p.NAME,p.categoryId,p.accountId,p.storeId,p.type FROM	t_product p  where id=? and p.storeId = ? ");
		parameters.add(id);
		parameters.add(storeId);
		
		
//		String sql = "SELECT p.id, p. NAME,p.categoryId,p.accountId,p.storeId,p.type FROM	t_product p  where p.name like ? and p.storeId = ? "
//				+ "AND (SELECT	COUNT(1) FROM t_product_stock ps	WHERE	ps.productId = p.id AND ps.shelves = "+Constants.ProductStock.STORE_SALEING+" ) >0  order by id desc";
//		logger.debug("posFind  sqlis {}"+sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,"%"+name+"%",storeId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		logger.debug("posFind  list {}"+list.size());
		return BeanUtils.toList(Product.class, list);
	}
}