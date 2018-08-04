package cn.lastmiles.service;
/**
 * updateDate : 2015-07-16 PM 17:47
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Withdraw;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PayAccountDao;
import cn.lastmiles.dao.WithdrawDao;
import cn.lastmiles.service.sms.SMSService;

@Service
public class WithdrawService {
	@Autowired
	private PayAccountDao payAccountDao;
	@Autowired
	private WithdrawDao withdrawDao;
	@Autowired
	private SMSService smsService;

	/**
	 * 保存提现信息
	 * @param withdraw 提现对象
	 * @param type 0商户，1代理商,2用户;
	 * @return 是否保存成功
	 */
	@Transactional
	public boolean save(Withdraw withdraw,Double balance,int type) {
		payAccountDao.updateBalance(balance,type,withdraw.getOwnerId());
		smsService.send("(提现审核通知)提现申请#"+withdraw.getId()+"#需要您审核，请及时登录管理后台查看处理。", null, "13822201156","13533206152");
		return withdrawDao.save(withdraw);
	}

	/**
	 * 查询提现流水记录(【管理员/代理商/商家 】查询)
	 * @param accountId 代理商或者商家的accountId，如果是用户null值
	 * @param ownerId 商户id，或者代理商id，用户id
	 * @param type 0商户，1代理商,2用户
	 * @param Id 记录ID
	 * @param name 商家名称或者代理商名称
	 * @param amount 金额
	 * @param status 状态 0 处理中 1 成功 2 失败
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getWithdraw(Long accountId,Long ownerId, Integer type, Long id,String name, Double amount, Integer status, String startTime, String endTime, Page page) {
		return withdrawDao.getWithdraw(accountId, ownerId, type, id,name, amount, status, startTime, endTime, page);
	}

	/**
	 * 查询提现流水记录(管理员admin查询)
	 * @param accountId 代理商或者商家的accountId，如果是用户null值
	 * @param ownerId 商户id，或者代理商id，用户id
	 * @param Id 记录ID
	 * @param name 商家名称或者代理商名称
	 * @param amount 金额
	 * @param status 状态 0 处理中 1 成功 2 失败
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @return Page对象
	 */
	public Page getWithdraw(Long accountId,Long ownerId, Long id,String name, Double amount,Integer status, String startTime, String endTime, Page page) {
		return withdrawDao.getWithdraw(accountId, ownerId, id,name, amount, status, startTime, endTime, page);
	}
	
	public Page getWithdraw(Long id,String name, Double amount,String startTime, String endTime, Page page) {
		return getWithdraw(null, null, id,name, amount, Constants.WithdrawStatus.WITHDRAW_PROGRESS, startTime, endTime, page);
	}
	
	public Page getWithdraw(Integer type, Long id,String name, Double amount, String startTime, String endTime, Page page) {
		return getWithdraw(null, null, type, id,name, amount, Constants.WithdrawStatus.WITHDRAW_PROGRESS, startTime, endTime, page);
	}

	@Transactional
	public boolean updateStatus(Integer type,Integer status ,Long id,Long ownerId,Double balance) {
		if(status.equals(Constants.WithdrawStatus.WITHDRAW_AUDIT_FAILURE)){ // 审核失败,将取现金额退回
			payAccountDao.updateBalance(balance,type,ownerId);
		}
		return withdrawDao.updateStatus(status,id,ownerId);
	}

	public Double getBalance(Long id, Integer type, Long ownerId) {
		return withdrawDao.getBalance(id,type,ownerId);
	}
	
	/**
	 * API-查询账户流水记录(商家)
	 * @param ownerId 商户id
	 * @param type 类型
	 * @param page
	 */
	public Page getWithdraw(int querySign,int querySort,String bank,String keyword,String timeInterval,Long ownerId,Integer type,Page page){
		return withdrawDao.getWithdraw(querySign,querySort,bank,keyword,timeInterval,ownerId, type, page);
	}
}