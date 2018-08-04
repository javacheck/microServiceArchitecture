package cn.lastmiles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Device;
import cn.lastmiles.bean.Order;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.DeviceService;
import cn.lastmiles.service.OrderServise;
import cn.lastmiles.service.ProductCategoryService;
import cn.lastmiles.service.ProductStockService;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.UserService;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("store")
public class StoreController {// 店铺增删改查
	
	@Autowired
	private StoreService storeService;// 注入StoreService
	
	@Autowired
	private ProductStockService productStockService;
	
	@Autowired
	private DeviceService deviceServcie;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private OrderServise orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;

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
	 * @param id
	 * @param name
	 * @param parentId
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
	 * @param model
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-storeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		
		// 关联了分类不让删
		List<ProductCategory> productCategoryList = productCategoryService.findByStoreId(id);
		if( null != productCategoryList && productCategoryList.size() > 0 ){
			return "1";
		}
		
		// 关联了商品不让删(通过查询库存表来判断----关联了商品也就是关联了属性，关联了属性值)
		ProductStock productStock = productStockService.findByStoreId(id);
		if( null != productStock ){
			return "2";
		}
		
		// 关联了设备不让删
		Device device = deviceServcie.findByStoreId(id);
		if( null != device ){
			return "3";
		}
		
		// 关联了订单不让删(有订单才会有统计)
		Order order = orderService.findByStoreId(id);
		if( null != order ){
			return "4";
		}
		
		// 关联了会员不让删
		User user = userService.findByStoreId(id);
		if( null != user){
			return "5";
		}
		
		// 关联了账号不让删
		List<Account> account = accountService.findByStoreId(id);
		if( !account.isEmpty() ){
			return "6";
		}
		
		storeService.deleteById(id);
		return "0";
	}

	/**
	 * 所有列表
	 * 
	 * @param model
	 * @return
	 */
	
	@RequestMapping("")
	public String index() {
		return "redirect:/store/list";
	}
	
	@RequestMapping(value = "list")
	public String list() {
		return "store/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		if( null != name ){
			name = name.replaceAll(" ", "");			
		}
		model.addAttribute("data",
				storeService.findAll(name, page, SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId()));
		return "store/list-data";
	}

}
