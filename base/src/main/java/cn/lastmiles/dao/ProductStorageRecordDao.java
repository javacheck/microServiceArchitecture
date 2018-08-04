package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductStorageRecord;
import cn.lastmiles.bean.ProductStorageRecordStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
@Repository
public class ProductStorageRecordDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page findAllPage(String storeIdString, String storageNumber, String mobile,
			String beginTime, String endTime, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer and = new StringBuffer();
		
		if (StringUtils.isNotBlank(storeIdString)) {
			and.append(" and psr.storeId in( " +storeIdString+" ) ");
		}
		if (StringUtils.isNotBlank(storageNumber)) {
			and.append(" and psr.storageNumber like ?");
			parameters.add("%" + storageNumber + "%");
		}
		if (StringUtils.isNotBlank(mobile)) {
			and.append(" and a.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			and.append(" and psr.createdTime >= ?");
			parameters.add(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			and.append(" and psr.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}
		
		Integer total = jdbcTemplate.queryForObject("select count(1)"
				+ "from  t_account a left join t_product_storage_record psr on a.id=psr.accountId "
				+ "where 1=1 " + and.toString(), Integer.class,
				parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select psr.*,a.mobile,(select name from t_store s where s.id=psr.storeId) as storeName "
						+ "from  t_account a left join t_product_storage_record psr on a.id=psr.accountId "
						+ "where 1=1 ");
		sql.append(and.toString() + "order by psr.createdTime desc  limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());

		page.setData(BeanUtils.toList(ProductStorageRecord.class, list));
		return page;
	}

	public void saveProductStorageRecord(ProductStorageRecord psr) {
		jdbcTemplate
		.update("insert into t_product_storage_record(id,storageNumber,accountId,storeId,storageTime,createdTime,memo) values(?,?,?,?,?,?,?)",
				psr.getId(), psr.getStorageNumber(),psr.getAccountId(),psr.getStoreId(),psr.getStorageTime(),psr.getCreatedTime(),psr.getMemo());
	}

	public void saveProductStorageRecordStock(
			ProductStorageRecordStock psrs) {
		jdbcTemplate
		.update("insert into t_product_storage_record_stock(productStorageRecordId,stockId,productName,barCode,attributeValues,unitName,costPrice,amount,stock,supplierName,memo) values(?,?,?,?,?,?,?,?,?,?,?)",
				psrs.getProductStorageRecordId(),psrs.getStockId(), psrs.getProductName(),psrs.getBarCode(),psrs.getAttributeValues(),psrs.getUnitName(),psrs.getCostPrice(),psrs.getAmount(),psrs.getStock(),psrs.getSupplierName(),psrs.getMemo());
		
	}

	public void updateStock(Long stockId, Double costPrice, Double stock) {
		jdbcTemplate.update(
				"update t_product_stock set costPrice=?,stock=? where id=?",
				costPrice, stock, stockId);
		
	}
	public void updateStock(Long stockId, Double stock) {
		jdbcTemplate.update(
				"update t_product_stock set stock=? where id=?",
				stock, stockId);
		
	}
	public List<ProductStorageRecordStock> findProductStorageRecordStockById(Long id) {
		
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select * from t_product_storage_record_stock where productStorageRecordId = ? order by stockId desc ",
						id);
		return BeanUtils.toList(ProductStorageRecordStock.class, list);
			
	}

	public boolean save(ProductStorageRecord psr) {
		int result = jdbcTemplate.update("insert into t_product_storage_record (id,storageNumber,accountId,storeId,storageTime,createdTime,memo) values(?,?,?,?,?,?,?)",
		psr.getId(),psr.getStorageNumber(),psr.getAccountId(),psr.getStoreId(),psr.getStorageTime(),psr.getCreatedTime(),psr.getMemo());
		return result > 0 ? true : false;
	}

	public void batchSave(List<Object[]> batchInsertStorageRecordArgs) {
		String sql = "insert into t_product_storage_record_stock(stockId,productName,barCode,attributeValues,unitName,costPrice,amount,stock,supplierName,memo,productStorageRecordId) values(?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql,batchInsertStorageRecordArgs);
	}

}
