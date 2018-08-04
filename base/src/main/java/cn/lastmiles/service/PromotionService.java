package cn.lastmiles.service;

/**
 * createDate : 2015年11月2日下午4:29:57
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.PromotionProduct;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.JsonUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PromotionDao;
import cn.lastmiles.dao.PromotionStock;

@Service
public class PromotionService {

	@Autowired
	private PromotionDao promotionDao;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductService productService;
	@Autowired
	private IdService idservice;
	@Autowired
	private ProductImageService productImageService;

	public Page list(Long storeId, String promotionName, int promotionType,
			int promotionStatus, String startDate, String endDate, Page page) {
		return promotionDao.list(storeId, promotionName, promotionType,
				promotionStatus, startDate, endDate, page);
	}

	public boolean checkName(Long id, Long storeId, String name) {
		return promotionDao.checkName(id, storeId, name);
	}

	public Page findProductStockList(String productName, Long storeId,
			Long productCategoryId, Page page) {
		page = promotionDao.findProductStockList(productName, storeId,
				productCategoryId, page);

		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) page
				.getData();
		for (ProductStock productStock : productStockList) {
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
		return page;
	}

	public boolean save(Promotion promotion, int promotion_type) {
		boolean flag = false;
		if (promotion_type == Constants.Promotion.TYPE_FULL_SUBTRACT) {
			// 用TreeMap排序，默认是根据key来升序排列
			Map<Long, String> hm = new TreeMap<Long, String>();
			Long[] key = promotion.getFullSubtractKey();
			String[] value = promotion.getFullSubtractValue();
			if (key.length == value.length) { // key和value匹配才放入map对象
				for (int i = 0; i < key.length; i++) {
					// 不存储空数据
					if ("".equals(key[i]) || "".equals(value[i])) {
						continue;
					}
					hm.put(key[i], value[i]);
				}
			}
			promotion.setCondition(JsonUtils.objectToJson(hm));
		}

		if (promotion_type == Constants.Promotion.TYPE_DISCOUNT) {
			promotion.setCondition((null == promotion.getCondition() || ""
					.equals(promotion.getCondition())) ? "-1" : promotion
					.getCondition());
		}

		String[] promotionProductCache = promotion.getPromotionProductCache();
		Long promotionId = null;
		if (null == promotion.getId()) {
			promotionId = idservice.getId();
		} else {
			promotionId = promotion.getId();
		}
		List<Object[]> batchArgs = new ArrayList<Object[]>();

		if (null != promotionProductCache && promotionProductCache.length > 0) {
			for (String stockID : promotionProductCache) {
				Object[] arg = new Object[2];
				arg[0] = promotionId;
				arg[1] = stockID;
				batchArgs.add(arg);
			}
		} else { // 折扣(未选择任何商品则认为所有商品都进入折扣活动,传入标识-1)
			if (promotion_type == Constants.Promotion.TYPE_DISCOUNT) { // 折扣
				Object[] arg = new Object[2];
				arg[0] = promotionId;
				arg[1] = -1;
				batchArgs.add(arg);
			}
		}

		// 先暂时不管什么类型，遇到促销数量为空时，均设置对象中的促销数量为无线
		promotion.setTotal((null == promotion.getTotal()) ? -1 : promotion
				.getTotal());

		if (null == promotion.getId()) {
			promotion.setId(promotionId);
			promotion.setType(promotion_type);
			flag = promotionDao.save(promotion, promotion_type);
		} else {
			flag = promotionDao.update(promotion, promotion_type);
		}
		if (promotion_type == Constants.Promotion.TYPE_DISCOUNT
				|| promotion_type == Constants.Promotion.TYPE_COMBINATION) { // 折扣
			promotionDao.saveBatch(batchArgs, promotionId);
		}
		if (promotion_type == Constants.Promotion.TYPE_COMBINATION) { // 组合
			ProductStock temp = productStockService.findByPromotionId(promotion
					.getId());
			if (null != temp) {
				// 删除商品，删除库存，删除图片
				productService.deleteById(temp.getProductId());
				productStockService.deleteById(temp.getId());
				productImageService.deleteByProductStockID(temp.getId());
			}
			;
			Long productId = insertProduct(promotion);
			Long productStockId = idservice.getId();
			insertProductStock(promotion, promotionId, productId,
					productStockId);
			insertProductImage(promotion, productStockId);
		}
		return flag;
	}

	private void insertProductImage(Promotion promotion, Long productStockId) {
		ProductImage productImage = new ProductImage();
		productImage.setId(promotion.getImageId());
		productImage.setProductStockId(productStockId);
		productImage.setSort(0);
		productImage.setSuffix(promotion.getSuffix());
		productStockService.saveProductImage(productImage);
	}

	private void insertProductStock(Promotion promotion, Long promotionId,
			Long productId, Long productStockId) {
		ProductStock productStock = new ProductStock();
		String productName = productService.findById(productId).getName();// 商品名称
		productStock.setId(productStockId);
		Double stock = (null == promotion.getTotal() || -1 == promotion
				.getTotal()) ? Constants.ProductStock.Store_Infinite
				: promotion.getTotal();
		productStock.setStock(stock.doubleValue());
		productStock.setAlarmValue(null);
		productStock.setProductId(productId);
		productStock.setAccountId(promotion.getAccountId());
		productStock.setStoreId(promotion.getStoreId());
		productStock.setAttributeCode(null);
		productStock.setPrice(promotion.getPrice());
		productStock.setBarCode(promotion.getBarCode());
		productStock.setRemarks(null);
		productStock.setSales(0L);
		productStock.setHits(0L);
		productStock.setRecommended(0);
		productStock.setShelves(Constants.ProductStock.ALL_UP);
		productStock.setType(Constants.ProductStock.TYPE_OFF);
		productStock.setMarketPrice(0D);
		productStock.setCostPrice(promotion.getCostPrice());
		productStock.setDetails(null);
		productStock.setSort(0);
		productStock.setAttributeName(productName);
		productStock.setPromotionId(promotionId);
		productStockService.save(productStock);
	}

	private Long insertProduct(Promotion promotion) {
		Product product = new Product();
		Long productId = idservice.getId();
		product.setId(productId);
		product.setName(promotion.getName()); // 活动名称就是商品名称
		product.setCategoryId(promotion.getCategoryId());
		product.setAccountId(promotion.getAccountId());
		product.setStoreId(promotion.getStoreId());
		product.setType(Constants.ProductStock.TYPE_OFF);
		product.setBrandId(null);
		productService.save(product);
		return productId;
	}

	public int delete(Long id, Long storeId) {
		return promotionDao.delete(id, storeId);
	}

	public Promotion findById(Long id) {
		return promotionDao.findById(id);
	}

	public List<PromotionProduct> findProductById(Long id) {
		return promotionDao.findProductById(id);
	}

	public List<ProductStock> findProductStockList(Long storeId) {
		List<ProductStock> productStockList = promotionDao
				.findProductStockList(storeId);
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService
					.getAttributeValuesListJointValue(
							productStock.getAttributeCode(), "|"));
		}
		return productStockList;
	}

	public List<ProductStock> findProductStockList(Long storeId, String stockIdS) {
		List<ProductStock> productStockList = promotionDao
				.findProductStockList(storeId, stockIdS);
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService
					.getAttributeValuesListJointValue(
							productStock.getAttributeCode(), "|"));
		}
		return productStockList;
	}

	public List<Promotion> findByStoreId(Long storeId) {
		return promotionDao.findByStoreId(storeId);
	}

	public Promotion appFindDiscount(Long storeId, Long productStockId,
			Integer scope) {
		return promotionDao.appFindDiscount(storeId, productStockId, scope);

	}

	public List<Promotion> checkTimeInterleaving(Long accountStoreId, Long id,
			int promotion_type, String startTime, String endTime) {
		return promotionDao.checkTimeInterleaving(accountStoreId, id,
				promotion_type, startTime, endTime);
	}

	public List<PromotionProduct> getPromotionProduct(String linkString) {
		if (null == linkString) {
			return null;
		}
		return promotionDao.getPromotionProduct(linkString);
	}

	public boolean checkFirstOrFull(Long accountStoreId, Long id,
			int promotion_type) {
		return promotionDao
				.checkFirstOrFull(accountStoreId, id, promotion_type);
	}

	public void lossStock(Long id, Integer amount) {
		promotionDao.lossStock(id, amount);
	}

	public Page findCombinationProductStockList(String productName,
			Long storeId, Long productCategoryId, Page page) {
		return promotionDao.findCombinationProductStockList(productName,
				storeId, productCategoryId, page);
	}

	/**
	 * pos端的折扣信息
	 * 
	 * @param storeId
	 * @param stockIds
	 * @return null表示无折扣
	 */
	public PromotionStock findDiscountProductStockForPos(Long storeId,
			List<Long> stockIds) {
		return promotionDao.findDiscountProductStockForPos(storeId, stockIds);
	}
	
	/**
	 * 满减的优惠
	 * @param storeId
	 * @return
	 */
	public Promotion findFullSubstractForPos(Long storeId){
		return promotionDao.findFullSubstractForPos(storeId);
	}
	
	
}