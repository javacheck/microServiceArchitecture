package cn.lastmiles.controller;
/**
 * createDate : 2015-07-10 PM 17:43
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.lastmiles.bean.CacheKeys;
import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.PayAccountService;
import cn.lastmiles.service.sms.SMSParameter;
import cn.lastmiles.service.sms.SMSService;
import cn.lastmiles.service.sms.SMSTemplate;
import cn.lastmiles.utils.SecurityUtils;

/**
 * 支付管理
 */
@Controller
@RequestMapping("payaccount")
public class PayAccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(PayAccountController.class);
	
	@Autowired
	private PayAccountService payAccountService; // 支付
	@Autowired
	private IdService idService; // ID自动生成
	@Autowired
	private CacheService cacheService; // 缓存
	@Autowired
	private SMSService sMSService; // 手机接口
	
	/**
	 * 点击支付密码管理时,先判断是否有账户信息,如有则进入修改支付密码界面,反之进入设置支付密码界面
	 * @return 跳转的界面
	 */
	@RequestMapping(value = "list")
	public String payaccountInit() {
		Long ownerId = null; // 商户id，或者代理商id，用户id
		Integer type = null; // 0商户，1代理商,2用户
		
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}
		
		PayAccount payAccount = payAccountService.queryHaveData(ownerId, type, null);
		if( null == payAccount ){ // 没有记录 , 设置支付密码
			return "payAccount/set";
		} else if( null == payAccount.getPassword() || "".equals(payAccount.getPassword())){ // 没有设置密码，则设置支付密码
			return "payAccount/set";
		}
		return "payAccount/update"; // 有记录就是修改支付密码
	}
	
	/**
	 * 账户余额
	 * @return
	 */
	@RequestMapping(value = "list-p")
	public String list() {
		return "payAccount/list"; 
	}
	
	/**
	 * 支付搜索详情
	 * @param mobile
	 * @param status
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list-data")
	public String listData(String mobile,Integer status,Integer type, Page page, Model model) {
		type=type.intValue()== -2?null:type;
		status=status.intValue()== -1?null:status;
		model.addAttribute("data", payAccountService.list(page, type, mobile, status));
		return "payAccount/list-data"; 
	}

	/**
	 * 初始设置支付密码,默认是保存支付账号信息
	 * @param payaccount 支付账号对象
	 * @return 进入修改支付密码界面
	 */
	@RequestMapping(value = "setPassword", method = RequestMethod.POST)
	public String setPassword(PayAccount payaccount) {
		Long ownerId = null;
		Integer type = null;
		
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}
		
		String passWord = PasswordUtils.encryptPassword(payaccount.getPassword());
		PayAccount payAccount = payAccountService.queryHaveData(ownerId, type, null);
		if( null == payAccount ){
			payaccount.setId(idService.getId());
			payaccount.setOwnerId(ownerId);
			payaccount.setType(type);
			payaccount.setPassword(passWord);
			payaccount.setStatus(Constants.PayAccountStatus.PAYACCOUNT_INACTIVE); // 初始设置支付账户信息为 : 0未激活，1正常，2冻结，3销户，4挂失，5锁定
			payaccount.setBalance(0.0); // 初始设置余额为0
			payaccount.setFrozenAmount(0.0); // 初始设置冻结金额为0
			payAccountService.save(payaccount);
		} else {
			payAccountService.setPassword(passWord,ownerId,type);			
		}
		
		return "payAccount/update";
	}
	
	/**
	 * 检测密码是否正确
	 * @param payPassWord
	 * @return
	 */
	@RequestMapping(value = "checkPasswordIsSure")
	@ResponseBody
	public int checkPasswordIsSure(String payPassWord) {
		if ( PasswordUtils.checkPassword(payPassWord, SecurityUtils.getAccount().getPassword()) ) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 检测原密码
	 * @param originalPassword
	 * @return
	 */
	@RequestMapping(value = "checkOriginalPassword")
	@ResponseBody
	public int checkOriginalPassword(String originalPassword) {
		Long ownerId = null;
		Integer type = null;
		
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}
		
		PayAccount payaccount = payAccountService.getByOwnerIdAndType(ownerId,type);
		if( null == payaccount ){
			return 0;
		}
		if ( PasswordUtils.checkPassword(originalPassword, payaccount.getPassword()) ) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 检测输入的密码是否是此支付账号的密码(输错5次单日会锁定)
	 * @param originalPassword 原密码
	 * @return 0 表示 账号信息查询不到或者密码不对 , 1表示 密码与数据库中的一致
	 */
	@RequestMapping(value = "checkPassword")
	@ResponseBody
	public String checkPassword(String originalPassword) {
		Long ownerId = null;
		Integer type = null;
		
		String returnString = "0";
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}
		
		if( null != cacheService.get(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId) 
				&& "0".equals(""+cacheService.get(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId)) ){
			return returnString + "_0";
		} 
		
		PayAccount payaccount = payAccountService.getByOwnerIdAndType(ownerId,type);
		
		if( null == payaccount ){
			returnString = "0";
		}
		if ( PasswordUtils.checkPassword(originalPassword, payaccount.getPassword()) ) {
			returnString = "1";
		} else {
			returnString = "0";
		}
		if("0".equals(returnString)){
			int countTime = 5;
		
			if( null == cacheService.get(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId) ){
				cacheService.set(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId, countTime);
			} else {
				countTime = (int) cacheService.get(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId);
				if(countTime >=1 ){
					countTime --;
				}
				cacheService.set(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId, countTime,60 * 60 * 24L); // 锁定一天
			}
			returnString += "_" + countTime;
		}
		logger.debug("ownerId is {}, returnString is {}",ownerId,returnString);
		return returnString;
	}
	
	/**
	 * 修改支付账号的支付密码
	 * @param payaccount 支付账号对象
	 * @return 进入修改支付密码界面
	 */
	@RequestMapping(value = "updatePassword", method = RequestMethod.POST)
	public String updatePassword(PayAccount payaccount) {
		Long ownerId = null;
		Integer type = null;
		
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
		}
		
		String passWord = PasswordUtils.encryptPassword(payaccount.getPassword());
		payAccountService.setPassword(passWord,ownerId,type);
		
		return "payAccount/update";
	}
	
	/**
	 * 发送支付安全验证的验证码
	 */
	@RequestMapping(value = "ajax/sendPayPassWordCode", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int sendPayPassWordCode(){
	
		String code = StringUtils.getRandomNumeric(4);

		String mobile = SecurityUtils.getAccount().getMobile();
		
		// 发送短信
		sMSService.send(SMSTemplate.UPDATEPAYPASSWORD,SMSParameter.getInstance("code", code),mobile);
		cacheService.set(CacheKeys.PAYPASSWORDFORGETRECOVEREDKEY + mobile, code, 60 * 2L);// 把生成的４位数字放进缓存，有效期2分钟

		logger.debug("找回支付密码的验证码为 {}", code);
		return 1;
	}
	
	/**
	 * 验证修改绑定手机验证码
	 * @return
	 */
	@RequestMapping(value = "ajax/verifyPayPassWordCode", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int verifyPayPassWordCode(String code) {

		String mobile = SecurityUtils.getAccount().getMobile();

		String cacheCode = (String) cacheService.get(CacheKeys.PAYPASSWORDFORGETRECOVEREDKEY + mobile);
		logger.debug("code is {},cacheCode is {}",code,cacheCode);
		// 验证码正确
		if (StringUtils.equals(code, cacheCode)) {
			return 1;
		}

		return 0;
	}
	
	/**
	 * 修改支付密码(修改成功则之前锁定过的会重新放开)
	 * @param code
	 * @param forgetPayPassword
	 * @return
	 */
	@RequestMapping(value = "ajax/sureUpdatePayPasswordCode", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int sureUpdatePayPasswordCode(String code,String forgetPayPassword) {

		String mobile = SecurityUtils.getAccount().getMobile();

		String cacheCode = (String) cacheService.get(CacheKeys.PAYPASSWORDFORGETRECOVEREDKEY + mobile);
		logger.debug("code is {},cacheCode is {}",code,cacheCode);
		// 验证码正确
		if (StringUtils.equals(code, cacheCode)) {
			if(!PasswordUtils.checkPassword(forgetPayPassword, SecurityUtils.getAccount().getPassword())){
				Long ownerId = null;
				Integer type = null;
				
				if( SecurityUtils.isStore() ){
					type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
					ownerId = SecurityUtils.getAccountStoreId();
					
				} else if(SecurityUtils.isAgent()){
					type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
					ownerId = SecurityUtils.getAccountAgentId();
				}
				
				String passWord = PasswordUtils.encryptPassword(forgetPayPassword);
				payAccountService.setPassword(passWord,ownerId,type);
				// 修改成功后查看支付密码是否被锁定，锁定则解除锁定
				if(null != cacheService.get(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId)){
					cacheService.delete(CacheKeys.PAYPASSWORDONEDAYLOCKKEY+ownerId);
				}
			} else {
				return 2; // 支付密码和登录密码一致
			}
		} else {
			return 1; // 验证码错误或验证码已失效
		}
		return 0;
	}
}