package cn.lastmiles.controller;
/**
 * createDate : 2016年2月25日上午10:43:54
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.Store;
import cn.lastmiles.service.OrganizationService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("organization")
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ShopService shopService;
	/**
	 * 菜单选项进入
	 * @return
	 */
	@RequestMapping("list")
	public String list() {
		return "organization/list";
	}
	
	/**
	 * 异步加载树形结构
	 * @return
	 */
	@RequestMapping(value = "ajax/loadZtreeList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Organization> ajaxLoadZtreeList() {
		Long storeId = null;
		if( !SecurityUtils.isAdmin() ) {
			storeId = SecurityUtils.getAccountStoreId();
		}
      
		return organizationService.findByStoreId(storeId);
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(String organizationName,Model model){
		model.addAttribute("organizationName", organizationName);
		return "/organization/add";
	}
	
	@RequestMapping(value="update",method = RequestMethod.GET)
	public String toUpdate(String organizationName,Long id,Model model){
		Organization organization = organizationService.findById(id);
		Store store = shopService.findMainShopById(organization.getStoreId());
		model.addAttribute("store", store);
		model.addAttribute("organization", organization);
		model.addAttribute("organizationName", organizationName);
		return "/organization/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(Organization organization,Store store,String organizationName,RedirectAttributes ra) {
		
		if (null != organization.getId()) {
			store.setUpdatedAccountId(SecurityUtils.getAccountId());
			organizationService.update(organization,store);
		} else {
			store.setAccountId(SecurityUtils.getAccountId());
			organizationService.save(organization,store);
		}
		ra.addFlashAttribute("organizationName", organizationName);
		ra.addFlashAttribute("addOrUpdateName", organization.getName());
		return "redirect:/organization/list";
	}
	
	
	@RequestMapping(value = "ajax/searchHigherLevelList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Organization> searchHigherLevelList(String keyword) {
		if( SecurityUtils.isAdmin() ){
			return organizationService.findByNameFuzzy(keyword);			
		} else {
			return organizationService.findByNameFuzzy(SecurityUtils.getAccountStoreId(),keyword);
		}
	}
	
	@RequestMapping(value = "checkParentName")
	@ResponseBody
	public Organization checkParentName(Long id,String parentName) {
		parentName = parentName.replaceAll("\\s*", "");
		return organizationService.checkParentName(id,parentName,SecurityUtils.getAccountStoreId());
	}
	
	@RequestMapping(value = "checkHasPermission")
	@ResponseBody
	public Organization checkHasPermission(Long id) {
		if(SecurityUtils.isAdmin()){
			return null;
		}
		return organizationService.checkHasPermission(id,SecurityUtils.getAccountStoreId());
	}
	
	@RequestMapping(value = "ajax/checkNameRepetition")
	@ResponseBody
	public int checkNameRepetition(Long parentId,Long id,String name) {
		return organizationService.checkNameRepetition(SecurityUtils.isAdmin(),parentId,id,name) > 0 ? 1 : 0;
	}
	
	@RequestMapping(value = "ajax/getChildrenTreeById")
	@ResponseBody
	public List<Organization> getChildrenTreeById(Long id,boolean olyDirectly) {
		return organizationService.getChildrenTreeById(id,olyDirectly);
	}
	
	@RequestMapping(value = "ajax/getParentTreeById")
	@ResponseBody
	public List<Organization> getParentTreeById(Long id,boolean onlyRoot) {
		return organizationService.getParentTreeById(id,onlyRoot);
	}
}