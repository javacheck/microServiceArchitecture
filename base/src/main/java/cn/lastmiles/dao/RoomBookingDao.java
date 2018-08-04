package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.RoomBooking;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class RoomBookingDao {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomBookingDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void  save(RoomBooking roomBooking){
		logger.debug("roomBooking is {}",roomBooking);
		JdbcUtils.save(roomBooking);
	}

	public void update(RoomBooking roomBooking) {
		String sql = "update t_movie_room_booking set contacts = ? "
				+ " ,arrivalTime=?,dateSettingId=?,phone=?,memo=?,reserveDate=?,reserveDuration=?,reserveEndDate=? "
				+ " where id = ?";
		jdbcTemplate.update(sql,roomBooking.getContacts()
				,roomBooking.getArrivalTime(),roomBooking.getDateSettingId(),roomBooking.getPhone(),roomBooking.getMemo()
				,roomBooking.getReserveDate(),roomBooking.getReserveDuration(),roomBooking.getReserveEndDate()
				,roomBooking.getId());
	}

	public RoomBooking findById(Long id) {
		return JdbcUtils.findById(RoomBooking.class, id);
	}

	public Page list(Long storeId, String number, String beginTime,	String endTime, String phone, Integer status, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_movie_room_booking mrb LEFT JOIN t_movie_room mr ON mrb.roomId = mr.id where 1 = 1 ");
		if (status != null&&status.intValue()!=-1){
			sql.append(" and mrb.status = ? ");
			parameters.add(status);
		}
		if (storeId!=null&&storeId.longValue()!=-1L) {
			sql.append(" and mrb.storeId= ? ");
			parameters.add(storeId);
		}
		if (StringUtils.isNotBlank(number)) {//房间号码
			sql.append(" and  mr.number like ? ");
			parameters.add("%" + number + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {//开始时间筛选
			sql.append(" and mrb.createdTime >= ?");
			parameters.add(beginTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {//结束时间筛选
			sql.append(" and mrb.createdTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		if (StringUtils.isNotBlank(phone)) {//手机号码筛选
			sql.append(" and mrb.phone like ?");
			parameters.add("%" + phone + "%");
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		
		page.setTotal(total);
		
		if (total.intValue() == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT  mrb.* "
						+ ",(select a.mobile from t_account a where a.id = mrb.accountId) as accountMobile"
						+ ",mr.number  as 'room.number' "
						+ ",mr.persons as 'room.persons' "
								+ sql + "  order by mrb.status, mrb.reserveDate,mrb.createdTime limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(RoomBooking.class, list));

		return page;
	}

	public void updateStatus(Long id, Integer status) {
		String sql = "update t_movie_room_booking set status = ? where id = ?";
		jdbcTemplate.update(sql,status,id);
	}

	public boolean checkBookingTime(Long id,Long storeId, Long roomId, String reserveDate, String reserveDurationDate) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select count(*) from t_movie_room_booking mrb where mrb.storeId = ? and mrb.roomId = ? and mrb.status = ?");
		parameters.add(storeId);
		parameters.add(roomId);
		parameters.add(Constants.RoomBooking.STATUS_BOOKING);
		if( null != id ){
			querySQL.append(" and mrb.id <> ? ");
			parameters.add(id);
		}
		querySQL.append(" and ( ");
		
		// 全包围
		querySQL.append(" ( reserveDate > ? ");
		parameters.add(reserveDate);
		querySQL.append(" and reserveEndDate < ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" or ");
		
		// 全包含
		querySQL.append(" ( reserveDate < ? ");
		parameters.add(reserveDate);
		querySQL.append(" and reserveEndDate > ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" or ");
		
		// 前半包含
		querySQL.append(" ( reserveDate < ? ");
		parameters.add(reserveDate);
		querySQL.append(" and reserveEndDate > ? )");
		parameters.add(reserveDate);
		
		querySQL.append(" or ");
		
		// 后半包含
		querySQL.append(" ( reserveDate < ? ");
		parameters.add(reserveDurationDate);
		querySQL.append(" and reserveEndDate > ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" ) ");
		Integer total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class, parameters.toArray());
		
		if( total.intValue() > 0 ){
			return true;
		}
		return false;
	}

	public Integer countNeedRemind(Long storeId) {
		String sql = "SELECT COUNT(1) FROM t_movie_room_booking mrb where mrb.`status` = 0 AND TIMESTAMPDIFF(MINUTE,NOW(),mrb.reserveDate) <=20 AND mrb.storeId = ? ";
		return  jdbcTemplate.queryForObject(sql,Integer.class, storeId);
	}

}
