package cn.lastmiles.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ActivityDetailLocation;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class ActivityDetailLocationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(ActivityDetailLocationDao.class);
	
	public void save (ActivityDetailLocation activityDetailLocation){
		String sql = "insert into t_activity_detail_location(id,activityDetailId,name,shopName) values(?,?,?,?)";
		logger.debug("save sql = "+sql);
		jdbcTemplate.update(sql,activityDetailLocation.getId(),activityDetailLocation.getActivityDetailId(),activityDetailLocation.getName(),activityDetailLocation.getShopName());
	}

	public void deleteByActivityDetailId(Long activityDetailId) {
		String sql = "delete from t_activity_detail_location where  activityDetailId = ?";
		logger.debug("save sql = "+sql);
		jdbcTemplate.update(sql,activityDetailId);
	}
	public List<ActivityDetailLocation> findByActivityDetailId(Long activityDetailId) {
		String sql = "select id,activityDetailId,name,shopName from t_activity_detail_location where activityDetailId = ? ";
		logger.debug("save sql = "+sql);
		return BeanUtils.toList(ActivityDetailLocation.class, jdbcTemplate.queryForList(sql,activityDetailId));
	}
	/**
	 * 通过ID查找
	 * @param id
	 * @return
	 */
	public ActivityDetailLocation findById(Long id) {
		String sql = "select id,activityDetailId,name,shopName from t_activity_detail_location where id = ? ";
		logger.debug("save sql = "+sql);
		return BeanUtils.toBean(ActivityDetailLocation.class, jdbcTemplate.queryForList(sql,id).get(0));
	}

}
