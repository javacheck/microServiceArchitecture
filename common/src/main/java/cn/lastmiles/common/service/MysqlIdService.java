package cn.lastmiles.common.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class MysqlIdService implements IdService {
	private JdbcTemplate jdbcTemplate;

	public MysqlIdService(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public Long getId() {
		Long id = jdbcTemplate.queryForObject(
				"select value from t_id where name = 'common'", Long.class);
		jdbcTemplate.update("update t_id set value = value + 1");
		return id;
	}

	@Override
	public Long getOrderId() {
		// TODO Auto-generated method stub
		return getId();
	}

}
