package cn.self.cloud.controller;

import cn.self.cloud.bean.Account;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.AccountRoleService;
import cn.self.cloud.service.AccountService;
import cn.self.cloud.service.RoleService;
import cn.self.cloud.service.StoreService;
import cn.self.cloud.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	public String listData(String mobile, Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		model.addAttribute("data", accountService.list(mobile, page,SecurityUtils.getAccountId()));
		return "account/list-data";
	}

	/**
	 * 数据 修改||添加 界面
	 * 
	 * @param account
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Account account, Model model, Long[] roleIds) {
		if (null != account.getId()) {
			String info = account.getId()
					+ (accountService.update(account) ? "修改成功" : "修改失败");
			// 更新子节点path
			accountService.UpdatePath(accountService.findAllByParentId(account
					.getId()));
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
	 * @param model
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		Account loginAccount = accountService.findById(SecurityUtils.getAccountId());// 当前登录用户
		System.out.println(loginAccount);
		model.addAttribute("list",accountService.findAllByParentId(loginAccount.getId()));
		model.addAttribute("roles",	roleService.findKidsByAccountId(SecurityUtils.getAccountId()));
		model.addAttribute("stores",storeService.findMyStore(SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId()));
		model.addAttribute("defaultSelect", loginAccount.getStoreId());// 默认选中状态
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
		Account account = accountService.findById(id);// 当前操作用户
		model.addAttribute("account", account);
		Account loginAccount = accountService.findById(SecurityUtils.getAccountId());// 当前登录用户
		model.addAttribute("loginAccount", loginAccount);
		Account parentAccount = accountService.findById(account.getParentId());// 当前操作用户的父级用户
		model.addAttribute("parentAccount", parentAccount);
		model.addAttribute("list",accountService.findAllByParentId(loginAccount.getId()));// 当前用户创建的用户
		model.addAttribute("roles",	roleService.findKidsByAccountId(SecurityUtils.getAccountId()));// 当前可选择角色
		model.addAttribute("nowRoleIds",roleService.findByAccountIdReturn(account.getId()));// 当前用户的角色
		model.addAttribute("defaultSelect", account.getStoreId());// 默认选中状态
		model.addAttribute("stores", storeService.findAll());// 商店列表
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

	@RequestMapping(value = "checkMobile/{mobile}")
	@ResponseBody
	public String checkMobile(@PathVariable String mobile, Model model) {
		if (accountService.checkMobile(mobile)) {
			return "1";
		} else {
			return "0";
		}

	}

}
