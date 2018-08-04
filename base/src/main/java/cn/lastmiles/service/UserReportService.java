package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ReportType;
import cn.lastmiles.bean.UserReport;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.UserReportDao;

@Service
public class UserReportService {
	@Autowired
	private UserReportDao userReportDao;
	@Autowired
	private IdService idService;
	public Page list(String beginTime,String endTime,String typeId, String content, String contact,
			Long accountId, Page page) {
		
		
		return userReportDao.list(beginTime,endTime,typeId,content,contact,accountId,page);
	}
	public List<ReportType> reportTypeList() {
		return userReportDao.reportTypeList();
	}
	public void editReport(UserReport userReport) {
		
		userReport.setId(idService.getId());
		userReportDao.editReport(userReport);
		
	}
	public void delByReportId(Long id) {
		userReportDao.delByReportId(id);
		
	}
	public UserReport findByReportId(Long id) {
		return userReportDao.findByReportId(id);
	}

}
