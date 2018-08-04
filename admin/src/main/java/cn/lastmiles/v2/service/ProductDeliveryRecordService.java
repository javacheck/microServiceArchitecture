package cn.lastmiles.v2.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductDeliveryRecord;
import cn.lastmiles.bean.ProductDeliveryRecordStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.ProductDeliveryRecordDao;


@Service
public class ProductDeliveryRecordService {
	private final static Logger logger = LoggerFactory.getLogger(ProductDeliveryRecordService.class);
	@Autowired
	private ProductDeliveryRecordDao productDeliveryRecordDao;
	@Autowired
	private IdService idService;
	@Autowired
	private ProductStockService productStockService;

	public Page findAllPage(String storeIdString, String deliveryNumber, String mobile,
			String beginTime, String endTime, Page page) {
		return productDeliveryRecordDao.findAllPage(storeIdString,deliveryNumber,mobile,beginTime,endTime,page);
	}

	

	public void save(List<ProductDeliveryRecordStock> productDeliveryRecordStocks,
			String deliveryTime, Long accountId, Long storeId, String memo) {
		
		ProductDeliveryRecord pdr=new ProductDeliveryRecord();
		pdr.setId(idService.getId());//出库ID
		pdr.setAccountId(accountId);//操作人ID
		pdr.setCreatedTime(new Date());//创建时间
		pdr.setDeliveryTime(DateUtils.parse("yyyy-MM-dd",deliveryTime));//出库时间
		pdr.setStoreId(storeId);//商店ID
		if(memo!=null && !"".equals(memo)){
			pdr.setMemo(memo);//备注
		}else{
			pdr.setMemo(null);//备注
		}
		pdr.setDeliveryNumber("CK"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));//出库编号
		
		productDeliveryRecordDao.saveProductDeliveryRecord(pdr);
		
		if (productDeliveryRecordStocks!=null) {
			for (ProductDeliveryRecordStock pdrs : productDeliveryRecordStocks) {
				pdrs.setProductDeliveryRecordId(pdr.getId());
				if(pdrs.getAttributeValues()==null || "".equals(pdrs.getAttributeValues())){
					pdrs.setAttributeValues(null);
				}
				if(pdrs.getUnitName()==null || "".equals(pdrs.getUnitName())){
					pdrs.setUnitName(null);
				}
				if(pdrs.getMemo()==null || "".equals(pdrs.getMemo())){
					pdrs.setMemo(null);
				}
				Double stock=NumberUtils.subtract(pdrs.getStock(), pdrs.getAmount());//库存减少
				pdrs.setStock(stock);
				productDeliveryRecordDao.saveProductDeliveryRecordStock(pdrs);
				logger.debug("stock={}",stock);
				productDeliveryRecordDao.updateStock(pdrs.getStockId(),stock);
			}
		}
		
	}



	public List<ProductDeliveryRecordStock> findProductDeliveryRecordStockById(
			Long id) {
		return productDeliveryRecordDao.findProductDeliveryRecordStockById(id);
	}
}
