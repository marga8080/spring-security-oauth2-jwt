package com.soj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/**
 * HTTP 请求工具类
 * 
 * @author mawei
 * 
 * See <a href="http://blog.csdn.net/happylee6688/article/details/47148227">HttpClient发送HTTP、HTTPS请求的简单封装</a>
 * 
 */
public class HttpUtil {
	
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 30000;
	private final static int CONNECT_TOTAL = 200;// 最大连接数

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(CONNECT_TOTAL);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		connMgr.setValidateAfterInactivity(MAX_TIMEOUT);

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		requestConfig = configBuilder.build();
	}

	// 请求重试处理
	static HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount >= 5) {// 如果已经重试了5次，就放弃
				return false;
			}
			if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
				return true;
			}
			if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
				return false;
			}
			if (exception instanceof InterruptedIOException) {// 超时
				return false;
			}
			if (exception instanceof UnknownHostException) {// 目标服务器不可达
				return false;
			}
			if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
				return false;
			}
			if (exception instanceof SSLException) {// SSL握手异常
				return false;
			}

			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			// 如果请求是幂等的，就再次尝试
			if (!(request instanceof HttpEntityEnclosingRequest)) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 获取一个http client
	 * @return http client
	 */
	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom()
				.setConnectionManager(connMgr)
				.setDefaultRequestConfig(requestConfig)
				.setRetryHandler(httpRequestRetryHandler)
				.build();
	}

	/**
	 * 获取一个https client
	 * @return http client
	 */
	protected static CloseableHttpClient getHttpsClient() {
		return HttpClients.custom()
				.setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr)
				.setDefaultRequestConfig(requestConfig)
				.setRetryHandler(httpRequestRetryHandler)
				.build();
	}

	
	/**
	 * get请求
	 * 
	 * @param uri 请求地址
	 * @return json对象
	 * @throws IOException io异常
	 */
	public static JSONObject get(String uri) throws IOException {
		String result = doGet(uri, new HashMap<String, Object>(),null);;
		JSONObject obj = JSONObject.fromObject(result);
		return obj;
	}
	
	/**
	 * get请求
	 * 
	 * @param uri 请求地址
	 * @param params 参数
	 * @param headerMap header
	 * @return 响应对象
	 * @throws IOException io异常
	 */
	public static Object get(String uri,Map<String,Object> params, Map<String,Object> headerMap) throws IOException {
		String result = doGet(uri, params,headerMap);;
//		JSONObject obj = JSONObject.fromObject(result);
		return result;
	}

	/**
	 * post请求
	 * 
	 * @param uri 请求地址
	 * @param params 请求参数
	 * @return json对象
	 * @throws IOException io异常
	 */
	public static JSONObject post(String uri, JSONObject params) throws IOException {
		return post(uri, params.toString());
	}
	
	/**
	 * post请求
	 * 
	 * @param uri 请求地址
	 * @param params 请求参数
	 * @param headerParams 头部请求参数
	 * @return json对象
	 * @throws IOException io异常
	 */
	public static JSONObject post(String uri, JSONObject params,Map<String,String> headerParams) throws IOException {
//		return post(uri, params.toString());
		String result = doPost(uri, params.toString(),headerParams);
		JSONObject obj = JSONObject.fromObject(result);
		return obj;
	}
	
	/**
	 * post请求
	 * 
	 * @param uri 请求地址
	 * @param params 请求地址 json字符串
	 * @return json对象
	 * @throws IOException io异常
	 */
	public static JSONObject post(String uri, String params) throws IOException {
		String result = doPost(uri, params);
		JSONObject obj = JSONObject.fromObject(result);
		return obj;
	}
	
	
	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param headerMap header
	 * 
	 * @return 响应字符串
	 * @throws IOException io异常
	 */
	public static String doGet(String url, Map<String, Object> params, Map<String, Object> headerMap) throws IOException {
		String apiUrl = url;
		if (params != null) {
			boolean hasP = url.indexOf("?") != -1;
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : params.keySet()) {
				if (i == 0 && !hasP)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(params.get(key));
				i++;
			}
			apiUrl += param;
		}
		String result = null;
		HttpClient httpclient = null;
		if (url.toLowerCase().startsWith("https://")) {
			httpclient = getHttpsClient();
		} else {
			httpclient = getHttpClient();
		}
		HttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			if(headerMap!=null) {
			     for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
			    	 String value = entry.getValue()!=null ? entry.getValue().toString() : null;
			    	 httpGet.setHeader(entry.getKey(),value);
		        } 
			}
			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = IOUtils.toString(instream, "UTF-8");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					//"释放连接错误"
				}
			}
		}
		return result;
	}

	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl 请求地址
	 * @param json json字符串
	 * @return 响应字符串
	 * @throws IOException io异常
	 */
	public static String doPost(String apiUrl, String json) throws IOException {
		CloseableHttpClient httpClient = null;
		if (apiUrl.toLowerCase().startsWith("https://")) {
			httpClient = getHttpsClient();
		} else {
			httpClient = getHttpClient();
		}
		
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					//"释放连接错误"
				}
			}
		}
		return httpStr;
	}
	
	/**
	 * 发送 POST 请求（HTTP），JSON形式
	 * 
	 * @param apiUrl 请求地址
	 * @param json json字符串
	 * @param headerParams header字符串集合
	 * @return 响应字符串
	 * @throws IOException io异常
	 */
	public static String doPost(String apiUrl, String json, Map<String,String> headerParams) throws IOException {
		CloseableHttpClient httpClient = null;
		if (apiUrl.toLowerCase().startsWith("https://")) {
			httpClient = getHttpsClient();
		} else {
			httpClient = getHttpClient();
		}
		
		String httpStr = null;
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;

		try {
			httpPost.setConfig(requestConfig);
			if(headerParams!=null) {
			     for (Map.Entry<String, String> entry : headerParams.entrySet()) {
			    	 httpPost.setHeader(entry.getKey(),entry.getValue());
		        } 
			}
			StringEntity stringEntity = new StringEntity(json, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			httpStr = EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					//"释放连接错误"
				}
			}
		}
		return httpStr;
	}

	/**
	 * 创建SSL安全连接
	 * 
	 * @return
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			
			sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
		} catch (GeneralSecurityException e) {
			// "创建SSL安全连接失败"
		}
		return sslsf;
	}

}