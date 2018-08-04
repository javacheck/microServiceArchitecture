package cn.lastmiles.service;

/**
 * createDate : 2015-06-29 AM 09:36
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Message;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.MyMessageDao;

@Service
public class MyMessageService {
	@Autowired
	private MyMessageDao myMessageDao;
	@Autowired
	private IdService idService;

	public void save(Message msg) {
		if (msg.getMessageId()==null) {
			msg.setMessageId(idService.getId());
		}
		myMessageDao.save(msg);
	}

	/**
	 * 修改信息的读取状态
	 * 
	 * @param userId
	 *            用户ID not null
	 * @param messageId
	 *            信息ID 可为空,(为空修改此用户的所有信息状态)
	 * @param readed
	 *            读取状态(1已读0未读) - 可为空，为空或超出状态范围(非0,1)则默认修改为已读
	 * @return true 修改成功
	 */
	public Boolean updateMessageStatus(Long userId, Long messageId,
			String readed) {
		if (!StringUtils.isBlank(readed) && readed.length() <= 1
				&& (readed.equals("0") || readed.equals("1"))) {
			return myMessageDao.updateMessageStatus(userId, messageId, readed);
		}

		return updateMessageStatus(userId, messageId);
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
		return myMessageDao.updateMessageStatus(userId, messageId);
	}

	/**
	 * 获取信息列表
	 * 
	 * @param page
	 *            分页对象 not null
	 * @param userId
	 *            用户ID not null
	 * @param messageId
	 *            信息ID 可为空,(为空查询此用户的所有信息)
	 * @param readed
	 *            读取状态(1已读0未读) 可为空，(为空查询所有状态)
	 * @return page(Message)对象
	 */
	public Page getMessageInformation(Page page, Long userId, Long messageId,
			String readed) {
		return myMessageDao.getMessageInformation(page, userId, messageId,
				readed);
	}

	public void delete(Long userId, Long id) {
		myMessageDao.delete(userId,id);
	}
}