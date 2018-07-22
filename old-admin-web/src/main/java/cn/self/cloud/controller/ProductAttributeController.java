package cn.self.cloud.controller;


import cn.self.cloud.bean.Product;
import cn.self.cloud.bean.ProductAttribute;
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
@RequestMapping("productAttribute")
public class ProductAttributeController {
	//private final static Logger logger = LoggerFactory.getLogger(ProductAttributeController.class);
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private IdService idService;
	/**
	 * 所有商品属性列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list() {
		return "productAttribute/list";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productAttributeService.findAll(name, page));
		return "productAttribute/list-data";
	}
	
	/**
	 * 跳到商品属性增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String add(Model model){
		Product product=new Product();
		product.setAccountId(SecurityUtils.getAccountId());
		model.addAttribute("list", productService.findByAccountId(product));
		return "productAttribute/add";
	}

	/**
	 * 查询商品属性是否已存在
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductAttribute(ProductAttribute productAttribute) {
		productAttribute.setId(productAttribute.getId());
		productAttribute.setName(productAttribute.getName());
		String flag=productAttributeService.findProductAttribute(productAttribute)==null?"0":"1";
		return flag;
	}
	/**
	 * 保存增加商品属性或保存修改商品属性
	 * 
	 * @param 
	 * @return flag 1表示保存成功
	 */
	@RequestMapping(value="list/ajax/save",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String save(ProductAttribute productAttribute){
		String flag="1";
		if (productAttribute.getId() != null){
				productAttribute.setId(productAttribute.getId());
				productAttribute.setName(productAttribute.getName());
				productAttributeService.update(productAttribute);
			
		}else {
			String[] arr=productAttribute.getProductIds().split(",");//productIds是商品属性id数组，数组以逗号隔开
			for(int i=0;i<arr.length;i++){
				productAttribute.setId(idService.getId());
				productAttribute.setName(productAttribute.getName());
				productAttribute.setProductId(Long.parseLong(arr[i]));
				productAttributeService.save(productAttribute);
			}
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
		Product product=new Product();
		product.setAccountId(SecurityUtils.getAccountId());
		model.addAttribute("productAttribute", productAttributeService.findById(id));//把productAttribute对像转回页面
		model.addAttribute("list", productService.findByAccountId(product));//把通过accountId查到的商品列表传回页面
		return "/productAttribute/add";
	}
	
	/**
	 * 通过ID删除 数据
	 * @param productAttribute
	 * @return
	 */
	
	@RequestMapping(value="delete/delete-by-productAttributeId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(ProductAttribute productAttribute){
		Integer count=productAttributeValueService.findByAttributeId(productAttribute.getId());
		if(count>0){
			return "0";
		}else{
			productAttribute.setId(productAttribute.getId());
			productAttributeService.deleteById(productAttribute);
			return "1";
		}
	}
	
}
