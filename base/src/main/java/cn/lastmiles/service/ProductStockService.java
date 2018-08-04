package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.ReportSales;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.ProductStockDao;
import cn.lastmiles.utils.FileServiceUtils;


@Service
public class ProductStockService {

	private final static Logger logger = LoggerFactory
			.getLogger(ProductStockService.class);
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private ProductStockDao productStockDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductAttributeValueService productAttributeValueService;

	@Autowired
	private IdService idService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private PromotionService promotionService;

	/**
	 * 
	 * @return 查看所有商品属性
	 */
	public Page findAll(String name, Page page) {
		return productStockDao.findAll(name, page);
	}
	@SuppressWarnings("unchecked")
	public Page findAll(String storeIdString, Long sysCategoryId, Long storeCategoryId,
			String name,Integer alarmValueType,Integer shelves,String barCode, Page page) {
		page=productStockDao.findAll(storeIdString, sysCategoryId, storeCategoryId,
				name, alarmValueType,shelves,barCode,page);
		List<ProductStock> psList= (List<ProductStock>) page.getData();
		if(psList!=null){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
				if(StringUtils.isNotBlank(psList.get(i).getAttributeCode())){
					String[] arr = psList.get(i).getAttributeCode().split("-");
					if(arr.length>0){
						for(int j=0;j<arr.length;j++){
							pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));													
						}
					}
				}
				
				psList.get(i).setPavList(pavList);
			}
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page findAll(Long storeId, String name, Page page) {
		page=productStockDao.findAll(storeId,name,page);
		List<ProductStock> psList= (List<ProductStock>) page.getData();
		if(psList!=null){
			String attributeValues="";
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
				if(StringUtils.isNotBlank(psList.get(i).getAttributeCode())){
					String[] arr = psList.get(i).getAttributeCode().split("-");
					if(arr.length>0){
						for(int j=0;j<arr.length;j++){
							pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));													
						}
					}
					String[] arrs =StringUtil.splitc(psList.get(i).getAttributeName(), '|');
					psList.get(i).setProductName(arrs[0]);
					for(int j=1;j<arrs.length;j++){
						attributeValues+=arrs[j]+"|";
					}
					psList.get(i).setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
					
				}else{
					psList.get(i).setProductName(psList.get(i).getAttributeName());
				}
				
				psList.get(i).setPavList(pavList);
			}
		}
		return page;
	}
	public Page findAll(String storeIds, Long categoryId, String name,
			Double min, Double max, Page page) {
		return productStockDao.findAll(storeIds, categoryId, name, min, max,
				page);
	}

	public ProductStock findProductStock(ProductStock productStock) {
		return productStockDao.findProductStock(productStock);
	}

	public ProductStock byAttributeFindProductStock(Product product,
			ProductStock productStock) {
		return productStockDao.byAttributeFindProductStock(product,
				productStock);
	}

	public void update(ProductStock productStock) {
		productStockDao.update(productStock);
	}

	public void save(ProductStock productStock) {
		if( null == productStock.getId() ){
			productStock.setId(idService.getId());
		}
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

	public List<ProductStock> findByProductIdAndHaveStock(Long productId) {
		return productStockDao.findByProductIdAndHaveStock(productId);
	}

	public List<Product> findByCategoryId(Long categoryId) {
		return productStockDao.findByCategoryId(categoryId);
	}

	public ProductStock findByStockId(Long id) {
		ProductStock productStock = productStockDao.findByStockId(id);
		
		if (productStock != null) {
			List<String> picUrlList = new ArrayList<String>();
			picUrlList.add(FileServiceUtils.getFileUrl(productStock.getImageId()));
			productStock.setPicUrlList(picUrlList);
			productStock.setProduct(productService.findById(productStock
					.getProductId()));
			String attributeValues="";
			if(StringUtils.isNotBlank(productStock.getAttributeName()) && productStock.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(productStock.getAttributeName(), '|');
				productStock.setProductName(arrs[0]);
				for(int i=1;i<arrs.length;i++){
					attributeValues+=arrs[i]+"|";
				}
				productStock.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				
			}else{
				productStock.setProductName(productStock.getAttributeName());
			}
		}
		return this.addPavList(productStock);
	}
	
	public ProductStock posFindByStockId(Long id) {
		ProductStock productStock = productStockDao.posFindByStockId(id);
		if (productStock == null){
			return null;
		}
		// 加入属性值处理
		productStock.setAttributeValuesListJointValue(getAttributeValuesListJointValue(productStock.getAttributeCode(),"|"));										
		
		return productStock;
	}

	public ProductStock addPavList(ProductStock productStock) {
		if (productStock != null) {
			List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
			if (StringUtils.isNotBlank(productStock.getAttributeCode())) {
				String[] attributeCodes = productStock.getAttributeCode()
						.split("-");
				for (String attributeCode : attributeCodes) {
					ProductAttributeValue productAttributeValue = productAttributeValueService
							.findById(Long.parseLong(attributeCode));
					if (productAttributeValue != null) {
						pavList.add(productAttributeValue);
					}
				}
			}
			productStock.setPavList(pavList);
		}
		return productStock;
	}

	public void saveProductImage(ProductImage productImage) {
		productStockDao.saveProductImage(productImage);

	}
	
	public void saveProductImageInfo(ProductImage productImage) {
		productStockDao.saveProductImageInfo(productImage);

	}

	public ProductImage findProductImage(ProductImage productImage) {
		return productStockDao.findProductImage(productImage);
	}

	public List<ProductImage> findImageByStockId(Long productStockId) {
		return productStockDao.findImageByStockId(productStockId);
	}

	public ProductStock findProductStock(String attributeCode) {
		ProductStock productStock = new ProductStock();
		productStock.setAttributeCode(attributeCode);
		return this.findProductStock(productStock);
	}

	public boolean updateStockNumber(Long id, Double StockNumber) {
		logger.debug("updateStockNumber ---> id is :{},StockNumber is :{}",id,StockNumber);
		return productStockDao.updateStockNumber(id, StockNumber);
	}

	/**
	 * 检验库存减少 是否合法
	 * 
	 * @param id
	 * @param decrease
	 * @return
	 */
	public boolean checkStock(Long id, Double decrease) {
		ProductStock productStock = this.findById(id);
		if (null != productStock && null != productStock.getStock()) {
			return productStock.getStock().equals(Constants.ProductStock.Store_Infinite)
					|| productStock.getStock() - decrease >= 0 ? true : false;
		}
		return false;
	}

	/**
	 * 库存减少
	 * 
	 * @param id
	 * @param decrease
	 * @return
	 */
	@Transactional
	public boolean decreaseStock(Long id, Double decrease) {
		logger.debug("decreaseStock is deal ---> id is :{},decrease is :{}",id,decrease);
		ProductStock productStock = this.findById(id);
		if (null != productStock && null != productStock.getStock()) {
			if (productStock.getStock().equals(Constants.ProductStock.Store_Infinite)) {// 是否为无限库存
				return true;
			}
			Double stock = NumberUtils.subtract(productStock.getStock(), decrease.doubleValue());
			logger.debug("product stock id = {},new stock = {},old stock = {} ",id,stock,productStock.getStock());
			if (stock >= 0) {
				return this.updateStockNumber(id, stock);
			}
		}
		return false;
	}

	/**
	 * 根据传递过来的参数 返回所有可能符合的库存信息
	 * 
	 * @param list
	 *            传递过来 AttributeCode 的队列
	 * @param productId
	 *            商品ID
	 * @param storeId
	 *            商店ID
	 * @return
	 */
	public String findByFuzzy(List<Long> list, Long productId, Long storeId) {
		List<String> papms = new ArrayList<String>();
		if (list == null || list.size() <= 0) {//
			return this.contentAttributeCode(productStockDao
					.findByFuzzyStockId(null, productId, storeId), null);
		}
		if (list.size() == 1) {// 只选中一个参数
			List<ProductAttributeValue> productAttributeValues = productAttributeValueService
					.findAllBrothersById(list.get(0));// 查找同一属性的所有属性值
			for (ProductAttributeValue productAttributeValue : productAttributeValues) {
				if (productStockDao.findByFuzzyAttributeCode(
						productAttributeValue.getId(), productId, storeId)
						.size() <= 0) {// 是否含有这个属性值的库存
					productAttributeValues.remove(productAttributeValue);
				}
			}
			this.contentAttributeCode(productStockDao.findByFuzzyAttributeCode(
					list.get(0), productId, storeId), productAttributeValues);
		}
		List<ProductStock> listProductStocks = new ArrayList<ProductStock>();
		for (int i = 0; i < list.size(); i++) {
			papms = new ArrayList<String>();
			StringBuffer sf = new StringBuffer();
			for (int j = 0; j < list.size(); j++) {
				if (i == j) {
				} else {
					Long l = list.get(j);
					sf.append("-" + l + "-%");
				}
			}// J循环结束
			papms.add("%" + sf.toString());
			papms.add(sf.substring(1));
			papms.add("%" + sf.substring(0, sf.length() - 2));
			if (list.size() >= 3) {// 如果超过三个参数多一个选择
				papms.add(sf.substring(1, sf.length() - 2));
			}
			logger.debug("papms is :" + papms.toString());
			listProductStocks.addAll(productStockDao.findByFuzzyStockId(papms,
					productId, storeId));
		}// I循环结束
		return this.contentAttributeCode(listProductStocks, null);
	}

	/**
	 * 根据传递过来的list 返回所有AttributeCode 拼接
	 * 
	 * @param list
	 * @return
	 */
	public String contentAttributeCode(List<ProductStock> list,
			List<ProductAttributeValue> productAttributeValues) {
		Set<String> sets = new HashSet<String>();
		if (list != null && list.size() > 0) {
			for (ProductStock productStock : list) {
				if (StringUtils.isNotBlank(productStock.getAttributeCode())) {
					String[] attributeCodes = productStock.getAttributeCode()
							.split("-");
					for (String attributeCode : attributeCodes) {
						sets.add(attributeCode);
					}
				}
			}
		}
		if (productAttributeValues != null && productAttributeValues.size() > 0) {
			for (ProductAttributeValue productAttributeValue : productAttributeValues) {
				sets.add(productAttributeValue.getId() + "");
			}
		}
		StringBuffer sf = new StringBuffer();
		for (String set : sets) {
			sf.append("-" + set);
		}
		return StringUtils.isNotBlank(sf.toString()) ? sf.deleteCharAt(0)
				.toString() : sf.toString();
	}

	public List<ProductStock> findByFuzzyAttributeCode(Long attributeCode,
			Long productId, Long storeId) {
		return productStockDao.findByFuzzyAttributeCode(attributeCode,
				productId, storeId);
	}

	public List<ProductStock> findByFuzzyAttributeCode(Long attributeCode,
			Long categoryId) {
		return productStockDao.findByFuzzyAttributeCode(attributeCode,
				categoryId);
	}

	public List<ProductStock> byBarCodeFindProductStock(String barCode,
			Long storeId) {
		return productStockDao.byBarCodeFindProductStock(barCode, storeId);
	}

	public ProductStock findByStoreId(Long id) {
		return productStockDao.findByStoreId(id);
	}

	public Product findProduct(Product product) {

		return productStockDao.findProduct(product);
	}

	public void editProduct(Product product) {
		if (product.getId() == null) {// 新增
			product.setId(idService.getId());// 商品ID

			productStockDao.save(product);
		} else {// 修改
			if(product.getType().intValue()==0){//有属性
				if(productStockDao.findTypeByid(product.getId()).getType().intValue()==1){
					productService.changeShelves(product.getId(),1);
				}
			}
			productStockDao.update(product);
		}

	}

	/**
	 * APP 查询商品
	 * 
	 * @param categoryId
	 * @param storeId
	 * @return
	 */
	public Page AppFindList(Page page, Long categoryId,Integer shelves, Long storeId) {
		page= productStockDao.AppFindList(page, categoryId, shelves,storeId);
		@SuppressWarnings("unchecked")
		List<ProductStock> productStocks = (List<ProductStock>) page.getData();
		if (productStocks!=null&&!productStocks.isEmpty()) {
			for (ProductStock productStock : productStocks) {
				if (productStock!=null&&productStock.getAttributeCode()!=null) {
//					List<ProductAttributeValue> productAttributeValues =productAttributeValueService.findByAttributeCode(productStock.getAttributeCode());
//					if (!productAttributeValues.isEmpty()) {
//						StringBuilder sb = new StringBuilder("(");
//						for (ProductAttributeValue productAttributeValue : productAttributeValues) {
//							sb.append(productAttributeValue.getValue());
//							sb.append(" ");
//						}
//						sb.setLength(sb.length() - 1);
//						sb.append(")");
//						productStock.setProductName(productStock.getProductName()+sb.toString());
//					}
					// 加入属性值处理
					String attributeName = getAttributeValuesListJointValue(productStock.getAttributeCode(),"-");
					if( null != attributeName && !StringUtils.equals(attributeName, "")){
						productStock.setProductName(productStock.getProductName() + "(" + attributeName + ")");
					}
					productStock.setAttributeValuesListJointValue(attributeName);
				}
			}
		}
		return page;
		
	}

	public List<ProductStock> byBarCodeFindProductStock1(String barCode,
			Long storeId, String name,Long categoryId) {
		return productStockDao.byBarCodeFindProductStock1(barCode, storeId,
				name,categoryId);
	}

	public List<ProductStock> findProductList(String name, Integer type,
			Long categoryId, Long storeId) {
		logger.debug("name={},type={},categoryId={},storeId={}",name,type,categoryId,storeId);
		List<ProductStock> list = productStockDao.findProductList(name, type,
				categoryId, storeId);
		logger.debug("list={}",list);
		String[] arr = {};
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();

				if (!"".equals(list.get(i).getAttributeCode())
						&& list.get(i).getAttributeCode() != null) {
					arr = list.get(i).getAttributeCode().split("-");
				}
				if (arr.length > 0) {
					for (int j = 0; j < arr.length; j++) {
						pavList.add(productAttributeValueService.findById1(Long
								.parseLong(arr[j])));
					}
				}

				list.get(i).setPavList(pavList);
			}
		}
		return list;
	}

	public ProductStock findByIdAndStoreId(Long stockId, Long storeId) {
		return productStockDao.findByIdAndStoreId(stockId, storeId);
	}

	public List<ProductStock> posFindByProductId(Long productId) {
		List<ProductStock> productStocks = productStockDao.posFindByProductId(productId);
		if (productStocks!=null) {
			for (ProductStock productStock : productStocks) {
				productStock.setImgUrlList(productImageService.findImgUrlByProductId(productId));//添加图片
				
				Promotion promotion =promotionService.appFindDiscount(productStock.getStoreId(),productStock.getId(),2);
				if (promotion!=null) {
					productStock.setPromotionPrice(NumberUtils.multiply(productStock.getPrice(), promotion.getDiscount(),0.1D));
					productStock.setPromotionNum(promotion.getTotal());
					productStock.setDiscount(promotion.getDiscount());
					productStock.setPromotionId(promotion.getId());
				}
				productStock.setImgUrlList(productImageService.findImgUrlByStockId(productStock.getId()));//添加图片
			}
		}
		return productStocks;
	}
	/**
	 * 处理销售数量
	 * @param id
	 * @param amount
	 */
	public void dealSales(Long id, Double amount) {
		productStockDao.dealSales(id, amount);
	}

	public List<ProductStock> posFindByBarCode(String barCode,Long storeId) {
		List<ProductStock> productStocks = productStockDao.posFindByBarCode(barCode,storeId);
		if (productStocks!=null) {
			for (ProductStock productStock : productStocks) {
				String imageId = productStock.getImageId();
				//if( StringUtils.isNotBlank(imageId) ){
					List<String> imgs = new ArrayList<String>();
					imgs.add(FileServiceUtils.getFileUrl(productStock.getImageId()));					
					productStock.setImgUrlList(imgs);//添加图片
				//}
				// 加入属性值处理
				productStock.setAttributeValuesListJointValue(getAttributeValuesListJointValue(productStock.getAttributeCode(),"-"));										
			}
		}
		return productStocks;
	}

	public ProductStock posFindById(Long productStockId, Long storeId) {
		return productStockDao.posFindById(productStockId,storeId);
		
	}

	public void posUpdate(Long id, Double costPrice, Double price,Integer stock, Integer shelves) {
		productStockDao.posUpdate(id,costPrice,price,stock,shelves);
	}
	
	public String getAttributeValuesListJointValue(String attributeCode,String separator) {
		if( null == attributeCode || "".equalsIgnoreCase(attributeCode.trim())){
			return null;
		}
		StringBuffer selectAttributeCode = new StringBuffer();
		if(-1 == attributeCode.indexOf("-")){
			selectAttributeCode.append(attributeCode);
		} else {
			selectAttributeCode.append(attributeCode.replaceAll("-", ","));
		}
		return productStockDao.getAttributeValuesListJointValue(selectAttributeCode.toString(), separator);
	}
	public void updateShelves(Long id, Integer shelves) {
		productStockDao.updateShelves(id,shelves);
	}
	public int checkProductStock(Long stockId,Integer stock) {
		return productStockDao.checkProductStock(stockId,stock);
	}

	public int getProductStock_stock(Long stockId) {
		return productStockDao.getProductStock_stock(stockId);
	}
	public Product findBrandByProduct(Product product) {
		
		return productStockDao.findBrandByProduct(product);
	}
	public List<Product> findProductByBrandId(Long id) {
		
		return productStockDao.findProductByBrandId(id);
	}
	public List<ProductStock> findAll(Long storeId, Long categoryId,
			String name, Integer alarmValueType, Integer shelves) {
		
		List<ProductStock> psList=productStockDao.findAll(storeId,categoryId,
				name, alarmValueType,shelves);
		
		if(psList!=null){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
				if(StringUtils.isNotBlank(psList.get(i).getAttributeCode())){
					String[] arr = psList.get(i).getAttributeCode().split("-");
					if(arr.length>0){
						for(int j=0;j<arr.length;j++){
							pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));													
						}
					}
				}
				
				psList.get(i).setPavList(pavList);
			}
		}
		return psList;
	}
	public List<ProductStock> getproductStockList(Integer alarmValueType,
			Integer shelves, Long storeId) {
		List<ProductStock> psList= productStockDao.getproductStockList(alarmValueType,shelves,storeId);
		if(psList!=null){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
				if(StringUtils.isNotBlank(psList.get(i).getAttributeCode())){
					String[] arr = psList.get(i).getAttributeCode().split("-");
					if(arr.length>0){
						for(int j=0;j<arr.length;j++){
							pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));													
						}
					}
				}
				
				psList.get(i).setPavList(pavList);
			}
		}
		return psList;
	}
	public List<ProductStock> findProductList1(Long id,String name, Integer type,
			Long categoryId, Long storeId) {
		
		List<ProductStock> psList= productStockDao.findProductList1(id,name,type,categoryId,storeId);
		if(!psList.isEmpty()){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList = new ArrayList<ProductAttributeValue>();
				if(StringUtils.isNotBlank(psList.get(i).getAttributeCode())){
					String[] arr = psList.get(i).getAttributeCode().split("-");
					if(arr.length>0){
						for(int j=0;j<arr.length;j++){
							pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));													
						}
					}
				}
				
				psList.get(i).setPavList(pavList);
			}
		}
		return psList;
	}
	public void delByImageId(String id) {
		productStockDao.delByImageId(id);
		
	}
	public List<ProductStock> appFindByProductId(Long productId) {
		List<ProductStock> productStocks = productStockDao.apiFindByProductId(productId);
		if (productStocks!=null) {
			for (ProductStock productStock : productStocks) {
				Promotion promotion =promotionService.appFindDiscount(productStock.getStoreId(),productStock.getId(),1);
				if (promotion!=null) {
					productStock.setPromotionPrice(NumberUtils.multiply(productStock.getPrice(), promotion.getDiscount(),0.1D));
					productStock.setPromotionNum(promotion.getTotal());
					productStock.setDiscount(promotion.getDiscount());
					productStock.setPromotionId(promotion.getId());
				}
				productStock.setImgUrlList(productImageService.findImgUrlByStockId(productStock.getId()));//添加图片
			}
		}
		return productStocks;
	}
	public void posUpdate(Long productStockId, String productName, String attributeValuesListJointValue) {
		productStockDao.posUpdate(productStockId,productName,attributeValuesListJointValue);
	}
	public ProductStock findByPromotionId(Long promotionId) {
		return productStockDao.findByPromotionId(promotionId);
	}
	public void batchDeleteByIds(List<Long> stockIds) {
		productStockDao.batchDeleteByIds(stockIds);
		
	}
	public void batchUpdateAttributeName(List<Object[]> batchArgs) {
		productStockDao.batchUpdateAttributeName(batchArgs);
	}
	
	/**
	 * 商家版查询商品管理列表
	 * @param storeId 商家ID(null则查询所有)
	 * @param brandId 品牌ID(null则查询所有)
	 * @param sort 排序规则(0:智能排序;1:销量最高;2:最新商品;3:最新入库)
	 * @param screening 筛选条件(0:全部商品;1:售卖中;2:未上架;3:库存预警)
	 * @param productName 关键字查询(商品名称模糊)
	 * @param Page page 分页对象
	 * @return Page或者null
	 */
	public Page apiShopGetProductManagerList(Long storeId, Long brandId, int sort, int screening, String productName,Page page) {
		return productStockDao.apiShopGetProductManagerList(storeId,brandId,sort,screening,productName,page);
	}
	
	/**
	 * 查询商家的库存预警
	 * @param storeId 商家ID(null查询整个平台)
	 * @return 库存预警数
	 */
	public int apiShopGetAlarmValueCount(Long storeId) {
		return productStockDao.apiShopGetAlarmValueCount(storeId);
	}
	
	public int byBarCodeFindProductStock(Long storeId, String barCode, String id) {
		return productStockDao.byBarCodeFindProductStock(storeId,barCode,id);
	}
	public boolean apiShopPutStorageUpdate(Long id, Integer sumStock, Double costPrice) {
		return productStockDao.apiShopPutStorageUpdate(id,sumStock,costPrice);
	}
	public ProductStock findProductStockSum(String storeIdString) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> storeIds=new ArrayList<Long>();
		if(StringUtils.isNotBlank(storeIdString)){
			String[] arrStoreId=storeIdString.split(",");
			for(int i=0;i<arrStoreId.length;i++){
				storeIds.add(Long.parseLong(arrStoreId[i]));
			}
			map.put("storeId", storeIds);
		}else{
			map.put("storeId", null);
		}
		
		return JdbcUtils.queryForObject("operationSummary.findProductStockSum", map, ProductStock.class);
	}
	public boolean updateStockNumberAtReturnGoods(Long stockId, Double StockNumber) {
		logger.debug("updateStockNumberAtReturnGoods ---> stockId is :{},StockNumber is :{}",stockId,StockNumber);
		return productStockDao.updateStockNumberAtReturnGoods(stockId, StockNumber);	
	}
	public List<ProductStock> PosGetProductStockList(Long categoryId,
			String productName, String barCode) {
		
		return productStockDao.PosGetProductStockList(categoryId,productName,barCode);
	}
	public void deleteByProductId(Long productID) {
		productStockDao.deleteByProductId(productID);
	}
	public void deleteByProductStockId(Long productStockId) {
		productStockDao.deleteByProductStockId(productStockId);
	}
	public ProductStock posFindByStockId1(Long parseLong) {
		
		return productStockDao.posFindByStockId(parseLong);
	}
}