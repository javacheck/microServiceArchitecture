package cn.self.cloud.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.self.cloud.bean.ProductAttribute;
import cn.self.cloud.bean.ProductAttributeValue;
import cn.self.cloud.bean.ProductImage;
import cn.self.cloud.bean.ProductStock;
import cn.self.cloud.commonutils.file.FileService;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.service.*;
import cn.self.cloud.utils.SecurityUtils;
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

@Controller
@RequestMapping("productStock")
public class ProductStockController {
	//private final static Logger logger = LoggerFactory.getLogger(ProductAttributeController.class);
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private IdService idService;
	
//	@Autowired
//	private FileService fileService;
	/**
	 * 所有商品库存列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "add")
	public String list() {
		return "productStock/add";
	}

	@RequestMapping("list/list-data")
	public String listData(String name, Page page, Model model) {
		model.addAttribute("data", productStockService.findAll(name, page));
		return "productStock/list-data";
	}
	@RequestMapping(value="list/json/find-by-productId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findStockListByproductId( Long id ){//入参为商品ID
		if(productAttributeService.productAttributeList(id).size()>0){
			return "1";
		}else{
			return "0";
		}
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
		List<ProductAttribute> productAttributeList=productAttributeService.productAttributeList(id);//通过分类id查属性列表
		List<ProductStock> psList=productStockService.findByProductId(id);//以productId查找库存表里的stock列表
		String[] arr;
		if(psList!=null){
			for(int i=0;i<psList.size();i++){
				List<ProductAttributeValue> pavList=new ArrayList<ProductAttributeValue>();
				arr=psList.get(i).getAttributeCode().split("-");//属性值111-222-333
				for(int j=0;j<arr.length;j++){
					pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));//把属性值放进list里面
				}
				psList.get(i).setPavList(pavList);
			}
		}
		model.addAttribute("productAttributeList", productAttributeList);
		model.addAttribute("psList",psList);
		return "/productStock/list";
	}
	/**
	 * 通过商品ID获取属性列表
	 * @param productId
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/productAttributeList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductAttribute> productAttributeList( Long productId ){//入参为商品ID
		List<ProductAttribute> productAttributeList=productAttributeService.productAttributeList(productId);//通过分类id查属性列表
		return productAttributeList;
	}
	/**
	 * 通过商品ID获取属性值列表
	 * @param productId
	 * @param 
	 * @return
	 */
	@RequestMapping(value="list/ajax/productAttributeValueList",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductStock> productAttributeValueList( Long productId ){//入参为商品ID
		
		List<ProductStock> psList=productStockService.findByProductId(productId);//以productId查找库存表里的stock列表
		String[] arr;
		for(int i=0;i<psList.size();i++){
			List<ProductAttributeValue> pavList=new ArrayList<ProductAttributeValue>();
			arr=psList.get(i).getAttributeCode().split("-");//属性值111-222-333
			for(int j=0;j<arr.length;j++){
				pavList.add(productAttributeValueService.findById(Long.parseLong(arr[j])));//把属性值放进list里面
			}
			psList.get(i).setPavList(pavList);
			
		}
		return psList;
	}
	
	/**
	 * 跳到商品库存增加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add/{productId}",method = RequestMethod.GET)
	public String toAdd(@PathVariable Long productId,Model model ){
		model.addAttribute("product", productService.findById(productId));
		model.addAttribute("store",storeService.findById(SecurityUtils.getAccountStoreId()));
		return "/productStock/add";
	}
	
	
	/**
	 * 查询库存商品属性值是否相同
	 * @param
	 * @return flag (0 不存在 1已存在)
	 */
	
	@RequestMapping(value="list/ajax/exist",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findProductStock(ProductStock productStock) {
		productStock.setAttributeCode(productStock.getAttributeCode());
		String flag=productStockService.findProductStock(productStock)==null?"0":"1";
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
	public String save(ProductStock productStock){
		String flag="1";
		if (productStock.getId() != null){
			productStock.setId(productStock.getId());
			productStock.setStock(productStock.getStock());
			productStock.setAlarmValue(productStock.getAlarmValue());
			productStock.setPrice(productStock.getPrice());
			productStock.setBarCode(productStock.getBarCode());
			productStockService.update(productStock);
		}else {
			productStock.setId(idService.getId());
			productStock.setId(productStock.getId());
			productStock.setStock(productStock.getStock());
			productStock.setAlarmValue(productStock.getAlarmValue());
			productStock.setProductId(productStock.getProductId());
			productStock.setAccountId(SecurityUtils.getAccountId());
			productStock.setStoreId(productStock.getStoreId());
			productStock.setAttributeCode(productStock.getAttributeCode());
			productStock.setPrice(productStock.getPrice());
			productStock.setBarCode(productStock.getBarCode());
			productStockService.save(productStock);
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
		model.addAttribute("product", productService.findById(productStockService.findById(id).getProductId()));
		model.addAttribute("store",storeService.findById(SecurityUtils.getAccountStoreId()));
		model.addAttribute("productStock", productStockService.findById(id));//把productStock对像转回页面
		return "/productStock/add";
	}
	 /**
		 * 跳转修改界面
		 * @param id
		 * @param model
		 * @return
		 */
		 @RequestMapping(value = "uploadImage/{id}",method = RequestMethod.GET)
		public String uploadImage(@PathVariable Long id,Model model ){
			model.addAttribute("productStockId", id);//把productStockId转回页面
			return "/productStock/uploadImage";
		}
	/**
	 * 	 
	 * @return
	 * @throws IOException
	 */
	 @RequestMapping(value = "saveImage", method = RequestMethod.POST)
		public String upload(@RequestParam(value="imageFile", required=true)  MultipartFile[] imageFile,Long productStockId, Model model) throws IOException {
		 	InputStream in = null;
		 	ProductImage productImage=null;
		    for(int i=0;i<imageFile.length;i++){
		    	in=imageFile[i].getInputStream();
//		    	String id = fileService.save(in);//图片ID
				String id = null;
		    	//图片后缀
		    	String suffix=imageFile[i].getOriginalFilename().substring(imageFile[i].getOriginalFilename().lastIndexOf(".")+1, imageFile[i].getOriginalFilename().length());
		    	productImage=new ProductImage();
		    	productImage.setId(id);
		    	productImage.setProductStockId(productStockId);//库存ID
		    	productImage.setSuffix(suffix);
		    	productImage.setSort(Integer.parseInt(imageFile[i].getOriginalFilename().substring(0, imageFile[i].getOriginalFilename().lastIndexOf("."))));//顺序
		    	productStockService.saveProductImage(productImage);
		    	in.close();
		    }
		   
			return "productStock/uploadImage";
		}
	/**
	 * 通过ID删除 数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="delete/delete-by-productStockId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteById(Long id){
		productStockService.deleteById(id);
		return "1";
	}
}
