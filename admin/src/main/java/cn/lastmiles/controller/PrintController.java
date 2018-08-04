package cn.lastmiles.controller;


import java.util.List;

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

import cn.lastmiles.bean.Print;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.PrintService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("print")
public class PrintController {
	private final static Logger logger = LoggerFactory.getLogger(PrintController.class);
	
	@Autowired
	private PrintService printService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/print/list";
	}
	
	/**
	 * 所有打印机列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "print/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String printSn,Long storeId,Integer status, Page page, Model model) {
		if(SecurityUtils.isStore()){//商家
			storeId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("data", printService.findAll(storeId,printSn.trim(),status, page));
		return "print/list-data";
		
	}
	
	// 弹窗
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		page = shopService.getShop(name, mobile, agentName, Constants.Status.SELECT_ALL, page);
		model.addAttribute("data", page);
		return "print/showModelList-data";
	}
	
	/**
	 * 跳到商打机增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		Long isStoreId=null;
	 	if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isStoreId",isStoreId);
		return "print/add";
	}
	
	/**
	 * 查询打印机是否已存在
	 * @param
	 * @return 
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findPrint(Print print) {
		logger.debug("print={}",print);
		
		print.setPrintSn(print.getPrintSn().trim());
		print.setPrintKey(print.getPrintKey().trim());
		String flag=printService.findPrint(print)==null?"0":"1";
		logger.debug("flag={}",flag);
		return flag;
	}
	
	/**
	 * 保存增加打印机或保存修改打印机
	 * 
	 * @param 
	 * @return 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editProductAttribute(Long id,Long storeId,String printName,String printSn,String printKey,String memo,String categoryIds,String categoryNames){
		logger.debug("storeId={},printSn={},printKey={}",storeId,printSn,printKey);
		printService.editPrint(id,storeId,printName.trim(),printSn.trim(),printKey.trim(),memo.trim(),categoryIds,categoryNames);
		return "1";	
	}
	
	/**
	 * 跳转修改界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		Print print= printService.findById(id);//把print对像转回页面
		model.addAttribute("print", print);
		
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "/print/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value="delete/delete-by-printId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		printService.deleteById(id);
		return "1";
	}
	
	/**
	 * 一级分类List
	 * @param 
	 * @return
	 */
	@RequestMapping(value="findCategoryList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> findCategoryList(Long storeId) {
		logger.debug("storeId={}",storeId);
		List<ProductCategory> cList=printService.findCategoryList(storeId);
		logger.debug("cList={}",cList);
		return cList;
	}
	
	@RequestMapping(value="typeChange/change-by-printId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String typeChangeByPrintId(Long id,Integer status){
		logger.debug("状态==="+status);
		printService.typeChangeByPrintId(id,status);
		return "1";
	}
}
