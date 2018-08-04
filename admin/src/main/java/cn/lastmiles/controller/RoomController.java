package cn.lastmiles.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.Verification;
import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.RoomCategoryService;
import cn.lastmiles.service.RoomService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("room")
public class RoomController {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomController.class);
	@Autowired
	private RoomService roomService;
	@Autowired
	private RoomCategoryService roomCategoryService;
	
	
	
	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("categorys",roomCategoryService.findByStoreId(SecurityUtils.getAccountStoreId())	);
		return "room/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String number,Integer status ,Long categoryId, Page page, Model model) {
		model.addAttribute("data",roomService.list(SecurityUtils.getAccountStoreId(),number,status,categoryId,page));
		return "/room/list-data";
	}
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model,RedirectAttributes ra) {
		List<RoomCategory> categorys=roomCategoryService.findByStoreId(SecurityUtils.getAccountStoreId());
		if (categorys.isEmpty()) {
			ra.addFlashAttribute("room_warning_msg","请先添加包间类型");
			return "redirect:/room/list";
		}
		model.addAttribute("categorys",categorys);
		return "/room/add";
	}
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model) {
		model.addAttribute("categorys",roomCategoryService.findByStoreId(SecurityUtils.getAccountStoreId()));
		model.addAttribute("room",roomService.findById(id));
		return "/room/add";
	}
	/**
	 * 更改状态
	 */
	@RequestMapping(value = "changStatus")
	@ResponseBody
	public String changStatus(Long id,Integer status) {
		roomService.changStatus(id,status);
		return "1";
	}
	
	/**
	 * 数据 修改||添加 界面
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Room room){
		room.setStoreId(SecurityUtils.getAccountStoreId());
		logger.debug("add room is :{}",room);
		if (room.getId()==null) {
			roomService.add(room);
		}else {
			roomService.update(room);
		}
		return "redirect:/room/list";
	}
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable Long id) {
		return roomService.delete(id)?"1":"0";
	}
	@RequestMapping(value = "checkNameRepeat",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String checkNameRepeat(Long id,String name) {
		logger.debug("checkNameRepeat id is {},name is {}",id,name);
		if (roomService.checkNameRepeat(id,SecurityUtils.getAccountStoreId(),name)) {
			return Verification.success();
		}else{
			return Verification.error("包间名称已存在");
		}
	}
	@RequestMapping(value = "checkNumberRepeat",produces = "text/plain;charset=UTF-8" )
	@ResponseBody
	public String checkNumberRepeat(Long id,String number) {
		logger.debug("checkNumberRepeat id is {},name is {}",id,number);
		if (roomService.checkNumberRepeat(id,SecurityUtils.getAccountStoreId(),number)) {
			return Verification.success();
		}else{
			return Verification.error("包间号已存在");
		}
	}

}
