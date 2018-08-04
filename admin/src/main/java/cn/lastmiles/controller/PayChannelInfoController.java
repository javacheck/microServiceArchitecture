package cn.lastmiles.controller;

import java.io.IOException;
import java.util.List;

import jodd.util.Base64;

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

import cn.lastmiles.bean.PayChannelInfo;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.PayChannelInfoService;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("payChannelInfo")
public class PayChannelInfoController {
private final static Logger logger = LoggerFactory.getLogger(PayChannelInfoController.class);
	
	@Autowired
	private PayChannelInfoService payChannelInfoService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ShopService shopService;
	@RequestMapping("")
	public String index() {
		return "redirect:/payChannelInfo/list";
	}
	/**
	 * 所有供应商列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model) {
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		return "payChannelInfo/list";
	}

	@RequestMapping("list/list-data")
	public String listData(Integer type,Long storeId, Page page, Model model) {
		
		model.addAttribute("data", payChannelInfoService.findAll(storeId,type, page));
		return "payChannelInfo/list-data";
		
	}
	
	// 弹窗测试
		@RequestMapping("showModel/list/list-data")
		public String shopListData(String name, String mobile,Page page, Model model) {
			String agentName="";
			StringBuffer storeIdString = new StringBuffer();
			if(SecurityUtils.isMainStore()){
				List<Store> storeList = storeService.findByParentId(SecurityUtils.getAccountStoreId());
				boolean index = false;
				for (Store store : storeList) {
					if(index){
						storeIdString.append(",");
					}
					storeIdString.append(store.getId());
					index = true;
				}
			}
			logger.debug("storeIdString.toString()={}",storeIdString.toString());
			page = shopService.getShop(storeIdString.toString(),Constants.Status.SELECT_ALL,name, mobile, agentName,  page);
			model.addAttribute("data", page);
			return "payChannelInfo/showModelList-data";
		}
		
		/**
		 * 跳到供应商增加页面
		 * @param model
		 * @return
		 */
		@RequestMapping(value="add",method=RequestMethod.GET)
		public String add(Model model){
			model.addAttribute("isSys",SecurityUtils.isAdmin());
			return "payChannelInfo/add";
		}
		
		/**
		 * 查询渠道支付是否已存在
		 * @param
		 * @return flag (0 不存在 1已存在) 2表示该属性值
		 */
		
		@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String findPayChannelInfo(PayChannelInfo payChannelInfo) {
			logger.debug("payChannelInfo={}",payChannelInfo);
			
			String flag=payChannelInfoService.findPayChannelInfo(payChannelInfo)==null?"0":"1";
			return flag;
		}
		
		/**
		 * 编辑供应商（增加或修改）
		 * 
		 * @param 
		 * @return flag 1表示保存成功
		 * @throws IOException 
		 */
		@RequestMapping(value="add",method=RequestMethod.POST)
		public String editPayChannelInfo(PayChannelInfo payChannelInfo,@RequestParam("cert") MultipartFile imageFile) throws IOException{
			logger.debug("payChannelInfo is {} ",payChannelInfo);
			logger.debug("imageFile is {} ",imageFile.getSize());
			if (imageFile!=null&&!imageFile.isEmpty()) {
				payChannelInfo.setCertIo(Base64.encodeToString(imageFile.getBytes()));
			}else{
				payChannelInfo.setCertIo(null);
			}
			payChannelInfoService.editPayChannelInfo(payChannelInfo);
			
			return "redirect:/payChannelInfo/list";	
		}
		
		/**
		 * 跳转修改界面
		 * @param id
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
		public String toUpdate(@PathVariable Long id,Model model ){
			model.addAttribute("isSys",SecurityUtils.isAdmin());
			PayChannelInfo payChannelInfo= payChannelInfoService.findById(id);//把productAttribute对像转回页面
			model.addAttribute("payChannelInfo", payChannelInfo);
			return "/payChannelInfo/add";
		}
		
		/**
		 * 通过ID删除 数据
		 * @param id
		 * @param model
		 * @return
		 */
		
		@RequestMapping(value="delete/delete-by-payChannelInfoId",produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String deleteById(Long id){
			logger.debug("id={}",id);
			payChannelInfoService.deleteById(id);
			return "1";
		}
}
