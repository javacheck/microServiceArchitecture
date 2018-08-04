package cn.lastmiles.getui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.XingeApp;

import cn.lastmiles.bean.Account;
import cn.lastmiles.bean.Message;
import cn.lastmiles.bean.Order;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.dao.MyMessageDao;
import cn.lastmiles.dao.UserDao;
import cn.lastmiles.service.AccountService;
import cn.lastmiles.service.OrderServise;

@Service
public class PushService {
	private static final Logger logger = LoggerFactory
			.getLogger(PushService.class);
	
	//android
	private static final Long ACCESSID = 2100144125L;
	private static final String SECRETKEY = "f811e840b968253a304490240b0149f2";
	
	//ios
	private static final Long IOSACCESSID = 2200144126l;
	private static final String IOSSECRETKEY = "2842fcb707cd850deab3b6193266eedb";
	
	private static final Long STOREACCESSID = 2100150404L;
	private static final String STORESECRETKEY = "c0b56cdddc50ea302e15bc459d3baebc";
	
	private static ThreadPoolTaskExecutor executor;
	private static UserDao userDao;
	private static MyMessageDao messageDao;
	private static IdService idService;
	private static AccountService accountService;
	private static OrderServise orderServise;

	@Autowired
	public PushService(ThreadPoolTaskExecutor e, UserDao uDao,
			MyMessageDao msgDao, IdService is,AccountService as,OrderServise os) {
		logger.debug("push service init ................... ");
		executor = e;
		userDao = uDao;
		messageDao = msgDao;
		idService = is;
		accountService=as;
		orderServise=os;
	}
	/**
	 * 推送到 用户APP
	 * @param msg
	 */
	public static void pushToSingle(Message msg) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.debug("send message {}",msg);
				String cid = userDao.getCid(msg.getUserId());
				if (msg.getMessageId() == null){
					msg.setMessageId(idService.getId());
				}
				messageDao.save(msg);
				if (StringUtils.isNotBlank(cid)) {
					JSONObject ret = null;
					if (cid.startsWith("ios:")){
						cid = cid.substring(4);
						ret = XingeApp.pushTokenIos(IOSACCESSID, IOSSECRETKEY, msg.getTitle(), cid, XingeApp.IOSENV_DEV);
					}else {
						XingeApp xinge = new XingeApp(ACCESSID, SECRETKEY);
						ClickAction clickAction = new ClickAction();
						clickAction.setActionType(ClickAction.TYPE_ACTIVITY);
						clickAction.setActivity("cn.lastmiles.community.activity.mine.MyMessageActivity");
						com.tencent.xinge.Message _msg = new com.tencent.xinge.Message();
						_msg.setTitle(msg.getTitle());
						_msg.setContent(msg.getMessage());
						_msg.setType(com.tencent.xinge.Message.TYPE_NOTIFICATION);
						_msg.setExpireTime(86400);
						_msg.setAction(clickAction);
						ret = xinge.pushSingleDevice(cid, _msg);
					}
					
					if (ret != null) {
						logger.debug(ret.toString());
					} else {
						logger.error("服务器响应异常");
					}
				}
			}
		});
	}
	
	/**
	 * 推送到 商户APP
	 * @param msg
	 * 根据storeId 去查找这个订单所属店铺所有登陆的账号 发送推送
	 * 如storeId 为null 则 会根据ownerId去查找
	 */
	public static void pushToStore(Message msg) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.debug("send message {}",msg);
				List<Account> accounts=new ArrayList<Account>();
				if (msg.getStoreId()==null&&msg.getOwnerId()!=null) {
					Order order = orderServise.findById(msg.getOwnerId());
					if (order!=null&&order.getStoreId()!=null) {
						msg.setUserId(order.getUserId());
						msg.setStoreId(order.getStoreId());
					}
				}
				accounts = accountService.findByStoreId(msg.getStoreId());
				for (Account account : accounts) {
					if (msg.getMessageId() == null){
						msg.setMessageId(idService.getId());
					}
					//messageDao.save(msg);
					if (StringUtils.isNotBlank(account.getCid())) {
						XingeApp xinge = new XingeApp(STOREACCESSID, STORESECRETKEY);
						ClickAction clickAction = new ClickAction();
						clickAction.setActionType(ClickAction.TYPE_ACTIVITY);
//						clickAction.setActivity("cn.lastmiles.shop.");
						com.tencent.xinge.Message _msg = new com.tencent.xinge.Message();
						_msg.setTitle(msg.getTitle());
						_msg.setContent(msg.getMessage());
						_msg.setType(com.tencent.xinge.Message.TYPE_NOTIFICATION);
						_msg.setExpireTime(86400);
						_msg.setAction(clickAction);
						JSONObject ret = xinge.pushSingleDevice(account.getCid(), _msg);
						
						if (ret != null) {
							logger.debug(ret.toString());
						} else {
							logger.error("服务器响应异常");
						}
					}
				}
			}
		});
	}

	public static void main(String[] args){
		XingeApp xinge = new XingeApp(IOSACCESSID, IOSSECRETKEY);
//		ClickAction clickAction = new ClickAction();
//		clickAction.setActionType(ClickAction.TYPE_ACTIVITY);
		com.tencent.xinge.MessageIOS _msg = new com.tencent.xinge.MessageIOS();
//		_msg.setTitle("测试标题20150828");
//		_msg.setContent("测试内容20150828");
//		_msg.setType(com.tencent.xinge.Message.TYPE_NOTIFICATION);
		_msg.setExpireTime(86400);
		_msg.setAlert("测试信息");
//		JSONObject ret = xinge.pushSingleDevice("4b16af0629e82ef149ffe2628eef8ce0c871d2a9d9cc0e0e8279c979720b658b", _msg, XingeApp.IOSENV_DEV);
		
//		System.out.println(ret.toString());
		
		System.out.println("ios:sfsda".substring(4));
		
		JSONObject ret = XingeApp.pushTokenIos(IOSACCESSID, IOSSECRETKEY, "你好!", "4b16af0629e82ef149ffe2628eef8ce0c871d2a9d9cc0e0e8279c979720b658b", XingeApp.IOSENV_DEV);
		System.out.println(ret.toString());
	}
}
