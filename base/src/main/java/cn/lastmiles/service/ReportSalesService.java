package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ReportSales;
import cn.lastmiles.bean.ReportSum;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.ReportSalesDao;

@Service
public class ReportSalesService {
	
	@Autowired
	private ReportSalesDao reportSalesDao;
	@SuppressWarnings("unchecked")
	public Page findAllPage(String storeIdString, Integer source,
			String beginTime, String endTime, Integer sort,Page page) {
		List<ReportSales> returnreportSales = new ArrayList<ReportSales>();
		page = reportSalesDao.findAllPage(storeIdString,source,beginTime,endTime,sort,page);
		List<ReportSales> sales = (List<ReportSales>) page.getData();
		for (ReportSales reportSales : sales) {
			returnreportSales.add(this.filling(reportSales));
		}
		page.setData(returnreportSales);
		return page;
	}
	public ReportSales filling(ReportSales reportSales) {
		if (reportSales == null) {
			return null;
		}
		reportSales.setSalesNum(NumberUtils.dealDecimal(reportSales.getSalesNum(), 2));
		if(reportSales.getOrderNum().intValue()!=0 && reportSales.getOrderNum()!=null){
			reportSales.setPricePerCustomer(NumberUtils.dealDecimal(NumberUtils.divide(reportSales.getSalesNum(),reportSales.getOrderNum()), 2));
		}else{
			reportSales.setPricePerCustomer(0.00D);
		}
		
		return reportSales;
	}
	public ReportSales findAllSum(String storeIdString,
			String beginTime, String endTime){
		return reportSalesDao.findAllSum(storeIdString,beginTime,endTime);
	}
	
	public List<ReportSales> findAll(String storeIdString, Integer source,
			String beginTime, String endTime,Integer sort){
		return reportSalesDao.findAll(storeIdString,source,beginTime,endTime,sort);
	}
	public ReportSales findReportSalesSum(String storeIdString, Integer source,Integer type) {
		
		return reportSalesDao.findReportSalesSum(storeIdString,source,type);
	}
	public ReportSales findStoreMunSum(String storeIdString, String beginTime,
			String endTime) {
		return reportSalesDao.findStoreMunSum(storeIdString,beginTime,endTime);
	}
	@SuppressWarnings("unchecked")
	public Page findStorePage(String storeIdString, Integer source, String beginTime,
			String endTime, Integer type, Page page) {
		List<ReportSales> returnreportSales = new ArrayList<ReportSales>();
		page = reportSalesDao.findStorePage(storeIdString,source,beginTime,endTime,type,page);
		List<ReportSales> sales = (List<ReportSales>) page.getData();
		for (ReportSales reportSales : sales) {
			if(type.intValue()==0){
				reportSales.setDateString(DateUtils.format(reportSales.getReportDate(), "yyyy-MM-dd"));
			}else{
				reportSales.setDateString(DateUtils.format(reportSales.getReportDate(), "yyyy-MM"));
			}
			returnreportSales.add(this.filling(reportSales));
		}
		page.setData(returnreportSales);
		return page;
	}
	public ReportSales findStoreSum(String storeIdString, String beginTime,
			String endTime) {
		
		return reportSalesDao.findStoreSum(storeIdString,beginTime,endTime);
	}
	public List<ReportSales> findStore(String storeIdString, Integer source,
			String beginTime, String endTime, Integer type) {
		
		return reportSalesDao.findStore(storeIdString,source,beginTime,endTime,type);
	}
}
