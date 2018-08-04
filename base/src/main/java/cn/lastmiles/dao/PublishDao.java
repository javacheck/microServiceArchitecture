package cn.lastmiles.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.service.Publish;
import cn.lastmiles.common.jdbc.*;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.JdbcUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class PublishDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 保持发布
	 * 
	 * @param publish
	 */
	public void save(Publish publish) {
		jdbcTemplate
				.update("insert into t_publish(ID,CREATEDTIME,KEYWORDS,TITLE,PRICE,TYPE,PAYTYPE,PAYSTATUS,VIEWNUMBER,STATUS,USERID,MOBILE,`RANGE`,CONTENT,DESCRIPTION,CATEGORYID,ADDRESS,bookingType,bookedNumber,userName,longitude,latitude) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						publish.getId(), new Date(), publish.getKeywords(),
						publish.getTitle(), publish.getPrice(),
						publish.getType(), publish.getPayType(),
						publish.getPayStatus(), publish.getViewNumber(),
						publish.getStatus(), publish.getUserId(),
						publish.getMobile(), publish.getRange(),
						publish.getContent(), publish.getDescription(),
						publish.getCategoryId(), publish.getAddress(),
						publish.getBookingType(), publish.getBookedNumber(),
						publish.getUserName(),publish.getLongitude(),publish.getLatitude());
	}
	/**
	 * 我的预约列表
	 * @param keywords
	 * @param page
	 * @param userId
	 * @return
	 */
	public Page myBookingList(String keywords, Page page, Long userId) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(
				" FROM t_publish p left join t_publish_category c on p.categoryId = c.id inner join t_user u on u.id = p.userId inner join t_booking b on b.publishId = p.id ");
		sql.append(" WHERE b.userId = ? ");
		parameters.add(userId);

		if (StringUtils.isNotBlank(keywords)) {
			sql.append("and ( ");
			sql.append(" p.keywords LIKE ?");// 关键字
			parameters.add("%" + keywords + "%");
			sql.append(" OR p.title LIKE ? ");// 标题
			parameters.add("%" + keywords + "%");
			//sql.append(" OR p.description LIKE ? ");// 描述
			//parameters.add("%" + keywords + "%");
			sql.append(" ) ");
		}

		return JdbcUtils.selectMysql(jdbcTemplate, page,
				"select p.*,c.name as categoryName,b.status as bookingStatus,b.id as bookingId,u.iconUrl as iconUrl " + sql.toString(),
				parameters, "CREATEDTIME desc", Publish.class);
	}

	/**
	 * 分页查找
	 * 
	 * @param categoryId
	 * @param keywords
	 * @param page
	 * @param type
	 * @return
	 */
	public Page list(String categoryId, String keywords, Page page,
			Integer type, Long userId,Double longitude,Double latitude) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(
				" FROM t_publish p left join t_publish_category c on p.categoryId = c.id inner join t_user u on u.id = p.userId left join t_user_publish_count upb on u.id = upb.userId ");
		sql.append(" WHERE 1 = 1");

		if (userId != null) {
			sql.append(" and p.userId = ?  ");
			parameters.add(userId);
		} else {
			sql.append(" and p.status = 1 ");
		}

		if (StringUtils.isNotBlank(categoryId)) {// 分类ID
			sql.append("and p.categoryId = ? ");
			parameters.add(categoryId);
		}
		if (StringUtils.isNotBlank(keywords)) {
			sql.append("and ( ");
			sql.append(" p.keywords LIKE ?");// 关键字
			parameters.add("%" + keywords + "%");
			sql.append(" OR p.title LIKE ? ");// 标题
			parameters.add("%" + keywords + "%");
			sql.append(" OR p.description LIKE ? ");// 描述
			parameters.add("%" + keywords + "%");
			sql.append(" ) ");
		}
		if (type != null) {
			sql.append(" AND p.type = ? ");
			parameters.add(type);
		}
		String selectDistance="",orderDistance="";
		if (null!=longitude&&null!=latitude) {
			selectDistance = ",theodolitic(p.longitude,p.latitude,"+longitude+","+latitude+") as distance ";
			orderDistance  = " distance is null,distance,";
		}
		
		String order = orderDistance+"createdtime desc";
		
		return JdbcUtils.selectMysql(jdbcTemplate, page,
				"select p.*,c.name as categoryName,u.iconUrl,upb.serviceTimes,u.idAudit "+selectDistance + sql.toString(),
				parameters, order, Publish.class);
	}

	public Publish findById(Long id) {
		String sql = "SELECT ID,CREATEDTIME,KEYWORDS,TITLE,PRICE,TYPE,PAYTYPE,PAYSTATUS,VIEWNUMBER,STATUS,USERID,MOBILE,`RANGE`,CONTENT,DESCRIPTION,CATEGORYID,ADDRESS,BOOKINGTYPE,BOOKEDNUMBER,USERNAME,userId FROM t_publish WHERE ID = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id);
		return list.isEmpty() ? null : BeanUtils.toBean(Publish.class,
				list.get(0));
	}

	public Publish findById(Long id, Long userId) {
		String sql = "SELECT ID,CREATEDTIME,KEYWORDS,TITLE,PRICE,TYPE,PAYTYPE,PAYSTATUS,VIEWNUMBER,STATUS,USERID,MOBILE,`RANGE`,CONTENT,DESCRIPTION,CATEGORYID,ADDRESS,BOOKINGTYPE,BOOKEDNUMBER FROM t_publish WHERE ID = ? AND USERID = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, id,
				userId);
		return list.isEmpty() ? null : BeanUtils.toBean(Publish.class,
				list.get(0));
	}

	public void updateBookedNumber(Long publishId, int bookingNumer) {
		String sql = "update t_publish set bookedNumber = ? WHERE ID = ? ";
		jdbcTemplate.update(sql, bookingNumer, publishId);
	}

	public Boolean updateStatus(Long id, Integer status, Long userId) {
		String sql = "update t_publish set status = ? WHERE ID = ? and userId = ? ";
		int flag = jdbcTemplate.update(sql, status, id, userId);
		if (flag > 0) {
			return true;
		}
		return false;
	}
	
	public Boolean updateStatus(Long id, Integer status) {
		String sql = "update t_publish set status = ? WHERE ID = ? ";
		int flag = jdbcTemplate.update(sql, status, id);
		if (flag > 0) {
			return true;
		}
		return false;
	}

	public Page list(String keywords, Integer type, String startTime,
			String endTime, Integer status, Page page) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer(" from t_publish r where 1=1 ");
		StringBuffer data = new StringBuffer(
				"r.id,r.createdTime,r.type,r.title,r.keywords,r.address,r.content,r.status,(select u.mobile from t_user u where u.id = r.userId) as userMobile");

		if (StringUtils.isNotBlank(keywords)) {
			querySQL.append("and ( ");
			querySQL.append(" keywords LIKE ?");// 关键字
			parameters.add("%" + keywords + "%");
			querySQL.append(" OR title LIKE ? ");// 标题
			parameters.add("%" + keywords + "%");
			querySQL.append(" OR description LIKE ? ");// 描述
			parameters.add("%" + keywords + "%");
			querySQL.append(" ) ");
		}

		if (type != null) {
			querySQL.append(" and r.type = ? ");
			parameters.add(type);
		}

		if (null != status) {
			querySQL.append(" and r.status = ? ");
			parameters.add(status);
		}

		if (StringUtils.isNotBlank(startTime)) {// 开始时间筛选
			querySQL.append(" and r.createdTime >= ?");
			parameters.add(startTime + " 00:00:00");
		}

		if (StringUtils.isNotBlank(endTime)) {// 结束时间筛选
			querySQL.append(" and r.createdTime <= ?");
			parameters.add(endTime + " 23:59:59");
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}
		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select "
				+ data + querySQL.toString() + " order by r.id desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(Publish.class, list));
		return page;
	}

	public void addViewNumber(Long id) {
		String sql = " UPDATE t_publish p set p.viewnumber = p.viewnumber + 1 where id = ? ";
		jdbcTemplate.update(sql, id);
	}
	public Integer delete(Long id, Long userId) {
		String sql = " delete from  t_publish   where id = ? and userId =? ";
		return jdbcTemplate.update(sql,id,userId);
	}
	public void update(Publish publish) {
		String sql = "UPDATE t_publish  set keywords= ?,title= ?,price= ?,type= ?,`status`= ?"
				+ ",userId= ?,mobile= ?,`range`= ?,content= ?,"
				+ "description= ?,categoryId= ?,address= ?"
				+ ",longitude= ?,latitude= ?,userName= ? , createdtime = ?"
				+ "where id = ? ";
		jdbcTemplate.update(sql,publish.getKeywords(),publish.getTitle(),publish.getPrice(),publish.getType(),publish.getStatus()
				,publish.getUserId(),publish.getMobile(),publish.getRange(),publish.getContent()
				,publish.getDescription(),publish.getCategoryId(),publish.getAddress()
				,publish.getLongitude(),publish.getLatitude(),publish.getUserName(),new Date()
				,publish.getId());
	}
	public Boolean updateStatus(Long id, Integer status, String reason) {
		String sql = "update t_publish set status = ? , reason = ? WHERE ID = ?";
		int flag = jdbcTemplate.update(sql, status, reason, id);
		if (flag > 0) {
			return true;
		}
		return false;
	}
}
