package cn.lastmiles.controller;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Activity;
import cn.lastmiles.bean.ActivityDetail;
import cn.lastmiles.bean.ActivityDetailLocation;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.ActivityDetailLocationService;
import cn.lastmiles.service.ActivityDetailService;
import cn.lastmiles.service.ActivityService;
import cn.lastmiles.utils.FileServiceUtils;

@Controller
@RequestMapping("activityDetail")
public class ActivityDetailController {
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityDetailService activityDetailService;
	@Autowired
	private ActivityDetailLocationService activityDetailLocationService;
	
	@RequestMapping(value="list")
	public String list(Page page,Long activityId, Model model){
		return "/activityDetail/list";
	}
	
	@RequestMapping(value="list-data")
	public String listData(Page page,Long activityId, Model model){
		activityDetailService.list(page, activityId);
		return "/activityDetail/list-data";
	}
	
	@RequestMapping(value="add/{activityId}" , method = RequestMethod.GET)
	public String toAdd(@PathVariable Long activityId,Model model){
		Activity activity =activityService.findById(activityId);
		model.addAttribute("activity",activity);
		return "/activityDetail/add";
	}
	@RequestMapping(value="update/{id}" , method = RequestMethod.GET)
	public String update(@PathVariable Long id ,Model model){
		ActivityDetail activityDetail =activityDetailService.findById(id);
		activityDetail.setImageUrl(FileServiceUtils.getFileUrl(activityDetail.getImageId()));
		model.addAttribute("activityDetail",activityDetail);
		model.addAttribute("activity",activityService.findById(activityDetail.getActivityId()));
		model.addAttribute("activityDetailLocations",activityDetailLocationService.findByActivityDetailId(id));
		return "/activityDetail/add";
	}
	@RequestMapping(value="add" , method = RequestMethod.POST)
	public String add(ActivityDetail activityDetail,String beginTime,String endTime,@RequestParam("activityDetailImage") MultipartFile imageFile,String [] treasureName,String [] shopName,Model model) throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		activityDetail.setStartDate(df.parse(beginTime));//转换时间
		activityDetail.setEndDate(df.parse(endTime));//转换时间
		List<ActivityDetailLocation> activityDetailLocations = new ArrayList<ActivityDetailLocation>();
		if (treasureName!=null&&shopName!=null) {
			for (int i = 0; i < shopName.length&&i < treasureName.length; i++) {
				ActivityDetailLocation activityDetailLocation = new ActivityDetailLocation();
				activityDetailLocation.setId(activityDetail.getId());
				activityDetailLocation.setName(treasureName[i]);
				activityDetailLocation.setShopName(shopName[i]);
				activityDetailLocations.add(activityDetailLocation);
			}
		}
		if (null==activityDetail.getId()) {
			activityDetailService.save(activityDetail,imageFile,activityDetailLocations);
			return "redirect:/activity/list";
		}else{
			activityDetailService.update(activityDetail,imageFile,activityDetailLocations);
			return "redirect:/activityDetail/list";
		}
	}
	@RequestMapping(value="delete/{id}" )
	@ResponseBody
	public String delete(@PathVariable Long id ,Model model){
		activityDetailService.delete(id);
		return "1";
	}
	
}
