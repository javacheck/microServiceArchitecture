package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.CashGift;
import cn.lastmiles.bean.PromotionCoupon;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class PromotionCouponDao {
	private final static Logger logger = LoggerFactory.getLogger(PromotionCouponDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Page findAll(Long storeId,String name, Integer type, Integer status,
			String startDate, String endDate, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		
		if( null != storeId){
			and.append(" and (t.storeId = ? or t.storeId is null) ");
			parameters.add(storeId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and t.name like ?");
			parameters.add("%" + name + "%");
		}
		
		if( null != type){
			and.append(" and t.type = ?");
			parameters.add(type);
		}
		//传进来为0表示已停止 1表示进行中 2即将开始
		if( null != status){
			if(status.intValue()==2){
				and.append(" and ?<t.startDate and t.status=1 ");
				parameters.add(new Date());
			}else if(status.intValue()==0){
				and.append(" and (t.status=? or t.status=2)");//2发放结束
				parameters.add(status);
			}else{
				and.append(" and t.status = 1 and ?>t.startDate and ?<t.endDate");
				parameters.add(new Date());
				parameters.add(new Date());
			}
		}
		if (StringUtils.isNotBlank(startDate)) {
			and.append(" and t.startDate >= ?");
			parameters.add(startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			and.append(" and t.endDate <= ?");
			parameters.add(endDate + " 23:59:59");
		}
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_promotion_coupon t where 1=1 " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);
		System.out.println("select count(1) from t_promotion_coupon t where 1=1 " + and.toString());
		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.name,t.type,t.minAmount,t.maxAmount, "
				+ " t.totalNum,t.totalAmount,t.startDate,t.endDate,t.storeId,t.issueType,t.reportNum,t.decimalBit, "
				+ " t.range,t.shared,t.orderAmount,t.validDay,t.memo,t.status,"
				+ "(select name from t_store s where s.id=t.storeId) as storeName "
				+ " from t_promotion_coupon t where 1=1 ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		System.out.println("sql===="+sql.toString());
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(PromotionCoupon.class, list));
		
		return page;
	}

	public void save(PromotionCoupon promotionCoupon) {
		if(promotionCoupon.getType().intValue()==0){
			promotionCoupon.setTotalAmount(null);
		}
		jdbcTemplate
				.update("insert into t_promotion_coupon(id,name,type,minAmount,maxAmount,totalNum,"
						+ "totalAmount,startDate,endDate,storeId,`range`,"
						+ "shared,orderAmount,validDay,memo,status,decimalBit,issueType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						promotionCoupon.getId(), promotionCoupon.getName(),
						promotionCoupon.getType(),promotionCoupon.getMinAmount(),
						promotionCoupon.getMaxAmount(),promotionCoupon.getTotalNum(),
						promotionCoupon.getTotalAmount(),promotionCoupon.getStartDate(),
						promotionCoupon.getEndDate(),promotionCoupon.getStoreId(),
						promotionCoupon.getRange(),promotionCoupon.getShared(),
						promotionCoupon.getOrderAmount(),promotionCoupon.getValidDay(),
						promotionCoupon.getMemo(),promotionCoupon.getStatus(),promotionCoupon.getDecimalBit(),promotionCoupon.getIssueType());
	
	}

	public void update(PromotionCoupon promotionCoupon) {
		if(promotionCoupon.getType().intValue()==0){
			promotionCoupon.setTotalAmount(null);
		}
		jdbcTemplate.update(
				"update t_promotion_coupon set name=?,type=?,minAmount=?,"
				+ "maxAmount=?,totalNum=?,totalAmount=?,startDate=?,endDate=?,storeId=?,"
				+ " `range`=?,shared=?,orderAmount=?,validDay=?,memo=?,status=?, decimalBit=? , issueType = ? where id=?",
				promotionCoupon.getName(), promotionCoupon.getType(), promotionCoupon.getMinAmount(),
				promotionCoupon.getMaxAmount(),promotionCoupon.getTotalNum(),
				promotionCoupon.getTotalAmount(),promotionCoupon.getStartDate(),
				promotionCoupon.getEndDate(),promotionCoupon.getStoreId(),
				promotionCoupon.getRange(),promotionCoupon.getShared(),
				promotionCoupon.getOrderAmount(),promotionCoupon.getValidDay(),
				promotionCoupon.getMemo(),promotionCoupon.getStatus(),promotionCoupon.getDecimalBit(),promotionCoupon.getIssueType(),promotionCoupon.getId());
	}

	/**
	 * 根据优惠活动ID查询优惠活动记录
	 * @param id 优惠活动ID
	 * @return 优惠活动对象或者null
	 */
	public PromotionCoupon findById(Long id) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList( "select t.* , (select name from t_store s where s.id=t.storeId) as storeName  from t_promotion_coupon t  where t.id = ?", id);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(PromotionCoupon.class, list.get(0));
		}
		return null;
	}

	public Page getCashGiftList(Long couponId,String name, String mobile, Long storeId,
			Integer status, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer and = new StringBuffer();
		
		if( null != couponId){
			and.append(" and t.couponId = ?  ");
			parameters.add(couponId);
		}
		if( null != storeId){
			and.append(" and t.storeId = ? ");
			parameters.add(storeId);
		}
		
		if (StringUtils.isNotBlank(name)) {
			and.append(" and pc.name like ?");
			parameters.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(mobile)) {
			and.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		
		if( null != status){
			if(status.intValue()==2){
				and.append(" and ?>t.validTime ");
				parameters.add(new Date());
			}else if(status.intValue()==0){
				and.append(" and t.status=? and ?<t.validTime ");
				parameters.add(status);
				parameters.add(new Date());
			}else{
				and.append(" and t.status = ?");
				parameters.add(status);
			}
		}
		
		Integer total = jdbcTemplate.queryForObject(
				"select count(1) from t_cash_gift t left join t_promotion_coupon pc on t.couponId=pc.id left join t_user u on t.userId=u.id where 1=1 " + and.toString(),
				Integer.class, parameters.toArray());
		page.setTotal(total);
		if (total == 0) {
			return page;
		}

		StringBuffer sql = new StringBuffer(
				"select t.id,t.userId,t.amount,t.status,t.createdTime, issueType,reportNum,decimalBit,"
				+ " t.memo,t.validTime,t.storeId,t.type,t.usedTime, "
				+ " t.couponId,t.shared,t.orderAmount,"
				+ "(select name from t_store s where s.id=t.storeId) as storeName ,"
				+ " pc.name as pcName,"
				+ " u.mobile "
				+ " from t_cash_gift t left join t_promotion_coupon pc on t.couponId=pc.id  left join t_user u on t.userId=u.id where 1=1 ");
		sql.append(and.toString() + " order by t.id desc limit ?,?");
		System.out.println("sql===="+sql.toString());
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(CashGift.class, list));
		
		return page;
	}

	public PromotionCoupon findStoreId(Long storeId) {
		List<Map<String, Object>> list=jdbcTemplate
				.queryForList(
						"select * from t_promotion_coupon t  where t.storeId = ? and date_add(t.endDate, interval t.validDay day) >?", storeId,new Date());
		return list.isEmpty()?null:BeanUtils.toBean(PromotionCoupon.class, list.get(0));
	}

	@Transactional
	public boolean saveCashGiftBatch(List<Object[]> batchArgs) {
		String sql = "insert into t_cash_gift(id,userId,amount,status,createdTime,memo,validTime,storeId,type,usedTime,couponId,shared,orderAmount) "
				+ "values (?,?,?,?,?,?,?,?,?,?  ,?,?,?)";
		int[] temp = jdbcTemplate.batchUpdate(sql, batchArgs);
		if (temp.length == batchArgs.size()) {
			return true;
		} else {
			new RuntimeException("batch insert t_cash_gift Failure");
			return false;
		}
	}
	
	/**
	 * 修改优惠活动的(领取/导出)数量
	 * @param promotionCouponId 优惠活动ID
	 * @param forNum 领取/导出数
	 * @return 是否修改成功
	 */
	public synchronized boolean  updateReportNumById(Long promotionCouponId, int forNum) {
		PromotionCoupon promotionCoupon = findById(promotionCouponId);
		if( null == promotionCoupon ){
			return false;
		}
		
		logger.debug("forNum = {},totalNum = {}",forNum,promotionCoupon.getTotalNum());
		// (导出/领取)量大于优惠活动的总数量,则返回失败信号
		if( promotionCoupon.getTotalNum().intValue() - promotionCoupon.getReportNum().intValue() < forNum ){
			return false;
		}
		forNum = forNum + promotionCoupon.getReportNum().intValue();
		return jdbcTemplate.update("update t_promotion_coupon set reportNum = ?  where id = ?",forNum,promotionCouponId) > 0 ? true : false ;
	}

	/**
	 * 根据优惠活动ID查询是否存在此活动且此活动为手动领取且未失效
	 * @param couponId 优惠活动ID
	 * @return 活动对象或者null
	 */
	public PromotionCoupon apiFindById(Long couponId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_promotion_coupon t  where t.id = ? and issueType = ? and t.endDate >= ? and t.status = ?  ", couponId,Constants.PromotionCoupon.ISSUETYPE_UP,new Date(),Constants.PromotionCoupon.STATUS_NORMAL);
		if ( null != list && list.size() > 0 ) {
			return BeanUtils.toBean(PromotionCoupon.class, list.get(0));
		}
		return null;
	}
}