package cn.lastmiles.controller;
/**
 * createDate : 2015年8月10日 下午2:11:56 
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.ActivityResultService;
import cn.lastmiles.utils.FileServiceUtils;

@Controller
@RequestMapping("activityResultAudit")
public class ActivityResultAuditController {

	@Autowired
	private ActivityResultService activityResultService;
	
	@RequestMapping("list")
	public String list(){
		return "activityResultAudit/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String mobile,Integer status,Page page,Model model){
		page = activityResultService.getAuditInfo(mobile,status,page);
		model.addAttribute("page", page);
		return "activityResultAudit/list-data";
	}
	
	@RequestMapping(value = "list/ajax/auditOperation",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String auditOperation(Long userId,Integer status,Long id){
		if(activityResultService.updateStatus(id,userId,status)){
			return "1";
		}
		return "0";
	}
	
	@RequestMapping(value = "showMode/auditWindow/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> getInfo(Long id,Long userId,Model model){
		List<Map<String, Object>> activityResult = activityResultService.findByIdAndUserId(id,userId);
		
		if (activityResult!=null) {
			activityResult.get(0).put("imageId", FileServiceUtils.getFileUrl((String) activityResult.get(0).get("imageId")));
		}
		return activityResult.get(0);
	}
}