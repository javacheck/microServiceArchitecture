package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.PublishTime;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class PublishTimeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save(List<PublishTime> timeList){
		String sql = "insert into t_publish_time(publishId,bookingNumber,bookedNumber,orderSort,id,startDate,endDate) values(?,?,?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (PublishTime time : timeList) {
			Object[] arg = new Object[7];
			arg[0] = time.getPublishId();
			arg[1] = time.getBookingNumber();
			arg[2] = time.getBookedNumber();
			arg[3] = time.getOrderSort();
			arg[4] =time.getId();
			arg[5] =time.getStartDate();
			arg[6] =time.getEndDate();
			batchArgs.add(arg);
		}

		jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	public List<PublishTime> findByPublishId(Long publishId) {
		String sql = "SELECT ID,STARTDATE,ENDDATE,PUBLISHID,BOOKINGNUMBER,BOOKEDNUMBER,ORDERSORT FROM t_publish_time WHERE PUBLISHID = ? order by orderSort";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,publishId);
		return BeanUtils.toList(PublishTime.class, list);
	}

	public PublishTime findById(Long id) {
		String sql = "SELECT ID,STARTDATE,ENDDATE,PUBLISHID,BOOKINGNUMBER,BOOKEDNUMBER,ORDERSORT FROM t_publish_time WHERE ID = ? ";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,id);
		return list.isEmpty()?null:BeanUtils.toBean(PublishTime.class, list.get(0));
	}

	public void updateBookednumber(Integer Bookednumber, Long id) {
		String sql = "UPDATE  t_publish_time SET BOOKEDNUMBER = ? WHERE ID= ?";
		jdbcTemplate.update(sql,Bookednumber,id);
	}
	public int countBookingNumer(Long publishId) {
		String sql = "SELECT SUM(bookedNumber) FROM t_publish_time WHERE publishId = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class,publishId );
	}

	public void delete(Long publishId) {
		String sql = "delete from t_publish_time WHERE publishId = ? ";
		jdbcTemplate.update(sql,publishId);
	}
}
