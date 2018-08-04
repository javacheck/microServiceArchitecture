package cn.lastmiles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.HtmlAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.HtmlAdService;
import cn.lastmiles.service.ShopService;

@Controller
@RequestMapping("htmlAd")
public class HtmlAdController {
	private final static Logger logger = LoggerFactory.getLogger(HtmlAdController.class);
	
	@Autowired
	private HtmlAdService htmlAdService;
	
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/htmlAd/list";
	}
	/**
	 * 所有商品属性列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "htmlAd/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Long storeId, Page page, Model model) {

		model.addAttribute("data", htmlAdService.findAll(storeId, page));
		return "htmlAd/list-data";
	}
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		page = shopService.getShop(name, mobile, agentName, Constants.Status.SELECT_ALL, page);
		model.addAttribute("data", page);
		return "htmlAd/showModelList-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "htmlAd/add";
	}
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		logger.debug("toUpdate  id -->"+id);
		model.addAttribute("htmlAd",htmlAdService.findById(id));
		return "/htmlAd/add";
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(HtmlAd htmlAd,@RequestParam("htmlImageId") MultipartFile htmlImageId,Long areaId, Model model) {
		logger.debug("htmlAd={}",htmlAd);
		logger.debug("areaId={}",areaId);
		logger.debug("htmlImageId={}",htmlImageId);
		if(htmlAd.getShare()==null){//0没有分享，1有分享
			htmlAd.setShare(0);
		}
		htmlAd.setCityId(areaId);
		htmlAdService.editHtmlAd(htmlAd,htmlImageId);
		return "redirect:/htmlAd/list";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		logger.debug("id={}",id);
		return htmlAdService.delete(id) ? "1":"0";
	}
}
