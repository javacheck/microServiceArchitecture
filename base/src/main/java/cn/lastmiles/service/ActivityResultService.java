package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.AcitivityStatistics;
import cn.lastmiles.bean.ActivityPartake;
import cn.lastmiles.bean.ActivityResult;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.ActivityResultDao;

@Service
public class ActivityResultService {
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(ActivityResultService.class);
	@Autowired
	private ActivityResultDao activityResultDao;
	@Autowired
	private IdService idService;
	
	public void save(ActivityResult activityResult){
		activityResult.setId(idService.getId());
		activityResult.setStatus(Constants.ActivityResult.STATUS_PENDING);//默认待审核
		activityResultDao.save(activityResult);
	}

	public List<List<Map<String, Object>>> findByUserId(Long userId) {
		List<List<Map<String, Object>>> listArray = new  ArrayList<List<Map<String, Object>>>();
		List<ActivityPartake> listAD = activityResultDao.findByUserIdGetDetialId(userId);
		for (ActivityPartake ActivityPartake : listAD) {
			List<Map<String, Object>> listAR = activityResultDao.findByUserId(userId,ActivityPartake.getActivityDetailId());
			for (Map<String, Object> map : listAR) {
				Long getTime =Long.parseLong(map.get("getTime")+"");
				long iHour=getTime/3600;
				long iMin=(getTime-iHour*3600)/60;
				long iSen=getTime-iHour*3600-iMin*60;
				map.put("getTime", iHour == 0 ? (iMin == 0 ? iSen+"秒" :iMin+"分"+iSen+"秒"):iHour+"小时"+iMin+"分"+iSen+"秒");
			}
			listArray.add(listAR);
		}
		return listArray;
	}

	public List<Map<String, Object>> GetActivityStatistics(){
		return activityResultDao.GetActivityStatistics();
	}
	
	public void saveStatistics(List<Map<String, Object>> listMap){
		activityResultDao.saveStatistics(listMap);
	}

	public List<AcitivityStatistics> getStatistics() {
		return activityResultDao.getStatistics();
	}

	public AcitivityStatistics getStatistics(Long userId) {
		return activityResultDao.getStatistics(userId);
	}

	public Page getAuditInfo(String mobile, Integer status, Page page) {
		return activityResultDao.getAuditInfo(mobile, status, page);
	}

	public boolean updateStatus(Long id, Long userId, Integer status) {
		return activityResultDao.updateStatus(id,userId,status);
	}

	public List<Map<String, Object>> findByIdAndUserId(Long id, Long userId) {
		return activityResultDao.findByIdAndUserId(id,userId);
	}
}