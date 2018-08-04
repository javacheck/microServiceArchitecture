package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.DeviceDao;

@Service
public class DeviceService {
	@Autowired
	private DeviceDao deviceDao;
	
	public Page list(List<Store> stores, String storeName,String deviceSn,Page page) {
		return deviceDao.list(stores,storeName,deviceSn, page);
	}
	
	public boolean update(Device device) {
		return deviceDao.update(device);
	}

	public boolean save(Device device) {
		return deviceDao.save(device);
	}
	
	public Device findById(Long id){
		return deviceDao.findById(id);
	}
	public boolean delete(Long id) {
		return deviceDao.delete(id);
	}
	
	public boolean checkDeviceSn(Long id,String deviceSn) {
		return deviceDao.checkDeviceSn(id,deviceSn) != null ? true : false;
	}

	public boolean checkSerialId(Long id,String serialId) {
		return deviceDao.checkSerialId(id,serialId) != null ? true : false;
	}
	
	public Device findByDeviceId(String deviceSn) {
		return deviceDao.findByDeviceSn(deviceSn);
	}

	public Device findByStoreId(Long id) {
		return deviceDao.findByStoreId(id);
	}

	public Device findBySerialId(String serialId) {
		return deviceDao.findBySerialId(serialId.trim());
	}

	public List<Device> findDeviceListByStoreId(String storeIdString) {
		return deviceDao.findDeviceListByStoreId(storeIdString);
	}
}