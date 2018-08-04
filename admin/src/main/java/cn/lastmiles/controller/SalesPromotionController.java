package cn.lastmiles.controller;
/**
 * createDate : 2015年8月19日 下午3:18:45 
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.SalesPromotionCategoryService;
import cn.lastmiles.service.SalesPromotionService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.utils.SecurityUtils;
/**
 * 主要实现的功能模块：促销管理
 */
@Controller
@RequestMapping("salesPromotion")
public class SalesPromotionController {

	@Autowired
	private SalesPromotionService salesPromotionService;
	@Autowired
	private SalesPromotionCategoryService salesPromotionCategoryService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping("list")
	public String list(Long storeId,Long salesPromotionCategoryId,Model model) {
		// 商家登陆
		if (SecurityUtils.isStore()) {
			storeId = SecurityUtils.getAccountStoreId();
			
			List<ProductCategory> categoryList = productSystemCategory(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM,SecurityUtils.getAccountStoreId());
			model.addAttribute("categoryList",categoryList);
		} else {
			if( null != storeId){
				List<ProductCategory> categoryList = productSystemCategory(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM,storeId);
				model.addAttribute("categoryList",categoryList);
			}
		}
		model.addAttribute("storeId",storeId);
		model.addAttribute("salesPromotionCategoryId",salesPromotionCategoryId);
		return "salesPromotion/list";
	}
	
	@RequestMapping("list-data")
	public String listData(Long storeId,Long salesPromotionCategoryId,Long productCategoryId,String productName,Page page, Model model) {
		// 商家登陆
		if (SecurityUtils.isStore()) {
			storeId = SecurityUtils.getAccountStoreId();
		}
		
		if(ObjectUtils.equals(productCategoryId, -1l)){
			productCategoryId = null;
		}
		
		model.addAttribute("data", salesPromotionService.list(storeId,salesPromotionCategoryId,productCategoryId,productName, page));
		return "salesPromotion/list-data";
	}
	
	@RequestMapping(value="list/ajax/productSystemCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productSystemCategory(Integer type, Long storeId) {
		List<ProductCategory> productCategorys = productCategoryService.findByTypeStoreId(storeId, type);
		return productCategorys;
	}
				
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id,Long stockId,int shelves){
		return salesPromotionService.delete(id,stockId,shelves) ? 1:0;
	}
	
	@RequestMapping(value = "updatePrice")
	@ResponseBody
	public int updatePrice(Long id,String price) {
		if (salesPromotionService.updatePrice(id,price)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@RequestMapping(value = "updateSalesNum")
	@ResponseBody
	public int updateSalesNum(Long id,String salesNum,Long stockId) {
		if( null == stockId){
			return 0;
		}
		
		int total = productStockService.getProductStock_stock(stockId);
		if(total != Constants.ProductStock.Store_Infinite){ // -- 库存表中是最大值
			if(StringUtils.isBlank(salesNum)){ // 修改为无限数量时，直接返回失败
				return 2;
			} else {
				if(Integer.parseInt(salesNum) > total){
					return 2;
				}
			}
		}
		if (salesPromotionService.updateSalesNum(id,salesNum)) {
			return 1;
		} else {
			return 0;
		}
	}
}