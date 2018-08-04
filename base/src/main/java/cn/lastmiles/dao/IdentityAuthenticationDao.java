package cn.lastmiles.dao;

/**
 * createDate : 2015-06-27 PM 14:50
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.IdentifyType;
import cn.lastmiles.bean.User;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class IdentityAuthenticationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询身份证件类型
	 * 
	 * @param identifyTypeId
	 *            证件类型ID 可为空，(为空查询全表)
	 * @return List<IdentifyType> POJO集合对象 或 null
	 */
	public List<IdentifyType> getIdentifyType(Long identifyTypeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				"select identifyTypeId , identifyTypeName from t_identifyType where 1 = 1 ");

		if (null != identifyTypeId) {
			querySQL.append(" and identifyTypeId = ? ");
			parameters.add(identifyTypeId);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				querySQL.toString(), parameters.toArray());

		if (list.isEmpty()) {
			return null;
		}

		return BeanUtils.toList(IdentifyType.class, list);
	}

	/**
	 * 修改用户的认证信息
	 * 
	 * @param userId
	 *            用户ID not null
	 * @param realName
	 *            真实姓名 not null
	 * @param identifyTypeId
	 *            证件类型ID not null
	 * @param identity
	 *            证件号码 not null
	 * @return true 修改成功
	 */
	public Boolean updateIdentityAuthentication(Long userId, String realName,
			Long identifyTypeId, String identity) {
		/*
		 * 暂不考虑传入的证件类型ID不存在于证件表中的情况。如考虑。则放开注释代码即可
		 */
		// String querySQL =
		// "select identifyTypeId from t_identifyType where identifyTypeId = '"
		// + identifyTypeId + "'";
		// IdentifyType identifyType =
		// jdbcTemplate.queryForObject(querySQL,IdentifyType.class);
		// if( null == identifyType){
		// return false;
		// }
		String updateSQL = "update t_user set realName = ? , identifyTypeId = ? , identity = ?,idAudit = ?  where id = ?";
		int updateRow = jdbcTemplate.update(updateSQL, realName,
				identifyTypeId, identity,Constants.User.IDAUDIT_ING, userId);

		if (updateRow > 0) {
			return true;
		}

		return false;
	}

	public User getIdentity(Long userId) {
		String sql = "select u.realName,u.identity,u.identifyTypeId,t.identifyTypeName from t_user u left join t_identifyType t on u.identifyTypeId = t.identifyTypeId where id=?";
		return BeanUtils.toBean(User.class,
				jdbcTemplate.queryForMap(sql, userId));
	}
}