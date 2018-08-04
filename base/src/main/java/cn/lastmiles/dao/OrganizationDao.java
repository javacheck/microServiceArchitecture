package cn.lastmiles.dao;
/**
 * createDate : 2016年2月26日上午10:57:57
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Organization;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class OrganizationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public int checkOrganizationName(Long id, String name) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("name", name);
		return JdbcUtils.queryForObject("organization.checkOrganizationName",parameters,Integer.class);
	}

	public Page findByName(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select * from t_organization where 1=1");
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and instr(name,?)>0 ");
			parameters.add(name);			
		}
		return JdbcUtils.selectMysql(jdbcTemplate, page, querySQL.toString(), parameters, "id desc", Organization.class);
	}

	public void save(Organization organization) {
		JdbcUtils.save(organization);
	}

	public int update(Organization organization) {
		return JdbcUtils.update(organization);
	}

	public Organization findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select o.*,(select oo.name from t_organization oo where oo.id = o.parentId) as parentName from t_organization o where id = ? ",id);
		if(list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(Organization.class,list.get(0));
	}
	
	/**
	 * 根据商家ID查询组织架构集合
	 * @param storeId  商家ID
	 * @return null或者组织架构集合
	 */
	public List<Organization> findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_organization where storeId = ? order by id ",storeId);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Organization.class,list);
		}
		return null;
	}

	public int deleteById(Long id) {
		return JdbcUtils.deleteById(Organization.class, id);
	}

	public List<Organization> findByNameFuzzy(String parentName) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList( "select name from t_organization where instr(name,?)>0 ",parentName);
		if( null != list && !list.isEmpty()){
			return BeanUtils.toList(Organization.class, list);			
		}
		return null;
	}
	
	public List<Organization> findByNameFuzzy(Long id,String parentName) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList( "select name from t_organization where instr(name,?)>0 and instr(path,(select id from t_organization where find_in_set( id, queryParentTreeInfo (?) ) and parentId is null)) > 0 ",parentName,id);
		if( null != list && !list.isEmpty()){
			return BeanUtils.toList(Organization.class, list);			
		}
		return null;
	}

	public Organization checkParentName(Long id ,String parentName) {
		List<Object> parameters = new ArrayList<Object>();
		String querySQL = null;
		if( null == id ){
			querySQL = "select o.id,o.level from t_organization o where name = ? ";
			parameters.add(parentName);
		} else {
			querySQL = "select o.id,o.level from t_organization o where name = ? and id <> ?  ";
			parameters.add(parentName);
			parameters.add(id);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,parameters.toArray());
		if( null != list && !list.isEmpty() ){
			return BeanUtils.toBean(Organization.class,list.get(0));
		}
		return null;
		
	}

	public List<Organization> findOrganizationList() {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_organization order by id ");
		return list.isEmpty() ? null : BeanUtils.toList(Organization.class,list);
	}

	public List<Organization> findByParentIdFuzzy(Long parentId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select * from t_organization where (id = ? or instr(path,?) > 0)  ");
		parameters.add(parentId);
		parameters.add(parentId+"_");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		
		return list.isEmpty() ? null : BeanUtils.toList(Organization.class,list);
	}

	public Organization checkHasPermission(Long id,Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		String querySQL = null;
		if( null == id ){
			querySQL = "select * from t_organization o where storeId = ?";
			parameters.add(storeId);
		} else {
			List<Organization> list = findByStoreId(storeId);
			if( null == list || list.isEmpty() ){
				return null;
			}
			String superParent = list.get(0).getPath().split("_")[0];
			if(ObjectUtils.equals(superParent, id)){
				return null;
			} else {
				return list.get(0);
			}
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,parameters.toArray());
		if( null != list && !list.isEmpty() ){
			return BeanUtils.toBean(Organization.class,list.get(0));
		}
		return null;
	}

	public int checkNameRepetition(boolean isAdmin,Long parentId, Long id, String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = null;
		if(isAdmin){
			querySQL = new StringBuffer(" select count(*) from t_organization o where o.name = ? ");
			parameters.add(name);
			if( null != id ){
				querySQL.append(" and o.id <> ?");
				parameters.add(id);
			}
			if( null != parentId){
				String path = jdbcTemplate.queryForObject("select path from t_organization where id = ? ",String.class,parentId);
				querySQL.append(" and instr(o.path,?)>0 ");
				parameters.add(path.split("_")[0]);
			} else {
				if( null != id ){
					String path = jdbcTemplate.queryForObject("select path from t_organization where id = ? ",String.class,id);
					querySQL.append(" and instr(o.path,?)>0 ");
					parameters.add(path.split("_")[0]);
				}
			}
			return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		} else {
			
			querySQL = new StringBuffer(" select count(*) from t_organization o where o.name = ? ");
			parameters.add(name);
			if( null != id ){
				querySQL.append(" and o.id <> ?");
				parameters.add(id);
			}
			if( null != parentId){
				String path = jdbcTemplate.queryForObject("select path from t_organization where id = ? ",String.class,parentId);
				querySQL.append(" and instr(o.path,?)>0 ");
				parameters.add(path.split("_")[0]);
			} else {
				if( null != id ){
					String path = jdbcTemplate.queryForObject("select path from t_organization where id = ? ",String.class,id);
					querySQL.append(" and instr(o.path,?)>0 ");
					parameters.add(path.split("_")[0]);
				}
			}
			return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		}
		
	}

	public Organization checkParentName(Long id, String parentName, String superParent) {
		List<Object> parameters = new ArrayList<Object>();
		String querySQL = null;
		if( null == id ){
			querySQL = "select o.id,o.level from t_organization o where name = ? and instr(path,?)>0 ";
			parameters.add(parentName);
			parameters.add(superParent);
		} else {
			querySQL = "select o.id,o.level from t_organization o where name = ? and id <> ? and instr(path,?)>0 ";
			parameters.add(parentName);
			parameters.add(id);
			parameters.add(superParent);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,parameters.toArray());
		if( null != list && !list.isEmpty() ){
			return BeanUtils.toBean(Organization.class,list.get(0));
		}
		return null;
	}

	/**
	 * 根据组织架构ID查询所属其下的组织架构列表
	 * @param id 组织架构ID
	 * @param onlyDirectly 是否只查询下一级别
	 * @return 组织架构集合或者null
	 */
	public List<Organization> getChildrenTreeById(Long id,boolean onlyDirectly) {
		String querySQL = null;
		if(onlyDirectly){
			querySQL = "select * from t_organization where find_in_set( id, queryChildrenTreeInfo (?) ) limit 1,1 ";
		} else {
			querySQL = "select * from t_organization where find_in_set( id, queryChildrenTreeInfo (?) ) ";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,id);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Organization.class,list);
		}
		return null;
	}

	/**
	 * 根据组织架构ID查询其所属的组织架构父级
	 * @param id 组织架构ID
	 * @param onlyDirectly 是否只查询顶级父级
	 * @return 组织架构集合或者null
	 */
	public List<Organization> getParentTreeById(Long id, boolean onlyRoot) {
		String querySQL = null;
		if(onlyRoot){
			querySQL = "select * from t_organization where find_in_set( id, queryParentTreeInfo (?) ) and parentId is null ";
		} else {
			querySQL = "select * from t_organization where find_in_set( id, queryParentTreeInfo (?) ) ";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL,id);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Organization.class,list);
		}
		return null;
	}

}