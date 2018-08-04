package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.PayChannelInfo;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.PayChannelInfoDao;

@Service
public class PayChannelInfoService {
	@Autowired
	private PayChannelInfoDao payChannelInfoDao;

	@Autowired
	private IdService idService;
	
	public PayChannelInfo findbyStoreIdAndTypeForPos(Long storeId, Integer type) {
		return payChannelInfoDao.findbyStoreIdAndTypeForPos(storeId,type);
	}

	public String findWXSubMchID(Long storeId) {
		return payChannelInfoDao.findWXSubMchID(storeId);
	}

	public PayChannelInfo findbyType(Integer type) {
		return payChannelInfoDao.findbyType(type);
	}

	public PayChannelInfo findbyStoreIdAndType(Long storeId, Integer type) {
		return payChannelInfoDao.findbyStoreIdAndType(storeId, type);
	}

	public Page findAll(Long storeId, Integer type, Page page) {
		return payChannelInfoDao.findAll(storeId, type, page);
	}

	public PayChannelInfo findPayChannelInfo(PayChannelInfo payChannelInfo) {
		return payChannelInfoDao.findPayChannelInfo(payChannelInfo);
	}

	public void editPayChannelInfo(PayChannelInfo payChannelInfo) {
		if (payChannelInfo.getId() == null) {
			payChannelInfo.setId(idService.getId());
			payChannelInfoDao.savePayChannelInfo(payChannelInfo);
		} else {
			payChannelInfoDao.updatePayChannelInfo(payChannelInfo);
		}
	}

	public PayChannelInfo findById(Long id) {
		return payChannelInfoDao.findById(id);
	}

	public void deleteById(Long id) {
		payChannelInfoDao.deleteById(id);
	}
}
