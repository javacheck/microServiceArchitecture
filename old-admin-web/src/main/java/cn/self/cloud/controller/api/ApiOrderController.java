package cn.self.cloud.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.self.cloud.bean.*;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.commonutils.json.JsonUtils;
import cn.self.cloud.service.OrderServise;
import cn.self.cloud.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/order")
public class ApiOrderController {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private OrderServise orderServise;
	@Autowired
	private ProductStockService productStockService;

	@RequestMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> add(String token, String json) {
		Map<String, Object> map= new HashMap<String, Object>();
		Account account = (Account) cacheService.get(token);
		Order order = JsonUtils.jsonToBean(json, Order.class);
		if (order==null) {
			map.put("code", "2");
			map.put("message", "参数错误");
			return map;
		}
		order.setCreatedTime(new Date());
		order.setStatus(1);
		order.setAccountId(account.getId());
		order.setStoreId(account.getStoreId());
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem==null) {
				map.put("code", "2");
				map.put("message", "参数错误");
				return map;
			}
			ProductStock productStock =productStockService.findById(orderItem.getStockId());//找到库存
			if (productStock==null) {
				map.put("code", "3");
				map.put("message", "库存ID未找到");
				return map;
			}
			orderItem.setPrice(productStock.getPrice());
		}
		Long id =orderServise.saveOrderAndOrderItem(order);
		map.put("code", "0");
		map.put("message", "插入成功");
		map.put("orderId", id);
		return  map;
	}
	@RequestMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public APIResponse update(String token, Long orderId, Integer status, Integer paymentMode) {
		if (orderId==null||status==null||paymentMode==null) {
			return new APIResponse(1,"解析JSON错误");
		}
		orderServise.update(orderId,status,paymentMode);
		return new APIResponse(0,"修改成功");
	}
	
}
