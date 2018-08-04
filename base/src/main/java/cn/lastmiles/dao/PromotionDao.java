package cn.lastmiles.dao;

/**
 * createDate : 2015年11月2日下午4:31:45
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.PromotionProduct;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class PromotionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory
			.getLogger(PromotionDao.class);

	public Page list(Long storeId, String promotionName, int promotionType,
			int promotionStatus, String startDate, String endDate, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_promotion p where p.storeId = ? ");
		parameters.add(storeId);

		if (StringUtils.isNotBlank(promotionName)) {
			querySQL.append(" and instr(p.name,?) > 0 ");
			parameters.add(promotionName);
		}

		if (Constants.Promotion.TYPE_OR_STATUS_ALL != promotionType) {
			querySQL.append(" and p.type = ? ");
			parameters.add(promotionType);
		}

		if (Constants.Promotion.TYPE_OR_STATUS_ALL != promotionStatus) {
			querySQL.append(" and p.status = ? ");
			parameters.add(promotionStatus);
		}

		if (StringUtils.isNotBlank(startDate)) {
			querySQL.append(" and p.startDate >= ? ");
			parameters.add(startDate);
		}

		if (StringUtils.isNotBlank(endDate)) {
			querySQL.append(" and p.endDate <= ? ");
			parameters.add(endDate);
		}

		Integer total = jdbcTemplate.queryForObject("select count(*) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		querySQL.append(" order by p.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(0, "select p.* ");

		page.setData(BeanUtils.toList(
				Promotion.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray())));
		return page;
	}

	public boolean checkName(Long id, Long storeId, String name) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(
				" select count(*) from t_promotion p where p.storeId = ? and p.name = ? ");
		parameters.add(storeId);
		parameters.add(name);

		if (null != id) {
			querySQL.append(" and p.id <> ?");
			parameters.add(id);
		}
		int total = jdbcTemplate.queryForObject(querySQL.toString(),
				Integer.class, parameters.toArray());
		if (total > 0) {
			return true;
		}
		return false;
	}

	public Page findProductStockList(String productName, Long storeId,
			Long productCategoryId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where 1=1 ");
		querySQL.append(" and ps.promotionId is null "); // 选择商品时不能选择已经是组合商品的商品记录
		// querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion sp ) ");
		// 只查询出已结束的促销商品
		// querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion_category spc inner join t_sales_promotion sp on spc.id = sp.salesPromotionCategoryId  where spc.status <> ? ) ");
		// parameters.add(Constants.SalesPromotionCategory.STATUS_RUNED);
		// querySQL.append(" and ps.shelves = '0' "); // 只显示上架的

		if (null != storeId) {
			querySQL.append(" and ps.storeId = ? ");
			parameters.add(storeId);
		}

		if (null != productCategoryId) {
			
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+productCategoryId+"%");
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
		
			querySQL.append(" and p.categoryId in("+sb.toString()+")");
		}

		if (StringUtils.isNotBlank(productName)) {
			querySQL.append(" and instr(p.name,?) > 0");
			parameters.add(productName);
		}

		Integer total = jdbcTemplate.queryForObject("select count(*) "
				+ querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		querySQL.append(" order by ps.storeId desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(
				0,
				"select ps.*,p.name as productName,s.name as storeName,s.mobile as 'store.mobile' ");
		page.setData(BeanUtils.toList(
				ProductStock.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray())));

		return page;
	}

	public boolean save(Promotion promotion, int promotion_type) {
		int temp = 0;
		StringBuffer insertSQL = new StringBuffer();
		switch (promotion_type) {
		case Constants.Promotion.TYPE_FIRST_ORDER:
			insertSQL.append("insert into t_promotion");
			insertSQL
					.append(" (id,name,storeId,amount,startDate,endDate,shared,status,type,scope) ");
			insertSQL.append(" values (? ,? ,? ,?, ? ,? ,? ,?,?,?)");
			logger.debug("insertSQL is {} ,promotion is {}",
					insertSQL.toString(), promotion);
			temp = jdbcTemplate.update(insertSQL.toString(), promotion.getId(),
					promotion.getName(), promotion.getStoreId(),
					promotion.getAmount(), promotion.getStartDate(),
					promotion.getEndDate(), promotion.getShared(),
					promotion.getStatus(), promotion.getType(),
					promotion.getScope());
			break;
		case Constants.Promotion.TYPE_FULL_SUBTRACT:
			insertSQL.append("insert into t_promotion");
			insertSQL
					.append(" (id,name,storeId,`condition`,startDate,endDate,shared,status,type,scope) ");
			insertSQL.append(" values (? ,? ,? ,?, ? ,? ,? ,?,?,?)");
			temp = jdbcTemplate.update(insertSQL.toString(), promotion.getId(),
					promotion.getName(), promotion.getStoreId(),
					promotion.getCondition(), promotion.getStartDate(),
					promotion.getEndDate(), promotion.getShared(),
					promotion.getStatus(), promotion.getType(),
					promotion.getScope());
			break;
		case Constants.Promotion.TYPE_DISCOUNT:
			insertSQL.append("insert into t_promotion");
			insertSQL
					.append(" (id,name,storeId,`condition`,discount,total,startDate,endDate,shared,status,type,scope) ");
			insertSQL.append("  values (? ,? ,? ,?, ? ,? ,? ,?,?,?,?,?)");
			logger.debug("insertSQL is {} ,promotion is {}", insertSQL,
					promotion);
			temp = jdbcTemplate.update(insertSQL.toString(), promotion.getId(),
					promotion.getName(), promotion.getStoreId(),
					promotion.getCondition(), promotion.getDiscount(),
					promotion.getTotal(), promotion.getStartDate(),
					promotion.getEndDate(), promotion.getShared(),
					promotion.getStatus(), promotion.getType(),
					promotion.getScope());
			break;
		case Constants.Promotion.TYPE_COMBINATION:
			insertSQL.append("insert into t_promotion");
			insertSQL
					.append(" (id,name,storeId,imageId,price,total,startDate,endDate,shared,status,type,scope) ");
			insertSQL.append(" values (? ,? ,? ,? , ? , ? ,? ,? ,?,?,?,?)");
			temp = jdbcTemplate.update(insertSQL.toString(), promotion.getId(),
					promotion.getName(), promotion.getStoreId(),
					promotion.getImageId(), promotion.getPrice(),
					promotion.getTotal(), promotion.getStartDate(),
					promotion.getEndDate(), promotion.getShared(),
					promotion.getStatus(), promotion.getType(),
					promotion.getScope());
			break;
		default:
			break;
		}
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int delete(Long id, Long storeId) {
		jdbcTemplate.update(
				"delete from t_promotion_product  where promotionId = ? ", id);
		int temp = jdbcTemplate.update(
				"delete from t_promotion  where id = ? and storeId = ?", id,
				storeId);
		return temp;
	}

	public Promotion findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select p.* from t_promotion p where p.id = ? ", id);
		return list.isEmpty() ? null : BeanUtils.toBean(Promotion.class,
				list.get(0));
	}

	public boolean update(Promotion promotion, int promotion_type) {

		int temp = 0;
		StringBuffer updateSQL = new StringBuffer();
		switch (promotion_type) {
		case Constants.Promotion.TYPE_FIRST_ORDER:
			updateSQL.append(" update t_promotion ");
			updateSQL
					.append(" set name = ? , amount = ? , startDate = ? , endDate = ? , shared = ? , status = ?,scope = ? ");
			updateSQL.append(" where id = ? ");
			temp = jdbcTemplate.update(updateSQL.toString(),
					promotion.getName(), promotion.getAmount(),
					promotion.getStartDate(), promotion.getEndDate(),
					promotion.getShared(), promotion.getStatus(),
					promotion.getScope(), promotion.getId());
			break;
		case Constants.Promotion.TYPE_FULL_SUBTRACT:
			updateSQL.append(" update t_promotion ");
			updateSQL
					.append(" set name = ? , `condition` = ? , startDate = ? , endDate = ? , shared = ? , status = ?,scope = ? ");
			updateSQL.append(" where id = ? ");
			temp = jdbcTemplate.update(updateSQL.toString(),
					promotion.getName(), promotion.getCondition(),
					promotion.getStartDate(), promotion.getEndDate(),
					promotion.getShared(), promotion.getStatus(),
					promotion.getScope(), promotion.getId());
			break;
		case Constants.Promotion.TYPE_DISCOUNT:
			updateSQL.append(" update t_promotion ");
			updateSQL
					.append(" set name = ? , `condition` = ? ,discount = ?, total = ? , startDate = ? , endDate = ? , shared = ? , status = ?,scope = ? ");
			updateSQL.append(" where id = ? ");
			temp = jdbcTemplate.update(updateSQL.toString(),
					promotion.getName(), promotion.getCondition(),
					promotion.getDiscount(), promotion.getTotal(),
					promotion.getStartDate(), promotion.getEndDate(),
					promotion.getShared(), promotion.getStatus(),
					promotion.getScope(), promotion.getId());
			break;
		case Constants.Promotion.TYPE_COMBINATION:
			updateSQL.append(" update t_promotion ");
			updateSQL
					.append(" set name = ? , imageId = ? ,price = ?, total = ? , startDate = ? , endDate = ? , shared = ? , status = ?,scope = ? ");
			updateSQL.append(" where id = ? ");
			temp = jdbcTemplate.update(updateSQL.toString(),
					promotion.getName(), promotion.getImageId(),
					promotion.getPrice(), promotion.getTotal(),
					promotion.getStartDate(), promotion.getEndDate(),
					promotion.getShared(), promotion.getStatus(),
					promotion.getScope(), promotion.getId());
			break;
		default:
			break;
		}
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<PromotionProduct> findProductById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select pp.* from t_promotion_product pp where pp.promotionId = ? and pp.productStockId <> -1 ",
						id);
		return list.isEmpty() ? null : BeanUtils.toList(PromotionProduct.class,
				list);
	}

	@Transactional
	public boolean saveBatch(List<Object[]> batchArgs, Long promotionId) {
		// 先删除
		jdbcTemplate.update(
				"delete from t_promotion_product where promotionId = ?",
				promotionId);

		String sql = "insert into t_promotion_product(promotionId,productStockId) values (? ,?)";
		int[] temp = jdbcTemplate.batchUpdate(sql, batchArgs);
		if (temp.length == batchArgs.size()) {
			return true;
		} else {
			new RuntimeException("batch insert t_promotion_product Failure");
			return false;
		}
	}

	public List<ProductStock> findProductStockList(Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where ps.storeId = ? ");

		parameters.add(storeId);

		querySQL.insert(0,
				"select ps.id,ps.attributeCode,p.name as productName,ps.price");
		List<ProductStock> productStockList = BeanUtils.toList(
				ProductStock.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray()));
		return productStockList.isEmpty() ? null : productStockList;
	}

	public List<ProductStock> findProductStockList(Long storeId, String stockIdS) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where ps.storeId = ? ");
		parameters.add(storeId);

		if (StringUtils.isNotBlank(stockIdS)) {
			querySQL.append(" and ps.id in (" + stockIdS + ")");
		}

		querySQL.insert(0,
				"select ps.id,ps.attributeCode,p.name as productName,ps.price");
		List<ProductStock> productStockList = BeanUtils.toList(
				ProductStock.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray()));
		// return productStockList.isEmpty() ? null : productStockList;
		return productStockList;
	}

	public List<Promotion> findByStoreId(Long storeId) {
		String sql = "SELECT * FROM t_promotion p WHERE p.startDate <= ? AND p.endDate > ? AND p.`status` = ?  and p.storeId = ?";
		return BeanUtils
				.toList(Promotion.class, jdbcTemplate.queryForList(sql,
						new Date(), new Date(), Constants.Promotion.STATUS_ON,
						storeId));
	}

	public Promotion appFindDiscount(Long storeId, Long productStockId,
			Integer scope) {
		String sql = "SELECT p.* FROM t_promotion p LEFT JOIN t_promotion_product pp ON p.id = pp.promotionId "
				+ " WHERE p.type = ? and p.status = ? AND p.startDate <= ? AND p.endDate >= ? AND p.storeId= ? and (p.total > 0 or p.total = '-1' )  AND ( pp.productStockId = '-1' OR pp.productStockId = ? ) and (p.scope = 0 or p.scope = ? )";
		if (scope == null) {
			scope = 0;
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				Constants.Promotion.TYPE_DISCOUNT,
				Constants.Promotion.STATUS_ON, new Date(), new Date(), storeId,
				productStockId, scope);
		return list.isEmpty() ? null : BeanUtils.toBean(Promotion.class,
				list.get(0));
	}

	public List<Promotion> checkTimeInterleaving(Long accountStoreId, Long id,
			int promotion_type, String startTime, String endTime) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();

		querySQL.append(" select p.id from t_promotion p where p.storeId = ? and p.type = ? and p.status = ? ");
		parameters.add(accountStoreId);
		parameters.add(promotion_type);
		parameters.add(Constants.Promotion.STATUS_ON);

		if (null != id) { // 修改时
			querySQL.append(" and p.id <> ? ");
			parameters.add(id);
		}

		if (StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			// 第一种情况(开始时间比定点的开始时间小,但是结束时间比定点的结束时间大,既时间段全部覆盖了定点的时间范围)
			querySQL.append(" and ");
			querySQL.append(" ( p.startDate >= ? and p.endDate <= ? ");
			parameters.add(startTime);
			parameters.add(endTime);

			// 第二种情况(结束时间比定点的开始时间大,但是结束时间比定点的结束时间小,既结束时间在定点的时间范围内有交集)
			querySQL.append(" or p.startDate <= ? and p.endDate >= ? ");
			parameters.add(endTime);
			parameters.add(endTime);

			// 第三种情况(开始时间比定点的开始时间大,但是开始时间比定点的结束时间小,既开始时间在定点的时间范围内有交集)
			querySQL.append(" or p.startDate <= ? and p.endDate >= ? ");
			parameters.add(startTime);
			parameters.add(startTime);

			querySQL.append(" )");
		}

		List<Promotion> promotionList = BeanUtils.toList(
				Promotion.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray()));
		return promotionList.isEmpty() ? null : promotionList;
	}

	public List<PromotionProduct> getPromotionProduct(String linkString) {
		List<PromotionProduct> promotionProductList = BeanUtils
				.toList(PromotionProduct.class,
						jdbcTemplate
								.queryForList("select pp.* from t_promotion_product pp where pp.promotionId in ("
										+ linkString + ")"));
		return promotionProductList.isEmpty() ? null : promotionProductList;
	}

	public boolean checkFirstOrFull(Long accountStoreId, Long id,
			int promotion_type) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" select count(*) from t_promotion p where p.storeId = ? and p.type = ? and p.status = ?");
		parameters.add(accountStoreId);
		parameters.add(promotion_type);
		parameters.add(Constants.Promotion.STATUS_ON);

		if (null != id) { // 修改时
			querySQL.append(" and p.id <> ? ");
			parameters.add(id);
		}

		int total = jdbcTemplate.queryForObject(querySQL.toString(),
				Integer.class, parameters.toArray());
		if (total > 0) {
			return true;
		}
		return false;
	}

	public void lossStock(Long id, Integer amount) {
		jdbcTemplate.update(" update t_promotion set total =  total -" + amount
				+ " where id = ?", id);
	}

	public Page findCombinationProductStockList(String productName,
			Long storeId, Long productCategoryId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where ps.type = ? ");
		querySQL.append(" and ps.promotionId is null "); // 组合选择商品时不能选择已经是组合商品的商品记录
		parameters.add(Constants.ProductStock.TYPE_OFF);
		// querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion sp ) ");
		// 只查询出已结束的促销商品
		// querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion_category spc inner join t_sales_promotion sp on spc.id = sp.salesPromotionCategoryId  where spc.status <> ? ) ");
		// parameters.add(Constants.SalesPromotionCategory.STATUS_RUNED);
		// querySQL.append(" and ps.shelves = '0' "); // 只显示上架的

		if (null != storeId) {
			querySQL.append(" and ps.storeId = ? ");
			parameters.add(storeId);
		}

		if (null != productCategoryId) {
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+productCategoryId+"%");
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
		
			querySQL.append(" and p.categoryId in("+sb.toString()+")");
		}

		if (StringUtils.isNotBlank(productName)) {
			querySQL.append(" and instr(p.name,?) > 0");
			parameters.add(productName);
		}

		Integer total = jdbcTemplate.queryForObject("select count(*) "
				+ querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		querySQL.append(" order by ps.storeId desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(
				0,
				"select ps.*,p.name as productName,s.name as storeName,s.mobile as 'store.mobile' ");
		page.setData(BeanUtils.toList(
				ProductStock.class,
				jdbcTemplate.queryForList(querySQL.toString(),
						parameters.toArray())));

		return page;
	}

	/**
	 * pos端的折扣信息
	 * 
	 * @param storeId
	 * @param stockIds
	 * @return
	 */
	public PromotionStock findDiscountProductStockForPos(Long storeId,
			List<Long> stockIds) {
		String sql = "select * from t_promotion where storeId = ? and type = 3 and status = 1 and startDate <= ? and endDate >= ? and scope in (0,2) ";
		Date now = new Date();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				storeId, now, now);
		if (list.isEmpty()) {
			return null;
		}

		Promotion promotion = BeanUtils.toBean(Promotion.class, list.get(0));
		logger.debug("promotion id = {}", promotion.getId());

		Integer obj = jdbcTemplate.queryForObject("select count(1) from t_promotion_product where promotionId = ? and productStockId = -1 ", Integer.class, promotion.getId());
		PromotionStock pStock = new PromotionStock();
		
		if (obj.intValue() == 1) {
			pStock.setDiscount(promotion.getDiscount());
			pStock.setPromotionId(promotion.getId());
			pStock.setStockIds(stockIds);
			return pStock;
		}
		String ids = null;

		for (Long stockId : stockIds) {
			if (ids == null) {
				ids = stockId.toString();
			} else {
				ids += "," + stockId;
			}
		}
		logger.debug("ids id = {}", ids);
		sql = "select productStockId from t_promotion_product where promotionId = ? and productStockId in ( "
				+ ids + ")";

		List<Long> idList = jdbcTemplate.queryForList(sql, Long.class,
				promotion.getId());

		pStock.setDiscount(promotion.getDiscount());
		pStock.setPromotionId(promotion.getId());
		if (!idList.isEmpty()) {
			pStock.setStockIds(idList);
		}

		return pStock;
	}

	public Promotion findFullSubstractForPos(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_promotion where type = ? and storeId = ? and `status` = ? and endDate >= ? and (scope = 0 or scope = 2)", 
				Constants.Promotion.TYPE_FULL_SUBTRACT,storeId,Constants.Promotion.STATUS_ON,new Date());
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(Promotion.class, list.get(0));
	}
}