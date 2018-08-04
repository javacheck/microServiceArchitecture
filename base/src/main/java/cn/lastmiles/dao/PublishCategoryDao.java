package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.PublishCategory;
import cn.lastmiles.common.utils.BeanUtils;


@Repository
public class PublishCategoryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<PublishCategory> findAll(){
		String sql = "SELECT ID , NAME FROM t_publish_category ";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql);
		return BeanUtils.toList(PublishCategory.class, list);
	}
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public PublishCategory findById(Long id) {
		String sql = "SELECT ID , NAME FROM t_publish_category where id = ?";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(PublishCategory.class, list.get(0));
	}
}