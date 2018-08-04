package cn.lastmiles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lastmiles.bean.Area;
import cn.lastmiles.service.AreaService;

@Controller
@RequestMapping("area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	
	@RequestMapping("list")
	@ResponseBody
	public List<Area> list(Long parentId) {
		return areaService.findByParent(parentId);
	}
	
	@RequestMapping(value = "path")
	@ResponseBody
	public String GetPath(Long id){
		Area area =areaService.findById(id);
		return area.getPath();
	}
}
