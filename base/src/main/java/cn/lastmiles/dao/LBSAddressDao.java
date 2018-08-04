package cn.lastmiles.dao;

/**
 * createDate : 2015-07-17 PM 15:27
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cn.lastmiles.bean.LBSAddress;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class LBSAddressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询LBSAddress对象
	 * 
	 * @param page
	 * @param userId
	 *            用户ID
	 * @param name
	 *            名称
	 * @return Page对象
	 */
	public Page getLBSAddress(Page page, Long userId, String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();

		querySQL.append(" from t_lbs_address where userId = ? ");
		parameters.add(userId);

		if (null != userId) {
			querySQL.append(" and userId = ?");
			parameters.add(userId);
		}

		if (!StringUtils.isBlank(name)) {
			querySQL.append(" and name like ?");
			parameters.add("%" + name + "%");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select  * "
				+ querySQL.toString() + " order by userId desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(LBSAddress.class, list));
		return page;
	}

	/**
	 * 查询LBSAddress对象
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            ID
	 * @return LBSAddress集合或者null
	 */
	public List<LBSAddress> getLBSAddress(Long userId, Long id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				"select * from t_lbs_address where userId = ? ");
		parameters.add(userId);

		if (null != id) {
			querySQL.append(" and id = ? ");
			parameters.add(id);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				querySQL.toString(), parameters.toArray());

		return BeanUtils.toList(LBSAddress.class, list);
	}

	/**
	 * 保存LBSAddress对象
	 * 
	 * @param lBSAddress
	 *            地址记录对象
	 * @return 是否保存成功
	 */
	public Boolean saveLBSAddress(LBSAddress lBSAddress) {
		String sql = "insert into t_lbs_address(id,name,address,userId,lat,lng) values(?,?,?,?,?,?)";
		int updateRow = jdbcTemplate.update(sql, lBSAddress.getId(),
				lBSAddress.getName(), lBSAddress.getAddress(),
				lBSAddress.getUserId(), lBSAddress.getLat(),
				lBSAddress.getLng());
		if (updateRow > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 通过用户ID和地址记录ID删除LBSAddress
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            地址记录ID
	 * @return 是否删除成功
	 */
	public Boolean deleteLBSAddress(Long userId, Long id) {
		String sql = "delete from t_lbs_address where userId = ? and id = ? ";
		int updateRow = jdbcTemplate.update(sql, userId, id);
		if (updateRow > 0) {
			return true;
		}
		return false;
	}
}