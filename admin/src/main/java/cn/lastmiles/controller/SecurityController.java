package cn.lastmiles.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.google.code.kaptcha.Constants;

import cn.lastmiles.bean.Account;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.PermissionService;
import cn.lastmiles.service.RoleService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.utils.WebUtils;

@Controller
public class SecurityController {
	private final static Logger logger = LoggerFactory
			.getLogger(SecurityController.class);
	private final static String COOKIESTOKENKEY = "_lm_token";
	private final static String REMEMBERCACHEKEY = "login:remember:";
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private CacheService cacheService;

	@RequestMapping("update-password")
	@ResponseBody
	public String updatePassword(String password, String oldPassword) {
		Account account = SecurityUtils.getAccount();
		if (!PasswordUtils.checkPassword(oldPassword, account.getPassword())) {
			// 旧密码不对
			return "2";
		}
		account.setPassword(PasswordUtils.encryptPassword(password));
		accountService.updatePassword(account.getPassword(), account.getId());

		WebUtils.setAttributeToSession(
				cn.lastmiles.constant.Constants.ACCOUNT_SESSION_KEY, account);
		return "1";
	}

	@RequestMapping("logout")
	public String logout() {
		WebUtils.getSession().invalidate();
		return "redirect:/login?action=1";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(String action) {
		if (StringUtils.isBlank(action)){
			String token = WebUtils.getAttributeFromCookies(COOKIESTOKENKEY);
			if (StringUtils.isNotBlank(token)){
				//
				Account account = (Account) cacheService.get(REMEMBERCACHEKEY + token);
				if (account != null){
					loginSuccess(account);
					return "redirect:/";
				}
			}
		}
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(String username, String password,int type, String captcha,
			String remember, Model model) {
		String text = (String) WebUtils
				.getAttributeFromSession(Constants.KAPTCHA_SESSION_KEY);
		
		logger.debug("remember = {}",remember);
		if (ConfigUtils.getProperty("redis.ip") != null
				&& !ConfigUtils.getProperty("redis.ip").startsWith("192")) {
			if (StringUtil.isBlank(captcha) || !captcha.equalsIgnoreCase(text)) {
				model.addAttribute("error", "验证码不对");
				model.addAttribute("username", HtmlUtils.htmlEscape(username));
				model.addAttribute("password", HtmlUtils.htmlEscape(password));
				model.addAttribute("type",type);
				model.addAttribute("remember", remember);
				return "login";
			}
		}
		Account account = accountService.login(username, type);
		logger.debug("login account = {} password is {} type is {}",account,type);
		
		if (account == null) {
			model.addAttribute("error", "用户名不对或者此帐号不属于此身份");
			model.addAttribute("username", HtmlUtils.htmlEscape(username));
			model.addAttribute("password", HtmlUtils.htmlEscape(password));
			model.addAttribute("type",type);
			model.addAttribute("remember", remember);
			return "login";
		}
		
		if ( !(null != account && PasswordUtils.checkPassword(password, account.getPassword())) ) {
			model.addAttribute("error", "密码错误");
			model.addAttribute("username", HtmlUtils.htmlEscape(username));
			model.addAttribute("password", HtmlUtils.htmlEscape(password));
			model.addAttribute("type",type);
			model.addAttribute("remember", remember);
			return "login";
		}
		
		loginSuccess(account);

		String token = WebUtils.getAttributeFromCookies(COOKIESTOKENKEY);
		logger.debug("token = {}",token);
		if (StringUtils.isNotBlank(remember)) {
			if (StringUtils.isBlank(token)) {
				//保存token到cookies和缓存
				token = StringUtils.uuid();
				int expiry = 60 * 60 * 24 * 7;
				WebUtils.setAttributeToCookies(COOKIESTOKENKEY, token, expiry);
				cacheService.set(REMEMBERCACHEKEY + token, account,
						Long.valueOf(expiry));
			}
		} else {
			if (StringUtils.isNotBlank(token)) {
				WebUtils.deleteCookies(COOKIESTOKENKEY);
				cacheService.delete(REMEMBERCACHEKEY + token);
			}
		}

		return "redirect:/";
	}

	private void loginSuccess(Account account) {
		// 帐号信息13622884338   A010000001
		WebUtils.setAttributeToSession(
				cn.lastmiles.constant.Constants.ACCOUNT_SESSION_KEY, account);
		WebUtils.setAttributeToSession(
				cn.lastmiles.constant.Constants.STORE_SESSION_KEY, account.getStore());
		// 拥有的角色
		WebUtils.setAttributeToSession(
				cn.lastmiles.constant.Constants.ROLE_SESSION_KEY,
				roleService.findByAccountId(account.getId()));
		// 拥有的权限
		WebUtils.setAttributeToSession(
				cn.lastmiles.constant.Constants.PERMISSION_SESSION_KEY,
				permissionService.findByAccountId(account.getId()));
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value = "execSql", method = RequestMethod.GET)
	public String execSql(){
		Account account = SecurityUtils.getAccount();
		if (account.getMobile().equals("admin") && account.getId().equals(1L)){
			return "execsql";
		}
		return null;
	}
	
	@RequestMapping(value = "execSql", method = RequestMethod.POST)
	@ResponseBody
	public Object execSql(String sql){
		Account account = SecurityUtils.getAccount();
		if (account.getMobile().equals("admin") && account.getId().equals(1L)){
			
			if (sql.trim().startsWith("update") || sql.trim().startsWith("delete")){
				return jdbcTemplate.update(sql);
			}else {
				return jdbcTemplate.execute(new StatementCallback<Object>() {

					@Override
					public Object doInStatement(Statement stmt)
							throws SQLException, DataAccessException {
						return stmt.execute(sql);
					}
				});
			}
		}
		return -1;
	}
}
