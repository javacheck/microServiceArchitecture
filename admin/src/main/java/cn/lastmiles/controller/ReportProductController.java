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

import cn.lastmiles.bean.ReportProduct;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ReportProductService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("reportProduct")
public class ReportProductController {
	private final static Logger logger = LoggerFactory.getLogger(ReportProductController.class);
	
	@Autowired
	private ReportProductService reportProductService;
	@Autowired 
	private StoreService storeService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/reportProduct/list";
	}
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * 商品销售统计列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		logger.debug("isMainStore={}",SecurityUtils.isMainStore());
		return "reportProduct/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Long storeId,String name,Integer type,Integer source,Integer sort,String beginTime,String endTime,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("name={}",name);
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findAllEntryChildrenStore(SecurityUtils.getAccountStoreId());
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
		model.addAttribute("data", reportProductService.findAllPage(storeIdString.toString(),name,type,source,sort,beginTime, endTime,page));
		model.addAttribute("reportSum", reportProductService.findAllSum(storeIdString.toString(),name,type,source,beginTime, endTime));
		model.addAttribute("reportStockSum", reportProductService.findStockMunSum(storeIdString.toString(),name,type,source,beginTime, endTime));
		model.addAttribute("reportStoreSum", reportProductService.findStoreMunSum(storeIdString.toString(),name,type,source,beginTime, endTime));
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		if(type.intValue()==0){//按商品统计
			return "reportProduct/stockList-data";
		}else if(type.intValue()==1){//按分类
			return "reportProduct/categoryList-data";
		}else{//按品牌
			return "reportProduct/brandList-data";
		}
		
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
		return "reportProduct/showModelList-data";
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
			Long storeId,String name,Integer type,Integer source,Integer sort,String beginTime,String endTime,Page page) throws ParseException {
		page.setIsOnePage();
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("type={}",type);
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
		String fileName = "商品销量统计";
		logger.debug("storeId={},source={},beginTime={},endTime={},name={}",storeId,source,beginTime,endTime,name);
		@SuppressWarnings("unchecked")
		List<ReportProduct> reportProductList = (List<ReportProduct>) reportProductService.findAllPage(storeIdString.toString(),name,type,source,sort,beginTime, endTime,page).getData();
		if(reportProductList.isEmpty()){
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
				//wb.write(os);
				os.flush();
				// 关闭输出流
				os.close();
				//wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		ReportProduct rp=reportProductService.findAllSum(storeIdString.toString(),name,type,source,beginTime, endTime);
		ReportProduct storeSum=reportProductService.findStoreMunSum(storeIdString.toString(),name,type,source,beginTime, endTime);
		ReportProduct stockSum=reportProductService.findStockMunSum(storeIdString.toString(),name,type,source,beginTime, endTime);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("商品销量统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		if(type.intValue()==0){
			for(int i=0;i<12;i++){
				if(i==0){
					cell = row.createCell(i);// 第一行第1格
					cell.setCellValue("商家名称");
					cell.setCellStyle(style);
				}else if(i==1){
					cell = row.createCell(i);// 第一行第2格
					cell.setCellValue("商品名称");
					cell.setCellStyle(style);
				}else if(i==2){
					cell = row.createCell(i);// 第一行第3格
					cell.setCellValue("规格");
					cell.setCellStyle(style);
				}else if(i==3){
					cell = row.createCell(i);// 第一行第4格
					cell.setCellValue("商品条码");
					cell.setCellStyle(style);
				}else if(i==4){
					cell = row.createCell(i);// 第一行第5格
					cell.setCellValue("商品分类");
					cell.setCellStyle(style);
				}else if(i==5){
					cell = row.createCell(i);// 第一行第6格
					cell.setCellValue("品牌名称");
					cell.setCellStyle(style);
				}else if(i==6){
					cell = row.createCell(i);// 第一行第7格
					cell.setCellValue("销量");
					cell.setCellStyle(style);
				}else if(i==7){
					cell = row.createCell(i);// 第一行第8格
					cell.setCellValue("进货价");
					cell.setCellStyle(style);
				}else if(i==8){
					cell = row.createCell(i);// 第一行第9格
					cell.setCellValue("销售单价");
					cell.setCellStyle(style);
				}else if(i==9){
					cell = row.createCell(i);// 第一行第10格
					cell.setCellValue("销售成本");
					cell.setCellStyle(style);
				}else if(i==10){
					cell = row.createCell(i);// 第一行第11格
					cell.setCellValue("参考销售额");
					cell.setCellStyle(style);
				}else if(i==11){
					cell = row.createCell(i);// 第一行第12格
					cell.setCellValue("参考毛利");
					cell.setCellStyle(style);
				}
			}
			logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportProductList.size());
			if (!reportProductList.isEmpty()) {
				for (int i = 0; i < reportProductList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
					ReportProduct reportProduct=reportProductList.get(i);
					row = sheet.createRow((int) i + 1);
					row.createCell(0).setCellValue(reportProduct.getStoreName());//商家名称
					row.createCell(1).setCellValue(reportProduct.getProductName());//商品名称
					row.createCell(2).setCellValue(reportProduct.getAttributeValues());//规格
					row.createCell(3).setCellValue(reportProduct.getBarCode()==null?"":reportProduct.getBarCode());//商品条码
					row.createCell(4).setCellValue(reportProduct.getCategoryName());//商品分类
					row.createCell(5).setCellValue(reportProduct.getBrandName());//品牌名称
					row.createCell(6).setCellValue(reportProduct.getSalesNum());//销量
					row.createCell(7).setCellValue("￥"+df.format(reportProduct.getCostPrice()));//进货价
					row.createCell(8).setCellValue("￥"+df.format(reportProduct.getPrice()));//销售单价
					row.createCell(9).setCellValue("￥"+df.format(reportProduct.getTotalCostPrice()));//销售成本
					row.createCell(10).setCellValue("￥"+df.format(reportProduct.getTotalPrice()));//销售额
					row.createCell(11).setCellValue("￥"+df.format(reportProduct.getTotalGrossProfit()));//参考毛利
					
				}
			}
		}else if(type.intValue()==1){
			for(int i=0;i<6;i++){
				if(i==0){
					cell = row.createCell(i);// 第一行第1格
					cell.setCellValue("商家名称");
					cell.setCellStyle(style);
				}else if(i==1){
					cell = row.createCell(i);// 第一行第2格
					cell.setCellValue("商品分类");
					cell.setCellStyle(style);
				}else if(i==2){
					cell = row.createCell(i);// 第一行第3格
					cell.setCellValue("销量");
					cell.setCellStyle(style);
				}else if(i==3){
					cell = row.createCell(i);// 第一行第4格
					cell.setCellValue("销售成本");
					cell.setCellStyle(style);
				}else if(i==4){
					cell = row.createCell(i);// 第一行第5格
					cell.setCellValue("参考销售额");
					cell.setCellStyle(style);
				}else if(i==5){
					cell = row.createCell(i);// 第一行第6格
					cell.setCellValue("参考毛利");
					cell.setCellStyle(style);
				}
			}
			logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportProductList.size());
			if (!reportProductList.isEmpty()) {
				for (int i = 0; i < reportProductList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
					ReportProduct reportProduct=reportProductList.get(i);
					row = sheet.createRow((int) i + 1);
					row.createCell(0).setCellValue(reportProduct.getStoreName());//商家名称
					row.createCell(1).setCellValue(reportProduct.getCategoryName());//商品分类
					row.createCell(2).setCellValue(reportProduct.getSalesNum());//销量
					row.createCell(3).setCellValue("￥"+df.format(reportProduct.getTotalCostPrice()));//销售成本
					row.createCell(4).setCellValue("￥"+df.format(reportProduct.getTotalPrice()));//销售额
					row.createCell(5).setCellValue("￥"+df.format(reportProduct.getTotalGrossProfit()));//参考毛利
					
				}
			}
		}else{
			for(int i=0;i<6;i++){
				if(i==0){
					cell = row.createCell(i);// 第一行第1格
					cell.setCellValue("商家名称");
					cell.setCellStyle(style);
				}else if(i==1){
					cell = row.createCell(i);// 第一行第2格
					cell.setCellValue("商品品牌");
					cell.setCellStyle(style);
				}else if(i==2){
					cell = row.createCell(i);// 第一行第3格
					cell.setCellValue("销量");
					cell.setCellStyle(style);
				}else if(i==3){
					cell = row.createCell(i);// 第一行第4格
					cell.setCellValue("销售成本");
					cell.setCellStyle(style);
				}else if(i==4){
					cell = row.createCell(i);// 第一行第5格
					cell.setCellValue("参考销售额");
					cell.setCellStyle(style);
				}else if(i==5){
					cell = row.createCell(i);// 第一行第6格
					cell.setCellValue("参考毛利");
					cell.setCellStyle(style);
				}
			}
			logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportProductList.size());
			if (!reportProductList.isEmpty()) {
				for (int i = 0; i < reportProductList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
					ReportProduct reportProduct=reportProductList.get(i);
					row = sheet.createRow((int) i + 1);
					row.createCell(0).setCellValue(reportProduct.getStoreName());//商家名称
					row.createCell(1).setCellValue(reportProduct.getBrandName());//商品品牌
					row.createCell(2).setCellValue(reportProduct.getSalesNum());//销量
					row.createCell(3).setCellValue("￥"+df.format(reportProduct.getTotalCostPrice()));//销售成本
					row.createCell(4).setCellValue("￥"+df.format(reportProduct.getTotalPrice()));//销售额
					row.createCell(5).setCellValue("￥"+df.format(reportProduct.getTotalGrossProfit()));//参考毛利
					
				}
			}
		}
			row=sheet.createRow((int) reportProductList.size()+2);
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("商家总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("商品总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("总销量");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("销售总成本");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("参考销售总额");
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("参考总毛利");
			cell.setCellStyle(style);
			
			row=sheet.createRow((int) reportProductList.size()+3);
			
		    cell = row.createCell(0);
			cell.setCellValue(String.valueOf(storeSum.getStoreSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue(String.valueOf(stockSum.getStockSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue(rp.getSalesNum());
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(rp.getTotalCostPrice()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("￥"+df.format(rp.getTotalPrice()));
			cell.setCellStyle(style);
			
			cell = row.createCell(5);
			cell.setCellValue("￥"+df.format(rp.getTotalGrossProfit()));
			cell.setCellStyle(style);
		
		
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
