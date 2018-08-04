package cn.lastmiles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.Promotion;
import cn.lastmiles.bean.Store;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class StoreService {
	private final static Logger logger = LoggerFactory.getLogger(StoreService.class);
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private IdService idService;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private PromotionCouponService promotionCouponService;
	@Autowired
	private OrganizationService organizationService;

	public Account login(String mobile, String password) {
		Account account = storeDao.findByMobile(mobile);

		if (account != null
				&& PasswordUtils.checkPassword(password, account.getPassword())) {
			account.setStore(storeDao.findById(account.getStoreId()));
			return account;
		}
		return null;
	}

	/**
	 * 保存 调用底层保存方法
	 * 
	 * @param Store
	 */
	public void save(Store store) {
		store.setId(idService.getId());
		store.setCreatedTime(new Date());
		storeDao.save(store);
	}

	/**
	 * 
	 * @return 参看所有商品分类
	 */
	public Page findAll(String name, Page page, Long accountId, Long storeId) {
		return storeDao.findAll(name, page, accountId, storeId);
	}

	/**
	 * 
	 * @return 参看所有商品分类
	 */
	public List<Store> findAll() {
		return storeDao.findAll();
	}

	public List<Store> findAllEntityStore(){
		return storeDao.finAllEntityStore();
	}
	/**
	 * 通过父类查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
//	public List<Store> findByParentId(Long parentId) {
//		return storeDao.findByParentId(parentId);
//	}
	// 加入组织结构后的下属店铺的查询方法修改------2016.03.05 PM 18:02
	public List<Store> findByParentId(Long storeId) {
		// 根据商家ID查询此商家属于哪个组织结构
//		List<Store> returnStoreList = new ArrayList<Store>();
		List<Organization> list = organizationService.getByStoreId(storeId);
		if( null == list || list.isEmpty() ){
//			Store store = new Store();
//			store.setId(0L);
//			returnStoreList.add(store);
			return null;
		}
				
		List<Store> storeList = storeDao.findByOrganizationId(list.get(0).getId());
		if( null != storeList && storeList.size() > 0  ){
			return storeList;			
		}
//		Store store = new Store();
//		store.setId(0L);
//		returnStoreList.add(store);
		return null;
	}

	/**
	 * 修改方法
	 * 
	 * @param Store
	 */
	public void updateByID(Store store) {
		storeDao.updateByID(store);
	}

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Store findById(Long id) {
		return storeDao.findById(id);
	}
	
	public Store posFindById(Long id) {
		return storeDao.posFindById(id);
	}

	public Store findByProduct(Long productId) {
		return storeDao.findByProduct(productId);
	}

	public void deleteById(Long id) {
		storeDao.delete(id);
	}

	/**
	 * 我创建的，或者我属于的商店
	 * 
	 * @param accountId
	 * @param storeId
	 * @return
	 */
	public List<Store> findMyStore(Long accountId, Long storeId) {
		return storeDao.findMyStore(accountId, storeId);
	}

	public List<Store> findByAccount(Long accountId) {
		return storeDao.findByAccount(accountId);

	}

	public List<Store> findByAgentId(Long agentId) {
		return storeDao.findByAgentId(agentId);
	}
	
	public List<Store> getAgentAndStoreList(Long agentId) {
		return storeDao.getAgentAndStoreList(agentId);
	}

	/**
	 * 接口列表查询
	 * 
	 * @param page
	 * @param preferential 
	 * @param sort 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page apiList(Page page, Long storeTypeId, String keywords,Integer status,
			Integer preferential, String sort,String sortRule, Double longitude, Double latitude) {
		if (preferential!=null&&preferential.intValue()==Constants.Promotion.TYPE_NO_FREIGHT) {//是否免配送费
			page = storeDao.apiList(page, storeTypeId, keywords,status,null,0D,sort,sortRule, longitude,latitude);
		}else{
			page = storeDao.apiList(page, storeTypeId, keywords,status,preferential,null,sort,sortRule, longitude,latitude);
		}
		
		if (null!=page.getData()&&!page.getData().isEmpty()) {
			List<Store> stores=(List<Store>) page.getData();
			logger.debug("store list = {} ",stores);
			if (null!=stores) {
				for (Store store : stores) {
					store.setLogo(FileServiceUtils.getFileUrl(store.getLogo()));//设置logo 图片
					List<Promotion>  promotions=promotionService.findByStoreId(store.getId());
					Promotion promotion =promotionCouponService.findByStoreId(store.getId());
					if (promotion!=null) {
						promotions.add(promotion);
					}
					store.setPromotions(promotions);
				}
			}
		}
		return page;
	}
	
	public Store apiStroeLogeUrl(Store store){
		if (store!=null) {
			store.setLogo(FileServiceUtils.getFileUrl(store.getLogo()));
		}
		return store;
	}

	public Store findShipTimeShipType(Long storeId) {
		return storeDao.findShipTimeShipType(storeId);
	}

	/**
	 * APP-Third
	 * @param lastId 商家数据从这个ID开始,既必须大于此ID
	 * @param appId 合作者ID
	 */
	public List<Store> getAssignStoreByPartnerId(Long lastId, Long appId) {
		return storeDao.getAssignStoreByPartnerId(lastId,appId);
	}
	
	/**
	 * 根据organizationId查找同一颗组织结构树的所有商家
	 * @param organizationId
	 * @return
	 */
	public List<Store> findAllStoreOnOrganizationId(Long organizationId){
		return storeDao.findAllStoreOnOrganizationId(organizationId);
	}

	/**
	 * 根据storeId查找同一颗组织结构树的所有商家
	 * @param storeId
	 * @return
	 */
	public List<Store> findAllStoreOnOneOrganizationTree(Long storeId){
		return storeDao.findAllStoreOnOneOrganizationTree(storeId);
	}
	
	/**
	 * 连锁商家总部
	 * @param storeId
	 * @return
	 */
	public Store findTopStore(Long storeId){
		return storeDao.findTopStore(storeId);
	}

	public List<Store> findAllEntryChildrenStore(Long storeId) {
		return storeDao.findAllEntryChildrenStore(storeId);
	}

	public List<Store> findByStoreIdString(String storeIdString) {
		return storeDao.findByStoreIdString(storeIdString);
	}
}
