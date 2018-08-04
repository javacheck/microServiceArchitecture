package cn.lastmiles.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.UserUpdateMobileRecordService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("userUpdateMobileRecord")
public class UserUpdateMobileRecordController {
	private final static Logger logger = LoggerFactory.getLogger(UserUpdateMobileRecordController.class);
	
	@Autowired
	private UserUpdateMobileRecordService userUpdateMobileRecordService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private StoreService storeService;
	@RequestMapping("")
	public String index() {
		return "redirect:/userUpdateMobileRecord/list";
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
		return "userUpdateMobileRecord/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long storeId,String oldMobile,String newMobile,String accountName,String beginTime,String endTime, Page page, Model model) {
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
		}
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		model.addAttribute("data", userUpdateMobileRecordService.findAll(storeIdString.toString(),oldMobile,newMobile,accountName,beginTime,endTime, page));
		return "userUpdateMobileRecord/list-data";
		
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
		logger.debug("storeIdString.toString()={}",storeIdString.toString());
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "userUpdateMobileRecord/showModelList-data";
	}
}
