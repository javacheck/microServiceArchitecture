package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class RoomDao {
	private final static Logger logger = LoggerFactory
			.getLogger(RoomDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save (Room room){
		JdbcUtils.save(room);
	}

	public void update(Room room) {
		String sql = "update t_movie_room set storeId=?"
				+ ",name=?,number=?,categoryId=?,persons=?,basePrice=?,baseUserPrice = ?"
				+ ",basePriceUnit=?,memo=?"
				+ "where id = ?";
		jdbcTemplate.update(sql,room.getStoreId()
				,room.getName(),room.getNumber(),room.getCategoryId(),room.getPersons(),room.getBasePrice(),room.getBaseUserPrice()
				,room.getBasePriceUnit(),room.getMemo()
				,room.getId());
	}

	public Page list(Long storeId,String number, Integer status, Long categoryId, Page page) {
		logger.debug("storeId is {},number is {},status is {},categoryId is {}",storeId,number,status,categoryId);
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_movie_room mr where 1 = 1  ");
		if (status!=null&&!status.equals(-1)) {
			sql.append(" and status= ? ");
			parameters.add(status);
		}
		if (storeId!=null&&storeId.longValue()!=-1L) {
			sql.append(" and storeId= ? ");
			parameters.add(storeId);
		}
		if (categoryId!=null&&categoryId.longValue()!=-1L) {
			sql.append(" and categoryId= ? ");
			parameters.add(categoryId);
		}
		if (StringUtils.isNotBlank(number)) {//
			sql.append(" and number like ? ");
			parameters.add("%" + number + "%");
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
						"SELECT mr.* "
						+ ",(select name from t_store s where s.id= mr.storeId) as storeName"
						+ ",(select name from t_movie_room_category mrs where mrs.id= mr.categoryId) as categoryName"
						+ sql + "  order by createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(Room.class, list));

		return page;
	}

	public boolean checkCategoryIsUse(Long categoryId) {
		String sql = "select count(1) from t_movie_room mr where mr.categoryId = ? ";
		return jdbcTemplate.queryForObject(sql,Integer.class, categoryId)>=1;
	}

	public Room findById(Long id) {
		return JdbcUtils.findById(Room.class, id);
	}
	public void changStatus(Long id, Integer status) {
		String sql = "update t_movie_room set status=? "
				+ " where id = ?";
		jdbcTemplate.update(sql,status,id);
	}
	
	public boolean checkNameRepeat(Long id, Long storeId, String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer(" from t_movie_room mr where 1 = 1 and mr.name  = ? ");
		parameters.add(name);
		if (id!=null) {
			sf.append(" and mr.id != ?");
			parameters.add(id);
		}
		if (storeId!=null) {
			sf.append(" and mr.storeId = ?");
			parameters.add(storeId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sf.toString(),
				Integer.class, parameters.toArray());
		return total==0;
	}

	public boolean checkNumberRepeat(Long id, Long storeId, String number) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sf = new StringBuffer(" from t_movie_room mr where 1 = 1 and mr.number  = ? ");
		parameters.add(number);
		if (id!=null) {
			sf.append(" and mr.id != ? ");
			parameters.add(id);
		}
		if (storeId!=null) {
			sf.append(" and mr.storeId = ?");
			parameters.add(storeId);
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sf.toString(),
				Integer.class, parameters.toArray());
		return total==0;
	}

	public void updateIsRemind(Long id, Integer isRemind) {
		String sql = "update t_movie_room set isRemind = ? where id = ?";
		jdbcTemplate.update(sql,isRemind,id);
		
	}
	
	public Integer countRemind(Long storeId, Integer isRemind) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(isRemind);
		parameters.add(storeId);
		String sql = " SELECT COUNT(1) FROM t_movie_room mr WHERE mr.isRemind = ? and  storeId = ? ";
		return  jdbcTemplate.queryForObject(sql,Integer.class, parameters.toArray());
	}
	
}
