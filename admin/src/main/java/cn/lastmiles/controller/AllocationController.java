package cn.lastmiles.controller;


import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

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

import cn.lastmiles.bean.AllocationConfig;
import cn.lastmiles.bean.AllocationRecord;
import cn.lastmiles.bean.AllocationRecordStock;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AllocationConfigService;
import cn.lastmiles.service.AllocationService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.ProductStockService;
import cn.lastmiles.common.utils.ObjectUtils;
@Controller
@RequestMapping("allocation")
public class AllocationController {
	private final static Logger logger = LoggerFactory.getLogger(AllocationController.class);
	
	@Autowired
	private AllocationService allocationService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductStockService productStockService;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private AllocationConfigService allocationConfigService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/allocation/list";
	}
	
	/**
	 * 库存调拨列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		boolean configFlag=true;
		
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("configFlag",configFlag);
		return "allocation/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long fromStoreId,Long toStoreId,String allocationNumber,Integer status,String beginTime,String endTime, Page page, Model model) {
		logger.debug("fromStoreId={},toStoreId={},allocationNumber={},status={},beginTime={},endTime={}",fromStoreId,toStoreId,allocationNumber,status,beginTime,endTime);
		String fromStoreIdString="";
		String toStoreIdString="";
		if(fromStoreId!=null){
			fromStoreIdString=fromStoreId.toString();
		}
		if(toStoreId!=null){
			toStoreIdString=toStoreId.toString();
		}
		StringBuffer storeIdString = new StringBuffer();
		if(fromStoreId==null && toStoreId==null){//如果登录的店不是总店,调入和调出仓库为空，则查出调入或调出仓库的列表(fromStoreId or toStoreId)
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
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else if(fromStoreId!=null && toStoreId==null){//如果调入仓库不为空
			if(!SecurityUtils.isMainStore()){//不是总店登录
				if(!ObjectUtils.equals(SecurityUtils.getAccountStoreId(), fromStoreId)){
					toStoreIdString=SecurityUtils.getAccountStoreId().toString();
				}
			}
		}else if(fromStoreId==null && toStoreId!=null){//如果调出仓库不为空
			if(!SecurityUtils.isMainStore()){//不是总店登录
				if(!ObjectUtils.equals(SecurityUtils.getAccountStoreId(), toStoreId)){
					fromStoreIdString=SecurityUtils.getAccountStoreId().toString();
				}
			}
		}
		logger.debug("fromStoreIdString={},toStoreIdString={}",fromStoreIdString,toStoreIdString);
		model.addAttribute("data", allocationService.findAllPage(fromStoreIdString,toStoreIdString,allocationNumber.trim(),status,beginTime,endTime, storeIdString.toString(),page));
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("storeId",SecurityUtils.getAccountStoreId());
		return "allocation/list-data";
		
	}
	
	// 弹窗测试
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
			page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		}else{
			Store s=storeService.findTopStore(SecurityUtils.getAccountStoreId());
			List<Store> storeList = storeService.findByParentId(s.getId());
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
			page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		}
		
		model.addAttribute("data", page);
		return "allocation/showModelList-data";
	}
	// 弹窗测试
		@RequestMapping("showModel/list/listFrom-data")
		public String shopListlistFromData(String name, String mobile,Page page, Model model) {
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
				logger.debug("storeIdString.toSting()={}",storeIdString.toString());
				page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
			}else{
				String storeIdS="";
				boolean configFlag=true;
				if(!SecurityUtils.isMainStore()){
					Store store=storeService.findTopStore(SecurityUtils.getAccountStoreId());
					logger.debug("store={}",store);
					AllocationConfig allocationConfig=allocationConfigService.getAllocationConfigByStoreId(store.getId());
					logger.debug("allocationConfig={}",allocationConfig);
					if(allocationConfig ==null){
						configFlag=false;
					}else{
						if(allocationConfig.getStatus()==0){
							configFlag=false;
						}
					}
				}
				
				Store s=storeService.findTopStore(SecurityUtils.getAccountStoreId());
				if(configFlag){
					List<Store> storeList = storeService.findByParentId(s.getId());
					for(int i=0;i<storeList.size();i++){
						if(!ObjectUtils.equals(SecurityUtils.getAccountStoreId(), storeList.get(i).getId())){
							storeIdS+=storeList.get(i).getId().toString()+",";
						}
					}
					storeIdS=storeIdS.substring(0,storeIdS.length()-1);
					logger.debug("storeIdS={}",storeIdS);
				}else{
					storeIdS=s.getId().toString();
				}
				page = shopService.getAllShop(storeIdS,Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
			}
			
			model.addAttribute("data", page);
			return "allocation/showModelList-data";
		}
	// 弹窗测试
	@RequestMapping("showModel/listStore/list-data")
	public String storeListData(String name, String mobile,Page page, Model model) {
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
			page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		}else{
			Store s=storeService.findTopStore(SecurityUtils.getAccountStoreId());
			List<Store> storeList = storeService.findByParentId(s.getId());
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
			page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		}
		model.addAttribute("data", page);
		return "allocation/showModelList-data";
	}
	/**
	 * 跳到库存调拨页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		Store store=storeService.findById(SecurityUtils.getAccountStoreId());
		model.addAttribute("store",store);
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "allocation/add";
	}
	
	@RequestMapping("stockList/list-data")
	public String listData(Long product_storeId,String product_name, Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if(product_storeId==null){
			storeIdString.append(SecurityUtils.getAccountStoreId().toString());
		}else{
			storeIdString.append(product_storeId.toString());
		}
		if( null != product_name){
			product_name = product_name.replaceAll("\\s*", "");
		}
		logger.debug("product_storeId={}",product_storeId);
		String barCode="";
		page = productStockService.findAllocationList(storeIdString.toString(),product_name,barCode,0,null,null,null,null,page);
		
		model.addAttribute("data",page);
		return "allocation/stockList-data";
		
	}
	
	@RequestMapping(value="findProductStockList-by-productStockIds",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> findProductStockList(String productStockIds){
		logger.debug("productStockIds={}",productStockIds);
		List<ProductStock> ps=allocationService.findProductStockList(productStockIds);
		logger.debug("ps={}",ps);
		return ps;
	}
	/**
	 * 查看申请调拨的商品本店是否存在
	 * @param 
	 * @return
	 */
	@RequestMapping(value="findStockExist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> findStockExist(Long toStoreId,String stockIds){
		logger.debug("toStoreId==={},stockIds={}",toStoreId,stockIds);
		List<String> returnData=new ArrayList<String>();
		List<ProductStock> pss = allocationService.findProductStockList(stockIds);
		logger.debug("productStockList={}",pss);
		List<ProductStock> _psList=new ArrayList<ProductStock>();
		//改变库存数量(如果是无属性的商品，条码判断,分店中同一个商品条码相同，如果条码在该商店不存在，不能调拨)
		//如果有属性的，判断属性值是否一致，长度相同，名称相同，不同不能调拨
		boolean flag=true;
		for(int i=0;i<pss.size();i++){
			ProductStock ps=pss.get(i);
			logger.debug("ProductStock={}",ps);
			Product product=allocationService.findProductById(ps.getProductId());
			logger.debug("product={}",product);
			//看商品是有属性还是无属性
			logger.debug("有无属性={}",product.getType().intValue());
			if(product.getType().intValue()==1){//无属性
				logger.debug("barCode={},toStoreId={}",ps.getBarCode(),toStoreId);
				//无属性的要判断分类、品牌、商品名称和条码
				ProductStock productStock=allocationService.findProductStock(ps.getBarCode(),toStoreId,product.getName(),product.getType());
				if(productStock==null){//商品不存在
					//商品不存在，不能调拨
					returnData.add(ps.getAttributeName()+","+"0");
					flag=false;
					break;
				}else{
					_psList.add(productStock);
				}
			}else{//有属性
				boolean attributeFlag=false;
					
				logger.debug("ps.getBarCode()={},product.getName()={},toStoreId={}",ps.getBarCode(),product.getName(),toStoreId);
				List<ProductStock> psList=allocationService.findProductStockList(ps.getBarCode(),product.getName(),toStoreId,0);
				
				logger.debug("ps.getAttributeName()={}",ps.getAttributeName());
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				Arrays.sort(arrs);
				
				for(int j=0;j<psList.size();j++){
					logger.debug("psList.get(j).getAttributeName()={}",psList.get(j).getAttributeName());
					String[] arrS =StringUtil.splitc(psList.get(j).getAttributeName(), '|');
					
					Arrays.sort(arrS);
					if(Arrays.equals(arrs, arrS)){
						attributeFlag=true;
						_psList.add(psList.get(j));
						break;
					}
				}
				if(attributeFlag==false){
					//商品不存在，不能调拨
					returnData.add(ps.getAttributeName()+","+"0");
					flag=false;
					break;
				}
					
			}
		}
		if(flag){//如果找到
			boolean _flag=true;
			for(int i=0;i<_psList.size();i++){
				if(_psList.get(i).getStock().intValue()<0){//如果本店商口是无限库存，不用调拨
					returnData.add(_psList.get(i).getAttributeName()+","+"1");
					_flag=false;
					break;
				}
			}
			if(_flag){
				return null;
			}else{
				return returnData;
			}
		}else{
			return returnData;
		}
	}
	/**
	 * 保存库存调拨
	 * @param 
	 * @return
	 */
	@RequestMapping(value="save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(Long fromStoreId,Long toStoreId,String amounts,String stockIds,String allocationTime,String memo){
		logger.debug("fromStoreId={},toStoreId={},amounts={},stockIds={},memo={}",fromStoreId,toStoreId,amounts,stockIds,memo);
		AllocationRecord ar=new AllocationRecord();
		ar.setId(idService.getId());
		ar.setAccountId(SecurityUtils.getAccountId());
		ar.setFromStoreId(fromStoreId);
		ar.setToStoreId(toStoreId);
		ar.setMemo(memo);
		ar.setStoreId(SecurityUtils.getAccountStoreId());
		ar.setAllocationTime(DateUtils.parse("yyyy-MM-dd",allocationTime));
		if(SecurityUtils.isMainStore()){//登录是总店
			ar.setStatus(1);
		}else{//登录的不是总店
			Store store=storeService.findTopStore(SecurityUtils.getAccountStoreId());
			AllocationConfig allocationConfig=allocationConfigService.getAllocationConfigByStoreId(store.getId());
			if(allocationConfig.getStatus().intValue()==1 || ObjectUtils.equals(store.getId(), fromStoreId)){
				ar.setStatus(1);
			}else{
				ar.setStatus(0);
			}
		}
		allocationService.save(ar);
		
		List<ProductStock> ps=allocationService.findProductStockList(stockIds);
		List<AllocationRecordStock> arss=new ArrayList<AllocationRecordStock>();
		String[] amountArr=amounts.split(",");
		for(int i=0;i<ps.size();i++){
			AllocationRecordStock ars=new AllocationRecordStock();
			ars.setAllocationRecordId(ar.getId());
			ars.setProductName(ps.get(i).getProductName());
			ars.setBarCode(ps.get(i).getBarCode());
			ars.setCategoryName(ps.get(i).getCategoryName());
			logger.debug("ps.get(i).getAttributeName()={}",ps.get(i).getAttributeName());
			
			String attributeName = "";
			if (StringUtils.isNotBlank(ps.get(i).getAttributeName())){
				String[] arr = StringUtil.splitc(ps.get(i).getAttributeName(), '|');
				
				if(arr.length>1){
					for (int j = 1; j < arr.length ;j++ ){
						if ("".equals(attributeName)){
							attributeName += arr[j];
						}else {
							attributeName += "|" + arr[j];
						}
					}
					//attributeName=attributeName.substring(0, attributeName.length()-1);
				}
			}
			logger.debug("attributeName={}",attributeName);
			ars.setAttributeName(attributeName);
			ars.setAmount(Double.parseDouble(amountArr[i]));
			ars.setStockId(ps.get(i).getId());
			arss.add(ars);
		}
		
		for(int i=0;i<arss.size();i++){
			allocationService.saveAllocationRecordStock(arss.get(i));
		}
		return "1";
	}
	
	@RequestMapping(value="info/showMode/{id}" )
	public String info(@PathVariable Long  id, Model model){
		logger.debug("allocation-info-ID IS :"+id);
		List<AllocationRecordStock> arss = allocationService.findById(id);
		model.addAttribute("arss",arss);
		return "allocation/info";
	}
	@RequestMapping(value="overInfo/showMode/{id}" )
	public String overInfo(@PathVariable Long  id, Model model){
		logger.debug("allocation-info-ID IS :"+id);
		List<AllocationRecordStock> arss = allocationService.findById(id);
		model.addAttribute("arss",arss);
		return "allocation/lastInfo";
	}
	@RequestMapping(value="auditInfo/showMode/{id}" )
	public String auditInfo(@PathVariable Long  id, Model model){
		logger.debug("allocation-auditInfo-ID IS :"+id);
		List<AllocationRecordStock> arss = allocationService.findById(id);
		model.addAttribute("arss",arss);
		return "allocation/auditInfo";
	}
	@RequestMapping(value="sendInfo/showMode/{id}" )
	public String sendInfo(@PathVariable Long  id, Model model){
		logger.debug("allocation-sendInfo-ID IS :"+id);
		List<AllocationRecordStock> arss = allocationService.findById(id);
		model.addAttribute("arss",arss);
		return "allocation/sendInfo";
	}
	@RequestMapping(value="confirmInfo/showMode/{id}" )
	public String confirmInfo(@PathVariable Long  id, Model model){
		logger.debug("allocation-confirmInfo-ID IS :"+id);
		List<AllocationRecordStock> arss = allocationService.findById(id);
		model.addAttribute("arss",arss);
		return "allocation/confirmInfo";
	}
	@RequestMapping(value="typeChange/change-by-allocationId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String typeChangeByorderId(Long id,Integer status){
		logger.debug("状态==={},id={}",status,id);
		allocationService.typeChangeByallocationId(id,status,SecurityUtils.getAccountId());
		return "1";
	}
	@RequestMapping(value="confirm/change-by-allocationId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String confirmTypeChage(Long id,Integer status,String confirmAmounts){
		logger.debug("状态==={},id={},confirmAmounts={}",status,id,confirmAmounts);
		allocationService.confirmStockAmount(id,status,confirmAmounts,SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId());
		return "1";	
		
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
			Long fromStoreId,Long toStoreId,String allocationNumber,Integer status,String beginTime,String endTime) throws ParseException {
		logger.debug("fromStoreId={},toStoreId={},allocationNumber={},status={},beginTime={},endTime={}",fromStoreId,toStoreId,allocationNumber,status,beginTime,endTime);
		StringBuffer storeIdString = new StringBuffer();
		if(fromStoreId==null && toStoreId==null){//如果登录的店不是总店,调入和调出仓库为空，则查出调入或调出仓库的列表(fromStoreId or toStoreId)
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
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}
		String fileName = "库存调拨列表";
		List<AllocationRecord> arList = allocationService.findAllBySearch(fromStoreId,toStoreId,allocationNumber.trim(),status,beginTime,endTime, storeIdString.toString());
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("库存调拨列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<14;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("调拨时间");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("调拨单号");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("调出仓库");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("调入仓库");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("调拨状态");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("商品条码");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("商品规格(属性值)");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("调拨数量");
				cell.setCellStyle(style);
			}else if(i==9){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("实际到货数量");
				cell.setCellStyle(style);
			}else if(i==10){
				cell = row.createCell(i);// 第一行第11格
				cell.setCellValue("操作人");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell(i);// 第一行第12格
				cell.setCellValue("备注");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell(i);// 第一行第13格
				cell.setCellValue("创建时间");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell(i);// 第一行第14格
				cell.setCellValue("完成时间");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",arList.size());
		int index=1;
		if (!arList.isEmpty()) {
			for (int i = 0; i < arList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				
				AllocationRecord ar=arList.get(i);
				List<AllocationRecordStock> arss = allocationService.findById(ar.getId());
				for(int j=0;j<arss.size();j++){
					if(j==0){
						row = sheet.createRow((int) index);
						
						row.createCell(0).setCellValue(DateUtils.format(ar.getAllocationTime(), "yyyy-MM-dd"));//申请调拨时间
						row.createCell(1).setCellValue(ar.getAllocationNumber());//调拨单号	
						row.createCell(2).setCellValue(ar.getFromStoreName());//调出仓库
						row.createCell(3).setCellValue(ar.getToStoreName());//调入仓库
						
						if(ar.getStatus().intValue()==0){
							row.createCell(4).setCellValue("待审核");//调拨状态
						}else if(ar.getStatus().intValue()==1){
							row.createCell(4).setCellValue("待发货");//调拨状态
						}else if(ar.getStatus().intValue()==2){
							row.createCell(4).setCellValue("待收货");//调拨状态
						}else if(ar.getStatus().intValue()==3){
							row.createCell(4).setCellValue("已拒绝");//调拨状态
						}else if(ar.getStatus().intValue()==4){
							row.createCell(4).setCellValue("已完成");//调拨状态
						}
						row.createCell(5).setCellValue(arss.get(j).getProductName());//商品名称
						row.createCell(6).setCellValue(arss.get(j).getBarCode());//商品条码
						row.createCell(7).setCellValue(arss.get(j).getAttributeName());//商品规格(属性值)
						row.createCell(8).setCellValue(arss.get(j).getAmount());//数量
						row.createCell(9).setCellValue(arss.get(j).getLastAmount()!=null?arss.get(j).getLastAmount().toString():"");//实际数量
						row.createCell(10).setCellValue(ar.getAccountMobile());//操作人
						row.createCell(11).setCellValue(ar.getMemo());//备注
						row.createCell(12).setCellValue(DateUtils.format(ar.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						row.createCell(13).setCellValue(ar.getFinishTime()!=null?DateUtils.format(ar.getFinishTime(), "yyyy-MM-dd HH:mm:ss"):"");//完成时间
						index++;
						
					}else{
						row = sheet.createRow((int) index);
						row.createCell(5).setCellValue(arss.get(j).getProductName());//商品名称
						row.createCell(6).setCellValue(arss.get(j).getBarCode());//商品条码
						row.createCell(7).setCellValue(arss.get(j).getAttributeName());//商品规格(属性值)
						row.createCell(8).setCellValue(arss.get(j).getAmount());//数量
						row.createCell(9).setCellValue(arss.get(j).getLastAmount()!=null?arss.get(j).getLastAmount().toString():"");//实际数量
						index++;
					}
					
				}
				index++;
				
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
