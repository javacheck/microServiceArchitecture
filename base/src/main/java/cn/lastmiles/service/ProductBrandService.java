package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ProductBrandDao;

@Service
public class ProductBrandService {
	@Autowired
	private ProductBrandDao productBrandDao;
	
	@Autowired
	private IdService idService;

	public Page findAll(String storeIdString, String name, Page page) {
		return productBrandDao.findAll(storeIdString,name, page);
	}

	public Brand findProductBrand(Brand brand) {
		return productBrandDao.findProductBrand(brand);
	}

	public void editProductBrand(Long id, Long storeId, String[] name) {
		if (id == null){
			for(int i=0;i<name.length;i++){
				Brand brand=new Brand();
				brand.setId(idService.getId());
				brand.setName(name[i]);
				brand.setStoreId(storeId);
				productBrandDao.save(brand);
			}
		}else {
			Brand brand=new Brand();
			brand.setId(id);
			brand.setName(name[0]);
			productBrandDao.update(brand);
		}
		
	}

	public Brand findById(Long id) {
		return productBrandDao.findById(id);
	}

	public void deleteById(Long id) {
		productBrandDao.deleteById(id);
		
	}

	public List<Brand> findBrandListByStoreId(Long storeId) {
		
		return productBrandDao.findBrandListByStoreId(storeId);
	}
}
