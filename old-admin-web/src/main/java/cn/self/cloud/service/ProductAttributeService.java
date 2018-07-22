package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;

import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.ProductAttributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeService {
	@Autowired
	private ProductAttributeDao productAttributeDao;
	
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String name, Page page) {
		return productAttributeDao.findAll(name, page);
	}

	public void update(ProductAttribute productAttribute) {
		productAttributeDao.update(productAttribute);
	}

	public void save(ProductAttribute productAttribute) {
		productAttributeDao.save(productAttribute);
	}

	public ProductAttribute findById(Long id) {
		return productAttributeDao.findById(id);
	}
	
	public void deleteById(ProductAttribute productAttribute) {
		productAttributeDao.deleteById(productAttribute);
	}
	
	public ProductAttribute findProductAttribute(ProductAttribute productAttribute) {
		return productAttributeDao.findProductAttribute(productAttribute);
	}

	public List<ProductAttribute> productAttributeList(Long productId) {
		List<ProductAttribute> list =productAttributeDao.productAttributeList(productId);
		List<ProductAttribute> listr = new ArrayList<ProductAttribute>();
		if (list==null) {
			return list;
		}
		for (ProductAttribute productAttribute :list ) {
			listr.add(this.infull(productAttribute));
		}
		return listr;
	}
	public ProductAttribute infull(ProductAttribute productAttribute){
		if (productAttribute==null) {
			return productAttribute;
		}
		productAttribute.setProductAttributeValues(productAttributeValueService.findByAttributeIdList(productAttribute.getId()));;
		return productAttribute;
	}
}
