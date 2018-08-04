package cn.lastmiles.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import jodd.io.FileNameUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Brand;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.UploadStock;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ExcelUtils;
import cn.lastmiles.common.utils.StringUtils;

@Service
public class ProductUploadService {

	private final static Logger logger = LoggerFactory
			.getLogger(ProductUploadService.class);
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

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ProductBrandService productBrandService;

	
	@Transactional
	public String rarFile(InputStream in, Long storeId,Long categoryId, String uploadPath,Long accountId,String _path) {
		String flag = "";
		
		try {
				List<List<String>> list = ExcelUtils.simpleExcel(in);// 读取model.xls
				logger.debug("行数==="+list.size());
				//把商家和分类进数据库，拿所有库存拿出来放到一个list里面
				//List<ProductStock> productStockList=productStockService.findAll(storeId, categoryId, null, null, null); 
				if(list.size()<=1){
					flag = "请按照模板填写商品信息！";
					return flag;
				}else{
					List<UploadStock> allList=new ArrayList<UploadStock>();
					List<ProductAttribute> attributeList=productAttributeService.productAttributeList1(categoryId);
					for (int i = 0; i < list.size(); i++) {// 一行一行的读
						if (i > 0) {// 读第二行开始
							logger.debug("第"+i+"行");
							UploadStock uploadStock=new UploadStock();
							Product product = new Product();// 商品
							List<ProductAttributeValue> attrbuteValueList=new ArrayList<ProductAttributeValue>();//属性值列表
							ProductStock productStock = new ProductStock();// 库存
							List<String> imageList = new ArrayList<String>();
							for (int j = 0; j < list.get(i).size(); j++) {// 每一行一格一格的读
								if (j == 0) {// 第一格获取商品
									if (list.get(i).get(j) !=null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).trim().length()<=120){
											product.setName(list.get(i).get(j).trim());// 商品名称
											product.setStoreId(storeId);//商店ID
											product.setAccountId(accountId);//创建者
											product.setCategoryId(categoryId);//分类ID
										}else{
											flag += "第" + (i+1) + "行的第1格的商品长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第1格的商品为空,请检查！</br>";
										return flag;
									}
								}else if(j==1){// 第一格获取库存
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).trim().length()<=9){
											logger.debug("库存===="+list.get(i).get(j).trim());
											if(list.get(i).get(j).trim().matches("[0-9]+")){
												Integer stock=Integer.parseInt(list.get(i).get(j).trim());
												if(stock>=0){
													productStock.setStock(stock.doubleValue());
												}else{
													flag += "第" + (i+1) + "行的第2格的库存量必须必须大于或等于0,请检查！</br>";
													return flag;
												}
											}else{
												flag += "第" + (i+1) + "行的第2格的库存量必须为整数,请检查！</br>";
												return flag;
											}
											
										}else{
											flag += "第" + (i+1) + "行的第2格的库存量长度过长,请检查！</br>";
											return flag;
										}
									} else {
										productStock.setStock(-99d);
									}
								}else if(j==2){//警报值
									if (list.get(i).get(j) != null) {
										if(list.get(i).get(j).length()<=9){
											if(list.get(i).get(j).trim().matches("[0-9]+")){
												Integer alarmValue=Integer.parseInt(list.get(i).get(j).trim());
												if(alarmValue>=0){
													productStock.setAlarmValue(alarmValue.doubleValue());
												}else{
													flag += "第" + (i+1) + "行的第3格的警报值必须大于或等于0,请检查！</br>";
													return flag;
												}
											}else{
												flag += "第" + (i+1) + "行的第3格的警报值必须为整数,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第3格的警报值长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第3格的警报值不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==3){//销售单价
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<=10){
											if(NumberUtils.isNumber(list.get(i).get(j).trim())){
												if(list.get(i).get(j).trim().indexOf(".")>0){
													if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
														Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
														if(d.doubleValue()>=0){
															productStock.setPrice(Double.valueOf(list.get(i).get(j).trim()));
														}else{
															flag += "第" + (i+1) + "行的第4格的销售单价必须大于或等于0,请检查！</br>";
															return flag;
														}
													}else{
														flag += "第" + (i+1) + "行的第4格的销售单价的小数位数不能大于2位,请检查！</br>";
														return flag;
													}
												}else{
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														productStock.setPrice(Double.valueOf(list.get(i).get(j).trim()));
													}else{
														flag += "第" + (i+1) + "行的第4格的销售单价必须大于或等于0,请检查！</br>";
														return flag;
													}
												}
											}else{
												flag += "第" + (i+1) + "行的第4格的销售单价必须为数字,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第4格的销售单价长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第4格的销售单价不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==4){//会员价
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<=10){
											if(NumberUtils.isNumber(list.get(i).get(j).trim())){
												if(list.get(i).get(j).trim().indexOf(".")>0){
													if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
														Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
														if(d.doubleValue()>=0){
															productStock.setMemberPrice(Double.valueOf(list.get(i).get(j).trim()));
														}else{
															flag += "第" + (i+1) + "行的第5格的销售单价必须大于或等于0,请检查！</br>";
															return flag;
														}
													}else{
														flag += "第" + (i+1) + "行的第5格的销售单价的小数位数不能大于2位,请检查！</br>";
														return flag;
													}
												}else{
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														productStock.setMemberPrice(Double.valueOf(list.get(i).get(j).trim()));
													}else{
														flag += "第" + (i+1) + "行的第5格的销售单价必须大于或等于0,请检查！</br>";
														return flag;
													}
												}
											}else{
												flag += "第" + (i+1) + "行的第5格的销售单价必须为数字,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第5格的销售单价长度过长,请检查！</br>";
											return flag;
										}
									}
								}else if(j==5){//市场价格
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<=10 ){
											if(NumberUtils.isNumber(list.get(i).get(j).trim())){
												if(list.get(i).get(j).trim().indexOf(".")>0){
													if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
														Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
														if(d.doubleValue()>=0){
															productStock.setMarketPrice(Double.valueOf(list.get(i).get(j).trim()));
														}else{
															flag += "第" + (i+1) + "行的第6格的市场价格必须大于或等于0,请检查！</br>";
															return flag;
														}
													}else{
														flag += "第" + (i+1) + "行的第6格的市场价格的小数位数不能大于2位,请检查！</br>";
														return flag;
													}
												}else{
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														productStock.setMarketPrice(Double.valueOf(list.get(i).get(j).trim()));
													}else{
														flag += "第" + (i+1) + "行的第6格的市场价格必须大于或等于0,请检查！</br>";
														return flag;
													}
												}
											}else{
												flag += "第" + (i+1) + "行的第6格的市场价格必须为数字,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第6格的市场价格长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第6格的市场价格不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==6){//成本单价
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<=10){
											if(NumberUtils.isNumber(list.get(i).get(j).trim())){
												if(list.get(i).get(j).trim().indexOf(".")>0){
													if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
														Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
														if(d.doubleValue()>=0){
															productStock.setCostPrice(Double.valueOf(list.get(i).get(j).trim()));
														}else{
															flag += "第" + (i+1) + "行的第7格的成本单价必须大于或等于0,请检查！</br>";
															return flag;
														}
													}else{
														flag += "第" + (i+1) + "行的第7格的成本单价的小数位数不能大于2位,请检查！</br>";
														return flag;
													}
												}else{
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														productStock.setCostPrice(Double.valueOf(list.get(i).get(j).trim()));
													}else{
														flag += "第" + (i+1) + "行的第7格的成本单价必须大于或等于0,请检查！</br>";
														return flag;
													}
												}
											}else{
												flag += "第" + (i+1) + "行的第7格的成本单价必须为数字,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第7格的成本单价长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第7格的成本单价不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==7){//商品条码
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<=18){
											//列表中找
											String barCode="";
											if(NumberUtils.isNumber(list.get(i).get(j).trim())){
												barCode=list.get(i).get(j).trim();
											}else{
												flag += "第" + (i+1) + "行的商品条码只能为数字,请检查！</br>";
												return flag;
											}
											if(allList.size()>0){
												int sort=0;
												boolean barCodeFlag=false;
												for(int k=0;k<allList.size();k++){
													if(barCode.equals(allList.get(k).getProductStock().getBarCode()) && !product.getName().equals(allList.get(k).getProduct().getName())){//不同商品有相同条形码
														sort=k;
														barCodeFlag=true;
														break;
													}
												}
												if(barCodeFlag==false){//不同商品没有相同条形码
													//去数据库判断
													// 当前店铺不同商品有此条形码
													logger.debug("barCode={},storeId={},productName={},categoryId={}",barCode,storeId,product.getName(),categoryId);
													if(!productStockService.byBarCodeFindProductStock1(barCode,storeId,product.getName(),categoryId).isEmpty()){
														flag += "第" + (i+1) + "行的商品条码和数据库不同商品的商品条码相同,请检查！</br>";
														return flag;
													}else{//当前店铺不同商品没有此条形码
														productStock.setBarCode(barCode);//加进List
													}
												}else{//不同商品有相同条形码
													flag += "第" + (i+1) + "行的商品条码和前面第"+(sort+2)+"行是不同商品,商品条码不能相同,请检查！</br>";
													return flag;
												}
											}else{
												//去数据库判断
												// 当前店铺有此条形码
												logger.debug("barCode={},storeId={},productName={},categoryId={}",barCode,storeId,product.getName(),categoryId);
												if(!productStockService.byBarCodeFindProductStock1(barCode,storeId,product.getName(),categoryId).isEmpty()){
													flag += "第" + (i+1) + "行的商品条码和数据库不同商品的商品条码相同,请检查！</br>";
													return flag;
												}else{//当前店铺没有此条形码
													productStock.setBarCode(barCode);//加进List
												}
											}
											
										}else{
											flag += "第" + (i+1) + "行的第8格的商品条码长度过长,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第8格的商品条码不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==8){//备注
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<125){
											productStock.setRemarks(list.get(i).get(j).trim());
										}else{
											flag += "第" + (i+1) + "行的第9格的备注长度过长,请检查！</br>";
											return flag;
										}
									}
								}else if(j==9){//单位名称
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if(list.get(i).get(j).length()<125){
											productStock.setUnitName(list.get(i).get(j).trim());
										}else{
											flag += "第" + (i+1) + "行的第10格的单位名称长度过长,请检查！</br>";
											return flag;
										}
									}
								}else if(j==10){//是否上架
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if("是".equals(list.get(i).get(j).trim())){
											productStock.setShelves(0);//上架
										}else if("否".equals(list.get(i).get(j).trim())){
											productStock.setShelves(1);//下架
										}else{
											flag += "第" + (i+1) + "行的第11格的是否上架必须填写是或否,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第11格是否上架不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==11){//图片路径
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										BufferedImage bi = null; 
										String imageId;
										String path = _path + File.separator + list.get(i).get(j).trim();// image/aa
										logger.debug("path==="+path);
										File file = new File(path);
										File[] tempList = file.listFiles();
										if(tempList !=null && tempList.length>0 ){
											for (int k = 0; k < tempList.length; k++) {
												if (tempList[k].isFile()) {
													 bi = ImageIO.read(tempList[k]);
													 if(bi!=null){
														 int width = bi.getWidth(); // 像素  
														 int height = bi.getHeight(); // 像素 
														 logger.debug("width={},height={}",width,height);
														 if(width<330 || height<220 || width*220!=height*330){
															 logger.debug("width1={},height1={}",width,height);
															 flag += "第" + (i+1) + "行的第12格的图片比例不对,请检查！</br>";
															 return flag;
														 }else{
															 InputStream is = new FileInputStream(tempList[k]);
															 imageId = fileService.save(is);// tempList[k].toString()
															 is.close();
															 
															 imageList.add(imageId+ ","+ FileNameUtil.getName(tempList[k].toString()));
														 }
													 }else{
														flag += "第" + (i+1) + "行的第12格的图片不正确,请检查！</br>";
														return flag;
													 }
												}
											}
										}else{
											flag += "第" + (i+1) + "行的第12格的图片路径不正确或该商品图片不存在,请检查！</br>";
											return flag;
										}
									}else{
										flag += "第" + (i+1) + "行的第12格的图片路径不能为空,请检查！</br>";
										return flag;
									}
								}else if(j==12){//所属品牌
									String brandName=null;
									logger.debug("brandName={}",list.get(i).get(j));
									if(list.get(i).get(j)!=null){
										brandName=list.get(i).get(j).trim();
									}
									product.setBrandName(brandName);
								}else if(j==13){//是否有属性
									if (list.get(i).get(j) != null && !list.get(i).get(j).equals("")) {
										if("是".equals(list.get(i).get(j).trim())){
											product.setType(0);//有属性
										}else if("否".equals(list.get(i).get(j).trim())){
											product.setType(1);//无属性
										}else{
											flag += "第" + (i+1) + "行的第14格的是否有属性必须填写是或否,请检查！</br>";
											return flag;
										}
											
									}else{
											flag += "第" + (i+1) + "行的第14格的是否有属性必须填写是或否,请检查！</br>";
											return flag;
									}
								}else if(j>13){
									logger.debug("属性值={}",list.get(i).get(j));
									if(product.getType()==0){//type=0表有属性，属性值不能为空
										if (list.get(i).get(j)!=null && !list.get(i).get(j).equals("")) {// 属性值不为空
											if(list.get(i).get(j).trim().length()<=120){
												
												ProductAttributeValue productAttributeValue = new ProductAttributeValue();// 商品属性值
												productAttributeValue.setValue(list.get(i).get(j).trim());// 属性值
												
												attrbuteValueList.add(productAttributeValue);
												
											}else{
												flag += "第" + (i+1) + "行的第" + (j+1) + "格的属性值长度过长,请检查！</br>";
												return flag;
											}
										}else{
											flag += "第" + (i+1) + "行的第" + (j+1) + "格的属性值不能为空,请检查！</br>";
											return flag;
										}
									}
								}
							//结束

							}
							logger.debug("type=="+product.getType());
							if(product.getType()==0){//type=0表有属性，属性值不能为空
								if(attrbuteValueList.isEmpty()){//属性值为空
									flag += "第" + (i+1) + "行的属性值不能为空,请检查！</br>";
									return flag;
									
								}else{
									if(attrbuteValueList.size()!=attributeList.size()){
										flag += "第" + (i+1) + "行的属性值填写不对,请检查！</br>";
										return flag;
									}
									//列表中找有没有商品名称相同，属性值相同的数据
									if(allList.size()>0){
										int n=0;
										boolean _flag = false;
										int brandFlag=1;
										for(int k=0;k<allList.size();k++){
											if(product.getName().equals(allList.get(k).getProduct().getName())){//商品名称相同
												if((product.getBrandName()==null && allList.get(k).getProduct().getBrandName()!=null) || (product.getBrandName()!=null && allList.get(k).getProduct().getBrandName()==null)){
													n=k;
													_flag=true;
													brandFlag=1;
												}else if(product.getBrandName()!=null && allList.get(k).getProduct().getBrandName()!=null){
													if(!product.getBrandName().equals(allList.get(k).getProduct().getBrandName())){//(判断是否属于同一品牌)
														n=k;
														_flag=true;
														brandFlag=1;
													}
												}
												if(!_flag){
													logger.debug("attrbuteValueList={},allList.get(k).getAttrbuteValueList()={}",attrbuteValueList,allList.get(k).getAttrbuteValueList());
													if(CollectionUtils.isEqualCollection(attrbuteValueList,allList.get(k).getAttrbuteValueList())){//比较两个属性值list对象是否一致
														n=k;
														_flag=true;
														brandFlag=0;
													}
												}
											}
											if(_flag){
												break;
											}
										}
										if(_flag && brandFlag==1){
											flag += "第" + (i+1) + "行商品的品牌和前面的第"+(n+2)+"行同一商品的品牌不一致,请检查！</br>";
											return flag;
										}else if(_flag && brandFlag==0){
											flag += "第" + (i+1) + "行的属性值和前面的第"+(n+2)+"行属性值一致且为同一商品,请检查！</br>";
											return flag;
											
										}else{
											Product p=productStockService.findBrandByProduct(product);
											if(p!=null){
												logger.debug("p.brandName()={},product.getBrandName={}",p.getBrandName(),product.getBrandName());
												if((product.getBrandName()==null && p.getBrandName()!=null) || (product.getBrandName()!=null && p.getBrandName()==null)){
													flag += "第" + (i+1) + "行的商品和数据库里所属品牌不一致,请检查！</br>";
													return flag;
												}else if(p.getBrandName()!=null && product.getBrandName()!=null){
													if(!p.getBrandName().equals(product.getBrandName())){//数据库中的商品一样，所属品牌不一样
														flag += "第" + (i+1) + "行的商品和数据库里所属品牌不一致,请检查！</br>";
														return flag;
													}
												}
												
											}else{
												boolean _flag1 = false;
												//去数据库找
												List<ProductStock> psList=productStockService.findProductList(product.getName(),product.getType(),product.getCategoryId(),product.getStoreId());
												if(!psList.isEmpty()){
													for(int k=0;k<psList.size();k++){
														if(CollectionUtils.isEqualCollection(attrbuteValueList,psList.get(k).getPavList())){//比较两个属性值list对象是否一致
															_flag1=true;
														}
														if(_flag1){
															break;
														}
													}
													
												}else{//看有没有商品名称相同的无属性库存
													if(!productStockService.findProductList(product.getName(),1,product.getCategoryId(),product.getStoreId()).isEmpty()){
														_flag1=true;
													}
												}
												if(_flag1){
													flag += "第" + (i+1) + "行的商品在数据库已存在,请检查！</br>";
													return flag;
												}	
											}
										}
										
									}else{
										Product p=productStockService.findBrandByProduct(product);
										if(p!=null){
											if((product.getBrandName()==null && product.getBrandName()!=null) || (product.getBrandName()!=null && product.getBrandName()==null)){
												flag += "第" + (i+1) + "行的商品和数据库里所属品牌不一致,请检查！</br>";
												return flag;
											}else if(product.getBrandName()!=null && product.getBrandName()!=null){
												if(!p.getBrandName().equals(product.getBrandName())){//数据库中的商品一样，所属品牌不一样
													flag += "第" + (i+1) + "行的商品和数据库里所属品牌不一致,请检查！</br>";
													return flag;
												}
											}
										}else{
											boolean _flag2 = false;
											//去数据库找
											logger.debug("product.getName()={},product.getType()={},product.getCategoryId()={},product.getStoreId()={}",product.getName(),product.getType(),product.getCategoryId(),product.getStoreId());
											List<ProductStock> psList=productStockService.findProductList(product.getName(),product.getType(),product.getCategoryId(),product.getStoreId());
											logger.debug("psList={}",psList);
											if(!psList.isEmpty()){
												for(int k=0;k<psList.size();k++){
													
													if(CollectionUtils.isEqualCollection(attrbuteValueList,psList.get(k).getPavList())){//比较两个属性值list对象是否一致
														_flag2=true;
													}
													if(_flag2){
														break;
													}
												}
												
											}else{//看有没有商品名称相同的无属性库存
												if(!productStockService.findProductList(product.getName(),1,product.getCategoryId(),product.getStoreId()).isEmpty()){
													_flag2=true;
												}
											}
											if(_flag2){
												flag += "第" + (i+1) + "行的商品在数据库已存在,请检查！</br>";
												return flag;
											}	
										}
									}
									
								}
							}else{
								//列表中找
								if(allList.size()>0){
									int sort=0;
									boolean typeFlag=false;
									for(int k=0;k<allList.size();k++){
										//List里有没有相同商品且是无属性的
										if(product.getName().equals(allList.get(k).getProduct().getName())){
											sort=k;
											typeFlag=true;
											break;
										}
									}
									if(typeFlag==false){//List里没有相同商品且是无属性的
										//去数据库判断
										// 当前店铺有此商品且是无属性的
										if(productService.findProduct1(product)!=null){
											flag += "第" + (i+1) + "行的商品在数据库已存在,请检查！</br>";
											return flag;
										}
									}else{//List里有相同商品且是无属性的
										flag += "第" + (i+1) + "行的商品和前面"+(sort+2)+"行是相同商品,请检查！</br>";
										return flag;
									}
								}else{
									//去数据库判断
									// 当前店铺有此商品且是无属性的
									if(productService.findProduct1(product)!=null){
										flag += "第" + (i+1) + "行的商品在数据库已存在,请检查！</br>";
										return flag;
									}
								}
							}
							logger.debug("product={}",product);
							uploadStock.setProduct(product);
							uploadStock.setImageList(imageList);
							uploadStock.setProductStock(productStock);
							uploadStock.setAttrbuteValueList(attrbuteValueList);
							allList.add(uploadStock);
						}
					}
					if("".equals(flag)){
						for(int k=0;k<allList.size();k++){
							ProductAttributeValue productAttributeValue = new ProductAttributeValue();// 商品属性值
							ProductStock productStock = new ProductStock();// 库存
							Long productId = 0L;// 商品ID
							String attributeCode = "";// 商品属性值ID以-连起来
							String attributeName = "";// 商品属性值以-连起来
							String attributeCodeNew="";
							ProductImage productImage = new ProductImage();// 商品图片
							
							
							
							//属性值操作开始
							if(allList.get(k).getProduct().getType().intValue()==0){//有属性
								for(int m=0;m<allList.get(k).getAttrbuteValueList().size();m++){//attributeList
									
									
									productAttributeValue.setValue(allList.get(k).getAttrbuteValueList().get(m).getValue());//属性值
									productAttributeValue.setAttributeId(attributeList.get(m).getId());//属性ID
									if(productAttributeValueService.findProductAttributeValue(productAttributeValue)==null){//数据库里属性值不存在
										productAttributeValue.setId(idService.getId());// 生成新属性值ID
										productAttributeValueService.edit(productAttributeValue);
										attributeCode += Long.toString(productAttributeValue.getId())+ "-";
										attributeName+="|"+productAttributeValue.getValue();
									}else{
										attributeCode += Long.toString(productAttributeValueService.findProductAttributeValue(productAttributeValue).getId())+ "-";
										attributeName+="|"+productAttributeValueService.findProductAttributeValue(productAttributeValue).getValue();
									}
								}
							}
							if(!"".equals(attributeCode)){
								String[] arr=attributeCode.split("-");
								Arrays.sort(arr);
								
								for(int i=0;i<arr.length;i++){
									attributeCodeNew+=arr[i]+"-";
								}
							}	
							//属性值操作结束
							
							// 如果数据库找不到分类下相同的商品时，把新商品存入数据库
							
							if (productService.findProduct(allList.get(k).getProduct())==null ) {
								if(allList.get(k).getProduct().getBrandName()!=null && !"".equals(allList.get(k).getProduct().getBrandName())){
									
									Brand brand=new Brand();
									logger.debug("brandName={},storeId={}",allList.get(k).getProduct().getBrandName(),allList.get(k).getProduct().getStoreId());
									brand.setName(allList.get(k).getProduct().getBrandName());//品牌名称00
									brand.setStoreId(allList.get(k).getProduct().getStoreId());//店铺Id
									if(productBrandService.findProductBrand(brand)==null){
										flag = "第" + (k+1) + "行的所输的品牌不存在,请检查！";
										return flag;
									}else{
										allList.get(k).getProduct().setBrandId(productBrandService.findProductBrand(brand).getId());
									}
								}
								productStockService.editProduct(allList.get(k).getProduct());
							}
							productId = productService.findProduct(allList.get(k).getProduct()).getId();// 商品ID
							
							//库存操作开始
							// 先判断每一行在库存中是否已存在，已存在，修改库存，不存在，新增
							// 通过每一行的属性值组成attributeCode去库存中查
							if(allList.get(k).getProduct().getType().intValue()==0){//有属性
								productStock.setAttributeCode(attributeCodeNew.substring(0,attributeCodeNew.length() - 1));
								productStock.setAttributeName(allList.get(k).getProduct().getName()+attributeName);
								productStock.setType(0);
							}else{
								productStock.setAttributeName(allList.get(k).getProduct().getName());
								productStock.setType(1);//商家分类
							}
							productStock.setStoreId(storeId);// 登录用户店铺ID
							
							productStock.setId(idService.getId());// 生成库存ID
							productStock.setStock(allList.get(k).getProductStock().getStock());// 库存数量
							productStock.setAlarmValue(allList.get(k).getProductStock().getAlarmValue());// 警报值
							productStock.setPrice(allList.get(k).getProductStock().getPrice());// 价格
							productStock.setMemberPrice(allList.get(k).getProductStock().getMemberPrice());// 会员格
							productStock.setMarketPrice(allList.get(k).getProductStock().getMarketPrice());//市场价
							productStock.setCostPrice(allList.get(k).getProductStock().getCostPrice());//成本价
							productStock.setShelves(allList.get(k).getProductStock().getShelves());//是否上架
							if(allList.get(k).getProductStock().getBarCode().matches("[\\d]+\\.[\\d]+")){
								productStock.setBarCode(allList.get(k).getProductStock().getBarCode().substring(0, allList.get(k).getProductStock().getBarCode().lastIndexOf(".")));// 条形码
							}else{
								productStock.setBarCode(allList.get(k).getProductStock().getBarCode());// 条形码
							}
							productStock.setRemarks(allList.get(k).getProductStock().getRemarks());// 备注
							productStock.setUnitName(allList.get(k).getProductStock().getUnitName());// 单位名称
							productStock.setAccountId(accountId);// 登录用户ID
							productStock.setProductId(productId);// 商品ID
							
							
							
							productStockService.save(productStock);
							
							for (int j = 0; j < allList.get(k).getImageList().size(); j++) {
								productImage.setProductStockId(productStock.getId());// 库存ID
								//productImage.setSort(Integer.parseInt(allList.get(k).getImageList().get(j).split(",")[1].substring(0,allList.get(k).getImageList().get(j).split(",")[1].lastIndexOf("."))));// 顺序
								String id = allList.get(k).getImageList().get(j).split(",")[0];// 图片ID
								String imageSuffix = allList.get(k).getImageList().get(j).split(",")[1].substring(allList.get(k).getImageList().get(j).split(",")[1].lastIndexOf(".") + 1, allList.get(k).getImageList().get(j).split(",")[1].length());// 图片后缀
								productImage.setId(id);
								productImage.setSuffix(imageSuffix);
								productStockService.saveProductImage(productImage);
							}
							flag="1";
						}
					}
				}
					
			
			
		} catch (Exception e) {
			logger.error("", e);
			//throw new RuntimeException(e);
			flag= "导入失败，请联系客服！";
		}
		return flag; 

	}
	
	@Transactional
	public String saveImage(MultipartFile[] imageFile, Long productStockId) {
		String flag="success";
		ProductImage productImage=new ProductImage();
		InputStream in=null;
		for(int i=0;i<imageFile.length;i++){
	    	if(StringUtils.isNotBlank(imageFile[i].getOriginalFilename())){
	    		productImage.setProductStockId(productStockId);//库存ID
	    		productImage.setSort(Integer.parseInt(imageFile[i].getOriginalFilename().substring(0, imageFile[i].getOriginalFilename().lastIndexOf("."))));//顺序
	    		if ((productStockService.findProductImage(productImage) == null ? "0": "1").equals("0")) {
					try {
						in = imageFile[i].getInputStream();
						String id = fileService.save(in);//图片ID
						productImage.setId(id);
						//图片后缀
						String suffix=imageFile[i].getOriginalFilename().substring(imageFile[i].getOriginalFilename().lastIndexOf(".")+1, imageFile[i].getOriginalFilename().length());
						productImage.setSuffix(suffix);
						productStockService.saveProductImage(productImage);
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						try {
							in.close();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
	    		}else{
	    			flag=imageFile[i].getOriginalFilename().substring(0, imageFile[i].getOriginalFilename().lastIndexOf("."));
	    		}
	    	}
	    }
		
		return flag;
	}

	

	//可以用异常来做校验/**  * 判断字符串是否是整数  */ 
		public static boolean isInteger(String value) {
			try { 
				Integer.parseInt(value);  
				return true;
				} catch (NumberFormatException e) { 
					return false; 
				} 
		} 
}
