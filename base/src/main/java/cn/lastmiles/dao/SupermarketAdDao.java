package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.SupermarketAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

/**
 * createDate : 2015年8月18日 下午4:14:30
 */

@Repository
public class SupermarketAdDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_supermarket_ad r where 1=1 ");

		if (storeId != null) {
			querySQL.append(" and r.storeId = ? ");
			parameters.add(storeId);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select *,(select name from t_store s where s.id = r.storeId) as storeName,(select name from t_product_category pc where pc.id = r.productCategoryId) as productCategoryName "
								+ querySQL.toString()
								+ " order by r.id desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(SupermarketAd.class, list));
		return page;
	}

	public boolean save(SupermarketAd supermarketAd) {
		String sql = "insert into t_supermarket_ad(id,storeId,imageId,productCategoryId,position,createTime) "
				+ "values (?,? ,? ,? ,?,?)";
		int temp = jdbcTemplate.update(sql, supermarketAd.getId(),
				supermarketAd.getStoreId(), supermarketAd.getImageId(),
				supermarketAd.getProductCategoryId(),
				supermarketAd.getPosition(), new Date());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(SupermarketAd supermarketAd) {
		String sql = "update t_supermarket_ad set storeId=?, imageId =?, productCategoryId = ? , position = ?, createTime = ? "
				+ " where id = ?";
		int temp = jdbcTemplate.update(sql, supermarketAd.getStoreId(),
				supermarketAd.getImageId(),
				supermarketAd.getProductCategoryId(),
				supermarketAd.getPosition(), new Date(), supermarketAd.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<SupermarketAd> findListById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select *,(select s.name from t_store s where s.id = t.storeId) as storeName from t_supermarket_ad t where id = ? ",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toList(SupermarketAd.class, list);
	}

	public boolean delete(Long id, Long storeId) {
		int temp = jdbcTemplate.update(
				"delete from t_supermarket_ad  where id = ? and storeId = ? ",
				id, storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查找距离最近的商家主广告
	 * 
	 * @return
	 */
	public List<SupermarketAd> findNearest(Double lat, Double lnt) {
		String sql = "select sai.imageId as storeAdLogo, ad.imageId,c.`name` as productCategoryName,s.logo as storeLogo,ad.productCategoryId,ad.storeId,s.name as storeName,s.address as storeAddress, s.shipAmount as storeShipAmount,s.businessTime as storeBusinessTime,s.phone as shopPhone, " 
				+ " s.payType,s.minAmount,s.freeShipAmount,s.shipType "
				+ " from t_supermarket_ad ad "
				+ " INNER JOIN t_store s on ad.storeId = s.id "
				+ " INNER JOIN t_product_category c on ad.productCategoryId = c.id " 
				+ " left join t_store_ad_image sai on ad.storeId = sai.storeId and sai.type = 0 "
				+ " where ad.position = 0 and ad.storeId = ( "
				+ "SELECT id from t_store where shopTypeId = 0 and theodolitic(longitude,latitude,?,?) <= 2000000 ORDER BY theodolitic(longitude,latitude,?,?) LIMIT 1) order by sort desc";
		return BeanUtils.toList(SupermarketAd.class, jdbcTemplate.queryForList(sql,lnt,lat, lnt,lat));
	}

	public int checkDataHave(Long id,Long storeId, Long productCategoryId, Integer position) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select count(1) from t_supermarket_ad where storeId = ? and productCategoryId = ? and position = ?");
		parameters.add(storeId);
		parameters.add(productCategoryId);
		parameters.add(position);
		
		if( null != id ){
			querySQL.append(" and id <> ? ");
			parameters.add(id);
		}
		int number = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		return number;
	}
}