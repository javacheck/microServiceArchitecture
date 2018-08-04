package cn.lastmiles.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.service.Publish;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.getui.PushService;
import cn.lastmiles.service.PublishService;
import cn.lastmiles.service.UserService;

@Controller
@RequestMapping("publish")
public class PublishController {
	@Autowired
	private PublishService publishService;
	@Autowired
	private IdService idService;
	@Autowired
	private UserService userService;
	
	private final static Logger logger = LoggerFactory.getLogger(PublishController.class);

	
	@RequestMapping("list")
	public String list(Model model) {
		return "publish/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String keywords,Integer type,String startTime ,String endTime,Integer status,Page page, Model model) {
		logger.debug("keywords = {} , type = {}",keywords,type);
		// 全部状态
		if (status.equals(Constants.Status.SELECT_ALL)) {
			status = null;
		}
		
		// 全部状态
		if (type.equals(Constants.Status.SELECT_ALL)) {
			type = null;
		}
		page = publishService.list(keywords,type,startTime,endTime,status, page);
		model.addAttribute("data", page);
		return "publish/list-data";
	}
	
	@RequestMapping(value = "ajax/modifyStatus/")
	@ResponseBody
	public String modifyStatus(Long id,Integer status,String reason,Long userId,String title, Model model) {
		String ret =  publishService.updateStatus(id, status,reason) ? "1" : "0";
		if(ObjectUtils.equals(ret,"1")){
			Message message = new Message();
			message.setUserId(userId);
			message.setReaded("0");
			message.setTitle("发布状态通知");
			message.setCreateTime(new Date());
			message.setOwnerId(id);
			if(status.intValue() == cn.lastmiles.constant.Constants.Publish.TYPE_AUDITED){ // 审核通过
				message.setType(Constants.MessageType.MESSAGE_AUDIT_YES);			
				message.setMessage(Constants.MessageType.MESSAGE_AUDIT_YES_CONTENT);
			} else if(status.intValue() == cn.lastmiles.constant.Constants.Publish.TYPE_CANCEL){ // 撤销审核状态
				message.setType(Constants.MessageType.MESSAGE_AUDIT_REVOCATION);
				message.setMessage(Constants.MessageType.MESSAGE_AUDIT_REVOCATION_CONTENT.replace("#title#", title).replace("#reason#", reason));
			} else if(status.intValue() == cn.lastmiles.constant.Constants.Publish.TYPE_AUDITED_FAIL){ // 审核不通过
				message.setType(Constants.MessageType.MESSAGE_AUDIT_NO);
				message.setMessage(Constants.MessageType.MESSAGE_AUDIT_NO_CONTENT);
			}
			PushService.pushToSingle(message);
		}
		return ret;
	}
	
	@RequestMapping(value = "showMode/publishAuditWindow/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Publish publishAuditWindow(Long id,Model model){
		Publish publish = publishService.findById(id);
		Integer idAudit = 0;
		if( ObjectUtils.equals(userService.findAuditStatus(publish.getUserId()),1)){
			idAudit = 1;
		}
		publish.setIdAudit(idAudit);
		return publish;
	}
}