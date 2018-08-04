package cn.lastmiles.controller;


import java.util.ArrayList;
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

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductBrandService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productBrand")
public class ProductBrandController {
	private final static Logger logger = LoggerFactory.getLogger(ProductBrandController.class);
	@Autowired
	private ProductBrandService productBrandService;
	
	@Autowired
	private ShopService shopService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ProductStockService productStockService; 
	@RequestMapping("")
	public String index() {
		return "redirect:/productBrand/list";
	}
	
	/**
	 * 所有商品品牌列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "productBrand/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name,Long storeId, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
//					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
//					boolean index = false;
//					for (Store store : storeList) {
//						if(index){
//							storeIdString.append(",");
//						}
//						storeIdString.append(store.getId());
//						index = true;
//					}
					storeIdString.append(SecurityUtils.getAccountStoreId().toString());		
				} else { // 有权限，且指定了固定查询的商家
 					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		model.addAttribute("data", productBrandService.findAll(storeIdString.toString(),name.trim(), page));
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeIdCache",SecurityUtils.getAccountStoreId());
		return "productBrand/list-data";
		
	}
	
	// 弹窗测试
		@RequestMapping("showModel/list/list-data")
		public String shopListData(String name, String mobile,Page page, Model model) {
			String agentName="";
			StringBuffer storeIdString = new StringBuffer();
			if(SecurityUtils.isMainStore()){
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				boolean index = false;
				for (Store store : storeList) {
					if(index){
						storeIdString.append(",");
					}
					storeIdString.append(store.getId());
					index = true;
				}
			}
			logger.debug("storeIdString.toString()={}",storeIdString.toString());
			page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
			model.addAttribute("data", page);
			return "productBrand/showModelList-data";
		}
	
		/**
		 * 跳到商品品牌增加页面
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
			return "productBrand/add";
		}
		
		/**
		 * 查询商品属性是否已存在
		 * @param
		 * @return flag (0 不存在 1已存在) 2表示该属性值
		 */
		
		@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String findBrand(Brand brand) {
			logger.debug("brand={}",brand);
			
			brand.setName(brand.getName().replaceAll("\\s*", ""));
			String flag=productBrandService.findProductBrand(brand)==null?"0":"1";
			logger.debug("flag={}",flag);
			return flag;
		}
		
		/**
		 * 保存增加商品属性或保存修改商品属性
		 * 
		 * @param 
		 * @return flag 1表示保存成功
		 */
		@RequestMapping(value = "save",method = RequestMethod.POST)
		public String editProductBrand(Long id,Long storeId,String brandName[]){
			logger.debug("storeId={},brandName={}",storeId,brandName);
			if(SecurityUtils.isStore()){//如果是商家登录
				storeId=SecurityUtils.getAccountStoreId();
			}
			productBrandService.editProductBrand(id,storeId,brandName);
			for(int i=0;i<brandName.length;i++){
				logger.debug("品牌名称＝＝"+brandName[i]);
			}
			
			return "/productBrand/list";
		}
		
		/**
		 * 跳转修改界面
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
		public String toUpdate(@PathVariable Long id,Model model ){
			Brand brand= productBrandService.findById(id);//把productAttribute对像转回页面
			model.addAttribute("productBrand", brand);
			
			model.addAttribute("isSys",SecurityUtils.isAdmin());
			model.addAttribute("isMainStore",SecurityUtils.isMainStore());
			return "/productBrand/add";
		}
		
		
		/**
		 * 通过ID删除 数据
		 * @param id
		 * @param model
		 * @return
		 */
		
		@RequestMapping(value="delete/delete-by-productBrandId",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String deleteById(Long id){
			List<Product> products=productStockService.findProductByBrandId(id);
			if(products.isEmpty()){
				productBrandService.deleteById(id);
				return "1";
			}else{
				return "2";
			}
		}
		@RequestMapping(value="delete/deleteAll-by-productBrandIds",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public List<String> deleteAllById(String productBrandIds) {
			logger.debug("productBrandIds={}",productBrandIds);
			String[] arrIds=productBrandIds.split(",");
			List<String> productBrandNames=new ArrayList<String>();
			boolean flag=true;
			for(int i=0;i<arrIds.length;i++){
				Long productBrandId=Long.parseLong(arrIds[i]);
				List<Product> products=productStockService.findProductByBrandId(productBrandId);
				
				if(!products.isEmpty()){
					productBrandNames.add(products.get(0).getBrandName()+","+"0");
					flag=false;
					break;
				}
			}
			logger.debug("flag=={}",flag);
			if(flag){
				for(int i=0;i<arrIds.length;i++){
					Long productAttributeId=Long.parseLong(arrIds[i]);
					productBrandService.deleteById(productAttributeId);
				}
				return null;
			}else{
				logger.debug("productAttributeNames=={}",productBrandNames);
				return productBrandNames;
			}
		}
		/**
		 * 通过商店ID查找品牌列表
		 * @param storeId
		 * @return
		 */
		@RequestMapping(value="list/ajax/byStoreId",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public List<Brand> findBrandListByStoreId(Long storeId) {
			logger.debug("storeId={}",storeId);
			return productBrandService.findBrandListByStoreId(storeId);
		}
}
