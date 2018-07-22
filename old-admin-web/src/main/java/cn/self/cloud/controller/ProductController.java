package cn.self.cloud.controller;

import java.util.List;

import cn.self.cloud.bean.Product;
import cn.self.cloud.bean.ProductCategory;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.ProductCategoryService;
import cn.self.cloud.service.ProductService;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("product")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private IdService idService;
	/**
	 * 所有商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "product/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productService.findAll(name, page));
		return "product/list-data";
	}
	
	/**
	 * 跳到商品增加页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		return "/product/add";
	}
	/**
	 * 查询父类为空的产品分类
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-parent1",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> category() {
		List<ProductCategory> list=productCategoryService.findByParentId(null);
		return list; 
	}
	/**
	 * 查询商品是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProduct(Product product) {
		product.setName(product.getName());
		product.setCategoryId(product.getCategoryId());
		product.setAccountId(SecurityUtils.getAccountId());
		String flag=productService.findProduct(product)==null?"0":"1";
		return flag;
	}
	/**
	 * 保存增加商品或保存修改商品
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(Product product){
		String flag="1";
		if (product.getId() != null){
				product.setId(product.getId());
				product.setName(product.getName());
				product.setCategoryId(product.getCategoryId());
				productService.update(product);
		}else {
				product.setId(idService.getId());
				product.setName(product.getName());
				product.setCategoryId(product.getCategoryId());
				product.setAccountId(SecurityUtils.getAccountId());//获得登录帐号accountId
				productService.save(product);
		}
		return flag;	
	}
	
	/**
	 * 跳转修改界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}",method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id,Model model ){
		model.addAttribute("product", productService.findById(id));
		return "/product/add";
	}
	
	/**
	 * 跳转商品库存列表界面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "productStock/{id}",method = RequestMethod.GET)
	public String productStockList(@PathVariable Long id,Model model ){
		model.addAttribute("productId", id);
		return "/productStock/list";
	}
	
	@RequestMapping(value="delete/delete-by-productId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Product product){
		product.setId(product.getId());
		productService.deleteById(product);
		return "1";
	}
	/**
	 * 根据parentId产品分类子类
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-parent",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductCategory> list(Long parentId) {
		List<ProductCategory> list=productCategoryService.findByParentId(parentId);
		return list; 
	}
	
	
	/**
	 * 根据categoryId取产品分类
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-categoryId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductCategory findById(Long categoryId) {
		
		ProductCategory productCategory=productCategoryService.findById(categoryId);
		return productCategory; 
	}
	
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-productCategoryId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Product> productList(Long categoryId) {
		List<Product> list=productService.productList(categoryId);
		return list; 
	}
}
