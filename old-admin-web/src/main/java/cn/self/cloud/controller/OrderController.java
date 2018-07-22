package cn.self.cloud.controller;

import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.OrderServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("order")
public class OrderController {
	
	@Autowired
	private OrderServise orderServise;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/order/list";
	}

	@RequestMapping("list")
	public String list(Page page, Model model) {
		return "order/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String beginTime,String endTime,String mobile,String orderId, Page page, Model model) {
		model.addAttribute("data", orderServise.list(beginTime, endTime, mobile, orderId, page));
		return "order/list-data";
	}
	@RequestMapping(value="info/showMode/{id}" )
	public String info(@PathVariable Long id, Model model){
		model.addAttribute("order",orderServise.findById(id));
		return "order/info";
	}

}
