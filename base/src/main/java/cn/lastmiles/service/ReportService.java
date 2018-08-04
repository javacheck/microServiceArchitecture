package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Report;
import cn.lastmiles.dao.ReportDao;

@Service
public class ReportService {

	@Autowired
	private ReportDao reportDao;

	public List<Report> findAll(String beginTime, String endTime, String storeStr,
			String category, String date,Long accountId) {
		return reportDao.findAll(beginTime, endTime, storeStr, category, date,accountId);
	}

	public List<Report> findAllBySearch(String beginTime, String endTime,
			String storeStr, String category, String date,Long accountId) {

		return reportDao.findAll(beginTime, endTime, storeStr, category,date,accountId);
	}

}
