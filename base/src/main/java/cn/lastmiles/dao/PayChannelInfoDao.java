package cn.lastmiles.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PayChannelInfo;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class PayChannelInfoDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private StoreDao storeDao;

	private final static Logger logger = LoggerFactory
			.getLogger(PayChannelInfoDao.class);
	
	public String findWXSubMchID(Long storeId){
		List<String> list = jdbcTemplate.queryForList("select subMchID from t_pay_channel_info where storeId = ? and type = 1 ", String.class, storeId);
		if (!list.isEmpty()){
			return list.get(0);
		}
		Store store = storeDao.findTopStore(storeId);
		list = jdbcTemplate.queryForList("select subMchID from t_pay_channel_info where storeId = ? and type = 1 ", String.class, store.getId());
		if (!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public Page findAll(Long storeId, Integer type, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("storeId", storeId);
		map.put("type", type);

		Integer total = JdbcUtils.queryForObject("payChannelInfo.findTotal",
				map, Integer.class);
		logger.debug("total={}", total);
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		logger.debug("page.getStart()={},page.getPageSize()={}",
				page.getStart(), page.getPageSize());
		map.put("startNumber", page.getStart());
		map.put("pageSize", page.getPageSize());

		page.setData(JdbcUtils.queryForList("payChannelInfo.findAllPage", map,
				PayChannelInfo.class));
		return page;
	}

	public PayChannelInfo findPayChannelInfo(PayChannelInfo payChannelInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payChannelInfo.getId());
		map.put("storeId", payChannelInfo.getStoreId());
		map.put("type", payChannelInfo.getType());
		map.put("appId", payChannelInfo.getAppId());
		map.put("appKey", payChannelInfo.getAppKey());
		payChannelInfo = JdbcUtils.queryForObject(
				"payChannelInfo.findPayChannelInfo", map, PayChannelInfo.class);
		return payChannelInfo;
	}

	public void savePayChannelInfo(PayChannelInfo payChannelInfo) {
		JdbcUtils.save(payChannelInfo);

	}

	public void updatePayChannelInfo(PayChannelInfo payChannelInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payChannelInfo.getId());
		map.put("storeId", payChannelInfo.getStoreId());
		map.put("appId", payChannelInfo.getAppId());
		map.put("appKey", payChannelInfo.getAppKey());
		map.put("type", payChannelInfo.getType());
		map.put("certPassword", payChannelInfo.getCertPassword());
		map.put("certLocalPath", payChannelInfo.getCertLocalPath());
		map.put("subMchID", payChannelInfo.getSubMchID());
		map.put("mchID", payChannelInfo.getMchID());
		map.put("certIo", payChannelInfo.getCertIo());
		JdbcUtils.update("payChannelInfo.updatePayChannelInfo", map);

	}

	public PayChannelInfo findById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		PayChannelInfo payChannelInfo = JdbcUtils.queryForObject(
				"payChannelInfo.findById", map, PayChannelInfo.class);
		return payChannelInfo;
	}

	public void deleteById(Long id) {
		logger.debug("id={}", id);
		JdbcUtils.deleteById(PayChannelInfo.class, id);

	}

	public PayChannelInfo findbyType(Integer type) {
		logger.debug("findbyType ---> type={}", type);
		return JdbcUtils.findByObj(PayChannelInfo.class, "type", type);
	}

	public PayChannelInfo findbyStoreIdAndType(Long storeId, Integer type) {
		String sql = "select pci.* from t_pay_channel_info pci where storeId = ? and type = ? ";
		logger.debug("findbyType --->sql is {},type is :{},storeId is :{}",
				sql, type, storeId);
		DefaultLobHandler lobHandler = new DefaultLobHandler();
		List<PayChannelInfo> list = jdbcTemplate.query(sql, new Object[] {
				storeId, type }, new RowMapper<PayChannelInfo>() {
			public PayChannelInfo mapRow(ResultSet rs, int i)
					throws SQLException {
				PayChannelInfo info = new PayChannelInfo();
				info.setAppId(rs.getString("appId"));
				info.setAppKey(rs.getString("appKey"));
				info.setCertIo(lobHandler.getClobAsString(rs, "certIo"));
				info.setCertLocalPath(rs.getString("certLocalPath"));
				info.setCertPassword(rs.getString("certPassword"));
				info.setId(rs.getLong("id"));
				info.setMchID(rs.getString("mchID"));
				info.setStoreId(rs.getLong("storeId"));
				info.setSubMchID(rs.getString("subMchID"));
				info.setType(rs.getInt("type"));
				return info;
			}
		});
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public PayChannelInfo findbyStoreIdAndTypeForPos(Long storeId, Integer type) {
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from t_pay_channel_info where storeId = ? and type = ? ", storeId,type);
		if (list.isEmpty()){
			Store store = storeDao.findTopStore(storeId);
			storeId = store.getId();
		}
		list = jdbcTemplate.queryForList("select * from t_pay_channel_info where storeId = ? and type = ? ", storeId,type);
		if (list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(PayChannelInfo.class, list.get(0));
	}

}
