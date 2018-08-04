package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.ReportProduct;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserBank;
import cn.lastmiles.bean.UserCer;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class UserDao {
	private final static Logger logger = LoggerFactory.getLogger(UserDao.class);
	@Autowired
	private StoreDao storeDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 用户分页列表
	 * 
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(String beginTime, String endTime, String name,
			String mobile, Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder(" from t_user where 1 = 1 ");

		if (StringUtils.isNotBlank(mobile)) {// 手机号码筛选
			sql.append(" and mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(name)) {// 名称筛选
			sql.append(" and name like ?");
			parameters.add("%" + name + "%");
		}
		if (storeId != null) {// 查询权限店铺
			
			List<Store> list = storeDao.findAllStoreOnOneOrganizationTree(storeId);
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < list.size(); i++){
				Store s = list.get(i);
				if (i != 0){
					temp.append("," + s.getId());
				}else {
					temp.append(s.getId());
				}
			}
			
			sql.append(" and  storeId in ("+temp+") ");
			
//			parameters.add(storeId);
		}
		if (StringUtils.isNotBlank(beginTime)) {// 开始时间筛选
			sql.append(" and createdTime >= ?");
			parameters.add(beginTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {// 结束时间筛选
			sql.append(" and createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		logger.debug(sql.toString());
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * "
				+ sql + " order by createdTime desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(User.class, list));

		return page;
	}

	public List<User> list(Long storeId,String beginTime, String endTime, String name, String mobile) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder(" from t_user where 1 = 1 ");

		if (StringUtils.isNotBlank(mobile)) {// 手机号码筛选
			sql.append(" and instr(mobile,?)>0 ");
			parameters.add(mobile);
		}
		if (StringUtils.isNotBlank(name)) {// 名称筛选
			sql.append(" and instr(name,?)>0 ");
			parameters.add(name);
		}
		if (storeId != null) {// 查询权限店铺
			sql.append(" and  storeId = ? ");
			parameters.add(storeId);
		}
		if (StringUtils.isNotBlank(beginTime)) {// 开始时间筛选
			sql.append(" and createdTime >= ?");
			parameters.add(beginTime + " 00:00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {// 结束时间筛选
			sql.append(" and createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * " + sql + " order by id desc ", parameters.toArray());

		return BeanUtils.toList(User.class, list);
	}
	
	/**
	 * 根据storeId更改折扣
	 * 
	 * @param discount
	 * @return
	 */
	public boolean updateDiscount(Long storeId, Double discount) {
		int temp = jdbcTemplate.update(
				"update t_user set discount=? where storeId=?", discount,
				storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		int temp = jdbcTemplate
				.update("update t_user set name = ? , mobile=? ,sex= ? , storeId= ? ,point=? where id = ? ",
						user.getName(), user.getMobile(), user.getSex(),
						user.getStoreId(), user.getPoint(),user.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	public boolean update(String identity,String birthTime,Integer sex ,Long id) {
		if ("".equals(birthTime)){
			birthTime = null;
		}
		int temp = jdbcTemplate
				.update("update t_user set identity = ? , birthDay=? ,sex= ? where id = ? ",
						identity,birthTime,sex,id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	public User findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_user u LEFT JOIN t_user_publish_count upc ON upc.userId = u.id  where id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		int temp = jdbcTemplate.update("DELETE FROM t_user WHERE id = ?", id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过手机号码和商店筛选会员 返回会员折扣 为 如果会员表存在折扣 则返回会员表里面折扣 如果不存在 则返回所属商店折扣 如果所属商店未设置折扣
	 * 则返回1
	 * 
	 * @param mobile
	 * @param storeId
	 * @return
	 */
	public User findByMobileAndStoreId(String mobile, Long storeId) {
		StringBuffer sf = new StringBuffer();
		sf.append("	SELECT t.id , t.mobile,t.name,t.sex,t.createdTime,t.createdId,t.storeId");// 查询表头
		/*
		// 第一层IF 开始
		sf.append("	IF (	t.discount IS NOT NULL,");// 判断会员表discount是否不为空
		sf.append(" 		t.discount,");// 会员表discount 不为空 --------直接返回会员折扣
		// 第二层IF开始
		sf.append("			IF ((SELECT d.discount FROM t_discount d 	WHERE	d.storeid = t.storeId ) IS  NULL,");// 去店铺折扣里面查找
		// 判断返回集合是否为空
		sf.append("				1,");// 如果返回集合为空证明没商店折扣表里面没有此商店信息------返回1
		sf.append("				(SELECT	IF (d.discount IS NULL,1,d.discount) FROM	t_discount d  WHERE d.storeid = t.storeId)");// 设置了商店折扣
		// 直接返回上商店折扣
		// 如果商店折扣设置为空则 ------返回1
		sf.append("			)");// 第二层IF结束
		sf.append("	)");// 第一层IF结束
		sf.append("	AS 'discount'");
		*/
		sf.append(" FROM	t_user t ");
		sf.append(" where mobile = ? and storeId = ? ");// 筛选条件
		logger.debug(
				"findByMobileAndStoreId SQL IS :{} mobile is {} storeId is {}",
				sf.toString(), mobile, storeId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sf.toString(), mobile, storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	public User findByStoreId(Long storeId) {
		String sql = "select * from t_user where storeId = ?";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql, storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	public void save(User user) {
		String sql = "insert into t_user(id,name,mobile,password,createdTime,storeId,recommended,sex) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, user.getId(),user.getName(), user.getMobile(),
				user.getPassword(), user.getCreatedTime(), user.getStoreId(),
				user.getRecommended(),user.getSex());
		logger.debug("{}新增会员成功！" + user);
	}

	public User findByMobileForPlatform(String mobile){
		String sql = "select id,mobile,password,name,nickName,iconUrl,cid,idAudit,realName from t_user where mobile = ? and storeId is null";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, mobile);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}
	
	public User findByMobile(String mobile) {
		String sql = "select id,mobile,password,name,nickName,iconUrl,cid,idAudit,realName from t_user where mobile = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, mobile);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}
	
	/**
	 * 根据手机号码查询用户是否存在(是否属于平台用户)
	 * @param mobile 手机号码
	 * @param isPlatform 查询平台用户
	 * @return 用户集合或者null
	 */ 
	public List<User> findByMobile(String mobile,boolean isPlatform) {
		String querySQL = null;
		if(isPlatform){
			querySQL = "select * from t_user where mobile = ? and storeId is null";
		} else {
			querySQL = "select * from t_user where mobile = ? and storeId is not null";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL, mobile);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toList(User.class, list);
		}
		return null;
	}
	
	public User findByMobile(String mobile,Long storeId) {
		String sql = "select id,mobile,password,name,nickName,iconUrl,cid,idAudit,realName from t_user where mobile = ? and storeId=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, mobile,storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}
	public User findByMobileForAppLogin(Long id) {
		String sql = "select id,mobile,password,name,nickName,iconUrl,cid,idAudit,realName from t_user where id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	/**
	 * 更新登录密码
	 * 
	 * @param userId
	 * @param loginPassword
	 * @return 0 失败，1成功
	 */
	public int updateLoginPassword(Long userId, String loginPassword) {
		String sql = "update t_user set PASSWORD = ? where id = ?";
		return jdbcTemplate.update(sql, loginPassword, userId);
	}

	/**
	 * 更新支付密码
	 * 
	 * @param userId
	 * @param payPassword
	 * @return 0 失败，1成功
	 */
	public int updatePayPassword(Long userId, String payPassword) {
		String sql = "update t_user set PAYPASSWORD = ? where id = ?";
		return jdbcTemplate.update(sql, payPassword, userId);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param User
	 * @return
	 */
	public int updateUser(User user) {
		String sql = "update t_user set nickName=?,realName=?,sex = ?,updateTime=?,memo = ?  where id = ?";
		return jdbcTemplate.update(sql, user.getNickName(), user.getRealName(),
				user.getSex(), new Date(), user.getMemo(), user.getId());
	}

	public void saveCertificate(String cerId, Long userId, String cerName) {
		String sql = "insert into t_user_cer(cerId,userId,cername) values(?,?,?)";
		jdbcTemplate.update(sql, cerId, userId, cerName);
	}

	public List<UserCer> findCertificates(Long userId) {
		String sql = "select cerId,userId,cerName from t_user_cer where userId=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(UserCer.class, list);
	}

	public void delCerByCerId(String cerId, Long userId) {
		String sql = "delete from t_user_cer where cerId=? and userId=?";
		jdbcTemplate.update(sql, cerId);

	}

	public List<UserBank> findBankByUserId(Long userId) {
		String sql = "select bankId,cardNumber,cardHolder,userId,inUse from t_user_bank where userId=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(UserBank.class, list);
	}

	public void saveUserBank(UserBank userBank) {
		String sql = "insert into t_user_bank(bankId,cardNumber,cardHolder,userId,inUse) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, userBank.getBankId(),
				userBank.getCardNumber(), userBank.getCardHolder(),
				userBank.getUserId(), userBank.getInUse());
	}

	public void updateUserBank(UserBank userBank) {
		String sql = "update t_user_bank set bankId=?,cardNumber=?,cardHolder=?,userId=?,inUse=?  where userId = ?";
		jdbcTemplate
				.update(sql, userBank.getBankId(), userBank.getCardNumber(),
						userBank.getCardHolder(), userBank.getUserId(),
						userBank.getInUse(), userBank.getUserId());

	}

	public int ModifyStatus(Long id, Integer status) {
		String sql = "update t_user set status=?  where id = ?";
		return jdbcTemplate.update(sql, status, id);
	}

	public String getIcon(Long userId) {
		return jdbcTemplate
				.queryForObject("select iconUrl from t_user where id = ?",
						String.class, userId);
	}

	public void updateIcon(Long userId, String iconId) {
		jdbcTemplate.update("update t_user set iconUrl = ? where id = ?",
				iconId, userId);
	}
	
	public void updateName(Long userId, String name) {
		jdbcTemplate.update("update t_user set name = ? where id = ?",
				name, userId);
	}

	public void updateMobile(Long userId, String mobile) {
		jdbcTemplate.update("update t_user set mobile = ? where id = ?",
				mobile, userId);
	}

	public Page identityList(String mobile, Integer idAudit, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder(
				" from t_user where 1 = 1 and identity is not null ");

		if (StringUtils.isNotBlank(mobile)) {// 手机号码筛选
			sql.append(" and mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (idAudit != null) {// 手机号码筛选
			sql.append(" and idAudit=?");
			parameters.add(idAudit);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * "
				+ sql + " order by id limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(User.class, list));

		return page;
	}

	public int modifyIdAudit(Long id, Integer idAudit) {
		String sql = "update t_user set idAudit=?  where id = ?";
		return jdbcTemplate.update(sql, idAudit, id);
	}

	public void updateCid(Long id, String cid) {
		jdbcTemplate.update("update t_user set cid = ? where id = ?", cid, id);
	}

	public void updatePassword(Long id, String password) {
		jdbcTemplate.update("update t_user set password = ? where id = ?",
				password, id);
	}

	public void updatePasswordByMobile(String mobile, String password) {
		jdbcTemplate.update("update t_user set password = ? where mobile = ?",
				password, mobile);
	}

	public Integer findAuditStatus(Long userId) {
		return jdbcTemplate.queryForObject("select idAudit from t_user where id = ?", Integer.class, userId);
	}

	public String getCid(Long userId) {
		return jdbcTemplate.queryForObject("select cid from t_user where id=?", String.class, userId);
	}

	public List<User> byCarNumberFindUser(String cardNumber, Long storeId) {
		String sql = "select * from t_user where cardNumber=? and storeId = ?";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql, cardNumber,storeId);
		
		return BeanUtils.toList(User.class, list);
	}

	public void saveUser(User user) {
		String sql = "insert into t_user(id,mobile,createdTime,storeId,point,cardNumber,balance,sex,name,realName) values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, user.getId(), user.getMobile(),
				new Date(), user.getStoreId(),user.getPoint(),user.getCardNumber(),
				user.getBalance(),user.getSex(),user.getName(),user.getRealName());
		
	}

	/**
	 * APP-Third
	 * @param lastId 商家数据从这个ID开始,既必须大于此ID
	 * @param storeId 商家ID
	 */
	public List<User> getAssignUserByPartnerIdAndStoreId(Long lastId, Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select u.* from t_user u where u.storeId = ? and u.id > ? order by u.id asc limit ?,?");
		parameters.add(lastId);
		parameters.add(0);
		parameters.add(100);
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		if( null == list || list.isEmpty()){
			return null;
		}
		return BeanUtils.toList(User.class, list);
	}

	public User findUserByStoreIdAndMobile(Long storeId, String mobile) {
		logger.debug("findUserByStoreIdAndMobile storeId is {} , mobile is {}",storeId,mobile);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(" select u.* from t_user u where u.storeId = ? and u.mobile = ? ", storeId,mobile);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	public User searchUserByStoreIdAndMobile(Long storeId, String mobile) {
		// 得到总店ID
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,parentId,isShareUser from t_store s where id = (select parentId from t_store where id = ? and parentId is not null)", storeId);
		if (!list.isEmpty()){
			Map<String,Object> map = list.get(0);
			Object isShareUser = map.get("isShareUser");
			if (isShareUser != null && Integer.valueOf(isShareUser.toString()).intValue() == Constants.Store.STORE_ISSHARE){
				String querySQL = "select u.* from t_user u where u.storeId in ( select t.id from t_store t where parentId = ? ) and u.mobile = ?";
				List<Map<String, Object>> ret = jdbcTemplate.queryForList(querySQL,map.get("id"),mobile);
				if (ret.isEmpty()) {
					return null;
				}
				return BeanUtils.toBean(User.class, ret.get(0));
			}
		}
		return null;
	}

	public void batchUpdate(List<User> userList) {
		String sql = "insert into t_user(id,mobile,createdTime,storeId,point,cardNumber,balance,sex,name,realName) values(?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> batchUser=new ArrayList<Object[]>();
		for(User user:userList){
			Object[] obj=new Object[10];
			obj[0]=user.getId();
			obj[1]=user.getMobile();
			obj[2]=new Date();
			obj[3]=user.getStoreId();
			obj[4]=user.getPoint();
			obj[5]=user.getCardNumber();
			obj[6]=user.getBalance();
			obj[7]=user.getSex();
			obj[8]=user.getName();
			obj[9]=user.getRealName();
			batchUser.add(obj);
		}
		jdbcTemplate.batchUpdate(sql, batchUser);
		
	}

	public List<User> findUserNotPlatform() {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select u.* from t_user u  where u.storeId is not null and u.mobile not in (select u.mobile from t_user u where u.storeId is null)");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		if( null == list || list.isEmpty()){
			return null;
		}
		return BeanUtils.toList(User.class, list);
	}

	public void save(final List<Object[]> batchUser) {
		StringBuffer insertSQL = new StringBuffer("insert into t_user(id,mobile,name,sex,discount,createdTime,createdId,storeId,password,identifyTypeId,");
			insertSQL.append(" identity,nickName,iconUrl,phoneNumber,address,memo,updateTime,realName,status,areaId,");
			insertSQL.append(" idAudit,cid,recommended,point,grade,cardNumber,balance)");
			insertSQL.append(" values(?,?,?,?,?,?,?,?,?,?,");
			insertSQL.append(" ?,?,?,?,?,?,?,?,?,?,");
			insertSQL.append(" ?,?,?,?,?,?,?)");
		int[] index = jdbcTemplate.batchUpdate(insertSQL.toString(), batchUser );
		
		logger.debug("共转换用户数据数量是：{}",index.length);
	}

	/**
	 * 查询手机号码是不是属于某个组织架构集合下的商家的会员
	 * @param mobile 手机号码
	 * @param organizationList 组织结构集合
	 * @return true 表示是
	 */
	public User searchOrganization_UserByMobile$organizationList(String mobile,List<Organization> organizationList) {
		// 根据组织结构ID查询其下的所有商家ID
		// 根据所有商家ID查询所有商家所拥有的所有的会员的手机号码
		// 查询此手机号码是否存在于上面查出的手机号码
		// 存在返回true,否则返回false
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("select * from t_user u where u.storeId in ");
		querySQL.append("(");
		querySQL.append(" select t.storeId from (");
		querySQL.append(" SELECT storeId FROM t_organization WHERE 	find_in_set(id,queryChildrenTreeInfo (?))");
		querySQL.append(" ) t");
		querySQL.append(" ) ");
		querySQL.append(" and u.mobile = ? ");
		
		User user = null;
		for (Organization organization : organizationList) {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),organization.getId(),mobile);
			if( null != list && list.size() > 0  ){
				user = BeanUtils.toBean(User.class, list.get(0));
				break;
			}
		}
		return user;
	}

	public User search(String mobile, Long organizationId) {
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("select * from t_user u where u.storeId in ");
		querySQL.append("(");
		querySQL.append(" select t.id from t_store t where t.organizationId = ? ");
		querySQL.append(" ) ");
		querySQL.append(" and u.mobile = ? ");
		User user = null;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),organizationId,mobile);
		logger.debug("search list is {} querySQL is {} organizationId is {} ",list,querySQL.toString(), organizationId);
		if( null != list && list.size() > 0  ){
			user = BeanUtils.toBean(User.class, list.get(0));
		}
		return user;
	}
	
	/**
	 * 查找某个商家的用户，包括连锁
	 * @param mobile
	 * @param storeId
	 * @return
	 */
	public User findUserForChainStore(String mobile, Long storeId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user where mobile = ? and storeId = ?", mobile,storeId);
		if (!list.isEmpty()){
			return BeanUtils.toBean(User.class, list.get(0));
		}
		List<Store> storeList = storeDao.findAllStoreOnOneOrganizationTree(storeId);
		
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < storeList.size(); i++){
			Store s = storeList.get(i);
			if (i != 0){
				temp.append("," + s.getId());
			}else {
				temp.append(s.getId());
			}
		}
		
		list = jdbcTemplate.queryForList("select * from t_user where mobile = ? and storeId in (" + temp + ")", mobile);
		if (!list.isEmpty()){
			return BeanUtils.toBean(User.class, list.get(0));
		}
		return null;
	}
	
	/**
	 * 查找单个商家用户
	 * @param mobile
	 * @param storeId
	 * @return
	 */
	public User findUserForStore(String mobile,Long storeId){
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user where mobile = ? and storeId = ?", mobile,storeId);
		if (!list.isEmpty()){
			return BeanUtils.toBean(User.class, list.get(0));
		}
		return null;
	}
	
	public void updateTotalPointAndTotalConsumption(Long userId,Double point,Double totalPoint,Double totalConsumption){
		jdbcTemplate.update("update t_user set point = ? ,totalPoint = ? ,totalConsumption = ? where id = ?", point,totalPoint,totalConsumption,userId);
	}
	
	public void updatePoint(Long userId,Double point,Double totalPoint){
		jdbcTemplate.update("update t_user set point = ? ,totalPoint = ? where id = ?", point,totalPoint,userId);
	}
	
	public void updateUserDiscount(Long userId,Double discount){
		jdbcTemplate.update("update t_user set discount = ? where id = ?", discount,userId);
	}
	
	public void updateTotalPointAndTotalConsumptionAndDiscount(Long userId,Double point,Double totalPoint,Double totalConsumption,Double discount){
		logger.debug("updateTotalPointAndTotalConsumption={},{},{},{}",userId,point,totalConsumption,discount);
		jdbcTemplate.update("update t_user set point = ?,totalPoint = ?,totalConsumption = ?,discount = ? where id = ?", point,
				totalPoint,totalConsumption,discount,userId);
	}
	
	/**
	 * 根据会员卡id查找平台用户
	 * @param userCardId
	 * @return
	 */
	public User findPlatformUserByUserCardId(Long userCardId){
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_user where mobile = (select mobile from t_user_card where id = ?)",userCardId) ;
		if (!list.isEmpty()){
			return BeanUtils.toBean(User.class, list.get(0));
		}
		return null;
	}

	/**
	 * 根据会员ID查找会员信息
	 * @param userId 会员ID
	 * @return null或者会员对象
	 */
	public User findByUserId(Long userId) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_user where id = ?", userId);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(User.class, list.get(0));
	}

	public List<User> findUserListByBirthDay() {
		//CONCAT(YEAR(CURDATE()),DATE_FORMAT(birthday,'-%m-%d')) 代表把生日调整为今年的生日日期
		//date_sub('2012-05-31',interval -1 day)
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", DateUtils.format(new Date(), "yyyy-MM-dd"));
		
		
		return JdbcUtils.queryForList("user.findUserListByBirthDay", map, User.class);
	}
}