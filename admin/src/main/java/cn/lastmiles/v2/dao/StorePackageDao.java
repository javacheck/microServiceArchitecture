/**
 * createDate : 2016年10月25日下午2:29:46
 */
package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.StoreServicePackage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class StorePackageDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder("from t_store_service_package ssp where 1=1 ");
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and ssp.name like ? ");
			parameters.add("%"+name+"%");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class,parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, "select ssp.*,(select a.mobile from t_account a where a.id = ssp.accountId ) as operator ");
		
		querySQL.append(" limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		
		page.setData(BeanUtils.toList(StoreServicePackage.class, list));
		return page;
	}

	public Page findProductStockList(String productName, Long storeId, Long productCategoryId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where 1 = 1 ");
//		querySQL.append(" and ps.promotionId is null "); // 组合选择商品时不能选择已经是组合商品的商品记录
//		parameters.add(Constants.ProductStock.TYPE_OFF);
//			querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion sp ) ");
		// 只查询出已结束的促销商品
		//querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion_category spc inner join t_sales_promotion sp on spc.id = sp.salesPromotionCategoryId  where spc.status <> ? ) ");
		//parameters.add(Constants.SalesPromotionCategory.STATUS_RUNED);
		// querySQL.append(" and ps.shelves = '0' "); // 只显示上架的
		
		if (null != storeId ) {
			querySQL.append(" and ps.storeId = ? ");
			parameters.add(storeId);
		}

		if( null != productCategoryId ){
			querySQL.append(" and p.categoryId = ?");
			parameters.add(productCategoryId);
		}

		if (StringUtils.isNotBlank(productName)) {
			querySQL.append(" and instr(p.name,?) > 0");
			parameters.add(productName);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		querySQL.append(" order by ps.storeId desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0,"select ps.*,p.name as productName,s.name as storeName,s.mobile as 'store.mobile' ");

		page.setData(BeanUtils.toList(ProductStock.class, jdbcTemplate.queryForList(querySQL.toString() , parameters.toArray())));

		return page;
	}
}