package com.soj.config.custom;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class RedirectAuthenticationFailureHandler implements AuthenticationFailureHandler {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private String defaultFailureUrl;

	public RedirectAuthenticationFailureHandler(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		if (StringUtils.isBlank(defaultFailureUrl)) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");
			response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return;
		}
		String url = "";
		if (defaultFailureUrl.indexOf("?") > 0) {
			url = defaultFailureUrl + "&error=" + exception.getMessage();
		} else {
			url = defaultFailureUrl + "?error=" + exception.getMessage();
		}
		redirectStrategy.sendRedirect(request, response, url);
	}

}
