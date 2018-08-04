package cn.lastmiles.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.UserReportService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("userReport")
public class UserReportController {

	private final static Logger logger = LoggerFactory
			.getLogger(UserReportController.class);

	@Autowired
	private UserReportService userReportService;

	@RequestMapping("")
	public String index() {
		return "redirect:/userReport/list";
	}

	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("reportTypeList", userReportService.reportTypeList());
		return "userReport/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String beginTime, String endTime, String typeId,
			String content, String contact, Page page, Model model) {
		if (StringUtils.isNotBlank(content)) {
			content = content.replaceAll("\\s*", "");
		}
		if (StringUtils.isNotBlank(contact)) {
			contact = contact.replaceAll("\\s*", "");
		}
		model.addAttribute("data", userReportService.list(beginTime, endTime,
				typeId, content, contact, SecurityUtils.getAccountId(), page));
		return "userReport/list-data";
	}

	@RequestMapping(value = "delete/delete-by-id", method = RequestMethod.POST)
	@ResponseBody
	public String toReportDel(Long id, Model model) {
		logger.debug("id==" + id);
		userReportService.delByReportId(id);
		if (userReportService.findByReportId(id) == null) {
			return "1";
		} else {
			return "0";
		}
	}
}
