package com.client1.filter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@WebFilter(urlPatterns = "/*")
@Configuration
public class AuthenticationFilter implements Filter {
	
	@Value("${oauth2.client.id}")
	private String clientId;
	@Value("${oauth2.client.secret}")
	private String clientSecret;
	@Value("${oauth2.client.url}")
	private String clientUrl;
	@Value("${oauth2.server.url}")
	private String serverUrl;

	public final static String TOKEN_NAME = "sso_token";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        // session 存在token信息
        String stoken = (String) request.getSession().getAttribute(TOKEN_NAME);
        if (StringUtils.isNotBlank(stoken)) {
        	chain.doFilter(request, response);
			return;
        }
        // cookie 存在 token信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
        	for (Cookie c : cookies) {
        		if (TOKEN_NAME.equals(c.getName()) && StringUtils.isNotBlank(c.getValue())) {
        			chain.doFilter(request, response);
        			return;
        		}
        	}
        }
        // 重定向后存在code
		String code = request.getParameter("code");
		if (StringUtils.isNotBlank(code)) {
			JSONObject json = getTokenByCode(code);
			String token = json.getStr("access_token");
			if (StringUtils.isNotBlank(token)) {
				request.getSession().setAttribute(TOKEN_NAME, token);
				Cookie cookie = new Cookie(TOKEN_NAME, token);
				cookie.setPath("/");
				cookie.setDomain(new URL(clientUrl).getHost());
				response.addCookie(cookie);
				response.sendRedirect(clientUrl);
			}
		}
		
		response.sendRedirect(getCodeUrl());
	}
	
	/**
	 * 获取授权码
	 * @return
	 */
	private String getCodeUrl() {
		String url = serverUrl + "/oauth/authorize?client_id=CLIENT_ID&response_type=code&redirect_uri=REDIRECT_URI";
		url = url.replace("CLIENT_ID", clientId);
		url = url.replace("REDIRECT_URI", URLUtil.encode(clientUrl));
		return url;
	}
	
	public JSONObject getTokenByCode(String code) {
		String url = serverUrl + "/oauth/token";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("code", code);
		paramMap.put("client_id", clientId);
		paramMap.put("client_secret", clientSecret);
		paramMap.put("scope", "read");
		paramMap.put("grant_type", "authorization_code");
		paramMap.put("redirect_uri", clientUrl);
		String resp = HttpUtil.post(url, paramMap);
		System.out.println(resp);
		JSONObject json = JSONUtil.parseObj(resp);
		return json;
	}

	@Override
	public void destroy() {

	}

}
