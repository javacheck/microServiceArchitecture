package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ActivityPartake;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class ActivityPartakeDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(ActivityPartakeDao.class);

	public void save(ActivityPartake activityPartake) {
		String sql = "INSERT INTO t_activity_partake(id,activityDetailId,userId,createdTime) values(?,?,?,?)";
		logger.debug("save sql "+sql);
		jdbcTemplate.update(sql,activityPartake.getId(),activityPartake.getActivityDetailId(),activityPartake.getUserId(),new Date());
	}

	public ActivityPartake findByactivityDetailIdAndUserId(	Long activityDetailId, Long userId) {
		String sql = "select id,activityDetailId,userId,createdTime from t_activity_partake where activityDetailId = ? and userId = ?";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,activityDetailId,userId);
		return list.isEmpty()?null:BeanUtils.toBean(ActivityPartake.class, list.get(0));
	}

	public ActivityPartake findById(Long id) {
		String sql = "select id,activityDetailId,userId,createdTime from t_activity_partake where id =  ?";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(ActivityPartake.class, list.get(0));
	}

}
