package cn.ucloud.ufile.sender;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class PutSender implements Sender {
	
	public PutSender() {
		
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
		String uri = "http://" + request.getBucketName() + ufileClient.getProxySuffix() + "/" + request.getKey();
		HttpPut putMethod = new HttpPut(uri);
		
		HttpEntity resEntity = null;
		try {
			//File file = new File(request.getFilePath());
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            putMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			long  len = request.getContentLength();
			if (len < 0) {
				throw new Exception("Missing Content-Length Exception");
			}
			InputStreamEntity reqEntity = new InputStreamEntity(request.getInputStream(), len);	
			
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
