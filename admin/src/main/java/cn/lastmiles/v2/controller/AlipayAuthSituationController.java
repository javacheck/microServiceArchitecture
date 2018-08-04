package cn.lastmiles.v2.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.lastmiles.bean.AlipayAuthSituation;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.v2.service.AlipayAuthSituationService;

/**
 * 2016-10-13
 * @author shaoyikun
 *
 */
@RequestMapping("alipayAuthSituation")
@Controller
public class AlipayAuthSituationController {

	private final static Logger logger = LoggerFactory.getLogger(AlipayAuthSituationController.class); // 日志记录

	@Autowired
	private StoreService storeService;
	@Autowired
	private AlipayAuthSituationService alipayAuthSituationService;

	@RequestMapping("")
	public String index(Model model) {
		return "redirect:/alipayAuthSituation/list";
	}

	@RequestMapping(value = "list")
	public String list(Model model) {
		return "alipayAuthSituation/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Long storeId,Long code, Page page, Model model) {
		logger.debug("in storeId is {},code is {}", storeId,code);

		StringBuffer storeIdString = new StringBuffer();
		
		// 支付宝授权情况查看只有Admin才能查看
		if (null != storeId) {
			List<Store> storeList = storeService.findByParentId(storeId);
			boolean index = false;
			if( null != storeList){
				for (Store store : storeList) {
					if (index) {
						storeIdString.append(",");
					}
					storeIdString.append(store.getId());
					index = true;
				}				
			}
		} 
		
		logger.debug("out storeId is {}", storeIdString.toString());
		model.addAttribute("data", alipayAuthSituationService.getResult(storeIdString.toString(),code, page));
		return "alipayAuthSituation/list-data";
	}
	
	@RequestMapping(value="info/showMode/{getAuthID}" )
	public String confirmInfo(@PathVariable Long  getAuthID, Model model){
		logger.debug("查看授权详情ID is {}",getAuthID);
		
		AlipayAuthSituation alipayAuthSituation = alipayAuthSituationService.findDetailByAuthID(getAuthID);
		model.addAttribute("alipayAuthSituation",alipayAuthSituation);
		
		logger.debug("查询到的详情信息 is {}",alipayAuthSituation.toString());
		return "alipayAuthSituation/info";
	}

}