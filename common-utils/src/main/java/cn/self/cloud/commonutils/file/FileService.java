package cn.self.cloud.commonutils.file;

import java.io.InputStream;
import java.io.OutputStream;
/**
 * 文件操作的根接口
 */
public interface FileService {
	/**
	 * 指定id的保存
	 * @param in
	 * @param id
	 * @return
	 */
	String save(InputStream in, String id);

	/**
	 * 保存流数据
	 * @param in 输入流
	 * @return
	 */
	String save(InputStream in);

	/**
	 * 根据id标识获取流数据
	 * @param id 标识id
	 * @return 输入流对象
	 */
	InputStream get(String id);

	/**
	 * 写数据
	 * @param id 标识id
	 * @param out 输出流
	 */
	void write(String id, OutputStream out);

	void delete(String id);

	String getFileServer();

	String getPrefix();
}
