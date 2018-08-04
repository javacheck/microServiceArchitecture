/**
 * createDate : 2016年4月11日上午11:47:49
 */
package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.PromotionProduct;
import cn.lastmiles.bean.movie.RoomPackage;
import cn.lastmiles.bean.movie.RoomPackageProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class RoomPackageDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer querySQL = new StringBuffer(" from t_movie_room_package mrp where 1=1 ");
		
		if( null != storeId){
			querySQL.append(" and mrp.storeId = ?");
			parameters.add(storeId);
		}
		
		Integer total = jdbcTemplate.queryForObject( "select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		querySQL.insert(0, "select mrp.*,(select a.mobile from t_account a where mrp.accountId = a.id) as createName,(select a.mobile from t_account a where mrp.updatedId = a.id) as updateName  ");
		querySQL.append(" order by mrp.id desc limit ?,? ");

		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(RoomPackage.class, list));
		
		return page;
	}

	public int deleteById(Long storeId, Long id) {
		if( deleteByRoomPackageId(id) > 0 ){
			return jdbcTemplate.update("delete from t_movie_room_package where storeId = ? and id = ? ",storeId,id);			
		}
		return 0;
	}

	public int deleteByRoomPackageId(Long id) {
		return jdbcTemplate.update("delete from t_movie_room_package_product_stock where roomPackageId = ? ",id);
	}
	
	public List<ProductStock> findProductStockList(Long storeId,String stockIdS) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where ps.storeId = ? ");
		parameters.add(storeId);
		
		if( StringUtils.isNotBlank(stockIdS) ){
			querySQL.append(" and ps.id in (" + stockIdS + ")");
		}
		
		querySQL.insert(0,"select ps.id,ps.attributeCode,p.name as productName,ps.price");
		List<ProductStock> productStockList = BeanUtils.toList(ProductStock.class, jdbcTemplate.queryForList(querySQL.toString() , parameters.toArray()));
//		return productStockList.isEmpty() ? null : productStockList;
		return productStockList;
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

	public boolean checkName(Long id, Long storeId, String name) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_movie_room_package p where p.storeId = ? and p.name = ? ");
		parameters.add(storeId);
		parameters.add(name);
		
		if( null != id ){
			querySQL.append(" and p.id <> ?");
			parameters.add(id);
		}
		int total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		if(total > 0 ){
			return true;
		}
		return false;
	}

	public boolean save(RoomPackage roomPackage) {
		int temp = 0;
		StringBuffer insertSQL = new StringBuffer();
		insertSQL.append("insert into t_movie_room_package ");
		insertSQL.append(" (id,price,createdTime,accountId,name,memo,storeId,duration) ");
		insertSQL.append(" values (? ,? ,? ,?, ? ,? ,? ,?)");
		temp = jdbcTemplate.update(insertSQL.toString(), 
				roomPackage.getId(),roomPackage.getPrice(),
				new Date(),
				roomPackage.getAccountId(),
				roomPackage.getName(),roomPackage.getMemo(),
				roomPackage.getStoreId(),roomPackage.getDuration()
				);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(RoomPackage roomPackage) {
		int temp = 0;
		StringBuffer updateSQL = new StringBuffer();
		updateSQL.append(" update t_movie_room_package ");
		updateSQL.append(" set name = ? , duration = ? , updatedTime = ? , updatedId = ? , memo = ? , price = ? ");
		updateSQL.append(" where id = ? and storeId = ? ");
		temp = jdbcTemplate.update(updateSQL.toString(), 
				roomPackage.getName(),roomPackage.getDuration(),
				new Date(),roomPackage.getUpdatedId(),
				roomPackage.getMemo(),roomPackage.getPrice(),
				roomPackage.getId(),roomPackage.getStoreId()
				);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean saveBatch(List<Object[]> batchArgs,Long roomPackageId) {
		// 先删除
		jdbcTemplate.update("delete from t_movie_room_package_product_stock where roomPackageId = ?",roomPackageId);
		
		String sql = "insert into t_movie_room_package_product_stock(roomPackageId,productStockId,amount) values (? ,?,?)";
		int[] temp = jdbcTemplate.batchUpdate(sql, batchArgs);
		if (temp.length == batchArgs.size()) {
			return true;
		} else {
			new RuntimeException("batch insert t_movie_room_package_product_stock Failure");
			return false;
		}
	}

	public RoomPackage findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.* from t_movie_room_package p where p.id = ? ", id);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toBean(RoomPackage.class, list.get(0));
		}
		return null;
	}

	public List<RoomPackageProductStock> findProductById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select pp.* from t_movie_room_package_product_stock pp where pp.roomPackageId = ? ", id);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(RoomPackageProductStock.class, list );
		}
		return null; 
	}

	public List<RoomPackage> findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select p.* from t_movie_room_package p where p.storeId = ? ", storeId);
		return BeanUtils.toList(RoomPackage.class, list);
	}
	
}
