package cn.lastmiles.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.movie.RoomTimeEndRemind;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class RoomTimeEndRemindDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final static Logger logger = LoggerFactory
			.getLogger(RoomTimeEndRemindDao.class);
	
	public void save(RoomTimeEndRemind roomTimeEndRemind){
		JdbcUtils.save(roomTimeEndRemind);
	}
	public void deleteByStoreId(Long storeId){
		logger.debug("storeId is :{}",storeId);
		String sql = "delete from t_room_timeend_remind where storeId = ?";
		jdbcTemplate.update(sql,storeId);
	}
	public void deleteById(Long id){
		String sql = "delete from t_room_timeend_remind where id = ?";
		jdbcTemplate.update(sql,id);
	}
	public List<RoomTimeEndRemind> findByStoreId(Long storeId) {
		String sql = "select * from t_room_timeend_remind where storeId = ?";
		return BeanUtils.toList(RoomTimeEndRemind.class, jdbcTemplate.queryForList(sql,storeId));
	}

}
