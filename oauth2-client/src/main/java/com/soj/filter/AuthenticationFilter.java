package com.soj.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soj.utils.HttpUtil;
import com.soj.utils.PropertiesUtils;

import net.sf.json.JSONObject;

@WebFilter(filterName="AuthenticationFilter", urlPatterns="/*")
public class AuthenticationFilter implements Filter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public final static String USER_INFO = "_user_info_";
	String oauthServer = null;
	String client_id = null;
	String client_secret = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("启动AuthenticationFilter ===>");
		PropertiesUtils.init("oauth2.properties");
		oauthServer = PropertiesUtils.getProperty("oauth2.server");	
		client_id = PropertiesUtils.getProperty("oauth2.client_id");	
		client_secret = PropertiesUtils.getProperty("oauth2.client_secret");	
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) rsp;
		// OPTIONS 不做记录
		if (request.getMethod() != null && "OPTIONS".equals(request.getMethod().toUpperCase())) {
			chain.doFilter(req, rsp);
			return;
		}
		
		String[] includeUrls = includeUrls();
		if (includeUrls != null && includeUrls.length != 0) {
			// 当需要过滤的链接有值时 如果不是属于includeUrls的链接都不做记录
			if (!isIncludeUrls(request)) {
				chain.doFilter(req, rsp);
				return;
			}
		}
		
		if (isExcludeUrls(request)) { // 不过滤
			chain.doFilter(req, rsp);
			return;
		}
		HttpSession session = request.getSession();
		if (session.getAttribute(USER_INFO) != null) {
			// session 有值说明已经登录过
			chain.doFilter(req, rsp);
			return;
		}
		
		String code = request.getParameter("code");
		if (StringUtils.isNotBlank(code)) {
			// TODO
			
			chain.doFilter(req, rsp);
			return;
		}
		
		String url = request.getRequestURL().toString();
		String params = request.getQueryString();
		if(StringUtils.isNotBlank(params)){
			url += "?" + params;
		}
		url = URLEncoder.encode(url, "UTF-8");
		//获取code
		//http://localhost:8080/oauth/authorize?client_id=client1&response_type=code&redirect_uri=http://www.baidu.com
		String rediectUrl = "OAUTHSERVER/oauth/authorize?client_id=CLIENT_ID&response_type=code&redirect_uri=REDIRECT_URI";
		rediectUrl = rediectUrl.replace("OAUTHSERVER", oauthServer).replace("CLIENT_ID", client_id).replace("REDIRECT_URI", url);
		response.sendRedirect(rediectUrl);
	}
	
	@Override
	public void destroy() {
		
	}
	
	private String getToken(String code) throws IOException {
		String uri = "http://localhost:8080/oauth/token";
		JSONObject json = HttpUtil.post(uri, "");
		
		return null;
	}
	
	private JSONObject getUserInfo() {
		
		return null;
	}
	
	private boolean isExcludeUrls(HttpServletRequest request) {
		String[] excludeUrls = excludeUrls();
		if (excludeUrls == null || excludeUrls.length == 0) {
			return false;
		}
		String url = request.getRequestURI();
		for (String excludeUrl : excludeUrls) {
			if (excludeUrl.endsWith("**")) {
				String eurl = excludeUrl.substring(0, excludeUrl.length() - 2);
				if (url.indexOf(eurl) != -1) {
					return true;
				}
			}
			if (excludeUrl.equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isIncludeUrls(HttpServletRequest request) {
		String[] includeUrls = includeUrls();
		if (includeUrls == null || includeUrls.length == 0) {
			return false;
		}
		String uri = request.getRequestURI();
		for (String url : includeUrls) {
			if (url.endsWith("**")) {
				String eurl = url.substring(0, url.length() - 2);
				if (uri.indexOf(eurl) != -1) {
					return true;
				}
			}
			if (url.equals(uri)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 需要被过滤的链接
	 * @return
	 */
	public String[] includeUrls() {
		String urls = PropertiesUtils.getProperty("oauth2.includeUrls");
		if (StringUtils.isNotBlank(urls)) {
			return urls.split(",");
		}
		return null;
	}
	
	/**
	 * 不需要被过滤的链接
	 * @return
	 */
	public String[] excludeUrls() {
		String urls = PropertiesUtils.getProperty("oauth2.excludeUrls");
		if (StringUtils.isNotBlank(urls)) {
			return urls.split(",");
		}
		return null;
	}

	
}
