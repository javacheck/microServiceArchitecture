package cn.lastmiles.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.FileUtils;
import cn.lastmiles.common.utils.NumberUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.ProductCategoryDao;
import cn.lastmiles.dao.ProductDao;
import cn.lastmiles.dao.ProductStockDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private PromotionService promotionService;

	@Autowired
	private ProductStockDao productStockDao;

	@Autowired
	private FileService fileService;

	@Autowired
	private IdService idService;

	@Autowired
	private ProductStockService productStockService;

	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 
	 * @return 参看所有商品
	 */
	public Page findAll(String storeIdString, Long sysCategoryId, Long storeCategoryId,
			String name, Page page) {
		return productDao.findAll(storeIdString, sysCategoryId, storeCategoryId,
				name, page);
	}

	public void update(Product product) {
		productDao.update(product);
	}

	public void save(Product product) {
		if (product.getId()==null) {
			product.setId(idService.getId());
		}
		productDao.save(product);
	}

	public Product findById(Long id) {
		return productDao.findById(id);
	}

	public Product findById(Long CategoryId, Long id) {
		return productDao.findById(CategoryId, id);
	}

	public void deleteById(Product product) {
		productDao.deleteById(product);
	}

	public void deleteById(Long id) {
		productDao.deleteById(id);
	}

	public List<ProductCategory> findByStoreId(Long storeId) {
		return productCategoryDao.findByStoreId(storeId);
	}

	public Product findProduct(Product product) {
		return productDao.findProduct(product);
	}
	public Product findProduct1(Product product) {
		return productDao.findProduct1(product);
	}
	public Product findProductByID(Product product) {
		return productDao.findProductByID(product);
	}

	public List<Product> productList(Long categoryId) {
		return productDao.productList(categoryId);
	}

	public List<Product> findByCategoryId(Long categoryId) {
		return productDao.findByCategoryId(categoryId);
	}
	public Page posFind(Long categoryId,Long storeId,Page page) {
		productDao.posFind(categoryId,storeId,page);
		List<Product> list = (List<Product>) page.getData();
		
//		addPicUrl(list);
		if (list != null){
			for (Product product : list) {
				if (product.getPicUrl() != null) {
					product.setPicUrl(FileServiceUtils.getFileUrl(product.getPicUrl()));// 文件服务器读取图片
				}else {
					product.setPicUrl("");
				}
			}
		}
		logger.debug("返回的商品列表是：{}",list);
		return page;
	}

	/**
	 * 为商品添加图片属性
	 * 
	 * @param product
	 * @return
	 */
	public Product addPicUrl(Product product) {
		ProductImage productImage = productImageService.findByProductId(product.getId());// 读取图片
		if (productImage == null || productImage.getId() == null) {// 判断是否有默认图片
			product.setPicUrl("");// 默认图片
		} else {
			product.setPicUrl(FileServiceUtils.getFileUrl(productImage.getId()));// 文件服务器读取图片
		}
		return product;
	}

	/**
	 * 为商品队列添加图片属性
	 * 
	 * @param products
	 * @return
	 */
	public List<Product> addPicUrl(List<Product> products) {
		List<Product> list = new ArrayList<Product>();
		if (products != null) {
			for (Product product : products) {
				if (product != null) {
					list.add(this.addPicUrl(product));
				}
			}
		}
		return list;
	}

	public Product findByStockId(Long stockId) {
		return productDao.findByStockId(stockId);
	}

	public List<Product> findByName(String getByName, Long storeId) {
		return productDao.findByName(getByName, storeId);
	}

	public List<Product> findByNameAndStoreId(String name, Long storeId) {
		return productDao.findByNameAndStoreId(name, storeId);
	}

	/**
	 * 获取商品列表
	 * 
	 * @param page
	 *            page对象
	 * @param userId
	 *            用户ID
	 * @param productId
	 *            商品ID 可为空，(为空查询此用户的所有商品)
	 * @param storeId
	 *            商家ID 可为空，(为空查询此用户的所有商铺商品)
	 * @param shelves
	 *            上架状态(0上架1下架) 可为空，(为空或非0,非1则查询缺货库存)
	 * @return page(ProductStock)
	 */
	public Page getProductInformation(Page page, Long storeId, Integer shelves) {
		return productDao.getProductInformation(page, storeId, shelves);
	}

	public ProductStock getProductInformation(Long stockId, Long storeId) {
		return productDao.getProductInformation(stockId, storeId);
	}
	
	/**
	 * 新增商品信息(加入事务管理,报错或失败则回滚数据)
	 * 
	 * @param userId
	 *            用户ID
	 * @param product
	 *            产品对象
	 * @param productStock
	 *            库存对象
	 * @param productImage
	 *            图片对象
	 * @return 是否新增成功
	 */
	@Transactional
	public Boolean addProductInformation(Long userId, Product product,
			ProductStock productStock, MultipartFile[] productImage) {
		product.setAccountId(userId);
		product.setType(1); // 设置为不关联属性和属性值(与之前的区别开)
		productDao.save(product);

		productStock.setAccountId(product.getAccountId());

		if (null == productStock.getRecommended()) {
			productStock.setRecommended(1); // 设为默认推荐
		}

		if (null == productStock.getShelves()) {
			productStock.setShelves(0); // 设为默认上架
		}

		if (null == productStock.getStock()) {
			productStock.setStock(0d);
		}
		productStock.setProductId(product.getId());
		productStock.setCreateId(userId);

		productStockDao.save(productStock);

		saveImage(productStock, productImage);
		return true;
	}

	/**
	 * 修改商品信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param product
	 *            产品对象
	 * @param productStock
	 *            库存对象
	 * @param productImage
	 *            图片对象
	 * @return 是否修改成功
	 */
	@Transactional
	public Boolean updateProductInformation(Long userId, Product product,
			ProductStock productStock, MultipartFile[] productImage) {
		Boolean flag = productDao.update(product, true);
		if (!flag) {
			return false;
		}

		ProductStock ps = productStockDao.getByProductId(product.getId());
		if (null != ps) {
			productStock.setId(ps.getId());
			productStock.setUpdateId(userId);

			if (null == productStock.getRecommended()) {
				productStock.setRecommended(1); // 设为默认推荐
			}

			if (null == productStock.getShelves()) {
				productStock.setShelves(0); // 设为默认上架
			}

			if (null == productStock.getStock()) {
				productStock.setStock(0d);
			}

			productStockDao.update(productStock, true);
		} else {
			return false;
		}
		List<ProductImage> piList = productImageService
				.getByProductStockID(productStock.getId());
		if (null != piList && piList.size() > 0) {
			for (ProductImage pi : piList) {
				fileService.delete(pi.getId());
			}
		}

		productImageService.deleteByProductStockID(ps.getId());
		saveImage(productStock, productImage);
		return true;
	}

	private void saveImage(ProductStock productStock,
			MultipartFile[] productImage) {
		if (null != productImage && productImage.length > 0) {
			int sort = 0;
			for (MultipartFile multipartFile : productImage) {
				sort++;
				try {
					String imageID = fileService.save(multipartFile
							.getInputStream());
					ProductImage pi = new ProductImage();
					pi.setId(imageID);
					pi.setProductStockId(productStock.getId());
					pi.setSuffix(FileUtils.getExtension(multipartFile
							.getOriginalFilename()));
					pi.setSort(sort);
					Boolean flag = productImageService.addImage(pi);
					if (!flag) {
						break;
					}
				} catch (IOException e) {
					throw new RuntimeException(e); // 抛出运行时异常,使其回滚
				}
			}
		}
	}

	/**
	 * 删除商品信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param productId
	 *            商品ID
	 * @return 是否删除成功
	 */
	@Transactional
	public Boolean deleteProductInformation(Long userId, Long productId) {
		productDao.deleteById(productId);
		ProductStock ps = productStockDao.getByProductId(productId);
		if (null != ps) {
			productStockDao.deleteById(ps.getId());
		} else {
			return false;
		}
		List<ProductImage> piList = productImageService.getByProductStockID(ps
				.getId());
		if (null != piList && piList.size() > 0) {
			for (ProductImage pi : piList) {
				fileService.delete(pi.getId());
			}
		}
		Boolean flag = productImageService.deleteByProductStockID(ps.getId());
		return flag;
	}

	public void editProduct(Long id, Long storeId, Long productId, String name,
			Long accountId, String barCode, String remarks, Integer shelves,
			Double price, Double memberPrice,Double marketPrice, Double costPrice, Double stock,
			Double alarmValue, MultipartFile[] image, String[] delImage,
			String attributeCode,String attributeName,Integer sort, String unitName,String textArea) {

		if (id == null) {// 新增

			// 库存
			ProductStock productStock = new ProductStock();
			productStock.setId(idService.getId());// 库存ID
			productStock.setStoreId(storeId);// 商家ID
			if (stock == null) {
				productStock.setStock(-99d);
			} else {
				productStock.setStock(stock);// 库存
			}
			productStock.setAccountId(accountId);// 创建者
			productStock.setBarCode(barCode);// 条码
			productStock.setRemarks(remarks);// 商品简介
			productStock.setAlarmValue(alarmValue);// 缺货提醒
			productStock.setProductId(productId);// 商品ID

			
			if(!"".equals(attributeCode)){
				String[] arr=attributeCode.split("-");
				Arrays.sort(arr);
				String attributeCodeNew="";
				for(int i=0;i<arr.length;i++){
					attributeCodeNew+=arr[i]+"-";
				}
				productStock.setAttributeCode(attributeCodeNew.substring(0,attributeCodeNew.length() - 1));//属性ID相连
				productStock.setAttributeName(attributeName);//属性值相连
				productStock.setType(0);
			}else{
				productStock.setAttributeName(name);
				productStock.setType(1);
			}
			
			productStock.setPrice(price);//销售单价
			productStock.setMemberPrice(memberPrice);//会员价
			productStock.setMarketPrice(marketPrice);//市场价格
			productStock.setCostPrice(costPrice);//成本单价


			
			productStock.setShelves(shelves);
			productStock.setSort(sort);
			productStock.setUnitName(unitName);//单位名称
			productStock.setDetails(textArea);// 商品详情
			productStockDao.save(productStock);
			// 商品图片
			ProductImage productImage = new ProductImage();
			InputStream in = null;
			for (int i = 0; i < image.length; i++) {
				if (StringUtils.isNotBlank(image[i].getOriginalFilename())) {
					productImage.setProductStockId(productStock.getId());// 库存ID
					// productImage.setSort(Integer.parseInt(image[i].getOriginalFilename().substring(0,
					// image[i].getOriginalFilename().lastIndexOf("."))));//顺序
					try {
						in = image[i].getInputStream();
						String imageId = fileService.save(in);// 图片ID
						productImage.setId(imageId);
						// 图片后缀
						String suffix = image[i]
								.getOriginalFilename()
								.substring(
										image[i].getOriginalFilename()
												.lastIndexOf(".") + 1,
										image[i].getOriginalFilename().length());
						productImage.setSuffix(suffix);
						productStockService.saveProductImage(productImage);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							in.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {// 修改
				// 库存
			ProductStock productStock = new ProductStock();
			productStock.setId(id);// 库存ID
			productStock.setStoreId(storeId);// 商家ID
			if (stock == null) {
				productStock.setStock(-99d);
			} else {
				productStock.setStock(stock);// 库存
			}
			productStock.setAccountId(accountId);// 创建者
			productStock.setBarCode(barCode);// 条码
			productStock.setRemarks(remarks);// 商品简介
			productStock.setAlarmValue(alarmValue);// 缺货提醒
			if(!"".equals(attributeCode)){
				String[] arr=attributeCode.split("-");
				Arrays.sort(arr);
				String attributeCodeNew="";
				for(int i=0;i<arr.length;i++){
					attributeCodeNew+=arr[i]+"-";
				}
				productStock.setAttributeCode(attributeCodeNew.substring(0,attributeCodeNew.length() - 1));//属性ID相连
				productStock.setType(0);
			}else{
				productStock.setAttributeName(name);
				productStock.setType(1);
			}
			productStock.setAttributeName(attributeName);//属性值相连
			productStock.setPrice(price);// 销售单价
			productStock.setMemberPrice(memberPrice);//会员价
			productStock.setMarketPrice(marketPrice);// 市场价格
			productStock.setCostPrice(costPrice);// 成本单价
			productStock.setShelves(shelves);
			productStock.setSort(sort);
			productStock.setUnitName(unitName);//单位名称
			productStock.setDetails(textArea);// 商品详情
			productStockDao.update(productStock);
			String delImaStr = "";
			if (delImage != null && delImage.length > 0) {
				for (int i = 0; i < delImage.length; i++) {
					delImaStr += "'" + delImage[i] + "'" + ",";
				}
				delImaStr = delImaStr.substring(0, delImaStr.lastIndexOf(","));
			}
			
			List<ProductImage> imageList = productStockDao
					.findDelProductImageList(productStock.getId(),
							delImaStr);
			if (imageList != null && imageList.size() > 0) {
				for (int i = 0; i < imageList.size(); i++) {
					productStockDao.delByImageId(imageList.get(i).getId());
					fileService.delete(imageList.get(i).getId());
				}
			}
			// 商品图片
			ProductImage productImage = new ProductImage();
			InputStream in = null;
			for (int i = 0; i < image.length; i++) {
				if (StringUtils.isNotBlank(image[i].getOriginalFilename())) {
					productImage.setProductStockId(productStock.getId());// 库存ID
					// productImage.setSort(Integer.parseInt(image[i].getOriginalFilename().substring(0,
					// image[i].getOriginalFilename().lastIndexOf("."))));//顺序
					try {
						in = image[i].getInputStream();
						String imageId = fileService.save(in);// 图片ID
						productImage.setId(imageId);
						// 图片后缀
						String suffix = image[i]
								.getOriginalFilename()
								.substring(
										image[i].getOriginalFilename()
												.lastIndexOf(".") + 1,
										image[i].getOriginalFilename().length());
						productImage.setSuffix(suffix);
						productStockService.saveProductImage(productImage);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							in.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	public Map<String, String> findProductImageList(Long productStockId) {
		List<ProductImage> productImageList = productStockDao
				.findProductImageList(productStockId);
		if (productImageList == null) {
			return null;
		} else {
			Map<String, String> cerUrlMap = new HashMap<String, String>();
			for (int i = 0; i < productImageList.size(); i++) {
				cerUrlMap.put(productImageList.get(i).getId(), FileServiceUtils
						.getFileUrl(productImageList.get(i).getId()));
			}
			return cerUrlMap;
		}

	}

	public Page AppFindList(Page page, Long categoryId,String name, Long storeId) {
		List<Product> returnProducts = new ArrayList<Product>();
		page=productDao.AppFindList(page,categoryId, name,storeId);
		@SuppressWarnings("unchecked")
		List<Product> products= (List<Product>) page.getData();
		for (Product product : products) {
			ProductStock productStock=productStockDao.appFindByProductId(product.getId());
			if (productStock!=null) {
				Promotion promotion =promotionService.appFindDiscount(productStock.getStoreId(),productStock.getId(),1);
				if (promotion!=null) {
					productStock.setPromotionPrice(NumberUtils.multiply(productStock.getPrice(), promotion.getDiscount(),0.1D));
					productStock.setPromotionNum(promotion.getTotal());
					productStock.setDiscount(promotion.getDiscount());
					productStock.setPromotionId(promotion.getId());
				}
				productStock.setImgUrlList(productImageService.findImgUrlByStockId(productStock.getId()));//添加图片
				product.setProductStock(productStock);
				returnProducts.add(product);
			}
		}
		this.addPicUrl(products);
		page.setData(returnProducts);
		return page;
	}

	public List<Product> findProductType(Product product) {
		return productDao.findProductType(product);
	}

	public List<Product> posFind(String name, Long storeId) {
		return productDao.posFind(name, storeId);
	}

	public void changeShelves(Long productId,int i) {
		productDao.changeShelves(productId,i);
		
	}

	public void deleteByproductStockId(long productStockId) {
		productDao.deleteByproductStockId(productStockId);
		
	}
	
	public boolean updateProductName(Long productID,String productName){
		return productDao.updateProductName(productID,productName);
	}
	public List<Product> findProductType1(Product product,Long id) {
		return productDao.findProductType1(product,id);
	}

	public void editProduct(Long id, Long storeId, Long productId, String name,
			Long accountId, String barCode, String remarks, Integer shelves,
			Double price, Double marketPrice, Double costPrice, Double stock,
			Double alarmValue, String imageIds, String attributeCode,
			String attributeName, Integer sort, String details) {
		if (id == null) {// 新增

			// 库存
			ProductStock productStock = new ProductStock();
			productStock.setId(idService.getId());// 库存ID
			productStock.setStoreId(storeId);// 商家ID
			if (stock == null) {
				productStock.setStock(-99d);
			} else {
				productStock.setStock(stock);// 库存
			}
			productStock.setAccountId(accountId);// 创建者
			productStock.setBarCode(barCode);// 条码
			productStock.setRemarks(remarks);// 商品简介
			productStock.setAlarmValue(alarmValue);// 缺货提醒
			productStock.setProductId(productId);// 商品ID

			if (productCategoryService.findById(
					productDao.findById(productId).getCategoryId())
					.getStoreId() == null) {
				productStock.setType(0);// 系统分类
			} else {
				productStock.setType(1);// 商家分类
			}
			if(!"".equals(attributeCode)){
				productStock.setAttributeCode(attributeCode);//属性ID相连
				productStock.setAttributeName(attributeName);//属性值相连
			}
			
			productStock.setPrice(price);//销售单价
			productStock.setMarketPrice(marketPrice);//市场价格
			productStock.setCostPrice(costPrice);//成本单价


			
			productStock.setShelves(shelves);
			productStock.setSort(sort);
			productStock.setDetails(details);// 商品详情
			productStockDao.save(productStock);
			// 商品图片
			String images[]=imageIds.split(",");
			ProductImage productImage = new ProductImage();
			for (int i = 0; i < images.length; i++) {
				productImage.setId(images[i]);
				productImage.setProductStockId(productStock.getId());// 库存ID
				productStockService.saveProductImage(productImage);
			}
		} else {// 修改
				// 库存
			ProductStock productStock = new ProductStock();
			productStock.setId(id);// 库存ID
			productStock.setStoreId(storeId);// 商家ID
			if (stock == null) {
				productStock.setStock(-99d);
			} else {
				productStock.setStock(stock);// 库存
			}
			productStock.setAccountId(accountId);// 创建者
			productStock.setBarCode(barCode);// 条码
			productStock.setRemarks(remarks);// 商品简介
			productStock.setAlarmValue(alarmValue);// 缺货提醒
			productStock.setAttributeCode(attributeCode);// 属性ID相连
			productStock.setAttributeName(attributeName);//属性值相连
			productStock.setPrice(price);// 销售单价
			productStock.setMarketPrice(marketPrice);// 市场价格
			productStock.setCostPrice(costPrice);// 成本单价
			productStock.setShelves(shelves);
			productStock.setSort(sort);
			productStock.setDetails(details);// 商品详情
			productStockDao.update(productStock);
			
			// 商品图片
			String images[]=imageIds.split(",");
			ProductImage productImage = new ProductImage();
			for (int i = 0; i < images.length; i++) {
				//判断是否已存在，不存在插入
				if(productStockDao.findImageByImageId(images[i])==null){
					productImage.setId(images[i]);
					productImage.setProductStockId(productStock.getId());// 库存ID
					productStockService.saveProductImage(productImage);
				}
			}
			

		}

		
	}

	public ProductStock findProductStockSum(String storeIdString) {
		return productStockService.findProductStockSum(storeIdString);
	}

	public List<Product> posFindById(Long id, Long storeId) {
		
		return productDao.posFindByProductId(id,storeId);
	}


	
}