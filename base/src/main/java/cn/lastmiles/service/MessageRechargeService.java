/**
 * createDate : 2016年5月11日上午10:59:45
 */
package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.MessageRecharge;
import cn.lastmiles.bean.MessageSaleRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.MessageRechargeDao;

@Service
public class MessageRechargeService {
	@Autowired
	private MessageRechargeDao messageRechargeDao;
	@Autowired
	private IdService idService;

	public Page list(String storeName,Page page) {
		return messageRechargeDao.list(storeName,page);
	}
	
	@Transactional
	public void save(MessageRecharge messageRecharge,Long[] storeIdArray){
		
		Long accountId = messageRecharge.getAccountId();
		Double price = messageRecharge.getPrice();
		Integer number = messageRecharge.getNumber();
		
		List<Object[]> saveBatchArray = new ArrayList<Object[]>();
		List<Object[]> updateBatchArray = new ArrayList<Object[]>();
		
		List<MessageRecharge> oldStoreIdArray = messageRechargeDao.find();
		// 全是新增
		if( null == oldStoreIdArray || oldStoreIdArray.size() <= 0 ){
			for (Long storeId : storeIdArray) {
				Object[] o = new Object[7];
				o[0] = idService.getId(); // id
				o[1] = storeId; // storeId
				o[2] = accountId; // accountId
				o[3] = price; // price
				o[4] = number; // number
				o[5] = new Date(); // updateTime
				o[6] = number; // remainNumber
				saveBatchArray.add(o);
			}
		} else {
			Map<Long,MessageRecharge> map = new HashMap<Long, MessageRecharge>();
			for (MessageRecharge objects : oldStoreIdArray) {
				map.put(objects.getStoreId(), objects);
			}
			for (Long storeId : storeIdArray) {
				MessageRecharge mr = map.get(storeId);
				if( null == mr ){
					Object[] o = new Object[7];
					o[0] = idService.getId(); // id
					o[1] = storeId; // storeId
					o[2] = accountId; // accountId
					o[3] = price; // price
					o[4] = number; // number
					o[5] = new Date(); // updateTime
					o[6] = number; // remainNumber
					saveBatchArray.add(o);
				} else {
					Object[] o = new Object[7];
					o[0] = accountId; // accountId
					o[1] = (price); // price
					o[2] = (number); // number
					o[3] = new Date(); // updateTime
					o[4] = (mr.getRemainNumber() + number); // remainNumber
					o[5] = mr.getRemainNumber(); // beforeRemainNumber
					o[6] = storeId; // storeId
					updateBatchArray.add(o);
				}
			}
		}
		
		List<Object[]> batchArray = new ArrayList<Object[]>();
		
		if( saveBatchArray.size() > 0 ){
			messageRechargeDao.batchSave(saveBatchArray);
			for (Object[] object : saveBatchArray) {
				Object[] o = new Object[7];
				o[0] = object[0]; // id
				o[1] = object[1]; // storeId
				o[2] = object[2]; // accountId
				o[3] = object[3]; // price
				o[4] = object[4]; // number
				o[5] = object[5]; // updateTime
				o[6] = 0; // remainNumber 
				batchArray.add(o);
			}
		} 
		
		if( updateBatchArray.size() > 0 ){
			messageRechargeDao.batchUpdate(updateBatchArray);
			for (Object[] object : updateBatchArray) {
				Object[] o = new Object[7];
				o[0] = idService.getId(); // id
				o[1] = object[6]; // storeId
				o[2] = object[0]; // accountId 
				o[3] = object[1]; // price
				o[4] = object[2]; // number
				o[5] = object[3]; // updateTime
				o[6] = object[5]; // remainNumber
				batchArray.add(o);
			}
		}
		
		if( batchArray.size() > 0 ){
			messageRechargeDao.batchSaveRecord(batchArray);
		}
	}

	public Page findStoreList(String modelStoreName, Page page) {
		return messageRechargeDao.findStoreList(modelStoreName,page);
	}

	public Page list(Long storeId, Long accountId, String searchTime, Page page) {
		return messageRechargeDao.list(storeId,accountId,searchTime, page);
	}

	public List<Account> findAccountList() {
		return messageRechargeDao.findAccountList();
	}

	public Page list(Long storeId,String userAccount, Integer type, String searchTime, Page page) {
		return messageRechargeDao.list(storeId, userAccount,type, searchTime, page);
	}

	public List<MessageSaleRecord> list(Long storeId, String userAccount, Integer type, String searchTime) {
		return messageRechargeDao.list(storeId, userAccount, type, searchTime);
	}	
}