package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.SupermarketAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.SupermarketAdDao;
import cn.lastmiles.utils.FileServiceUtils;

/**
 * createDate : 2015年8月18日 下午4:15:23 
 */

@Service
public class SupermarketAdService {

	@Autowired
	private SupermarketAdDao supermarketAdDao;

	public Page list(Long storeId, Page page) {
		return supermarketAdDao.list(storeId,page);
	}
	
	public boolean save(SupermarketAd supermarketAd) {
		return supermarketAdDao.save(supermarketAd);
	}
	
	public boolean update(SupermarketAd supermarketAd) {
		return supermarketAdDao.update(supermarketAd);
	}
	
	public boolean delete(Long id,Long storeId) {
		return supermarketAdDao.delete(id, storeId);
	}
	
	public List<SupermarketAd> findListById(Long id) {
		List<SupermarketAd> saList = supermarketAdDao.findListById(id);
		for (SupermarketAd supermarketAd : saList) {
			supermarketAd.setPicURL(FileServiceUtils.getFileUrl(supermarketAd.getImageId()));
		}
		return saList;
	}
	
	public SupermarketAd findById(Long id){
		return (null == findListById(id)) ? null : findListById(id).get(0);
	}
	
	/**
	 * 查找最近的超市主广告
	 * @param lat
	 * @param lnt
	 * @return
	 */
	public List<SupermarketAd> findNearest(Double lat, Double lnt) {
		return supermarketAdDao.findNearest(lat, lnt);
	}

	public int checkDataHave(Long id,Long storeId, Long productCategoryId, Integer position) {
		return supermarketAdDao.checkDataHave(id,storeId,productCategoryId,position);
	}
}