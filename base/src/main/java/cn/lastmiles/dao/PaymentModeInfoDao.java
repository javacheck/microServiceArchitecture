package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PaymentModeInfo;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class PaymentModeInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保存方法
	 * 
	 * @param paymentModeInfo
	 * @return
	 */
	public void save(PaymentModeInfo paymentModeInfo) {
		String sql = "INSERT INTO t_paymentMode_info(id,associationId,paymentMode,amount,price) "
				+ "VALUES (?,?,?,?,?)";
		jdbcTemplate.update(sql, paymentModeInfo.getId(),
				paymentModeInfo.getAssociationId(),
				paymentModeInfo.getPaymentMode(), paymentModeInfo.getAmount(),
				paymentModeInfo.getPrice());
	}
	
	/**
	 * 保存方法
	 * 
	 * @param paymentModeInfo
	 * @return
	 */
	public List<PaymentModeInfo> findByAssociationId(Long associationId) {
		String sql = "SELECT id,associationId,paymentMode,amount,price "
				+ " from t_paymentMode_info where associationId = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, associationId);
		return BeanUtils.toList(PaymentModeInfo.class, list);
	}
}
