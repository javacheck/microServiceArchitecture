package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class DeviceDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 分页查找
	 * @param storeIds 
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(List<Store> stores, String storeName,String deviceSn, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_device d  LEFT JOIN t_store s ON  d.storeId = s.id  where 1 = 1 ");
		
		// 查出传入店铺的所有商品和库存信息
		if(StringUtils.isNotBlank(storeName)){
			sql.append(" and s.name like ?");
			parameters.add("%" + storeName + "%");
		}
		
		if (StringUtils.isNotBlank(deviceSn)) {
			sql.append(" and d.deviceSn like ? ");
			parameters.add("%" + deviceSn + "%");
		}
		if (stores!=null) {
			sql.append(" and ( 1 != 1 ");
			for (Store store : stores) {
				sql.append(" or d.storeId = ? ");
				parameters.add(store.getId());
			}
			sql.append(" ) ");
			
		}
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql ,
				Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"SELECT  d.serialId,d.id, d.deviceSn, d.deviceName, d.storeId,	d.status,d.factory,d.model,(select s.name from t_store s where s.id = d.storeId) as storeName "
								+ sql + " order by d.storeId desc limit ?,?",
						parameters.toArray());
		page.setData(BeanUtils.toList(Device.class, list));

		return page;
	}
	 

	/**
	 * 更新方法
	 * @param device
	 * @return
	 */
	public boolean update(Device device) {
		String sql = "update t_device set deviceSn=?, deviceName =?, storeId = ? , factory = ? , status = ? ,model = ?,serialId = ? "
					+ " where id = ?";
		int temp = jdbcTemplate.update(sql, device.getDeviceSn(),
				device.getDeviceName(),device.getStoreId(),device.getFactory(),device.getStatus(),device.getModel(),device.getSerialId(),
				device.getId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存方法
	 * @param device
	 * @return
	 */
	public boolean save(Device device) {
		String sql = "INSERT INTO t_device(deviceSn,deviceName,storeId,id,status,factory,model,serialId) "
				+ "VALUES (? ,? ,? ,?, ? ,? ,?,?)";
		int temp = jdbcTemplate.update(sql, device.getDeviceSn(),
				device.getDeviceName(),device.getStoreId(),device.getId(),device.getStatus(),device.getFactory(),device.getModel(),device.getSerialId());
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Device findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select *,(select s.name from t_store s where s.id = t.storeId) as storeName from t_device t where id = ?",
						id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(Device.class, list.get(0));
	}
	
	/**
	 * 通过ID删除
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		int temp= jdbcTemplate.update("DELETE FROM  t_device  where id = ? ",id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Device checkDeviceSn(Long id,String deviceId) {
		
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select * from t_device where deviceSn = ?");
		parameters.add(deviceId);
		if( null != id ){
			sb.append(" and id <> ?");
			parameters.add(id);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(),parameters.toArray());
		return list.isEmpty() ? null : BeanUtils.toBean(Device.class,	list.get(0));
	}

	public Device checkSerialId(Long id,String serialId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select * from t_device where serialId = ?");
		parameters.add(serialId);
		if( null != id ){
			sb.append(" and id <> ?");
			parameters.add(id);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString(),parameters.toArray());
		return list.isEmpty() ? null : BeanUtils.toBean(Device.class,	list.get(0));
	}
	
	public Device findByDeviceSn(String deviceSn) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select *  from t_device t where deviceSn = ?",deviceSn);
		return list.isEmpty() ? null:BeanUtils.toBean(Device.class, list.get(0));
	}

	public Device findByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_device t where storeId = ? ",storeId);
		return list.isEmpty() ? null:BeanUtils.toBean(Device.class, list.get(0));
	}


	public Device findBySerialId(String serialId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select id,deviceSn,deviceName,storeId,model,status,factory,serialId from t_device t where serialId = ? and status = ?",serialId,Constants.NORMAL);
		return list.isEmpty() ? null:BeanUtils.toBean(Device.class, list.get(0));
	}


	public List<Device> findDeviceListByStoreId(String storeIdString) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select t.* from t_device t where t.storeId in ("+storeIdString+")");
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(Device.class, list);
		}
		return null;
	}
	

}