package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.PromotionCoupon;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PromotionCouponDao;

@Service
public class PromotionCouponService {
	
	@Autowired
	private PromotionCouponDao promotionCouponDao;
	
	@Autowired
	private IdService idService;
	
	public Page findAll(Long storeId,String name, Integer type, Integer status,
			String startDate, String endDate, Page page) {
		
		return promotionCouponDao.findAll(storeId,name,type,status,startDate,endDate,page);
	}
	public void editPromotionCoupon(PromotionCoupon promotionCoupon) {
		if(promotionCoupon.getIsUpdate()==null){//新增
			if(promotionCoupon.getTotalNum()==null || "".equals(promotionCoupon.getTotalNum())){
				promotionCoupon.setTotalNum(0);
			}
			if(promotionCoupon.getTotalAmount()==null || "".equals(promotionCoupon.getTotalAmount())){
				promotionCoupon.setTotalAmount(0.0);
			}
			promotionCouponDao.save(promotionCoupon);
		}else{//修改
			if(promotionCoupon.getTotalNum()==null || "".equals(promotionCoupon.getTotalNum())){
				promotionCoupon.setTotalNum(0);
			}
			if(promotionCoupon.getTotalAmount()==null || "".equals(promotionCoupon.getTotalAmount())){
				promotionCoupon.setTotalAmount(0.0);
			}
			promotionCouponDao.update(promotionCoupon);
		}
	}
	public PromotionCoupon findById(Long id) {
		return promotionCouponDao.findById(id);
	}
	public Page getCashGiftList(Long couponId,String name, String mobile, Long storeId,
			Integer status, Page page) {
		return promotionCouponDao.getCashGiftList(couponId,name,mobile,storeId,status,page);
	}
	public Promotion findByStoreId(Long storeId) {
		PromotionCoupon promotionCoupon =promotionCouponDao.findStoreId(storeId);
		if (promotionCoupon!=null) {
			Promotion promotion = new Promotion();
			promotion.setType(Constants.Promotion.TYPE_PREFERENTIAL_VOLUME);
			return promotion;
		}
		return null;
	}
	public boolean saveCashGiftBatch(List<Object[]> batchArgs) {
		return promotionCouponDao.saveCashGiftBatch(batchArgs);
	}
	
	/**
	 * 修改优惠活动的(领取/导出)数量
	 * @param promotionCouponId 优惠活动ID
	 * @param forNum 领取/导出数
	 * @return 是否修改成功
	 */
	public boolean updateReportNumById(Long promotionCouponId, int forNum) {
		return promotionCouponDao.updateReportNumById(promotionCouponId,forNum);
	}
	
	/**
	 * 根据优惠活动ID查询是否存在此活动且此活动为手动领取且未失效
	 * @param couponId 优惠活动ID
	 * @return 活动对象或者null
	 */
	public PromotionCoupon apiFindById(Long couponId) {
		return promotionCouponDao.apiFindById(couponId);
	}

}