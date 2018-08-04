package cn.lastmiles.controller;
/**
 * createDate : 2015年10月12日 上午11:06:38 
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.SalesPromotionCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.SalesPromotionCategoryService;
import cn.lastmiles.service.SalesPromotionService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.utils.SecurityUtils;
/**
 * 主要实现的功能模块：促销分类
 */
@Controller
@RequestMapping("salesPromotionCategory")
public class SalesPromotionCategoryController {
	
	@Autowired
	private SalesPromotionCategoryService salesPromotionCategoryService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private SalesPromotionService salesPromotionService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping("list")
	public String list(Model model) {
		String isStore = null;
		if ( SecurityUtils.isStore() ) {// 商家登陆
			isStore = "isStore";
		}
		model.addAttribute("salesPromotionCategoryIsStore", isStore); // 页面是否显示商家选项的标识(商家登录不显示)
		return "salesPromotionCategory/list";
	}
	
	@RequestMapping("list-data")
	public String listData(Long storeId,String salesPromotionCategoryName,int salesPromotionCategoryType,Page page, Model model) {
		
		if ( SecurityUtils.isStore() ) {// 商家登陆
			storeId = SecurityUtils.getAccountStoreId();
		}
		
		model.addAttribute("data", salesPromotionCategoryService.list(storeId,salesPromotionCategoryName,salesPromotionCategoryType, page));
		return "salesPromotionCategory/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		Long storeId = null;
		if (SecurityUtils.isStore()) {// 商家登陆
			storeId = SecurityUtils.getAccountStoreId();
			Store store = shopService.findByStoreId(storeId);
			
			if( null == store){
				return "salesPromotionCategory/list";
			}
			StringBuffer sb = new StringBuffer();
			for (String element : store.getShipTypeGroup()) {
				sb.append(element);
				sb.append(",");
			}
			if(sb.length() > 0){
				sb.delete(sb.length()-1, sb.length());
			}
			// 得到商家所拥有的送货方式和支付方式
			model.addAttribute("shipType_group",sb.toString());
			model.addAttribute("payType_group",store.getPayType());
			model.addAttribute("storeName", store.getName()); // 页面显示商家名称
		}
		model.addAttribute("addOrUpdate_storeId",storeId);
		return "/salesPromotionCategory/add";
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){

		SalesPromotionCategory salesPromotionCategory = salesPromotionCategoryService.findById(id);
		if( null == salesPromotionCategory ){
			return "salesPromotionCategory/list"; 
		}
		Store store = shopService.findByStoreId(salesPromotionCategory.getStoreId());
		if( null == store){
			return "salesPromotionCategory/list";
		}
		
		StringBuffer sb = new StringBuffer();
		for (String element : store.getShipTypeGroup()) {
			sb.append(element);
			sb.append(",");
		}
		if(sb.length() > 0){
			sb.delete(sb.length()-1, sb.length());
		}
		model.addAttribute("shipType_group",sb.toString());
		model.addAttribute("payType_group",store.getPayType());
		// 促销分类对象
		model.addAttribute("salesPromotionCategory",salesPromotionCategory);
		model.addAttribute("storeName", store.getName()); // 页面显示商家名称
		model.addAttribute("addOrUpdate_storeId",salesPromotionCategory.getStoreId());
		return "/salesPromotionCategory/add";
	}
	
	@RequestMapping(value = "view/{id}",method = RequestMethod.GET)
	public String toView(@PathVariable Long id,Model model ){

		SalesPromotionCategory salesPromotionCategory = salesPromotionCategoryService.findById(id);
		if( null == salesPromotionCategory ){
			return "salesPromotionCategory/list"; 
		}
		Store store = shopService.findByStoreId(salesPromotionCategory.getStoreId());
		if( null == store){
			return "salesPromotionCategory/list";
		}
		
		StringBuffer sb = new StringBuffer();
		for (String element : store.getShipTypeGroup()) {
			sb.append(element);
			sb.append(",");
		}
		if(sb.length() > 0){
			sb.delete(sb.length()-1, sb.length());
		}
		model.addAttribute("shipType_group",sb.toString());
		model.addAttribute("payType_group",store.getPayType());
		model.addAttribute("salesPromotionCategory",salesPromotionCategory);
		model.addAttribute("storeName", store.getName()); // 页面显示商家名称
		model.addAttribute("addOrUpdate_storeId",salesPromotionCategory.getStoreId());
		return "/salesPromotionCategory/view";
	}
	
	@RequestMapping(value="list/ajax/storePayType",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int storePayType(Long storeId) {
		Store store = shopService.findByStoreId(storeId); 
		return (null == store) ? 3 : store.getPayType();
	}
	
	@RequestMapping(value="list/ajax/storeShipType",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String[] storeShipType(Long storeId) {
		Store store = shopService.findByStoreId(storeId); 
		return (null == store) ? new String[]{} : store.getShipTypeGroup();
	}
	
	@RequestMapping(value = "list/ajax/checkName")
	@ResponseBody
	public int checkName(Long id,Long storeId,String name) {
		if (salesPromotionCategoryService.checkName(id,storeId,name)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(SalesPromotionCategory salesPromotionCategory,String startTime,String endTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			salesPromotionCategory.setStartDate(df.parse(startTime)); 
			salesPromotionCategory.setEndDate(df.parse(endTime)); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if( null == salesPromotionCategory.getId()){
			salesPromotionCategory.setId(idService.getId());
			salesPromotionCategoryService.save(salesPromotionCategory);			
		} else {
			salesPromotionCategoryService.update(salesPromotionCategory);
		}
		
		return "redirect:/salesPromotionCategory/list";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long storeId,Long id){
		return salesPromotionCategoryService.delete(storeId,id) ? 1:0;
	}
	
	@RequestMapping(value="again/again-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int againById(Long storeId,Long id){
		return salesPromotionCategoryService.again(storeId,id) ? 1:0;
	}
	
	// 商家弹窗
	@RequestMapping("showShopModel/list/list-data")
	public String showShopModelListData(String name, String mobile, Page page, Model model) {
		model.addAttribute("data", shopService.getShop(name, mobile,null, page));
		return "salesPromotionCategory/showShopModelList-data";
	}
	
	// 商品弹窗
	@RequestMapping("showProductStockModel/list/list-data")
	public String showProductStockModelListData(String productName,Long model_query_storeId,Long salesPromotionCategoryId,Long productCategoryId, Page page, Model model) {
		// 商家登陆
		if (SecurityUtils.isStore()) {
			model_query_storeId = SecurityUtils.getAccountStoreId();
		}
		
		if(ObjectUtils.equals(productCategoryId, -1l)){
			productCategoryId = null;
		}
		
		model.addAttribute("data", salesPromotionCategoryService.findProductStockList(productName,model_query_storeId,productCategoryId,page));
		
		SalesPromotionCategory salesPromotionCategory = salesPromotionCategoryService.findById(salesPromotionCategoryId);
		if( null != salesPromotionCategory){
			// 得到此促销分类的促销数量 (用于判断选中的促销商品的库存不能小于此促销数量)
			model.addAttribute("stock_salesNum",salesPromotionCategory.getSalesNum());
		}
		
		return "salesPromotionCategory/showProductStockModelList-data";
	}
	
	@RequestMapping(value = "saveSalesPromotionInfo", method = RequestMethod.POST)
	public String save(String[] salesPromotionInfoCache,Long salesPromotionCategoryIdCache) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		SalesPromotionCategory salesPromotionCategory = salesPromotionCategoryService.findById(salesPromotionCategoryIdCache);
		if( null == salesPromotionCategory ){
			return "redirect:/salesPromotionCategory/list";
		}
		
		boolean flag = false;
		
		Long isHaveUse = System.currentTimeMillis()-salesPromotionCategory.getStartDate().getTime() ;
		if(isHaveUse.longValue() >= 0){
			flag = true; // 促销已经开始
		}
		Integer salesNum = salesPromotionCategory.getSalesNum();
		Double amount = salesPromotionCategory.getAmount();
		Double discount = salesPromotionCategory.getDiscount();
		Long storeId = salesPromotionCategory.getStoreId();
		
		// 用于记录每个选中的当前促销商品的上下架情况的集合(用于之后活动结束后自动恢复之前的上下架状态)
		List<Long> listShelves = new ArrayList<Long>();
		
		for (String stock_originalPrice_shelves : salesPromotionInfoCache) {
			String[] stock_originalPrice_shelves_Array = stock_originalPrice_shelves.split("_");
			Object[] arg = new Object[9];
			arg[0] = idService.getId();    // id
			arg[1] = storeId; // storeId
			arg[2] = stock_originalPrice_shelves_Array[0]; // stockId
			arg[3] = salesPromotionCategoryIdCache; // salesPromotionCategoryId
			arg[4] = new Date();  // createDate
			arg[5] = salesNum;  // salesNum
			arg[6] = stock_originalPrice_shelves_Array[1]; // originalPrice
			double originalPrice = Double.parseDouble(stock_originalPrice_shelves_Array[1]);
			arg[7] = (null == amount) ? NumberUtils.multiply(originalPrice,discount,0.01D):amount ; // price 
			arg[8] = stock_originalPrice_shelves_Array[2]; // shelves
			batchArgs.add(arg);
			if(flag){
				listShelves.add(Long.parseLong(stock_originalPrice_shelves_Array[0]));				
			}
		}
		salesPromotionService.save(batchArgs); // 批量保存
		for (Long stockId : listShelves) {
			// 将选中促销的商品下架
			productStockService.updateShelves(stockId, Constants.ProductStock.ALL_DOWN);			
		}
		return "redirect:/salesPromotionCategory/list";
	}
	
	@RequestMapping(value = "list/ajax/checkProductStock")
	@ResponseBody
	public int checkProductStock(Long stockId,Integer stock) {
		return productStockService.checkProductStock(stockId,stock);
	}
	
	@RequestMapping(value="list/ajax/productSystemCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productSystemCategory(Integer type, Long storeId) {
		List<ProductCategory> productCategorys = productCategoryService.findByTypeStoreId(storeId, type);
		return productCategorys;
	}
}