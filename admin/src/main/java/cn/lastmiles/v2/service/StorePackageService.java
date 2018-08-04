/**
 * createDate : 2016年10月25日下午2:27:22
 */
package cn.lastmiles.v2.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.v2.dao.StorePackageDao;

@Service
public class StorePackageService {

	@Autowired
	private StorePackageDao storePackageDao;
	@Autowired
	private ProductStockService productStockService;
	
	public Page list(String name, Page page) {
		return storePackageDao.list(name,page);
	}

	public Page findProductStockList(String productName, Long storeId, Long productCategoryId, Page page) {
		page = storePackageDao.findProductStockList(productName,storeId,productCategoryId,page); 
		if( page.getTotal().intValue() == 0 ){
			return page;
		}
		List<ProductStock> productStockList = (List<ProductStock>) page.getData();
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService.getAttributeValuesListJointValue(productStock.getAttributeCode(),"|"));
		}
		page.setData(productStockList);
		return page;
	}
	
}
