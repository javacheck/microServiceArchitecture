package cn.lastmiles.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.OrderItem;
import cn.lastmiles.bean.movie.DateSetting;
import cn.lastmiles.bean.movie.Room;
import cn.lastmiles.bean.movie.RoomCategory;
import cn.lastmiles.bean.movie.RoomCategoryDateSetting;
import cn.lastmiles.bean.movie.RoomOpen;
import cn.lastmiles.bean.movie.RoomOpenDetail;
import cn.lastmiles.bean.movie.RoomPackage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.RoomCategoryDao;
import cn.lastmiles.dao.RoomCategoryDateSettingDao;
import cn.lastmiles.dao.RoomDao;
import cn.lastmiles.dao.RoomDateSettingDao;

@Service
public class RoomService {
	private final static Logger logger = LoggerFactory.getLogger(RoomService.class);
	
	@Autowired
	private IdService idService;
	@Autowired
	private RoomPackageService roomPackageService;
	@Autowired
	private RoomDao roomdao;
	@Autowired
	private RoomCategoryDao roomCategoryDao;
	@Autowired
	private RoomCategoryDateSettingDao roomCategoryDateSettingDao;
	@Autowired
	private RoomDateSettingDao roomDateSettingDao;
	
	
	/**
	 * 开台要进行的操作
	 * @param order
	 */
	public void roomOpen(Order order){
		this.changStatus(order.getRoomId(), Constants.Room.STATUS_USERED);
		RoomOpen roomOpen = new RoomOpen();
		roomOpen.setOrderId(order.getId());
		roomOpen.setStartTime(order.getCreatedTime());
		roomOpen.setStoreId(order.getStoreId());
		roomOpen.setTotalAmount(order.getPrice());
		roomOpen.setRoomId(order.getRoomId());
		for (OrderItem orderItem : order.getOrderItems()) {
			roomOpen.setChargeType(orderItem.getType());
			if (orderItem.getType().intValue()==Constants.OrderItem.TYPE_PRODUCT) {//如果是商品
			}else if(orderItem.getType().intValue()==Constants.OrderItem.TYPE_ROOM){
				roomOpen.setDuration(orderItem.getAmount().intValue());
				roomOpen.setEndTime(DateUtils.addMinute(order.getCreatedTime(), orderItem.getAmount().intValue()));
			}else if(orderItem.getType().intValue()==Constants.OrderItem.TYPE_PACKAGE){
				RoomPackage roomPackage =roomPackageService.findById(orderItem.getStockId());
				roomOpen.setDuration(roomPackage.getDuration());
				roomOpen.setEndTime(DateUtils.addMinute(order.getCreatedTime(), roomPackage.getDuration()));
			}
		}
	}
	public void addProduct(List<OrderItem> orderItems){
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getType().intValue()==Constants.OrderItem.TYPE_PRODUCT) {//如果是商品
				RoomOpenDetail roomOpenDetail = new RoomOpenDetail();
				/*
				roomOpenDetail.setName("");
				roomOpenDetail.setNumber("");
				roomOpenDetail.setPrice("");
				roomOpenDetail.setProductStockId("");
				roomOpenDetail.setRoomOpenId("");
				*/
			}else if(orderItem.getType().intValue()==Constants.OrderItem.TYPE_ROOM){
				
			}else if(orderItem.getType().intValue()==Constants.OrderItem.TYPE_PACKAGE){
				
				RoomPackage roomPackage =roomPackageService.findById(orderItem.getStockId());
			}
		}
	}
	
	
	public void add(Room room){
		if (room.getId()==null) {
			room.setId(idService.getId());
		}
		room.setStatus(Constants.Room.STATUS_IDLE);
		roomdao.save(room);
	}
	public void update(Room room){
		roomdao.update(room);
	}
	public Page list(Long storeId,String number, Integer status, Long categoryId, Page page) {
		return roomdao.list(storeId,number,status,categoryId,page);
	}
	/**
	 * 检测分类ID是否已经使用
	 * @param id
	 * @return
	 */
	public boolean checkCategoryIsUse(Long id) {
		return roomdao.checkCategoryIsUse(id);
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		return false;
	}
	public Room findById(Long id) {
		return roomdao.findById(id);
	}
	public void changStatus(Long id, Integer status) {
		roomdao.changStatus(id,status);
	}
	/**
	 * 通过包间名称判断是否可以注册
	 * @param id
	 * @param storeId
	 * @param name
	 * @return 是否可以注册
	 */
	public boolean checkNameRepeat(Long id, Long storeId, String name) {
		return roomdao.checkNameRepeat(id,storeId,name);
	}
	/**
	 * 通过包间号码判断是否可以注册
	 * @param id
	 * @param storeId
	 * @param name
	 * @return 是否可以注册
	 */
	public boolean checkNumberRepeat(Long id, Long storeId, String number) {
		return roomdao.checkNumberRepeat(id,storeId,number);
	}
	public Double reckonRoomPrice(Date beginTime, Integer minute,Long storeId,Room room, boolean isUser) {
		beginTime = DateUtils.getDate(beginTime, "yyyy-MM-dd HH:mm");
		if (minute < 10 ){
			return 0.0;
		}
		//消费时间计费原则为小于10分钟不计费，大于等于10小于40分钟按半小时算，大于40分钟按1小时算  凌晨4点前算前一天
		Long roomId = room.getId();
		Date endTime=DateUtils.addMinute(beginTime, minute);
		Integer mod = minute % 60;
		if (mod < 10){
			endTime = DateUtils.addMinute(endTime, -mod);
		}else if (mod <= 30){
			endTime = DateUtils.addMinute(endTime, 30 - mod);
		}else if (mod < 40){
			endTime = DateUtils.addMinute(endTime, 30 - mod);
		}else {
			endTime = DateUtils.addMinute(endTime, 60 - mod);
		}
		//按照小时分开的时间
		List<Map<String, Date>> list = findDate(beginTime, endTime);
		logger.debug("date list = {}",list);
		//时间段
		List<DateSetting> dateSettingList = roomDateSettingDao.findByStoreId(storeId,3);
		//节假日
		List<DateSetting> holidayList = roomDateSettingDao.findByStoreId(storeId, 4);
		List<DateSetting> holidayDateSettingList = roomDateSettingDao.findByStoreId(storeId, 5);
		
		RoomCategory roomCategory = roomCategoryDao.findByRoomId(roomId);
		List<RoomCategoryDateSetting> categoryDateSettingList = roomCategoryDateSettingDao.findByCategoryId(roomCategory.getId());
		
		Double total = 0.0;
		
		//收费，合并时间段一致的
		Map<Double, Integer> priceMinute = new HashMap<Double, Integer>();
		for (Map<String, Date> map : list){
			Long mm = DateUtils.getMinuteDiff(map.get("start"), map.get("end"));
			Double price = 0.0;
			//是否是节假日
			if (isHoliday(map.get("start"), holidayList)){
				//是否在节假日时间段内
				DateSetting ds = isInDateSetting(map, holidayDateSettingList);
				if (ds != null){
					price = findPrice(ds, categoryDateSettingList,isUser);
					if (price == null || price.doubleValue() == 0.0){
						if (isUser){
							price = room.getBaseUserPrice();
						}else {
							price = room.getBasePrice();
						}
					}
				}else {
					if (isUser){
						price = room.getBaseUserPrice();
					}else {
						price = room.getBasePrice();
					}
				}
			}else {
				//是否在时间段内
				DateSetting ds = isInDateSetting(map, dateSettingList);
				if (ds != null){
					price = findPrice(ds, categoryDateSettingList,isUser);
					if (price == null || price.doubleValue() == 0.0){
						if (isUser){
							price = room.getBaseUserPrice();
						}else {
							price = room.getBasePrice();
						}
					}
				}else {
					//基础收费
					if (isUser){
						price = room.getBaseUserPrice();
					}else {
						price = room.getBasePrice();
					}
				}
			}
			
			if (price == null){
				price = room.getBasePrice();
			}
			
			if (priceMinute.containsKey(price)){
				priceMinute.put(price, priceMinute.get(price) + mm.intValue());
			}else {
				priceMinute.put(price, mm.intValue());
			}
			
		}
		logger.debug("priceMinute =  {}",priceMinute);
		Set<Double> set = priceMinute.keySet();
		for (Double d : set){
			if (d != null){
				Double tmp = NumberUtils.divideHALFEVEN(Double.valueOf(priceMinute.get(d)), 60);
				logger.debug("Double d ========== {},tmp = {}",d,tmp);
				tmp = NumberUtils.multiply(tmp.toString(),d.toString());
				total = NumberUtils.add(tmp, total);
			}
		}
		//结果四舍五入月
		return new BigDecimal(String.valueOf(total)).setScale(2,
				BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	private List<Map<String, Date>> findDate(Date beginTime,Date endTime){
		//计算出所有按小时分段的时间
		List<Date> dateList = DateUtils.getDay24HList(beginTime,endTime);
		List<Map<String, Date>> list = new ArrayList<Map<String,Date>>();
		for (int i = 0; i < dateList.size() - 1; i++){
			if (beginTime.getTime() >= dateList.get(i).getTime() && beginTime.getTime() < dateList.get(i+1).getTime()){
				if ( dateList.get(i+1).getTime() < endTime.getTime()){
					Map<String,Date> m = new HashMap<String, Date>();
					m.put("start", beginTime);
					m.put("end", dateList.get(i+1));
					beginTime = dateList.get(i+1);
					list.add(m);
				}else {
					Map<String,Date> m = new HashMap<String, Date>();
					m.put("start", beginTime);
					m.put("end", endTime);
					list.add(m);
					break;
				}
			}
		}
		return list;
	}
	
	private Double findPrice(DateSetting ds , List<RoomCategoryDateSetting> categoryDateSettingList,boolean isUser){
		for (RoomCategoryDateSetting rcds  : categoryDateSettingList){
			if (ds.getId().equals(rcds.getDateSettingId())){
				if (isUser){
					return rcds.getUserPrice();
				}else {
					return rcds.getPrice();
				}
			}
		}
		return 0D;
	}
	
	private DateSetting isInDateSetting(Map<String, Date> map, List<DateSetting> dateSettingList){
		for (DateSetting ds : dateSettingList){
			Date start = map.get("start");
			Date end = map.get("end");
			Date dsStart = DateUtils.parse("yyyy-MM-dd HH", DateUtils.format(start, "yyyy-MM-dd ") + getHour(ds.getStartTime().intValue()) ) ;
			Date dsEnd = DateUtils.parse("yyyy-MM-dd HH", DateUtils.format(start, "yyyy-MM-dd ") + getHour(ds.getEndTime().intValue()) ) ;
			if (start.getTime() >= dsStart.getTime()
					&& end.getTime() <= dsEnd.getTime()){
				return ds;
			}
		}
		return null;
	}
	
	private boolean isHoliday(Date date,List<DateSetting> holidayList){
		// 凌晨4点前算前一天  周六日算节假日     周六凌晨4点后，周日凌晨4点前算节假日 
		int week = DateUtils.getWeek(date);// 1是周日
		
		if (week == 1){//周日
			return true;
		}
		
		int hour = DateUtils.getHour(date);
		
		if (week == 2 && hour < 4){//周一凌晨
			return true;
		}
		
		if (week == 7 && hour >= 4){//周六 从凌晨4点开始
			return true;
		}
		
		for (DateSetting ds : holidayList){
			if (DateUtils.theSameDay(date, ds.getHoliday()) && hour >= 4){
				return true;
			}
			
			if (DateUtils.theSameDay(DateUtils.addDay(date, -1), ds.getHoliday()) && hour < 4){
				return true;
			}
		}
		return false;
	}
	
	private String getHour(int hour){
		if (hour < 10 ){
			return "0" + hour;
		}else {
			return String.valueOf(hour);
		}
	}
	public void updateIsRemind(Long id, Integer isRemind) {
		roomdao.updateIsRemind(id,isRemind);
	}
	public Integer countNeedRemind(Long storeId) {
		return  roomdao.countRemind(storeId,1);
	}
}
