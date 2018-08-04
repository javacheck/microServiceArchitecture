package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class RoomCategoryDateSettingDao {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<RoomCategoryDateSetting> findByCategoryId(Long categoryId){
		return BeanUtils.toList(RoomCategoryDateSetting.class, jdbcTemplate.queryForList("select * from t_movie_room_category_date_setting where categoryId = ? ", categoryId));
	}

	public RoomCategoryDateSetting findByCategoryIdAndDateSettingId(Long categoryId,Long dateSettingId) {
		String sql = "select * from t_movie_room_category_date_setting where categoryId = ? and dateSettingId = ?";
		logger.debug("sql is {}",sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,categoryId,dateSettingId);
		return list.isEmpty()?null:BeanUtils.toBean(RoomCategoryDateSetting.class, list.get(0));
	}


	public void save(RoomCategoryDateSetting roomCategoryDateSetting) {
		JdbcUtils.save(roomCategoryDateSetting);
	}


	public void deleteByCategoryId(Long categoryId) {
		String sql = "delete from t_movie_room_category_date_setting where categoryId = ? ";
		jdbcTemplate.update(sql,categoryId);
	}


	public int findByDateSettingId(Long dateSettingId) {
		return jdbcTemplate.queryForObject(" select count(*) from t_movie_room_category_date_setting where dateSettingId = ? ", Integer.class, dateSettingId);
	}
	

}
