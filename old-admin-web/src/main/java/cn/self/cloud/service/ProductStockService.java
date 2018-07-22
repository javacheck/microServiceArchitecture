package cn.self.cloud.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.self.cloud.bean.ProductAttributeValue;
import cn.self.cloud.bean.ProductImage;
import cn.self.cloud.bean.ProductStock;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.ProductStockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductStockService {
	
	private final static Logger logger = LoggerFactory.getLogger(ProductStockService.class);
	@Autowired
	private ProductStockDao productStockDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String name, Page page) {
		return productStockDao.findAll(name, page);
	}
	public ProductStock findProductStock(ProductStock productStock) {
		return productStockDao.findProductStock(productStock);
	}
	public void update(ProductStock productStock) {
		productStockDao.update(productStock);
	}

	public void save(ProductStock productStock) {
		productStockDao.save(productStock);
	}
	public void deleteById(Long id) {
		productStockDao.deleteById(id);
	}
	public ProductStock findById(Long id) {
		return productStockDao.findById(id);
	}
	public List<ProductStock> findByProductId(Long productId) {
		return productStockDao.findByProductId(productId);
	}
	
	public ProductStock findByStockId(Long id){
		ProductStock productStock=productStockDao.findByStockId(id);
		if (productStock==null) {
			return null;
		}
		productStock.setProduct(productService.findById(productStock.getProductId()));
		return productStock;
	}
	public void saveProductImage(ProductImage productImage) {
		productStockDao.saveProductImage(productImage);
		
	}
	public ProductImage findProductImage(ProductImage productImage) {
		return productStockDao.findProductImage(productImage);
	}
	public ProductStock findProductStock(String attributeCode) {
		ProductStock productStock = new ProductStock();
		productStock.setAttributeCode(attributeCode);
		return this.findProductStock(productStock);
	}
	/**
	 * 根据传递过来的参数 返回所有可能符合的库存信息
	 * @param list 传递过来 AttributeCode 的队列
	 * @param productId 商品ID
	 * @param storeId 商店ID
	 * @return
	 */
	public String findByFuzzy(List<Long> list,Long productId,Long storeId){
		List<String> papms= new ArrayList<String>();
		if (list==null||list.size()<=0) {//
			return this.contentAttributeCode(productStockDao.findByFuzzyStockId(null,productId,storeId),null);
		}
		if (list.size()==1) {//只选中一个参数
			List<ProductAttributeValue> productAttributeValues=productAttributeValueService.findAllBrothersById(list.get(0));//查找同一属性的所有属性值
			for (ProductAttributeValue productAttributeValue : productAttributeValues) {
				if (productStockDao.findByFuzzyAttributeCode(productAttributeValue.getId(), productId, storeId).size()<=0) {//是否含有这个属性值的库存
					productAttributeValues.remove(productAttributeValue);
				}
			}
			this.contentAttributeCode(productStockDao.findByFuzzyAttributeCode(list.get(0),productId,storeId),productAttributeValues);
		}
		List<ProductStock> listProductStocks = new ArrayList<ProductStock>();
		for (int i = 0; i < list.size(); i++) {
			papms= new ArrayList<String>();
			StringBuffer sf=new StringBuffer();
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
				} else {
					Long l = list.get(j);
					sf.append("-" + l + "-%");
				}
			}//J循环结束
			papms.add("%"+sf.toString());
			papms.add(sf.substring(1));
			papms.add("%"+sf.substring(0,sf.length()-2));
			if (list.size()>=3) {//如果超过三个参数多一个选择
				papms.add(sf.substring(1,sf.length()-2));
			}
			logger.debug("papms is :"+papms.toString());
			listProductStocks.addAll(productStockDao.findByFuzzyStockId(papms,productId,storeId));
		}//I循环结束
		return this.contentAttributeCode(listProductStocks,null);
	}
	/**
	 * 根据传递过来的list 
	 * 返回所有AttributeCode 拼接
	 * @param list
	 * @return
	 */
	public String  contentAttributeCode(List<ProductStock> list,List<ProductAttributeValue> productAttributeValues){
		Set<String> sets= new HashSet<String>();
		if (list!=null&&list.size()>0) {
			for (ProductStock productStock : list) {
				if (StringUtils.isNotBlank(productStock.getAttributeCode())) {
					String [] attributeCodes=productStock.getAttributeCode().split("-");
					for (String attributeCode : attributeCodes) {
						sets.add(attributeCode);
					}
				}
			}
		}
		if (productAttributeValues!=null&&productAttributeValues.size()>0) {
			for (ProductAttributeValue productAttributeValue : productAttributeValues) {
				sets.add(productAttributeValue.getId()+"");
			}
		}
		StringBuffer sf = new StringBuffer();
		for (String set : sets) {
			sf.append("-"+set);
		}
		return StringUtils.isNotBlank(sf.toString())?sf.deleteCharAt(0).toString():sf.toString();
	}
	
	
}
