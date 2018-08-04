package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ActivityDetailLocation;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ActivityDetailLocationDao;
@Service
public class ActivityDetailLocationService {
	
	@Autowired
	private ActivityDetailLocationDao activityDetailLocationDao;
	@Autowired
	private IdService idService;
	
	public void save (ActivityDetailLocation activityDetailLocation){
		activityDetailLocation.setId(idService.getId());
		activityDetailLocationDao.save(activityDetailLocation);
	}
	public void save (List<ActivityDetailLocation> activityDetailLocations){
		if (activityDetailLocations!=null) {
			for (ActivityDetailLocation activityDetailLocation : activityDetailLocations) {
				if (activityDetailLocation!=null) {
					this.save(activityDetailLocation);
				}
			}
		}
	}
	/**
	 * 通过详情ID删除
	 * @param activityDetailId
	 */
	public void deleteByActivityDetailId(Long activityDetailId) {
		activityDetailLocationDao.deleteByActivityDetailId(activityDetailId);
	}
	/**
	 * 通过详情ID查找
	 * @param activityDetailId
	 * @return
	 */
	public List<ActivityDetailLocation> findByActivityDetailId(Long activityDetailId) {
		return activityDetailLocationDao.findByActivityDetailId(activityDetailId);
	}
	public ActivityDetailLocation findById(Long id) {
		return activityDetailLocationDao.findById(id);
	}

}
