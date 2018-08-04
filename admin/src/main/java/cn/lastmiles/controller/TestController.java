package cn.lastmiles.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

import cn.lastmiles.bean.User;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.service.FileService;
import cn.lastmiles.init.TestService;
import cn.lastmiles.mq.MQService;

@RequestMapping("test")
//@Controller
public class TestController {
	@Autowired
	private MQService mqService;
	@Autowired
	private FileService fileService;
	@Autowired
	private TestService testService;
	@Autowired
	private cn.lastmiles.dao.temporaryDao temporaryDao;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	@Autowired
	private CacheService cacheService;
	
	public String code(){
		return null;
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String upload() {
		return "test/upload";
	}
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String test() {
		System.out.println("test .............2");
		
		temporaryDao.dealStockIsNull();

		System.out.println("test .............2end");
		return "test/test";
	}
	
	@RequestMapping(value="test2",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String test2(){
		System.out.println("test2 ....");
//		mqService.sendMessage("chat", new User());
		return "asdfd";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile image, Model model) throws IOException {
		InputStream in = image.getInputStream();
		IOUtils.copy(in, new FileOutputStream("c://" + image.getName()));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "1001");
		map.put("msg", "00");
		return map;
	}
	
	
	
	@RequestMapping(value="onloadData",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String onloadData(Model model){
		List<String> data=new ArrayList<String>();
		try{ 
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			String url="jdbc:mysql://192.168.80.58:3306/test"; 
			String user="lmuser"; 
			String password="lmuser"; 
			
			Connection conn= (Connection) DriverManager.getConnection(url,user,password); 
			Statement stmt=(Statement) conn.createStatement(); 
			String sql="select * from t_product"; 
			ResultSet rs=stmt.executeQuery(sql); 
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData(); 
			 //获取字段名 
			for(int i=1;i<=rsmd.getColumnCount();i++){
				data.add(rsmd.getColumnName(i));
				System.out.println(rsmd.getColumnName(i));
			}
			model.addAttribute("data", data);
		 }catch(Exception e){ 
			 e.printStackTrace(); 
		 }
		return "test/test";
	}
}
