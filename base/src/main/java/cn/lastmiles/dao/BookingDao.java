package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.Booking;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class BookingDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Booking booking) {
		String sql = "INSERT INTO t_booking(ID,CREATEDTIME,PUBLISHID,USERID,PUBLISHTIMEID,CONFIRMTIME,STATUS,PAYSTATUS,address,userName,memo,phone) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, booking.getId(), new Date(),
				booking.getPublishId(), booking.getUserId(),
				booking.getPublishTimeId(), booking.getConfirmTime(),
				booking.getStatus(), booking.getPayStatus(),
				booking.getAddress(), booking.getUserName(), booking.getMemo(),
				booking.getPhone());
	}

	public Booking findById(Long id) {
		String sql = "SELECT ID,CREATEDTIME,PUBLISHID,USERID,PUBLISHTIMEID,CONFIRMTIME,STATUS,PAYSTATUS,PAYSTATUS "
				+ ",memo ,reply, userName ,address ,phone ,serviceCompletionTime,orderCompleteTime,cancelTime,cancelUserId,cancelReason"
				+ ", (select iconUrl from t_user u where b.userId =u.id ) as 'imageID' "
				+ "FROM t_booking b WHERE ID = ? ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		return list.isEmpty() ? null : BeanUtils.toBean(Booking.class,
				list.get(0));
	}

	/**
	 * 更新支付状态
	 * 
	 * @param paystatus
	 * @param id
	 * @param userId
	 * @return
	 */
	public int updatePaystatus(Integer paystatus, Long id, Long userId) {
		String sql = "UPDATE t_booking SET PAYSTATUS = ?  WHERE ID = ? AND USERID =? ";
		return jdbcTemplate.update(sql, paystatus, id, userId);
	}

	/**
	 * 更新状态
	 * 
	 * @param status
	 * @param id
	 * @param userId
	 * @return
	 */
	public int updateStatus(Integer status, String id, String userId) {
		String sql = "UPDATE t_booking SET STATUS = ?  WHERE ID = ? ";
		if (status.equals(Constants.BookingStatus.BOOKING_CONFIRM)) {
			sql += " ,  CONFIRMTIME  = SYSDATE() ";
		}
		sql += " WHERE ID = ? AND USERID = ?";
		return jdbcTemplate.update(sql, status, id, userId);
	}

	/**
	 * 更新状态
	 * 
	 * @param status
	 * @param id
	 * @param userId
	 * @param reply 
	 * @return
	 */
	public int updateStatus(Integer status, Long id, Long userId, String reply) {
		String sql = "UPDATE t_booking SET STATUS = ?";
		if (status.equals(Constants.BookingStatus.BOOKING_CONFIRM)) {//确认
			sql += " ,  CONFIRMTIME  = ? , reply = ? WHERE ID = ?";
			return jdbcTemplate.update(sql, status,new Date(),reply, id);
		}
		if (status.equals(Constants.BookingStatus.BOOKING_COMPLETION)) {//服务完成
			sql += " ,  serviceCompletionTime  = ? WHERE ID = ?";
			return jdbcTemplate.update(sql, status,new Date(),id);
		}
		if (status.equals(Constants.BookingStatus.BOOKING_SUCCESS)) {//订单完成
			sql += " ,  orderCompleteTime  = ? WHERE ID = ?";
			return jdbcTemplate.update(sql, status,new Date(),id);
		}
		if (status.equals(Constants.BookingStatus.BOOKING_CANCEL)) {//订单取消
			sql += " ,  cancelTime  = ? ,cancelUserId =  ? ,cancelReason = ? WHERE ID = ?";
			return jdbcTemplate.update(sql, status,new Date(),userId,reply,id);
		}
		sql += " WHERE ID = ? ";
		return jdbcTemplate.update(sql, status, id);
	}

	public Booking findByIdAndUserId(Long id, Long userId) {
		String sql = "SELECT ID,CREATEDTIME,PUBLISHID,USERID,PUBLISHTIMEID,CONFIRMTIME,STATUS,PAYSTATUS,PAYSTATUS FROM t_booking WHERE ID = ? AND  USERID = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id,
				userId);
		return list.isEmpty() ? null : BeanUtils.toBean(Booking.class,
				list.get(0));
	}

	public List<Booking> findByPublishId(Long publishId) {
		String sql = "SELECT b.ID,b.CREATEDTIME,b.PUBLISHID,b.USERID,b.PUBLISHTIMEID,b.CONFIRMTIME,b.`STATUS`,b.PAYSTATUS,b.PAYSTATUS,b.MEMO "
				+" ,(select iconUrl from t_user u where b.userId =u.id ) as 'imageID' "
				+" ,(select startDate from t_publish_time pt  where b.publishTimeId =pt.id ) as 'publishTime.startDate' "
				+" ,(select endDate from t_publish_time pt  where b.publishTimeId =pt.id ) as 'publishTime.endDate' "
				+ " FROM t_booking b WHERE publishId = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, publishId);
		return BeanUtils.toList(Booking.class,list);
	}

	public List<Booking> findByStatus(Integer status) {
		String sql = "SELECT ID,CREATEDTIME,PUBLISHID,USERID,PUBLISHTIMEID,CONFIRMTIME,STATUS,PAYSTATUS,PAYSTATUS FROM t_booking WHERE status = ? ";
		return BeanUtils.toList(Booking.class, jdbcTemplate.queryForList(sql,status));
	}
}
