package com.soj.config.custom;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 自定义的密码处理器   
 * 
 * @author mawei
 *
 */
public class CustomerPasswordEncoder implements PasswordEncoder {
	
	@Override
	public String encode(CharSequence rawPassword) {
		//return SecurityServiceUtil.hashPassword(rawPassword.toString());
		return rawPassword.toString();
	}

	/**
	 * 验证密码是否正确
	 *
	 * @param rawPassword 来自客户端的密码
	 * @param encodedPassword 来自数据库的密码
	 * @return 密码是否匹配
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// TODO 
		if (rawPassword.equals(encodedPassword)) {
			return true;
		}
		throw new BadCredentialsException("密码错误");
	}

}
