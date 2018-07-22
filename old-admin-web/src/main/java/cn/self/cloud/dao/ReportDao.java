package cn.self.cloud.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.Report;
import cn.self.cloud.commonutils.basictype.StringUtils;
import cn.self.cloud.commonutils.reflec.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Report> findAll(String beginTime, String endTime, String store,
								String category, String date) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order_item oi ");
		StringBuffer sql1 = new StringBuffer();
		sql.append(" LEFT JOIN t_order o on oi.orderId=o.id ");
		sql.append(" WHERE 1=1 and o.status=0 ");

		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		if (StringUtils.isNotBlank(store)) {
			sql.append(" and o.storeId= ?");
			parameters.add(store);
		}
		if (StringUtils.isNotBlank(category)) {
			if ("0".equals(category)) {
				sql1.append(" group by oi.stockId ");
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
			} else if ("1".equals(category)) {
				sql1.append(" ,DATE_FORMAT(o.createdTime,'%Y%m') ");
			} else if ("2".equals(category)) {
				sql1.append(" ,DATE_FORMAT(o.createdTime,'%Y') ");
			}
		}
		sql.append("");
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select o.accountId,oi.stockId,oi.categoryId,o.storeId,(select name from  t_product where id=(select productId from t_product_stock where id=oi.stockId)) as productName,"
								+ " (select name from t_product_category where id=(select categoryId from t_product where id=(select productId from t_product_stock where id=oi.stockId))) as categoryName, "
								+ " (select name from t_store where id=o.storeId) as storeName, "
								+ " (select name from t_account where id=o.accountId) as accountName, "
								+ " sum(oi.price*oi.amount) as sumPrice,sum(oi.amount) as sumAmount,o.createdTime  "
								+ sql + sql1 + " order by o.id",
						parameters.toArray());
		return BeanUtils.toList(Report.class, list);
	}

	public List<Report> findAllBySearch(String beginTime, String endTime,
			String account, String category, String date) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select id,value,attributeId from t_product_attribute_value where value = ? and attributeId=?");
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toList(Report.class, list);
	}

}
