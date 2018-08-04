package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.OrderPrePay;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class OrderPrePayDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OrderPrePay find(Long orderId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_order_prepay where orderId = ?", orderId);
		return list.isEmpty() ? null : BeanUtils.toBean(OrderPrePay.class,
				list.get(0));
	}

	public void save(OrderPrePay prePay) {
		jdbcTemplate
				.update("insert into t_order_prepay (orderId,channel,prepayId,noncestr,timestamp,sign) values(?,?,?,?,?,?)",
						prePay.getOrderId(), prePay.getChannel(),
						prePay.getPrepayId(), prePay.getNoncestr(),
						prePay.getTimestamp(),prePay.getSign());
	}
}
