package cn.lastmiles.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.CashGift;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.CashGiftDao;

@Service
public class CashGiftService {
	@Autowired
	private CashGiftDao cashGiftDao;
	@Autowired
	private IdService idService;
	
	public Page list(String mobile, String shopName, Page page) {
		return cashGiftDao.list(mobile,shopName,page);
	}

	/**
	 * 进行优惠卷的新增(默认设置优惠卷为未使用状态,创建时间为新增时)
	 * @param cashGift 优惠卷对象
	 * @return 是否新增成功(大于0,则新增成功)
	 */
	public int save(CashGift cashGift) {
		if (cashGift.getId()==null) {
			cashGift.setId(idService.getId());
		}
		cashGift.setStatus(Constants.CashGift.STATUSNORMAL);
		return cashGiftDao.save(cashGift);
	}

	public String checkMobile(String mobile) {
		return cashGiftDao.checkMobile(mobile);
	}

	public int delete(Long id,Long userId) {
		return cashGiftDao.delete(id,userId);
	}
	
	public CashGift findOne(Long id, Long userId, Long storeId) {
		return cashGiftDao.findOne(id, userId, storeId);
	}
	
	public CashGift findOne(Long id, Long userId, Long storeId,Integer status) {
		if( ObjectUtils.equals(status, Constants.CashGift.STATUSNORMAL)){
			return findOne(id, userId, storeId);
		}
		return cashGiftDao.findOne(id, userId, storeId, status);
	}
	
	public CashGift findById(Long id) {
		return cashGiftDao.findById(id);
	}
	
	
	public List<CashGift> findByUserIdAndStoreId(Long userId, Long storeId) {
		return cashGiftDao.findByUserIdAndStoreId(userId, storeId);
	}

	public void useCashGift(Long cashGiftId, Long storeId, Long userId) {
		cashGiftDao.used(cashGiftId,userId,Constants.CashGift.STATUSUSED);
	}
	
	public void updateStatus(Long cashGiftId,Long storeId,Long userId,Integer status){
		if(ObjectUtils.equals(status, Constants.CashGift.STATUSUSED)){
			useCashGift(cashGiftId,storeId,userId);
		} else {
			cashGiftDao.used(cashGiftId,userId,Constants.CashGift.STATUSNORMAL);
		}
	}

	public Page findByUserId(Long userId,Page page) {
		page = cashGiftDao.findByUserId(userId,page);
		LinkedList<CashGift> list = new LinkedList<CashGift>(); 
		@SuppressWarnings("unchecked")
		List<CashGift> cashGiftList = (List<CashGift>) page.getData();
		if(cashGiftList.isEmpty()){
			return page;
		}
		
		Collections.reverse(cashGiftList);
		
		for (CashGift cashGift : cashGiftList) {
			if(ObjectUtils.equals(cashGift.getStatus(),Constants.CashGift.STATUSUSED)){
				cashGift.setUseTipInfo("已使用");
				list.addLast(cashGift);
				continue;
			}
			Long isHaveUse = cashGift.getValidTime().getTime() - System.currentTimeMillis() ;
			if(isHaveUse.longValue() <= 0){
				cashGift.setUseTipInfo("已过期"); // 过期
				list.addLast(cashGift);
				continue;
			}
			long day = (isHaveUse / (24*60*60*1000));
			
			cashGift.setUseTipInfo("还有" + (day + 1) + "天过期");
			list.addFirst(cashGift);
		}
		page.setData(list);
		
		return page;
	}

	/**
	 * 根据优惠活动ID查询优惠卷的信息
	 * @param couponId 优惠活动ID
	 * @return 优惠卷集合或者null
	 */
	public List<CashGift> findByCouponId(Long couponId) {
		return cashGiftDao.findByCouponId(couponId);
	}

	/**
	 * 根据优惠卷ID查询优惠卷信息(有效,未使用,未过期)
	 * @param cashGiftId 优惠卷ID
	 * @return null或者优惠卷对象
	 */
	public CashGift checkCashGiftById(Long cashGiftId) {
		return cashGiftDao.checkCashGiftById(cashGiftId);
	}

	/**
	 * 根据用户ID和商家ID以及优惠活动ID查询是否存在优惠卷
	 * @param userId 用户ID
	 * @param storeId 商家ID
	 * @param couponId 优惠活动ID
	 * @return 优惠卷对象或者null
	 */
	public CashGift checkByUserId$StoreId$couponId(Long userId, Long storeId, Long couponId) {
		return cashGiftDao.checkByUserId$StoreId$couponId(userId,storeId,couponId);
	}
}