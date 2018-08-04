package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.HtmlAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class HtmlAdDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<HtmlAd> findAll(){
		return BeanUtils.toList(HtmlAd.class, jdbcTemplate.queryForList("select * from t_html_ad where valid = 0"));
	}

	public Page findAll(Long storeId, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		
		if( null != storeId){
			and.append(" and t.storeId = ?");
			parameters.add(storeId);
		}
		
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_html_ad t where 1=1 " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.url,t.title,t.imageId,t.storeId,valid,(select name from t_store s where s.id=t.storeId) as storeName,t.cityId,(select fullName from t_area a where a.id=t.cityId) as cityName from t_html_ad t where 1=1 ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(HtmlAd.class, list));
		
		return page;
	}

	public void saveHtmlAd(HtmlAd htmlAd) {
		jdbcTemplate
		.update("insert into t_html_ad(id,url,title,imageId,storeId,cityId,share,valid) "
				+ "values(?,?,?,?,?,?,?,?)",
				htmlAd.getId(),htmlAd.getUrl(),htmlAd.getTitle(),
				htmlAd.getImageId(),htmlAd.getStoreId(),htmlAd.getCityId(),htmlAd.getShare(),htmlAd.getValid() );
		
	}

	public HtmlAd findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList("select t.id,t.url,t.title,t.imageId,t.storeId,valid,(select name from t_store s where s.id=t.storeId) as storeName,t.cityId,(select path from t_area a where a.id=t.cityId) as areaPath,share from t_html_ad t where id = ? ",id);
		if (list.isEmpty() || null == list) {
			return null;
		}
		return BeanUtils.toBean(HtmlAd.class, list.get(0));
	}

	public void updateHtmlAd(HtmlAd htmlAd) {
		jdbcTemplate
		.update("update t_html_ad set url=?,title=?,imageId=?,cityId=?,share=?,valid=?,storeId=? where id=?",htmlAd.getUrl(),htmlAd.getTitle(),
				htmlAd.getImageId(),htmlAd.getCityId(),htmlAd.getShare(),htmlAd.getValid(),htmlAd.getStoreId(),htmlAd.getId() 
				);
		
	}

	public boolean delete(Long id) {
		int temp= jdbcTemplate.update("delete from t_html_ad  where id = ? ",id);
		if (temp > 0) {
			return true;
		} else {
			return false;
		}
	}
}
