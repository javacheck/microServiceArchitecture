package cn.lastmiles.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;








import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.OrderItemServise;
import cn.lastmiles.service.OrderServise;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.UserCardRecordService;
import cn.lastmiles.service.UserCardService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("order")
public class OrderController {
	private final static Logger logger = LoggerFactory
			.getLogger(OrderController.class);
	
	@Autowired
	private OrderServise orderServise;
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private OrderItemServise orderItemServise;
	@Autowired
	private ShopService shopService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private UserCardRecordService userCardRecordService;
	
	@RequestMapping("")
	public String index() {
		return "redirect:/order/list";
	}

	@RequestMapping("list")
	public String list(Page page, Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "order/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String beginTime,String endTime,Integer paymentMode,
			String mobile,String orderId,Long storeId,Integer status,Integer haveReturnGoods,
			String memo,Page page, Model model) {
		if (StringUtils.isNotBlank(mobile)){
			mobile = mobile.trim();
		}
		if (StringUtils.isNotBlank(orderId)){
			orderId = orderId.trim();
		}
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
		logger.debug("storeIdString.toString()={},beginTime={},endTime={}",storeIdString.toString(),beginTime,endTime);
		// 判断登录者是商家且非总店(2016-01-19-update)
		model.addAttribute("isStore", SecurityUtils.isStore() ? SecurityUtils.isStore()^SecurityUtils.isMainStore() : false); 
		model.addAttribute("data", orderServise.list(beginTime, endTime, paymentMode,mobile, orderId, storeIdString.toString(),1,status,haveReturnGoods,memo, page));
		return "order/list-data";
	}
	@RequestMapping(value = "shopList/list")
	public String shopList() {
		return "order/shopList";
	}

	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String shopListData(String name, String mobile,Page page, Model model) {
		String agentName="";
		StringBuffer storeIdString = new StringBuffer();
		if(SecurityUtils.isMainStore()){
			List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
			boolean index = false;
			for (Store store : storeList) {
				if(index){
					storeIdString.append(",");
				}
				storeIdString.append(store.getId());
				index = true;
			}
		}
		page = shopService.getAllShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
		model.addAttribute("data", page);
		return "order/showModelList-data";
	}
	@RequestMapping(value="info/showMode/{id}" )
	public String info(@PathVariable Long id, Model model){
		logger.debug("order-info-ID IS :"+id);
		Order order = orderServise.findById(id);
		
		//会员卡
		if (order.getUserId() != null && order.getUserId().intValue() != -1){
			UserCard uc = userCardService.findByMobileAndStoreId(order.getUser().getMobile(), order.getStoreId());
			model.addAttribute("uc", uc);
		}
		
		logger.debug("cashgift : {}" + order.getCashGift());
		model.addAttribute("order",order);
		return "order/info";
	}
	@RequestMapping(value="appInfo/showMode/{id}" )
	public String appInfo(@PathVariable Long id, Model model){
		logger.debug("order-info-ID IS :"+id);
		Order order = orderServise.findById(id);
		model.addAttribute("order",order);
		return "orderApp/info";
	}
	@RequestMapping(value="typeChange/change-by-orderId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String typeChangeByorderId(Long orderId,Integer status){
		logger.debug("状态==="+status);
		orderServise.typeChangeByorderId(orderId,status);
		return "1";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findAllBySearch(HttpServletResponse response,
			String beginTime, String endTime, Long storeId, String mobile,
			Integer paymentMode,String orderId,Integer status,String memo,Integer haveReturnGoods) throws ParseException {
		DecimalFormat    df   = new DecimalFormat("######0.00"); 
		StringBuffer storeIdString = new StringBuffer();
		if (SecurityUtils.isStore()) {//如果是商家登录
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){ // 没总店订单商家查看权限的情况下，只显示总店自己的订单信息
					storeIdString.append(SecurityUtils.getAccountStoreId().toString());					
				} else if( null != storeId && ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
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
		}else{
			if( null != storeId ){
				storeIdString.append(storeId.toString());
			}
		}
		
		String fileName = "订单列表";
		logger.debug("storeId={},mobile={},id={}",storeIdString.toString(),mobile,orderId);
		List<Order> orderList = orderServise.findPOSBySearch(beginTime,
				endTime, storeIdString.toString(), mobile, paymentMode,orderId,1,status,memo,haveReturnGoods);
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		for(int i=0;i<19;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("订单编号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("商家");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("交易时间");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("商品总价");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("总优惠");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第6格
				cell.setCellValue("实付金额");
				cell.setCellStyle(style);
			}else if(i==6){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("支付方式");
				cell.setCellStyle(style);
			}else if(i==7){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("金额");
				cell.setCellStyle(style);
			}else if(i==8){
				cell = row.createCell(i);// 第一行第7格
				cell.setCellValue("状态");
				cell.setCellStyle(style);
			}else if (i==9){
				cell = row.createCell(i);// 第一行第16格
				cell.setCellValue("售后");
				cell.setCellStyle(style);
			}else if (i==10){
				cell = row.createCell(i);// 第一行第16格
				cell.setCellValue("退款金额");
				cell.setCellStyle(style);
			}else if(i==11){
				cell = row.createCell(i);// 第一行第8格
				cell.setCellValue("商品名称");
				cell.setCellStyle(style);
			}else if(i==12){
				cell = row.createCell(i);// 第一行第9格
				cell.setCellValue("单价");
				cell.setCellStyle(style);
			}else if(i==13){
				cell = row.createCell(i);// 第一行第10格
				cell.setCellValue("数量");
				cell.setCellStyle(style);
			}else if(i==14){
				cell = row.createCell(i);// 第一行第11格
				cell.setCellValue("会员手机");
				cell.setCellStyle(style);
			}else if(i==15){
				cell = row.createCell(i);// 第一行第12格
				cell.setCellValue("会员卡号");
				cell.setCellStyle(style);
			}else if(i==16){
				cell = row.createCell(i);// 第一行第13格
				cell.setCellValue("会员姓名");
				cell.setCellStyle(style);
			}else if(i==17){
				cell = row.createCell(i);// 第一行第14格
				cell.setCellValue("会员性别");
				cell.setCellStyle(style);
			}else if(i==18){
				cell = row.createCell(i);// 第一行第15格
				cell.setCellValue("会员备注");
				cell.setCellStyle(style);
			}
		}
		logger.debug("长度＝＝＝＝＝＝＝＝＝{}",orderList.size());
		int index=1;
		if (!orderList.isEmpty()) {
			logger.debug("长度1＝＝＝＝＝＝＝＝＝{}",orderList.size());
			for (int i = 0; i < orderList.size(); i++) {// 通过nameList有长度创建行数(第二行开始)
				Order order=orderList.get(i);
				List<OrderItem> ois=order.getOrderItems();
				if(ois.size()==1){//订单只有一条商品
					row = sheet.createRow((int) index);
					row.createCell(0).setCellValue(order.getId());//订单编号
					row.createCell(1).setCellValue(order.getStore().getName());//商家	
					row.createCell(2).setCellValue(DateUtils.format(order.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//时间
					
					row.createCell(3).setCellValue("￥"+df.format(order.getPrice()));//商品总价
					//总优惠
					if(order.getActualPrice()==null){
						row.createCell(4).setCellValue("");
					}else{
						Double a=NumberUtils.subtract(order.getPrice(), order.getActualPrice());
						row.createCell(4).setCellValue("￥"+df.format(a));
					}
					row.createCell(5).setCellValue(order.getActualPrice()==null?"":"￥"+df.format( order.getActualPrice() ));//实付金额
					//支付方式
					if(order.getPaymentMode().intValue()==0){
						row.createCell(6).setCellValue("支付宝");
					}else if(order.getPaymentMode().intValue()==1){
						row.createCell(6).setCellValue("微信");
					}else if(order.getPaymentMode().intValue()==2){
						row.createCell(6).setCellValue("刷卡");
					}else if(order.getPaymentMode().intValue()==3){
						row.createCell(6).setCellValue("现金");
					}else if(order.getPaymentMode().intValue()==9){
						row.createCell(6).setCellValue("会员卡");
					}
					Double p1=NumberUtils.subtract(NumberUtils.subtract(order.getCashPrice(), order.getChange()),order.getActualPrice());
					Double p2=NumberUtils.subtract(order.getActualPrice(),order.getCashPrice());
					Double p3=NumberUtils.add(p2, order.getChange());
					if(order.getPaymentMode().intValue()==3){//现金
						row.createCell(7).setCellValue("￥"+df.format(order.getActualPrice()));
					}else if(order.getPaymentMode().intValue()==9 && p1==0){
						row.createCell(7).setCellValue("￥"+df.format(0.00D));
					}else if(order.getPaymentMode().intValue()==9 && p1<0){
						row.createCell(7).setCellValue("￥"+df.format(p3));
					}else{
						row.createCell(7).setCellValue("￥"+df.format(p2));
					}
					row.createCell(8).setCellValue(order.getStatusDes());
					logger.debug("ois.get(j).getProductStock()＝{}}",ois.get(0).getProductStock());
					row.createCell(9).setCellValue(order.getHaveReturnGoods()==0?"无":"有退货");//售后haveReturnGoods
					row.createCell(10).setCellValue("￥"+df.format(order.getSumReturnGoodsPrice()));
					
					if(ois.get(0).getProductStock()==null){
						row.createCell(11).setCellValue("");
					}else{
						row.createCell(11).setCellValue(ois.get(0).getProductStock().getAttributeName());//商品名称
					}
					row.createCell(12).setCellValue("￥"+df.format(ois.get(0).getPrice()));//商品单价
					row.createCell(13).setCellValue(ois.get(0).getAmount());//商品数量
					
					if(order.getUser()!=null){
						if(order.getUserCard()!=null){
							row.createCell(14).setCellValue(order.getUserCard().getMobile()!=null?order.getUserCard().getMobile():"非会员");//会员手机
							row.createCell(15).setCellValue(order.getUserCard().getMobile()!=null?order.getUserCard().getCardNum():"");//会员卡号
							row.createCell(16).setCellValue(order.getUserCard().getName()!=null?order.getUserCard().getName():"");//会员姓名
							
							if(order.getUser().getSex().intValue()==0){//会员姓别
								row.createCell(17).setCellValue("男");
							}else if(order.getUser().getSex().intValue()==1){
								row.createCell(17).setCellValue("女");
							}else{
								row.createCell(17).setCellValue("");
							}
							row.createCell(18).setCellValue(order.getUserCard().getMemo()!=null?order.getUserCard().getMemo():"");//会员备注
						}
					}
					if(order.getPaymentMode().intValue()!=3 && order.getCashPrice()>0){//有两种支付方式
						row = sheet.createRow((int) index+1);//增加一行显支付方式和金额
						row.createCell(6).setCellValue("现金");//支付方式
						row.createCell(7).setCellValue("￥"+df.format(NumberUtils.subtract(order.getCashPrice(), order.getChange())));
						
						index=index+2;//有两种支付方式
					}else{//没有第二种支付方式
						index++;//没有第二种支付方式
					}
					
				}else{//ois.size()>1
					for(int j=0;j<ois.size();j++){
						if(j==0){
							row = sheet.createRow((int) index);
							row.createCell(0).setCellValue(order.getId());//订单编号
							row.createCell(1).setCellValue(order.getStore().getName());//商家	
							row.createCell(2).setCellValue(DateUtils.format(order.getCreatedTime(), "yyyy-MM-dd HH:mm:ss"));//时间
							
							row.createCell(3).setCellValue("￥"+df.format(order.getPrice()));//商品总价
							//总优惠
							if(order.getActualPrice()==null){
								row.createCell(4).setCellValue("");
							}else{
								Double a=NumberUtils.subtract(order.getPrice(), order.getActualPrice());
								row.createCell(4).setCellValue("￥"+df.format(a));
							}
							row.createCell(5).setCellValue(order.getActualPrice()==null?"":"￥"+df.format( order.getActualPrice() ));//实付金额
							//支付方式
							if(order.getPaymentMode().intValue()==0){
								row.createCell(6).setCellValue("支付宝");
							}else if(order.getPaymentMode().intValue()==1){
								row.createCell(6).setCellValue("微信");
							}else if(order.getPaymentMode().intValue()==2){
								row.createCell(6).setCellValue("刷卡");
							}else if(order.getPaymentMode().intValue()==3){
								row.createCell(6).setCellValue("现金");
							}else if(order.getPaymentMode().intValue()==9){
								row.createCell(6).setCellValue("会员卡");
							}
							Double p1=NumberUtils.subtract(NumberUtils.subtract(order.getCashPrice(), order.getChange()),order.getActualPrice());
							Double p2=NumberUtils.subtract(order.getActualPrice(),order.getCashPrice());
							Double p3=NumberUtils.add(p2, order.getChange());
							if(order.getPaymentMode().intValue()==3){//现金
								row.createCell(7).setCellValue("￥"+df.format(order.getActualPrice()));
							}else if(order.getPaymentMode().intValue()==9 && p1==0){
								row.createCell(7).setCellValue("￥"+df.format(0.00D));
							}else if(order.getPaymentMode().intValue()==9 && p1<0){
								row.createCell(7).setCellValue("￥"+df.format(p3));
							}else{
								row.createCell(7).setCellValue("￥"+df.format(p2));
							}
							row.createCell(8).setCellValue(order.getStatusDes());
							logger.debug("ois.get(j).getProductStock()＝{}}",ois.get(j).getProductStock());
							row.createCell(9).setCellValue(order.getHaveReturnGoods()==0?"无":"有退货");//售后haveReturnGoods
							row.createCell(10).setCellValue("￥"+df.format(order.getSumReturnGoodsPrice()));
							
							if(ois.get(j).getProductStock()==null){
								row.createCell(11).setCellValue("");
							}else{
								row.createCell(11).setCellValue(ois.get(j).getProductStock().getAttributeName());//商品名称
							}
							row.createCell(12).setCellValue("￥"+df.format(ois.get(j).getPrice()));//商品单价
							row.createCell(13).setCellValue(ois.get(j).getAmount());//商品数量
							
							if(order.getUser()!=null){
								if(order.getUserCard()!=null){
									row.createCell(14).setCellValue(order.getUserCard().getMobile()!=null?order.getUserCard().getMobile():"非会员");//会员手机
									row.createCell(15).setCellValue(order.getUserCard().getMobile()!=null?order.getUserCard().getCardNum():"");//会员卡号
									row.createCell(16).setCellValue(order.getUserCard().getName()!=null?order.getUserCard().getName():"");//会员姓名
									
									if(order.getUser().getSex().intValue()==0){//会员姓别
										row.createCell(17).setCellValue("男");
									}else if(order.getUser().getSex().intValue()==1){
										row.createCell(17).setCellValue("女");
									}else{
										row.createCell(17).setCellValue("");
									}
									row.createCell(18).setCellValue(order.getUserCard().getMemo()!=null?order.getUserCard().getMemo():"");//会员备注
								}
							}
							index++;
						}else if(j==1){
							row = sheet.createRow((int) index);
							if(order.getPaymentMode().intValue()!=3 && order.getCashPrice()>0){//有两种支付方式
								row.createCell(6).setCellValue("现金");//支付方式
								row.createCell(7).setCellValue("￥"+df.format(NumberUtils.subtract(order.getCashPrice(), order.getChange())));
							}
							if(ois.get(j).getProductStock()==null ){
								row.createCell(11).setCellValue("");
							}else{
								row.createCell(11).setCellValue(ois.get(j).getProductStock().getAttributeName());//商品名称
							}
							row.createCell(12).setCellValue("￥"+df.format(ois.get(j).getPrice()));//商品单价
							row.createCell(13).setCellValue(ois.get(j).getAmount());//商品数量
							index++;
						}else{
							row = sheet.createRow((int) index);
							if(ois.get(j).getProductStock()==null ){
								row.createCell(11).setCellValue("");
							}else{
								row.createCell(11).setCellValue(ois.get(j).getProductStock().getAttributeName());//商品名称
							}
							row.createCell(12).setCellValue("￥"+df.format(ois.get(j).getPrice()));//商品单价
							row.createCell(13).setCellValue(ois.get(j).getAmount());//商品数量
							index++;
						}
					}
				}
				
				

				
			}
		}
		// 第六步，将文件存到指定位置
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			os.flush();
			// 关闭输出流
			os.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
