package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.dao.ProductImageDao;
import cn.lastmiles.utils.FileServiceUtils;


@Service
public class ProductImageService {
	@Autowired
	private  ProductImageDao productImageDao;
	
	/**
	 * 通过productId 查找分类的第一张图片
	 * @param productId
	 * @return
	 */
	public ProductImage findByProductId(Long productId){
		return productImageDao.findByProductId(productId);
	}
	/**
	 * 通过productId 
	 * @param productId
	 * @return 返回图片地址列表
	 */
	public List<String> findImgUrlByProductId(Long productId){
		List<ProductImage> ProductImages = productImageDao.findListByProductId(productId);
		List<String> imgUrls = new ArrayList<String>();
		if (ProductImages!=null) {
			for (ProductImage productImage : ProductImages) {
				if (productImage!=null&&productImage.getId()!=null) {
					imgUrls.add(FileServiceUtils.getFileUrl(productImage.getId()));		
				}
			}
		}
		return imgUrls;
	}
	/**
	 * 通过productId 
	 * @param productId
	 * @return 返回图片地址列表
	 */
	public List<String> findImgUrlByStockId(Long stockId){
		List<ProductImage> ProductImages = productImageDao.findImgUrlByStockId(stockId);
		List<String> imgUrls = new ArrayList<String>();
		if (ProductImages!=null) {
			for (ProductImage productImage : ProductImages) {
				if (productImage!=null&&productImage.getId()!=null) {
					imgUrls.add(FileServiceUtils.getFileUrl(productImage.getId()));		
				}
			}
		}
		return imgUrls;
	}

	public Boolean addImage(ProductImage pi) {
		return productImageDao.addImage(pi);
	}

	public List<ProductImage> getByProductStockID(Long id) {
		return productImageDao.getByProductStockID(id);
	}

	public Boolean deleteByProductStockID(Long id) {
		return productImageDao.deleteByProductStockID(id);
	}
	
	
}
