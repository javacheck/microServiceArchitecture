package cn.self.cloud.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.self.cloud.bean.Account;
import cn.self.cloud.bean.WorkRecord;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.service.AccountService;
import cn.self.cloud.service.OrderServise;
import cn.self.cloud.service.WorkRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WorkRecordService workRecordService;
	
	@Autowired
	private OrderServise orderServise;
	
	
	@RequestMapping(value="api/login",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> index(String mobile,String password){
		Map<String, String> map = new HashMap<String, String>();
		Account account=accountService.login(mobile, password);
		if (account!=null) {
			String token=StringUtils.uuid();
			cacheService.set(token, account);
			WorkRecord workRecord =  new WorkRecord();
			workRecord.setAccountId(account.getId());
			workRecord.setStoreId(account.getStoreId());
			workRecord.setToken(token);
			workRecordService.beginSave(workRecord);
			map.put("code", "0");
			map.put("token", token);
			map.put("message", "登录成功");
		}else {
			map.put("code", "1");
			map.put("message", "账户密码错误");
		}
		return map;
	}
	@RequestMapping(value="api/logout",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, String> logout(String token){
		Map<String, String> map = new HashMap<String, String>();
		Account account = (Account) cacheService.get(token);
		if (account!=null) {
			WorkRecord workRecord =  workRecordService.findByToken(token);
			if (workRecord!=null) {
				workRecord.setEndDate(new Date());
				workRecord.setSales(orderServise.calculatSales(workRecord.getAccountId(), workRecord.getStoreId(), workRecord.getStartDate(), workRecord.getEndDate()));
				workRecordService.endUpdate(workRecord);
				cacheService.delete(token);
				map.put("code", "0");
				map.put("message", "退出成功");
			}else {
				map.put("code", "1");
				map.put("message", "用户已退出");
			}
		}else {
			map.put("code", "2");
			map.put("message", "不存在登录用户");
		}
		return map;
	}
	
}
