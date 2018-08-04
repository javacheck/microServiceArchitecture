package cn.lastmiles.ufile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jodd.io.StreamUtil;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lastmiles.common.service.FileService;
import cn.lastmiles.common.utils.StringUtils;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.DeleteSender;
import cn.ucloud.ufile.sender.GetSender;
import cn.ucloud.ufile.sender.PutSender;

public class UFileService implements FileService {
	private static final Logger logger = LoggerFactory
			.getLogger(UFileService.class);

	public UFileService() {

	}

	public UFileService(String bucketName) {
		super();
		this.bucketName = bucketName;
	}

	private String bucketName = "lastmiles";

	public static void main(String[] args) throws FileNotFoundException {
		// String base = "admin/";
		// String dirPath =
		// "E:/workspace/lastmiles-admin/src/main/webapp/WEB-INF/static/images";
		//
		//
		// File dir = new File(dirPath);
		//
		// Collection<File> files =
		// org.apache.commons.io.FileUtils.listFiles(dir, new
		// String[]{"jpg","woff","svg","eot","rar","csv","html","js","png","css","ttf","gif"},
		// true);
		//
		// for (File f : files){
		// String filename = base +
		// f.getPath().substring(f.getPath().indexOf("images")).replaceAll("\\\\",
		// "/");
		// System.out.println(filename);
		// saveStatic(new FileInputStream(f), filename);
		// }
		//
//		Collection<File> files = org.apache.commons.io.FileUtils.listFiles(
//				new File("f://logo"), new String[] { "jpg" }, true);
//		for (File f : files) {
//			System.out.println(f.getName());
//			saveStatic(new FileInputStream(f), "bank/" + f.getName());
//		}
//		String id = StringUtils.uuid();
//		 saveStatic(new FileInputStream("f://1.gif"),
//		 id);
//		 System.out.println(id);cc39f06939b1469586df15789506bb81
//		InputStream is = new UFileService().get("cc39f06939b1469586df15789506bb81");
//		try {
//			StreamUtil.copy(is, new FileOutputStream("f://222.gif"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static void saveStatic(InputStream in, String filename) {
		UFileClient ufileClient = new UFileClient();
		HttpClient httpClient = new DefaultHttpClient();
		ufileClient.setHttpClient(httpClient);

		UFileRequest request = new UFileRequest();
		request.setBucketName("lastmiles");
		request.setKey(filename);

		try {
			request.setInputStream(in);
			request.setContentLength(0L + in.available());

			PutSender sender = new PutSender();
			sender.makeAuth(ufileClient, request);
			UFileResponse response = sender.send(ufileClient, request);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {

			} else {
				throw new RuntimeException("保存文件出错");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ufileClient.getHttpClient().getConnectionManager().shutdown();
		}
	}

	@Override
	public String save(InputStream in) {
		return save(in, null);
	}

	@Override
	public InputStream get(String id) {
		UFileClient ufileClient = new UFileClient();
		HttpClient httpClient = HttpClientBuilder.create().build();
		ufileClient.setHttpClient(httpClient);

		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(id);

		GetSender getSender = new GetSender();
		getSender.makeAuth(ufileClient, request);
		UFileResponse res = getSender.send(ufileClient, request);

		return res.getContent();
	}

	@Override
	public void write(String id, OutputStream out) {
		throw new RuntimeException();
	}

	@Override
	public void delete(String id) {
		UFileClient ufileClient = new UFileClient();
		HttpClient httpClient = new DefaultHttpClient();
		ufileClient.setHttpClient(httpClient);

		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(id);

		DeleteSender sender = new DeleteSender();
		sender.makeAuth(ufileClient, request);
		sender.send(ufileClient, request);
	}

	@Override
	public String getFileServer() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String save(InputStream in, String id) {
		UFileClient ufileClient = new UFileClient();
		HttpClient httpClient = new DefaultHttpClient();
		ufileClient.setHttpClient(httpClient);

		String key = id;
		if (key == null) {
			key = StringUtils.uuid();
		}
		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(key);

		try {
			request.setInputStream(in);
			request.setContentLength(0L + in.available());

			PutSender sender = new PutSender();
			sender.makeAuth(ufileClient, request);
			UFileResponse response = sender.send(ufileClient, request);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				return key;
			} else {
				logger.error("保存文件:" + response.getStatusLine().getStatusCode());
				throw new RuntimeException("保存文件出错");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			ufileClient.getHttpClient().getConnectionManager().shutdown();
		}
	}
}
