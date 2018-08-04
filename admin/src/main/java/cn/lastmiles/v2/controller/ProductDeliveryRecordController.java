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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.ProductDeliveryRecord;
import cn.lastmiles.bean.ProductDeliveryRecordStock;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.SupplierService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.ProductStockService;
import cn.lastmiles.v2.service.ProductDeliveryRecordService;

@Controller
@RequestMapping("productDeliveryRecord")
public class ProductDeliveryRecordController {
private final static Logger logger = LoggerFactory.getLogger(ProductDeliveryRecordController.class);
	
	@Autowired
	private ProductDeliveryRecordService ProductDeliveryRecordService;
	@Autowired
	private ProductStockService productStockService;
	
	@Autowired
	private SupplierService supplierService;
	@RequestMapping("")
	public String index() {
		return "redirect:/v2/productDeliveryRecord/list";
	}
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	/**
	 * 入库列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "v2/productDeliveryRecord/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(String deliveryNumber,String mobile,Long storeId,String beginTime,String endTime, Page page, Model model) {
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
		model.addAttribute("data", ProductDeliveryRecordService.findAllPage(storeIdString.toString(),deliveryNumber,mobile.trim(),beginTime,endTime,page));
		return "v2/productDeliveryRecord/list-data";
		
	}
	
	/**
	 * 跳到商品出库页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		return "v2/productDeliveryRecord/add";
	}
	
	@RequestMapping("stockList/list-data")
	public String listData(Long product_storeId,String product_name, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if(product_storeId==null){
			storeIdString.append(SecurityUtils.getAccountStoreId().toString());
		}
		if( null != product_name){
			product_name = product_name.replaceAll("\\s*", "");
		}
		logger.debug("product_storeId={}",product_storeId);
		String barCode="";
		page = productStockService.findAllList(storeIdString.toString(),product_name,barCode,0,null,null,null,null,page);
		
		model.addAttribute("data",page);
		return "v2/productDeliveryRecord/stockList-data";
		
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
			storeIdString.append(SecurityUtils.getAccountStoreId());
			boolean index = true;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
		}
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "v2/productDeliveryRecord/showModelList-data";
	}
	@RequestMapping(value="findProductStockList-by-barCode",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> findProductStockList(String barCode,Page page){
		logger.debug("barCode={}",barCode);
		page.setIsOnePage();
		StringBuffer storeIdString = new StringBuffer();
		storeIdString.append(SecurityUtils.getAccountStoreId().toString());
		
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) productStockService.findAllPage(storeIdString.toString(),null,barCode,0,null,null,null,null,page).getData();
		logger.debug("ps={}",productStockList);
		return productStockList;
	}
	
	@RequestMapping(value="findProductStockList-by-productStockIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> findProductStockList(String productStockIds){
		logger.debug("productStockIds={}",productStockIds);
		List<ProductStock> ps=productStockService.findProductStockList(productStockIds);
		logger.debug("ps={}",ps);
		return ps;
	}
	
	
	/**
	 * 保存库存出库
	 * @param 
	 * @return
	 */
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(String dateProductStockSJson,String deliveryTime,String memo){
		logger.debug("dateProductStockSJson={},DeliveryTime={},memo={}",dateProductStockSJson,deliveryTime,memo);
		Long accountId=SecurityUtils.getAccountId();
		Long storeId=SecurityUtils.getAccountStoreId();
		List<ProductDeliveryRecordStock> productDeliveryRecordStocks=JsonUtils.jsonToList(dateProductStockSJson,ProductDeliveryRecordStock.class);
		ProductDeliveryRecordService.save(productDeliveryRecordStocks,deliveryTime,accountId,storeId,memo);
		return "1";
	}
	
	@RequestMapping(value="info/showMode/{id}" )
	public String confirmInfo(@PathVariable Long  id, Model model){
		logger.debug("productDeliveryRecord-info-ID IS :"+id);
		List<ProductDeliveryRecordStock> pdrs = ProductDeliveryRecordService.findProductDeliveryRecordStockById(id);
		logger.debug("pdrs={}",pdrs);
		model.addAttribute("pdrs",pdrs);
		return "v2/productDeliveryRecord/info";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,Long storeId,
			String DeliveryNumber,String mobile,String beginTime,String endTime,Page page) throws ParseException {
		logger.debug("DeliveryNumber={},mobile={},beginTime={},endTime={}",DeliveryNumber,mobile,beginTime,endTime);
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
		String fileName = "商品出库列表";
		page.setIsOnePage();
		@SuppressWarnings("unchecked")
		List<ProductDeliveryRecord> pdrList=(List<ProductDeliveryRecord>) ProductDeliveryRecordService.findAllPage(storeIdString.toString(),DeliveryNumber,mobile.trim(),beginTime,endTime,page).getData();
		logger.debug("pdrList={}",pdrList);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("商品出库列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<14;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("序号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("出库单号");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("操作人");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("出库时间");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("操作时间");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("出库备注");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("规格");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("单位");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell(i);// 第一行第11格
				cell.setCellValue("进货价");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell(i);// 第一行第12格
				cell.setCellValue("出库数量");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell(i);// 第一行第13格
				cell.setCellValue("库存数量");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell(i);// 第一行第14格
				cell.setCellValue("商品备注");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",pdrList.size());
		int index=1;
		if (!pdrList.isEmpty()) {
			for (int i = 0; i < pdrList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				
				ProductDeliveryRecord pdr=pdrList.get(i);
				List<ProductDeliveryRecordStock> pdrs = ProductDeliveryRecordService.findProductDeliveryRecordStockById(pdr.getId());
				for(int j=0;j<pdrs.size();j++){
					if(j==0){
						row = sheet.createRow((int) index);
						
						row.createCell(0).setCellValue(i+1);//序号
						row.createCell(1).setCellValue(pdr.getDeliveryNumber());//出库单号	
						row.createCell(2).setCellValue(pdr.getMobile());//操作人
						row.createCell(3).setCellValue(DateUtils.format(pdr.getDeliveryTime(), "yyyy-MM-dd"));//出库时间
						row.createCell(4).setCellValue(DateUtils.format(pdr.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//操作时间
						row.createCell(5).setCellValue(pdr.getMemo());//出库备注
						
						row.createCell(6).setCellValue(pdrs.get(j).getProductName());//商品名称
						row.createCell(7).setCellValue(pdrs.get(j).getBarCode());//商品条码
						row.createCell(8).setCellValue(pdrs.get(j).getAttributeValues());//规格
						row.createCell(9).setCellValue(pdrs.get(j).getUnitName()!=null?pdrs.get(j).getUnitName():"");//单位
						row.createCell(10).setCellValue(pdrs.get(j).getCostPrice()!=null?"￥"+df.format(pdrs.get(j).getCostPrice()):"");//进货价
						row.createCell(11).setCellValue(pdrs.get(j).getAmount());//出库数量
						row.createCell(12).setCellValue(pdrs.get(j).getStock());//库存数量
						row.createCell(13).setCellValue(pdrs.get(j).getMemo()!=null?pdrs.get(j).getMemo():"");//商品备注
						index++;
						
					}else{
						row = sheet.createRow((int) index);
						row.createCell(6).setCellValue(pdrs.get(j).getProductName());//商品名称
						row.createCell(7).setCellValue(pdrs.get(j).getBarCode());//商品条码
						row.createCell(8).setCellValue(pdrs.get(j).getAttributeValues());//规格
						row.createCell(9).setCellValue(pdrs.get(j).getUnitName()!=null?pdrs.get(j).getUnitName():"");//单位
						row.createCell(10).setCellValue(pdrs.get(j).getCostPrice()!=null?"￥"+df.format(pdrs.get(j).getCostPrice()):"");//进货价
						row.createCell(11).setCellValue(pdrs.get(j).getAmount());//出库数量
						row.createCell(12).setCellValue(pdrs.get(j).getStock());//库存数量
						row.createCell(13).setCellValue(pdrs.get(j).getMemo()!=null?pdrs.get(j).getMemo():"");//商品备注
						index++;
					}
					
				}
				
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
