package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.service.PublishImage;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.dao.PublishImageDao;
import cn.lastmiles.utils.FileServiceUtils;
@Service
public class PublishImageService {
	private final static Logger logger = LoggerFactory
			.getLogger(PublishImageService.class);
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PublishImageDao publishImageDao;
	
	public void save(String[] images,Long publishId,Integer type){
		List<PublishImage> imageList = new ArrayList<PublishImage>();
		if (images != null&&images.length>0) {
			int index=1;
			for (String imageId : images) {
				PublishImage publishImage = new PublishImage();
				publishImage.setPublishId(publishId);
				publishImage.setImageId(imageId);
				publishImage.setType(type);
				publishImage.setOrderSort(index++);
				imageList.add(publishImage);
			}//FOR 循环结束
		}//IF 结束
		if (!imageList.isEmpty()) {
			publishImageDao.save(imageList);//存储
		}
	}

	public List<PublishImage> findByPublishId(Long publishId) {
		List<PublishImage> publishImages= publishImageDao.findByPublishId(publishId);
		if (publishImages!=null) {
			for (PublishImage publishImage : publishImages) {
				publishImage.setPicUrl(FileServiceUtils.getFileUrl(publishImage.getImageId()));
			}
		}
		return publishImages;
	}
	
	@Transactional
	public void update(String[] images, Long publishId, int type) {
		this.delete(publishId);
		List<PublishImage> imageList = new ArrayList<PublishImage>();
		if (images != null&&images.length>0) {
			int index=1;
			for (String imageId : images) {
				PublishImage publishImage = new PublishImage();
				publishImage.setPublishId(publishId);
				publishImage.setImageId(imageId);
				publishImage.setType(type);
				publishImage.setOrderSort(index++);
				imageList.add(publishImage);
			}//FOR 循环结束
		}//IF 结束
		if (!imageList.isEmpty()) {
			publishImageDao.save(imageList);//存储
		}
	}
	/**
	 * 根据发布ID删除
	 * @param publishId
	 */
	@Transactional
	public void delete(Long publishId) {
		logger.debug("delete publishId is {}",publishId);
		List<PublishImage>  publishImages =publishImageDao.findByPublishId(publishId);
		if (!publishImages.isEmpty()) {
			for (PublishImage publishImage : publishImages) {
				fileService.delete(publishImage.getImageId());
			}
			publishImageDao.delete(publishId);
		}
	}
}
