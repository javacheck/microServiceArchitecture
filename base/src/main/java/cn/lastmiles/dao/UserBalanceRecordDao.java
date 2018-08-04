package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserBalanceRecord;

@Repository
public class UserBalanceRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(UserBalanceRecordDao.class);
	
	public void save(UserBalanceRecord balanceRecord){
		String sql = "insert into t_user_balance_record(userId,createdTime,amount,memo,orderId,type) values(?,?,?,?,?,?)";
		logger.debug("save sql is :{}",sql);
		jdbcTemplate.update(sql,balanceRecord.getUserId(),new Date(),balanceRecord.getAmount(),balanceRecord.getMemo(),balanceRecord.getOrderId(),balanceRecord.getType());
	}

	public Double sumPrice(Long userId, Integer type, Long orderId,	Date beginTime, Date endTime) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" SELECT sum(amount) as price FROM t_user_balance_record WHERE 1= 1 ");
		if (beginTime!=null) {
			sql.append(" AND createdTime >= ?");
			parameters.add(beginTime);
		}
		if (endTime!=null) {
			sql.append(" AND createdTime <= ?");
			parameters.add(endTime);
		}
		if (type!=null) {
			sql.append(" AND type = ?");
			parameters.add(type);
		}
		if (orderId!=null) {
			sql.append(" AND orderId = ?");
			parameters.add(orderId);
		}
		if (userId!=null) {
			sql.append(" AND userId = ?");
			parameters.add(userId);
		}
		logger.debug("sumPrice sql is :{}, parameters is :{}",sql,parameters);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), parameters.toArray());
		logger.debug("list is :{}, parameters is :{}",list,parameters);
		return list.isEmpty()||list.get(0).get("price")==null?0D: Double.valueOf(list.get(0).get("price").toString()) ;
	}
}
