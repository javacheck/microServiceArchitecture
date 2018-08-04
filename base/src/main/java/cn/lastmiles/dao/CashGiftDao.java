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

import cn.lastmiles.bean.CashGift;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.common.utils.StringUtils;

/**
 * createDate : 2015年9月7日 下午2:46:44
 */

@Repository
public class CashGiftDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CashGiftDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CashGift findOne(Long id, Long userId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select id,userId,amount,status,createdTime,memo,validTime,storeId,type from t_cash_gift where id = ? and status = ? ");
		parameters.add(id);
		parameters.add(Constants.CashGift.STATUSNORMAL);
		if( null != userId ){
			querySQL.append(" and userId = ? ");
			parameters.add(userId);
		}
		
		if( null != storeId ){
			querySQL.append(" and (storeId = ? or storeId is null) ");
			parameters.add(storeId);
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if (null != list && list.size() > 0 ) {
			return BeanUtils.toBean(CashGift.class, list.get(0));
		}
		return null;
	}
	
	public CashGift findOne(Long id, Long userId, Long storeId,Integer status) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select id,userId,amount,status,createdTime,memo,validTime,storeId,type from t_cash_gift where id = ? ");
		parameters.add(id);
		if( null != status ){
			querySQL.append(" and status = ? ");
			parameters.add(status);
		}
		if( null != userId ){
			querySQL.append(" and userId = ? ");
			parameters.add(userId);
		}
		
		if( null != storeId ){
			querySQL.append(" and (storeId = ? or storeId is null) ");
			parameters.add(storeId);
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if (null != list && list.size() > 0 ) {
			return BeanUtils.toBean(CashGift.class, list.get(0));
		}
		return null;
	}

	/**
	 * 用户在某个商家下未使用的优惠卷
	 * 
	 * @param userId
	 * @param storeId
	 * @return
	 */
	public List<CashGift> findByUserIdAndStoreId(Long userId, Long storeId) {
		logger.debug("findByUserIdAndStoreId userid = {},storeId = {}",userId,storeId);
		List<Long> ids = new ArrayList<Long>();
		ids.add(storeId);
		
		List<Map<String,Object>> path = jdbcTemplate.queryForList("select path,id from t_organization where id = (select organizationId from t_store where id = ?)", storeId);
		
		if (!path.isEmpty()){
			String p = (String) path.get(0).get("path");
			String[] arr = p.split("_");
			
			List<Long> orgIds = new ArrayList<Long>();
			for (String s : arr){
				orgIds.add(Long.valueOf(s));
				if (s.equals(path.get(0).get("id").toString())){
					//只到这个商家的关联组织架构
					break;
				}
			}
			logger.debug("orgIds = {}",orgIds);
			String temp = "";
			for (int i = 0; i < orgIds.size(); i++){
				if (i == 0){
					temp += orgIds.get(i);
				}else {
					temp += ",";
					temp += orgIds.get(i);
				}
			}
			List<Map<String,Object>> sIds = jdbcTemplate.queryForList("select storeId from t_organization where id in ("+temp+")");
			for (Map<String,Object> map : sIds){
				ids.add(Long.valueOf(map.get("storeId").toString()));
			}
		}
		
		logger.debug("ids = {}",ids);
		String temp = "";
		for (int i = 0; i < ids.size(); i++){
			if (i == 0){
				temp += ids.get(i);
			}else {
				temp += ",";
				temp += ids.get(i);
			}
		}
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,userId,amount,status,createdTime,memo,validTime,storeId,type from t_cash_gift where userId = ? and ( storeId in ("+temp+") or storeId is null)  and status = ?"
						+ " and validTime >= ?",
						userId,  Constants.CashGift.STATUSNORMAL,new Date());
		logger.debug("findByUserIdAndStoreId list = {}",list);
		return BeanUtils.toList(CashGift.class, list);
	}

	public Page list(String mobile, String shopName, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				" from t_cash_gift cg left join t_user u on cg.userId = u.id left join t_store s on cg.storeId = s.id ");
		sb.append(" where 1=1");
		String data = "cg.*,u.mobile as mobile , s.name as storeName ,(select pc.name from t_promotion_coupon pc where cg.couponId = pc.id ) as pcName";
		if (StringUtils.isNotBlank(mobile)) {
			sb.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}

		if (StringUtils.isNotBlank(shopName)) {
			sb.append(" and s.name like ?");
			parameters.add("%" + shopName + "%");
		}

		Integer total = jdbcTemplate.queryForObject(
				"select count(1) " + sb.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + sb.toString() + " order by id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(CashGift.class, list));

		return page;
	}

	/**
	 * 进行优惠卷的新增(默认设置优惠卷为未使用状态,创建时间为新增时)
	 * @param cashGift 优惠卷对象
	 * @return 是否新增成功(大于0,则新增成功)
	 */
	public int save(CashGift cashGift) {
		String sql = "insert into t_cash_gift(id,userId,amount,status,createdTime,memo,validTime,storeId,type,shared,orderAmount,couponId) "
				+ "values (? ,? ,? ,?, ? ,?,?,?,?,?,?,? )";
		
		int temp = jdbcTemplate.update(sql, cashGift.getId(),
				cashGift.getUserId(), cashGift.getAmount(),
				cashGift.getStatus(), new Date(), cashGift.getMemo(),
				cashGift.getValidTime(), cashGift.getStoreId(),
				cashGift.getType(),cashGift.getShared(),cashGift.getOrderAmount(),cashGift.getCouponId());
		return temp;
	}

	public String checkMobile(String mobile) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select id from t_user where mobile = ?", mobile);
		if (list.isEmpty()) {
			return "null";
		}
		return list.get(0).get("id") + "";
	}

	public int delete(Long id, Long userId) {
		int temp = jdbcTemplate.update(
				"delete from t_cash_gift  where id = ? and userId = ?", id,
				userId);
		logger.debug("delete : " + temp);
		return temp;
	}

	public void used(Long cashGiftId,  Long userId,Integer status) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("update t_cash_gift set status = ?,usedTime=? where id = ? ");
		if(ObjectUtils.equals(status, Constants.CashGift.STATUSUSED)){
			parameters.add(Constants.CashGift.STATUSUSED);			
			parameters.add(new Date());
		} else {
			parameters.add(Constants.CashGift.STATUSNORMAL);
			parameters.add(null);
		}
		parameters.add(cashGiftId);
		
		if(null != userId){
			sb.append(" and userId = ?");
			parameters.add(userId);
		}
		
		int temp = jdbcTemplate.update(sb.toString(),parameters.toArray());
		logger.debug("udpate Status is " + temp);
	}

	public Page findByUserId(Long userId,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer( "from t_cash_gift cg where cg.userId = ?");
		parameters.add(userId);
		
		String data = "cg.id,cg.type, cg.validTime,cg.status,cg.amount,"
				+ "(select mobile from t_user u where u.id = cg.userId) as mobile,"
				+ "(select name from t_store s where s.id = cg.storeId) as storeName ";

		Integer total = jdbcTemplate.queryForObject(
				"select count(1) " + sb.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + sb.toString() + " order by cg.`status`  asc ,cg.validTime asc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(CashGift.class, list));

		return page;
	}

	/**
	 * 根据优惠活动ID查询优惠卷的信息
	 * @param couponId 优惠活动ID
	 * @return 优惠卷集合或者null
	 */
	public List<CashGift> findByCouponId(Long couponId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(" select * from t_cash_gift where couponId = ?",couponId);
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(CashGift.class, list);
		}
		return null;
	}

	public CashGift findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_cash_gift where id = ? and status = ?",id, Constants.CashGift.STATUSNORMAL);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(CashGift.class, list.get(0));
		}
		return null;
	}

	/**
	 * 根据优惠卷ID查询优惠卷信息(有效,未使用,未过期)
	 * @param cashGiftId 优惠卷ID
	 * @return null或者优惠卷对象
	 */
	public CashGift checkCashGiftById(Long cashGiftId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_cash_gift where id = ? and status = ? and usedTime is null and validTime >= ?",cashGiftId, Constants.CashGift.STATUSNORMAL,new Date());
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(CashGift.class, list.get(0));
		}
		return null;
	}

	/**
	 * 根据用户ID和商家ID以及优惠活动ID查询是否存在优惠卷
	 * @param userId 用户ID
	 * @param storeId 商家ID
	 * @param couponId 优惠活动ID
	 * @return 优惠卷对象或者null
	 */
	public CashGift checkByUserId$StoreId$couponId(Long userId, Long storeId, Long couponId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_cash_gift where userId = ? and storeId = ? and couponId= ?",userId, storeId,couponId);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(CashGift.class, list.get(0));
		}
		return null;
	}
}