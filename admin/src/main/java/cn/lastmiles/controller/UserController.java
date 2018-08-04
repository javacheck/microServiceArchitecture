package cn.lastmiles.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.lastmiles.bean.User;
import cn.lastmiles.common.jdbc.Page;
import cn.lastmiles.common.utils.ConfigUtils;
import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.FileUtils;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.StoreService;
import cn.lastmiles.service.UserService;
import cn.lastmiles.service.sms.SMSService;
import cn.lastmiles.service.sms.SMSTemplate;
import cn.lastmiles.utils.SecurityUtils;

@Controller
@RequestMapping("user")
public class UserController {
	private final static Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@Autowired
	private SMSService sMSService; // 注入手机接口
	
	@RequestMapping("")
	public String index() {
		return "redirect:/user/list";
	}

	@RequestMapping("list")
	public String list() {
		return "user/list";
	}

	@RequestMapping("list-data")
	public String listData(String beginTime,String endTime,String mobile,String name, Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		name = name.replaceAll("\\s*", "");
		Long storeId=0L;
		if (SecurityUtils.isAdmin()) {
			storeId=null;
		}
		if (SecurityUtils.isStore()) {
			storeId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("data", userService.list(beginTime,endTime,name,mobile,storeId, page));
		return "user/list-data";
	}

	/**
	 * 数据 修改||添加 界面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(User user, Model model) {
		if (null != user.getId()) {
			String info = user.getId()
					+ (userService.update(user) ? "修改成功" : "修改失败");
			logger.debug(info);
		} else {
			user.setCreatedId(SecurityUtils.getAccountId());
			userService.save(user);
		}
		return "redirect:/user/list";
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String toAdd(User user, Model model) {
		model.addAttribute("stores",storeService.findMyStore(SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId()));
		return "/user/add";
	}

	/**
	 * 跳转修改界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String toUpdate(@PathVariable Long id, Model model) {
		model.addAttribute("stores",storeService.findMyStore(SecurityUtils.getAccountId(),SecurityUtils.getAccountStoreId()));
		model.addAttribute("User", userService.findById(id));
		return "/user/add";
	}

	/**
	 * 通过ID删除 数据
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ajax/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id, Model model) {
		return  userService.delete(id) ? "0" : "1";
	}
	/**
	 * 修改会员状态
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ajax/modifyStatus/{id}")
	@ResponseBody
	public String modifyStatus(@PathVariable Long id,Integer status, Model model) {
		return  userService.ModifyStatus(id, status) ? "1" : "0";
	}
	
	@RequestMapping(value = "checkMobile")
	@ResponseBody
	public String checkMobile(Long storeId, String mobile) {
		return userService.findByMobileAndStoreId(mobile,storeId)!=null?"1":"0";
	}
	
	@RequestMapping("identityList/list")
	public String identityList() {
		return "user/identityList";
	}

	@RequestMapping("identityList/list-data")
	public String identityListData(String mobile,Integer idAudit,Page page, Model model) {
		mobile = mobile.replaceAll("\\s*", "");
		model.addAttribute("data", userService.identityList(mobile,idAudit, page));
		return "user/identityList-data";
	}
	
	/**
	 *证件审核是否通过
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ajax/modifyIdAudit/{id}")
	@ResponseBody
	public String modifyIdAudit(@PathVariable Long id,Integer idAudit, Model model) {
		logger.debug("idAudit=="+idAudit);
		if(userService.modifyIdAudit(id, idAudit)){
			if(idAudit.intValue()==2){
				logger.debug("手机号码="+userService.findById(id).getMobile());
				// 调用手机接口发送验证码给用户
				sMSService.send(SMSTemplate.IdAUDITFAIL,null, userService.findById(id).getMobile());
			}
			return "1";
		}else{
			return "0";
		}
	}
	

    
	
	
	@RequestMapping("uploadList/list")
	public String uploadList(Model model) {
		Long isStoreId=null;
		if(SecurityUtils.isStore()){//如果是商家登录
			isStoreId=SecurityUtils.getAccountStoreId();
		}
		model.addAttribute("isSys",SecurityUtils.isAdmin());
		model.addAttribute("isStoreId",isStoreId);
		return "user/uploadList";
	}
	
	/**
	 * poi导出excel
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "list/ajax/list-by-search", produces = MediaType.APPLICATION_JSON_VALUE)
	public void findAttributeBySearch(HttpServletResponse response) throws ParseException {
		
		
		String fileName = "model";
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("会员导入模板");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = null;
		
		for(int i=0;i<5;i++){
			if(i==0){
				cell = row.createCell(i);// 第一行第1格
				cell.setCellValue("卡号");
				cell.setCellStyle(style);
			}else if(i==1){
				cell = row.createCell(i);// 第一行第2格
				cell.setCellValue("姓名");
				cell.setCellStyle(style);
			}else if(i==2){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("性别");
				cell.setCellStyle(style);
			}else if(i==3){
				cell = row.createCell(i);// 第一行第3格
				cell.setCellValue("手机号码");
				cell.setCellStyle(style);
			}else if(i==4){
				cell = row.createCell(i);// 第一行第4格
				cell.setCellValue("积分");
				cell.setCellStyle(style);
			}else if(i==5){
				cell = row.createCell(i);// 第一行第5格
				cell.setCellValue("余额");
				cell.setCellStyle(style);
			}
		}
		String [] tureOrfalse={"男","女"};
		@SuppressWarnings("deprecation")
		CellRangeAddressList regions1 = new CellRangeAddressList(1, 65535,2, 2);
	    DVConstraint constraint1 = DVConstraint.createExplicitListConstraint(tureOrfalse);
	    HSSFDataValidation data_validation1 = new HSSFDataValidation(regions1,constraint1);
	    sheet.addValidationData(data_validation1);
		
		
		try {
			// FileOutputStream fout = new FileOutputStream("F:/students.xls");
			// wb.write(fout);
			// fout.close();
			// 输出工作簿
			// 这里使用的是 response 的输出流，如果将该输出流换为普通的文件输出流则可以将生成的文档写入磁盘等
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(
									(((null == fileName) || ("".equals(fileName
											.trim()))) ? ((new Date().getTime()) + "")
											: fileName.trim())
											+ ".xls", "utf-8"));
			// 将工作簿进行输出
			wb.write(os);
			wb.close();
			os.flush();
			// 关闭输出流
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * rar文件上传
	 * 
	 * @param 
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "save1", method = RequestMethod.POST)
	public String zipOrRarUpload(MultipartFile zipOrRarFile,Long storeId,RedirectAttributes redirectAttributes,HttpSession session) throws IOException {
		if(SecurityUtils.isStore()){//如果是商家登录
			storeId=SecurityUtils.getAccountStoreId();
		}
		logger.debug("店铺=={}",storeId);
		String uploadPath=ConfigUtils.getProperty("file.uploadPath");
		File f = new File(uploadPath);
		// 如果文件夹不存在则创建
		if (!f.exists() && !f.isDirectory()) {
			f.mkdir();
		}
		String uuid = StringUtils.uuid();
		String _fileName = uploadPath + File.separator +  uuid + "."
				+ FileUtils.getExtension(zipOrRarFile.getOriginalFilename());
		logger.debug("_fileName={}",_fileName);
		
		zipOrRarFile.transferTo(new File(_fileName));
		
		Long  sId = storeId;
		session.setAttribute("userImportResult", "-2");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				String flag;
				try {
					InputStream in=new FileInputStream(_fileName);
					flag = userService.rarFile(in,sId);
					session.setAttribute("userImportResult", flag);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		return "redirect:/user/uploadList/list";
	}
	
	@RequestMapping(value="importResult",produces="plain/text;charset=utf-8")
	@ResponseBody
	public String importResult(HttpSession session){
		//logger.debug("session.getAttribute().toString()=={}",session.getAttribute("userImportResult").toString());
		String obj = (String)session.getAttribute("userImportResult");
		if (!obj.equals("-2")){
			session.removeAttribute("userImportResult");
		}
		return obj;
	}
	
	@RequestMapping(value = "ajax/reportOrderToExcel")
	public String reportOrderToExcel(HttpServletResponse response, String reportBeginTime,String reportEndTime,String reportName,String reportMobile) {
		List<User> userList = userService.list(SecurityUtils.isAdmin() ? null : (SecurityUtils.isStore() ? SecurityUtils.getAccountStoreId() : null) ,reportBeginTime,reportEndTime,reportName,reportMobile);
		
		String fileOrTitleName = "会员列表";
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream os = response.getOutputStream();
			// 这个是弹出下载对话框的关键代码
//			response.setContentType("application/vnd.ms-excel"); 
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileOrTitleName.trim() + ".xls", "utf-8"));
			//暂不考虑大数据量的问题和Excel版本的问题(2015.12.16)
			exportExcel(workbook,fileOrTitleName,getHeaders(),userList);
            workbook.write(os);
            workbook.close();
            os.flush();
            os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * EXCEL表头
	 * @return
	 */
	public String[] getHeaders() {  
        return new String[] { "手机号码","姓名","性别","创建时间","推荐人" };  
	} 
	
	public void exportExcel(HSSFWorkbook workbook, String title, String[] headers, Collection<?> dataset) {
	     
	      HSSFSheet sheet = workbook.createSheet(title);
	      sheet.setDefaultColumnWidth(20);
	      HSSFCellStyle style = workbook.createCellStyle();
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	      
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (int i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      
	    if(!dataset.isEmpty()){
	    	Iterator<?> it = dataset.iterator();
	    	int index = 0;
	    	while(it.hasNext()){
	    		index++;
	    		row = sheet.createRow(index);
	    		row.setRowStyle(style);
	    		User user = (User) it.next();
	    		row.createCell(0).setCellValue(user.getMobile()); // 手机号码
	    		row.createCell(1).setCellValue(user.getRealName()); // 姓名
	    		row.createCell(2).setCellValue(user.getSex().intValue() == 0  ? "男" : user.getSex().intValue() == 1 ? "女" : "保密"  ); // 性别
	    		row.createCell(3).setCellValue(DateUtils.format(user.getCreatedTime(),"yyyy-MM-dd HH:mm:ss")); // 创建时间
	    		row.createCell(4).setCellValue(user.getRecommended()); // 推荐人
	    	}
	    	logger.debug("共导出用户数据:{}条",index);
	    	index = 0;
	    }
	}
}
