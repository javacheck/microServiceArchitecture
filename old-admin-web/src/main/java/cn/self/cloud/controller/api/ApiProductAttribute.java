package cn.self.cloud.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.ProductStock;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.cache.CacheService;
import cn.self.cloud.service.ProductAttributeService;
import cn.self.cloud.service.ProductStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/productAttribute")
public class ApiProductAttribute {
	private final static Logger logger = LoggerFactory
			.getLogger(ApiProductAttribute.class);

	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductStockService productStockService;

	@RequestMapping(value = "info/{id}")
	public String info(@PathVariable Long id, String token, Model model) {
		ProductStock productStock = productStockService.findByStockId(id);
		model.addAttribute("productAttributes", productAttributeService.productAttributeList(productStock.getProductId()));
		model.addAttribute("productStock", productStock);
		String [] s= productStock.getAttributeCode().split("-");//处理得到参数
		List<Long> list = new ArrayList<Long>();
		for (String string : s) {//类型转换
			list.add(Long.parseLong(string));
		}
		String attributeCodeCountent=productStockService.findByFuzzy(list, productStock.getProductId(), productStock.getStoreId());
		model.addAttribute("attributeCodeCountent",attributeCodeCountent);
		return "api/productAttribute";
	}

	@RequestMapping(value = "ajax/isonCheck" ,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> check(String papm,Long productId,Long storeId) {
		String [] attributeCodes=papm.split("-");//处理得到参数
		List<Long> list = new ArrayList<Long>();
		for (String attributeCode : attributeCodes) {//类型转换
			list.add(Long.parseLong(attributeCode));
		}
		Collections.sort(list, new Comparator<Long>() {//排序
			@Override
			public int compare(Long b1, Long b2) {
				return (int) (b1 - b2);
			}
		});
		StringBuffer sf = new StringBuffer();
		for (Long attributeCode : list) {//按照顺序重新拼接
			sf.append(attributeCode+"-");
		}
		if (StringUtils.isNotBlank(sf.toString())) {
			sf.deleteCharAt(sf.length()-1);
		}
		logger.debug("attributeCode is :"+sf.toString());
		ProductStock productStock =productStockService.findProductStock(sf.toString());//按照attributeCode 看是否可查到库存
		String attributeCodeCountent=productStockService.findByFuzzy(list, productId, storeId);////查找可选的节点
		Map<String, Object> returnMap = new HashMap<String, Object>();//返回处理
		returnMap.put("productStock", productStock);
		returnMap.put("attributeCodeCountent", attributeCodeCountent);
		return returnMap;
	}
}
