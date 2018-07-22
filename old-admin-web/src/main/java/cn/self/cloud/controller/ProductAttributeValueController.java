package cn.self.cloud.controller;


import java.util.List;

import cn.self.cloud.bean.Product;
import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.bean.ProductAttributeValue;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.ProductAttributeService;
import cn.self.cloud.service.ProductAttributeValueService;
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
@RequestMapping("productAttributeValue")
public class ProductAttributeValueController {
	//private final static Logger logger = LoggerFactory.getLogger(ProductAttributeController.class);
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private IdService idService;
	/**
	 * 所有商品属性值列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "productAttributeValue/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productAttributeValueService.findAll(name, page));
		return "productAttributeValue/list-data";
	}
	
	/**
	 * 根据登录帐号查询帐号下所有商品（查询商品表t_product）
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-accountId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Product> findByAccountId() {
		Product product=new Product();
		product.setAccountId(SecurityUtils.getAccountId());
		List<Product> list=productService.findByAccountId(product);
		return list; 
	}
	
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @param productId
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-productId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttribute> list(Long productId) {
		List<ProductAttribute> list=productAttributeValueService.findByProductId(productId);
		return list; 
	}
	/**
	 * 通过商品属性值表里的商品属性ID查找商品属性表里的productid
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-id",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductAttribute findById(Long id) {
		return productAttributeService.findById(id); 
	}
	/**
	 * 通过商品属性值表里的商品属性ID查找全部商品属性值(查t_product_attribute_value)
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-attributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttributeValue> findByAttributeId(ProductAttributeValue productAttributeValue) {
		productAttributeValue.setAttributeId(productAttributeValue.getAttributeId());
		return productAttributeValueService.findByAttributeId(productAttributeValue); 
	}
	/**
	 * 跳到商品属性值增加页面
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(){
		return "productAttributeValue/add";
	}

	/**
	 * 查询商品属性是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductAttributeValue(ProductAttributeValue productAttributeValue) {
		productAttributeValue.setValue(productAttributeValue.getValue());
		productAttributeValue.setAttributeId(productAttributeValue.getAttributeId());
		String flag=productAttributeValueService.findProductAttributeValue(productAttributeValue)==null?"0":"1";
		return flag;
	}
	/**
	 * 保存增加商品属性值或保存修改商品属性值
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(ProductAttributeValue productAttributeValue){
		String flag="1";
		if (productAttributeValue.getId() != null){
			productAttributeValue.setId(productAttributeValue.getId());
			productAttributeValue.setValue(productAttributeValue.getValue());
			productAttributeValue.setAttributeId(productAttributeValue.getAttributeId());
			productAttributeValueService.update(productAttributeValue);
		}else {
			productAttributeValue.setId(idService.getId());
			productAttributeValue.setValue(productAttributeValue.getValue());
			productAttributeValue.setAttributeId(productAttributeValue.getAttributeId());
			productAttributeValueService.save(productAttributeValue);
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
		model.addAttribute("productAttributeValue", productAttributeValueService.findById(id));//把productAttributeValue对像转回页面
		return "/productAttributeValue/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-productAttributeValueId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(ProductAttributeValue productAttributeValue){
		productAttributeValue.setId(productAttributeValue.getId());
		productAttributeValueService.deleteById(productAttributeValue);
		return "1";
	}
	
	/**
	 * 根据商品id查询所有商品属性(查询商品属性表t_product_attribute)
	 * @return
	 */
	@RequestMapping(value="list/ajax/list-by-productAttributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttributeValue> productAttributeValueList(Long productAttributeId) {
		List<ProductAttributeValue> list=productAttributeValueService.productAttributeValueList(productAttributeId);
		return list; 
	}
}
