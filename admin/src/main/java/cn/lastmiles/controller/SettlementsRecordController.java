package cn.lastmiles.controller;
/**
 * updateDate : 2015-07-16 PM 18:37
 */
import java.io.OutputStream;
import java.net.URLEncoder;
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

import cn.lastmiles.bean.SettlementsRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.SettlementsRecordService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("settlementsRecord")
/**
 * 账户流水记录
 */
public class SettlementsRecordController {
	private final static Logger logger = LoggerFactory
			.getLogger(SettlementsRecordController.class);
	@Autowired
	private SettlementsRecordService settlementsRecordService;
	@RequestMapping("")
	public String index(){

		if( SecurityUtils.isStore() ){
			return "settlementsRecord/storeList";
			
		} else if(SecurityUtils.isAgent()){
			return "settlementsRecord/agentList";
			
		}
		return "settlementsRecord/adminList";
	}
	
	/**
	 * 账户流水记录列表
	 * @param model
	 * @return 根据登录的账号跳转到不同的处理页面
	 */
	@RequestMapping("list")
	public String list(Model model) {
		
		if( SecurityUtils.isStore() ){
			return "settlementsRecord/storeList";
			
		} else if(SecurityUtils.isAgent()){
			return "settlementsRecord/agentList";
			
		}
		return "settlementsRecord/adminList";
	}
	
	/**
	 * 账户流水详情搜索查询
	 * @param orderId 订单ID
	 * @param name 商家名称或者代理商名称
	 * @param type 0商户，1代理商
	 * @param startTime 开始时间(创建时间)
	 * @param endTime 结束时间(创建时间)
	 * @param page
	 * @param model
	 */
	@RequestMapping("list-data")
	public String listData(Long orderId,String name,Integer type,String startTime,String endTime,Page page, Model model) {
		Long ownerId = null;
		name = (null == name) ? null : name.trim();
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
			page = settlementsRecordService.getSettlementsRecord(ownerId, type, orderId,name, startTime, endTime, page);
			model.addAttribute("data", page);				
			return "settlementsRecord/List-data";
			
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
			
			page = settlementsRecordService.getSettlementsRecord(ownerId, type, orderId,name, startTime, endTime, page);
			model.addAttribute("data", page);
			return "settlementsRecord/List-data";	
			
		} else {
			// 管理员 --
			if( type.equals(Constants.Type.ALl) ){ // 查询所有类型
				page = settlementsRecordService.getSettlementsRecord(ownerId, orderId,name, startTime, endTime, page);				
			} else { // 根据类型条件查询
				page = settlementsRecordService.getSettlementsRecord(ownerId, type, orderId,name, startTime, endTime, page);
			}
			model.addAttribute("data", page);
			return "settlementsRecord/adminList-data";
		}
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
			Long orderId,String name,Integer type,String startTime,String endTime) throws ParseException {
		
		String fileName = "订单列表";
		logger.debug("orderId={},name={},type={}",orderId,name,type);
		List<SettlementsRecord> settlementsRecords=null;
		Long ownerId = null;
		
		if( SecurityUtils.isStore() ){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_STORE;
			ownerId = SecurityUtils.getAccountStoreId();
			
			settlementsRecords = settlementsRecordService.findAllBySearch(ownerId, type, orderId,name, startTime, endTime);
			
		} else if(SecurityUtils.isAgent()){
			type = cn.lastmiles.constant.Constants.PayAccount.PAY_ACCOUNT_TYPE_AGENT;
			ownerId = SecurityUtils.getAccountAgentId();
			
			settlementsRecords = settlementsRecordService.findAllBySearch(ownerId, type, orderId,name, startTime, endTime);
			
		} else {
			// 管理员 --
			if( type.equals(Constants.Type.ALl) ){ // 查询所有类型
				settlementsRecords = settlementsRecordService.findAllBySearch(ownerId, orderId,name, startTime, endTime);				
			} else { // 根据类型条件查询
				settlementsRecords = settlementsRecordService.findAllBySearch(ownerId, type, orderId,name, startTime, endTime);
			}
		}
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<7;i++){
			if(i==0){
				cell = row.createCell((short)i);// 第一行第1格
				cell.setCellValue("流水单号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell((short)i);// 第一行第2格
				cell.setCellValue("订单号");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell((short)i);// 第一行第3格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell((short)i);// 第一行第4格
				cell.setCellValue("创建时间");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell((short)i);// 第一行第5格
				cell.setCellValue("户主类型");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell((short)i);// 第一行第6格
				cell.setCellValue("户主名称");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell((short)i);// 第一行第7格
				cell.setCellValue("金额(元)");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",settlementsRecords.size());
		if (!settlementsRecords.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",settlementsRecords.size());
			for (int i = 0; i < settlementsRecords.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				SettlementsRecord settlementsRecord=settlementsRecords.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(settlementsRecord.getId());//流水单号
				row.createCell(1).setCellValue(settlementsRecord.getOrderId());//订单编号
				row.createCell(2).setCellValue(settlementsRecord.getStore().getName());//商家名称
				row.createCell(3).setCellValue(DateUtils.format(settlementsRecord.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				if(settlementsRecord.getType().intValue()==0){
					row.createCell(4).setCellValue("商家");//户主类型
				}else if(settlementsRecord.getType().intValue()==1){
					row.createCell(4).setCellValue("代理商");//户主类型
				}else{
					row.createCell(4).setCellValue("平台");//户主类型
				}
				if(settlementsRecord.getNameS()==null){
					row.createCell(5).setCellValue("平台");//户主名称
				}else{
					row.createCell(5).setCellValue(settlementsRecord.getNameS());//户主名称
				}
				row.createCell(6).setCellValue(settlementsRecord.getAmountString());//金额(元)
			}
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
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
	}
}