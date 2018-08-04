package cn.lastmiles.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.dao.RoomCategoryDateSettingDao;

@Service
public class RoomCategoryDateSettingService {
	
	@Autowired
	private RoomCategoryDateSettingDao roomCategoryDateSettingDao;
	
	public RoomCategoryDateSetting findByCategoryIdAndDateSettingId(Long categoryId,Long dateSettingId){
		return roomCategoryDateSettingDao.findByCategoryIdAndDateSettingId(categoryId,dateSettingId);
	}

	public void save(RoomCategoryDateSetting roomCategoryDateSetting) {
		roomCategoryDateSettingDao.save(roomCategoryDateSetting);
	}

	public void deleteByCategoryId(Long categoryId) {
		roomCategoryDateSettingDao.deleteByCategoryId(categoryId);
	}

	public int findByDateSettingId(Long dateSettingId) {
		return roomCategoryDateSettingDao.findByDateSettingId(dateSettingId);
	}
}
