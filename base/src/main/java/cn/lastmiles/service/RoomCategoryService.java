package cn.lastmiles.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.RoomCategoryDao;

@Service
public class RoomCategoryService {
	@Autowired
	private RoomCategoryDao RoomCategoryDao;
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomCategoryDateSettingService roomCategoryDateSettingService;
	@Autowired
	private IdService idService;
	@Transactional
	public void add (RoomCategory roomCategory, List<RoomCategoryDateSetting> roomCategoryDateSettings){
		if (roomCategory.getId()==null) {
			roomCategory.setId(idService.getId());
		}
		roomCategory.setCreateDate(new Date());
		roomCategory.setUpdateDate(new Date());
		RoomCategoryDao.save(roomCategory);
		if (roomCategoryDateSettings!=null) {
			for (RoomCategoryDateSetting roomCategoryDateSetting : roomCategoryDateSettings) {
				roomCategoryDateSetting.setCategoryId(roomCategory.getId());
				roomCategoryDateSettingService.save(roomCategoryDateSetting);
			}
		}
	}
	
	public List<RoomCategory> findByStoreId (Long storeId){
		return RoomCategoryDao.findByStoreId(storeId);
	}
	@Transactional
	public void update(RoomCategory roomCategory, List<RoomCategoryDateSetting> roomCategoryDateSettings) {
		RoomCategoryDao.update(roomCategory);
		if (roomCategoryDateSettings!=null) {
			roomCategoryDateSettingService.deleteByCategoryId(roomCategory.getId());//删除原来关联
			for (RoomCategoryDateSetting roomCategoryDateSetting : roomCategoryDateSettings) {
				roomCategoryDateSetting.setCategoryId(roomCategory.getId());
				roomCategoryDateSettingService.save(roomCategoryDateSetting);
			}
		}
	}

	public Page list(Long storeId, Page page) {
		return RoomCategoryDao.list(storeId, page);
	}
	
	@Transactional
	public boolean delete(Long id) {
		if (roomService.checkCategoryIsUse(id)) {
			return false;
		}
		RoomCategoryDao.delete(id);
		return true;
	}
	public RoomCategory findById(Long id) {
		return RoomCategoryDao.findById(id);
	}

	public boolean checkName(Long id, String name,Long storeId) {
		return RoomCategoryDao.checkName(id,name,storeId);
	}
}
