package cn.lastmiles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ActivityPartake;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ActivityPartakeDao;

@Service
public class ActivityPartakeService {
	
	@Autowired
	private ActivityPartakeDao activityPartakeDao;
	@Autowired
	private IdService idService;
	
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory
			.getLogger(ActivityPartakeDao.class);

	public void save(ActivityPartake activityPartake) {
		activityPartake.setId(idService.getId());
		activityPartakeDao.save(activityPartake);
	}

	public ActivityPartake findByactivityDetailIdAndUserId(Long activityDetailId,Long userId) {
		return activityPartakeDao.findByactivityDetailIdAndUserId(activityDetailId,userId);
	}

	public ActivityPartake findById(Long id) {
		return activityPartakeDao.findById(id);
	}

}
