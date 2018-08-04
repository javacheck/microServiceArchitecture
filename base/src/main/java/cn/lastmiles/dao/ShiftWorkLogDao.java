package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ShiftWorkLog;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class ShiftWorkLogDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(ShiftWorkLog shiftWorkLog) {
		String sql = "INSERT INTO t_shift_work_log(id,beginTime,endTime,accountId,storeId) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, shiftWorkLog.getId(),
				shiftWorkLog.getBeginTime(), shiftWorkLog.getEndTime(),
				shiftWorkLog.getAccountId(),shiftWorkLog.getStoreId()
				);
	}

	public Page list(String beginTime, String endTime, String name,	String mobile, Page page, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("  FROM t_shift_work_log sfl LEFT JOIN t_account a ON a.id = sfl.accountId WHERE 	1 = 1 ");
		
		if (storeId!=null&& !storeId.equals(Constants.Status.SELECT_ALL)) { //店铺唯一筛选
			sql.append(" AND sfl.storeId = ?");
			parameters.add(storeId);
			
		}
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and a.name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and sfl.endTime >= ?");
			parameters.add(beginTime+" 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and sfl.endTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		Integer total = jdbcTemplate.queryForObject(" select count(1) " + sql ,Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT	sfl.*,a.name AS 'accountName',a.mobile  "+ sql + "  order by endTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(ShiftWorkLog.class, list));
		return page;
	}

}
