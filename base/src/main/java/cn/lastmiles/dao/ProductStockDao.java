package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.utils.FileServiceUtils;

@Repository
public class ProductStockDao {
	private final static Logger logger = LoggerFactory
			.getLogger(ProductStockDao.class);
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
				"select count(1) from t_product_stock " + where, Integer.class,
				parameters.toArray());
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
	public Page findAll(Long storeId, String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (storeId != null) {
			and.append(" and t.storeId =  ? ");
			parameters.add(storeId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and tp.name like ?");
			parameters.add("%" + name + "%");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product tp,t_product_stock t "
				+ "where t.productId = tp.Id " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
						+ "tp.name as productName,(select name from t_store s where s.id=t.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=tp.categoryId) as categoryName, "
						+ "t.accountId,t.attributeCode,t.attributeName,t.price,t.alarmValue,t.shelves,t.type,t.sort "
						+ "from t_product tp,t_product_stock t "
						+ "where t.productId = tp.Id ");
		sql.append(and.toString() + " order by t.sort desc, t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}
	public Page findAll(String storeIdString, Long sysCategoryId, Long storeCategoryId,
			String name,Integer alarmValueType,Integer shelves,String barCode, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and t.storeId in( " +storeIdString+" ) ");
		}
		if (sysCategoryId != null && storeCategoryId == null) {
			and.append(" and tp.categoryId =  ? ");
			parameters.add(sysCategoryId);
		}
		if (storeCategoryId != null && sysCategoryId == null) {
			and.append(" and tp.categoryId = ? ");
			parameters.add(storeCategoryId);
		}
		if (storeCategoryId != null && sysCategoryId != null) {
			and.append(" and ( tp.categoryId =  ? or tp.categoryId = ? ) ");
			parameters.add(sysCategoryId);
			parameters.add(storeCategoryId);
		}
		if (StringUtils.isNotBlank(name)) {
			and.append(" and tp.name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(barCode)) {
			and.append(" and t.barCode like ?");
			parameters.add("%" + barCode + "%");
		}
		if (alarmValueType != null && !"".equals(alarmValueType)) {
			and.append(" and t.stock<=t.alarmValue and t.stock<>-99 ");
		}
		if (shelves != null && !"".equals(shelves)) {
			and.append(" and t.shelves=? ");
			parameters.add(shelves);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product tp,t_product_stock t "
				+ "where t.productId = tp.Id " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,t.unitName,"
						+ "tp.name as productName,t.storeId,s.name as storeName,"
						+ "(select name from t_product_category pc where pc.id=tp.categoryId) as categoryName, "
						+ "t.accountId,t.attributeCode,t.price,t.memberPrice,t.costPrice,t.marketPrice,t.alarmValue,t.shelves,t.type,t.sort "
						+ "from t_product tp,t_product_stock t inner join t_store s on s.id = t.storeId "
						+ "where t.productId = tp.Id ");
		sql.append(and.toString() + " order by s.isMain desc, t.sort desc, t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}

	public Page findAll(String storeIds, Long categoryId, String name,
			Double min, Double max, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();

		// 查出传入店铺的所有商品和库存信息
		if (StringUtils.isNotBlank(storeIds)) {
			and.append(" and t.storeId in (" + storeIds + ") ");
		}

		if (StringUtils.isNotBlank(name)) {
			and.append(" and name like ?");
			parameters.add("%" + name + "%");
		}

		if (min != null) {
			and.append(" and price >= ?");
			parameters.add(min);
		}

		if (max != null) {
			and.append(" and price <= ?");
			parameters.add(max);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product tp,t_product_stock t "
				+ "where t.productId = tp.Id " + "and tp.categoryId="
				+ categoryId + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
						+ "tp.name as productName,"
						+ "t.accountId,t.attributeCode,t.price,t.memberPrice,t.alarmValue "
						+ "from t_product tp,t_product_stock t "
						+ "where t.productId = tp.Id " + "and tp.categoryId="
						+ categoryId);
		sql.append(and.toString() + " order by tp.name,t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}

	public ProductStock findProductStock(ProductStock productStock) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,stock,alarmValue,productId,accountId,storeId,attributeCode,price,memberPrice,barCode from t_product_stock where attributeCode=?",
						productStock.getAttributeCode());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public ProductStock byAttributeFindProductStock(Product product,
			ProductStock productStock) {
		if (productStock.getId() == null) {
			List<Map<String, Object>> list=null;
			if(productStock.getAttributeCode()==""){
				list = jdbcTemplate
						.queryForList(
								"select ts.id,ts.stock,ts.alarmValue,ts.productId,ts.accountId,ts.storeId,ts.attributeCode,ts.price,ts.memberPrice,ts.barCode "
										+ " from t_product t,t_product_stock ts where ts.productId = t.id and t.name=? and ts.storeId=? and ts.attributeCode is null",
								product.getName(), productStock.getStoreId());
			}else{
				list = jdbcTemplate
						.queryForList(
								"select ts.id,ts.stock,ts.alarmValue,ts.productId,ts.accountId,ts.storeId,ts.attributeCode,ts.price,ts.memberPrice,ts.barCode "
										+ " from t_product t,t_product_stock ts where ts.productId = t.id and t.name=? and ts.storeId=? and ts.attributeCode='"
										+ productStock.getAttributeCode() + "'",
										product.getName(), productStock.getStoreId());
			}
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductStock.class, list.get(0));
		} else {
			List<Map<String, Object>> list=null;
			if(productStock.getAttributeCode()==""){
				list = jdbcTemplate
						.queryForList(
								"select ts.id,ts.stock,ts.alarmValue,ts.productId,ts.accountId,ts.storeId,ts.attributeCode,ts.price,ts.memberPrice,ts.barCode "
										+ " from t_product t,t_product_stock ts where ts.productId = t.id and ts.id<> ? and t.name=? and ts.storeId=? and ts.attributeCode is null",
								productStock.getId(), product.getName(),
								productStock.getStoreId());
			}else{
				list = jdbcTemplate
						.queryForList(
								"select ts.id,ts.stock,ts.alarmValue,ts.productId,ts.accountId,ts.storeId,ts.attributeCode,ts.price,ts.memberPrice,ts.barCode "
										+ " from t_product t,t_product_stock ts where ts.productId = t.id and ts.id<> ? and t.name=? and ts.storeId=? and ts.attributeCode='"
										+ productStock.getAttributeCode() + "'",
								productStock.getId(), product.getName(),
								productStock.getStoreId());
			}
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(ProductStock.class, list.get(0));
		}
	}

	public void save(ProductStock productStock) {
		jdbcTemplate
				.update("insert into t_product_stock(id,stock,alarmValue,productId,accountId,storeId,attributeCode,price,memberPrice,barCode,remarks,"
						+ "sales,hits,createTime,createId,recommended,shelves,type,marketPrice,costPrice,Details,sort,unitName,attributeName,promotionId) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,"
						+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?)", productStock.getId(),
						productStock.getStock(), productStock.getAlarmValue(),
						productStock.getProductId(),
						productStock.getAccountId(), productStock.getStoreId(),
						productStock.getAttributeCode(),
						productStock.getPrice(),productStock.getMemberPrice(), productStock.getBarCode(),
						productStock.getRemarks(), productStock.getSales(),
						productStock.getHits(), new Date(),
						productStock.getCreateId(),
						productStock.getRecommended(),
						productStock.getShelves(), productStock.getType(),
						productStock.getMarketPrice(),
						productStock.getCostPrice(), productStock.getDetails(),productStock.getSort(),productStock.getUnitName(),
						productStock.getAttributeName(),productStock.getPromotionId());
	}

	public void update(ProductStock productStock) {
		jdbcTemplate
				.update("update t_product_stock set stock=?,price=?,memberPrice=?,marketPrice=?,costPrice=?,"
						+ "alarmValue=?,barCode=?,remarks=?,attributeCode=?,attributeName=? ,shelves=?,"
						+ "details=?,sort=?,unitName=?,updateTime=? where id=?",
						productStock.getStock(), productStock.getPrice(),productStock.getMemberPrice(),
						productStock.getMarketPrice(),productStock.getCostPrice(),
						productStock.getAlarmValue(),
						productStock.getBarCode(), productStock.getRemarks(),
						productStock.getAttributeCode(),productStock.getAttributeName(),
						productStock.getShelves(), productStock.getDetails(),productStock.getSort(),productStock.getUnitName(),new Date(),
						productStock.getId());

	}

	public void deleteById(Long id) {
		jdbcTemplate.update("delete from t_product_stock  where id = ?", id);
	}

	public ProductStock findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select t.* "
								+ ",(select name from t_product tp where tp.id=t.productId) as productName"
								+ " from t_product_stock t where t.id=?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public List<ProductStock> findByProductId(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select t.*"
						+ " ,tp.name as productNamev "
						+ " from t_product_stock t,t_product tp "
						+ " where t.productId = tp.Id " + "and t.productId=? ",
				productId);
		return BeanUtils.toList(ProductStock.class, list);
	}
	public List<ProductStock> posFindByProductId(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select t.* "
						+ ",tp.name as productName "
						+ " from t_product_stock t,t_product tp "
						+ "where t.productId = tp.Id " + "and t.productId=? and (t.shelves = " +Constants.ProductStock.POS_UP + " or t.shelves = " + Constants.ProductStock.ALL_UP + ")" ,
				productId);
		return BeanUtils.toList(ProductStock.class, list);
	}
	
	public List<ProductStock> apiFindByProductId(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select t.* "
						+ ",tp.name as productName "
						+ " from t_product_stock t,t_product tp "
						+ "where t.productId = tp.Id " + "and t.productId=? and (t.shelves = " +Constants.ProductStock.APP_UP + " or t.shelves = " + Constants.ProductStock.ALL_UP + ")" ,
				productId);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStock> findByProductIdAndHaveStock(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
								+ "tp.name as productName,"
								+ "t.accountId,t.attributeCode,t.price,t.memberPrice,t.alarmValue "
								+ "from t_product_stock t,t_product tp "
								+ "where t.productId = tp.Id and (t.stock > 0 OR t.stock=-99 )"
								+ "and t.productId=?", productId);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<Product> findByCategoryId(Long categoryId) {
		// List<Map<String, Object>> list=jdbcTemplate
		// .queryForList(
		// "select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
		// +
		// "(select name from t_product tp where tp.id=t.productId) as productName,"
		// + "t.accountId,t.attributeCode,t.price,t.alarmValue "
		// + "from t_product_stock t where t.productId=?", productId);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_product tp where  tp.categoryId=? and tp.type=0 ",
				categoryId);
		
		return BeanUtils.toList(Product.class, list);
	}

	public ProductStock findByStockId(Long stockId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select t.*,"
								+ "(select name from t_product tp where tp.id=t.productId) as productName," 
								+ "(select id from t_product_image where productStockId = t.id limit 1) as imageId," 
								+ "t.accountId,t.attributeCode,t.price,t.memberPrice,t.attributeName " 
								+ "from t_product_stock t where t.id=? ",
						stockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}
	
	public ProductStock posFindByStockId(Long stockId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select "
								+ "(select name from t_product tp where tp.id=t.productId) as productName," 
								+ "(select id from t_product_image where productStockId = t.id limit 1) as imageId,"
								+ "t.accountId,t.attributeCode,t.price,t.memberPrice, "
								+ "(select name from t_product_category pc where pc.id=tp.categoryId) as categoryName,tp.categoryId " 
								+ "from t_product tp,t_product_stock t where t.productId=tp.id and t.id=? ",
						stockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public void saveProductImage(ProductImage productImage) {
		jdbcTemplate
				.update("insert into t_product_image(id,productStockId,suffix,sort,createTime) values(?,?,?,?,?)",
						productImage.getId(), productImage.getProductStockId(),
						productImage.getSuffix(), productImage.getSort(),new Date());

	}
	
	public void saveProductImageInfo(ProductImage productImage) {
		jdbcTemplate
				.update("insert into t_product_image(id,productId,productStockId,suffix,sort,createTime) values(?,?,?,?,?,?)",
						productImage.getId(),productImage.getProductId(), productImage.getProductStockId(),
						productImage.getSuffix(), productImage.getSort(),new Date());

	}

	public ProductImage findProductImage(ProductImage productImage) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,productStockId,suffix,sort from t_product_image where productStockId=? and sort=?",
						productImage.getProductStockId(),
						productImage.getSort());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductImage.class, list.get(0));
	}
	public ProductImage findImageByImageId(String imageId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,productStockId,suffix,sort from t_product_image where id=? ",
						imageId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductImage.class, list.get(0));
		
	}
	public List<ProductImage> findProductImageList(Long productStockId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,productStockId,suffix,sort,createTime from t_product_image where productStockId=? order by createTime desc",
						productStockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductImage.class, list);
	}

	public List<ProductImage> findImageByStockId(Long productStockId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,productStockId,suffix,sort from t_product_image where productStockId=? order by createTime desc",
						productStockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductImage.class, list);
	}

	/**
	 * 
	 * @param id
	 * @param stockNumber
	 * @return
	 */
	public boolean updateStockNumber(Long id, Double stockNumber) {
		String sql = "UPDATE t_product_stock SET stock = ? WHERE id = ? ";
		return jdbcTemplate.update(sql, stockNumber, id) >= 1 ? true : false;
	}

	/**
	 * 通过模糊的方式 attributeCode 查找
	 * 
	 * @param papms
	 * @param productId
	 * @param storeId
	 * @return
	 */
	public List<ProductStock> findByFuzzyAttributeCode(Long attributeCode,
			Long productId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "SELECT * FROM t_product_stock WHERE 1=1";
		StringBuffer sb = new StringBuffer();
		if (null != productId) {
			sb.append(" and productId = ?");
			parameters.add(productId);
		}
		if (null != storeId) {
			sb.append(" and storeId = ?");
			parameters.add(storeId);
		}

		sb.append(" and ( attributeCode = ? or attributeCode like ? or attributeCode like ? or attributeCode like ? )");
		parameters.add(attributeCode); // 属性值本身
		parameters.add(attributeCode + "-%"); // 以属性值开头
		parameters.add("%-" + attributeCode); // 以属性值结尾
		parameters.add("%-" + attributeCode + "-%"); // 属性值在中间

		logger.debug("findByFuzzyAttributeCode  sql is :" + sql + sb.toString());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql + sb.toString(), parameters.toArray());
		return BeanUtils.toList(ProductStock.class, list);
	}

	/**
	 * 通过不定项的 attributeCode 查找
	 * 
	 * @param papms
	 * @param productId
	 * @param storeId
	 * @return
	 */
	public List<ProductStock> findByFuzzyStockId(List<String> papms,
			Long productId, Long storeId) {
		StringBuffer sf = new StringBuffer();
		String sql = "SELECT * FROM t_product_stock WHERE 1=1 AND  productId = ? AND storeId = ?";
		if (papms != null && papms.size() >= 1) {
			sf.append("AND (");
			for (int i = 0; i < papms.size(); i++) {
				sf.append(" attributeCode like ? OR");
			}
			sf.setLength(sf.length() - 2);
			sf.append(")");
		}
		sql = sql + sf.toString();
		logger.debug("sql is :" + sql);
		papms.add(0, storeId + "");
		papms.add(0, productId + "");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				papms.toArray());
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStock> findByFuzzyAttributeCode(Long attributeCode,
			Long categoryId) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "SELECT t.* FROM t_product_stock t,t_product tp where t.productId = tp.Id ";

		StringBuffer sb = new StringBuffer();

		if (null != categoryId) {
			sb.append(" and categoryId = ?");
			parameters.add(categoryId);
		}

		sb.append(" and ( attributeCode = ? or attributeCode like ? or attributeCode like ? or attributeCode like ? )");
		parameters.add(attributeCode); // 属性值本身
		parameters.add(attributeCode + "-%"); // 以属性值开头
		parameters.add("%-" + attributeCode); // 以属性值结尾
		parameters.add("%-" + attributeCode + "-%"); // 属性值在中间

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql + sb.toString(), parameters.toArray());
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStock> byBarCodeFindProductStock(String barCode,
			Long storeId) {
		String sql = "SELECT p.* FROM  t_product_stock p WHERE  p.barCode = ? and storeId = ? order by p.id desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				barCode, storeId);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public ProductStock findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_product_stock t where storeId = ?", storeId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	/**
	 * 通过商品ID和标识区分符查询库存表中对应的数据
	 * 
	 * @param productId
	 *            商品ID
	 * @return 库存对象
	 */
	public ProductStock getByProductId(Long productId) {
		String sql = "select p.* from  t_product_stock p where p.productId = ? and p.type = 1 ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				productId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public Boolean update(ProductStock productStock, boolean b) {

		int updateRow = jdbcTemplate
				.update("update t_product_stock set stock = ?,alarmValue=?,attributeCode=?,price=?,barCode=?,remarks=?,sales=?,hits=?,updateTime=?,updateId=?,recommended=?,shelves=?"
						+ " where id=?", productStock.getStock(),
						productStock.getAlarmValue(),
						productStock.getAttributeCode(),
						productStock.getPrice(), productStock.getBarCode(),
						productStock.getRemarks(), productStock.getSales(),
						productStock.getHits(), new Date(),
						productStock.getUpdateId(),
						productStock.getRecommended(),
						productStock.getShelves(), productStock.getId());
		if (updateRow > 0) {
			return true;
		}
		return false;
	}

	public List<ProductImage> findDelProductImageList(Long id, String delImaStr) {
		List<Map<String, Object>> list=null;
		if("".equals(delImaStr)){
			list = jdbcTemplate
					.queryForList(
							"select id,productStockId,suffix,sort from t_product_image where productStockId=?", id);
		}else{
			list = jdbcTemplate
					.queryForList(
							"select id,productStockId,suffix,sort from t_product_image where productStockId=? and id not in ("
									+ delImaStr + ")", id);
		}
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(ProductImage.class, list);
	}

	public void delByImageId(String id) {
		jdbcTemplate.update("delete from t_product_image  where id = ?", id);

	}

	public Product findProduct(Product product) {
		if (product.getId() == null) {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(
					"select id,name,categoryId,storeId,type from t_product "
							+ " where name=? and storeId=? and categoryId=?",
					product.getName(), product.getStoreId(),
					product.getCategoryId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Product.class, list.get(0));
		} else {
			List<Map<String, Object>> list = jdbcTemplate
					.queryForList(
							"select id,name,categoryId,storeId,type from t_product "
									+ " where name=? and storeId=? and categoryId=? and id<>?",
							product.getName(), product.getStoreId(),
							product.getCategoryId(), product.getId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Product.class, list.get(0));
		}
	}

	public void save(Product product) {
		jdbcTemplate
				.update("insert into t_product(id,name,categoryId,accountId,storeId,type,brandId) "
						+ "values(?,?,?,?,?,?,?)", product.getId(),
						product.getName(), product.getCategoryId(),
						product.getAccountId(), product.getStoreId(),
						product.getType(),product.getBrandId());

	}

	public void update(Product product) {
		if(product.getType().intValue()==0){
			jdbcTemplate
			.update("update t_product_stock set attributeName = replace(attributeName,?,?) where productId = ?",
					product.getOldName(),product.getName(),product.getId());
		}else{
			jdbcTemplate
			.update("update t_product_stock set attributeName = ? where productId = ?",
					product.getName(),product.getId());
		}
		jdbcTemplate
				.update("update t_product set  name=?,categoryId=?,accountId=?,storeId=?,type=?,brandId=? where id=?",
						product.getName(), product.getCategoryId(),
						product.getAccountId(), product.getStoreId(),
						product.getType(),product.getBrandId(), product.getId());

	}

	public Page AppFindList(Page page, Long categoryId,Integer shelves, Long storeId) {
		// CategoryId < 0 表示是促销商品
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder sf = new StringBuilder();
		sf.append(" from t_product_stock ps LEFT JOIN t_product p ON p.id = ps.productId  ");
		
		String promotionNum = "";
		if (categoryId != null && categoryId.longValue() < 0L){
			sf.append(" inner join t_sales_promotion sp on ps.id = sp.stockId ");
			promotionNum = " sp.salesNum as promotionNum,sp.price as promotionPrice,";
		}
		
		sf.append(" where 1=1 ");
		
		if (categoryId != null) {
			if (categoryId.longValue() > 0L){
				sf.append(" and  p.categoryId = ? ");
				parameters.add(categoryId);
			}else {
				sf.append(" and  sp.salesPromotionCategoryId = ? ");
				parameters.add(-categoryId);
			}
		}
		if (storeId != null) {
			sf.append(" and  p.storeId = ? ");
			parameters.add(storeId);
		}
		if (shelves != null && categoryId.longValue() > 0L ) {
			//不是促销商品才要这个条件  
			sf.append(" and  ps.shelves = ? ");
			parameters.add(shelves);
		}
		
		sf.append(" order by ps.sort desc ");
		Integer total = jdbcTemplate.queryForObject(
				" select count(1) " + sf.toString(), Integer.class,
				parameters.toArray());
		page.setTotal(total);

		if (total.intValue() == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select ps.* ," + promotionNum + "  p.name as 'productName',(select id from t_product_image where productStockId = ps.id limit 1) as imageId " + sf.toString()
						+ "ORDER BY ps.sort is null limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}

	public List<ProductStock> byBarCodeFindProductStock1(String barCode,
			Long storeId, String name,Long categoryId) {
		String sql = "SELECT p.id,s.barCode,p.storeId FROM  t_product_stock s,t_product p WHERE s.productId=p.id  and  s.barCode = ? and s.storeId = ? and !(p.name =? and p.categoryId=?) order by p.id desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				barCode, storeId, name,categoryId);
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStock> findProductList(String name, Integer type,
			Long categoryId, Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.* from t_product_stock s,t_product p "
								+ " where s.productId=p.id and p.name=? and p.type=?  and p.categoryId=? and p.storeId=? ",
						name, type, categoryId, storeId);

		return BeanUtils.toList(ProductStock.class, list);
	}

	public ProductStock findByIdAndStoreId(Long stockId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();

		String data = "s.id,p.name as productName,p.accountId,p.categoryId , s.remarks, "
				+ "s.stock,s.alarmValue,s.productId,s.storeId,s.price,s.barCode,s.createTime,"
				+ "s.remarks,s.sales,s.hits,s.updateTime,s.recommended,s.shelves";

		querySQL.append(" from t_product p,t_product_stock s where p.id = s.productId ");

		// 商家
		if (null != storeId) {
			querySQL.append(" and s.storeId = ?");
			parameters.add(storeId);
		}

		// 库存ID
		if (null != stockId) {
			querySQL.append(" and s.id = ?");
			parameters.add(stockId);
		}

		return BeanUtils.toBean(ProductStock.class, jdbcTemplate.queryForMap(
				"select " + data + querySQL.toString(), parameters.toArray()));
	}

	public void dealSales(Long id, Double amount) {
		String sql = " update t_product_stock ps set ps.sales = ps.sales + "+amount.longValue()+" where id = ? ";
		jdbcTemplate.update(sql, id);
	}

	public Product findTypeByid(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,categoryId,storeId,type from t_product "
								+ " where  id=?",
						id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
		
	}

	public List<ProductStock> posFindByBarCode(String barCode,Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer();
		sf.append(" select t.weighing,t.id,tp.type,t.stock,t.alarmValue,t.barCode,t.productId,tp.name as productName,t.accountId,t.attributeCode,t.price,t.alarmValue,t.costPrice ,t.shelves,t.imageId ");
		sf.append(" FROM t_product_stock t inner JOIN t_product tp on t.productId = tp.Id");
		// sf.append(" INNER JOIN t_product_image img on img.productStockId = t.id ");
		sf.append(" where 1=1 and (t.shelves=0 or t.shelves=4) ");
		if (StringUtils.isNotBlank(barCode)) {
			sf.append(" and t.barCode=?");
			parameters.add(barCode);
		}
		if (storeId!=null) {
			sf.append(" and t.storeId=?");
			parameters.add(storeId);
		}
		sf.append(" group by t.id ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sf.toString(),parameters.toArray());
		return BeanUtils.toList(ProductStock.class, list);
	}

	public ProductStock posFindById(Long productStockId, Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
								+ "(select name from t_product tp where tp.id=t.productId) as productName,"
								+ "t.accountId,t.attributeCode,t.type,t.price,t.alarmValue,t.storeId,t.shelves,t.marketPrice,t.costPrice,t.details "
								+ ",t.remarks"
								+ " from t_product_stock t where t.id=? and t.storeId = ? ", productStockId,storeId);
		return list.isEmpty()?null:BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public void posUpdate(Long id, Double costPrice, Double price,Integer stock, Integer shelves) {
		logger.debug("update t_product_stock set costPrice = {},price = {} ,stock = {},shelves = {} where id = {}",costPrice,price,stock,shelves,id);
		String sql = "update t_product_stock set  costPrice = ? ,price = ? ,stock = ? ,shelves = ? where id = ?";
		jdbcTemplate.update(sql,costPrice,price,stock,shelves,id);
	}

	/**
	 * 根据属性值ID,取到并连接属性值名称
	 * @param attributeCode
	 * @return
	 */
	public String getAttributeValuesListJointValue(String attributeCode,String separator) {
		if( null == separator || "".equals(separator)){
			separator = "|";
		}
		String sql = "select GROUP_CONCAT(value SEPARATOR '" + separator + "') attributeValuesListJointValue from t_product_stock_attribute_value pav where pav.id in (" + attributeCode + ") limit 0, 30";
		String attributeValuesListJointValue = jdbcTemplate.queryForObject(sql, String.class);
		logger.debug("getAttributeValuesListJointValue --- >>> sql is {}",sql);
		return attributeValuesListJointValue;
	}

	public void updateShelves(Long id, Integer shelves) {
		String sql = "update t_product_stock set shelves = ? where id = ?";
		jdbcTemplate.update(sql,shelves,id);
	}

	public int checkProductStock(Long stockId,Integer stock) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_product_stock ps where ps.id = ? and ps.stock >= ? ");
		parameters.add(stockId);
		parameters.add(stock);
		int total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		logger.debug("stockId is {} , stock is {},total is {}",stockId,stock,total);
		return total;
	}

	public int getProductStock_stock(Long stockId) {
		int total =jdbcTemplate.queryForObject("select ps.stock from t_product_stock ps where ps.id = ? ", Integer.class,stockId);
		return total;
	}

	public Product findBrandByProduct(Product product) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select p.id,p.name,p.categoryId,p.storeId,p.type,p.brandId,(select name from t_brand b where b.id=p.brandId) as brandName from t_product p"
								+ " where  p.name=? and p.storeId=?",
						product.getName(),product.getStoreId());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Product.class, list.get(0));
	}

	public List<Product> findProductByBrandId(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select p.id,p.name,p.categoryId,p.storeId,p.type,p.brandId,(select name from t_brand b where b.id=p.brandId) as brandName from t_product p"
								+ " where  p.brandId=?",
						id);
		
		return BeanUtils.toList(Product.class, list);
	}

	public List<ProductStock> findAll(Long storeId, Long categoryId,
			String name, Integer alarmValueType, Integer shelves) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (storeId != null) {
			and.append(" and t.storeId =  ? ");
			parameters.add(storeId);
		}
		if (categoryId != null) {
			and.append(" and tp.categoryId =  ? ");
			parameters.add(categoryId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and tp.name like ?");
			parameters.add("%" + name + "%");
		}
		
		if (alarmValueType != null && !"".equals(alarmValueType)) {
			and.append(" and t.stock<=t.alarmValue and t.stock<>-99 ");
		}
		if (shelves != null && !"".equals(shelves)) {
			and.append(" and t.shelves=? ");
			parameters.add(shelves);
		}
		

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,"
						+ "tp.name as productName,(select name from t_store s where s.id=t.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=tp.categoryId) as categoryName, "
						+ "t.accountId,t.attributeCode,t.price,t.alarmValue,t.shelves,t.type,t.sort "
						+ "from t_product tp,t_product_stock t "
						+ "where t.productId = tp.Id ");
		sql.append(and.toString() + " order by t.sort desc, t.id desc ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		
		return BeanUtils.toList(ProductStock.class, list);
	}

	public List<ProductStock> getproductStockList(Integer alarmValueType,
			Integer shelves, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (storeId != null) {
			and.append(" and t.storeId =  ? ");
			parameters.add(storeId);
		}
		
		if (alarmValueType != null && !"".equals(alarmValueType)) {
			and.append(" and t.stock<=t.alarmValue and t.stock<>-99 ");
		}
		if (shelves != null && !"".equals(shelves)) {
			and.append(" and t.shelves=? ");
			parameters.add(shelves);
		}
		

		StringBuffer sql = new StringBuffer(
				"select t.id,t.stock,t.alarmValue,t.barCode,t.productId,t.sales,"
						+ "tp.name as productName,(select name from t_store s where s.id=t.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=tp.categoryId) as categoryName, "
						+ "t.accountId,t.attributeCode,t.price,t.alarmValue,t.shelves,t.type,t.sort "
						+ "from t_product tp,t_product_stock t "
						+ "where t.productId = tp.Id ");
		sql.append(and.toString() + " order by t.sort desc, t.id desc ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		
		return BeanUtils.toList(ProductStock.class, list);
	}


	public List<ProductStock> findProductList1(Long id, String name,
			Integer type, Long categoryId, Long storeId) {
		
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.* from t_product_stock s,t_product p "
								+ " where s.productId=p.id and s.id<>? and p.name=? and p.type=?  and p.categoryId=? and p.storeId=? ",
						id,name, type, categoryId, storeId);

		return BeanUtils.toList(ProductStock.class, list);
	}


	public ProductStock appFindByProductId(Long productId) {
		String sql = "select * from t_product_stock where productId = ? and (shelves = ? or shelves = ? ) LIMIT 0,1";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,productId,Constants.ProductStock.APP_UP,Constants.ProductStock.ALL_UP);
		return list.isEmpty()?null:BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public void posUpdate(Long productStockId, String productName, String attributeValuesListJointValue) {
		logger.debug("posUpdateAttributeName  --> productStockId is {} productName is {} <-->{}",productStockId,productName,attributeValuesListJointValue);
		jdbcTemplate.update( "update t_product_stock set attributeName = concat(?,'|',?) where id=?",productName,attributeValuesListJointValue, productStockId);
	}

	public ProductStock findByPromotionId(Long promotionId) {
		String querySQL = "select ps.* from t_product_stock ps where promotionId = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,promotionId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
	}

	public void batchDeleteByIds(List<Long> stockIds) {
		String sql = "delete from t_product_stock  where id = ?";
		List<Object[]> batchId=new ArrayList<Object[]>();
		for(Long id:stockIds){
			Object[] obj=new Object[1];
			obj[0]=id;
			batchId.add(obj);
		}
		jdbcTemplate.batchUpdate(sql, batchId);
		
	}
	public void batchUpdateAttributeName(List<Object[]> batchArgs) {
		String sql = "update t_product_stock set attributeName = ? where id = ? ";
		int[] batchInt = jdbcTemplate.batchUpdate(sql,batchArgs);
		if(batchArgs.size() - batchInt.length > 0 ){
			logger.debug("根据库存ID批量修改库存attributeName有失败数据:{}",batchArgs.toArray());
		}
	}
	
	/**
	 * 商家版查询商品管理列表
	 * @param storeId 商家ID(null则查询所有)
	 * @param brandId 品牌ID(null则查询所有)
	 * @param sort 排序规则(0:智能排序;1:销量最高;2:最新商品;3:最新入库)
	 * @param screening 筛选条件(0:全部商品;1:售卖中;2:未上架;3:库存预警)
	 * @param productName 关键字查询(商品名称模糊)
	 * @param Page page 分页对象
	 * @return Page或者null
	 */
	public Page apiShopGetProductManagerList(Long storeId, Long brandId, int sort, int screening, String productName,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(" from t_product p,t_product_stock ps");
		querySQL.append(" where p.id = ps.productId");
		
		if( null != storeId ){
			querySQL.append(" and p.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != brandId ){
			querySQL.append(" and p.brandId = ? ");
			parameters.add(brandId);
		}
		
		switch (screening) { // 筛选条件
			case 0: break; // 全部商品(不进行任何条件约束)
			case 1: 
				querySQL.append(" and (ps.shelves = ? or ps.shelves = ? ) ");
				parameters.add(Constants.ProductStock.APP_UP);
				parameters.add(Constants.ProductStock.ALL_UP);
				break; // 售卖中
			case 2:
				querySQL.append(" and (ps.shelves = ? or ps.shelves = ? )");
				parameters.add(Constants.ProductStock.APP_DOWN);
				parameters.add(Constants.ProductStock.ALL_DOWN);
				break; // 未上架
			case 3:
				querySQL.append(" and (ps.stock <> ? and ps.stock <= ps.alarmValue)");
				parameters.add(Constants.ProductStock.Store_Infinite);
				break; // 库存预警
			default: break; //默认也是全部商品
		}
		
		if( StringUtils.isNotBlank(productName) ){
			querySQL.append(" and instr(ps.attributeName,?)>0 ");
			parameters.add(productName);
		}
		
		switch (sort) { // 排序
			case 0: querySQL.append(" order by ps.sort desc, ps.id desc "); 
				break; // 默认排序
			case 1: querySQL.append(" order by ps.sales desc,ps.sort asc ");
				break; // 销量最高
			case 2: querySQL.append(" order by ps.createTime desc,ps.sort asc ");
				break; // 最新商品
			case 3: querySQL.append(" order by ps.updateTime desc,ps.sort asc ");
				break; // 最新入库
			default: querySQL.append(" order by ps.sort desc, ps.id desc "); break; // 默认排序
		}
		
		logger.debug("apiShopGetProductManagerList 查询语句为：{},参数列表为：{}",querySQL.toString(),parameters.toArray());
		
		Integer total = jdbcTemplate.queryForObject("select count(*) "+querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.insert(0," select p.id,ps.id,ps.barCode,ps.attributeName as productName,SUBSTR(attributeName,LOCATE('|',attributeName)+1) as attributeName,ps.price,ps.costPrice,ps.sales,ps.stock,DATE_FORMAT(ps.createTime,'%Y-%m-%d %T') as createTime,ps.sort, (select pi.id from t_product_image pi where pi.productStockId = ps.id limit 0,1) as productIcon ");

		querySQL.append(" limit ?,? ");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		if( null != list && list.size() > 0 ){
			for (Map<String, Object> map : list) {
				if( null != map.get("productIcon")){
					map.put("productIcon", FileServiceUtils.getFileUrl((String) map.get("productIcon")));
					continue;
				};
			}
		}
		
		page.setData(list);
		return page;
	}
	
	/**
	 * 查询商家的库存预警
	 * @param storeId 商家ID(null查询整个平台)
	 * @return 库存预警数
	 */
	public int apiShopGetAlarmValueCount(Long storeId) {
		String querySQL = null;
		List<Object> parameters = new ArrayList<Object>();
		if( null == storeId ){
			querySQL = "select count(*) from t_product_stock ps where (ps.stock <> ? and ps.stock <= ps.alarmValue) ";
			parameters.add(Constants.ProductStock.Store_Infinite);
		} else {
			querySQL = "select count(*) from t_product_stock ps where ps.storeId = ? and (ps.stock <> ? and ps.stock <= ps.alarmValue) ";
			parameters.add(storeId);
			parameters.add(Constants.ProductStock.Store_Infinite);
		}
		return jdbcTemplate.queryForObject(querySQL,Integer.class,parameters.toArray());
	}

	public int byBarCodeFindProductStock(Long storeId, String barCode, String id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select count(*) from  t_product_stock p where  p.barcode = ? and p.storeId = ? ");
		parameters.add(barCode);
		parameters.add(storeId);
		
		if(StringUtils.isNotBlank(id)){
			querySQL.append(" and ( p.promotionId is null or p.promotionId <> ? )");
			parameters.add(id);
		}
		
		return jdbcTemplate.queryForObject(querySQL.toString(),Integer.class,parameters.toArray());
	}
	
	public boolean apiShopPutStorageUpdate(Long id, Integer sumStock, Double costPrice) {
		String updateSQL = null;
		List<Object> parameters = new ArrayList<Object>();
		if( null == sumStock ){
			updateSQL = "update t_product_stock set costPrice = ? where id = ? ";
			parameters.add(costPrice);
			parameters.add(id);
		} else {
			updateSQL = "update t_product_stock set costPrice = ?,stock = ?  where id = ? ";
			parameters.add(costPrice);
			parameters.add(sumStock);
			parameters.add(id);
		}
		return jdbcTemplate.update(updateSQL,parameters.toArray()) > 0 ? true : false;
	}
	public boolean updateStockNumberAtReturnGoods(Long stockId, Double stockNumber) {
		String sql = "UPDATE t_product_stock SET stock = (if(stock = ?,? ,(stock+?) )), sales = (sales - ?) WHERE id = ? ";
		return jdbcTemplate.update(sql, Constants.ProductStock.Store_Infinite,Constants.ProductStock.Store_Infinite,stockNumber,stockNumber,stockId) >= 1 ? true : false;
	}
	public List<ProductStock> PosGetProductStockList(Long categoryId,
			String productName, String barCode) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.*,(select mobile from t_account a where a.id=s.accountId) as accountMobile from t_product_stock s,t_product p "
								+ " where s.productId=p.id  and p.categoryId=? and s.barCode=? and s.attributeName like '%"+productName+"%'",
						categoryId, barCode);

		return BeanUtils.toList(ProductStock.class, list);
	}
	public void deleteByProductId(Long productID) {
		jdbcTemplate.update("delete from t_product_image where productId = ? ",productID);		
	}
	public void deleteByProductStockId(Long productStockId) {
		jdbcTemplate.update("delete from t_product_image where productStockId = ? ",productStockId);
	}
}