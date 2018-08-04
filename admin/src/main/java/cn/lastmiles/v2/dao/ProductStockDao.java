package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository("cn.lastmiles.v2.dao.ProductStockDao")
public class ProductStockDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page findAllPage(String storeIdString, String name,
			String barCode, Integer sort, String brandName, Long categoryId,
			Integer alarmType, Integer shelves, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and ps.storeId in( " +storeIdString+" ) ");
		}
		if (categoryId != null) {
			and.append(" and (p.categoryId =  ? or pc.path like ?  ) ");
			parameters.add(categoryId);
			parameters.add("%" + categoryId + "%");
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and (p.name like ? or p.shortName like ? ) ");
			parameters.add("%" + name + "%");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(barCode)) {
			and.append(" and ps.barCode = ?");
			parameters.add(barCode);
		}
		if (StringUtils.isNotBlank(brandName)) {
			and.append(" and b.name like ?");
			parameters.add("%" + brandName + "%");
		}
		if (alarmType != null) {
			and.append(" and ps.stock<=ps.alarmValue and ps.stock<>-99 ");
		}
		if (shelves != null) {
			and.append(" and ps.shelves=? ");
			parameters.add(shelves);
		}
		if (sort != null) {
			if(sort.intValue()==0){
				and.append(" order by (ps.stock<=ps.alarmValue and ps.stock<>-99) desc,(case when ps.shelves = 1 or ps.shelves = 3   or ps.shelves = 5 THEN 9999999999 ELSE ps.shelves end) asc,(case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==1){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==2){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) desc ");
			}else if(sort.intValue()==3){
				and.append(" order by ps.alarmValue asc ");
			}else if(sort.intValue()==4){
				and.append(" order by ps.alarmValue desc ");
			}else if(sort.intValue()==5){
				and.append(" order by ps.costPrice asc ");
			}else if(sort.intValue()==6){
				and.append(" order by ps.costPrice desc ");
			}else if(sort.intValue()==7){
				and.append(" order by ps.price asc ");
			}else if(sort.intValue()==8){
				and.append(" order by ps.price desc ");
			}else if(sort.intValue()==9){
				and.append(" order by ps.memberPrice asc ");
			}else if(sort.intValue()==10){
				and.append(" order by ps.memberPrice desc ");
			}else if(sort.intValue()==99){
				and.append(" order by ps.productId desc ");
			}
			
		}
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id left join t_product_category pc on p.categoryId=pc.id "
				+ "where ps.productId = p.id " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select ps.id,ps.stock,ps.alarmValue,ps.weighing,ps.remarks,ps.returnGoods,ps.barCode,ps.attributeName,ps.productId,(select name from t_product_unit u where u.id=ps.unitId) as unitName,"
						+ "p.name as productName,p.storeId,(select name from t_store s where s.id=ps.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=p.categoryId) as categoryName, "
						+ "ps.accountId,ps.attributeCode,ps.price,ps.marketPrice,ps.memberPrice,ps.costPrice,ps.alarmValue,b.name as brandName,ps.shelves,ps.type,ps.sort "
						+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id left join t_product_category pc on p.categoryId=pc.id "
						+ "where ps.productId = p.id ");
		sql.append(and.toString() + "  limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}
	public Page findAllList(String storeIdString, String name,
			String barCode, Integer sort, String brandName, Long categoryId,
			Integer alarmType, Integer shelves, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and ps.storeId in( " +storeIdString+" ) ");
		}
		if (categoryId != null) {
			and.append(" and p.categoryId =  ? ");
			parameters.add(categoryId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and p.name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(barCode)) {
			and.append(" and ps.barCode = ?");
			parameters.add(barCode);
		}
		if (StringUtils.isNotBlank(brandName)) {
			and.append(" and b.name like ?");
			parameters.add("%" + brandName + "%");
		}
		if (alarmType != null) {
			and.append(" and ps.stock<=ps.alarmValue and ps.stock<>-99 ");
		}
		if (shelves != null) {
			and.append(" and ps.shelves=? ");
			parameters.add(shelves);
		}
		if (sort != null) {
			if(sort.intValue()==0){
				and.append(" order by (ps.stock<=ps.alarmValue and ps.stock<>-99) desc,(case when ps.shelves = 1 or ps.shelves = 3   or ps.shelves = 5 THEN 9999999999 ELSE ps.shelves end) asc,(case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==1){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==2){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) desc ");
			}else if(sort.intValue()==3){
				and.append(" order by ps.alarmValue asc ");
			}else if(sort.intValue()==4){
				and.append(" order by ps.alarmValue desc ");
			}else if(sort.intValue()==5){
				and.append(" order by ps.costPrice asc ");
			}else if(sort.intValue()==6){
				and.append(" order by ps.costPrice desc ");
			}else if(sort.intValue()==7){
				and.append(" order by ps.price asc ");
			}else if(sort.intValue()==8){
				and.append(" order by ps.price desc ");
			}else if(sort.intValue()==9){
				and.append(" order by ps.memberPrice asc ");
			}else if(sort.intValue()==10){
				and.append(" order by ps.memberPrice desc ");
			}
			
		}
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id "
				+ "where ps.productId = p.id and ps.stock>=0 " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select ps.id,ps.weighing,ps.stock,ps.alarmValue,ps.barCode,ps.attributeName,ps.productId,(select name from t_product_unit u where u.id=ps.unitId) as unitName,"
						+ "p.name as productName,p.storeId,(select name from t_store s where s.id=ps.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=p.categoryId) as categoryName, "
						+ "ps.accountId,ps.attributeCode,ps.price,ps.memberPrice,ps.costPrice,ps.alarmValue,b.name as brandName,ps.shelves,ps.type,ps.sort "
						+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id "
						+ "where ps.productId = p.id and ps.stock>=0 ");
		sql.append(and.toString() + ",ps.shelves asc  limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}
	public List<ProductStock> findProductStockList(String productStockIds) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer and = new StringBuffer();
		
		and.append(" and ps.id in( " +productStockIds+" ) ");
		
		
		
		StringBuffer sql = new StringBuffer(
				"select ps.id,ps.stock,ps.weighing,ps.alarmValue,ps.barCode,ps.attributeName,ps.productId,(select name from t_product_unit u where u.id=ps.unitId) as unitName,"
						+ "p.name as productName,p.storeId,(select name from t_store s where s.id=ps.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=p.categoryId) as categoryName, "
						+ "ps.accountId,ps.attributeCode,ps.price,ps.memberPrice,ps.costPrice,ps.alarmValue,b.name as brandName,ps.shelves,ps.type,ps.sort "
						+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id "
						+ "where ps.productId = p.id and ps.stock>=0 ");
		sql.append(and.toString() + " order by ps.id desc ");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		List<ProductStock> psList=BeanUtils.toList(ProductStock.class, list);
		return psList;
	}

	public ProductStock findProductStockById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_stock where id = ? ",
						id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(ProductStock.class, list.get(0));
		
	}
	public List<ProductStockAttributeValue> findAttributeValuesById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_stock_attribute_value where productStockId = ? ",
						id);
		return BeanUtils.toList(ProductStockAttributeValue.class, list);
		
	}
	public Page findAllocationList(String storeIdString, String name,
			String barCode, Integer sort, String brandName, Long categoryId,
			Integer alarmType, Integer shelves, Page page) {
List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and ps.storeId in( " +storeIdString+" ) ");
		}
		if (categoryId != null) {
			and.append(" and p.categoryId =  ? ");
			parameters.add(categoryId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and p.name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(barCode)) {
			and.append(" and ps.barCode = ?");
			parameters.add(barCode);
		}
		if (StringUtils.isNotBlank(brandName)) {
			and.append(" and b.name like ?");
			parameters.add("%" + brandName + "%");
		}
		if (alarmType != null) {
			and.append(" and ps.stock<=ps.alarmValue and ps.stock<>-99 ");
		}
		if (shelves != null) {
			and.append(" and ps.shelves=? ");
			parameters.add(shelves);
		}
		if (sort != null) {
			if(sort.intValue()==0){
				and.append(" order by (ps.stock<=ps.alarmValue and ps.stock<>-99) desc,(case when ps.shelves = 1 or ps.shelves = 3   or ps.shelves = 5 THEN 9999999999 ELSE ps.shelves end) asc,(case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==1){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) asc ");
			}else if(sort.intValue()==2){
				and.append(" order by (case when ps.stock = -99 THEN 9999999999 ELSE ps.stock end) desc ");
			}else if(sort.intValue()==3){
				and.append(" order by ps.alarmValue asc ");
			}else if(sort.intValue()==4){
				and.append(" order by ps.alarmValue desc ");
			}else if(sort.intValue()==5){
				and.append(" order by ps.costPrice asc ");
			}else if(sort.intValue()==6){
				and.append(" order by ps.costPrice desc ");
			}else if(sort.intValue()==7){
				and.append(" order by ps.price asc ");
			}else if(sort.intValue()==8){
				and.append(" order by ps.price desc ");
			}else if(sort.intValue()==9){
				and.append(" order by ps.memberPrice asc ");
			}else if(sort.intValue()==10){
				and.append(" order by ps.memberPrice desc ");
			}
			
		}
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id "
				+ "where ps.productId = p.id " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select ps.id,ps.weighing,ps.stock,ps.alarmValue,ps.barCode,ps.attributeName,ps.productId,(select name from t_product_unit u where u.id=ps.unitId) as unitName,"
						+ "p.name as productName,p.storeId,(select name from t_store s where s.id=ps.storeId) as storeName,"
						+ "(select name from t_product_category pc where pc.id=p.categoryId) as categoryName, "
						+ "ps.accountId,ps.attributeCode,ps.price,ps.memberPrice,ps.costPrice,ps.alarmValue,b.name as brandName,ps.shelves,ps.type,ps.sort "
						+ "from t_product_stock ps,t_product p left join t_brand b on p.brandId=b.id "
						+ "where ps.productId = p.id ");
		sql.append(and.toString() + ",ps.shelves asc  limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStock.class, list));
		return page;
	}

}
