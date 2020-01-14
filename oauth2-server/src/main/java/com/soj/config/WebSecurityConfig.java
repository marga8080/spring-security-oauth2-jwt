package com.soj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.soj.config.authentication.filter.SmsCodeAuthenticationProcessingFilter;
import com.soj.config.authentication.provider.SmsCodeAuthenticationProvider;
import com.soj.config.authentication.provider.UsernamePasswordAuthenticationProvider;
import com.soj.config.custom.FailureHandler;

/**
 * Web Security 配置
 * 
 * spring security自定义Provider实现多种认证方式 https://www.jianshu.com/p/779d3071e98d
 * https://blog.csdn.net/biboheart/article/details/80666700
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 登录方式Provider
		auth.authenticationProvider(smsCodeAuthenticationProvider());
		auth.authenticationProvider(usernamePasswordAuthenticationProvider());
	}

	@Bean
	public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
		return new UsernamePasswordAuthenticationProvider();
	}

	@Bean
	public SmsCodeAuthenticationProcessingFilter smsCodeAuthenticationProcessingFilter() {
		SmsCodeAuthenticationProcessingFilter filter = new SmsCodeAuthenticationProcessingFilter();
		filter.setAuthenticationManager(authenticationManager);
		filter.setAuthenticationFailureHandler(new FailureHandler("/login"));
		return filter;
	}

	@Bean
	public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
		return new SmsCodeAuthenticationProvider();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable(); // 关闭csrf保护功能（跨域访问）

		http.authorizeRequests()
				.antMatchers("/login", "/sms/**", "/static/**").permitAll().anyRequest().authenticated()
				.and()
				.formLogin().loginPage("/login")
				.failureHandler(new FailureHandler("/login"))
				.defaultSuccessUrl("/index").permitAll()
				.and()
				.logout()
				.logoutSuccessHandler((request, response, authentication) -> {
					String callback = request.getParameter("callback");
					if (callback == null) {
						callback = "/login";
					}
					response.sendRedirect(callback);
				});

		http.addFilterBefore(smsCodeAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

	}

}
