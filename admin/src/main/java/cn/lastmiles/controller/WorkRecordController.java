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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import cn.lastmiles.bean.WorkRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.service.WorkRecordService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("/workrecord")
public class WorkRecordController {
	private final static Logger logger = LoggerFactory.getLogger(WorkRecordController.class);
	@Autowired
	private WorkRecordService workRecordService;

	@RequestMapping("list")
	public String list() {
		return "workrecord/list";
	}

	@RequestMapping("list-data")
	public String listData(Page page, Model model, String accountName,
			String accountMobile, String startDate, String endDate) {
		
		model.addAttribute("page", workRecordService.list(accountName,
				accountMobile, startDate, endDate,
				SecurityUtils.getAccountStoreId(), page));
		return "workrecord/list-data";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list/ajax/workrecordList-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findWorkrecordListBySearch(String accountName,
			String accountMobile, String startDate, String endDate,HttpServletResponse response) throws ParseException {
		
		Page page=new Page();
		page.setIsOnePage();
		String fileName = "model";
		List<WorkRecord> workRecordList = (List<WorkRecord>) workRecordService.list(accountName,
				accountMobile, startDate, endDate,
				SecurityUtils.getAccountStoreId(), page).getData();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("交接班记录");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setLocked(true);
		HSSFCell cell = null;
		for(int i=0;i<17;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("编号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("手机号");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("姓名");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("开始时间");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("交班时间");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("订单数");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("总金额");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("支付宝交易量");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("支付宝金额");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("微信交易量");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell(i);// 第一行第11格
				cell.setCellValue("微信金额");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell(i);// 第一行第12格
				cell.setCellValue("刷卡交易量");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell(i);// 第一行第13格
				cell.setCellValue("刷卡金额");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell(i);// 第一行第14格
				cell.setCellValue("现金交易量");
				cell.setCellStyle(style);
			}else if(i==14){
				cell = row.createCell(i);// 第一行第15格
				cell.setCellValue("现金金额");
				cell.setCellStyle(style);
			}else if(i==15){
				cell = row.createCell(i);// 第一行第15格
				cell.setCellValue("会员卡交易量");
				cell.setCellStyle(style);
			}else if(i==16){
				cell = row.createCell(i);// 第一行第15格
				cell.setCellValue("会员卡金额");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",workRecordList.size());
		if (!workRecordList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",workRecordList.size());
			for (int i = 0; i < workRecordList.size(); i++) {// 第二行开始
				WorkRecord workRecord=workRecordList.get(i);
				row = sheet.createRow((int) i + 1);
				
				
				row.createCell(0).setCellValue(workRecord.getId());//编号
				row.createCell(1).setCellValue(workRecord.getAccountMobile());//手机号	
				row.createCell(2).setCellValue(workRecord.getAccountName());//姓名
				
				HSSFCell startDateCell = row.createCell(3);
				startDateCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				startDateCell.setCellValue(DateUtils.format(workRecord.getStartDate(), "yyyy-MM-dd HH:mm:ss"));//开始时间
				row.createCell(4).setCellValue(workRecord.getEndDate()==null?"":DateUtils.format(workRecord.getEndDate(), "yyyy-MM-dd HH:mm:ss"));//交班时间
				row.createCell(5).setCellValue(workRecord.getTotalNum());//交易量
				row.createCell(6).setCellValue(df.format(workRecord.getSales()));//总金额
				
				row.createCell(7).setCellValue(workRecord.getAlipayNum());//支付宝交易量
				row.createCell(8).setCellValue(df.format(workRecord.getAlipay()));//支付宝金额
				
				row.createCell(9).setCellValue(workRecord.getWxPayNum());//微信交易量
				row.createCell(10).setCellValue(df.format(workRecord.getWxPay()));//微信金额
				
				row.createCell(11).setCellValue(workRecord.getBankCardPayNum());//刷卡交易量
				row.createCell(12).setCellValue(df.format(workRecord.getBankCardPay()));//刷卡金额
				
				row.createCell(13).setCellValue("");//现金交易量
				row.createCell(14).setCellValue(df.format(workRecord.getCashPay()));//现金金额
				
				row.createCell(15).setCellValue(workRecord.getUserCardNum());//
				row.createCell(16).setCellValue(df.format(workRecord.getUserCard()));
				
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
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
