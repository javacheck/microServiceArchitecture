package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Partner;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.PartnerDao;

/**
 * createDate : 2015年12月21日下午4:19:16
 */
@Service
public class PartnerService {
	
	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private IdService idService;

	public Page list(String name ,Page page) {
		return partnerDao.list(name,page);
	}

	public Partner findById(Long id) {
		return partnerDao.findById(id);
	}

	public boolean save(Partner partner) {
		partner.setId(idService.getId());
//		partner.setAppKey(StringUtils.uuid());
		return partnerDao.save(partner);
	}

	public boolean update(Partner partner) {
		return partnerDao.update(partner);
	}

	public int delete(Long id) {
		return partnerDao.delete(id);
	}

	public int checkPartnerName(Long id, String name) {
		return partnerDao.checkPartnerName(id,name);
	}

	public boolean findStoreById(Long id) {
		return partnerDao.findStoreById(id);
	}
	
	public int checkPartnerToken(Long id,String token){
		return partnerDao.checkPartnerToken(id,token);
	}
	
	public Partner findByToken(String token){
		if( StringUtils.isNotBlank(token) ){
			return partnerDao.findByToken(token);
		}
		return null;
	}

	public Partner findByStoreId(Long storeId) {
		return partnerDao.findByStoreId(storeId);
	}
}