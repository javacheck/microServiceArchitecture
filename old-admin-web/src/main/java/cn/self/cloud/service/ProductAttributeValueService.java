package cn.self.cloud.service;

import java.util.List;

import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.bean.ProductAttributeValue;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.ProductAttributeValueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeValueService {
	@Autowired
	private ProductAttributeValueDao productAttributeValueDao;
	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String name, Page page) {
		return productAttributeValueDao.findAll(name, page);
	}
	
	public List<ProductAttribute> findByProductId(Long productId) {
		return productAttributeValueDao.findByProductId(productId);
	}
	
	public void update(ProductAttributeValue productAttributeValue) {
		productAttributeValueDao.update(productAttributeValue);
	}

	public void save(ProductAttributeValue productAttributeValue) {
		productAttributeValueDao.save(productAttributeValue);
	}

	public ProductAttributeValue findById(Long id) {
		return productAttributeValueDao.findById(id);
	}
	
	public void deleteById(ProductAttributeValue productAttributeValue) {
		productAttributeValueDao.deleteById(productAttributeValue);
	}
	
	public ProductAttributeValue findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		return productAttributeValueDao.findProductAttributeValue(productAttributeValue);
	}
	public Integer findByAttributeId(Long attributeId){
		return productAttributeValueDao.findByAttributeId(attributeId);
	}

	public List<ProductAttributeValue> findByAttributeId(ProductAttributeValue productAttributeValue) {
		return productAttributeValueDao.findByAttributeId(productAttributeValue);
	}
	public List<ProductAttributeValue> productAttributeValueList(
			Long productAttributeId) {
		return productAttributeValueDao.productAttributeValueList(productAttributeId);
	}
	public List<ProductAttributeValue> findByAttributeIdList(Long productAttributeId) {
		return productAttributeValueDao.findByAttributeIdList(productAttributeId);
	}
	
	/**
	 * 查找一个属于下所有的节点 通过属于Id查找
	 * @return
	 */
	public List<ProductAttributeValue> findAllBrothersById(Long id){
		return productAttributeValueDao.findAllBrothersById(id);
	}
	
}
