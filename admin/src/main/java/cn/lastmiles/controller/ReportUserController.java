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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ReportUserService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("reportUser")
public class ReportUserController {
	private final static Logger logger = LoggerFactory.getLogger(ReportUserController.class);

	@Autowired
	private ReportUserService reportUserService;
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		logger.debug("SecurityUtils.isMainStore()={}",SecurityUtils.isMainStore());
		if(SecurityUtils.isMainStore()){
			return "redirect:/reportUser/list";
		}else{
			return "redirect:/reportUser/storeList";
		}
	}
	
	/**
	 * 会员统计列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "reportUser/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long storeId,Integer sort,String beginTime,String endTime,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					logger.debug("storeId={}",SecurityUtils.getAccountStoreId());
					List<Store> storeList = storeService.findAllEntryChildrenStore(SecurityUtils.getAccountStoreId());
					logger.debug("storeList={}",storeList);
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
		logger.debug("storeId={},beginTime={},endTime={},sort={}",storeIdString.toString(),beginTime,endTime,sort);
		
		model.addAttribute("data", reportUserService.findAllPage(storeIdString.toString(),beginTime, endTime,sort,page));
		model.addAttribute("reportSum", reportUserService.findAllSum(storeIdString.toString(),beginTime, endTime));
		model.addAttribute("reportSumByTime", reportUserService.findAllSumByTime(storeIdString.toString(),beginTime, endTime));
		model.addAttribute("reportStoreSum", reportUserService.findStoreMunSum(storeIdString.toString(),beginTime, endTime));
		return "reportUser/list-data";
	}
	/**
	 * 会员统计列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "storeList")
	public String storeList(Model model) {
		return "reportUser/storeList";
	}
	@RequestMapping("storeList/storeList-data")
	public String storeList(Integer type,String beginTime,String endTime,Page page, Model model) {
		Long storeId=SecurityUtils.getAccountStoreId();
		
		// 判断登录者是商家且非总店(2016-01-19-update)
		logger.debug("storeId={},beginTime={},endTime={}",storeId,beginTime,endTime);
		
		model.addAttribute("data", reportUserService.findStorePage(storeId,type,beginTime, endTime,page));
		model.addAttribute("reportSum", reportUserService.findStoreSum(storeId,type,beginTime, endTime));
		model.addAttribute("reportSumByTime", reportUserService.findReportSumByTime(storeId,type,beginTime, endTime));
		return "reportUser/storeList-data";
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
		return "reportUser/showModelList-data";
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
			Long storeId,Integer sort,String beginTime,String endTime) throws ParseException {
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		StringBuffer storeIdString = new StringBuffer();
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
		String fileName = "会员统计";
		logger.debug("storeId={},beginTime={},endTime={}",storeId,beginTime,endTime);
		List<ReportUser> reportUserList = reportUserService.findAll(storeIdString.toString(),beginTime, endTime,sort);
		ReportUser ru=reportUserService.findAllSum(storeIdString.toString(),beginTime, endTime);
		ReportUser r=reportUserService.findAllSumByTime(storeIdString.toString(),beginTime, endTime);
		ReportUser storeSum=reportUserService.findStoreMunSum(storeIdString.toString(),beginTime, endTime);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("会员统计");
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
				cell.setCellValue("新增会员总数");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("新增储值");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("储值卡消费");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("储值卡余额");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("会员剩余积分");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("累计会员");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("累计储值");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("储值卡累计消费");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportUserList.size());
		if (!reportUserList.isEmpty()) {
			for (int i = 0; i < reportUserList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ReportUser reportUser=reportUserList.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(reportUser.getStoreName());//商家名称
				row.createCell(1).setCellValue(reportUser.getUserNum());
				
				row.createCell(2).setCellValue("￥"+df.format(reportUser.getRecharge()));
				row.createCell(3).setCellValue("￥"+df.format(reportUser.getConsumption()));
				row.createCell(4).setCellValue("￥"+df.format(reportUser.getTotalBalance()));
				
				row.createCell(5).setCellValue(reportUser.getTotalPoint());
				row.createCell(6).setCellValue(reportUser.getTotalUserNum());
				row.createCell(7).setCellValue("￥"+df.format(reportUser.getTotalRecharge()));
				row.createCell(8).setCellValue("￥"+df.format(reportUser.getTotalConsumption()));
			}
			row=sheet.createRow((int) reportUserList.size()+2);
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("商家总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("新增会员总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("新增总储值");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("储值卡总消费");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("储值卡总余额");
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("会员总剩余积分");
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("累计会员");
			cell.setCellStyle(style);
			
			cell = row.createCell(7);
			cell.setCellValue("累计储值");
			cell.setCellStyle(style);
			
			cell = row.createCell(8);
			cell.setCellValue("储值卡累计消费");
			cell.setCellStyle(style);
			
			row=sheet.createRow((int) reportUserList.size()+3);
			
		    cell = row.createCell(0);
			cell.setCellValue(String.valueOf(storeSum.getStoreSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue(String.valueOf(r.getUserNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("￥"+df.format(r.getRecharge()));
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(r.getConsumption()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("￥"+df.format(r.getTotalBalance()));
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue(r.getTotalPoint());
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			logger.debug("ru.getTotalUserNum()={}",ru.getTotalUserNum());
			cell.setCellValue(String.valueOf(ru.getTotalUserNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(7);
			cell.setCellValue("￥"+df.format(ru.getTotalRecharge()));
			cell.setCellStyle(style);
			
			cell = row.createCell(8);
			cell.setCellValue("￥"+df.format(ru.getTotalConsumption()));
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
			Integer type,String beginTime,String endTime) throws ParseException {
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		Long storeId=SecurityUtils.getAccountStoreId();
		String fileName = "会员统计";
		logger.debug("storeId={},beginTime={},endTime={}",storeId,beginTime,endTime);
		List<ReportUser> reportUserList = reportUserService.findStore(storeId,type,beginTime, endTime);
		ReportUser ru=reportUserService.findReportSumByTime(storeId,type,beginTime, endTime);
		ReportUser r=reportUserService.findStoreSum(storeId,type,beginTime, endTime);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("会员统计");
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
				cell.setCellValue("新增会员总数");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("新增储值");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("储值卡消费");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("储值卡余额");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("会员剩余积分");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("累计会员");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("累计储值");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("储值卡累计消费");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportUserList.size());
		if (!reportUserList.isEmpty()) {
			for (int i = 0; i < reportUserList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ReportUser reportUser=reportUserList.get(i);
				row = sheet.createRow((int) i + 1);
				if(type.intValue()==0){
					row.createCell(0).setCellValue(DateUtils.format(reportUser.getReportDate(), "yyyy-MM-dd"));
				}else{
					row.createCell(0).setCellValue(DateUtils.format(reportUser.getReportDate(), "yyyy-MM"));
				}
				
				row.createCell(1).setCellValue(reportUser.getUserNum());
				
				row.createCell(2).setCellValue("￥"+df.format(reportUser.getRecharge()));
				row.createCell(3).setCellValue("￥"+df.format(reportUser.getConsumption()));
				row.createCell(4).setCellValue("￥"+df.format(reportUser.getTotalBalance()));
				
				row.createCell(5).setCellValue(reportUser.getTotalPoint());
				row.createCell(6).setCellValue(reportUser.getTotalUserNum());
				row.createCell(7).setCellValue("￥"+df.format(reportUser.getTotalRecharge()));
				row.createCell(8).setCellValue("￥"+df.format(reportUser.getTotalConsumption()));
			}
			row=sheet.createRow((int) reportUserList.size()+2);
			
			
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("新增会员总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("新增总储值");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("储值卡总消费");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("储值卡余额");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("会员剩余积分");
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("累计会员总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("累计储值");
			cell.setCellStyle(style);
			
			cell = row.createCell(7);
			cell.setCellValue("储值卡累计总消费");
			cell.setCellStyle(style);
			
			row=sheet.createRow((int) reportUserList.size()+3);
			
		    
			
			cell = row.createCell(0);
			cell.setCellValue(String.valueOf(ru.getUserNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("￥"+df.format(ru.getRecharge()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("￥"+df.format(ru.getConsumption()));
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(r.getTotalBalance()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue(r.getTotalPoint());
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue(String.valueOf(r.getTotalUserNum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(6);
			cell.setCellValue("￥"+df.format(r.getTotalRecharge()));
			cell.setCellStyle(style);
			
			cell = row.createCell(7);
			cell.setCellValue("￥"+df.format(r.getTotalConsumption()));
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
