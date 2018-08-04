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

import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class RoomCategoryDao {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomCategoryDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save (RoomCategory roomCategory){
		JdbcUtils.save(roomCategory);
	}
	public List<RoomCategory> findByStoreId (Long storeId){
		String sql  = "select * from t_movie_room_category where storeId = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,storeId);
		return BeanUtils.toList(RoomCategory.class, list);
	}

	public void update(RoomCategory roomCategory) {
		String sql = "update t_movie_room_category set name=? "
				+ ",storeId=?,updateAccountId=? ,updateDate=?,holidayPrice=? ,lowestPrice = ?"
				+ " where id = ?";
		jdbcTemplate.update(sql,roomCategory.getName(),roomCategory.getStoreId()
				,roomCategory.getUpdateAccountId(),new Date(),roomCategory.getHolidayPrice(),roomCategory.getLowestPrice()
				,roomCategory.getId());
	}

	public Page list(Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_movie_room_category mrc where 1 = 1  ");
		if (storeId!=null&&!storeId.equals(-1)) {
			sql.append(" and mrc.storeId= ?");
			parameters.add(storeId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);
		
		if (total.intValue() == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		logger.debug("list  sql is {} parameters is {}",sql.toString(),parameters);
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT mrc.* "
						+ ",(select a.mobile from t_account a where a.id= mrc.createAccountId) as createAccountName"
						+ ",(select a.mobile from t_account a where a.id= mrc.updateAccountId) as updateAccountName"
						+ sql + "  order by id desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(RoomCategory.class, list));
		return page;
	}
	public void delete(Long id) {
		String sql = "delete from t_movie_room_category where id = ?";
		jdbcTemplate.update(sql,id);
		sql = "delete from t_movie_room_category_date_setting where categoryId = ?";
		jdbcTemplate.update(sql,id);
	}
	public RoomCategory findById(Long id) {
		return JdbcUtils.findById(RoomCategory.class, id);
	}
	public boolean checkName(Long id, String name,Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer(" from t_movie_room_category mrc where 1 = 1 and mrc.name  = ? ");
		parameters.add(name);
		if (id!=null) {
			sf.append(" and mrc.id != ? ");
			parameters.add(id);
		}
		if (storeId!=null) {
			sf.append(" and mrc.storeId = ?");
			parameters.add(storeId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sf.toString(),
				Integer.class, parameters.toArray());
		return total==0;
	}
	
	public RoomCategory findByRoomId(Long roomId){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_movie_room_category where id = (SELECT categoryId from t_movie_room where id = ?)", roomId);
		if (!list.isEmpty()){
			return BeanUtils.toBean(RoomCategory.class, list.get(0));
		}
		return null;
	}
}
