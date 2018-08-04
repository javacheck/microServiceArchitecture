package cn.lastmiles.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.AfterSales;
import cn.lastmiles.bean.AfterSalesExcel;
import cn.lastmiles.bean.AfterSalesType;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.AfterSalesService;
import cn.lastmiles.service.OrderItemServise;
import cn.lastmiles.service.OrderServise;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

/**
 * createdTime:2016/10/18
 * 
 * @author shaoyikun
 *
 */
@Controller
@RequestMapping("afterSales")
public class AfterSalesController {

	/*
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(AfterSalesController.class);
	@Autowired
	private AfterSalesService afterSalesService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private OrderServise orderServise;
	@Autowired
	private OrderItemServise orderItemServise;

	@RequestMapping("list")
	public String afterSalesList(Model model) {
		List<AfterSalesType> afterSalesType = afterSalesService.getAllAfterSalesType();
		model.addAttribute("afterSalesTypeList", afterSalesType);
		model.addAttribute("isSys", SecurityUtils.isAdmin());
		model.addAttribute("isMainStore", SecurityUtils.isMainStore());
		return "/afterSales/list";
	}

	@RequestMapping("list-data")
	public String afterSalesListData(String storeId, String orderId, String productName, Long categoryId,
			String accountMobile, Long afterSalesType, String beginTime, String endTime, Page page, Model model) {

		logger.debug(
				"storeId is {}, orderId is {},productName is {},categoryId is {},accountMobile is {},afterSalesType is {},beginTime is {},endTime is {}.",
				storeId, orderId, productName, categoryId, accountMobile, afterSalesType, beginTime, endTime);

		model.addAttribute("isSys", SecurityUtils.isAdmin());
		model.addAttribute("isMainStore", SecurityUtils.isMainStore());
		StringBuilder storeIdString = new StringBuilder();
		if (SecurityUtils.isMainStore()) {
			if (StringUtils.isBlank(storeId)) {
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				if (null != storeList && storeList.size() > 0) {
					for (Store store : storeList) {
						if (storeIdString.length() > 0) {
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(SecurityUtils.getAccountStoreId());
				}
			} else {

				List<Store> storeList = storeService.findByParentId(Long.parseLong(storeId));
				if (null != storeList && storeList.size() > 0) {
					for (Store store : storeList) {
						if (ObjectUtils.equals(0L, store.getId())) {
							continue;
						}
						if (storeIdString.length() > 0) {
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(storeId);
				}

			}
		} else {
			if (StringUtils.isBlank(storeId)) {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			} else {
				storeIdString.append(storeId);
			}
		}
		model.addAttribute("data", afterSalesService.list(storeIdString.toString(), orderId, productName, categoryId,
				afterSalesType, accountMobile, beginTime, endTime, page));
		return "afterSales/list-data";
	}

	@RequestMapping("add")
	public String afterSalesAdd(Model model) {
		List<AfterSalesType> afterSalesType = afterSalesService.getAllAfterSalesType();
		model.addAttribute("afterSalesTypeList", afterSalesType);
		return "afterSales/add";
	}

	@RequestMapping("orderList/list-data")
	public String orderListData(String storeId, String orderId, String beginTime, String endTime, Page page,
			Model model) {
		if (StringUtils.isNotBlank(storeId)) {
			model.addAttribute("data", orderServise.getOrderListByStoreId(storeId, orderId, beginTime, endTime, page));
		} else {
			model.addAttribute("data", null);
		}
		return "afterSales/orderList-data";
	}

	@RequestMapping("productList/list-data")
	public String productListData(String orderId, String productName, String barCode, Page page, Model model) {
		model.addAttribute("data", orderItemServise.getProductListByOrderId(orderId, productName, barCode, page));
		return "afterSales/productList-data";
	}

	@RequestMapping(value = "save", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(String afterSalesJson, Long storeId, String storeName, Long orderId) {
		if (StringUtils.isBlank(SecurityUtils.getAccountStoreId().toString()) || StringUtils.isBlank(storeId.toString())
				|| !SecurityUtils.getAccountStoreId().equals(storeId) || StringUtils.isBlank(storeName)
				|| StringUtils.isBlank(SecurityUtils.getStoreName())
				|| !(SecurityUtils.getStoreName().equals(storeName)) || StringUtils.isBlank(orderId.toString())
				|| StringUtils.isBlank(afterSalesJson)) {
			logger.error("afterSalesJson is {},storeId is {},storeName is {},orderId is {}", afterSalesJson, storeId,
					storeName, orderId);
			return "2";
		}
		List<AfterSales> afterSalesList = JsonUtils.jsonToList(afterSalesJson, AfterSales.class);
		Long accountId = SecurityUtils.getAccountId();
		afterSalesService.save(afterSalesList, storeId, storeName, orderId, accountId);
		return "1";
	}

	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response, String storeId, String orderId, String productName,
			Long categoryId, String accountMobile, Long afterSalesType, String beginTime, String endTime) {

		boolean isMain = SecurityUtils.isMainStore();
		StringBuilder storeIdString = new StringBuilder();
		if (SecurityUtils.isMainStore()) {
			if (StringUtils.isBlank(storeId)) {
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				if (null != storeList && storeList.size() > 0) {
					for (Store store : storeList) {
						if (storeIdString.length() > 0) {
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(SecurityUtils.getAccountStoreId());
				}
			} else {

				List<Store> storeList = storeService.findByParentId(Long.parseLong(storeId));
				if (null != storeList && storeList.size() > 0) {
					for (Store store : storeList) {
						if (ObjectUtils.equals(0L, store.getId())) {
							continue;
						}
						if (storeIdString.length() > 0) {
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
					}
				} else {
					storeIdString.append(storeId);
				}

			}
		} else {
			if (StringUtils.isBlank(storeId)) {
				storeIdString.append(SecurityUtils.getAccountStoreId());
			} else {
				storeIdString.append(storeId);
			}
		}
		Page page = new Page();
		List<AfterSalesExcel> ases = afterSalesService.listWithoutLimit(storeIdString.toString(), orderId, productName,
				categoryId, afterSalesType, accountMobile, beginTime, endTime, page);
		DecimalFormat df = new DecimalFormat("######0.00");
		String fileName = "售后列表";
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("售后列表");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		int index = 0;
		HSSFCell cell = null;
		if (isMain) {
			cell = row.createCell(index);
			cell.setCellValue("商家名称");
			cell.setCellStyle(style);
			index++;
		}
		cell = row.createCell(index);
		cell.setCellValue("订单编号");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("商品名称");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("商品条码");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("规格");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("商品分类");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("销售价");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("售后类型");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("数量");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("时间");
		cell.setCellStyle(style);
		index++;
		cell = row.createCell(index);
		cell.setCellValue("操作人");
		cell.setCellStyle(style);
		int rowIndex = 1;
		if (ases != null && ases.size() > 0) {
			for (AfterSalesExcel ase : ases) {
				row = sheet.createRow(rowIndex);
				rowIndex++;
				index = 0;
				if (isMain) {
					cell = row.createCell(index);
					cell.setCellValue(ase.getStoreName());
					cell.setCellStyle(style);
					index++;
				}
				cell = row.createCell(index);
				cell.setCellValue(ase.getOrderId());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getProductName());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getBarCode());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getUnitName());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getProductCategoryName());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue("￥" + df.format(ase.getPrice()));
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getAfterSalesTypeName());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(df.format(ase.getAmount()));
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getRemark());
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(DateUtils.format(ase.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
				cell.setCellStyle(style);
				index++;
				cell = row.createCell(index);
				cell.setCellValue(ase.getUsername());
				cell.setCellStyle(style);
			}
		}
		try {
			OutputStream os = response.getOutputStream();
			response.setHeader("Content-disposition",
					"attachment;filename=" + URLEncoder.encode((((null == fileName) || ("".equals(fileName.trim())))
							? ((new Date().getTime()) + "") : fileName.trim()) + ".xls", "utf-8"));
			wb.write(os);
			os.flush();
			os.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"生成Excel文件出错。storeId is {}, orderId is {}, productName is {}, "
							+ "categoryId is {}, accountMobile is {}, afterSalesType is {}, "
							+ "beginTime is {}, endTime is {}",
					storeId, orderId, productName, categoryId, accountMobile, afterSalesType, beginTime, endTime);
			throw new RuntimeException("生成Excel文件出错！");
		}
		return null;
	}
}
