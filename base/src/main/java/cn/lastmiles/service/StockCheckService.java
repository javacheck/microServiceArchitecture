package cn.lastmiles.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.StockCheck;
import cn.lastmiles.bean.StockCheckDetail;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ExcelUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.dao.StockCheckDao;

@Service
public class StockCheckService {
	private final static Logger logger = LoggerFactory.getLogger(StockCheckService.class);
	@Autowired
	private StockCheckDao stockCheckDao;
	
	@Autowired
	private IdService idService;
	public Page findAll(String storeIdString, String checkedName,
			String createdTime, Page page) {
		
		return stockCheckDao.findAll(storeIdString,checkedName,createdTime,page);
	}
	public List<ProductStock> findAllBySearch(String storeIdString) {
		return stockCheckDao.findAllBySearch(storeIdString);
	}
	@Transactional
	public String uploadFile(InputStream in, Long storeId,Long accountId) {
		String flag="";
		try {
			List<List<String>> list = ExcelUtils.simpleExcel(in);// 读取model.xls
			logger.debug("行数==="+list.size());
			if(list.size()<=1){
				flag = "excel文件数据为空，请检查！";
				return flag;
			}else{
				List<StockCheckDetail> scdList=new ArrayList<StockCheckDetail>();
				List<String> checkNameList=new ArrayList<String>();//存盘点人姓名
				for (int i = 1; i < list.size(); i++) {// 一行一行的读(第二行开始读)
					StockCheckDetail sdc=new StockCheckDetail();
					for (int j = 0; j < list.get(i).size(); j++) {// 每一行一格一格的读
						if(j==1){//商品名称
							sdc.setProductName(list.get(i).get(j));
						}else if(j==2){//商品条码
							sdc.setBarCode(list.get(i).get(j));
						}else if(j==3){//商品分类
							sdc.setCategoryName(list.get(i).get(j));
						}else if(j==4){//库存数量
							sdc.setStock(Integer.parseInt(list.get(i).get(j).trim()));
						}if (j == 5) {// 第一格获取盘点库存
							if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
								if(list.get(i).get(j).trim().length()<=9){
									logger.debug("盘点库存===="+list.get(i).get(j).trim());
									if(list.get(i).get(j).trim().matches("[0-9]+")){
										Integer checkedStock=Integer.parseInt(list.get(i).get(j).trim());
										if(checkedStock>=0){
											sdc.setCheckedStock(checkedStock);
										}else{
											flag += "第" + (i+1) + "行的第6格的盘点库存必须必须大于或等于0,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第6格的盘点库存必须为整数,请检查！</br>";
										return flag;
									}
									
								}else{
									flag += "第" + (i+1) + "行的第6格的盘点库存长度过长,请检查！</br>";
									return flag;
								}
							} else {
								flag += "第" + (i+1) + "行的第6格的盘点库存不能为空,请检查！</br>";
								return flag;
							}
						}else if(j==6){//盘点人
							if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
								sdc.setCheckedName(list.get(i).get(j).trim());
								checkNameList.add(list.get(i).get(j).trim());
							} else {
								flag += "第" + (i+1) + "行的第7格的盘点人不能为空,请检查！</br>";
								return flag;
							}
						}else if(j==7){//盘点时间
							if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
								logger.debug("盘点时间===="+list.get(i).get(j).trim());
								sdc.setCheckedTime(list.get(i).get(j).trim());
							} else {
								flag += "第" + (i+1) + "行的第8格的盘点时间不能为空,请检查！</br>";
								return flag;
							}
						}
					}
					scdList.add(sdc);
				}
				
				//数据全部正确，进入表插入数据操作
				if("".equals(flag)){
					String cns="";
					List<String> cnList = new ArrayList<String>(new HashSet<String>(checkNameList));
					logger.debug("cnList={}",cnList);
					for(int l=0;l<cnList.size();l++){
						cns+=cnList.get(l)+",";
					}
					cns=cns.substring(0,cns.length()-1);
					StockCheck sc=new StockCheck();
					sc.setId(idService.getId());//盘点编号
					sc.setCheckedName(cns);//盘点人
					sc.setAccountId(accountId);//导入人员
					sc.setStoreId(storeId);//商家
					this.saveStockCheck(sc);
					for(int k=0;k<scdList.size();k++){
						scdList.get(k).setStockCheckId(sc.getId());//盘点编号
						scdList.get(k).setStoreId(storeId);
						if(scdList.get(k).getCheckedStock().intValue()>scdList.get(k).getStock().intValue()){//盘点库存比库存大，盘盈，反之盘亏
							Integer inventoryProfit=scdList.get(k).getCheckedStock().intValue()-scdList.get(k).getStock().intValue();
							scdList.get(k).setInventoryProfit(inventoryProfit);
							scdList.get(k).setInventoryLoss(null);
						}else if(scdList.get(k).getCheckedStock().intValue()<scdList.get(k).getStock().intValue()){
							Integer inventoryProfit=scdList.get(k).getStock().intValue()-scdList.get(k).getCheckedStock().intValue();
							scdList.get(k).setInventoryProfit(null);
							scdList.get(k).setInventoryLoss(inventoryProfit);
						}else{
							scdList.get(k).setInventoryProfit(null);
							scdList.get(k).setInventoryLoss(null);
						}
						this.saveStockCheckDetail(scdList.get(k));
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			//throw new RuntimeException(e);
			flag= "导入失败，导入的数据不正确！";
		}
		return flag; 
	}
	private void saveStockCheck(StockCheck sc) {
		stockCheckDao.saveStockCheck(sc);
		
	}
	private void saveStockCheckDetail(StockCheckDetail scd) {
		stockCheckDao.saveStockCheckDetail(scd);
	}
	public Page finddetailsAll(Long stockCheckId, String productName,String barCode,Integer sort, Page page) {
		
		return stockCheckDao.finddetailsAll(stockCheckId,productName,barCode,sort,page);
	}
	public List<StockCheckDetail> findDetailsListAllBySearch(Long stockCheckId,
			String productName, String barCode) {
		return stockCheckDao.findDetailsListAllBySearch(stockCheckId,productName,barCode);
	}
	
	
}
