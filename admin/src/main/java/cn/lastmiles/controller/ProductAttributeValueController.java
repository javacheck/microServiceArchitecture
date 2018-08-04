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

import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
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
@RequestMapping("productAttributeValue")
public class ProductAttributeValueController {
	private final static Logger logger = LoggerFactory.getLogger(ProductAttributeController.class);
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/productAttributeValue/list";
	}
	
	/**
	 * 所有商品属性值列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "productAttributeValue/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Long storeId,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if( null != storeId ){
			storeIdString.append(storeId);
		}else{
			storeIdString.append(SecurityUtils.getAccountStoreId());
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
			
		model.addAttribute("data", productAttributeValueService.findAll(storeIdString.toString(),name.trim(), page));
		return "productAttributeValue/list-data";

		
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
	 * 根据登录账号查询账号下所有商品（查询商品表t_product）
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-storeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> findByStoreId() {
		List<Store> storeList = storeService.findMyStore(SecurityUtils.getAccountId(), null);
		
		StringBuffer storeIds = new StringBuffer();
		for (int i = 0; i < storeList.size(); i++) {
			if(i!=0){
				storeIds.append(","); 
			}
			storeIds.append(storeList.get(i).getId());
		}
		List<ProductCategory> list= productCategoryService.findByParentIdAndStoreId(null,storeIds.toString());
		return list; 
	}
	
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-categoryId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttribute> findByCategoryId(Long categoryId) {
		List<ProductAttribute> list=productAttributeValueService.findByCategoryId(categoryId);
		return list; 
	}
	/**
	 * 通过商品属性值表里的商品属性ID查找商品属性表里的productid
	 * @param attributeId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductAttribute findById(Long id) {
		return productAttributeService.findById(id); 
	}
	/**
	 * 通过商品属性值表里的商品属性ID查找全部商品属性值(查t_product_attribute_value)
	 * @param attributeId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-attributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttributeValue> findByAttributeId(ProductAttributeValue productAttributeValue) {
		return productAttributeValueService.findByAttributeId(productAttributeValue); 
	}
	/**
	 * 跳到商品属性值增加页面
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
		return "productAttributeValue/add";
	}

	/**
	 * 查询商品属性是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		productAttributeValue.setValue(productAttributeValue.getValue().replaceAll("\\s*", ""));
		String flag=productAttributeValueService.findProductAttributeValue(productAttributeValue)==null?"0":"1";
		return flag;
	}
	/**
	 * 保存增加商品属性值或保存修改商品属性值
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(Long id,Long childid,String value[]){
		Long attributeId=childid;//属性ID
		productAttributeValueService.editProductAttribute(id,attributeId,value);
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
		ProductAttributeValue attributeValue=productAttributeValueService.findById(id);
		ProductAttribute attribute= productAttributeService.findById(attributeValue.getAttributeId());
		ProductCategory productCategory=productCategoryService.findById(attribute.getCategoryId());
		Store store=storeService.findById(productCategory.getStoreId());
		model.addAttribute("productAttribute", attribute);//把productAttributeValue对像转回页面
		
		model.addAttribute("productAttributeValue", attributeValue);//把productAttributeValue对像转回页面
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("store",store);
		return "/productAttributeValue/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-productAttributeValueId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(ProductAttributeValue productAttributeValue){
		String flag = "1";
		productAttributeValue.setId(productAttributeValue.getId());
		ProductAttributeValue productAttributeValueObject = productAttributeValueService.findById(productAttributeValue.getId());
		
		ProductAttribute productAttribute = productAttributeService.findById(productAttributeValueObject.getAttributeId());
		
		// 传入商品分类ID，关联商品表和产品表取数据
		List<ProductStock> list = productStockService.findByFuzzyAttributeCode(productAttributeValue.getId(),productAttribute.getCategoryId());
		
		if( null == list || list.size() <= 0 ){
			productAttributeValueService.deleteById(productAttributeValue);			
		} else {
			flag = "0";
		}
		return flag;
	}
	@RequestMapping(value="delete/deleteAll-by-productAttributeValueIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttributeValue> deleteAllById(String productAttributeValueIds) {
		logger.debug("productIds={}",productAttributeValueIds);
		String[] arrIds=productAttributeValueIds.split(",");
		List<ProductAttributeValue> productAttributeValues=null;
		boolean flag=true;
		for(int i=0;i<arrIds.length;i++){
			Long productAttributeValueId=Long.parseLong(arrIds[i]);
			productAttributeValues = productAttributeValueService.findById2(productAttributeValueId);
			ProductAttribute productAttribute=productAttributeService.findById(productAttributeValues.get(0).getAttributeId());
			List<ProductStock> productStocks=productStockService.findByFuzzyAttributeCode(productAttributeValues.get(0).getId(),productAttribute.getCategoryId());
			if(!productStocks.isEmpty()){
				flag=false;
				break;
			}
		}
		logger.debug("flag=={}",flag);
		if(flag){
			for(int i=0;i<arrIds.length;i++){
				Long productAttributeValueId=Long.parseLong(arrIds[i]);
				productAttributeValueService.deleteById(productAttributeValueId);	
			}
			return null;
		}else{
			logger.debug("productAttributeValues=={}",productAttributeValues);
			return productAttributeValues;
		}
	}
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-productAttributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttributeValue> productAttributeValueList(Long productAttributeId) {
		List<ProductAttributeValue> list=productAttributeValueService.productAttributeValueList(productAttributeId);
		return list; 
	}
}
