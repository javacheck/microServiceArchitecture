package cn.lastmiles.controller;
/**
 * 此类主要处理商品和库存的展示，新增，修改，删除
 */
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.ProductCategoryDao;
import cn.lastmiles.service.OrderItemServise;
import cn.lastmiles.service.ProductAttributeService;
import cn.lastmiles.service.ProductAttributeValueService;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("product")
public class ProductController {
	private final static Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductCategoryDao productCategorydao;
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
	private ShopService shopService;
	@Autowired
	private IdService idService;
	
	@Autowired
	private OrderItemServise orderItemServise;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/product/list";
	}
	
	/**
	 * 查询所有属于当前登录用户的店铺，根据店铺得到商品分类，然后读取此分类下的商品库存
	 * 进入商品库存信息列表页面，展示商品库存列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model,Integer pageNo) {
		
		List<Store> storeList = storeService.findMyStore(SecurityUtils.getAccountId(), null);
		model.addAttribute("storeList",storeList);
		
		if( null != storeList && storeList.size() > 0 ){
			if( storeList.size() <= 1 ){ 
				List<ProductCategory> productCategoryList = productCategoryService.findByParentIdAndStoreId(null,storeList.get(0).getId());
				model.addAttribute("categoryList",productCategoryList);			
			} else { 
				model.addAttribute("storeHaveAll","storeHaveAll");
				StringBuffer storeIds = new StringBuffer();
				for (int i = 0; i < storeList.size(); i++) {
					if(i!=0){
						storeIds.append(","); 
					}
					storeIds.append(storeList.get(i).getId());
				}
				List<ProductCategory> productCategoryList = productCategoryService.findByParentIdAndStoreId(null,storeIds.toString());
				model.addAttribute("categoryList",productCategoryList);	
			}
		} else {
			model.addAttribute("categoryList",null);
		}
	
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeIdCache",SecurityUtils.getAccountStoreId());
		return "product/list";
	}

	/**
	 * 根据传入进来的店铺ID查询其下所属的商品分类ID，根据商品分类ID得到商品库存和商品属性及属性值
	 * 进入商品库存信息列表页面，展示商品库存列表
	 * @param storeSelect 店铺ID
	 * @param topid 商品分类ID
	 * @param name 查询的商品名称
	 * @param min 查询的最小价格
	 * @param max 查询的最大价格
	 * @param page 
	 * @param model
	 * @return
	 */
	@RequestMapping("list/list-data")
	public String listData(Long storeId,Long sysCategoryId,Long storeCategoryId,String name,Integer alarmValueType,Integer shelves,String barCode, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
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
		page = productStockService.findAll(storeIdString.toString(),sysCategoryId,storeCategoryId,name,alarmValueType,shelves,barCode.trim(),page);
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeIdCache",SecurityUtils.getAccountStoreId());
		model.addAttribute("data",page);
		return "product/list-data";
		
	}
	
	
	@RequestMapping(value = "shopList/list")
	public String shopList() {
		return "product/shopList";
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
		return "product/shopList-data";
	}
	
	/**
	 * 得到当前登录用户的所有店铺和第一个店铺下的所有商品分类
	 * 跳转到商品库存增加页面
	 * @return
	 */
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
		return "/product/add";
	}
		
	/**
	 * 根据传入的库存ID，得到库存对象，得到此库存所对应的商品对象和商品分类
	 * 跳转到商品库存修改界面
	 * @param productStockId 库存ID 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{productStockId}/{pageNo}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long productStockId,@PathVariable Integer pageNo,Model model ){
		logger.debug("pageNo={}",pageNo);
		ProductStock productStock = productStockService.findById(productStockId);
		Product product = productService.findById(productStock.getProductId());
		logger.debug("product={}",product);
		ProductCategory productCategory=productCategoryService.findById(product.getCategoryId());
		Map<String,String> cerUrlMap=productService.findProductImageList(productStockId);
		Store store=storeService.findById(productStock.getStoreId());
		model.addAttribute("product", product);
		model.addAttribute("productStock", productStock);
		model.addAttribute("cerUrlMap", cerUrlMap);
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("store", store);
		Long isStoreId=null;
	 	if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		return "/product/add";
	}
	
	/**
	 * 保存商品和库存或者只保存库存
	 * @param 库存对象，商品对象
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value = "save",method = RequestMethod.POST)
	public String save(Long id,Integer pageNo,Long storeId,Long productId,String name,Long storeCategoryId,Long sysCategoryId,String barCode,
			String remarks,Integer shelves,Double price,Double memberPrice,Double marketPrice,
			Double costPrice,Double stock,Double alarmValue,
			@RequestParam(value="image", required=true) MultipartFile image[],String[] delImage,
			String attributeCode,String attributeName,Integer sort,String unitName,String textArea,Model model ){
		String data="0";
		logger.debug("storeId={},unitName={}",storeId,unitName);
		if(!SecurityUtils.isAdmin()){
			storeId=SecurityUtils.getAccountStoreId();
		}
		productService.editProduct(id,storeId,productId,name,SecurityUtils.getAccountId(),barCode,remarks,shelves,price,memberPrice,marketPrice,costPrice,stock,alarmValue,image,delImage,attributeCode,attributeName,sort,unitName,textArea);
		
		model.addAttribute("data", data);
		model.addAttribute("isSys", SecurityUtils.isAdmin());
		if(id==null){
			return "product/list";
		}else{
			return "redirect:/product/list?pageNo="+pageNo;
		}
	}
	
	/**
	 * 商品列表页面删除操作
	 * 根据传入进来的库存ID判断此库存关联的商品是否关联多个库存，如果关联多个则只删除库存，反之则连同商品一起删除
	 * @param id 库存表的库存ID
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-productStockId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteByProductStockId(Long productStockId){
		OrderItem orderItem=orderItemServise.findByStockId(productStockId);
		if(orderItem==null){
			productStockService.deleteById(productStockId);
			return "1";
		}else{
			return "0";
		}
		
		
	}
	@RequestMapping(value="delete/deleteAll-by-productStockIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> deleteAllById(String productStockIds){
		logger.debug("productStockIds={}",productStockIds);
		List<Long> stockIds=new ArrayList<Long>();
		String[] arrIds=productStockIds.split(",");
		boolean flag=true;
		List<String> stockNames=new ArrayList<String>();
		for(int i=0;i<arrIds.length;i++){
			logger.debug("productIds==="+arrIds[i]);
			Long productStockId=Long.parseLong(arrIds[i]);
			OrderItem orderItem=orderItemServise.findByStockId(productStockId);
			if(orderItem==null){
				stockIds.add(productStockId);
			}else{
				ProductStock ps=productStockService.findById(productStockId);
				logger.debug("ps.getAttributeName()={},ps.getAttributeName()={}",ps.getAttributeName(),ps.getAttributeName());
				if(ps.getAttributeName()==null){
					stockNames.add(ps.getProductName());
				}else{
					stockNames.add(ps.getAttributeName());
				}
				flag=false;
				break;
			}
			
		}
		logger.debug("flag={},stockIds={}",flag,stockIds);
		if(flag){
			productStockService.batchDeleteByIds(stockIds);
		}
		return stockNames;
		
		
		
	}
	
	/**
	 * 通过商品分类ID获取属性列表
	 * @param categoryId 商品分类ID
	 * @return
	 */
	@RequestMapping(value="list/ajax/productAttributeList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttribute> productAttributeList( Long categoryId ,Long storeId){
		List<ProductAttribute> productAttributeList = productAttributeService.productAttributeList(categoryId,storeId);
		return productAttributeList;
	}
	
	/**
	 * 根据传入的店铺ID查询其下的商品分类
	 * @param storeId 店铺ID
	 * @return
	 */
	@RequestMapping(value="list/ajax/productCategoryIdList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productCategoryIdList(Long storeId ){
		List<ProductCategory> productCategoryList = null;
		if( null != storeId ){
			if(storeId.equals(Constants.Status.SELECT_ALL)){
				productCategoryList = productCategoryService.findByParentIdAndStoreId(null,storeId);
			} else {
				List<Store> storeList = storeService.findMyStore(SecurityUtils.getAccountId(), null);
				if( null != storeList && storeList.size() > 0 ){
					StringBuffer storeIds = new StringBuffer();
					for (int i = 0; i < storeList.size(); i++) {
						if(i!=0){
							storeIds.append(",");							
						}
						storeIds.append(storeList.get(i).getId());
					}
					 productCategoryList = productCategoryService.findByParentIdAndStoreId(null,storeIds.toString());
				}
			}
		}
		return productCategoryList;
	}
	
	/**
	 * 根据传入的库存对象，得到店铺ID和库存对象的属性值连接字段
	 * @param productStock
	 * @return
	 */
	@RequestMapping(value="list/ajax/existProductStock",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String byAttributeFindProductStock(Product product,ProductStock productStock) {
		logger.debug("id={},proudctName={},storeId={},attrbuteCode={}",productStock.getId(),product.getName(),product.getStoreId(),productStock.getAttributeCode());
		product.setName(product.getName().replaceAll("\\s*", ""));
		String flag = productStockService.byAttributeFindProductStock(product,productStock)==null?"0":"1";
		logger.debug("flag={}",flag);
		return flag;
	}
	
	/**
	 * 根据传入的库存对象，得到店铺ID和库存对象的条形码字段
	 * @param productStock
	 * @return
	 */
	@RequestMapping(value="list/ajax/existBarCode",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String byBarCodeFindProductStock(Product product,ProductStock productStock) {
		logger.debug("proudctName={},storeId={},barCode={}",product.getName(),product.getStoreId(),productStock.getBarCode());
		productStock.setStoreId(productStock.getStoreId());
		productStock.setBarCode(productStock.getBarCode().replaceAll("\\s*", ""));
		productStock.setAttributeCode(productStock.getAttributeCode());
		product.setName(product.getName().replaceAll("\\s*", ""));
		
		List<ProductStock> productStockList = productStockService.byBarCodeFindProductStock(productStock.getBarCode(),productStock.getStoreId());
		// 当前店铺有此条形码
		if( null != productStockList && productStockList.size() > 0 ){
			List<Product> productList = productService.findByNameAndStoreId(product.getName(), productStock.getStoreId());
			// 当前店铺有这个商品
			if( null != productList && productList.size() > 0 ){
				ArrayList<Long> al = new ArrayList<Long>(); 
				for (int i = 0; i < productStockList.size(); i++) {
					al.add(productStockList.get(i).getProductId());
				}
				boolean flag = false;
				for (int i = 0; i < productList.size(); i++) {
					for (Long long1 : al) {
						if(!productList.get(i).getId().equals(long1)){
							flag = true;
							break; // 有了就不循环了。减少消耗
						}
						
					}
				}
				if(flag){
					return "1";
				} else {
					return "0";					
				}
			} else {
				return "1"; // 有条形码了。但是不属于此商品，则增加不成功！
			}
			 
		} else {
			return "0"; // 直接返回可以操作
		}
	}
	
	// -------> 下面的预留 <---------
	
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductStock(ProductStock productStock) {
		productStock.setAttributeCode(productStock.getAttributeCode());
		String flag=productStockService.findProductStock(productStock)==null?"0":"1";
		return flag;
	}
	
	/**
	 * 查询父类为空的产品分类
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-parent1",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> category() {
		List<ProductCategory> list=productCategoryService.findByParentId(null,null);
		return list; 
	}
	
	/**
	 * 查询商品是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist1",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProduct(Product product) {
		product.setName(product.getName());
		product.setCategoryId(product.getCategoryId());
		product.setAccountId(SecurityUtils.getAccountId());
		String flag=productService.findProduct(product)==null?"0":"1";
		return flag;
	}
	
	/**
	 * 跳转商品库存列表界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "productStock/{id}",method = RequestMethod.GET)
	public String productStockList(@PathVariable Long id,Model model ){
		model.addAttribute("productId", id);
		return "/productStock/list";
	}
	
	@RequestMapping(value="delete/delete-by-productId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Product product){
		product.setId(product.getId());
		productService.deleteById(product);
		return "1";
	}
	
	/**
	 * 根据parentId产品分类子类
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-parent",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> list(Long parentId) {
		List<ProductCategory> list=productCategoryService.findByParentId(parentId,null);
		return list; 
	}
	
	/**
	 * 根据categoryId取产品分类
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-categoryId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductCategory findById(Long categoryId) {
		
		ProductCategory productCategory=productCategoryService.findById(categoryId);
		return productCategory; 
	}
	
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-productCategoryId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Product> productList(Long categoryId) {
		List<Product> list=productService.productList(categoryId);
		return list; 
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,
			Long storeId, Long categoryId,
			String name,Integer alarmValueType,Integer shelves,String barCode,Page page) throws ParseException {
		page.setIsOnePage();
		StringBuffer storeIdString = new StringBuffer();
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
		String fileName = "库存列表";
		logger.debug("storeId={},categoryId={},name={},alarmValueType={},shelves={}",storeId,categoryId,name,alarmValueType,shelves);
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) productStockService.findAll(storeIdString.toString(),null,categoryId,name,alarmValueType,shelves,barCode.trim(),page).getData();
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("库存列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<12;i++){
			if(i==0){
				cell = row.createCell((short)i);// 第一行第1格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell((short)i);// 第一行第2格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell((short)i);// 第一行第3格
				cell.setCellValue("商家分类");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell((short)i);// 第一行第4格
				cell.setCellValue("库存");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell((short)i);// 第一行第5格
				cell.setCellValue("缺货提醒");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell((short)i);// 第一行第6格
				cell.setCellValue("成本价");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell((short)i);// 第一行第7格
				cell.setCellValue("市场价");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell((short)i);// 第一行第8格
				cell.setCellValue("会员价");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell((short)i);// 第一行第9格
				cell.setCellValue("销售单价");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell((short)i);// 第一行第10格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell((short)i);// 第一行第11格
				cell.setCellValue("商品状态");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell((short)i);// 第一行第12格
				cell.setCellValue("排序");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",productStockList.size());
		if (!productStockList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",productStockList.size());
			for (int i = 0; i < productStockList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ProductStock productStock=productStockList.get(i);
				row = sheet.createRow((int) i + 1);
				String productName=productStock.getProductName();
				if(!productStock.getPavList().isEmpty()){
					for(int j=0;j<productStock.getPavList().size();j++){
						productName+="|"+productStock.getPavList().get(j).getValue();
					}
				}
				row.createCell(0).setCellValue(productName);//商品名称
				row.createCell(1).setCellValue(productStock.getStoreName());//商家	
				row.createCell(2).setCellValue(productStock.getCategoryName());//分类名称
				logger.debug("productStock.getStock()={}",productStock.getStock());
				if(productStock.getStock()!=null){
					row.createCell(3).setCellValue(productStock.getStock()==-99?"无限":productStock.getStock().toString());//库存
				}
				if(productStock.getAlarmValue()!=null){
					row.createCell(4).setCellValue(productStock.getAlarmValue());//缺货提醒
				}
				row.createCell(5).setCellValue(productStock.getCostPrice()==null?"":productStock.getCostPrice().toString());//成本价
				row.createCell(6).setCellValue(productStock.getMarketPrice()==null?"":productStock.getMarketPrice().toString());//市场价
				row.createCell(7).setCellValue(productStock.getMemberPrice()==null?"":productStock.getMemberPrice().toString());//会员价
				if(productStock.getPrice()!=null){
					row.createCell(8).setCellValue(productStock.getPrice());//销售单价
				}
				row.createCell(9).setCellValue(productStock.getBarCode());//商品条码
				row.createCell(10).setCellValue(productStock.getShelves()==0?"上架":"下架");//商品状态
				row.createCell(11).setCellValue(productStock.getSort()==null?"":productStock.getSort().toString());//排序
			}
		}
		// 第六步，将文件存到指定位置
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			os.flush();
			// 关闭输出流
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}