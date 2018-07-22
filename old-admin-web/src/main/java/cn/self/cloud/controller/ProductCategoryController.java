package cn.self.cloud.controller;

import cn.self.cloud.bean.ProductCategory;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("productCategory")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 分类管理主页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "manager")
	public String manager(Model model) {
		model.addAttribute("list", productCategoryService.findByParentId(null));
		return "productCategory/manager";
	}

	/**
	 * 保存 提交修改 提交保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "manager/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(ProductCategory productCategory) {
		if (null != productCategory.getId()) {
			productCategoryService.updateByID(productCategory);
		} else {
			productCategoryService.save(productCategory);
		}
		return "1";
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
		model.addAttribute("list",
				productCategoryService.findByParentId(parentId));
		return "productCategory/list-by-parent";
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("manager/delete/{id}")
	@ResponseBody
	public int delete(@PathVariable Long id) {
		return productCategoryService.deleteById(id);
	}

	/**
	 * 所有列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "productCategory/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productCategoryService.findAll(name, page));
		return "productCategory/list-data";
	}

}
