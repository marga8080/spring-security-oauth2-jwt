package com.soj.config.custom;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;


/**
 * 自定义AccessToken的转换，增加自定义属性
 * 
 */
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	public CustomAccessTokenConverter() {
		super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
	}

	private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, ?> convertUserAuthentication(Authentication authentication) {
			// 默认放进access token的只有user_name和authorities(如有)
			Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(authentication);
			// 增加自定义属性
			CustomUserDetails ud = (CustomUserDetails) authentication.getPrincipal();
			response.put("user_id", ud.getId()); // 用户id
			response.put("real_name", ud.getRealname()); // 用户姓名
			return response;
		}
	}

}
