package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class ProductSynchronizationDao {
	private final static Logger logger = LoggerFactory.getLogger(ProductSynchronizationDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<ProductStock> findStockExist(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_stock where storeId=?",
						storeId);
		
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductCategory> findProductCategoryByStoreId(Long fromStoreId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_category where storeId=? order by id",
						fromStoreId);
		
		return BeanUtils.toList(ProductCategory.class, list);
	}

	public void saveProductCategroy(ProductCategory productCategory) {
		jdbcTemplate
		.update("insert into t_product_category(id,name,pId,storeId,path,type,sort) values(?,?,?,?,?,?,?)",
				productCategory.getId(), productCategory.getName(),
				productCategory.getpId(), productCategory.getStoreId(),
				productCategory.getPath(), productCategory.getType(),productCategory.getSort());
		
	}

	public List<ProductAttribute> findProductAttributeByCategoryId(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_attribute where categoryId=? order by id",
						id);
		
		return BeanUtils.toList(ProductAttribute.class, list);
	}

	public void saveProductAttribute(ProductAttribute productAttribute) {
		jdbcTemplate.update(
				"insert into t_product_attribute(id,name,categoryId) values(?,?,?)", productAttribute.getId(),
				productAttribute.getName(),productAttribute.getCategoryId());
		
	}

	public List<ProductAttributeValue> findProductAttributeValueByAttributeId(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_attribute_value where attributeId=? order by id",
						id);
		
		return BeanUtils.toList(ProductAttributeValue.class, list);
	}

	public void saveProductAttributeValue(ProductAttributeValue productAttributeValue) {
		jdbcTemplate.update(
				"insert into t_product_attribute_value(id,value,attributeId) values(?,?,?)", productAttributeValue.getId(),
				productAttributeValue.getValue(),productAttributeValue.getAttributeId());
		
	}

	public List<Brand> findBrandByStoreId(Long fromStoreId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_brand where storeId=? order by id",
						fromStoreId);
		
		return BeanUtils.toList(Brand.class, list);
	}

	public void saveBrand(Brand brand) {
		jdbcTemplate.update(
				"insert into t_brand(id,name,storeId) values(?,?,?)", brand.getId(),
				brand.getName(),brand.getStoreId());
		
	}

	public List<Product> findProductByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select p.*,(select b.`name` from t_brand b where b.id=p.brandId)as brandName,"
						+ "(select name from t_product_category c where c.id=p.categoryId) as categoryName from t_product p where p.storeId=?",
						storeId);
		
		return BeanUtils.toList(Product.class, list);
	}

	public Brand findByBrandId(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_brand where id=? ",
						id);
		
		return BeanUtils.toBean(Brand.class, list.get(0));
	}

	public Brand findBrand(String name, Long toStoreId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_brand where name=? and storeId=? ",
						name,toStoreId);
		
		return BeanUtils.toBean(Brand.class, list.get(0));
	}

	public ProductCategory findCategory(String name, Long toStoreId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_category where name=? and storeId=? ",
						name,toStoreId);
		
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}

	public void saveProduct(Product product) {
		jdbcTemplate
		.update("insert into t_product(id,name,categoryId,accountId,storeId,type,brandId,shortName) values(?,?,?,?,?,?,?,?)",
				product.getId(), product.getName(),
				product.getCategoryId(), product.getAccountId(),product.getStoreId(),product.getType(),product.getBrandId(),product.getShortName());
		
	}

	public List<ProductStock> findProductStockByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select ps.*,(select p.`name` from t_product p where p.id=ps.productId)as productName,"
						+ "(select name from t_product_category c where c.id=p.categoryId) as categoryName from t_product_stock ps,t_product p where ps.productId=p.id and ps.storeId=?",
						storeId);
		
		return BeanUtils.toList(ProductStock.class, list);
	}

	public Product findProduct(String name, Long categoryId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product where name=? and categoryId=? ",
						name,categoryId);
		
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	public void saveProductStock(ProductStock productStock) {
		JdbcUtils.save(productStock);
//		jdbcTemplate
//		.update("insert into t_product_stock(id,stock,alarmValue,productId,accountId,storeId,attributeCode,price,memberPrice,barCode,remarks,"
//				+ "sales,hits,createTime,createId,recommended,shelves,type,marketPrice,costPrice,Details,sort,unitName,attributeName,promotionId) "
//				+ "values(?,?,?,?,?,?,?,?,?,?,?,"
//				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?)", productStock.getId(),
//				productStock.getStock(), productStock.getAlarmValue(),
//				productStock.getProductId(),
//				productStock.getAccountId(), productStock.getStoreId(),
//				productStock.getAttributeCode(),
//				productStock.getPrice(),productStock.getMemberPrice(), productStock.getBarCode(),
//				productStock.getRemarks(), productStock.getSales(),
//				productStock.getHits(), new Date(),
//				productStock.getCreateId(),
//				productStock.getRecommended(),
//				productStock.getShelves(), productStock.getType(),
//				productStock.getMarketPrice(),
//				productStock.getCostPrice(), productStock.getDetails(),productStock.getSort(),productStock.getUnitName(),
//				productStock.getAttributeName(),productStock.getPromotionId());
		
	}

	public ProductAttributeValue findPAVById(long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_attribute_value where id=? ",
						id);
		
		return BeanUtils.toBean(ProductAttributeValue.class, list.get(0));
	}

	public ProductAttribute findPAById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_attribute where id=? ",
						id);
		
		return BeanUtils.toBean(ProductAttribute.class, list.get(0));
	}

	public ProductCategory findPCById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_category where id=? ",
						id);
		
		return BeanUtils.toBean(ProductCategory.class, list.get(0));
	}
	
	public List<ProductStockAttributeValue> findAllProductStockAttributeValue(Long storeId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select psav.* from t_product_stock_attribute_value psav " + 
				"INNER JOIN t_product_stock ps on psav.productStockId = ps.id " + 
				"where ps.storeId = ?",storeId);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
	}
}
