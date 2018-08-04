package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.PublishImage;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class PublishImageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 存储图片
	 * @param imageList
	 */
	public void save(List<PublishImage> imageList) {
		String sql = "insert into t_publish_image(PUBLISHID,IMAGEID,SUFFIX,ORDERSORT,TYPE) values(?,?,?,?,?)";

		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (PublishImage image : imageList) {
			Object[] arg = new Object[5];
			arg[0] = image.getPublishId();
			arg[1] = image.getImageId();
			arg[2] = image.getSuffix();
			arg[3] = image.getOrderSort();
			arg[4] = image.getType();
			batchArgs.add(arg);
		}

		jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	
	public List<PublishImage> findByPublishId(Long publishId) {
		String sql = "SELECT PUBLISHID,IMAGEID,SUFFIX,ORDERSORT,TYPE FROM t_publish_image WHERE PUBLISHID = ? order by type,orderSort";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,publishId);
		return BeanUtils.toList(PublishImage.class, list);
	}

	public void delete(Long publishId) {
		String sql = "delete from t_publish_image where publishId = ?";
		jdbcTemplate.update(sql,publishId);
	}
}
