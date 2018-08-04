package cn.lastmiles.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.TransactionAnalysisService;
import cn.lastmiles.utils.SecurityUtils;
import cn.lastmiles.v2.service.ProductStockService;

@Controller
@RequestMapping("transactionAnalysis")
public class TransactionAnalysisController {
	private final static Logger logger = LoggerFactory.getLogger(TransactionAnalysisController.class);
	
	@Autowired
	private TransactionAnalysisService transactionAnalysisService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired 
	private StoreService storeService;
	@RequestMapping("")
	public String index() {
		
		return "redirect:/transactionAnalysis/list";
	}
	/**
	 * 
	 * @param dateBeginTime 天开始时间 
	 * @param dateEndTime 天结束时间
	 * @param storeId
	 * @param analysisType 0订单数量 1销售额 2商品毛利
	 * @param stockId 库存名称
	 * @param dateOrderType 0 总订单 1平均订单
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String listData(Long storeId,Integer analysisType,Long stockId,
			String dateBeginTime, String dateEndTime,String monthTime,String yearTime,Integer dateOrderType,Model model){
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		int mNum=0;
		if (!StringUtils.isNotBlank(monthTime)) {
			logger.debug("date={}",DateUtils.getLastDate(new Date()));
			mNum =DateUtils.getDayDiff(DateUtils.getLastDay(new Date()),DateUtils.getFirstDate(new Date()));
			mNum=mNum+1;
			logger.debug("mNum={}",mNum); 
		}
		
		logger.debug("storeIdString={}",storeIdString.toString());
		//总订单date
		List<Map<String,Object>> dateSumRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,0,null);
		//线上date
		List<Map<String,Object>> dateAppRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,0,0);
		//线下date
		List<Map<String,Object>> dateDevicesRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,0,1);
		
		//总订单month
		List<Map<String,Object>> monthSumRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,0,null);
		//线上month
		List<Map<String,Object>> monthAppRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,0,0);
		//线下month
		List<Map<String,Object>> monthDevicesRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,0,1);
		
		//总订单year
		List<Map<String,Object>> yearSumRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,0,null);
		//线上year
		List<Map<String,Object>> yearAppRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,0,0);
		//线下year
		List<Map<String,Object>> yearDevicesRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,0,1);
		String dOrderSum="";
		String dAppSum="";
		String dDevicesSum="";
		
		String mOrderSum="";
		String mAppSum="";
		String mDevicesSum="";
		
		String yOrderSum="";
		String yAppSum="";
		String yDevicesSum="";
		
		Map<String,Object> dMap=dateSumRp.get(0);
		Map<String,Object> dAppMap=dateAppRp.get(0);
		Map<String,Object> dateDevicesMap=dateDevicesRp.get(0);
		//logger.debug("dMap={},dAppMap={},dateDevicesMap={}",dMap,dAppMap,dateDevicesMap);
		
		Map<String,Object> mMap=monthSumRp.get(0);
		Map<String,Object> mAppMap=monthAppRp.get(0);
		Map<String,Object> mDevicesMap=monthDevicesRp.get(0);
		//logger.debug("mMap={},mAppMap={},mDevicesMap={}",mMap,mAppMap,mDevicesMap);
		
		Map<String,Object> yMap=yearSumRp.get(0);
		Map<String,Object> yAppMap=yearAppRp.get(0);
		Map<String,Object> yDevicesMap=yearDevicesRp.get(0);
		//logger.debug("yMap={},yAppMap={},yDevicesMap={}",yMap,yAppMap,yDevicesMap);
		//date
		Collection<Object> coll = dMap.values();
		Iterator<Object> it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				dOrderSum += "0"+",";
			}else{
				dOrderSum += obj.toString()+",";
			}
		}
		coll=dAppMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				dAppSum += "0"+",";
			}else{
				dAppSum += obj.toString()+",";
			}
		}
		coll=dateDevicesMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				dDevicesSum += "0"+",";
			}else{
				dDevicesSum += obj.toString()+",";
			}
		}
		
		//month
		logger.debug("mMap={}",mMap.size());
		coll=mMap.values();
		
		it = coll.iterator();
		int indexO=0;
		while (it.hasNext()){
			indexO=indexO+1;
			Object obj = it.next();
			if (obj==null){
				mOrderSum += "0"+",";
			}else{
				mOrderSum += obj.toString()+",";
			}
			if(indexO==mNum){
				break;
			}
		}
		coll=mAppMap.values();
		it = coll.iterator();
		int indexA=0;
		while (it.hasNext()){
			indexA=indexA+1;
			Object obj = it.next();
			if (obj==null){
				mAppSum += "0"+",";
			}else{
				mAppSum += obj.toString()+",";
			}
			if(indexA==mNum){
				break;
			}
			
		}
		
		coll=mDevicesMap.values();
		it = coll.iterator();
		int indexD=0;
		while (it.hasNext()){
			indexD=indexD+1;
			Object obj = it.next();
			if (obj==null){
				mDevicesSum += "0"+",";
			}else{
				mDevicesSum += obj.toString()+",";
			}
			if(indexD==mNum){
				break;
			}
			
		}
		//year
		coll=yMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yOrderSum += "0"+",";
			}else{
				yOrderSum += obj.toString()+",";
			}
		}
		coll=yAppMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yAppSum += "0"+",";
			}else{
				yAppSum += obj.toString()+",";
			}
		}
		coll=yDevicesMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yDevicesSum += "0"+",";
			}else{
				yDevicesSum += obj.toString()+",";
			}
		}
		//date
		model.addAttribute("dOrderSum",dOrderSum.substring(0, dOrderSum.length()-1));
		model.addAttribute("dAppSum",dAppSum.substring(0, dAppSum.length()-1));
		model.addAttribute("dDevicesSum",dDevicesSum.substring(0, dDevicesSum.length()-1));
		//month
		
		model.addAttribute("mOrderSum",mOrderSum.substring(0, mOrderSum.length()-1));
		model.addAttribute("mAppSum",mAppSum.substring(0, mAppSum.length()-1));
		model.addAttribute("mDevicesSum",mDevicesSum.substring(0, mDevicesSum.length()-1));
		//year
		logger.debug("yOrderSum={}",yOrderSum);
		model.addAttribute("yOrderSum",yOrderSum.substring(0, yOrderSum.length()-1));
		model.addAttribute("yAppSum",yAppSum.substring(0, yAppSum.length()-1));
		model.addAttribute("yDevicesSum",yDevicesSum.substring(0, yDevicesSum.length()-1));
		
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		boolean flag = SecurityUtils.isStore() ? SecurityUtils.isStore()^SecurityUtils.isMainStore() : false;
		model.addAttribute("isStore",flag);
		if(flag){
			model.addAttribute("isStoreId",SecurityUtils.getAccountStoreId());			
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());

		
		return "transactionAnalysis/list";
	}
	@RequestMapping("list/list-data")
	public String listData(Long product_storeId,String product_name, Page page, Model model) {
		logger.debug("product_storeId={}",product_storeId);
		boolean flag = SecurityUtils.isStore() ? SecurityUtils.isStore()^SecurityUtils.isMainStore() : false;
		if(flag){//如果是商家登录
			product_storeId=SecurityUtils.getAccountStoreId();
		}
		if( null != product_name){
			product_name = product_name.replaceAll("\\s*", "");
		}
		
		page = productStockService.findAllocationList(product_storeId.toString(), product_name, null, 0, null, null, null, null, page);
		
		model.addAttribute("data",page);
		return "transactionAnalysis/stockList-data";
		
	}
	
	@RequestMapping(value="findAll",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> findAll(Long storeId,Integer analysisType,Long stockId,
			String dateBeginTime, String dateEndTime,Integer dateOrderType, 
			String monthTime,Integer monthOrderType,
			String yearTime,Integer yearOrderType){
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId || ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					boolean index = false;
					for (Store store : storeList) {
						if(index){
							storeIdString.append(",");
						}
						storeIdString.append(store.getId());
						index = true;
					}
				} else { // 有权限，且指定了固定查询的商家
					storeIdString.append(storeId.toString());
				}
			} else {
				storeIdString.append(SecurityUtils.getAccountStoreId().toString());				
			}
		}else { // 非商家登录
			if( null != storeId ){
				storeIdString.append(storeId);
			}
		}
		List<String> allSum=new ArrayList<String>();
		int mNum=0;
		if (StringUtils.isNotBlank(monthTime)) {
			if(DateUtils.format(new Date(), "yyyy-MM").equals(monthTime)){//传进来的当前月
				mNum =DateUtils.getDayDiff(DateUtils.getLastDay(new Date()),DateUtils.getFirstDate(new Date()));
				mNum=mNum+1;
				logger.debug("mNum={}",mNum); 
			}else{
				mNum =DateUtils.getDayDiff(DateUtils.getLastDay(DateUtils.parse("yyyy-MM",monthTime)),DateUtils.getFirstDate(DateUtils.parse("yyyy-MM",monthTime)));
				mNum=mNum+1;
				logger.debug("mNum={}",mNum); 
			}
				
		}
		logger.debug("storeIdString={}",storeIdString);
		//总订单date
		List<Map<String,Object>> dateSumRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,dateOrderType,null);
		//线上date
		List<Map<String,Object>> dateAppRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,dateOrderType,0);
		//线下date
		List<Map<String,Object>> dateDevicesRp=transactionAnalysisService.findDateAll(storeIdString.toString(),stockId,analysisType,dateBeginTime,dateEndTime,dateOrderType,1);
		
		//总订单month
		List<Map<String,Object>> monthSumRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,monthOrderType,null);
		//线上month
		List<Map<String,Object>> monthAppRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,monthOrderType,0);
		//线下month
		List<Map<String,Object>> monthDevicesRp=transactionAnalysisService.findMonthAll(storeIdString.toString(),stockId,analysisType,monthTime,monthOrderType,1);
		
		//总订单year
		List<Map<String,Object>> yearSumRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,yearOrderType,null);
		//线上year
		List<Map<String,Object>> yearAppRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,yearOrderType,0);
		//线下year
		List<Map<String,Object>> yearDevicesRp=transactionAnalysisService.findYearAll(storeIdString.toString(),stockId,analysisType,yearTime,yearOrderType,1);
		String dOrderSum="";
		String dAppSum="";
		String dDevicesSum="";
		
		String mOrderSum="";
		String mAppSum="";
		String mDevicesSum="";
		
		String yOrderSum="";
		String yAppSum="";
		String yDevicesSum="";
		
		Map<String,Object> dMap=dateSumRp.get(0);
		Map<String,Object> dAppMap=dateAppRp.get(0);
		Map<String,Object> dateDevicesMap=dateDevicesRp.get(0);
		logger.debug("dMap={},dAppMap={},dateDevicesMap={}",dMap,dAppMap,dateDevicesMap);
		
		Map<String,Object> mMap=monthSumRp.get(0);
		Map<String,Object> mAppMap=monthAppRp.get(0);
		Map<String,Object> mDevicesMap=monthDevicesRp.get(0);
		logger.debug("mMap={},mAppMap={},mDevicesMap={}",mMap,mAppMap,mDevicesMap);
		
		Map<String,Object> yMap=yearSumRp.get(0);
		Map<String,Object> yAppMap=yearAppRp.get(0);
		Map<String,Object> yDevicesMap=yearDevicesRp.get(0);
		logger.debug("yMap={},yAppMap={},yDevicesMap={}",yMap,yAppMap,yDevicesMap);
		//date
		Collection<Object> coll = dMap.values();
		Iterator<Object> it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			logger.debug("obj={}",obj);
			if (obj==null){
				if(analysisType.intValue()==1){
					dOrderSum += "0.00"+",";
				}else{
					dOrderSum += "0"+",";
				}
			}else{
				if(analysisType.intValue()==1){
					dOrderSum += df.format(obj)+",";
				}else{
					dOrderSum += obj.toString()+",";
				}
			}
		}
		coll=dAppMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				if(analysisType.intValue()==1){
					dAppSum += "0.00"+",";
				}else{
					dAppSum += "0"+",";
				}
			}else{
				if(analysisType.intValue()==1){
					dAppSum += df.format(obj)+",";
				}else{
					dAppSum += obj.toString()+",";
				}
			}
		}
		coll=dateDevicesMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				if(analysisType.intValue()==1){
					dDevicesSum += "0.00"+",";
				}else{
					dDevicesSum += "0"+",";
				}
			}else{
				if(analysisType.intValue()==1){
					dDevicesSum += df.format(obj)+",";
				}else{
					dDevicesSum += obj.toString()+",";
				}
			}
		}
		
		//month
		coll=mMap.values();
		it = coll.iterator();
		int indexO=0;
		logger.debug("mNum={}",mNum); 
		while (it.hasNext()){
			indexO=indexO+1;
			Object obj = it.next();
			if (obj==null){
				mOrderSum += "0"+",";
			}else{
				mOrderSum += obj.toString()+",";
			}
			if(indexO==mNum){
				break;
			}
			
		}
		logger.debug("mOrderSum1={}",mOrderSum);
		coll=mAppMap.values();
		it = coll.iterator();
		int indexA=0;
		while (it.hasNext()){
			indexA=indexA+1;
			Object obj = it.next();
			if (obj==null){
				mAppSum += "0"+",";
			}else{
				mAppSum += obj.toString()+",";
			}
			if(indexA==mNum){
				break;
			}
			
		}
		coll=mDevicesMap.values();
		it = coll.iterator();
		int indexD=0;
		while (it.hasNext()){
			indexD=indexD+1;
			Object obj = it.next();
			if (obj==null){
				mDevicesSum += "0"+",";
			}else{
				mDevicesSum += obj.toString()+",";
			}
			if(indexD==mNum){
				break;
			}
			
		}
		//year
		coll=yMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yOrderSum += "0"+",";
			}else{
				yOrderSum += obj.toString()+",";
			}
		}
		coll=yAppMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yAppSum += "0"+",";
			}else{
				yAppSum += obj.toString()+",";
			}
		}
		coll=yDevicesMap.values();
		it = coll.iterator();
		while (it.hasNext()){
			Object obj = it.next();
			if (obj==null){
				yDevicesSum += "0"+",";
			}else{
				yDevicesSum += obj.toString()+",";
			}
		}
		//date
		dOrderSum=dOrderSum.substring(0, dOrderSum.length()-1);
		dAppSum=dAppSum.substring(0, dAppSum.length()-1);
		dDevicesSum=dDevicesSum.substring(0, dDevicesSum.length()-1);
		
		//month
		
		mOrderSum=mOrderSum.substring(0, mOrderSum.length()-1);
		mAppSum=mAppSum.substring(0, mAppSum.length()-1);
		mDevicesSum=mDevicesSum.substring(0, mDevicesSum.length()-1);
		logger.debug("mOrderSum={}",mOrderSum);
		//year
		yOrderSum=yOrderSum.substring(0, yOrderSum.length()-1);
		yAppSum=yAppSum.substring(0, yAppSum.length()-1);
		yDevicesSum=yDevicesSum.substring(0, yDevicesSum.length()-1);
		
		allSum.add(dOrderSum);
		allSum.add(dAppSum);
		allSum.add(dDevicesSum);
		allSum.add(mOrderSum);
		allSum.add(mAppSum);
		allSum.add(mDevicesSum);
		allSum.add(yOrderSum);
		allSum.add(yAppSum);
		allSum.add(yDevicesSum);
		
		return allSum;	
	}
}
