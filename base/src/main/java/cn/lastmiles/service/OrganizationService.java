package cn.lastmiles.service;
/**
 * createDate : 2016年2月26日上午10:55:58
 */
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.AccountRole;
import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.OrganizationDao;
import cn.lastmiles.dao.ShopDao;

/**
 * *会员等级管理(列表/新增/修改/删除)
 */
@Service
public class OrganizationService {

	@Autowired
	private OrganizationDao organizationDao; // 数据查询对象
	@Autowired
	private IdService idService; // ID自动生成
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountRoleService accountRoleService;
	
	/**
	 * 根据名称查询数据
	 * @param name 查询标识
	 * @param page 页码对象
	 * @return 页码数据对象
	 */
	public Page findByName(String name, Page page) {
		return organizationDao.findByName(name,page);
	}

	/**
	 * 检测字段唯一性
	 * @param id 根据id来判断(新增/修改)区分自身的标识
	 * @param name 判断标识
	 * @return 是否修改成功(成功1、失败0)
	 */
	public int checkOrganizationName(Long id, String name) {
		return organizationDao.checkOrganizationName(id,name) > 0 ? 1 : 0 ;
	}
	
	/**
	 * 新增数据对象
	 * @param Organization 对象信息
	 */
	@Transactional
	public void save(Organization organization,Store store) {
		if( null == store.getId() ){
			store.setId(idService.getId());
		}
		
		if( null == organization.getId() ){
			organization.setId(idService.getId());
		}
		store.setOrganizationId(organization.getId());
		
		if(shopDao.mainShopSave(store)){
			organization.setStoreId(store.getId());
			
			if( null == organization.getParentId() ){ // 没有父类则直接设置等级为顶级1,path为本身ID,pathName为本身name
				organization.setLevel(1); 
				organization.setPath(organization.getId().toString());
				organization.setPathName(organization.getName());
			} else {
				Organization o = organizationDao.findById(organization.getParentId());
				organization.setLevel(o.getLevel()+1);
				organization.setPath(o.getPath() + "_" + organization.getId());
				organization.setPathName(o.getPathName() + "_" + organization.getName());
			}
			organizationDao.save(organization);
			
			Account account = new Account();
			account.setMobile(store.getStoreAcountName()); // 设置商家手机号码
			account.setPassword(store.getStoreAcountPassWord()); // 将手机号码作为密码
			account.setStoreId(store.getId()); // 设置商家ID
			account.setType(Constants.Account.ACCOUNT_TYPE_STORE); // 设置账号类型
			if (accountService.save(account)) { // 新增账号成功
				AccountRole accountRole = new AccountRole();
				accountRole.setAccountId(account.getId());
				accountRole.setRoleId(Constants.Role.ROLE_STORE_ID); // 商家

				accountRoleService.save(accountRole);
			}
		} else {
			throw new RuntimeException("新增商家总部时出错！");
		}
	}


	/**
	 * 根据ID查询数据对象
	 * @param id 查询ID
	 * @return 数据对象
	 */
	public Organization findById(Long id) {
		return organizationDao.findById(id);
	}
	
	/**
	 * 根据ID查询数据对象
	 * @param id 查询ID
	 * @return 数据对象
	 */
	public List<Organization> findByStoreId(Long storeId) {
		if( null == storeId ){
			return organizationDao.findOrganizationList();
		}
		List<Organization> list = organizationDao.findByStoreId(storeId);
		if( null != list && list.size() > 0 ){
			List<Organization> listData = new ArrayList<Organization>();
			for (Organization organization : list) {
				List<Organization> listOrganization = organizationDao.findByParentIdFuzzy(organization.getId());
				listData.addAll(listOrganization);
			}
			return listData.isEmpty() ? null : listData;
		}
		return null;
	}
	
	/**
	 * 根据商家ID查询组织架构集合
	 * @param storeId  商家ID
	 * @return null或者组织架构集合
	 */
	public List<Organization> getByStoreId(Long storeId) {
		return organizationDao.findByStoreId(storeId);
	}
	
	/**
	 * 修改信息
	 * @param Organization 对象信息
	 * @return 是否修改成功(成功true、失败false)
	 */
	public boolean update(Organization organization,Store store) {
		store.setParentId(null);
		store.setParentName(null);
		store.setId(organization.getStoreId());
		// 有修改登录密码则修改密码
		if(StringUtils.isNotBlank(store.getStoreAcountPassWord())){
			String password = PasswordUtils.encryptPassword(store.getStoreAcountPassWord());
			if(StringUtils.isNotBlank(password)){
				if(StringUtils.isNotBlank(store.getStoreAcountNameCache())){
					accountService.updatePasswordByMobile(store.getStoreAcountNameCache(),password,Constants.Account.ACCOUNT_TYPE_STORE);									
				}
			}
		}
			
		if( null == organization.getId() ){
			organization.setId(idService.getId());
		}
		store.setOrganizationId(organization.getId());
		
		if(shopDao.mainShopUpdate(store)){
			Account account = null;
			if(StringUtils.isNotBlank(store.getStoreAcountNameCache())){
				account = accountService.findByStoreIdAndTypeAndMobile(organization.getStoreId(),Constants.Account.ACCOUNT_TYPE_STORE,store.getStoreAcountNameCache());				
			}
			if( null != account ){
				account.setMobile(store.getStoreAcountName()); // 设置商家手机号码
				accountService.update(account);				
			}
		}
		if( null == organization.getParentId() ){ // 没有父类则直接设置等级为顶级1,path为本身ID,pathName为本身name
			organization.setLevel(1); 
			organization.setPath(organization.getId().toString());
			organization.setPathName(organization.getName());
		} else {
			Organization o = organizationDao.findById(organization.getParentId());
			organization.setLevel(o.getLevel()+1);
			organization.setPath(o.getPath() + "_" + organization.getId());
			organization.setPathName(o.getPathName() + "_" + organization.getName());
		}
		return organizationDao.update(organization) > 0 ? true : false ;
	}

	/**
	 * 删除数据
	 * @param id 删除标识
	 * @return 是否删除成功(成功1、失败0)
	 */
	public int deleteById(Long id) {
		return organizationDao.deleteById(id) > 0 ? 1 : 0 ;
	}

	public List<Organization> findByNameFuzzy(String parentName) {
		return organizationDao.findByNameFuzzy(parentName);
	}
	
	public List<Organization> findByNameFuzzy(Long storeId,String parentName) {
		List<Organization> list = organizationDao.findByStoreId(storeId);
		if( null == list || list.isEmpty()){
			return organizationDao.findByNameFuzzy(parentName);
		}
		return organizationDao.findByNameFuzzy(list.get(0).getId(),parentName);
	}

	public Organization checkParentName(Long id,String parentName,Long storeId) {
		List<Organization> list = organizationDao.findByStoreId(storeId);
		if( null == list || list.isEmpty()){
			return organizationDao.checkParentName(id,parentName);
		}
		String superParent = list.get(0).getPath().split("_")[0];
		return organizationDao.checkParentName(id,parentName,superParent);
	}

	public Organization checkHasPermission(Long id,Long storeId) {
		return organizationDao.checkHasPermission(id,storeId);
	}

	public int checkNameRepetition(boolean isAdmin, Long parentId, Long id, String name) {
		return organizationDao.checkNameRepetition(isAdmin,parentId,id,name);
	}

	/**
	 * 根据组织架构ID查询所属其下的组织架构列表
	 * @param id 组织架构ID
	 * @param onlyDirectly 是否只查询下一级别
	 * @return 组织架构集合或者null
	 */
	public List<Organization> getChildrenTreeById(Long id,boolean onlyDirectly) {
		return organizationDao.getChildrenTreeById(id,onlyDirectly);
	}

	/**
	 * 根据组织架构ID查询其所属的组织架构父级
	 * @param id 组织架构ID
	 * @param onlyDirectly 是否只查询顶级父级
	 * @return 组织架构集合或者null
	 */
	public List<Organization> getParentTreeById(Long id,boolean onlyRoot) {
		return organizationDao.getParentTreeById(id,onlyRoot);
	}
	
}