package cn.lastmiles.v2.controller;

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
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.ProductStockService;
@Controller("cn.lastmiles.v2.controller.ProductStockController")
@RequestMapping("v2/productStock")
public class ProductStockController {
	private final static Logger logger = LoggerFactory.getLogger(ProductStockController.class);

	@Autowired
	private ProductStockService productStockService;
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	
	
	@RequestMapping("")
	public String index() {
		return "redirect:/v2/productStock/list";
	}
	
	/**
	 * 库存列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		logger.debug("isMainStore={}",SecurityUtils.isMainStore());
		return "v2/productStock/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long storeId,String name,Integer sort,String brandName,Long categoryId,String barCode,Integer alarmType,Integer shelves,Page page, Model model) {
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
		
		model.addAttribute("data", productStockService.findAllPage(storeIdString.toString(),name,barCode,sort,brandName,categoryId,alarmType,shelves,page));
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "v2/productStock/list-data";
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
	public String shopListData(Long storeId,String name, String mobile,Page page, Model model) {
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
		logger.debug("storeIdString.toString1()={}",storeIdString.toString());
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "v2/productStock/showModelList-data";
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
			Long storeId,String name,Integer sort,String brandName,Long categoryId,String barCode,Integer alarmType,Integer shelves,Page page) throws ParseException {
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
		String fileName = "库存列表";
		page.setIsOnePage();
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) productStockService.findAllPage(storeIdString.toString(),name,barCode,sort,brandName,categoryId,alarmType,shelves,page).getData();
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("库存列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<14;i++){
			if(i==0){
				cell = row.createCell((short)i);// 第一行第1格
				cell.setCellValue("序号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell((short)i);// 第一行第2格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell((short)i);// 第一行第3格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell((short)i);// 第一行第4格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell((short)i);// 第一行第5格
				cell.setCellValue("规格");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell((short)i);// 第一行第6格
				cell.setCellValue("单位");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell((short)i);// 第一行第7格
				cell.setCellValue("库存数量");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell((short)i);// 第一行第8格
				cell.setCellValue("缺货提醒");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell((short)i);// 第一行第9格
				cell.setCellValue("进货价");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell((short)i);// 第一行第10格
				cell.setCellValue("销售价");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell((short)i);// 第一行第11格
				cell.setCellValue("会员价");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell((short)i);// 第一行第12格
				cell.setCellValue("商品状态");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell((short)i);// 第一行第13格
				cell.setCellValue("商品分类");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell((short)i);// 第一行第14格
				cell.setCellValue("品牌");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",productStockList.size());
		if (!productStockList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",productStockList.size());
			for (int i = 0; i < productStockList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				ProductStock productStock=productStockList.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(i + 1);//序号
				row.createCell(1).setCellValue(productStock.getStoreName());//商家	
				row.createCell(2).setCellValue(productStock.getProductName());//商品名称
				row.createCell(3).setCellValue(productStock.getBarCode());//商品条码
				row.createCell(4).setCellValue(productStock.getAttributeValues());//规格
				row.createCell(5).setCellValue(productStock.getUnitName()==null?"":productStock.getUnitName());//单位
				row.createCell(6).setCellValue(productStock.getStock()==-99?"无限":productStock.getStock().toString());//库存数量
				row.createCell(7).setCellValue(productStock.getAlarmValue()==null?"":productStock.getAlarmValue().toString());//缺货提醒
				row.createCell(8).setCellValue(productStock.getCostPrice()==null?"":"￥"+df.format(productStock.getCostPrice()));//进货价
				row.createCell(9).setCellValue("￥"+df.format(productStock.getPrice()));//销售价
				row.createCell(10).setCellValue(productStock.getMemberPrice()==null?"":"￥"+df.format(productStock.getMemberPrice()));//会员价
				logger.debug("productStock.getCostPrice()={}",productStock.getCostPrice());
				//商品状态
				if(productStock.getShelves().intValue()==0){
					row.createCell(11).setCellValue("仅收银端上架");
				}else if(productStock.getShelves().intValue()==2){
					row.createCell(11).setCellValue("仅APP上架");
				}else if(productStock.getShelves().intValue()==4){
					row.createCell(11).setCellValue("收银端、APP端均上架");
				}else if(productStock.getShelves().intValue()==5){
					row.createCell(11).setCellValue("全部下架");
				}
				row.createCell(12).setCellValue(productStock.getCategoryName());//商品分类
				row.createCell(13).setCellValue(productStock.getBrandName());//品牌
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
