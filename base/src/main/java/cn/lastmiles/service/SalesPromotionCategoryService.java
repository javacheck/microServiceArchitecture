package cn.lastmiles.service;
/**
 * createDate : 2015年10月12日 上午11:10:07 
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.SalesPromotionCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.SalesPromotionCategoryDao;

@Service
public class SalesPromotionCategoryService {
	@Autowired
	private SalesPromotionCategoryDao salesPromotionCategoryDao;
	@Autowired
	private ProductStockService productStockService;
	
	public Page list(Long storeId,String salesPromotionCategoryName,int salesPromotionCategoryType, Page page) {
		return salesPromotionCategoryDao.list(storeId,salesPromotionCategoryName,salesPromotionCategoryType,page);
	}

	public boolean save(SalesPromotionCategory salesPromotionCategory) {
		return salesPromotionCategoryDao.save(salesPromotionCategory);
	}

	public boolean delete(Long storeId, Long id) {
		return salesPromotionCategoryDao.delete(storeId,id);
	}
	
	public boolean again(Long storeId, Long id) {
		return salesPromotionCategoryDao.again(storeId,id);
	}

	public Page findProductStockList(String productName,Long storeId,Long productCategoryId,Page page) {
		page = salesPromotionCategoryDao.findProductStockList(productName,storeId,productCategoryId,page);

		@SuppressWarnings("unchecked")
		List<ProductStock> productStockList = (List<ProductStock>) page.getData();
		for (ProductStock productStock : productStockList) {
			productStock.setAttributeValuesListJointValue(productStockService.getAttributeValuesListJointValue(productStock.getAttributeCode(),"|"));
		}
		return page;
	}

	public SalesPromotionCategory findById(Long salesPromotionCategoryId) {
		return salesPromotionCategoryDao.findById(salesPromotionCategoryId);
	}

	public boolean update(SalesPromotionCategory salesPromotionCategory) {
		return salesPromotionCategoryDao.udpate(salesPromotionCategory);
	}
	
	public List<SalesPromotionCategory> getByStoreId(Long storeId){
		return salesPromotionCategoryDao.getByStoreId(storeId);
	}

	public void updateStatus(Long id, Integer status) {
		salesPromotionCategoryDao.updateStatus(id,status);
	}

	public boolean checkName(Long id ,Long storeId ,String name) {
		return salesPromotionCategoryDao.checkName(id,storeId,name);
	}
}