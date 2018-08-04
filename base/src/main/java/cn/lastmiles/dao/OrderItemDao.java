package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class OrderItemDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<OrderItem> findByOrderId(Long orderId) {
		String sql = "SELECT ot.* FROM t_order_item ot LEFT JOIN t_product_stock  ps on  ps.id =ot.stockId  WHERE ot.orderId = ?";
		return BeanUtils.toList(OrderItem.class,
				jdbcTemplate.queryForList(sql, orderId));
	}

	public void save(OrderItem orderItem) {
		String sql = "INSERT INTO t_order_item(id,status,name,type,orderId,stockId,amount,price,categoryId,salesPromotionCategoryId,createdTime,userId,promotionId,discount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, orderItem.getId(),orderItem.getStatus(),orderItem.getName(),orderItem.getType(),orderItem.getOrderId(),
				orderItem.getStockId(), orderItem.getAmount(),
				orderItem.getPrice(), orderItem.getCategoryId(),
				orderItem.getSalesPromotionCategoryId(), new Date(),
				orderItem.getUserId(),orderItem.getPromotionId(),orderItem.getDiscount());
	}

	public List<OrderItem> findByNoCategoryId() {
		String sql = "SELECT ot.* FROM t_order_item ot WHERE ot.categoryId is null";
		return BeanUtils
				.toList(OrderItem.class, jdbcTemplate.queryForList(sql));
	}

	public void updateCategoryId(Long id, Long categoryId) {
		String sql = "update  t_order_item set categoryId=?  WHERE id = ? ";
		jdbcTemplate.update(sql, categoryId, id);

	}

	public void updateCategoryId(Long orderId, Double discount) {
		String sql = "update  t_order_item set discount=?  WHERE orderId = ? and type = 1  ";
		jdbcTemplate.update(sql, discount, orderId);
	}

	public Integer countPromotion(Long promotionCategoryId, Long userId) {
		return jdbcTemplate
				.queryForObject(
						"select sum(amount) from t_order_item where userId = ? and salesPromotionCategoryId = ? and status = 1",
						Integer.class, userId, promotionCategoryId);
	}

	public void updateStatus(Long orderId) {
		String sql = "update  t_order_item set status = ?  WHERE orderId = ? ";
		jdbcTemplate.update(sql, Constants.OrderItem.STATUSCANCEL, orderId);
	}

	public OrderItem findByStockId(Long stockId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_order_item where stockId=?",
						stockId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(OrderItem.class, list.get(0));
	}

	public OrderItem findByOrderIdAndStockIdAndType(Long orderId, Long stockId,
			Integer type) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_order_item where stockId=? and orderId = ? and type = ?",
						stockId,orderId,type);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(OrderItem.class, list.get(0));
	}

	public void updateAmount(Long id, Integer amount) {
		String sql = "update t_order_item set amount = ? where id = ?";
		jdbcTemplate.update(sql,amount,id);
	}

	public void updateStatus(Long orderId, Integer status) {
		String sql = "update t_order_item set status = ? where orderId = ? ";
		jdbcTemplate.update(sql,status,orderId);
	}

	public Boolean updateNumberAndPrice(Long orderId, Long stockId, Integer type, Double updateReturnNumber, Double updateReturnPrice) {
		String sql = "update t_order_item set returnNumber = ? , returnPrice = ?  where orderId = ? and stockId = ? ";
		return jdbcTemplate.update(sql,updateReturnNumber,updateReturnPrice,orderId,stockId) > 0 ? true : false ;
	}
	
	public void deleteByOrderId(Long orderId){
		jdbcTemplate.update("delete from t_order_item where orderId = ?", orderId);
	}

	public void updateDiscount(Long orderId, Long stockId, Double discount) {
		jdbcTemplate.update("update t_order_item set discount = ? where stockId = ? and orderId = ?", discount,stockId,orderId);
	}

	public Page getProductListByOrderId(String orderId, String productName, String barCode, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" FROM t_order_item ot LEFT JOIN t_product_stock  ps ON ps.id =ot.stockId LEFT JOIN t_product pr ON ps.productId=pr.id LEFT JOIN t_after_sales afsa ON afsa.orderItemId = ot.id LEFT JOIN t_after_sales_type afsaty ON afsa.afterSalesTypeId = afsaty.id WHERE ot.orderId = ? ");
		parameters.add(orderId);
		if (StringUtils.isNotBlank(productName)) {
			querySQL.append(" AND pr.name = ? ");
			parameters.add(productName);
		}
		if (StringUtils.isNotBlank(barCode)) {
			querySQL.append(" AND ps.barCode = ? ");
			parameters.add(barCode);
		}
		
		Integer total = jdbcTemplate.queryForObject("SELECT COUNT(1) " + querySQL.toString() ,Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		
		querySQL.insert(0, "SELECT ot.*,pr.name realName,afsa.amount afterSalesAmount,afsaty.name afterSalesType ");
		querySQL.append(" LIMIT ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		List<OrderItem> orderItems = BeanUtils.toList(OrderItem.class, list);
		page.setData(orderItems);
		return page;
	}
}
