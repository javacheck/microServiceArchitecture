package cn.lastmiles.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.dao.TransactionAnalysisDao;

@Service
public class TransactionAnalysisService {
	@Autowired
	private TransactionAnalysisDao transactionAnalysisDao;

	public List<Map<String,Object>> findDateAll(String storeIdString, Long stockId,Integer analysisType,
			String dateBeginTime, String dateEndTime, Integer dateOrderType,Integer source) {
		
		return transactionAnalysisDao.findDateAll(storeIdString,stockId,analysisType,dateBeginTime,dateEndTime,dateOrderType,source);
	}

	

	public List<Map<String, Object>> findMonthAll(String storeIdString,
			Long stockId, Integer analysisType, String monthTime,
			Integer monthOrderType, Integer source) {
		
		return transactionAnalysisDao.findMonthAll(storeIdString,stockId,analysisType,monthTime,monthOrderType,source);
	}
	public List<Map<String, Object>> findYearAll(String storeIdString,
			Long stockId, Integer analysisType, String yearTime,
			Integer yearOrderType, Integer source) {
		
		return transactionAnalysisDao.findYearAll(storeIdString,stockId,analysisType,yearTime,yearOrderType,source);
	}



	public List<Map<String, Object>> findMonthAll(String storeIdString, 
			Integer analysisType, String beginTime, String endTime,Integer source) {
		
		return transactionAnalysisDao.findMonthAll(storeIdString, analysisType, beginTime, endTime,source);
	}



	
}
