package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Activity;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ActivityDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 分页列表，根据名称模糊查找
	 * @param page
	 * @param name
	 * @return
	 */
	public Page list(Page page, String name) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and title like ? ");
			parameters.add("%" + name + "%");
		}

		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_activity where 1 = 1" + sql.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total.intValue() == 0) {
			return page;
		}

		String select = "SELECT act.id,act.title,act.startDate,act.endDate,act.`status`,area.name as areaName FROM t_activity act "
				+ " inner join t_area area on act.areaId = area.id where 1 = 1";

		sql.insert(0, select);
		sql.append(" order by act.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		page.setData(BeanUtils.toList(Activity.class,
				jdbcTemplate.queryForList(sql.toString(), parameters.toArray())));
		return page;
	}

	public void save(Activity activity) {
		String sql = "insert into t_activity(id,title,startDate,endDate,imageId,memo,createdTime,status,areaId) values(?,?,?,?,?,?,now(),?,?)";
		jdbcTemplate.update(sql,activity.getId(),activity.getTitle(),activity.getStartDate(),activity.getEndDate(),activity.getImageId(),activity.getMemo(),activity.getStatus(),activity.getAreaId());
	}

	public void update(Activity activity) {
		String sql = "update t_activity set title =?,startDate=?,endDate=?,imageId=?,memo=?,status=?,areaId=? where id = ? ";
		jdbcTemplate.update(sql,activity.getTitle(),activity.getStartDate(),activity.getEndDate(),activity.getImageId(),activity.getMemo(),activity.getStatus(),activity.getAreaId(),activity.getId());
	}

	public Activity findById(Long id) {
		String sql  = "SELECT a.id,a.title,a.startDate,a.endDate,a.`status`,a.memo,a.areaId,imageId from t_activity a where id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(Activity.class, list.get(0));
	}

	public void delete(Long id) {
		String sql = "delete from t_activity where id = ?";
		jdbcTemplate.update(sql,id);
	}
}
