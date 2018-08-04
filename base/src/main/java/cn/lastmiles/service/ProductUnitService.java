package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ProductUnitDao;

@Service
public class ProductUnitService {
	
	@Autowired
	private ProductUnitDao productUnitDao;
	
	@Autowired
	private IdService idService;
	
	public Page findAllPage(String storeIdString, String name, Page page) {
		
		return productUnitDao.findAllPage(storeIdString,name,page);
	}

	public ProductUnit findProductUnitExist(ProductUnit productUnit) {
		return productUnitDao.findProductUnitExist(productUnit);
	}
	
	/**
	 * 通过商家ID查询出其所有的商品单位
	 * @param storeId
	 * @return
	 */
	public List<ProductUnit> getProductUnitListByStoreID(Long storeId){
		return productUnitDao.getProductUnitListByStoreID(storeId);
	}
	
	public ProductUnit findProductUnitById(Long id) {
		ProductUnit pu=productUnitDao.findProductUnitById(id);
		return pu;
	}
	
	public void editProductUnit(Long id, Long storeId, String[] unitName) {
		if (id == null) {
			for (int i = 0; i < unitName.length; i++) {
				ProductUnit productUnit = new ProductUnit();
				productUnit.setId(idService.getId());
				productUnit.setName(unitName[i]);
				productUnit.setStoreId(storeId);
				productUnitDao.save(productUnit);
			}
		} else {
			ProductUnit productUnit = new ProductUnit();
			productUnit.setId(id);
			productUnit.setName(unitName[0]);
			productUnit.setStoreId(storeId);
			productUnitDao.update(productUnit);
		}
	}

	public void deleteById(Long id) {
		productUnitDao.deleteById(id);
		
	}

	public Integer findStockCountByUnitId(Long productUnitId) {
		return productUnitDao.findStockCountByUnitId(productUnitId);
	}

}
