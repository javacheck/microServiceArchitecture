package cn.lastmiles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Supplier;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.SupplierDao;

@Service
public class SupplierService {
	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	private IdService idService;
	
	public Page findAll(Long storeId,String name,String phone, Page page) {
		return supplierDao.findAll(storeId,name,phone, page);
	}
	public Supplier findSupplier(Supplier supplier) {
		return supplierDao.findSupplier(supplier);
	}
	public void editSupplier(Supplier supplier) {
		if(supplier.getId()==null){
			supplier.setId(idService.getId());
			supplierDao.saveSupplier(supplier);
		}else{
			supplierDao.updateSupplier(supplier);
		}
		
	}
	public Supplier findById(Long id) {
		return supplierDao.findById(id);
	}
	public void deleteById(Long id) {
		supplierDao.deleteById(id);
		
	}
}
