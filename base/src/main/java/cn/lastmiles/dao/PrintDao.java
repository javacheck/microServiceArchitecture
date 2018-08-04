package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Print;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class PrintDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Page findAll(Long storeId, String printSn,Integer status, Page page) {
		
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(printSn)) {
			and.append(" and t.printSn like ?");
			parameters.add("%" + printSn + "%");
		}
		
		if( null != storeId){
			and.append(" and t.storeId = ?");
			parameters.add(storeId);
		}
		if( null != status){
			and.append(" and t.status = ?");
			parameters.add(status);
		}
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_print t where 1=1" + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.*, "
				+ "(select name from t_store s where s.id=t.storeId) as storeName "
				+ " from t_print t where 1=1 ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Print.class, list));
		
		return page;
	}

	public Print findPrint(Print print) {
		System.out.println(print.getPrintSn()+"===="+print.getPrintKey());
		if(print.getId()==null){
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select t.* from t_print t where  t.printSn=? and t.printKey=?",print.getPrintSn(),print.getPrintKey());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Print.class, list.get(0));
		}else{
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select t.* from t_print  t where t.id<>?  and t.printSn=? and t.printKey=?",print.getId(),print.getPrintSn(),print.getPrintKey());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Print.class, list.get(0));
		}
	}

	public void save(Long id, Long storeId,String printName, String printSn, String printKey,String memo,String categoryIds,String categoryNames) {
		jdbcTemplate.update(
				"insert into t_print(id,storeId,printName,printSn,printKey,status,memo,categoryIds,categoryNames,updatedTime) values(?,?,?,?,?,?,?,?,?,?)", id,
				storeId,printName,printSn,printKey,1,memo,categoryIds,categoryNames,new Date());
		
	}

	public void update(Long id, Long storeId,String printName, String printSn, String printKey,String memo,String categoryIds,String categoryNames) {
		jdbcTemplate.update("update t_print set storeId=?,printName=?,printSn=?,printKey=?,memo=?,categoryIds=?,categoryNames=?,updatedTime = ?  where id=?",storeId,printName,
				printSn, printKey,memo,categoryIds,categoryNames,new Date(),id);
		
	}

	public Print findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.*,(select name from t_store s where s.id=t.storeId) as storeName from t_print t  where t.id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Print.class, list.get(0));
	}

	public void deleteById(Long id) {
		jdbcTemplate.update(
				"delete from t_print  where id = ?", id);
		
	}

	public Print findByStoreId(Long storeId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.*,(select name from t_store s where s.id=t.storeId) as storeName from t_print t  where t.storeId = ?", storeId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Print.class, list.get(0));
	}

	public List<Print> findListByStoreId(Long storeId,Integer status) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.*,(select name from t_store s where s.id=t.storeId) as storeName from t_print t  where t.storeId = ? and t.status=?", storeId,status);
		
		return BeanUtils.toList(Print.class, list);
	}

	public List<ProductCategory> findCategoryList(Long storeId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.id,t.name from t_product_category t  where t.storeId = ? and t.pId is null", storeId);
		
		return BeanUtils.toList(ProductCategory.class, list);
	}

	public void typeChangeByPrintId(Long id, Integer status) {
		jdbcTemplate.update("update t_print set status=?  where id=?",status,id);
	}

}
