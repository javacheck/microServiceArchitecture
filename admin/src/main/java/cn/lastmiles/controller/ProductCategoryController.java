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

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductAttributeService;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productCategory")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductAttributeService productAttributeService;

	@Autowired
	private StoreService storeService;

	private static final Logger logger = LoggerFactory
			.getLogger(ProductCategoryController.class);

	/**
	 * 分类管理主页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "manager")
	public String manager(Integer type, Long storeId, Model model) {
		if (SecurityUtils.isAdmin()) {// 管理员登陆
			if (type == null) {
				type = Constants.ProductCategory.PRODUCT_CATEGORY_SYSTEM;
			}
		} else if (SecurityUtils.isStore()) {// 商家登陆
			if (type == null) {
				type = Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM;
			}
			//type = Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM;
			storeId = SecurityUtils.getAccountStoreId();
		} else {
			return null;
		}
		List<ProductCategory> productCategorys = productCategoryService
				.findByTypeStoreId(storeId, type);
		String jsonTree = JsonUtils.objectToJson(productCategorys);
		model.addAttribute("jsonTree", jsonTree);
		model.addAttribute("type", type);
		model.addAttribute("storeId", storeId);
		logger.debug("jsonTree = {}", jsonTree);
		return "productCategory/list";
	}

	/**
	 * 保存 提交修改 提交保存
	 * 
	 * @param id
	 * @param name
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "manager/add", method = RequestMethod.POST)
	@ResponseBody
	public Long add(ProductCategory productCategory) {
		if (productCategoryService.checkName(productCategory.getName(),
				productCategory.getId(), productCategory.getType(),
				productCategory.getStoreId())) {
			return 0L;
		}

		if (null != productCategory.getId()&&!productCategory.getId().equals(0L)) {
			return productCategoryService.updateById(productCategory);
		} else {
			if (SecurityUtils.isStore()) {// 商家添加 强制 添加自己商店的自定义分类
				productCategory
						.setType(Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
				productCategory.setStoreId(SecurityUtils.getAccountStoreId());
			}
			if (productCategory.getpId().intValue() == Constants.ProductCategory.PRODUCT_CATEGORY_FATHER_ADD) {
				productCategory.setpId(null);
			}
			return productCategoryService.save(productCategory);
		}
	}

	/**
	 * 根据parentId查询子类
	 * 
	 * @param parentId
	 * @param model
	 * @return
	 */
	@RequestMapping("manager/ajax/list-by-parent")
	public String list(Long parentId, Model model) {
		List<Store> storeList = storeService
				.findMyStore(SecurityUtils.getAccountId(),
						SecurityUtils.getAccountStoreId());
		StringBuffer storeIds = new StringBuffer();
		for (int i = 0; i < storeList.size(); i++) {
			if (i != 0) {
				storeIds.append(",");
			}
			storeIds.append(storeList.get(i).getId());
		}

		model.addAttribute(
				"list",
				productCategoryService.findByParentId(parentId,
						storeIds.toString()));
		return "productCategory/list-by-parent";
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("manager/delete/{id}")
	@ResponseBody
	public int delete(@PathVariable Long id) {
		// 查找分类下面是否有商品
		List<Product> products = productService.findByCategoryId(id);
		if (products != null && products.size() >= 1) {// 分类下有商品
			// 有 不可删除
			return 1;
		}
		// 删除分类
		return productCategoryService.deleteById(id);
	}

	/**
	 * 所有列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "productCategory/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productCategoryService.findAll(name, page));
		return "productCategory/list-data";
	}

	@RequestMapping("ajax/find-by-parent")
	@ResponseBody
	public List<ProductCategory> findByParent(Long parentId, int type,
			Long storeId) {
		logger.debug("find by parent parentid = {},type = {},storeId = {}",parentId,type,storeId);
		Long sId = SecurityUtils.getAccountStoreId();
		if (storeId != null && storeId.longValue() != -1L) {
			sId = storeId;
		}
		return productCategoryService.findByParent(parentId, type == 0 ? null
				: sId);
	}
	/**
	 * 分类查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ajax/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> ajaxList(Integer type, Long storeId) {
		if (SecurityUtils.isAdmin()) {// 管理员登陆
			if (type == null) {
				type = Constants.ProductCategory.PRODUCT_CATEGORY_SYSTEM;//系统分类
			}
		} else if (SecurityUtils.isStore()) {// 商家登陆
			if (type == null) {
				type = Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM;
			}
			//type = Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM;
			storeId = SecurityUtils.getAccountStoreId();
		} else {
			return null;
		}
		
		List<ProductCategory> productCategorys = productCategoryService.findByTypeStoreId(storeId, type);
		logger.debug("ajaxList --> jsonTree = {}", productCategorys);
		return productCategorys;
	}
	
	/**
	 * 分类查询
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ajax/find", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductCategory ajaxFind(Long id) {
		ProductCategory productCategory = productCategoryService.findById(id);
		logger.debug("ajaxFind --> productCategory = {}", productCategory);
		return productCategory;
	}

}
