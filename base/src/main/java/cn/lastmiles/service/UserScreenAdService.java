package cn.lastmiles.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserScreenAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserScreenAdDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class UserScreenAdService {
	@Autowired
	private UserScreenAdDao userScreenAdDao;
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private FileService fileService;

	private static final Logger logger = LoggerFactory
			.getLogger(UserScreenAdService.class);
	
	public List<String> findImageByStoreId(Long storeId){
		List<String> ret = new ArrayList<String>();
		List<String> list = userScreenAdDao.findImageByStoreId(storeId);
		if (list.isEmpty()){
			Store store = storeDao.findTopStore(storeId);
			list = userScreenAdDao.findImageByStoreId(store.getId());
		}
		for (String img : list){
			img = FileServiceUtils.getFileUrl(img);
			ret.add(img);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Page findAll(Long storeId, Page page) {
		page = userScreenAdDao.findAll(storeId, page);
		List<UserScreenAd> userScreenAds = (List<UserScreenAd>) page.getData();
		for (UserScreenAd userScreenAd : userScreenAds) {
			userScreenAd.setPicUrl(FileServiceUtils.getFileUrl(userScreenAd
					.getImageId()));
		}
		page.setData(userScreenAds);
		return page;
	}

	public void edituserScreenAd(Long storeId, MultipartFile userScreenImageId) {
		UserScreenAd userScreenAd = new UserScreenAd();
		userScreenAd.setStoreId(storeId);
		InputStream in = null;
		if (StringUtils.isNotBlank(userScreenImageId.getOriginalFilename())) {
			try {
				in = userScreenImageId.getInputStream();
				String imageId = fileService.save(in);// LOGO ID
				userScreenAd.setImageId(imageId);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		userScreenAdDao.saveUserScreenAd(userScreenAd);
	}

	public void deleteByImageId(String imageId) {
		int flag = userScreenAdDao.deleteByImageId(imageId);
		logger.debug("flag======{}", flag);
		if (flag == 1) {
			fileService.delete(imageId);
		}
	}

}
