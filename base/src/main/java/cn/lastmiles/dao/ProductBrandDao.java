package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class ProductBrandDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public  Page findAll(String storeIdString, String name, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		if (StringUtils.isNotBlank(name)) {
			and.append(" and t.name like ?");
			parameters.add("%" + name + "%");
		}
		
		if(StringUtils.isNotBlank(storeIdString)){
			and.append(" and t.storeId in("+storeIdString+")");
		}
		
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_brand t where 1=1 " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.name, "
				+ "t.storeId,(select name from t_store s where s.id=t.storeId) as storeName "
				+ " from t_brand t where 1=1 ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Brand.class, list));
		
		return page;
	}

	public Brand findProductBrand(Brand brand) {
		if(brand.getId()==null){
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,storeId from t_brand where name = ? and storeId=?", brand.getName(),brand.getStoreId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Brand.class, list.get(0));
		}else{
			List<Map<String, Object>> list=jdbcTemplate
					.queryForList(
					"select id,name,storeId from t_brand where id<>? and name = ? and storeId=?",brand.getId(), brand.getName(),brand.getStoreId());
			if (list.isEmpty()) {
				return null;
			}
			return BeanUtils.toBean(Brand.class, list.get(0));
		}
	}

	public void save(Brand brand) {
		jdbcTemplate.update(
				"insert into t_brand(id,name,storeId) values(?,?,?)", brand.getId(),
				brand.getName(),brand.getStoreId());
	}

	public void update(Brand brand) {
		jdbcTemplate.update("update t_brand set name=? where id=?",
				brand.getName(), brand.getId());
	}

	public Brand findById(Long id) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
				"select t.id,t.name,t.storeId,(select name from t_store s where s.id=t.storeId) as storeName from t_brand t  where t.id = ?", id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Brand.class, list.get(0));
	}

	public void deleteById(Long id) {
		jdbcTemplate.update(
				"delete from t_brand  where id = ?", id);
		
	}

	public List<Brand> findBrandListByStoreId(Long storeId) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(
						"select id,name,storeId from t_brand where storeId = ? order by id",
						storeId);
		
		return BeanUtils.toList(Brand.class, list);
	}
}
