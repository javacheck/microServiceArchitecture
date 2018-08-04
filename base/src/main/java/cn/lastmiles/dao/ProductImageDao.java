package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.common.utils.BeanUtils;

@Repository
public class ProductImageDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通过ID 查找第一张图片
	 * @param productId
	 * @return
	 */
	public ProductImage findByProductId(Long productId){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pie.* FROM t_product_image pie ");
		sql.append(" INNER JOIN t_product_stock psk ");
		sql.append(" ON  psk.id=pie.productStockId ");
		sql.append(" WHERE psk.productId=? ");
		sql.append(" ORDER BY pie.productStockId, pie.sort LIMIT 0,1 ");
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql.toString(),productId);
		return list.isEmpty() ? null : BeanUtils.toBean(ProductImage.class,	list.get(0));
	}
	
	/**
	 * 通过ID 查找所有库存图片
	 * @param productId
	 * @return
	 */
	public List<ProductImage> findListByProductId(Long productId){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT pie.* FROM t_product_image pie ");
		sql.append(" INNER JOIN t_product_stock psk ");
		sql.append(" ON  psk.id=pie.productStockId ");
		sql.append(" WHERE psk.productId=? ");
		sql.append(" ORDER BY pie.productStockId, pie.sort LIMIT 0,1 ");
		List<Map<String, Object>> list = jdbcTemplate
				.queryForList(sql.toString(),productId);
		return BeanUtils.toList(ProductImage.class,list);
	}


	public Boolean addImage(ProductImage pi) {
		int flag = jdbcTemplate.update("insert into t_product_image(id,productStockId,suffix,sort) values(?,?,?,?)",
				pi.getId(), pi.getProductStockId(),
				pi.getSuffix(),pi.getSort());
		if(flag > 0 ){
			return true;
		}
		return false;
	}

	public List<ProductImage> getByProductStockID(Long id) {
		String sql = "select * from t_product_image where productStockId = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,id);
		return BeanUtils.toList(ProductImage.class, list);
	}

	public Boolean deleteByProductStockID(Long id) {
		int flag = jdbcTemplate.update("delete from t_product_image where productStockId = ?",id);
		if(flag > 0 ){
			return true;
		}
		return false;
	}

	public List<ProductImage> findImgUrlByStockId(Long stockId) {
		String sql = "SELECT pie.* FROM t_product_image pie  where pie.productStockId = ? ";
		return BeanUtils.toList(ProductImage.class, jdbcTemplate.queryForList(sql,stockId));
	}
}
