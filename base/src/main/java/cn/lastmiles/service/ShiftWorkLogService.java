package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.PaymentModeInfo;
import cn.lastmiles.bean.ShiftWorkLog;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ShiftWorkLogDao;

@Service
public class ShiftWorkLogService {
	@Autowired
	private IdService idService;
	@Autowired
	private ShiftWorkLogDao shiftWorkLogDao;
	
	@Autowired
	private PaymentModeInfoService paymentModeInfoService;

	public void save(ShiftWorkLog shiftWorkLog) {
		shiftWorkLog.setId(idService.getId());
		List<PaymentModeInfo> paymentModeInfos =shiftWorkLog.getPaymentModeInfos();
		if (paymentModeInfos!=null) {
			for (PaymentModeInfo paymentModeInfo : paymentModeInfos) {
				paymentModeInfo.setAssociationId(shiftWorkLog.getId());
				paymentModeInfoService.save(paymentModeInfo);
			}
		}
		shiftWorkLogDao.save(shiftWorkLog);
	}

	@SuppressWarnings("unchecked")
	public Page list(String beginTime, String endTime, String name,String mobile, Page page,Long storeId) {
		page=shiftWorkLogDao.list(beginTime,endTime,name,mobile, page,storeId);
		List<ShiftWorkLog> shiftWorkLogs=(List<ShiftWorkLog>) page.getData();
		if (shiftWorkLogs!=null&&!shiftWorkLogs.isEmpty()) {
			for (ShiftWorkLog shiftWorkLog : shiftWorkLogs) {
				List<PaymentModeInfo> paymentModeInfos = paymentModeInfoService.findByAssociationId(shiftWorkLog.getId());
				shiftWorkLog.setPaymentModeInfos(paymentModeInfos);
			}
		}
		return page;
	}

}
