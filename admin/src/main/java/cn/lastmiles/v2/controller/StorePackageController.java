/**
 * createDate : 2016年10月25日上午10:38:48
 */
package cn.lastmiles.v2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.StorePackageService;

@RequestMapping("storePackage")
@Controller
public class StorePackageController {
	private static final Logger logger = LoggerFactory.getLogger(StorePackageController.class);
	
	@Autowired
	private StorePackageService storePackageService;
	
	@RequestMapping(value = "list")
	public String commodityManagerList(Model model) {
		return "/v2/storePackage/list";
	}
	
	@RequestMapping("list-data")
	public String storePackageListData(String name,Page page,Model model){
		logger.debug("packageListData ----->>> receive parameters name is {} ,page is {}", name,page);
		page = storePackageService.list(name,page);
		model.addAttribute("data",page);
		logger.debug("return Data is {}",page);
		return "/v2/storePackage/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "/v2/storePackage/add";
	}
	
	// 商品弹窗
	@RequestMapping("showProductStockModel/list/list-data")
	public String showProductStockModelListData(String productName,Long productCategoryId, Page page, Model model) {
		if( ObjectUtils.equals(productCategoryId, -1l) ){
			productCategoryId = null;
		}
		model.addAttribute("data", storePackageService.findProductStockList(productName,SecurityUtils.getAccountStoreId(),productCategoryId,page));
	
		return "/v2/storePackage/showProductStockModelList-data";
	}
}
