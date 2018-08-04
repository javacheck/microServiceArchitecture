package cn.lastmiles.controller;
/**
 * createDate : 2015年11月2日下午3:34:48
 */
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.PromotionProduct;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.PromotionService;
import cn.lastmiles.utils.FileServiceUtils;
import cn.lastmiles.utils.SecurityUtils;

/**
 * 促销管理
 */
@Controller
@RequestMapping("promotion")
public class PromotionController {
	
	@Autowired
	private PromotionService promotionService; // 促销
	@Autowired
	private ProductCategoryService productCategoryService; // 商品分类
	@Autowired
	private FileService fileService; // 文件
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductService productService;
	
	/**
	 * 促销列表
	 */
	@RequestMapping("list")
	public String list() {
		return "promotion/list";
	}
	
	/**
	 * 促销详情列表
	 * @return
	 */
	@RequestMapping("list-data")
	public String listData(String promotionName,int promotionType,int promotionStatus,String startDate,String endDate,Page page, Model model) {
		model.addAttribute("data", promotionService.list(SecurityUtils.getAccountStoreId(),promotionName,promotionType,promotionStatus,startDate,endDate, page));
		return "promotion/list-data";
	}
	
	/**
	 * 促销新增
	 * @param promotion_type 促销类型(首单、满减、折扣、组合)
	 * @return
	 */
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(int promotion_type,Model model){
		model.addAttribute("promotion_type",promotion_type);
		if( promotion_type == Constants.Promotion.TYPE_COMBINATION ){
//			List<ProductCategory> list =  productCategoryService.findByTypeStoreId(SecurityUtils.getAccountStoreId(), Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
//			model.addAttribute("categoryList",list);
			model.addAttribute("treeStoreId",SecurityUtils.getAccountStoreId());
		}
		return "/promotion/add";
	}
	
	/**
	 * 检查促销名称是否唯一
	 * @return
	 */
	@RequestMapping(value = "list/ajax/checkName")
	@ResponseBody
	public int checkName(Long id,String name) {
		if ( promotionService.checkName(id,SecurityUtils.getAccountStoreId(),name) ) {
			return 1;
		} else {
			return 0;
		}
	}
	
	// 商品弹窗
	@RequestMapping("showProductStockModel/list/list-data")
	public String showProductStockModelListData(int promotionType,String productName,Long productCategoryId, Page page, Model model) {
		if( ObjectUtils.equals(productCategoryId, -1l) ){
			productCategoryId = null;
		}
		if( promotionType == Constants.Promotion.TYPE_COMBINATION ){
			model.addAttribute("data", promotionService.findCombinationProductStockList(productName,SecurityUtils.getAccountStoreId(),productCategoryId,page));
		} else {
			model.addAttribute("data", promotionService.findProductStockList(productName,SecurityUtils.getAccountStoreId(),productCategoryId,page));			
		}
		
		return "promotion/showProductStockModelList-data";
	}
	
//	本来是专用的组合情况下的促销商品弹窗，后修改为与其他三种情况一样的促销商品弹窗而废除
//	/**
//	 * 促销商品弹窗 
//	 * @param promotionCache 已选中的促销商品组合(商品ID编号)
//	 * @return
//	 */
//	@RequestMapping("showCombinationProductStockModel/list/list-data")
//	public String showCombinationProductStockModelListData(String[] promotionCache, Model model) {
//		List<ProductStock> productStockCacheList = new ArrayList<ProductStock>();
//		
//		// 从数据库中获取当前商家的促销商品数据(属于当前商家的所有商品)
//		List<ProductStock> productStockList = promotionService.findProductStockList(SecurityUtils.getAccountStoreId());
//		// 从总数据中
//		if( null != promotionCache && null != productStockList ){
//			for (int i = 0; i < promotionCache.length; i++) {
//				Long productStockId = Long.parseLong(promotionCache[i]);
//				for (int j = 0; j < productStockList.size(); j++) {
//					ProductStock productStock = productStockList.get(j);
//					if(ObjectUtils.equals(productStockId,productStock.getId())){
//						productStockList.remove(j); //将现有商品移除集合
//						productStockCacheList.add(productStock);
//						break;
//					}
//				}
//			}			
//		}
//		model.addAttribute("productStockCacheList",productStockCacheList); // 组合商品
//		model.addAttribute("productStockList",productStockList); // 现有商品
//		return "promotion/showCombinationProductStock";
//	}
		
	/**
	 * 获取此商家的商品分类
	 */
	@RequestMapping(value="list/ajax/productSystemCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productSystemCategory() {
		return productCategoryService.findByTypeStoreId(SecurityUtils.getAccountStoreId(), Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
	}
	
	/**
	 * 保存促销商品
	 * @param promotion
	 * @param promotion_type
	 * @param startTime
	 * @param endTime
	 * @param imageFile 图片信息(只有组合情况下才需要上传图片信息)
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Promotion promotion,int promotion_type,String startTime,String endTime,@RequestParam("promotionImageId") MultipartFile imageFile) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			promotion.setStartDate(df.parse(startTime)); 
			promotion.setEndDate(df.parse(endTime)); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if( promotion_type == Constants.Promotion.TYPE_COMBINATION ){ // 组合情况下需要检测图片的上传
			if (null != imageFile && imageFile.getSize() > 0){
				if( null == promotion.getId() ){ // 新增
					String imageID;
					try {
						imageID = fileService.save(imageFile.getInputStream());
						promotion.setImageId(imageID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else { // 修改
					if(!"".equals(promotion.getImageId()) ){
						fileService.delete(promotion.getImageId()); // 先删除之前的					
					}
					String imageID;
					try {
						imageID = fileService.save(imageFile.getInputStream());
						promotion.setImageId(imageID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		promotion.setStoreId(SecurityUtils.getAccountStoreId());
		promotion.setAccountId(SecurityUtils.getAccountId());
		promotionService.save(promotion, promotion_type);
		
		return "redirect:/promotion/list";
	}
	
	/**
	 * 删除促销信息
	 */
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id,Long storeId){
		return promotionService.delete(id,storeId);
	}
	
	/**
	 * 修改促销信息
	 * @param type
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{type}/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable int type,@PathVariable Long id,Model model ){
		Promotion promotion = promotionService.findById(id);
		if( null == promotion ){
			return "redirect:/promotion/list";
		}
		StringBuffer sb = new StringBuffer();
		List<PromotionProduct> promotionProductList = promotionService.findProductById(id);
		if( null != promotionProductList ){
			for (PromotionProduct promotionProduct : promotionProductList) {
				sb.append(promotionProduct.getProductStockId());
				sb.append(",");
			}			
		}
		if( sb.length() > 0 ){
			sb.delete(sb.length()-1, sb.length());
		}
		// 将此促销信息所选择的商品传到前台进行分析和计算
		model.addAttribute("promotionProductList",sb.toString());
		
		if( type == Constants.Promotion.TYPE_COMBINATION ){
			List<ProductCategory> list =  productCategoryService.findByTypeStoreId(SecurityUtils.getAccountStoreId(), Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
			model.addAttribute("categoryList",list);
		}
		
		// 组合情况下需要将已选择了的商品组合成列表到页面进行展示(同时需要获取图片信息)
		if( type == Constants.Promotion.TYPE_COMBINATION ){
			List<ProductStock> productStockList = null;
			if( null != sb && sb.length() > 0 ){
				productStockList = promotionService.findProductStockList(SecurityUtils.getAccountStoreId(),sb.toString());
			}
			model.addAttribute("productStockList",productStockList);
			if( null != promotion.getImageId() ){
				promotion.setImagePIC( FileServiceUtils.getFileUrl(promotion.getImageId()) );				
			}
			ProductStock productStock = productStockService.findByPromotionId(promotion.getId());
			if( null != productStock ){
				Product product = productService.findById(productStock.getProductId());
				if( null != product ){
					promotion.setCategoryId(product.getCategoryId());
					promotion.setBarCode(productStock.getBarCode());
					promotion.setCostPrice(productStock.getCostPrice());
					promotion.setPrice(productStock.getPrice());
				}
			}
		}
		
		// 满减情况下需要解析JSON到Map数据
		if( type == cn.lastmiles.constant.Constants.Promotion.TYPE_FULL_SUBTRACT){ 
			LinkedHashMap<String, Object> fullSubtractMap = JsonUtils.jsonToLinkedHashMap(promotion.getCondition());
			model.addAttribute("fullSubtractMap",fullSubtractMap);
			promotion.setCondition("");
		}
		// 促销分类对象
		model.addAttribute("promotion",promotion);
		model.addAttribute("promotion_type",type);
		return "/promotion/add";
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
			productStockList = promotionService.findProductStockList(SecurityUtils.getAccountStoreId(),cacheString);
		}
		return productStockList;
	}
	
	@RequestMapping(value = "list/ajax/checkTimeInterleaving")
	@ResponseBody
	public int checkTimeInterleaving(Long id,int promotion_type,int status,String startTime,String endTime,String[] promotionProductCache) {
		// 促销开通才进行判断
		// 根据商家ID、起止时间和活动标识去查是否有标识所属的活动且时间存在交集的。
		// 将存在交集的活动中的商品ID与当前活动的商品ID进行对比,存在相同则记录下来
		// 如果相同记录集合为空，则直接返回可以新增或者修改的标识 0 , 方之则将商品ID记录集合进行查询得到商品名称连接名称并返回
		if( Constants.Promotion.STATUS_ON == status ){
			List<Promotion> promotionList = promotionService.checkTimeInterleaving(SecurityUtils.getAccountStoreId(),id,promotion_type,startTime,endTime);
			if( null == promotionList ){
				return 0;
			}
			
			if( promotionList.size() > 0 ){
				StringBuffer linkString = new StringBuffer();
				int i = 0;
				for (Promotion promotion : promotionList) {
					if( i > 0){
						linkString.append(",");
					}
					linkString.append(promotion.getId());
					i++;
				}	
				
				// 根据促销编号得到商品集合
				List<PromotionProduct> promotionProductList = promotionService.getPromotionProduct(linkString.toString());
				if( null == promotionProductList ){
					return 0;
				}
				if( promotionProductList.size() > 0 ){
					if( promotionProductCache.length > 0 ){
						boolean flag = false;
						for (PromotionProduct promotionProduct : promotionProductList) {
							if(flag){
								break;
							}
							for (int j = 0; j < promotionProductCache.length; j++) {
								Long productID = Long.parseLong(promotionProductCache[j]);
								if( promotionProduct.getProductStockId().longValue() == productID.longValue() ){
									flag = true;
									break;
								}
							}
						}
						if(flag){
							return 1;
						}
					} else { // 选择的是所有商品,则直接证明在时间交错段有重复商品
						return 1;
					}
				}
			}
		}
		
		return 0;
	}
	
	@RequestMapping(value="list/ajax/checkFirstOrFull",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkFirst(Long id,int promotion_type,int status) {
		if( Constants.Promotion.STATUS_OFF == status ){
			return 0;
		}
		return promotionService.checkFirstOrFull(SecurityUtils.getAccountStoreId(),id,promotion_type) ? 1 : 0;
	}
	
	@RequestMapping(value="list/ajax/existBarCode",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int byBarCodeFindProductStock(String id,String barCode) {
		int count = productStockService.byBarCodeFindProductStock(SecurityUtils.getAccountStoreId(),barCode,id);
		return count > 0 ? 1 : 0 ;
	}
}