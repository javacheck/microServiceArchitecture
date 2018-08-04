package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Area;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class AreaDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Area> findByParent(Long parentId) {
		String sql = "select id,name,parentId,path,depth,fullName from t_area ";
		if (parentId == null) {
			sql += " where parentId is null";
			return BeanUtils.toList(Area.class, jdbcTemplate.queryForList(sql));
		} else {
			sql += " where parentId = ?";
			return BeanUtils.toList(Area.class,
					jdbcTemplate.queryForList(sql, parentId));
		}
	}

	public Area findById(Long id) {
		String sql = "select id,name,parentId,path,depth,fullName from t_area  where id = ? ";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null: BeanUtils.toBean(Area.class, list.get(0));
	}
}
