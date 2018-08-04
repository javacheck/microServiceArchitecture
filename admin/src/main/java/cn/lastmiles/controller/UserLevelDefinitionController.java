package cn.lastmiles.controller;
/**
 * createDate : 2016年1月20日下午2:15:45
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.service.OrganizationService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.UserLevelDefinitionService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.CommodityCategoryService;

/**
 * *会员等级管理(列表/新增/修改/删除)
 */
@Controller
@RequestMapping("userLevelDefinition")
public class UserLevelDefinitionController {
	private final static Logger logger = LoggerFactory.getLogger(UserLevelDefinitionController.class);
	@Autowired
	private UserLevelDefinitionService userLevelDefinitionService; // 会员等级设置对象
	@Autowired
	private StoreService storeService;
	@Autowired
	private OrganizationService organizationService;  
	@Autowired
	private CommodityCategoryService commodityCategoryService;
	/**
	 * 信息列表
	 * @return 页面跳转
	 */
	@RequestMapping("list")
	public String list(Model model){
		boolean flag = false;
		Store shop = storeService.findById(SecurityUtils.getAccountStoreId());
		if( null == shop.getOrganizationId() ){ // 没有组织架构(没有总部)
			flag = true; // 可以新增修改
		}
		model.addAttribute("isMainStore", SecurityUtils.isMainStore() || flag );
		return "userLevelDefinition/list";
	}
	
	/**
	 * 信息详情列表
	 * @param name 查询名称
	 * @param page 页码对象
	 * @param model 存储模型
	 * @return 页面跳转
	 */
	@RequestMapping("list-data")
	public String list_data(String name,Page page,Model model){
		StringBuffer storeIdString = new StringBuffer();
		boolean flag = false;
		if (SecurityUtils.isStore()) {
			if(SecurityUtils.isMainStore()){
				storeIdString.append(SecurityUtils.getAccountStoreId());				
			} else {
				Store shop = storeService.findById(SecurityUtils.getAccountStoreId());
				if( null == shop.getOrganizationId() ){ // 没有组织架构(没有总部)
					flag = true; // 可以新增修改
					storeIdString.append(SecurityUtils.getAccountStoreId());		
				} else {
					List<Organization> listOrg = organizationService.getParentTreeById(shop.getOrganizationId(), true);
					storeIdString.append(listOrg.get(0).getStoreId());
				}
			}
		}
		model.addAttribute("isMainStore", SecurityUtils.isMainStore() || flag );
		model.addAttribute("userLevelDefinition",userLevelDefinitionService.findByName(storeIdString.toString(),name,page));
		return "userLevelDefinition/list-data";
	}
	/**
	 * 异步加载树形结构
	 * @return
	 */
	@RequestMapping(value = "ajax/loadZtreeListNoSubordinate", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String,Object>> loadZtreeListNoSubordinate(Long id) {
		Long storeId=null;
		if(SecurityUtils.isMainStore()){
			storeId=SecurityUtils.getAccountStoreId();
		}else{
			Store s=storeService.findTopStore(SecurityUtils.getAccountStoreId());
			storeId=s.getId();
		}
		List<ProductCategory> productCategoryList = commodityCategoryService.findByStoreId(storeId);
		List<Long> longC=null;
		if(id!=null){
			longC=userLevelDefinitionService.findUserLevelDefinitionProductCategoryById(id);
		}
		List<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
		if( null != productCategoryList ){
			for (ProductCategory productCategory : productCategoryList) {
				Map<String,Object> map=new HashMap<String, Object>();
				if(productCategory.getpId()==null){
					map.put("id", productCategory.getId());
					map.put("pId", productCategory.getpId());
					map.put("name", productCategory.getName());
					if(longC!=null){
						for(Long catogoryId:longC){
							if(ObjectUtils.equals(catogoryId, productCategory.getId())){
								map.put("checked", true);
								break;
							}
						}
					}
					listMap.add(map);
				}
			}				
		}
		logger.debug("listMap is {}",listMap);
		return listMap;
	}
	
	@RequestMapping(value = "ajax/judgeExistType", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String,Object> judgeExistType(Long storeId) {
		return userLevelDefinitionService.judgeByStoreId(storeId);
	}
	/**
	 * 新增信息
	 * @return 新增跳转
	 */
	@RequestMapping( value = "add" , method = RequestMethod.GET )
	public String toAdd(Model model){
		UserLevelDefinition userLevelDefinition = new UserLevelDefinition();
		userLevelDefinition.setStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("userLevelDefinition",userLevelDefinition);
		return "userLevelDefinition/add";
	}
	
	/**
	 * 修改信息
	 * @param id 判断标识
	 * @param model 存储模型
	 * @return 修改跳转
	 */
	@RequestMapping( value = "update/{id}" , method = RequestMethod.GET )
	public String toUpdate(@PathVariable Long id,Model model){
		model.addAttribute("isUpdate",true);
		model.addAttribute("userLevelDefinition",userLevelDefinitionService.findById(id));
		return "userLevelDefinition/add";
	}
	/**
	 * 详情信息
	 * @param id 判断标识
	 * @param model 存储模型
	 * @return 修改跳转
	 */
	@RequestMapping( value = "details/{id}" , method = RequestMethod.GET )
	public String details(@PathVariable Long id,Model model){
		model.addAttribute("isUpdate",true);
		model.addAttribute("userLevelDefinition",userLevelDefinitionService.findById(id));
		return "userLevelDefinition/details";
	}
	/**
	 * 异步检测字段唯一性
	 * @param id 根据id来判断(新增/修改)区分自身的标识
	 * @param name 判断标识
	 * @return 是否修改成功
	 */
	@RequestMapping( value = "list/ajax/checkUserLevelDefinitionName" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int checkUserLevelDefinitionName(Long id,Long storeId,String name){
		return userLevelDefinitionService.checkUserLevelDefinitionName(id,storeId,name);
	}
	
	/**
	 * (新增/修改)信息
	 * @param userLevelDefinition 保存对象
	 * @return (新增/修改)跳转
	 */
	@RequestMapping( value = "save" , produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(UserLevelDefinition userLevelDefinition,String dateCategorySJson){
		//userLevelDefinition.setStoreId(SecurityUtils.getAccountStoreId());
		List<Long> categoryIdList=new ArrayList<Long>();
		if(userLevelDefinition.getDiscountScope().intValue()==1){
			List<Object> ObjectList=JsonUtils.jsonToList(dateCategorySJson);
			for(int i=0;i<ObjectList.size();i++){
				@SuppressWarnings("unchecked")
				Map<String,Object> categoyIdMap=(Map<String,Object>)ObjectList.get(i);
				logger.debug("categoyIdMap.getKey={}",categoyIdMap.get("categoryId"));
				categoryIdList.add(Long.parseLong(categoyIdMap.get("categoryId").toString()));
			}
		}
		if( null == userLevelDefinition.getId() ){ // 新增
			userLevelDefinitionService.save(userLevelDefinition,categoryIdList);
		} else { // 修改
			userLevelDefinitionService.update(userLevelDefinition,categoryIdList);
		}
		return "1";
	}
	
	/**
	 * 删除信息
	 * @param id 判断标识
	 * @return 删除跳转
	 */
	@RequestMapping( value = "delete/deleteById" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int deleteById(Long id){
		return userLevelDefinitionService.deleteById(id);
	}
}