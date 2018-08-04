package cn.lastmiles.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lastmiles.bean.PointRule;
import cn.lastmiles.bean.Store;
import cn.lastmiles.service.PointRuleService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;


@Controller
@RequestMapping("pointRule")
public class PointRuleController {
	private final static Logger logger = LoggerFactory.getLogger(PointRuleController.class);
	
	
	@Autowired
	private PointRuleService pointRuleService;
	@Autowired
	private StoreService storeService;
	
	
	@RequestMapping("")
	public String index() {
		return "redirect:/pointRule/list";
	}
	
	@RequestMapping(value = "list")
	public String list(Model model) {
		Long storeId=null;
		boolean flag = false;
		Store shop = storeService.findById(SecurityUtils.getAccountStoreId());
		if( null == shop.getOrganizationId() ){ // 没有组织架构(没有总部)
			flag = true; // 可以新增修改
		}
		boolean isMainFlag=SecurityUtils.isMainStore() || flag;
		model.addAttribute("isMainStore", SecurityUtils.isMainStore() || flag );
		boolean isNotChainStore=!SecurityUtils.isChainStore();
		model.addAttribute("isNotChainStore", isNotChainStore );
		if(isMainFlag){
			storeId=SecurityUtils.getAccountStoreId();
		}else{
			Store s=storeService.findTopStore(SecurityUtils.getAccountStoreId());
			storeId=s.getId();
			model.addAttribute("unifiedPointRule",storeService.findById(SecurityUtils.getAccountStoreId()).getUnifiedPointRule());
		}
		model.addAttribute("pointRule",pointRuleService.getPointRuleByStoreId(storeId));
		return "pointRule/list";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(PointRule pointRule, Model model) throws ParseException {
		pointRule.setStoreId(SecurityUtils.getAccountStoreId());
		logger.debug("pointRule={}",pointRule);
		pointRuleService.editPointRule(pointRule);
		return "redirect:/pointRule/list";
	}
}
