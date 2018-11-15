package com.soj.config.provider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.soj.config.token.SmsCodeAuthenticationToken;
import com.soj.service.CustomUserService;
import com.soj.utils.JedisUtils;

/**
 * 短信登录
 * @author mawei
 *
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	CustomUserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String phone = authentication.getName();
        String code = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(phone)) {
            throw new BadCredentialsException("手机号码不能为空");
        }
        if (StringUtils.isEmpty(code)) {
        	throw new BadCredentialsException("验证码不能为空");
        }
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String sessionId = details.getSessionId();
        // 判断code是否有效
        String key = sessionId + "-" + phone;
        if (!JedisUtils.exists(key)) {
        	throw new BadCredentialsException("验证码不正确");
        }
        String smsCode = JedisUtils.get(key);
        if (!code.equals(smsCode)) {
        	throw new BadCredentialsException("验证码不正确");
        }
        // 验证成功后删除redis中的验证码
        JedisUtils.del(key);
        UserDetails userDetails = userService.loadUserByPhone(phone);
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(SmsCodeAuthenticationToken.class);
	}

}
