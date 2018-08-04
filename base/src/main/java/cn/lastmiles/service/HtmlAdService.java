package cn.lastmiles.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.HtmlAd;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.HtmlAdDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class HtmlAdService {
	@Autowired
	private HtmlAdDao htmlAdDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private IdService idService;
	
	private static final Logger logger = LoggerFactory.getLogger(HtmlAdService.class);
	
	public List<HtmlAd> findAll(){
		return  htmlAdDao.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public Page findAll(Long storeId, Page page) {
		page = htmlAdDao.findAll(storeId, page);
		List<HtmlAd> HtmlAds = (List<HtmlAd>) page.getData();
		for (HtmlAd htmlAd : HtmlAds) {
			htmlAd.setPicUrl(FileServiceUtils.getFileUrl(htmlAd.getImageId()));
		}
		page.setData(HtmlAds);
		return page;
	}

	public void editHtmlAd(HtmlAd htmlAd, MultipartFile htmlImageId) {
		if(htmlAd.getId()==null){//新增
			htmlAd.setId(idService.getId());//广告ID
			InputStream in = null;
			if (StringUtils.isNotBlank(htmlImageId.getOriginalFilename())) {
				try {
					in = htmlImageId.getInputStream();
					String imageId = fileService.save(in);// LOGO ID
					htmlAd.setImageId(imageId);
					
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
			
			htmlAdDao.saveHtmlAd(htmlAd);
		}else{//修改
			if (StringUtils.isNotBlank(htmlImageId.getOriginalFilename())) {
				fileService.delete(htmlAd.getImageId());
				InputStream in = null;
				try {
					in = htmlImageId.getInputStream();
					String imageId = fileService.save(in);// LOGO ID
					htmlAd.setImageId(imageId);
					logger.debug("modify imageid = {}",imageId);
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
			htmlAdDao.updateHtmlAd(htmlAd);
		}
	}

	public HtmlAd findById(Long id) {
		HtmlAd htmlAd= htmlAdDao.findById(id);
		htmlAd.setPicUrl(FileServiceUtils.getFileUrl(htmlAd.getImageId()));
		return htmlAd;
	}

	public boolean delete(Long id) {
		return htmlAdDao.delete(id);
	}
}
