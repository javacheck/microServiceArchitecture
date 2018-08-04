package cn.lastmiles.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lastmiles.bean.AllocationConfig;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.service.AllocationConfigService;

@Controller
@RequestMapping("allocationConfig")
public class AllocationConfigController {
	private final static Logger logger = LoggerFactory.getLogger(AllocationConfigController.class);
	
	@Autowired
	private AllocationConfigService allocationConfigService;
	
	
	@RequestMapping("")
	public String index() {
		return "redirect:/allocationConfig/list";
	}
	
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("allocationConfig",allocationConfigService.getAllocationConfigByStoreId(SecurityUtils.getAccountStoreId()));
		return "allocationConfig/list";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(AllocationConfig allocationConfig, Model model) throws ParseException {
		logger.debug("allocationConfig={}",allocationConfig);
		if(allocationConfig.getStoreId()==null){
			allocationConfig.setStoreId(SecurityUtils.getAccountStoreId());
			allocationConfigService.saveAllocationConfig(allocationConfig);
		}else{
			allocationConfigService.updateAllocationConfig(allocationConfig);
		}
		return "redirect:/allocationConfig/list";
	}
}
