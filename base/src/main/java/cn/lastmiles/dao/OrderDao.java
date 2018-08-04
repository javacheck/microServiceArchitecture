package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.PaymentModeInfo;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 分页查找
	 * pos端订单
	 * @param mobile
	 * @param page
	 * @return
	 */
	public Page list(String beginTime, String endTime, Integer paymentMode,String mobile,
			String orderId, String storeIdString, Integer source, Integer status,Integer haveReturnGoods,String memo,
			Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		sql.append(" LEFT JOIN t_account a on  a.id =o.accountId ");
		sql.append(" LEFT JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" LEFT JOIN t_user_card uc on uc.mobile = u.mobile and uc.storeId = o.storeId ");
		sql.append(" WHERE 1=1 ");
		if ( StringUtils.isNotBlank(storeIdString) ) {
			sql.append(" and o.storeId in( " +storeIdString+" )");
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ "00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + "59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
		if (status != null) {
			sql.append(" and o.status = ?");
			parameters.add(status);
		}else {
			sql.append("and (o.status = " + Constants.Order.TYPE_PAY + " or o.status = " + Constants.Order.TYPE_BANK_CARD_REVOKE + " or o.status = " + Constants.Order.TYPE_PAYING+" )");
		}
		
		if( null != haveReturnGoods ){
			sql.append(" and o.haveReturnGoods = ? ");
			parameters.add(haveReturnGoods);
		}

		if (source != null) {
			sql.append(" and o.source = ?");
			parameters.add(source);
		}
		if (paymentMode != null) {
			sql.append(" and o.paymentMode = ?");
			parameters.add(paymentMode);
		}
		if (StringUtils.isNotBlank(memo)) {
			sql.append(" and uc.memo like ?");
			parameters.add("%" + memo + "%");
		}
		sql.append(" and o.roomId is null ");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		if (total == null){
			total = 0;
		}
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		String sql1 = "SELECT o.*,uc.memo  " + sql 
				+ " order by o.createdTime desc limit ?,?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,
				parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));

		return page;
	}
	public Page appList(String beginTime, String endTime, Integer paymentMode,String mobile,
			String orderId, String storeIdString, Integer source, Integer status,Integer haveReturnGoods,String memo,
			Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		sql.append(" LEFT JOIN t_account a on  a.id =o.accountId ");
		sql.append(" LEFT JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" WHERE 1=1 ");
		if ( StringUtils.isNotBlank(storeIdString) ) {
			sql.append(" and o.storeId in( " +storeIdString+" )");
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ "00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + "59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
		if (status != null) {
			sql.append(" and o.status = ?");
			parameters.add(status);
		}
		
		if( null != haveReturnGoods ){
			sql.append(" and o.haveReturnGoods = ? ");
			parameters.add(haveReturnGoods);
		}

		if (source != null) {
			sql.append(" and o.source = ?");
			parameters.add(source);
		}
		if (paymentMode != null) {
			sql.append(" and o.paymentMode = ?");
			parameters.add(paymentMode);
		}
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		String sql1 = "SELECT o.*  " + sql
				+ " and o.roomId is null order by o.createdTime desc limit ?,?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,
				parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));

		return page;
	}
	public Order findById(Long id) {
		String sql = "SELECT * FROM t_order WHERE id = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Order.class, list.get(0));
	}
	
	public Order findByIdNoRoomId(Long id) {
		String sql = "SELECT * FROM t_order WHERE id = ? and roomId is null ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Order.class, list.get(0));
	}
	
	public Order findByIdAndUserId(Long id,Long userId) {
		String sql = "SELECT * FROM t_order WHERE id = ? and userId = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id,userId);
		if (list.isEmpty()) {
			return null;
		}
		return BeanUtils.toBean(Order.class, list.get(0));
	}

	public Double calculatSales(Long accountId, Long storeId, Date beginDate,
			Date endDate) {
		String sql = "SELECT SUM(price) FROM t_order t WHERE createdTime BETWEEN ? AND ? AND accountId = ? AND storeId = ?";
		Object[] list = { beginDate, endDate, accountId, storeId };
		Double sales = jdbcTemplate.queryForObject(sql, list, Double.class);
		return sales != null ? sales : 0D;
	}

	public boolean save(Order order) {
		String sql = "INSERT INTO t_order(id,duration,refundPrice,roomId,accountId,storeId,price,createdTime,userId,status,paymentMode,addressId,message,actualPrice,source,shipType,shipTime,shipAmount,balance,cashGiftId,cashGiftDesc,shipTypeId,cashPrice,freeShipAmount) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int temp = jdbcTemplate.update(sql,order.getId(),order.getDuration(),order.getRefundPrice(),order.getRoomId(),
				order.getAccountId(), order.getStoreId(), order.getPrice(),
				order.getCreatedTime(), order.getUserId(), order.getStatus(),
				order.getPaymentMode(), order.getAddressId(),
				order.getMessage(), order.getActualPrice(), order.getSource(),
				order.getShipType(), order.getShipTime(),
				order.getShipAmount(), order.getBalance(),
				order.getCashGiftId(), order.getCashGiftDesc(),
				order.getShipTypeId(),order.getCashPrice(),order.getFreeShipAmount());
		return temp <= 0 ? false : true;
	}

	public boolean update(Long orderId, Integer status, Integer paymentMode,Double price, Long userId, Double discount, Double cashPrice,Double point,Double change) {
//		Order order= this.findById(orderId);
		String sql = "UPDATE t_order set status = ? ,paymentMode =? ,actualPrice = ? ,userId = ? , discount=? ,cashPrice = ?,point = ?,`change` = ? WHERE id = ? ";
		int temp = jdbcTemplate.update(sql, status, paymentMode,price , userId,
				discount,cashPrice,point,change, orderId);
		return temp <= 0 ? false : true;
	}
	
	public boolean update(Long orderId, Integer status, Integer paymentMode,
			Double price, Long userId, Double discount, Double cashPrice,
			Double point,Double change,Double fullSubstractAmount ,
			Double promotionAmount,
			Double promotionDiscount,
			Double reductionAmount,
			Double reductionDiscount,String fullSubstractCondition){
		String sql = "UPDATE t_order set status = ? ,paymentMode =? ,actualPrice = ? ,userId = ? , discount=? ,cashPrice = ?,point = ?,`change` = ?,fullSubstractAmount=?"
				+ ",promotionAmount=?,promotionDiscount=?,reductionAmount=?,reductionDiscount=?,fullSubstractCondition=? WHERE id = ? ";
		int temp = jdbcTemplate.update(sql, status, paymentMode,price , userId,
				discount,cashPrice,point,change,fullSubstractAmount,promotionAmount,promotionDiscount,reductionAmount,reductionDiscount,fullSubstractCondition, orderId);
		return temp <= 0 ? false : true;
	}

	/**
	 * 根据订单状态和ID查找
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public Order findByIdAndStatus(Long id, Integer status) {
		String sql = "SELECT * FROM t_order WHERE id = ? and status = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id,
				status);
		return list.isEmpty() ? null : BeanUtils.toBean(Order.class,
				list.get(0));
	}

	public List<PaymentModeInfo> StatisticsShiftWorkLog(Long accountId,
			Long storeId, Date startDate, Date endTime) {
		String sql = " SELECT	IF(paymentMode is not NULL,paymentMode,"
				+ Constants.Order.PAYMENT_TOTAL_PAY
				+ ") as 'paymentMode',	COUNT(*) AS 'amount',	SUM(actualPrice) AS 'price'  FROM t_order WHERE paymentMode is NOT NULL AND createdTime BETWEEN ? AND ? AND accountId = ? AND storeId = ? AND status = '1'  GROUP BY paymentMode WITH ROLLUP ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
				startDate, endTime, accountId, storeId);
		return BeanUtils.toList(PaymentModeInfo.class, list);
	}

	public Order findByStoreId(Long storeId) {
		String sql = "SELECT * FROM t_order WHERE storeId = ? ";
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql, storeId);
		return list.isEmpty() ? null : BeanUtils.toBean(Order.class,
				list.get(0));
	}

	public Page AppList(String beginTime, String endTime, String orderId,
			Long userId, Page page, Integer status, Integer source) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from t_order o");
		// sql.append(" INNER JOIN t_account a on  a.id =o.accountId ");
		sql.append(" INNER JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" WHERE 1=1 ");

		if (source != null) {
			sql.append(" and o.source = ? ");
			parameters.add(source);
		}

		if (userId != null) {
			sql.append(" and o.userId = ? ");
			parameters.add(userId);
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ "00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + "59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
		if (status != null&&status!=Constants.SELECTALL) {
			sql.append(" and o.status = ? ");
			parameters.add(status);
		}
		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT o.*,s.name as storeName  " + sql
						+ " order by o.createdTime desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));

		return page;
	}

	public boolean appUpdate(Long orderId, Integer status, Integer paymentMode,
			Long userId) {
		String sql = "UPDATE t_order set status = ? ,paymentMode =?  WHERE id = ? and userId = ? ";
		int temp = jdbcTemplate.update(sql, status, paymentMode, orderId,
				userId);
		return temp <= 0 ? false : true;
	}

	public void updateStatus(Long userId, Long orderId, Integer status) {
		jdbcTemplate.update(
				"update t_order set status = ? where id = ? and userId = ?",
				status, orderId, userId);
	}
	
	public int updateStatus(Long orderId, Integer status) {
		return jdbcTemplate.update(
				"update t_order set status = ? where id = ? ",
				status, orderId);
	}
	
	public int updateStatus(Long orderId, Integer status,Integer oldStatus) {
		return jdbcTemplate.update(
				"update t_order set status = ? where id = ? and status = ?",
				status, orderId,oldStatus);
	}
	
	public void close(Long id) {
		jdbcTemplate.update(
				"update t_order set status = ? where id = ? ",Constants.Order.TYPE_CLOSED, id);
	}

	public void typeChangeByorderId(Integer typeClose, Long orderId) {
		jdbcTemplate.update("UPDATE t_order set status = ?  where id = ?",
				typeClose, orderId);

	}

	public void paySuccess(Long orderId) {
		jdbcTemplate.update(
				"update t_order set status = ? where id = ? and status = ?",
				Constants.Order.TYPE_PAY, orderId, Constants.Order.TYPE_NO_PAY);
	}

	public void offlinePay(Long orderId, Integer paymentMode) {
		jdbcTemplate.update(
				"update t_order set paymentMode = ?,status = ? where id = ?",
				paymentMode, Constants.Order.TYPE_PAY, orderId);
	}

	public void evaluate(Long userId, Long orderId, String evaluate) {
		jdbcTemplate.update(
				"update t_order set evaluate = ? where id= ? and userId = ?",
				evaluate, orderId, userId);
	}

	public void orderHandel(Integer status, Long orderId) {
		String sql = "INSERT INTO t_order_handle(orderId,status,handleTime,memo) VALUES(?,?,?,?)";
		jdbcTemplate.update(sql, orderId, status, new Date(), null);
	}

	public void udpatePaymentMode(Long orderId, Integer paymentMode) {
		jdbcTemplate.update("update t_order set paymentMode = ? where id = ?",
				paymentMode, orderId);
	}

	public Page posList(String beginTime, String endTime, String mobile,
			String orderId, Long storeId, Integer status, Page page) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		// sql.append(" LEFT JOIN t_account a on  a.id =o.accountId ");
		sql.append(" LEFT JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" WHERE 1=1 and roomId is null and (o.status = 1 or o.status = 15 or o.status = 100) ");
		if (storeId != null) {
			sql.append(" and o.storeId = ?");
			parameters.add(storeId);
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ " 00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + " 59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
//		if (status != null) {
//			sql.append(" and o.status = ?");
//			parameters.add(status);
//		}

		sql.append(" and o.source = ?");
		parameters.add(Constants.Order.ORDER_SOURCE_DEVICES);

		sql.append("");
		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		String sql1 = "SELECT o.*  " + sql
				+ " order by o.createdTime desc limit ?,?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,
				parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));

		return page;
	}

	public List<Order> posGetNotPrintedOrder(Long storeId) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"select o.*,(select address from t_user_address ua where ua.id = o.addressId) as printAddress,(select phone from t_user_address ua where ua.id = o.addressId) as userMobile,(select name from t_user_address ua where ua.id = o.addressId) as userName,s.name as storeName,s.mobile as storeMobile,s.phone as storePhone from t_order o left join t_store s on o.storeId = s.id ");
		sql.append(" where o.storeId = ?");
		parameters.add(storeId);

		sql.append(" and o.printed = 0"); // 未打印
		sql.append(" and o.status <> 0"); // 除了未支付的
		sql.append(" and o.source = 0"); // APP端
		sql.append(" order by id asc"); // 先接受的单先打印
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		return BeanUtils.toList(Order.class, list);
	}

	public void updatePrinted(Long userId, Long orderId, Integer printed) {
		jdbcTemplate.update(
				"update t_order set printed = ? where id = ? and userId = ?",
				printed, orderId, userId);
	}

	public void changePrinted(Long orderId, int printed) {
		jdbcTemplate.update("update t_order set printed = ? where id = ? ",
				printed, orderId);

	}
	public void changeWifiPrinted(Long orderId, int printed) {
		jdbcTemplate.update("update t_order set wifiPrinted = ? where id = ? ",
				printed, orderId);

	}
	public Page posGetAppOrder(Long storeId, String orderIdOrMobile,Integer status,Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				" from t_order o left join t_user_address ua on o.addressId = ua.id ");
		
		sql.append(" where o.storeId = ?");
		parameters.add(storeId);

		String data = " o.*,ua.address as printAddress,ua.name as userName,ua.phone as userMobile , "
				+ "(select s.name from t_store s where s.id = o.storeId) as storeName , "
				+ "(select s.mobile from t_store s where s.id = o.storeId) as storeMobile , "
				+ "(select s.phone from t_store s where s.id = o.storeId) as storePhone";
		if (!ObjectUtils.equals(status, -1) && !ObjectUtils.equals(status, -2)) { // POS传过来的状态是非全部且非其他的时候就查询相对应的状态
			sql.append(" and o.status = ?");
			parameters.add(status);
		}

		if (ObjectUtils.equals(status, -1)) { // POS传过来的状态是其他,(除待发货5,待确认1,已完成3之外的其他状态)
			sql.append(" and o.status not in (5,1,3)");
		}

		sql.append(" and o.source = 0"); // APP端

		// 最后-----------计算并排序
		if (StringUtils.isNotBlank(orderIdOrMobile)) { 
			sql.append(" and (o.id like ? or ua.phone like ?)");
			parameters.add("%" + orderIdOrMobile + "%");
			parameters.add("%" + orderIdOrMobile + "%");

			sql.append(" order by o.id desc"); // 使用订单ID或者手机号码时查询(最近的订单最先显示)
		} else {
			if( ObjectUtils.equals(status, Constants.Order.TYPE_PAY) || ObjectUtils.equals(status, Constants.Order.TYPE_WAITING_DELIVER)){
				sql.append(" order by o.id asc"); // 待发货5-待确认1(先下的订单先显示)
			} else {
				sql.append(" order by o.id desc"); // 最近的订单最先显示				
			}
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) " + sql,
				Integer.class, parameters.toArray());
		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList( "select " + data + 
				sql.toString() + " limit ?,?", parameters.toArray());
		
		page.setData(BeanUtils.toList(Order.class, list));
		
		return page;
	}

	public List<Order> findNoPrint() {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"select o.id from t_order o where 1=1 ");

		sql.append(" and o.printed = 0"); // 未打印
		sql.append(" and o.status = 1"); // 已支付
		sql.append(" and o.source = 0"); // app端
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		return BeanUtils.toList(Order.class, list);
	}
	
	public List<Order> findWifiNoPrint() {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(
				"select o.id from t_order o where 1=1 ");

		sql.append(" and o.wifiPrinted = 0"); // 未打印
		sql.append(" and o.status = 1"); // 已支付
		sql.append(" and o.source = 0"); // app端
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				sql.toString(), parameters.toArray());
		return BeanUtils.toList(Order.class, list);
	}

	public void updateSettlement(Long id, Integer settlement) {
		String sql ="update t_order set settlement = ? where id = ?";
		jdbcTemplate.update(sql,settlement,id);
	}
	
	public List<Order> findAppBySearch(String beginTime, String endTime,
			Integer paymentMode, String mobile, String orderId,
			String storeIdString, Integer source, Integer status, String memo) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		sql.append(" LEFT JOIN t_account a on  a.id =o.accountId ");
		sql.append(" LEFT JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" WHERE 1=1 ");
		if ( StringUtils.isNotBlank(storeIdString) ) {
			sql.append(" and o.storeId in( " +storeIdString+" )");
		}
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ "00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + "59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
		if (status != null) {
			sql.append(" and o.status = ?");
			parameters.add(status);
		}

		if (source != null) {
			sql.append(" and o.source = ?");
			parameters.add(source);
		}
		if (paymentMode != null) {
			sql.append(" and o.paymentMode = ?");
			parameters.add(paymentMode);
		}
		sql.append("");
		
		String sql1 = "SELECT o.*  " + sql
				+ " and o.roomId is null order by o.createdTime desc limit ?,?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,
				parameters.toArray());
		

		return BeanUtils.toList(Order.class, list);
	}
	/**
	 * @param beginTime
	 * @param endTime
	 * @param paymentMode
	 * @param mobile
	 * @param orderId
	 * @param storeIdString
	 * @param source
	 * @param status
	 * @return
	 */
	public List<Order> findPOSBySearch(String beginTime, String endTime,
			Integer paymentMode, String mobile, String orderId, String storeIdString,
			Integer source, Integer status,String memo,Integer haveReturnGoods) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" from t_order o");
		sql.append(" LEFT JOIN t_account a on  a.id =o.accountId ");
		sql.append(" LEFT JOIN t_store s ON o.storeId=s.id ");
		sql.append(" LEFT JOIN t_user u ON o.userId = u.id");
		sql.append(" LEFT JOIN t_user_card uc on uc.mobile = u.mobile and uc.storeId = o.storeId ");
		sql.append(" WHERE 1=1 ");
		
		if ( StringUtils.isNotBlank(storeIdString) ) {
			sql.append(" and o.storeId in( " +storeIdString+" )");
		}
		
		if (StringUtils.isNotBlank(mobile)) {
			sql.append(" and u.mobile like ?");
			parameters.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(beginTime)) {
			sql.append(" and o.createdTime >= ?");
			parameters.add(beginTime+ "00:00");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and o.createdTime <= ?");
			parameters.add(endTime + "59:59");
		}
		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and o.id like ?");
			parameters.add("%" + orderId + "%");
		}
		if (status != null) {
			sql.append(" and o.status = ?");
			parameters.add(status);
		}else {
			sql.append("and (o.status = " + Constants.Order.TYPE_PAY + " or o.status = " + Constants.Order.TYPE_BANK_CARD_REVOKE + " or o.status = " + Constants.Order.TYPE_PAYING+" )");
		}
		if( null != haveReturnGoods ){
			sql.append(" and o.haveReturnGoods = ? ");
			parameters.add(haveReturnGoods);
		}
		if (source != null) {
			sql.append(" and o.source = ?");
			parameters.add(source);
		}
		if (paymentMode != null) {
			sql.append(" and o.paymentMode = ?");
			parameters.add(paymentMode);
		}
		if (StringUtils.isNotBlank(memo)) {
			sql.append(" and uc.memo like ?");
			parameters.add("%" + memo + "%");
		}
		sql.append(" and o.roomId is null ");
		String sql1 = "SELECT o.*,uc.memo " + sql
				+ " order by o.createdTime desc ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,
				parameters.toArray());
		return BeanUtils.toList(Order.class, list);
		
	}

	public boolean isFirstOrder(Long userId) {
		String sql = "SELECT COUNT(1) FROM t_order o WHERE o.userId = ? and  o.`status` != ? and  o.`status` != ? and  o.`status` != ?";
		int count =jdbcTemplate.queryForObject(sql, Integer.class,userId,Constants.Order.TYPE_CANCEL,Constants.Order.TYPE_USER_CANCEL,Constants.Order.TYPE_SYS_CANCEL);
		return count>=1?false:true;
	}

	public boolean updateStoreDerate(Long orderId,Double price) {
		return jdbcTemplate.update("update t_order set storeDerate = ? where id = ?",price,orderId)>0;
	}

	public synchronized boolean updateCashGiftID(Long orderId, Long cashGiftId) {
		return jdbcTemplate.update("update t_order set cashGiftId = ? where id = ?",cashGiftId,orderId)>0;
	}

	public Order findByRoomIdAndStatus(Long roomId,Integer status) {
		String sql = "SELECT * FROM t_order o WHERE  o.`status` = ? and o.roomId = ?";
		List<Map<String, Object>> list =jdbcTemplate.queryForList(sql,status,roomId);
		return list.isEmpty()?null:BeanUtils.toBean(Order.class, list.get(0));
	}

	public void updatePrice(Long id, Double price) {
		jdbcTemplate.update("update t_order set price = ? where id = ?",price,id);
	}

	public void updatePrice(Long id, Integer status) {
		jdbcTemplate.update("update t_order set status = ? where id = ?",status,id);
	}

	public void updateRefundPrice(Long id, Double refundPrice) {
		jdbcTemplate.update("update t_order set refundPrice = ? where id = ?",refundPrice,id);
	}

	public void updateDuration(Long id, Integer duration) {
		jdbcTemplate.update("update t_order set duration = ? where id = ?",duration,id);
	}

	public void updateHaveReturnGoods(Long orderId, int i) {
		jdbcTemplate.update("update t_order set haveReturnGoods = ? where id = ?",i,orderId);
	}

	public void updatePoint(Double point,Double getPoint, Long orderId) {
		jdbcTemplate.update("update t_order set point = ?,getPoint = ? where id = ?",point,getPoint,orderId);
	}

	public List<Order> findCurrentOrderRecord(Long accountId, Date startDate) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from t_order where accountId = ? and createdTime >= ? and source = 1 and status = ? "
				, accountId,startDate,Constants.Order.TYPE_PAY);
		return BeanUtils.toList(Order.class, list);
	}
	
	public int deletePayingOrder(Long orderId){
		return jdbcTemplate.update("delete from t_order where id = ? and status = 100 and source = 1 and roomId is null ", orderId);
	}
	
	public int deleteRestoreOrder(Long orderId){
		return jdbcTemplate.update("delete from t_order where id = ? and status = 0 and source = 1 and roomId is null ", orderId);
	}
	public Integer getStatus(Long orderId) {
		List<Integer> list = jdbcTemplate.queryForList("select status from t_order where id = ?",Integer.class,orderId);
		if (list.isEmpty()){
			return null;
		}else {
			return list.get(0);
		}
	}
	public void updateStatus(Long orderId, int status, int oldStatus,
			Long storeId) {
		jdbcTemplate.update(
				"update t_order set status = ? where id = ? and status = ? and storeId = ?",
				status, orderId,oldStatus,storeId);
	}
	
	public Page getOrderListByStoreId(String storeId, String orderId, String beginTime, String endTime,Page page) {

		StringBuffer querySQL = new StringBuffer(" FROM t_order o WHERE 1 = 1 ");
		List<Object> parameters = new ArrayList<Object>();

		querySQL.append(" AND o.storeId = ?");
		parameters.add(storeId);

		if (StringUtils.isNotBlank(orderId)) {
			querySQL.append(" AND o.id = ?");
			parameters.add(orderId);
		}

		if (StringUtils.isNotBlank(beginTime)) {
			querySQL.append(" AND o.createdTime >= ?");
			parameters.add(beginTime);
		}

		if (StringUtils.isNotBlank(endTime)) {
			querySQL.append(" AND o.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		Integer total = jdbcTemplate.queryForObject("SELECT COUNT(1) " + querySQL.toString() ,Integer.class, parameters.toArray());
		page.setTotal(total);
		
		if (total == 0) {
			return page;
		}
		
		querySQL.insert(0, "SELECT * ");
		querySQL.append(" ORDER BY o.createdTime DESC LIMIT ?,?");
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString(), parameters.toArray());
		page.setData(BeanUtils.toList(Order.class, list));
		return page;
	}
}
