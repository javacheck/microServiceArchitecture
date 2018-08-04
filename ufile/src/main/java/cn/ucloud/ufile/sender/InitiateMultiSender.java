package cn.ucloud.ufile.sender;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class InitiateMultiSender implements Sender {
	
	public InitiateMultiSender() {
		
	}

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {	
		String httpMethod = "POST";
		String contentMD5 = request.getContentMD5();
		String contentType = "";	
		String date = request.getDate();
		String canonicalizedUcloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalized_resource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
				 canonicalizedUcloudHeaders + canonicalized_resource;
		
		//attatch the contentType
		client.makeAuth(stringToSign, request);
	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "http://" + request.getBucketName() 
				+ ufileClient.getProxySuffix() + "/" + request.getKey() + "?uploads";	
		
		HttpPost postMethod = new HttpPost(uri);
	
		HttpEntity resEntity = null;
		try {
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            postMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(postMethod);
			
			resEntity = httpResponse.getEntity();
			UFileResponse ufileResponse = new UFileResponse();
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
