package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.apache.tools.ant.types.resources.selectors.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.BookingMessage;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class BookingMessageDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(BookingMessage bookingMessage) {
		String sql = "INSERT INTO t_booking_message(id,bookingId,userId,message,type,createdTime) value(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, bookingMessage.getId(),
				bookingMessage.getBookingId(), bookingMessage.getUserId(),
				bookingMessage.getMessage(), bookingMessage.getType(),
				new Date());
	}
	
	public List<BookingMessage> findByBookId(Long bookingId){
		String sql ="SELECT id,bookingId,userId,message,type,createdTime from t_booking_message where bookingId = ? ";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,bookingId);
		return BeanUtils.toList(BookingMessage.class, list);
	}
}
