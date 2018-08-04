package cn.lastmiles.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.ReportSales;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ReportSalesService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("reportSales")
public class ReportSalesController {
	private final static Logger logger = LoggerFactory.getLogger(ReportSalesController.class);
	
	@Autowired
	private ReportSalesService reportSalesService;
	
	@Autowired 
	private StoreService storeService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/reportSales/list";
	}
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * 销售统计列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "reportSales/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Long storeId, Integer sort,Integer source,String beginTime,String endTime,Page page, Model model) {
		
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("storeId={}",storeId);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		// 判断登录者是商家且非总店(2016-01-19-update)
		logger.debug("storeId={},beginTime={},endTime={}",storeIdString.toString(),beginTime,endTime);
		model.addAttribute("data", reportSalesService.findAllPage(storeIdString.toString(),source,beginTime, endTime,sort,page));
		model.addAttribute("reportSum", reportSalesService.findAllSum(storeIdString.toString(),beginTime, endTime));
		model.addAttribute("reportStoreSum", reportSalesService.findStoreMunSum(storeIdString.toString(),beginTime, endTime));
		return "reportSales/list-data";
		
	}
	/**
	 *销售统计列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "storeList")
	public String storeList(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "reportSales/storeList";
	}
	@RequestMapping("storeList/storeList-data")
	public String storeList(Long storeId,Integer source,Integer type,String beginTime,String endTime,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("storeId={}",storeId);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		
		// 判断登录者是商家且非总店(2016-01-19-update)
		logger.debug("storeId={},beginTime={},endTime={}",storeId,beginTime,endTime);
		
		model.addAttribute("data", reportSalesService.findStorePage(storeIdString.toString(),source,beginTime, endTime,type,page));
		model.addAttribute("reportSum", reportSalesService.findStoreSum(storeIdString.toString(),beginTime, endTime));
		model.addAttribute("reportStoreSum", reportSalesService.findStoreMunSum(storeIdString.toString(),beginTime, endTime));
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "reportSales/storeList-data";
	}
	/**
	 * 弹窗测试
	 * @param name
	 * @param mobile
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isMainStore()){
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
		}
		page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "reportSales/showModelList-data";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,
			String beginTime, String endTime, Long storeId,Integer source,Integer sort) throws ParseException {
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("source={}",source);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		
		String fileName = "销售统计";
		logger.debug("storeId={},source={},beginTime={},endTime={}",storeIdString.toString(),source,beginTime,endTime);
		List<ReportSales> reportSalesList = reportSalesService.findAll(storeIdString.toString(),source,beginTime,endTime,sort);
		ReportSales rss=reportSalesService.findAllSum(storeIdString.toString(),beginTime, endTime);
		ReportSales reportStoreSum=reportSalesService.findStoreMunSum(storeIdString.toString(),beginTime, endTime);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("销售统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<12;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("订单数量");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("销售额");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("销售成本");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("参考毛利");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("促销让利");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("实销毛利");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportSalesList.size());
		if (!reportSalesList.isEmpty()) {
			for (int i = 0; i < reportSalesList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ReportSales reportSales=reportSalesList.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(reportSales.getStoreName());//商家名称
				row.createCell(1).setCellValue(reportSales.getOrderNum());//订单数量
				row.createCell(2).setCellValue("￥"+df.format(reportSales.getSalesNum()));//销售额
				row.createCell(3).setCellValue("￥"+df.format(reportSales.getCostPrice()));//销售额
				row.createCell(4).setCellValue("￥"+df.format(reportSales.getGrossProfit()));//参考毛利
				row.createCell(5).setCellValue("￥"+df.format(reportSales.getPromotionsGrossProfit()));
				row.createCell(6).setCellValue("￥"+df.format(reportSales.getActualGrossProfit()));
				
			}
			row=sheet.createRow((int) reportSalesList.size()+2);
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("商家数量");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("订单总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("销售总额");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("销售总成本");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("参考总毛利");
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("促销总让利");
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("实销总毛利");
			cell.setCellStyle(style);
			row=sheet.createRow((int) reportSalesList.size()+3);
			
			cell = row.createCell(0);
			cell.setCellValue(String.valueOf(reportStoreSum.getStoreSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue(String.valueOf(rss.getOrderNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("￥"+df.format(rss.getSalesNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(rss.getCostPrice()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("￥"+df.format(rss.getGrossProfit()));
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("￥"+df.format(rss.getPromotionsGrossProfit()));
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("￥"+df.format(rss.getActualGrossProfit()));
			cell.setCellStyle(style);
		}
		
		// 第六步，将文件存到指定位置
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			os.flush();
			// 关闭输出流
			os.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-storeSearch", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findStoreBySearch(HttpServletResponse response,
			String beginTime, String endTime, Long storeId,Integer source,Integer type,Page page) throws ParseException {
		page.setIsOnePage();
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("source={}",source);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		
		String fileName = "销售统计";
		logger.debug("storeId={},source={},beginTime={},endTime={}",storeIdString.toString(),source,beginTime,endTime);
		@SuppressWarnings("unchecked")
		List<ReportSales> reportSalesList = (List<ReportSales>) reportSalesService.findStorePage(storeIdString.toString(),source,beginTime,endTime,type,page).getData();
		ReportSales rss=reportSalesService.findStoreSum(storeIdString.toString(),beginTime, endTime);
		ReportSales reportStoreSum=reportSalesService.findStoreMunSum(storeIdString.toString(),beginTime, endTime);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("销售统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<12;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("日期");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("订单数量");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("销售额");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("销售成本");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("参考毛利");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("促销让利");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("实销毛利");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportSalesList.size());
		if (!reportSalesList.isEmpty()) {
			for (int i = 0; i < reportSalesList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ReportSales reportSales=reportSalesList.get(i);
				row = sheet.createRow((int) i + 1);
				if(type.intValue()==0){
					row.createCell(0).setCellValue(DateUtils.format(reportSales.getReportDate(), "yyyy-MM-dd"));
				}else{
					row.createCell(0).setCellValue(DateUtils.format(reportSales.getReportDate(), "yyyy-MM"));
				}
				row.createCell(1).setCellValue(reportSales.getOrderNum());//订单数量
				row.createCell(2).setCellValue("￥"+df.format(reportSales.getSalesNum()));//销售额
				row.createCell(3).setCellValue("￥"+df.format(reportSales.getCostPrice()));//销售额
				row.createCell(4).setCellValue("￥"+df.format(reportSales.getGrossProfit()));//参考毛利
				row.createCell(5).setCellValue("￥"+df.format(reportSales.getPromotionsGrossProfit()));
				row.createCell(6).setCellValue("￥"+df.format(reportSales.getActualGrossProfit()));
				
			}
			row=sheet.createRow((int) reportSalesList.size()+2);
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("商家数量");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("订单总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("销售总额");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("销售总成本");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("参考总毛利");
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("促销总让利");
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("实销总毛利");
			cell.setCellStyle(style);
			row=sheet.createRow((int) reportSalesList.size()+3);
			
			cell = row.createCell(0);
			cell.setCellValue(String.valueOf(reportStoreSum.getStoreSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue(String.valueOf(rss.getOrderNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("￥"+df.format(rss.getSalesNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(rss.getCostPrice()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("￥"+df.format(rss.getGrossProfit()));
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("￥"+df.format(rss.getPromotionsGrossProfit()));
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("￥"+df.format(rss.getActualGrossProfit()));
			cell.setCellStyle(style);
		}
		
		// 第六步，将文件存到指定位置
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			os.flush();
			// 关闭输出流
			os.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
