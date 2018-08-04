package cn.lastmiles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.UserScreenAdService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("userScreenAd")
public class UserScreenAdController {
	private final static Logger logger = LoggerFactory.getLogger(UserScreenAdController.class);
	@Autowired
	private UserScreenAdService userScreenAdService;
	
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/userScreenAd/list";
	}
	/**
	 * 所有客屏广告列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "userScreenAd/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Page page, Model model) {
		Long storeId=SecurityUtils.getAccountStoreId();
		model.addAttribute("data", userScreenAdService.findAll(storeId, page));
		return "userScreenAd/list-data";
	}
	
	
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "userScreenAd/add";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(@RequestParam("htmlImageId") MultipartFile htmlImageId, Model model) {
		Long storeId=SecurityUtils.getAccountStoreId();
		userScreenAdService.edituserScreenAd(storeId,htmlImageId);
		return "redirect:/userScreenAd/list";
	}
	
	@RequestMapping(value="delete/delete-by-imageId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(String imageId){
		logger.debug("imageId={}",imageId);
		userScreenAdService.deleteByImageId(imageId);
		return "1";
	}
}
