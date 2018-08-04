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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Supplier;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.SupplierService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("supplier")
public class SupplierController {
	private final static Logger logger = LoggerFactory.getLogger(SupplierController.class);
	
	@Autowired
	private SupplierService supplierService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/supplier/list";
	}
	
	/**
	 * 所有供应商列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "supplier/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name,Long storeId,String phone, Page page, Model model) {
		
		storeId=SecurityUtils.getAccountStoreId();
		
		model.addAttribute("data", supplierService.findAll(storeId,name.trim(),phone.trim(), page));
		return "supplier/list-data";
		
	}
	
	/**
	 * 跳到供应商增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		
		return "supplier/add";
	}
	
	/**
	 * 查询商品属性是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在) 2表示该属性值
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findSupplier(Supplier supplier) {
		logger.debug("supplier={}",supplier);
		supplier.setStoreId(SecurityUtils.getAccountStoreId());
		supplier.setName(supplier.getName().replaceAll("\\s*", ""));
		String flag=supplierService.findSupplier(supplier)==null?"0":"1";
		return flag;
	}
	
	/**
	 * 编辑供应商（增加或修改）
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editSupplier(Supplier supplier){
		logger.debug("supplier={}",supplier);
		Long storeId=SecurityUtils.getAccountStoreId();
		supplier.setStoreId(storeId);
		supplierService.editSupplier(supplier);
		
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
		Supplier supplier= supplierService.findById(id);//把productAttribute对像转回页面
		model.addAttribute("supplier", supplier);
		return "/supplier/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value="delete/delete-by-supplierId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		supplierService.deleteById(id);
		return "1";
	}
}
