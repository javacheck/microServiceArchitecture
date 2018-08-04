/**
 * createDate : 2016年7月27日下午4:44:44
 */
package cn.lastmiles.alipay.bean;

import com.google.gson.annotations.SerializedName;

public class GoodsDetail {
	/**
	 * <必填>
	 * 最大长度为32位
	 * 商品的编号
	 */
    @SerializedName("goods_id")
	private String goodsId;
	/**
	 * <可选>
	 * 最大长度为32位
	 * 支付宝定义的统一商品编号
	 */
    @SerializedName("alipay_goods_id")
	private String alipayGoodsId;
	/**
	 * <必填>
	 * 最大长度为256位
	 * 商品名称
	 */
    @SerializedName("goods_name")
	private String goodsName;
	/**
	 * <必填>
	 * 最大长度为10位
	 * 商品数量
	 */
	private Integer quantity;
	/**
	 * <必填>
	 * 最大长度为9位
	 * 商品单价，单位为元，精确到小数点后2位
	 */
	private Double price;
	/**
	 * <可选>
	 * 最大长度为24位
	 * 商品类目
	 */
    @SerializedName("goods_category")
	private String goodsCategory;
	/**
	 * <可选>
	 * 最大长度为1000位
	 * 商品描述信息
	 */
	private String body;
	/**
	 * <可选>
	 * 最大长度为400位
	 * 商品的展示地址
	 */
	private String showUrl;
	/**
	 * <可选>
	 * 最大长度为28位
	 * 商户操作员编号
	 */
	private String operatorId;
	/**
	 * <可选>
	 * 最大长度为32位
	 * 商户门店编号
	 */
	private String storeId;
	/**
	 * <可选>
	 * 最大长度为32位
	 * 商户机具终端编号
	 */
	private String terminalId;
	/**
	 * <可选>
	 * 最大长度为32位
	 * 支付宝的店铺编号
	 */
	private String alipayStoreId;
	
	public GoodsDetail() {
		super();
	}

    // 创建一个商品信息，参数含义分别为商品id（使用国标）、商品名称、商品价格（单位为分）、商品数量
    public static GoodsDetail newInstance(String goodsId, String goodsName, Double price, int quantity) {
        GoodsDetail info = new GoodsDetail();
        info.setGoodsId(goodsId);
        info.setGoodsName(goodsName);
        info.setPrice(price);
        info.setQuantity(quantity);
        return info;
    }
    
	public GoodsDetail(String goodsId, String goodsName, Integer quantity, Double price) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.quantity = quantity;
		this.price = price;
	}
	
	public GoodsDetail(String goodsId, String alipayGoodsId, String goodsName,
			Integer quantity, Double price, String goodsCategory, String body,
			String showUrl, String operatorId, String storeId,
			String terminalId, String alipayStoreId) {
		super();
		this.goodsId = goodsId;
		this.alipayGoodsId = alipayGoodsId;
		this.goodsName = goodsName;
		this.quantity = quantity;
		this.price = price;
		this.goodsCategory = goodsCategory;
		this.body = body;
		this.showUrl = showUrl;
		this.operatorId = operatorId;
		this.storeId = storeId;
		this.terminalId = terminalId;
		this.alipayStoreId = alipayStoreId;
	}

	public String getGoodsId() {
		return goodsId;
	}
	
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
	public String getAlipayGoodsId() {
		return alipayGoodsId;
	}
	
	public void setAlipayGoodsId(String alipayGoodsId) {
		this.alipayGoodsId = alipayGoodsId;
	}
	
	public String getGoodsName() {
		return goodsName;
	}
	
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getGoodsCategory() {
		return goodsCategory;
	}
	
	public void setGoodsCategory(String goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getShowUrl() {
		return showUrl;
	}
	
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	
	public String getOperatorId() {
		return operatorId;
	}
	
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getStoreId() {
		return storeId;
	}
	
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public String getTerminalId() {
		return terminalId;
	}
	
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	public String getAlipayStoreId() {
		return alipayStoreId;
	}
	
	public void setAlipayStoreId(String alipayStoreId) {
		this.alipayStoreId = alipayStoreId;
	}

	@Override
	public String toString() {
		return "GoodsDetail [goodsId=" + goodsId + ", alipayGoodsId="
				+ alipayGoodsId + ", goodsName=" + goodsName + ", quantity="
				+ quantity + ", price=" + price + ", goodsCategory="
				+ goodsCategory + ", body=" + body + ", showUrl=" + showUrl
				+ ", operatorId=" + operatorId + ", storeId=" + storeId
				+ ", terminalId=" + terminalId + ", alipayStoreId="
				+ alipayStoreId + "]";
	}
	
}