package cn.lastmiles.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Log;

@Repository
public class LogDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Log log) {
		jdbcTemplate
				.update("insert into t_log (createdTime,accountId,requestUrl,parameters) values(?,?,?,?)",
						log.getCreatedTime(), log.getAccountId(),
						log.getRequestUrl(), log.getParameters());
	}
}
