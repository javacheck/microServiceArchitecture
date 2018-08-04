/**
 * createDate : 2016年1月29日上午10:16:31
 */
package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class BrandDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(BrandDao.class);
	/**
	 * 根据商家ID查询品牌信息
	 * @param storeId 商家ID(为空查询所有品牌信息)
	 * @return 品牌信息列表或者null
	 */
	public List<Brand> getBrandListByStoreId(Long storeId) {
		List<Map<String,Object>> brandList = null;
		if( null == storeId ){
			brandList = jdbcTemplate.queryForList("select * from t_brand b order by b.id desc");
		} else {
			brandList = jdbcTemplate.queryForList("select * from t_brand b where b.storeId = ? order by b.id desc", storeId);
		}
		
		if( null != brandList ){
			return BeanUtils.toList(Brand.class, brandList);
		}
		return null;
	}

	public void batchSave(List<Object[]> addBrand) {
		StringBuilder insertSQL = new StringBuilder(50);
		
		insertSQL.append("insert into t_brand(id,name,storeId)");
		insertSQL.append(" values(?,?,?)");
		
		int[] result = jdbcTemplate.batchUpdate(insertSQL.toString(),addBrand) ;
		logger.debug("批量新增品牌信息{}个",result);
	}
	
}