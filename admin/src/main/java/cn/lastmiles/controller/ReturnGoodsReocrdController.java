/**
 * createDate : 2016年9月14日上午9:56:38
 */
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

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.OrderReturnGoods;
import cn.lastmiles.bean.OrderReturnGoodsRecord;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.OrderReturnGoodsService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@RequestMapping("returnGoodsRecord")
@Controller
public class ReturnGoodsReocrdController {
	private final static Logger logger = LoggerFactory.getLogger(ReturnGoodsReocrdController.class);
	
	@Autowired
	private OrderReturnGoodsService orderReturnGoodsService;
	@Autowired
	private StoreService storeService;
	
	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "returnGoodsRecord/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String storeId,String orderId,String productName,String barcode,Long categoryId,String beginTime,String endTime, Page page, Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		
		logger.debug("storeId is {},orderId is {},productName is {},categoryId is {}",storeId,orderId,productName,categoryId);
		
		StringBuilder storeIdString = new StringBuilder();
		if(SecurityUtils.isMainStore()){
			if( StringUtils.isBlank(storeId) ){ 
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				if( null != storeList && storeList.size() > 0 ){
					for (Store store : storeList) {
						if( storeIdString.length() > 0 ){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(SecurityUtils.getAccountStoreId());
				}
			} else {
				 
				List<Store> storeList = storeService.findByParentId(Long.parseLong(storeId));
				if( null != storeList && storeList.size() > 0 ){
					for (Store store : storeList) {
						if( ObjectUtils.equals(0L, store.getId()) ){
							continue;
						}
						if( storeIdString.length() > 0 ){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(storeId);
				}
							
			}
		} else {
			if(  StringUtils.isBlank(storeId) ){
				storeIdString.append(SecurityUtils.getAccountStoreId());				
			} else {
				storeIdString.append(storeId);
			}
		}
		model.addAttribute("data", orderReturnGoodsService.list(storeIdString.toString(),orderId,productName,barcode,categoryId,beginTime,endTime,page) );
		return "returnGoodsRecord/list-data";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,String storeId,String orderId,String productName,String barcode,Long categoryId,String beginTime,String endTime, Page page) {
		page.setIsOnePage();
		DecimalFormat    df   = new DecimalFormat("######0.00");
		logger.debug("storeId is {},orderId is {},productName is {},categoryId is {}",storeId,orderId,productName,categoryId);
		
		StringBuilder storeIdString = new StringBuilder();
		if(SecurityUtils.isMainStore()){
			if( StringUtils.isBlank(storeId) ){ 
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				if( null != storeList && storeList.size() > 0 ){
					for (Store store : storeList) {
						if( storeIdString.length() > 0 ){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(SecurityUtils.getAccountStoreId());
				}
			} else {
				 
				List<Store> storeList = storeService.findByParentId(Long.parseLong(storeId));
				if( null != storeList && storeList.size() > 0 ){
					for (Store store : storeList) {
						if( ObjectUtils.equals(0L, store.getId()) ){
							continue;
						}
						if( storeIdString.length() > 0 ){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(storeId);
				}
							
			}
		} else {
			if(  StringUtils.isBlank(storeId) ){
				storeIdString.append(SecurityUtils.getAccountStoreId());				
			} else {
				storeIdString.append(storeId);
			}
		}
		@SuppressWarnings("unchecked")
		List<OrderReturnGoodsRecord> orderReturnGoodsRecordList=(List<OrderReturnGoodsRecord>) orderReturnGoodsService.findAll(storeIdString.toString(),orderId,productName,barcode,categoryId,beginTime,endTime,page).getData();
		String fileName = "退货记录";
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("退货记录");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<12;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("订单编号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("规格");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("商品分类");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("销售价");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("退货数量");
				cell.setCellStyle(style);
			}else if (i == 8){
				cell = row.createCell(i);// 第一行第16格
				cell.setCellValue("退款金额");
				cell.setCellStyle(style);
			}else if (i == 9){
				cell = row.createCell(i);// 第一行第16格
				cell.setCellValue("退款方式");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("退款时间");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("操作人");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",orderReturnGoodsRecordList.size());
		if (!orderReturnGoodsRecordList.isEmpty()) {
			for (int i = 0; i < orderReturnGoodsRecordList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				OrderReturnGoodsRecord orgr=orderReturnGoodsRecordList.get(i);
						row = sheet.createRow((int) i+1);
						row.createCell(0).setCellValue(orgr.getOrderId());//订单编号
						row.createCell(1).setCellValue(orgr.getStoreName());//商家名称	
						row.createCell(2).setCellValue(orgr.getProductName());//商品名称
						
						row.createCell(3).setCellValue(orgr.getBarcode());//商品条码
						
						row.createCell(4).setCellValue(orgr.getStandard()==null?"":orgr.getStandard());//规格standard
						
						row.createCell(5).setCellValue(orgr.getCategoryName());//商品分类
						
						row.createCell(6).setCellValue("￥"+df.format(orgr.getPrice()));//销售价
						
						row.createCell(7).setCellValue(orgr.getReturnNumber());//退货数量
						row.createCell(8).setCellValue("￥"+df.format(orgr.getReturnPrice()));//退款金额
						if(orgr.getType().intValue()==0){
							row.createCell(9).setCellValue("退储值");//退款方式
						}else{
							row.createCell(9).setCellValue("退现金");//退款方式
						}
						row.createCell(10).setCellValue(DateUtils.format(orgr.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//退款时间
						
						
						row.createCell(11).setCellValue(orgr.getOperationName());//操作人operationName
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
