/**
 * createDate : 2016年4月14日下午5:18:28
 */
package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.movie.RoomOpen;
import cn.lastmiles.bean.movie.RoomOpenDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.RoomOpenRecordDao;
@Service
public class RoomOpenRecordService {

	@Autowired
	private RoomOpenRecordDao roomOpenRecordDao;

	/**
	 * 根据订单id查找详情
	 * @param orderId
	 * @return
	 */
	public List<RoomOpenDetail> findDetailByOrderId(Long orderId) {
		return roomOpenRecordDao.findDetailByOrderId(orderId);
	}
	
	/**
	 * 根据订单id查找
	 * @param orderId
	 * @return
	 */
	public RoomOpen findByOrderId(Long orderId){
		return roomOpenRecordDao.findByOrderId(orderId);
	}
	
	/**
	 * 查询订单表记录
	 * @param storeId
	 * @param number
	 * @param categoryId
	 * @param page
	 * @return
	 */
	public Page listFromOrder(Long storeId,Long number,Long categoryId,Page page){
		return roomOpenRecordDao.listFromOrder(storeId, number, categoryId, page);
	}
	
	public Page list(String storeIdString, Long number, Integer status,Long categoryId, Page page) {
		return roomOpenRecordDao.list(storeIdString,number,status,categoryId,page);
	}

	public Page list(String storeIdString, Page page) {
		return roomOpenRecordDao.list(storeIdString,page);
	}

	public RoomOpen findById(Long id) {
		return roomOpenRecordDao.findById(id);
	}

	public List<RoomOpenDetail> findByRoomOpenId(Long id) {
		return roomOpenRecordDao.findByRoomOpenId(id);
	}
	
	public boolean checkWhetherUsedTime( Long storeId,Long roomId,String reserveDate,String reserveDurationDate ){
		return roomOpenRecordDao.checkWhetherUsedTime( storeId,roomId,reserveDate,reserveDurationDate );
	}
	
}
