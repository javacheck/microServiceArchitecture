package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.PointRule;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.PointRuleDao;

@Service
public class PointRuleService {
	
	@Autowired
	private PointRuleDao pointRuleDao;
	
	@Autowired
	private IdService idService;

	public PointRule getPointRuleByStoreId(Long storeId) {
		
		return pointRuleDao.getPointRuleByStoreId(storeId);
	}

	public void editPointRule(PointRule pointRule) {
		if(pointRule.getId()==null){
			pointRule.setId(idService.getId());
			pointRuleDao.save(pointRule);
		}else{
			pointRuleDao.update(pointRule);
		}
		
	}
}
