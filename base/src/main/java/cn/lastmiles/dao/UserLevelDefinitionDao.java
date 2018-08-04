package cn.lastmiles.dao;

/**
 * createDate : 2016年1月20日下午2:53:48
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class UserLevelDefinitionDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StoreDao storeDao;

	public int checkUserLevelDefinitionName(Long id, Long storeId, String name) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("name", name);
		parameters.put("storeId", storeId);
		return JdbcUtils.queryForObject(
				"userLevelDefinition.checkUserLevelDefinitionName", parameters,
				Integer.class);
	}

	public Page findByName(String storeIdString, String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" select uld.*,(select s.name from t_store s where s.id = uld.storeId) as storeName from t_user_level_definition uld where 1=1");

		if (StringUtils.isNotBlank(name)) {
			querySQL.append(" and instr(uld.name,?)>0 ");
			parameters.add(name);
		}

		if (StringUtils.isNotBlank(storeIdString)) {
			querySQL.append(" and uld.storeId in ( " + storeIdString + " )");
		}

		return JdbcUtils.selectMysql(jdbcTemplate, page, querySQL.toString(),
				parameters, "uld.discount ", UserLevelDefinition.class);
	}

	public void save(UserLevelDefinition userLevelDefinition) {
		JdbcUtils.save(userLevelDefinition);
	}

	public int update(UserLevelDefinition userLevelDefinition) {
		return JdbcUtils.update(userLevelDefinition, "createdTime");
	}

	public UserLevelDefinition findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select uc.*,(select s.name from t_store s where s.id = uc.storeId) as storeName from t_user_level_definition uc where uc.id = ?",
						id);
		if (null != list && list.size() > 0) {
			return BeanUtils.toBean(UserLevelDefinition.class, list.get(0));
		}
		return null;
	}

	public int deleteById(Long id) {
		return JdbcUtils.deleteById(UserLevelDefinition.class, id);
	}

	/**
	 * 查找这个店会员等级定义，按照折扣排序
	 * 如果有总部，直接找总部的积分定义
	 * @param storeId
	 * @return
	 */
	public List<UserLevelDefinition> findByStoreId(Long storeId){
//		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user_level_definition where storeId = ? order by discount ", storeId);
//		if (list.isEmpty()){
//			Long orgId = jdbcTemplate.queryForObject("select organizationId from t_store where id = ?",Long.class,storeId);
//			if (orgId != null){
//				String path = jdbcTemplate.queryForObject("select path from t_organization where id = ?", String.class,orgId);
//				String[] arr = path.split("_");
//				for (String id : arr){
//					if (!id.equals(orgId.toString())){
//						list = jdbcTemplate.queryForList("select * from t_user_level_definition where storeId = (select storeId from t_organization where id = ?) order by discount ", id);
//						if (!list.isEmpty()){
//							return BeanUtils.toList(UserLevelDefinition.class, list);
//						}
//					}else {
//						return new ArrayList<UserLevelDefinition>();
//					}
//				}
//			}
//			return new ArrayList<UserLevelDefinition>();
			Store topStore = storeDao.findTopStore(storeId);
			return BeanUtils.toList(UserLevelDefinition.class, jdbcTemplate.queryForList("select * from t_user_level_definition where storeId = ? order by discount ", topStore.getId()));
//		}else {
//			return BeanUtils.toList(UserLevelDefinition.class, list);
//		}
	}

	public void saveUserLevelDefinitionProductCategory(Long id, Long categoryId) {
		jdbcTemplate
		.update("insert into t_user_level_definition_product_category(userLevelDefinitionId,productCategoryId) values(?,?)",
				id,categoryId);
		
	}

	public List<Long> findUserLevelDefinitionProductCategoryById(Long id) {
		List<Long> list = jdbcTemplate
				.queryForList(
						"select productCategoryId from t_user_level_definition_product_category where userLevelDefinitionId= ?",
						Long.class,id);
		return list;
	}

	public void deleteUserLevelDefinitionProductCategoryById(Long id) {
		jdbcTemplate.update("delete from t_user_level_definition_product_category  where userLevelDefinitionId = ?",
				id);
		
	}

	public Map<String, Object> judgeByStoreId(Long storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user_level_definition  where storeId = ?", storeId);
		if( list.isEmpty()){
			map.put("status", 0);
			map.put("type", 0);
			return map;
		}
		UserLevelDefinition ud = BeanUtils.toBean(UserLevelDefinition.class, list.get(0));
		if( list.size() >= 2 ){ // 有多个
			map.put("status", 2);
			map.put("type", ud.getType());
		} else { // 只有一个
			map.put("status", 1);
			map.put("type", ud.getType());
		}
		return map;
	}
}