package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.AfterSales;
import cn.lastmiles.bean.AfterSalesExcel;
import cn.lastmiles.bean.AfterSalesType;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.AfterSalesDao;

/**
 * 
 * lastUpdate 2016/10/20
 * 
 * @author shaoyikun
 *
 */
@Service
public class AfterSalesService {

	@Autowired
	private AfterSalesDao afterSalesDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private IdService idService;

	public List<AfterSalesType> getAllAfterSalesType() {
		return afterSalesDao.getAllAfterSalesType();
	}

	public Page list(String storeId, String orderId, String productName, Long categoryId, Long afterSalesType,
			String accountMobile, String beginTime, String endTime, Page page) {

		String accountId = null;
		if (StringUtils.isNotBlank(accountMobile)) {
			Account account = accountService.findByMobile(accountMobile);
			accountId = (account == null ? "查无此人" : account.getId().toString());
		}

		return afterSalesDao.list(storeId, orderId, productName, categoryId, afterSalesType, accountId, beginTime,
				endTime, page);
	}

	@SuppressWarnings("unchecked")
	public List<AfterSalesExcel> listWithoutLimit(String storeId, String orderId, String productName, Long categoryId,
			Long afterSalesType, String accountMobile, String beginTime, String endTime, Page page) {

		String accountId = null;
		if (StringUtils.isNotBlank(accountMobile)) {
			Account account = accountService.findByMobile(accountMobile);
			accountId = (account == null ? "查无此人" : account.getId().toString());
		}

		page = afterSalesDao.listWithoutLimit(storeId, orderId, productName, categoryId, afterSalesType, accountId,
				beginTime, endTime, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) page.getData();
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return BeanUtils.toList(AfterSalesExcel.class, list);
		}
	}

	public void save(List<AfterSales> afterSalesList, Long storeId, String storeName, Long orderId, Long accountId) {
		List<AfterSales> resultList = new ArrayList<AfterSales>();
		Date now = new Date();
		for (AfterSales afterSales : afterSalesList) {
			afterSales.setId(idService.getId());
			afterSales.setStoreId(storeId);
			afterSales.setStoreName(storeName);
			afterSales.setOrderId(orderId);
			afterSales.setCreatedTime(now);
			afterSales.setAccountId(accountId);
			if (afterSales.getAfterSalesTypeId() == null) {
				throw new RuntimeException("商品" + afterSales.getProductName() + "的售后服务类型为空！");
			}
			if (afterSales.getAmount() == null || afterSales.getAmount() <= 0) {
				throw new RuntimeException("商品" + afterSales.getProductName() + "的售后数量异常！");
			}
			if (afterSales.getProductId() == null) {
				throw new RuntimeException("商品" + afterSales.getProductName() + "的商品Id为空！");
			}
			if (afterSales.getProductCategoryId() == null) {
				throw new RuntimeException("商品" + afterSales.getProductName() + "的商品类型为空！");
			}
			resultList.add(afterSales);
		}
		this.save(resultList);
	}

	public void save(List<AfterSales> afterSalesList) {
		for (AfterSales afterSales : afterSalesList) {
			afterSalesDao.save(afterSales);
		}
	}
}
