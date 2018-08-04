package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.service.BookingMessage;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.BookingMessageDao;

@Service
public class BookingMessageService {
	@Autowired
	private BookingMessageDao bookingMessageDao;
	
	private IdService idService;
	/**
	 * 保存
	 * @param bookingMessage
	 */
	public void save(BookingMessage bookingMessage){
		bookingMessage.setId(idService.getId());
		bookingMessageDao.save(bookingMessage);
	}
	/**
	 * 查看
	 * @param bookingId
	 * @param long1 
	 * @return
	 */
	public List<BookingMessage> findByBookId(Long bookingId){
		return bookingMessageDao.findByBookId(bookingId);
	}
	

}
