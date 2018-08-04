package cn.ucloud.ufile;

import java.util.Date;

import cn.ucloud.ufile.HmacSHA1;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;

public class DownloadUrl {
	
	public static final String UCLOUD_PROXY_SUFFIX = ".ufile.ucloud.cn";
	
	public DownloadUrl() {
		
	}
	
	public String downloadUrl(UFileClient client, UFileRequest request, int ttl, boolean isPrivate) {
		if (!isPrivate) {
			return "http://" + request.getBucketName() + client.getProxySuffix() + "/" + request.getKey();
		}
		
		return null;
	}
	
	public String makeSignatrue(UFileClient client, UFileRequest request, long expires) {	
		String httpMethod = "GET";
		String contentMD5 = request.getContentMD5();
		String contentType = request.getContentType();	
		String canonicalizedUcloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalized_resource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + expires + "\n" +
				 canonicalizedUcloudHeaders + canonicalized_resource;

		String signature = new HmacSHA1().sign(client.getUcloudPrivateKey(), stringToSign);
		return signature;
	}

	public String makeDownloadUrl(UFileClient ufileClient,
			UFileRequest request, int ttl) {
		long expires = (new Date().getTime() / 1000) + ttl;
		String signature = makeSignatrue(ufileClient, request, expires);
		String url = "http://" + request.getBucketName() + ufileClient.getProxySuffix() + "/" + request.getKey()
				+ "?UCloudPublicKey=" + ufileClient.getUcloudPublicKey()
				+ "&Expires=" + String.valueOf(expires)
				+ "&Signature=" + signature;
		return url;
	}

}
