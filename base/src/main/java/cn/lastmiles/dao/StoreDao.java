package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class StoreDao {
	private final static Logger logger = LoggerFactory
			.getLogger(StoreDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Account findByMobile(String mobile) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select a.id,a.mobile,a.password,a.name,a.parentId,a.path,a.storeId from t_account a where mobile = ? and storeId is not null",
						mobile);
		return list.isEmpty() ? null : BeanUtils.toBean(Account.class,
				list.get(0));
	}

	/**
	 * 保存方法
	 * 
	 * @param Store
	 */
	public void save(Store store) {
		jdbcTemplate
				.update("insert into t_store(id,name,parentId,path,accountId,createdTime) values(?,?,?,?,?,?)",
						store.getId(), store.getName(), store.getParentId(),
						store.getPath(), store.getAccountId(),
						store.getCreatedTime());
	}

	/**
	 * 查询所有商品分类
	 * 
	 * @return
	 */
	public Page findAll(String name, Page page, Long accountId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();

		String where = " where 1 = 1 ";
		if (StringUtils.isNotBlank(name)) {
			where += " and s.name like ? ";
			parameters.add("%" + name + "%");
		}

		where += " and ( s.id in (SELECT DISTINCT a.storeId FROM t_account a WHERE path like ? OR path LIKE ? OR path LIKE ? OR path LIKE ? ) OR accountId = ? ";
		parameters.add("%-" + accountId + "-%");
		parameters.add(accountId + "-%");
		parameters.add("%-" + accountId);
		parameters.add(accountId);
		parameters.add(accountId);

		where += " OR s.accountId IN (SELECT DISTINCT a1.id FROM t_account a1	WHERE	a1.path LIKE ?	OR a1.path LIKE ?	OR a1.path LIKE ?	OR a1.path = ? )";// 下属创建的店
		parameters.add("%-" + accountId + "-%");
		parameters.add(accountId + "-%");
		parameters.add("%-" + accountId);
		parameters.add(accountId);
		if (storeId != null) {
			where += " OR  s.id= ? ";
			parameters.add(storeId);

		}

		where += " ) ";
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_store s " + where, Integer.class,
				parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer("select s.* from t_store s ");
		sql.append(where + " order by id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	/**
	 * 通过 父类ID 查询
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Store> findByParentId(Long parentId) {
		StringBuffer sql = new StringBuffer(
				"select t.* ,(SELECT name FROM t_store tpc WHERE  t.parentId=tpc.id ) AS parentName from t_store t where 1 = 1 ");
		List<Map<String, Object>> list = null;
		if (parentId != null) {
			sql.append(" and parentId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), parentId);
		} else {
			sql.append(" and parentId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString());
		}

		return BeanUtils.toList(Store.class, list);
	}

	/**
	 * 通过ID 修改数据
	 * 
	 * @param Store
	 */
	public void updateByID(Store store) {
		jdbcTemplate.update("update t_store set name = ?  where id = ?",
				store.getName(), store.getId());
	}

	/**
	 * 通过ID查询数据
	 * 
	 * @param id
	 * @return
	 */
	public Store findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_store t where id = ?", id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public void delete(Long id) {
		jdbcTemplate
				.update("DELETE FROM  t_store  where id = ? or path like ? or path like ?",
						id, "%-" + id + "-%", id + "-%");
	}

	/**
	 * 我创建的，或者我属于的商店 或者我下属的商店 或者 我和我下属创建的店
	 * 
	 * @param accountId
	 * @param storeId
	 * @return
	 */
	public List<Store> findMyStore(Long accountId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer();
		sf.append(" select s.* from t_store s where 1= 1 ");
		sf.append(" and (");
		sf.append("  s.id in (SELECT DISTINCT a.storeId FROM t_account a WHERE path like ? OR path LIKE ? OR path LIKE ? OR path = ? ) OR accountId = ? ");// 下属拥有的店
		parameters.add("%-" + accountId + "-%");
		parameters.add(accountId + "-%");
		parameters.add("%-" + accountId);
		parameters.add(accountId);
		parameters.add(accountId);
		sf.append(" OR s.accountId IN (SELECT DISTINCT a1.id FROM t_account a1	WHERE	a1.path LIKE ?	OR a1.path LIKE ?	OR a1.path LIKE ?	OR a1.path = ? )");// 下属创建的店
		parameters.add("%-" + accountId + "-%");
		parameters.add(accountId + "-%");
		parameters.add("%-" + accountId);
		parameters.add(accountId);
		if (storeId != null && storeId != Constants.Status.NO_STORE) {
			sf.append(" OR  s.id= ? ");
			parameters.add(storeId);

		}
		sf.append(" )  ORDER BY s.id");
		return BeanUtils.toList(Store.class,
				jdbcTemplate.queryForList(sf.toString(), parameters.toArray()));
	}

	public List<Store> findAll() {
		return BeanUtils.toList(Store.class, jdbcTemplate
				.queryForList("select id,name,path,parentId from t_store"));
	}

	public Store findByProduct(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId ,(SELECT name FROM t_store tpc WHERE  t.parentId=tpc.id ) AS parentName from t_store t where productId = ?",
						productId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public List<Store> findByAccount(Long accountId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId from t_store where accountId = ? order by id desc",
						accountId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toList(Store.class, list);
	}

	public List<Store> findByAgentId(Long agentId) {
		return BeanUtils.toList(Store.class, jdbcTemplate.queryForList(
				"select id,name,path,parentId from t_store where agentId = ? ",
				agentId));
	}

	// 代理商商家查询(代理商及其他下面子代理商的商家)
	public List<Store> getAgentAndStoreList(Long agentId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_agent a ,t_store s where a.id = s.agentId ");
		querySQL.append(" and a.id in (select a.id from t_agent a where a.path like ? or (a.path like ? and parentId is null) or a.path like ? )");

		String data = "s.id ";

		parameters.add(agentId + "_%");
		parameters.add(agentId);
		parameters.add("%_" + agentId);

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + querySQL.toString() + " order by s.id desc",
				parameters.toArray());
		return BeanUtils.toList(Store.class, list);
	}

	public Page apiList(Page page, Long storeTypeId, String keywords,
			Integer status, Integer promotionType,Double shipAmount, String sort,String sortRule, Double longitude, Double latitude) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer(" from t_store s where 1 = 1");
		if (StringUtils.isNotBlank(keywords)) {
			sf.append(" and s.name like ?");
			parameters.add("%" + keywords + "%");
		}
		if (storeTypeId != null) {
			sf.append(" and s.shopTypeId = ?");
			parameters.add(storeTypeId);
		}

		if (shipAmount != null) {
			sf.append(" and s.shipAmount = ? ");
			parameters.add(shipAmount);
		}
		if (status != null) {
			sf.append(" and s.status = ?");
			parameters.add(status);
		}
		
		if (promotionType != null) {
			sf.append(" and (SELECT count(1) FROM t_promotion p WHERE p.startDate <= ? AND p.endDate > ? AND p.`status` = ? AND p.type = ? AND p.storeId=s.id  ) >=1 ");
			parameters.add(new Date());
			parameters.add(new Date());
			parameters.add(Constants.Promotion.STATUS_ON);
			parameters.add(promotionType);
		}

		Integer total = jdbcTemplate.queryForObject(
				"select count(1)  " + sf.toString(), Integer.class,
				parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		String selectDistance = (null != longitude && null != latitude) ? ",theodolitic(s.longitude,s.latitude,"+ longitude + "," + latitude + ") as distance "	: "";
		String orderDistance = " ";
		if (StringUtils.isNotBlank(sort)) {
			orderDistance+=sort+ " "+sortRule+",";
		}
		if (null != longitude && null != latitude) {
			orderDistance+=" distance is null,distance,";
		}
		logger.debug("select s.* " + selectDistance + " " + sf.toString()
				+ " order by " + orderDistance + " id desc limit ?,?"+"parameters is :"+parameters);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select s.* " + selectDistance + " " + sf.toString()
						+ " order by " + orderDistance + " id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));
		return page;
	}

	public Store posFindById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select name,mobile,phone from t_store t where id = ?", id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public Store findShipTimeShipType(Long storeId) {
		return BeanUtils.toBean(Store.class, jdbcTemplate.queryForMap(
				"select businessTime,shipType,payType from t_store where id = ?",
				storeId));
	}

	/**
	 * APP-Third
	 * @param lastId 商家数据从这个ID开始,既必须大于此ID
	 * @param appId 合作者ID
	 */
	public List<Store> getAssignStoreByPartnerId(Long lastId, Long appId) {
		List<Object> parameters = new ArrayList<Object>();
//		StringBuffer querySQL = new StringBuffer("select s.* from t_store s where s.partnerId = ? and s.id > ? order by s.id asc limit ?,?");
		StringBuffer querySQL = new StringBuffer("select s.* from t_store s where s.parentId = (select pa.storeId from t_partner pa where pa.id = ? ) and s.id > ? order by s.id asc limit ?,?");
		parameters.add(appId);
		parameters.add(lastId);
		parameters.add(0);
		parameters.add(100);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Store.class, list);
		}
		return null;
	}

	public List<Store> findByOrganizationId(Long id) {
		StringBuffer querySQL = new StringBuffer(" select * from t_store s where organizationId in (");
		querySQL.append("select t.id from (");
		querySQL.append("select id from t_organization where find_in_set( id, queryChildrenTreeInfo (?) )");
		querySQL.append(") t ");
		querySQL.append(" )");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), id);
		if( null != list && list.size() > 0  ){
			return BeanUtils.toList(Store.class, list);
		}
		return null; 
	}
	
	/**
	 * 如果是连锁，找这个storeId下，子的商家。否则就是这个商家
	 * @param 
	 * @return
	 */
	public List<Store> findAllChildrenStore(Long storeId){
		List<Map<String,Object>> paths = jdbcTemplate.queryForList("select path,id from t_organization where id = (select organizationId from t_store where id = ?)", storeId);
		if (paths.isEmpty()){
			return BeanUtils.toList(Store.class, jdbcTemplate.queryForList("select * from t_store where id = ?",storeId));
		}else {
			Object id = paths.get(0).get("id");
			String sql = "select id from t_organization where id = ? or  path like ?";
			List<Long> ids = jdbcTemplate.queryForList(sql, Long.class,id,"%"+id+"_%");
			
			StringBuilder path = new StringBuilder();
			
			for (Long i : ids){
				if (path.length() == 0){
					path.append(i);
				}else {
					path.append("," + i);
				}
			}
			
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_store where organizationId in ("+path+")");
			return BeanUtils.toList(Store.class, list);
		}
	}
	
	/**
	 * 如果是连锁，找这个storeId下，子的实体商家。否则就是这个商家
	 * @param 
	 * @return
	 */
	public List<Store> findAllEntryChildrenStore(Long storeId){
		List<Map<String,Object>> paths = jdbcTemplate.queryForList("select path,id from t_organization where id = (select organizationId from t_store where id = ?)", storeId);
		if (paths.isEmpty()){
			return BeanUtils.toList(Store.class, jdbcTemplate.queryForList("select * from t_store where id = ?",storeId));
		}else {
			Object id = paths.get(0).get("id");
			String sql = "select id from t_organization where id = ? or  path like ?";
			List<Long> ids = jdbcTemplate.queryForList(sql, Long.class,id,"%"+id+"_%");
			
			StringBuilder path = new StringBuilder();
			
			for (Long i : ids){
				if (path.length() == 0){
					path.append(i);
				}else {
					path.append("," + i);
				}
			}
			List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_store where isMain = 0 and organizationId in ("+path+")");
			return BeanUtils.toList(Store.class, list);
		}
	}
	
	/**
	 * 根据organizationId查找同一颗组织结构树的所有商家
	 * @param organizationId
	 * @return
	 */
	public List<Store> findAllStoreOnOrganizationId(Long organizationId){
		String path = jdbcTemplate.queryForObject("select path from t_organization where id = ?",String.class, organizationId);
		String parentId = path.split("_")[0];
		logger.debug("parentId = {}",parentId);
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_store where organizationId = ? or organizationId in (select id from t_organization where path like ?)", parentId,"%" + parentId + "_%");
		return BeanUtils.toList(Store.class, list);
	}

	/**
	 * 根据storeId查找同一颗组织结构树的所有商家
	 * @param storeId
	 * @return
	 */
	public List<Store> findAllStoreOnOneOrganizationTree(Long storeId){
		List<Store> list = new ArrayList<Store>();
		Store store = this.findById(storeId);
		if (store.getOrganizationId() == null){
			list.add(store);
			return list;
		}
		
		return findAllStoreOnOrganizationId(store.getOrganizationId());
	}
	
	/**
	 * 连锁商家总部
	 * @param storeId
	 * @return
	 */
	public Store findTopStore(Long storeId){
		Store store = this.findById(storeId);
		if (store.getOrganizationId() == null){
			return store;
		}
		String path = jdbcTemplate.queryForObject("select path from t_organization where id = ?",String.class, store.getOrganizationId());
		String parentId = path.split("_")[0];
		
		Long topId = jdbcTemplate.queryForObject("select storeId from t_organization where id = ?",Long.class, parentId);
		return findById(topId);
	}

	public List<Store> finAllEntityStore() {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select id,name,path,parentId from t_store where isMain = ?",Constants.Store.STORE_SHOP );
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Store.class, list);			
		}
		return null;
	}

	public List<Store> findByStoreIdString(String storeIdString) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select id,name from t_store where id in ("+storeIdString+") and isMain = ?",Constants.Store.STORE_SHOP);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Store.class, list);			
		}
		return null;
	}
}