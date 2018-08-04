package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.types.resources.selectors.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.AcitivityStatistics;
import cn.lastmiles.bean.ActivityPartake;
import cn.lastmiles.bean.ActivityResult;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ActivityResultDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static Logger logger = LoggerFactory.getLogger(ActivityResultDao.class);

	public void save(ActivityResult activityResult) {
		String sql = "insert into t_activity_result(id,locationId,userId,createdTime,status,accountId,imageId,partakeId) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, activityResult.getId(),
				activityResult.getLocationId(), activityResult.getUserId(),
				new Date(), activityResult.getStatus(),
				activityResult.getAccountId(), activityResult.getImageId(),
				activityResult.getPartakeId());
	}

	/**
	 * 用活动详情编码和用户ID来确认此用户的列表数据
	 * @param userId
	 * @param activityDetailId
	 * @return
	 */
	public List<Map<String, Object>> findByUserId(Long userId,Long activityDetailId) {
		 String sql = " select "
				    + "if(adl.id is null,'-10',adl.id) as 'getId'," // ID,如果是null则证明是此条记录是统计总时的记录
				    + "adl.name as 'getName'," // 名称
				    + "ar.createdTime as 'getSubmitTime'," // 提交时间
				    + "ap.createdTime as 'getpullTime'," // 领取时间
				    + "ad.placeName as 'getAddress' ," // 区域地址
				    + "SUM(TIMESTAMPDIFF(SECOND,ap.createdTime,ar.createdTime)) as 'getTime'" // 提交时间和领取时间的时间差
		 			+ " from t_activity_result ar"
					+ " left join t_activity_detail_location adl on ar.locationId = adl.id"
		 			+ " left join t_activity_detail ad on adl.activityDetailId = ad.id"
					+ " left join t_activity_partake ap on ar.partakeId = ap.id"
					+ " where ar.userId = ? and adl.activityDetailId = ?"
					+ " GROUP BY adl.id"
					+ " WITH ROLLUP";
		 logger.debug("querySQL = {} , usreId = {}, activityDetailId = {}",sql,userId,activityDetailId);
		 List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,userId,activityDetailId);
			
		return list;
	}

	/**
	 * 先得到用户的所有的活动记录个数
	 * @param userId
	 * @return
	 */
	public List<ActivityPartake> findByUserIdGetDetialId(Long userId) {
		String sql = " select activityDetailId from t_activity_result ar "
				   + " left join t_activity_partake ap on ar.partakeId = ap.id "
				   + " where ar.userId = ?"
				   + " group by activityDetailId "
				   + " order by ar.id ";
		 logger.debug("querySQL = {} , usreId = {}",sql,userId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,userId);
		return  BeanUtils.toList(ActivityPartake.class, list);
	}
	
	/**
	 * 用于统计
	 * @return
	 */
	public List<Map<String, Object>> GetActivityStatistics() {
		String sql =" select adl.id, u.`name`, ar.userId , u.mobile,"
				+ " count(adl.activityDetailId) as allPoint,"
				+ " TIMESTAMPDIFF( SECOND, ap.createdTime, max(ar.createdTime)) as 'getTime' " // 提交时间和领取时间的时间差
				+ " from t_activity_result ar "
				+ " left join t_activity_detail_location adl on ar.locationId = adl.id "
				+ " left join t_activity_detail ad on adl.activityDetailId = ad.id "
				+ " left join t_activity_partake ap on ar.partakeId = ap.id "
				+ " left join t_user u on ar.userId = u.id "
				+ " where ar.status = '1'"
				+ " group by ar.userId "
				+ " order by allPoint desc,getTime asc";
		 logger.debug("querySQL = {}",sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	
	@Transactional
	public void saveStatistics(List<Map<String, Object>> listMap){
		String truncateTable = "truncate table t_activity_statistics";
		jdbcTemplate.execute(truncateTable);
		
		String sql = "insert into t_activity_statistics(id,userId,mobile,allTreasurePoint,time,Ranklist) values(?,?,?,?,?,?)";
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		int i = 0;
		for (Map<String, Object> map : listMap) {
			Object[] arg = new Object[6];
			i++;
			arg[0] = i;
			arg[1] = map.get("userId");
			arg[2] = map.get("mobile");
			arg[3] = map.get("allPoint");
			
			Long getTime =Long.parseLong(map.get("getTime")+"");
			long iHour=getTime/3600;
			long iMin=(getTime-iHour*3600)/60;
			long iSen=getTime-iHour*3600-iMin*60;
			
			arg[4] = iHour == 0 ? (iMin == 0 ? iSen+"秒" :iMin+"分"+iSen+"秒"):iHour+"小时"+iMin+"分"+iSen+"秒";
			arg[5] = i; 
			batchArgs.add(arg);
		}

		jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	public List<AcitivityStatistics> getStatistics() {
		String sql = "select * from t_activity_statistics where 1=1 order by id";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql);
		return BeanUtils.toList(AcitivityStatistics.class, list);
	}

	public AcitivityStatistics getStatistics(Long userId) {
		String sql = "select * from t_activity_statistics where userId = ? order by id";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,userId);
		return BeanUtils.toBean(AcitivityStatistics.class, list.get(0));
	}

	public Page getAuditInfo(String mobile, Integer status, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" from t_activity_result ar ");
		sql.append(" left join t_activity_partake ap on ap.id = ar.partakeId ");
		sql.append(" left join t_user u on ar.userId = u.id ");
		sql.append(" where 1=1 ");
		
		String data = "ar.userId,ar.id, ar.createdTime as upLoadTime , ap.createdTime as participationTime , ar.`status` , u.mobile ,u.`name`,"
				+ " (select a.name from t_account a where a.id = ar.accountId) as accountName";
		if(StringUtils.isNotBlank(mobile)){
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		
		if( null != status ){
			sql.append(" and ar.status = ?");
			parameters.add(status);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + sql.toString() + " order by ar.userId limit ?,?", parameters.toArray());
		page.setData(list);

		return page;
	}

	public boolean updateStatus(Long id, Long userId, Integer status) {
		int update = jdbcTemplate.update(" update t_activity_result set status = ? where userId = ? and id = ? ",status,userId,id);
		if(update > 0){
			return true;
		}
		return false;
	}

	public List<Map<String, Object>> findByIdAndUserId(Long id, Long userId) {
		String sql = "select ar.id,ar.userId,ar.createdTime as dateTime,adl.name ,adl.shopName,ad.placeName,ar.imageId"
				+ " from t_activity_result ar "
				+ " left join t_activity_detail_location adl on adl.id = ar.locationId "
				+ " left join t_activity_detail ad on ad.id = adl.activityDetailId "
				+ " where ar.userId = ? and ar.id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId,id);
		return list;
	}
}