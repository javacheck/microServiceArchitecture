package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.bean.ReportType;
import cn.lastmiles.bean.UserReport;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class UserReportDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(String beginTime,String endTime,String typeId, String content, String contact,
			Long accountId, Page page) {
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_report r");
		
		sql.append(" WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(typeId)) {
			sql.append(" and r.typeId = ?");
			parameters.add(Integer.parseInt(typeId));
		}
		if (StringUtils.isNotBlank(content)) {
			sql.append(" and r.content like ?");
			parameters.add("%" + content + "%");
		}
		if (StringUtils.isNotBlank(contact)) {
			sql.append(" and r.contact like ?");
			parameters.add("%" + contact + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and r.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and r.createdTime <= ?");
			parameters.add(endTime+" 23:59:59");
		}
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT r.id,r.typeId,(select name from t_report_type t where t.id=r.typeId) as typeName,r.content,r.contact,r.userId,r.createdTime  "+ sql + " order by r.createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(UserReport.class, list));

		return page;
	}

	public List<ReportType> reportTypeList() {
		String sql="select id,name  from t_report_type order by id ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list.isEmpty()?null: BeanUtils.toList(ReportType.class, list);
	}

	public void editReport(UserReport userReport) {
		String sql="insert into t_report(id,typeId,content,contact,userId,createdTime) values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql,userReport.getId(),userReport.getTypeId(),userReport.getContent(),userReport.getContact(),userReport.getUserId(),new Date());
		
	}

	public void delByReportId(Long id) {
		String sql="delete from t_report where id=? ";
		jdbcTemplate.update(sql,id);
	}

	public UserReport findByReportId(Long id) {
		String sql="SELECT r.id,r.typeId,(select name from t_report_type t where t.id=r.typeId) as typeName,r.content,r.contact,r.userId,r.createdTime from t_report r where r.id=? ";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql,id);
		return list.isEmpty()?null: BeanUtils.toBean(UserReport.class, list.get(0));
	}
	
}
