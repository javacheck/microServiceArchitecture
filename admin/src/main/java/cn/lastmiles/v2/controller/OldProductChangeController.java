/**
 * createDate : 2016年7月7日下午5:34:51
 */
package cn.lastmiles.v2.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ProductStockAttributeValue;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.SpellHelper;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.service.ProductAttributeValueService;
import cn.lastmiles.service.ProductImageService;
import cn.lastmiles.v2.service.CommodityManagerService;

@Controller
@RequestMapping("hiddenProduct")
public class OldProductChangeController {
	private static final Logger logger = LoggerFactory.getLogger(OldProductChangeController.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private CommodityManagerService commodityManagerService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired 
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private IdService idService;
	
	@RequestMapping("/operation")
	public String operation(){
		long startTime = System.currentTimeMillis();
		logger.debug("转换开始....");
		
		// 待修改商品拼音码的集合
		List<Object[]> updateProductArr = new ArrayList<Object[]>();
		
		// 待修改的商品库存信息集合
		List<Object[]> updateProductStockArr = new ArrayList<Object[]>();
		
		// 待新增的规格属性
		List<Object[]> insertProductValueArr = new ArrayList<Object[]>();
		
		// 每一个库存对应的图片地址
		Map<Long,String> imgMap = new HashMap<Long, String>();
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList("select p.* from t_product p");
		
		List<Product> productList = BeanUtils.toList(Product.class, list);
		
		List<Long> readyUpdatePSValue = new ArrayList<Long>();
		if( null != productList && productList.size() > 0 ){
			for (Product product : productList) {
				Long productId = product.getId();
				String productName = product.getName();
				if( StringUtils.isNotBlank(productName) ){
					// 存储需要修改的商品信息
					Object[] pro = new Object[2];
					pro[0] = SpellHelper.getPingYin(productName); // sortName
					pro[1] = productId; // id
					updateProductArr.add(pro);					
				}
				
				List<ProductStock> productStockList = commodityManagerService.getProductStockListByProductId(productId);
				if( null != productStockList && productStockList.size() > 0 ){
					for (ProductStock productStock : productStockList) {
						// 如果商品库存表中有returnGoods和weighing字段了,则证明之前已经转换过了
//						if( null != productStock.getReturnGoods() 
//								&& null != productStock.getWeighing() ){
//							continue;
//						}
						
						Long productStockId = productStock.getId();
						
						readyUpdatePSValue.add(productStockId);
						
						String imageId = null;
						if( null == imgMap.get(productId) ){
							List<ProductImage> imageList = productImageService.getByProductStockID(productStockId);
							if( null != imageList && imageList.size() > 0 ){
								imageId = imageList.get(0).getId();
								imgMap.put(productId, imageId);
							}
						} else {
							imageId = imgMap.get(productId);
						}
						
						Long attributeId = null;
						if( StringUtils.isNotBlank(productStock.getAttributeCode()) ){
							String[] valueID = productStock.getAttributeCode().split("-");
							StringBuilder value = new StringBuilder();
							boolean flag = false;
							for (String valueId : valueID) {
								ProductAttributeValue pav = productAttributeValueService.findById( Long.parseLong(valueId) );
								String linkValue = null;
								if( null == pav ){
									logger.debug("id is {}",valueId);
									List<Map<String, Object>> stockValue = jdbcTemplate.queryForList("select * from t_product_stock_attribute_value where id = ? ",valueId);
									if( null != stockValue && stockValue.size() > 0 ){
										ProductStockAttributeValue psav = BeanUtils.toBean(ProductStockAttributeValue.class, stockValue.get(0));
										linkValue = psav.getValue(); 										
									}
								} else {
									linkValue= pav.getValue();
								}
								if(flag){
									value.append("|");
								}
								value.append(linkValue);
								flag = true;
							}
							Object[] v = new Object[6];
							v[0] = productStockId; // productStockId
							v[1] = productId; // productId
							v[2] = 1; // number(默认都是规格--1)
							v[3] = 1; // productAttributeId(默认都是规格---1)
							attributeId = idService.getId();
							v[4] = attributeId; // id 
							v[5] = value.toString(); // value
							insertProductValueArr.add(v);
						}
						
						Object[] ps = new Object[8];
						ps[0] = 1; // returnGoods(默认支持退货)
						ps[1] = 0; // weighing(默认不支持称重)
						ps[2] = ((null == productStock.getSort()) ? 1 : productStock.getSort()); // sort(默认排序为1)
						ps[3] = new Date(); // updateTime 修改时间
						ps[4] = imageId; // imageId(取图片表中的第一个图片)
						ps[5] = attributeId; // attributeCode
						ps[6] = ObjectUtils.equals(Constants.ProductStock.STORE_OFF_SHELVES, productStock.getShelves()) ? Constants.ProductStock.ALL_DOWN : Constants.ProductStock.ALL_UP;
						ps[7] = productStockId; // id
						updateProductStockArr.add(ps);
					}
				}
			}
		}
		
		logger.debug("分析完毕....耗时：{},共需修改商品信息{}条,库存信息{}条,新增规格信息{}条", (System.currentTimeMillis() - startTime),updateProductArr.size(),updateProductStockArr.size(),insertProductValueArr.size());
		int[] count = null;
		if( updateProductArr.size() > 0 ){
			count = jdbcTemplate.batchUpdate("update t_product set shortName = ? where id = ?", updateProductArr);
			for (int i : count) {
				if( i == 0 ){
					logger.debug("{}位转换商品信息未成功!",i);
				}
			}
			logger.debug("转换商品信息{}个",count.length);
		}
		
		if( updateProductStockArr.size() > 0 ){
			count = jdbcTemplate.batchUpdate("update t_product_stock set returnGoods = ?, weighing = ? , sort = ? , updateTime = ? , imageId = ? ,attributeCode = ?,shelves = ?  where id = ?", updateProductStockArr);
			for (int i : count) {
				if( i == 0 ){
					logger.debug("{}位转换库存信息未成功!",i);
				}
			}
			logger.debug("转换库存信息{}个",count.length);
		}
		
		if( insertProductValueArr.size() > 0 ){
			
			if( readyUpdatePSValue.size() > 0 ){
				StringBuilder delete = new StringBuilder();
				boolean flag = false;
				for (Long psID : readyUpdatePSValue) {
					if(flag){
						delete.append(",");
					}
					delete.append(psID);
					flag = true;
				}
				jdbcTemplate.update("delete from t_product_stock_attribute_value where  productStockId in ("+delete.toString()+")");
			}
			
			count = jdbcTemplate.batchUpdate("insert into t_product_stock_attribute_value(productStockId,productId,number,productAttributeId,id,value) values(?,?,?,?,?,?)", insertProductValueArr);
			for (int i : count) {
				if( i == 0 ){
					logger.debug("{}位新增规格信息未成功!",i);
				}
			}
			logger.debug("新增规格信息{}个",count.length);
		}
		logger.debug("转换结束...");
		return "/v2/commodityManager/list";
	}
}
