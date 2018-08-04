package cn.lastmiles.service;
/**
 * updateDate : 2015-07-17 AM 10:32
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.APK;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.dao.ApkDao;

@Service
public class ApkService {
	@Autowired
	private ApkDao apkDao;
	@Autowired
	private IdService idService;
	
	public APK getLastest(int type){
		return apkDao.getLastest(type);
	}
	
	/**
	 * APK查询
	 * @param page
	 */
	public Page list(Page page) {
		return apkDao.list(page);
	}

	/**
	 * 检测APK名称是否唯一
	 * @param name APK名称
	 * @return APK对象或者null
	 */
	public boolean checkApkName(Long id,String name) {
		return apkDao.checkApkName(id,name);
	}
	
	/**
	 * 保存APK信息
	 * @param apk APK对象
	 */
	public Boolean save(APK apk) {
		apk.setId(idService.getId());
		return apkDao.save(apk);
	}
	
	public Boolean update(APK apk) {
		return apkDao.update(apk);
	}

	public APK findById(Long id) {
		return apkDao.findById(id);
	}
}