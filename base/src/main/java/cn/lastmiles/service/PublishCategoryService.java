package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.service.PublishCategory;
import cn.lastmiles.dao.PublishCategoryDao;

@Service
public class PublishCategoryService {
	
	@Autowired
	private PublishCategoryDao publishCategoryDao;
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<PublishCategory> findAll(){
		return publishCategoryDao.findAll();
	}
	public PublishCategory findById(Long id) {
		return publishCategoryDao.findById(id);
	}
}
