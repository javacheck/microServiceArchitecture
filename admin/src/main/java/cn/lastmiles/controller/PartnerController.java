package cn.lastmiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Partner;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.PartnerService;
import cn.lastmiles.service.ShopService;

/**
 * createDate : 2015年12月21日下午4:15:25
 */
@Controller
@RequestMapping("partner")
public class PartnerController {
	
	@Autowired
	private PartnerService partnerService; // 合作者
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/partner/list";
	}
	
	@RequestMapping("list")
	public String list() {
		return "partner/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String name,Page page, Model model) {
		model.addAttribute("data", partnerService.list(name,page));				
		return "partner/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(){
		return "/partner/add";
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		model.addAttribute("partner",partnerService.findById(id));
		return "/partner/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(Partner partner, Model model) {
		if( null == partner.getId()){
			partnerService.save(partner);			
		} else {
			partnerService.update(partner);
		}
		
		return "redirect:/partner/list";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id){
//		if(partnerService.findStoreById(id)){ // 商家已引用，不能删除此跳合作者记录
//			return 2;
//		};
		return partnerService.delete(id) > 0 ? 1 : 0 ;
	}
	
	@RequestMapping(value="list/ajax/checkPartnerName",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkPartnerName(Long id,String name) {
		return partnerService.checkPartnerName(id,name) > 0 ? 1 : 0;
	}
	
	@RequestMapping(value="list/ajax/checkPartnerToken",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkPartnerToken(Long id,String token) {
		return partnerService.checkPartnerToken(id,token) > 0 ? 1 : 0;
	}
	
	@RequestMapping(value = "mainShop/showMode")
	public String mainShopShowList() {
		return "partner/mainShopShowList";
	}
	
	@RequestMapping(value = "mainShopShow/list-data")
	public String mainShopShowListData(String name, Page page, Model model) {
		model.addAttribute("data", shopService.mainShoplist(name, page));
		return "partner/mainShopShowList-data";
	}
}