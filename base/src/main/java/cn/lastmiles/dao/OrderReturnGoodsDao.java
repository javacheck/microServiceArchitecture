/**
 * createDate : 2016年4月28日下午5:43:46
 */
package cn.lastmiles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.OrderReturnGoods;
import cn.lastmiles.bean.OrderReturnGoodsRecord;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class OrderReturnGoodsDao {
	private static final Logger logger = LoggerFactory.getLogger(OrderReturnGoodsDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public void save(OrderReturnGoods orderReturnGoods) {
		JdbcUtils.save(orderReturnGoods);
	}
	
	public void saveRecord(OrderReturnGoodsRecord orderReturnGoodsRecord) {
		JdbcUtils.save(orderReturnGoodsRecord);
	}

	public List<OrderReturnGoods> findByOrderId(Long orderId) {
		String sql = "SELECT * FROM t_order_returnGoods WHERE orderId = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,orderId);
		if ( list.isEmpty() ){
			return null;
		}
		return BeanUtils.toList(OrderReturnGoods.class, list);
	}

	public Page list(String storeId,String orderId, String productName, String barcode, Long categoryId,String beginTime,String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" from t_order_returnGoods_record orgr where 1=1 ");
		
		if( StringUtils.isNotBlank(storeId) && !"0".equals(storeId) ){
			querySQL.append(" and orgr.storeId in ("+storeId+")");
		}
		
		if( StringUtils.isNotBlank(orderId) ){
			querySQL.append(" and orgr.orderId = ? ");
			parameters.add(orderId);
		}
		
		if( StringUtils.isNotBlank(productName) ){ // 模糊
			querySQL.append(" and orgr.productName like ?");
			parameters.add("%"+productName+"%");
		}
		
		if( StringUtils.isNotBlank(barcode) ){
			querySQL.append(" and orgr.barCode = ? ");
			parameters.add(barcode);
		}
		
		if( null != categoryId ){
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+categoryId+"%");
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
		
			querySQL.append(" and orgr.categoryId in("+sb.toString()+")");
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and orgr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and orgr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		logger.debug("sql is {}, parameters is {}",querySQL.toString(),parameters.toArray());	
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() ,Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select orgr.*,(select name from t_product_category pc where pc.id = orgr.categoryId) as categoryName,(select name from t_store p where p.id = orgr.storeId) as storeName ");
		
		querySQL.append(" order by orgr.createdTime desc limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		//page.setData(BeanUtils.toList(OrderReturnGoodsRecord.class, list));
		page.setData(list);

		return page;
	}
	
	public Page list(String storeId,Integer sort,String productName, String barcode, Long categoryId,Integer dateType,String beginTime,String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" from t_order_returnGoods_record orgr where 1=1 ");
		
		if( StringUtils.isNotBlank(storeId) && !"0".equals(storeId) ){
			querySQL.append(" and orgr.storeId in ("+storeId+")");
		}
		
		if( StringUtils.isNotBlank(productName) ){ // 模糊
			querySQL.append(" and orgr.productName like ?");
			parameters.add("%"+productName+"%");
		}
		
		if( StringUtils.isNotBlank(barcode) ){
			querySQL.append(" and orgr.barCode = ? ");
			parameters.add(barcode);
		}
		
		if( null != categoryId ){
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+categoryId+"%");
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
		
			querySQL.append(" and orgr.categoryId in("+sb.toString()+")");
		}
		
		if( null != dateType){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			switch (dateType) {
				case 0: // 今日
					querySQL.append(" and orgr.createdTime >= ? and orgr.createdTime <= ? ");
					parameters.add( sdf.format(new Date()) + " 00:00:00");
					parameters.add( sdf.format(new Date()) + " 23:59:59");
					break;
				case 1: // 本月
					SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
					String month = ym.format(new Date());
					querySQL.append(" and date_format(orgr.createdTime, '%Y-%m') >= ? ");
					parameters.add(month);
					break;
				case 2: // 上月
					Calendar c2 = new GregorianCalendar();  
			        c2.setTime(new Date());//当前日期  
			        c2.add(Calendar.MONTH, -1);//减去1月  
			        SimpleDateFormat yymm = new SimpleDateFormat("yyyy-MM");
			        querySQL.append(" and date_format(orgr.createdTime, '%Y-%m') >= ? and date_format(orgr.createdTime, '%Y-%m') < ? ");
					parameters.add(yymm.format(c2.getTime()));
					parameters.add(yymm.format(new Date()));
					break;
				case 3: // 本年
					Calendar c3 = new GregorianCalendar();  
			        c3.setTime(new Date()); // 当前日期  
			        c3.add(Calendar.YEAR, 0); // 当年  
			        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
			        querySQL.append(" and date_format(orgr.createdTime, '%Y') >= ? ");
					parameters.add(yy.format(c3.getTime()));
					break;
				default:
					querySQL.append(" and orgr.createdTime >= ? and orgr.createdTime <= ? ");
					parameters.add( sdf.format(new Date()) + " 00:00:00");
					parameters.add( sdf.format(new Date()) + " 23:59:59");
					break;
			}
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and orgr.createdTime >= ?");
			parameters.add(beginTime);
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and orgr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		querySQL.append(" group by orgr.stockId ");
		
		System.out.println(querySQL.toString());
		Integer total = jdbcTemplate.queryForObject("select count(*) from ( select orgr.returnGoodsId " + querySQL.toString() + ") as stat" ,Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select orgr.*,sum(orgr.price) as price,sum(orgr.returnNumber) as returnNumber,sum(orgr.returnPrice) as returnPrice,(select name from t_product_category pc where pc.id = orgr.categoryId) as categoryName,(select name from t_store p where p.id = orgr.storeId) as storeName ");
		if( null != sort ){
			switch (sort) {
			case 1:
				querySQL.append(" order by returnNumber ");
				break;
			case 2:
				querySQL.append(" order by returnNumber desc ");
				break;
			case 3:
				querySQL.append(" order by returnPrice ");
				break;
			case 4:
				querySQL.append(" order by returnPrice desc ");
				break;
			default:
				querySQL.append(" order by returnNumber desc ");
				break;
			}
		}
		
		querySQL.append(" limit ?,?");
		

		logger.debug("sql is {}, parameters is {}",querySQL.toString(),parameters.toArray());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		//page.setData(BeanUtils.toList(OrderReturnGoodsRecord.class, list));
		page.setData(list);

		return page;
	}

	public Page findAll(String storeId, String orderId, String productName,
			String barcode, Long categoryId, String beginTime, String endTime,
			Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" from t_order_returnGoods_record orgr where 1=1 ");
		
		if( StringUtils.isNotBlank(storeId) && !"0".equals(storeId) ){
			querySQL.append(" and orgr.storeId in ("+storeId+")");
		}
		
		if( StringUtils.isNotBlank(orderId) ){
			querySQL.append(" and orgr.orderId = ? ");
			parameters.add(orderId);
		}
		
		if( StringUtils.isNotBlank(productName) ){ // 模糊
			querySQL.append(" and orgr.productName like ?");
			parameters.add("%"+productName+"%");
		}
		
		if( StringUtils.isNotBlank(barcode) ){
			querySQL.append(" and orgr.barCode = ? ");
			parameters.add(barcode);
		}
		
		if( null != categoryId ){
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+categoryId+"%");
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
		
			querySQL.append(" and orgr.categoryId in("+sb.toString()+")");
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and orgr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and orgr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		logger.debug("sql is {}, parameters is {}",querySQL.toString(),parameters.toArray());	
		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString() ,Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select orgr.*,(select name from t_product_category pc where pc.id = orgr.categoryId) as categoryName,(select name from t_store p where p.id = orgr.storeId) as storeName ");
		
		querySQL.append(" order by orgr.createdTime desc limit ?,?");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(OrderReturnGoodsRecord.class, list));

		return page;
	}

	public Page findStatisticalAll(String storeId, Integer sort,
			String productName, String barcode, Long categoryId,
			Integer dateType, String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" from t_order_returnGoods_record orgr where 1=1 ");
		
		if( StringUtils.isNotBlank(storeId) && !"0".equals(storeId) ){
			querySQL.append(" and orgr.storeId in ("+storeId+")");
		}
		
		if( StringUtils.isNotBlank(productName) ){ // 模糊
			querySQL.append(" and orgr.productName like ?");
			parameters.add("%"+productName+"%");
		}
		
		if( StringUtils.isNotBlank(barcode) ){
			querySQL.append(" and orgr.barCode = ? ");
			parameters.add(barcode);
		}
		
		if( null != categoryId ){
			// 因新商品的修改,现在返回给POS端的只有第一级分类,而根据第一级分类的ID要获取到其下子分类的所有商品,故在原有的基础上加入一层根据分类ID获取其下分类ID的处理(2016.07.10)
			List<Map<String, Object>> temp = jdbcTemplate.queryForList("select id from t_product_category where path like ? ","%"+categoryId+"%");
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
		
			querySQL.append(" and orgr.categoryId in("+sb.toString()+")");
		}
		
		if( null != dateType){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			switch (dateType) {
				case 0: // 今日
					querySQL.append(" and orgr.createdTime >= ? and orgr.createdTime <= ? ");
					parameters.add( sdf.format(new Date()) + " 00:00:00");
					parameters.add( sdf.format(new Date()) + " 23:59:59");
					break;
				case 1: // 本月
					SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
					String month = ym.format(new Date());
					querySQL.append(" and date_format(orgr.createdTime, '%Y-%m') >= ? ");
					parameters.add(month);
					break;
				case 2: // 上月
					Calendar c2 = new GregorianCalendar();  
			        c2.setTime(new Date());//当前日期  
			        c2.add(Calendar.MONTH, -1);//减去1月  
			        SimpleDateFormat yymm = new SimpleDateFormat("yyyy-MM");
			        querySQL.append(" and date_format(orgr.createdTime, '%Y-%m') >= ? and date_format(orgr.createdTime, '%Y-%m') < ? ");
					parameters.add(yymm.format(c2.getTime()));
					parameters.add(yymm.format(new Date()));
					break;
				case 3: // 本年
					Calendar c3 = new GregorianCalendar();  
			        c3.setTime(new Date()); // 当前日期  
			        c3.add(Calendar.YEAR, 0); // 当年  
			        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
			        querySQL.append(" and date_format(orgr.createdTime, '%Y') >= ? ");
					parameters.add(yy.format(c3.getTime()));
					break;
				default:
					querySQL.append(" and orgr.createdTime >= ? and orgr.createdTime <= ? ");
					parameters.add( sdf.format(new Date()) + " 00:00:00");
					parameters.add( sdf.format(new Date()) + " 23:59:59");
					break;
			}
		}
		
		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" and orgr.createdTime >= ?");
			parameters.add(beginTime);
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" and orgr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		querySQL.append(" group by orgr.stockId ");
		
		System.out.println(querySQL.toString());
		Integer total = jdbcTemplate.queryForObject("select count(*) from ( select orgr.returnGoodsId " + querySQL.toString() + ") as stat" ,Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, " select orgr.*,sum(orgr.price) as price,sum(orgr.returnNumber) as returnNumber,sum(orgr.returnPrice) as returnPrice,(select name from t_product_category pc where pc.id = orgr.categoryId) as categoryName,(select name from t_store p where p.id = orgr.storeId) as storeName ");
		if( null != sort ){
			switch (sort) {
			case 1:
				querySQL.append(" order by returnNumber ");
				break;
			case 2:
				querySQL.append(" order by returnNumber desc ");
				break;
			case 3:
				querySQL.append(" order by returnPrice ");
				break;
			case 4:
				querySQL.append(" order by returnPrice desc ");
				break;
			default:
				querySQL.append(" order by returnNumber desc ");
				break;
			}
		}
		
		querySQL.append(" limit ?,?");
		

		logger.debug("sql is {}, parameters is {}",querySQL.toString(),parameters.toArray());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(OrderReturnGoodsRecord.class, list));

		return page;
	}
	
}
