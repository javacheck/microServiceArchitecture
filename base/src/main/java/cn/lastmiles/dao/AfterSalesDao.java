package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AfterSales;
import cn.lastmiles.bean.AfterSalesType;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

/**
 * 
 * lastUpdate 2016/10/20
 * @author shaoyikun
 *
 */
@Repository
public class AfterSalesDao {

	private static final Logger logger = LoggerFactory.getLogger(AfterSalesDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<AfterSalesType> getAllAfterSalesType() {
		String sql = "SELECT * FROM t_after_sales_type";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list.isEmpty() ? null : BeanUtils.toList(AfterSalesType.class, list);
	}

	public Page list(String storeId, String orderId, String productName, Long categoryId, Long afterSalesType,
			String accountId, String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" FROM t_after_sales afsa WHERE 1 = 1 ");

		if (StringUtils.isNotBlank(storeId) && !"0".equals(storeId)) {
			querySQL.append(" AND afsa.storeId IN (" + storeId + ")");
		}

		if (StringUtils.isNotBlank(orderId)) {
			querySQL.append(" AND afsa.orderId = ? ");
			parameters.add(orderId);
		}

		if (StringUtils.isNotBlank(productName)) { // 模糊
			querySQL.append(" AND afsa.productName LIKE ?");
			parameters.add("%" + productName + "%");
		}

		if (null != categoryId) {
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate
					.queryForList("SELECT id FROM t_product_category WHERE path LIKE ? ", "%" + categoryId + "%");
			List<ProductCategory> pcList = BeanUtils.toList(ProductCategory.class, temp);
			StringBuilder sb = new StringBuilder(20);
			boolean flag = false;
			for (ProductCategory productCategory : pcList) {
				if (flag) {
					sb.append(",");
				}
				sb.append(productCategory.getId());
				flag = true;
			}
			// ------------------------------------

			logger.debug("查询的商品分类是：{}", sb.toString());

			querySQL.append(" AND afsa.productCategoryId IN(" + sb.toString() + ")");
		}

		if (null != afterSalesType) {
			querySQL.append(" AND afsa.afterSalesTypeId = ? ");
			parameters.add(afterSalesType);
		}

		if (StringUtils.isNotBlank(accountId)) {
			querySQL.append(" AND afsa.accountId = ? ");
			parameters.add(accountId);
		}

		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" AND afsa.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" AND afsa.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		logger.debug("sql is {}, parameters is {}", querySQL.toString(), parameters.toArray());

		Integer total = jdbcTemplate.queryForObject("SELECT COUNT(1) " + querySQL.toString(), Integer.class,
				parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0,
				"SELECT afsa.*,(SELECT name FROM t_product_category pc WHERE pc.id = afsa.productCategoryId) AS productCategoryName,(SELECT mobile FROM t_account ac WHERE ac.id = afsa.accountId) AS username,(SELECT name FROM t_after_sales_type afsaty WHERE afsaty.id = afsa.afterSalesTypeId) AS afterSalesTypeName,(SELECT source FROM t_order od WHERE afsa.orderId = od.id) AS source ");
		querySQL.append(" ORDER BY afsa.createdTime DESC LIMIT ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(list);
		return page;
	}
	
	public Page listWithoutLimit(String storeId, String orderId, String productName, Long categoryId, Long afterSalesType,
			String accountId, String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" FROM t_after_sales afsa WHERE 1 = 1 ");

		if (StringUtils.isNotBlank(storeId) && !"0".equals(storeId)) {
			querySQL.append(" AND afsa.storeId IN (" + storeId + ")");
		}

		if (StringUtils.isNotBlank(orderId)) {
			querySQL.append(" AND afsa.orderId = ? ");
			parameters.add(orderId);
		}

		if (StringUtils.isNotBlank(productName)) { // 模糊
			querySQL.append(" AND afsa.productName LIKE ?");
			parameters.add("%" + productName + "%");
		}

		if (null != categoryId) {
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate
					.queryForList("SELECT id FROM t_product_category WHERE path LIKE ? ", "%" + categoryId + "%");
			List<ProductCategory> pcList = BeanUtils.toList(ProductCategory.class, temp);
			StringBuilder sb = new StringBuilder(20);
			boolean flag = false;
			for (ProductCategory productCategory : pcList) {
				if (flag) {
					sb.append(",");
				}
				sb.append(productCategory.getId());
				flag = true;
			}
			// ------------------------------------

			logger.debug("查询的商品分类是：{}", sb.toString());

			querySQL.append(" AND afsa.productCategoryId IN(" + sb.toString() + ")");
		}

		if (null != afterSalesType) {
			querySQL.append(" AND afsa.afterSalesTypeId = ? ");
			parameters.add(afterSalesType);
		}

		if (StringUtils.isNotBlank(accountId)) {
			querySQL.append(" AND afsa.accountId = ? ");
			parameters.add(accountId);
		}

		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" AND afsa.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" AND afsa.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		logger.debug("sql is {}, parameters is {}", querySQL.toString(), parameters.toArray());

		Integer total = jdbcTemplate.queryForObject("SELECT COUNT(1) " + querySQL.toString(), Integer.class,
				parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.insert(0,
				"SELECT afsa.*,(SELECT name FROM t_product_category pc WHERE pc.id = afsa.productCategoryId) AS productCategoryName,(SELECT mobile FROM t_account ac WHERE ac.id = afsa.accountId) AS username,(SELECT name FROM t_after_sales_type afsaty WHERE afsaty.id = afsa.afterSalesTypeId) AS afterSalesTypeName,(SELECT source FROM t_order od WHERE afsa.orderId = od.id) AS source ");
		querySQL.append(" ORDER BY afsa.createdTime DESC");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(list);
		return page;
	}

	public void save(AfterSales as) {
		jdbcTemplate.update(
				"INSERT INTO t_after_sales(id,storeId,storeName,orderId,productId,"
						+ "productName,barCode,unitName,productCategoryId,price,afterSalesTypeId,"
						+ "amount,remark,createdTime,accountId,orderItemId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				as.getId(), as.getStoreId(), as.getStoreName(), as.getOrderId(), as.getProductId(), as.getProductName(),
				as.getBarCode(), as.getUnitName(), as.getProductCategoryId(), as.getPrice(), as.getAfterSalesTypeId(),
				as.getAmount(), as.getRemark(), as.getCreatedTime(), as.getAccountId(), as.getOrderItemId());
	}
}
