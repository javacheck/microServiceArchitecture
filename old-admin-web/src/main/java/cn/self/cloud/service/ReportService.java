package cn.self.cloud.service;

import java.util.List;

import cn.self.cloud.bean.Report;
import cn.self.cloud.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

	@Autowired
	private ReportDao reportDao;

	public List<Report> findAll(String beginTime, String endTime, String store,
								String category, String date) {
		return reportDao.findAll(beginTime, endTime, store, category, date);
	}

	public List<Report> findAllBySearch(String beginTime, String endTime,
			String store, String category, String date) {

		return reportDao.findAllBySearch(beginTime, endTime, store, category,
				date);
	}

}
