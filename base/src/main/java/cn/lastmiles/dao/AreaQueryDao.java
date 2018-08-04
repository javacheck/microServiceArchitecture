/**
 * createDate : 2016年10月10日下午2:31:23
 */
package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AreaQueryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getprovinceAll() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_province");
		return list;
	}

	public List<Map<String, Object>> getCityByProvinceID(String provinceID) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_city where provinceID = ? ",provinceID);
		return list;
	}
	
	public List<Map<String, Object>> getDistrictByCityID(String cityID) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_district where cityID = ? ",cityID);
		return list;
	}

	public List<Map<String, Object>> getShopCategory() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_alipay_shop_category");
		return list;
	}
}
