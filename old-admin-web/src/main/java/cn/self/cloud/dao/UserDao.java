package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.User;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 用户分页列表
	 * 
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(String mobile, Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder(" from t_user where 1 = 1 ");

		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and mobile like ?");
			parameters.add("%" + mobile + "%");
		}

		if (storeId != null) {
			sql.append(" and storeId = ? ");
			parameters.add(storeId);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select id,mobile,name,createdTime " + sql
						+ " order by id limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(User.class, list));

		return page;
	}

	/**
	 * 保存
	 * 
	 * @param user
	 * @return
	 */
	public boolean save(User user) {
		int temp = jdbcTemplate
				.update("insert into t_user(id,mobile,name,discount,createdTime,storeId,createdId) values(?,?,?,?,now(),?,?)",
						user.getId(), user.getMobile(), user.getName(),
						user.getDiscount(), user.getStoreId(),
						user.getCreatedId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据电话查找
	 * 
	 * @param mobile
	 * @return
	 */
	public User findByMobile(String mobile) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,mobile,name,discount,storeId from t_user where mobile = ?",
						mobile);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	/**
	 * 根据storeId更改折扣
	 * 
	 * @param discount
	 * @return
	 */
	public boolean updateDiscount(Long storeId, Double discount) {
		int temp = jdbcTemplate.update(
				"update t_user set discount=? where storeId=?", discount,
				storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		int temp = jdbcTemplate.update(
				"update t_user set name = ? , mobile=? where id = ? ",
				user.getName(), user.getMobile(), user.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	public User findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select id,mobile,name,discount from t_user where id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		int temp = jdbcTemplate.update("DELETE FROM t_user WHERE id = ?", id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public User findByMobileAndStoreId(String mobile, Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select *  from t_user where mobile = ? and storeId = ?",mobile,storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}
}