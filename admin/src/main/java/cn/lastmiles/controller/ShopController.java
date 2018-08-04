package cn.lastmiles.controller;
/**
 * createDate : 2015-07-07 AM 10:19 
 */
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Bank;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.StoreTerminal;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AccountRoleService;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.AgentService;
import cn.lastmiles.service.BusinessBankService;
import cn.lastmiles.service.PartnerService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.FileServiceUtils;
import cn.lastmiles.utils.SecurityUtils;

/**
 * 商家信息操作
 */
@Controller
@RequestMapping("shop")
public class ShopController {

	private final static Logger logger = LoggerFactory.getLogger(ShopController.class); // 日志记录
	@Autowired
	private ShopService shopService; // 商家
	@Autowired
	private AgentService agentService; // 代理商
	@Autowired
	private IdService idService; // ID自动生成
	@Autowired
	private FileService fileService; // 文件
	@Autowired
	private AccountService accountService; // 账户
	@Autowired
	private AccountRoleService accountRoleService; // 账户角色
	@Autowired
	private BusinessBankService businessBankService; // 银行卡
	@Autowired
	private PartnerService partnerService;// 合作者
	@Autowired
	private StoreService storeService;

	/**
	 * 重定向商家列表主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("isStore",SecurityUtils.isStore() ? 0 : null);
		return "redirect:/shop/list";
	}

	/**
	 * 商家列表
	 */
	@RequestMapping(value = "list")
	public String list(Long storeId$,Model model) {
		model.addAttribute("isStore",(null == storeId$) ? (SecurityUtils.isStore() ? "0" : null) : "0");
		model.addAttribute("storeId$",storeId$);
		return "shop/list";
	}

	/**
	 * 商家列表主页面详细数据查询
	 * @param shopName 商家名称
	 * @param mobile 商家手机号码
	 * @param agentName 代理商名称
	 * @param status 营业状态
	 * @param page 分页对象
	 * @param model 数据存储对象
	 */
	@RequestMapping("list/list-data")
	public String listData(Long storeId$,String shopName, String mobile, String agentName, int status, Page page, Model model) {
		model.addAttribute("isStore",(null == storeId$) ? (SecurityUtils.isStore() ? "0" : null) : "0");
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("storeId$ is {}",storeId$);
		if( null == storeId$ ){
			if (SecurityUtils.isStore()) {//如果是商家登录
				if( SecurityUtils.isMainStore() ){
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
		} else {
			List<Store> storeList = storeService.findByParentId(storeId$);
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
		}
		logger.debug("storeId={},mobile={},agentName={},status={}",storeIdString.toString(),mobile,agentName,status);
		model.addAttribute("data", shopService.getShop(storeIdString.toString(),shopName, mobile, agentName, status, page));
//		model.addAttribute("data", shopService.getShop(SecurityUtils.isStore() ? SecurityUtils.getAccountStoreId(): null,shopName, mobile, agentName, status, page));
		return "shop/list-data";
	}

	/**
	 * 新增跳转
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd() {
		return "/shop/add";
	}
	
	// 弹窗测试
	@RequestMapping("showModle/list/list-data")
	public String showModleListData(String shopName,String shop_ListData_mode_Id, String mobile, String agentName, Integer status, Page page, Model model) {
		logger.debug("随机标示---{}",shop_ListData_mode_Id);
		if( null == status ){
			status = Constants.Status.SELECT_ALL;
		}
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isStore()){
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
				storeIdString.append(SecurityUtils.getAccountStoreId());
			}
		}
		model.addAttribute("data", shopService.getShop(storeIdString.toString(),shopName, mobile, null, status, page));
		model.addAttribute("shop_ListData_mode_Id", shop_ListData_mode_Id);
		return "shop/showModleList-data";
	}

	@RequestMapping(value = "agentList")
	public String agentList() {
		return "shop/agentList";
	}

	@RequestMapping(value = "agentList/agentList-data")
	public String agentListData(Long agentId,String name, String mobile, Page page, Model model) {
		model.addAttribute("data", agentService.getAgentList(agentId,name,null, mobile, null, "", page));
		return "shop/agentList-data";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(Store store, @RequestParam("logoFile") MultipartFile imageFile, Model model) throws IOException {
		
		if (null != store.getId()) {
			
			if(null == store.getLogoFileCache() || "".equals(store.getLogoFileCache())){
				if(!"".equals(store.getLogo())){
					fileService.delete(store.getLogo()); // 因为LOGO图片后面可以为空,故当值为空时要删除图片信息					
				}
				store.setLogo("");
			}
			
			// 当上传控件有文件信息且LOGO图片地址标识有值时进行图片保存操作
			if (null != imageFile && imageFile.getSize() > 0 && null != store.getLogoFileCache() && !"".equals(store.getLogoFileCache())) {
				// 当需要上传图片信息时,先判断上次是否有图片,如果有，则删除之前的图片信息
				if(!"".equals(store.getLogo()) ){
					fileService.delete(store.getLogo()); // 先删除之前的					
				}
				String imageID;
				try {
					imageID = fileService.save(imageFile.getInputStream());
					store.setLogo(imageID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			store.setUpdatedAccountId(SecurityUtils.getAccountId());
			store.setBusinessTime(store.getStartBusinessString() + "-" + store.getEndBusinessString());
			String info = store.getId() + (shopService.update(store) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			Long storeID = idService.getId();
			store.setId(storeID);
			if (null != imageFile && imageFile.getSize() > 0 && null != store.getLogoFileCache() && !"".equals(store.getLogoFileCache())) {
				String imageID;
				try {
					imageID = fileService.save(imageFile.getInputStream());
					store.setLogo(imageID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			store.setAccountId(SecurityUtils.getAccountId());

			store.setBusinessTime(store.getStartBusinessString() + "-" + store.getEndBusinessString());
			String posAdminPassword = null;
			if( StringUtils.isNotBlank(store.getPosAdminPassword()) ){
				posAdminPassword = PasswordUtils.encryptPassword(store.getPosAdminPassword());
			}
			store.setPosAdminPassword(posAdminPassword);
			String info = shopService.save(store) ? "添加成功" : "添加失败";
			logger.debug(info);
		}
		return "redirect:/shop/list";
	}
	
	/**
	 * 商家修改保存
	 */
	@RequestMapping(value = "storeSave", method = RequestMethod.POST)
	public String storeSave(Store store,String myStore, @RequestParam("logoFile") MultipartFile imageFile, Model model) throws IOException {
		logger.debug("logo is {}, logoFileCache is {}",store.getLogo(),store.getLogoFileCache());
		
		if(null == store.getLogoFileCache() || "".equals(store.getLogoFileCache())){
			if(!"".equals(store.getLogo())){
				fileService.delete(store.getLogo()); // 因为LOGO图片后面可以为空,故当值为空时要删除图片信息					
			}
			store.setLogo("");
		}
		// 当上传控件有文件信息且LOGO图片地址标识有值时进行图片保存操作
		if (null != imageFile && imageFile.getSize() > 0 && null != store.getLogoFileCache() && !"".equals(store.getLogoFileCache())) {
			// 当需要上传图片信息时,先判断上次是否有图片,如果有，则删除之前的图片信息
			if(!"".equals(store.getLogo()) ){
				fileService.delete(store.getLogo()); // 先删除之前的					
			}
			String imageID;
			try {
				imageID = fileService.save(imageFile.getInputStream());
				store.setLogo(imageID);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		store.setUpdatedAccountId(SecurityUtils.getAccountId());
		store.setBusinessTime(store.getStartBusinessString() + "-" + store.getEndBusinessString());
		String info = store.getId() + (shopService.storeUpdate(store) ? "修改成功" : "修改失败");
		logger.debug(info);
		if( StringUtils.isBlank(myStore) ){
			return "redirect:/shop/list";
		}
		return "redirect:/shop/myStore/";
	}
	
	/**
	 * 修改商家信息
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		Store store = shopService.findById(id);
		if( null == store ){
			return "/shop/add";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] s = store.getBusinessTime().split("-");
		try {
			store.setStartBusinessDate(sdf.parse(s[0]));
			store.setEndBusinessDate(sdf.parse(s[1]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		model.addAttribute("store", store);
		
		if( SecurityUtils.isStore() ){
			return "/shop/storeAdd";
		}
		
		StringBuffer sb = new StringBuffer();
		List<StoreTerminal> terminalIdArray = shopService.getTerminalIdArray(id);
		for (StoreTerminal storeTerminal : terminalIdArray) {
			sb.append(storeTerminal.getTerminalId());
			sb.append(",");
		}
		model.addAttribute("terminalIdArray",terminalIdArray);
		model.addAttribute("terminalIdList",sb);
		if( null == store.getAppAuthToken()){
			model.addAttribute("alipayAuth",true);			
		}
		model.addAttribute("alipayRedirectUri",ConfigUtils.getProperty("alipayRedirectUri"));
		logger.debug("支付宝回调地址为：{}",ConfigUtils.getProperty("alipayRedirectUri"));
		return "/shop/add";
	}
	
	/**
	 * 商家修改商家信息
	 */
	@RequestMapping(value = "myStore/update/{id}", method = RequestMethod.GET)
	public String myStoreToUpdate(@PathVariable Long id, Model model) {
		Store store = shopService.findById(id);
		if(!SecurityUtils.isMainStore()){
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String[] s = store.getBusinessTime().split("-");
			try {
				store.setStartBusinessDate(sdf.parse(s[0]));
				store.setEndBusinessDate(sdf.parse(s[1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}
		model.addAttribute("store", store);
		model.addAttribute("myStore", true);
		return "/shop/storeAdd";
	}
	
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String toView(@PathVariable Long id, Model model) {
		Store store = shopService.findById(id);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[] s = store.getBusinessTime().split("-");
		try {
			store.setStartBusinessDate(sdf.parse(s[0]));
			store.setEndBusinessDate(sdf.parse(s[1]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		model.addAttribute("store", store);
		
		List<StoreTerminal> terminalIdArray = shopService.getTerminalIdArray(id);
		model.addAttribute("terminalIdArray",terminalIdArray);

		return "/shop/view";
	}

	@RequestMapping(value = "shopTypeList")
	public String shopTypeList() {
		return "shop/shopTypeList";
	}

	@RequestMapping(value = "shopTypeList/shopTypeList-data")
	public String shopTypeListData(String name, Page page, Model model) {
		model.addAttribute("data", shopService.getShopTypeList(name, page));
		return "shop/shopTypeList-data";
	}

	@RequestMapping(value = "checkName")
	@ResponseBody
	public int checkName(String name) {
		return shopService.checkName(name) ? 1 : 0;
	}

	@RequestMapping(value = "checkCompanyName")
	@ResponseBody
	public int checkCompanyName(String companyName) {
		return shopService.checkCompanyName(companyName) ? 1 : 0 ;
	}

	@RequestMapping(value = "checkMerchantNo")
	@ResponseBody
	public int checkMerchantNo(String merchantNo) {
		return shopService.checkMerchantNo(merchantNo) ? 1 : 0 ;
	}

	@RequestMapping(value = "checkmobile")
	@ResponseBody
	public int checkmobile(String mobile) {
		return accountService.checkMobile(mobile,Constants.Account.ACCOUNT_TYPE_STORE) ? 1 : 0 ;
	}
	
	@RequestMapping(value = "checkTerminalId")
	@ResponseBody
	public int checkTerminalId(String terValue) {
		return shopService.checkTerminalId(terValue) ? 1 : 0 ;
	}
	
	@RequestMapping(value = "checkMerchantName")
	@ResponseBody
	public int checkMerchantName(String merchantName) {
		return shopService.checkMerchantName(merchantName) ? 1 : 0 ;
	}
	
	@RequestMapping(value = "checkBank")
	@ResponseBody
	public int checkBank(Long businessId,String accountNumber, Model model) {
		
		BusinessBank businessBank = businessBankService.getBusinessBank(businessId, Constants.BusinessBank.STORE_TYPE, accountNumber);
		
		if ( null != businessBank) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@RequestMapping(value = "checkBankHaveDefault")
	@ResponseBody
	public int checkBankHaveDefault(Long businessId,String accountNumber, Model model) {
		
		BusinessBank businessBank = businessBankService.getBusinessBank(businessId, Constants.BusinessBank.STORE_TYPE);
		
		if ( null != businessBank) {
			return 1;
		} else {
			return 0;
		}
	}

	@RequestMapping(value = "delete/delete-byId", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Long id) {
		Boolean flag = shopService.deleteById(id);
		if (!flag) {
			return 0;
		}
		return 1;
	}

	@RequestMapping(value = "businessList/{storeId}")
	public String businessList(@PathVariable Long storeId, Model model) {
		model.addAttribute("businessId", storeId);
		model.addAttribute("storeName",shopService.findStoreNameById(storeId));
		return "shop/businessList";
	}

	@RequestMapping(value = "businessList/businessList-data/{businessId}")
	public String businessListData(@PathVariable Long businessId, Page page, Model model) {
		model.addAttribute("data", agentService.getBusinessList(businessId, page));
		return "shop/businessList-data";
	}

	@RequestMapping(value = "business/add/{businessId}", method = RequestMethod.GET)
	public String toBusinessAdd(@PathVariable Long businessId, Model model) {
		List<Bank> bankList = agentService.findBankList();
		model.addAttribute("bankList", bankList);
		model.addAttribute("businessId", businessId);
		
		model.addAttribute("storeName",shopService.findStoreNameById(businessId));			
		return "/shop/businessAdd";
	}

	@RequestMapping(value = "business/delete/delete-by-id", method = RequestMethod.POST)
	@ResponseBody
	public int toBusinessDel(Long id, Model model) {
		agentService.delByBusinessBankId(id);
		if (agentService.findByBusinessBankId(id) == null) {
			return 1;
		} else {
			return 0;
		}
	}

	@RequestMapping(value = "business/update/{id}", method = RequestMethod.GET)
	public String toBusinessUpdate(@PathVariable Long id, Model model) {
		List<Bank> bankList = agentService.findBankList();
		BusinessBank businessBank = businessBankService.getById(id);
		model.addAttribute("businessBank", businessBank);
		model.addAttribute("bankList", bankList);
		
		model.addAttribute("storeName",shopService.findStoreNameById(businessBank.getBusinessId()));			
		return "/shop/businessAdd";
	}

	@RequestMapping(value = "saveBusinessBank", method = RequestMethod.POST)
	public String add(BusinessBank businessBank, Model model) {
		if (null != businessBank.getId()) {

			businessBank.setType(Constants.BusinessBank.STORE_TYPE);
			if( businessBank.getIsDefault().equals(1) ){
				businessBankService.updateIsDefault(businessBank);
			}
			String info = businessBank.getId() + (businessBankService.update(businessBank) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			businessBank.setId(idService.getId());
			businessBank.setType(Constants.BusinessBank.STORE_TYPE);
			if( businessBank.getIsDefault().equals(1) ){
				businessBankService.updateIsDefault(businessBank);
			}
			String info = businessBankService.save(businessBank) ? "添加成功" : "添加失败";
			logger.debug(info);
		}
		return "redirect:/shop/businessList/"+businessBank.getBusinessId();
	}
	
	@RequestMapping(value = "storeList")
	public String storeList() {
		return "agent/storeList";
	}

	@RequestMapping("storeList/store/list-data")
	public String storeList(String name, Page page, Model model) {
		model.addAttribute("data", shopService.getAgentAndStoreList(SecurityUtils.getAccountAgentId(),name, page));
		return "agent/storeList-data";
	}
	
	@RequestMapping("myStore/")
	public String myStore(Model model) {
		if (SecurityUtils.isStore()) {
			Store store=shopService.findById(SecurityUtils.getAccountStoreId());
			if( null != store ){ // 总店可能查询出来是空
				store.setLogo(FileServiceUtils.getFileUrl(store.getLogo()));
			}
			model.addAttribute("isMainStore",SecurityUtils.isMainStore() ? null:0);
			model.addAttribute("store", store);
		}
		return "shop/myStoreInfo";
	}
	
	@RequestMapping(value = "partnerList")
	public String partnerList() {
		return "shop/partnerList";
	}
	@RequestMapping(value = "ajax/copyProduct")
	@ResponseBody
	public String copyProduct(Long oldShopId,Long newShopId) {
		try {
			shopService.copyStopProduct(oldShopId, newShopId);
			return "0";
		} catch (Exception e) {
			return "1";
		}
	}
	
	@RequestMapping(value = "copyProduct")
	public String toCopyProduct(Long oldShopId,Long newShopId) {
		return "shop/copyProduct";
	}

	@RequestMapping(value = "partnerList/partnerList-data")
	public String partnerListData(String name, Page page, Model model) {
		model.addAttribute("data", partnerService.list(name, page));
		return "shop/partnerList-data";
	}
	
	
	/**
	 * 总店列表
	 */
	@RequestMapping(value = "mainShop/list")
	public String mainShopList() {
		return "shop/mainShopList";
	}
	
	@RequestMapping(value = "mainShop/list-data")
	public String mainShopListData(String name, Page page, Model model) {
		model.addAttribute("data", shopService.mainShoplist(name, page));
		return "shop/mainShopList-data";
	}
	
	@RequestMapping(value = "mainShop/add", method = RequestMethod.GET)
	public String mainAdd() {
		return "/shop/mainShopAdd";
	}
	
	@RequestMapping(value = "/mainShop/update/{id}", method = RequestMethod.GET)
	public String mainShopUpdate(@PathVariable Long id, Model model) {
		Store store = shopService.findMainShopById(id);
		model.addAttribute("store", store);
		return "/shop/mainShopAdd";
	}
	
	@RequestMapping(value = "mainShopSave", method = RequestMethod.POST)
	public String mainShopSave(Store store,Model model) throws IOException {
		String info = null;
		if (null != store.getId()) {
			store.setUpdatedAccountId(SecurityUtils.getAccountId());
			info = store.getId() + (shopService.mainShopUpdate(store) ? "修改成功" : "修改失败");
		} else {
			store.setId(idService.getId());
			store.setAccountId(SecurityUtils.getAccountId());
			info = store.getId() + (shopService.mainShopSave(store) ? "新增成功" : "新增失败");
		}
		
		logger.debug(info);
		return "redirect:/shop/mainShop/list";
	}
	
	@RequestMapping(value = "mainShop/list/ajax/checkName")
	@ResponseBody
	public int mainShopCheckName(Long id,String name) {
		return shopService.mainShopCheckName(id,name) > 0 ? 1 : 0;
	}
	
	@RequestMapping(value = "mainShop/showMode")
	public String mainShopShowList() {
		return "shop/mainShopShowList";
	}
	
	@RequestMapping(value = "mainShop/ajax/showOrganizationTreeMode")
	public String showOrganizationTreeMode() {
		return "shop/organizationList";
	}
	
	@RequestMapping(value = "mainShopShow/list-data")
	public String mainShopShowListData(String name, Page page, Model model) {
		model.addAttribute("data", shopService.mainShoplist(name, page));
		return "shop/mainShopShowList-data";
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
			Long storeId, String mobile,String agentName,Integer status,String shopName,Page page) throws ParseException {
		page.setIsOnePage();
		StringBuffer storeIdString = new StringBuffer();
		logger.debug("storeId$ is {}",storeId);
		if( null == storeId ){
			if (SecurityUtils.isStore()) {//如果是商家登录
				if( SecurityUtils.isMainStore() ){
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
		} else {
			List<Store> storeList = storeService.findByParentId(storeId);
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
		}
		
		String fileName = "商家列表";
		logger.debug("storeId={},mobile={},agentName={},status={}",storeIdString.toString(),mobile,agentName,status);
		@SuppressWarnings("unchecked")
		List<Store> storeList = (List<Store>) shopService.getShop(storeIdString.toString(),shopName, mobile, agentName, status, page).getData();
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("商家列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<6;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("商家名称");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("商家类型");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("所属组织");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("代理商");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("手机号码");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("营业状态");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",storeList.size());
		if (!storeList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",storeList.size());
			for (int i = 0; i < storeList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				Store s=storeList.get(i);
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(s.getName());//商家名称
				row.createCell(1).setCellValue(s.getShopTypeName());//商家类型
				row.createCell(2).setCellValue(s.getOrganizationName());//所属组织
				row.createCell(3).setCellValue(s.getAgentName());//代理商
				row.createCell(4).setCellValue(s.getMobile());//手机号码
				
				row.createCell(5).setCellValue(s.getStatus()==1?"营业中":"已结业");//营业状态
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