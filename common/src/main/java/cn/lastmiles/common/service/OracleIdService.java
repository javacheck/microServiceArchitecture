package cn.lastmiles.common.service;

import org.springframework.jdbc.core.JdbcTemplate;

public class OracleIdService implements IdService {
	private JdbcTemplate jdbcTemplate;

	public OracleIdService(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long getId() {
		return jdbcTemplate.queryForObject(
				"select COMMONSEQUENCE.nextval as id from dual", Long.class);
	}

	@Override
	public Long getOrderId() {
		// TODO Auto-generated method stub
		return getId();
	}

}