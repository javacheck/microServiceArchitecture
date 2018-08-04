package cn.lastmiles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Discount;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.service.DiscountService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("discount")
public class DiscountController {
	private final static Logger logger = LoggerFactory
			.getLogger(DiscountController.class);
	@Autowired
	private DiscountService discountService;
	@Autowired
	private StoreService storeService;

	@RequestMapping(value = "set", method = RequestMethod.GET)
	public String set(Model model) {
		model.addAttribute("discount",discountService.find(SecurityUtils.getAccountStoreId()));
		model.addAttribute("discounts",JsonUtils.objectToJson(discountService.findAll()));
		model.addAttribute("stores",storeService.findMyStore(SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId()));
		return "discount/set";
	}

	@RequestMapping(value = "set", method = RequestMethod.POST)
	@ResponseBody
	public String set(Discount discount) {
		logger.debug("set discount is "+discount);
		discountService.set(discount);
		return "1";
	}
}
