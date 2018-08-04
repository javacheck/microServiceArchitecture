package cn.lastmiles.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductSynchronizationService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("productSynchronization")
public class ProductSynchronizationController {
	private final static Logger logger = LoggerFactory.getLogger(ProductSynchronizationController.class);
	
	@Autowired
	private ProductSynchronizationService productSynchronizationService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private ShopService shopService;
	
	@RequestMapping(value = "list")
	public String list(Model model) {
		
		Store store=storeService.findById(SecurityUtils.getAccountStoreId());//只是能把总店商品同步到分店
		model.addAttribute("store",store);
		return "productSynchronization/list";
	}
	
	// 商家列表弹窗
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
			page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		}
		model.addAttribute("data", page);
		return "productSynchronization/showModelList-data";
	}
		
	/**
	 * 查看调拨的店是否存在商品
	 * @param 
	 * @return
	 */
	@RequestMapping(value="findStockExist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findStockExist(Long toStoreId){
		List<ProductStock> ps=productSynchronizationService.findStockExist(toStoreId);
		if(!ps.isEmpty()){
			return "1";
		}
		return null;
	}
	
	@RequestMapping(value="edit",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String edit(Long toStoreId){
		Long fromStoreId=SecurityUtils.getAccountStoreId();
		Long accountId=SecurityUtils.getAccountId();
		productSynchronizationService.sync(fromStoreId,toStoreId,accountId);
		return "1";
	}
}
