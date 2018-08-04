/**
 * createDate : 2016年1月29日上午10:13:47
 */
package cn.lastmiles.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.Brand;
import cn.lastmiles.dao.BrandDao;

@Service
public class BrandService {
	@Autowired
	private BrandDao brandDao;
	
	/**
	 * 根据商家ID查询品牌信息
	 * @param storeId 商家ID(为空查询所有品牌信息)
	 * @return 品牌信息列表或者null
	 */
	public List<Brand> getBrandListByStoreId(Long storeId){
		return brandDao.getBrandListByStoreId(storeId);
	}
}