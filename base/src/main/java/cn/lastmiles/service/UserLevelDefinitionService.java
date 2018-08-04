package cn.lastmiles.service;

/**
 * createDate : 2016年1月20日下午2:51:34
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserLevelDefinition;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserCardDao;
import cn.lastmiles.dao.UserLevelDefinitionDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * *会员等级管理(列表/新增/修改/删除)
 */
@Service
public class UserLevelDefinitionService {
	private final static Logger logger = LoggerFactory.getLogger(UserLevelDefinitionService.class);
	@Autowired
	private UserLevelDefinitionDao userLevelDefinitionDao; // 数据查询对象
	@Autowired
	private IdService idService; // ID自动生成
	@Autowired
	private UserCardDao userCardDao;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private StoreDao storeDao;
	/**
	 * 根据名称查询数据
	 * 
	 * @param name
	 *            查询标识
	 * @param page
	 *            页码对象
	 * @return 页码数据对象
	 */
	public Page findByName(String storeIdString, String name, Page page) {
		return userLevelDefinitionDao.findByName(storeIdString, name, page);
	}

	/**
	 * 检测字段唯一性
	 * 
	 * @param id
	 *            根据id来判断(新增/修改)区分自身的标识
	 * @param name
	 *            判断标识
	 * @return 是否修改成功(成功1、失败0)
	 */
	public int checkUserLevelDefinitionName(Long id, Long storeId, String name) {
		return userLevelDefinitionDao.checkUserLevelDefinitionName(id, storeId,
				name) > 0 ? 1 : 0;
	}

	/**
	 * 新增数据对象
	 * 
	 * @param userLevelDefinition
	 *            对象信息
	 */
	@Transactional
	public void save(UserLevelDefinition userLevelDefinition,List<Long> categoryIdList) {
		if (null == userLevelDefinition.getId()) {
			userLevelDefinition.setId(idService.getId());
		}
		if(userLevelDefinition.getDiscountScope().intValue()==1){//按分类
			for(Long categoryId:categoryIdList){
				this.saveUserLevelDefinitionProductCategory(userLevelDefinition.getId(),categoryId);
			}
		}
		userLevelDefinitionDao.save(userLevelDefinition);
		handleUserCard(userLevelDefinition.getStoreId());
	}

	private void saveUserLevelDefinitionProductCategory(Long id, Long categoryId) {
		userLevelDefinitionDao.saveUserLevelDefinitionProductCategory(id,categoryId);
	}
	
	public UserLevelDefinition posFindById(Long id) {
		return userLevelDefinitionDao.findById(id);
	}

	/**
	 * 根据ID查询数据对象
	 * 
	 * @param id
	 *            查询ID
	 * @return 数据对象
	 */
	public UserLevelDefinition findById(Long id) {
		UserLevelDefinition uld= userLevelDefinitionDao.findById(id);
		if(uld!=null && uld.getDiscountScope().intValue()==1){//按分类
			List<Long> categoryIdList=this.findUserLevelDefinitionProductCategoryById(id);
			uld.setCategoryIdList(categoryIdList);
		}
		return uld;
	}

	public List<Long> findUserLevelDefinitionProductCategoryById(Long id) {
		return userLevelDefinitionDao.findUserLevelDefinitionProductCategoryById(id);
	}

	/**
	 * 修改信息
	 * 
	 * @param userLevelDefinition
	 *            对象信息
	 * @return 是否修改成功(成功true、失败false)
	 */
	@Transactional
	public void update(UserLevelDefinition userLevelDefinition,List<Long> categoryIdList) {
		List<Long> cList=this.findUserLevelDefinitionProductCategoryById(userLevelDefinition.getId());
		if(!cList.isEmpty()){//删除之前按分类存起来的分类信息
			this.deleteUserLevelDefinitionProductCategoryById(userLevelDefinition.getId());
		}
		userLevelDefinition.setUpdateTime(new Date());
		if(userLevelDefinition.getDiscountScope().intValue()==1){//按分类
			for(Long categoryId:categoryIdList){
				this.saveUserLevelDefinitionProductCategory(userLevelDefinition.getId(),categoryId);
			}
		}
		logger.debug("UserLevelDefinition : {}",userLevelDefinition.getMode());
		boolean ret = userLevelDefinitionDao.update(userLevelDefinition) > 0 ? true: false;
		if (ret){
			handleUserCard(userLevelDefinition.getStoreId());
		}
	}

	private void deleteUserLevelDefinitionProductCategoryById(Long id) {
		userLevelDefinitionDao.deleteUserLevelDefinitionProductCategoryById(id);
		
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 *            删除标识
	 * @return 是否删除成功(成功1、失败0)
	 */
	@Transactional
	public int deleteById(Long id) {
		List<Long> cList=this.findUserLevelDefinitionProductCategoryById(id);
		if(!cList.isEmpty()){//删除之前按分类存起来的分类信息
			this.deleteUserLevelDefinitionProductCategoryById(id);
		}
		UserLevelDefinition def = findById(id);
		int ret =  userLevelDefinitionDao.deleteById(id) > 0 ? 1 : 0;
		if (ret == 1){
			handleUserCard(def.getStoreId());
		}
		return ret; 
	}

	/**
	 * 处理会员等级
	 * 
	 * @param userLevelDefinition
	 */
	private void handleUserCard(Long storeId) {
		List<UserLevelDefinition> defList = userLevelDefinitionDao
				.findByStoreId(storeId);
		List<UserCard> userCardList = userCardDao
				.findByChainStoreId(storeId);
		if (defList != null && !defList.isEmpty()) {
			for (UserCard uc : userCardList) {
				Double discount = null;
				String levelName = null;
				for (UserLevelDefinition def : defList) {
					// 0表示积分，1表示消费总额
					if (def.getType().intValue() == 0
							&& def.getDiscount() != null) {
						if (uc.getTotalPoint().doubleValue() >= def.getPoint()
								.doubleValue()) {
							discount = def.getDiscount();
							levelName = def.getName();
							break;
						}
					} else if (def.getType().intValue() == 1
							&& def.getDiscount() != null) {
						if (uc.getTotalConsumption().doubleValue() >= def.getPoint()
								.doubleValue()) {
							discount = def.getDiscount();
							levelName = def.getName();
							break;
						}
					}
				}
				userCardDao.updateLevel(uc.getId(), levelName, discount);
			}
		}else {
			for (UserCard uc : userCardList) {
				userCardDao.updateLevel(uc.getId(), null, null);
			}
		}
	}

	public List<UserLevelDefinition> findByStoreId(Long storeId) {
		return userLevelDefinitionDao.findByStoreId(storeId);
	}

	public List<Map<String, Object>> getDiscountByCategoryList(Long levelId,
			String productStockIds) {
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String, Object> mapIds =null;
		//分割库存ID(productStockIds)
		String[] pStockIdArr=productStockIds.split(",");
		if(levelId==null){//会员等级Id为空
			for(int i=0;i<pStockIdArr.length;i++){
				mapIds = new HashMap<String, Object>();
				mapIds.put(pStockIdArr[i], 10);
				mapList.add(mapIds);
			}
		}else{//会员等级Id不为空
			UserLevelDefinition uld=this.findById(levelId);//通过会员卡Id去查t_user_level_definition会员等级表
			logger.debug("uld.getDiscountScope().intValue()={}",uld.getDiscountScope());
			if(uld.getDiscountScope().intValue()==0){//0全部商品 1按分类
				for(int i=0;i<pStockIdArr.length;i++){
					mapIds = new HashMap<String, Object>();
					mapIds.put(pStockIdArr[i], uld.getDiscount());//每个商品的折扣一样
					mapList.add(mapIds);
				}
			}else{//1按分类
				//查出有折扣的分类名称pcList
				List<Long> categoryIdList=uld.getCategoryIdList();
				List<ProductCategory> pcList=new ArrayList<ProductCategory>();
				for(int i=0;i<categoryIdList.size();i++){
					ProductCategory pc=productCategoryService.findById(categoryIdList.get(i));
					pcList.add(pc);
				}
				boolean flag=false;
				logger.debug("pStockIdArr.length={}",pStockIdArr.length);
				for(int i=0;i<pStockIdArr.length;i++){
					logger.debug("pStockIdArr[i]={}",pStockIdArr[i]);
					ProductStock ps=productStockService.posFindByStockId(Long.parseLong(pStockIdArr[i]));
					if (ps != null){
						logger.debug("ps.getCategoryId()={}",ps.getCategoryId());
						ProductCategory pc=productCategoryService.findById(ps.getCategoryId());
						logger.debug("pc={}",pc);
						logger.debug("pc.getPath()={},pc.getPath()1={}",pc.getPath(),pc.getPath().split("_")[0]);
						String name=productCategoryService.findById(Long.parseLong(pc.getPath().split("_")[0])).getName();
						for(int j=0;j<pcList.size();j++){
							logger.debug("ps.getCategoryName()={},pcList.get(j).getName()={}",ps.getCategoryName(),pcList.get(j).getName());
							if(name.equals(pcList.get(j).getName())){//分类名称相等
								mapIds = new HashMap<String, Object>();
								mapIds.put(pStockIdArr[i], uld.getDiscount());//该商品的折扣put进map
								flag=true;
								break;
							}
						}
					}
					if(flag==false){//没找到不打折
						mapIds = new HashMap<String, Object>();
						mapIds.put(pStockIdArr[i], 10);
					}
					mapList.add(mapIds);
					flag=false;
				}
			}
		}
		return mapList;
	}
	
	/**
	 * 根据手机号，商店id找比现存在的会员等级高的等级
	 * @param mobile
	 * @param storeId
	 * @return
	 */
	public List<UserLevelDefinition> findByMobileAndStore(String mobile,Long storeId){
		List<Store> storeList = storeDao.findAllStoreOnOneOrganizationTree(storeId);
		UserCard uc = null;
		for (Store s : storeList){
			uc = userCardDao.findByMobileAndStoreId(mobile, s.getId());
			if (uc != null){
				break;
			}
		}
		List<UserLevelDefinition> list = findByStoreId(storeDao.findTopStore(storeId).getId());
		logger.debug("usercard:" + uc);
		logger.debug("list:" + list);
		
		if (list != null && !list.isEmpty() && list.get(0).getType().intValue() == 1){
			//0表示积分，1表示消费总额
			return list;
		}
		
		if (uc == null){
			return list;
		}else {
			List<UserLevelDefinition> ret = new ArrayList<UserLevelDefinition>();
			UserLevelDefinition current = null;
			for (UserLevelDefinition def : list){
				if (ObjectUtils.equals(def.getId(), uc.getLevelId())){
					current = def;
					break;
				}
			}
			
			if (current != null){
				for (UserLevelDefinition def : list){
					if (def.getDiscount().doubleValue() <= current.getDiscount().doubleValue()){
						ret.add(def);
					}
				}
				return ret;
			}else{
				return list;
			}
		}
	}

	public Map<String,Object> judgeByStoreId(Long storeId) {
		return userLevelDefinitionDao.judgeByStoreId(storeId);
	}
}