package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.WorkRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class WorkRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page list(String accountName,String accountMobile,String startDate,String endDate,Long storeId,Page page){
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(storeId);
		
		StringBuilder sql = new StringBuilder(" from t_work_record r inner join t_account a on r.accountId = a.id where r.storeId = ?  and r.endDate is not null ");
		
		if (StringUtils.isNotBlank(accountName)){
			parameters.add("%" + accountName + "%");
			sql.append(" and a.name like ? ");
		}
		
		if (StringUtils.isNotBlank(accountMobile)){
			parameters.add("%" + accountMobile + "%");
			sql.append(" and a.mobile like ? ");
		}
		
		if (StringUtils.isNotBlank(startDate)){
			parameters.add(startDate + " 00:00");
			sql.append(" and r.startDate >= ? ");
		}
		
		if (StringUtils.isNotBlank(endDate)){
			parameters.add(endDate + " 23:59");
			sql.append(" and r.startDate <= ? ");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql, Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total.intValue() == 0){
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select r.*,a.name as accountName,a.mobile as accountMobile " + sql + " order by id desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(WorkRecord.class, list));
		return page;
	}

	public Long beginSave(WorkRecord workRecord) {
		JdbcUtils.save(workRecord);
		return workRecord.getId();
	}

	public void endUpdate(WorkRecord workRecord) {
		String sql = "update  t_work_record set endDate = ? ,sales= ? Where id = ?  ";
		jdbcTemplate.update(sql, workRecord.getEndDate(),
				workRecord.getSales(), workRecord.getId());
	}

	public WorkRecord findById(Long id) {
		String sql = "SELECT * FROM t_work_record WHERE id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(WorkRecord.class, list.get(0));
	}

	public WorkRecord findByToken(String token) {
		String sql = "SELECT * FROM t_work_record WHERE token = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, token);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(WorkRecord.class, list.get(0));
	}

	public WorkRecord findByAccountId(Long accountId) {
		String sql = "SELECT * FROM t_work_record WHERE accountId = ? and enddate IS NULL order by id desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				accountId);
		return list.isEmpty() ? null : BeanUtils.toBean(WorkRecord.class,
				list.get(0));
	}

	public void updateEndDate(Long id,Long accountId,Date endDate) {
		String sql = "update  t_work_record set endDate = ? Where id = ? and accountId = ? ";
		jdbcTemplate.update(sql,endDate,id,accountId);
	}
}
