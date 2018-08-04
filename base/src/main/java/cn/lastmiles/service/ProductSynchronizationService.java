package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.ProductImageDao;
import cn.lastmiles.dao.ProductSynchronizationDao;
import cn.lastmiles.dao.ProductUnitDao;

@Service
public class ProductSynchronizationService {
	private final static Logger logger = LoggerFactory.getLogger(ProductSynchronizationService.class);
	@Autowired
	private ProductSynchronizationDao productSynchronizationDao;
	@Autowired
	private ProductUnitDao productUnitDao;
	@Autowired
	private IdService idService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ProductImageDao productImageDao;
	
	@Transactional
	public void sync(Long fromStoreId, Long toStoreId,Long accountId){
		//总部分类
		List<ProductCategory> pcList=productSynchronizationDao.findProductCategoryByStoreId(fromStoreId);
		Map<Long, Long> catIds = new HashMap<Long, Long>();
		for (ProductCategory cat : pcList){
			catIds.put(cat.getId(), idService.getId());
		}
		String insert = "insert into t_product_category(id,name,pId,storeId,path,type,sort,level) values(?,?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (ProductCategory cat : pcList){
			Object[] obj = new Object[8];
			obj[0] = catIds.get(cat.getId());
			obj[1] = cat.getName();
			if (cat.getpId() == null){
				obj[2] = null;
			}else {
				obj[2] = catIds.get(cat.getpId());
			}
			obj[3] = toStoreId;
			
			String[] arr = cat.getPath().split("_");
			String path = "";
			for (String s : arr){
				if (StringUtils.isBlank(path)){
					path = catIds.get(Long.valueOf(s)).toString();
				}else {
					path = path + "_" + catIds.get(Long.valueOf(s));
				}
			}
			obj[4] = path;
			obj[5] = cat.getType();
			obj[6] = cat.getSort();
			obj[7] = cat.getLevel();
			
			batchArgs.add(obj);
		}
		
		jdbcTemplate.batchUpdate(insert, batchArgs);
		
		//单位
		List<ProductUnit> productUnitList = productUnitDao.getProductUnitListByStoreID(fromStoreId);
		Map<Long,ProductUnit> productUnitIds = new HashMap<Long, ProductUnit>();
		if (!productUnitList.isEmpty()){
			for (ProductUnit productUnit : productUnitList){
				ProductUnit pu = new ProductUnit();
				pu.setId(idService.getId());
				pu.setName(productUnit.getName());
				pu.setStoreId(toStoreId);
				productUnitIds.put(productUnit.getId(), pu);
				
				productUnitDao.save(pu);
			}
		}

		//品牌
		List<Brand> brandList=productSynchronizationDao.findBrandByStoreId(fromStoreId);
		Map<Long,Long> brandIds = new HashMap<Long, Long>();
		if(!brandList.isEmpty()){//总部品牌不为空
			for(int i=0;i<brandList.size();i++){
				Brand brand=new Brand();
				brand.setId(idService.getId());//品牌
				brand.setName(brandList.get(i).getName());//品牌名称
				brand.setStoreId(toStoreId);//商家ID
				brandIds.put(brandList.get(i).getId(), brand.getId());
				productSynchronizationDao.saveBrand(brand);
			}
		}
		
		/*//属性
		Set<Long> setIds = catIds.keySet();
		Map<Long, Long> attrIds = new HashMap<Long, Long>();
		for (Long id : setIds){
			List<ProductAttribute> paList=productSynchronizationDao.findProductAttributeByCategoryId(id);
			System.out.println(paList);
			for (ProductAttribute pattri : paList){
				ProductAttribute pa=new ProductAttribute();
				pa.setId(idService.getId());//属性ID
				pa.setName(pattri.getName());//属性名称
				pa.setCategoryId(catIds.get(pattri.getCategoryId()));//分类id
				attrIds.put(pattri.getId(), pa.getId());
				productSynchronizationDao.saveProductAttribute(pa);
			}
		}
		
		//属性值
		setIds = attrIds.keySet();
		Map<Long, Long> attrValueIds = new HashMap<Long, Long>();
		for (Long id : setIds){
			List<ProductAttributeValue> pavList=productSynchronizationDao.findProductAttributeValueByAttributeId(id);
			
			if(!pavList.isEmpty()){//属性值列表不为空
				for(int k=0;k<pavList.size();k++){
					ProductAttributeValue pav=new ProductAttributeValue();
					pav.setId(idService.getId());//属性值ID
					pav.setValue(pavList.get(k).getValue());//属性值
					pav.setAttributeId(attrIds.get(id));//属性Id
					
					attrValueIds.put(pavList.get(k).getId(), pav.getId());
					
					productSynchronizationDao.saveProductAttributeValue(pav);
				}
			}
		}
		*/
		
		//查总店的商品列表
		List<Product> pList=productSynchronizationDao.findProductByStoreId(fromStoreId);
		Map<Long, Long> proIds = new HashMap<Long, Long>();
		for(Product pro : pList){
			Product p=new Product();
			
			if (pro.getBrandId() != null){
				p.setBrandId(brandIds.get(pro.getBrandId()));//品牌ID
			}
			
			p.setId(idService.getId());//商品ID
			p.setName(pro.getName());//商品名称
			p.setAccountId(accountId);//当前操作人
			p.setShortName(pro.getShortName());
			p.setCategoryId(catIds.get(pro.getCategoryId()));//分类ID
			p.setStoreId(toStoreId);//商家ID
			p.setType(pro.getType());//是否有属性
			
			proIds.put(pro.getId(), p.getId());
			productSynchronizationDao.saveProduct(p);
		}
		
		
		//查总店的商品库存列表
		List<ProductStock> psList=productSynchronizationDao.findProductStockByStoreId(fromStoreId);
		Map<Long,Long> stockIds = new HashMap<Long, Long>();
		Map<Long,Long> attrValueIds = new HashMap<Long, Long>();
		for(ProductStock stock : psList){
			ProductStock ps=new ProductStock();
			ps.setId(idService.getId());//库存ID
			
			if (stock.getStock() != null && stock.getStock().doubleValue() == Constants.ProductStock.Store_Infinite.doubleValue()){
				ps.setStock(Constants.ProductStock.Store_Infinite);//库存设为0
			}else {
				ps.setStock(0d);//库存设为0
			}
			ps.setAlarmValue(stock.getAlarmValue());//警报值设为0
			ps.setCategoryId(catIds.get(stock.getCategoryId()));//分类ID
			ps.setProductId(proIds.get(stock.getProductId()));//商品ID
			ps.setAccountId(accountId);//操作人ID
			ps.setStoreId(toStoreId);//商店ID
			
			ps.setPrice(stock.getPrice());//价格
			ps.setMarketPrice(stock.getMarketPrice());//市场价
			ps.setBarCode(stock.getBarCode());//条码
			ps.setRemarks(stock.getRemarks());//商品简介
			ps.setShelves(stock.getShelves());//是否上架
			ps.setType(stock.getType());//是否有属性
			ps.setMemberPrice(stock.getMemberPrice());//会员价
			ps.setCostPrice(0.0);//成本价
			ps.setFreezeNum(stock.getFreezeNum());//冻结库存数量
			ps.setDetails(stock.getDetails());
			ps.setSort(stock.getSort());
			ps.setCreateTime(new Date());
			ps.setUnitName(stock.getUnitName());
			if (stock.getUnitId() == null){
				ps.setUnitId(null);
			}else {
				ps.setUnitId(productUnitIds.get(stock.getUnitId()).getId());
			}
			ps.setWeighing(stock.getWeighing());
			ps.setReturnGoods(stock.getReturnGoods());
			ps.setImageId(stock.getImageId());
			
			ps.setAttributeName(stock.getAttributeName());//属性值相连
			
			if(StringUtils.isBlank(stock.getAttributeCode())){
				ps.setAttributeCode(null);//属性值ID相连(无属性商品)
			}else{
				String[] attributeValues = stock.getAttributeCode().split("-");
				String avs = null;
				for(int j=0;j<attributeValues.length;j++){
					Long id = attrValueIds.get(Long.valueOf(attributeValues[j]));
					if (id == null){
						id = idService.getId();
						attrValueIds.put(Long.valueOf(attributeValues[j]), id);
					}
					if (avs == null){
						avs = id.toString();
					}else {
						avs = avs + "-" + id;
					}
				}
				attributeValues = avs.split("-");
				List<Long> list = new ArrayList<Long>();
				for (String s : attributeValues){
					list.add(Long.valueOf(s));
				}
				Collections.sort(list);
				avs = null;
				for (Long id : list){
					if (avs == null){
						avs = id.toString();
					}else {
						avs = avs + "-" + id;
					}
				}
				ps.setAttributeCode(avs);
			}
			
			stockIds.put(stock.getId(), ps.getId());
			productSynchronizationDao.saveProductStock(ps);
		}
		
		//规格
		List<ProductStockAttributeValue> attrValueList = productSynchronizationDao.findAllProductStockAttributeValue(fromStoreId);
		for (ProductStockAttributeValue psav : attrValueList){
			psav.setId(attrValueIds.get(psav.getId()));
			psav.setProductId(proIds.get(psav.getProductId()));
			psav.setProductStockId(stockIds.get(psav.getProductStockId()));
			
			JdbcUtils.save(psav);
		}
		
		//图片
		for (Long id : stockIds.keySet()){
			List<ProductImage> list = productImageDao.getByProductStockID(id);
			for (ProductImage img : list){
				img.setProductStockId(stockIds.get(id));
				productImageDao.addImage(img);
			}
		}
	}
	
	public List<ProductStock> findStockExist(Long storeId) {
		
		return productSynchronizationDao.findStockExist(storeId);
	}

	public void edit(Long fromStoreId, Long toStoreId,Long accountId) {
		//总部分类
		List<ProductCategory> pcList=productSynchronizationDao.findProductCategoryByStoreId(fromStoreId);
		
		for(int i=0;i<pcList.size();i++){
			
			ProductCategory pc=new ProductCategory();
			pc.setId(idService.getId());//分类ID
			pc.setName(pcList.get(i).getName());//分类名称
			pc.setType(1);//自定义分类
			pc.setStoreId(toStoreId);//调入店铺名称
			pc.setPath(String.valueOf(pc.getId()));//父节点为本身
			pc.setSort(i);
			
			productSynchronizationDao.saveProductCategroy(pc);
			List<ProductAttribute> paList=productSynchronizationDao.findProductAttributeByCategoryId(pcList.get(i).getId());
			if(!paList.isEmpty()){//分类下的属性列表不为空
				for(int j=0;j<paList.size();j++){
					ProductAttribute pa=new ProductAttribute();
					pa.setId(idService.getId());//属性ID
					pa.setName(paList.get(j).getName());//属性名称
					pa.setCategoryId(pc.getId());//分类名称
					productSynchronizationDao.saveProductAttribute(pa);
					
					List<ProductAttributeValue> pavList=productSynchronizationDao.findProductAttributeValueByAttributeId(paList.get(j).getId());
					
					if(!pavList.isEmpty()){//属性值列表不为空
						for(int k=0;k<pavList.size();k++){
							ProductAttributeValue pav=new ProductAttributeValue();
							pav.setId(idService.getId());//属性值ID
							pav.setValue(pavList.get(k).getValue());//属性值
							pav.setAttributeId(pa.getId());//属性Id
							
							productSynchronizationDao.saveProductAttributeValue(pav);
						}
					}
				}
			}
		}
		
		List<Brand> brandList=productSynchronizationDao.findBrandByStoreId(fromStoreId);
		if(!brandList.isEmpty()){//总部品牌不为空
			for(int i=0;i<brandList.size();i++){
				Brand brand=new Brand();
				brand.setId(idService.getId());//品牌
				brand.setName(brandList.get(i).getName());//品牌名称
				brand.setStoreId(toStoreId);//商家ID
				productSynchronizationDao.saveBrand(brand);
			}
		}
		//查总店的商品列表
		List<Product> pList=productSynchronizationDao.findProductByStoreId(fromStoreId);
		
		for(int i=0;i<pList.size();i++){
			Product p=new Product();
			if(pList.get(i).getBrandId()!=null){
				Brand b=productSynchronizationDao.findBrand(pList.get(i).getBrandName(),toStoreId);
				p.setBrandId(b.getId());//品牌ID
			}
			p.setId(idService.getId());//商品ID
			p.setName(pList.get(i).getName());//商品名称
			p.setAccountId(accountId);//当前操作人
			ProductCategory pc=productSynchronizationDao.findCategory(pList.get(i).getCategoryName(),toStoreId);
			p.setCategoryId(pc.getId());//分类ID
			p.setStoreId(toStoreId);//商家ID
			p.setType(pList.get(i).getType());//是否有属性
			productSynchronizationDao.saveProduct(p);
		}
		//查总店的商品库存列表
		List<ProductStock> psList=productSynchronizationDao.findProductStockByStoreId(fromStoreId);
		
		for(int i=0;i<psList.size();i++){
			ProductStock ps=new ProductStock();
			ps.setId(idService.getId());//库存ID
			ps.setStock(0d);//库存设为0
			ps.setAlarmValue(0d);//警报值设为0
			ProductCategory pc=productSynchronizationDao.findCategory(psList.get(i).getCategoryName(),toStoreId);
			ps.setCategoryId(pc.getId());//分类ID
			Product p=productSynchronizationDao.findProduct(psList.get(i).getProductName(),pc.getId());
			ps.setProductId(p.getId());//商品ID
			ps.setAccountId(accountId);//操作人ID
			ps.setStoreId(toStoreId);//商店ID
			if(psList.get(i).getAttributeCode()==null){
				ps.setAttributeCode(null);//属性值ID相连(无属性商品)
			}else{
				String[] attributeValues=psList.get(i).getAttributeCode().split("-");
				for(int j=0;j<attributeValues.length;j++){
					//查出属性值
					ProductAttributeValue pav=productSynchronizationDao.findPAVById(Long.parseLong(attributeValues[j]));
					//查出属性
					ProductAttribute pa=productSynchronizationDao.findPAById(pav.getAttributeId());
					//查出分类
					ProductCategory productCategory=productSynchronizationDao.findPCById(pa.getCategoryId());
					
					//通过分类名称去分店找出分类ID
					//通过分店分类ID和属性名称去找属性ID
					//通过分店属性ID和属性值去找属性值ID
					//把属性值ID用-连起来
					//下面还要做图片操作
					ps.setAttributeCode(null);//属性值ID相连
				}
			}
			ps.setAttributeName(psList.get(i).getAttributeName());//属性值相连
			ps.setPrice(psList.get(i).getPrice());//价格
			ps.setMarketPrice(psList.get(i).getMarketPrice());//市场价
			ps.setBarCode(psList.get(i).getBarCode());//条码
			ps.setRemarks(psList.get(i).getRemarks());//商品简介
			ps.setShelves(psList.get(i).getShelves());//是否上架
			ps.setType(psList.get(i).getType());//是否有属性
			ps.setMemberPrice(psList.get(i).getMemberPrice());//会员价
			ps.setCostPrice(0D);//成本价
			ps.setFreezeNum(0);//冻结库存数量
			
			productSynchronizationDao.saveProductStock(ps);
		}
		
	}
}
