package cn.lastmiles.dao;

/**
 * createDate : 2015-06-29 AM 09:32
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.Message;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.StringUtils;

@Repository
public class MyMessageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Message msg) {
		jdbcTemplate
				.update("insert into t_message(messageId,message,title,userId,readed,memo,createTime,ownerId) values(?,?,?,?,?,?,?,?)",
						msg.getMessageId(), msg.getMessage(), msg.getTitle(),
						msg.getUserId(), msg.getReaded(), msg.getMemo(),
						new Date(),msg.getOwnerId());
	}

	/**
	 * 修改信息的读取状态
	 * 
	 * @param userId
	 *            用户ID not null
	 * @param messageId
	 *            信息ID 可为空,(为空修改此用户的所有信息状态)
	 * @param readed
	 *            读取状态(1已读0未读)
	 * @return true 修改成功
	 */
	public Boolean updateMessageStatus(Long userId, Long messageId,
			String readed) {
		List<Object> parameters = new ArrayList<Object>();

		StringBuffer updateSQL = new StringBuffer(
				"update t_message set readed = ?  where userId = ?");

		parameters.add(readed);
		parameters.add(userId);

		if (null != messageId) {
			updateSQL.append(" and messageId = ?");
			parameters.add(messageId);
		}

		int updateRow = jdbcTemplate.update(updateSQL.toString(),
				parameters.toArray());

		if (updateRow > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 修改信息的读取状态(修改为已读)
	 * 
	 * @param userId
	 *            用户ID not null
	 * @param messageId
	 *            信息ID 可为空,(为空修改此用户的所有信息状态)
	 * @return true 修改成功
	 */
	public Boolean updateMessageStatus(Long userId, Long messageId) {
		return updateMessageStatus(userId, messageId, "1");
	}

	/**
	 * 获取信息列表
	 * 
	 * @param page
	 *            分页对象 not null
	 * @param userId
	 *            用户ID not null
	 * @param messageId
	 *            信息ID 可为空，(为空查询此用户的所有信息)
	 * @param readed
	 *            读取状态(1已读0未读) 可为空，(为空查询所有状态)
	 * @return page(Message)对象
	 */
	public Page getMessageInformation(Page page, Long userId, Long messageId,
			String readed) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer querySQL = new StringBuffer();

		querySQL.append(" from t_message where userId = ? ");
		parameters.add(userId);

		if (null != messageId) {
			querySQL.append(" and messageId = ?");
			parameters.add(messageId);
		}

		if (!StringUtils.isBlank(readed) && readed.length() <= 1) {
			querySQL.append(" and readed = ?");
			parameters.add(readed);
		}

		Integer total = jdbcTemplate.queryForObject("select count(1) "
				+ querySQL.toString(), Integer.class, parameters.toArray());

		page.setTotal(total);

		if (total == 0) {
			return page;
		}

		parameters.add(page.getStart());
		parameters.add(page.getPageSize());

		List<Map<String, Object>> list = jdbcTemplate.queryForList("select  * "
				+ querySQL.toString()
				+ " order by userId,messageId desc limit ?,?",
				parameters.toArray());
		page.setData(BeanUtils.toList(Message.class, list));
		return page;
	}

	public void delete(Long userId, Long id) {
		jdbcTemplate.update("delete from t_message where messageId=? and userId = ?",
				id, userId);
	}
}