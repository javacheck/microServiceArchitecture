package cn.lastmiles.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.service.Publish;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.constant.Constants.PublishImage;
import cn.lastmiles.dao.PublishDao;
import cn.lastmiles.dao.PublishImageDao;
import cn.lastmiles.dao.PublishTimeDao;
import cn.lastmiles.getui.PushService;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class PublishService {
	private final static Logger logger = LoggerFactory
			.getLogger(PublishService.class);

	@Autowired
	private PublishDao publishDao;
	@Autowired
	private PublishImageDao publishImageDao;
	@Autowired
	private PublishTimeDao publishTimeDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private PublishImageService publishImageService;
	@Autowired
	private PublishTimeService publishTimeService;
	@Autowired
	private IdService idService;
	@Autowired
	private PublishCategoryService publishCategoryService;

	/**
	 * 我的预约列表
	 * 
	 * @param keywords
	 * @param page
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page myBookingList(String keywords, Page page, Long userId) {
		page = publishDao.myBookingList(keywords, page, userId);
		List<Publish> data = (List<Publish>) page.getData();
		for (Publish pub : data) {
			String iconUrl = pub.getIconUrl();
			if (StringUtils.isNotBlank(iconUrl)) {
				pub.setIconUrl(FileServiceUtils.getFileUrl(iconUrl));
			} else {
				pub.setIconUrl(User.getDefaultIcon());
			}
		}
		return page;
	}

	@Transactional
	public Long save(Publish publish, String[] descriptionImages,
			String[] patentImages) {
		logger.debug(
				"publish is : {}  descriptionImages is : {}  patentImages is : {}",
				publish, descriptionImages, patentImages);
		publish.setId(idService.getId());// 设置ID
		publishTimeService.save(publish.getPublishTimes(), publish.getId());// 保持图片
		if (descriptionImages != null) {
			publishImageService.save(descriptionImages, publish.getId(),
					Constants.PublishImage.IMAGE_TYPE_DES);// 储存描述图片
		}
		if (patentImages != null) {
			publishImageService.save(patentImages, publish.getId(),
					PublishImage.IMAGE_TYPE_CER);// 储存证书图片
		}

		publish.setViewNumber(0);// 设置浏览数为0
		publish.setStatus(0);// 设置默认带审核状态
		publishDao.save(publish);// 存储发布
		PushService.pushToSingle(new Message("发布状态通知", Constants.MessageType.MESSAGE_PUBLISH_NOTICE_CONTENT, publish.getUserId(), Constants.MessageType.MESSAGE_PUBLISH_NOTICE, publish.getId()));
		return publish.getId();
	}

	public Page list(String categoryId, String keywords, Page page,
			Integer type, Long userId, Double longitude, Double latitude) {
		return publishDao.list(categoryId, keywords, page, type, userId,
				longitude, latitude);
	}

	/**
	 * 通过ID查找
	 * 
	 * @param id
	 * @return
	 */
	public Publish findById(Long id) {
		Publish publish = publishDao.findById(id);
		if (publish != null) {
			publish.setPublishTimes(publishTimeService.findByPublishId(id));
			publish.setPublishImages(publishImageService.findByPublishId(id));
			// publish.setPublishCategory(publishCategoryService.findById(publish.getCategoryId()));
		}
		return publish;
	}

	public Publish findById(Long id, Long userId) {
		return publishDao.findById(id, userId);
	}

	// 0 待审，1 已审 ，2 取消
	public Boolean updateStatus(Long id, Long userId) {
		return publishDao.updateStatus(id, 1, userId);
	}

	public Boolean updateStatus(Long id, Integer status, Long userId) {
		return publishDao.updateStatus(id, status, userId);
	}

	public Boolean updateStatus(Long id, Integer status) {
		return publishDao.updateStatus(id, status);
	}
	
	public Boolean updateStatus(Long id, Integer status,String reason) {
		if( null == reason || "".equals(reason) || status.intValue() != cn.lastmiles.constant.Constants.Publish.TYPE_CANCEL){
			return updateStatus(id,status);
		}
		return publishDao.updateStatus(id, status,reason);
	}

	public Page list(String keywords, Integer type, String startTime,
			String endTime, Integer status, Page page) {
		return publishDao
				.list(keywords, type, startTime, endTime, status, page);
	}

	public void addViewNumber(Long id) {
		publishDao.addViewNumber(id);
	}
	@Transactional
	public Long update(Publish publish, String[] descriptionImages, String[] patentImages) {
		logger.debug("publish is : {}  descriptionImages is : {}  patentImages is : {}",
				publish, descriptionImages, patentImages);
		publishTimeService.update(publish.getPublishTimes(), publish.getId());// 保存时间
		if (descriptionImages != null) {
			publishImageService.update(descriptionImages, publish.getId(),
					Constants.PublishImage.IMAGE_TYPE_DES);// 储存描述图片
		}
		if (patentImages != null) {
			publishImageService.update(patentImages, publish.getId(),
					PublishImage.IMAGE_TYPE_CER);// 储存证书图片
		}
		publish.setStatus(0);// 设置默认带审核状态
		publishDao.update(publish);// 发布修改
		return publish.getId();
	}
	@Transactional
	public void delete(Long id, Long userId) {
		if (publishDao.delete(id,userId).longValue()>0) {
			publishTimeService.delete(id);
			publishImageService.delete(id);
		}
	}
}
