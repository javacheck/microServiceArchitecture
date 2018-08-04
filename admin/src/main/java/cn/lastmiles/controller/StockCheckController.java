package cn.lastmiles.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.StockCheckDetail;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StockCheckService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("stockCheck")
public class StockCheckController {
	private final static Logger logger = LoggerFactory.getLogger(StockCheckController.class);
	
	@Autowired
	private StockCheckService stockCheckService;
	
	@Autowired
	private StoreService storeService;
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/stockCheck/list";
	}
	
	/**
	 * 所有商品属性列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "stockCheck/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(String checkedName,String createdTime,Long storeId, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if( null != storeId ){
			storeIdString.append(storeId);
		}else{
			storeIdString.append(SecurityUtils.getAccountStoreId());
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		model.addAttribute("data", stockCheckService.findAll(storeIdString.toString(),checkedName, createdTime,page));
		return "stockCheck/list-data";
		
	}
	//详情页
	@RequestMapping(value="detailsList/{id}" )
	public String detailsList(@PathVariable Long  id, Model model){
		model.addAttribute("stockCheckId", id);
		return "stockCheck/detailsList";
	}
	@RequestMapping("detailsList/detailsList-data")
	public String detailsListData(String productName,String barCode,Long stockCheckId, Integer sort,Page page, Model model) {
		logger.debug("stockCheckId={}",stockCheckId);
		model.addAttribute("data", stockCheckService.finddetailsAll(stockCheckId,productName, barCode,sort,page));
		return "stockCheck/detailsList-data";
		
	}
	// 弹出商家列表
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
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "stockCheck/showModelList-data";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response) throws ParseException {
		
		
		String fileName = "model";
		List<ProductStock> productStockList = stockCheckService.findAllBySearch(SecurityUtils.getAccountStoreId().toString());
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("盘点数据");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setLocked(true);
		HSSFCell cell = null;
		for(int i=0;i<9;i++){
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
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("商品分类");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("库存数量");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("盘点库存");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("盘点人");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("盘点时间");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",productStockList.size());
		if (!productStockList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",productStockList.size());
			for (int i = 0; i < productStockList.size(); i++) {// 第二行开始
				ProductStock ps=productStockList.get(i);
				row = sheet.createRow((int) i + 1);
				
				
				row.createCell(0).setCellValue(ps.getStoreName());//商家
				row.createCell(1).setCellValue(ps.getAttributeName());//商品名称	
				row.createCell(2).setCellValue(ps.getBarCode());//条码
				row.createCell(3).setCellValue(ps.getCategoryName());//分类名称
				
				row.createCell(4).setCellValue(ps.getStock());//库存数量
				
				//新增的四句话，设置CELL格式为文本格式  
				HSSFCellStyle cellStyle = wb.createCellStyle();  
				HSSFDataFormat format = wb.createDataFormat();  
				cellStyle.setDataFormat(format.getFormat("@"));  
				cell=row.createCell(7);
				cell.setCellStyle(cellStyle); 
				cell.setCellValue("");

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
	
	@RequestMapping(value="uploadPage/showMode" )
	public String uploadPage(Model model){
		return "stockCheck/stockCheckUpload";
	}
	
	@RequestMapping(value = "uploadStockCheck",method = RequestMethod.POST)
	public String uploadStockCheck(@RequestParam("stockCheckFile") MultipartFile stockCheckFile,RedirectAttributes redirectAttributes, Model model) throws IOException {
		logger.debug("stockCheckFile.getOriginalFilename()={}",stockCheckFile.getOriginalFilename());
		Long storeId=SecurityUtils.getAccountStoreId();
		Long accountId=SecurityUtils.getAccountId();
		
		try {
			InputStream in = stockCheckFile.getInputStream();
			String flag=stockCheckService.uploadFile(in,storeId,accountId);
			logger.debug("flag="+flag);
			redirectAttributes.addFlashAttribute("action", flag);
		}catch(Exception e){
			logger.error("",e);
		}
		return "redirect:/stockCheck/list";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/detailslist-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findDetailsListAllBySearch(Long stockCheckId,String productName,String barCode,HttpServletResponse response) throws ParseException {
		
		logger.debug("stockCheckId={},productName={},barCode={}",stockCheckId,productName,barCode);
		String fileName = "库存盘点详情";
		List<StockCheckDetail> scdList = stockCheckService.findDetailsListAllBySearch(stockCheckId,productName,barCode);
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("库存盘点详情");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = null;
		for(int i=0;i<14;i++){
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
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("商品分类");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("库存数量");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("盘点库存");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("盘盈");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("盘亏");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("盘点人");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("盘点时间");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",scdList.size());
		if (!scdList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",scdList.size());
			for (int i = 0; i < scdList.size(); i++) {// 第二行开始
				StockCheckDetail scd=scdList.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(scd.getStoreName());//商家
				row.createCell(1).setCellValue(scd.getProductName());//商品名称	
				row.createCell(2).setCellValue(scd.getBarCode());//条码
				row.createCell(3).setCellValue(scd.getCategoryName());//分类名称
				row.createCell(4).setCellValue(scd.getStock());//库存数量
				row.createCell(5).setCellValue(scd.getCheckedStock());//盘点库存
				if(scd.getInventoryProfit()==null){//盘盈
					row.createCell(6).setCellValue("");
				}else{
					row.createCell(6).setCellValue(scd.getInventoryProfit());
				}
				if(scd.getInventoryLoss()==null){//盘亏
					row.createCell(7).setCellValue("");
				}else{
					row.createCell(7).setCellValue(scd.getInventoryLoss());
				}
				row.createCell(8).setCellValue(scd.getCheckedName());//盘点人
				row.createCell(9).setCellValue(scd.getCheckedTime());//盘点时间
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
