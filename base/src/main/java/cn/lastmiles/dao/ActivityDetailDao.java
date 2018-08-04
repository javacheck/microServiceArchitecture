package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ActivityDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class ActivityDetailDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(ActivityDetailDao.class);
	
	public Page list(Page page ,Long activityId){
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_activity_detail  ad  ");
		sql.append(" LEFT JOIN t_activity a on ad.activityId = a.id ");
		sql.append(" LEFT JOIN t_area area on ad.areaId = area.id ");
		sql.append(" where 1 = 1 ");
		if (activityId!=null) {
			sql.append(" and ad.activityId = ? ");
			parameters.add(activityId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT ad.id,	ad.activityId,ad.startDate,ad.endDate,ad.areaId,ad.placeName,ad.imageId,ad.createdTime,ad.status,"
						+ "a.title as 'activity.title' ,"
						+ "area.name as 'area.name',area.fullName as 'area.fullName'  "
								+ sql + "  order by createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(ActivityDetail.class, list));

		return page;
	}
	

	public void save(ActivityDetail activityDetail) {
		String sql = "insert into t_activity_detail(id,activityId,startDate,endDate,areaId,placeName,imageId,createdTime,status) values(?,?,?,?,?,?,?,now(),?)";
		jdbcTemplate.update(sql,activityDetail.getId(),activityDetail.getActivityId(),activityDetail.getStartDate(),activityDetail.getEndDate(),activityDetail.getAreaId(),activityDetail.getPlaceName(),activityDetail.getImageId(),activityDetail.getStatus());
	}

	public void update(ActivityDetail activityDetail) {
		String sql = "update t_activity_detail set activityId = ?,startDate =?,endDate=?,areaId=?,placeName=?,imageId=?,status=? where id =?";
		jdbcTemplate.update(sql,activityDetail.getActivityId(),activityDetail.getStartDate(),activityDetail.getEndDate(),activityDetail.getAreaId(),activityDetail.getPlaceName(),activityDetail.getImageId(),activityDetail.getStatus(),activityDetail.getId());
	}


	public ActivityDetail findById(Long id) {
		String sql = "select id,activityId,startDate,endDate,areaId,placeName,imageId,createdTime,status from t_activity_detail where id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(ActivityDetail.class, list.get(0));
	}


	public void delete(Long id) {
		String sql = "delete from t_activity_detail  where id =?";
		jdbcTemplate.update(sql,id);
	}


	public ActivityDetail findByTime(Long activityId, Date date) {
		String sql = "select id,activityId,startDate,endDate,areaId,placeName,imageId,createdTime,status from t_activity_detail where activityId = ? and startDate < ? and endDate > ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,activityId,date,date);
		return list.isEmpty()?null:BeanUtils.toBean(ActivityDetail.class, list.get(0));
	}


	public ActivityDetail findByIdAndTime(Long id, Date date) {
		String sql = "select id,activityId,startDate,endDate,areaId,placeName,imageId,createdTime,status from t_activity_detail where id = ? and startDate < ? and endDate > ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id,date,date);
		return list.isEmpty()?null:BeanUtils.toBean(ActivityDetail.class, list.get(0));
	}
	

}
