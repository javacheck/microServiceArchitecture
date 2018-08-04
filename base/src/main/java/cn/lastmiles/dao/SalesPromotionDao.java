package cn.lastmiles.dao;
/**
 * createDate : 2015年8月19日 下午3:03:13 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import cn.lastmiles.bean.SalesPromotion;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class SalesPromotionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(SalesPromotionDao.class);

	public Page list(Long storeId,Long salesPromotionCategoryId,Long productCategoryId,String productName, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from  t_sales_promotion r ");
		querySQL.append(" inner join");
		querySQL.append(" (select p.name as productName,ps.id as pId ,p.categoryId ,ps.attributeCode from t_product_stock  ps left join t_product p on p.id = ps.productId) z ");
		querySQL.append(" on z.pId =  r.stockId ");
		querySQL.append(" inner join t_sales_promotion_category spc on r.salesPromotionCategoryId = spc.id");
		querySQL.append(" where 1=1");

		if ( null != storeId ) {
			querySQL.append(" and r.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != salesPromotionCategoryId ){
			querySQL.append(" and r.salesPromotionCategoryId = ?");
			parameters.add(salesPromotionCategoryId);
		}
		
		if( null != productCategoryId ){
			querySQL.append(" and z.categoryId = ?");
			parameters.add(productCategoryId);
		}
		
		if( StringUtils.isNotBlank(productName) ){
			querySQL.append(" and instr(z.productName,?) > 0");
			parameters.add(productName);
		}

		logger.debug("SalesPromotionDao.list >> sql syntax : {} , parameter : {}",querySQL.toString(),parameters.toArray());
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.append(" order by r.storeId desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0,"select r.*,spc.startDate as startDate ,(select name from t_store s where s.id = r.storeId) as storeName,z.productName,z.attributeCode ");
		
		page.setData(BeanUtils.toList(SalesPromotion.class, jdbcTemplate.queryForList(querySQL.toString() , parameters.toArray())));
		return page;
	}

	public boolean delete(Long id) {
		int temp = jdbcTemplate.update("delete from t_sales_promotion where id = ? ", id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public Boolean save(List<Object[]> batchArgs) {
		String sql = "insert into  t_sales_promotion(id,storeId,stockId,salesPromotionCategoryId,createTime,salesNum,originalPrice,price,shelves) values (? ,? ,?,?,?,?,?,?,?)";
		int[] temp = jdbcTemplate.batchUpdate(sql, batchArgs);
		if (temp.length == batchArgs.size()) {
			return true;
		} else {
			new RuntimeException("batch insert t_sales_promotion Failure");
			return false;
		}
	}

	public Integer findAmountByStoreId(Long storeId) {
		return jdbcTemplate.queryForObject(
				"select count(1) from t_sales_promotion where storeId = ?",
				Integer.class, storeId);
	}

	public boolean updatePrice(Long id, String price) {
		String sql = "update t_sales_promotion set price = ? where id = ?";
		int temp = jdbcTemplate.update(sql,price,id);
		if(temp > 0){
			return true;
		}
		return false;
	}
	public SalesPromotion getByStockId(Long stockId,Long categoryId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_sales_promotion where stockId = ? and salesPromotionCategoryId = ?", stockId,categoryId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(SalesPromotion.class, list.get(0));
	}
	
	public void updateSalesNum(int num,Long stockId,Long categoryId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select salesNum from t_sales_promotion where stockId = ? and salesPromotionCategoryId = ?", stockId,categoryId);
		if (!list.isEmpty()){
			if (Integer.valueOf(list.get(0).get("salesNum").toString()).intValue() != -1){
				jdbcTemplate.update("update t_sales_promotion set salesNum = salesNum - ? where stockId = ? and salesPromotionCategoryId = ?", num,stockId,categoryId);
			}
		}
	}

	public List<SalesPromotion> findBySalesPromotionCategoryId(Long salesPromotionCategoryId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from  t_sales_promotion t where salesPromotionCategoryId = ?",salesPromotionCategoryId);
		return BeanUtils.toList(SalesPromotion.class, list);
	}

	public boolean updateSalesNum(Long id, String salesNum) {
		String sql = "update t_sales_promotion set salesNum = ? where id = ?";
		int temp = jdbcTemplate.update(sql,salesNum,id);
		if(temp > 0){
			return true;
		}
		return false;
	}
}