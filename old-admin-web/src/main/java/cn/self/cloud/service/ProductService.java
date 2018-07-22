package cn.self.cloud.service;

import java.util.List;

import cn.self.cloud.bean.Product;
import cn.self.cloud.commonutils.page.Page;
import cn.self.cloud.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	/**
	 * 
	 * @return 参看所有商品
	 */
	public Page findAll(String name, Page page) {
		return productDao.findAll(name, page);
	}

	public void update(Product product) {
		productDao.update(product);
	}

	public void save(Product product) {
		productDao.save(product);
	}

	public Product findById(Long id) {
		return productDao.findById(id);
	}


	public void deleteById(Product product) {
		productDao.deleteById(product);
	}
	
	public List<Product> findByAccountId(Product product){
		return productDao.findByAccountId(product);
	}

	public Product findProduct(Product product) {
		return productDao.findProduct(product);
	}

	public List<Product> productList(Long categoryId) {
		return productDao.productList(categoryId);
	}
}
