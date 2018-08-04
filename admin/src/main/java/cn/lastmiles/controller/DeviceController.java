package cn.lastmiles.controller;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.service.DeviceService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("device")
public class DeviceController {
	private final static Logger logger = LoggerFactory
			.getLogger(DeviceController.class);
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private IdService idService;
	
	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isMainStore",SecurityUtils.isMainStore());
		return "device/list";
	}
	
	@RequestMapping("list-data")
	public String listData(String storeName,String deviceSn,Long storeId, Page page, Model model) {
		List<Store> stores =new ArrayList<Store>();
		if (SecurityUtils.isStore()) {//商家
			if(SecurityUtils.isMainStore()){
				if( null == storeId ){ // 没总店订单商家查看权限的情况下，只显示总店自己的订单信息
					Store store = new Store();
					store.setId(SecurityUtils.getAccountStoreId());
					stores.add(store);						
				} else if( null != storeId && ObjectUtils.equals(storeId, -1L) ){ // 有查看权限，且是全部选项，则只查询此总店下的商家
					List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
					stores.addAll(storeList);
				} else { // 有权限，且指定了固定查询的商家
					Store store = new Store();
					store.setId(storeId);
					stores.add(store);
				}
			} else {
				Store store = new Store();
				store.setId(SecurityUtils.getAccountStoreId());
				stores.add(store);			
			}
		}
		if (SecurityUtils.isAdmin()) {//管理员
			stores=null;
		}
		if (SecurityUtils.isAgent()) {//代理商
			stores=storeService.getAgentAndStoreList(SecurityUtils.getAccountAgentId());
		}
		model.addAttribute("data", deviceService.list(stores,storeName,deviceSn, page));
		return "device/list-data";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String toAdd(Model model){
		return "/device/add";
	}
	
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		logger.debug("toUpdate  id -->"+id);
		Device device = deviceService.findById(id);
		logger.debug("toUpdate  device -->"+device);
		model.addAttribute("device",device);
		return "/device/add";
	}
	
	@RequestMapping(value="delete/delete-by-Id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		deviceService.delete(id);
		return "1";
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String add(Device device, Model model) {
		if (null != device.getId()) {
			String info = device.getId()
					+ (deviceService.update(device) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			device.setId(idService.getId());
			String info = deviceService.save(device) ? "添加成功" : "添加失败";
			logger.debug(device.getId() + "--" + info);
		}
		return "redirect:/device/list";
	}
	
	@RequestMapping(value = "checkDeviceId")
	@ResponseBody
	public String checkDeviceId(Long id,String deviceSn, Model model) {
		deviceSn = deviceSn.replaceAll("\\s*", "");
		if (deviceService.checkDeviceSn(id,deviceSn)) {
			return "1";
		} else {
			return "0";
		}

	}
	
	@RequestMapping(value = "checkSerialId")
	@ResponseBody
	public String checkSerialId(Long id,String serialId) {
		serialId = serialId.replaceAll("\\s*", "");
		if (deviceService.checkSerialId(id,serialId)) {
			return "1";
		} else {
			return "0";
		}

	}
}