package cn.lastmiles.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.AlipayCategory;

/**
 * 
 * createdTime:2016-10-11
 * @author shaoyikun
 *
 */
@Repository
public class AlipayCategoryDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	//private final static Logger logger = LoggerFactory.getLogger(AccountDao.class);
	
	public List<Map<String, Object>> getById(String id) {
		return jdbcTemplate.queryForList("SELECT * FROM t_alipay_category WHERE id = ?", id);
	}
	
	public List<AlipayCategory> getByParentId(String pid) {
		return jdbcTemplate.query("SELECT * FROM t_alipay_category WHERE parent_id = ?", new RowMapper<AlipayCategory>(){
			@Override
			public AlipayCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlipayCategory pojo = new AlipayCategory();
				pojo.setId(rs.getLong("id"));
				pojo.setParentId(rs.getLong("parent_id"));
				pojo.setCreatedTime(rs.getDate("created_time"));
				pojo.setName(rs.getString("name"));
				pojo.setNote(rs.getString("note"));
				return pojo;
			}
		}, pid);
	}
}
