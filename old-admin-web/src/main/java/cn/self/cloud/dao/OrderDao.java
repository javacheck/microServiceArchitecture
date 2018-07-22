package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Order;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 分页查找
	 * 
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(String beginTime, String endTime, String mobile, String orderId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		sql.append(" INNER JOIN t_account a on  a.id =o.accountId ");
		sql.append(" INNER JOIN t_store s ON o.storeId=s.id ");
		sql.append(" INNER JOIN t_user u ON o.userId = u.id");
		sql.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%"+orderId+"%");
		}
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT o.*  "+ sql + " order by o.id limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));

		return page;
	}
	public Order findById(Long id) {
		String sql="SELECT * FROM t_order WHERE id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Order.class, list.get(0));
	}
	public Double calculatSales(Long accountId, Long storeId,Date beginDate,Date endDate) {
		String sql = "SELECT SUM(price) FROM t_order t WHERE createdTime BETWEEN ? AND ? AND accountId = ? AND storeId = ?";
		Object [] list= {beginDate,endDate,accountId,storeId};
		Double sales= jdbcTemplate.queryForObject(sql, list, Double.class);
		return sales!=null?sales:0D;
	}
	public boolean save(Order order) {
		String sql = "INSERT INTO t_order(id,accountId,storeId,price,createdTime,userId,status,paymentMode) VALUES(?,?,?,?,?,?,?,?)";
		int temp=jdbcTemplate.update(sql,order.getId(),order.getAccountId(),order.getStoreId(),order.getPrice(),order.getCreatedTime(),order.getUserId(),order.getStatus(),order.getPaymentMode());
		return temp<=0?false:true;
	}
	public boolean update(Long orderId, Integer status, Integer paymentMode) {
		String sql = "UPDATE t_order set status = ? ,paymentMode =? WHERE id = ?";
		int temp=jdbcTemplate.update(sql,status,paymentMode,orderId);
		return temp<=0?false:true;
	}
}
