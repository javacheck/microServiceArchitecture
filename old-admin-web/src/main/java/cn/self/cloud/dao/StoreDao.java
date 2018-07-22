package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Store;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StoreDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存方法
	 * 
	 * @param store
	 */
	public void save(Store store) {
		jdbcTemplate
				.update("insert into t_store(id,name,parentId,path,accountId,createdTime) values(?,?,?,?,?,?)",
						store.getId(), store.getName(), store.getParentId(),
						store.getPath(), store.getAccountId(),
						store.getCreatedTime());
	}

	/**
	 * 查询所有商品分类
	 * 
	 * @return
	 */
	public Page findAll(String name, Page page, Long accountId) {
		List<Object> parameters = new ArrayList<Object>();

		String where = " where 1 = 1 ";
		if (StringUtils.isNotBlank(name)) {
			where += " and name like ? ";
			parameters.add("%" + name + "%");
		}

		if (accountId != null) {
			where += " and accountId = ? ";
			parameters.add(accountId);
		}

		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_store " + where, Integer.class,
				parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select id,name,accountId,createdTime from t_store t ");
		sql.append(where + " order by id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	/**
	 * 通过 父类ID 查询
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Store> findByParentId(Long parentId) {
		StringBuffer sql = new StringBuffer(
				"select id,name,parentId ,(SELECT name FROM t_store tpc WHERE  t.parentId=tpc.id ) AS parentName from t_store t where 1 = 1 ");
		List<Map<String, Object>> list = null;
		if (parentId != null) {
			sql.append(" and parentId = ? order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString(), parentId);
		} else {
			sql.append(" and parentId is null  order by id desc ");
			list = jdbcTemplate.queryForList(sql.toString());
		}

		return BeanUtils.toList(Store.class, list);
	}

	/**
	 * 通过ID 修改数据
	 * 
	 * @param store
	 */
	public void updateByID(Store store) {
		jdbcTemplate.update("update t_store set name = ?  where id = ?",
				store.getName(), store.getId());
	}

	/**
	 * 通过ID查询数据
	 * 
	 * @param id
	 * @return
	 */
	public Store findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId ,(SELECT name FROM t_store tpc WHERE  t.parentId=tpc.id ) AS parentName from t_store t where id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public void delete(Long id) {
		jdbcTemplate
				.update("DELETE FROM  t_store  where id = ? or path like ? or path like ?",
						id, "%-" + id + "-%", id + "-%");
	}

	/**
	 * 我创建的，或者我属于的商店
	 * 
	 * @param accountId
	 * @param storeId
	 * @return
	 */
	public List<Store> findMyStore(Long accountId, Long storeId) {
		return BeanUtils
				.toList(Store.class,
						jdbcTemplate
								.queryForList(
										"select id,name,path,parentId from t_store where id = ? or accountId = ?",
										storeId, accountId));
	}

	public List<Store> findAll() {
		return BeanUtils.toList(Store.class, jdbcTemplate
				.queryForList("select id,name,path,parentId from t_store"));
	}

	public Store findByProduct(Long productId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId ,(SELECT name FROM t_store tpc WHERE  t.parentId=tpc.id ) AS parentName from t_store t where productId = ?",
						productId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public List<Store> findByAccount(Long accountId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,path,parentId from t_store where accountId = ?",accountId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toList(Store.class, list);
	}
}
