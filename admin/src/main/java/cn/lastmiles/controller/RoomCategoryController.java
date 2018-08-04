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

import cn.lastmiles.bean.Verification;
import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.RoomCategoryDateSettingService;
import cn.lastmiles.service.RoomCategoryService;
import cn.lastmiles.service.RoomDateSettingService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("roomCategory")
public class RoomCategoryController {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomCategoryController.class);
	@Autowired
	private RoomCategoryService roomCategoryService;
	
	@Autowired
	private RoomDateSettingService roomDateSettingService;
	
	@Autowired
	private RoomCategoryDateSettingService roomCategoryDateSettingService;
	
	@RequestMapping("list")
	public String list(Model model) {
		//model.addAttribute("categorys",	);
		return "roomCategory/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String number,Integer status ,Long categoryId, Page page, Model model) {
		model.addAttribute("data",roomCategoryService.list(SecurityUtils.getAccountStoreId(),page));
		return "/roomCategory/list-data";
	}
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		model.addAttribute("dateSettingSpecials",roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(), Constants.RoomDateSetting.TYPE_SPECIAL));
		model.addAttribute("dateSettingFestivals",roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(), Constants.RoomDateSetting.TYPE_FESTIVALS));
		return "/roomCategory/add";
	}
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value = "update/{id}")
	public String toUpdate(@PathVariable Long id,Model model) {
		model.addAttribute("roomCategory",roomCategoryService.findById(id));
		List<DateSetting> dateSettingSpecials= roomDateSettingService.findByStoreId(id,SecurityUtils.getAccountStoreId(), Constants.RoomDateSetting.TYPE_SPECIAL);
		List<DateSetting> dateSettingFestivals= roomDateSettingService.findByStoreId(id,SecurityUtils.getAccountStoreId(), Constants.RoomDateSetting.TYPE_FESTIVALS);
		model.addAttribute("dateSettingSpecials",dateSettingSpecials);
		model.addAttribute("dateSettingFestivals",dateSettingFestivals);
		return "/roomCategory/add";
	}
	
	/**
	 * 数据 修改||添加 界面
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(RoomCategory roomCategory,String dateSettingsJson){
		logger.debug("roomCategory is {},dateSettingsJson is {}",roomCategory,dateSettingsJson);
		roomCategory.setCreateAccountId(SecurityUtils.getAccountId());
		roomCategory.setUpdateAccountId(SecurityUtils.getAccountId());
		roomCategory.setStoreId(SecurityUtils.getAccountStoreId());
		logger.debug("add roomCategory is :{}",roomCategory);
		List<RoomCategoryDateSetting> roomCategoryDateSettings=JsonUtils.jsonToList(dateSettingsJson,RoomCategoryDateSetting.class);
		if (roomCategory.getId()==null) {
			roomCategoryService.add(roomCategory,roomCategoryDateSettings);
		}else {
			roomCategoryService.update(roomCategory,roomCategoryDateSettings);
		}
		return "redirect:/roomCategory/list";
	}
	
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id, Model model) {
		return roomCategoryService.delete(id)?"1":"0";
	}
	@RequestMapping(value = "checkName",produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String checkName(Long id, String name) {
		logger.debug("checkName id is {},name is {}",id,name);
		if (roomCategoryService.checkName(id,name,SecurityUtils.getAccountStoreId())) {
			return Verification.success();
		}else{
			return Verification.error("类型名称已存在");
		}
	}
}
