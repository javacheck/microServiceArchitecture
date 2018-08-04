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

import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.RoomCategoryDateSettingService;
import cn.lastmiles.service.RoomDateSettingService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("room/roomDateSetting")
public class RoomDateSettingController {
	private final static Logger logger = LoggerFactory.getLogger(RoomDateSettingController.class);
	@Autowired
	private RoomDateSettingService roomDateSettingService;
	@Autowired
	private RoomCategoryDateSettingService roomCategoryDateSettingService;
	
	@RequestMapping("list")
	public String list(Model model) {
		return "roomDateSetting/list";
	}
		
	@RequestMapping(value = "getDateSetting")
	@ResponseBody
	public List<DateSetting> getDateSetting() {
		List<DateSetting> dateSettingList = roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(),Constants.RoomDateSetting.TYPE_HOLIDAYS);
		logger.debug("getDateSetting dateSettingList is {}",dateSettingList);
		
		return dateSettingList;
	}
	
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping(value = "addOrUpdateBy")
	public String addOrUpdate(Model model) {
		
		List<DateSetting> dateSettingGeneralList = roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(),Constants.RoomDateSetting.TYPE_SPECIAL);
		List<DateSetting> dateSettingFestivalsList = roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(),Constants.RoomDateSetting.TYPE_FESTIVALS);
		logger.debug("addOrUpdateBy dateSettingGeneralList is {},dateSettingFestivalsList is {}",dateSettingGeneralList,dateSettingFestivalsList);
		
		model.addAttribute("dateSettingFestivalsList",dateSettingFestivalsList);
		model.addAttribute("dateSettingGeneralList",dateSettingGeneralList);
		return "/roomDateSetting/add";
	}
	
	/**
	 * 数据 修改||添加 界面
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(DateSetting dateSetting){
		logger.debug("save dateSetting is :{}",dateSetting);
		
		dateSetting.setStoreId(SecurityUtils.getAccountStoreId());		
		
		boolean success = roomDateSettingService.save(dateSetting);
		logger.debug("save dateSetting is {}",success );
		
		return "redirect:/room/list";
	}
		
	@RequestMapping(value = "ajax/delete")
	@ResponseBody
	public int delete(Long id) {
		logger.debug("delete id is {}",id);
		int count = roomCategoryDateSettingService.findByDateSettingId(id);
		if( count > 0 ){
			logger.debug("存在交互,不能删除");
			return 2;
		}
		return roomDateSettingService.deleteByIdAndStoreId(SecurityUtils.getAccountStoreId(),id) > 0 ? 1: 0 ;
	}
	
	@RequestMapping(value = "ajax/addOrUpdateByHoliday")
	@ResponseBody
	public List<DateSetting> addOrUpdateByHoliday() {
		List<DateSetting> dateSettingList = roomDateSettingService.findByStoreId(SecurityUtils.getAccountStoreId(),Constants.RoomDateSetting.TYPE_HOLIDAYS);
		logger.debug("addOrUpdateByHoliday dateSettingList is {}",dateSettingList);
		
		return dateSettingList;
	}
	
	@RequestMapping(value = "ajax/saveHoliday")
	@ResponseBody
	public int saveHoliday(String[] cacheMapData) {
		if( null != cacheMapData && cacheMapData.length > 0){
			DateSetting dateSetting = new DateSetting();
			dateSetting.setStoreId(SecurityUtils.getAccountStoreId());	
			dateSetting.setType(Constants.RoomDateSetting.TYPE_HOLIDAYS);
			dateSetting.setCacheMapData(cacheMapData);
			boolean success = roomDateSettingService.save(dateSetting);
			logger.debug("save dateSetting is {}",success );
			if(success){
				return 1;
			}
		} else {
			return roomDateSettingService.deleteByStoreIdAndType(SecurityUtils.getAccountStoreId(),Constants.RoomDateSetting.TYPE_HOLIDAYS);
		}
		return 0;
	}
}
