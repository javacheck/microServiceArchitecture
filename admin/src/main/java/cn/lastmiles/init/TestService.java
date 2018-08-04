package cn.lastmiles.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public void ttt(){
		jdbcTemplate.update("insert into t_sys_config (name,value) values(?,?)", "test4","test4");
		if (1 > 0)
		throw new RuntimeException("出差");
		jdbcTemplate.update("insert into t_sys_config (name,value) values(?,?)", "test2","test2");
	}
	
}
