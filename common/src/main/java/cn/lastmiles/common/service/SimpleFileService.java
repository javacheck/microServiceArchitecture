package cn.lastmiles.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import cn.lastmiles.common.utils.DateUtils;
import cn.lastmiles.common.utils.StringUtils;
import jodd.io.FileUtil;
import jodd.io.StreamUtil;

public class SimpleFileService implements FileService {
	private String defaultPath;
	private String fileServer;
	private String prefix;

	public String getStorePath() {
		return defaultPath;
	}

	public String getFileServer() {
		return fileServer;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public SimpleFileService(String defaultPath, String fileServer,
			String prefix) {
		super();
		this.defaultPath = defaultPath;
		this.fileServer = fileServer;
		this.prefix = prefix;
	}

	public SimpleFileService(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	@Override
	public String save(InputStream in) {
		return save(in,null);
	}

	@Override
	public InputStream get(String id) {
		try {
			String date = id.substring(0, 8);
			String filename = id.substring(8);
			return new FileInputStream(defaultPath + "/" + date + "/"
					+ filename);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void write(String id, OutputStream out) {
		InputStream in = null;
		try {
			in = get(id);
			StreamUtil.copy(in, out);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setStorePath(String storePath) {
		this.defaultPath = storePath;
	}

	@Override
	public void delete(String id) {
		if (StringUtils.isNotBlank(id)) {
			String date = id.substring(0, 8);
			String filename = id.substring(8);
			File f = new File(defaultPath + "/" + date + "/" + filename);
			if (f.exists() && f.isFile()) {
				f.delete();
			}
		}
	}

	public String getFileUrl(String id) {
		throw new RuntimeException("请使用FileServiceUtils");
	}

	@Override
	public String save(InputStream in, String id) {
		String currentDate = DateUtils.format(new Date(), "yyyyMMdd");
		if (id == null){
			id = StringUtils.uuid();
		}
		try {
			String path = defaultPath + "/" + currentDate;
			FileUtil.mkdir(path);
			FileUtil.writeStream(path + "/" + id, in);
			return currentDate + id;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
