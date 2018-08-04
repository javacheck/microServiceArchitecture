package cn.lastmiles.service;
/**
 * updateDate : 2015-07-16 PM 18:31
 */
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.SettlementsRecord;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.SettlementsRecordDao;

@Service
public class SettlementsRecordService {

	@Autowired
	private SettlementsRecordDao settlementsRecordDao;
	@Autowired
	private IdService idService ;
	@Autowired
	private OrderServise orderServise;
	@Autowired
	private StoreService storeServise;
	/**
	 * 查询账户流水记录(【管理员/代理商/商家 】查询)
	 * @param ownerId 商户id，或者代理商id
	 * @param type 0商户，1代理商
	 * @param orderId 订单ID
	 * @param name 商家名称或者代理商名称
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	@SuppressWarnings("unchecked")
	public Page getSettlementsRecord(Long ownerId,Integer type,Long orderId,String name,String startTime,String endTime,Page page){
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		page =settlementsRecordDao.getSettlementsRecord(ownerId, type, orderId,name , startTime, endTime, page);
		List<SettlementsRecord> settlementsRecords=(List<SettlementsRecord>)page.getData();
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		page.setData(returnsettlementsRecords);
		return page;
	}
	
	/**
	 * 查询账户流水记录(管理员admin查询)
	 * @param ownerId 商户id，或者代理商id
	 * @param orderId 订单ID
	 * @param name 商家名称或者代理商名称
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	@SuppressWarnings("unchecked")
	public Page getSettlementsRecord(Long ownerId,Long orderId,String name,String startTime,String endTime,Page page){
		
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		page =settlementsRecordDao.getSettlementsRecord(ownerId, orderId,name , startTime, endTime, page);
		List<SettlementsRecord> settlementsRecords=(List<SettlementsRecord>)page.getData();
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		page.setData(returnsettlementsRecords);
		return page;
	}


	/**
	 * API-查询账户流水记录(商家/代理商)
	 * @param ownerId 商户id，或者代理商id
	 * @param type 类型
	 * @param page
	 */
	@SuppressWarnings("unchecked")
	public Page getSettlementsRecord(Long ownerId,Integer type,Page page){
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		page =settlementsRecordDao.getSettlementsRecord(ownerId, type, page);
		List<SettlementsRecord> settlementsRecords=(List<SettlementsRecord>)page.getData();
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		page.setData(returnsettlementsRecords);
		return page;
	}
	private SettlementsRecord filling(SettlementsRecord settlementsRecord) {
		if (settlementsRecord == null) {
			return null;
		}
		Order order=orderServise.findById(settlementsRecord.getOrderId());
		if( null != order ){
			Store store = storeServise.findById(order.getStoreId());			
			settlementsRecord.setStore(store);
		}
		return settlementsRecord;
	}
	public void save (SettlementsRecord settlementsRecord){
		settlementsRecord.setId(idService.getId());
		settlementsRecordDao.save(settlementsRecord);
	}

	public void updateStatus(Long id, Integer status) {
		settlementsRecordDao.updateStatus(id,status);
	}
	
	public List<SettlementsRecord> findAllBySearch(Long ownerId,Integer type,Long orderId,String name,String startTime,String endTime){
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		List<SettlementsRecord>  settlementsRecords=settlementsRecordDao.findAllBySearch(ownerId, type, orderId,name , startTime, endTime);
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		return returnsettlementsRecords;
	}
	public List<SettlementsRecord> findAllBySearch(Long ownerId,Long orderId,String name,String startTime,String endTime){
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		List<SettlementsRecord>  settlementsRecords=settlementsRecordDao.findAllBySearch(ownerId, orderId,name , startTime, endTime);
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		return returnsettlementsRecords;
	}
	public List<SettlementsRecord> findAllBySearch(Long ownerId,Integer type){
		List<SettlementsRecord> returnsettlementsRecords = new ArrayList<SettlementsRecord>();
		List<SettlementsRecord>  settlementsRecords=settlementsRecordDao.findAllBySearch(ownerId, type);
		for (SettlementsRecord settlementsRecord : settlementsRecords) {
			returnsettlementsRecords.add(this.filling(settlementsRecord));
		}
		return returnsettlementsRecords;
	}
}