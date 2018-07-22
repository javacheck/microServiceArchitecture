package cn.self.cloud.dao;

import java.util.List;
import cn.self.cloud.bean.OrderItem;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<OrderItem> findByOrderId(Long orderId) {
		String sql = "SELECT ot.* FROM t_order_item ot INNER JOIN t_product_stock  ps on  ps.id =ot.stockId  WHERE ot.id = ?";
		return BeanUtils.toList(OrderItem.class, jdbcTemplate.queryForList(sql,orderId));
	}

	public void save(OrderItem orderItem) {
		String sql = "INSERT INTO t_order_item(orderId,stockId,amount,price,categoryId) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql,orderItem.getOrderId(),orderItem.getStockId(),orderItem.getAmount(),orderItem.getPrice(),orderItem.getCategoryId());
	}
}
