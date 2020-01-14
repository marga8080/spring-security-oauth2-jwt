package com.soj.config.authentication.provider;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户名密码登录
 * @author mawei
 *
 */
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	UserDetailsService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
        	throw new BadCredentialsException("密码不能为空");
        }
        UserDetails userDetails = userService.loadUserByUsername(username);
        String frontPass = passwordEncoder.encode(password);
        boolean match = passwordEncoder.matches(frontPass, userDetails.getPassword());
        if (!match) {
            throw new BadCredentialsException("用户名或密码不正确");
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
