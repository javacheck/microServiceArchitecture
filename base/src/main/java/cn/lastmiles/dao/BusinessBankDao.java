package cn.lastmiles.dao;
/**
 * createDate : 2015-07-09 PM 19:00
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

import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class BusinessBankDao {
	
	private final static Logger logger = LoggerFactory.getLogger(BusinessBankDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存银行卡信息
	 * @param businessBank 银行信息
	 * @return 是否新增成功
	 */
	public boolean save(BusinessBank businessBank) {
		logger.debug("save BusinessBank : subbranch is {} , bankId is {}",businessBank.getSubbranch(),businessBank.getBankId());
		
		String sql = "insert into t_business_bank(id,businessId,type,bankName,accountNumber,accountName,"
				+ "mobile,isDefault,createdTime,subbranch,bankId,accountType) "
				 + "values (? ,? ,? ,?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql, businessBank.getId(),
				businessBank.getBusinessId(), businessBank.getType(),
				businessBank.getBankName(), businessBank.getAccountNumber(),
				businessBank.getAccountName(), businessBank.getMobile(),
				businessBank.getIsDefault(),new Date(),businessBank.getSubbranch(),businessBank.getBankId(),businessBank.getAccountType());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改银行卡信息
	 * @param businessBank 银行对象
	 * @return 是否修改成功
	 */
	public boolean update(BusinessBank businessBank) {
		logger.debug("update businessBank is {}",businessBank);
		
		String sql = "update t_business_bank set businessId=?, type =?, bankName = ? , accountNumber = ? ,accountName = ? ,mobile = ? ,isDefault = ?,subbranch=?,bankId=?,accountType= ? where id = ?";
		int temp = jdbcTemplate.update(sql, businessBank.getBusinessId(),
				businessBank.getType(), businessBank.getBankName(),
				businessBank.getAccountNumber(), businessBank.getAccountName(),
				businessBank.getMobile(), businessBank.getIsDefault(),businessBank.getSubbranch(),businessBank.getBankId(),businessBank.getAccountType(),businessBank.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public BusinessBank getById(Long id,Long businessId) {

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select s.* from t_business_bank s where s.id = ? and s.businessId = ?", id,businessId);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(BusinessBank.class, list.get(0));
	}

	// 管理员用
	public BusinessBank getById(Long id) {

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select s.* from t_business_bank s where s.id = ? ", id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(BusinessBank.class, list.get(0));
	}
	
	public Page getBusinessBank(Long businessId, Integer type, String bankName,
			Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_business_bank where 1=1 ");

		if (null != businessId) {
			querySQL.append(" and businessId = ?");
			parameters.add(businessId);
		}

		if (StringUtils.isNotBlank(bankName)) {
			querySQL.append(" and bankName like ?");
			parameters.add("%" + bankName + "%");
		}

		if (null != type) {
			querySQL.append(" and type = ?");
			parameters.add(type);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select *"
				+ querySQL.toString() + " order by id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(BusinessBank.class, list));

		return page;
	}

	public BusinessBank getBusinessBank(Long businessId, Integer type,
			String accountNumber) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select accountNumber from t_business_bank where businessId = ? and type = ? and accountNumber = ?",
						businessId, type, accountNumber);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(BusinessBank.class, list.get(0));

	}
	
	public BusinessBank getBusinessBank(Long id,Long businessId, Integer type,
			String accountNumber) {
		List<Map<String, Object>> list=null;
		if(id==null){
			list = jdbcTemplate
					.queryForList(
							"select accountNumber from t_business_bank where businessId = ? and type = ? and accountNumber = ?",
							businessId, type, accountNumber);
		}else{
			
			list = jdbcTemplate
					.queryForList(
							"select accountNumber from t_business_bank where businessId = ? and type = ? and accountNumber = ? and id <> ?",
							businessId, type, accountNumber,id);
		}
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(BusinessBank.class, list.get(0));

	}

	public BusinessBank getBusinessBank(Long businessId, Integer type) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select accountNumber from t_business_bank where businessId = ? and type = ? and isDefault = 1",
						businessId, type);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(BusinessBank.class, list.get(0));
	}

	public boolean updateIsDefault(BusinessBank businessBank) {
		String sql = "update t_business_bank set isDefault = 0 where  businessId = ?";
		int temp = jdbcTemplate.update(sql, businessBank.getBusinessId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean setDefault(Long id, Long storeId) {
		jdbcTemplate
				.update("update t_business_bank set isDefault = 0 where businessId = ? and isDefault = 1",
						storeId);
		String sql = "update t_business_bank set isDefault = 1 where  id = ? and businessId = ?";
		int temp = jdbcTemplate.update(sql, id, storeId);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<BusinessBank> getConnectBusinessBank(Long ownerId, Integer type) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_business_bank b left join t_bank tb on b.bankId = tb.id where 1=1");

		if (null != ownerId) {
			querySQL.append(" and b.businessId = ?");
			parameters.add(ownerId);
		}

		if (null != type) {
			querySQL.append(" and b.type = ?");
			parameters.add(type);
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select b.*,tb.iconUrl"
				+ querySQL.toString() + " order by b.isDefault desc ",
				parameters.toArray());
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(BusinessBank.class, list);
	}

	public boolean deleteBankCard(Long storeId, String cardId) {
		int temp = jdbcTemplate.update( "delete from t_business_bank  where businessId = ? and id = ? and type = ? ", storeId, cardId,Constants.BusinessBank.STORE_TYPE);
		return temp > 0 ? true : false;
	}

	public Page getBusinessBank(String storeIdArray, Integer type, String bankName, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(
				" from t_business_bank bb where 1=1 ");

		if (StringUtils.isNotBlank(storeIdArray)) {
			querySQL.append(" and bb.businessId in( "+storeIdArray+" )");
		}

		if (StringUtils.isNotBlank(bankName)) {
			querySQL.append(" and bb.bankName like ?");
			parameters.add("%" + bankName + "%");
		}

		if (null != type) {
			querySQL.append(" and bb.type = ?");
			parameters.add(type);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select bb.*,(select name from t_store s where s.id = bb.businessId) as storeName"
				+ querySQL.toString() + " order by id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(BusinessBank.class, list));

		return page;
	}
}