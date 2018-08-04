package cn.lastmiles.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Address;
import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.service.Booking;
import cn.lastmiles.bean.service.Publish;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.AddressDao;
import cn.lastmiles.dao.BookingDao;
import cn.lastmiles.getui.PushService;

@Service
public class BookingService {
	@Autowired
	private BookingDao bookingDao;
	@Autowired
	private PublishService publishService;
	@Autowired
	private PublishTimeService publishTimeService;
	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private IdService idService;
	@Autowired
	private AddressService addressService;
	private final static Logger logger = LoggerFactory
			.getLogger(BookingService.class);
	
	/**
	 * 提交预约
	 * @param booking
	 */
	@Transactional
	public void save(Booking booking) {
		booking.setId(idService.getId());
		booking.setStatus(Constants.BookingStatus.BOOKING_CONFIRMING);//确认中
		
		if (booking.getAddressId() != null){
			Address address = addressDao.get(booking.getAddressId());
			booking.setAddress(address.getAreaFullName());
			booking.setUserName(address.getName());
			booking.setPhone(address.getPhone());
		}
		
		bookingDao.save(booking);
		Publish publish=publishService.findById(booking.getPublishId());
		publishTimeService.updateAdd(booking.getPublishTimeId());//已经预约数+1
		PushService.pushToSingle(new Message("预约状态通知", Constants.MessageType.MESSAGE_ORDER_CONTENT.replace("#title#", publish.getTitle()), publish.getUserId(), Constants.MessageType.MESSAGE_ORDER, publish.getId()));
	}


	/**
	 * 修改支付
	 * @param bookingId
	 */
	@Transactional
	public void pay(Long bookingId,Long userId) {
		bookingDao.updatePaystatus(Constants.isPaid.PAID,bookingId,userId);//修改支付状态
		//发送短信
	}
	/**
	 * 发布方 确认预约
	 * @param reply 
	 * @param bookingId
	 */
	@Transactional
	public void confirm(Long id,Long userId, String reply) {
		PushService.pushToSingle(new Message("预约状态通知",Constants.MessageType.MESSAGE_CONFIRMATION_SUCCESS_CONTENT.replace("#title#", publishService.findById(bookingDao.findById(id).getPublishId()).getTitle()),this.findById(id).getUserId(), Constants.MessageType.MESSAGE_CONFIRMATION_SUCCESS, id));
		bookingDao.updateStatus(Constants.BookingStatus.BOOKING_CONFIRM, id,userId,reply);//设置为确认状态
	}
	
	/**
	 * 取消
	 * @param bookingId
	 */
	@Transactional
	public void cancel(Long id, Long userId,String reason) {
		Booking booking=bookingDao.findById(id);
		Publish publish =publishService.findById(booking.getPublishId());
		logger.debug("booking is :{},publish is :{},id is :{},userId is :{}",booking,publish,id,userId);
		
		if (publish.getUserId().equals(userId)&&booking.getStatus().equals(Constants.BookingStatus.BOOKING_CONFIRMING)) {//发布方取消已预约状态 为拒绝
			logger.debug("发布方拒绝");
			PushService.pushToSingle(new Message("预约状态通知", 
					Constants.MessageType.MESSAGE_REFUSE_ORDER_CONTENT.replace("#title#", publish.getTitle()), 
					booking.getUserId(), Constants.MessageType.MESSAGE_REFUSE_ORDER, id));
		}
		if (publish.getUserId().equals(userId)&&!booking.getStatus().equals(Constants.BookingStatus.BOOKING_CONFIRMING)) {//发布中途取消
			logger.debug("发布方取消");
			PushService.pushToSingle(new Message("预约状态通知", 
					Constants.MessageType.MESSAGE_PUBLISH_MIDWAY_CANCEL_CONTENT.replace("#title#", publish.getTitle()), 
					booking.getUserId(), Constants.MessageType.MESSAGE_PUBLISH_MIDWAY_CANCEL, id));
		}
		if (booking.getUserId().equals(userId)) {//预约方中途取消
			logger.debug("预约方取消");
			PushService.pushToSingle(new Message("预约状态通知", 
					Constants.MessageType.MESSAGE_ORDER_MIDWAY_CANCEL_CONTENT.replace("#title#", publish.getTitle()), 
					publish.getUserId(), Constants.MessageType.MESSAGE_ORDER_MIDWAY_CANCEL, id));
		}
		
		
		bookingDao.updateStatus(Constants.BookingStatus.BOOKING_CANCEL, id,userId,reason);//设置为取消状态
		publishTimeService.updateDesc(this.findById(id).getPublishTimeId());//已经预约数-1
	}
	/**
	 * 服务完成
	 * @param bookingId
	 * @param userId
	 */
	public void completion(Long id, Long userId) {
		bookingDao.updateStatus(Constants.BookingStatus.BOOKING_COMPLETION, id, userId,null);//设置为成功
	}
	
	/**
	 * 订单完成
	 * @param bookingId
	 */
	public void success(Long id, Long userId) {
		bookingDao.updateStatus(Constants.BookingStatus.BOOKING_SUCCESS, id, userId,null);//设置为确认状态
	}
	
	/**
	 * 通过ID查找
	 * @param id
	 * @return
	 */
	public Booking findById(Long id) {
		return bookingDao.findById(id);
	}
	
	public Booking findByIdAndUserId(Long id, Long userId) {
		return bookingDao.findByIdAndUserId(id,userId);
	}


	public List<Booking> findByPublishId(Long publishId) {
		List<Booking> bookings= bookingDao.findByPublishId(publishId);
		return bookings;
	}


	public List<Booking> findByStatus(Integer status) {
		return bookingDao.findByStatus(status);
	}


}
