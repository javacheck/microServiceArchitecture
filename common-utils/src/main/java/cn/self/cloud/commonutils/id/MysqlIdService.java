package cn.self.cloud.commonutils.id;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
/**
 * MySQL数据库的ID生成策略
 */
public class MysqlIdService implements IdService {
	
	// 数据库查询
	private JdbcTemplate jdbcTemplate;

	public MysqlIdService(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public Long getId() {
		// 读取数据库中的生成ID
		Long id = jdbcTemplate.queryForObject("select value from t_id where name = 'common'", Long.class);
		// 读取完后将ID递增一
		jdbcTemplate.update("update t_id set value = value + 1");
		return id;
	}

	@Override
	public Long getOrderId() {
		return null;
	}

}