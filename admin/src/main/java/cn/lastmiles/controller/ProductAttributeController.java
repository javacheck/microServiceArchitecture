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

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductAttributeService;
import cn.lastmiles.service.ProductAttributeValueService;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productAttribute")
public class ProductAttributeController {
	private final static Logger logger = LoggerFactory.getLogger(ProductAttributeController.class);
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private IdService idService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/productAttribute/list";
	}
	
	/**
	 * 所有商品属性列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "productAttribute/list";
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
		model.addAttribute("data", productAttributeService.findAll(storeIdString.toString(),name.trim(), page));
		return "productAttribute/list-data";
		
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
		page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "productAttribute/showModelList-data";
	}
	/**
	 * 跳到商品属性增加页面
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
		return "productAttribute/add";
	}

	/**
	 * 查询商品属性是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在) 2表示该属性值
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductAttribute(ProductAttribute productAttribute) {
		logger.debug("productAttribute={}",productAttribute);
		// 当此商品分类已经关联了商品列表，则此分类不能在增加属性
		List<Product> list = productStockService.findByCategoryId(productAttribute.getCategoryId());
		if( list != null && list.size() > 0 ){
			return "2";
		}	
		productAttribute.setName(productAttribute.getName().replaceAll("\\s*", ""));
		String flag=productAttributeService.findProductAttribute(productAttribute)==null?"0":"1";
		logger.debug("flag={}",flag);
		return flag;
	}
	/**
	 * 保存增加商品属性或保存修改商品属性
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editProductAttribute(Long id,Long cacheCategoryId,String attributeName[]){
		logger.debug("attributeName={},cacheCategoryId={}",attributeName,cacheCategoryId);
		String flag="1";
		Long categoryId=cacheCategoryId;//分类ID
		
		productAttributeService.editProductAttribute(id,categoryId,attributeName);
		for(int i=0;i<attributeName.length;i++){
			logger.debug("属性名称＝＝"+attributeName[i]);
		}
		
		
		
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
		ProductAttribute attribute= productAttributeService.findById(id);//把productAttribute对像转回页面
		model.addAttribute("productAttribute", attribute);
		
		ProductCategory productCategory=productCategoryService.findById(attribute.getCategoryId());
		Store store=storeService.findById(productCategory.getStoreId());
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("store",store);
		return "/productAttribute/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value="delete/delete-by-productAttributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(ProductAttribute productAttribute){
		Integer count=productAttributeValueService.findByAttributeId(productAttribute.getId());
		// 根据属性ID得到相对应的整个属性对象
		ProductAttribute attribute= productAttributeService.findById(productAttribute.getId());
		// 传入商品分类ID，关联商品表和产品表取数据
		List<Product> list = productStockService.findByCategoryId(attribute.getCategoryId());
		
		// 先判断是否有关联到商品，然后再判断是否有关联到属性值
		if( list != null && list.size() > 0 ){ //不为null且数据大于0则此属性有关联商品项，此属性不可删除
			return "2";
		} else if(count>0){
			return "0";
		} else {
			productAttribute.setId(productAttribute.getId());
			productAttributeService.deleteById(productAttribute);
			return "1";
		}
	}
	@RequestMapping(value="delete/deleteAll-by-productAttributeIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> deleteAllById(String productAttributeIds) {
		logger.debug("productAttributeIds={}",productAttributeIds);
		String[] arrIds=productAttributeIds.split(",");
		List<String> productAttributeNames=new ArrayList<String>();
		boolean flag=true;
		for(int i=0;i<arrIds.length;i++){
			Long productAttributeId=Long.parseLong(arrIds[i]);
			//关联商品库存不为空
			List<ProductAttribute> productAttributes=productAttributeService.findById1(productAttributeId);
			List<Product> products=productStockService.findByCategoryId(productAttributes.get(0).getCategoryId());
			Integer count=productAttributeValueService.findByAttributeId(productAttributeId);
			if(!products.isEmpty()){
				productAttributeNames.add(productAttributes.get(0).getName()+","+"0");
				flag=false;
				break;
			}else if(count.intValue()>0){
				productAttributeNames.add(productAttributes.get(0).getName()+","+"1");
				flag=false;
				break;
			}
		}
		logger.debug("flag=={}",flag);
		if(flag){
			for(int i=0;i<arrIds.length;i++){
				Long productAttributeId=Long.parseLong(arrIds[i]);
				productAttributeService.deleteById(productAttributeId);
			}
			return null;
		}else{
			logger.debug("productAttributeNames=={}",productAttributeNames);
			return productAttributeNames;
		}
	}
	@RequestMapping(value="list/ajax/byStoreId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> findCategoryListByStoreId(Long storeId) {
		logger.debug("storeId={}",storeId);
		return productCategoryService.findByStoreId(storeId);
	}
}
