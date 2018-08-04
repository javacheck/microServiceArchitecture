package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Discount;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class DiscountDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void set(Discount discount) {
		int result = jdbcTemplate.update(
				"update t_discount set discount = ? where storeId = ?",
				discount.getDiscount(), discount.getStoreId());
		if (result == 0) {
			jdbcTemplate.update(
					"insert into t_discount(discount,storeId) values(?,?)",
					discount.getDiscount(), discount.getStoreId());
		}
	}

	public Discount find(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select discount,storeId from t_discount where storeId = ?",
				storeId);
		return list.isEmpty() ? null : BeanUtils.toBean(Discount.class,
				list.get(0));
	}

	public List<Discount> findAll() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select discount,storeId from t_discount where 1 = 1");
		return BeanUtils.toList(Discount.class, list);
	}
}
