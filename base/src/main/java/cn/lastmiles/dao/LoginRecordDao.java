package cn.lastmiles.dao;

/**
 * createDate : 2015年7月28日 下午2:14:35 
 */

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.LoginRecord;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class LoginRecordDao {

	private final static Logger logger = LoggerFactory
			.getLogger(LoginRecordDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 新增登录记录
	 * 
	 * @param loginRecord
	 *            登录记录
	 * @return 是否新增成功
	 */
	public boolean save(LoginRecord loginRecord) {
		logger.debug(
				"save loginRecord INFO : token = {} , type = {} , ownereId = {} ",
				loginRecord.getToken(), loginRecord.getType(),
				loginRecord.getOwnerId());

		String insertSQL = "insert into t_login_record(token,type,ownerId,createTime) values(?,?,?,?)";
		int insertRow = jdbcTemplate.update(insertSQL, loginRecord.getToken(),
				loginRecord.getType(), loginRecord.getOwnerId(),new Date());
		if (insertRow > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 新增登录记录
	 * 
	 * @param loginRecord
	 *            登录记录
	 * @return 是否修改成功
	 */
	public boolean update(LoginRecord loginRecord) {
		logger.debug(
				"update loginRecord INFO : token = {} , type = {} , ownereId = {} ",
				loginRecord.getToken(), loginRecord.getType(),
				loginRecord.getOwnerId());

		String updateSQL = "update t_login_record set token = ?,createTime = ? where ownerId = ? and type = ? ";
		int updateRow = jdbcTemplate.update(updateSQL, loginRecord.getToken(),new Date(),
				loginRecord.getOwnerId(), loginRecord.getType());
		if (updateRow > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除登录记录
	 * 
	 * @param loginRecord
	 *            登录记录
	 * @return 是否删除成功
	 */
	public boolean delete(LoginRecord loginRecord) {
		logger.debug(
				"delete loginRecord INFO : token = {} , type = {} , ownereId = {} ",
				loginRecord.getToken(), loginRecord.getType(),
				loginRecord.getOwnerId());

		String deleteSQL = "delete from t_login_record where token = ? and ownerId = ? and type = ? ";
		int deleteRow = jdbcTemplate.update(deleteSQL, loginRecord.getToken(),
				loginRecord.getOwnerId(), loginRecord.getType());
		if (deleteRow > 0) {
			return true;
		}
		return false;
	}

	public LoginRecord get(String token) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select token,type,ownerId from t_login_record where token = ?",
						token);
		if (list.isEmpty()){
			return null;
		}
		
		return BeanUtils.toBean(LoginRecord.class, list.get(0));
	}
}