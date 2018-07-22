package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.ProductImage;
import cn.self.cloud.bean.ProductStock;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductStockDao {
	private final static Logger logger = LoggerFactory.getLogger(ProductStockDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 查询所有商品库存
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
				"select count(1) from t_product_stock " + where,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,t.productAttributeId,t.productAttributeValueId,t.accountId,"
				+ "(select pav.`value` from t_product_attribute_value pav where pav.id=t.productAttributeValueId)as productAttributeValueName,"
				+ "(select pa.`name` from t_product_attribute pa where pa.id=t.productAttributeId)as productAttributeName,"
				+ "(select p.`name` from t_product p where p.id=t.productId)as productName"
				+ " from t_product_stock t ");
		sql.append(where + " limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(ProductStock.class, list));
		
		return page;
	}
	public ProductStock findProductStock(ProductStock productStock) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,stock,alarmValue,productId,accountId,storeId,attributeCode,price,barCode from t_product_stock where attributeCode=?", productStock.getAttributeCode());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}
	
	public  void save(ProductStock productStock) {
		jdbcTemplate.update(
				"insert into t_product_stock(id,stock,alarmValue,productId,accountId,storeId,attributeCode,price,barCode) values(?,?,?,?,?,?,?,?,?)", productStock.getId(),productStock.getStock(),productStock.getAlarmValue(),productStock.getProductId(),productStock.getAccountId(),productStock.getStoreId(),productStock.getAttributeCode(),productStock.getPrice(),productStock.getBarCode());
	}	
	
	public void update(ProductStock productStock) {
		jdbcTemplate.update("update t_product_stock set stock=?,price=?,alarmValue=?,barCode=? where id=?",
				productStock.getStock(),productStock.getPrice(),productStock.getAlarmValue(),productStock.getBarCode(),productStock.getId());
		
	}
	public void deleteById(Long id) {
		jdbcTemplate.update(
				"delete from t_product_stock  where id = ?", id);
	}
	public ProductStock findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
						+ "(select name from t_product tp where tp.id=t.productId) as productName,"
						+ "t.accountId,t.attributeCode,t.price,t.alarmValue "
						+ "from t_product_stock t where t.id=?",id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}
	public List<ProductStock> findByProductId(Long productId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
						+ "(select name from t_product tp where tp.id=t.productId) as productName,"
						+ "t.accountId,t.attributeCode,t.price,t.alarmValue "
						+ "from t_product_stock t where t.productId=?", productId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductStock.class, list);
	}
	public ProductStock findByStockId(Long stockId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select t.*,"
						+ "(select name from t_product tp where tp.id=t.productId) as productName,"
						+ "t.accountId,t.attributeCode,t.price "
						+ "from t_product_stock t where t.id=? ", stockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}
	public void saveProductImage(ProductImage productImage) {
		jdbcTemplate.update(
				"insert into t_product_image(id,productStockId,suffix,sort) values(?,?,?,?)",productImage.getId(),productImage.getProductStockId(),productImage.getSuffix(),productImage.getSort() );
		
	}
	public ProductImage findProductImage(ProductImage productImage) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select id,productStockId,suffix,sort from t_product_image where productStockId=? and sort=?", productImage.getProductStockId(),productImage.getSort());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductImage.class, list.get(0));
	}
	
	/**
	 * 通过模糊的方式 attributeCode 查找
	 * @param attributeCode
	 * @param productId
	 * @param storeId
	 * @return
	 */
	public List<ProductStock> findByFuzzyAttributeCode(Long attributeCode,Long productId,Long storeId) {
		String sql = "SELECT * FROM t_product_stock WHERE 1=1 AND  productId = ? AND storeId = ? AND ( attributeCode like ? OR attributeCode like ? OR attributeCode like ? )";
		logger.debug("findByFuzzyAttributeCode  sql is :"+sql);
		Object [] papms= {productId,storeId,"%-"+attributeCode,"%-"+attributeCode+"-%",attributeCode+"-%"};
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(sql,papms);
		return BeanUtils.toList(ProductStock.class, list);
	}
	
	/**
	 * 通过不定项的 attributeCode 查找
	 * @param papms
	 * @param productId
	 * @param storeId
	 * @return
	 */
	public List<ProductStock> findByFuzzyStockId(List<String> papms,Long productId,Long storeId) {
		StringBuffer sf= new StringBuffer();
		String sql = "SELECT * FROM t_product_stock WHERE 1=1 AND  productId = ? AND storeId = ?";
		if (papms!=null&&papms.size()>=1) {
			sf.append("AND (");
			for (int i = 0; i < papms.size(); i++) {
				sf.append(" attributeCode like ? OR");
			}
			sf.setLength(sf.length() - 2);
			sf.append(")");
		}
		sql=sql+sf.toString();
		logger.debug("sql is :"+sql);
		papms.add(0, storeId+"");
		papms.add(0, productId+"");
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(sql,papms.toArray());
		return BeanUtils.toList(ProductStock.class, list);
	}
}
