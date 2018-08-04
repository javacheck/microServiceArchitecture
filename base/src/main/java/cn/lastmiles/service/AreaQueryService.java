/**
 * createDate : 2016年10月10日下午2:29:33
 */
package cn.lastmiles.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.dao.AreaQueryDao;

@Service
public class AreaQueryService {
	@Autowired
	private AreaQueryDao areaQueryDao;
	
	public List<Map<String, Object>> getProvinceAll(){
		return areaQueryDao.getprovinceAll();
	}
	
	public List<Map<String, Object>> getCityByProvinceID(String provinceID){
		return areaQueryDao.getCityByProvinceID(provinceID);
	}
	
	public List<Map<String, Object>> getDistrictByCityID(String cityID) {
		return areaQueryDao.getDistrictByCityID(cityID);
	}

	public List<Map<String, Object>> getShopCategory() {
		return areaQueryDao.getShopCategory();
	}

}
