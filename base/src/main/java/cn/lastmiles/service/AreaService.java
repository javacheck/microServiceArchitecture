package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Area;
import cn.lastmiles.dao.AreaDao;

@Service
public class AreaService {
	@Autowired
	private AreaDao areaDao;

	public List<Area> findByParent(Long parentId) {
		return areaDao.findByParent(parentId);
	}

	public Area findById(Long id) {
		return areaDao.findById(id);
	}
}
