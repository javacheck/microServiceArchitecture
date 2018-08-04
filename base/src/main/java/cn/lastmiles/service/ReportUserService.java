package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.ReportUser;
import cn.lastmiles.bean.UserCardRecord;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.ReportUserDao;

@Service
public class ReportUserService {
	private final static Logger logger = LoggerFactory.getLogger(ReportUserService.class);
	@Autowired
	private ReportUserDao reportUserDao;

	public ReportUser findReportSalesSum(String storeIdString, Integer type) {
		
		return reportUserDao.findReportSalesSum(storeIdString,type);
	}
	
	@SuppressWarnings("unchecked")
	public Page findAllPage(String storeIdString, String beginTime, String endTime,Integer sort,
			Page page) {
		List<ReportUser> ruList=reportUserDao.findTotalReportUserList(storeIdString,endTime,sort);
		logger.debug("ruList={}",ruList);
		page=reportUserDao.findAllPage(storeIdString,beginTime,endTime,sort,page);
		List<ReportUser> rus = (List<ReportUser>) page.getData();
		for(int i=0;i<rus.size();i++){
			logger.debug("userNum()={},recharge={},consumption()={}",ruList.get(i).getTotalUserNum(),ruList.get(i).getTotalRecharge(),ruList.get(i).getTotalConsumption());
			rus.get(i).setTotalBalance(ruList.get(i).getTotalBalance());//会员余额
			rus.get(i).setTotalPoint(ruList.get(i).getTotalPoint());//会员剩余积分
			rus.get(i).setTotalUserNum(ruList.get(i).getTotalUserNum());
			rus.get(i).setTotalRecharge(ruList.get(i).getTotalRecharge());
			rus.get(i).setTotalConsumption(ruList.get(i).getTotalConsumption());
		}
		page.setData(rus);
		return page;
	}

	public ReportUser findAllSum(String storeIdString, String beginTime, String endTime) {
		
		return reportUserDao.findAllSum(storeIdString,beginTime,endTime);
	}

	public ReportUser findStoreMunSum(String storeIdString, String beginTime, String endTime) {
		return reportUserDao.findStoreMunSum(storeIdString,beginTime,endTime);
	}

	public List<ReportUser> findAll(String storeIdString, String beginTime,
			String endTime,Integer sort) {
		List<ReportUser> ruList=reportUserDao.findTotalReportUserList(storeIdString,endTime,sort);
		logger.debug("ruList={}",ruList);
		List<ReportUser> rus=reportUserDao.findAll(storeIdString,beginTime,endTime,sort);
		for(int i=0;i<rus.size();i++){
			logger.debug("userNum()={},recharge={},consumption()={}",ruList.get(i).getTotalUserNum(),ruList.get(i).getTotalRecharge(),ruList.get(i).getTotalConsumption());
			rus.get(i).setTotalBalance(ruList.get(i).getTotalBalance());//会员余额
			rus.get(i).setTotalPoint(ruList.get(i).getTotalPoint());//会员剩余积分
			rus.get(i).setTotalUserNum(ruList.get(i).getTotalUserNum());
			rus.get(i).setTotalRecharge(ruList.get(i).getTotalRecharge());
			rus.get(i).setTotalConsumption(ruList.get(i).getTotalConsumption());
		}
		return rus;
	}
	@SuppressWarnings("unchecked")
	public Page findStorePage(Long storeId, Integer type, String beginTime,
			String endTime, Page page) {
		List<ReportUser> ReturnUses = new ArrayList<ReportUser>();
		page= reportUserDao.findStorePage(storeId,type,beginTime,endTime,page);
		
		List<ReportUser> rus=(List<ReportUser>)page.getData();
		for (ReportUser ru : rus) {
			if(type.intValue()==0){
				ru.setDateString(DateUtils.format(ru.getReportDate(), "yyyy-MM-dd"));
			}else{
				String moonDate="";
				if(DateUtils.format(new Date(), "yyy-MM").equals(DateUtils.format(ru.getReportDate(), "yyy-MM"))){//如果是当前月
					moonDate=DateUtils.format(new Date(), "yyy-MM-dd");
				}else{
					moonDate=DateUtils.format(DateUtils.getLastDay(ru.getReportDate()), "yyy-MM-dd");
				}
				logger.debug("ru={}",ru);
				logger.debug("storeId={},moonDate={}",storeId,moonDate);
				ReportUser moonRu=reportUserDao.findreportUserByLastDate(storeId,moonDate);
				logger.debug("moonRu={}",moonRu);
				ru.setTotalBalance(moonRu.getTotalBalance());//会员余额
				ru.setTotalPoint(moonRu.getTotalPoint());//会员剩余积分
				ru.setTotalUserNum(moonRu.getTotalUserNum());//累计会员
				ru.setTotalRecharge(moonRu.getTotalRecharge());//累计储值
				ru.setTotalConsumption(moonRu.getTotalConsumption());//累计消费
				ru.setDateString(DateUtils.format(ru.getReportDate(), "yyyy-MM"));
			}
			ReturnUses.add(ru);
		
		page.setData(ReturnUses);
		}
		return page;
	}

	public ReportUser findStoreSum(Long storeId, Integer type, String beginTime,
			String endTime) {
		return reportUserDao.findStoreSum(storeId,type,beginTime,endTime);
	}

	public List<ReportUser> findStore(Long storeId, Integer type,
			String beginTime, String endTime) {
		List<ReportUser> ReturnUses = new ArrayList<ReportUser>();
		List<ReportUser> rus=reportUserDao.findStore(storeId,type,beginTime,endTime);
		for (ReportUser ru : rus) {
			if(type.intValue()==0){
				ru.setDateString(DateUtils.format(ru.getReportDate(), "yyyy-MM-dd"));
			}else{
				String moonDate="";
				if(DateUtils.format(new Date(), "yyy-MM").equals(DateUtils.format(ru.getReportDate(), "yyy-MM"))){//如果是当前月
					moonDate=DateUtils.format(new Date(), "yyy-MM-dd");
				}else{
					moonDate=DateUtils.format(DateUtils.getLastDay(ru.getReportDate()), "yyy-MM-dd");
				}
				logger.debug("ru={}",ru);
				ReportUser moonRu=reportUserDao.findreportUserByLastDate(storeId,moonDate);
				ru.setTotalBalance(moonRu.getTotalBalance());//会员余额
				ru.setTotalPoint(moonRu.getTotalPoint());//会员剩余积分
				ru.setTotalUserNum(moonRu.getTotalUserNum());//累计会员
				ru.setTotalRecharge(moonRu.getTotalRecharge());//累计储值
				ru.setTotalConsumption(moonRu.getTotalConsumption());//累计消费
				ru.setDateString(DateUtils.format(ru.getReportDate(), "yyyy-MM"));
			}
			ReturnUses.add(ru);
		}
		return ReturnUses;
	}
	
	public List<ReportUser> findReportUserDateList(){
		return reportUserDao.findReportUserDateList();
	}
	
	public int insertReportUser(Long id,Long storeId,String date){
		return reportUserDao.insertReportUser(id, storeId, date);
	}

	public int updateReportUser(Long id, Long storeId, String date, String time) {
		if( StringUtils.isBlank(time) ){
			time = date+" 23:59:59";
		}
		return reportUserDao.updateReportUser(id,storeId,date,time);
	}

	public List<ReportUser> findReportUserStoreIdListByDate(String time) {
		return reportUserDao.findReportUserStoreIdListByDate(time);
	}

	public ReportUser findReportSumByTime(Long storeId, Integer type,
			String beginTime, String endTime) {
		
		return reportUserDao.findStoreSumByTime(storeId, type, beginTime, endTime);
	}

	public ReportUser findAllSumByTime(String storeIdString, String beginTime,
			String endTime) {
		ReportUser ru= reportUserDao.findAllSumByTime(storeIdString,beginTime,endTime);
		ReportUser reportUser= reportUserDao.findTotolSumByTime(storeIdString,endTime);
		ru.setTotalBalance(reportUser.getTotalBalance());
		ru.setTotalPoint(reportUser.getTotalPoint());
		return ru;
	}

	public ReportUser findByUserCardCreatedTime(Long storeId,String tempDate) {
		
		//新增储值recharge(会员卡记录里的amount type=1充值 7退款)
		Double recharge=reportUserDao.findAmountSumByCreatedTime(storeId,tempDate,1);
		if(recharge==null){
			recharge=0D;
		}
		//消费金额consumption(订单里useBalance type=2消费)
		List<UserCardRecord> ucrs=reportUserDao.findByCreatedTime(storeId,tempDate,2);
		Double consumption=0D;
		if(!ucrs.isEmpty()){
			for(UserCardRecord ucr:ucrs){
				Order order=reportUserDao.findByOrderId(ucr.getOrderId());
				if(order!=null){
					consumption=NumberUtils.add(consumption,order.getUseBalance());
				}
			}
		}
		//会员余额totalBalance 会员积分totalPoint(会员里的balance point)
		ReportUser ru=(ReportUser) reportUserDao.findUserCardSumByCreatedTime(storeId,tempDate);
		ru.setConsumption(consumption);//消费金额
		ru.setRecharge(recharge);//新增储值
		return ru;
	}

	public ReportUser findByReportDate(Long storeId, String tempDate) {
		
		return reportUserDao.findByReportDate(storeId,tempDate);
	}

	public void saveReportUser(ReportUser ru) {
		reportUserDao.saveReportUser(ru);
	}

	public void updateReportUser(ReportUser ru) {
		reportUserDao.updateReportUser(ru);
		
	}
}
