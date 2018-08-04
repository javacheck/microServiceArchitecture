package cn.lastmiles.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Bank;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class BankDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Bank> findAll() {
		return BeanUtils.toList(Bank.class,
				jdbcTemplate.queryForList("select id,name,code,iconUrl from t_bank"));
	}
}
