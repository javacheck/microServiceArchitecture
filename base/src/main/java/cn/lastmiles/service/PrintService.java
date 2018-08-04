package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Print;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.PrintDao;

@Service
public class PrintService {
	@Autowired
	private PrintDao printDao;
	@Autowired
	private IdService idService;
	
	public Page findAll(Long storeId,String printSn,Integer status, Page page) {
		return printDao.findAll(storeId,printSn,status, page);
	}

	public Print findPrint(Print print) {
		
		return printDao.findPrint(print);
	}

	public void editPrint(Long id, Long storeId,String printName, String printSn, String printKey,String memo,String categoryIds,String categoryNames) {
		if (id == null){
			id=idService.getId();
			printDao.save(id,storeId,printName,printSn,printKey,memo,categoryIds,categoryNames);
		}else {
			
			printDao.update(id,storeId,printName,printSn,printKey,memo,categoryIds,categoryNames);
		}
		
	}

	public Print findById(Long id) {
		return printDao.findById(id);
	}

	public void deleteById(Long id) {
		printDao.deleteById(id);
		
	}

	public Print findByStoreId(Long storeId) {
		return printDao.findByStoreId(storeId);
	}
	
	public List<Print> findListByStoreId(Long storeId,Integer status){
		return printDao.findListByStoreId(storeId,status);
	}

	public List<ProductCategory> findCategoryList(Long storeId) {
		return printDao.findCategoryList(storeId);
	}

	public void typeChangeByPrintId(Long id, Integer status) {
		printDao.typeChangeByPrintId(id,status);
	}
	
}
