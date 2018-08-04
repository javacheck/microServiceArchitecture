package cn.lastmiles.controller;
/**
 * createDate : 2016年2月18日下午2:15:45
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.lastmiles.bean.Employee;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.EmployeeService;
import cn.lastmiles.utils.SecurityUtils;

/**
 * *员工管理(列表/新增/修改/删除)
 */
@Controller
@RequestMapping("employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService; // 员工对象
	
	/**
	 * 信息列表
	 * @return 页面跳转
	 */
	@RequestMapping("list")
	public String list(){
		return "employee/list";
	}
	
	/**
	 * 信息详情列表
	 * @param name 查询名称
	 * @param page 页码对象
	 * @param model 存储模型
	 * @return 页面跳转
	 */
	@RequestMapping("list-data")
	public String list_data(String name,Page page,Model model){
		model.addAttribute("employee",employeeService.findByName(name,page));
		return "employee/list-data";
	}
	
	/**
	 * 新增信息
	 * @return 新增跳转
	 */
	@RequestMapping( value = "add" , method = RequestMethod.GET )
	public String toAdd(){
		return "employee/add";
	}
	
	/**
	 * 修改信息
	 * @param id 判断标识
	 * @param model 存储模型
	 * @return 修改跳转
	 */
	@RequestMapping( value = "update/{id}" , method = RequestMethod.GET )
	public String toUpdate(@PathVariable Long id,Model model){
		model.addAttribute("employee",employeeService.findById(id));
		return "employee/add";
	}
	
	/**
	 * 异步检测字段唯一性
	 * @param id 根据id来判断(新增/修改)区分自身的标识
	 * @param name 判断标识
	 * @return 是否修改成功
	 */
	@RequestMapping( value = "list/ajax/checkEmployeeName" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int checkEmployeeName(Long id,String name){
		return employeeService.checkEmployeeName(id,name);
	}
	
	/**
	 * (新增/修改)信息
	 * @param employee 保存对象
	 * @return (新增/修改)跳转
	 */
	@RequestMapping( value = "save" , method = RequestMethod.POST )
	public String save(Employee employee){
		employee.setStoreId(SecurityUtils.getAccountStoreId());
		if( null == employee.getId() ){ // 新增
			employeeService.save(employee);
		} else { // 修改
			employeeService.update(employee);
		}
		return "redirect:/employee/list";
	}
	
	/**
	 * 删除信息
	 * @param id 判断标识
	 * @return 删除跳转
	 */
	@RequestMapping( value = "delete/deleteById" , produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public int deleteById(Long id){
		return employeeService.deleteById(id);
	}
}