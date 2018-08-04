package cn.lastmiles.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.PurchaseAcceptance;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.PurchaseAcceptanceDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class PurchaseAcceptanceService {
	private final static Logger logger = LoggerFactory.getLogger(PurchaseAcceptanceService.class);
	@Autowired
	private PurchaseAcceptanceDao purchaseAcceptanceDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private IdService idService;
	
	@SuppressWarnings("unchecked")
	public Page findAllPage(Long storeId, String purchaseNumber, String mobile,
			String beginTime, String endTime, Integer status, Page page) {
		page=purchaseAcceptanceDao.findAllPage(storeId,purchaseNumber,mobile,beginTime,endTime,status,page);
		List<PurchaseAcceptance> pas = (List<PurchaseAcceptance>) page.getData();
		for (PurchaseAcceptance pa : pas) {
			pa.setPicUrl(FileServiceUtils.getFileUrl(pa.getImageId()));
		}
		page.setData(pas);
		return page;
	}
	public PurchaseAcceptance findById(Long id) {
		PurchaseAcceptance pa= purchaseAcceptanceDao.findById(id);
		pa.setPicUrl(FileServiceUtils.getFileUrl(pa.getImageId()));
		return pa;
	}
	public void editPurchaseAcceptance(PurchaseAcceptance purchaseAcceptance,
			MultipartFile paImageId) {
		if(purchaseAcceptance.getId()==null){//新增
			purchaseAcceptance.setId(idService.getId());//广告ID
			InputStream in = null;
			if (StringUtils.isNotBlank(paImageId.getOriginalFilename())) {
				try {
					in = paImageId.getInputStream();
					String imageId = fileService.save(in);// LOGO ID
					purchaseAcceptance.setImageId(imageId);
					
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
			
			purchaseAcceptanceDao.savePurchaseAcceptance(purchaseAcceptance);
		}else{//修改
			if (StringUtils.isNotBlank(paImageId.getOriginalFilename())) {
				fileService.delete(purchaseAcceptance.getImageId());
				InputStream in = null;
				try {
					in = paImageId.getInputStream();
					String imageId = fileService.save(in);// LOGO ID
					purchaseAcceptance.setImageId(imageId);
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
			purchaseAcceptanceDao.updatePurchaseAcceptance(purchaseAcceptance);
		}
		
	}
	public PurchaseAcceptance findPaExist(Long id, String purchaseNumber,Long storeId) {
		return purchaseAcceptanceDao.findPaExist(id,purchaseNumber,storeId);
	}

}
