package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.AllocationRecord;
import cn.lastmiles.bean.AllocationRecordStock;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductDeliveryRecord;
import cn.lastmiles.bean.ProductDeliveryRecordStock;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStorageRecord;
import cn.lastmiles.bean.ProductStorageRecordStock;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.AllocationDao;
import cn.lastmiles.dao.ProductDeliveryRecordDao;
import cn.lastmiles.dao.ProductStorageRecordDao;

@Service
public class AllocationService {
	private final static Logger logger = LoggerFactory.getLogger(AllocationService.class);
	@Autowired
	private AllocationDao allocationDao;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private ProductDeliveryRecordDao productDeliveryRecordDao;
	@Autowired
	private ProductStorageRecordDao productStorageRecordDao;

	public Page findAllPage(String fromStoreIdString, String toStoreIdString, String allocationNumber,
			Integer status, String beginTime, String endTime,String storeIdString, Page page) {
		
		return allocationDao.findAllPage(fromStoreIdString,toStoreIdString,allocationNumber,status,beginTime,endTime,storeIdString,page);
	}

	public List<ProductStock> findProductStockList(String productStockIds) {
		
		List<ProductStock> psList=allocationDao.findProductStockList(productStockIds);
		logger.debug("psList={}",psList.size());
		
		for(int i=0;i<psList.size();i++){
			ProductStock ps=psList.get(i);
			String attributeValues="";
			logger.debug("ps.getAttributeName()={}",ps.getAttributeName());
			if(ps!=null && ps.getAttributeCode()!=null && ps.getAttributeName()!=null && ps.getAttributeName().indexOf("|")>0){
				String[] arrs =StringUtil.splitc(ps.getAttributeName(), '|');
				for(int j=1;j<arrs.length;j++){
					attributeValues+=arrs[j]+"|";
				}
				logger.debug("attributeValues={}",attributeValues);
				ps.setAttributeValues(attributeValues.substring(0,attributeValues.length()-1));
				logger.debug("ps={}",ps);
			}else{
				ps.setAttributeValues("");
			}
		}
		return psList;
	}

	public void save(AllocationRecord ar) {
		Integer total=allocationDao.findTotal(DateUtils.format(new Date(), "yyyy-MM-dd"),ar.getToStoreId());
		System.out.println("totalSum==="+total);
		total=total+1;
		String allocationNumber="";
		if(total<10){
			allocationNumber=DateUtils.format(new Date(), "yyyyMMdd")+"0"+total;
		}else{
			allocationNumber=DateUtils.format(new Date(), "yyyyMMdd")+total;
		}
		ar.setAllocationNumber(allocationNumber);
		
		allocationDao.save(ar);
	}

	public void saveAllocationRecordStock(AllocationRecordStock allocationRecordStock) {
		logger.debug("库存ID======={}",allocationRecordStock.getStockId());
		ProductStock ps=allocationDao.findProductStock(allocationRecordStock.getStockId());
		logger.debug("库存================{}",ps.getStock());
		if(ps.getStock().intValue()>=0){//库存不是无限
			allocationDao.subtractStockAmount(allocationRecordStock.getStockId(),allocationRecordStock.getAmount());
		}
		allocationDao.saveAllocationRecordStock(allocationRecordStock);
	}

	

	public List<AllocationRecordStock> findById(Long id) {
		return allocationDao.findById(id);
	}

	public void typeChangeByallocationId(Long id, Integer status,Long accountId) {
		List<AllocationRecordStock> arss = allocationDao.findById(id);
		if(status.intValue()==3){//拒绝，调 出方库存要加回去
			this.productStorage(arss,accountId);
		}else{//同意，调出方库存减少
			this.productDelivery(arss,accountId);
		}
		allocationDao.typeChangeByallocationId(id,status);
		
	}

	private void productDelivery(List<AllocationRecordStock> arss,Long accountId) {//出库
		ProductStock ps=this.findProductStockById(arss.get(0).getStockId());
		ProductDeliveryRecord pdr=new ProductDeliveryRecord();
		pdr.setId(idService.getId());//出库ID
		pdr.setAccountId(accountId);//操作人ID
		pdr.setCreatedTime(new Date());//创建时间
		pdr.setDeliveryTime(new Date());//出库时间
		pdr.setStoreId(ps.getStoreId());//商店ID
		pdr.setDeliveryNumber("CK"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));//出库编号
		pdr.setMemo("调拨出库，单号"+arss.get(0).getAllocationNumber());//备注
		
		boolean flag = false;
		for (AllocationRecordStock ars : arss) {
			ProductStock productStock=this.findProductStockById(ars.getStockId());
			if (productStock.getStock().doubleValue() != Constants.ProductStock.Store_Infinite.doubleValue()){
				flag = true;
				ProductDeliveryRecordStock pdrs=new ProductDeliveryRecordStock();
				pdrs.setProductDeliveryRecordId(pdr.getId());//出库ID
				pdrs.setStockId(ars.getStockId());//库存ID
				pdrs.setProductName(ars.getProductName());//商品名称
				pdrs.setBarCode(ars.getBarCode());//商品条码
				pdrs.setUnitName(productStock.getUnitName());//单位名称
				pdrs.setCostPrice(productStock.getCostPrice());//成本价
				pdrs.setAmount(ars.getAmount());//出库数量
				pdrs.setStock(productStock.getStock());//库存
				//if(ars.getAttributeName()!=null && !"".equals(ars.getAttributeName())){//规格
	//				String attributeValues="";
	//				String[] arrs =StringUtil.splitc(ars.getAttributeName(), '|');
	//				for(int i=1;i<arrs.length;i++){
	//					if ("".equals(attributeValues)){
	//						attributeValues += arrs[i];
	//					}else {
	//						attributeValues += "|" + arrs[i];
	//					}
	//				}
					//pdrs.setAttributeValues(ars.getAttributeName());
				//}
				pdrs.setAttributeValues(ars.getAttributeName());
				logger.debug("pdrs.getStock()={},pdrs.getAmount()={}",pdrs.getStock(),pdrs.getAmount());
				//Double stock=NumberUtils.subtract(pdrs.getStock(), pdrs.getAmount());//库存减少
				pdrs.setStock(pdrs.getStock());
				logger.debug("ProductDeliveryRecordStock = {}",pdrs);
				productDeliveryRecordDao.saveProductDeliveryRecordStock(pdrs);
				//logger.debug("stock={}",stock);
				//productDeliveryRecordDao.updateStock(pdrs.getStockId(),stock);
			}
		}
		
		if (flag){
			productDeliveryRecordDao.saveProductDeliveryRecord(pdr);
		}
	}

	private void productStorage(List<AllocationRecordStock> arss,Long accountId) {//入库
		ProductStock ps=this.findProductStockById(arss.get(0).getStockId());
		ProductStorageRecord psr=new ProductStorageRecord();
		psr.setId(idService.getId());//入库ID
		psr.setAccountId(accountId);//操作人ID
		psr.setCreatedTime(new Date());//创建时间
		psr.setStorageTime(new Date());//入库时间
		psr.setStoreId(ps.getStoreId());//商店ID
		psr.setStorageNumber("RK"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));//入库编号
		psr.setMemo("调拨入库，单号"+arss.get(0).getAllocationNumber());//备注
		
		productStorageRecordDao.saveProductStorageRecord(psr);
		
		for (AllocationRecordStock ars : arss) {
			ProductStock productStock=this.findProductStockById(ars.getStockId());
			ProductStorageRecordStock psrs=new ProductStorageRecordStock();
			psrs.setProductStorageRecordId(psr.getId());//入库ID
			psrs.setStockId(ars.getStockId());//库存ID
			psrs.setProductName(ars.getProductName());//商品名称
			psrs.setBarCode(ars.getBarCode());//商品条码
			psrs.setUnitName(productStock.getUnitName());//单位名称
			psrs.setCostPrice(productStock.getCostPrice());//成本价
			psrs.setAmount(ars.getAmount());//入库数量
			psrs.setStock(productStock.getStock());//库存
//			if(ars.getAttributeName()!=null && !"".equals(ars.getAttributeName())){//规格
//				String attributeValues="";
//				String[] arrs =StringUtil.splitc(ars.getAttributeName(), '|');
//				for(int i=1;i<arrs.length;i++){
//					if ("".equals(attributeValues)){
//						attributeValues += arrs[i];
//					}else {
//						attributeValues += "|" + arrs[i];
//					}
//				}
//				psrs.setAttributeValues(attributeValues);
//			}
			psrs.setAttributeValues(ars.getAttributeName());
			logger.debug("psrs.getStock()={},psrs.getAmount()={}",psrs.getStock(),psrs.getAmount());
			Double stock=NumberUtils.add(psrs.getStock(), psrs.getAmount());//库存增加
			psrs.setStock(stock);
			logger.debug("psrs={}",psrs);
			productStorageRecordDao.saveProductStorageRecordStock(psrs);
			productStorageRecordDao.updateStock(psrs.getStockId(),psrs.getCostPrice(),stock);
		}
		
	}

	

	public ProductStock findProductStock(String barCode, Long storeId,String productName,Integer type) {
		
		return allocationDao.findProductStock(barCode,storeId,productName,type);
	}

	public void addConfirmStockAmount(int amount, String attributeName,String barCode, Long storeId) {
		allocationDao.addConfirmStockAmount(amount,attributeName,barCode,storeId);
		
	}

	public List<AllocationRecord> findAllBySearch(Long fromStoreId,
			Long toStoreId, String allocationNumber, Integer status, String beginTime,
			String endTime, String storeIdString) {
		
		return allocationDao.findAllBySearch(fromStoreId,toStoreId,allocationNumber,status,beginTime,endTime,storeIdString);
	}

	public ProductStock findProductStockById(Long stockId) {
		
		return allocationDao.findProductStockById(stockId);
	}

	public Product findProductById(Long productId) {
		return allocationDao.findProductById(productId);
	}

	public ProductCategory findProductCategory(String categoryName,
			Long storeId) {
		
		return allocationDao.findProductCategory(categoryName,storeId);
	}

	

	public List<ProductStock> findProductStockList(String barCode,String productName,
			 Long storeId, Integer type) {
		return allocationDao.findProductStockList(barCode,productName,storeId,type);
	}

	public void addConfirmStockAmount(Long stockId, int amount) {
		if(allocationDao.findProductStock(stockId).getStock().intValue()>=0){//库存不是无限就加
				allocationDao.addConfirmStockAmount(stockId,amount);
		}
		
	}

	public void typefinishChangeByallocationId(Long id, Integer status) {
		allocationDao.typefinishChangeByallocationId(id,status);
		
	}

	public void updateLastAmounts(Long allocationRecordId, Long stockId,
			Double lastAmount) {
		allocationDao.updateLastAmounts(allocationRecordId,stockId,lastAmount);
		
	}

	public void confirmStockAmount(Long id, Integer status,String confirmAmounts,Long accountId,Long storeId) {
		String[] amountArr=confirmAmounts.split(",");
		List<AllocationRecordStock> arss = this.findById(id);
		logger.debug("arss={}",arss);
		List<ProductStock> productStockList=new ArrayList<ProductStock>();
		for(int i=0;i<arss.size();i++){//增加库存
			this.updateLastAmounts(arss.get(i).getAllocationRecordId(),arss.get(i).getStockId(),Double.parseDouble(amountArr[i]));
			logger.debug("arss.get(i).getAttributeName()={}",arss.get(i).getAttributeName());
			String attributeName="";
			if(StringUtils.isBlank(arss.get(i).getAttributeName())){//当属性为空时，表示是无属性商品，库存条码唯一
				attributeName=arss.get(i).getProductName();
				logger.debug("arss.get(i).getBarCode()={},storeId={},attributeName={}",arss.get(i).getBarCode(),storeId,attributeName);
				ProductStock pStock=this.findProductStock(arss.get(i).getBarCode(), storeId, attributeName, 1);
				logger.debug("pStock={}",pStock);
				productStockList.add(pStock);
			}else{
				attributeName=arss.get(i).getProductName()+"|"+arss.get(i).getAttributeName();
				logger.debug("有属性商品名称attributeName={}",attributeName);
				String[] arrs =StringUtil.splitc(attributeName, '|');
				Arrays.sort(arrs);
				List<ProductStock> psList=this.findProductStockList(arss.get(i).getBarCode(),arss.get(i).getProductName(),storeId,0);
				logger.debug("有属性psList={}",psList);
				for(int j=0;j<psList.size();j++){
					logger.debug("有属性的库存attributeName={}",psList.get(j).getAttributeName());
					String[] arrS =StringUtil.splitc(psList.get(j).getAttributeName(), '|');
					Arrays.sort(arrS);
					if(Arrays.equals(arrs, arrS)){
						productStockList.add(psList.get(j));
						break;
					}
				}
				
			}
			
		}
		logger.debug("productStockList={}",productStockList.size());
		this.confirmProductStorage(arss,confirmAmounts,accountId,storeId,productStockList);
		this.typefinishChangeByallocationId(id,status);
		
	}

	private void confirmProductStorage(List<AllocationRecordStock> arss,String confirmAmounts, Long accountId, Long storeId,List<ProductStock> productStockList) {
		logger.debug("productStockList={}",productStockList.size());
		for(int i=0;i<productStockList.size();i++){
			logger.debug("stock111111111111111111111111111={}",productStockList.get(i).getStock());
		}
		String[] amountArr=confirmAmounts.split(",");
		ProductStorageRecord psr=new ProductStorageRecord();
		psr.setId(idService.getId());//入库ID
		psr.setAccountId(accountId);//操作人ID
		psr.setCreatedTime(new Date());//创建时间
		psr.setStorageTime(new Date());//入库时间
		psr.setStoreId(storeId);//商店ID
		psr.setStorageNumber("RK"+DateUtils.format(new Date(), "yyyyMMddHHmmss"));//入库编号
		psr.setMemo("调拨入库，单号"+arss.get(0).getAllocationNumber());//备注
		
		productStorageRecordDao.saveProductStorageRecord(psr);
		int num=0;
		for (AllocationRecordStock ars : arss) {
			ProductStock productStock=this.findProductStockById(ars.getStockId());
			ProductStorageRecordStock psrs=new ProductStorageRecordStock();
			psrs.setProductStorageRecordId(psr.getId());//入库ID
			psrs.setStockId(ars.getStockId());//库存ID
			psrs.setProductName(ars.getProductName());//商品名称
			psrs.setBarCode(ars.getBarCode());//商品条码
			psrs.setUnitName(productStock.getUnitName());//单位名称
			psrs.setCostPrice(null);//成本价
			psrs.setAmount(Double.parseDouble(amountArr[num]));//入库数量
			psrs.setStock(productStock.getStock());//库存
//			if(ars.getAttributeName()!=null && !"".equals(ars.getAttributeName())){//规格
//				String attributeValues="";
//				String[] arrs =StringUtil.splitc(ars.getAttributeName(), '|');
//				for(int i=1;i<arrs.length;i++){
//					if ("".equals(attributeValues)){
//						attributeValues += arrs[i];
//					}else {
//						attributeValues += "|" + arrs[i];
//					}
//				}
//				psrs.setAttributeValues(attributeValues);
//			}
			psrs.setAttributeValues(ars.getAttributeName());
			this.updateLastAmounts(ars.getAllocationRecordId(),ars.getStockId(),Double.parseDouble(amountArr[num]));
			logger.debug("psrs.getStock()={},psrs.getAmount()={}",psrs.getStock(),Double.parseDouble(amountArr[num]));
			Double stock=NumberUtils.add(productStockList.get(num).getStock(), Double.parseDouble(amountArr[num]));//库存增加
			psrs.setStock(stock);
			productStorageRecordDao.saveProductStorageRecordStock(psrs);
			productStorageRecordDao.updateStock(productStockList.get(num).getId(),stock);
			num++;
		}

	}
	
}
