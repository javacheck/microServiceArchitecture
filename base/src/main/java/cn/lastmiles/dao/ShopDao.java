package cn.lastmiles.dao;

/**
 * createDate : 2015-07-07 PM 18:56
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.ShopType;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.StoreTerminal;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class ShopDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(ShopDao.class);

	/**
	 * 商户app更新商家信息
	 * @param store
	 */
	public void appUpdate(Store store) {
		String sql = "update t_store set name=?, shopTypeId = ? , address = ? , updatedTime = ? ,"
				+ "  payType = ? ,  "
				+ " phone = ? ,  minAmount = ? ,"
				+ " shipRange = ? , shipTime = ? , shipAmount = ?, service = ? , shipType = ? where id = ? ";
		jdbcTemplate.update(sql, store.getName(), store.getShopTypeId(),
				store.getAddress(), new Date(), store.getPayType(),
				store.getPhone(), store.getMinAmount(), store.getShipRange(),
				store.getShipTime(), store.getShipAmount(), store.getService(),
				store.getShipType(), store.getId());
	}

	public Page getShop(Long storeId,String shopName, String mobile, String agentName, int status, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s left join t_agent a on s.agentId = a.id where s.isMain  = ? ");
		parameters.add(Constants.Store.STORE_SHOP);
		String data = "s.id,s.name, "
				+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ," // 得到商家类型
				+ " a.name as agentName , " // 得到代理商名称
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if( null != storeId ){
			querySQL.append(" and s.id = ? ");
			parameters.add(storeId);
		}
		
		if (StringUtils.isNotBlank(shopName)) {
			querySQL.append(" and instr(s.name,?) > 0 ");
			parameters.add(shopName);
		}

		if (StringUtils.isNotBlank(agentName)) {
			querySQL.append(" and instr(a.name,?) > 0 ");
			parameters.add(agentName);
		}
		
		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and instr(s.mobile,?) > 0 ");
			parameters.add(mobile);
		}
		
		// 营业状态(全部)
		if (status != Constants.Status.SELECT_ALL) {
			querySQL.append(" and s.status = ?");
			parameters.add(status);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() 
				+ " order by s.id desc limit ?,?", parameters.toArray());
		
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}
	public Page getAllShop(String storeIdString,String shopName, String mobile, String agentName, int status, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s left join t_agent a on s.agentId = a.id where 1=1 ");
		String data = "s.id,s.name, "
				+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ," // 得到商家类型
				+ " a.name as agentName , " // 得到代理商名称
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and s.id in (" + storeIdString + ") ");
		}
		
		if (StringUtils.isNotBlank(shopName)) {
			querySQL.append(" and instr(s.name,?) > 0 ");
			parameters.add(shopName);
		}

		if (StringUtils.isNotBlank(agentName)) {
			querySQL.append(" and instr(a.name,?) > 0 ");
			parameters.add(agentName);
		}
		
		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and instr(s.mobile,?) > 0 ");
			parameters.add(mobile);
		}
		
		// 营业状态(全部)
		if (status != Constants.Status.SELECT_ALL) {
			querySQL.append(" and s.status = ?");
			parameters.add(status);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() 
				+ " order by s.isMain desc,s.id desc limit ?,?", parameters.toArray());
		
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}
	
	public Page getShop(String storeIdString,String shopName, String mobile, String agentName, int status, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s left join t_agent a on s.agentId = a.id left join t_organization o on s.organizationId=o.id where s.isMain  = ? ");
		parameters.add(Constants.Store.STORE_SHOP);
		String data = "s.id,s.name,o.name as organizationName, "
				+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ," // 得到商家类型
				+ " a.name as agentName , " // 得到代理商名称
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and s.id in (" + storeIdString + ") ");
		}
		
		if (StringUtils.isNotBlank(shopName)) {
			querySQL.append(" and instr(s.name,?) > 0 ");
			parameters.add(shopName);
		}

		if (StringUtils.isNotBlank(agentName)) {
			querySQL.append(" and instr(a.name,?) > 0 ");
			parameters.add(agentName);
		}
		
		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and instr(s.mobile,?) > 0 ");
			parameters.add(mobile);
		}
		
		// 营业状态(全部)
		if (status != Constants.Status.SELECT_ALL) {
			querySQL.append(" and s.status = ?");
			parameters.add(status);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() 
				+ " order by s.id desc limit ?,?", parameters.toArray());
		
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}
	
	public boolean save(Store store) {
		String sql = "insert into t_store(id,name,companyName,createdTime,parentId,path,accountId,shopTypeId,agentId,areaId,"
				+ "address,updatedTime,updatedAccountId,logo,description,longitude,latitude,payType,merchantName,merchantNo,"
				+ "mcc,mccSign,mccCost,status,posBankDeposit,posAccountName,posBankLine,posBankAccount,contact,mobile,"
				+ "phone,businessTime,minAmount,shipRange,shipTime,shipAmount,service,shipType,freeShipAmount,onLineRateType,"
				+" onLineRate,partnerId,posAdminPassword,unifiedPointRule,organizationId,posCartAuthority,receiptReprint,receiptPrintAmount,printer) "
				+ "VALUES (? ,? ,? ,?,?,?,?,?,?,?,"
				+ "? ,? ,? ,?,?,?,?,?,?,?,"
				+ "? ,? ,? ,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql, store.getId(), store.getName(),
				store.getCompanyName(), new Date(), store.getParentId(), store.getPath(),
				store.getAccountId(), store.getShopTypeId(),
				store.getAgentId(), store.getAreaId(), store.getAddress(),
				store.getUpdatedTime(), store.getUpdatedAccountId(),
				store.getLogo(), store.getDescription(), store.getLongitude(),
				store.getLatitude(), store.getPayType(),
				store.getMerchantName(), store.getMerchantNo(), store.getMcc(),
				store.getMccSign(), store.getMccCost(), store.getStatus(),
				store.getPosBankDeposit(), store.getPosAccountName(),
				store.getPosBankLine(), store.getPosBankAccount(),
				store.getContact(), store.getMobile(), store.getPhone(),
				store.getBusinessTime(), store.getMinAmount(),
				store.getShipRange(), store.getShipTime(),
				store.getShipAmount(), store.getService(), store.getShipType(),store.getFreeShipAmount(),
				store.getOnLineRateType(),store.getOnLineRate(),store.getPartnerId(),store.getPosAdminPassword(),
				store.getUnifiedPointRule(),store.getOrganizationId(),store.getPosCartAuthority(),store.getReceiptReprint(),store.getReceiptPrintAmount(),store.getPrinter());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Store findById(Long id) {

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.*,(select ss.name from t_organization ss where ss.id = s.organizationId ) as mainShopName,"
								+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ,"
								+ " (select p.name from t_partner p where s.partnerId = p.id ) as partnerName ,"
								+ " a.name as agentName"
								+ " from t_store s ,t_agent a where s.agentId = a.id and s.id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public boolean update(Store store) {
		String sql = "update t_store set name=?, companyName =?, parentId = ? , path = ? ,shopTypeId = ? ,agentId = ? ,areaId = ? , address = ? , updatedTime = ? ,updatedAccountId = ? ,"
				+ " logo = ? , description = ? , longitude = ? ,latitude = ? , payType = ? , merchantName = ? , merchantNo = ? , mcc = ? , mccSign = ? , mccCost = ? , "
				+ " status = ? , posBankDeposit = ? , posAccountName = ? , posBankLine = ? , posBankAccount = ? , contact = ? , mobile = ? , phone = ? , businessTime = ? , minAmount = ? ,"
				+ " shipRange = ? , shipTime = ? , shipAmount = ?, service = ? , shipType = ?,freeShipAmount = ?,onLineRateType = ? ,onLineRate = ?,partnerId = ?,unifiedPointRule = ? ,organizationId = ?,printer=?,alipayStoreId=?  where id = ?";
		int temp = jdbcTemplate.update(sql, store.getName(),
				store.getCompanyName(), store.getParentId(), store.getPath(),
				store.getShopTypeId(), store.getAgentId(), store.getAreaId(),
				store.getAddress(), new Date(), store.getUpdatedAccountId(),
				store.getLogo(), store.getDescription(), store.getLongitude(),
				store.getLatitude(), store.getPayType(),
				store.getMerchantName(), store.getMerchantNo(), store.getMcc(),
				store.getMccSign(), store.getMccCost(), store.getStatus(),
				store.getPosBankDeposit(), store.getPosAccountName(),
				store.getPosBankLine(), store.getPosBankAccount(),
				store.getContact(), store.getMobile(), store.getPhone(),
				store.getBusinessTime(), store.getMinAmount(),
				store.getShipRange(), store.getShipTime(),
				store.getShipAmount(), store.getService(), store.getShipType(),store.getFreeShipAmount(),
				store.getOnLineRateType(),store.getOnLineRate(),
				store.getPartnerId(),
				store.getUnifiedPointRule(),
				store.getOrganizationId(),store.getPrinter(),
				store.getAlipayStoreId(),
				store.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean storeUpdate(Store store) {
		String sql = "update t_store set updatedAccountId = ? ,"
				+ " logo = ? , description = ? , "
				+ " phone = ? , businessTime = ? , minAmount = ? ,"
				+ " shipRange = ? , shipTime = ? , shipAmount = ?, shipType = ?,freeShipAmount = ?,unifiedPointRule = ?,posCartAuthority=?,receiptReprint=?,receiptPrintAmount=? where id = ?";
		int temp = jdbcTemplate.update(sql, store.getUpdatedAccountId(),
				store.getLogo(), store.getDescription(),
				store.getPhone(),
				store.getBusinessTime(), store.getMinAmount(),
				store.getShipRange(), store.getShipTime(),
				store.getShipAmount(),store.getShipType(),store.getFreeShipAmount(),
				store.getUnifiedPointRule(),store.getPosCartAuthority(),store.getReceiptReprint(),store.getReceiptPrintAmount(),
				store.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}


	public Store checkName(String name) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_store where name = ?", name);
		return list.isEmpty() ? null : BeanUtils.toBean(Store.class,
				list.get(0));
	}

	public Store checkCompanyName(String companyName) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_store where companyName = ?", companyName);
		return list.isEmpty() ? null : BeanUtils.toBean(Store.class,
				list.get(0));
	}

	public Object checkMerchantNo(String merchantNo) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_store where merchantNo = ?", merchantNo);
		return list.isEmpty() ? null : BeanUtils.toBean(Store.class,
				list.get(0));
	}

	public Object checkMerchantName(String merchantName) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select * from t_store where merchantName = ?", merchantName);
		return list.isEmpty() ? null : BeanUtils.toBean(Store.class,
				list.get(0));
	}

	public Boolean deleteById(Long id) {
		int temp = jdbcTemplate.update("delete from  t_store  where id = ? ",
				id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Page getShopTypeList(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_shop_type s where 1 = 1 ");
		String data = " s.id,s.name ";

		if (StringUtils.isNotBlank(name)) {
			querySQL.append(" and s.name like ?");
			parameters.add("%" + name + "%");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + querySQL.toString()
				+ " order by s.id,s.sort desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(ShopType.class, list));

		return page;
	}

	// 代理商商家查询(代理商及其他下面子代理商的商家)
	public Page getAgentAndStoreList(Long agentId, String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_agent a ,t_store s where a.id = s.agentId ");
		querySQL.append(" and a.id in (select a.id from t_agent a where a.path like ? or (a.path like ? and parentId is null) or a.path like ? )");

		String data = "s.id,s.name,a.name as agentName ,s.mobile,s.merchantNo,s.merchantName,s.createdTime,s.address,s.contact, (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName";
		parameters.add(agentId + "_%");
		parameters.add(agentId);
		parameters.add("%_" + agentId);

		if (StringUtils.isNotBlank(name)) {
			querySQL.append(" and s.name like ?");
			parameters.add("%" + name + "%");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + querySQL.toString() + " order by s.id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	public List<ShopType> findAllShopType() {
		return BeanUtils
				.toList(ShopType.class,
						jdbcTemplate
								.queryForList("select id,name from t_shop_type order by `sort` "));
	}

	public String getMobile(Long storeId) {
		return jdbcTemplate.queryForObject("select mobile from t_store where id=?", String.class,storeId);
	}
	
	public Page getShop(String name, String mobile ,Integer shopTypeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_store s left join t_shop_type st on s.shopTypeId = st.id where s.isMain = ? ");
		parameters.add(Constants.Store.STORE_SHOP);
		String data = "s.id,s.name," // 得到商家名称和商家类型
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if( null != shopTypeId){
			querySQL.append("and st.id = ?");
			parameters.add(shopTypeId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			querySQL.append(" and s.name like ?");
			parameters.add("%" + name + "%");
		}

		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and s.mobile like ?");
			parameters.add("%" + mobile + "%");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list 
			= jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by s.id desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	public Page getShopNotInStoreAdImage(String name, String mobile,
			Integer shopTypeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_store s left join t_shop_type st on s.shopTypeId = st.id where 1=1 ");
		querySQL.append(" and s.id not in (select sai.storeId from t_store_ad_image sai ) ");
		String data = "s.id,s.name," // 得到商家名称和商家类型
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if( null != shopTypeId){
			querySQL.append("and st.id = ?");
			parameters.add(shopTypeId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			querySQL.append(" and s.name like ?");
			parameters.add("%" + name + "%");
		}

		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and s.mobile like ?");
			parameters.add("%" + mobile + "%");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list 
			= jdbcTemplate.queryForList("select " + data + querySQL.toString() + " order by s.id desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	public void saveTerminalId(List<Object[]> batchArgs, Long storeId) {
			if( null != storeId ){ // 修改先进行删除操作
				jdbcTemplate.update("delete from t_store_terminal where storeId = ? ",storeId);
			}
		if(batchArgs.size() > 0){
			String sql = "insert into t_store_terminal values(?,?)";
			jdbcTemplate.batchUpdate(sql,batchArgs);			
		}
	}

	public List<StoreTerminal> getTerminalIdArray(Long storeId) {
		List<Map<String, Object>> list 
		= jdbcTemplate.queryForList("select st.terminalId from t_store_terminal st where storeId = ? order by st.storeId ", storeId);
		return BeanUtils.toList(StoreTerminal.class, list);
	}

	public boolean checkTerminalId(String terValue) {
		int i = jdbcTemplate.queryForObject("select count(1) from t_store_terminal where terminalId = ?",Integer.class, terValue);
		return i == 0 ? false : true;
	}
	
	/**
	 * 根据商家ID查询商家信息
	 * @param storeId 商家ID
	 * @return 商家对象或者null
	 */
	public Store findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate .queryForList( "select * from t_store s where s.id = ?", storeId);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(Store.class, list.get(0));
		}
		return null;
	}
	
	public String findStoreNameById(Long id) {

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select s.*,"
								+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ,"
								+ " a.name as agentName"
								+ " from t_store s ,t_agent a where s.agentId = a.id and s.id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return (BeanUtils.toBean(Store.class, list.get(0))).getName();
	}

	public boolean updateLoginPW(Long accountId, String newPW) {
		return jdbcTemplate.update("update t_account set password = ? where id = ? ",newPW,accountId) > 0 ? true : false ;
	}

	public boolean updateMobile(Long accountId, String mobile) {
		return jdbcTemplate.update("update t_account set mobile = ? where id = ?", mobile,accountId) > 0 ? true : false;
	}

	public boolean updateLoginPW(String mobile, String newPW) {
		return jdbcTemplate.update("update t_account set password = ? where mobile = ? ",newPW,mobile) > 0 ? true : false ;
	}
	
	public List<Map<String, Object>> findStoreSignById(Long storeId) {
		return jdbcTemplate .queryForList( "select s.logo,s.name,(select pa.balance+pa.frozenAmount from t_pay_account pa where pa.ownerId = s.id) as sumPrice from t_store s where s.id = ?", storeId);
	}

	public boolean updateStoreMobile(Long storeId, String mobile) {
		return jdbcTemplate.update("update t_store set mobile = ? where id = ? ",mobile,storeId) > 0 ? true : false ;
	}

	public Page mainShopList(String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s  where isMain = ? ");
		parameters.add(Constants.Store.STORE_MAINSHOP);
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and instr(s.name,?) > 0 ");
			parameters.add(name);
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString() , Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.append(" order by s.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());		
		
		querySQL.insert(0,"select s.id, s.name , s.agentId,s.isShareUser ,(select name from t_agent a where a.id = s.agentId limit 1) as agentName, s.status , s.mobile , (select a.mobile from t_account a where s.id = a.storeId limit 1) as storeAcountName , (select a.password from t_account a where s.id = a.storeId limit 1) as storeAcountPassWord ");
		
		page.setData(BeanUtils.toList(Store.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray())));
		return page;
	}

	public boolean mainShopUpdate(Store store) {
		String sql = "update t_store set name = ? , mobile = ? , isShareUser = ? , updatedTime = ? ,updatedAccountId = ? , agentId = ? , organizationId = ? where id = ? and isMain = ?";
		int temp = jdbcTemplate.update(sql, store.getName(),store.getMobile(),store.getIsShareUser(),new Date(),store.getAccountId(),store.getAgentId(),store.getOrganizationId(),store.getId(),Constants.Store.STORE_MAINSHOP);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean mainShopSave(Store store) {
		String sql = "insert into t_store(id,name,createdTime,accountId,status,mobile,isShareUser,isMain,agentId,organizationId) values (? ,? ,?,? ,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql,store.getId(), store.getName(),new Date(),store.getAccountId(),store.getStatus(),store.getMobile(),store.getIsShareUser(),Constants.Store.STORE_MAINSHOP,store.getAgentId(),store.getOrganizationId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Store findMainShopById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select s.id , s.name , s.isShareUser ,s.agentId, s.status , s.mobile ,(select name from t_agent a where a.id = s.agentId) as agentName, (select a.mobile from t_account a where s.id = a.storeId ) as storeAcountName,(select a.mobile from t_account a where s.id = a.storeId ) as storeAcountNameCache , (select a.password from t_account a where s.id = a.storeId ) as storeAcountPassWord from t_store s where s.id = ? and s.isMain = ?",
						id,Constants.Store.STORE_MAINSHOP);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Store.class, list.get(0));
	}

	public int mainShopCheckName(Long id, String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_store s where s.name = ? ");
		parameters.add(name);
		
		if( null != id ){
			querySQL.append(" and s.id <> ?");
			parameters.add(id);
		}
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}

	public boolean updatePosPassword(Long id, String posAdminPassword) {
		String sql = "update t_store set posAdminPassword = ? where id = ? and isMain = ? ";
		int temp = jdbcTemplate.update(sql,posAdminPassword,id,Constants.Store.STORE_SHOP);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Page getShop(String storeIdString, int status ,String shopName, String mobile, String agentName, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_store s left join t_agent a on s.agentId = a.id where s.isMain  = ? ");
		parameters.add(Constants.Store.STORE_SHOP);
		String data = "s.id,s.name, "
				+ " (select t.name from t_shop_type t where s.shopTypeId = t.id ) as shopTypeName ," // 得到商家类型
				+ " a.name as agentName , " // 得到代理商名称
				+ " s.mobile,"
				+ " s.phone," 
				+ " s.status";

		if ( StringUtils.isNotBlank(storeIdString) ) {
			querySQL.append(" and s.id in( " +storeIdString+" )");
		}
		
		if (StringUtils.isNotBlank(shopName)) {
			querySQL.append(" and instr(s.name,?) > 0 ");
			parameters.add(shopName);
		}

		if (StringUtils.isNotBlank(agentName)) {
			querySQL.append(" and instr(a.name,?) > 0 ");
			parameters.add(agentName);
		}
		
		if (StringUtils.isNotBlank(mobile)) {
			querySQL.append(" and instr(s.mobile,?) > 0 ");
			parameters.add(mobile);
		}
		
		// 营业状态(全部)
		if (status != Constants.Status.SELECT_ALL) {
			querySQL.append(" and s.status = ?");
			parameters.add(status);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select " + data + querySQL.toString() 
				+ " order by s.id desc limit ?,?", parameters.toArray());
		
		page.setData(BeanUtils.toList(Store.class, list));

		return page;
	}

	/**
	 * 根据组拼的组织架构ID查询商家信息中所有属于此组织架构ID组合其下的商家列表
	 * @param linkString 组拼的组织架构ID
	 * @return null或者商家集合列表
	 */
	public List<Store> searchOrganization_StoreByorganizationLink( String linkString) {
		if( StringUtils.isNotBlank(linkString) ){
			String querySQL = "select * from t_store s where s.organizationId in (" + linkString + ")";
			
			List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL);
			if( null != list && list.size() > 0 ){
				return BeanUtils.toList(Store.class, list);
			}			
		}
		return null;
	}

	public boolean updateAlipayAuthToken(String storeId, String appAuthToken) {
		String sql = " update t_store set appAuthToken = ? where id = ? ";
		int temp = jdbcTemplate.update(sql,appAuthToken,storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
}