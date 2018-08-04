package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ReportProduct;
import cn.lastmiles.bean.ReportSum;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.ReportProductDao;

@Service
public class ReportProductService {
	
	@Autowired
	private ReportProductDao reportProductDao;
	@SuppressWarnings("unchecked")
	public Page findAllPage(String storeIdString, String name,Integer type,Integer source,Integer sort,
			String beginTime, String endTime, Page page) {
		List<ReportProduct> returnReportProduct = new ArrayList<ReportProduct>();
		page = reportProductDao.findAllPage(storeIdString,name,type,source,sort,beginTime,endTime,page);
		
		List<ReportProduct> rps = (List<ReportProduct>) page.getData();
		for (ReportProduct reportProduct : rps) {
			returnReportProduct.add(this.filling(reportProduct));
		}
		page.setData(returnReportProduct);
		return page;
	}
	public ReportProduct filling(ReportProduct reportProduct) {
		if (reportProduct == null) {
			return null;
		}
		String attributeValues="";
		if(StringUtils.isNotBlank(reportProduct.getStockName()) && reportProduct.getStockName().indexOf("|")>0){
			String[] arrs =StringUtil.splitc(reportProduct.getStockName(), '|');
			reportProduct.setProductName(arrs[0]);
			for(int i=1;i<arrs.length;i++){
				attributeValues+=arrs[i]+"|";
			}
			reportProduct.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
			
		}else{
			reportProduct.setProductName(reportProduct.getStockName());
		}
		reportProduct.setActualPrice(NumberUtils.dealDecimal(reportProduct.getActualPrice(), 2));
		return reportProduct;
	}
	
	public List<ReportProduct> findAll(String storeIdString, String name,Integer type,Integer source,Integer sort,
			String beginTime, String endTime){
		return reportProductDao.findAll(storeIdString,name,type,source,sort,beginTime,endTime);
	}


	public ReportProduct findAllSum(String storeIdString, String name,Integer type,Integer source, String beginTime, String endTime) {
		
		return reportProductDao.findAllSum(storeIdString,name,type,source,beginTime,endTime);
	}
	public ReportProduct findStoreMunSum(String storeIdString, String name,Integer type,Integer source, String beginTime,
			String endTime) {
		return reportProductDao.findStoreMunSum(storeIdString,name,type,source,beginTime,endTime);
	}
	public ReportProduct findStockMunSum(String storeIdString, String name,Integer type,Integer source, String beginTime,
			String endTime) {
		return reportProductDao.findStockMunSum(storeIdString,name,type,source,beginTime,endTime);
	}
}
