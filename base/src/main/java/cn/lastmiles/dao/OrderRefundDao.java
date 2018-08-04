package cn.lastmiles.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.OrderRefund;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class OrderRefundDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static Logger logger = LoggerFactory
			.getLogger(OrderRefundDao.class);

	public void save(OrderRefund orderRefund) {
		String sql = "insert into t_order_refund(id,orderId,status,totalPrice,refundPrice,channel,createdTime) values(?,?,?,?,?,?,?)";
		logger.debug("save --> sql is {}", sql);
		jdbcTemplate.update(sql, orderRefund.getId(), orderRefund.getOrderId(),
				orderRefund.getStatus(), orderRefund.getTotalPrice(),
				orderRefund.getRefundPrice(), orderRefund.getChannel(),
				new Date());
	}

	public void updateStatus(Long id, Integer status) {
		logger.debug("updateDate --> id is :{} ,status is :{}", id, status);
		String sql = "update t_order_refund  set status = ? where id = ?";
		jdbcTemplate.update(sql, status, id);
	}

	public OrderRefund get(Long id) {
		return BeanUtils.toBean(OrderRefund.class, jdbcTemplate.queryForMap(
				"select * from t_order_refund where id = ?", id));
	}

}
