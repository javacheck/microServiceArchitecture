package cn.self.cloud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.self.cloud.bean.*;
import cn.self.cloud.commonutils.compress.CompressFileUtils;
import cn.self.cloud.commonutils.eximport.ExcelUtils;
import cn.self.cloud.commonutils.file.FileService;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.commonutils.properties.ConfigUtils;
import cn.self.cloud.service.*;
import cn.self.cloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("productUpload")
public class ProductUploadController {
	
	@Autowired
	private IdService idService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	
	@Autowired
	private ProductStockService productStockService;
	
//	@Autowired
//	private FileService fileService;
	
	@RequestMapping(value = "list")
	public String list() {
		return "productUpload/list";
	}
	
	/**
	 * xls文件上传
	 * 
	 * @param 
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String fileUpload(MultipartFile exelFile) throws IOException {
		InputStream in = exelFile.getInputStream();
		List<List<String>> list=ExcelUtils.simpleExcel(in);
		for(int i=0;i<list.size();i++){//一行一行的读
			if(i>0){
				Product product=new Product();
				ProductAttribute productAttribute=new ProductAttribute();
				ProductAttributeValue productAttributeValue=new ProductAttributeValue();
				ProductStock productStock=new ProductStock();
				Long productId = 0L;
				String attributeCode="";
				for(int j=0;j<list.get(i).size();j++){//每一行一格一格的读
					if(j==0){
						if(list.get(i).get(j).split("-").length==1){//长度为1，表示只有一级分类
							ProductCategory productCategory=new ProductCategory();
							if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
								productCategory.setName(list.get(i).get(j).split("-")[0]);
								//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
								productCategoryService.save(productCategory);
							}
						}else if(list.get(i).get(j).split("-").length==2){//长度为2，表示有二级分类
							ProductCategory productCategory=new ProductCategory();
							//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
							if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
								productCategory.setName(list.get(i).get(j).split("-")[0]);
								productCategoryService.save(productCategory);
								Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
								//把新的二级分类名称加入数据库
								productCategory.setName(list.get(i).get(j).split("-")[0]);
								productCategory.setParentId(parentId);
								productCategoryService.save(productCategory);
							}else{//如果一级分类已存在
								Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
								if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
									//把新的二级分类名称加入数据库
									productCategory.setName(list.get(i).get(j).split("-")[1]);
									productCategory.setParentId(parentId);
									productCategoryService.save(productCategory);
								}
							}
						}else{//长度为3，表示有三级分类
							ProductCategory productCategory=new ProductCategory();
							//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
							if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
								productCategory.setName(list.get(i).get(j).split("-")[0]);
								productCategoryService.save(productCategory);
								Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
								//把新的二级分类名称加入数据库
								productCategory.setName(list.get(i).get(j).split("-")[1]);
								productCategory.setParentId(parentId);
								productCategoryService.save(productCategory);
								
							}else{//如果一级分类已存在
								Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
								//如果二级分类不存在
								if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
									//把新的二级分类名称加入数据库
									productCategory.setName(list.get(i).get(j).split("-")[1]);
									productCategory.setParentId(parentId);
									productCategoryService.save(productCategory);
									Long parentId1=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId()).getId();
									//存入三级分类
									productCategory.setName(list.get(i).get(j).split("-")[2]);
									productCategory.setParentId(parentId1);
									productCategoryService.save(productCategory);
								}else{//如果二级分类已存在
									Long parentId1=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId()).getId();
									//如果三级分类不存在
									if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[2],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
										productCategory.setName(list.get(i).get(j).split("-")[2]);
										productCategory.setParentId(parentId1);
										productCategoryService.save(productCategory);
									}
								}
							}
						}
					}else if(j==1){//第二格为商品名称
						product.setName(list.get(i).get(j));
						product.setCategoryId(productCategoryService.findProductCategoryByName(list.get(i).get(0).split("-")[list.get(i).get(0).split("-").length-1], SecurityUtils.getAccountStoreId()).getId());
						product.setAccountId(SecurityUtils.getAccountId());
						//如果数据库找不到分类下相同的商品时，把新商品存入数据库
						if((productService.findProduct(product)==null?"0":"1").equals("0")){
							product.setId(idService.getId());
							productService.save(product);
							//productId=product.getId();
						}
						productId=productService.findProduct(product).getId();
					}else if(j>5){//第六格子开始是属性值
						if(list.get(i).get(j)!=null){//属性值不为空
							productAttribute.setName(list.get(0).get(j));//属性名称
							productAttribute.setProductId(productService.findProduct(product).getId());//商品ID
							//如果该商品下没有存在该属性
							if((productAttributeService.findProductAttribute(productAttribute)==null?"0":"1").equals("0")){
								productAttribute.setId(idService.getId());//生成新属性ID
								productAttributeService.save(productAttribute);//把属性存进数据库
							}
							productAttributeValue.setValue(list.get(i).get(j));//属性值
							productAttributeValue.setAttributeId(productAttributeService.findProductAttribute(productAttribute).getId());//属性ID
							//如果该属性下没有存在该属性值
							if((productAttributeValueService.findProductAttributeValue(productAttributeValue)==null?"0":"1").equals("0")){
								productAttributeValue.setId(idService.getId());//生成新属性值ID
								productAttributeValueService.save(productAttributeValue);//把属性值存进数据库
							}
							attributeCode+=Long.toString(productAttributeValueService.findProductAttributeValue(productAttributeValue).getId())+"-";
						}
					}
				}
				
				productStock.setId(idService.getId());//生成库存ID);
				Double d = new Double(Double.valueOf(list.get(i).get(2)));
				productStock.setStock(d.intValue());//库存数量
				Double d1 = new Double(Double.valueOf(list.get(i).get(3)));
				productStock.setAlarmValue(d1.intValue());//警报值
				productStock.setPrice(Double.valueOf(list.get(i).get(4)));//价格
				productStock.setAccountId(SecurityUtils.getAccountId());//登录用户ID
				productStock.setStoreId(SecurityUtils.getAccountStoreId());//登录用户店铺ID
				System.out.println(productId);
				productStock.setProductId(productId);//商品ID
				System.out.println(attributeCode);
				productStock.setAttributeCode(attributeCode.substring(0,attributeCode.length()-1));//属性值ID相接
				productStockService.save(productStock);
			}
			
		}
		return "redirect:/productUpload/list";
	}
	
	/**
	 * xls文件上传
	 * 
	 * @param 
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "save1", method = RequestMethod.POST)
	public String zipOrRarUpload(MultipartFile zipOrRarFile) throws IOException {
		String uploadPath=ConfigUtils.getProperty("file.uploadPath");
		String suffix=zipOrRarFile.getOriginalFilename().substring(zipOrRarFile.getOriginalFilename().lastIndexOf(".")+1, zipOrRarFile.getOriginalFilename().length());
		File file=new File(uploadPath+zipOrRarFile.getOriginalFilename());
		zipOrRarFile.transferTo(file);
		if(suffix.equals("zip")){//判断后缀为.zip或是.rar
			CompressFileUtils.unZipFiles(file, uploadPath);
		}else{
			CompressFileUtils.unRarFile(uploadPath+zipOrRarFile.getOriginalFilename(), uploadPath);
			InputStream in = new FileInputStream(uploadPath+"modle.xls"); 
			List<List<String>> list=ExcelUtils.simpleExcel(in);
			for(int i=0;i<list.size();i++){//一行一行的读
				if(i>0){//读第二行开始
					Product product=new Product();
					ProductAttribute  productAttribute=new ProductAttribute();
					ProductAttributeValue productAttributeValue=new ProductAttributeValue();
					ProductStock productStock=new ProductStock();
					Long productId = 0L;
					String attributeCode="";
					List<String> imageList=new ArrayList<String>();
					Map<Integer,List<String>> imageMap=new HashMap<Integer,List<String>>();
					ProductImage productImage=new ProductImage();
					for(int j=0;j<list.get(i).size();j++){//每一行一格一格的读
						if(j==0){
							if(list.get(i).get(j).split("-").length==1){//长度为1，表示只有一级分类
								ProductCategory productCategory=new ProductCategory();
								if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
									productCategory.setName(list.get(i).get(j).split("-")[0]);
									//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
									productCategoryService.save(productCategory);
								}
							}else if(list.get(i).get(j).split("-").length==2){//长度为2，表示有二级分类
								ProductCategory productCategory=new ProductCategory();
								//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
								if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
									productCategory.setName(list.get(i).get(j).split("-")[0]);
									productCategoryService.save(productCategory);
									Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
									//把新的二级分类名称加入数据库
									productCategory.setName(list.get(i).get(j).split("-")[0]);
									productCategory.setParentId(parentId);
									productCategoryService.save(productCategory);
								}else{//如果一级分类已存在
									Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
									if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
										//把新的二级分类名称加入数据库
										productCategory.setName(list.get(i).get(j).split("-")[1]);
										productCategory.setParentId(parentId);
										productCategoryService.save(productCategory);
									}
								}
							}else{//长度为3，表示有三级分类
								ProductCategory productCategory=new ProductCategory();
								//查询到名称为“一级分类名称”在数据库为空时，把新的一级分类名称加入数据库
								if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
									productCategory.setName(list.get(i).get(j).split("-")[0]);
									productCategoryService.save(productCategory);
									Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
									//把新的二级分类名称加入数据库
									productCategory.setName(list.get(i).get(j).split("-")[1]);
									productCategory.setParentId(parentId);
									productCategoryService.save(productCategory);
									
								}else{//如果一级分类已存在
									Long parentId=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[0],SecurityUtils.getAccountStoreId()).getId();
									//如果二级分类不存在
									if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
										//把新的二级分类名称加入数据库
										productCategory.setName(list.get(i).get(j).split("-")[1]);
										productCategory.setParentId(parentId);
										productCategoryService.save(productCategory);
										Long parentId1=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId()).getId();
										//存入三级分类
										productCategory.setName(list.get(i).get(j).split("-")[2]);
										productCategory.setParentId(parentId1);
										productCategoryService.save(productCategory);
									}else{//如果二级分类已存在
										Long parentId1=productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[1],SecurityUtils.getAccountStoreId()).getId();
										//如果三级分类不存在
										if((productCategoryService.findProductCategoryByName(list.get(i).get(j).split("-")[2],SecurityUtils.getAccountStoreId())==null?"0":"1").equals("0")){
											productCategory.setName(list.get(i).get(j).split("-")[2]);
											productCategory.setParentId(parentId1);
											productCategoryService.save(productCategory);
										}
									}
								}
							}
						}else if(j==1){//第二格为商品名称
							product.setName(list.get(i).get(j));
							product.setCategoryId(productCategoryService.findProductCategoryByName(list.get(i).get(0).split("-")[list.get(i).get(0).split("-").length-1], SecurityUtils.getAccountStoreId()).getId());
							product.setAccountId(SecurityUtils.getAccountId());
							//如果数据库找不到分类下相同的商品时，把新商品存入数据库
							if((productService.findProduct(product)==null?"0":"1").equals("0")){
								product.setId(idService.getId());
								productService.save(product);
								//productId=product.getId();
							}
							productId=productService.findProduct(product).getId();
						}else if(j==6){
							 String imageId;
							String path="e:/zipfile/"+list.get(i).get(j);
							 file=new File(path);
							 File[] tempList = file.listFiles();
							 for (int k = 0; k < tempList.length; k++) {
							   if (tempList[k].isFile()) {
								   InputStream is = new FileInputStream(tempList[k]); 
								   imageId=null;//fileService.save(is);
								   imageList.add(imageId+","+tempList[k].toString().substring(tempList[k].toString().lastIndexOf("\\")+1, tempList[k].toString().length()));
							   }
							 }
							 imageMap.put(i, imageList);
						}else if(j>6){//第六格子开始是属性值
							if(list.get(i).get(j)!=null){//属性值不为空
								productAttribute.setName(list.get(0).get(j));//属性名称
								productAttribute.setProductId(productService.findProduct(product).getId());//商品ID
								//如果该商品下没有存在该属性
								if((productAttributeService.findProductAttribute(productAttribute)==null?"0":"1").equals("0")){
									productAttribute.setId(idService.getId());//生成新属性ID
									productAttributeService.save(productAttribute);//把属性存进数据库
								}
								productAttributeValue.setValue(list.get(i).get(j));//属性值
								productAttributeValue.setAttributeId(productAttributeService.findProductAttribute(productAttribute).getId());//属性ID
								//如果该属性下没有存在该属性值
								if((productAttributeValueService.findProductAttributeValue(productAttributeValue)==null?"0":"1").equals("0")){
									productAttributeValue.setId(idService.getId());//生成新属性值ID
									productAttributeValueService.save(productAttributeValue);//把属性值存进数据库
								}
								attributeCode+=Long.toString(productAttributeValueService.findProductAttributeValue(productAttributeValue).getId())+"-";
							}
						}
					}
					//先判断每一行在库存中是否已存在，已存在，修改库存，不存在，新增
					//通过每一行的属性值组成attributeCode去库存中查
					productStock.setAttributeCode(attributeCode.substring(0,attributeCode.length()-1));
					if((productStockService.findProductStock(productStock)==null?"0":"1").equals("0")){//新增
						productStock.setId(idService.getId());//生成库存ID
						Double d = new Double(Double.valueOf(list.get(i).get(2)));
						productStock.setStock(d.intValue());//库存数量
						Double d1 = new Double(Double.valueOf(list.get(i).get(3)));
						productStock.setAlarmValue(d1.intValue());//警报值
						productStock.setPrice(Double.valueOf(list.get(i).get(4)));//价格
						productStock.setBarCode(list.get(i).get(5));//条形码
						productStock.setAccountId(SecurityUtils.getAccountId());//登录用户ID
						productStock.setStoreId(SecurityUtils.getAccountStoreId());//登录用户店铺ID
						productStock.setProductId(productId);//商品ID
						productStock.setAttributeCode(attributeCode.substring(0,attributeCode.length()-1));//属性值ID相接
						productStockService.save(productStock);
						for(int j=0;j<imageMap.get(i).size();j++){
							productImage.setProductStockId(productStock.getId());//库存ID
							productImage.setSort(Integer.parseInt(imageMap.get(i).get(j).split(",")[1].substring(0, imageMap.get(i).get(j).split(",")[1].lastIndexOf("."))));//顺序
							if((productStockService.findProductImage(productImage)==null?"0":"1").equals("0")){
								String id=imageMap.get(i).get(j).split(",")[0];//图片ID
								String imageSuffix=imageMap.get(i).get(j).split(",")[1].substring(imageMap.get(i).get(j).split(",")[1].lastIndexOf(".")+1,imageMap.get(i).get(j).split(",")[1].length());//图片后缀
								productImage.setId(id);
								productImage.setSuffix(imageSuffix);
								productStockService.saveProductImage(productImage);
							}
						}
					    
					}else{//修改
						productStock.setId(productStockService.findProductStock(productStock).getId());
						Double d = new Double(Double.valueOf(list.get(i).get(2)));
						productStock.setStock(d.intValue());//库存数量
						Double d1 = new Double(Double.valueOf(list.get(i).get(3)));
						productStock.setAlarmValue(d1.intValue());//警报值
						productStock.setPrice(Double.valueOf(list.get(i).get(4)));//价格
						productStock.setBarCode(list.get(i).get(5));//条形码
						productStockService.update(productStock);
						
						for(int j=0;j<imageMap.get(i).size();j++){
							productImage.setProductStockId(productStock.getId());//库存ID
							productImage.setSort(Integer.parseInt(imageMap.get(i).get(j).split(",")[1].substring(0, imageMap.get(i).get(j).split(",")[1].lastIndexOf("."))));//顺序
							if((productStockService.findProductImage(productImage)==null?"0":"1").equals("0")){
								String id=imageMap.get(i).get(j).split(",")[0];//图片ID
								String imageSuffix=imageMap.get(i).get(j).split(",")[1].substring(imageMap.get(i).get(j).split(",")[1].lastIndexOf(".")+1,imageMap.get(i).get(j).split(",")[1].length());//图片后缀
								productImage.setId(id);
								productImage.setSuffix(imageSuffix);
								productStockService.saveProductImage(productImage);
							}
						}
					}
				}
				
			}
		}
		return "redirect:/productUpload/list";
	}
}
