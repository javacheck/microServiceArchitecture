package cn.lastmiles.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.utils.FileServiceUtils;

@Controller
@RequestMapping("file")
public class FileController {
	@Autowired
	private FileService fileService;

	@RequestMapping("image/{id}")
	public String image(@PathVariable String id, HttpServletResponse response)
			throws IOException {
		if (StringUtils.isNotBlank(id)) {
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
		}
		return null;
	}

	@RequestMapping("uploadImage")
	@ResponseBody
	public Map<String, Object> uploadImage(
			@RequestParam("imgFile") MultipartFile[] imgFile,
			HttpServletRequest request) throws IOException {
		String id = fileService.save(imgFile[0].getInputStream());
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("error", 0);
		ret.put("url", FileServiceUtils.getFileUrl(id));
		return ret;
	}
}
