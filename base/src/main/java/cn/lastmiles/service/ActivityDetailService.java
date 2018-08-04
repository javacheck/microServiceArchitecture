package cn.lastmiles.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.ActivityDetail;
import cn.lastmiles.bean.ActivityDetailLocation;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ActivityDetailDao;

@Service
public class ActivityDetailService {
	
	@Autowired
	private IdService idService;
	@Autowired
	private ActivityDetailDao activityDetailDao;
	@Autowired
	private ActivityDetailLocationService activityDetailLocationService;
	
	@Autowired
	private FileService fileService;
	
	public Page list(Page page,Long activityId ){
		return activityDetailDao.list(page, activityId);
	}
	
	@Transactional
	public void save(ActivityDetail activityDetail, MultipartFile imageFile, List<ActivityDetailLocation> activityDetailLocations) {
		activityDetail.setId(idService.getId());
		if (imageFile!=null) {
			String imageId = null;
			try {
				imageId=fileService.save(imageFile.getInputStream());
				activityDetail.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		activityDetailDao.save(activityDetail);
		if (activityDetailLocations!=null) {//保持藏宝地点
			for (ActivityDetailLocation activityDetailLocation : activityDetailLocations) {
				activityDetailLocation.setActivityDetailId(activityDetail.getId());
				activityDetailLocationService.save(activityDetailLocation);
			}
		}
	}
	@Transactional
	public void update(ActivityDetail activityDetail, MultipartFile imageFile, List<ActivityDetailLocation> activityDetailLocations) {
		if (null != imageFile && imageFile.getSize() > 0) {
			fileService.delete(activityDetail.getImageId()); // 先删除之前的
			String imageId=null;
			try {
				imageId = fileService.save(imageFile.getInputStream());
				activityDetail.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		activityDetailDao.update(activityDetail);
		activityDetailLocationService.deleteByActivityDetailId(activityDetail.getId());//删除之前设置的藏宝地点
		if (activityDetailLocations!=null) {//保持藏宝地点
			for (ActivityDetailLocation activityDetailLocation : activityDetailLocations) {
				activityDetailLocation.setActivityDetailId(activityDetail.getId());
				activityDetailLocationService.save(activityDetailLocation);
			}
		}
	}

	public ActivityDetail findById(Long id) {
		return activityDetailDao.findById(id);
	}
	@Transactional
	public void delete(Long id) {
		activityDetailDao.delete(id);
		activityDetailLocationService.deleteByActivityDetailId(id);
	}

	public ActivityDetail findByTime(Long activityId,Date date) {
		return activityDetailDao.findByTime(activityId,date);
	}

	public ActivityDetail findByIdAndTime(Long id, Date date) {
		return activityDetailDao.findByIdAndTime(id,date);
	}
	

}
