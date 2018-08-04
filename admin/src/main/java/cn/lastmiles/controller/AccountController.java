package cn.lastmiles.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cn.lastmiles.bean.*;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AccountRoleService;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.RoleService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("account")
public class AccountController {
	private final static Logger logger = LoggerFactory
			.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private StoreService storeService;

	@RequestMapping("")
	public String index() {
		return "redirect:/account/list";
	}

	@RequestMapping("list")
	public String list() {
		return "account/list";
	}

	@RequestMapping("list-data")
	public String listData(String beginTime,String endTime,String name,Integer sex,String mobile, Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		name = name.replaceAll("\\s*", "");
		Integer type = -1;//默认不可查询
		Long agentId=-1L;
		Long storeId=-1L;
		//权限初始化
		if (SecurityUtils.isAdmin()) {//管理员  查询所有
			type=Constants.Account.ACCOUNT_TYPE_ADMIN;
			type = null;
		}else if (SecurityUtils.isAgent()) {//代理商
			agentId = SecurityUtils.getAccount().getAgentId();
			type=Constants.Account.ACCOUNT_TYPE_AGENT;
		}else if (SecurityUtils.isStore()) {//商家
			storeId = SecurityUtils.getAccountStoreId();
			type=Constants.Account.ACCOUNT_TYPE_STORE;
		} 
		model.addAttribute("data", accountService.list(beginTime,endTime,name,sex,mobile,type,agentId,storeId,page,SecurityUtils.getAccountId()));
		return "account/list-data";
	}

	/**
	 * 数据 修改||添加 界面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Account account, Model model, Long[] roleIds) {
		logger.debug("account is {} roleIds is {}" ,account, Arrays.asList(roleIds) );
		if (null != account.getId() && account.getId().equals(Constants.Account.SUPER_ACCOUNT_ID)) {
			logger.debug("用户{}正在进行非法操作（尝试修改系统管理员）",SecurityUtils.getAccountId());
			return "redirect:/account/list";
		}
		//权限初始化
		if (SecurityUtils.isAdmin()) {//管理员
			account.setType(Constants.Account.ACCOUNT_TYPE_ADMIN);
		}else if (SecurityUtils.isAgent()) {//代理商
			account.setType(Constants.Account.ACCOUNT_TYPE_AGENT);
			account.setAgentId(SecurityUtils.getAccountAgentId());
		}else if (SecurityUtils.isStore()) {//商家
			account.setType(Constants.Account.ACCOUNT_TYPE_STORE);
			account.setStoreId(SecurityUtils.getAccountStoreId());
		} 
		if (null != account.getId()) {
			String info = account.getId()+(accountService.update(account) ? "修改成功" : "修改失败");
			// 更新子节点path
			accountService.UpdatePath(accountService.findAllByParentId(account.getId()));
			accountRoleService.update(account.getId(), roleIds);
			logger.debug(info);
		} else {
			String info = accountService.save(account) ? "添加成功" : "添加失败";
			
			accountRoleService.save(account.getId(), roleIds);
			logger.debug(account.getId() + "--" + info);
		}
		return "redirect:/account/list";
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		model.addAttribute("roles",	roleService.findMyRoles(SecurityUtils.getAccountId()));
		return "/account/add";
	}

	/**
	 * 跳转修改界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		if (id.equals(Constants.Account.SUPER_ACCOUNT_ID)) {
			logger.debug("用户{}正在进行非法操作（尝试修改系统管理员）",SecurityUtils.getAccountId());
			return "/account/list";
		}
		Account account = accountService.findById(id);// 当前操作用户
		model.addAttribute("account", account);
		Account loginAccount = accountService.findById(SecurityUtils.getAccountId());// 当前登录用户
		model.addAttribute("loginAccount", loginAccount);
		model.addAttribute("roles",	roleService.findMyRoles(SecurityUtils.getAccountId()));// 当前可选择角色
		model.addAttribute("defaultroles",JsonUtils.objectToJson(roleService.findByAccountId(id)));
		return "/account/add";
	}

	/**
	 * 通过ID删除 数据
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable Long id, Model model) {
		String info = id + (accountService.delete(id) ? "删除成功" : "删除失败");
		logger.debug(info);
		// 更新子节点用户
		accountService.UpdatePath(accountService.findAllByParentId(id));
		return "1";
	}

	@RequestMapping(value = "checkMobile")
	@ResponseBody
	public String checkMobile(String mobile,Long id) {
		if (accountService.checkMobile(mobile,SecurityUtils.getAccount().getType())) {
			return "1";
		} else {
			return "0";
		}

	}

}
