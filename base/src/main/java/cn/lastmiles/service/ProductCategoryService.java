package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.TreeJsonBean;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ProductCategoryDao;
import cn.lastmiles.dao.ProductDao;

@Service
public class ProductCategoryService {
	private final static Logger logger = LoggerFactory
			.getLogger(ProductCategoryService.class);
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;

	@Autowired
	private IdService idService;

	@Autowired
	private ProductAttributeService productAttributeService;

	/**
	 * 检测名称
	 * 
	 * @param name
	 * @param type
	 * @return true表示存在
	 */
	public boolean checkName(String name, Long id, Integer type, Long storeId) {
		return productCategoryDao.checkName(name, id, type, storeId);
	}
	
	/**
	 * 保存 调用底层保存方法
	 * 
	 * @param productCategory
	 */
	@Transactional
	public Long save(ProductCategory productCategory) {
		if (productCategory.getId()==null||productCategory.getId().intValue()==0) {
			productCategory.setId(idService.getId());
		}
		if (productCategory.getpId() != null) {
			ProductCategory parent = productCategoryDao
					.findById(productCategory.getpId());
			productCategory.setPath(parent.getPath() + "-"
					+ productCategory.getId());
		} else {
			productCategory.setPath(productCategory.getId().toString());
		}
		this.updateSort(productCategory.getpId(), productCategory.getStoreId(), productCategory.getSort(), null);
		productCategoryDao.save(productCategory);
		return productCategory.getId();
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
	public List<ProductCategory> findByParentId(Long parentId, String storeIds) {
		return productCategoryDao.findBypId(parentId, storeIds);
	}

	/**
	 * 通过父类查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ProductCategory> findByParentIdAndStoreId(Long parentId,
			Long storeId) {
		return productCategoryDao.findBypIdAndStoreId(parentId, storeId);
	}

	public List<ProductCategory> findByParentIdAndStoreId(Long parentId,
			String storeIds) {
		return productCategoryDao.findBypIdAndStoreId(parentId, storeIds);
	}

	/**
	 * 修改方法
	 * 
	 * @param productCategory
	 */
	@Transactional
	public Long updateById(ProductCategory productCategory) {
		updateSort(productCategory.getpId(),productCategory.getStoreId(),productCategory.getSort(),this.findById(productCategory.getId()).getSort());
		productCategoryDao.updateById(productCategory);
		return productCategory.getId();
	}
	
	/**
	 * 修改比当前排序大的值
	 * @param storeId 
	 */
	public void updateSort(Long pid, Long storeId, Integer newSort,Integer oldSort) {
		logger.debug("pid is {},storeId is {},newSort is {},oldSort is {}",pid,storeId,newSort,oldSort);
		if (oldSort==null) {//添加
			productCategoryDao.updateSort(pid,storeId,newSort,oldSort,'+');
			return ;
		}
		if (newSort.intValue()==oldSort.intValue()) {
			return ;
		}
		if (oldSort==null||oldSort.intValue()>newSort.intValue()) {
			productCategoryDao.updateSort(pid,storeId,newSort,oldSort,'+');
		}else{
			productCategoryDao.updateSort(pid,storeId,oldSort,newSort,'-');
		}
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
		productAttributeService.deleteByCategoryId(id);// 删除分类属性
		productCategoryDao.delete(id);
		return 0;
	}

	public ProductCategory findProductCategoryByName(String string,
			Long accountStoreId) {
		return productCategoryDao.findProductCategoryByName(string,
				accountStoreId);
	}

	public List<ProductCategory> findByStoreId(Long storeId) {
		return productCategoryDao.findByStoreId(storeId);
	}

	/**
	 * 根据商品分类ID删除商品分类信息
	 * 
	 * @param categoryIDS
	 *            商品分类ID或商品分类集合(格式： ID1,ID2,ID3,ID4....)
	 * @return true 删除成功
	 */
	public Boolean deleteById(Long storeId,String categoryIDS) {
		String[] categoryID = categoryIDS.split(",");
		return productCategoryDao.deleteById(storeId,categoryID);
	}

	/**
	 * 根据商家ID获取商品分类信息
	 * 
	 * @param storeId
	 *            商家ID
	 * @param categoryIDS
	 *            分类ID
	 * @return List<ProductCategory>或者null
	 */
	public List<ProductCategory> getProductCategory(Long storeId,
			Long categoryId) {
		return productCategoryDao.getProductCategory(storeId, categoryId);
	}

	public List<ProductCategory> findByTypeStoreId(Long storeId, Integer type) {
		return productCategoryDao.findByTypeStoreId(storeId, type);
	}

	public List<TreeJsonBean> toJsonTree(List<ProductCategory> productCategorys) {
		List<TreeJsonBean> treeJsonBeans = new ArrayList<TreeJsonBean>();
		for (ProductCategory productCategory : productCategorys) {
			treeJsonBeans.add(this.toJsonTree(productCategory));
		}
		return treeJsonBeans;
	}

	public TreeJsonBean toJsonTree(ProductCategory productCategory) {
		return new TreeJsonBean(productCategory.getId(),
				productCategory.getpId(), productCategory.getName());
	}

	/**
	 * 根据父id查询
	 * 
	 * @param parentId
	 * @param storeId
	 * @return
	 */
	public List<ProductCategory> findByParent(Long parentId, Long storeId) {
		return productCategoryDao.findByParent(parentId, storeId);
	}
	/**
	 * APP 用户登录里面
	 * @param parentId
	 * @param storeId
	 * @return
	 */
	public List<ProductCategory> appFindProductCategory(Long parentId, Long storeId) {
		return productCategoryDao.appFindProductCategory(parentId, storeId);
	}
	/**
	 * POS 查询
	 * @param parentId
	 * @param storeId
	 * @return
	 */
	public List<ProductCategory> posFindProductCategory(Long parentId, Long storeId) {
		return productCategoryDao.posFindProductCategory(parentId, storeId);
	}

	public List<ProductCategory> checkName(String name, Long id, Long storeId) {
		
		return productCategoryDao.checkName(name,id,storeId);
	}

	public ProductCategory findProductCategoryByName(Long id, String name, Long storeId) {
		
		return productCategoryDao.findProductCategoryByName(id,name,storeId);
	}

	public Page findByStoreId(Long storeId, Page page) {
		
		return productCategoryDao.findByStoreId(storeId,page);
	}
}