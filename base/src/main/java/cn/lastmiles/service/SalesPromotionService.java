package cn.lastmiles.service;
/**
 * createDate : 2015年8月19日 下午3:15:35 
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.SalesPromotion;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.SalesPromotionDao;

@Service
public class SalesPromotionService {

	@Autowired
	private SalesPromotionDao salesPromotionDao;
	@Autowired
	private ProductStockService productStockService;
	
	public Page list(Long storeId,Long salesPromotionCategoryId,Long productCategoryId,String productName, Page page) {
		page = salesPromotionDao.list(storeId,salesPromotionCategoryId,productCategoryId,productName, page);
		@SuppressWarnings("unchecked")
		List<SalesPromotion> salesPromotionList = (List<SalesPromotion>) page.getData();
		for (SalesPromotion salesPromotion : salesPromotionList) {
			salesPromotion.setAttributeName(productStockService.getAttributeValuesListJointValue(salesPromotion.getAttributeCode(),"|"));
		}
		return page;
	}
	
	public List<SalesPromotion> findBySalesPromotionCategoryId(Long salesPromotionCategoryId) {
		return salesPromotionDao.findBySalesPromotionCategoryId(salesPromotionCategoryId);
	}
		
	public boolean delete(Long id,Long stockId,int shelves) {
		boolean falg = salesPromotionDao.delete(id);
		if(falg){
			productStockService.updateShelves(stockId, shelves);
		}
		return falg;
	}

	public Boolean save(List<Object[]> batchArgs) {
		return salesPromotionDao.save(batchArgs);
	}
	
	/**
	 * 是否有促销
	 * @param storeId
	 * @return
	 */
	public boolean havePromotion(Long storeId){
		return salesPromotionDao.findAmountByStoreId(storeId).intValue() > 0;
	}

	public boolean updatePrice(Long id, String price) {
		return salesPromotionDao.updatePrice(id,price);
	}
	public SalesPromotion getByStockId(Long stockId,Long categoryId){
		return salesPromotionDao.getByStockId(stockId,categoryId);
	}
	public void updateSalesNum(int num,Long stockId,Long categoryId){
		salesPromotionDao.updateSalesNum(num, stockId,categoryId);
	}

	public boolean updateSalesNum(Long id, String salesNum) {
		if(StringUtils.isBlank(salesNum)){
			salesNum = "-1";
		}
		return salesPromotionDao.updateSalesNum(id,salesNum);
	}
}