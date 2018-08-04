/**
 * createDate : 2016年5月20日上午11:02:27
 */
package cn.lastmiles.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Inform;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class InformDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page list(String storeIdString,String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_inform i where 1=1 ");
		
		if( StringUtils.isNotBlank(name) ){
			querySQL.append(" and instr(i.name,?) > 0 ");
			parameters.add(name);
		}
		if( StringUtils.isNotBlank(storeIdString) ){
			querySQL.append(" and i.storeId in ("+storeIdString+")");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(*) " + querySQL.toString() , Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		querySQL.append(" order by i.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());		
		
		querySQL.insert(0,"select i.*,if(i.isMainStoreId = -1,'所有商家',(select s.name from t_store s where s.id = i.storeId)) as storeName,(select a.mobile from t_account a where a.id = i.accountId ) as createdName ");
		
		page.setData(BeanUtils.toList(Inform.class, jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray())));
		return page;
	}

	public void save(Inform inform) {
		jdbcTemplate.update("insert into t_inform(id,name,content,loseTime,storeId,createdTime,accountId,isMainStoreId) values(?,?,?,?,?,?,?,?)",
				inform.getId(),inform.getName(),
				inform.getContent(),inform.getLoseTime(),
				inform.getStoreId(),inform.getCreatedTime(),
				inform.getAccountId(),inform.getIsMainStoreId());
	}

	public void batchSave(List<Object[]> batchArr) {
		String sql = "insert into t_inform_middle_device(id,informId,deviceId,createdTime,markRead,readTime) values(?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, batchArr);
	}

	public List<Inform> findInformByDeviceId(Long deviceId,String storeIdString) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select i.*,imd.markRead from t_inform i inner join t_inform_middle_device imd on i.id = imd.informId  where  imd.deviceId = ? and i.loseTime >= ? and i.storeId in ("+storeIdString+") order by i.createdTime desc ");
		parameters.add(deviceId);
		// parameters.add(Constants.InformMiddleDevice.NOTREAD);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		parameters.add(DateUtils.parse("yyyy-MM-dd", sdf.format(new Date())));
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Inform.class, list);
		}
		return null;
	}

	public boolean updateIsRead(Long informId, Long deviceId) {
		return jdbcTemplate.update(" update t_inform_middle_device set markRead = ?,readTime = ? where informId = ? and deviceId = ? ",Constants.InformMiddleDevice.ISREAD,new Date(), informId,deviceId) > 0 ? true : false;
	}
}