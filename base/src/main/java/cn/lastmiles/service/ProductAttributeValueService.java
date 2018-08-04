package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.ProductAttributeValueDao;

@Service
public class ProductAttributeValueService {
	@Autowired
	private ProductAttributeValueDao productAttributeValueDao;
	
	@Autowired
	private IdService idService;
	
	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String storeIdString,String name, Page page) {
		return productAttributeValueDao.findAll(storeIdString,name, page);
	}
	
	public List<ProductAttribute> findByCategoryId(Long categoryId) {
		return productAttributeValueDao.findByCategoryId(categoryId);
	}
	
	public void update(ProductAttributeValue productAttributeValue) {
		productAttributeValueDao.update(productAttributeValue);
	}

	public void edit(ProductAttributeValue productAttributeValue) {
		if(productAttributeValue.getId()==null){
			productAttributeValue.setId(idService.getId());
			productAttributeValueDao.save(productAttributeValue);
		}else{
			productAttributeValueDao.update(productAttributeValue);
		}
	}

	public ProductAttributeValue findById(Long id) {
		return productAttributeValueDao.findById(id);
	}
	public ProductAttributeValue findById1(Long id) {
		return productAttributeValueDao.findById1(id);
	}
	public List<ProductAttributeValue> findById2(Long id) {
		return productAttributeValueDao.findById2(id);
	}
	public void deleteById(ProductAttributeValue productAttributeValue) {
		productAttributeValueDao.deleteById(productAttributeValue);
	}
	public void deleteByAttributeId(Long attributeId) {
		productAttributeValueDao.deleteAttributeId(attributeId);
	}
	
	public ProductAttributeValue findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		return productAttributeValueDao.findProductAttributeValue(productAttributeValue);
	}
	public Integer findByAttributeId(Long attributeId){
		return productAttributeValueDao.findByAttributeId(attributeId);
	}
	public List<ProductAttributeValue> findByAttributeId1(Long attributeId){
		return productAttributeValueDao.findByAttributeId1(attributeId);
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

	public void editProductAttribute(Long id, Long attributeId, String[] value) {
		if (id == null){
			for(int i=0;i<value.length;i++){
				ProductAttributeValue productAttributeValue=new ProductAttributeValue();
				productAttributeValue.setId(idService.getId());
				productAttributeValue.setAttributeId(attributeId);
				productAttributeValue.setValue(value[i]);
				productAttributeValueDao.save(productAttributeValue);
			}
		}else {
			ProductAttributeValue productAttributeValue=new ProductAttributeValue();
			productAttributeValue.setId(id);
			productAttributeValue.setAttributeId(attributeId);
			productAttributeValue.setValue(value[0]);
			productAttributeValueDao.update(productAttributeValue);
		}
		
	}
	
	/**
	 * 
	 * @param attributeCode
	 * @return
	 */
	public List<ProductAttributeValue> findByAttributeCode(String attributeCode){
		List<ProductAttributeValue> productAttributeValues = new ArrayList<ProductAttributeValue>();
		if (StringUtils.isNotBlank(attributeCode)) {//安全判断
			String [] ids = attributeCode.split(Constants.ProductStock.ATTRIBUTECODE_SEGMENTATION);
			if (ids!=null) {//安全判断
				for (String id : ids) {
					ProductAttributeValue productAttributeValue =this.findById(Long.parseLong(id));
					if (productAttributeValue!=null) {//安全判断
						productAttributeValues.add(productAttributeValue);
					}
				}
				
			}
		}
		return productAttributeValues;
	}

	public void deleteById(Long productAttributeValueId) {
		productAttributeValueDao.deleteById(productAttributeValueId);
		
	}

	public void saveProductAttributeValue(ProductAttributeValue pav) {
		pav.setId(idService.getId());
		productAttributeValueDao.save(pav);
		
	}

	public void save(ProductAttributeValue productAttributeValue) {
		if(productAttributeValue.getId()==null){
			productAttributeValue.setId(idService.getId());
		}
		productAttributeValueDao.save(productAttributeValue);
	}

	
	
}
