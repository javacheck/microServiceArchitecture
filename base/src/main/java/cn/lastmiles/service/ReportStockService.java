package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ReportProduct;
import cn.lastmiles.bean.ReportStock;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.ReportStockDao;

@Service
public class ReportStockService {
	@Autowired
	private ReportStockDao reportStockDao;

	@SuppressWarnings("unchecked")
	public Page findAllPage(String storeIdString, String name, String barCode,
			Integer sort, Page page) {
		List<ReportStock> returnReportStock = new ArrayList<ReportStock>();
		page = reportStockDao.findAllPage(storeIdString,name,barCode,sort,page);
		
		List<ReportStock> rss = (List<ReportStock>) page.getData();
		for (ReportStock reportStock : rss) {
			returnReportStock.add(this.filling(reportStock));
		}
		page.setData(returnReportStock);
		return page;
	}

	private ReportStock filling(ReportStock reportStock) {
		if (reportStock == null) {
			return null;
		}
		String attributeValues="";
		if(reportStock.getName().indexOf("|")>0){
			String[] arrs =StringUtil.splitc(reportStock.getName(), '|');
			reportStock.setProductName(arrs[0]);
			for(int i=1;i<arrs.length;i++){
				attributeValues+=arrs[i]+"|";
			}
			reportStock.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
			
		}else{
			reportStock.setProductName(reportStock.getName());
		}
		return reportStock;
	}

	public ReportStock findAllSum(String storeIdString, String name, String barCode) {
		
		return reportStockDao.findAllSum(storeIdString,name,barCode);
	}

	public ReportStock findProductSum(String storeIdString, String name, String barCode) {
		return reportStockDao.findProductSum(storeIdString,name,barCode);
	}

	public List<ReportStock> findAll(String storeIdString, String name, String barCode,
			Integer sort) {
		return reportStockDao.findAll(storeIdString,name,barCode,sort);
	}
}
