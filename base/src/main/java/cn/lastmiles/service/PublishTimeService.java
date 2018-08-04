package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.service.PublishTime;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.PublishDao;
import cn.lastmiles.dao.PublishTimeDao;

@Service
public class PublishTimeService {
	@Autowired
	private PublishTimeDao publishTimeDao;
	
	@Autowired
	private PublishDao publishDao;
	
	@Autowired
	private IdService idSerivce;

	public void save(List<PublishTime> publishTimes, Long publishId) {
		List<PublishTime> timeList = new ArrayList<PublishTime>();
		if (publishTimes!=null&&publishTimes.size()>0) {
			for (PublishTime publishTime : publishTimes) {
				publishTime.setId(idSerivce.getId());
				publishTime.setPublishId(publishId);
				timeList.add(publishTime);
			}
		}
		if (!timeList.isEmpty()) {
			publishTimeDao.save(timeList);
		}
	}

	public List<PublishTime> findByPublishId(Long publishId) {
		return publishTimeDao.findByPublishId(publishId);
	}
	public PublishTime findById(Long id) {
		return publishTimeDao.findById(id);
	}
	
	public void update(Integer bookednumber, Long id) {
		publishTimeDao.updateBookednumber(bookednumber,id);
		this.countBookingNumer(this.findById(id).getPublishId());
	}
	/**
	 * 已经预约数量+1
	 * @param id
	 */
	public void updateAdd(Long id) {
		PublishTime publishTime =this.findById(id);
		this.update(publishTime.getBookedNumber()+1,id);
	}
	/**
	 * 已经预约数量-1
	 * @param id
	 */
	public void updateDesc(Long id) {
		PublishTime publishTime =this.findById(id);
		this.update(publishTime.getBookedNumber()-1,id);
	}
	
	/**
	 * 统计已经预约次数
	 * @param publishId
	 * @return
	 */
	public void countBookingNumer(Long publishId) {
		publishDao.updateBookedNumber(publishId,publishTimeDao.countBookingNumer(publishId));
	}
	
	@Transactional
	public void update(List<PublishTime> publishTimes, Long publishId) {
		publishTimeDao.delete(publishId);
		List<PublishTime> timeList = new ArrayList<PublishTime>();
		if (publishTimes!=null&&publishTimes.size()>0) {
			for (PublishTime publishTime : publishTimes) {
				publishTime.setId(idSerivce.getId());
				publishTime.setPublishId(publishId);
				timeList.add(publishTime);
			}
		}
		if (!timeList.isEmpty()) {
			publishTimeDao.save(timeList);
		}
	}

	public void delete(Long publishId) {
		publishTimeDao.delete(publishId);
	}
}
