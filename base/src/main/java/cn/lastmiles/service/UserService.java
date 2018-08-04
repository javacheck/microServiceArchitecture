package cn.lastmiles.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.Organization;
import cn.lastmiles.bean.PayAccount;
import cn.lastmiles.bean.Store;
import cn.lastmiles.bean.User;
import cn.lastmiles.bean.UserBalanceRecord;
import cn.lastmiles.bean.UserBank;
import cn.lastmiles.bean.UserCard;
import cn.lastmiles.bean.UserCer;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.ExcelUtils;
import cn.lastmiles.common.utils.ObjectUtils;
import cn.lastmiles.common.utils.PasswordUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.constant.Constants;
import cn.lastmiles.dao.PayAccountDao;
import cn.lastmiles.dao.StoreDao;
import cn.lastmiles.dao.UserDao;
import cn.lastmiles.getui.PushService;
import cn.lastmiles.utils.FileServiceUtils;

@Service
public class UserService {
	private final static Logger logger = LoggerFactory
			.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private PayAccountService payAccountService;
	@Autowired
	private FileService fileService;
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StoreDao storeDao;
	
	@Autowired
	private PayAccountDao payAccountDao;
	@Autowired
	private MyMessageService myMessageService;
	@Autowired
	private UserBalanceRecordService userBalanceRecordService;
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private IdService idService;
	
	@Autowired
	private UserAccountManagerService userAccountManagerService;
	@Autowired
	private ShopService shopService; // 商家
	
	/**
	 * 
	 * @param id
	 * @return 0没有认证，1认证
	 */
	public int isVerify(Long id){
		User user = userDao.findByMobileForAppLogin(id);
		return user.getIdAudit() == null ? 0 : user.getIdAudit();
	}

	public Page list(String beginTime, String endTime, String name,
			String mobile, Long storeId, Page page) {
		page = userDao.list(beginTime, endTime, name, mobile, storeId, page);
		return page;
	}

	public List<User> list(Long storeId,String reportBeginTime,String reportEndTime,String reportName,String reportMobile) {
		return userDao.list(storeId,reportBeginTime,reportEndTime,reportName,reportMobile);
	}
	
	public User infull(User user) {
		user.setStore(storeService.findById(user.getStoreId()));
		return user;
	}

	/**
	 * 通过电话和商店ID 查找
	 * 
	 * @param mobile
	 * @param storeId
	 * @return
	 */
	public User findByMobileAndStoreId(String mobile, Long storeId) {
		return userDao.findByMobileAndStoreId(mobile, storeId);
	}

	/**
	 * 修改折扣
	 * 
	 * @param storeId
	 * @param discount
	 * @return
	 */
	public boolean updateDiscount(Long storeId, Double discount) {
		return userDao.updateDiscount(storeId, discount);
	}

	/**
	 * 修改账号
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		return userDao.update(user);

	}
	
	public boolean update(String identity,String birthTime,Integer sex ,Long id) {
		return userDao.update(identity,birthTime,sex,id);

	}

	/**
	 * 根据ID 删除
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(Long id) {
		return userDao.delete(id);
	}

	public User findByStoreId(Long id) {
		return userDao.findByStoreId(id);
	}

	/**
	 * 通过手机号码查找
	 * 
	 * @param mobile
	 * @return
	 */
	public User findByMobile(String mobile) {
		return userDao.findByMobile(mobile);
	}
	
	/**
	 * 根据手机号码查询用户是否存在(是否属于平台用户)
	 * @param mobile 手机号码
	 * @param isPlatform 查询平台用户
	 * @return 用户集合或者null
	 */ 
	public List<User> findByMobile(String mobile,boolean isPlatform) {
		return userDao.findByMobile(mobile, isPlatform);
	}
	private User findByMobile(String mobile, Long storeId) {
		return userDao.findByMobile(mobile,storeId);
	}
	/**
	 * 保持登陆账号
	 * 
	 * @param user
	 */
	@Transactional
	public void save(User user) {
		user.setId(idService.getId());
		user.setCreatedTime(new Date());
		user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
		userDao.save(user);
		
		// 只有平台的用户才有支付账号和支付余额记录
		if( null != user && null == user.getStoreId()){
			//支付账号
//			PayAccount pa = new PayAccount();
//			pa.setId(idService.getId());
//			pa.setBalance(6d);//注册送6元
//			pa.setCreatedTime(new Date());
//			pa.setOwnerId(user.getId());
//			pa.setType(Constants.PayAccount.PAY_ACCOUNT_TYPE_USER);
//			pa.setStatus(Constants.PayAccountStatus.PAYACCOUNT_NORMAL);
//			payAccountDao.save(pa);
			
//			UserBalanceRecord userBalanceRecord = new UserBalanceRecord();
//			userBalanceRecord.setMemo("新用户注册送6元");
//			userBalanceRecord.setAmount(6D);
//			userBalanceRecord.setUserId(user.getId());
//			userBalanceRecord.setOrderId(null);
//			userBalanceRecord.setType(Constants.UserBalanceRecord.TYPE_REGISTER);
//			userBalanceRecordService.save(userBalanceRecord);
			
//			if (StringUtils.isNotBlank(user.getRecommended())) {//填写了推荐人
//				User recommendedUser = this.findByMobile(user.getRecommended());
//				if (recommendedUser!=null) {
//					if (userBalanceRecordService.sumPrice(recommendedUser.getId(),Constants.UserBalanceRecord.TYPE_RECOMMENDED, null, null, null).doubleValue()>=1500D) {
//						PushService.pushToSingle(new Message("推荐金额已达上限通知", Constants.MessageType.MESSAGE_RECOMMENDED_MAX_CONTENT, recommendedUser.getId(), Constants.MessageType.MESSAGE_RECOMMENDED_MAX, user.getId()));
//					}else{
//						userBalanceRecord = new UserBalanceRecord();
//						userBalanceRecord.setMemo("推荐了|"+user.getMobile()+"|注册获赠");
//						userBalanceRecord.setAmount(5D);
//						userBalanceRecord.setUserId(recommendedUser.getId());
//						userBalanceRecord.setOrderId(null);
//						userBalanceRecord.setType(Constants.UserBalanceRecord.TYPE_RECOMMENDED);
//						userBalanceRecordService.save(userBalanceRecord);
//						payAccountService.addBalance(Constants.PayAccount.PAY_ACCOUNT_TYPE_USER,recommendedUser.getId(),5D);
//						PushService.pushToSingle(new Message("推荐新用户拿奖励通知", Constants.MessageType.MESSAGE_RECOMMENDED_CONTENT.replaceAll("#title#", user.getMobile()), recommendedUser.getId(), Constants.MessageType.MESSAGE_RECOMMENDED, user.getId()));
//					}
//				}
//			}
		}
	}
	
	public User findByMobileForAppLogin(Long id) {
		return userDao.findByMobileForAppLogin(id);
	}
	
	public User findByMobileForPlatform(String mobile){
		return userDao.findByMobileForPlatform(mobile);
	}
	
	public User appLogin(String mobile, String loginPassword) {
		User user = userDao.findByMobileForPlatform(mobile);
		if (user != null) {
			if (PasswordUtils.checkPassword(loginPassword, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param mobile
	 * @param loginPassword
	 * @return
	 */
	public User loginForApp(String mobile, String loginPassword) {
		User user = userDao.findByMobileForPlatform(mobile);
		if (user != null) {
			if (PasswordUtils.checkPassword(loginPassword, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param mobile
	 * @param loginPassword
	 * @return
	 */
	public User login(String mobile, String loginPassword) {
		User user = userDao.findByMobile(mobile);
		if (user != null) {
			if (PasswordUtils.checkPassword(loginPassword, user.getPassword())) {
				return user;
			}
		}
		return null;
	}

	/**
	 * 更新登录密码
	 * 
	 * @param userId
	 * @param loginPassword
	 * @return 1成功，0失败
	 */
	public int updateLoginPassword(Long userId, String loginPassword) {
		return userDao.updateLoginPassword(userId, loginPassword);
	}

	/**
	 * 更新支付密码
	 * 
	 * @param userId
	 * @param payPassword
	 * @return 1成功，0 失败
	 */
	public int updatePayPassword(Long userId, String payPassword) {
		return userDao.updatePayPassword(userId, payPassword);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param User
	 * @return
	 */
	public int editUserInfo(User user) {
		return userDao.updateUser(user);

	}

	/**
	 * 保存或修改证书文件
	 * 
	 * @param certificates
	 * @param userId
	 */
	public void uploadCertificates(MultipartFile[] certificates, Long userId) {
		List<UserCer> userCerList = userDao.findCertificates(userId);

		if (userCerList != null && userCerList.size() > 0) {
			for (int i = 0; i < userCerList.size(); i++) {
				userDao.delCerByCerId(userCerList.get(i).getCerId(),userId);
				fileService.delete(userCerList.get(i).getCerId());
			}

		}
		InputStream in = null;
		for (int i = 0; i < certificates.length; i++) {
			if (StringUtils.isNotBlank(certificates[i].getOriginalFilename())) {
				try {
					in = certificates[i].getInputStream();
					String cerId = fileService.save(in);// 证书ID
					String cerName = certificates[i].getOriginalFilename();
					userDao.saveCertificate(cerId, userId, cerName);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						in.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public Map<String, String> findCertificates(Long userId) {
		List<UserCer> userCerList = userDao.findCertificates(userId);
		if (userCerList == null) {
			return null;
		} else {
			Map<String, String> cerUrlMap = new HashMap<String, String>();
			for (int i = 0; i < userCerList.size(); i++) {
				cerUrlMap.put(userCerList.get(i).getCerId(),
						FileServiceUtils.getFileUrl(userCerList.get(i).getCerId()));
			}
			return cerUrlMap;
		}
	}

	public void delCerByCerId(String cerId,Long userId) {
		userDao.delCerByCerId(cerId,userId);
		fileService.delete(cerId.toString());
	}

	public User findById(Long id) {
		return userDao.findById(id);
	}

	public List<UserBank> findBankByUserId(Long userId) {
		return userDao.findBankByUserId(userId);
	}

	public void editUserBank(UserBank userBank) {
		if (userBank.getBankId() == null) {
			userBank.setBankId(idService.getId());
			userDao.saveUserBank(userBank);
		} else {
			userDao.updateUserBank(userBank);
		}

	}

	public boolean ModifyStatus(Long id, Integer status) {
		return userDao.ModifyStatus(id, status) >= 1;
	}

	public void updateIcon(Long userId, String iconId) {
		userDao.updateIcon(userId, iconId);
	}
	
	public void updateName(Long userId,String name){
		userDao.updateName(userId, name);
	}

	public Page identityList(String mobile, Integer idAudit,Page page) {
		page = userDao.identityList( mobile,idAudit, page);
		return page;
	}

	public boolean modifyIdAudit(Long id, Integer idAudit) {
		return userDao.modifyIdAudit(id, idAudit) >= 1;
	}

	/**
	 * 用户余额
	 * @param userId
	 * @return
	 */
	public Double getBalance(Long userId) {
		PayAccount payAccount = payAccountDao.getByOwnerIdAndType(userId, Constants.PayAccount.PAY_ACCOUNT_TYPE_USER);
		if (payAccount == null || payAccount.getBalance() == null){
			return 0.0;
		}
		return payAccount.getBalance();
	}

	public Integer findAuditStatus(Long userId) {
		return userDao.findAuditStatus(userId);
	}
	
	public String getCid(Long userId){
		return userDao.getCid(userId);
	}
	@SuppressWarnings("finally")
	@Transactional
	public String rarFile(InputStream in, Long storeId) {
		String flag = "";
		
		try {
			List<List<String>> list = ExcelUtils.simpleExcel(in);// 读取model.xls
			logger.debug("行数==="+list.size());
			List<User> userList=new ArrayList<User>();
			if(list.size()<=1){
				flag = "请按照模板填写商品信息！";
				return flag;
			}else{
				for (int i = 0; i < list.size(); i++) {// 一行一行的读
					User user=new User();
					user.setStoreId(storeId);
					if (i > 0) {// 读第二行开始
						logger.debug("第"+i+"行");
						for (int j = 0; j < list.get(i).size(); j++) {// 每一行一格一格的读
							if (j == 0) {// 第一格获取卡号
								if (list.get(i).get(j) != null && !"".equals(list.get(i).get(j).trim())) {
									if(list.get(i).get(j).trim().length()<=255){
										logger.debug("卡号==="+list.get(i).get(j).trim());
										//列表中找
										String cardNumber=list.get(i).get(j).trim();
										if(userList.size()>0){
											int sort=0;
											boolean barCodeFlag=false;
											for(int k=0;k<userList.size();k++){
												if(cardNumber.equals(userList.get(k).getCardNumber())){//是否有相同的卡号
													sort=k;
													barCodeFlag=true;
													break;
												}
											}
											if(barCodeFlag==false){//没有相同的卡号
												//去数据库判断
												// 当前店铺有此卡号
												if(!this.byCarNumberFindUser(cardNumber,storeId).isEmpty()){
													flag += "第" + (i+1) + "行的卡号在此商店已存在,请检查！</br>";
													user.setCardNumber(cardNumber);//加进User
												}else{//当前店铺没有此卡号
													user.setCardNumber(cardNumber);//加进User
												}
											}else{//前面已存在
												flag += "第" + (i+1) + "行的卡号和前面第"+(sort+2)+"行相同,请检查！</br>";
												user.setCardNumber(cardNumber);//加进User
											}
										}else{
											//去数据库判断
											// 当前店铺有此卡号
											if(!this.byCarNumberFindUser(cardNumber,storeId).isEmpty()){
												flag += "第" + (i+1) + "行的卡号在此商店已存在,请检查！</br>";
												user.setCardNumber(cardNumber);//加进User
											}else{//当前店铺没有此卡号
												user.setCardNumber(cardNumber);//加进User
											}
										}
									}else{
										flag += "第" + (i+1) + "行第1格的卡号长度过长,请检查！</br>";
										user.setCardNumber(list.get(i).get(j));//加进User
									}
								}else{
									flag += "第" + (i+1) + "行第1格的卡号不能为空,请检查！</br>";
									user.setCardNumber(list.get(i).get(j));//加进User
								}
							}else if(j==1){//姓名
								logger.debug("姓名={}",list.get(i).get(j));
								if(list.get(i).get(j)!=null && !"".equals(list.get(i).get(j).trim())){
									if(list.get(i).get(j).trim().length()<=50){
										user.setName(list.get(i).get(j).trim());
										user.setRealName(list.get(i).get(j).trim());
									}else{
										flag += "第" + (i+1) + "行第1格的姓名长度过长,请检查！</br>";
									}
								}
							}else if(j==2){//性别
								if (list.get(i).get(j) !=null && !"".equals(list.get(i).get(j))) {
									if(list.get(i).get(j).trim().equals("男")){
										user.setSex(0);
									}else if(list.get(i).get(j).trim().equals("女")){
										user.setSex(1);
									}else{
										user.setSex(2);
									}
								}else{
									user.setSex(2);
								}
							}else if(j==3){//手机号码
								if (list.get(i).get(j) != null && !list.get(i).get(j).trim().equals("")) {
									if(list.get(i).get(j).trim().length()<=50 ){
										logger.debug("手机号码==="+list.get(i).get(j).trim());
										//列表中找
										String mobile=list.get(i).get(j).trim();
										if(NumberUtils.isNumber(mobile)){
											if(StringUtils.isMobile(mobile)){
												user.setMobile(mobile);//加进User
											}else{
												flag += "第" + (i+1) + "行的第4格的手机电话不正确,请检查！<br>";
												user.setMobile(mobile);//加进User
											}
											if(userList.size()>0){
												int sort=0;
												boolean barCodeFlag=false;
												for(int k=0;k<userList.size();k++){
													if(mobile.equals(userList.get(k).getMobile())){//是否有相同的手机号码
														sort=k;
														barCodeFlag=true;
														break;
													}
												}
												if(barCodeFlag==false){//没有相同的手机号码
													//去数据库判断
													// 当前店铺有此手机号码
													if(this.findByMobile(mobile,storeId)!=null){
														flag += "第" + (i+1) + "行的手机号码在数据库已存在,请检查！</br>";
														user.setMobile(mobile);//加进User
													}else{//当前店铺没有此手机号码
														user.setMobile(mobile);//加进User
													}
												}else{//前面已存在
													flag += "第" + (i+1) + "行的手机号码和前面第"+(sort+2)+"行相同,请检查！</br>";
													user.setMobile(mobile);//加进User
												}
											}else{
												//去数据库判断
												// 当前店铺有此手机号码
												if(this.findByMobile(mobile,storeId)!=null){
													flag += "第" + (i+1) + "行的第4格手机号码在数据库已存在,请检查！</br>";
													user.setMobile(mobile);//加进User
												}else{//当前店铺没有此手机号码
													user.setMobile(mobile);//加进User
												}
											}
										}else{
											flag += "第" + (i+1) + "行第4格的手机号码只能为数字,请检查！</br>";
											user.setMobile(mobile);//加进User
										}
						
									}else{
										flag += "第" + (i+1) + "行的第4格的手机号码长度过长,请检查！</br>";
										user.setMobile(list.get(i).get(j));//加进User
									}
								}else{
									flag += "第" + (i+1) + "行的第4格的手机号码不能为空,请检查！</br>";
									user.setMobile(list.get(i).get(j));//加进User
								}
							}else if(j==4){//积分
								if (list.get(i).get(j) != null && !"".equals(list.get(i).get(j).trim())) {
									if(list.get(i).get(j).trim().length()<=15){
										logger.debug("积分==="+list.get(i).get(j).trim());
										//列表中找
										String point="";
										if(NumberUtils.isNumber(list.get(i).get(j).trim())){
											if(list.get(i).get(j).trim().indexOf(".")>0){
												if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														point=list.get(i).get(j).trim();
														user.setPoint(Double.valueOf(point));
													}else{
														flag += "第" + (i+1) + "行第5格的积分必须大于或等于0,请检查！</br>";
														//不set进user
													}
												}else{
													flag += "第" + (i+1) + "行第5格的积分的小数位数不能大于2位,请检查！</br>";
													//不set进user
												}
											}else{
												Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
												if(d.doubleValue()>=0){
													point=list.get(i).get(j).trim();
													user.setPoint(Double.valueOf(point));
												}else{
													flag += "第" + (i+1) + "行第5格的积分必须大于或等于0,请检查！</br>";
													//不set进user
												}
											}
										}else{
											flag += "第" + (i+1) + "行第5格的积分只能为数字,请检查！</br>";
											//不set进user
										}
										
									}else{
										flag += "第" + (i+1) + "行第5格的积分长度过长,请检查！</br>";
										//不set进user
									}
								}
							}else if(j==5){//余额
								if (list.get(i).get(j) != null && !"".equals(list.get(i).get(j).trim())) {
									if(list.get(i).get(j).length()<=10 ){
										String balance="";
										if(NumberUtils.isNumber(list.get(i).get(j).trim())){
											if(list.get(i).get(j).trim().indexOf(".")>0){
												if(list.get(i).get(j).trim().substring(list.get(i).get(j).trim().lastIndexOf(".")+1,list.get(i).get(j).trim().length()).length()<=2){
													Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
													if(d.doubleValue()>=0){
														balance=list.get(i).get(j).trim();
														user.setBalance(Double.valueOf(balance));
													}else{
														flag = "第" + (i+1) + "行第6格的余额必须大于或等于0,请检查！";
														return flag;
													}
												}else{
													flag = "第" + (i+1) + "行第6格的余额小数位数不能大于2位,请检查！";
													return flag;
												}
											}else{
												Double d = new Double(Double.valueOf(list.get(i).get(j).trim()));
												if(d.doubleValue()>=0){
													balance=list.get(i).get(j).trim();
													user.setBalance(Double.valueOf(balance));
												}else{
													flag = "第" + (i+1) + "行第6格的余额必须大于或等于0,请检查！";
													return flag;
												}
											}
										}else{
											flag = "第" + (i+1) + "行第6格的余额只能为数字,请检查！";
											return flag;
										}
										
										
									}else{
										flag = "第" + (i+1) + "行第6格的余额长度过长,请检查！";
										return flag;
									}
								}else{
									flag = "第" + (i+1) + "行第6格的余额不能为空,请检查！";
									return flag;
								}
							}
							//结束
							
						}
						
							userList.add(user);
					}
				}
				
				if("".equals(flag)){
					
					//把数据会员数据插入数据库
					for(int k=0;k<userList.size();k++){
						userList.get(k).setId(idService.getId());
						this.saveUser(userList.get(k));
					}
					userDao.batchUpdate(userList);
					flag="1";
				}
			}
		//}
					
			
		} catch (Exception e) {
			logger.error("", e);
			flag= "导入失败，请联系客服！";
		} finally {
			/*if (null != in) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}*/
			logger.debug("delete file ................. ");
			//FileUtils.delete(_fileName);
			//logger.debug("_path=" + _path);
			//FileUtils.delete(_path);
			return flag;
		}
	}

	

	private void saveUser(User user) {
		userDao.saveUser(user);
		
	}

	/**
	 * APP-Third
	 * @param lastId 商家数据从这个ID开始,既必须大于此ID
	 * @param storeId 商家ID
	 */
	public List<User> getAssignUserByPartnerIdAndStoreId(Long lastId, Long storeId) {
		return userDao.getAssignUserByPartnerIdAndStoreId(lastId,storeId);
	}
	
	
	

	public User findByStoreIdAndMobile(Long storeId,String mobile) {
		User user = userDao.findUserByStoreIdAndMobile(storeId,mobile);
		// 直接在本店查询出来了。就直接返回用户信息，否则继续查询此商家是否有总店，
		// 如果没总店，则直接返回null
		// 如果有总店，查询是否是共享会员的。如果不是共享会员，则返回null
		// 如果是共享会员，则查询此总店下的所有店铺信息，然后根据店铺信息查询此手机号码是否存在于那些会员中
		if( null == user ){
		 	user = searchUserByStoreIdAndMobile(storeId,mobile);
		} 
		return user;
	}
	
	/**
	 * 
	 * @param storeId
	 * @param mobile
	 * @return
	 */
	public User searchUserByStoreIdAndMobile(Long storeId,String mobile){
		return userDao.searchUserByStoreIdAndMobile(storeId, mobile);
	}
	
	private List<User> byCarNumberFindUser(String cardNumber, Long storeId) {
		
		return userDao.byCarNumberFindUser(cardNumber,storeId);
	}

	public List<User> findUserNotPlatform() {
		return userDao.findUserNotPlatform();
	}

	@Transactional
	public void save(List<User> listUser) {
		List<Object[]> batchUser=new ArrayList<Object[]>();
		for(User user : listUser){
			Long id = idService.getId();
			Object[] obj=new Object[27];
			obj[0] = id;
			obj[1] = user.getMobile();
			obj[2] = user.getName();
			obj[3] = user.getSex();
			obj[4] = user.getDiscount();
			obj[5] = user.getCreatedTime();
			obj[6] = user.getCreatedId();
			obj[7] = null; // 将店级别转换成系统级别
			obj[8] = user.getPassword();
			obj[9] = user.getIdentifyTypeId();
			
			obj[10] = user.getIdentity();
			obj[11] = user.getNickName();
			obj[12] = user.getIconUrl();
			obj[13] = user.getPhoneNumber();
			obj[14] = user.getAddress();
			obj[15] = user.getMemo();
			obj[16] = user.getUpdateTime();
			obj[17] = user.getRealName();
			obj[18] = user.getStatus();
			obj[19] = null;
					
			obj[20] = user.getIdAudit();
			obj[21] = user.getCid();
			obj[22] = user.getRecommended();
			obj[23] = user.getPoint();
			obj[24] = user.getGrade();
			obj[25] = user.getCardNumber();
			obj[26] = user.getBalance();
			batchUser.add(obj);
		}
		logger.debug("执行转换用户的总数量是：{}",batchUser.size());
		userDao.save(batchUser);
	}
	
	/**
	 * 查询某一个手机号码是不是属于某一个商家或者商家下属的会员
	 * @param mobile 手机号码
	 * @param storeId 商家ID
	 * @return null或者会员对象
	 */
	public User searchOrganization_UserByMobile$StoreId(String mobile,Long storeId){
		/**
		 * 先根据手机号码查询出其所属的所有用户列表
		 * 再根据商家ID查询出商家信息
		 * 判断商家是否有组织架构,如果没有则直接循环用户列表匹配此商家ID
		 * 如果有组织架构,则根据组织架构ID查询最顶级的父级(总部)
		 * 根据总部查询其下的组织架构集合,如果查询不到下级组织架构集合,则直接循环用户列表匹配此商家ID
		 * 如果查询到了下级组织架构集合,则循环用户列表匹配下级组织架构集合中的商家ID
		 * 如果匹配到,则直接返回此用户信息
		 * 如果匹配不到,则根据下级组织架构集合中的组织架构ID查询系统商家中是否有以其为上级的,如果没有有,直接返回null
		 * 如果有,则循环用户列表匹配商家列表集合中的上级ID
		 * 如果匹配不到,则直接返回null
		 * 如果匹配到,则直接返回此用户信息
		 */
		List<User> listUser = userDao.findByMobile(mobile,false); 
		if( null == listUser || listUser.size() <= 0 ){
			return null; // 查找不到用户
		}
		
		Store store = storeService.findById(storeId);
		if( null == store ){
			return null; // 查找不到商家
		}
		
		Long organizationId = store.getOrganizationId();

		// 没有组织架构
		User isUser = null;
		if( null == organizationId ){ 
			 for ( User user : listUser ) {
				if( ObjectUtils.equals( storeId , user.getStoreId() ) ){
					isUser = user;
					break;
				}
			 }
		} else {
			// 有组织架构
			List<Organization> organizationList =  organizationService.getParentTreeById(organizationId, true);
			// 根据组织架构ID查询其下所属的下级组织架构系统
			List<Organization> list = organizationService.getChildrenTreeById(organizationList.get(0).getId(), false);
			if( null != list && list.size() > 0 ){
				boolean isFlag = true; // 默认设置找不到
				// 查询组织架构中是否存在当前登录用户所属的商家,有则直接跳出判断
				for ( User user : listUser ) {
					for (Organization organization : list) {
						if( ObjectUtils.equals( user.getStoreId() , organization.getStoreId() ) ){
							isFlag = false;
							isUser = user;
							break;
						}
					}
					if(!isFlag){ 
						break;
					}
				}
				
				// 如果组织架构中查找不到,则查询商家中以此组织架构集合为上级的商家信息,再从中查询是否存在当前登录用户所属的商家
				if(isFlag){
					logger.debug("organizationSystem is not , continue search organizationBelongStore info...");
					
					List<Store> tempStoreArray = shopService.searchOrganization_StoreByorganizationList(list);
					if( null != tempStoreArray && tempStoreArray.size() > 0 ){
						for ( User user : listUser ) {
							for (Store tempStore : tempStoreArray) {
								if( ObjectUtils.equals( user.getStoreId() , tempStore.getId() ) ){
									isFlag = false;
									isUser = user;
									break;
								}
							}
							if(!isFlag){
								break;
							}
						}
					}
				}
				
				logger.debug("in the end isFlag is {} ",isFlag);
			} else {
				for ( User user : listUser ) {
					if( ObjectUtils.equals( storeId , user.getStoreId() ) ){
						isUser = user;
						break;
					}
				 }
			}
			
		}
		logger.debug("finally retrun isUser is {}",isUser);
		return isUser;
	}
	
	/**
	 * 返回平台级别的用户信息(如果没有，则返回null)
	 * @param mobile
	 * @param storeId
	 * @param createPlatform
	 * @return
	 */
	@Transactional
	public User createUserToStore$Platform(String mobile, Long storeId,boolean createPlatform,boolean returnPlatform) {
		User platform$User = null;
		if(returnPlatform){
			// 商家创建
			User user = new User();
			user.setMobile(mobile);
			user.setPassword(mobile);
			user.setStoreId(storeId);
			save(user);
			
			if(createPlatform){
				// 平台创建
				platform$User = new User();
				platform$User.setMobile(mobile);
				platform$User.setPassword(mobile);
				save(platform$User);			
			}
		} else {
			// 商家创建
			platform$User = new User();
			platform$User.setMobile(mobile);
			platform$User.setPassword(mobile);
			platform$User.setStoreId(storeId);
			save(platform$User);
			
			if(createPlatform){
				// 平台创建
				User platform = new User();
				platform.setMobile(mobile);
				platform.setPassword(mobile);
				save(platform);			
			}
		}
		return platform$User;
	}
	
	public User returnStoreUserByStoreId$Mobile(Long storeId, String mobile) {
		User user = searchOrganization_UserByMobile$StoreId(mobile, storeId);
		
		if( null == user ){
			// 查找平台用户是否存在
			user = findByMobileForPlatform(mobile);
			if( null == user ){
				user = createUserToStore$Platform(mobile, storeId, true,false);				
			} else {
				user = createUserToStore$Platform(mobile, storeId, false,false);
			}
		}
		return user;
	}

	/**
	 * 保存商家用户
	 * @param user
	 */
	@Transactional
	public void saveForStore(User user){
		User platformUser = findByMobileForPlatform(user.getMobile());
		if (platformUser == null){
			//创建平台用户
			platformUser = new User();
			platformUser.setMobile(user.getMobile());
			platformUser.setPassword(user.getMobile());
			save(platformUser);
		}
		
		save(user);
		
		//添加会员卡
		UserCard userCard = new UserCard();
		userCard.setAccountId(user.getCreatedId());
		userCard.setCreatedTime(new Date());
		userCard.setBalance(0D);
		userCard.setStoreId(user.getStoreId());
		Long id = idService.getId();
		userCard.setId(id);
		userCard.setCardNum(id.toString());
		userCard.setMobile(user.getMobile());
		userAccountManagerService.save(userCard, 0);
	}
	
	/**
	 * 根据会员卡id查找平台用户
	 * @param userCardId
	 * @return
	 */
	public User findPlatformUserByUserCardId(Long userCardId){
		return userDao.findPlatformUserByUserCardId(userCardId);
	}
	
	public User findUserForChainStore(String mobile, Long storeId){
		return userDao.findUserForChainStore(mobile, storeId);
	}

	/**
	 * 根据会员ID查找会员信息
	 * @param userId 会员ID
	 * @return null或者会员对象
	 */
	public User findByUserId(Long userId) {
		return userDao.findByUserId(userId);
	}

	public List<User> findUserListByBirthDay() {
		
		return userDao.findUserListByBirthDay();
	}
}