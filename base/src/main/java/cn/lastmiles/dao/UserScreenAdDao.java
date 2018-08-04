package cn.lastmiles.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserScreenAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class UserScreenAdDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<String> findImageByStoreId(Long storeId){
		return jdbcTemplate.queryForList("select imageId from t_user_screen_ad where storeId = ? ", String.class, storeId);
	}

	public Page findAll(Long storeId, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("storeId", storeId);
		Integer total = JdbcUtils.queryForObject("ad.findTotal", map,
				Integer.class);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		map.put("startNumber", page.getStart());
		map.put("pageSize", page.getPageSize());

		page.setData(JdbcUtils.queryForList("ad.findAllPage", map,
				UserScreenAd.class));

		return page;
	}

	public void saveUserScreenAd(UserScreenAd userScreenAd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", userScreenAd.getStoreId());
		map.put("imageId", userScreenAd.getImageId());
		JdbcUtils.save("t_user_screen_ad", map);
	}

	public int deleteByImageId(String imageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("imageId", imageId);
		return JdbcUtils.deleteById(UserScreenAd.class, imageId);
	}

}
