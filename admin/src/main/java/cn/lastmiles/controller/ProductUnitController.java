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

import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductUnitService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productUnit")
public class ProductUnitController {
	private final static Logger logger = LoggerFactory.getLogger(ProductUnitController.class);
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ProductUnitService productUnitService;
	@Autowired
	private ShopService shopService;
	@RequestMapping("")
	public String index() {
		return "redirect:/productUnit/list";
	}
	
	/**
	 * 商品单位列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "productUnit/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(String name,Long storeId, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if( null != storeId ){
			storeIdString.append(storeId);
		}else{
			storeIdString.append(SecurityUtils.getAccountStoreId());
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		model.addAttribute("data", productUnitService.findAllPage(storeIdString.toString(),name.trim(), page));
		return "productUnit/list-data";
		
	}
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isMainStore()){
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			storeIdString.append(SecurityUtils.getAccountStoreId());
			for (Store store : storeList) {
				storeIdString.append(",");
				storeIdString.append(store.getId());
			}
		}
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, "",  page);
		model.addAttribute("data", page);
		return "productUnit/showModelList-data";
	}
	
	/**
	 * 跳到商品单位增加页面
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
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		return "productUnit/add";
	}
	
	/**
	 * 查询商品单位是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductUnit(ProductUnit productUnit) {
		logger.debug("productUnit={}",productUnit);
	 	if(productUnit.getStoreId()==null){//如果是商家登录
	 		productUnit.setStoreId(SecurityUtils.getAccountStoreId());
		}	
		productUnit.setName(productUnit.getName().replaceAll("\\s*", ""));
		String flag=productUnitService.findProductUnitExist(productUnit)==null?"0":"1";
		logger.debug("flag={}",flag);
		return flag;
	}
	
	/**
	 * 保存增加商品单位或保存修改商品单位
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editProductUnit(Long id,Long storeId,String unitName[]){
		logger.debug("unitName={},storeId={}",unitName,storeId);
		String flag="1";
		if(storeId==null){//如果是商家登录
			storeId=SecurityUtils.getAccountStoreId();
		}	
		productUnitService.editProductUnit(id,storeId,unitName);
		return flag;	
	}
	
	/**
	 * 跳转修改界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		Long isStoreId=null;
	 	if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		ProductUnit productUnit= productUnitService.findProductUnitById(id);//把productUnit对像转回页面
		model.addAttribute("productUnit", productUnit);
		return "/productUnit/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value="delete/delete-by-productUnitId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		Integer count=productUnitService.findStockCountByUnitId(id);
		if(count.intValue()>0){
			return "0";
		}else{
			productUnitService.deleteById(id);
			return "1";
		}
	}
	
	@RequestMapping(value="delete/deleteAll-by-productUnitIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteAllById(String productUnitIds) {
		logger.debug("productUnitIds={}",productUnitIds);
		String[] arrIds=productUnitIds.split(",");
		boolean flag=true;
		for(int i=0;i<arrIds.length;i++){
			Long productUnitId=Long.parseLong(arrIds[i]);
			Integer count=productUnitService.findStockCountByUnitId(productUnitId);
			if(count.intValue()>0){
				flag=false;
				break;
			}
		}
		logger.debug("flag=={}",flag);
		if(flag){
			for(int i=0;i<arrIds.length;i++){
				Long productUnitId=Long.parseLong(arrIds[i]);
				productUnitService.deleteById(productUnitId);
			}
			return "1";
		}else{
			return "0";
		}
	}
}
