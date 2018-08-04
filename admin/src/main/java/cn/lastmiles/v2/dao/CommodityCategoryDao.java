/**
 * createDate : 2016年5月27日上午11:07:21
 */
package cn.lastmiles.v2.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class CommodityCategoryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ProductCategory> findByStoreId(Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select * from t_product_category where 1=1 ");
		if( null != storeId ){
			querySQL.append(" and storeId = ? ");
			parameters.add(storeId);
		}
		
		querySQL.append(" order by sort");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		return BeanUtils.toList(ProductCategory.class,list);
	}

	public List<ProductCategory> findByStoreIdAndId(Long storeId,Long id) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer("select * from t_product_category where 1=1 ");
		if( null != storeId ){
			querySQL.append(" and storeId = ? ");
			parameters.add(storeId);
		}
		
		if( null != id ){
			querySQL.append(" and path not like ? ");
			parameters.add("%_" + id + "%");
		}
		
		querySQL.append(" order by sort");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(),parameters.toArray());
		if( null != list && list.size() > 0 ){
			return BeanUtils.toList(ProductCategory.class,list);
		}
		return null;
	}
	
	public ProductCategory findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select pc.*,(select p.name from t_product_category p where p.id = pc.pId ) parentName from t_product_category pc where pc.id = ? ",id);
		if(list.isEmpty()){
			return null;
		}
		return BeanUtils.toBean(ProductCategory.class,list.get(0));
	}

	public void save(ProductCategory productCategory) {
		JdbcUtils.save(productCategory);
	}

	public void update(ProductCategory productCategory) {
		String[] noUpdateColumn = {"storeId","type"};
		JdbcUtils.update(productCategory, noUpdateColumn);
	}

	public int deleteById(Long id) {
		String deleteSQL = "delete from t_product_category where path like ? or path like ? ";
		return jdbcTemplate.update(deleteSQL, ("%_"+id+"%"),("%"+id+"%") ); 
	}

	public int existCategoryName(String name, Long id,Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" select count(*) from t_product_category s where s.name = ? and s.storeId = ? ");
		parameters.add(name);
		parameters.add(storeId);
		
		if( null != id ){
			querySQL.append(" and s.id <> ?");
			parameters.add(id);
		}
		return jdbcTemplate.queryForObject(querySQL.toString(), Integer.class,parameters.toArray());
	}
}
