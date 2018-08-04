package cn.lastmiles.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lastmiles.common.service.FileService;

@Controller
@RequestMapping("file")
public class FileController {
	@Autowired
	private FileService fileService;

	@RequestMapping("image/{id}")
	public String image(@PathVariable String id, HttpServletResponse response)
			throws IOException {
		try {
			InputStream in = fileService.get(id);
			response.setContentType("application/octet-stream");
			OutputStream out = response.getOutputStream();
			IOUtils.copy(in, out);
			in.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
