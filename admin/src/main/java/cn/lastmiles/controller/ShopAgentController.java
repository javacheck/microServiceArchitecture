package cn.lastmiles.controller;

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

import cn.lastmiles.bean.Account;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.ShopAgentService;

@Controller
@RequestMapping("shopAgent")
public class ShopAgentController {
	private final static Logger logger = LoggerFactory.getLogger(ShopAgentController.class);
	
	@Autowired
	private ShopAgentService shopAgentService;
	@Autowired
	private AccountService accountService;
	
	@RequestMapping("list")
	public String list() {
		return "shopAgent/list";
	}

	@RequestMapping("list-data")
	public String listData(String mobile, Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		
		model.addAttribute("data", shopAgentService.list(mobile,page));
		return "shopAgent/list-data";
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
		
		return "/shopAgent/updatePwd";
	}
	/**
	 * 修改账号密码
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping(value="updatePwd",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateAccountPwd(Account account){
		logger.debug("account={}",account);
		shopAgentService.updateAccountPwd(account);
		return "1";
	}
}
