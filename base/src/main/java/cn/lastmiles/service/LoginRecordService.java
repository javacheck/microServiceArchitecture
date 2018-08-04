package cn.lastmiles.service;
/**
 * createDate : 2015年7月28日 下午2:26:30 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.LoginRecord;
import cn.lastmiles.dao.LoginRecordDao;

@Service
public class LoginRecordService {
	@Autowired
	private LoginRecordDao loginRecordDao;
	
	/**
	 * 新增登录记录
	 * @param loginRecord 登录记录
	 * @return 是否新增成功
	 */
	public boolean save(LoginRecord loginRecord){
		return loginRecordDao.save(loginRecord);
	}
	
	/**
	 * 修改登录记录
	 * @param loginRecord 登录记录
	 * @return 是否修改成功
	 */
	public boolean update(LoginRecord loginRecord){
		return loginRecordDao.update(loginRecord);
	}
	
	/**
	 * 登陆时判断是否有记录。有记录则直接修改，无记录则进行新增
	 * @param loginRecord 登录记录
	 * @return 是否成功
	 */
	public boolean loginRecord(LoginRecord loginRecord){
		return update(loginRecord) ? true : save(loginRecord);
	}
	
	/**
	 * 退出时进行登录记录删除
	 * @param loginRecord 登录记录
	 * @return 是否删除成功
	 */
	public boolean delete_loginRecord(LoginRecord loginRecord){
		return loginRecordDao.delete(loginRecord);
	}
	
	public LoginRecord get(String token){
		return loginRecordDao.get(token);
	}
}