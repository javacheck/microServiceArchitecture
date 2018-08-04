/**
 * createDate : 2016年4月11日上午11:28:05
 */
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

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.movie.RoomPackage;
import cn.lastmiles.bean.movie.RoomPackageProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.RoomPackageService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("roomPackage")
public class RoomPackageController {
	private final static Logger logger = LoggerFactory.getLogger(RoomPackageController.class);
	@Autowired
	private RoomPackageService roomPackageService;
	@Autowired
	private ProductCategoryService productCategoryService; // 商品分类
	
	@RequestMapping("list")
	public String list(){
		return "roomPackage/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Page page, Model model) {
		
		model.addAttribute("data", roomPackageService.list(SecurityUtils.getAccountStoreId(), page));
		return "roomPackage/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(){
		return "roomPackage/add";
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		RoomPackage roomPackage = roomPackageService.findById(id);
		if( null == roomPackage ){
			return "redirect:/promotion/list";
		}
		StringBuffer sb = new StringBuffer();
		List<RoomPackageProductStock> roomPackageProductList = roomPackageService.findProductById(id);
		if( null != roomPackageProductList ){
			for (RoomPackageProductStock roomPackageProduct : roomPackageProductList) {
				sb.append(roomPackageProduct.getProductStockId());
				sb.append(",");
			}			
		}
		if( sb.length() > 0 ){
			sb.delete(sb.length()-1, sb.length());
		}
		// 将此促销信息所选择的商品传到前台进行分析和计算
		model.addAttribute("roomPackageProductList",sb.toString());
		
		// 组合情况下需要将已选择了的商品组合成列表到页面进行展示
		List<ProductStock> productStockList = null;
		if( null != sb && sb.length() > 0 ){
			productStockList = roomPackageService.findProductStockList(SecurityUtils.getAccountStoreId(),sb.toString());
		}
		
		for (ProductStock ps : productStockList){
			for (RoomPackageProductStock ppps : roomPackageProductList){
				if (ps.getId().equals(ppps.getProductStockId())){
					ps.setStock(ppps.getAmount().doubleValue());
				}
			}
		}
		
		model.addAttribute("productStockList",productStockList);
		
		model.addAttribute("roomPackage",roomPackage);
		return "/roomPackage/add";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id){
		return roomPackageService.deleteById(SecurityUtils.getAccountStoreId(),id) > 0 ? 1 : 0;
	}
	
	/**
	 * 根据商品ID获取商品详细信息
	 * @param cacheString 商品ID组拼字符串
	 */
	@RequestMapping(value="list/ajax/getProductStock",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> getProductStock(String cacheString) {
		List<ProductStock> productStockList = null;
		if( StringUtils.isNotBlank(cacheString) ){
			productStockList = roomPackageService.findProductStockList(SecurityUtils.getAccountStoreId(),cacheString);
		}
		return productStockList;
	}
	
	// 商品弹窗
	@RequestMapping("showProductStockModel/list/list-data")
	public String showProductStockModelListData(String productName,Long productCategoryId, Page page, Model model) {
		if( ObjectUtils.equals(productCategoryId, -1l) ){
			productCategoryId = null;
		}
		model.addAttribute("data", roomPackageService.findProductStockList(productName,SecurityUtils.getAccountStoreId(),productCategoryId,page));
	
		return "roomPackage/showProductStockModelList-data";
	}
	
	@RequestMapping(value = "list/ajax/checkName")
	@ResponseBody
	public int checkName(Long id,String name) {
		if ( roomPackageService.checkName(id,SecurityUtils.getAccountStoreId(),name) ) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 获取此商家的商品分类
	 */
	@RequestMapping(value="list/ajax/productSystemCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productSystemCategory() {
		return productCategoryService.findByTypeStoreId(SecurityUtils.getAccountStoreId(), Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(RoomPackage roomPackage) {
		roomPackage.setStoreId(SecurityUtils.getAccountStoreId());
		if( null == roomPackage.getId()){
			roomPackage.setAccountId(SecurityUtils.getAccountId());
		} else {
		    roomPackage.setUpdatedId(SecurityUtils.getAccountId());
		}
		roomPackageService.save(roomPackage);
		
		return "redirect:/roomPackage/list";
	}
	
}
