package cn.lastmiles.service;

/**
 * createDate : 2015-07-07 PM 18:40
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.AccountRole;
import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.bean.Product;
import cn.lastmiles.bean.ProductAttribute;
import cn.lastmiles.bean.ProductAttributeValue;
import cn.lastmiles.bean.ProductCategory;
import cn.lastmiles.bean.ProductImage;
import cn.lastmiles.bean.ProductStock;
import cn.lastmiles.bean.ShopType;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.StoreTerminal;
import cn.lastmiles.bean.User;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.BeanUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PayAccountDao;
import cn.lastmiles.dao.ShopDao;

@Service
public class ShopService {
	@Autowired
	private ShopDao shopDao;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRoleService accountRoleService;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductAttributeValueService productAttributeValueService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductStockService productStockService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private PayAccountDao payAccountDao;
	private final static Logger logger = LoggerFactory
			.getLogger(ShopService.class);

	/**
	 * 商户app更新商家信息
	 * 
	 * @param store
	 */
	public void appUpdate(Store store) {
		shopDao.appUpdate(store);
	}

	public Page getShop(Long storeId,String shopName, String mobile, String agentName, int status, Page page) {
		return shopDao.getShop(storeId,shopName, mobile, agentName, status, page);
	}
	
	public Page getShop(String storeIdString,String shopName, String mobile, String agentName, int status, Page page) {
		return shopDao.getShop(storeIdString,shopName, mobile, agentName, status, page);
	}
	
	public Page getShop(String shopName, String mobile, String agentName, int status, Page page) {
		Long storeId = null;
		return shopDao.getShop(storeId,shopName, mobile, agentName, status, page);
	}
	public Page getAllShop(String storeIdString,int status,String shopName, String mobile, String agentName, Page page) {
		return shopDao.getAllShop(storeIdString,shopName, mobile, agentName, status, page);
	}
	
	@Transactional
	public boolean save(Store store) {
		shopDao.save(store);
		String[] terminalIdarray = store.getTerminalIdArray();
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		Long storeId = store.getId();
		for (String element : terminalIdarray) {
			if(null != element && !"".equals(element)){
				Object[] arg = new Object[2];
				arg[0] = storeId;
				arg[1] = element;
				batchArgs.add(arg);
			}
		}
		
		shopDao.saveTerminalId(batchArgs,null);
		
		Account account = new Account();
		account.setMobile(store.getMobile()); // 设置商家手机号码
		account.setPassword(store.getMobile()); // 将手机号码作为密码
		account.setStoreId(store.getId()); // 设置商家ID
		account.setType(Constants.Account.ACCOUNT_TYPE_STORE); // 设置账号类型
		if (accountService.save(account)) { // 新增账号成功
			AccountRole accountRole = new AccountRole();
			accountRole.setAccountId(account.getId());
			accountRole.setRoleId(Constants.Role.ROLE_STORE_ID); // 商家

			accountRoleService.save(accountRole);
		}
		
		PayAccount payaccount=new PayAccount();
		payaccount.setId(idService.getId());
		payaccount.setOwnerId(store.getId());//商家ID
		payaccount.setType(Constants.Withdraw.WITHDRAW_TYPE_STORE);//商家
		payaccount.setStatus(Constants.PayAccountStatus.PAYACCOUNT_NORMAL);//0未激活，1正常，2冻结，3销户，4挂失，5锁定
		payAccountDao.save(payaccount);
		return  true;
	}

	public Store findById(Long id) {
		return shopDao.findById(id);
	}

	@Transactional
	public boolean update(Store store) {
//		accountService.updateMobileByMobile(store.getMobile(),store.getMobileCache());	
		accountService.updateMobileByMobile(store.getMobile(),store.getMobileCache(),Constants.Account.ACCOUNT_TYPE_STORE);
		String[] terminalIdarray = store.getTerminalIdArray();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		Long storeId = store.getId();
		for (String element : terminalIdarray) {
			if(null != element && !"".equals(element)){
				Object[] arg = new Object[2];
				arg[0] = storeId;
				arg[1] = element;
				batchArgs.add(arg);
			}
		}
		shopDao.saveTerminalId(batchArgs,storeId);
		
		// 有修改登录密码则修改密码
		if( StringUtils.isNotBlank(store.getPosAdminPassword()) ){
			String posAdminPassword = PasswordUtils.encryptPassword(store.getPosAdminPassword());
			shopDao.updatePosPassword( store.getId(),posAdminPassword );			
		}
		
		return shopDao.update(store);
	}
	
	public boolean storeUpdate(Store store) {
		
		// 有修改登录密码则修改密码
		if( StringUtils.isNotBlank(store.getPosAdminPassword()) ){
			String posAdminPassword = PasswordUtils.encryptPassword(store.getPosAdminPassword());
			shopDao.updatePosPassword( store.getId(),posAdminPassword );			
		}
		return shopDao.storeUpdate(store);
	}

	public boolean checkName(String name) {
		return shopDao.checkName(name) != null ? true : false;
	}

	public boolean checkCompanyName(String companyName) {
		return shopDao.checkCompanyName(companyName) != null ? true : false;
	}

	public boolean checkMerchantNo(String merchantNo) {
		return shopDao.checkMerchantNo(merchantNo) != null ? true : false;
	}

	public boolean checkMerchantName(String merchantName) {
		return shopDao.checkMerchantName(merchantName) != null ? true : false;
	}

	public Boolean deleteById(Long id) {
		return shopDao.deleteById(id);
	}

	public Page getShopTypeList(String name, Page page) {
		return shopDao.getShopTypeList(name, page);
	}

	public Page getAgentAndStoreList(Long agentId, String name, Page page) {
		return shopDao.getAgentAndStoreList(agentId, name, page);
	}

	public List<ShopType> findAllShopType() {
		return shopDao.findAllShopType();
	}

	public String getMobile(Long storeId) {
		return shopDao.getMobile(storeId);
	}
	
	public Page getShop(String name, String mobile,Integer shopTypeId,Page page) {
		return shopDao.getShop(name, mobile,shopTypeId, page);
	}

	public Page getShopNotInStoreAdImage(String name, String mobile, Integer shopTypeId,
			Page page) {
		return shopDao.getShopNotInStoreAdImage(name, mobile,shopTypeId, page);
	}

	public List<StoreTerminal> getTerminalIdArray(Long storeId) {
		return shopDao.getTerminalIdArray(storeId);
	}

	public boolean checkTerminalId(String terValue) {
		return shopDao.checkTerminalId(terValue);
	}
	
	/**
	 * 根据商家ID查询商家信息
	 * @param storeId 商家ID
	 * @return 商家对象或者null
	 */
	public Store findByStoreId(Long storeId) {
		return shopDao.findByStoreId(storeId);
	}
	
	public String findStoreNameById(Long id) {
		return shopDao.findStoreNameById(id);
	}

	public boolean updateLoginPW(Long accountId, String newPW) {
		return shopDao.updateLoginPW(accountId,newPW);
	}

	public boolean updateMobile(Long accountId, String mobile) {
		return shopDao.updateMobile(accountId,mobile);
	}
	
	@Transactional
	public boolean updateMobile(Long accountId,Long storeId, String mobile) {
		if(updateMobile(accountId,mobile)){
			return shopDao.updateStoreMobile(storeId, mobile);
		}
		return false;
	}

	public boolean updateLoginPW(String mobile, String newPW) {
		return shopDao.updateLoginPW(mobile,newPW);
	}
	
	public List<Map<String, Object>> findStoreSignById(Long storeId) {
		return shopDao.findStoreSignById(storeId);
	}
	@Transactional
	public void  copyStopProduct(Long oldStoreId,Long newStoreId){
		Map<Long, Long> attributeCodeMap = new HashMap<Long, Long>();//临时Map 新老对应关系
		List<ProductCategory> productCategorys =productCategoryService.findByStoreId(oldStoreId);//找到现有分类
		for (ProductCategory productCategory : productCategorys) {
			Long categoryId= idService.getId();
			List<ProductAttribute> productAttributes = productAttributeService.findByCategoryId(productCategory.getId());//找到现在有属性
			for (ProductAttribute productAttribute : productAttributes) {//修改属性
				Long attributeId= idService.getId();
				 List<ProductAttributeValue> productAttributeValues =productAttributeValueService.findByAttributeId1(productAttribute.getId());
				 for (ProductAttributeValue productAttributeValue : productAttributeValues) {//修改属性值
					 Long productAttributeValueId= idService.getId();
					 attributeCodeMap.put(productAttributeValue.getId(), productAttributeValueId);//保存到临时Map 对应关系
					 productAttributeValue.setId(productAttributeValueId);//设置新的属性值ID
					 productAttributeValue.setAttributeId(attributeId);//设置新的属性ID
					 productAttributeValueService.save(productAttributeValue);
				}//修改属性值结束
				 productAttribute.setId(attributeId);//设置新的属性ID
				 productAttribute.setCategoryId(categoryId);//设置新的分类ID
				 productAttributeService.save(productAttribute);
			}//修改属性结束
			List<Product> products = productService.findByCategoryId(productCategory.getId());//找到现有商品
			for (Product product : products) {//处理产品
				Long productId= idService.getId();
				List<ProductStock> productStocks =productStockService.findByProductId(product.getId());
				for (ProductStock productStock : productStocks) {//处理库存
					Long stockId= idService.getId();
					List<ProductImage> productImages =productImageService.getByProductStockID(productStock.getId());
					logger.debug("productImages is {}",productImages);
					for (ProductImage productImage : productImages) {//修改图片
						productImage.setProductStockId(stockId);
						logger.debug("productImage is {}",productImage);
						productImageService.addImage(productImage);
					}//修改图片结束
					productStock.setId(stockId);
					productStock.setProductId(productId);
					productStock.setStoreId(newStoreId);
					if (StringUtils.isNotBlank(productStock.getAttributeCode())) {//处理属性值拼接问题
						logger.debug("转换之前 attributeCode is :{} " , productStock.getAttributeCode());
						logger.debug("attributeCodeMap is :{} " , attributeCodeMap);
						String[] attributeCode =StringUtils.split(productStock.getAttributeCode(), "-") ;
						StringBuffer sf= new StringBuffer();
						for (String key : attributeCode) {
							sf.append(attributeCodeMap.get(Long.parseLong(key)));
							sf.append("-");
						}
						sf.deleteCharAt(sf.length()-1);
						logger.debug("转换之后 attributeCode is :{} " , sf.toString());
						productStock.setAttributeCode(sf.toString());
					}
					productStockService.save(productStock);
				}//处理库存结束
				product.setCategoryId(categoryId);
				product.setStoreId(newStoreId);
				product.setId(productId);
				productService.save(product);
			}//处理产品结束
			productCategory.setId(categoryId);
			productCategory.setStoreId(newStoreId);
			productCategoryService.save(productCategory);
		}
	}
	
	public Page mainShoplist(String name, Page page) {
		return shopDao.mainShopList(name,page);
	}

	public boolean mainShopUpdate(Store store) {
		
		// 有修改登录密码则修改密码
		if(StringUtils.isNotBlank(store.getStoreAcountPassWord())){
			String password = PasswordUtils.encryptPassword(store.getStoreAcountPassWord());
			if(StringUtils.isNotBlank(password)){
				accountService.updatePasswordByMobile(store.getStoreAcountName(),password,Constants.Account.ACCOUNT_TYPE_STORE);				
			}
		}
		
		Account account = null;
		if(StringUtils.isNotBlank(store.getStoreAcountNameCache())){
			account = accountService.findByStoreIdAndTypeAndMobile(store.getId(),Constants.Account.ACCOUNT_TYPE_STORE,store.getStoreAcountNameCache());				
		}
		if( null != account ){
			account.setMobile(store.getStoreAcountName()); // 设置商家手机号码
			accountService.update(account);				
		}
		
		return shopDao.mainShopUpdate(store);
	}

	@Transactional
	public boolean mainShopSave(Store store) {
		if(shopDao.mainShopSave(store)){
			Account account = new Account();
			account.setMobile(store.getStoreAcountName()); // 设置商家手机号码
			account.setPassword(store.getStoreAcountPassWord()); // 将手机号码作为密码
			account.setStoreId(store.getId()); // 设置商家ID
			account.setType(Constants.Account.ACCOUNT_TYPE_STORE); // 设置账号类型
			if (accountService.save(account)) { // 新增账号成功
				AccountRole accountRole = new AccountRole();
				accountRole.setAccountId(account.getId());
				accountRole.setRoleId(Constants.Role.ROLE_STORE_ID); // 商家

				accountRoleService.save(accountRole);
			}
		} else {
			throw new RuntimeException("新增商家总部时出错！");
		}
		return true;
	}
	
	public Store findMainShopById(Long id) {
		return shopDao.findMainShopById(id);
	}

	public int mainShopCheckName(Long id, String name) {
		return shopDao.mainShopCheckName(id,name);
	}

	public Page getShop(String storeIdString,int status, String shopName, String mobile, String agentName,  Page page) {
		return shopDao.getShop(storeIdString, status, shopName, mobile, agentName, page);
	}
	
	/**
	 * 根据组织架构集合查询所属其下的所有商家集合列表
	 * @param organizationList 组织架构集合
	 * @return null或者商家集合列表
	 */
	public List<Store> searchOrganization_StoreByorganizationList(List<Organization> organizationList) {
		StringBuffer organizationLink = new StringBuffer();
		if( null != organizationList && organizationList.size() > 0 ){
			boolean flag = false;
			for (Organization organization : organizationList) {
				if(flag){
					organizationLink.append(",");
				}
				organizationLink.append(organization.getId());
				flag = true;
			}			
		}
		return shopDao.searchOrganization_StoreByorganizationLink(organizationLink.toString());
	}
	
	public boolean updateAlipayAuthToken(String storeId,String appAuthToken){
		return shopDao.updateAlipayAuthToken(storeId,appAuthToken);
	}

}