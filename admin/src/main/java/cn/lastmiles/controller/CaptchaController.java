package cn.lastmiles.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import cn.lastmiles.utils.WebUtils;

@Controller
public class CaptchaController {
	@Autowired
	private Producer captchaProducer;

	@RequestMapping("captcha")
	public String handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// Set to expire far in the past.
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		String captcha = captchaProducer.createText();
		WebUtils.setAttributeToSession(Constants.KAPTCHA_SESSION_KEY, captcha);
		BufferedImage bi = captchaProducer.createImage(captcha);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		out.flush();
		out.close();
		return null;
	}
}
