package cn.lastmiles.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.CashGift;
import cn.lastmiles.bean.PromotionCoupon;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BarCode4jUtils;
import cn.lastmiles.common.utils.CompressFileUtils;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.FileUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.CashGiftService;
import cn.lastmiles.service.PromotionCouponService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("promotionCoupon")
public class PromotionCouponController {
	private final static Logger logger = LoggerFactory.getLogger(PromotionCouponController.class);
	@Autowired
	private PromotionCouponService promotionCouponService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private CashGiftService cashGiftService;

	@Autowired
	private IdService idService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/promotionCoupon/list";
	}
	
	/**
	 * 所有优惠列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "promotionCoupon/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name,Integer type,Integer status,String startDate,String endDate, Page page, Model model) {
		
		logger.debug("优惠名称={}",name);
		Long storeId=null;
		if (SecurityUtils.isStore()) {//如果是商家登录
			storeId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("data", promotionCouponService.findAll(storeId,name.trim(),type,status,startDate,endDate, page));
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "promotionCoupon/list-data";
		
	}
	
	/**
	 * 跳到优惠增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		PromotionCoupon promotionCoupon = new PromotionCoupon();
		promotionCoupon.setId(idService.getId());
		promotionCoupon.setURL(ConfigUtils.getProperty("coupon.url")+File.separator+promotionCoupon.getId());
		model.addAttribute("promotionCoupon",promotionCoupon);
		return "promotionCoupon/add";
	}
	
	/**
	 * 保存增加优惠或保存修改优惠
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String editPromotionCoupon(PromotionCoupon promotionCoupon,String minAmount,String maxAmount,
			String beginDate,String LastDate){
		logger.debug("minAmount======{},maxAmount======{},status={}",minAmount,maxAmount,promotionCoupon.getStatus());
		
		if (SecurityUtils.isStore()) {//如果是商家登录
			promotionCoupon.setStoreId(SecurityUtils.getAccountStoreId());
		}
		if(promotionCoupon.getType().intValue()==1){
			if(promotionCoupon.getTotalNum()==null){
				promotionCoupon.setTotalNum(0);
			}
			if(promotionCoupon.getTotalAmount()==null){
				promotionCoupon.setTotalAmount(0D);
			}
		}
		logger.debug("isDecimalBit======{}",isDecimalBit(minAmount,maxAmount));
		promotionCoupon.setStartDate(DateUtils.parse("yyyy-MM-dd HH:mm:ss", beginDate+" 00:00:00"));
		promotionCoupon.setEndDate(DateUtils.parse("yyyy-MM-dd HH:mm:ss", LastDate+ " 23:59:59"));
		if(minAmount.indexOf(".")<0 && maxAmount.indexOf(".")<0){
			promotionCoupon.setDecimalBit(0);
		}else{
			promotionCoupon.setDecimalBit(isDecimalBit(minAmount,maxAmount));
		}
		
		promotionCouponService.editPromotionCoupon(promotionCoupon);
		return "1";
	}
	
	/**
	 * 跳转修改界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		PromotionCoupon promotionCoupon= promotionCouponService.findById(id);//把promotionCoupon对像转回页面
		promotionCoupon.setIsUpdate(1);
		promotionCoupon.setURL(ConfigUtils.getProperty("coupon.url")+File.separator+promotionCoupon.getId());
		model.addAttribute("promotionCoupon", promotionCoupon);
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "/promotionCoupon/add";
	}
	
	@RequestMapping(value = "detailList/{couponId}",method = RequestMethod.GET)
	public String detailList(Model model,@PathVariable Long couponId) {
		logger.debug("couponId={}",couponId);
		model.addAttribute("couponId", couponId);
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "promotionCoupon/detailList";
	}
	@RequestMapping(value = "detailList-data")
	public String detailListData(Long couponId,String name, String mobile,Long storeId,Integer status,Page page, Model model) {
		logger.debug("storeId={},name={},mobile={},status={}",storeId,name,mobile,status);
		if (SecurityUtils.isStore()) {//如果是商家登录
			storeId=SecurityUtils.getAccountStoreId();
		}
		
		page = promotionCouponService.getCashGiftList(couponId,name, mobile,storeId,status, page);
		model.addAttribute("data", page);
		return "promotionCoupon/detailList-data";
	}
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		page = shopService.getShop(name, mobile, agentName, Constants.Status.SELECT_ALL, page);
		model.addAttribute("data", page);
		return "promotionCoupon/showModelList-data";
	}
	
	
	public static int isDecimalBit(String minAmount,String maxAmount){
		
		//String minAmounts=Double.toString(minAmount);
		//String maxAmounts=Double.toString(maxAmount);
		//System.out.println(minAmounts+"==="+maxAmounts);
		
		String minSub=minAmount.substring(minAmount.indexOf(".")+1, minAmount.length());
		String maxSub=maxAmount.substring(maxAmount.indexOf(".")+1, maxAmount.length());
		if(minAmount.indexOf(".")>0 && maxAmount.indexOf(".")<0){
			if(minSub.length()==1){//min小数为1位
				if("0".equals(minSub)){//min小数为0
					return 0;
				}else{
					return 1;
				}
			}else{
				if(minSub.equals("00")){
					return 0;
				}else if(minSub.charAt(1)=='0'){
					return 1;
				}else{
					return 2;
				}
			}
		}else if(minAmount.indexOf(".")<0 && maxAmount.indexOf(".")>0){
			if(maxAmount.length()==1){//min小数为1位
				if("0".equals(maxAmount)){//min小数为0
					return 0;
				}else{
					return 1;
				}
			}else{
				if(maxAmount.equals("00")){
					return 0;
				}else if(maxAmount.charAt(1)=='0'){
					return 1;
				}else{
					return 2;
				}
			}
		}else{
			if(minSub.length()==1 && maxSub.length()==1){
				if("0".equals(minSub)){//min小数为0
					if("0".equals(maxSub)){//max小数为0
						return 0;
					}else{
						return 1;
					}
				}else{
					return 1;
				}
			}else if(minSub.length()==2 && maxSub.length()==1){
				if(minSub.equals("00")){
					if("0".equals(maxSub)){//max小数为0
						return 0;
					}else{
						return 1;
					}
				}else if(minSub.charAt(1)=='0'){
					if("0".equals(maxSub)){//max小数为0
						return 0;
					}else{
						return 1;
					}
				}else{
					return 2;
				}
			}else if(minSub.length()==1 && maxSub.length()==2){
				if(maxSub.equals("00")){
					if("0".equals(minSub)){//max小数为0
						return 0;
					}else{
						return 1;
					}
				}else if(maxSub.charAt(1)=='0'){
					if("0".equals(minSub)){//max小数为0
						return 0;
					}else{
						return 1;
					}
				}else{
					return 2;
				}
			}else{
				if(minSub.equals("00")){
					if("00".equals(maxSub)){//max小数为0
						return 0;
					}else{
						if(maxSub.charAt(1)=='0'){
								return 1;
						}else{
							return 2;
						}
					}
				}else if(minSub.charAt(1)=='0'){
					if("00".equals(maxSub)){//max小数为0
						return 1;
					}else{
						if(maxSub.charAt(1)=='0'){
								return 1;
						}else{
							return 2;
						}
					}
				}else{
					return 2;
				}
			}

		}
	}
	
	/**
	 * 优惠活动列表的导出和重新导出方法调用
	 * @param request 请求
	 * @param response 响应
	 * @param promotionCouponId 需要导出的优惠活动ID
	 * @return 
	 */
	@RequestMapping(value="ajax/reportCashGiftBarcode") 
	public String reportCashGiftBarcode(HttpServletRequest request, HttpServletResponse response,Long promotionCouponId) {
		PromotionCoupon promotionCoupon = promotionCouponService.findById(promotionCouponId);
		if( null == promotionCoupon ){
			return null;
		}
		
		Integer issueType = promotionCoupon.getIssueType();
		if( null == issueType || issueType.intValue() != 2 ){
			return null;
		}
		String barcodeTempUrl = request.getServletContext().getRealPath("/WEB-INF/static/barcodeTemp/");
		String fileOrTitleName = promotionCoupon.getName();
		if( StringUtils.isBlank(fileOrTitleName) ){
			fileOrTitleName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		File tempDir = new File(barcodeTempUrl+fileOrTitleName);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();

		int forNum = 0; 
		
		// 生成条形码并记录
		forNum = produceBarcodeAndRecord(promotionCouponId, promotionCoupon,barcodeTempUrl, fileOrTitleName, tempDir, batchArgs, forNum);
		
		try {
			//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
	        response.setContentType("multipart/form-data"); 
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".rar", "utf-8"));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File((barcodeTempUrl+fileOrTitleName)+File.separator+fileOrTitleName+".rar")));// 放到缓冲流里
			BufferedOutputStream bouts = new BufferedOutputStream(response.getOutputStream());
			
			IOUtils.copy(bis, bouts);
			
			bis.close();
			bouts.flush();
			bouts.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != batchArgs && batchArgs.size() > 0 ){
				if(promotionCouponService.updateReportNumById(promotionCouponId,forNum)){ // forNum 修改已导出的数量标识 
					promotionCouponService.saveCashGiftBatch(batchArgs); // 往数据库中插入优惠券数据						
				}
			}
			if(tempDir.exists()){ 
				FileUtils.delete(tempDir.getAbsolutePath()); // 递归删除目录
			} 
		}
		return null;
	}

	private int produceBarcodeAndRecord(Long promotionCouponId,PromotionCoupon promotionCoupon, String barcodeTempUrl,
			String fileOrTitleName, File tempDir, List<Object[]> batchArgs, int forNum) {
		
		Integer totalNum = promotionCoupon.getTotalNum();
		if( null != totalNum && totalNum.intValue() > 0){
			
			List<CashGift> cashGiftList = null;
			Integer reportNum = promotionCoupon.getReportNum();
			
			if( null != reportNum ){
				if( reportNum.intValue() == 0 ){
					forNum = totalNum.intValue();
				}
				if( reportNum.intValue() != 0 && totalNum.intValue() - reportNum.intValue() != 0 ){
					forNum = totalNum.intValue() - reportNum.intValue();
				}
				if( totalNum.intValue() - reportNum.intValue() == 0 ){
					cashGiftList = cashGiftService.findByCouponId(promotionCouponId);					
				}
			}
			
			if(!tempDir.exists()){
				tempDir.mkdirs();
			} 
			
			if( null != cashGiftList && cashGiftList.size() > 0 ){
				for (CashGift cashGift : cashGiftList) {
					BarCode4jUtils.generateBarcode(cashGift.getId(),new File(barcodeTempUrl+fileOrTitleName+File.separator+cashGift.getId()+"("+cashGift.getAmount()+").jpg"));
				}
			}
			
			//记录数据到SQL临时存储器中并生成条形码
			if( forNum > 0 ){
				recordInSQLStorage(promotionCouponId, promotionCoupon,barcodeTempUrl, fileOrTitleName, batchArgs, forNum);
			}
			
			// 压缩
			CompressFileUtils.ZipFiles(tempDir.listFiles(), new File( (barcodeTempUrl+fileOrTitleName)+File.separator+fileOrTitleName+".rar"));
		}
		return forNum;
	}

	private void recordInSQLStorage(Long promotionCouponId,
			PromotionCoupon promotionCoupon, String barcodeTempUrl,
			String fileOrTitleName, List<Object[]> batchArgs, int forNum) {
		
			Integer shared = promotionCoupon.getShared();
			Double orderAmount = promotionCoupon.getOrderAmount();
			Integer type = promotionCoupon.getType();
			Long storeId = promotionCoupon.getStoreId();
			String memo = promotionCoupon.getMemo();
			Integer status = Constants.CashGift.STATUSNORMAL;
			Double minAmount = promotionCoupon.getMinAmount();
			Double maxAmount = promotionCoupon.getMaxAmount();
			Integer validDay = promotionCoupon.getValidDay();
			int decimalBit = promotionCoupon.getDecimalBit();
			
			boolean isSame = true;
			if( null != minAmount && null != maxAmount ){
				if( minAmount.doubleValue() == maxAmount.doubleValue() ){
					isSame = false;
				}
			}
			Date date = null;
			if( null != validDay && validDay.intValue() > 0 ){
				GregorianCalendar gc = new GregorianCalendar(); 
				gc.setTime(new Date()); 
				gc.add(Calendar.DATE,validDay.intValue());
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				date = gc.getTime();
			}
			
			for (int i = 0; i < forNum; i++) {
				Long barcode = idService.getId();
				Object[] arg = new Object[13];
				arg[0] = barcode; 	  		 //id
				arg[1] = null; 		  		 //userId
				double amount = isSame ? ThreadLocalRandom.current().nextDouble(minAmount, maxAmount): minAmount;
				
				switch(decimalBit){ // 根据小数点进行数据截取
					case 0: amount = BigDecimal.valueOf(amount).setScale(0,BigDecimal.ROUND_FLOOR).doubleValue(); break;
					case 1: amount = BigDecimal.valueOf(amount).setScale(1,BigDecimal.ROUND_FLOOR).doubleValue(); break;
					case 2: amount = BigDecimal.valueOf(amount).setScale(2,BigDecimal.ROUND_FLOOR).doubleValue(); break;
				}
				
				arg[2] = amount; 	  		 //amount
				arg[3] =status;		  		 //status
				arg[4] =new Date();   		 //createdTime
				arg[5] =memo;		  		 //memo
				arg[6] =date;		  		 //validTime
				arg[7] =storeId;	  		 //storeId
				arg[8] =type;		  		 //type
				arg[9] =null;		  		 //usedTime
				arg[10] =promotionCouponId;  //couponId
				arg[11] =shared;      		 //shared
				arg[12] =orderAmount; 		 //orderAmount
				
				batchArgs.add(arg);
				BarCode4jUtils.generateBarcode(barcode,new File(barcodeTempUrl+fileOrTitleName+File.separator+barcode+"("+amount+").jpg"));				
			}
	}
}