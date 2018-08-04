package cn.lastmiles.service;
/**
 * createDate : 2015-06-27 PM 14:50
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.IdentifyType;
import cn.lastmiles.bean.User;
import cn.lastmiles.dao.IdentityAuthenticationDao;

@Service
public class IdentityAuthenticationService {

	@Autowired
	private IdentityAuthenticationDao identityAuthenticationDao;
	
	/**
	 * 查询身份证件类型
	 * @param identifyTypeId 证件类型ID 可为空，(为空查询全表)
	 * @return List<IdentifyType> POJO集合对象 或 null
	 */
	public List<IdentifyType> getIdentifyType(Long identifyTypeId){
		return identityAuthenticationDao.getIdentifyType(identifyTypeId);
	}

	/**
	 * 修改用户的认证信息
	 * @param userId 用户ID not null
	 * @param realName 真实姓名 not null
	 * @param identifyTypeId 证件类型ID not null
	 * @param identity 证件号码 not null
	 * @return true 修改成功
	 */
	public Boolean updateIdentityAuthentication(Long userId, String realName, Long identifyTypeId, String identity) {
		return identityAuthenticationDao.updateIdentityAuthentication(userId,realName,identifyTypeId,identity);
	}
	
	public User getIdentity(Long userId) {
		return identityAuthenticationDao.getIdentity(userId);
	}
}