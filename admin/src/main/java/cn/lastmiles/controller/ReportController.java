package cn.lastmiles.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.Report;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.ReportService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.JsonUtils;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("report")
public class ReportController {
	private final static Logger logger = LoggerFactory.getLogger(ReportController.class);
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
			String category, String date, String action, Model model)
			throws ParseException {
		
		List<Store> storeList = storeService.findMyStore(SecurityUtils.getAccountId(), null);
		if( null != storeList && storeList.size() > 0 ){
			model.addAttribute("storeList",storeList);
		}
		if (StringUtils.isNotBlank(action)) {
			model.addAttribute("beginTime", beginTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("date", date);
			model.addAttribute("store", store);
			model.addAttribute("category", category);

			model.addAttribute("action", action);

			model.addAttribute("category", category);
			String storeStr="";
			if(store!=null && !"".equals(store)){
				storeStr=store;
			}else{
				for(int i=0;i<storeList.size();i++){
					storeStr+=storeList.get(i).getId().toString()+",";
				}
				storeStr=storeStr.substring(0, storeStr.lastIndexOf(","));
			}
			logger.debug("storeStr=="+storeStr);
			List<Report> reportList = reportService.findAll(beginTime, endTime,
					storeStr, category, date,SecurityUtils.getAccountId());
		
			Set<String> dateList = new LinkedHashSet<String>();
			Set<String> nameList = new LinkedHashSet<String>();// 去重

			String dStr = "";
			for (Report report : reportList) {

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
			if ("0".equals(date)) {// 按日统计
				Date date0 = new SimpleDateFormat("yyyy-MM-dd")
						.parse(beginTime);
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date0);
				dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(cal
						.getTime()));
				while (cal.getTime().compareTo(date1) < 0) {
					cal.add(Calendar.DAY_OF_MONTH, 1);
					dStr = new SimpleDateFormat("yyyy-MM-dd").format(cal
							.getTime());
					dateList.add(dStr);
				}
			} else if ("1".equals(date)) {// 按月份统计
				Date date0 = new SimpleDateFormat("yyyy-MM").parse(beginTime);
				Date date1 = new SimpleDateFormat("yyyy-MM").parse(endTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date0);
				dateList.add(new SimpleDateFormat("yyyy-MM").format(cal
						.getTime()));
				while (cal.getTime().compareTo(date1) < 0) {
					cal.add(Calendar.MONTH, 1);
					dStr = new SimpleDateFormat("yyyy-MM")
							.format(cal.getTime());
					dateList.add(dStr);
				}
			} else if ("2".equals(date)) {// 按年统计
				Date date0 = new SimpleDateFormat("yyyy").parse(beginTime);
				Date date1 = new SimpleDateFormat("yyyy").parse(endTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date0);
				dateList.add(new SimpleDateFormat("yyyy").format(cal.getTime()));
				while (cal.getTime().compareTo(date1) < 0) {
					cal.add(Calendar.YEAR, 1);
					dStr = new SimpleDateFormat("yyyy").format(cal.getTime());
					dateList.add(dStr);
				}
			}

			model.addAttribute("nameList", nameList);
			model.addAttribute("dateList", dateList);
			model.addAttribute("data", JsonUtils.objectToJson(reportList));
		} else {
			model.addAttribute("data",
					JsonUtils.objectToJson(new ArrayList<String>()));
		}
		return "report/list";
	}

	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,
			String beginTime, String endTime, String store, String category,
			String date) throws ParseException {
		List<Store> storeList = storeService.findMyStore(SecurityUtils.getAccountId(), null);
		String storeStr="";
		if(store!=null && !"".equals(store)){
			storeStr=store;
		}else{
			for(int i=0;i<storeList.size();i++){
				storeStr+=storeList.get(i).getId().toString()+",";
			}
			storeStr=storeStr.substring(0, storeStr.lastIndexOf(","));
		}
		logger.debug("storeStr=="+storeStr);
		String fileName = "统计报表";
		List<Report> reportList = reportService.findAllBySearch(beginTime,
				endTime, storeStr, category, date,SecurityUtils.getAccountId());
		Set<String> dateSet = new LinkedHashSet<String>();
		Set<String> nameSet = new LinkedHashSet<String>();// 去重
		String dStr = "";
		for (Report report : reportList) {
			if ("0".equals(category)) {
				nameSet.add(report.getProductName());
			} else if ("1".equals(category)) {
				nameSet.add(report.getCategoryName());
			} else if ("2".equals(category)) {
				nameSet.add(report.getStoreName());
			} else if ("3".equals(category)) {
				nameSet.add(report.getAccountName());
			}
		}
		if ("0".equals(date)) {// 按日统计
			Date date0 = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime);
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date0);
			dateSet.add(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
			while (cal.getTime().compareTo(date1) < 0) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				dateSet.add(dStr);
			}
		} else if ("1".equals(date)) {// 按月份统计
			Date date0 = new SimpleDateFormat("yyyy-MM").parse(beginTime);
			Date date1 = new SimpleDateFormat("yyyy-MM").parse(endTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date0);
			dateSet.add(new SimpleDateFormat("yyyy-MM").format(cal.getTime()));
			while (cal.getTime().compareTo(date1) < 0) {
				cal.add(Calendar.MONTH, 1);
				dStr = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
				dateSet.add(dStr);
			}
		} else if ("2".equals(date)) {// 按年统计
			Date date0 = new SimpleDateFormat("yyyy").parse(beginTime);
			Date date1 = new SimpleDateFormat("yyyy").parse(endTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date0);
			dateSet.add(new SimpleDateFormat("yyyy").format(cal.getTime()));
			while (cal.getTime().compareTo(date1) < 0) {
				cal.add(Calendar.YEAR, 1);
				dStr = new SimpleDateFormat("yyyy").format(cal.getTime());
				dateSet.add(dStr);
			}
		}
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("统计报表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);// 第一行第一格
		if ("0".equals(category)) {
			cell.setCellValue("商品名称");
			cell.setCellStyle(style);
		} else if ("1".equals(category)) {
			cell.setCellValue("分类名称");
			cell.setCellStyle(style);
		} else if ("2".equals(category)) {
			cell.setCellValue("店铺名称");
			cell.setCellStyle(style);
		} else if ("3".equals(category)) {
			cell.setCellValue("账号名称");
			cell.setCellStyle(style);
		}

		List<String> dateList = new ArrayList<String>(dateSet);
		List<String> nameList = new ArrayList<String>(nameSet);
		for (int i = 0; i < dateList.size(); i++) {
			cell = row.createCell((short) i + 1);// 第一行第二格开始
			cell.setCellValue(dateList.get(i));
			cell.setCellStyle(style);
		}
		cell = row.createCell((short) dateList.size() + 1);// 第一行第最后一格
		cell.setCellValue("小计");
		cell.setCellStyle(style);
		if (nameList.size() > 0) {
			for (int i = 0; i < nameList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				row = sheet.createRow((int) i + 1);
				row.createCell(0).setCellValue(nameList.get(i));//把商品名称放进去
				for (int j = 0; j < dateList.size(); j++) {
					boolean flag = true;
					for (int k = 0; k < reportList.size(); k++) {
						String _name = "";

						if ("0".equals(category)) {
							_name = reportList.get(k).getProductName();
						} else if ("1".equals(category)) {
							_name = reportList.get(k).getCategoryName();
						} else if ("2".equals(category)) {
							_name = reportList.get(k).getStoreName();
						} else if ("3".equals(category)) {
							_name = reportList.get(k).getAccountName();
						}

						String _date = "";
						if ("0".equals(date)) {// 按日统计
							_date = reportList.get(k).getDate();
						} else if ("1".equals(date)) {// 按月统计
							_date = (reportList.get(k).getDate()).substring(0,reportList.get(k).getDate().lastIndexOf("-"));
						} else {// 按年统计
							_date = (reportList.get(k).getDate()).substring(0,reportList.get(k).getDate().indexOf("-"));
						}
						
						if (_name.equals(nameList.get(i))&& _date.equals(dateList.get(j))) {// 如果循环（以dateList.length）时，和dataList的数据中的名称和天数相同，把当天总金额打印出来，不同给0
							flag = false;
							row.createCell((short) j + 1).setCellValue(reportList.get(k).getSumPrice());
							break;
						}
					}
					if (flag == true) {
						row.createCell((short) j + 1).setCellValue(0);
					}

				}
			}

		}

		// 最后一行，第一格
		row = sheet.createRow((int) nameList.size() + 1);
		row.createCell((short) 0).setCellValue("总计");

		int lastRowNum = sheet.getLastRowNum();// 获得表行数
		int lastCellNum = sheet.getRow(0).getLastCellNum();// 获得表列数(这里取第一行列数为最大列表)
		double d[]  = new double[lastCellNum - 2];

		for (int i = 1; i <= lastRowNum - 1; i++) {// 循环一行一行取数(最后一行不读)
			double total = 0;
			Row row1 = sheet.getRow(i);// 获取第i行的工作行(取第二行开始)
			
			for (int j = 1; j < lastCellNum - 1; j++) {
				total += row1.getCell(j).getNumericCellValue();
				d[j - 1] += Double.valueOf(row1.getCell(j).getNumericCellValue());
			}

			row1.createCell((short) lastCellNum - 1).setCellValue(total);// 小计
		}

		double sumtotal = 0;
		for (int i = 0; i < d.length; i++) {
			row.createCell((short) i + 1).setCellValue(d[i]);// 统计
			sumtotal = NumberUtils.add(sumtotal, d[i]);
		}
		row.createCell((short) sheet.getRow(0).getLastCellNum() - 1).setCellValue(sumtotal);// 统计

		// 第六步，将文件存到指定位置
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			os.flush();
			// 关闭输出流
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
