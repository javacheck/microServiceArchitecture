package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductDeliveryRecord;
import cn.lastmiles.bean.ProductDeliveryRecordStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ProductDeliveryRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page findAllPage(String storeIdString, String deliveryNumber, String mobile,
			String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer and = new StringBuffer();
		
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and pdr.storeId in( " +storeIdString+" ) ");
		}
		if (StringUtils.isNotBlank(deliveryNumber)) {
			and.append(" and pdr.deliveryNumber like ?");
			parameters.add("%" + deliveryNumber + "%");
		}
		if (StringUtils.isNotBlank(mobile)) {
			and.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			and.append(" and pdr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			and.append(" and pdr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from  t_account a left join t_product_delivery_record pdr on a.id=pdr.accountId "
				+ "where 1=1 " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select pdr.*,a.mobile,(select name from t_store s where s.id=pdr.storeId) as storeName "
						+ "from  t_account a left join t_product_delivery_record pdr on a.id=pdr.accountId "
						+ "where 1=1 ");
		sql.append(and.toString() + "order by pdr.createdTime desc  limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductDeliveryRecord.class, list));
		return page;
	}

	public void saveProductDeliveryRecord(ProductDeliveryRecord pdr) {
		jdbcTemplate
		.update("insert into t_product_delivery_record(id,deliveryNumber,accountId,storeId,deliveryTime,createdTime,memo) values(?,?,?,?,?,?,?)",
				pdr.getId(), pdr.getDeliveryNumber(),pdr.getAccountId(),pdr.getStoreId(),pdr.getDeliveryTime(),pdr.getCreatedTime(),pdr.getMemo());
	}

	public void saveProductDeliveryRecordStock(
			ProductDeliveryRecordStock pdrs) {
		jdbcTemplate
		.update("insert into t_product_delivery_record_stock(productDeliveryRecordId,stockId,productName,barCode,attributeValues,unitName,costPrice,amount,stock,shipmentPrice,memo) values(?,?,?,?,?,?,?,?,?,?,?)",
				pdrs.getProductDeliveryRecordId(),pdrs.getStockId(), pdrs.getProductName(),pdrs.getBarCode(),pdrs.getAttributeValues(),pdrs.getUnitName(),
				pdrs.getCostPrice(),pdrs.getAmount(),pdrs.getStock(),pdrs.getShipmentPrice(),pdrs.getMemo());
		
	}

	public void updateStock(Long stockId, Double stock) {
		jdbcTemplate.update(
				"update t_product_stock set stock=? where id=?",
				stock, stockId);
		
	}

	public List<ProductDeliveryRecordStock> findProductDeliveryRecordStockById(Long id) {
		
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_delivery_record_stock where productDeliveryRecordId = ? order by stockId desc ",
						id);
		return BeanUtils.toList(ProductDeliveryRecordStock.class, list);
			
	}
}
