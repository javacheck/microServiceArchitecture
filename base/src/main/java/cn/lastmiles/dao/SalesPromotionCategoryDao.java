package cn.lastmiles.dao;
/**
 * createDate : 2015年10月12日 上午11:17:57 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.SalesPromotionCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class SalesPromotionCategoryDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(SalesPromotionCategoryDao.class); 

	public Page list(Long storeId,String salesPromotionCategoryName,int salesPromotionCategoryType, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_sales_promotion_category r where 1=1 ");
		
		if( null != storeId ){
			querySQL.append(" and r.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( StringUtils.isNotBlank(salesPromotionCategoryName)){
			querySQL.append(" and instr(r.name,?) > 0 ");
			parameters.add(salesPromotionCategoryName);
		}
		
		if(salesPromotionCategoryType == 0 ){ // 传0则查折扣
			querySQL.append(" and r.discount is not null ");
		} else if(salesPromotionCategoryType == 1) { // 传1则查统一价格
			querySQL.append(" and r.amount is not null ");
		}
		
		logger.debug("SalesPromotionCategoryDao.list >> sql syntax : {} , parameter : {}",querySQL.toString(),parameters.toArray());
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class,parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		
		querySQL.append(" order by r.startDate desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0,"select r.*,(select name from t_store s where r.storeId = s.id ) as storeName ");
		
		page.setData(BeanUtils.toList(SalesPromotionCategory.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray())));
		return page;
	}

	public boolean save(SalesPromotionCategory salesPromotionCategory) {
		if( null == salesPromotionCategory ){
			return false;
		}
		String insertSQL = "insert into t_sales_promotion_category"
				+ " (id,name,storeId,amount,discount,buyNum,shipType,payType,useBalance,useCashGift,startDate,endDate,status,salesNum,share) "
				+ " values (? ,? ,? ,?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";
		int temp = jdbcTemplate.update(insertSQL, 
				salesPromotionCategory.getId(),salesPromotionCategory.getName(),
				salesPromotionCategory.getStoreId(),salesPromotionCategory.getAmount(),
				salesPromotionCategory.getDiscount(),salesPromotionCategory.getBuyNum(),
				salesPromotionCategory.getShipType(),salesPromotionCategory.getPayType(),
				salesPromotionCategory.getUseBalance(),salesPromotionCategory.getUseCashGift(),
				salesPromotionCategory.getStartDate(),salesPromotionCategory.getEndDate(),
				salesPromotionCategory.getStatus(),salesPromotionCategory.getSalesNum(),
				salesPromotionCategory.getShare());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean delete(Long storeId, Long id) {
		int temp = 0;
		// 先删除促销表中的此分类下的促销商品
		int temp_one = jdbcTemplate.update("delete from t_sales_promotion where storeId = ? and salesPromotionCategoryId = ?", storeId,id);
		if(temp_one > 0){
			// 再删除促销分类表中的此分类
			temp = jdbcTemplate.update("delete from t_sales_promotion_category where storeId = ? and id = ?", storeId,id);
		} else {
			new RuntimeException("delete t_sales_promotion failure --->>> t_sales_promotion_category-->>> delete");
		}
		if (temp > 0) {
			return true;
		} else {
			return false;
		} 
	}

	public boolean again(Long storeId, Long id) {
		int temp  = jdbcTemplate.update("update t_sales_promotion_category set status = 0 where storeId = ? and id = ?", storeId,id);
	
		if (temp > 0) {
			return true;
		} else {
			return false;
		} 
	}
	
	public Page findProductStockList(String productName, Long storeId,Long productCategoryId, Page page) {

		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_product_stock ps ");
		querySQL.append("inner join t_product p on p.id = ps.productId ");
		querySQL.append("inner join t_store s on s.id = ps.storeId  ");
		querySQL.append(" where 1=1 ");
//		querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion sp ) ");
		// 只查询出已结束的促销商品
		querySQL.append(" and ps.id not in (select sp.stockId from t_sales_promotion_category spc inner join t_sales_promotion sp on spc.id = sp.salesPromotionCategoryId  where spc.status <> ? ) ");
		parameters.add(Constants.SalesPromotionCategory.STATUS_RUNED);
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

		logger.debug("SalesPromotionCategoryDao.findProductStockList >> sql syntax : {} , parameter : {}",querySQL.toString(),parameters.toArray());
		
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

	public SalesPromotionCategory findById(Long salesPromotionCategoryId) {
		String querySQL = "select spc.*,(select name from t_store s where s.id = spc.storeId ) as storeName from t_sales_promotion_category spc where spc.id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL, salesPromotionCategoryId);
		if( null == list || list.isEmpty()){
			return null;
		}
		
		return BeanUtils.toBean(SalesPromotionCategory.class, list.get(0));
	}

	public boolean udpate(SalesPromotionCategory salesPromotionCategory) {
		if( null == salesPromotionCategory ){
			return false;
		}
		String updateSQL = "update t_sales_promotion_category "
				+ "set storeId = ?,name = ? , amount = ? ,discount = ? ,buyNum = ? , shipType = ? ,payType = ? , useBalance = ? , useCashGift = ? ,startDate = ? ,endDate = ? , salesNum = ? , share = ?  where id = ?";
		int temp = jdbcTemplate.update(updateSQL,
				salesPromotionCategory.getStoreId(),salesPromotionCategory.getName(),
				salesPromotionCategory.getAmount(),salesPromotionCategory.getDiscount(),
				salesPromotionCategory.getBuyNum(),salesPromotionCategory.getShipType(),
				salesPromotionCategory.getPayType(),salesPromotionCategory.getUseBalance(),
				salesPromotionCategory.getUseCashGift(),salesPromotionCategory.getStartDate(),
				salesPromotionCategory.getEndDate(),salesPromotionCategory.getSalesNum(),
				salesPromotionCategory.getShare(), salesPromotionCategory.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<SalesPromotionCategory> getByStoreId(Long storeId){
		return BeanUtils.toList(SalesPromotionCategory.class, 
				jdbcTemplate.queryForList("select * from t_sales_promotion_category where storeId = ? and status = ?", storeId,1));
	}

	public void updateStatus(Long id, Integer status) {
		String sql = "update t_sales_promotion_category set status = ? where id = ?";
		jdbcTemplate.update(sql, status,id);
	}

	public boolean checkName(Long id,Long storeId,String name) {
		List<Object> parameters = new ArrayList<Object>();
		
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_sales_promotion_category spc where spc.storeId = ? and (spc.status = ? or spc.status = ?) and spc.name = ? ");
		parameters.add(storeId);
		parameters.add(Constants.SalesPromotionCategory.STATUS_NOT_RUNNING); // 未开始
		parameters.add(Constants.SalesPromotionCategory.STATUS_RUNED); // 已开始
		parameters.add(name);
		
		if( null != id ){
			querySQL.append(" and spc.id <> ?");
			parameters.add(id);
		}
		int total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		if(total > 0 ){
			return true;
		}
		return false;
	}
}