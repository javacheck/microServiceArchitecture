/**
 * createDate : 2016年4月14日下午5:12:40
 */
package cn.lastmiles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lastmiles.bean.movie.RoomOpen;
import cn.lastmiles.bean.movie.RoomOpenDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.service.RoomCategoryService;
import cn.lastmiles.service.RoomOpenRecordService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("roomOpenRecord")
public class RoomOpenRecordController {
	@Autowired
	private RoomOpenRecordService roomOpenRecordService;
	@Autowired
	private RoomCategoryService roomCategoryService;
	
	@RequestMapping("list")
	public String list(Model model){
		model.addAttribute("categoryList",roomCategoryService.findByStoreId(SecurityUtils.getAccountStoreId()));
		return "roomOpenRecord/list";
	}
	
	@RequestMapping("list/list-data")
	public String listData(Long number , Integer status , Long categoryId,Page page, Model model) {
		model.addAttribute("data", roomOpenRecordService.listFromOrder(SecurityUtils.getAccountStoreId(),number,categoryId, page));
		return "roomOpenRecord/list-data";
	}
	
	@RequestMapping(value = "details/{id}", method = RequestMethod.GET)
	public String toDetails(@PathVariable Long id, Model model) {
		RoomOpen roomOpen = roomOpenRecordService.findByOrderId(id);
		
		List<RoomOpenDetail> roomOpenDetailArray = roomOpenRecordService.findDetailByOrderId(id);
		for (RoomOpenDetail detail : roomOpenDetailArray){
			Double total = NumberUtils.multiply(detail.getPrice().toString(), detail.getNumber().toString());
			if (detail.getType() != null && detail.getType().intValue() == 0){
				//包间   把时长改成小时   roomOpenDetail.price * 100 * roomOpenDetail.number / 100
				total = detail.getPrice();
				detail.setNumber(NumberUtils.divide(detail.getNumber(), 60.0));
			}
			detail.setTotal(total);
		}
		model.addAttribute("roomOpenDetailArray",roomOpenDetailArray);
		model.addAttribute("roomOpen",roomOpen);
		return "/roomOpenRecord/details";
	}
	
}
