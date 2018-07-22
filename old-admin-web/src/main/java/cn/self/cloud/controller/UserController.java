package cn.self.cloud.controller;

import cn.self.cloud.bean.User;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.UserService;
import cn.self.cloud.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("user")
public class UserController {
	private final static Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("")
	public String index() {
		return "redirect:/user/list";
	}

	@RequestMapping("list")
	public String list() {
		return "user/list";
	}

	@RequestMapping("list-data")
	public String listData(String mobile, Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		model.addAttribute("data", userService.list(mobile,
				SecurityUtils.getAccountStoreId(), page));
		return "user/list-data";
	}

	/**
	 * 数据 修改||添加 界面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(User user, Model model) {
		if (null != user.getId()) {
			String info = user.getId()
					+ (userService.update(user) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			user.setStoreId(SecurityUtils.getAccountStoreId());
			user.setCreatedId(SecurityUtils.getAccountId());
			String info = userService.save(user) ? "添加成功" : "添加失败";
			logger.debug(info);
		}
		return "redirect:/user/list";
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(User user, Model model) {
		return "/user/add";
	}

	/**
	 * 跳转修改界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		model.addAttribute("User", userService.findById(id));
		return "/user/add";
	}

	/**
	 * 通过ID删除 数据
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model) {
		String info = id + (userService.delete(id) ? "删除成功" : "删除失败");
		logger.debug(info);
		return "redirect:/user/list";
	}
}
