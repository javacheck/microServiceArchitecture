/**
 * createDate : 2016年5月26日下午3:42:01
 */
package cn.lastmiles.v2.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.v2.dao.CommodityCategoryDao;

@Service
public class CommodityCategoryService {
	/**
	 * 日志记录
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommodityCategoryService.class);
	@Autowired
	private CommodityCategoryDao commodityCategoryDao;
	@Autowired
	private IdService idService;

	public List<ProductCategory> findByStoreId(Long storeId) {
		return commodityCategoryDao.findByStoreId(storeId);
	}

	public List<ProductCategory> findByStoreIdAndId(Long storeId,Long id) {
		return commodityCategoryDao.findByStoreIdAndId(storeId,id);
	}
	
	public ProductCategory findById(Long id) {
		return commodityCategoryDao.findById(id);
	}

	public void save(ProductCategory productCategory) {
		if( null == productCategory.getId() ){
			productCategory.setId(idService.getId());
		}
		if( null == productCategory.getpId() ){ // 没有父类则直接设置等级为顶级0,path为本身ID
			productCategory.setPath(productCategory.getId().toString());
			productCategory.setLevel(0); 
		} else {
			ProductCategory parent = findById(productCategory.getpId());
			
			logger.debug("新增商品分类时获取的上级商品分类是: 《{}》",parent);
			
			String parentPath = parent.getPath();
			if( StringUtils.isNotBlank(parentPath) ){
				productCategory.setPath(parent.getPath() + "_" + productCategory.getId());				
			} else {
				logger.error("新增商品分类时获取的上级商品分类存在错误~~~");
			}
			int level = 0;
			if( null == parent.getLevel() ){ // 兼容之前版本的数据
				 if( -1 == parentPath.lastIndexOf("_") ){ // 没有_连接符
					 level = 0;
				 } else {
					 String[] s = parentPath.split("_");					
					 level = s.length -1 ;
				 }
			} else {
				level = parent.getLevel();
			}
			level = level + 1 ;
			productCategory.setLevel(level);
		}
		logger.debug("新增的商品分类信息是：《{}》",productCategory);
		
		commodityCategoryDao.save(productCategory);
	}
	
	public void update(ProductCategory productCategory) {
		if( null == productCategory.getpId() ){ // 没有父类则直接设置等级为顶级0,path为本身ID
			productCategory.setPath(productCategory.getId().toString());
			productCategory.setLevel(0); 
		} else {
			ProductCategory parent = findById(productCategory.getpId());
			
			logger.debug("修改商品分类时获取的上级商品分类是: 《{}》",parent);
			
			String parentPath = parent.getPath();
			if( StringUtils.isNotBlank(parentPath) ){
				productCategory.setPath(parent.getPath() + "_" + productCategory.getId());				
			} else {
				logger.error("修改商品分类时获取的上级商品分类存在错误~~~");
			}
			int level = 0;
			if( null == parent.getLevel() ){ // 兼容之前版本的数据
				 if( -1 == parentPath.lastIndexOf("_") ){ // 没有_连接符
					 level = 0;
				 } else {
					 String[] s = parentPath.split("_");					
					 level = s.length -1 ;
				 }
			} else {
				level = parent.getLevel();
			}
			level = level + 1 ;
			productCategory.setLevel(level);
		}
		logger.debug("修改的商品分类信息是：《{}》",productCategory);
		
		commodityCategoryDao.update(productCategory);
	}

	public int deleteById(Long id) {
		return commodityCategoryDao.deleteById(id);
	}

	public int existCategoryName(String name, Long id,Long storeId) {
		return commodityCategoryDao.existCategoryName(name,id,storeId);
	}
}