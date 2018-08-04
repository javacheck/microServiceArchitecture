package cn.lastmiles.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.CompressFileUtils;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.FileUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.ProductAttributeService;
import cn.lastmiles.service.ProductAttributeValueService;
import cn.lastmiles.service.ProductBrandService;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductUploadService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("productUpload")
public class ProductUploadController {
	private final static Logger logger = LoggerFactory.getLogger(ProductUploadController.class);
	
	@Autowired
	private ProductUploadService productUploadService;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private ProductAttributeValueService productValueAttributeService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductBrandService productBrandService;
	
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@RequestMapping(value = "list")
	public String list(Model model) {
		Long isStoreId=null;
		boolean flag = SecurityUtils.isStore() ? SecurityUtils.isStore()^SecurityUtils.isMainStore() : false;
		if(flag){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("isStoreId",isStoreId);
		return "productUpload/list";
	}
	@RequestMapping(value = "checkAttributeByCagetoryId" ,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String checkAttributeByCagetoryId(Long categoryId) {
		//获取传过来分类下的属性列表
		List<ProductAttribute> attributeList=productAttributeService.productAttributeList1(categoryId);
		boolean flag=true;
		if(!attributeList.isEmpty()){
			return "0";
		}else{
			return "1";
		}
		
		
	}
	@RequestMapping(value = "checkCagetoryId" ,produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<String> checkCagetoryId(Long categoryId) {
		List<String> strList=new ArrayList<String>();
		strList.add(productCategoryService.findById(categoryId).getName());
		//获取传过来分类下的属性列表
		List<ProductAttribute> attributeList=productAttributeService.productAttributeList1(categoryId);
		boolean flag=true;
		if(!attributeList.isEmpty()){
			for(int i=0;i<attributeList.size();i++){
				List<ProductAttributeValue> valuesList=productValueAttributeService.findByAttributeId1(attributeList.get(i).getId());
				if(valuesList.isEmpty()){
					flag=false;
					strList.add(attributeList.get(i).getName());
				}
			}
		}
		if(flag){
			return null;
		}else{
			return strList;
		}
		
	}
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public void findAttributeBySearch(HttpServletResponse response,Long storeId, Long categoryId) throws ParseException {
		//String action=productCategoryService.findById(categoryId).getName();
		//获取传过来分类下的属性列表
		List<ProductAttribute> attributeList=productAttributeService.productAttributeList1(categoryId);
		List<List<String>> list=new ArrayList<List<String>>();
		
		if(!attributeList.isEmpty()){
			//boolean flag=true;
			for(int i=0;i<attributeList.size();i++){
				List<ProductAttributeValue> valuesList=productValueAttributeService.findByAttributeId1(attributeList.get(i).getId());
				if(!valuesList.isEmpty()){
					List<String> listChil=new ArrayList<String>();
					logger.debug("valuesList.size("+i+")=="+valuesList.size());
					for(int j=0;j<valuesList.size();j++){
						listChil.add(valuesList.get(j).getValue());
					}
					list.add(listChil);
				}/*else{
					flag=false;
					action+=attributeList.get(i).getCategoryName();
				}*/
			}
		}
		logger.debug("attributeList={}",attributeList);
		List<Brand> brandList=productBrandService.findBrandListByStoreId(storeId);
		List<String> brandNames=new ArrayList<String>();
		if(!brandList.isEmpty()){
			for(int i=0;i<brandList.size();i++){
				brandNames.add(brandList.get(i).getName());
			}
		}
		String fileName = "model";
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("商品导入模板");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		
		for(int i=0;i<14;i++){
			if(i==0){
				cell = row.createCell((short)i);// 第一行第1格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell((short)i);// 第一行第2格
				cell.setCellValue("库存");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell((short)i);// 第一行第3格
				cell.setCellValue("缺货提醒");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell((short)i);// 第一行第4格
				cell.setCellValue("销售单价");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell((short)i);// 第一行第5格
				cell.setCellValue("会员价");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell((short)i);// 第一行第6格
				cell.setCellValue("市场价格");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell((short)i);// 第一行第7格
				cell.setCellValue("成本单价");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell((short)i);// 第一行第8格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell((short)i);// 第一行第9格
				cell.setCellValue("商品简介");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell((short)i);// 第一行第10格
				cell.setCellValue("单位名称");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell((short)i);// 第一行第11格
				cell.setCellValue("是否上架");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell((short)i);// 第一行第12格
				cell.setCellValue("图片(330*220)");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell((short)i);// 第一行第13格
				cell.setCellValue("所属品牌");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell((short)i);// 第一行第14格
				cell.setCellValue("是否有属性");
				cell.setCellStyle(style);
			}
			
			
		}
		String [] tureOrfalse={"是","否"};
		@SuppressWarnings("deprecation")
		CellRangeAddressList regions1 = new CellRangeAddressList(1, 65535, 10, 10);
	    DVConstraint constraint1 = DVConstraint.createExplicitListConstraint(tureOrfalse);
	    HSSFDataValidation data_validation1 = new HSSFDataValidation(regions1,constraint1);
	    sheet.addValidationData(data_validation1);
	    
		if(!brandList.isEmpty()){
			@SuppressWarnings("deprecation")
			CellRangeAddressList regions = new CellRangeAddressList(1, 65535, 12, 12);
		    DVConstraint constraint = DVConstraint.createExplicitListConstraint((String[])brandNames.toArray(new String[brandNames.size()]));
		    HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);
		    sheet.addValidationData(data_validation);
		}
		//CellRangeAddressList(firstRow, lastRow, firstCol, lastCol)设置行列范围
	    @SuppressWarnings("deprecation")
		CellRangeAddressList regions = new CellRangeAddressList(1, 65535, 13, 13);//生成下拉框内容
	    DVConstraint constraint = DVConstraint.createExplicitListConstraint(tureOrfalse);//绑定下拉框和作用区域
	    HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);//对sheet页生效
	    sheet.addValidationData(data_validation);
		
		if(!attributeList.isEmpty()){
			
			for(int i=0;i<attributeList.size();i++){
				cell = row.createCell((short)i+14);
				cell.setCellValue(attributeList.get(i).getName());
				cell.setCellStyle(style);
				
				@SuppressWarnings("deprecation")
				CellRangeAddressList regions2 = new CellRangeAddressList(1, 65535, i+14, i+14);
			    DVConstraint constraint2 = DVConstraint.createExplicitListConstraint((String[])list.get(i).toArray(new String[list.get(i).size()]));
			    HSSFDataValidation data_validation2 = new HSSFDataValidation(regions2,constraint2);
			    sheet.addValidationData(data_validation2);
			}
			
		}
		
	    
	   
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
			logger.debug("size ================{} ",wb.getBytes() == null ? 0 : wb.getBytes().length);
			wb.write(os);
			wb.close();
			os.flush();
			// 关闭输出流
			os.close();
			logger.debug("download end ........................ ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * rar文件上传
	 * 
	 * @param 
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "save1", method = RequestMethod.POST)
	public String zipOrRarUpload(MultipartFile zipOrRarFile,Long storeId,Long sysCategoryId,Long storeCategoryId,RedirectAttributes redirectAttributes,HttpSession session) throws IOException {
		
		if(SecurityUtils.isStore()){//如果是商家登录
			storeId=SecurityUtils.getAccountStoreId();
		}
		Long categoryId=null;
		if(sysCategoryId!=null){
			categoryId=sysCategoryId;
		}else{
			categoryId=storeCategoryId;
		}
		logger.debug("店铺=={},sysCategoryId={},storeCategoryId={}",storeId,sysCategoryId,storeCategoryId);
		String uploadPath=ConfigUtils.getProperty("file.uploadPath");
		File f = new File(uploadPath);
		// 如果文件夹不存在则创建
		if (!f.exists() && !f.isDirectory()) {
			f.mkdir();
		}
		String uuid = StringUtils.uuid();
		String _fileName = uploadPath + File.separator +  uuid + "." + FileUtils.getExtension(zipOrRarFile.getOriginalFilename());
		String _path = uploadPath + File.separator + uuid;
		
		Long sId = storeId;
		Long cId=categoryId;
		File file = new File(_fileName);
		zipOrRarFile.transferTo(file);
		CompressFileUtils.unRarFile(_fileName, _path);// 解压rar文件
		
		String xlsPath=_path + File.separator + "model.xls";
		
		logger.debug("xlsPath的路径＝＝"+xlsPath);
		File f1 = new File(xlsPath);
		
		session.setAttribute("productImportResult", "-2");
		Long accountId=SecurityUtils.getAccountId();
		if(!f1.exists()){
			String flag = "Excel文件必须为model.xls,请检查！";
			session.setAttribute("productImportResult", flag);
		}else{
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String flag;
					try {
						InputStream in = new FileInputStream(xlsPath); // 拿到解压后的model.xls
						flag=productUploadService.rarFile(in,sId,cId,uploadPath,accountId,_path);
						session.setAttribute("productImportResult", flag);
						
					} catch (IOException e) {
						e.printStackTrace();
					}finally {
						logger.debug("delete file ................. ");
						FileUtils.delete(_fileName);
						logger.debug("_path=" + _path);
						FileUtils.delete(_path);
						
					}
				}
			});
		}
		
		
		return "redirect:/productUpload/list";
	}
	@RequestMapping(value="importResult",produces="plain/text;charset=utf-8")
	@ResponseBody
	public String importResult(HttpSession session){
		String obj = (String)session.getAttribute("productImportResult");
		logger.debug(obj);
		if (!"-2".equals(obj)){
			session.removeAttribute("productImportResult");
		}
		return obj;
	}
	@RequestMapping(value = "saveImage", method = RequestMethod.POST)
	public String upload(@RequestParam(value="imageFile", required=true)  MultipartFile[] imageFile,Long productStockId, Model model,RedirectAttributes redirectAttributes) throws IOException {
		String flag=productUploadService.saveImage(imageFile,productStockId);
		redirectAttributes.addFlashAttribute("action", flag);
		return "redirect:/productStock/uploadImage/" + productStockId;
	}
	
	
}
