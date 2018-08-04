/**
 * createDate : 2016年6月14日上午10:12:53
 */
package cn.lastmiles.v2.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ExcelUtils;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.SpellHelper;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.BrandService;
import cn.lastmiles.service.ProductService;
import cn.lastmiles.v2.service.ProductStockService;
import cn.lastmiles.service.ProductBrandService;
import cn.lastmiles.service.ProductUnitService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.FileServiceUtils;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.CommodityCategoryService;
import cn.lastmiles.v2.service.CommodityManagerService;
import jodd.util.Base64;

@Controller
@RequestMapping("commodityManager")
public class CommodityManagerController {
	/**
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommodityManagerController.class);
	@Autowired
	private CommodityCategoryService commodityCategoryService;
	@Autowired
	private CommodityManagerService commodityManagerService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductUnitService productUnitService;
	@Autowired 
	private StoreService storeService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private FileService fileService; // 文件
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductBrandService productBrandService;
	
	/**
	 * 菜单列表点击
	 * @param model 储值容器
	 * @return 跳转页面
	 */
	@RequestMapping(value = "list")
	public String commodityManagerList(Model model) {
		
		// 管理员登录标识为0、商店登录(总部)标识为1、商店登录(非总部)标识为2、其他身份登录为-1
		int loginIdentityMarking = SecurityUtils.isAdmin() ? 0 : (SecurityUtils.isStore() ? (SecurityUtils.isMainStore() ? 1 : 2 ):-1 );
		logger.debug("commodityCategoryList ------->>> loginIdentityMarking is {} ",loginIdentityMarking);
		model.addAttribute("loginIdentityMarking", loginIdentityMarking);
		
		List<Brand> brandList = brandService.getBrandListByStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("brandList",brandList);
		logger.debug("return brandList is {}",brandList);
		return "/v2/commodityManager/list";
	}
	
	/**
	 * 菜单列表详情
	 * @param storeId 商家ID
	 * @param name 商品名称或拼音码
	 * @param barCode 商品条码
	 * @param categoryId 商品分类ID
	 * @param shelves 商品状态(上架/下架)
	 * @param brandId 品牌ID
	 * @return
	 */
	@RequestMapping("list-data")
	public String commodityManagerListData(Long storeId,String name,String barCode,Long categoryId,Integer shelves,Long brandId,Page page,Model model){
		logger.debug("commodityManagerListData ----->>> receive parameters storeId is {}, name is {},barCode is {},categoryId is {},shelves is {},brandId is {} ,page is {}",
				    storeId,name,barCode,categoryId,shelves,brandId,page);
		boolean isCurrentStore = false;
		if( null == storeId ){
			// 非管理员
			if( !SecurityUtils.isAdmin() ){
				storeId = SecurityUtils.getAccountStoreId();
				isCurrentStore = true;
			}
		} else {
			if( ObjectUtils.equals(storeId, SecurityUtils.getAccountStoreId()) ){
				isCurrentStore = true;
			}
		} 
		model.addAttribute("isCurrentStore",isCurrentStore);
		page = commodityManagerService.list(storeId,name,barCode,categoryId,shelves,brandId,page);
		model.addAttribute("data",page);
		logger.debug("isCurrentStore is {} , return Data is {}",isCurrentStore,page);
		return "/v2/commodityManager/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		List<Brand> brandList = brandService.getBrandListByStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("brandList",brandList);
		
		List<ProductUnit> unitList = productUnitService.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		model.addAttribute("unitList",unitList);

		List<ProductAttribute> attributeList = commodityManagerService.getProductAttributeListByStoreID();
		model.addAttribute("attributeList",attributeList);
		
		model.addAttribute("storeId",SecurityUtils.getAccountStoreId());
		return "/v2/commodityManager/add";
	}
	
	@RequestMapping(value = "ajax/analysisChineseSrc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String,Object> analysisChineseSrc(String src){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.put("returnSrc", SpellHelper.getPingYin(src));
		return returnMap;
	}
	
	/**
	 * 
	 * @param product
	 * @param productStockJson
	 * @param imgUpLoad
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String sykAdd(Product product,String productStockJson, String imgUpLoad) throws IOException {
		
		//System.out.println(product.getPictureFileCache());
		product.setStoreId(SecurityUtils.getAccountStoreId());
		product.setAccountId(SecurityUtils.getAccountId());
		
		logger.debug("productStockJson is {}",productStockJson);
		
		List<ProductStock> productStockList = JsonUtils.jsonToList(productStockJson,ProductStock.class);
		
		logger.debug("productStockList is {},product is {}",productStockList,product);
		
		if( null == product.getPictureFileCache() || "del".equals(product.getPictureFileCache())){
			if(!"".equals(product.getPicture())){
				fileService.delete(product.getPicture()); // 因为LOGO图片后面可以为空,故当值为空时要删除图片信息					
			}
			product.setPicUrl("");
		} else {
			product.setPicUrl(product.getPicture());
		}
		
		if (null != imgUpLoad && !"original".equals(imgUpLoad) && !"del".equals(imgUpLoad) && null != product.getPictureFileCache() && "change".equals(product.getPictureFileCache())) {
			if(!"".equals(product.getPicture()) ){
				fileService.delete(product.getPicture()); // 先删除之前的					
			}
			String imageID;
			try {
				imgUpLoad = imgUpLoad.split(",")[1];
				imageID = fileService.save(new ByteArrayInputStream(Base64.decode(imgUpLoad)));
				logger.debug("controller .... imageID is {}",imageID);
				
				product.setPicUrl(imageID); 
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/commodityManager/list";
			}
		}
		if( null != product.getId() ){
			logger.debug("save or Update product is {}",product);
			commodityManagerService.saveOrUpdate(product,productStockList);
		} else {
			logger.debug("save ....");
			commodityManagerService.save(product,productStockList);				
		}
	
	return "redirect:/commodityManager/list";
		
	}
	
	//@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(Product product,String productStockJson, @RequestParam("pictureFile") MultipartFile imageFile) {
			product.setStoreId(SecurityUtils.getAccountStoreId());
			product.setAccountId(SecurityUtils.getAccountId());
			
			logger.debug("productStockJson is {}",productStockJson);
			
			List<ProductStock> productStockList = JsonUtils.jsonToList(productStockJson,ProductStock.class);
			
			logger.debug("productStockList is {},product is {}",productStockList,product);
			
			if( null == product.getPictureFileCache() || "".equals(product.getPictureFileCache())){
				if(!"".equals(product.getPicture())){
					fileService.delete(product.getPicture()); // 因为LOGO图片后面可以为空,故当值为空时要删除图片信息					
				}
				product.setPicUrl("");
			} else {
				product.setPicUrl(product.getPicture());
			}
			
			if (null != imageFile && imageFile.getSize() > 0 && null != product.getPictureFileCache() && !"".equals(product.getPictureFileCache())) {
				if(!"".equals(product.getPicture()) ){
					fileService.delete(product.getPicture()); // 先删除之前的					
				}
				String imageID;
				try {
					imageID = fileService.save(imageFile.getInputStream());
					logger.debug("controller .... imageID is {}",imageID);
					
					product.setPicUrl(imageID); 
				} catch (IOException e) {
					e.printStackTrace();
					return "redirect:/commodityManager/list";
				}
			}
			if( null != product.getId() ){
				logger.debug("save or Update product is {}",product);
				commodityManagerService.saveOrUpdate(product,productStockList);
			} else {
				logger.debug("save ....");
				commodityManagerService.save(product,productStockList);				
			}
		
		return "redirect:/commodityManager/list";
	}
	
	@RequestMapping(value="update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model){
		logger.debug("to update parameters id is {}",id);
		
		Product product = commodityManagerService.findById(id);
		List<ProductStock> productStockList = commodityManagerService.findByProductId(id);
		for (ProductStock productStock : productStockList) {
			if( StringUtils.isBlank(product.getPicUrl())){
				if( StringUtils.isNotBlank(productStock.getImageId())){
					logger.debug(" ps imageId is {}",productStock.getImageId());
					
					product.setPicUrl(FileServiceUtils.getFileUrl(productStock.getImageId()));	
					product.setPicture(productStock.getImageId());
				}
			}
			List<ProductStockAttributeValue> valueList = commodityManagerService.findByAttributeCode(productStock.getAttributeCode());
			if( null != valueList && valueList.size() > 0 ){
				for (ProductStockAttributeValue productStockAttributeValue : valueList) {
					if( productStockAttributeValue.getNumber() == 1 ){
						productStock.setValue_1(productStockAttributeValue.getValue());
						if( null == product.getAttribute_1() ){
							product.setAttribute_1(productStockAttributeValue.getProductAttributeId());						
						}
					} else if( productStockAttributeValue.getNumber() == 2 ){
						productStock.setValue_2(productStockAttributeValue.getValue());
						if( null == product.getAttribute_2() ){
							product.setAttribute_2(productStockAttributeValue.getProductAttributeId());
						}
					}
				}
			}
		}
		logger.debug("product is {}",product);
		model.addAttribute("product", product);
		model.addAttribute("productStockList", productStockList);
		
		List<Brand> brandList = brandService.getBrandListByStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("brandList",brandList);
		
		List<ProductUnit> unitList = productUnitService.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		model.addAttribute("unitList",unitList);
		
		List<ProductAttribute> attributeList = commodityManagerService.getProductAttributeListByStoreID();
		model.addAttribute("attributeList",attributeList);
		
		model.addAttribute("storeId",SecurityUtils.getAccountStoreId());
		return "/v2/commodityManager/update";
	}
	
	@RequestMapping(value = "ajax/deleteProductStock", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteProductStock(Long productStockId,Long productId){
		return commodityManagerService.deleteProductStock(productStockId,productId);
	}
	
	@RequestMapping(value = "ajax/batchOpertion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int batchOpertion(Integer weighing,Integer returnGoods,Integer shelves,Long categoryId,Long brandId,String[] productStockArray){
		logger.debug("weighing is {},returnGoods is {},shelves is {},categoryId is {},barandId is {},productStockArray is {}",weighing,returnGoods,shelves,categoryId,brandId,productStockArray);
		StringBuilder linkString = new StringBuilder();
		boolean flag = false;
		for (String productId : productStockArray) {
			if(flag){
				linkString.append(",");				
			}
			linkString.append(productId);
			flag = true;
		}
		int i = 0 ;
		if( commodityManagerService.updateProduct(categoryId,brandId,linkString.toString()) ){
			i = commodityManagerService.updateProductStock(weighing,returnGoods,shelves,categoryId,linkString.toString());
		}
		
		return i;
	}
	
	/**
	 * 根据传入的库存对象，得到店铺ID和库存对象的条形码字段
	 * @param productStock
	 * @return
	 */
	@RequestMapping(value="list/ajax/existBarCode",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int byBarCodeFindProductStock(String name,Long storeId,String barCode,Long id) {
		logger.debug("existBarCode --- >>> proudctName={},storeId={},barCode={} ,id = {}",name,storeId,barCode,id);
		
		
		List<ProductStock> productStockList = commodityManagerService.byBarCodeFindProductStock(storeId,barCode,id);
		// 当前店铺有此条形码
		if( null != productStockList && productStockList.size() > 0 ){
			List<Product> productList = commodityManagerService.findByNameAndStoreId(name, storeId,id);
			// 当前店铺有这个商品
			if( null != productList && productList.size() > 0 ){
				ArrayList<Long> al = new ArrayList<Long>(); 
				for (int i = 0; i < productStockList.size(); i++) {
					al.add(productStockList.get(i).getProductId());
				}
				boolean flag = false;
				for (int i = 0; i < productList.size(); i++) {
					for (Long long1 : al) {
						if(!productList.get(i).getId().equals(long1)){
							flag = true;
							break; // 有了就不循环了。减少消耗
						}
						
					}
					if(flag){
						break;
					}
				}
				if(flag){
					return 1;
				} else {
					return 0;					
				}
			} else {
				return 1; // 有条形码了。但是不属于此商品，则增加不成功！
			}
			 
		} else {
			return 0; // 直接返回可以操作
		}
	}
	
	
	@RequestMapping(value="list/ajax/existProductName",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int byProductNameFindProduct(String name,Long storeId,Long id) {
		return commodityManagerService.byProductNameFindProduct(name,storeId,id);
	}
	@RequestMapping(value = "delete/deleteById", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long productId){
		logger.debug("delete productId is {}",productId);
		
		return commodityManagerService.deleteByProductId(productId);
	}
	
	@RequestMapping(value="details/{id}",method = RequestMethod.GET)
	public String toDetails(@PathVariable Long id,Model model){
		logger.debug("to details parameters id is {}",id);
		
		Product product = commodityManagerService.findById(id);
		List<ProductStock> productStockList = commodityManagerService.findByProductId(id);
		for (ProductStock productStock : productStockList) {
			if( StringUtils.isBlank(product.getPicUrl())){
				if( StringUtils.isNotBlank(productStock.getImageId())){
					logger.debug(" ps imageId is {}",productStock.getImageId());
					
					product.setPicUrl(FileServiceUtils.getFileUrl(productStock.getImageId()));	
					product.setPicture(productStock.getImageId());
				}
			}
			List<ProductStockAttributeValue> valueList = commodityManagerService.findByAttributeCode(productStock.getAttributeCode());
			if( null != valueList && valueList.size() > 0 ){
				for (ProductStockAttributeValue productStockAttributeValue : valueList) {
					if( productStockAttributeValue.getNumber() == 1 ){
						productStock.setValue_1(productStockAttributeValue.getValue());
						if( null == product.getAttribute_1() ){
							product.setAttribute_1(productStockAttributeValue.getProductAttributeId());						
						}
					} else if( productStockAttributeValue.getNumber() == 2 ){
						productStock.setValue_2(productStockAttributeValue.getValue());
						if( null == product.getAttribute_2() ){
							product.setAttribute_2(productStockAttributeValue.getProductAttributeId());
						}
					}
				}
			}
		}
		logger.debug("product is {}",product);
		model.addAttribute("product", product);
		model.addAttribute("productStockList", productStockList);
		
		List<Brand> brandList = brandService.getBrandListByStoreId(SecurityUtils.getAccountStoreId());
		model.addAttribute("brandList",brandList);
		
		List<ProductUnit> unitList = productUnitService.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		model.addAttribute("unitList",unitList);
		
		List<ProductAttribute> attributeList = commodityManagerService.getProductAttributeListByStoreID();
		model.addAttribute("attributeList",attributeList);
		
		model.addAttribute("storeId",SecurityUtils.getAccountStoreId());
		return "/v2/commodityManager/details";
	}
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isMainStore()){
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			storeIdString.append(SecurityUtils.getAccountStoreId());
			for (Store store : storeList) {
				storeIdString.append(",");
				storeIdString.append(store.getId());
			}
		}
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, "",  page);
		model.addAttribute("data", page);
		return "/v2/commodityManager/showModelList-data";
	}
	
	/**
	 * EXCEL表头
	 * @return
	 */
	public String[] getHeaders() {  
        return new String[] { "*商品名称","*商品条码","*商品分类","规格1","规格2","单位","*销售价(元)","*进货价(元)","*市场价(元)","会员价(元)","库存数量","是否无限库存","缺货提醒","商品排序","品牌","是否称重","上架管理","是否支持退货","商品备注"  };  
	} 
	
	@RequestMapping(value = "ajax/reportProductToExcel")
	public String reportOrderToExcel(HttpServletResponse response, String reportStoreId,String reportName,String reportBarCode,Long reportCategoryId,Integer reportShelves,String reportBrandId,Page page) {
		page.setIsOnePage();
		long startTime = System.currentTimeMillis();
		if( StringUtils.isBlank(reportStoreId) ){
			// 非管理员
			if( !SecurityUtils.isAdmin() ){
				reportStoreId = SecurityUtils.getAccountStoreId().toString();
			}
		}
		String brandName="";
		if(reportBrandId!=null && !reportBrandId.equals("")){
			brandName=productBrandService.findById(Long.parseLong(reportBrandId)).getName();
		}
		//List<Product> productList = commodityManagerService.reportList(reportStoreId,reportName,reportBarCode,reportCategoryId,reportShelves,reportBrandId);
		logger.debug("reportStoreId={},reportName={},reportBarCode={},reportCategoryId={},reportShelves={},reportBrandId={}",reportStoreId,reportName,reportBarCode,reportCategoryId,reportShelves,reportBrandId);
		
		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) productStockService.findAllPage(reportStoreId,reportName,reportBarCode,99,brandName,reportCategoryId,null,reportShelves,page).getData();
		String fileOrTitleName = "productList";
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream os = response.getOutputStream();
			
			// 这个是弹出下载对话框的关键代码
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			//暂不考虑大数据量的问题和Excel版本的问题(2015.12.16)
			exportExcel(workbook,fileOrTitleName,getHeaders(),productStockList);
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
            
            logger.debug("导出商品耗时：{}",(System.currentTimeMillis() - startTime) );
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Map<Integer,String> getShelves(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(cn.lastmiles.constant.Constants.ProductStock.POS_UP, "收银端上架");
		map.put(cn.lastmiles.constant.Constants.ProductStock.POS_DOWN, "收银端下架");
		map.put(cn.lastmiles.constant.Constants.ProductStock.APP_UP, "APP端上架");
		map.put(cn.lastmiles.constant.Constants.ProductStock.APP_DOWN, "APP端下架");
		map.put(cn.lastmiles.constant.Constants.ProductStock.ALL_UP, "收银端上架、APP端上架");
		map.put(cn.lastmiles.constant.Constants.ProductStock.ALL_DOWN, "全部下架");
		
		return map;
	}
	public void exportExcel(HSSFWorkbook workbook, String title, String[] headers, Collection<?> dataset) {
	     
			  HSSFSheet sheet = workbook.createSheet(title);
		      HSSFCellStyle style = workbook.createCellStyle();
		      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		      
		      HSSFDataFormat format = workbook.createDataFormat();
		      HSSFCellStyle cellStyle = workbook.createCellStyle();    
		      
		      cellStyle.setDataFormat(format.getFormat("@"));
		      sheet.setColumnWidth(1, 8000);
			  sheet.setDefaultColumnStyle(1,cellStyle);
			  
			  sheet.setDefaultColumnWidth(20);
				
		      HSSFRow firstRow = sheet.createRow(0);
		     // firstRow.setHeight((short) 2600);
		      HSSFCell firstCell = firstRow.createCell(0);
		      cellStyle.setWrapText(true);    
		      firstCell.setCellStyle(cellStyle);
		      /**
		       * 商品导入说明：
				1.同一个分类下，如果商品名称相同则默认为同一个商品，同种商品多种规格的按照多行添加。
				2.有*的表示必填，其余为选填项
				3.每个商品的分类需要下拉选择，商户在做商品导入时，必须首先在管理后台新建商品分类。
				4.商品数量最多支持导入3000个商品，如商品超过3000，请分批次导入。
				5.无图片的商品会使用系统默认图片。
				6.同种商品的商品排序、品牌、是否称重、上架管理、是否支持退换货、商品备注必须一致，如果不一致则以首次添加的商品信息导入。
		       */
		      StringBuilder sb = new StringBuilder(200);
		      sb.append(" 您已成功导出商品资料：");
		      firstCell.setCellValue(sb.toString());
		      
		      //1.生成字体对象  
	        HSSFFont font = workbook.createFont();  
	        font.setFontHeightInPoints((short) 10);  // 设置字号 
	        font.setFontName("新宋体");  
	        font.setColor(HSSFColor.RED.index);  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 设置字体样式 正常显示
		      style.setFont(font); // 调用字体样式对象 
		      
	        sheet.addMergedRegion(new CellRangeAddress(     
	              0, //first row (0-based)  from 行     
	              0, //last row  (0-based)  to 行     
	              0, //first column (0-based) from 列     
	              20  //last column  (0-based)  to 列     
	      ));      
	      
        //产生表格标题行
	      HSSFRow row = sheet.createRow(1);
	      for (int i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         if( i == 0 || i == 1 || i == 2 || i == 6 || i == 7 || i == 8 ){
	        	 cell.setCellStyle(style);	        	 
	         }
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }

	    if(!dataset.isEmpty()){
	    	Iterator<?> it = dataset.iterator();
	    	int index = 1;
	    	Map<Integer,String> map = getShelves();
	    	while(it.hasNext()){
	    		index++;
	    		row = sheet.createRow(index);
	    		row.setRowStyle(style);
	    		 
	    		ProductStock ps = (ProductStock) it.next();
	    		row.createCell(0).setCellValue(ps.getProductName()); // 商品名称
	    		row.createCell(1).setCellValue(ps.getBarCode()); // 商品条码
	    		row.createCell(2).setCellValue(ps.getCategoryName()); // 商品分类
	    		if(ps.getAttributeValues()!=null && !ps.getAttributeValues().equals("")){
	    			logger.debug("ps.getAttributeValues()={}",ps.getAttributeValues());
	    			if(ps.getAttributeValues().indexOf("|")>0){
	    				row.createCell(3).setCellValue(StringUtil.splitc(ps.getAttributeValues(),"|")[0]); // 规格1
	    				row.createCell(4).setCellValue(StringUtil.splitc(ps.getAttributeValues(),"|")[1]==null?"":StringUtil.splitc(ps.getAttributeValues(),"|")[1]); // 规格2
	    			}else{
	    				row.createCell(3).setCellValue(ps.getAttributeValues()); // 规格1
	    				row.createCell(4).setCellValue(""); // 规格2
	    			}
	    		}else{
	    			row.createCell(3).setCellValue(""); // 规格1
	    			row.createCell(4).setCellValue(""); // 规格2
	    		}
	    		row.createCell(5).setCellValue(ps.getUnitName()); //单位
	    		row.createCell(6).setCellValue(ps.getPrice()); //销售价(元)
	    		row.createCell(7).setCellValue(ps.getCostPrice()); //进货价(元)
	    		row.createCell(8).setCellValue(ps.getMarketPrice()==null?"":ps.getMarketPrice().toString()); //市场价(元)
	    		row.createCell(9).setCellValue(ps.getMemberPrice()==null?"":ps.getMemberPrice().toString()); //会员价(元)
	    		row.createCell(10).setCellValue(ps.getStock()==-99?"":ps.getStock().toString()); //库存数量
	    		row.createCell(11).setCellValue(ps.getStock()==-99?"是":"否"); //是否无限库存
	    		row.createCell(12).setCellValue(ps.getAlarmValue()==null?"":ps.getAlarmValue().toString()); //缺货提醒
	    		row.createCell(13).setCellValue(ps.getSort()); //商品排序
	    		row.createCell(14).setCellValue(ps.getBrandName()); //品牌
	    		row.createCell(15).setCellValue(ps.getWeighing()==0?"否":"是"); //是否称重
	    		if(ps.getShelves().intValue()==0 || ps.getShelves().intValue()==2 || ps.getShelves().intValue()==4){
	    			row.createCell(16).setCellValue("上架"); //上架管理
	    		}else{
	    			row.createCell(16).setCellValue("下架"); //上架管理
	    		}
	    		row.createCell(17).setCellValue(ps.getReturnGoods()==0?"否":"是"); //是否支持退货
	    		row.createCell(18).setCellValue(ps.getRemarks()==null?"":ps.getRemarks()); //商品备注
	    	}
	    	
	    	logger.debug("共导出商品数据:{}条",index);
	    	index = 0;
	    }
	}
	
	@RequestMapping("uploadList/list")
	public String uploadList() {
		return "/v2/commodityManager/uploadList";
	}
	
	/**
	 * poi导出excel
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/exportModelExcel", produces = MediaType.APPLICATION_JSON_VALUE)
	public void exportModelExcelBySearch(HttpServletResponse response,HttpServletRequest request) throws Exception {
		long startTime = System.currentTimeMillis();
		HSSFWorkbook workbook = new HSSFWorkbook();
		OutputStream os = response.getOutputStream();
		try {
			String fileOrTitleName = "productModel";
			// 这个是弹出下载对话框的关键代码
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			String[] hearder = new String[] { "*商品名称","*商品条码","*商品分类","规格1","规格2","单位","*销售价(元)","*进货价(元)","*市场价(元)","会员价(元)","库存数量","是否无限库存","缺货提醒","商品排序","品牌","是否称重","上架管理","是否支持退货","商品备注" };
			exportModelExcel(workbook,fileOrTitleName,hearder);
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
            
            logger.debug("导出商品模版耗时：{}",(System.currentTimeMillis() - startTime) );
            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( null != os ){
				os.close();
			}
			if( null != workbook ){
				workbook.close();
			}
		}
	}
	
	public void exportModelExcel(HSSFWorkbook workbook, String title, String[] headers) {
	      HSSFSheet sheet = workbook.createSheet(title);
	      HSSFCellStyle style = workbook.createCellStyle();
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	      
	      HSSFDataFormat format = workbook.createDataFormat();
	      HSSFCellStyle cellStyle = workbook.createCellStyle();    
	      
	      cellStyle.setDataFormat(format.getFormat("@"));
	      sheet.setColumnWidth(1, 8000);
		  sheet.setDefaultColumnStyle(1,cellStyle);
		  
		  sheet.setDefaultColumnWidth(20);
			
	      HSSFRow firstRow = sheet.createRow(0);
	      firstRow.setHeight((short) 2600);
	      HSSFCell firstCell = firstRow.createCell(0);
	      cellStyle.setWrapText(true);    
	      firstCell.setCellStyle(cellStyle);
	      /**
	       * 商品导入说明：
			1.同一个分类下，如果商品名称相同则默认为同一个商品，同种商品多种规格的按照多行添加。
			2.有*的表示必填，其余为选填项
			3.每个商品的分类需要下拉选择，商户在做商品导入时，必须首先在管理后台新建商品分类。
			4.商品数量最多支持导入3000个商品，如商品超过3000，请分批次导入。
			5.无图片的商品会使用系统默认图片。
			6.同种商品的商品排序、品牌、是否称重、上架管理、是否支持退换货、商品备注必须一致，如果不一致则以首次添加的商品信息导入。
	       */
	      StringBuilder sb = new StringBuilder(200);
	      sb.append("\r\n商品导入说明：\r\n");
	      sb.append("1.如果商品名称相同则默认为同一个商品，同种商品多种规格的按照多行添加。\r\n");
	      sb.append("2.有*的表示必填，其余为选填项\r\n");
	      sb.append("3.每个商品的分类需要下拉选择，商户在做商品导入时，必须首先在管理后台新建商品分类。\r\n");
	      sb.append("4.商品数量最多支持导入3000个商品，如商品超过3000，请分批次导入。\r\n");
	      sb.append("5.无图片的商品会使用系统默认图片。\r\n");
	      sb.append("6.同种商品的商品排序、品牌、是否称重、上架管理、是否支持退换货、商品备注必须一致，如果不一致则以首次添加的商品信息导入。\r\n");
	      sb.append("7.“是否无限库存”、“是否称重”为空时则默认为“否”，“是否支持退货”为空时则默认为“是”，“库存数量”为空时则默认为0（非无限库存时），“上架管理”为空时默认为“上架”。\r\n");
	      sb.append("8.仅支持新商品导入，系统中如果已有的商品则不能再次导入该商品信息，如需新增规格请在管理后台新增。");
	      firstCell.setCellValue(sb.toString());
	      
	      //1.生成字体对象  
          HSSFFont font = workbook.createFont();  
          font.setFontHeightInPoints((short) 10);  // 设置字号 
          font.setFontName("新宋体");  
          font.setColor(HSSFColor.RED.index);  
          font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 设置字体样式 正常显示
	      style.setFont(font); // 调用字体样式对象 
	      
          sheet.addMergedRegion(new CellRangeAddress(     
                0, //first row (0-based)  from 行     
                0, //last row  (0-based)  to 行     
                0, //first column (0-based) from 列     
                20  //last column  (0-based)  to 列     
        ));      
          
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(1);
	      for (int i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         if( i == 0 || i == 1 || i == 2 || i == 6 || i == 7 || i == 8 ){
	        	 cell.setCellStyle(style);	        	 
	         }
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      
	      // 给第三列填充商品分类
	      List<ProductCategory> categoryList = commodityCategoryService.findByStoreId(SecurityUtils.getAccountStoreId());
	      List<String> list = new ArrayList<String>();
	      for (ProductCategory productCategory : categoryList) {
	    	  list.add(productCategory.getName());
		  }
	      ExcelUtils.generateModel(workbook,sheet,"categoryList",list,2,2,true);
	      
	      List<String> yesOrNO = new ArrayList<String>();
	      yesOrNO.add("是");
	      yesOrNO.add("否");
		  ExcelUtils.generateModel(workbook,sheet,"yesOrno",yesOrNO,11,2,false);
		  ExcelUtils.generateModel(workbook,sheet,"yesOrno",yesOrNO,15,2,false);
		  ExcelUtils.generateModel(workbook,sheet,"yesOrno",yesOrNO,17,2,false);
		  
		  List<String> upOrDown = new ArrayList<String>();
		  upOrDown.add("上架");
		  upOrDown.add("下架");
		  ExcelUtils.generateModel(workbook,sheet,"upOrDown",upOrDown,16,2,false);
	}
		
	@RequestMapping(value = "uploadProductExcel", method = RequestMethod.POST)
	public String uploadProductExcel(@RequestParam("userExcelFile") MultipartFile userExcelFile,RedirectAttributes ra) {
		System.out.println(userExcelFile.getSize());
		long startTime = System.currentTimeMillis();
//---------------------------------------------------------------读取EXCEL文件信息----->>>>>
		List<List<String>> lo = null;
		try {
			InputStream is = userExcelFile.getInputStream();
			lo = ExcelUtils.readProductExcel(is);
		} catch (Exception e) {
			e.printStackTrace();
			ra.addFlashAttribute("uploadResult", "文件读取失败!");
			return "redirect:/commodityManager/uploadList/list";
		}
		
		if( null == lo ){
			logger.debug("文件读取失败--->>>耗时:{}", (System.currentTimeMillis()-startTime) );

			ra.addFlashAttribute("uploadResult", "文件读取失败!");
			return "redirect:/commodityManager/uploadList/list";
		}
		
		int totalNumber = lo.size();
		
		logger.debug("读取文件得到的总行数是{}行",totalNumber);
		
//---------------------------------------------------------------空文件判断----->>>>>
		if ( lo.isEmpty() || totalNumber <= 2 ){
			logger.debug("空文件--->>>耗时:{}", (System.currentTimeMillis()-startTime) );

			ra.addFlashAttribute("uploadResult", "空文件");			
			return "redirect:/commodityManager/uploadList/list";
		}
		
		StringBuilder errors = new StringBuilder();

//---------------------------------------------------------------文件本身基本数据格式正确性校验----->>>>>
		Map<String, Object> categoryMap = commodityManagerService.excelFileSelfCheck(lo, totalNumber, errors);
		
		logger.debug("文件本身基本数据格式正确性校验完毕---结果：错误提示信息为：<{}>",errors.toString());
		
		if ( StringUtils.isNotBlank(errors.toString()) ){
			logger.debug("完成文件本身基本数据格式正确性校验 --->>> 耗时:{}", (System.currentTimeMillis()-startTime) );
			
			ra.addFlashAttribute("uploadResult", errors.toString());
			return "redirect:/commodityManager/uploadList/list";
		}
		
//---------------------------------------------------------------单位信息和品牌信息数据库新增处理----->>>>>
		Map<String,Object> unitMap = new HashMap<String, Object>();
		Map<String,Object> brandMap = new HashMap<String, Object>();
		commodityManagerService.saveUnitAndBrandInfo(lo, totalNumber, unitMap, brandMap);
		
		logger.debug("单位信息和品牌信息数据库新增处理完毕...此商家共存在单位{}个,品牌{}个",unitMap.size(),brandMap.size());
		
//---------------------------------------------------------------文件商品信息校验和内存存储处理----->>>>>
		List<Object[]> productBatchInsertArr = new ArrayList<Object[]>();
		Map<String,Map<String,List<ProductStock>>> serialMap = commodityManagerService.productInfoCheck(lo, totalNumber, errors, categoryMap, unitMap,brandMap, productBatchInsertArr);
		
		logger.debug("文件商品信息校验和内存存储处理完毕---此次导入共将新增商品信息{}个",serialMap.size());
		
//---------------------------------------------------------------文件库存信息校验----->>>>>
		commodityManagerService.productStockInfoCheck(errors, serialMap);
		
		logger.debug("文件库存信息校验完毕---结果：错误提示信息为：{}",errors.toString());

		if ( StringUtils.isNotBlank(errors.toString()) ){
			logger.debug("完成文件库存信息校验 --->>> 耗时:{}", (System.currentTimeMillis()-startTime) );
			
			ra.addFlashAttribute("uploadResult", errors.toString());
			return "redirect:/commodityManager/uploadList/list";
		}
	
//---------------------------------------------------------------文件库存信息内存存储处理----->>>>>
		List<Object[]> productStockBatchInsertArr = commodityManagerService.productStockAssemble(serialMap);
		
		logger.debug("文件库存信息内存存储处理完毕---总共将新增库存信息{}个",productStockBatchInsertArr.size());
	
//---------------------------------------------------------------批量商品信息和库存信息数据库存储操作----->>>>>
		if( null != productBatchInsertArr && productBatchInsertArr.size() > 0 ){
			if( null != productStockBatchInsertArr && productStockBatchInsertArr.size() > 0 ){
				if(commodityManagerService.batchProductSave(productBatchInsertArr)){ 	// 保存商品信息
					boolean flag = commodityManagerService.save(productStockBatchInsertArr); // 保存库存	
					
					logger.debug("批量导入商品信息成功{}!",flag);
					
					logger.debug("批量导入商品成功--->>>耗时:{}", (System.currentTimeMillis()-startTime) );
					
					ra.addFlashAttribute("uploadResult", "导入成功");
					return "redirect:/commodityManager/uploadList/list";
				};
			}
		}
		
		logger.debug("出现异常情况--->>>耗时:{}", (System.currentTimeMillis()-startTime) );
		ra.addFlashAttribute("uploadResult", "出现异常情况,导入失败!");
		return "redirect:/commodityManager/uploadList/list";
	}
}
