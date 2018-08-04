package cn.lastmiles.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PayRecord;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class PayRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PayRecord get(Long orderId) {
		return BeanUtils
				.toBean(PayRecord.class,
						jdbcTemplate
								.queryForMap(
										"select `orderId`, `queryId`, `respCode`, `respMsg`, `settleDate`, `traceNo`, `traceTime`, `txnAmt`, `txnTime`, `channel` from t_pay_record where orderId = ?",
										orderId));
	}

	public void save(PayRecord record) {
		jdbcTemplate
				.update("INSERT INTO `t_pay_record` (`orderId`, `queryId`, `respCode`, `respMsg`, `settleDate`, `traceNo`, `traceTime`, `txnAmt`, `txnTime`, `channel`,`status`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						record.getOrderId(), record.getQueryId(),
						record.getRespCode(), record.getRespMsg(),
						record.getSettleDate(), record.getTraceNo(),
						record.getTraceTime(), record.getTxnAmt(),
						record.getTxnTime(), record.getChannel(),record.getStatus());
	}

	public void delete(Long orderId) {
		jdbcTemplate.update("delete from t_pay_record where orderId = ?",
				orderId);
	}
}
