package cn.lastmiles.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.ActivityService;
import cn.lastmiles.utils.FileServiceUtils;

@Controller
@RequestMapping("activity")
public class ActivityController {
	@Autowired
	private ActivityService activityService;

	@RequestMapping("list")
	public String list() {
		return "activity/list";
	}

	@RequestMapping("list-data")
	public String listData(Page page, String name, Model model) {
		model.addAttribute("page", activityService.list(page, name));
		return "activity/list-data";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		return "activity/add";
	}
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model) {
		Activity activity =activityService.findById(id);
		if (activity!=null) {
			activity.setImageUrl(FileServiceUtils.getFileUrl(activity.getImageId()));
		}
		model.addAttribute("activity", activity);
		return "activity/add";
	}
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Activity activity,String beginTime,String endTime,@RequestParam("activityImage") MultipartFile imageFile, Model model) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		activity.setStartDate(df.parse(beginTime));//转换时间
		activity.setEndDate(df.parse(endTime));//转换时间
		if (null==activity.getId()) {
			activityService.save(activity,imageFile);
		}else{
			activityService.update(activity,imageFile);
		}
		return "redirect:/activity/list";
	}
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id ,Model model) {
		activityService.delete(id);
		return "1";
	}
}
