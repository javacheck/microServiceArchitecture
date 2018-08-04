package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Report;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class ReportDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Report> findAll(String beginTime, String endTime, String storeStr,
			String category, String date,Long accountId) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order_item oi ");
		StringBuffer sql1 = new StringBuffer();
		sql.append(" LEFT JOIN t_order o on oi.orderId=o.id ");
		sql.append(" WHERE 1=1 and o.status=1 ");

		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ? ");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ? ");
			parameters.add(endTime + " 23:59:59");
		}
		if (accountId!=null&& !accountId.equals(Constants.Status.SELECT_ALL)) {
			sql.append(" AND o.accountId in (SELECT id FROM t_account WHERE path like ? OR path LIKE ? OR path LIKE ? OR path LIKE ?)");
			parameters.add("%-"+accountId+"-%");
			parameters.add(accountId+"-%");
			parameters.add("%-"+accountId);
			parameters.add(accountId);
		}
		if (StringUtils.isNotBlank(storeStr)) {
			sql.append(" and o.storeId in ("+storeStr+") ");
		}
		if (StringUtils.isNotBlank(category)) {
			if ("0".equals(category)) {
				sql1.append(" group by productId ");
			} else if ("1".equals(category)) {
				sql1.append(" group by oi.categoryId ");
			} else if ("2".equals(category)) {
				sql1.append(" group by o.storeId ");
			} else {
				sql1.append(" group by o.accountId ");
			}
		}
		if (StringUtils.isNotBlank(date)) {
			if ("0".equals(date)) {
				sql1.append(" ,DATE_FORMAT(o.createdTime,'%Y%m%d') ");
			} else if ("1".equals(date)) {
				sql1.append(" ,DATE_FORMAT(o.createdTime,'%Y%m') ");
			} else if ("2".equals(date)) {
				sql1.append(" ,DATE_FORMAT(o.createdTime,'%Y') ");
			}
		}
		sql.append("");
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select o.accountId,oi.stockId,oi.categoryId,o.storeId,(SELECT productId FROM t_product_stock WHERE id = oi.stockId) as productId,"
						+ "(select name from  t_product where id=(select productId from t_product_stock where id=oi.stockId)) as productName,"
								+ " (select name from t_product_category where id=(select categoryId from t_product where id=(select productId from t_product_stock where id=oi.stockId))) as categoryName, "
								+ " (select name from t_store where id=o.storeId) as storeName, "
								+ " (select name from t_account where id=o.accountId) as accountName, "
								+ " sum(oi.price * oi.discount*oi.amount) as sumPrice,sum(oi.amount) as sumAmount,o.createdTime  "
								+ sql + sql1 + " order by o.id",
						parameters.toArray());
		return BeanUtils.toList(Report.class, list);
	}

	

}
