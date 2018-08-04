package cn.lastmiles.common.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileService {
	/**
	 * 指定id的保存
	 * @param in
	 * @param id
	 * @return
	 */
	public String save(InputStream in,String id);
	
	public String save(InputStream in);

	public InputStream get(String id);

	public void write(String id, OutputStream out);

	public void delete(String id);

	public String getFileServer();

	public String getPrefix();
}
