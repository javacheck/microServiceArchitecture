/**
 * createDate : 2016年5月20日上午11:01:52
 */
package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Inform;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.InformDao;

@Service
public class InformService {
	
	private static final Logger logger = LoggerFactory.getLogger(InformService.class);
	@Autowired
	private InformDao informDao;
	@Autowired
	private IdService idService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private StoreService storeService;

	public Page list(String storeIdString,String name, Page page) {
		return informDao.list(storeIdString,name,page);
	}

	public boolean save(Inform inform) {
		logger.debug("save inform info is {}",inform);
		
		if( null == inform.getId() ){
			inform.setId(idService.getId());
		}
		
		if( null == inform.getCreatedTime() ){
			inform.setCreatedTime(new Date());
		}
		StringBuffer storeIdString = new StringBuffer();
		if( ObjectUtils.equals(-1L,inform.getIsMainStoreId()) ){ // 如果是全部商家
			Long mainStoreId = inform.getStoreId();
			if( null != mainStoreId ){

				List<Store> storeList = storeService.findByParentId(mainStoreId);
				boolean index = false;
				for (Store store : storeList) {
					if(index){
						storeIdString.append(",");
					}
					storeIdString.append(store.getId());
					index = true;
				}			
			}
		} else { // 单个商家
			storeIdString.append(inform.getStoreId());			
		}
		
		List<Device> deviceList = deviceService.findDeviceListByStoreId(storeIdString.toString());
		
		logger.debug("确定的商家ID组合是：{},确定的设备集合是：{}",storeIdString.toString(),deviceList);
		
		Long informId = inform.getId();
		List<Object[]> batchArr = new ArrayList<Object[]>();
		if( null != deviceList && deviceList.size() > 0 ){
			for (Device device : deviceList) {
				Object[] o = new Object[6];
				o[0] = idService.getId(); // id 
				o[1] = informId; // informId
				o[2] = device.getId(); // deviceId
				o[3] = new Date(); // createdTime
				o[4] = Constants.InformMiddleDevice.NOTREAD; // markRead
				o[5] = null; // readTime
				batchArr.add(o);	
			}
		}
		informDao.save(inform); // 保存通知信息
		if( batchArr.size() > 0 ){
			informDao.batchSave(batchArr);// 保存中间表信息
			return true;
		}
		return false;
	}

	public List<Inform> findInformByDeviceId(Long deviceId,String storeIdString) {
		return informDao.findInformByDeviceId(deviceId,storeIdString);
	}

	public boolean updateIsRead(Long informId, Long deviceId) {
		return informDao.updateIsRead(informId,deviceId);
	}
}