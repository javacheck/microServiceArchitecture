package cn.self.cloud.controller.api;

import java.util.HashMap;
import java.util.Map;

import cn.self.cloud.bean.Account;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/productCategory")
public class ApiProductCategoryController {
	
	@Autowired
	private CacheService cacheService;
	
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="list",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> list(String token,Long parentId){
		Map<String, Object> list = new HashMap<String, Object>();
		Account account=null;
		try {
			account = (Account) cacheService.get(token);
		} catch (Exception e) {
			list.put("code", "1");
			list.put("message", "用户未登录");
			return list;
		}
		parentId=parentId!=null&&!parentId.equals("")?parentId:null;
		list.put("code", "0");
		list.put("message", "成功获取");
		list.put("data", productCategoryService.findByParentIdAndStoreId(parentId,account.getStoreId()));
		return list;
	}

}
