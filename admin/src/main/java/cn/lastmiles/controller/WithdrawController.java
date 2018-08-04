package cn.lastmiles.controller;
/**
 * updateDate : 2015-07-16 PM 16:53
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.APIResponse;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.bean.Withdraw;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.BusinessBankService;
import cn.lastmiles.service.PayAccountService;
import cn.lastmiles.service.WithdrawService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("withdraw")
/**
 * 提现/提现流水记录 
 */
public class WithdrawController {

	@Autowired
	private WithdrawService withdrawService; // 提现
	@Autowired
	private PayAccountService payAccountService; // 支付
	@Autowired
	private BusinessBankService businessBankService; // 银行卡
	@Autowired
	private IdService idService; // ID自动生成
	private final static Logger logger = LoggerFactory.getLogger(WithdrawController.class);

	/**
	 * 提现列表
	 * @param model
	 */
	@RequestMapping("withdrawData/list")
	public String withdraw(Model model) {
		Long ownerId = null;
		Integer type = null;

		if (SecurityUtils.isStore()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();

		} else if (SecurityUtils.isAgent()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		} 

		PayAccount payAccount = payAccountService.queryHaveData(ownerId, type, null);

		List<BusinessBank> businessBankList = businessBankService.getConnectBusinessBank(ownerId, type);

		model.addAttribute("payAccount", payAccount);
		model.addAttribute("businessBankList", businessBankList);
		
		model.addAttribute("accountNumber", 
				null == businessBankList ? "" : businessBankList.get(0).getAccountNumber());
		model.addAttribute("accountName",
				null == businessBankList ? "" : businessBankList.get(0).getAccountName());

		return "withdraw/withdraw-data";
	}

	/**
	 * 保存提现信息
	 * @param withdraw 提现对象
	 * @return 跳转到提现流水记录页面
	 */
	@RequestMapping("saveWithdraw")
	public String saveWithdraw(Withdraw withdraw, double amount,String payPassword, Model model) {
		Long ownerId = null;
		Integer type = null;

		if (SecurityUtils.isStore()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();

		} else if (SecurityUtils.isAgent()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}

		PayAccount payAccount = payAccountService.queryHaveData( ownerId, type, null);
		
		if( StringUtils.isBlank(payAccount.getPassword()) ){ // 商户支付密码未设
			logger.debug("{}账户,请先设置支付密码",ownerId);
			return "redirect:/withdraw/expenditure/list";
		}
		
		if(amount < 200){
			logger.debug("{}账户,提现金额为{},单次提现金额小于200元，不予提现",ownerId,amount);
			return "redirect:/withdraw/expenditure/list";
		}
		
		if (payAccount.getBalance().doubleValue() < amount) {
			logger.debug("{}账户,提现金额为{},余额不足，不予提现",ownerId,amount);
			return "redirect:/withdraw/expenditure/list";
		}
		
		if (StringUtils.isNotBlank(payAccount.getPassword())
				&& !PasswordUtils.checkPassword(payPassword, payAccount.getPassword())) {
			logger.debug("{}账户,支付密码不对，不予提现",ownerId);
			return "redirect:/withdraw/expenditure/list";
		}
		
		BusinessBank businessBank = businessBankService.getById(withdraw.getId(), ownerId);

		withdraw.setId(idService.getId());
		withdraw.setType(type);
		withdraw.setOwnerId(ownerId);
		withdraw.setAccountId(SecurityUtils.getAccountId()); // 将账号设置为当前登录用户ID
		withdraw.setBankName(businessBank.getBankName());
		withdraw.setBankAccountNumber(businessBank.getAccountNumber());
		withdraw.setBankAccountName(businessBank.getAccountName());
		withdraw.setStatus(Constants.WithdrawStatus.WITHDRAW_PROGRESS); // 默认设置为处理中

		Double balance = NumberUtils.subtract(payAccount.getBalance(), amount);
		
		withdrawService.save(withdraw,balance,type);

		return "redirect:/withdraw/expenditure/list";
	}

	/**
	 * 根据ID获取提现银行卡信息
	 * @param id 银行卡ID
	 */
	@RequestMapping(value = "list/ajax/getBankInformation", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BusinessBank getBankInformation(Long id) {
		Long ownerId = null;

		if (SecurityUtils.isStore()) {
			ownerId = SecurityUtils.getAccountStoreId();
		} else if (SecurityUtils.isAgent()) {
			ownerId = SecurityUtils.getAccountAgentId();
		}
		BusinessBank businessBank = businessBankService.getById(id, ownerId);
		return businessBank;
	}

	/**
	 * 提现流水记录列表
	 * @param model
	 * @return 根据登录的账号跳转到不同的处理页面
	 */
	@RequestMapping("expenditure/list")
	public String expenditure(Model model) {

		if (SecurityUtils.isStore()) {
			return "withdraw/storeList";

		} else if (SecurityUtils.isAgent()) {
			return "withdraw/agentList";

		}
		return "withdraw/adminList";
	}

	/**
	 * 提现流水详情搜索查询
	 * @param Id 提现流水记录ID
	 * @param name 商家名称或者代理商名称
	 * @param amount 金额
	 * @param status 状态 0 处理中 1 成功 2 失败
	 * @param type 类型
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @param model
	 */
	@RequestMapping("expenditure/list-data")
	public String expenditureData(Long Id, String name, Double amount,Integer status, Integer type,
								  String startTime, String endTime,Page page, Model model) {
		Long ownerId = null;
		Long accountId = null;
		
		if ( null != status && status.intValue() == Constants.Type.ALl ) {
			status = null;
		}

		if (SecurityUtils.isStore()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
			page = withdrawService.getWithdraw(accountId, ownerId, type, Id,name, amount, status, startTime, endTime, page);
			model.addAttribute("data", page);
			return "withdraw/expenditureList-data";

		} else if (SecurityUtils.isAgent()) {
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();

			page = withdrawService.getWithdraw(accountId, ownerId, type, Id, name, amount, status, startTime, endTime, page);
			model.addAttribute("data", page);
			return "withdraw/expenditureList-data";

		} else {
			// 管理员 --
			if ( null != type && type.intValue() == Constants.Type.ALl ) { // 查询所有类型
				page = withdrawService.getWithdraw(accountId, ownerId, Id, name, amount, status, startTime, endTime, page);
			} else { // 根据类型条件查询
				page = withdrawService.getWithdraw(accountId, ownerId, type, Id, name, amount, status, startTime, endTime, page);
			}
			model.addAttribute("data", page);
			return "withdraw/adminList-data";
		}
	}
	
	/**
	 * 提现审核列表
	 * @return
	 */
	@RequestMapping("audit/list")
	public String audit() {
		return "withdraw/audit_list";
	}
	
	/**
	 * 提现审核详情列表
	 * @return
	 */
	@RequestMapping("audit/list-data")
	public String auditListData(Long Id,String name,Integer type,Double amount,String startTime ,String endTime,Page page, Model model) {
		
		// 管理员 --
		if ( null != type && type.intValue() == Constants.Type.ALl ) { // 查询所有类型
			page = withdrawService.getWithdraw(Id, name, amount, startTime, endTime, page);
		} else { // 根据类型条件查询
			page = withdrawService.getWithdraw(type, Id, name, amount,startTime, endTime, page);
		}
		model.addAttribute("data", page);
		return "withdraw/audit_list-data";
	}
	
	/**
	 * 提现审核异步修改状态
	 * @return
	 */
	@RequestMapping(value = "ajax/modifyStatus")
	@ResponseBody
	public int modifyStatus(Long id,Integer status,Long ownerId,Integer type,Model model) {
		Double balance = (double) 0; 
		if(null != status && status.intValue() == Constants.WithdrawStatus.WITHDRAW_AUDIT_FAILURE ){ // 审核失败,将取现金额退回
			// 不能相信页面传过来的提现金额,自己根据id,从数据库中取
			Double amount = withdrawService.getBalance(id,type,ownerId); 
			PayAccount payAccount= payAccountService.getByOwnerIdAndType(ownerId, type);
			// 取不到支付对象,则无法返回提现金额，故返回审核操作失败信号
			if(null == payAccount){ 
				return 0;
			} else {
				Double getBalance = payAccount.getBalance();				
				balance = NumberUtils.add(getBalance, amount);
			}
		}
		return  withdrawService.updateStatus(type,status,id,ownerId,balance) ? 1 : 0;
	}
}