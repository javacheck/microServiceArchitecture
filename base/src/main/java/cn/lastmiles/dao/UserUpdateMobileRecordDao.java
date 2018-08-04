package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.UserUpdateMobileRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class UserUpdateMobileRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page findAll(String storeIdString, String oldMobile, String newMobile,
			String accountName, String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and uumr.storeId in ("+storeIdString+")");
		}
		if (StringUtils.isNotBlank(oldMobile)) {
			and.append(" and uumr.oldMobile like ?");
			parameters.add("%" + oldMobile + "%");
		}
		if (StringUtils.isNotBlank(newMobile)) {
			and.append(" and uumr.newMobile like ?");
			parameters.add("%" + newMobile + "%");
		}
		if (StringUtils.isNotBlank(accountName)) {
			and.append(" and a.mobile = ?");
			parameters.add(accountName);
		}
		if (StringUtils.isNotBlank(beginTime)) {
			and.append(" and uumr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			and.append(" and uumr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_user_update_mobile_record uumr left join t_account a on uumr.accountId=a.id where 1=1 " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select uumr.*,a.mobile as accountName,(select name from t_store s where s.id=uumr.accountStoreId) as accountStoreName from t_user_update_mobile_record uumr left join t_account a on uumr.accountId=a.id where 1=1 ");
				
		sql.append(and.toString() + " order by uumr.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(UserUpdateMobileRecord.class, list));
		
		return page;
	}
}
