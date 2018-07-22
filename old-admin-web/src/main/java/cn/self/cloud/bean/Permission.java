package cn.self.cloud.bean;

import java.io.Serializable;

public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2211538284111985039L;
	
	private Long id;
	// 操作
	private String operator;
	// 操作说明
	private String desc;
	
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// 权限
	public static enum DefaultPermission {
		ACCOUNTADD("accountAdd", "添加帐号"), ACCOUNTDELETE("accountDelete", "删除帐号"), ACCOUNTEDIT(
				"accountEdit", "修改帐号"), ACCOUNTLIST("accountList", "查询帐号"),

		USERADD("userAdd", "添加会员"), USERDELETE("userDelete", "删除会员"), USEREDIT(
				"userEdit", "修改会员"), USERLIST("userList", "查询会员"),

		SETDISCOUNT("setDiscount", "设置会员折扣"),

		STOREADD("storeAdd", "添加商店"), STOREDELETE("storeDelete", "删除商店"), STOREEDIT(
				"storeEdit", "修改商店"), STORELIST("storeList", "查询商店"),

		PRODUCTCATEGORYADD("productCategoryAdd", "添加商品分类"), PRODUCTCATEGORYDELETE(
				"productCategoryDelete", "删除商品分类"), PRODUCTCATEGORYEDIT(
				"productCategoryEdit", "修改商品分类"), PRODUCTCATEGORYLIST(
				"productCategoryList", "查询商品分类"),

		PRODUCTADD("productAdd", "添加商品"), PRODUCTDELETE("productDelete", "删除商品"), PRODUCTEDIT(
				"productEdit", "修改商品"), PRODUCTLIST("productList", "查询商品"),

		PRODUCTATTRIBUTEADD("productAttributeAdd", "添加商品属性"), PRODUCTATTRIBUTEDELETE(
				"productAttributeDelete", "删除商品属性"), PRODUCTATTRIBUTEEDIT(
				"productAttributeEdit", "修改商品属性"), PRODUCTATTRIBUTELIST(
				"productAttributeList", "查询商品属性"),

		PRODUCTATTRIBUTEVALUEADD("productAttributeValueAdd", "添加商品属性值"), PRODUCTATTRIBUTEVALUEDELETE(
				"productAttributeValueDelete", "删除商品属性值"), PRODUCTATTRIBUTEVALUEEDIT(
				"productAttributeValueEdit", "修改商品属性值"), PRODUCTATTRIBUTEVALUELIST(
				"productAttributeValueList", "查询商品属性值"),

		PRODUCTSTOCKADD("productStockAdd", "添加库存"), PRODUCTSTOCKDELETE(
				"productStockDelete", "删除库存"), PRODUCTSTOCKEDIT(
				"productStockEdit", "修改库存"), PRODUCTSTOCKLIST(
				"productStockList", "查询库存"),

		PRODUCTIMPORT("productImport", "商品导入"),

		REPORT("report", "统计报表"),
		
		ORDERLIST("orderList","订单列表"),

		ROLEADD("roleAdd", "添加角色"), ROLEDELETE("roleDelete", "删除角色"), ROLEEDIT(
				"roleEdit", "修改角色"), ROLELIST("roleList", "查询角色"), ROLEPERMISSION(
				"rolePermission", "角色授权");
		
		// TODO

		private String operator;
		private String desc;

		private DefaultPermission(String operator, String desc) {
			this.operator = operator;
			this.desc = desc;
		}

		public String getDesc() {
			return this.desc;
		}

		public String getOperator() {
			return operator;
		}
	}
}
