package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.BusinessBank;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.BusinessBankDao;

@Service
public class BusinessBankService {

	@Autowired
	private BusinessBankDao businessBankDao;
	
	public boolean save(BusinessBank businessBank) {
		return businessBankDao.save(businessBank);
	}
	
	public boolean update(BusinessBank businessBank) {
		return businessBankDao.update(businessBank);
	}

	public boolean setDefault(Long id,Long storeId) {
		return businessBankDao.setDefault(id,storeId);
	}
	public BusinessBank getById(Long id,Long businessId) {
		return businessBankDao.getById(id,businessId);
	}

	public BusinessBank getById(Long id) {
		return businessBankDao.getById(id);
	}
	
	public Page getBusinessBank(Long businessId, Integer type, String bankName, Page page) {
		return businessBankDao.getBusinessBank(businessId,type,bankName,page);
	}
	
	public BusinessBank getBusinessBank(Long businessId,Integer type,String accountNumber){
		return businessBankDao.getBusinessBank(businessId, type, accountNumber);
	}

	public BusinessBank getBusinessBank(Long id,Long businessId,Integer type,String accountNumber){
		return businessBankDao.getBusinessBank(id,businessId, type, accountNumber);
	}
	
	public BusinessBank getBusinessBank(Long businessId,Integer type) {
		return businessBankDao.getBusinessBank(businessId, type);
	}
	
	public boolean updateIsDefault(BusinessBank businessBank) {
		return businessBankDao.updateIsDefault(businessBank);
	}

	public List<BusinessBank> getConnectBusinessBank(Long ownerId, Integer type) {
		return businessBankDao.getConnectBusinessBank(ownerId,type);
	}

	public boolean deleteBankCard(Long storeId, String cardId) {
		return businessBankDao.deleteBankCard(storeId,cardId);
	}

	public Page getBusinessBank(Long businessId, String storeIdArray, Integer type, String bankName, Page page) {
		if( StringUtils.isBlank(storeIdArray) ){
			return getBusinessBank(businessId,type,bankName,page);
		}
		return businessBankDao.getBusinessBank(storeIdArray, type, bankName, page);
	}
}
