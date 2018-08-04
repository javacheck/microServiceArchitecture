package cn.lastmiles.controller;


import java.util.List;

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

import cn.lastmiles.bean.HtmlAd;
import cn.lastmiles.bean.PurchaseAcceptance;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.service.ProductStorageRecordService;
import cn.lastmiles.service.PurchaseAcceptanceService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;


@Controller
@RequestMapping("purchaseAcceptance")
public class PurchaseAcceptanceController {
	private final static Logger logger = LoggerFactory.getLogger(PurchaseAcceptanceController.class);
	
	@Autowired
	private PurchaseAcceptanceService purchaseAcceptanceService;
	@Autowired
	private ProductStorageRecordService productStorageRecordService;
	@Autowired 
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	@RequestMapping("")
	public String index() {
		return "redirect:/purchaseAcceptance/list";
	}
	
	/**
	 * 入库列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "purchaseAcceptance/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(String mobile,String beginTime,String endTime,Integer status,String purchaseNumber, Page page, Model model) {
		Long storeId=SecurityUtils.getAccountStoreId();
		model.addAttribute("data", purchaseAcceptanceService.findAllPage(storeId,purchaseNumber.trim(),mobile.trim(),beginTime,endTime,status,page));
		return "purchaseAcceptance/list-data";
		
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "purchaseAcceptance/add";
	}
	
	@RequestMapping("storageList/list-data")
	public String listData(String storageNumber,String storeId,String mobile, Page page, Model model) {
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
		model.addAttribute("data", productStorageRecordService.findAllPage(storeIdString.toString(),storageNumber,mobile.trim(),null,null,page));
		return "purchaseAcceptance/storageRecordList-data";
		
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		logger.debug("toUpdate  id -->"+id);
		model.addAttribute("purchaseAcceptance",purchaseAcceptanceService.findById(id));
		return "/purchaseAcceptance/add";
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public String edit(HtmlAd htmlAd,@RequestParam("paImageId") MultipartFile paImageId,PurchaseAcceptance purchaseAcceptance,String _storageNumber, Model model) {
		logger.debug("_storageNumber={}",_storageNumber);
		purchaseAcceptance.setStorageNumber(_storageNumber);
		purchaseAcceptance.setAccountId(SecurityUtils.getAccountId());
		purchaseAcceptance.setStoreId(SecurityUtils.getAccountStoreId());
		purchaseAcceptanceService.editPurchaseAcceptance(purchaseAcceptance,paImageId);
		return "redirect:/purchaseAcceptance/list";
	}
	
	@RequestMapping(value="imgToBig/showMode/{id}" )
	public String confirmInfo(@PathVariable Long  id, Model model){
		logger.debug("purchaseAcceptance-info-ID IS :"+id);
		model.addAttribute("purchaseAcceptance",purchaseAcceptanceService.findById(id));
		return "purchaseAcceptance/imgToBig";
	}
	/**
	 * 判断广告是否已存在
	 * @return
	 */
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findPaExist(Long id,String purchaseNumber) {
		PurchaseAcceptance pa =purchaseAcceptanceService.findPaExist(id,purchaseNumber,SecurityUtils.getAccountStoreId());
		if(pa==null){
			return "0";
		}else{
			return "1";
		}
	}
}
