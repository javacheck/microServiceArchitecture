/**
 * createDate : 2016年4月14日下午5:20:33
 */
package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.RoomOpen;
import cn.lastmiles.bean.movie.RoomOpenDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class RoomOpenRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 根据订单id查找详情
	 * @param orderId
	 * @return
	 */
	public List<RoomOpenDetail> findDetailByOrderId(Long orderId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuilder querySQL = new StringBuilder(" select it.type, it.`name`,it.price ,it.amount as number,it.status,o.duration,o.actualPrice,o.refundPrice from t_order_item it inner join t_order o on o.id = it.orderId where it.orderId = ?  ");
		parameters.add(orderId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		return BeanUtils.toList(RoomOpenDetail.class, list);
	}
	
	/**
	 * 根据订单id查找
	 * @param orderId
	 * @return
	 */
	public RoomOpen findByOrderId(Long orderId){
		StringBuilder sql = new StringBuilder(" from t_order o INNER JOIN t_movie_room room on o.roomId = room.id ");
		sql.append(" INNER JOIN t_movie_room_category rcate on room.categoryId = rcate.id ");
		sql.append(" where o.id = ? ");
		sql.insert(0, " select o.createdTime as startTime,o.id as id,room.number as 'room.number' ,room.`name` as 'room.name',rcate.`name` as 'room.categoryName',room.persons as 'room.persons',o.duration,o.actualPrice as totalAmount ,o.refundPrice,o.price ");
		return BeanUtils.toBean(RoomOpen.class, jdbcTemplate.queryForMap(sql.toString(), orderId));
	}
	
	/**
	 * 查询订单表记录
	 * @param storeId
	 * @param number
	 * @param categoryId
	 * @param page
	 * @return
	 */
	public Page listFromOrder(Long storeId,Long number,Long categoryId,Page page){
		StringBuilder sql = new StringBuilder(" from t_order o INNER JOIN t_movie_room room on o.roomId = room.id ");
		sql.append(" INNER JOIN t_movie_room_category rcate on room.categoryId = rcate.id ");
		sql.append(" where o.storeId = ? ");
		
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(storeId);
		
		if (number != null){
			sql.append(" and room.number = ? ");
			parameters.add(number);
		}
		
		if (categoryId != null){
			sql.append(" and room.categoryId  = ? ");
			parameters.add(categoryId);
		}
		
		Integer total = jdbcTemplate.queryForObject( "select count(1) " + sql.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		sql.append(" order by o.id desc limit ?,? ");
		sql.insert(0, " select o.createdTime as startTime, o.id as id,room.number as 'room.number' ,room.`name` as 'room.name',rcate.`name` as 'room.categoryName',room.persons as 'room.persons',o.duration,o.actualPrice as totalAmount  ");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList( sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(RoomOpen.class, list));
		return page;
	}

	public Page list(String storeIdString, Long number, Integer status, Long categoryId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_movie_room_open mro,t_movie_room mr where mro.roomId = mr.id ");
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and mro.storeId = ? ");
			parameters.add(storeIdString);
		}
		
		if( null != number ){
			querySQL.append(" and mr.number = ? ");
			parameters.add(number);
		}
		
		if( null != status ){
			//querySQL.append(" and mro");
		}
		
		if( null != categoryId ){
			querySQL.append(" and mr.categoryId = ? ");
			parameters.add(categoryId);
		}
		
		Integer total = jdbcTemplate.queryForObject( "select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, "select mro.*,mr.number as 'room.number', mr.persons as 'room.persons',mr.name as 'room.name', (select mrc.name from t_movie_room_category mrc where mrc.id = mr.categoryId ) as 'room.categoryName', (select s.name from t_store s where s.id = mr.storeId ) as 'room.storeName' ");
		querySQL.append(" order by mro.id desc limit ?,? ");

		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(RoomOpen.class, list));
		
		return page;
	}

	public Page list(String storeIdString, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_movie_room_open mro,t_movie_room mr where mro.roomId = mr.id ");
		
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and mro.storeId = ? ");
			parameters.add(storeIdString);
		}
		
		Integer total = jdbcTemplate.queryForObject( "select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		querySQL.insert(0, "select mro.*,mr.number as 'room.number', mr.persons as 'room.persons',mr.name as 'room.name', (select mrc.name from t_movie_room_category mrc where mrc.id = mr.categoryId ) as 'room.categoryName', (select s.name from t_store s where s.id = mr.storeId ) as 'room.storeName' ");
		querySQL.append(" order by mro.id desc limit ?,? ");

		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(RoomOpen.class, list));
		
		return page;
	}

	public RoomOpen findById(Long id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_movie_room_open mro,t_movie_room mr where mro.roomId = mr.id and mro.id = ? ");
		parameters.add(id);
		querySQL.insert(0, "select mro.*,mr.number as 'room.number', mr.persons as 'room.persons',mr.name as 'room.name', (select mrc.name from t_movie_room_category mrc where mrc.id = mr.categoryId ) as 'room.categoryName', (select s.name from t_store s where s.id = mr.storeId ) as 'room.storeName' ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		if( null != list && list.size() >0 ){
			return BeanUtils.toBean(RoomOpen.class, list.get(0));
		}
		return null;
	}

	public List<RoomOpenDetail> findByRoomOpenId(Long id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_movie_room_open_detail mrod where roomOpenId = ?  ");
		parameters.add(id);
		querySQL.insert(0, "select mrod.* ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList( querySQL.toString(), parameters.toArray());
		if( null != list && list.size() >0 ){
			return BeanUtils.toList(RoomOpenDetail.class, list);
		}
		return null;
	}

	public boolean checkWhetherUsedTime(Long storeId, Long roomId, String reserveDate, String reserveDurationDate) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select count(*) from t_movie_room_open mro where mro.storeId = ? and mro.roomId = ? ");
		parameters.add(storeId);
		parameters.add(roomId);
		querySQL.append(" and ( ");
		
		// 全包围
		querySQL.append(" ( startTime > ? ");
		parameters.add(reserveDate);
		querySQL.append(" and endTime < ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" or ");
		
		// 全包含
		querySQL.append(" ( startTime < ? ");
		parameters.add(reserveDate);
		querySQL.append(" and endTime > ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" or ");
		
		// 前半包含
		querySQL.append(" ( startTime < ? ");
		parameters.add(reserveDate);
		querySQL.append(" and endTime > ? )");
		parameters.add(reserveDate);
		
		querySQL.append(" or ");
		
		// 后半包含
		querySQL.append(" ( startTime < ? ");
		parameters.add(reserveDurationDate);
		querySQL.append(" and endTime > ? )");
		parameters.add(reserveDurationDate);
		
		querySQL.append(" ) ");
		Integer total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class, parameters.toArray());
		if( total.intValue() > 0 ){
			return true;
		}
		return false;
	}
	
}