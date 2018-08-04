/**
 * createDate : 2016年6月14日上午10:31:20
 */
package cn.lastmiles.v2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.bean.ProductStorageRecord;
import cn.lastmiles.bean.ProductUnit;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.SpellHelper;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.BrandDao;
import cn.lastmiles.dao.ProductUnitDao;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.ProductStorageRecordService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.dao.CommodityCategoryDao;
import cn.lastmiles.v2.dao.CommodityManagerDao;

@Service
public class CommodityManagerService {
	/**
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommodityManagerService.class);

	@Autowired
	private CommodityManagerDao commodityManagerDao;
	@Autowired
	private CommodityCategoryDao commodityCategoryDao;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductUnitDao productUnitDao;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductStorageRecordService productStorageRecordService;
	
	public Page list(Long storeId, String name, String barCode, Long categoryId, Integer shelves, Long brandId, Page page) {
		return commodityManagerDao.list(storeId,name,barCode,categoryId,shelves,brandId,page);
	}
	
	@Transactional
	public void save(Product product, List<ProductStock> productStockList) {
		logger.debug("receive Product is {} ,productStockList is {}",product,productStockList);
		
		// 保存入库记录（主表）
		ProductStorageRecord psr = new ProductStorageRecord();
		Date date = new Date();
		Long PSRID = idService.getId();
		psr.setId(PSRID);
		psr.setStorageNumber("RK"+DateUtils.format(date, "yyyyMMddHHmmss")); //入库编号
		psr.setAccountId(product.getAccountId());
		psr.setStoreId(product.getStoreId());
		psr.setStorageTime(date);
		psr.setCreatedTime(date);
		psr.setMemo("新增商品");
		
		Long productID = 0L;
		if( null == product.getId()){
			productID = idService.getId();
			product.setId(productID);
		} else {
			productID = product.getId();
		}
		Long storeId = product.getStoreId();
		Long accountId = product.getAccountId();
		String imageID = product.getPicUrl();
		List<Long> productStockIDArray = null;
		boolean flag = false;
		if( StringUtils.isNotBlank(imageID) ){
			productStockIDArray = new ArrayList<Long>();
			flag = true;
		}
		List<Object[]> batchInsertArgs = new ArrayList<Object[]>();
		List<Object[]> batchValueArgs = new ArrayList<Object[]>();
		List<Object[]> batchInsertStorageRecordArgs = new ArrayList<Object[]>();
		String productName = product.getName();
		Map<String,Object> attribute1Map = new HashMap<String, Object>();
		Map<String,Object> attribute2Map = new HashMap<String, Object>();
		
		 Map<Long, Object> unitMap = new HashMap<Long, Object>();
		// 根据商家ID获取所有的单位信息
		List<ProductUnit> unitList = productUnitDao.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		if( null != unitList && unitList.size() > 0 ){
			for (ProductUnit productUnit : unitList) {
				unitMap.put(productUnit.getId(),productUnit.getName());
			}
			unitList = null;			
		}
		Integer type = 0; // 0表示有属性 1表示无属性
		for (ProductStock ps : productStockList) {
			Long productStockID = idService.getId();
			
			if(flag){				
				productStockIDArray.add(productStockID) ; // 保存库存ID用于图片的存储
			} 
			List<Long> linkCode = new ArrayList<Long>();
			
			StringBuilder valueName = new StringBuilder("");
			if( StringUtils.isNotBlank(ps.getValue_1()) | StringUtils.isNotBlank(ps.getValue_2()) ){
				// 有属性
				if(StringUtils.isNotBlank(ps.getValue_1())){
					if( valueName.length() > 0 ){
						valueName.append("|");
					}
					valueName.append(ps.getValue_1());
					
					Object attribute1 = attribute1Map.get(ps.getValue_1());
					if( null == attribute1 ){
						Object[] val = new Object[6];
						Long attributeValue1 = idService.getId();
						linkCode.add(attributeValue1);
						
						val[0] = attributeValue1;
						val[1] = productStockID;
						val[2] = product.getAttribute_1();
						val[3] = ps.getValue_1();
						val[4] = 1;
						val[5] = product.getId();
						batchValueArgs.add(val);
						attribute1Map.put(ps.getValue_1(), attributeValue1);
					} else {
						linkCode.add((Long) attribute1);
					}
				}
				if(StringUtils.isNotBlank(ps.getValue_2())){
					if( valueName.length() > 0 ){
						valueName.append("|");
					}
					valueName.append(ps.getValue_2());
					
					Object attribute2 = attribute2Map.get(ps.getValue_2());
					if( null == attribute2 ){
						Object[] val = new Object[6];
						Long attributeValue2 = idService.getId();
						linkCode.add(attributeValue2);						
						
						val[0] = attributeValue2;
						val[1] = productStockID;
						val[2] = product.getAttribute_2();
						val[3] = ps.getValue_2();
						val[4] = 2;
						val[5] = product.getId();
						batchValueArgs.add(val);
						attribute2Map.put(ps.getValue_2(), attributeValue2);
					} else {
						linkCode.add((Long) attribute2);
					}
				}
				type = 0;
			} else {
				// 无属性
				type = 1;
			}
			Object[] arg = new Object[25];
			arg[0] = productID; // productId
			arg[1] = ps.getCategoryId(); // categoryId
			arg[2] = accountId; // accountId
			arg[3] = storeId; // storeId
			arg[4] = ps.getSort(); // sort
			arg[5] = ps.getUnitId(); // unitId
			arg[6] = ps.getStock(); // stock
			arg[7] = ps.getAlarmValue(); // alarmValue
			arg[8] = ps.getBarCode(); // barCode
			arg[9] = ps.getRemarks(); // remarks
			arg[10] = ps.getDetails(); // details
			arg[11] = new Date(); // createTime
			arg[12] = accountId; // createId
			arg[13] = ps.getShelves(); // shelves
			arg[14] = type; // type
			arg[15] = ps.getPrice(); // price
			arg[16] = ps.getMemberPrice(); // memberPrice
			arg[17] = ps.getMarketPrice(); // marketPrice
			arg[18] = ps.getCostPrice(); // costPrice
			arg[19] = ps.getWeighing(); // weighing
			arg[20] = ps.getReturnGoods(); // returnGoods
			arg[21] = imageID;
			
			String attributeCode = "";
			if( linkCode.size() > 0 ){
				Collections.sort(linkCode);
				boolean sign = false;
				for (Long code : linkCode) {
					if(sign){
						attributeCode +="-";
					}
					attributeCode += code;
					sign = true;
				}
			} 
			arg[22] = attributeCode; // attributeCode
			
			String attributeName = productName;
			if( StringUtils.isNotBlank(valueName.toString()) ){
				attributeName += "|";
				attributeName += valueName.toString();
			}
			arg[23] = attributeName; // attributeName
			arg[24] = productStockID; // id
			batchInsertArgs.add(arg);
			
			// 不是无限库存时
			if( !ObjectUtils.equals(ps.getStock(), Constants.ProductStock.Store_Infinite) ){
				Object[] psrs = new Object[11]; 
				psrs[0] = productStockID; // stockId
				psrs[1] = productName; // productName
				psrs[2] = ps.getBarCode(); // barCode
				psrs[3] = valueName.toString(); // attributeValues
				psrs[4] = unitMap.get(ps.getUnitId()); // unitName
				psrs[5] = ps.getCostPrice(); // costPrice
				psrs[6] = ps.getStock(); // amount
				psrs[7] = ps.getStock(); // stock
				psrs[8] = ""; // supplierName(代理商)
				psrs[9] = "新增商品"; // memo
				psrs[10] = PSRID; // productStorageRecordId
				batchInsertStorageRecordArgs.add(psrs);
			}			
		}
		
		product.setType(type); // 给商品设置属性类型
		if(flag){
			if( productStockIDArray.size() > 0 ){
				ProductImage productImage=null;
				if( StringUtils.isNotBlank(imageID) ){ // 有图片上传，则保存图片
					for (Long objects : productStockIDArray) {
						productImage=new ProductImage();
						productImage.setId(imageID);
						productImage.setProductStockId(objects);
						productImage.setProductId(productID);
						productStockService.saveProductImageInfo(productImage);
					}				
				}
			}			
		}
		
		logger.debug("save Product is {} ,batchInsertArgs is {},batchValueArgs is {}",product,batchInsertArgs,batchValueArgs);
		if(commodityManagerDao.save(product)){ // 保存商品
			flag = commodityManagerDao.save(batchInsertArgs); // 保存库存
			logger.debug("batch save productStock flag is {}",flag);
			
			if( batchValueArgs.size() > 0 ){
				// 保存规格
				flag = commodityManagerDao.saveValue(batchValueArgs);
				logger.debug("batch save ProductStockAttributeValue flag is {}",flag);
			}
						
			if( batchInsertStorageRecordArgs.size() > 0 ){
				if(productStorageRecordService.save(psr)){// 保存主表
					logger.debug("保存入库库存为：{}",batchInsertStorageRecordArgs);
					productStorageRecordService.batchSave(batchInsertStorageRecordArgs);
				};  				
			}
		} else {
			new RuntimeException();
		};
	}

	public Product findById(Long id) {
		return commodityManagerDao.findById(id);
	}

	public List<ProductStock> findByProductId(Long id) {
		List<ProductStock> productStockList = commodityManagerDao.findByProductId(id); 
		return productStockList;
	}

	public List<ProductStockAttributeValue> findByProductStock(Long id) {
		return commodityManagerDao.findByProductStock(id);
	}

	@Transactional
	public void saveOrUpdate(Product product, List<ProductStock> productStockList) {
		logger.debug("product is {} , imageID is {}",product,product.getPicUrl());
		
		// 保存入库记录（主表）
		ProductStorageRecord psr = new ProductStorageRecord();
		Date date = new Date();
		Long PSRID = idService.getId();
		psr.setId(PSRID);
		psr.setStorageNumber("RK"+DateUtils.format(date, "yyyyMMddHHmmss")); //入库编号
		psr.setAccountId(product.getAccountId());
		psr.setStoreId(product.getStoreId());
		psr.setStorageTime(date);
		psr.setCreatedTime(date);
		psr.setMemo("新增商品");
				
		Long productID = 0L;
		if( null == product.getId()){
			productID = idService.getId();
			product.setId(productID);
		} else {
			productID = product.getId();
		}
		logger.debug("productID is {}",productID);
		Long storeId = product.getStoreId();
		Long accountId = product.getAccountId();
		String imageID = product.getPicUrl();
		List<Long> productStockIDArray = null;
		boolean flag = false;
		if( StringUtils.isNotBlank(imageID) ){
			productStockIDArray = new ArrayList<Long>();
			flag = true;
		}
		List<Object[]> batchInsertArgs = new ArrayList<Object[]>();
		List<Object[]> batchUpdateArgs = new ArrayList<Object[]>();
		
		List<Object[]> batchValueArgs = new ArrayList<Object[]>();
		List<Object[]> batchInsertStorageRecordArgs = new ArrayList<Object[]>();
		String productName = product.getName();
		Map<String,Object> attribute1Map = new HashMap<String, Object>();
		Map<String,Object> attribute2Map = new HashMap<String, Object>();
		
		 Map<Long, Object> unitMap = new HashMap<Long, Object>();
		// 根据商家ID获取所有的单位信息
		List<ProductUnit> unitList = productUnitDao.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		if( null != unitList && unitList.size() > 0 ){
			for (ProductUnit productUnit : unitList) {
				unitMap.put(productUnit.getId(),productUnit.getName());
			}
			unitList = null;			
		}
			
		Integer type = 0; // 0表示有属性 1表示无属性
		for (ProductStock ps : productStockList) {
			Long productStockID = null;
			boolean isUpdate = false;
			if( ObjectUtils.equals(-90000L, ps.getId())){
				logger.debug("新增的库存.....");
				productStockID = idService.getId();
			} else {
				productStockID = ps.getId();
				isUpdate = true;
			}
			logger.debug("库存ID是 ---》{}",productStockID);
			if(flag){				
				productStockIDArray.add(productStockID) ; // 保存库存ID用于图片的存储
			} 
			
			List<Long> linkCode = new ArrayList<Long>();
			StringBuilder valueName = new StringBuilder("");
			
			if( StringUtils.isNotBlank(ps.getValue_1()) | StringUtils.isNotBlank(ps.getValue_2()) ){
				// 有属性
				if(StringUtils.isNotBlank(ps.getValue_1())){
					if( valueName.length() > 0 ){
						valueName.append("|");
					}
					valueName.append(ps.getValue_1());
					
					Object attribute1 = attribute1Map.get(ps.getValue_1());
					if( null == attribute1 ){
						Object[] val = new Object[6];
						Long attributeValue1 = idService.getId();
						linkCode.add(attributeValue1);
						
						val[0] = attributeValue1;
						val[1] = productStockID;
						val[2] = product.getAttribute_1();
						val[3] = ps.getValue_1();
						val[4] = 1;
						val[5] = product.getId();
						batchValueArgs.add(val);
						attribute1Map.put(ps.getValue_1(), attributeValue1);
					} else {
						linkCode.add((Long) attribute1);
					}
				}
				if(StringUtils.isNotBlank(ps.getValue_2())){
					if( valueName.length() > 0 ){
						valueName.append("|");
					}
					valueName.append(ps.getValue_2());
					
					Object attribute2 = attribute2Map.get(ps.getValue_2());
					if( null == attribute2 ){
						Object[] val = new Object[6];
						Long attributeValue2 = idService.getId();
						linkCode.add(attributeValue2);						
						
						val[0] = attributeValue2;
						val[1] = productStockID;
						val[2] = product.getAttribute_2();
						val[3] = ps.getValue_2();
						val[4] = 2;
						val[5] = product.getId();
						batchValueArgs.add(val);
						attribute2Map.put(ps.getValue_2(), attributeValue2);
					} else {
						linkCode.add((Long) attribute2);
					}
				}
				type = 0;
			} else {
				// 无属性
				type = 1;
			}
			Object[] arg = new Object[25];
			arg[0] = productID; // productId
			arg[1] = ps.getCategoryId(); // categoryId
			arg[2] = accountId; // accountId
			arg[3] = storeId; // storeId
			arg[4] = ps.getSort(); // sort
			arg[5] = ps.getUnitId(); // unitId
			arg[6] = ps.getStock(); // stock
			arg[7] = ps.getAlarmValue(); // alarmValue
			arg[8] = ps.getBarCode(); // barCode
			arg[9] = ps.getRemarks(); // remarks
			arg[10] = ps.getDetails(); // details
			arg[11] = new Date(); // createTime
			arg[12] = accountId; // createId
			arg[13] = ps.getShelves(); // shelves
			arg[14] = type; // type
			arg[15] = ps.getPrice(); // price
			arg[16] = ps.getMemberPrice(); // memberPrice
			arg[17] = ps.getMarketPrice(); // marketPrice
			arg[18] = ps.getCostPrice(); // costPrice
			arg[19] = ps.getWeighing(); // weighing
			arg[20] = ps.getReturnGoods(); // returnGoods
			arg[21] = imageID;
			
			String attributeCode = "";
			if( linkCode.size() > 0 ){
				Collections.sort(linkCode);
				boolean sign = false;
				for (Long code : linkCode) {
					if(sign){
						attributeCode +="-";
					}
					attributeCode += code;
					sign = true;
				}
			} 
			arg[22] = attributeCode; // attributeCode
			
			String attributeName = productName;
			if( StringUtils.isNotBlank(valueName.toString()) ){
				attributeName += "|";
				attributeName += valueName.toString();
			}
			arg[23] = attributeName; // attributeName
			arg[24] = productStockID; // id
			
			if(isUpdate){
				logger.debug("保存进入修改队列");
				batchUpdateArgs.add(arg);
			} else {
				batchInsertArgs.add(arg);
				// 不是无限库存时
				if( !ObjectUtils.equals(ps.getStock(), Constants.ProductStock.Store_Infinite) ){
					Object[] psrs = new Object[11]; 
					psrs[0] = productStockID; // stockId
					psrs[1] = productName; // productName
					psrs[2] = ps.getBarCode(); // barCode
					psrs[3] = valueName.toString(); // attributeValues
					psrs[4] = unitMap.get(ps.getUnitId()); // unitName
					psrs[5] = ps.getCostPrice(); // costPrice
					psrs[6] = ps.getStock(); // amount
					psrs[7] = ps.getStock(); // stock
					psrs[8] = ""; // supplierName(代理商)
					psrs[9] = "新增库存"; // memo
					psrs[10] = PSRID; // productStorageRecordId
					batchInsertStorageRecordArgs.add(psrs);
				}
			}
		}
		product.setType(type); // 给商品设置属性
		productStockService.deleteByProductId(productID);
		if(flag){
			if( productStockIDArray.size() > 0 ){
				ProductImage productImage=null;
				if( StringUtils.isNotBlank(imageID) ){ // 有图片上传，则保存图片
					for (Long objects : productStockIDArray) {
						productImage=new ProductImage();
						productImage.setId(imageID);
						productImage.setProductStockId(objects);
						productImage.setProductId(productID);
						productStockService.saveProductImageInfo(productImage);
					}				
				}
			}			
		}
		
		if(commodityManagerDao.update(product)){ // 保存商品
			if( batchInsertArgs.size() > 0 ){
				flag = commodityManagerDao.save(batchInsertArgs); // 保存库存				
				logger.debug("batch save productStock flag is {}",flag);
			}
			if( batchUpdateArgs.size() > 0 ){
				flag = commodityManagerDao.update(batchUpdateArgs); // 修改库存				
				logger.debug("batch update productStock flag is {}",flag);
			}
			if( batchValueArgs.size() > 0 ){
				// 先删除
				commodityManagerDao.deleteValue(product.getId());
				// 保存规格
				flag = commodityManagerDao.saveValue(batchValueArgs);
				logger.debug("batch save ProductStockAttributeValue flag is {}",flag);
			}
			
			if( batchInsertStorageRecordArgs.size() > 0 ){
				if(productStorageRecordService.save(psr)){// 保存主表
					productStorageRecordService.batchSave(batchInsertStorageRecordArgs);
				};  				
			}
			
		} else {
			new RuntimeException();
		};
	}

	public int deleteProductStock(Long productStockId,Long productId) {
		//是否已经销售
		if (commodityManagerDao.checkSales(productId)){
			return 2;
		}
		// 删除图片库存
		productStockService.deleteByProductStockId(productStockId);
		// 删除商品规格
		commodityManagerDao.deleteByProductStockId(productStockId);
		// 删除库存
		productStockService.deleteById(productStockId);
		return 0;
	}
	
	public int deletProductStockByStockId(Long productStockId){
		// 删除图片库存
		productStockService.deleteByProductStockId(productStockId);
		// 删除商品规格
		commodityManagerDao.deleteByProductStockId(productStockId);
		// 删除库存
		productStockService.deleteById(productStockId);
		return 0;
	}

	@Transactional
	public boolean save(List<Object[]> batchInsertArgs){
		return commodityManagerDao.save(batchInsertArgs); 
	}
	
	public boolean updateProduct(Long categoryId, Long brandId, String productIdString) {
		return commodityManagerDao.updateProduct(categoryId,brandId,productIdString);
	}

	public int updateProductStock(Integer weighing, Integer returnGoods, Integer shelves, Long categoryId, String productIdString) {
		return commodityManagerDao.updateProductStock(weighing,returnGoods,shelves,categoryId,productIdString);
	}

	@Transactional
	public int deleteByProductId(Long productId) {
		//是否已经销售
		if (commodityManagerDao.checkSales(productId)){
			return 2;
		}
		
		List<ProductStock> productStockList = commodityManagerDao.findByProductId(productId);
		for (ProductStock productStock : productStockList) {
			deletProductStockByStockId(productStock.getId());
		}
		return commodityManagerDao.deleteProductByProductId(productId) > 0 ? 1 : 0;
	}

	public List<Product> getProductListByStoreId(Long storeId) {
		return commodityManagerDao.getProductListByStoreId(storeId);
	}

	public List<ProductStock> getProductStockListByStoreId(Long storeId) {
		return commodityManagerDao.getProductStockListByStoreId(storeId);
	}

	@Transactional
	public boolean batchProductSave(List<Object[]> productBatchInsertArr) {
		return commodityManagerDao.batchProductSave(productBatchInsertArr); 
	}
	
	public List<Product> reportList(String storeId, String name, String barCode, String categoryId, String shelves, String brandId) {
		return commodityManagerDao.reportList(storeId, name, barCode, categoryId, shelves, brandId);
	}
//----------------------------------------------批量导入的方法-------------------------------------------------------------
	/**
	 * 导入文件本身基本数据格式正确性校验
	 * @param lo
	 * @param totalNumber
	 * @param errors
	 * @return
	 */
	public Map<String, Object> excelFileSelfCheck(List<List<String>> lo, int totalNumber, StringBuilder errors) {
		
		// 根据商家ID获取所有的商品分类信息
		List<ProductCategory> categoryList = commodityCategoryDao.findByStoreId(SecurityUtils.getAccountStoreId()); 
		Map<String,Object> categoryMap = new HashMap<String, Object>();
		if( null != categoryList && categoryList.size() > 0 ){
			for (ProductCategory productCategory : categoryList) {
				categoryMap.put(productCategory.getName(), productCategory.getId());
			}
			categoryList = null;
		}
		
		ArrayList<String> productAttrValueList = new ArrayList<String>();
		
		Map<String,List<Object[]>> objMap = new HashMap<String, List<Object[]>>();
		// 前面两行是标头
		for (int i = 2; i < totalNumber; i++){
			List<String> row = lo.get(i);
			String productName = row.get(0); // 得到商品名称
			String barCode = row.get(1); // 商品条码
			String category = row.get(2); // 商品分类
			String value_1 = row.get(3); // 规格1
			String value_2 = row.get(4); // 规格2
			String price = row.get(6); // 销售价
			String costPrice = row.get(7); // 进货价
			String marketPrice = row.get(8); // 市场价
			String memberPrice = row.get(9); // 会员价
			String stock = row.get(10); // 库存数量
			String isLimit = row.get(11); //  是否无限库存
			String alarmValue = row.get(12); // 缺货提醒
			String sort = row.get(13); // 商品排序
			String weighing = row.get(15); // 是否称重
			String remarks = row.get(18); // 商品备注
			
			value_1 = org.apache.commons.lang.StringUtils.trimToEmpty(value_1);
			value_2 = org.apache.commons.lang.StringUtils.trimToEmpty(value_2);
			
			//商品名称，分类，规格连接，为了检查是否重复 商品
			productAttrValueList.add(productName + row.get(3) +  row.get(4));
			productName = org.apache.commons.lang.StringUtils.trim(productName);
			List<Object[]> judgeConditions = objMap.get(productName); 
			if( null == judgeConditions ){
				List<Object[]> objList = new ArrayList<Object[]>();
				Object[] o = new Object[3];
				o[0] = value_1;
				o[1] = value_2;
				o[2] = i+1;
				objList.add(o);
				objMap.put(productName, objList);
			} else {
				Object[] o = new Object[3];
				o[0] = value_1;
				o[1] = value_2;
				o[2] = i+1;
				judgeConditions.add(o);
				objMap.put(productName, judgeConditions);
			}
			// ********************* 不能为空判断
			if( StringUtils.isBlank(productName) ){
				errors.append("第"+(i+1)+"行商品名称不能为空<br/>"); // 商品名称
			} 
			if( StringUtils.isBlank(barCode) ){
				errors.append("第"+(i+1)+"行商品条码不能为空<br/>"); // 商品条码
			}
			if( StringUtils.isBlank(category) ){
				errors.append("第"+(i+1)+"行商品分类不能为空<br/>"); // 商品分类
			}
			if( StringUtils.isBlank(price) ){
				errors.append("第"+(i+1)+"行销售价不能为空<br/>"); // 销售价 
			}
			if( StringUtils.isBlank(costPrice) ){
				errors.append("第"+(i+1)+"行进货价不能为空<br/>"); // 进货价
			}
			if( StringUtils.isBlank(marketPrice) ){
				errors.append("第"+(i+1)+"行市场价不能为空<br/>"); // 市场价
			}
			
			// ********************* 长度判断
			if( StringUtils.isNotBlank(productName) && productName.length() > 48 ){
				errors.append("第"+(i+1)+"行商品名称长度过长,最长只能是48个字符<br/>"); // 商品名称
			}
			
			if ( StringUtils.isNotBlank(remarks) && remarks.length() > 150){ 	//商品备注
				errors.append("第"+(i+1)+"行商品备注长度过长,最长只能是150个字符<br/>");
			}
			
			// ******************** 格式判断
			if( StringUtils.isNotBlank(barCode) ){
				if(!StringUtils.isNumeric(barCode)){
					errors.append("第"+(i+1)+"行商品条码输入格式不正确,只能由正整数组成<br/>"); // 商品条码					
				}
				if( barCode.length() > 17 ){
					errors.append("第"+(i+1)+"行商品条码最长只支持17个字符<br/>"); // 商品条码
				}
			}
			
			if( StringUtils.isNotBlank(price) ){
				if(!StringUtils.isTwoPointFloat(price)){
					errors.append("第"+(i+1)+"行销售价输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 销售价					
				}
				if( -1 == price.lastIndexOf(".")){
					if(price.length() > 7 ){
						errors.append("第"+(i+1)+"行销售价最长只支持整数位为7位<br/>"); // 销售价
					}
				} else {
					if( StringUtil.splitc(price, ".")[1].length() > 1 ){
						if(price.length() > 10 ){
							errors.append("第"+(i+1)+"行销售价最长只支持整数位为7位<br/>"); // 销售价
						}
					} else {
						if(price.length() > 9 ){
							errors.append("第"+(i+1)+"行销售价最长只支持整数位为7位<br/>"); // 销售价
						}
					}
				}
			}
			
			if( StringUtils.isNotBlank(costPrice) ){
				if(!StringUtils.isTwoPointFloat(costPrice)){
					errors.append("第"+(i+1)+"行进货价输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 进货价					
				}
				if( -1 == costPrice.lastIndexOf(".")){
					if(costPrice.length() > 7 ){
						errors.append("第"+(i+1)+"行进货价最长只支持整数位为7位<br/>"); // 进货价
					}
				} else {
					if( StringUtil.splitc(costPrice, ".")[1].length() > 1 ){
						if(costPrice.length() > 10 ){
							errors.append("第"+(i+1)+"行进货价最长只支持整数位为7位<br/>"); // 进货价
						}
					} else {
						if(costPrice.length() > 9 ){
							errors.append("第"+(i+1)+"行进货价最长只支持整数位为7位<br/>"); // 进货价
						}
					}
				}
			}
			
			if( StringUtils.isNotBlank(marketPrice) ){
				if(!StringUtils.isTwoPointFloat(marketPrice)){
					errors.append("第"+(i+1)+"行市场价输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 市场价					
				}
				if( -1 == marketPrice.lastIndexOf(".")){
					if(marketPrice.length() > 7 ){
						errors.append("第"+(i+1)+"行市场价最长只支持整数位为7位<br/>"); // 市场价
					}
				} else {
					if( StringUtil.splitc(marketPrice, ".")[1].length() > 1 ){
						if(marketPrice.length() > 10 ){
							errors.append("第"+(i+1)+"行市场价最长只支持整数位为7位<br/>"); // 市场价
						}
					} else {
						if(marketPrice.length() > 9 ){
							errors.append("第"+(i+1)+"行市场价最长只支持整数位为7位<br/>"); // 市场价
						}
					}
				}
			}
			
			if( StringUtils.isNotBlank(memberPrice) ){
				if(!StringUtils.isTwoPointFloat(memberPrice)){
					errors.append("第"+(i+1)+"行会员价输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 会员价					
				}
				if( -1 == memberPrice.lastIndexOf(".")){
					if(memberPrice.length() > 7 ){
						errors.append("第"+(i+1)+"行会员价最长只支持整数位为7位<br/>"); // 会员价
					}
				} else {
					if( StringUtil.splitc(memberPrice, ".")[1].length() > 1 ){
						if(memberPrice.length() > 10 ){
							errors.append("第"+(i+1)+"行会员价最长只支持整数位为7位<br/>"); // 会员价
						}
					} else {
						if(memberPrice.length() > 9 ){
							errors.append("第"+(i+1)+"行会员价最长只支持整数位为7位<br/>"); // 会员价
						}
					}
				}
			}
			
			if( StringUtils.isNotBlank(stock) ){
				if( !StringUtils.equals("是", isLimit)){ // 不是无限库存
					if( ObjectUtils.equals("是", weighing) ){ //  可称重
						if(!StringUtils.isTwoPointFloat(stock)){
							errors.append("第"+(i+1)+"行库存数量输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 库存数量				
						}
						if( -1 == stock.lastIndexOf(".")){
							if(stock.length() > 7 ){
								errors.append("第"+(i+1)+"行库存数量最长只支持整数位为7位<br/>"); // 库存数量
							}
						} else {
							if( StringUtil.splitc(stock, ".")[1].length() > 1 ){
								if(stock.length() > 10 ){
									errors.append("第"+(i+1)+"行库存数量最长只支持整数位为7位<br/>"); // 库存数量
								}
							} else {
								if(stock.length() > 9 ){
									errors.append("第"+(i+1)+"行库存数量最长只支持整数位为7位<br/>"); // 库存数量
								}
							}
						}
					} else {
						if( -1 == stock.lastIndexOf(".")){
							if(!StringUtils.isNumeric(stock)){
								errors.append("第"+(i+1)+"行库存数量输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 缺货提醒					
							}
							
							if(stock.length() > 7 ){
								errors.append("第"+(i+1)+"行库存数量最大只支持7位数<br/>"); // 库存数量
							}	
						} else {
							if( StringUtil.splitc(stock, ".")[1].length() > 1 ){
								if(!StringUtils.isNumeric(stock)){
									errors.append("第"+(i+1)+"行库存数量输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 缺货提醒					
								}
								
								if(stock.length() > 7 ){
									errors.append("第"+(i+1)+"行库存数量最大只支持7位数<br/>"); // 库存数量
								}
							} else {
								String a = StringUtil.splitc(stock, ".")[1];
								if( !StringUtils.equals("0", a)){
									if(!StringUtils.isNumeric(stock)){
										errors.append("第"+(i+1)+"行库存数量输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 库存数量					
									}
									
									if(stock.length() > 7 ){
										errors.append("第"+(i+1)+"行库存数量最大只支持7位数<br/>"); // 库存数量
									}
								}
							}
						}										
					}
				}
			}
			if( StringUtils.isNotBlank(alarmValue) ){
				if( ObjectUtils.equals("是", weighing) ){ //  可称重
					if(!StringUtils.isTwoPointFloat(alarmValue)){
						errors.append("第"+(i+1)+"行缺货提醒输入格式不正确,只能由正整数或者有两位小数的数字组成<br/>"); // 缺货提醒				
					}
					if( -1 == alarmValue.lastIndexOf(".")){
						if(alarmValue.length() > 7 ){
							errors.append("第"+(i+1)+"行缺货提醒最长只支持整数位为7位<br/>"); // 缺货提醒
						}
					} else {
						if( StringUtil.splitc(alarmValue, ".")[1].length() > 1 ){
							if(alarmValue.length() > 10 ){
								errors.append("第"+(i+1)+"行缺货提醒最长只支持整数位为7位<br/>"); // 缺货提醒
							}
						} else {
							if(alarmValue.length() > 9 ){
								errors.append("第"+(i+1)+"行缺货提醒最长只支持整数位为7位<br/>"); // 缺货提醒
							}
						}
					}
				} else {
					if( -1 == alarmValue.lastIndexOf(".")){
						if(!StringUtils.isNumeric(alarmValue)){
							errors.append("第"+(i+1)+"行缺货提醒输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 缺货提醒					
						}
						
						if(alarmValue.length() > 7 ){
							errors.append("第"+(i+1)+"行缺货提醒最大只支持7位数<br/>"); // 库存数量
						}	
					} else {
						if( StringUtil.splitc(alarmValue, ".")[1].length() > 1 ){
							if(!StringUtils.isNumeric(alarmValue)){
								errors.append("第"+(i+1)+"行缺货提醒输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 缺货提醒					
							}
							
							if(alarmValue.length() > 7 ){
								errors.append("第"+(i+1)+"行缺货提醒最大只支持7位数<br/>"); // 库存数量
							}
						} else {
							String a = StringUtil.splitc(alarmValue, ".")[1];
							if( !StringUtils.equals("0", a)){
								if(!StringUtils.isNumeric(alarmValue)){
									errors.append("第"+(i+1)+"行缺货提醒输入格式不正确,不可称重情况下只能由正整数组成<br/>"); // 缺货提醒					
								}
								
								if(alarmValue.length() > 7 ){
									errors.append("第"+(i+1)+"行缺货提醒最大只支持7位数<br/>"); // 库存数量
								}
							}
						}
					}
				} 
			}
			if( StringUtils.isNotBlank(sort) && !StringUtils.isNumeric(sort) ){
				errors.append("第"+(i+1)+"行商品排序输入格式不正确,只能由正整数组成<br/>"); // 商品排序
			}
			
			// ********************* 存在判断
			if( StringUtils.isNotBlank(category) && null == categoryMap.get(category)){
				errors.append("第"+(i+1)+"行商品分类在原先的系统中不存在<br/>"); // 商品分类
			}
		}
		
		//规格重复判断
		for (int i = 0 ; i < productAttrValueList.size() - 1; i++){
			String val1 = productAttrValueList.get(i);
			for (int j = i + 1; j < productAttrValueList.size(); j++){
				String val2 = productAttrValueList.get(j);
				if (val1.equals(val2)){
					errors.append("第" +(i + 3) + "行" + "，第" + (j + 3) + "行商品重复<br/>");
				}
			}
		}
		
		// 规格必须都填写或者都不填写,且拥有空规格的商品只能有一种规格(下面是判断--->)
		for (Map.Entry<String,List<Object[]>> valueEntry : objMap.entrySet()) {
			List<Object[]> innerList = valueEntry.getValue();
			String productName = valueEntry.getKey();
			int noNull_1 = 0;
			int noNull_2 = 0;
			int count = innerList.size();
			List<Object> nullElement_1 = new ArrayList<Object>();
			List<Object> nullElement_2 = new ArrayList<Object>();
			for (int i = 0; i < count ; i++) {
				Object[] element = innerList.get(i);
				if( StringUtils.isBlank(element[0]+"")){
					nullElement_1.add(element[2]);
				} else {
					noNull_1++;					
				}
				
				if( StringUtils.isBlank(element[1]+"")){
					nullElement_2.add(element[2]);
				} else {
					noNull_2++;					
				}
			}
			
			if( count != noNull_1 && noNull_1 != 0 ){
				if(nullElement_1.size() > 0 ){
					for (Object object : nullElement_1) {
						errors.append("商品--->"+productName + " 的第" +(object) + "行的规格1必须进行统一,不能为空!<br/>");						
					}
				}
			}
			
			if( count != noNull_2 && noNull_2 != 0 ){
				if(nullElement_2.size() > 0 ){
					for (Object object : nullElement_2) {
						errors.append("商品--->"+productName + " 的第" +(object) + "行的规格2必须进行统一,不能为空!<br/>");						
					}
				}
			}
			
			logger.debug("商品：{},规格1不为空的条数是：{},规格2不为空的条数是：{}",productName,noNull_1,noNull_2);
			
			if( count > 1 && noNull_1 == 0 && noNull_2 == 0 ){
				errors.append("因为商品--->"+productName + " 是无规格的,不能拥有多种规格! <br/>");
			}
		}
		
		return categoryMap;
	}
		
	/**
	 * 单位信息和品牌信息数据库新增处理
	 * @param lo
	 * @param totalNumber
	 * @param unitMap
	 * @param brandMap
	 */
	public void saveUnitAndBrandInfo(List<List<String>> lo, int totalNumber, Map<String, Object> unitMap, Map<String, Object> brandMap) {
		// 根据商家ID获取所有的单位信息
		List<ProductUnit> unitList = productUnitDao.getProductUnitListByStoreID(SecurityUtils.getAccountStoreId());
		if( null != unitList && unitList.size() > 0 ){
			for (ProductUnit productUnit : unitList) {
				unitMap.put(productUnit.getName(), productUnit.getId());
			}
			unitList = null;			
		}
		
		// 根据商家ID获取所有的品牌信息
		List<Brand> brandList = brandDao.getBrandListByStoreId(SecurityUtils.getAccountStoreId());
		if( null != brandList && brandList.size() > 0 ){
			for (Brand brand : brandList) {
				brandMap.put(brand.getName(), brand.getId());
			}
			brandList = null;			
		}
		
		// 创建保存新增单位的集合
		List<Object[]> addUnit = new ArrayList<Object[]>();
		// 创建保存新增品牌的集合
		List<Object[]> addBrand = new ArrayList<Object[]>();
		
		// 前面两行是标头
		for (int i = 2; i < totalNumber; i++){
			List<String> row = lo.get(i);
			String unitName = row.get(5); // 单位
			String brandName = row.get(14); // 品牌
			
			// ********************* 格式判断
			if( StringUtils.isNotBlank(unitName) ){ // 新增的单位信息
				unitName = org.apache.commons.lang.StringUtils.trim(unitName);
				if( null == unitMap.get(unitName) ){
					Object[] ou = new Object[3];
					Long unitId = idService.getId(); 
					ou[0] = unitId;
					ou[1] = unitName;
					ou[2] = SecurityUtils.getAccountStoreId();
					addUnit.add(ou); // 进入待新增单位集合中
					unitMap.put(unitName, unitId);  // 将新增的单位信息填充进判断集合中
				}
			}
			
			if( StringUtils.isNotBlank(brandName) ){ // 新增的品牌信息
				brandName = org.apache.commons.lang.StringUtils.trim(brandName);
				if( null == brandMap.get(brandName) ){
					Object[] ob = new Object[3];
					Long brandId = idService.getId(); 
					ob[0] = brandId;
					ob[1] = brandName;
					ob[2] = SecurityUtils.getAccountStoreId();
					addBrand.add(ob); // 进入待新增品牌集合中
					brandMap.put(brandName, brandId); // 将新增的品牌信息填充进判断集合中
				}
			}
		}
		
		logger.debug("单位信息和品牌信息数据库新增处理完毕---结果：新增单位信息{}个,新增品牌信息{}个",addUnit.size(),addBrand.size());
		
		if( addUnit.size() > 0 ){
			// 批量新增单位信息
			productUnitDao.batchSave(addUnit);
		}
		if( addBrand.size() > 0 ){
			// 批量新增品牌信息
			brandDao.batchSave(addBrand);
		}
	}
	
	/**
	 * 文件库存信息内存存储处理
	 * @param serialMap
	 * @return
	 */
	@Transactional
	public List<Object[]> productStockAssemble(Map<String, Map<String, List<ProductStock>>> serialMap ) {
		List<Object[]> productStockBatchInsertArr = new ArrayList<Object[]>();
		
		List<Object[]> batchValueArgs = new ArrayList<Object[]>();
		Map<String,Object[]> firstCache = new HashMap<String, Object[]>();
		
		// 保存入库记录（主表）
		ProductStorageRecord psr = new ProductStorageRecord();
		Date date = new Date();
		Long PSRID = idService.getId();
		psr.setId(PSRID);
		psr.setStorageNumber("RK"+DateUtils.format(date, "yyyyMMddHHmmss")); //入库编号
		psr.setAccountId(SecurityUtils.getAccountId());
		psr.setStoreId( SecurityUtils.getAccountStoreId());
		psr.setStorageTime(date);
		psr.setCreatedTime(date);
		psr.setMemo("新增商品");
		List<Object[]> batchInsertStorageRecordArgs = new ArrayList<Object[]>();
		
		// 批量存储库存信息
		for (Map.Entry<String,Map<String,List<ProductStock>>> serialEntry : serialMap.entrySet()) {

			String productName = serialEntry.getKey();
			
			Map<String,Object> attribute1Map = new HashMap<String, Object>();
			Map<String,Object> attribute2Map = new HashMap<String, Object>();
			
			Map<String, List<ProductStock>> innerSmallMap = serialEntry.getValue();
			
			 for (Map.Entry<String , List<ProductStock>> smallEntry : innerSmallMap.entrySet()) {
				 if( null != smallEntry.getValue() ){
					 List<ProductStock> psList = (List<ProductStock>) smallEntry.getValue();
					 for (ProductStock ps : psList) {
						 Long productStockID = idService.getId();
						 
						 List<Long> linkCode = new ArrayList<Long>();
						 StringBuilder valueName = new StringBuilder("");
						 
						// 有属性
						 if(StringUtils.isNotBlank(ps.getValue_1())){
							 if( valueName.length() > 0 ){
								valueName.append("|");
							}
							valueName.append(ps.getValue_1());
							
							Object attribute1 = attribute1Map.get(ps.getValue_1());
							if( null == attribute1 ){
								 Object[] val = new Object[6];
								 Long attributeValue1 = idService.getId();
								 linkCode.add(attributeValue1);
								 
								 val[0] = attributeValue1;
								 val[1] = productStockID;
								 val[2] = 1; // 默认规定死了为规格,而数据库中也规定1代表规格
								 val[3] = ps.getValue_1();
								 val[4] = 1;
								 val[5] = ps.getProductId();
								 batchValueArgs.add(val);
								 attribute1Map.put(ps.getValue_1(), attributeValue1);
							} else {
								linkCode.add((Long) attribute1);
							}
						 }
						 if(StringUtils.isNotBlank(ps.getValue_2())){
							 if( valueName.length() > 0 ){
								valueName.append("|");
							}
							valueName.append(ps.getValue_2());
								
							Object attribute2 = attribute2Map.get(ps.getValue_2());
							if( null == attribute2 ){
								 Object[] val = new Object[6];
								 Long attributeValue2 = idService.getId();
								 linkCode.add(attributeValue2);
								
								 val[0] = attributeValue2;
								 val[1] = productStockID;
								 val[2] = 2; // 默认规定死了为颜色,而数据库中也规定2代表颜色
								 val[3] = ps.getValue_2();
								 val[4] = 2;
								 val[5] = ps.getProductId();
								 batchValueArgs.add(val);	
								 attribute2Map.put(ps.getValue_2(), attributeValue2);
							} else {
								linkCode.add((Long) attribute2);
							}
							 				
						 }
						 Long productId = ps.getProductId();
						 
						 // 用于《商品导入模版》中的----->>>【6.同种商品的商品排序、品牌、是否称重、上架管理、是否支持退换货、商品备注必须一致，如果不一致则以首次添加的商品信息导入。】
						 checkFirstParameterValue(firstCache, ps, productId);
						 Object[] arg = new Object[25];
					 	 arg[0] = productId; // productId
						 arg[1] = ps.getCategoryId(); // categoryId
						 arg[2] = ps.getAccountId(); // accountId
						 arg[3] = ps.getStoreId(); // storeId
						 arg[4] = ps.getSort(); // sort
						 arg[5] = ps.getUnitId(); // unitId
						 arg[6] = ps.getStock(); // stock
						 arg[7] = ps.getAlarmValue(); // alarmValue
						 arg[8] = ps.getBarCode(); // barCode
						 arg[9] = ps.getRemarks(); // remarks
						 arg[10] = ps.getDetails(); // details
						 arg[11] = new Date(); // createTime
						 arg[12] = ps.getCreateId(); // createId
						 arg[13] = ps.getShelves(); // shelves
						 arg[14] = ps.getType(); // type
						 arg[15] = ps.getPrice(); // price
						 arg[16] = ps.getMemberPrice(); // memberPrice
						 arg[17] = ps.getMarketPrice(); // marketPrice
						 arg[18] = ps.getCostPrice(); // costPrice
						 arg[19] = ps.getWeighing(); // weighing
						 arg[20] = ps.getReturnGoods(); // returnGoods
						 arg[21] = ps.getImageId();

						 String attributeCode = "";
						 if( linkCode.size() > 0 ){
							Collections.sort(linkCode);
							boolean sign = false;
							for (Long code : linkCode) {
								if(sign){
									attributeCode +="-";
								}
								attributeCode += code;
								sign = true;
							}
						 } 
						arg[22] = attributeCode; // attributeCode
						
						 String attributeName = productName;
						 if( StringUtils.isNotBlank(valueName.toString()) ){
							attributeName += "|";
							attributeName += valueName.toString();
						 }
						 arg[23] = attributeName; // attributeName
						 arg[24] = productStockID; // id
						 productStockBatchInsertArr.add(arg);
						 
						// 不是无限库存时
						if( !ObjectUtils.equals(ps.getStock(), Constants.ProductStock.Store_Infinite) ){
							 Object[] psrs = new Object[11]; 
							 psrs[0] = productStockID; // stockId
							 psrs[1] = productName; // productName
							 psrs[2] = ps.getBarCode(); // barCode
							 psrs[3] = valueName.toString(); // attributeValues
							 psrs[4] = ps.getUnitName(); // unitName
							 psrs[5] = ps.getCostPrice(); // costPrice
							 psrs[6] = ps.getStock(); // amount
							 psrs[7] = ps.getStock(); // stock
							 psrs[8] = ""; // supplierName(代理商)
							 psrs[9] = "新增库存"; // memo
							 psrs[10] = PSRID; // productStorageRecordId
							 batchInsertStorageRecordArgs.add(psrs);
						}
					 }
				 }
			 }
		}
		
		if( batchInsertStorageRecordArgs.size() > 0 ){
			logger.debug("批量导入时保存的入库记录是：{}",psr);
			if(productStorageRecordService.save(psr)){// 保存主表
				productStorageRecordService.batchSave(batchInsertStorageRecordArgs);
			};  				
		}
		
		if( batchValueArgs.size() > 0 ){
			// 保存规格
			boolean flag = commodityManagerDao.saveValue(batchValueArgs);
			logger.debug("productStockAssemble --->>>保存{}个规格, batch save ProductStockAttributeValue flag is {}",batchValueArgs.size(),flag);
		}
		
		return productStockBatchInsertArr;
	}

	public void checkFirstParameterValue(Map<String, Object[]> firstCache,ProductStock ps, Long productId) {
		Object[] object = firstCache.get(productId);
		 if( null == object ){
			 // 0商品排序、1是否称重、2上架管理、3是否支持退换货、4商品备注
			 Object[] o = new Object[5];
			 if( null != ps.getSort() ){
				 o[0] = ps.getSort();
			 } 
			 if( null != ps.getWeighing() ){
				 o[1] = ps.getWeighing();
			 }
			 if( null != ps.getShelves() ){
				 o[2] = ps.getShelves();
			 }
			 if( null != ps.getReturnGoods()){
				 o[3] = ps.getReturnGoods();
			 }
			 if( StringUtils.isNotBlank(ps.getRemarks()) ){
				 o[4] = ps.getRemarks();
			 }
		 } else {
			 if( null == object[0]){
				 object[0] = ps.getSort();
			 } else {
				 ps.setSort((Integer) object[0]);
			 }
			 if( null == object[1]){
				 object[1] = ps.getSort();
			 } else {
				 ps.setWeighing((Integer) object[1]);
			 }
			 if( null == object[2]){
				 object[2] = ps.getSort();
			 } else {
				 ps.setShelves((Integer) object[2]);
			 }
			 if( null == object[3]){
				 object[3] = ps.getSort();
			 } else {
				 ps.setReturnGoods((Integer) object[3]);
			 }
			 if( null == object[4]){
				 object[4] = ps.getSort();
			 } else {
				 ps.setRemarks(object[4]+"");
			 }
		 }
	}
	
	/**
	 * 文件库存信息校验
	 * @param errors
	 * @param serialMap
	 */
	public void productStockInfoCheck(StringBuilder errors, Map<String, Map<String, List<ProductStock>>> serialMap) {
		
		// 根据商家ID获取所有的商品库存信息
		List<ProductStock> productStockList = getProductStockListByStoreId(SecurityUtils.getAccountStoreId());
		Map<String,Object> productStockMap = new HashMap<String, Object>();
		
		if( null != productStockList && productStockList.size() > 0 ){
			for ( ProductStock productStock : productStockList) {
				productStockMap.put(productStock.getBarCode(),productStock.getId());
			}
			productStockList = null;
		}
				
		for (Map.Entry<String,Map<String,List<ProductStock>>> serialEntry : serialMap.entrySet()) {
			 Map<String, List<ProductStock>> innerSmallMap = serialEntry.getValue();
			 for (Map.Entry<String , List<ProductStock>> smallEntry : innerSmallMap.entrySet()) {
				 if( null != smallEntry.getValue() ){
					 List<ProductStock> psList = (List<ProductStock>) smallEntry.getValue();
					 for (ProductStock productStock : psList) {
						 for (Map.Entry<String,Map<String,List<ProductStock>>> serialEntry2 : serialMap.entrySet()) {
							 if( ObjectUtils.equals(serialEntry.getKey(),serialEntry2.getKey()) ){ // 同一商品就不对比商品条码了
								continue; 
							 }
							 Map<String, List<ProductStock>> innerSmallMap2 = serialEntry2.getValue();
							 for (Map.Entry<String , List<ProductStock>> smallEntry2 : innerSmallMap2.entrySet()) {
								 List<ProductStock> psList2 = (List<ProductStock>) smallEntry2.getValue();
								 for (ProductStock productStock2 : psList2) {
									
									if( ObjectUtils.equals(productStock.getBarCode(),productStock2.getBarCode() )){
										errors.append("第"+productStock.getIndex()+"行商品条码在"+serialEntry2.getKey()+"商品中已存在<br/>"); // 商品名称
									}
								}
							 }
						}
						 
						if( null != productStockMap.get(productStock.getBarCode()) ){
							errors.append("第"+productStock.getIndex()+"行商品条码在原先的系统中已存在<br/>"); // 商品条码
						}
					}
				 }
			 }
		}
	}
	
	/**
	 * 文件商品信息校验和内存存储处理
	 * @param lo
	 * @param totalNumber
	 * @param errors
	 * @param categoryMap
	 * @param unitMap
	 * @param brandMap
	 * @param productBatchInsertArr
	 * @return
	 */
	public Map<String, Map<String, List<ProductStock>>> productInfoCheck(List<List<String>> lo, int totalNumber, StringBuilder errors, Map<String, Object> categoryMap, Map<String, Object> unitMap, Map<String, Object> brandMap, List<Object[]> productBatchInsertArr) {
		
		// 因为涉及到提示语句是按EXCEl文件的行数顺序来进行提示,故使用LinkedHashMap来进行数据的存储
		Map<String, Map<String, List<ProductStock>>> serialMap = new LinkedHashMap<String, Map<String,List<ProductStock>>>();
		
		// 根据商家ID获取所有的商品信息
		List<Product> productList = getProductListByStoreId(SecurityUtils.getAccountStoreId());
		Map<String,Object> productMap = new HashMap<String, Object>();
		if( null != productList && productList.size() > 0 ){
			for ( Product product : productList) {
				productMap.put(product.getName(),product.getId());
			}
			productList = null;
		}
		
		Map<String,Object> productCacheMap = new HashMap<String, Object>();
		// 前面两行是标头
		for (int i = 2; i < totalNumber; i++){
			List<String> row = lo.get(i);
			String productName = row.get(0); // 得到商品名称
			String barCode = row.get(1); // 商品条码
			String category = row.get(2); // 商品分类
			String value_1 = row.get(3); // 规格一(规格)
			String value_2 = row.get(4); // 规格二(颜色)
			String unitName = row.get(5); // 单位
			Double price = ( StringUtils.isBlank(row.get(6)) ) ? 0 : Double.parseDouble(row.get(6)); // 销售价
			Double costPrice = ( StringUtils.isBlank(row.get(7)) ) ? 0 : Double.parseDouble(row.get(7)); // 进货价
			Double marketPrice = ( StringUtils.isBlank(row.get(8)) ) ? 0 : Double.parseDouble(row.get(8));; // 市场价
			Double memberPrice = ( StringUtils.isBlank(row.get(9)) ) ? null : Double.parseDouble(row.get(9));; // 会员价
			Double stock = ( StringUtils.isBlank(row.get(10)) ) ? 0 : Double.parseDouble(row.get(10)); // 库存数量
			String islimit = row.get(11); // 是否无限库存
			Double alarmValue = (StringUtils.isBlank(row.get(12)) ) ? 0 : Double.parseDouble(row.get(12)); // 缺货提醒
			Integer sort = ( StringUtils.isBlank(row.get(13)) ) ? 0 : Integer.parseInt(row.get(13)); // 商品排序
			String brandName = row.get(14); // 品牌
			String weighing = ( StringUtils.isBlank(row.get(15)) ) ? "否" : row.get(15); // 是否称重
			String shelves = ( StringUtils.isBlank(row.get(16)) ) ? "上架" : row.get(16); // 是否上架管理
			String returnGoods = ( StringUtils.isBlank(row.get(17)) ) ? "是" : row.get(17); // 是否支持退货
			String remarks = row.get(18); // 商品备注
			
			Object categoryId = categoryMap.get(category);
			Object brandId = brandMap.get(brandName);
			Object unitId = unitMap.get(unitName);
			// 数据重复性检验
			Map<String, List<ProductStock>> innerSmallMap = serialMap.get(productName);
			 if( null == innerSmallMap ){
				 // 将商品信息汇总并放入缓存待保存
				 Integer type = 1;
				 if( StringUtils.isNotBlank(value_1) | StringUtils.isNotBlank(value_2) ){
					 type = 0;
				 }
				 Long productId = saveProduct(productBatchInsertArr,productCacheMap, productName, categoryId, brandId,type);
				 // 组拼库存信息
				 if( null == productId){
					 productId = (Long) productCacheMap.get(productName);
				 }
				 ProductStock ps = saveProductStock(i, barCode, value_1, value_2, price, costPrice, marketPrice, memberPrice,
						stock, islimit, alarmValue, sort, weighing, shelves, returnGoods, remarks, categoryId, unitId, productId);
				 // 用于之后的入库管理的数据保存
				 ps.setUnitName(unitName);
				 
				// 将库存信息汇总并放入缓存待保存
				 Map<String, List<ProductStock>> smallMap = new TreeMap<String, List<ProductStock>>();
				 List<ProductStock> psList = new ArrayList<ProductStock>();
				 psList.add(ps);
				 smallMap.put(category, psList);
				 serialMap.put(productName, smallMap);
			 } else {
				 for (Map.Entry<String , List<ProductStock>> smallEntry : innerSmallMap.entrySet()) {
					 if( !ObjectUtils.equals(category, smallEntry.getKey()) ){
						 errors.append("第"+(i+1)+"行商品名称在"+ smallEntry.getKey() +"分类中已存在<br/>"); // 商品名称
					 } else {
						 // 组拼库存信息
						 Long productId = (Long) productCacheMap.get(productName);
						 ProductStock ps = saveProductStock(i, barCode, value_1, value_2, price, costPrice, marketPrice, memberPrice,
								stock, islimit, alarmValue, sort, weighing, shelves, returnGoods, remarks, categoryId, unitId, productId);
						 // 用于之后的入库管理的数据保存
						 ps.setUnitName(unitName);
						 
						 if( null != smallEntry.getValue()){
							 List<ProductStock> psList = (List<ProductStock>) smallEntry.getValue();
							 psList.add(ps);
							 innerSmallMap.put(category, psList);
							 serialMap.put(productName, innerSmallMap);							 
						 }
					 }
				 }
			 }
		
			if( null != productMap.get(productName) ){
				errors.append("第"+(i+1)+"行商品名称在原先的系统中已存在<br/>"); // 商品名称
			}
		}
					
		return serialMap;
	}
	
	public ProductStock saveProductStock(int i, String barCode, String value_1,
			String value_2, Double price, Double costPrice, Double marketPrice,
			Double memberPrice, Double stock, String islimit,
			Double alarmValue, Integer sort, String weighing, String shelves,
			String returnGoods, String remarks, Object categoryId,
			Object unitId, Long productId) {
		 ProductStock ps = new ProductStock();
		 
		 ps.setValue_1(value_1);
		 ps.setValue_2(value_2);
		 
		 ps.setProductId(productId);
		 ps.setCategoryId( (null == categoryId) ? null : (Long)categoryId );
		 ps.setAccountId(SecurityUtils.getAccountId());
		 ps.setStoreId(SecurityUtils.getAccountStoreId());
		 ps.setSort(sort);
		 ps.setUnitId( (null == unitId ) ? null : (Long)unitId );
		 if( StringUtils.isBlank(islimit) || !"是".equals(islimit) ){
			 islimit = "否";
		 }
		 if( "是".equals(islimit)){
			 ps.setStock(cn.lastmiles.constant.Constants.ProductStock.Store_Infinite.doubleValue());
		 } else {
			 ps.setStock(stock.doubleValue());					 
		 }
		 ps.setAlarmValue(alarmValue.doubleValue());
		 ps.setBarCode(barCode);
		 ps.setRemarks(remarks);
		 ps.setDetails("");
		 ps.setCreateId(SecurityUtils.getAccountId());
		 ps.setShelves( "下架".equals(shelves) ? cn.lastmiles.constant.Constants.ProductStock.ALL_DOWN : cn.lastmiles.constant.Constants.ProductStock.ALL_UP );
		 if( StringUtils.isNotBlank(value_1) | StringUtils.isNotBlank(value_2) ){
			 ps.setType(0);
		 } else {
			 ps.setType(1);
		 }
		 ps.setPrice(price);
		 ps.setMemberPrice(memberPrice);
		 ps.setMarketPrice(marketPrice);
		 ps.setCostPrice(costPrice);
		 ps.setWeighing( "是".equals(weighing) ? 1 : 0 );
		 ps.setReturnGoods( "否".equals(returnGoods) ? 0 : 1 );
		 ps.setImageId(null);
		 
		 ps.setIndex((i+1));
		return ps;
	}
	
	public Long saveProduct(List<Object[]> productBatchInsertArr, Map<String, Object> productCacheMap, String productName, Object categoryId, Object brandId,Integer type) {
		Long productId = idService.getId(); 
		Object[] product = new Object[8];
		product[0] = productName; // name
		product[1] = categoryId; // categoryId
		product[2] = SecurityUtils.getAccountId(); // accountId
		product[3] = SecurityUtils.getAccountStoreId(); // storeId
		product[4] = brandId; // brandId
		product[5] = SpellHelper.getPingYin(productName); // shortName
		product[6] = type;
		product[7] = productId;
		productBatchInsertArr.add(product);
		productCacheMap.put(productName, productId);
		return productId;
	}
//----------------------------------------------批量导入的方法-------------------------------------------------------------

	public int byProductNameFindProduct(String name,Long storeId, Long id) {
		return commodityManagerDao.byProductNameFindProduct(name,storeId,id);
	}

	public List<ProductStock> byBarCodeFindProductStock(Long storeId, String barCode, Long id) {
		return commodityManagerDao.byBarCodeFindProductStock(storeId,barCode,id);
	}

	public List<Product> findByNameAndStoreId(String name, Long storeId, Long id) {
		return commodityManagerDao.findByNameAndStoreId(name,storeId,id);
	}

	public int exitProductByCategoryId(Long id) {
		return commodityManagerDao.exitProductByCategoryId(id);
	}

	public List<ProductAttribute> getProductAttributeListByStoreID() {
		return commodityManagerDao.getProductAttributeListByStoreID();
	}

	public List<ProductStock> getProductStockListByProductId(Long productId) {
		return commodityManagerDao.getProductStockListByProductId(productId);
	}

	public List<ProductStockAttributeValue> findByAttributeCode(String attributeCode) {
		if( StringUtil.isBlank(attributeCode) ){
			return null;
		}
		String[] code = attributeCode.split("-");
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (String attribute : code) {
			if(flag){
				sb.append(",");
			}
			sb.append(attribute);
			flag = true;
		}
		logger.debug("获取的属性值ID是：{}",sb.toString());
		return commodityManagerDao.findByAttributeCode(sb.toString());
	}
		
}