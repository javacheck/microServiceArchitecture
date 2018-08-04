package cn.lastmiles.dao;
/**
 * createDate : 2016年2月18日下午2:53:48
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import cn.lastmiles.bean.Employee;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class EmployeeDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public int checkEmployeeName(Long id, String name) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("name", name);
		return JdbcUtils.queryForObject("employee.checkEmployeeName",parameters,Integer.class);
	}

	public Page findByName(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select * from t_employee where 1=1");
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and instr(name,?)>0 ");
			parameters.add(name);			
		}
		return JdbcUtils.selectMysql(jdbcTemplate, page, querySQL.toString(), parameters, "id desc", Employee.class);
	}

	public void save(Employee employee) {
		JdbcUtils.save(employee);
	}

	public int update(Employee employee) {
		return JdbcUtils.update(employee);
	}

	public Employee findById(Long id) {
		return JdbcUtils.findById(Employee.class, id);
	}

	public int deleteById(Long id) {
		return JdbcUtils.deleteById(Employee.class, id);
	}

}