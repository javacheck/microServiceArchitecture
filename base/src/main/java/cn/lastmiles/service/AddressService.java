package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Address;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.AddressDao;

@Service
public class AddressService {
	
	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private IdService idService;
	
	public void editAddress(Address address) {
		if(address.getId()==null){
			address.setId(idService.getId());
			if(address.getIsDefault() != null && address.getIsDefault().intValue()==1){
				addressDao.updateDefault(address.getUserId());
			}
			addressDao.saveAddress(address);
		}else{
			if(address.getIsDefault() != null && address.getIsDefault().intValue()==1){
				addressDao.updateDefault(address.getUserId());
			}
			addressDao.updateAddress(address);
		}
		
	}

	public List<Address> findAddressByUserId(Long userId) {
		return addressDao.findAddressByUserId(userId);
	}

	public void delete(Long id,Long userId){
		addressDao.delete(id, userId);
	}

	public Address findById(Long id) {
		return addressDao.findById(id);
		
	}

	public void setDefaultAddress(Long addressId, Long userId) {
		addressDao.setDefaultAddress(addressId,userId);
	}
}
