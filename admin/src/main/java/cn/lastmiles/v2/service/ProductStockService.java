package cn.lastmiles.v2.service;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.v2.dao.ProductStockDao;

@Service("cn.lastmiles.v2.service.ProductStockService")
public class ProductStockService {
	private final static Logger logger = LoggerFactory.getLogger(ProductStockService.class);
	@Autowired
	private ProductStockDao productStockDao;
	@SuppressWarnings("unchecked")
	public Page findAllPage(String storeIdString, String name, String barCode,
			Integer sort, String brandName, Long categoryId, Integer alarmType,
			Integer shelves, Page page) {
		List<ProductStock> pss=new ArrayList<ProductStock>();
		List<ProductStock> psList=(List<ProductStock>) productStockDao.findAllPage(storeIdString,name,barCode,sort,brandName,categoryId,alarmType,shelves,page).getData();
		for(ProductStock ps:psList){
			String attributeValues="";
			
			if(ps!=null && ps.getAttributeCode()!=null && ps.getAttributeName()!=null && ps.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				for(int i=1;i<arrs.length;i++){
					attributeValues+=arrs[i]+"|";
				}
				ps.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				
			}
			
			pss.add(ps);
		}
		page.setData(pss);
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page findAllList(String storeIdString, String name, String barCode,
			Integer sort, String brandName, Long categoryId, Integer alarmType,
			Integer shelves, Page page) {
		List<ProductStock> pss=new ArrayList<ProductStock>();
		List<ProductStock> psList=(List<ProductStock>) productStockDao.findAllList(storeIdString,name,barCode,sort,brandName,categoryId,alarmType,shelves,page).getData();
		for(ProductStock ps:psList){
			String attributeValues="";
			
			if(ps!=null && ps.getAttributeCode()!=null && ps.getAttributeName()!=null && ps.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				for(int i=1;i<arrs.length;i++){
					attributeValues+=arrs[i]+"|";
				}
				ps.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				
			}
			pss.add(ps);
		}
		page.setData(pss);
		return page;
	}
	public List<ProductStock> findProductStockList(String productStockIds) {
		List<ProductStock> pss=new ArrayList<ProductStock>();
		List<ProductStock> psList=productStockDao.findProductStockList(productStockIds);
		for(ProductStock ps:psList){
			String attributeValues="";
			if(ps!=null && ps.getAttributeCode()!=null && ps.getAttributeName()!=null && ps.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				for(int i=1;i<arrs.length;i++){
					attributeValues+=arrs[i]+"|";
				}
				ps.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				
			}
			pss.add(ps);
		}
		return pss;
	}
	public ProductStock findProductStockById(Long id) {
		return productStockDao.findProductStockById(id);
	}
	@SuppressWarnings("unchecked")
	public Page findAllocationList(String storeIdString, String name, String barCode,
			Integer sort, String brandName, Long categoryId, Integer alarmType,
			Integer shelves, Page page) {
		List<ProductStock> pss=new ArrayList<ProductStock>();
		List<ProductStock> psList=(List<ProductStock>) productStockDao.findAllocationList(storeIdString,name,barCode,sort,brandName,categoryId,alarmType,shelves,page).getData();
		for(ProductStock ps:psList){
			String attributeValues="";
			
			if(ps!=null && ps.getAttributeCode()!=null && ps.getAttributeName()!=null && ps.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				for(int i=1;i<arrs.length;i++){
					attributeValues+=arrs[i]+"|";
				}
				ps.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				
			}
			pss.add(ps);
		}
		page.setData(pss);
		return page;
	}
	
}
