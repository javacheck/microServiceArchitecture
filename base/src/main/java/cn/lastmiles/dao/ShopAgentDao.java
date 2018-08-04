package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Account;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ShopAgentDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(String mobile, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		
		if (StringUtils.isNotBlank(mobile)) {
			and.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		
		Integer total =jdbcTemplate.queryForObject(
				"select count(1) from t_account a where 1=1 and (a.type=2 or a.type=3)" + and.toString(), Integer.class, parameters.toArray());
		
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select a.id,a.mobile,a.type,a.createdTime, "
				+ " a.storeId,(select name from t_store s where s.id=a.storeId) as storeName,"
				+ " a.agentId,(select name from t_agent t where t.id=a.agentId) as agentName "
				+ " from t_account a  where 1=1 and (a.type=2 or a.type=3)");
		sql.append(and.toString() + " order by a.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		
		page.setData(BeanUtils.toList(Account.class, list));
		return page;
	}

	public void updateAccountPwd(Long id, String password) {
		if(password!=null){
			String sql = "update t_account set password = ? where id = ?";
			jdbcTemplate.update(sql, password,id);
		}
		
	}
}
