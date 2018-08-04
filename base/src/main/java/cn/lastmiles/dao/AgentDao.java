package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Agent;
import cn.lastmiles.bean.Bank;
import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class AgentDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page getAgentList(Long agentId,String name, Integer type,String mobile,
			String contactName, String path,Page page) {
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_agent a");
		
		sql.append(" WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(name)) {
			sql.append(" and a.name like ?");
			parameters.add("%" + name + "%");
		}
		if(agentId!=null){
			sql.append(" and a.id <> ?");
			parameters.add(agentId);
		}
		if (type!=null) {
			sql.append(" and a.type= ?");
			parameters.add(type);
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		
		if (StringUtils.isNotBlank(contactName)) {
			sql.append(" and a.contactName like ?");
			parameters.add("%" + contactName + "%");
		}
		if (path!="") {
			sql.append(" and a.path like ? ");
			parameters.add("%" +path+"-%");
//			parameters.add(path);
		}
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT a.id,a.name,a.type,a.parentId,(select name from t_agent t where t.id=a.parentId) as parentName,a.mobile,a.contactName  "+ sql + " order by a.createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(Agent.class, list));

		return page;
	}

	public void saveAgent(Long id, String name, Integer type, Long parentId,
			String contactName, String mobile, Long areaId,
			String address, Long createdId,Long accountId,String path) {
		String sql="insert into t_agent(id,name,type,parentId,contactName,mobile,areaId,address,createdId,createdTime,path) values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql,id,name,type,parentId,contactName,mobile,areaId,address,createdId,new Date(),path);
		
	}

	public void updateAgent(Long id, String name, Integer type, Long parentId,
			String contactName, String mobile, Long areaId,
			String address, Long updatedId,String path) {
		String sql="update t_agent set name=? ,type=? ,parentId=?,contactName=?,mobile=?,areaId=?,address=?,updatedId=?,updatedTime=?,path=? where id=? ";
		jdbcTemplate.update(sql,name,type,parentId,contactName,mobile,areaId,address,updatedId,new Date(),path,id);
	}

	public Agent findAgent(Long id, String name, Integer type, Long parentId,
			String contactName, Long areaId, String address) {
		String sql="";
		if(id==null){
			if(parentId==null){
				sql="select id,name,type,parentId,contactName, "
						+ "areaId,address,path from t_agent where  name=? and type=? and parentId is null and contactName=?   and areaId=? and address=? ";
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(sql,name,type,contactName,areaId,address);
				return list.isEmpty()?null: BeanUtils.toBean(Agent.class, list.get(0));
			}else{
				sql="select id,name,type,parentId,contactName,mobile, "
						+ "areaId,address,path from t_agent where name=? and type=? and parentId =? and contactName=?   and areaId=? and address=? ";
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(sql,name,type,parentId,contactName,areaId,address);
				return list.isEmpty()?null: BeanUtils.toBean(Agent.class, list.get(0));
			}
		}else{
			if(parentId==null){
				sql="select id,name,type,parentId,contactName, "
						+ "areaId,address,path from t_agent where id<>? and name=? and type=? and parentId is null and contactName=?   and areaId=? and address=? ";
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(sql,id,name,type,contactName,areaId,address);
				return list.isEmpty()?null: BeanUtils.toBean(Agent.class, list.get(0));
			}else{
				sql="select id,name,type,parentId,contactName,mobile, "
						+ "areaId,address,path from t_agent where id<>? and name=? and type=? and parentId =? and contactName=?   and areaId=? and address=? ";
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(sql,id,name,type,parentId,contactName,areaId,address);
				return list.isEmpty()?null: BeanUtils.toBean(Agent.class, list.get(0));
			}
		}
	}

	public Agent findById(Long id) {
		String sql="select a.id,a.name,a.type,a.parentId,(select name from t_agent t where t.id=a.parentId) as parentName,a.contactName,a.mobile, "
				+ "a.areaId,a.address,a.path,(select path from t_area  r where r.id=a.areaId) as areaPath from t_agent a where a.id=? ";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql,id);
		return list.isEmpty()?null: BeanUtils.toBean(Agent.class, list.get(0));
	}

	public Page getBusinessList(Long businessId,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_business_bank b");
		
		sql.append(" WHERE 1=1 ");
		
		if (businessId!=null) {
			sql.append(" and b.businessId = ?");
			parameters.add(businessId);
		}
		
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("SELECT b.id,b.businessId,b.type,b.bankName,b.accountNumber,b.accountName,b.mobile,b.isDefault,b.createdTime  "+ sql + " order by b.createdTime desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(BusinessBank.class, list));

		return page;
	}

	public List<Bank> findBankList() {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select id,code,name,iconUrl from t_bank order by id");
		return list.isEmpty()?null: BeanUtils.toList(Bank.class, list);
	}

	public BusinessBank findBusinessBank(Long id,Long businessId, Integer type,String bankName,
			String accountNumber,String accountName, String mobile) {
		String sql="";
		if(id==null){
			sql="select id,businessId,type,bankName,accountNumber,accountName,mobile from t_business_bank "
					+ " where businessId=? and type=? and bankName=? and accountNumber=? and accountName=? and mobile=?";
			List<Map<String, Object>> list = jdbcTemplate
					.queryForList(sql,businessId,type,bankName,accountNumber,accountName,mobile);
			return list.isEmpty()?null: BeanUtils.toBean(BusinessBank.class, list.get(0));
		}else{
			sql="select id,businessId,type,bankName,accountNumber,accountName,mobile from t_business_bank "
					+ " where id <>? and businessId=? and type=? and bankName=? and accountNumber=? and accountName=? and mobile=?";
			List<Map<String, Object>> list = jdbcTemplate
					.queryForList(sql,id,businessId,type,bankName,accountNumber,accountName,mobile);
			return list.isEmpty()?null: BeanUtils.toBean(BusinessBank.class, list.get(0));
		}
	}

	public void saveBusinessBank(Long id, Long businessId,Integer type, String bankName,
			String accountNumber, String accountName, String mobile,
			Integer isDefault,String subbranch,Long bankId) {
		String sql="insert into t_business_bank(id,businessId,type,bankName,accountNumber,"
				+ "accountName,mobile,isDefault,createdTime,subbranch,bankId) values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql,id,businessId,type,bankName,accountNumber,accountName,mobile,isDefault,new Date(),subbranch,bankId);
		
	}

	public void updateBusinessBank(Long id, Long businessId, String bankName,
			String accountNumber, String accountName, String mobile,
			Integer isDefault,String subbranch,Long bankId) {
		String sql="update t_business_bank set bankName=? ,accountNumber=? ,accountName=?,mobile=?,isDefault=?,createdTime=?,subbranch=?,bankId=? where id=? ";
		jdbcTemplate.update(sql,bankName,accountNumber,accountName,mobile,isDefault,new Date(),subbranch,bankId,id);
		
	}

	public BusinessBank findByBusinessBankId(Long id) {
		String sql="select id,businessId,bankName,accountNumber,accountName,mobile,isDefault,subbranch,bankId from t_business_bank where id=? ";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql,id);
		return list.isEmpty()?null: BeanUtils.toBean(BusinessBank.class, list.get(0));
	}

	public void updateDefault(Long businessId) {
		String sql="update t_business_bank set isDefault=0  where businessId=? ";
		jdbcTemplate.update(sql,businessId);
		
	}

	public void delByBusinessBankId(Long id) {
		String sql="delete from t_business_bank where id=? ";
		jdbcTemplate.update(sql,id);
		
	}

	


}
