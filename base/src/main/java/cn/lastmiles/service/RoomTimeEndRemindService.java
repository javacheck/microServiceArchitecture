package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.movie.RoomTimeEndRemind;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.RoomTimeEndRemindDao;

@Service
public class RoomTimeEndRemindService {
	
	@Autowired
	private RoomTimeEndRemindDao RoomTimeEndRemindDao;
	@Autowired
	private IdService idService;
	
	public void add(RoomTimeEndRemind roomTimeEndRemind){
		if (roomTimeEndRemind.getId()==null) {
			roomTimeEndRemind.setId(idService.getId());
		}
		RoomTimeEndRemindDao.save(roomTimeEndRemind);
	}
	
	public void add(List<RoomTimeEndRemind> roomTimeEndReminds){
		for (RoomTimeEndRemind roomTimeEndRemind : roomTimeEndReminds) {
			add(roomTimeEndRemind);
		}
	}
	public List<RoomTimeEndRemind> findByStoreId(Long storeId){
		return RoomTimeEndRemindDao.findByStoreId(storeId);
	}
	public void deleteByStoreId(Long storeId){
		 RoomTimeEndRemindDao.deleteByStoreId(storeId);
	}

}
