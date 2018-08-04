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

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ReportSales;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.service.ReportProductService;
import cn.lastmiles.service.ReportSalesService;
import cn.lastmiles.service.ReportUserService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("operationSummary")
public class OperationSummaryController {
	private final static Logger logger = LoggerFactory.getLogger(OperationSummaryController.class);
	
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ReportSalesService reportSalesService;
	
	@Autowired
	private ReportProductService reportProductService;
	
	@Autowired
	private ReportUserService reportUserService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/operationSummary/list";
	}
	
	/**
	 * 经营汇总表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Long storeId,String storeName,Integer source,Model model) {
		if(storeId!=null){
			model.addAttribute("storeIdCashe",storeId);
			model.addAttribute("storeNameCashe",storeName);
		}
		if(source!=null){
			model.addAttribute("sourceCashe",source);
		}
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("storeId={}",SecurityUtils.isMainStore());
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
		logger.debug("storeId={}",storeIdString.toString());
		
		//type 0当天 1本月 2上月 3今年
		//销售统计
		model.addAttribute("reportDateSales", reportSalesService.findReportSalesSum(storeIdString.toString(),source,0));
		model.addAttribute("reportThisMonthSales", reportSalesService.findReportSalesSum(storeIdString.toString(),source,1));
		model.addAttribute("reportLastMonthSales", reportSalesService.findReportSalesSum(storeIdString.toString(),source,2));
		model.addAttribute("reportYearSales", reportSalesService.findReportSalesSum(storeIdString.toString(),source,3));
		//会员统计
		model.addAttribute("reportDateUser", reportUserService.findReportSalesSum(storeIdString.toString(),0));
		model.addAttribute("reportThisMonthUser", reportUserService.findReportSalesSum(storeIdString.toString(),1));
		model.addAttribute("reportLastMonthUser", reportUserService.findReportSalesSum(storeIdString.toString(),2));
		model.addAttribute("reportYearUser", reportUserService.findReportSalesSum(storeIdString.toString(),3));
		//库存统计
		model.addAttribute("reportProductStock", productService.findProductStockSum(storeIdString.toString()));
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		boolean flag = SecurityUtils.isStore() ? SecurityUtils.isStore()^SecurityUtils.isMainStore() : false;
		model.addAttribute("isStore",flag);
		if(flag){
			model.addAttribute("isStoreId",SecurityUtils.getAccountStoreId());			
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		
		return "operationSummary/list";
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
		return "operationSummary/showModelList-data";
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
			Long storeId,Integer source) throws ParseException {
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
		String fileName = "经营总汇表";
		logger.debug("storeId={}",storeId);
		//销售统计
		ReportSales drs=reportSalesService.findReportSalesSum(storeIdString.toString(),source,0);
		ReportSales tmrs=reportSalesService.findReportSalesSum(storeIdString.toString(),source,1);
		ReportSales lmrs=reportSalesService.findReportSalesSum(storeIdString.toString(),source,2);
		ReportSales yrs=reportSalesService.findReportSalesSum(storeIdString.toString(),source,3);
		//会员统计
		ReportUser dru=reportUserService.findReportSalesSum(storeIdString.toString(),0);
		ReportUser tmru=reportUserService.findReportSalesSum(storeIdString.toString(),1);
		ReportUser lmru=reportUserService.findReportSalesSum(storeIdString.toString(),2);
		ReportUser yru=reportUserService.findReportSalesSum(storeIdString.toString(),3);
		//库存统计
		ProductStock ps=productService.findProductStockSum(storeIdString.toString());
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("经营总汇表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		
		cell = row.createCell(0);
		cell.setCellValue("销售统计概览");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 1);
		
		cell = row.createCell(0);
		cell.setCellValue("今日销售额");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月销售额");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月销售额");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年销售额");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 2);
		
		cell = row.createCell(0);
		cell.setCellValue("￥"+df.format(drs.getSalesNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("￥"+df.format(tmrs.getSalesNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("￥"+df.format(lmrs.getSalesNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("￥"+df.format(yrs.getSalesNumSum()));
		cell.setCellStyle(style);
		
		
		row=sheet.createRow((int) 3);
		
		cell = row.createCell(0);
		cell.setCellValue("今日销售毛利");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月销售毛利");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月销售毛利");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年销售毛利");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 4);
		
		cell = row.createCell(0);
		cell.setCellValue("￥"+df.format(drs.getGrossProfit()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("￥"+df.format(tmrs.getGrossProfit()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("￥"+df.format(lmrs.getGrossProfit()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("￥"+df.format(yrs.getGrossProfit()));
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 5);
		
		cell = row.createCell(0);
		cell.setCellValue("今日订单数");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月订单数");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月订单数");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年订单数");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 6);
		
		cell = row.createCell(0);
		cell.setCellValue(String.valueOf(drs.getOrderNumSum()));
		
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue(String.valueOf(tmrs.getOrderNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue(String.valueOf(lmrs.getOrderNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue(String.valueOf(yrs.getOrderNumSum()));
		cell.setCellStyle(style);
		
		/*row=sheet.createRow((int) 8);
		cell = row.createCell(0);
		cell.setCellValue("会员统计概览");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 9);
		
		cell = row.createCell(0);
		cell.setCellValue("今日新增会员");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月新增会员");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月新增会员");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年新增会员");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 10);
		
		cell = row.createCell(0);
		cell.setCellValue(String.valueOf(dru.getTotalUserNumSum()==null?"0":dru.getTotalUserNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue(String.valueOf(tmru.getTotalUserNumSum()==null?"0":tmru.getTotalUserNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue(String.valueOf(lmru.getTotalUserNumSum()==null?"0":lmru.getTotalUserNumSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue(String.valueOf(lmru.getTotalUserNumSum()==null?"0":lmru.getTotalUserNumSum()));
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 11);
		
		cell = row.createCell(0);
		cell.setCellValue("今日会员储值");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月会员储值");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月会员储值");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年会员储值");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 12);
		
		cell = row.createCell(0);
		cell.setCellValue("￥"+df.format(dru.getRechargeSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("￥"+df.format(tmru.getRechargeSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("￥"+df.format(lmru.getRechargeSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("￥"+df.format(yru.getRechargeSum()));
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 13);
		
		cell = row.createCell(0);
		cell.setCellValue("今日会员消费");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("本月会员消费");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("上月会员消费");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("今年会员消费");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 14);
		
		cell = row.createCell(0);
		cell.setCellValue("￥"+df.format(dru.getConsumptionSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("￥"+df.format(tmru.getConsumptionSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("￥"+df.format(lmru.getConsumptionSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("￥"+df.format(yru.getConsumptionSum()));
		cell.setCellStyle(style);
		*/
		row=sheet.createRow((int) 16);
		cell = row.createCell(0);
		cell.setCellValue("库存统计概览");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 17);
		
		cell = row.createCell(0);
		cell.setCellValue("商品总数");
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("商品总成本");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("预计销售额");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("预计毛利");
		cell.setCellStyle(style);
		
		row=sheet.createRow((int) 18);
		
		cell = row.createCell(0);
		cell.setCellValue(String.valueOf(ps.getStockSum()==null?"0":ps.getStockSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(1);
		cell.setCellValue("￥"+df.format(ps.getCostPriceSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("￥"+df.format(ps.getPriceSum()));
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		cell.setCellValue("￥"+df.format(ps.getGrossProfit()));
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
