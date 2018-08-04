package cn.lastmiles.dao;

import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.PointDetail;
import cn.lastmiles.common.utils.JdbcUtils;

@Repository
public class PointDetailDao {

	public void save(PointDetail pointDetail) {
		JdbcUtils.save(pointDetail);
	}

}
