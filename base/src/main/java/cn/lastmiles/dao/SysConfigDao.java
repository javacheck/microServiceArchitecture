package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.SysConfig;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class SysConfigDao {
	@Autowired 
	private JdbcTemplate jdbcTemplate;

	public SysConfig get(String name) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select name,value from t_sys_config where name = ?", name);
		if (list.isEmpty()) {
			return null;
		} else {
			return BeanUtils.toBean(SysConfig.class, list.get(0));
		}
	}
	
	public void saveOrUpdate(SysConfig sysConfig){
		if (get(sysConfig.getName()) == null){
			save(sysConfig);
		}else {
			update(sysConfig);
		}
	}

	public void save(SysConfig sysConfig) {
		jdbcTemplate.update("insert into t_sys_config(name,value) values(?,?)",
				sysConfig.getName(), sysConfig.getValue());
	}

	public void update(SysConfig sysConfig) {
		jdbcTemplate.update("update t_sys_config set value = ? where name = ?",
				 sysConfig.getValue(),sysConfig.getName());
	}
}
