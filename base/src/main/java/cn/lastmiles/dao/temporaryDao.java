package cn.lastmiles.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;

@Repository
public class temporaryDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ProductAttributeValueDao productAttributeValueDao;
	
	private final static Logger logger = LoggerFactory
			.getLogger(temporaryDao.class);

	/**
	 * 根据手机查询
	 * 
	 * @param mobile
	 * @return
	 */
	@Transactional
	public boolean dealStockIsNull() {
		logger.debug("dealStockIsNull     >>>>>>>>>>>>>>>>>>>> ");
		String sql = "SELECT ps.id ,ps.productId ,p.`name`,p.type ,ps.attributeCode FROM t_product_stock ps INNER JOIN t_product p where ps.productId = p.id  ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : list) {
			sql  = "UPDATE t_product_stock  SET type = ?,attributeName = ? WHERE id = ?";
			//logger.debug(map.get("type").toString()+"--"+Constants.Product.PRODUCT_HAVE_ATTRIBUTE);
			if(map.get("type").toString().equals(Constants.Product.PRODUCT_HAVE_ATTRIBUTE+"")){//有属性
				logger.debug(map.get("attributeCode")+"--"+map.get("id")+"--"+map.get("type"));
				if(map.get("attributeCode")!=null){
					String [] s =map.get("attributeCode").toString().split("-");
					StringBuffer sf = new StringBuffer(""+map.get("name"));
					for (String string : s) {
						sf.append("|");
						sf.append(productAttributeValueDao.findById(Long.parseLong(string)).getValue());
					}
					logger.debug("sql is {}",sql,map.get("type"),sf.toString(),map.get("id"));
					jdbcTemplate.update(sql,map.get("type"),sf.toString(),map.get("id"));
				}
			}else{
				//logger.debug("sql is {}",sql,map.get("type"),map.get("name"),map.get("id"));
				jdbcTemplate.update(sql,map.get("type"),map.get("name"),map.get("id"));
			}
		}
		logger.debug("dealStockIsNull  end   >>>>>>>>>>>>>>>>>>>> ");
		return false;
	}
}
