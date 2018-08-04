package cn.lastmiles.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.common.utils.QRCodeUtil;
import cn.lastmiles.common.utils.StringUtils;

@Controller
@RequestMapping("qrcode")
public class QRCodeController {
	
	@RequestMapping("born")
	public String born(String content,Integer logo,Integer qrcode, HttpServletResponse response)
			throws IOException {
		if (StringUtils.isNotBlank(content)) {
			try {
				response.setContentType("application/octet-stream");
				 response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("lastmiles_"+qrcode+"_"+logo+".png", "UTF-8"));  
				OutputStream out = response.getOutputStream();
				QRCodeUtil.encode(content, "http://www.lastmiles.cn/icon.png", out, true,logo, qrcode);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
