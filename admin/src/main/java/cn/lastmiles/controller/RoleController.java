package cn.lastmiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Role;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.PermissionService;
import cn.lastmiles.service.RoleService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;

	@RequestMapping("")
	public String index() {
		return "redirect:/role/list";
	}

	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("loginAccount",SecurityUtils.getAccount());
		if (SecurityUtils.isAdmin()) {//是否为超级管理员
			model.addAttribute("data",roleService.findAll());
		}else{
			model.addAttribute("data",roleService.findMyRoles(SecurityUtils.getAccountId()));
		}
		
		return "role/list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add() {
		return "role/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Role role) {
		System.out.println(SecurityUtils.getAccount().getType());
		if (SecurityUtils.isAdmin()) {
			role.setType(Constants.Account.ACCOUNT_TYPE_ADMIN);
			role.setOwnerId(null);
		}
		if (SecurityUtils.isStore()) {
			role.setType(Constants.Account.ACCOUNT_TYPE_STORE);
			role.setOwnerId(SecurityUtils.getAccountStoreId());
		}
		if (SecurityUtils.isAgent()) {
			role.setType(Constants.Account.ACCOUNT_TYPE_AGENT);
			role.setOwnerId(SecurityUtils.getAccountAgentId());
		}
		if (role.getId() != null) {
			roleService.updateName(role);
		} else {
			role.setCreatedId(SecurityUtils.getAccountId());
			roleService.save(role);
		}
		return "redirect:/role/list";
	}
	@RequestMapping(value="update/{id}")
	public String update(@PathVariable Long id,Model model){
		model.addAttribute("role",roleService.findById(id));
		return "role/add";
	}
	@RequestMapping(value="ajax/checkName/{roleName}")
	@ResponseBody
	public String checkName(@PathVariable String roleName,Model model){
		if (SecurityUtils.isAdmin()) {
			return roleService.findbyName(roleName,Constants.Account.ACCOUNT_TYPE_ADMIN,null)!=null?"1":"0";
		}
		if (SecurityUtils.isStore()) {
			return roleService.findbyName(roleName,Constants.Account.ACCOUNT_TYPE_STORE,SecurityUtils.getAccountStoreId())!=null?"1":"0";
		}
		if (SecurityUtils.isAgent()) {
			return roleService.findbyName(roleName,Constants.Account.ACCOUNT_TYPE_AGENT,SecurityUtils.getAccountAgentId())!=null?"1":"0";
		}
		return "1";
		
	}
	@RequestMapping(value="delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id,Model model){
		roleService.delete(id);
		return "1";
	}
	
	@RequestMapping(value="permission/{id}/status/{status}")
	public String permission(@PathVariable Long id,Model model,@PathVariable String status){
		if (SecurityUtils.isSuperAccount()) {
			model.addAttribute("permissions",permissionService.findByALLGroup(SecurityUtils.getAccountId()));
		}else{
			model.addAttribute("permissions",permissionService.findByAccountIdGroup(SecurityUtils.getAccountId()));
		}
		model.addAttribute("role",roleService.findById(id));
		model.addAttribute("permissionIds",permissionService.findByRoleIdReturnListLong(id));
		model.addAttribute("status",status);
		return "role/authorised";
	}
	@RequestMapping(value="permissionAdd", method = RequestMethod.POST)
	public String permissionAdd(Long roleId, Long[] permissionIds){
		permissionService.updateRolePermission(roleId,permissionIds);
		return "redirect:/role/list";
	}
}
