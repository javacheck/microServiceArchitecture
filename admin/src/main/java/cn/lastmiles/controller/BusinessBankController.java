package cn.lastmiles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Bank;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AgentService;
import cn.lastmiles.service.BusinessBankService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("businessbank")
public class BusinessBankController {

	@Autowired
	private BusinessBankService businessBankService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private IdService idService;
	
	@RequestMapping(value = "list")
	public String BusinessBank(Model model) {
		String isMainStore = null;
		if(SecurityUtils.isMainStore()){
			isMainStore = "isMainStore";
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			model.addAttribute("storeList",storeList);
		}
		model.addAttribute("isMainStore",isMainStore);
		return "businessbank/list";
	}

	@RequestMapping(value = "list-data")
	public String BusinessBankData(String bankName,Long main_storeId,Page page, Model model) {
		Long businessId = null;
		Integer type = null;
		String storeIdArray = null;
		String isMainStore = null;
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.BusinessBank.STORE_TYPE;
			if( SecurityUtils.isMainStore() ){
				isMainStore = "isMainStore";
				if( null != main_storeId && ObjectUtils.equals(main_storeId,-1l) ){
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					StringBuffer sb = new StringBuffer();
					int index = 0;
					for (Store store : storeList) {
						if(index != 0 ){
							sb.append(",");
						}
						sb.append(store.getId());
						index ++;
					}
					storeIdArray = sb.toString();
				} else {
					storeIdArray = main_storeId.toString();
				}
				
			} else {
				businessId = SecurityUtils.getAccountStoreId();				
			}
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.BusinessBank.AGENT_TYPE;
			businessId = SecurityUtils.getAccountAgentId();
		}
		model.addAttribute("isMainStore",isMainStore);
		model.addAttribute("data", businessBankService.getBusinessBank(businessId,storeIdArray,type,bankName, page));
		return "businessbank/list-data";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		Long businessId = null;
		if( SecurityUtils.isStore() ){
			businessId = SecurityUtils.getAccountStoreId();
		} else if(SecurityUtils.isAgent()){
			businessId = SecurityUtils.getAccountAgentId();
		}
		List<Bank> bankList = agentService.findBankList();
		model.addAttribute("bankList", bankList);
		model.addAttribute("businessId", businessId);
		return "/businessbank/add";
	}
	
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		List<Bank> bankList = agentService.findBankList();
		Long businessId = null;
		if (SecurityUtils.isAgent()){
			businessId = SecurityUtils.getAccountAgentId();
		}else if (SecurityUtils.isStore()){
			businessId = SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("businessBank", businessBankService.getById(id,businessId));
		model.addAttribute("bankList", bankList);
		return "/businessbank/add";
	}
	
	@RequestMapping(value = "delete/delete-by-id", method = RequestMethod.POST)
	@ResponseBody
	public String toBusinessDel(Long id, Model model) {
		agentService.delByBusinessBankId(id);
		if (agentService.findByBusinessBankId(id) == null) {
			return "1";
		} else {
			return "0";
		}
	}
	
	@RequestMapping(value = "saveBusinessBank", method = RequestMethod.POST)
	public String add(BusinessBank businessBank, Model model) {
		Integer type = null;
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.BusinessBank.STORE_TYPE;
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.BusinessBank.AGENT_TYPE;
		}
		if (null != businessBank.getId()) {
			
			businessBank.setType(type);
			if( businessBank.getIsDefault().equals(1)){
				businessBankService.updateIsDefault(businessBank);
			}
			String info = businessBank.getId()
					+ (businessBankService.update(businessBank) ? "修改成功"
							: "修改失败");
		} else {
			businessBank.setId(idService.getId());
			businessBank.setType(type);
			if( businessBank.getIsDefault().equals(1)){
				businessBankService.updateIsDefault(businessBank);
			}
			String info = businessBankService.save(businessBank) ? "添加成功"
					: "添加失败";
		}
		return "redirect:/businessbank/list";
	}
}