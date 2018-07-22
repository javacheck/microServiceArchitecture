package cn.self.cloud.service;

import java.util.List;

import cn.self.cloud.bean.ProductCategory;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.ProductCategoryDao;
import cn.self.cloud.dao.ProductDao;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;

	@Autowired
	private IdService idService;

	/**
	 * 保存 调用底层保存方法
	 * 
	 * @param productCategory
	 */
	public void save(ProductCategory productCategory) {
		productCategory.setId(idService.getId());
		productCategory.setStoreId(SecurityUtils.getAccountStoreId());
		if (productCategory.getParentId() != null) {
			ProductCategory parent = productCategoryDao
					.findById(productCategory.getParentId());
			productCategory.setPath(parent.getPath() + "-"
					+ productCategory.getId());
		} else {
			productCategory.setPath(productCategory.getId().toString());
		}
		productCategoryDao.save(productCategory);
	}

	/**
	 * 
	 * @return 参看所有商品分类
	 */
	public Page findAll(String name, Page page) {
		return productCategoryDao.findAll(name, page);
	}

	/**
	 * 通过父类查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductCategory> findByParentId(Long parentId) {
		return productCategoryDao.findByParentId(parentId);
	}

	/**
	 * 通过父类查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductCategory> findByParentIdAndStoreId(Long parentId,
			Long storeId) {
		return productCategoryDao.findByParentIdAndStoreId(parentId, storeId);
	}

	/**
	 * 修改方法
	 * 
	 * @param productCategory
	 */
	public void updateByID(ProductCategory productCategory) {
		productCategoryDao.updateByID(productCategory);
	}

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public ProductCategory findById(Long id) {
		return productCategoryDao.findById(id);
	}

	/**
	 * 
	 * @param id
	 * @return 0成功，1表示还有产品，不能删除
	 */
	public int deleteById(Long id) {
		List<ProductCategory> list = productCategoryDao.findAllChildren(id);
		for (ProductCategory category : list) {
			Long count = productDao.countByCategory(category.getId());
			if (count.longValue() > 0L) {
				return 1;
			}
		}
		productCategoryDao.delete(id);
		return 0;
	}

	public ProductCategory findProductCategoryByName(String string,
			Long accountStoreId) {
		return productCategoryDao.findProductCategoryByName(string,
				accountStoreId);
	}
}
