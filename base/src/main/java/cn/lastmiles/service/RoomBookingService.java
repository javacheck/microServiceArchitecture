package cn.lastmiles.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.movie.RoomBooking;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.dao.RoomBookingDao;

@Service
public class RoomBookingService {
	
	private static final Logger logger = LoggerFactory.getLogger(RoomBookingService.class);
	@Autowired
	private RoomBookingDao roomBookingDao;
	@Autowired
	private RoomOpenRecordService roomOpenRecordService;
	@Autowired
	private OrderServise orderServise;
	@Autowired
	private IdService idService;
	public Page list(Long storeId, String number,String beginTime, String endTime, String phone, Integer status, Page page) {
		return roomBookingDao.list(storeId,number,beginTime,endTime,phone,status,page);
	}
	public void add(RoomBooking roomBooking){
		if (roomBooking.getId()==null) {
			roomBooking.setId(idService.getId());
		}
		roomBooking.setCreatedTime(new Date());
		roomBookingDao.save(roomBooking);
	}

	public void update(RoomBooking roomBooking) {
		roomBookingDao.update(roomBooking);
	}

	public RoomBooking findById(Long id) {
		return roomBookingDao.findById(id);
	}
	public void changeStatus(Long id, Integer status) {
		roomBookingDao.updateStatus(id,status);
	}
	public Integer countNeedRemind(Long storeId) {
		return roomBookingDao.countNeedRemind(storeId);
	}
	
	/**
	 * 根据商家ID和房间号以及预定时间、预定时间加上预定时长查询是否与数据库中的数据存在交集
	 * (在某个商家中的某个房间的某个时间段已经有预订了，则此时间段内此房间不能再被其他人预定)
	 * 返回-1表示没有
	 */
	public int checkBookingTime(Long id,Long storeId,Long roomId,String reserveDateString,Double reserveDuration){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date reserveDate = null;
		Date currentDate = null;
		try {
			reserveDate = sdf.parse(reserveDateString);
			if( null == id ){
				currentDate = sdf.parse(sdf.format(new Date()));
				logger.debug("reserveDate is {} , currentDate is {}",reserveDate,currentDate);
				if( currentDate.getTime() - reserveDate.getTime() > 0 ){
					logger.debug("预定时间不能小于当前系统时间~!");
					return 0;
				}				
			}
		} catch (ParseException e) {
			e.printStackTrace();
			logger.debug("传入的预定时间存在错误,错误数据为：{}",reserveDate);
			return 1;
		}
		
		// 截取
		double reserveDuration_ = BigDecimal.valueOf(reserveDuration).setScale(1,BigDecimal.ROUND_FLOOR).doubleValue();
		
		int minute = (int) (reserveDuration_ * 60) ;
		logger.debug("截取后的时长为：{},计算后的分钟数为：{}",reserveDuration_,minute);
		Date endDate = DateUtils.addMinute(reserveDate, minute);
		String reserveEndDateString = sdf.format(endDate);
		logger.debug("计算后的预定结束时间为：{}",reserveEndDateString);
		
		Order order = orderServise.findByRoomId(roomId);
		logger.debug("{}编号的房间现在是否有人在开台使用中？{}",roomId,order);
		if( null != order ){
			Date createTime = order.getCreatedTime();
			Integer duration = order.getDuration();
			Date endDuration = DateUtils.addMinute(createTime,duration);
			logger.debug("订单表中查询到的开台时间是：{},结束分钟数是：{},转化后的结束时间是：{}",createTime,duration,endDuration);
			// 预定的开始、结束时间
			Long reserveStartLong = ( ( null == reserveDate ) ? 0 : reserveDate.getTime() );
			Long reserveEndLong = endDate.getTime();
			// 正在开台的开始、结束时间
			Long startLong = createTime.getTime();
			Long endLong = endDuration.getTime();
			if( reserveStartLong.longValue() >= endLong.longValue() || reserveEndLong.longValue() <= startLong.longValue()  ){
				
			} else {
				return 2;					
			}
		}
		
//		// 在交易记录中存在此时间段的记录
//		if( roomOpenRecordService.checkWhetherUsedTime(storeId, roomId, reserveDateString, reserveEndDateString) ){
//			return 2;
//		};
		
		/**
		 * 预定时间只能预定当前时间之后的时间段
		 * 先去记录中查询是否有交易记录,如果有则证明此时间段有人在使用此房间
		 */
	
		 if( roomBookingDao.checkBookingTime(id,storeId,roomId,reserveDateString,reserveEndDateString) ){
			 return 3;
		 }
		 return -1;
	}
}
