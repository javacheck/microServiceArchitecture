package cn.lastmiles.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.CashGift;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.service.CashGiftService;

/**
 * createDate : 2015年9月7日 下午2:49:58 
 */

@Controller
@RequestMapping("cashGift")
public class CashGiftController {

	@Autowired
	private CashGiftService cashGiftService;
	@Autowired
	private IdService idService;
	
	@RequestMapping("list")
	public String list(Model model) {
		return "cashGift/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String mobile,String shopName,Page page, Model model) {
		page = cashGiftService.list(mobile,shopName,page);
		model.addAttribute("data", page);				
		return "cashGift/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(){
		return "/cashGift/add";
	}
	
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int save(CashGift cashGift,String validTimeReplace){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			cashGift.setValidTime(df.parse(validTimeReplace + " 23:59:59")); // 此处为了之后的使用情况时间对比而加上时分秒
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cashGift.setId(idService.getId());
		return cashGiftService.save(cashGift);	
	}
	
	@RequestMapping(value="list/ajax/checkMobile",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String checkMobile(String mobile){
		String s = cashGiftService.checkMobile(mobile); 
		return 	s;
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id,Long userId){
		return cashGiftService.delete(id,userId);
	}
}