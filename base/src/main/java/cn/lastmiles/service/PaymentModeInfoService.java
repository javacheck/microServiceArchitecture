package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.PaymentModeInfo;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.PaymentModeInfoDao;

/**
 * 
 * @author zhangpengcheng
 *
 */
@Service
public class PaymentModeInfoService {
	
	@Autowired
	private PaymentModeInfoDao paymentModeInfoDao;
	@Autowired
	private IdService idService;
	
	/**
	 * 保存方法
	 * @param paymentModeInfo
	 */
	public void save(PaymentModeInfo paymentModeInfo) {
		paymentModeInfo.setId(idService.getId());
		paymentModeInfoDao.save(paymentModeInfo);
	}
	/**
	 * 根据关联查询
	 * @param associationId
	 * @return
	 */
	public List<PaymentModeInfo> findByAssociationId(Long associationId) {
		return paymentModeInfoDao.findByAssociationId(associationId);
	}
	

}
