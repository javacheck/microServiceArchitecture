package cn.lastmiles.service;
/**
 * createDate : 2015-07-17 PM 15:46
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lastmiles.bean.LBSAddress;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.dao.LBSAddressDao;

@Service
public class LBSAddressService {

	@Autowired
	private LBSAddressDao lBSAddressDao;
	
	/**
	 * 查询LBSAddress对象
	 * @param page
	 * @param userId 用户ID
	 * @param name 名称
	 * @return Page对象
	 */
	public Page getLBSAddress(Page page , Long userId, String name) {
		return lBSAddressDao.getLBSAddress(page, userId, name);
	}
	
	/**
	 * 查询LBSAddress对象
	 * @param userId 用户ID
	 * @param id ID
	 * @return LBSAddress集合或者null
	 */
	public List<LBSAddress> getLBSAddress(Long userId,Long id){
		return lBSAddressDao.getLBSAddress(userId, id);
	}
	
	/**
	 * 保存LBSAddress对象
	 * @param lBSAddress 地址记录对象
	 * @return 是否保存成功
	 */
	public Boolean saveLBSAddress(LBSAddress lBSAddress) {
		return lBSAddressDao.saveLBSAddress(lBSAddress);
	}
	
	/**
	 * 通过用户ID和地址记录ID删除LBSAddress
	 * @param userId 用户ID
	 * @param id 地址记录ID
	 * @return 是否删除成功
	 */
	public Boolean deleteLBSAddress(Long userId,Long id){
		return lBSAddressDao.deleteLBSAddress(userId, id);
	}
}