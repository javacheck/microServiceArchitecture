/**
 * createDate : 2016年4月8日上午10:25:25
 */
package cn.lastmiles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class RoomDateSettingDao {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomDateSettingDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<DateSetting> findByStoreId(Long storeId,Integer type) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select mds.* from t_movie_date_setting mds where 1=1 ");
		if( null != storeId ){
			querySQL.append(" and mds.storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != type ){
			querySQL.append(" and mds.type = ? ");
			parameters.add(type);
		}
			
		List<Map<String,Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		return BeanUtils.toList(DateSetting.class, list);
	}

	public boolean save(List<Object[]> batchInsertArgs) {
		String sql = "insert into t_movie_date_setting(id,storeId,type,startTime,endTime,name) values(?,?,?,?,?,?)";
		int[] result = jdbcTemplate.batchUpdate(sql,batchInsertArgs) ;
		if( result.length == batchInsertArgs.size() ){
			return true;
		}
		return false;
	}

	public boolean update(List<Object[]> batchUpdateArgs) {
		String sql = "update t_movie_date_setting set storeId = ? ,type = ? ,startTime = ? ,endTime = ? ,name = ? where id = ? ";
		int[] result = jdbcTemplate.batchUpdate(sql,batchUpdateArgs) ;
		if( result.length == batchUpdateArgs.size() ){
			return true;
		}
		return false;
	}

	public int deleteByIdAndStoreId(Long storeId, Long id) {
		String sql = "delete from t_movie_date_setting where storeId = ? and id = ? ";
		return jdbcTemplate.update(sql,storeId,id);
	}

	public boolean saveHoliday(List<Object[]> batchInsertArgs,Long storeId) {
		
		deleteByStoreIdAndType(storeId,Constants.RoomDateSetting.TYPE_HOLIDAYS);
			String sql = "insert into t_movie_date_setting(id,storeId,type,holiday) values(?,?,?,?)";
			int[] result = jdbcTemplate.batchUpdate(sql,batchInsertArgs) ;
			if( result.length == batchInsertArgs.size() ){
				return true;
			}			
		return false;
	}

	public int deleteByStoreIdAndType(Long storeId,Integer type) {
		String deleteSQL = "delete from t_movie_date_setting where storeId = ? and type = ? ";
		int z = jdbcTemplate.update(deleteSQL,storeId,type);
		return z;
	}
	/**
	 * 查找可预订时间
	 * @param storeId
	 * @param categoryId 
	 * @param type
	 * @return
	 */
	public List<DateSetting> findCanBooingDate(Date date,Long roomId,Long storeId, Long categoryId, Integer type) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from  t_movie_room_category_date_setting mrcd  LEFT JOIN t_movie_date_setting mds ON mds.id=mrcd.dateSettingId where 1=1 ");
		if (storeId!=null&&storeId.longValue()!=-1L) {
			sql.append(" and mds.storeId = ? ");
			parameters.add(storeId);
		}
		if (type!=null&&type.intValue()!=-1) {
			sql.append(" and mds.type = ? ");
			parameters.add(type);
		}
		if (categoryId!=null&&categoryId.longValue()!=-1L) {
			sql.append(" and mrcd.categoryId = ? ");
			parameters.add(categoryId);
		}
		//查找未预约
		sql.append(" AND (SELECT count(1) FROM t_movie_room_booking mrb WHERE mrb.dateSettingId = mds.id and mrb.roomId= "+roomId+" and mrb.bookingDate = '"+DateUtils.format(date, "yyyy-MM-dd")+"' AND mrb.status="+Constants.RoomBooking.STATUS_BOOKING+") = 0 ");
		//判断是否过了今天的时间
		sql.append(" AND mds.endTime > ? ");
		parameters.add(new SimpleDateFormat("HH").format(new Date()));
		logger.debug("sql is :{},parameters is {}","select  mds.* "+sql,parameters);
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select  mds.* "+sql,parameters.toArray());
		return BeanUtils.toList(DateSetting.class, list);
	}

	public DateSetting findById(Long id) {
		return JdbcUtils.findById(DateSetting.class, id);
	}

	public List<DateSetting> findByTime(Date beginTime, Date endTime,Long storeId) {
		
		return null;
	}
	
}