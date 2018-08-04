package cn.lastmiles.dao;
/**
 * updateDate : 2015-07-17 AM 10:50
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.APK;
import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.SalesPromotionCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class ApkDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public APK getLastest(int type){
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_apk where type = " + type + " order by id desc limit 1");
		if (list.isEmpty()){
			return null;
		}else {
			return BeanUtils.toBean(APK.class, list.get(0));
		}
	}
	
	/**
	 * APK查询
	 * @param page
	 */
	public Page list(Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_apk where 1 = 1 ");
			
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql.toString() , Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select  * " + sql + " order by id desc limit ?,?", parameters.toArray());
		page.setData(BeanUtils.toList(APK.class, list));

		return page;
	}

	/**
	 * 检测APK名称是否唯一
	 * @param name APK名称
	 * @return APK对象或者null
	 */
	public boolean checkApkName(Long id,String name) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_apk a where a.name = ? ");
		parameters.add(name);
		if( null != id ){
			querySQL.append(" and a.id <> ?");
			parameters.add(id);
		}
		int total = jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
		if(total > 0 ){
			return true;
		}
		return false;
	}

	/**
	 * 保存APK信息
	 * @param apk APK对象
	 */
	public Boolean save(APK apk) {
		String sql = "insert into t_apk(id,name,version,memo,createdTime,fileId,type,needingUpdate) "
				+ "values (? ,? ,? ,?, ? ,? ,?,?)";
		int temp = jdbcTemplate.update(sql, apk.getId(),apk.getName(),apk.getVersion(),apk.getMemo(),new Date(),apk.getFileId(),apk.getType(),apk.getNeedingUpdate());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean update(APK apk) {
		String sql = "update t_apk set name = ?,version = ? , memo = ? ,fileId = ? ,type = ? , needingUpdate = ? where id = ?";
		int temp = jdbcTemplate.update(sql, apk.getName(),apk.getVersion(),apk.getMemo(),apk.getFileId(),apk.getType(),apk.getNeedingUpdate(),apk.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public APK findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_apk t where id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(APK.class, list.get(0));
	}
}