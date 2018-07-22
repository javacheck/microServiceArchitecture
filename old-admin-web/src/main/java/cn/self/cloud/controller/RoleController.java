package cn.self.cloud.controller;

import cn.self.cloud.bean.Role;
import cn.self.cloud.service.PermissionService;
import cn.self.cloud.service.RoleService;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		model.addAttribute("data",
				roleService.findByCreatedId(SecurityUtils.getAccountId()));
		return "role/list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add() {
		return "role/add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Role role) {
		if (role.getId() != null) {
			roleService.updateName(role);
		} else {
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
		return roleService.findbyName(roleName)!=null?"1":"0";
	}
	@RequestMapping(value="delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id,Model model){
		roleService.delete(id);
		return "1";
	}
	
	@RequestMapping(value="permission/{id}")
	public String permission(@PathVariable Long id,Model model){
		model.addAttribute("permissions",permissionService.findByAccountId(SecurityUtils.getAccountId()));
		model.addAttribute("role",roleService.findById(id));
		model.addAttribute("permissionIds",permissionService.findByRoleIdReturnListLong(id));
		return "role/authorised";
	}
	@RequestMapping(value="permissionAdd", method = RequestMethod.POST)
	public String permissionAdd( Long roleId, Long[] permissionIds){
		permissionService.updateRolePermission(roleId,permissionIds);
		return "redirect:/role/list";
	}
}
