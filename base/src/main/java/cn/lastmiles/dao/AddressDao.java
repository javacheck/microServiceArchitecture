package cn.lastmiles.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Address;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class AddressDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Address get(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"select addr.*,a.fullName as areaFullName from t_user_address addr left join t_area a on addr.areaId = a.id where addr.id = ? ",id);
		return list.isEmpty() ? new Address() : BeanUtils.toBean(Address.class, list.get(0));
	}

	public void saveAddress(Address address) {
		String sql = "insert into t_user_address(id,name,address,phone,createdTime,isDefault,userId,areaId) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, address.getId(), address.getName(),
				address.getAddress(), address.getPhone(), new Date(),
				address.getIsDefault(), address.getUserId(),
				address.getAreaId());

	}

	public void updateAddress(Address address) {
		String sql = "update t_user_address set name=? ,phone=? , address=? , createdTime=? , isDefault=?  , areaId=? where id=?";
		jdbcTemplate.update(sql, address.getName(), address.getPhone(),
				address.getAddress(), new Date(), address.getIsDefault(),
				address.getAreaId(), address.getId());

	}

	public void updateDefault(Long userId) {
		String sql = "update t_user_address set isDefault=? where userId=?";
		jdbcTemplate.update(sql, 0, userId);

	}

	public List<Address> findAddressByUserId(Long userId) {
		String sql = "select id,name,address,phone,createdTime,isDefault,userId,areaId,(select fullName from t_area where id=areaId) as areaFullName from t_user_address where userId=? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId);
		return list.isEmpty() ? null : BeanUtils.toList(Address.class, list);
	}

	public void delete(Long id, Long userId) {
		jdbcTemplate.update(
				"delete from t_user_address where id = ? and userId = ?", id,
				userId);
	}

	public Address findById(Long id) {
		String sql = "select id,name,address,phone,createdTime,isDefault,userId,areaId,(select fullName from t_area where id=areaId) as areaFullName from t_user_address where id=? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		return list.isEmpty() ? null : BeanUtils.toBean(Address.class, list.get(0));
	}

	public void setDefaultAddress(Long addressId, Long userId) {
		jdbcTemplate.update("update t_user_address set isDefault = 0 where isDefault = 1 and userId = ?", userId);
		jdbcTemplate.update("update t_user_address set isDefault = 1 where id = ? and userId = ?", addressId,userId);
	}
}
