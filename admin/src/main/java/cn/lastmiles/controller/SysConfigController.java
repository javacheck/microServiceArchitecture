package cn.lastmiles.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;





import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Activity;
import cn.lastmiles.bean.SysConfig;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.SysConfigService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("sysConfig")
public class SysConfigController {
		private final static Logger logger = LoggerFactory.getLogger(SysConfigController.class);
		@Autowired
		private SysConfigService sysConfigService;
		
		
		@RequestMapping("")
		public String index() {
			return "redirect:/sysConfig/edit";
		}
		
		@RequestMapping(value = "edit")
		public String list(Model model) {
			model.addAttribute("sysConfig",sysConfigService.get(Constants.SysConfig.RECOMMENDED_NAME));
			return "sysConfig/edit";
		}
		
		@RequestMapping(value = "save", method = RequestMethod.POST)
		public String add(SysConfig sysConfig, Model model) throws ParseException {
			logger.debug("sysConfig={}",sysConfig);
			if("1".equals(sysConfig.getValue())){
				sysConfig.setValue(Constants.SysConfig.RECOMMENDED);//开启
			}else{
				sysConfig.setValue(Constants.SysConfig.NOTRECOMMENDED);//关闭
			}
			sysConfigService.update(sysConfig);
			return "redirect:/sysConfig/edit";
		}
}
