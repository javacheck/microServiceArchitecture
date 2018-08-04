package cn.lastmiles.service;
/**
 * createDate : 2016年2月18日下午2:51:34
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.Employee;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.EmployeeDao;

/**
 * *员工管理(列表/新增/修改/删除)
 */
@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao; // 数据查询对象
	@Autowired
	private IdService idService; // ID自动生成

	/**
	 * 根据名称查询数据
	 * @param name 查询标识
	 * @param page 页码对象
	 * @return 页码数据对象
	 */
	public Page findByName(String name, Page page) {
		return employeeDao.findByName(name,page);
	}

	/**
	 * 检测字段唯一性
	 * @param id 根据id来判断(新增/修改)区分自身的标识
	 * @param name 判断标识
	 * @return 是否修改成功(成功1、失败0)
	 */
	public int checkEmployeeName(Long id, String name) {
		return employeeDao.checkEmployeeName(id,name) > 0 ? 1 : 0 ;
	}
	
	/**
	 * 新增数据对象
	 * @param employee 对象信息
	 */
	public void save(Employee employee) {
		if( null == employee.getId() ){
			employee.setId(idService.getId());
		}
		employeeDao.save(employee);
	}

	/**
	 * 根据ID查询数据对象
	 * @param id 查询ID
	 * @return 数据对象
	 */
	public Employee findById(Long id) {
		return employeeDao.findById(id);
	}
	
	/**
	 * 修改信息
	 * @param employee 对象信息
	 * @return 是否修改成功(成功true、失败false)
	 */
	public boolean update(Employee employee) {
		return employeeDao.update(employee) > 0 ? true : false ;
	}

	/**
	 * 删除数据
	 * @param id 删除标识
	 * @return 是否删除成功(成功1、失败0)
	 */
	public int deleteById(Long id) {
		return employeeDao.deleteById(id) > 0 ? 1 : 0 ;
	}
	
}