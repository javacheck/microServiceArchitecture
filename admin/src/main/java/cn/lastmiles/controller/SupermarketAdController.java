package cn.lastmiles.controller;

import java.io.IOException;
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

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.SupermarketAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.SupermarketAdService;

/**
 * createDate : 2015年8月18日 下午4:08:58 
 */

@Controller
@RequestMapping("supermarketAd")
public class SupermarketAdController {

	private final static Logger logger = LoggerFactory.getLogger(SupermarketAdController.class);
	
	@Autowired
	private SupermarketAdService supermarketAdService;
	@Autowired
	private IdService idService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping("list")
	public String list(Model model) {
		return "supermarketAd/list";
	}
	
	@RequestMapping("list-data")
	public String listData(Long storeId,Page page, Model model) {
		logger.debug("storeId = {} ",storeId);
	
		page = supermarketAdService.list(storeId, page);
		model.addAttribute("data", page);
		return "supermarketAd/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "supermarketAd/add";
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		logger.debug("toUpdate  id -->"+id);
		SupermarketAd supermarketAd = supermarketAdService.findById(id);
		
		List<ProductCategory> productCategorys = productCategoryService.findByTypeStoreId(supermarketAd.getStoreId(), Constants.ProductCategory.PRODUCT_CATEGORY_CUSTOM);
		
		model.addAttribute("categoryList",productCategorys);
		model.addAttribute("supermarketAd",supermarketAd);
		return "/supermarketAd/add";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id,Long storeId){
		return supermarketAdService.delete(id,storeId) ? "1":"0";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(SupermarketAd supermarketAd,@RequestParam("supermarketImageId") MultipartFile supermarketImageId, Model model) {
		
		if (null != supermarketAd.getId()) {
			if (null != supermarketImageId && supermarketImageId.getSize() > 0) {
				fileService.delete(supermarketAd.getImageId()); // 先删除之前的
				String imageId=null;
				try {
					imageId = fileService.save(supermarketImageId.getInputStream());
					supermarketAd.setImageId(imageId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String info = supermarketAd.getId() + (supermarketAdService.update(supermarketAd) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			try {
				String imageId = fileService.save(supermarketImageId.getInputStream());
				supermarketAd.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
			supermarketAd.setId(idService.getId());
			String info = supermarketAdService.save(supermarketAd) ? "添加成功" : "添加失败";
			logger.debug(supermarketAd.getId() + "--" + info);
		}
		return "redirect:/supermarketAd/list";
	}
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String showModelListData(String name, String mobile, Page page, Model model) {
		page = shopService.getShop(name, mobile,0, page);
		model.addAttribute("data", page);
		return "supermarketAd/showModelList-data";
	}
	
	@RequestMapping(value="list/ajax/productSystemCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> productSystemCategory(Integer type, Long storeId, Model model) {
		logger.debug("type is {}, storeId is {} ",type,storeId);
	
		List<ProductCategory> productCategorys = productCategoryService.findByTypeStoreId(storeId, type);
		return productCategorys;
	}
	
	@RequestMapping(value="list/ajax/checkDataHave",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkDataHave(Long id ,Integer position, Long storeId,Long productCategoryId) {
		logger.debug("id is {},position is {}, storeId is {},productCategoryId is {} ",id,position,storeId,productCategoryId);
		return supermarketAdService.checkDataHave(id,storeId,productCategoryId,position);
	}
}