/**
 * createDate : 2016年8月16日上午9:55:22
 */
package cn.lastmiles.v2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.StoreService;

@Controller
@RequestMapping("hiddenLevelName")
public class OldLevelNameChangeController {
	private static final Logger logger = LoggerFactory.getLogger(OldProductChangeController.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private StoreService storeService;
	
	@RequestMapping("/operation")
	public String operation(){
		long startTime = System.currentTimeMillis();
		
		logger.debug("转换开始....");
		List<Map<String,Object>> list1 = jdbcTemplate.queryForList(" select tuc.* from t_user_card tuc where tuc.userLevelName is not null ");
		List<UserCard> userCardList = BeanUtils.toList(UserCard.class, list1);
		logger.debug("需要转换的信息有{}条",userCardList.size());
		
		List<Map<String,Object>> list2 = jdbcTemplate.queryForList(" select tuld.* from t_user_level_definition tuld ");
		List<UserLevelDefinition> userLevelDefinitionList = BeanUtils.toList(UserLevelDefinition.class, list2);
		logger.debug("共存在等级信息{}条",userLevelDefinitionList.size());
		
		Map<Long,List<UserLevelDefinition>> levelMap = new HashMap<Long, List<UserLevelDefinition>>();
		for (UserLevelDefinition userLevelDefinition : userLevelDefinitionList) {
			Long storeId = userLevelDefinition.getStoreId();
			List<UserLevelDefinition> temp = levelMap.get(storeId);
			if( null == temp ){
				 List<UserLevelDefinition> psList = new ArrayList<UserLevelDefinition>();
				 psList.add(userLevelDefinition);
				 levelMap.put(storeId, psList);
			} else {
				temp.add(userLevelDefinition);
				levelMap.put(storeId, temp);
			}
		}
		
		// 待修改的商品库存信息集合
		List<Object[]> updateArr = new ArrayList<Object[]>();
				
		for (UserCard userCard : userCardList) {
			Long storeId = userCard.getStoreId();
			List<UserLevelDefinition> temp = levelMap.get(storeId);
			boolean flag = false;
			if( null == temp ){
				Store store = storeService.findTopStore(storeId);
				temp = levelMap.get(store.getId());
				if( null == temp ){
					continue;
				}
				flag = true;
			}
			String levelName = (userCard.getUserLevelName()).trim();
			flag = getUpdateArrIsFlag(updateArr, userCard, temp, flag,levelName);
			
			if(!flag){
				Store store = storeService.findTopStore(storeId);
				temp = levelMap.get(store.getId());
				if( null == temp ){
					continue;
				}
				boolean returnFlag = getUpdateArrIsFlag(updateArr, userCard, temp, flag,levelName);
				logger.debug("{}编号---从总部获取的等级名称和等级折扣，结果是：{}",storeId,returnFlag);
			}
		}
		
		logger.debug("分析结果：待修改的userCard对象有{}个，分析耗时：{}",updateArr.size(),(System.currentTimeMillis() - startTime));
		
		int[] count = null;
		if( updateArr.size() > 0 ){
			count = jdbcTemplate.batchUpdate("update t_user_card set levelId = ? where id = ?", updateArr);
			for (int i : count) {
				if( i == 0 ){
					logger.debug("{}位未成功!",i);
				}
			}
			logger.debug("实际转换信息{}个",count.length);
		}
		
		logger.debug("转换结束...共耗时：{}",(System.currentTimeMillis() - startTime));
		
		return "/userAccountManager/list";
	}

	public boolean getUpdateArrIsFlag(List<Object[]> updateArr,
			UserCard userCard, List<UserLevelDefinition> temp, boolean flag,
			String levelName) {
		for (UserLevelDefinition userLevelDefinition : temp) {
			String name = (userLevelDefinition.getName()).trim();
			if( StringUtils.equals(levelName, name) ){
				Object[] o = new Object[2];
				o[0] = userLevelDefinition.getId();
				o[1] = userCard.getId();
				updateArr.add(o);
				flag = true;
			} else {
				Double levelDiscount = userCard.getUserLevelDiscount();
				if( null != levelDiscount ){
					if( ObjectUtils.equals(levelDiscount, userLevelDefinition.getDiscount()) ){
						Object[] o = new Object[2];
						o[0] = userLevelDefinition.getId();
						o[1] = userCard.getId();
						updateArr.add(o);
						flag = true;
					}
				}
			}
		}
		return flag;
	}
}
