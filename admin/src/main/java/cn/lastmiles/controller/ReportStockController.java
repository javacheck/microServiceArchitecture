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

import cn.lastmiles.bean.ReportStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ReportStockService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("reportStock")
public class ReportStockController {
	private final static Logger logger = LoggerFactory.getLogger(ReportUserController.class);

	@Autowired
	private ReportStockService reportStockService;
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/reportStock/list";
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
		return "reportStock/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long storeId,Integer sort,String name,String barCode,Page page, Model model) {
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
		// 判断登录者是商家且非总店(2016-01-19-update)
		logger.debug("storeId={},name={},barCode={},sort={}",storeIdString.toString(),name,barCode,sort);
		
		model.addAttribute("data", reportStockService.findAllPage(storeIdString.toString(),name,barCode,sort,page));
		model.addAttribute("reportSum", reportStockService.findAllSum(storeIdString.toString(),name,barCode));
		model.addAttribute("productSum", reportStockService.findProductSum(storeIdString.toString(),name,barCode));
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "reportStock/list-data";
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
		return "reportStock/showModelList-data";
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
			Long storeId,Integer sort,String name,String barCode,Page page) throws ParseException {
		page.setIsOnePage();
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
		String fileName = "库存统计";
		logger.debug("storeId={},name={},barCode={}",storeId,name,barCode);
		@SuppressWarnings("unchecked")
		List<ReportStock> reportStockList = (List<ReportStock>) reportStockService.findAllPage(storeIdString.toString(),name,barCode,sort,page).getData();
		ReportStock rs=reportStockService.findAllSum(storeIdString.toString(),name,barCode);
		ReportStock nameSum=reportStockService.findProductSum(storeIdString.toString(),name,barCode);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("库存统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		
		HSSFCell cell = null;
		for(int i=0;i<11;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("规格");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("商品分类");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("库存数量");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("进货价");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("销售单价");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("库存成本");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("预计销售额");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("预计毛利");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",reportStockList.size());
		if (!reportStockList.isEmpty()) {
			for (int i = 0; i < reportStockList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ReportStock reportStock=reportStockList.get(i);
				row = sheet.createRow((int) i + 1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				row.createCell(0).setCellValue(reportStock.getStoreName());//商家名称
				row.createCell(1).setCellValue(reportStock.getProductName());
				row.createCell(2).setCellValue(reportStock.getAttributeValues());
				row.createCell(3).setCellValue(reportStock.getBarCode());
				row.createCell(4).setCellValue(reportStock.getCategoryName());
				row.createCell(5).setCellValue(reportStock.getStock());
				
				row.createCell(6).setCellValue("￥"+df.format(reportStock.getCostPrice()));
				row.createCell(7).setCellValue("￥"+df.format(reportStock.getPrice()));
				row.createCell(8).setCellValue("￥"+df.format(reportStock.getTotalCostPrice()));
				row.createCell(9).setCellValue("￥"+df.format(reportStock.getPreSales()));
				row.createCell(10).setCellValue("￥"+df.format(reportStock.getPreGrossProfit()));
			}
			row=sheet.createRow((int) reportStockList.size()+2);
			
			
			
			cell = row.createCell(0);
			cell.setCellValue("商品总数");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue("库存总数量");
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("库存总成本");
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("预计总销售额");
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("预计总毛利");
			cell.setCellStyle(style);
			
			
			row=sheet.createRow((int) reportStockList.size()+3);
			
		    cell = row.createCell(0);
			cell.setCellValue(String.valueOf(nameSum.getNameSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue(String.valueOf(rs.getStockSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue("￥"+df.format(rs.getTotalCostPriceSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			cell.setCellValue("￥"+df.format(rs.getPreSalesSum()));
			cell.setCellStyle(style);
			
			cell = row.createCell(4);
			cell.setCellValue("￥"+df.format(rs.getPreGrossProfitSum()));
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
