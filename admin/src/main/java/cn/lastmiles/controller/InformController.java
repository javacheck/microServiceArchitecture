/**
 * createDate : 2016年5月20日上午11:03:42
 */
package cn.lastmiles.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.lastmiles.bean.Inform;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.service.InformService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("inform")
public class InformController {
	private static final Logger logger = LoggerFactory.getLogger(InformController.class);
	@Autowired
	private InformService informService;
	@Autowired
	private StoreService storeService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/inform/list";
	}
	
	@RequestMapping("list")
	public String list() {
		return "inform/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String name,Page page, Model model) {
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
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
		model.addAttribute("data", informService.list(storeIdString.toString(),name,page));				
		return "inform/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {
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
		
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		model.addAttribute("scopeList",storeService.findByStoreIdString(storeIdString.toString()));
		return "/inform/add";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Inform inform,String loseDate){
		inform.setAccountId(SecurityUtils.getAccountId());
		inform.setLoseTime(DateUtils.parse("yyyy-MM-dd HH:mm:ss", loseDate + " 23:59:59"));
		
		if( ObjectUtils.equals(-1L, inform.getStoreId()) ){
			inform.setIsMainStoreId(-1L);
			inform.setStoreId(SecurityUtils.getAccountStoreId());
		} else {
			inform.setIsMainStoreId(0L);
		}
		boolean flag = informService.save(inform);
		
		logger.debug("save Inform is {}",flag);
		return "redirect:/inform/list";
	}
}