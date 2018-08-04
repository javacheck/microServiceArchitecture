package cn.lastmiles.controller;
/**
 * updateDate : 2015-07-17 AM 10:28
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.APIResponse;
import cn.lastmiles.bean.APK;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ApkService;

/**
 * APK新增和修改 
 */
@Controller
@RequestMapping("apk")
public class ApkController {

	@Autowired
	private ApkService apkService; // APK
	@Autowired
	private IdService idService; // ID自动生成
	@Autowired
	private FileService fileService; // 文件
	
	private static final Logger logger = LoggerFactory.getLogger(ApkController.class);
	
	@RequestMapping(value = "getVersion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public APIResponse getVersion(int type){
		APK apk = apkService.getLastest(type);
		
		if (apk != null){
			if (type == 3){
				apk.setFileId("http://www.lastmiles.cn/lastmiles_app_center.apk");
			}
		}
		logger.debug("更新返回：{}",apk);
		return APIResponse.success(apk);
	}
	
	/**
	 * APK菜单链接
	 * @param model
	 */
	@RequestMapping("list")
	public String list() {
		return "apk/list";
	}
	
	/**
	 * APK详情搜索查询
	 * @param page
	 * @param model
	 */
	@RequestMapping("list-data")
	public String listData(Page page, Model model) {
		model.addAttribute("data", apkService.list(page));				
		return "apk/list-data";
	}
	
	/**
	 * 新增APK信息跳转
	 * @param model
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(){
		return "/apk/add";
	}
	
	/**
	 * 修改APK信息跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		model.addAttribute("apk",apkService.findById(id));
		return "/apk/add";
	}
	
	/**
	 * 保存和修改APK信息
	 * @param apk APK对象
	 * @param model
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(APK apk, Model model) {
//		try {
//			String fileId = fileService.save(apkFile.getInputStream(),apk.getName()+apk.getVersion()+".apk");
//			apk.setFileId(fileId);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		if( null == apk.getId()){
			apkService.save(apk);			
		} else {
			apkService.update(apk);
		}
		
		return "redirect:/apk/list";
	}
	
	/**
	 * 检测APK名称是否唯一
	 * @param name APK名称
	 * @return 1 表示数据库中已存在
	 */
	@RequestMapping(value="list/ajax/checkApkName",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkApkName(Long id ,String name){
		if( apkService.checkApkName(id,name) ){
			return 1;
		}
		return 0;
	}
}