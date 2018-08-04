package cn.lastmiles.bean;

import java.util.List;

public class UploadStock {
	private ProductCategory productCategory;
	private Product product;
	private List<ProductAttribute> attrbuteList;
	private List<ProductAttributeValue> attrbuteValueList;
	private ProductStock productStock;
	private List<String> imageList;
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public List<ProductAttribute> getAttrbuteList() {
		return attrbuteList;
	}
	public void setAttrbuteList(List<ProductAttribute> attrbuteList) {
		this.attrbuteList = attrbuteList;
	}
	public List<ProductAttributeValue> getAttrbuteValueList() {
		return attrbuteValueList;
	}
	public void setAttrbuteValueList(List<ProductAttributeValue> attrbuteValueList) {
		this.attrbuteValueList = attrbuteValueList;
	}
	public ProductStock getProductStock() {
		return productStock;
	}
	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	
	
}
