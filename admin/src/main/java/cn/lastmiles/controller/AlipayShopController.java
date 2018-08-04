package cn.lastmiles.controller;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

import cn.lastmiles.alipay.bean.CreateShopInfo;
import cn.lastmiles.alipay.external.AlipayShopInvoke;
import cn.lastmiles.alipay.model.result.shop.AlipayCreateShopInfoResult;
import cn.lastmiles.alipay.model.result.shop.AlipayStoresImageUploadResult;
import cn.lastmiles.bean.CacheKeys;
import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Store;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.MathUtil;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.AreaQueryService;
import cn.lastmiles.service.DeviceService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.AlipayOperationService;

@Controller
@RequestMapping("alipayShop")
public class AlipayShopController {
	private final static Logger logger = LoggerFactory
			.getLogger(AlipayShopController.class);
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private IdService idService;
	@Autowired
	private AreaQueryService areaQueryService;
	@Autowired
	private CacheService cacheService; // 缓存
	@Autowired
	private AlipayOperationService alipayOperationService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("list")
	public String list(Model model) {
		return "alipayShop/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String storeName,String deviceSn,Long storeId, Page page, Model model) {
		List<Store> stores =new ArrayList<Store>();
		if (SecurityUtils.isStore()) {//商家
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){ // 没总店订单商家查看权限的情况下，只显示总店自己的订单信息
					Store store = new Store();
					store.setId(SecurityUtils.getAccountStoreId());
					stores.add(store);						
				} else if( null != storeId && ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					stores.addAll(storeList);
				} else { // 有权限，且指定了固定查询的商家
					Store store = new Store();
					store.setId(storeId);
					stores.add(store);
				}
			} else {
				Store store = new Store();
				store.setId(SecurityUtils.getAccountStoreId());
				stores.add(store);			
			}
		}
		if (SecurityUtils.isAdmin()) {//管理员
			stores=null;
		}
		if (SecurityUtils.isAgent()) {//代理商
			stores=storeService.getAgentAndStoreList(SecurityUtils.getAccountAgentId());
		}
		model.addAttribute("data", deviceService.list(stores,storeName,deviceSn, page));
		return "alipayShop/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		model.addAttribute("province",areaQueryService.getProvinceAll());
		model.addAttribute("category",areaQueryService.getShopCategory());
		return "/alipayShop/add";
	}
	
	@RequestMapping(value="ajax/getCityByProvinceID",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> getCityByProvinceID(String provinceID){
		return areaQueryService.getCityByProvinceID(provinceID);
	}
	
	@RequestMapping(value="ajax/getDistrictByCityID",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> getDistrictByCityID(String cityID){
		return areaQueryService.getDistrictByCityID(cityID);
	}
	
	@RequestMapping(value="ajax/getShopCategory",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> getShopCategory(){
		return areaQueryService.getShopCategory();
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		logger.debug("toUpdate  id -->"+id);
		Device device = deviceService.findById(id);
		logger.debug("toUpdate  device -->"+device);
		model.addAttribute("device",device);
		return "/device/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(HttpServletRequest request,CreateShopInfo createShopInfo,
			@RequestParam(value="brandLogoTemp") MultipartFile brandLogoTemp,
			@RequestParam(value="mainImageTemp") MultipartFile mainImageTemp,
			@RequestParam(value="windowImage") MultipartFile windowImage,
			@RequestParam(value="oneInternal") MultipartFile oneInternal,
			@RequestParam(value="twoInternal") MultipartFile twoInternal,
			@RequestParam(value="licenceTemp") MultipartFile licenceTemp,
			@RequestParam(value="businessCertificateTemp") MultipartFile businessCertificateTemp,
			Model model) {
		String storeId = createShopInfo.getStoreId();
		
		String alipayTempUrl = request.getServletContext().getRealPath("/WEB-INF/static/alipayTemp/");
		List<MultipartFile> fileList = new ArrayList<MultipartFile>();
		fileList.add(brandLogoTemp);
		fileList.add(mainImageTemp);
		fileList.add(windowImage);
		fileList.add(oneInternal);
		fileList.add(twoInternal);
		fileList.add(licenceTemp);
		fileList.add(businessCertificateTemp);
		
		List<File> tempList = saveTheServer(alipayTempUrl, fileList);
		List<String> imageList = new ArrayList<String>();
		
		Object object = cacheService.get(CacheKeys.ALIPAYAUTHKEY + storeId);
		if( null == object ){ // 之前授过权限则刷新授权码
			List<Map<String, Object>> list = alipayOperationService.findAuthByStoreId(storeId);
			if(list.isEmpty() || list.size() <= 0 ){
				model.addAttribute("errorMsg","请先进行第三方授权---");	
				return "redirect:/alipayShop/list";	 							
			} else {
				object = list.get(0).get("app_refresh_token");
				cacheService.set(CacheKeys.ALIPAYAUTHKEY + storeId, object);// 把生成的授权Token放进缓存
				
				logger.debug("从数据库中取出的刷新令牌是：{}",object);
			}
		}
		
		// 调用图片上传接口
		AlipayShopInvoke asi = upLoadImage(tempList, imageList, object+"");
	
		try {
			createShopInfo.setBrandLogo(imageList.get(0)); // brandLogo
			createShopInfo.setMainImage(imageList.get(1)); // mainImage
			createShopInfo.setAuditImage(imageList.get(2) + ","
					+ imageList.get(3) + "," + imageList.get(4)); // windowImage,oneInternal,twoInternal
			createShopInfo.setLicence(imageList.get(5)); // licenceTemp
			createShopInfo.setBusinessCertificate(imageList.get(6)); // businessCertificateTemp
			DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			createShopInfo.setRequestId(storeId + "requestId" + sdf.format(new Date()));
			createShopInfo.setIsvUid("2088021174383713"); // 返佣必备参数--填写我们的PID
			createShopInfo.setBizVersion("2.0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 异步通知支付是否成功的地址
		createShopInfo.setOperateNotifyUrl(ConfigUtils.getProperty("alipayShopNoticeURL"));
		
		logger.debug("---->{}",createShopInfo);
		
		Map<String,Object> returnMap = asi.createShopInfo(object+"",createShopInfo);
		if( null == returnMap.get("errorParameters")){
			AlipayCreateShopInfoResult result = (AlipayCreateShopInfoResult) returnMap.get("responseParameters");
			if(result.isOperationSuccess()){ // 授权成功
				model.addAttribute("errorMsg","创建申请已提交。具体需要等待支付宝审核....");				
			} else {
				model.addAttribute("errorMsg",result.getResponse().getSubMsg());
			}
		} else {
			model.addAttribute("errorMsg",returnMap.get("errorParameters"));			
		}
		
		try {
			FileUtils.cleanDirectory(new File(alipayTempUrl));
		} catch (IOException e) {
			logger.debug("删除图片文件失败");
			e.printStackTrace();
		}
		
//		areaQueryService.save(createShopInfo);
		return "redirect:/alipayShop/list";			
	}

	public AlipayShopInvoke upLoadImage(List<File> tempList,
			List<String> imageList, String object) {
		AlipayShopInvoke asi = new AlipayShopInvoke();
		for (File file : tempList) {
			String tempFile = file.getAbsolutePath();
			Map<String,Object> upLoad = asi.storesImageUpload(object,file.getName(), new File(tempFile));
			if( null == upLoad.get("errorParameters") ){
				AlipayStoresImageUploadResult storeImage = (AlipayStoresImageUploadResult) upLoad.get("responseParameters");
				imageList.add(storeImage.getResponse().getImageId());
			} else {
				logger.debug("上传图片出错--->{}",upLoad.get("errorParameters"));
			}
		}
		return asi;
	}

	public List<File> saveTheServer(String alipayTempUrl,
			List<MultipartFile> fileList) {
		BufferedInputStream bis = null;
		BufferedOutputStream bouts = null;
		List<File> tempList = new ArrayList<File>();
		for (int i = 0; i < 7; i++) {
			String fileOrTitleName = MathUtil.randomNumber(5); 
			File tempDir = new File((alipayTempUrl)+File.separator+fileOrTitleName + "temp" + fileList.get(i).getOriginalFilename());
			tempList.add(tempDir);
			try {
				bis = new BufferedInputStream(fileList.get(i).getInputStream());// 放到缓冲流里
				bouts = new BufferedOutputStream(new FileOutputStream(tempDir) );
				
				IOUtils.copy(bis, bouts);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		try {
			bis.close();
			bouts.flush();
			bouts.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempList;
	}
	
	@RequestMapping(value = "shopList/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		
		logger.debug("name={}",name);
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
		return "alipayShop/shopList-data";
	}
}