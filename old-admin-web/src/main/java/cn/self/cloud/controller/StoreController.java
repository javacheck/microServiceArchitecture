package cn.self.cloud.controller;

import cn.self.cloud.bean.Store;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.StoreService;
import cn.self.cloud.utils.SecurityUtils;
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

@Controller
@RequestMapping("store")
public class StoreController {// 店铺增删改查
	private final static Logger logger = LoggerFactory
			.getLogger(StoreController.class);
	@Autowired
	private StoreService storeService;// 注入StoreService

	/**
	 * 分类管理主页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "manager")
	public String manager(Model model) {
		model.addAttribute("list", storeService.findByParentId(null));
		return "store/manager";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add() {
		return "store/add";
	}

	/**
	 * 保存 提交修改 提交保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(Store store) {
		if (null != store.getId()) {
			storeService.updateByID(store);
		} else {
			store.setAccountId(SecurityUtils.getAccountId());
			storeService.save(store);
		}
		return "redirect:/store/list";
	}
	
	@RequestMapping("edit/{id}")
	public String edit(@PathVariable Long id,Model model){
		model.addAttribute("store", storeService.findById(id));
		return "store/add";
	}

	/**
	 * 根据parentId查询子类
	 * 
	 * @param parentId
	 * @param model
	 * @return
	 */
	@RequestMapping("manager/ajax/list-by-parent")
	public String list(Long parentId, Model model) {
		model.addAttribute("list", storeService.findByParentId(parentId));
		return "store/list-by-parent";
	}

	/**
	 * 通过ID删除 数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-storeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		storeService.deleteById(id);
		return "1";
	}

	/**
	 * 所有列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "store/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data",
				storeService.findAll(name, page, SecurityUtils.getAccountId()));
		return "store/list-data";
	}

}
