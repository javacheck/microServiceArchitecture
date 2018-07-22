package cn.self.cloud.dao;

import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.WorkRecord;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WorkRecordDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Long beginSave(WorkRecord workRecord) {
		String sql = "INSERT INTO t_work_record(id,accountId,storeId,startDate,token) VALUES (?,?,?,NOW(),?)";
		jdbcTemplate.update(sql, workRecord.getId(), workRecord.getAccountId(),
				workRecord.getStoreId(), workRecord.getToken());
		return workRecord.getId();
	}
	public void endUpdate(WorkRecord workRecord) {
		String sql = "update  t_work_record set endDate = ? ,sales= ? Where id = ?  ";
		jdbcTemplate.update(sql, workRecord.getEndDate(),workRecord.getSales(),workRecord.getId());
	}
	public WorkRecord findById(Long id) {
		String sql = "SELECT * FROM t_work_record WHERE id = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(WorkRecord.class, list.get(0));
	}
	public WorkRecord findByToken(String token) {
		String sql = "SELECT * FROM t_work_record WHERE token = ? and enddate IS NULL";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,token);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(WorkRecord.class, list.get(0));
	}
}
