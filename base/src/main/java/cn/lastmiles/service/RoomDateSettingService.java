/**
 * createDate : 2016年4月8日上午10:24:26
 */
package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.RoomDateSettingDao;

@Service
public class RoomDateSettingService {
	private final static Logger logger = LoggerFactory.getLogger(RoomDateSettingService.class);
	@Autowired
	private RoomDateSettingDao roomDateSettingDao;
	@Autowired
	private RoomCategoryDateSettingService roomCategoryDateSettingService;
	@Autowired
	private IdService idService;

	public List<DateSetting> findByStoreId(Long storeId,Integer type) {
		return roomDateSettingDao.findByStoreId(storeId,type);
	}
	public DateSetting findById(Long id) {
		return roomDateSettingDao.findById(id);
	}
	
	/**
	 * 查找可预约时间段
	 * @param storeId
	 * @param type
	 * @param object 
	 * @return
	 */
	public List<DateSetting> findCanBooingDate(Date date,Long roomId,Long storeId,Integer type,Long categoryId,Long id) {
		List<DateSetting>  dateSettings= roomDateSettingDao.findCanBooingDate(date,roomId,storeId,categoryId,type);
		if (id!=null) {
			DateSetting dateSetting =this.findById(id);
			if (dateSetting!=null) {
				dateSettings.add(dateSetting);
			}
		}
		return dateSettings;
	}
	/**
	 * 
	 * @param storeId
	 * @param type	
	 * @return
	 */
	public List<DateSetting> findByStoreId(Long categoryId,Long storeId,Integer type) {
		List<DateSetting> dateSettings = roomDateSettingDao.findByStoreId(storeId,type);
		for (DateSetting dateSetting : dateSettings) {
			RoomCategoryDateSetting roomCategoryDateSetting =roomCategoryDateSettingService.findByCategoryIdAndDateSettingId(categoryId, dateSetting.getId());
			if (roomCategoryDateSetting!=null) {
				dateSetting.setCategoryPrice(roomCategoryDateSetting.getPrice());
				dateSetting.setUserPrice(roomCategoryDateSetting.getUserPrice());
			}
		}
		return dateSettings;
	}

	public boolean save(DateSetting dateSetting) {
		if( null == dateSetting.getType()){
			logger.debug("save dataSetting is Failure , reason : type is null ");
			return false;
		}
		int type = dateSetting.getType().intValue();
		
		switch(type){
			case Constants.RoomDateSetting.TYPE_SPECIAL : 
				return saveSpecialDateSetting(dateSetting);
			case Constants.RoomDateSetting.TYPE_HOLIDAYS : 
				return saveHolidaysDateSetting(dateSetting);
			default : break;
		}
		return true;
	}

	private boolean saveHolidaysDateSetting(DateSetting dateSetting) {
		Long storeId = dateSetting.getStoreId();
		String[] dateArray = dateSetting.getCacheMapData();
		
		List<Object[]> batchInsertArgs = new ArrayList<Object[]>();
		for (String dates : dateArray) {
			Object[] arg = new Object[4];
			arg[0] = idService.getId(); // id
			arg[1] = storeId; // storeId
			arg[2] = Constants.RoomDateSetting.TYPE_HOLIDAYS; // type
			arg[3] = dates;
			batchInsertArgs.add(arg);
		}
		return roomDateSettingDao.saveHoliday(batchInsertArgs,storeId);
	}

	@Transactional
	private boolean saveSpecialDateSetting(DateSetting dateSetting) {
		Long storeId = dateSetting.getStoreId();
		Long[] idArray = dateSetting.getIdArray();
		String[] nameArray = dateSetting.getNameArray();
		Double[] startTimeArray = dateSetting.getStartTimeArray();
		Double[] endTimeArray = dateSetting.getEndTimeArray();
		Integer[] typeArray = dateSetting.getTypeArray();

		List<Object[]> batchInsertArgs = new ArrayList<Object[]>();
		List<Object[]> batchUpdateArgs = new ArrayList<Object[]>();
		for (int i = 0; i < idArray.length; i++) {
			if( ObjectUtils.equals(idArray[i], -1000L) ){ // 新增的数据
				Object[] arg = new Object[6];
				arg[0] = idService.getId(); // id
				arg[1] = storeId; // storeId
				arg[2] = typeArray[i]; // type
				arg[3] = startTimeArray[i]; // startTime
				arg[4] = endTimeArray[i]; // endTime
				arg[5] = nameArray[i]; // name
				batchInsertArgs.add(arg);							
			} else {
				Object[] arg = new Object[6];
				arg[0] = storeId; // storeId
				arg[1] = typeArray[i]; // type
				arg[2] = startTimeArray[i]; // startTime
				arg[3] = endTimeArray[i]; // endTime
				arg[4] = nameArray[i]; // name
				arg[5] = idArray[i]; // id
				batchUpdateArgs.add(arg);	
			}
		}
		boolean insertFlag = true , updateFlag = true;
		if( batchInsertArgs.size() > 0  ){ // 新增
			insertFlag = roomDateSettingDao.save(batchInsertArgs);
		}
		if( batchUpdateArgs.size() > 0 ){ // 修改
			updateFlag = roomDateSettingDao.update(batchUpdateArgs);
		}
		return insertFlag && updateFlag;
	}

	public int deleteByIdAndStoreId(Long storeId, Long id) {
		return roomDateSettingDao.deleteByIdAndStoreId(storeId,id);
	}
	
	public int deleteByStoreIdAndType(Long storeId,Integer type) {
		return roomDateSettingDao.deleteByStoreIdAndType(storeId, type);
	}
	public List<DateSetting> findByTime(Date beginTime, Date endTime, Long storeId) {
		return roomDateSettingDao.findByTime(beginTime, endTime,storeId);
	}
}