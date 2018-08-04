package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cn.lastmiles.bean.StoreAdImage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

/**
 * createDate : 2015年8月25日 下午3:32:40 
 */

@Repository
public class StoreAdImageDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(Long storeId, Integer type, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_store_ad_image r where 1=1 ");

		if (storeId != null) {
			querySQL.append(" and r.storeId = ? ");
			parameters.add(storeId);
		}
		
		if (type != null) {
			querySQL.append(" and r.type = ? ");
			parameters.add(type);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
					"select *,(select name from t_store s where s.id = r.storeId) as storeName " + querySQL.toString() + " order by r.storeId desc limit ?,?",
					 parameters.toArray());
		page.setData(BeanUtils.toList(StoreAdImage.class, list));
		return page;
	}

	public boolean delete(Integer type, Long storeId) {
		int temp = jdbcTemplate.update("delete from t_store_ad_image  where type = ? and storeId = ? ", type, storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public StoreAdImage findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select *,(select s.name from t_store s where s.id = t.storeId) as storeName from t_store_ad_image t where storeId = ? ",
						storeId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(StoreAdImage.class, list.get(0));
	}

	public boolean save(StoreAdImage storeAdImage) {
		String sql = "insert into t_store_ad_image(storeId,imageId,type) "
				+ "values (? ,? ,?)";
		int temp = jdbcTemplate.update(sql,storeAdImage.getStoreId(),storeAdImage.getImageId(),storeAdImage.getType());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(StoreAdImage storeAdImage) {
		String sql = "update t_store_ad_image set imageId =? , type=? "
				+ " where storeId = ?";
		int temp = jdbcTemplate.update(sql, storeAdImage.getImageId(),storeAdImage.getType(),storeAdImage.getStoreId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
}