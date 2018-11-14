package com.soj.config.custom;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;


/**
 * 自定义AccessToken的转换，增加自定义属性
 * 
 */
public class CustomerAccessTokenConverter extends DefaultAccessTokenConverter {

	public CustomerAccessTokenConverter() {
		super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
	}

	private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, ?> convertUserAuthentication(Authentication authentication) {
			// 默认放进access token的只有user_name和authorities(如有)
			Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);
			// 增加自定义属性
			response.put("name", ((OAuthUserDetails) authentication.getPrincipal()).getName()); // 用户姓名
			response.put("id", ((OAuthUserDetails) authentication.getPrincipal()).getUnid()); // 用户id
			return response;
		}
	}

}
