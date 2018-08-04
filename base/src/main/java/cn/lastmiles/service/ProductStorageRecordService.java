package cn.lastmiles.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductStorageRecord;
import cn.lastmiles.bean.ProductStorageRecordStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.ProductStorageRecordDao;

@Service
public class ProductStorageRecordService {
	private final static Logger logger = LoggerFactory.getLogger(ProductStorageRecordService.class);
	@Autowired
	private ProductStorageRecordDao productStorageRecordDao;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductStockService productStockService;

	public Page findAllPage(String storeIdString, String storageNumber, String mobile,
			String beginTime, String endTime, Page page) {
		return productStorageRecordDao.findAllPage(storeIdString,storageNumber,mobile,beginTime,endTime,page);
	}

	

	public void save(List<ProductStorageRecordStock> productStorageRecordStocks,
			String storageTime, Long accountId, Long storeId, String memo) {
		
		ProductStorageRecord psr=new ProductStorageRecord();
		psr.setId(idService.getId());//入库ID
		psr.setAccountId(accountId);//操作人ID
		psr.setCreatedTime(new Date());//创建时间
		psr.setStorageTime(DateUtils.parse("yyyy-MM-dd",storageTime));//入库时间
		psr.setStoreId(storeId);//商店ID
		if(memo!=null && !"".equals(memo)){
			psr.setMemo(memo);//备注
		}else{
			psr.setMemo(null);//备注
		}
		psr.setStorageNumber("RK"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));//入库编号
		
		productStorageRecordDao.saveProductStorageRecord(psr);
		
		if (productStorageRecordStocks!=null) {
			for (ProductStorageRecordStock psrs : productStorageRecordStocks) {
				psrs.setProductStorageRecordId(psr.getId());
				if(psrs.getAttributeValues()==null || "".equals(psrs.getAttributeValues())){
					psrs.setAttributeValues(null);
				}
				if(psrs.getUnitName()==null || "".equals(psrs.getUnitName())){
					psrs.setUnitName(null);
				}
				if(psrs.getMemo()==null || "".equals(psrs.getMemo())){
					psrs.setMemo(null);
				}
				logger.debug("psrs.getStock()={},psrs.getAmount()={}",psrs.getStock(),psrs.getAmount());
				Double stock=NumberUtils.add(psrs.getStock(), psrs.getAmount());//库存增加
				psrs.setStock(stock);
				logger.debug("psrs={}",psrs);
				productStorageRecordDao.saveProductStorageRecordStock(psrs);
				productStorageRecordDao.updateStock(psrs.getStockId(),psrs.getCostPrice(),stock);
			}
		}
		
	}



	public List<ProductStorageRecordStock> findProductStorageRecordStockById(
			Long id) {
		return productStorageRecordDao.findProductStorageRecordStockById(id);
	}



	public boolean save(ProductStorageRecord psr) {
		return productStorageRecordDao.save(psr);
	}



	public void batchSave(List<Object[]> batchInsertStorageRecordArgs) {
		logger.debug("入库记录是：{}",batchInsertStorageRecordArgs);
		productStorageRecordDao.batchSave(batchInsertStorageRecordArgs);
	}
}
