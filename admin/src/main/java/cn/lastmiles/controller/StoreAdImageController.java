package cn.lastmiles.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.StoreAdImage;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreAdImageService;
import cn.lastmiles.utils.FileServiceUtils;

/**
 * createDate : 2015年8月25日 下午3:56:12 
 */

@Controller
@RequestMapping("storeAdImage")
public class StoreAdImageController {

	private static final Logger logger = LoggerFactory.getLogger(StoreAdImageController.class);
	@Autowired
	private StoreAdImageService storeAdImageService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping("list")
	public String list(Model model) {
		return "storeAdImage/list";
	}
	
	@RequestMapping("list-data")
	public String listData(Long storeId,Integer type,Page page, Model model) {
		logger.debug("storeId = {} ",storeId);
	
		page = storeAdImageService.list(storeId,type, page);
		model.addAttribute("data", page);
		return "storeAdImage/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "storeAdImage/add";
	}
	
	@RequestMapping(value = "update/{storeId}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long storeId,Model model ){
		logger.debug("toUpdate  id -->"+storeId);
		StoreAdImage storeAdImage = storeAdImageService.findByStoreId(storeId);
		storeAdImage.setPicURL(FileServiceUtils.getFileUrl(storeAdImage.getImageId()));
		
		model.addAttribute("storeAdImage",storeAdImage);
		return "/storeAdImage/add";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public int deleteById(Integer type,Long storeId){
		return storeAdImageService.delete(type,storeId) ? 1:0;
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(StoreAdImage storeAdImage,@RequestParam("storeAdImageImageId") MultipartFile storeAdImageImageId, Model model) {
		
		if (null != storeAdImage.getCacheStoreId()) {
			if (null != storeAdImageImageId && storeAdImageImageId.getSize() > 0) {
				fileService.delete(storeAdImage.getImageId()); // 先删除之前的
				String imageId=null;
				try {
					imageId = fileService.save(storeAdImageImageId.getInputStream());
					storeAdImage.setImageId(imageId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String info = storeAdImage.getStoreId() + (storeAdImageService.update(storeAdImage) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			try {
				String imageId = fileService.save(storeAdImageImageId.getInputStream());
				storeAdImage.setImageId(imageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String info = storeAdImageService.save(storeAdImage) ? "添加成功" : "添加失败";
			logger.debug(storeAdImage.getStoreId() + "--" + info);
		}
		return "redirect:/storeAdImage/list";
	}
	
	// 弹窗测试
	@RequestMapping("showModel/list/list-data")
	public String showModelListData(String name, String mobile, Page page, Model model) {
		page = shopService.getShopNotInStoreAdImage(name, mobile,0, page);
		model.addAttribute("data", page);
		return "supermarketAd/showModelList-data";
	}
}