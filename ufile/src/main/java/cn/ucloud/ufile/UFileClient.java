package cn.ucloud.ufile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;

import cn.ucloud.ufile.body.ErrorBody;

import com.google.gson.Gson;

public class UFileClient {

	private static final String CONFIG_FILE = "ufileconfig.properties";
	private static final String CANONICAL_PREFIX = "X-UCloud";

	/*
	 * config.properties public key private key domain suffix
	 */
	private static String ucloudPublicKey;
	private static String ucloudPrivateKey;
	private static String proxySuffix;
	private HttpClient httpClient;

	public UFileClient() {
	}
	
	static {
		loadConfig();
	}

	private static void loadConfig() {
		InputStream inputStream = UFileClient.class.getClassLoader()
				.getResourceAsStream(CONFIG_FILE);
		Properties configProperties = new Properties();
		try {
			if (inputStream == null)
				throw new Exception(CONFIG_FILE + " not found on classpath");
			configProperties.load(inputStream);
			ucloudPublicKey = configProperties
					.getProperty("UCloudPublicKey");
			ucloudPrivateKey = configProperties
					.getProperty("UCloudPrivateKey");
			proxySuffix = configProperties.getProperty("ProxySuffix");
		} catch (Exception e) {
			System.out.println("Unable to load config info: " + e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printProperties() {
	}

	public String getUcloudPublicKey() {
		return ucloudPublicKey;
	}

	public void setUcloudPublicKey(String upk) {
		ucloudPublicKey = upk;
	}

	public String getUcloudPrivateKey() {
		return ucloudPrivateKey;
	}

	public void setUcloudPrivateKey(String upk) {
		ucloudPrivateKey = upk;
	}

	public String getProxySuffix() {
		return proxySuffix;
	}

	public void setProxySuffix(String ps) {
		proxySuffix = ps;
	}

	public void makeAuth(String stringToSign, UFileRequest request) {
		String signature = new HmacSHA1().sign(ucloudPrivateKey,
				stringToSign);
		String authorization = "UCloud" + " " + ucloudPublicKey + ":"
				+ signature;
		request.setAuthorization(authorization);
	}

	public String spliceCanonicalHeaders(UFileRequest request) {
		Map<String, String> headers = request.getHeaders();
		Map<String, String> sortedMap = new TreeMap<String, String>();

		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (entry.getKey().startsWith(CANONICAL_PREFIX)) {
					sortedMap.put(entry.getKey().toLowerCase(),
							entry.getValue());
				}
			}
			String result = "";
			for (Entry<String, String> entry : sortedMap.entrySet()) {
				result += entry.getKey() + ":" + entry.getValue() + "\n";
			}
			return result;
		} else {
			return "";
		}
	}

	public void closeErrorResponse(UFileResponse res) {
		InputStream inputStream = res.getContent();
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
			Gson gson = new Gson();
			ErrorBody body = gson.fromJson(reader, ErrorBody.class);
			String bodyJson = gson.toJson(body);
			System.out.println(bodyJson);

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}
