package cn.lastmiles.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.AccountRole;
import cn.lastmiles.bean.Agent;
import cn.lastmiles.bean.Bank;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.AccountDao;
import cn.lastmiles.dao.AgentDao;
import cn.lastmiles.dao.PayAccountDao;

@Service
public class AgentService {

	@Autowired
	private AgentDao agentDao;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountRoleService accountRoleService;;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private PayAccountDao payAccountDao;
	
	public Page getAgentList(Long agentId,String name, Integer type,String mobile,
			String contactName,String path,Page page) {
		return agentDao.getAgentList(agentId,name,type,mobile,contactName,path,page);
	}
	@Transactional
	public void editAgent(Long id, String name, Integer type, Long parentId,String contactName,
	 String mobile, Long areaId,String address, Long createdId, Long updatedId) {
		String path="";
		if(id==null){
			id=idService.getId();
			Long accountId=idService.getId();
			if(parentId==null){
				path=id.toString();
			}else{
				path=agentDao.findById(parentId).getPath()+"-"+id.toString();
			}
			Account account = new Account();
			account.setMobile(mobile);
			account.setPassword(mobile);
			account.setAgentId(id);
			account.setType(Constants.Account.ACCOUNT_TYPE_AGENT);
			accountService.save(account);
			
			AccountRole accountRole = new AccountRole();
			accountRole.setAccountId(account.getId());
			accountRole.setRoleId(Constants.Role.ROLE_AGENT_ID);
			accountRoleService.save(accountRole);
			
			PayAccount payaccount=new PayAccount();
			payaccount.setId(idService.getId());
			payaccount.setOwnerId(id);//代理商ID
			payaccount.setType(Constants.Withdraw.WITHDRAW_TYPE_AGENT);//代理商
			payaccount.setStatus(Constants.PayAccountStatus.PAYACCOUNT_NORMAL);//0未激活，1正常，2冻结，3销户，4挂失，5锁定
			payAccountDao.save(payaccount);
			
			agentDao.saveAgent(id,name,type,parentId,contactName,mobile,areaId,address,createdId,accountId, path);
		}else{
			if(parentId==null){
				path=id.toString();
			}else{
				path=agentDao.findById(parentId).getPath()+"-"+id.toString();
			}
			String oldMobile=agentDao.findById(id).getMobile();
			if(!mobile.equals(oldMobile)){//如果手机号码相同则不修改账号和密码
				accountService.updateMobileByMobile(mobile,oldMobile,Constants.Account.ACCOUNT_TYPE_AGENT);
			}
			
			agentDao.updateAgent(id,name,type,parentId,contactName,mobile,areaId,address,updatedId,path);
		}
	}

	public Agent findAgent(Long id, String name, Integer type, Long parentId,
			String contactName, Long areaId, String address) {
		return agentDao.findAgent(id,name,type,parentId,contactName,areaId,address);
		
	}

	public Agent findById(Long id) {
		return agentDao.findById(id);
	}

	public Page getBusinessList(Long businessId,Page page) {
		return agentDao.getBusinessList(businessId,page);
	}

	public List<Bank> findBankList() {
		return agentDao.findBankList();
	}

	public BusinessBank findBusinessBank(Long id,Long businessId,Integer type, String bankName,
			String accountNumber,String accountName, String mobile) {
		
		return agentDao.findBusinessBank(id,businessId,type,bankName,accountNumber,accountName,mobile);
	}

	public void editBusinessBank(Long id, Long businessId, Integer type,String bankName,
			String accountNumber, String accountName, String mobile,
			Integer isDefault,String subbranch,Long bankId) {
		if(id==null){
			id=idService.getId();
			if(isDefault==1){
				agentDao.updateDefault(businessId);
			}
			agentDao.saveBusinessBank(id,businessId,type,bankName,accountNumber,accountName,mobile,isDefault,subbranch,bankId);
		}else{
			if(isDefault==1){
				agentDao.updateDefault(businessId);
			}
			agentDao.updateBusinessBank(id,businessId,bankName,accountNumber,accountName,mobile,isDefault,subbranch,bankId);
		}
		
	}

	public BusinessBank findByBusinessBankId(Long id) {
		return agentDao.findByBusinessBankId(id);
	}

	public void delByBusinessBankId(Long id) {
		agentDao.delByBusinessBankId(id);
		
	}

}
