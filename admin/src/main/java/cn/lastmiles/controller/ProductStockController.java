package cn.lastmiles.controller;

import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductAttributeService;
import cn.lastmiles.service.ProductAttributeValueService;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.FileServiceUtils;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productStock")
public class ProductStockController {
	private final static Logger logger = LoggerFactory.getLogger(ProductStockController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private IdService idService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductCategoryService productCategoryService;
	/**
	 * 所有商品列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeIdCache",SecurityUtils.getAccountStoreId());
		return "productStock/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Long storeId,Long sysCategoryId,Long storeCategoryId,String name, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("name={}",name);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
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
		if( null != name){
			name = name.replaceAll("\\s*", "");
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeId",SecurityUtils.getAccountStoreId());
		model.addAttribute("data",  productService.findAll(storeIdString.toString(),sysCategoryId,storeCategoryId,name,page));
		return "productStock/list-data";
	}

	@RequestMapping(value = "shopList/list")
	public String shopList() {
		return "productStock/shopList";
	}
	@RequestMapping(value = "shopList/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		
		logger.debug("name={}",name);
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
		logger.debug("storeIdString.toString1()={}",storeIdString.toString());
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "productStock/shopList-data";
	}
	@RequestMapping("productList/list-data")
	public String productListData(Long storeModelId,Long sysCategoryId,Long storeCategoryId,String name, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if( null != storeModelId ){
			storeIdString.append(storeModelId);
		}else{
			storeIdString.append(SecurityUtils.getAccountStoreId());
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		if( null != name){
			name = name.replaceAll("\\s*", "");
		} 
		model.addAttribute("data",  productService.findAll(storeIdString.toString(),sysCategoryId,storeCategoryId,name,page));
		return "product/productList-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		logger.debug("isSys"+SecurityUtils.isAdmin());
		Long isStoreId=null;
		if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		return "/productStock/add";
	}
	
	/**
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping(value="list/ajax/existProduct",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String finProduct(Product product){
		if(productStockService.findProduct(product)==null){
			return "0";
		}else{
			return "1";
		}
	}
	
	/**
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editProduct(Product product){
		logger.debug("productList={}",product);
		product.setAccountId(SecurityUtils.getAccountId());//创建者
		productStockService.editProduct(product);
		return "1";
	}
	
	
	@RequestMapping(value="list/json/find-by-productId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findStockListByproductId( Long id ){//入参为商品ID
		Long storeId=null;
		if(productAttributeService.productAttributeList(id,storeId).size()>0){
			return "1";
		}else{
			return "0";
		}
	}
	
	/**
	 * 跳转商品库存列表界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "productStock/{id}",method = RequestMethod.POST)
	public String productStockList(@PathVariable Long id,Model model ){
		Long storeId=null;
		model.addAttribute("productId", id);
		List<ProductAttribute> productAttributeList=productAttributeService.productAttributeList(id,storeId);//通过分类id查属性列表
		List<ProductStock> psList=productStockService.findByProductId(id);//以productId查找库存表里的stock列表
		String[] arr;
		if(psList!=null){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList=new ArrayList<ProductAttributeValue>();
				arr=psList.get(i).getAttributeCode().split("-");//属性值111-222-333
				for(int j=0;j<arr.length;j++){
					pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));//把属性值放进list里面
				}
				psList.get(i).setPavList(pavList);
			}
		}
		model.addAttribute("productAttributeList", productAttributeList);
		model.addAttribute("psList",psList);
		return "/productStock/list-data";
	}
	
	/**
	 * 通过商品ID获取属性列表
	 * @param productId
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/productAttributeList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttribute> productAttributeList( Long categoryId ){//入参为商品ID
		Long storeId=null;
		List<ProductAttribute> productAttributeList=productAttributeService.productAttributeList(categoryId,storeId);//通过分类id查属性列表
		return productAttributeList;
	}
	
	/**
	 * 通过商品ID获取属性值列表
	 * @param productId
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/productAttributeValueList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> productAttributeValueList( Long productId ){//入参为商品ID
		
		List<ProductStock> psList=productStockService.findByProductId(productId);//以productId查找库存表里的stock列表
		String[] arr;
		for(int i=0;i<psList.size();i++){
			List<ProductAttributeValue> pavList=new ArrayList<ProductAttributeValue>();
			arr=psList.get(i).getAttributeCode().split("-");//属性值111-222-333
			for(int j=0;j<arr.length;j++){
				pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));//把属性值放进list里面
			}
			psList.get(i).setPavList(pavList);
			
		}
		return psList;
	}
	
	/**
	 * 跳到商品库存增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add/{productId}",method = RequestMethod.GET)
	public String toAdd(@PathVariable Long productId,Model model ){
		model.addAttribute("product", productService.findById(productId));
		model.addAttribute("store",storeService.findById(SecurityUtils.getAccountStoreId()));
		
		return "/productStock/add";
	}
	
	/**
	 * 查询库存商品属性值是否相同
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductStock(ProductStock productStock) {
		productStock.setAttributeCode(productStock.getAttributeCode());
		String flag=productStockService.findProductStock(productStock)==null?"0":"1";
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
	public String save(ProductStock productStock){
		String flag="1";
		if (productStock.getId() != null){
			productStock.setId(productStock.getId());
			productStock.setStock(productStock.getStock());
			productStock.setAlarmValue(productStock.getAlarmValue());
			productStock.setPrice(productStock.getPrice());
			productStock.setBarCode(productStock.getBarCode());
			productStockService.update(productStock);
		}else {
			productStock.setId(idService.getId());
			productStock.setId(productStock.getId());
			productStock.setStock(productStock.getStock());
			productStock.setAlarmValue(productStock.getAlarmValue());
			productStock.setProductId(productStock.getProductId());
			productStock.setAccountId(SecurityUtils.getAccountId());
			productStock.setStoreId(productStock.getStoreId());
			productStock.setAttributeCode(productStock.getAttributeCode());
			productStock.setPrice(productStock.getPrice());
			productStock.setBarCode(productStock.getBarCode());
			productStockService.save(productStock);
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
		Long isStoreId=null;
	 	if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		model.addAttribute("product", productService.findById(id));
		model.addAttribute("categoryStoreId",productCategoryService.findById(productService.findById(id).getCategoryId()).getStoreId());
		model.addAttribute("path",productCategoryService.findById(productService.findById(id).getCategoryId()).getPath());
		if(!productStockService.findByProductId(id).isEmpty()){
			model.addAttribute("exitStockType",0);
		}else{
			model.addAttribute("exitStockType",1);
		}
		return "/productStock/add";
	}
	 
	 /**
		 * 跳转修改界面
		 * @param id
		 * @param model
		 * @return
		 */
	@RequestMapping(value = "uploadImage/{id}",method = RequestMethod.GET)
	public String uploadImage(@PathVariable Long id,Model model ){
		model.addAttribute("productStockId", id);//把productStockId转回页面
		return "/productStock/uploadImage";
	}
	
	/**
	 * @param image
	 * @param model
	 * @return
	 * @throws IOException
	 */
	 @RequestMapping(value = "saveImage", method = RequestMethod.POST)
		public String upload(@RequestParam(value="imageFile", required=true)  MultipartFile[] imageFile,Long productStockId, Model model) throws IOException {
		 	InputStream in = null;
		 	ProductImage productImage=null;
		    for(int i=0;i<imageFile.length;i++){
		    	if(StringUtils.isNotBlank(imageFile[i].getOriginalFilename())){
		    		in=imageFile[i].getInputStream();
		    		String id = fileService.save(in);//图片ID
		    		//图片后缀
		    		String suffix=imageFile[i].getOriginalFilename().substring(imageFile[i].getOriginalFilename().lastIndexOf(".")+1, imageFile[i].getOriginalFilename().length());
		    		productImage=new ProductImage();
		    		productImage.setId(id);
		    		productImage.setProductStockId(productStockId);//库存ID
		    		productImage.setSuffix(suffix);
		    		productImage.setSort(Integer.parseInt(imageFile[i].getOriginalFilename().substring(0, imageFile[i].getOriginalFilename().lastIndexOf("."))));//顺序
		    		productStockService.saveProductImage(productImage);
		    		in.close();
		    	}
		    }
		    model.addAttribute("flag",1);
			return "productStock/uploadImage";
	}
	 
	 /**
		 * 通过ID删除 数据
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value="delete/delete-by-id",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String deleteByProductId(Long id){
			if(!productStockService.findByProductId(id).isEmpty()){
				return "1";
			}else{
				productService.deleteById(id);
				return "0";
			}
		}
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-productStockId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		productStockService.deleteById(id);
		return "1";
	}
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delete/deleteAll-by-productIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> deleteAllById(String productIds){
		logger.debug("productIds={}",productIds);
		String[] arrIds=productIds.split(",");
		List<ProductStock> products=null;
		boolean flag=true;
		for(int i=0;i<arrIds.length;i++){
			Long productId=Long.parseLong(arrIds[i]);
			products = productStockService.findByProductId(productId);
			if(!products.isEmpty()){
				flag=false;
				break;
			}
		}
		logger.debug("flag=={}",flag);
		if(flag){
			for(int i=0;i<arrIds.length;i++){
				Long productId=Long.parseLong(arrIds[i]);
				productService.deleteById(productId);
			}
			return null;
		}else{
			logger.debug("products=={}",products);
			return products;
		}
			
			
		
	}
	/**
	 * 跳到图片预览页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "preview/{id}",method = RequestMethod.GET)
	public String toPreview(@PathVariable Long id,Model model){
		logger.debug("productStockId=="+id);
		if((productStockService.findImageByStockId(id) == null ? "0": "1").equals("0")){
			model.addAttribute("action", 1);
			return "product/view";
		}else{
			List<ProductImage> list=productStockService.findImageByStockId(id);
			ProductStock productStock=new ProductStock();
			List<String> imageString=new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				imageString.add(FileServiceUtils.getFileUrl(list.get(i).getId()));//文件服务器读取图片
			}
			productStock.setPicUrlList(imageString);
			model.addAttribute("action", 0);
			model.addAttribute("productStock", productStock); 
			return "product/view";
		}
	}
}