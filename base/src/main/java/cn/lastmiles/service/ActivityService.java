package cn.lastmiles.service;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Activity;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ActivityDao;

@Service
public class ActivityService {
	
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private FileService fileService;

	/**
	 * 分页列表，根据名称模糊查找
	 * 
	 * @param page
	 * @param name
	 * @return
	 */
	public Page list(Page page, String name) {
		return activityDao.list(page, name);
	}

	public void save(Activity activity, MultipartFile imageFile) {
		activity.setId(idService.getId());
		if (imageFile!=null) {
			String imageId = null;
			try {
				imageId=fileService.save(imageFile.getInputStream());
				activity.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		activityDao.save(activity);
	}

	public void update(Activity activity,MultipartFile imageFile) {
		if (null != imageFile && imageFile.getSize() > 0) {
			fileService.delete(activity.getImageId()); // 先删除之前的
			String imageId=null;
			try {
				imageId = fileService.save(imageFile.getInputStream());
				activity.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		activityDao.update(activity);
	}

	public Activity findById(Long id) {
		return activityDao.findById(id);
	}

	public void delete(Long id) {
		activityDao.delete(id);
	}
}
