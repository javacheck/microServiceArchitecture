package cn.lastmiles.controller;
/**
 * createDate : 2016年3月9日上午10:41:35
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.StoreServicePackage;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.bean.UserStoreServicePackage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.ExcelUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.StoreServicePackageService;
import cn.lastmiles.service.UserAccountManagerService;
import cn.lastmiles.service.UserLevelDefinitionService;
import cn.lastmiles.service.UserService;
import cn.lastmiles.service.UserstoreservicepackageService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("userAccountManager")
public class UserAccountManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserAccountManagerController.class);
	@Autowired
	private StoreService storeService;
	@Autowired
	private UserAccountManagerService userAccountManagerService;
	@Autowired
	private StoreServicePackageService storeServicePackageService;
	@Autowired
	private UserstoreservicepackageService userStoreServicePackageService;
	@Autowired
	private IdService idService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserLevelDefinitionService userLevelDefinitionService;
	
	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "userAccountManager/list";
	}

	@RequestMapping("list-data")
	public String listData(Long storeId,String mobile,String cardNum, Page page, Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){
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
					storeIdString.append(storeId);
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		model.addAttribute("data", userAccountManagerService.list(mobile,cardNum,storeIdString.toString(), page));
		return "userAccountManager/list-data";
	}
	
	@RequestMapping("upgrade/list")
	public String upgradeList(Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "userAccountManager/upgrade-list";
	}

	@RequestMapping("upgrade/list-data")
	public String upgradeListData(Long storeId,String mobile,String cardNum, Page page, Model model) {
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){
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
					storeIdString.append(storeId);
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		model.addAttribute("data", userAccountManagerService.list(mobile,cardNum,storeIdString.toString(),null, page));
		return "userAccountManager/upgrade-list-data";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(Model model) {
		UserCard uc = new UserCard();
		uc.setCardNum(idService.getId().toString());
		if( SecurityUtils.isStore() ? (SecurityUtils.isMainStore() ? false : true ) : false){
			uc.setStoreId(SecurityUtils.getAccountStoreId());
		}
		model.addAttribute("userCard",uc);
		model.addAttribute("isStore",SecurityUtils.isStore() ? (SecurityUtils.isMainStore() ? false : true ) : false);
//		model.addAttribute("userLevelList",userLevelDefinitionService.findByStoreId(SecurityUtils.getAccountStoreId()));
		return "/userAccountManager/add";
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
		}
		page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "userAccountManager/showModelList-data";
	}
		
	@RequestMapping(value = "list/ajax/saveServicePackage")
	@ResponseBody
	public StoreServicePackage saveServicePackage(StoreServicePackage storeServicePackage) {
		storeServicePackage.setAccountId(SecurityUtils.getAccountId());
		storeServicePackageService.save(storeServicePackage);
		return storeServicePackage;
	}
	
	@RequestMapping(value = "list/ajax/checkServicePackageName")
	@ResponseBody
	public int checkServicePackageName(Long storeId,String name) {
		return storeServicePackageService.checkServicePackageName(storeId,name);
	}
	
	@RequestMapping(value = "list/ajax/getServicePackageSelect")
	@ResponseBody
	public List<StoreServicePackage> getServicePackageSelect(Long storeId) {
		return storeServicePackageService.getServicePackageSelect(storeId);
	}
	
	@RequestMapping(value = "list/ajax/checkCardNumBystoreId")
	@ResponseBody
	public int checkCardNumBystoreId(Long id,Long storeId,String cardNum) {
		return userAccountManagerService.checkCardNumBystoreId(id,storeId,cardNum);
	}
	
	@RequestMapping(value = "list/ajax/checkMobileBystoreId")
	@ResponseBody
	public int checkMobileBystoreId(Long id,Long storeId,String mobile) {
		return userAccountManagerService.checkMobileBystoreId(id,storeId,mobile,null);
	}
	
	@RequestMapping(value = "list/ajax/getRandomCardNumber")
	@ResponseBody
	public Long getRandomCardNumber() {
		return idService.getId();
	}
	
	@RequestMapping(value = "list/ajax/saveRandomCardNumber")
	@ResponseBody
	public int saveRandomCardNumber(String cardID,String changeCardNumber) {
		int check = userAccountManagerService.checkCardNumBystoreId(Long.parseLong(cardID),userAccountManagerService.findById(Long.parseLong(cardID)).getStoreId(),changeCardNumber);
		if( check == 0 ){
			return userAccountManagerService.updateRandomCardNumber(cardID,changeCardNumber);			
		} 
		return 2;
	}
	
	@RequestMapping(value = "list/ajax/getChangeCardInfo")
	@ResponseBody
	public UserCard getChangeCardInfo(String cardID) {
		return userAccountManagerService.getChangeCardInfo(cardID);
	}
	
	@RequestMapping(value = "saveOrUpdateUserCard", method = RequestMethod.POST)
	public String saveOrUpdateUserCard(UserCard userCard,int fun_Select) {
		userCard.setAccountId(SecurityUtils.getAccountId());
		if( null == userCard.getId() ){ // 新增
			userAccountManagerService.save(userCard,fun_Select);			
		} else { // 修改
			userAccountManagerService.update(userCard,fun_Select);
		}
		return "redirect:/userAccountManager/list";
	}
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		UserCard userCard = userAccountManagerService.findById(id);
		
		Long storeId = userCard.getStoreId();
		StringBuffer linkArray = new StringBuffer();
		List<StoreServicePackage> storeServicePackageArray = storeServicePackageService.findByStoreId(storeId);
		if( null != storeServicePackageArray && storeServicePackageArray.size() > 0 ){
			for (StoreServicePackage storeServicePackage : storeServicePackageArray) {
				linkArray.append(storeServicePackage.getId() + "," + storeServicePackage.getTimes());
				linkArray.append(";");
			}
			model.addAttribute("storeServicePackageArray",linkArray);
		}
		model.addAttribute("isStore",SecurityUtils.isStore() ? (SecurityUtils.isMainStore() ? false : true ) : false);	
		model.addAttribute("userCard",userCard);
		return "/userAccountManager/update";
	}
	
	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public String toDetails(@PathVariable Long id, Model model) {
		UserCard userCard = userAccountManagerService.findById(id);
		
		List<UserStoreServicePackage> userStoreServicePackageArray = userStoreServicePackageService.findByUserCardId(id);
		model.addAttribute("userStoreServicePackageArray",userStoreServicePackageArray);
		model.addAttribute("userCard",userCard);
		logger.debug("userCard.getMobile()={},userCard.getStoreId()={}",userCard.getMobile(),userCard.getStoreId());
		User user = userService.searchOrganization_UserByMobile$StoreId(userCard.getMobile(),userCard.getStoreId());
		
		logger.debug("点击详情，查询到会员卡关联的会员信息是：{}",user);
		
		model.addAttribute("user",user);
		return "/userAccountManager/details";
	}
	
	@RequestMapping(value = "updateUserCardStatus", method = RequestMethod.POST)
	public String updateUserCardStatus(Long id,Integer status) {
		//userAccountManagerService.updateUserCardStatus(id,status);
		return "redirect:/userAccountManager/list";
	}
	
	@RequestMapping(value = "updateDetail", method = RequestMethod.POST)
	public String updateDetail(UserCard userCard,User user,String userId,String birthTime,String createdDate) {
		if( StringUtils.isNotBlank(birthTime) ){
			user.setBirthDay(DateUtils.parse("yyyy-MM-dd", birthTime));			
		}
		userCard.setCreatedTime(DateUtils.parse("yyyy-MM-dd", createdDate));
		user.setId(Long.parseLong(userId));
		
		logger.debug("userCard is {} , user is {}",userCard,user);
		
		userAccountManagerService.updateDetail(userCard,user,SecurityUtils.isMainStore());
		return "redirect:/userAccountManager/list";
	}
	
	@RequestMapping(value = "ajax/checkConnectedMobile",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String checkConnectedMobile(String mobile,Long storeId) {
		logger.debug("checkConnectedMobile mobile is {} , storeId is {}",mobile,storeId);
		String flag=userAccountManagerService.checkConnectedMobile(mobile, storeId, SecurityUtils.isMainStore());
		return flag;
	}
	
	
	@RequestMapping(value = "ajax/reportUserAccountManagerToExcel")
	public String reportUserAccountManagerToExcel(HttpServletResponse response,String reportMobile,String reportCardNum,Long storeId) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){
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
					storeIdString.append(storeId);
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		List<UserCard> userCardList = userAccountManagerService.list(storeIdString.toString(),reportMobile,reportCardNum);
		String fileOrTitleName = "会员账户管理列表";
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
//			response.setContentType("application/vnd.ms-excel"); 
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			//暂不考虑大数据量的问题和Excel版本的问题(2015.12.16)
			exportExcel(workbook,fileOrTitleName,getHeaders(),userCardList);
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] getHeaders() {  
        return new String[] { "会员手机号","姓名","卡号","账户余额","账户积分","会员等级","享受折扣","服务套餐","发卡人","发卡日期","卡状态","发卡商家" ,"会员备注"};  
	} 
	
	public Map<Integer,String> getTypeMap(){
		Map<Integer,String> typeMap = new HashMap<Integer, String>();
		typeMap.put(Constants.UserCard.STATUS_ON, "启用");
		typeMap.put(Constants.UserCard.STATUS_OFF, "禁用");
		return typeMap;
	}
	
	public void exportExcel(HSSFWorkbook workbook, String title, String[] headers, Collection<?> dataset) {
	     
	      HSSFSheet sheet = workbook.createSheet(title);
	      sheet.setDefaultColumnWidth(20);
	      HSSFCellStyle style = workbook.createCellStyle();
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	      
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (int i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      
	    if(!dataset.isEmpty()){
	    	Map<Integer,String> typeMap = getTypeMap();
	    	Iterator<?> it = dataset.iterator();
	    	int index = 0;
	    	while(it.hasNext()){
	    		index++;
	    		row = sheet.createRow(index);
	    		row.setRowStyle(style);
//	    		"会员手机号","姓名","卡号","账户余额","账户积分","会员等级","享受折扣","服务套餐","发卡人","发卡日期","卡状态","发卡商家"
	    		UserCard userCard = (UserCard) it.next();
	    		row.createCell(0).setCellValue( userCard.getMobile() ); // 会员手机号
	    		row.createCell(1).setCellValue( userCard.getName() ); // 姓名
	    		row.createCell(2).setCellValue(userCard.getCardNum()); // 卡号
	    		row.createCell(3).setCellValue(userCard.getBalance()); // 账户余额
	    		row.createCell(4).setCellValue(userCard.getPoint()); // 账户积分
	    		row.createCell(5).setCellValue( (null == userCard.getUserLevelName()) ? "" : userCard.getUserLevelName()); // 会员等级
	    		row.createCell(6).setCellValue( (null == userCard.getUserLevelDiscount()) ? "" : userCard.getUserLevelDiscount()+""); // 享受折扣
	    		row.createCell(7).setCellValue((null == userCard.getServiceNameArray()) ? "" : userCard.getServiceNameArray()); // 服务套餐
	    		row.createCell(8).setCellValue( (null == userCard.getAccountName()) ? "" : userCard.getAccountName() ); // 发卡人
	    		row.createCell(9).setCellValue(DateUtils.format(userCard.getCreatedTime(),"yyyy-MM-dd HH:mm:ss")); // 发卡日期
	    		
	    		row.createCell(10).setCellValue(typeMap.get(userCard.getStatus())); // 卡状态
	    		row.createCell(11).setCellValue( (null == userCard.getStoreName()) ? "" : userCard.getStoreName()); // 发卡商家
	    		row.createCell(12).setCellValue( (null == userCard.getMemo()) ? "" : userCard.getMemo()); // 会员备注
	    	}
	    	logger.debug("共导出会员账户管理记录数据:{}条",index);
	    	index = 0;
	    }
	}
	
	@RequestMapping(value = "ajax/updateStatus-by-Id")
	@ResponseBody
	public int updateStatusById(Integer status,String[] cacheArray) {
		logger.debug("status is {} ,cacheArray is {}",status ,cacheArray);
		return userAccountManagerService.updateStatusById(status,cacheArray);
	}
	
	@RequestMapping(value = "ajax/upgrade-update")
	@ResponseBody
	public int upgradeUpdate(Long storeId,String mobile,String cardNum,String[] cacheArray) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){
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
					storeIdString.append(storeId);
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		logger.debug("upgradeUpdate cacheArray is {},storeIdString is {},mobile is {},cardNum is {}",cacheArray,storeIdString.toString(),mobile,cardNum);
		return userAccountManagerService.upgradeUpdate(storeIdString.toString(),mobile,cardNum,cacheArray);
	}
	
	@RequestMapping("uploadList/list")
	public String uploadList() {
		return "userAccountManager/uploadList";
	}
	
    /**
     * 在输出流中导出excel。
     * @param excelName 导出的excel名称 包括扩展名
     * @param sheetName 导出的sheet名称
     * @param fieldName 列名数组
     * @param data 数据组
     * @param response response
     * @throws IOException 转换流时IO错误
     */
    public void makeStreamExcel(String excelName, String sheetName, String[] fieldName, List<Object[]> data,HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream os = null;
        response.reset(); // 清空输出流
        os = response.getOutputStream(); // 取得输出流
        
        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
        	excelName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
		} else {  
			excelName = URLEncoder.encode(excelName, "UTF-8");  
		} 
        response.setHeader("Content-Disposition", "attachment; filename=" + excelName );  
		//response.setContentType("application/octet-stream;charset=utf-8");
		response.setCharacterEncoding("UTF-8"); 
        
        // 在内存中生成工作薄
        HSSFWorkbook workbook = ExcelUtils.makeWorkBook(sheetName, fieldName, data);
        os.flush();
        workbook.write(os);
        workbook.close();
        os.close();
    }
    
	/**
	 * poi导出excel
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/exportModelExcel", produces = MediaType.APPLICATION_JSON_VALUE)
	public void exportModelExcelBySearch(HttpServletResponse response,HttpServletRequest request) throws ParseException {
		List<Object[]> data = new ArrayList<Object[]>();
		String[] fieldName = {"会员手机号","姓名","卡号","会员备注"};
		try {
			makeStreamExcel("user.xls","会员导入模板",fieldName,data,request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "uploadUserExcel", method = RequestMethod.POST)
	public String uploadUserExcel(@RequestParam("userExcelFile") MultipartFile userExcelFile,RedirectAttributes ra) throws IOException {
		// 1、将excel数据读入到内存数组中
		// 2、比较第一列不能为空且是手机号码格式（用一个数组列表保存起来进行对比）
		// 3、比较第一列不能有重复（数组内部对比）
		// -------------------去掉------------------4、比较第二列不能为空（用一个数组列表保存起来进行对比）
		// 5、比较第三列只能为数字组成的数据
		// 6、比较第三列不能有重复（数组内部对比）
		// 7、从数据库中查询出导入商家的所有会员手机号,然后跟第一列的数组对比,不能有重复
		// 8、从数据库中查询出导入商家的所有会员卡号,然后跟第三列的数组对比,不能有重复
		// 9、比较第三列的数组数据，如果有空的,则默认给ID
		// 9、如果都没错，则逐条的进行保存数据
		// 手机号，姓名，卡号，备注
		StringBuilder errors = new StringBuilder();
		
		InputStream is = userExcelFile.getInputStream();
		List<List<String>> lo = ExcelUtils.simpleExcel(is);
		int totalNumber = lo.size();
		logger.debug("读取的总的行数是{}行",totalNumber);
		
		if (lo.isEmpty() || totalNumber <= 1){
			ra.addFlashAttribute("uploadResult", "空文件");
			return "redirect:/userAccountManager/uploadList/list";
		}
		
		List<UserCard> userCardList = userAccountManagerService.getAllUserCardByStoreId(SecurityUtils.getAccountStoreId());
		Set<String> mobiles = new HashSet<String>();
		Set<String> nums = new HashSet<String>();
		for (int i = 1; i < totalNumber; i++){
			List<String> row = lo.get(i);
			String mobile = row.get(0);
			String name = row.get(1);
			String cardNum = row.get(2);
			String memo = row.get(3);
			
			//手机号码校验
			if (!StringUtils.isMobile(mobile)){
				errors.append("第"+(i+1)+"行手机号码不正确<br/>");
			}else {
				for (UserCard uc : userCardList){
					if (ObjectUtils.equals(uc.getMobile(), mobile)){
						errors.append("第"+(i+1)+"行手机号码已经存在<br/>");
					}
				}
				//检查文件中手机号是否重复
				for (int j = 1; j < totalNumber; j++){
					if (i != j){
						String _mobile = lo.get(j).get(0);
						if (ObjectUtils.equals(_mobile, mobile)){
							mobiles.add(mobile);
						}
					}
				}
				
			}
			
			//姓名校验
			if (StringUtils.isNotBlank(name) && name.length() > 10){
				errors.append("第"+(i+1)+"行姓名长度过长<br/>");
			}
			//卡号校验
			if (StringUtils.isNotBlank(cardNum)){
				if (cardNum.length() > 20){
					errors.append("第"+(i+1)+"行卡号长度过长<br/>");
				}else {
					//卡号只能数字
					if (!StringUtil.containsOnlyDigits(cardNum)){
						errors.append("第"+(i+1)+"行卡号只能是数字<br/>");
					}
					
					for (UserCard uc : userCardList){
						if (ObjectUtils.equals(uc.getCardNum(), cardNum)){
							errors.append("第"+(i+1)+"卡号已经存在<br/>");
						}
					}
					
					//检查文件中卡号是否重复
					for (int j = 1; j < totalNumber; j++){
						if (i != j){
							String _num = lo.get(j).get(2);
							if (ObjectUtils.equals(_num, cardNum)){
								nums.add(_num);
							}
						}
					}
					
				}
			}
			//备注校验
			if (StringUtils.isNotBlank(memo) && memo.length() > 20){
				errors.append("第"+(i+1)+"行备注长度过长<br/>");
			}
		}
		
		if (mobiles.size() > 0){
			for (String m : mobiles){
				errors.append("手机号码" + m + "重复<br/>");
			}
		}
		
		if (nums.size() > 0){
			for (String m : nums){
				errors.append("卡号" + m + "重复<br/>");
			}
		}
		
		if (StringUtils.isNotBlank(errors.toString())){
			ra.addFlashAttribute("uploadResult", errors.toString());
			return "redirect:/userAccountManager/uploadList/list";
		}else {
			//涉及到比较多的逻辑判断，做不了批量插入，以后会员多，有性能问题可考虑异步处理
			userAccountManagerService.export(lo,SecurityUtils.getAccountStoreId(),SecurityUtils.getAccountId());
		}
		
//		List<Object[]> cacheList = new ArrayList<Object[]>();
//		for (List<String> list : lo) {
//			Object[] o = list.toArray();
//			cacheList.add(o);
//		}
//		lo = null;
//		int uploadResult = 0;
//		for (Object[] o : cacheList) {
//			if( null == o[0] || StringUtils.isBlank(o[0]+"") ){ // 第一列数据存在空数据
//				uploadResult = 1;
//				break;
//			}
//		}
//		if( uploadResult == 1 ){
//			ra.addFlashAttribute("uploadResult", uploadResult);
//			return "redirect:/userAccountManager/uploadList/list";
//		}
//		for (Object[] o : cacheList) {
//			if( !StringUtils.isMobile(o[0]+"") ){
//				uploadResult = 2;
//				break;
//			}
//		}
//		if( uploadResult == 2 ){
//			ra.addFlashAttribute("uploadResult", uploadResult);
//			return "redirect:/userAccountManager/uploadList/list";
//		}
//		
//		List<String> mobileArray = new ArrayList<String>();
//		for (Object[] o : cacheList) {
//			if(-1 != mobileArray.indexOf((o[0]+"")) ){
//				uploadResult = 3;
//				break;
//			}
//			mobileArray.add((o[0]+""));
//		}
//		
//		if( uploadResult == 3 ){
//			ra.addFlashAttribute("uploadResult", uploadResult);
//			return "redirect:/userAccountManager/uploadList/list";
//		}
//		
//		for (Object[] o : cacheList) {
//			if( null == o[2] ){
//				continue;
//			}
//			if( !StringUtils.isPositiveIntegral(o[2]+"") ){
//				uploadResult = 4;
//				break;
//			}
//		}
//		
//		if( uploadResult == 4 ){
//			ra.addFlashAttribute("uploadResult", uploadResult);
//			return "redirect:/userAccountManager/uploadList/list";
//		}
//		
//		List<String> cardNumberArray = new ArrayList<String>();
//		for (Object[] o : cacheList) {
//			if(-1 != cardNumberArray.indexOf((o[2]+""))){
//				uploadResult = 5;
//				break;
//			}
//			cardNumberArray.add((o[2]+""));
//		}
//		
//		if( uploadResult == 5 ){
//			ra.addFlashAttribute("uploadResult", uploadResult);
//			return "redirect:/userAccountManager/uploadList/list";
//		}
//		
//		if( null != userCardList ){
//			for (UserCard userCard : userCardList) {
//				String oldMobile = userCard.getMobile();
//				boolean flag = false;
//				for (String mobile : mobileArray) {
//					if( ObjectUtils.equals(oldMobile, mobile) ){
//						flag = true;
//						break;
//					}
//				}
//				if(flag){
//					uploadResult = 6;
//					break;
//				}
//			}
//			if( uploadResult == 6 ){
//				ra.addFlashAttribute("uploadResult", uploadResult);
//				return "redirect:/userAccountManager/uploadList/list";
//			}
//			for (UserCard userCard : userCardList) {
//				String oldCardNum = userCard.getCardNum();
//				boolean flag = false;
//				for (String cardNum : cardNumberArray) {
//					if( ObjectUtils.equals(oldCardNum, cardNum) ){
//						flag = true;
//						break;
//					}
//				}
//				if(flag){
//					uploadResult = 7;
//					break;
//				}
//			}
//			if( uploadResult == 7 ){
//				ra.addFlashAttribute("uploadResult", uploadResult);
//				return "redirect:/userAccountManager/uploadList/list";
//			}
//		}
//		
//		
//		for (Object[] o : cacheList) {
//			if(o[2] instanceof Integer){
//				logger.debug("---------integer----------");
//			} else {
//				logger.debug("----------------no integer---------------");
//			}
//			logger.debug("-------->{}",o);
//		}
		ra.addFlashAttribute("uploadResult", "导入成功");
		return "redirect:/userAccountManager/uploadList/list";
	}
	
	@RequestMapping(value = "list/ajax/checkUserMobile")
	@ResponseBody
	public User checkMobileBystoreId(Long storeId,String mobile) {
		return userService.searchOrganization_UserByMobile$StoreId(mobile,storeId);
	}
	
	@RequestMapping(value = "list/ajax/getLevel")
	@ResponseBody
	public List<UserLevelDefinition> getLevel(Long storeId,String mobile) {
		List<UserLevelDefinition> list = userLevelDefinitionService.findByMobileAndStore(mobile, storeId);
		logger.debug("获取的会员等级列表为：{}",list);
		return list;
	}
	
}