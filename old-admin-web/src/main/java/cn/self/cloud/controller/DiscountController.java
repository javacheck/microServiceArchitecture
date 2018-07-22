package cn.self.cloud.controller;

import cn.self.cloud.bean.Discount;
import cn.self.cloud.service.DiscountService;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("discount")
public class DiscountController {
	@Autowired
	private DiscountService discountService;

	@RequestMapping(value = "set", method = RequestMethod.GET)
	public String set(Model model) {
		model.addAttribute("discount",
				discountService.find(SecurityUtils.getAccountStoreId()));
		return "discount/set";
	}

	@RequestMapping(value = "set", method = RequestMethod.POST)
	@ResponseBody
	public String set(Discount discount) {
		discount.setStoreId(SecurityUtils.getAccountStoreId());
		discountService.set(discount);
		return "1";
	}
}
