package cn.lastmiles.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.AlipayAuthSituation;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.v2.dao.AlipayAuthSituationDao;

/**
 * 2016/10/13
 * @author shaoyikun
 *
 */
@Service
public class AlipayAuthSituationService {

	@Autowired
	private AlipayAuthSituationDao alipayAuthSituationDao;
	
	public Page getResult(String storeId,Long code, Page page) {
		return alipayAuthSituationDao.getResult(storeId,code, page);
	}

	public AlipayAuthSituation findDetailByAuthID(Long getAuthID) {
		return alipayAuthSituationDao.findDetailByAuthID(getAuthID);
	}
}
