package cn.lastmiles.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PointRule;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class PointRuleDao {
	

	public PointRule getPointRuleByStoreId(Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		PointRule pointRule = JdbcUtils.queryForObject("pointRule.getPointRuleByStoreId", map, PointRule.class);
		return pointRule;
	}

	public void save(PointRule pointRule) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", pointRule.getId());
		map.put("storeId", pointRule.getStoreId());
		map.put("money", pointRule.getMoney());
		map.put("point", pointRule.getPoint());
		map.put("validTime", pointRule.getValidTime());
		map.put("status", pointRule.getStatus());
		map.put("birthdayDoublePoint", pointRule.getBirthdayDoublePoint());
		map.put("evaluatePoint", pointRule.getEvaluatePoint());
		map.put("numberDay", pointRule.getNumberDay());
		map.put("costPoint", pointRule.getCostPoint());
		map.put("equalMoney", pointRule.getEqualMoney());
		map.put("restriction", pointRule.getRestriction());
		JdbcUtils.save("t_point_rule", map);
		
	}

	public void update(PointRule pointRule) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", pointRule.getId());
		map.put("storeId", pointRule.getStoreId());
		map.put("money", pointRule.getMoney());
		map.put("point", pointRule.getPoint());
		map.put("validTime", pointRule.getValidTime());
		map.put("status", pointRule.getStatus());
		map.put("birthdayDoublePoint", pointRule.getBirthdayDoublePoint());
		map.put("evaluatePoint", pointRule.getEvaluatePoint());
		map.put("numberDay", pointRule.getNumberDay());
		map.put("costPoint", pointRule.getCostPoint());
		map.put("equalMoney", pointRule.getEqualMoney());
		map.put("restriction", pointRule.getRestriction());
		int ret = JdbcUtils.update("pointRule.updatePointRule", map);
		System.out.println(ret);
		
	}
}
