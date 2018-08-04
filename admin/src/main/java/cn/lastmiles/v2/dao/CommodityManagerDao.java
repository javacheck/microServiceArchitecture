/**
 * createDate : 2016年6月14日上午10:44:19
 */
package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class CommodityManagerDao {
	/**
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommodityManagerDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(Long storeId, String name, String barCode, Long categoryId, Integer shelves, Long brandId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(150);
		querySQL.append(" from t_product p inner join t_product_stock ps on p.id = ps.productId inner join t_store s on s.id = p.storeId where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and p.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and (p.name like ? or p.shortName like ? )");
			parameters.add("%" + name + "%");
			parameters.add("%" + name + "%");
		}
		
		if( null != categoryId ){
			String path = jdbcTemplate.queryForObject("select pp.path from t_product_category pp where pp.id = " + categoryId, String.class);
			querySQL.append(" and p.categoryId in ( select pc.id from t_product_category pc where pc.path like ?)");
			parameters.add(path + "%");
		}
		
		if( null != shelves ){
			if( ObjectUtils.equals(-2, shelves)){ // 收银端、APP端上架
				querySQL.append(" and ps.shelves = ? ");
//				parameters.add(cn.lastmiles.constant.Constants.ProductStock.APP_UP);
//				parameters.add(cn.lastmiles.constant.Constants.ProductStock.POS_UP);
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.ALL_UP);
			} else if( ObjectUtils.equals(-4, shelves)){ // 全部下架
				querySQL.append(" and ps.shelves = ? ");
//				parameters.add(cn.lastmiles.constant.Constants.ProductStock.APP_DOWN);
//				parameters.add(cn.lastmiles.constant.Constants.ProductStock.POS_DOWN);
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.ALL_DOWN);
			} else {				
				querySQL.append(" and ps.shelves = ?");
				parameters.add(shelves);
			}
		}
		
		if( StringUtils.isNotBlank(barCode) ){
			querySQL.append(" and ps.barCode like ?");
			parameters.add("%"+barCode+"%");
		}
		
		if( null != brandId ){
			querySQL.append(" and p.brandId = ? ");
			parameters.add(brandId);
		}
		String sql = "select count(*) from ( select p.* " + querySQL.toString() + " group by p.id ) s";
		logger.debug("sql is {},parameters is {}",sql,parameters.toArray());
		Integer total = jdbcTemplate.queryForObject(sql,Integer.class,parameters.toArray());
		
		page.setTotal(total);
		if( total == 0 ){
			return page;
		}
		
		querySQL.append(" group by p.id  order by p.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());		
		
		querySQL.insert(0,"select s.name as storeName,p.*,(select name from t_product_category pc where pc.id = p.categoryId limit 1) as categoryName,ps.shelves as shelves,ps.sort as sort,(select name from t_brand b where b.id = p.brandId) as brandName");
		
		logger.debug("query SQL is {},parameters is {}",querySQL.toString(),parameters.toArray());
		
		page.setData(BeanUtils.toList(Product.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray())));
		return page;
	}

	public boolean save(Product product) {
		StringBuilder insertSQL = new StringBuilder(200);
		insertSQL.append("insert into t_product(id,name,categoryId,accountId,storeId,type,brandId,shortName)");
		insertSQL.append(" values(?,?,?,?,?,?,?,?)");
		
		Object[] parameters = new Object[8];
		parameters[0] = product.getId();
		parameters[1] = product.getName();
		parameters[2] = product.getCategoryId();
		parameters[3] = product.getAccountId();
		parameters[4] = product.getStoreId();
		parameters[5] = product.getType();
		parameters[6] = product.getBrandId();
		parameters[7] = product.getShortName();

		return jdbcTemplate.update(insertSQL.toString(),parameters) > 0 ? true : false;
	}

	public boolean save(List<Object[]> batchInsertArgs) {
		StringBuilder insertSQL = new StringBuilder(200);
		logger.debug("batchInsertArgs is {}",batchInsertArgs);
		insertSQL.append("insert into t_product_stock");
		insertSQL.append("(productId,categoryId,accountId,storeId,sort,unitId,stock,alarmValue,barCode,remarks");
		insertSQL.append(",details,createTime,createId,shelves,type,price,memberPrice,marketPrice,costPrice,weighing");
		insertSQL.append(",returnGoods,imageId,attributeCode,attributeName,id)");
		insertSQL.append(" values(?,?,?,?,?,?,?,?,?,?");
		insertSQL.append(",?,?,?,?,?,?,?,?,?,?");
		insertSQL.append(",?,?,?,?,?)");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),batchInsertArgs) ;
		if( result.length == batchInsertArgs.size() ){
			return true;
		}
		return false;
	}

	public boolean saveValue(List<Object[]> batchValueArgs) {
		StringBuilder insertSQL = new StringBuilder(50);
		
		insertSQL.append("insert into t_product_stock_attribute_value");
		insertSQL.append("(id,productStockId,productAttributeId,value,number,productId)");
		insertSQL.append(" values(?,?,?,?,?,?)");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),batchValueArgs) ;
		if( result.length == batchValueArgs.size() ){
			return true;
		}
		return false;
	}

	public Product findById(Long id) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select ps.*,p.*,(select name from t_product_category pc where pc.id = p.categoryId limit 1) as categoryName from t_product p,t_product_stock ps where p.id = ps.productId and p.id = ? limit 1",id);
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	public List<ProductStock> findByProductId(Long id) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_stock where productId = ? ",id);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStockAttributeValue> findByProductStock(Long id) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where productStockId = ? ",id);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}

	public boolean update(Product product) {
		StringBuilder updateSQL = new StringBuilder(200);
		updateSQL.append("update t_product set name = ? ,categoryId = ? ,accountId = ? ,storeId = ? ,type = ? ,brandId = ? ,shortName = ? where id = ?");
		
		Object[] parameters = new Object[8];
		parameters[0] = product.getName();
		parameters[1] = product.getCategoryId();
		parameters[2] = product.getAccountId();
		parameters[3] = product.getStoreId();
		parameters[4] = product.getType();
		parameters[5] = product.getBrandId();
		parameters[6] = product.getShortName();
		parameters[7] = product.getId();

		return jdbcTemplate.update(updateSQL.toString(),parameters) >= 0 ? true : false;
	}

	public boolean update(List<Object[]> batchInsertArgs) {
		StringBuilder insertSQL = new StringBuilder(200);
		
		insertSQL.append("update t_product_stock");
		insertSQL.append(" set productId = ? ,categoryId = ? ,accountId = ? ,storeId = ? ,sort = ? ,unitId = ? ,stock = ? ,alarmValue = ? ,barCode = ? ");
		insertSQL.append(",remarks = ?,details = ?,updateTime = ? ,updateId = ?,shelves = ? ,type = ? ,price = ? ");
		insertSQL.append(",memberPrice = ? ,marketPrice = ? ,costPrice = ? ,weighing = ? ,returnGoods = ? ,imageId = ?,attributeCode = ?,attributeName = ?  where id = ?");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),batchInsertArgs) ;
		if( result.length == batchInsertArgs.size() ){
			return true;
		}
		return false;
	}

	public void deleteValue(Long id) {
		jdbcTemplate.update("delete from t_product_stock_attribute_value where productId = ? ",id);
	}

	public void deleteByProductStockId(Long productStockId) {
		jdbcTemplate.update("delete from t_product_stock_attribute_value where productStockId = ? ",productStockId);		
	}

	public boolean updateProduct(Long categoryId, Long brandId, String productIdString) {
		if( StringUtils.isBlank(productIdString) ){
			return false;
		}
		// 分类没选择,品牌没选择
		if( null == categoryId && ObjectUtils.equals(-1L, brandId)){
			return true;
		}
		
		StringBuilder sql = new StringBuilder(" update t_product set ");
		long sqlInitLength = sql.length();
		
		List<Object> parameters = new ArrayList<Object>();
		if( null != categoryId ){
			sql.append(" categoryId = ? ");
			parameters.add(categoryId);
		}
		if( !ObjectUtils.equals(-1L, brandId)){
			if( sql.length() - sqlInitLength > 0  ){
				sql.append(",");
			}
			sql.append(" brandId = ? ");
			parameters.add(brandId);
		}
		
		sql.append("where id in("+productIdString+")");
		
		return jdbcTemplate.update(sql.toString(),parameters.toArray()) > 0 ? true : false;
	}

	public int updateProductStock(Integer weighing, Integer returnGoods, Integer shelves, Long categoryId, String productIdString) {
		if( StringUtils.isBlank(productIdString) ){
			return 0;
		}
		
		StringBuilder sql = new StringBuilder(" update t_product_stock set ");
		long sqlInitLength = sql.length();
		
		List<Object> parameters = new ArrayList<Object>();
		if( null != categoryId ){
			sql.append(" categoryId = ? ");
			parameters.add(categoryId);
		}
		
		if( sql.length() - sqlInitLength > 0  ){
			sql.append(",");
		}
		sql.append(" weighing = ? ");
		parameters.add(weighing);
	
		sql.append(", returnGoods = ? ");
		parameters.add(returnGoods);
		
		sql.append(", shelves = ? ");
		parameters.add(shelves);
		
		sql.append("where productId in("+productIdString+")");
		
		return jdbcTemplate.update(sql.toString(),parameters.toArray());
	}

	public int deleteProductByProductId(Long productId) {
		jdbcTemplate.update("delete from t_product_stock where productId = ? ",productId);
		return jdbcTemplate.update("delete from t_product where id = ? ",productId);
	}

	public List<Product> getProductListByStoreId(Long storeId) {
		List<Map<String,Object>> list = null;
		if( null == storeId ){
			list = jdbcTemplate.queryForList("select ps.*,p.*,(select name from t_product_category pc where pc.id = p.categoryId limit 1) as categoryName from t_product p,t_product_stock ps where p.id = ps.productId group by p.id");
		} else {
			list = jdbcTemplate.queryForList("select ps.*,p.*,(select name from t_product_category pc where pc.id = p.categoryId limit 1) as categoryName from t_product p,t_product_stock ps where p.id = ps.productId and p.storeId = ? group by p.id",storeId);
		}
		return BeanUtils.toList(Product.class, list);
	}

	public List<ProductStock> getProductStockListByStoreId(Long storeId) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select ps.* from t_product_stock ps where ps.storeId = ? ",storeId);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public boolean batchProductSave(List<Object[]> productBatchInsertArr) {
		StringBuilder insertSQL = new StringBuilder(200);
		
		insertSQL.append("insert into t_product(name,categoryId,accountId,storeId,brandId,shortName,type,id)");
		insertSQL.append(" values(?,?,?,?,?,?,?,?)");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),productBatchInsertArr) ;
		if( result.length == productBatchInsertArr.size() ){
			return true;
		}
		return false;
	}
	
	
	
	public List<Product> reportList(String storeId, String name, String barCode, String categoryId, String shelves, String brandId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(150);
		querySQL.append(" from t_product p inner join t_product_stock ps on p.id = ps.productId where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and p.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and (p.name like ? or p.shortName like ? )");
			parameters.add("%" + name + "%");
			parameters.add("%" + name + "%");
		}
		
		if( StringUtils.isNotBlank(categoryId) ){
			querySQL.append(" and p.categoryId = ? ");
			parameters.add(categoryId);
		}
		
		if( StringUtils.isNotBlank(shelves) ){
			if( "-2".equals(shelves) ){ // 收银端、APP端上架
				querySQL.append(" and (ps.shelves = ? or ps.shelves = ? )");
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.APP_UP);
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.POS_UP);
			} else if( "-4".equals(shelves) ){ // 下架
				querySQL.append(" and (ps.shelves = ? or ps.shelves = ? )");
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.APP_DOWN);
				parameters.add(cn.lastmiles.constant.Constants.ProductStock.POS_DOWN);
			} else {				
				querySQL.append(" and ps.shelves = ?");
				parameters.add(shelves);
			}
		}
		
		if( StringUtils.isNotBlank(barCode) ){
			querySQL.append(" and ps.barCode like ?");
			parameters.add("%"+barCode+"%");
		}
		
		if( StringUtils.isNotBlank(brandId) ){
			querySQL.append(" and p.brandId = ? ");
			parameters.add(brandId);
		}
		
		querySQL.append(" group by p.id  order by p.id desc ");
		
		querySQL.insert(0,"select p.*,(select name from t_product_category pc where pc.id = p.categoryId limit 1) as categoryName,ps.shelves as shelves,ps.sort as sort,(select name from t_brand b where b.id = p.brandId) as brandName");
		
		logger.debug("reportQuery SQL is {},parameters is {}",querySQL.toString(),parameters.toArray());
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		return BeanUtils.toList(Product.class, list) ;			
	}

	public int byProductNameFindProduct(String name, Long storeId, Long id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(150);
		querySQL.append("select count(*) from t_product p where 1=1 ");
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and name = ?");
			parameters.add(name);
		}
		
		if( null != storeId ){
			querySQL.append(" and storeId = ? ");
			parameters.add(storeId);
		}
		if( null != id ){
			querySQL.append(" and id <> ?");
			parameters.add(id);
		}
		Integer count = jdbcTemplate.queryForObject(querySQL.toString(),Integer.class,parameters.toArray()); 
		logger.debug("byProductNameFindProduct count is {}",count);
		return count;
	}

	public List<ProductStock> byBarCodeFindProductStock(Long storeId, String barCode, Long id) {
		List<Map<String, Object>> list = null;
		if( null == id ){
			String sql = "SELECT ps.* FROM  t_product_stock ps WHERE  ps.barCode = ? and ps.storeId = ? order by ps.id desc";			
			list = jdbcTemplate.queryForList(sql, barCode, storeId);
		} else {
			String sql = "SELECT ps.* FROM  t_product_stock ps WHERE  ps.barCode = ? and ps.storeId = ? and ps.productId <> ? order by ps.id desc";
			list = jdbcTemplate.queryForList(sql, barCode, storeId,id);

		}
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<Product> findByNameAndStoreId(String name, Long storeId,Long id) {
		
		List<Map<String, Object>> list = null;
		if( null == id ){
			String sql = "SELECT p.* FROM  t_product p WHERE  p.name = ? and p.storeId = ? order by p.id desc";			
			list = jdbcTemplate.queryForList(sql, name, storeId);
		} else {
			String sql = "SELECT p.* FROM  t_product p WHERE  p.name = ? and p.storeId = ? and p.id <> ? order by p.id desc";
			list = jdbcTemplate.queryForList(sql, name, storeId,id);

		}
		return BeanUtils.toList(Product.class, list);
	}

	public int exitProductByCategoryId(Long id) {
		Integer count = jdbcTemplate.queryForObject("select count(*) from t_product p where p.categoryId = ? ",Integer.class,id);
		
		logger.debug("{}号商品分类存在关联商品！result:{}",id,count);
		
		return count;
	}

	public List<ProductAttribute> getProductAttributeListByStoreID() {
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select pa.* from t_product_attribute pa ");
		return BeanUtils.toList(ProductAttribute.class, list);
	}

	public List<ProductStock> getProductStockListByProductId(Long productId) {
		List<Map<String, Object>> list = null;
		if( null == productId ){
			String sql = "SELECT ps.* FROM  t_product_stock ps WHERE 1=1 order by ps.id desc";			
			list = jdbcTemplate.queryForList(sql);
		} else {
			String sql = "SELECT ps.* FROM  t_product_stock ps WHERE  ps.productId = ? order by ps.id desc";
			list = jdbcTemplate.queryForList(sql, productId);

		}
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStockAttributeValue> findByAttributeCode(String attributeCode) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where id in (" + attributeCode + ")");
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}
	
	/**
	 * 商品是否已经销售
	 * @param productId
	 * @return
	 */
	public boolean checkSales(Long productId){
		Integer n = jdbcTemplate.queryForObject("SELECT count(1) from t_order_item oi " +  
				"INNER JOIN t_product_stock ps on oi.stockId = ps.id " +  
				"INNER JOIN t_product p on p.id = ps.productId " + 
				"where p.id = ?", Integer.class,productId);
		return n > 0;
	}
}