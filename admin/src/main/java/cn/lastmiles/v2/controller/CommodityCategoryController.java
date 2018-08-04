/**
 * createDate : 2016年5月26日上午11:02:22
 * 商品基本设置(商品分类)
 */
package cn.lastmiles.v2.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.CommodityCategoryService;
import cn.lastmiles.v2.service.CommodityManagerService;

@Controller
@RequestMapping("commodityCategory")
public class CommodityCategoryController {
	/**
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommodityCategoryController.class);
	@Autowired
	private CommodityCategoryService commodityCategoryService;
	@Autowired
	private CommodityManagerService commodityManagerService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ShopService shopService;
	
	/**
	 * 菜单列表点击
	 * @param model 储值容器
	 * @return 跳转页面
	 */
	@RequestMapping(value = "list")
	public String commodityCategoryList(Model model) {
		
		// 管理员登录标识为0、商店登录(总部)标识为1、商店登录(非总部)标识为2、其他身份登录为-1
		int loginIdentityMarking = SecurityUtils.isAdmin() ? 0 : (SecurityUtils.isStore() ? (SecurityUtils.isMainStore() ? 1 : 2 ):-1 );
		logger.debug("commodityCategoryList ------->>> loginIdentityMarking is {} ",loginIdentityMarking);
		model.addAttribute("loginIdentityMarking", loginIdentityMarking);
		
		return "/v2/commodityCategory/list";
	}

	/**
	 * 异步加载树形结构
	 * @return
	 */
	@RequestMapping(value = "ajax/loadZtreeList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> ajaxLoadZtreeList(Long storeId) {
		logger.debug("异步加载的树形结构的商家ID是：{}",storeId);
		
		Long currentStoreId = -1L;
		if( null == storeId ){ // 从页面传送过来的值
			if( !SecurityUtils.isAdmin() ) {
				currentStoreId = SecurityUtils.getAccountStoreId();
			}						
		} else {
			currentStoreId = storeId;
		}
		
		logger.debug("ajaxLoadZtreeList ----->>> load storeId is {}",currentStoreId);
		List<ProductCategory> productCategoryList = commodityCategoryService.findByStoreId(currentStoreId);
		// 当查询的商家即为当前登录商家的时候则将是否当前商家数据的标识设置为true
		if( ObjectUtils.equals(currentStoreId, SecurityUtils.getAccountStoreId())){
			if( null != productCategoryList ){
				for (ProductCategory productCategory : productCategoryList) {
					productCategory.setIsCurrentStoreData(true);
				}				
			}
		}
		logger.debug("productCategoryList is {}",productCategoryList);
		return productCategoryList;
	}
	
	
	/**
	 * 异步加载树形结构
	 * @return
	 */
	@RequestMapping(value = "ajax/loadZtreeListNoSubordinate", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> loadZtreeListNoSubordinate(Long storeId,Long id) {
		logger.debug("异步加载的树形结构的商家ID是：{},需要修改的ID是：{}",storeId,id);
		
		Long currentStoreId = -1L;
		if( null == storeId ){ // 从页面传送过来的值
			if( !SecurityUtils.isAdmin() ) {
				currentStoreId = SecurityUtils.getAccountStoreId();
			}						
		} else {
			currentStoreId = storeId;
		}
		
		logger.debug("ajaxLoadZtreeList ----->>> load storeId is {}",currentStoreId);
		List<ProductCategory> productCategoryList = commodityCategoryService.findByStoreIdAndId(currentStoreId,id);
		// 当查询的商家即为当前登录商家的时候则将是否当前商家数据的标识设置为true
		if( ObjectUtils.equals(currentStoreId, SecurityUtils.getAccountStoreId())){
			if( null != productCategoryList ){
				for (ProductCategory productCategory : productCategoryList) {
					productCategory.setIsCurrentStoreData(true);
				}				
			}
		}
		logger.debug("productCategoryList is {}",productCategoryList);
		return productCategoryList;
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(String categoryName,Model model){
		model.addAttribute("categoryName", categoryName);
		List<ProductCategory> productCategoryList = commodityCategoryService.findByStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("productCategoryList",productCategoryList);
		return "/v2/commodityCategory/add";
	}
	
	@RequestMapping(value="update",method = RequestMethod.GET)
	public String toUpdate(String categoryName,Long id,boolean isParent,Model model){
		ProductCategory productCategory = commodityCategoryService.findById(id);
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("isParent",isParent);
		return "/v2/commodityCategory/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(ProductCategory productCategory,String categoryName,RedirectAttributes ra) {
		
		
		if (null != productCategory.getId()) {
			commodityCategoryService.update(productCategory);
		} else {
			productCategory.setStoreId(SecurityUtils.getAccountStoreId());
			productCategory.setType(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM); // 自定义分类
			commodityCategoryService.save(productCategory);
		}
		ra.addFlashAttribute("categoryName", categoryName);
		ra.addFlashAttribute("addOrUpdateName", productCategory.getName());
		return "redirect:/commodityCategory/list";
	}
	
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isMainStore()){
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			storeIdString.append(SecurityUtils.getAccountStoreId());
			for (Store store : storeList) {
				storeIdString.append(",");
				storeIdString.append(store.getId());
			}
		}
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, "",  page);
		
		model.addAttribute("data", page);
		return "/v2/commodityCategory/showModelList-data";
	}
	
	@RequestMapping( value = "delete/deleteById" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int deleteById(Long id){
		if(commodityManagerService.exitProductByCategoryId(id) > 0 ){
			return 0;
		};
		return commodityCategoryService.deleteById(id);
	}
	
	@RequestMapping( value = "list/ajax/existCategoryName" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int existCategoryName(String name,Long id){
		return commodityCategoryService.existCategoryName(name,id,SecurityUtils.getAccountStoreId());
	}
}