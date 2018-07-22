package cn.self.cloud.controller.api;

import cn.self.cloud.bean.APIResponse;
import cn.self.cloud.bean.Account;
import cn.self.cloud.bean.User;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/user")
public class ApiUserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CacheService cacheService;
	
	@RequestMapping(value="add",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public APIResponse add(String token, String mobile, String name){
		APIResponse apiResponse = new APIResponse();
		Account account=null;
		try {
			account = (Account) cacheService.get(token);
			if (account==null) {
				apiResponse.set(1, "用户未登录");
				return apiResponse;
			}
		} catch (Exception e) {
			apiResponse.set(1, "用户未登录");
			return apiResponse;
		}
		if (StringUtils.isBlank(mobile)||StringUtils.isBlank(name)) {
			apiResponse.set(2, "参数错误");
			return apiResponse;
		}
		if (!StringUtils.isMobile(mobile)) {
			apiResponse.set(3, "手机号码格式不正确");
			return apiResponse;
		}
		if (userService.findByMobileAndStoreId(mobile, account.getStoreId())!=null) {
			apiResponse.set(4, "手机号码在该店也注册");
			return apiResponse;
		}
		User user = new User();
		user.setCreatedId(account.getId());
		user.setMobile(mobile);
		user.setName(name);
		user.setDiscount(null);
		user.setStoreId(account.getStoreId());
		if(!userService.save(user)){
			apiResponse.set(5, "连接数据库错误 联系管理员");
			return apiResponse;
		}else {
			apiResponse.set(0, "会员添加成功");
			return apiResponse;
		}
	}

}
