package cn.lastmiles.service;

/**
 * createDate : 2015-07-10 PM 16:01
 */
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.bean.UserBalanceRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PayAccountDao;

@Service
public class PayAccountService {
	@Autowired
	private IdService idService;
	@Autowired
	private PayAccountDao payAccountDao;
	@Autowired
	private UserBalanceRecordService userBalanceRecordService;
	
	public Page list(Page page,Integer type,String mobile,Integer status){
		return payAccountDao.list(page,type,mobile,status);
	}

	/**
	 * 使用余额
	 * @param balance
	 * @param ownerId
	 * @param type
	 */
	@Transactional
	public void reduceBalance(Double balance,Long ownerId,Integer type,Long orderId){
		payAccountDao.reduceBalance(balance, ownerId, type);
		UserBalanceRecord userBalanceRecord = new UserBalanceRecord();
		userBalanceRecord.setMemo("使用余额");
		userBalanceRecord.setAmount(-balance);
		userBalanceRecord.setUserId(ownerId);
		userBalanceRecord.setOrderId(orderId);
		userBalanceRecord.setType(Constants.UserBalanceRecord.TYPE_SPEND);
		userBalanceRecordService.save(userBalanceRecord);
		
	}
	
	/**
	 * 根据条件查询是否有支付账号信息
	 * 
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @param status
	 *            支付账号状态
	 * @return 支付账号对象或者null
	 */
	public PayAccount queryHaveData(Long ownerId, Integer type, Integer status) {
		return payAccountDao.queryHaveData(ownerId, type, status);
	}

	/**
	 * 根据条件修改支付账号的密码信息
	 * 
	 * @param passWord
	 *            修改后的密码
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @return 是否修改成功
	 */
	public Boolean setPassword(String passWord, Long ownerId, Integer type) {
		if (!payAccountDao.setPassword(passWord, ownerId, type)) {
			PayAccount pa = new PayAccount();
			pa.setId(idService.getId());
			pa.setCreatedTime(new Date());
			pa.setOwnerId(ownerId);
			pa.setType(type);
			pa.setPassword(passWord);
			pa.setStatus(Constants.PayAccountStatus.PAYACCOUNT_NORMAL);
			this.save(pa);
		}
		return true;
	}

	/**
	 * 保存支付账号对象信息
	 * 
	 * @param payaccount
	 *            支付账号对象
	 * @return 是否修改成功
	 */
	public Boolean save(PayAccount payaccount) {
		return payAccountDao.save(payaccount);
	}

	/**
	 * 根据条件查询支付账号对象信息
	 * 
	 * @param ownerId
	 *            支付账号的所有者ID
	 * @param type
	 *            支付账号的所有者类型
	 * @return 支付账号对象或者null
	 */
	public PayAccount getByOwnerIdAndType(Long ownerId, Integer type) {
		return payAccountDao.getByOwnerIdAndType(ownerId, type);
	}

	/**
	 * 校验支付密码
	 * 
	 * @param ownerId
	 * @param type
	 * @return
	 */
	public int checkPaypassword(Long ownerId, int type, String paypassword) {
		String pwd = payAccountDao.getPaypassword(ownerId, type);
		if (pwd == null){
			return 0;
		}
		if( PasswordUtils.checkPassword(paypassword,
				payAccountDao.getPaypassword(ownerId, type))){
			return 1;
		}
		return 2;
	}
	
	/**
	 * 返还价格
	 * @param ownerId
	 * @param type
	 * @param balance
	 */
	@Transactional
	public void revertBalance(Long ownerId,Integer type,Double balance,Long orderId){
		if (balance.doubleValue()!=0) {
			PayAccount payAccount =payAccountDao.getByOwnerIdAndType(ownerId, type);
			if (payAccount!=null) {
				payAccountDao.updateBalance(NumberUtils.add(balance, payAccount.getBalance()), type, ownerId);
				UserBalanceRecord userBalanceRecord = new UserBalanceRecord();
				userBalanceRecord.setMemo("订单取消退回余额");
				userBalanceRecord.setType(Constants.UserBalanceRecord.TYPE_ORDER_CANCEL);
				userBalanceRecord.setAmount(balance);
				userBalanceRecord.setUserId(ownerId);
				userBalanceRecord.setOrderId(orderId);
				userBalanceRecordService.save(userBalanceRecord);
			}
		}
	}
	@Transactional
	public void AddfrozenAmount(Long ownerId,Integer type,Double frozenAmount){
		PayAccount payAccount=this.getByOwnerIdAndType(ownerId, type);
		if (payAccount!=null) {
			payAccountDao.updateFrozenAmount(ownerId,type,NumberUtils.add(payAccount.getFrozenAmount(), frozenAmount));
		}
	}
	/**
	 * 冻结余额到用户可提现余额
	 * @param ownerId
	 * @param type
	 * @param amount
	 */
	@Transactional
	public void dealFrozenAmountToBalance(Long ownerId, Integer type,Double amount) {
		PayAccount payAccount=this.getByOwnerIdAndType(ownerId, type);
		payAccountDao.updateFrozenAmount(ownerId, type, NumberUtils.subtract(payAccount.getFrozenAmount(), amount));
		payAccountDao.updateBalance(NumberUtils.add(payAccount.getBalance(), amount), type, ownerId);
	}
	
	@Transactional
	public void addBalance(Integer type, Long ownerId, Double amount) {
		PayAccount payAccount=this.getByOwnerIdAndType(ownerId, type);
		payAccountDao.updateBalance(NumberUtils.add(payAccount.getBalance(), amount), type, ownerId);
	}
	
}