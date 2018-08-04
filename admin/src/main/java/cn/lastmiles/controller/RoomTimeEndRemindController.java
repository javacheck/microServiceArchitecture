package cn.lastmiles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.movie.RoomTimeEndRemind;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.service.RoomTimeEndRemindService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("roomTimeEndRemind")
public class RoomTimeEndRemindController {
	
	@Autowired
	private RoomTimeEndRemindService roomTimeEndRemindService;
	
	
	@RequestMapping(value = "add",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String add(String json){
		List<RoomTimeEndRemind> roomTimeEndReminds=JsonUtils.jsonToList(json, RoomTimeEndRemind.class);
		for (RoomTimeEndRemind roomTimeEndRemind : roomTimeEndReminds) {
			roomTimeEndRemind.setStoreId(SecurityUtils.getAccountStoreId());
		}
		roomTimeEndRemindService.deleteByStoreId(SecurityUtils.getAccountStoreId());
		roomTimeEndRemindService.add(roomTimeEndReminds);
		return "1";
	}
	@RequestMapping(value = "find",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object find(){
		List<RoomTimeEndRemind> roomTimeEndReminds =roomTimeEndRemindService.findByStoreId(SecurityUtils.getAccountStoreId());
		return roomTimeEndReminds.size()==0?-1:roomTimeEndReminds;
	}
	
	
	

}
