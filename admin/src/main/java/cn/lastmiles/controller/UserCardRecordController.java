package cn.lastmiles.controller;
/**
 * createDate : 2016年3月19日上午10:41:35
 */
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.StoreServicePackageService;
import cn.lastmiles.service.UserAccountManagerService;
import cn.lastmiles.service.UserstoreservicepackageService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("userCardRecord")
public class UserCardRecordController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserCardRecordController.class);
	@Autowired
	private StoreService storeService;
	@Autowired
	private UserAccountManagerService userAccountManagerService;
	@Autowired
	private StoreServicePackageService storeServicePackageService;
	@Autowired
	private UserstoreservicepackageService userStoreServicePackageService;
	@Autowired
	private IdService idService;
	
	@RequestMapping("list")
	public String list() {
		return "userCardRecord/list";
	}

	@RequestMapping("list-data")
	public String listData(Long orderId,String mobile,String cardNum,Integer type,String beginTime,String endTime, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
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
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		model.addAttribute("data", userAccountManagerService.userCardRecordlist(orderId,mobile,cardNum,type,beginTime,endTime,storeIdString.toString(), page));
		return "userCardRecord/list-data";
	}
	
	@RequestMapping(value = "ajax/reportUserCardRecordToExcel")
	public String reportUserCardRecordToExcel(HttpServletResponse response, String reportBeginTime,String reportEndTime,String reportMobile,Long reportOrderId,String reportCardNum,Integer reportType,Page page) {
		page.setIsOnePage();
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
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
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		@SuppressWarnings("unchecked")
		List<UserCardRecord> userCardRecordList =(List<UserCardRecord>) userAccountManagerService.userCardRecordlist(reportOrderId,reportMobile,reportCardNum,reportType,reportBeginTime,reportEndTime,storeIdString.toString(), page).getData();
		String fileOrTitleName = "卡交易记录列表";
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
//			response.setContentType("application/vnd.ms-excel"); 
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			//暂不考虑大数据量的问题和Excel版本的问题(2015.12.16)
			exportExcel(workbook,fileOrTitleName,getHeaders(),userCardRecordList);
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
        return new String[] { "订单编号","会员号","会员卡号","交易类型","实收金额","充值/消费","积分","剩余积分","发卡商家","日期","操作人员" };  
	} 
	
	public Map<Integer,String> getTypeMap(){
		Map<Integer,String> typeMap = new HashMap<Integer, String>();
		typeMap.put(Constants.UserCardRecord.TYPE_RECHARGE, "充值");
		typeMap.put(Constants.UserCardRecord.TYPE_CONSUMER, "消费");
		typeMap.put(Constants.UserCardRecord.TYPE_EXCHANGE, "积分兑换");
		typeMap.put(Constants.UserCardRecord.TYPE_DEDUCTION, "积分抵扣");
		typeMap.put(Constants.UserCardRecord.TYPE_WEIXIN, "微信公众号过来的积分");
		typeMap.put(Constants.UserCardRecord.TYPE_SERVICE_PACKAGE, "购买服务套餐");
		typeMap.put(Constants.UserCardRecord.TYPE_RETURNGOODS, "退换货");
		return typeMap;
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
//	    		"订单编号","会员号","会员卡号","交易类型","实收金额","充值/消费","积分","剩余积分","发卡商家","日期","操作人员" 
	    		UserCardRecord userCardRecord = (UserCardRecord) it.next();
	    		row.createCell(0).setCellValue( (null == userCardRecord.getOrderId()) ? 0L : userCardRecord.getOrderId() ); // 订单编号
	    		row.createCell(1).setCellValue(userCardRecord.getMobile()); // 会员号
	    		row.createCell(2).setCellValue(userCardRecord.getUserCard().getCardNum()); // 会员卡号
	    		row.createCell(3).setCellValue(typeMap.get(userCardRecord.getType())); // 交易类型
	    		row.createCell(4).setCellValue(userCardRecord.getAmount()); // 实收金额
	    		row.createCell(5).setCellValue(userCardRecord.getActualAmount()); // 实收金额
	    		row.createCell(6).setCellValue(userCardRecord.getPoint()); // 积分
	    		row.createCell(7).setCellValue(userCardRecord.getTotalPoint()); // 剩余积分
	    		row.createCell(8).setCellValue(userCardRecord.getUserCard().getStoreName()); // 发卡商家
	    		row.createCell(9).setCellValue(DateUtils.format(userCardRecord.getCreatedTime(),"yyyy-MM-dd HH:mm:ss")); // 创建时间
	    		row.createCell(10).setCellValue(userCardRecord.getUserCard().getAccountName()); // 操作人员
	    	}
	    	logger.debug("共导出会员卡记录数据:{}条",index);
	    	index = 0;
	    }
	}
}