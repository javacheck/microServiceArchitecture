package cn.lastmiles.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.Verification;
import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.bean.movie.RoomBooking;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.RoomBookingService;
import cn.lastmiles.service.RoomDateSettingService;
import cn.lastmiles.service.RoomService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("roomBooking")
public class RoomBookingController {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomBookingController.class);
	@Autowired
	private RoomBookingService roomBookingService;
	@Autowired
	private RoomDateSettingService roomDateSettingService;
	@Autowired
	private RoomService roomService;
	
	@InitBinder    
	protected void initBinder(WebDataBinder binder) {    
	       binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm"), true));    
	}
	
	
	@RequestMapping("list")
	public String list() {
		return "roomBooking/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String number,String beginTime,String endTime,String phone, Page page, Model model,Integer status) {
		model.addAttribute("data",roomBookingService.list(SecurityUtils.getAccountStoreId(),number,beginTime,endTime,phone,status,page));
		return "/roomBooking/list-data";
	}
	
	@RequestMapping(value = "add/{roomId}", method = RequestMethod.GET)
	public String toAdd(@PathVariable Long roomId,Model model,RedirectAttributes ra){
		Room room =roomService.findById(roomId);
		model.addAttribute("room",room);
		model.addAttribute("nowDate",new Date());
		return "/roomBooking/add";
	}
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id,Model model){
		RoomBooking roomBooking =roomBookingService.findById(id);
		Room room=roomService.findById(roomBooking.getRoomId());
		List<DateSetting> dateSettings =roomDateSettingService.findCanBooingDate(new Date(),roomBooking.getRoomId(),SecurityUtils.getAccountStoreId(), Constants.RoomDateSetting.TYPE_SPECIAL,room.getCategoryId(), roomBooking.getDateSettingId());
		model.addAttribute("dateSettings",dateSettings);
		model.addAttribute("roomBooking", roomBooking);
		model.addAttribute("room",room);
		model.addAttribute("nowDate",new Date());
		return "/roomBooking/add";
	}
	@RequestMapping(value = "changeStatus")
	@ResponseBody
	public String changeStatus(Long id,Integer status){
		roomBookingService.changeStatus(id,status);
		return "1";
	}
	
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(RoomBooking roomBooking){
		roomBooking.setBookingDate(new Date());
		Double reserveDuration = roomBooking.getReserveDuration();
		Date reserveDate = roomBooking.getReserveDate();
		// 截取
		double reserveDuration_ = BigDecimal.valueOf(reserveDuration).setScale(1,BigDecimal.ROUND_FLOOR).doubleValue();
		
		int minute = (int) (reserveDuration_ * 60) ;
		logger.debug("截取后的时长为：{},计算后的分钟数为：{}",reserveDuration_,minute);

		roomBooking.setReserveEndDate(DateUtils.addMinute(reserveDate, minute));
		
		if (roomBooking.getId()==null) {
			roomBooking.setAccountId(SecurityUtils.getAccountId());
			roomBooking.setStoreId(SecurityUtils.getAccountStoreId());
			roomBookingService.add(roomBooking);
		}else{
			roomBookingService.update(roomBooking);
		}
		logger.debug("roomBooking is {}",roomBooking);
		return "redirect:/roomBooking/list";
	}

	@RequestMapping(value = "checkBookingTime",produces = "text/plain;charset=UTF-8" )
	@ResponseBody
	public String checkBookingTime(Long id,Long roomId,String reserveDate,Double reserveDuration){
		logger.debug("checkBookingTime id is {},roomId is {},reserveDate is {},reserveDuration is {} ",id,roomId,reserveDate,reserveDuration);
		if( StringUtils.isBlank(reserveDate) ){
			return Verification.error("预定时间不能为空");
		}
		if( null == reserveDuration ){
			return Verification.error("预定时长不能为空");
		}
		
		int result = roomBookingService.checkBookingTime(id,SecurityUtils.getAccountStoreId(),roomId,reserveDate,reserveDuration);
		logger.debug("result is {} ",result);
		
		if ( result == -1 ) {
			return Verification.success();
		}else if( result == 0){
			return Verification.error("预定时间不能小于当前系统时间");
		} else {
			return Verification.error("房间此时间段已经被预定了");
		}
	}
}
