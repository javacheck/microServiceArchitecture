/**
 * createDate : 2016年5月11日上午11:33:44
 */
package cn.lastmiles.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.MessageRecharge;
import cn.lastmiles.bean.MessageSaleRecord;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.MessageRechargeService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("messageRecharge")
public class MessageRechargeController {

	private static final Logger logger = LoggerFactory.getLogger(MessageRechargeController.class);
	@Autowired
	private MessageRechargeService messageRechargeService;
	
	@RequestMapping("list")
	public String list(){
		return "messageRecharge/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String storeName,Model model,Page page){
		logger.debug("search storeName is {}",storeName);
		model.addAttribute("data",messageRechargeService.list(storeName,page));
		return "messageRecharge/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(){
		return "/messageRecharge/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(MessageRecharge messageRecharge,Long[] storeIdArray) {
		
		logger.debug("messageRecharge is {}  ,  storeIdArray is {}",messageRecharge,storeIdArray);
		messageRecharge.setAccountId(SecurityUtils.getAccountId());
		messageRechargeService.save(messageRecharge, storeIdArray);	
		return "redirect:/messageRecharge/list";
	}
	
	
	@RequestMapping(value="ajax/checkOperationPassword",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int checkOperationPassword(String operationPassword){
		logger.debug("operationPassword is {} ",operationPassword);
		
		String encryptedPassword = SecurityUtils.getAccount().getPassword();
		if(PasswordUtils.checkPassword(operationPassword, encryptedPassword)){
			return 0;
		};
		return 1;
	}
	
	// 商家弹窗
	@RequestMapping("showStoreModel/list/list-data")
	public String showProductStockModelListData(String modelStoreName, Page page, Model model) {
		
		model.addAttribute("data", messageRechargeService.findStoreList(modelStoreName,page));			
		return "messageRecharge/showStoreModelList-data";
	}
	
	@RequestMapping(value="recharge/{storeId}" )
	public String recharge(@PathVariable Long storeId, Model model){
		logger.debug("recharge storeId is {}",storeId);
		model.addAttribute("accountIdList",messageRechargeService.findAccountList());
		model.addAttribute("storeId",storeId);
		return "messageRecharge/rechargeRecordList";
	}
		
	@RequestMapping("recharge/record/list-data")
	public String recordListData(Long storeId,Long accountId,String searchTime,Model model,Page page){
		logger.debug("search storeId is {}  , accountId is {} , searchTime is {}",storeId,accountId,searchTime);
		
		model.addAttribute("data",messageRechargeService.list(storeId,accountId,searchTime,page));
		return "messageRecharge/recharge-record-list-data";
	}
	
	@RequestMapping(value="returnList" )
	public String returnList(){
		return "redirect:/messageRecharge/list";
	}
	
	@RequestMapping(value="sale/{storeId}" )
	public String sale(@PathVariable Long storeId, Model model){
		logger.debug("sale storeId is {}",storeId);
		model.addAttribute("storeId",storeId);
		return "messageRecharge/saleRecordList";
	}
		
	@RequestMapping("sale/record/list-data")
	public String saleRecordListData(Long storeId,String userAccount,Integer type,String searchTime,Model model,Page page){
		logger.debug("search storeId is {}  ,userAccount is {} , type is {} , searchTime is {}",storeId,userAccount,type,searchTime);
		
		model.addAttribute("data",messageRechargeService.list(storeId,userAccount,type,searchTime,page));
		return "messageRecharge/sale-record-list-data";
	}
	
	@RequestMapping(value = "ajax/reportMessageRechargeSaleToExcel")
	public String reportMessageRechargeSaleToExcel(HttpServletResponse response,Long storeId, String userAccount,Integer type,String searchTime) {
		List<MessageSaleRecord> messageSaleRecordList = messageRechargeService.list(storeId,userAccount,type,searchTime);
		String fileOrTitleName = "消费记录列表";
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
//			response.setContentType("application/vnd.ms-excel"); 
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			//暂不考虑大数据量的问题和Excel版本的问题(2015.12.16)
			exportExcel(workbook,fileOrTitleName,getHeaders(),messageSaleRecordList);
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * EXCEL表头
	 * @return
	 */
	public String[] getHeaders() {  
        return new String[] { "会员帐号","姓名","类型","消费数量","消费时间","发送时间","信息内容" };  
	} 
	
	public void exportExcel(HSSFWorkbook workbook, String title, String[] headers, Collection<?> dataset) {
	     
	      HSSFSheet sheet = workbook.createSheet(title);
	      sheet.setDefaultColumnWidth(20);
	      HSSFCellStyle style = workbook.createCellStyle();
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	      
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (int i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      
	    if(!dataset.isEmpty()){
	    	Map<Integer,String> typeMap = getTypeMap();
	    	Iterator<?> it = dataset.iterator();
	    	int index = 0;
	    	while(it.hasNext()){
	    		index++;
	    		row = sheet.createRow(index);
	    		row.setRowStyle(style);
	    		MessageSaleRecord messageSaleRecord = (MessageSaleRecord) it.next();
	    		// "会员帐号","姓名","类型","消费数量","消费时间","发送时间","信息内容"
	    		row.createCell(0).setCellValue( messageSaleRecord.getUserAccountMobile() ); // 订单编号
	    		row.createCell(1).setCellValue(messageSaleRecord.getName()); // 会员号
	    		row.createCell(2).setCellValue(typeMap.get(messageSaleRecord.getType())); // 交易类型
	    		row.createCell(3).setCellValue(messageSaleRecord.getSaleNumber()); // 实收金额
	    		row.createCell(4).setCellValue(DateUtils.format(messageSaleRecord.getCreatedTime(),"yyyy-MM-dd HH:mm:ss")); // 实收金额
	    		row.createCell(5).setCellValue(DateUtils.format(messageSaleRecord.getSendTime(),"yyyy-MM-dd HH:mm:ss")); // 积分
	    		row.createCell(6).setCellValue(messageSaleRecord.getMessageContent()); // 剩余积分
	    	}
	    	logger.debug("共导出消费记录数据:{}条",index);
	    	index = 0;
	    }
	}
	
	public Map<Integer,String> getTypeMap(){
		Map<Integer,String> typeMap = new HashMap<Integer, String>();
		typeMap.put(1, "通知");
		return typeMap;
	}
}