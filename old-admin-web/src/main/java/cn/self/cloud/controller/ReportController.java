package cn.self.cloud.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.self.cloud.bean.Report;
import cn.self.cloud.commonutils.basictype.DateUtils;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.json.JsonUtils;
import cn.self.cloud.service.ReportService;
import cn.self.cloud.service.StoreService;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("report")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private StoreService storeService;

	@RequestMapping("")
	public String index() {
		return "redirect:/report/list";
	}

	@RequestMapping("list")
	public String listData(String beginTime, String endTime, String store,
			String category, String date, String action, Model model) {
		if (StringUtils.isNotBlank(action)) {
			model.addAttribute("beginTime", beginTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("date", date);
			model.addAttribute("store", store);
			model.addAttribute("category", category);
			
			model.addAttribute("action", action);

			model.addAttribute("storeList",
					storeService.findByAccount(SecurityUtils.getAccountId()));
			model.addAttribute("category", category);
			List<Report> reportList = reportService.findAll(beginTime, endTime,
					store, category, date);

			Set<String> dateList = new LinkedHashSet<String>();
			Set<String> nameList = new LinkedHashSet<String>();

			double total = 0;

			for (Report report : reportList) {
				String dStr = DateUtils.format(report.getCreatedTime(),
						"YYYY-MM-dd");
				dateList.add(dStr);
				total += report.getSumPrice();

				if ("0".equals(category)) {
					nameList.add(report.getProductName());
				} else if ("1".equals(category)) {
					nameList.add(report.getCategoryName());
				} else if ("2".equals(category)) {
					nameList.add(report.getStoreName());
				} else if ("3".equals(category)) {
					nameList.add(report.getAccountName());
				}
			}

			model.addAttribute("total", total);
			model.addAttribute("nameList", nameList);
			model.addAttribute("dateList", dateList);
			model.addAttribute("data", JsonUtils.objectToJson(reportList));
		}else {
			model.addAttribute("data", JsonUtils.objectToJson(new ArrayList<String>()));
		}
		return "report/list";
	}

	/**
	 * 根据角色ID查帐号
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Report> findAllBySearch(String beginTime, String endTime,
			String store, String category, String date) {
		List<Report> reprotList = reportService.findAllBySearch(beginTime,
				endTime, store, category, date);
		return reprotList;
	}
}
