package cn.ucloud.ufile.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class UploadPartSender implements Sender {
	
	private String uploadId;
	private int partNumber;
	private long start;
	private long size;


	public UploadPartSender(String uploadId, int partNumber, long start,
			long size) {
		this.uploadId = uploadId;
		this.partNumber = partNumber;
		this.start = start;
		this.size = size;
	}

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {
		String httpMethod = "PUT";
		String contentMD5 = request.getContentMD5();
		String contentType = request.getContentType();	
		String date = request.getDate();
		String canonicalizedUcloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalized_resource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
				 canonicalizedUcloudHeaders + canonicalized_resource;
	
		client.makeAuth(stringToSign, request);
	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "http://" + request.getBucketName() + ufileClient.getProxySuffix() + "/" + request.getKey()
				+ "?uploadId=" + this.uploadId + "&partNumber=" + this.partNumber;	
		HttpPut putMethod = new HttpPut(uri);
	
		HttpEntity resEntity = null;
		try {
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            putMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			
			BasicHttpEntity reqEntity = new BasicHttpEntity();
			InputStream inputStream = new FileInputStream(request.getFilePath());
			inputStream.skip(start);
			reqEntity.setContent(inputStream);
			reqEntity.setContentLength(size);
			
			putMethod.setEntity(reqEntity);
			UFileResponse ufileResponse = new UFileResponse();
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(putMethod);
			
			resEntity = httpResponse.getEntity();
		
			ufileResponse.setStatusLine(httpResponse.getStatusLine());
			ufileResponse.setHeaders(httpResponse.getAllHeaders());
			
			if (resEntity != null) {
				ufileResponse.setContentLength(resEntity.getContentLength());
				ufileResponse.setContent(resEntity.getContent());
			}
			return ufileResponse;
		} catch (Exception e) {
			try {
				if (resEntity != null && resEntity.getContent() != null) {
					resEntity.getContent().close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				ufileClient.getHttpClient().getConnectionManager().shutdown();
				e.printStackTrace();
			}
		} 
		return null;
	}

}
