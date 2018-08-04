package cn.lastmiles.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Agent;
import cn.lastmiles.bean.Bank;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.Role;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.AgentService;
import cn.lastmiles.service.BusinessBankService;
import cn.lastmiles.utils.SecurityUtils;


@Controller
@RequestMapping("agent")
public class AgentController {
	
	private final static Logger logger = LoggerFactory.getLogger(AgentController.class);
	
	@Autowired
	private AgentService agentService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private BusinessBankService businessBankService;
	@RequestMapping("")
	public String index() {
		return "redirect:/agent/list";
	}

	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("isAdmin", SecurityUtils.isRole(Role.DefaultRole.ADMIN));
		return "agent/list";
	}
	
	/**
	 * 获取代理商列表
	 * @param name
	 * @param type
	 * @param mobile
	 * @param contactName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list/list-data")
	public String listData(Long agentId,String name, Integer type,String mobile,String contactName, Page page, Model model) {
		model.addAttribute("isAdmin", SecurityUtils.isRole(Role.DefaultRole.ADMIN));
		if( null != name){
			name = name.replaceAll("\\s*", "");
		}
		if( null != mobile){
			mobile = mobile.replaceAll("\\s*", "");
		}
		if(null !=contactName){
			contactName = contactName.replaceAll("\\s*", "");
		}
		String path="";
		if(SecurityUtils.isRole(Role.DefaultRole.ADMIN) || SecurityUtils.isAdmin()){
			logger.debug("name={},type={},mobile={},contactName={}",name,type,mobile,contactName);
			model.addAttribute("data", agentService.getAgentList(agentId,name,type,mobile,contactName,path,page));
		}else if(SecurityUtils.isRole(Role.DefaultRole.AGENCY)){
			logger.debug("代理商ID＝＝"+SecurityUtils.getAccount().getAgentId());
			path=SecurityUtils.getAccount().getAgentId().toString();
			model.addAttribute("data", agentService.getAgentList(agentId,name,type,mobile,contactName,path,page));
		}
		
		return "agent/list-data";
	}
	
	@RequestMapping(value = "list/ajax/editAgent")
	@ResponseBody
	public String editAgent(Long id,String name,Integer type,Long parentId,String contactName,
			String mobile,Long areaId, String address) {
		logger.debug("parentId={}",parentId);
		if( null != name){
			name = name.replaceAll("\\s*", "");
		}
		if(null !=contactName){
			contactName = contactName.replaceAll("\\s*", "");
		}
		if(null !=address){
			address = address.replaceAll("\\s*", "");
		}
		if((agentService.findAgent(id,name,type,parentId,contactName,areaId,address)!= null)){
			return "0";//已存在该代理商
		}else{
			agentService.editAgent(id,name,type,parentId,contactName,mobile,areaId,address,SecurityUtils.getAccount().getId(),SecurityUtils.getAccount().getId());
			return "1";//保存成功
		}
	}
	
	@RequestMapping(value="agentList/{id}" )
	public String agentList(@PathVariable Long id,Model model) {
		model.addAttribute("agentId", id);
		return "agent/agentList";
	}
	@RequestMapping(value="agentList" )
	public String agentList() {
		return "agent/agentList";
	}
	@RequestMapping(value="agentList/agentList-data" )
	public String agentListData(Long agentId,String name, Integer type,String mobile,String contactName, Page page, Model model) {
		String path="";
		model.addAttribute("data", agentService.getAgentList(agentId,name,type,mobile,contactName,path,page));
		return "agent/agentList-data";
	}
	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd() {
		return "/agent/add";
	}
	
	/**
	 * 跳转到修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model) {
		Agent agent= agentService.findById(id);
		model.addAttribute("agent", agent);
		return "/agent/add";
	}
	
	@RequestMapping(value="businessList/{id}" )
	public String businessList(@PathVariable Long id,Model model) {
		model.addAttribute("businessId", id);
		return "agent/businessList";
	}
	@RequestMapping(value="businessList/business/list-data/{businessId}" )
	public String accountListtData(@PathVariable Long businessId,Page page, Model model) {
		model.addAttribute("data", agentService.getBusinessList(businessId,page));
		return "agent/businessList-data";
	}
	
	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "business/add/{businessId}", method = RequestMethod.GET)
	public String toBusinessAdd(@PathVariable Long businessId,Model model) {
		List<Bank> bankList = agentService.findBankList();
		model.addAttribute("bankList",bankList);
		model.addAttribute("businessId", businessId);
		return "/agent/businessAdd";
	}
	
	/**
	 * 跳转到修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "business/update/{id}", method = RequestMethod.GET)
	public String toBusinessUpdate(@PathVariable Long id,Model model) {
		List<Bank> bankList = agentService.findBankList();
		BusinessBank businessBank=agentService.findByBusinessBankId(id);
		model.addAttribute("bankList",bankList);
		model.addAttribute("businessBank", businessBank);
		return "/agent/businessAdd";
	}
	
	@RequestMapping(value = "business/delete/delete-by-id", method = RequestMethod.POST)
	@ResponseBody
	public String toBusinessDel(Long id,Model model) {
		logger.debug("id=="+id);
		agentService.delByBusinessBankId(id);
		if(agentService.findByBusinessBankId(id)==null){
			return "1";
		}else{
			return "0";
		}
	}
	@RequestMapping(value = "businessList/ajax/editBusinessBank")
	@ResponseBody
	public String editBusinessBank(Long id,Long businessId,String bankName,String accountNumber,String accountName,
			String mobile,Integer isDefault,String subbranch,Long bankId) {
		int type=Constants.BusinessBank.AGENT_TYPE;//代理商
		if(SecurityUtils.isStore()){//商家
			type=Constants.BusinessBank.STORE_TYPE;
		}
		logger.debug("{},{},{},{},{},{},{}",businessId,bankName,accountNumber,accountName,mobile,isDefault,subbranch,bankId);
		if((agentService.findBusinessBank(id,businessId,type,bankName,accountNumber,accountName,mobile)!= null )){
			return "0";//已存在该银行卡
		}else{
			agentService.editBusinessBank(id,businessId,type,bankName,accountNumber,accountName,mobile,isDefault,subbranch,bankId);
			return "2";//保存成功
		}
	}
	@RequestMapping(value = "checkBank")
	@ResponseBody
	public String checkBank(Long id,Long businessId,String accountNumber, Model model) {
		
		BusinessBank businessBank = businessBankService.getBusinessBank(id,businessId, Constants.BusinessBank.AGENT_TYPE, accountNumber);
		
		if ( null != businessBank) {
			return "1";
		} else {
			return "0";
		}
	}
	
	/**
	 * 2015.12.23 修改为只在代理商范围内手机号码不能重复，既：一个手机号码即可属于商家也可属于代理商，但不能属于两个代理商
	 * @param mobile 新增或者修改的手机号码
	 */
	@RequestMapping(value = "checkmobile")
	@ResponseBody
	public int checkmobile(String mobile) {
		return accountService.checkMobile(mobile,Constants.Account.ACCOUNT_TYPE_AGENT) ? 1 : 0 ;
	}
}
