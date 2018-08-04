package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.ProductAttributeDao;

@Service
public class ProductAttributeService {
	private static  final Logger logger = LoggerFactory.getLogger(ProductAttributeValueService.class);
	@Autowired
	private ProductAttributeDao productAttributeDao;

	@Autowired
	private ProductAttributeValueService productAttributeValueService;

	@Autowired
	private IdService idService;

	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String storeIdString, String name, Page page) {
		return productAttributeDao.findAll(storeIdString, name, page);
	}

	public void update(ProductAttribute productAttribute) {
		productAttributeDao.update(productAttribute);
	}

	public void edit(ProductAttribute productAttribute) {
		if (productAttribute.getId() == null) {
			productAttribute.setId(idService.getId());
			productAttributeDao.save(productAttribute);
		} else {
			productAttributeDao.update(productAttribute);
		}
	}

	public ProductAttribute findById(Long id) {
		return productAttributeDao.findById(id);
	}

	public void deleteById(ProductAttribute productAttribute) {
		productAttributeDao.deleteById(productAttribute);
	}

	public ProductAttribute findProductAttribute(
			ProductAttribute productAttribute) {
		return productAttributeDao.findProductAttribute(productAttribute);
	}

	public List<ProductAttribute> productAttributeList(Long categoryId,
			Long storeId) {
		List<ProductAttribute> list = productAttributeDao.productAttributeList(
				categoryId, storeId);
		List<ProductAttribute> listr = new ArrayList<ProductAttribute>();
		if (list == null) {
			return list;
		}
		for (ProductAttribute productAttribute : list) {
			listr.add(this.infull(productAttribute));
		}
		return listr;
	}
	
	

	public ProductAttribute productAttribute(Long attributeId) {
		ProductAttribute list = productAttributeDao.productAttribute(attributeId);
		return list;
	}
	
	public List<ProductAttribute> productAttributeList1(Long categoryId) {
		return productAttributeDao.productAttributeList1(categoryId);
	}

	public List<ProductAttribute> productAttributeListByCategoryId(
			Long categoryId) {
		Long storeId = null;
		List<ProductAttribute> list = productAttributeDao.productAttributeList(
				categoryId, storeId);
		List<ProductAttribute> listr = new ArrayList<ProductAttribute>();
		if (list == null) {
			return list;
		}
		for (ProductAttribute productAttribute : list) {
			if (productAttribute != null) {
				listr.add(this.infull(productAttribute));
			}
		}
		return listr;
	}
	
	public List<ProductAttribute> productAttributeListByProductIdAndPsId(Long productId,ProductStock pStock) {
		List<ProductStockAttributeValue> valueList = productAttributeDao.findByProductStockAttributeValue(productId,pStock.getId());
		logger.debug("获取的属性值列表为：{}",valueList);
		
		if( null == valueList || valueList.size() <= 0 ){
			return null;
		}
		
		StringBuilder link = new StringBuilder(15);
		boolean firstAttribute = true;
		boolean secondAttribute = true;
		for (ProductStockAttributeValue productStockAttributeValue : valueList) {
			if( productStockAttributeValue.getNumber().intValue() == 1 ){
				if(firstAttribute){
					if( StringUtils.isNotBlank(link.toString())){
						link.append(",");
					}
					link.append(productStockAttributeValue.getProductAttributeId());				
					firstAttribute = false;
				}
			}
			if( productStockAttributeValue.getNumber().intValue() == 2 ){
				if(secondAttribute){
					if( StringUtils.isNotBlank(link.toString())){
						link.append(",");
					}
					link.append(productStockAttributeValue.getProductAttributeId());
					secondAttribute = false;
				}
			}
		}
		logger.debug("得到的属性ID是：{}",link.toString());
		
		List<ProductAttribute> list = productAttributeDao.productAttributeList(link.toString());
		logger.debug("根据属性ID查询出来的属性对象是：{}",list);
		
		List<ProductAttribute> listr = new ArrayList<ProductAttribute>();
		if (list == null) {
			return list;
		}
		for (ProductAttribute productAttribute : list) {
			if (productAttribute != null) {
				listr.add(this.newInfull(productAttribute,valueList,pStock));
			}
		}
		return listr;
	}
	
	public ProductAttribute newInfull(ProductAttribute productAttribute,List<ProductStockAttributeValue> valueList,ProductStock pStock) {
		if (productAttribute == null) {
			return productAttribute;
		}
		List<ProductAttributeValue> pvList = new ArrayList<ProductAttributeValue>();
		
		for (ProductStockAttributeValue productStockAttributeValue : valueList) {
			logger.debug("对比的属性值ID是：{},{}",productAttribute.getId(),productStockAttributeValue.getProductAttributeId());
			
			if( ObjectUtils.equals(productAttribute.getId(),productStockAttributeValue.getProductAttributeId()) ){
				ProductAttributeValue pv = new ProductAttributeValue();
				pv.setId(productStockAttributeValue.getId());
				pv.setAttributeId(productStockAttributeValue.getProductAttributeId());
				pv.setValue(productStockAttributeValue.getValue());
				pvList.add(pv);
			}
		}
		productAttribute.setProductAttributeValues(pvList);				
		return productAttribute;
	}

	public ProductAttribute infull(ProductAttribute productAttribute) {
		if (productAttribute == null) {
			return productAttribute;
		}
		productAttribute.setProductAttributeValues(productAttributeValueService
				.findByAttributeIdList(productAttribute.getId()));
		;
		return productAttribute;
	}

	/**
	 * 根据分类删除属性 和属性值
	 * 
	 * @param id
	 */
	public void deleteByCategoryId(Long categoryId) {
		List<ProductAttribute> list = this
				.productAttributeListByCategoryId(categoryId);
		if (list != null && list.size() >= 1) {
			// 删除属性值
			for (ProductAttribute productAttribute : list) {
				if (productAttribute != null
						&& productAttribute.getId() != null) {
					productAttributeValueService
							.deleteByAttributeId(productAttribute.getId());// 删除属性值
				}
			}
			productAttributeDao.deleteByCategoryId(categoryId);// 删除属性
		}
	}

	public void editProductAttribute(Long id, Long categoryId, String[] name) {
		if (id == null) {
			for (int i = 0; i < name.length; i++) {
				ProductAttribute productAttribute = new ProductAttribute();
				productAttribute.setId(idService.getId());
				productAttribute.setName(name[i]);
				productAttribute.setCategoryId(categoryId);
				productAttributeDao.save(productAttribute);
			}
		} else {
			ProductAttribute productAttribute = new ProductAttribute();
			productAttribute.setId(id);
			productAttribute.setName(name[0]);
			productAttribute.setCategoryId(categoryId);
			productAttributeDao.update(productAttribute);
		}
	}

	public List<ProductAttribute> findById1(Long productAttributeId) {
		return productAttributeDao.findById1(productAttributeId);
	}

	public void deleteById(Long productAttributeId) {
		productAttributeDao.deleteById(productAttributeId);

	}

	public ProductAttribute findProductAttribute(Long categoryId, String name) {
		return productAttributeDao.findProductAttribute(categoryId, name);
	}

	public void saveProductAttribute(ProductAttribute pa) {
		pa.setId(idService.getId());
		productAttributeDao.saveProductAttribute(pa);

	}

	public List<ProductAttribute> findByCategoryId(Long id) {
		return productAttributeDao.findByCategoryId(id);
	}

	public void save(ProductAttribute productAttribute) {
		if (productAttribute.getId() == null) {
			productAttribute.setId(idService.getId());
		}
		productAttributeDao.save(productAttribute);
	}

	public List<ProductStockAttributeValue> findByProductStockAttributeValue(Long productId) {
		return productAttributeDao.findByProductStockAttributeValue(productId);
	}
	public List<ProductStockAttributeValue> findProductStockAttributeValue(Long productId,Long attributeId) {
		return productAttributeDao.findProductStockAttributeValue(productId,attributeId);
	}
}
