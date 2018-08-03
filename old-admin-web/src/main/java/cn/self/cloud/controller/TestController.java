package cn.self.cloud.controller;

import java.io.IOException;
import java.io.InputStream;

import cn.self.cloud.commonutils.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("test")
@Controller
public class TestController {
//	@Autowired
//	private FileService fileService;

	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String upload() {
		return "test/upload";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String upload(MultipartFile image, Model model) throws IOException {
		InputStream in = image.getInputStream();
		String id = null; //fileService.save(in);
		in.close();
		model.addAttribute("fileId", id);
		return "test/upload";
	}
}
