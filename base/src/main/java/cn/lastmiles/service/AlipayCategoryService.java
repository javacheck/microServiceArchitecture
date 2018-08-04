package cn.lastmiles.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.AlipayCategory;
import cn.lastmiles.dao.AlipayCategoryDao;

/**
 * 
 * createdTime:2016-10-11
 * @author shaoyikun
 *
 */
@Service
public class AlipayCategoryService {
	@Autowired
	private AlipayCategoryDao alipayCategoryDao;

	//private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	public List<Map<String, Object>> getById(String id) {
		return alipayCategoryDao.getById(id);
	}
	
	public List<AlipayCategory> getByParentId(String pid) {
		return alipayCategoryDao.getByParentId(pid);
	}
}
