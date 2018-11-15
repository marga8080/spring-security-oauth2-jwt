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

import com.soj.config.custom.RedirectUrlAuthenticationFailureHandler;
import com.soj.config.filter.SmsCodeAuthenticationProcessingFilter;
import com.soj.config.provider.SmsCodeAuthenticationProvider;
import com.soj.config.provider.UsernamePasswordAuthenticationProvider;


/**
 * Web Security 配置
 * 
 * spring security自定义Provider实现多种认证方式
 *https://www.jianshu.com/p/779d3071e98d
 *https://blog.csdn.net/biboheart/article/details/80666700
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
        filter.setAuthenticationFailureHandler(new RedirectUrlAuthenticationFailureHandler("/login"));
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
		
		/*
		 * '/oauth/token'、'/oauth/token_key'、'/oauth/check_token'这些uri端点，已被oauth2的一个安全配置类定义了匹配规则，
		 * 如果在这里定义自定义过滤器，是在ResourceServerConfiguration配置对应的过滤器链，而且匹配顺序在上面的安全配置类之后，因此对以上的uri拦截不了，
		 * 只能拦截到下面定义的这些uri："/login", "/tandn", "/stn", "/public_key"
		 * 所以我们采取下面FilterRegistrationBean的方式进行注册。
		 */
		http.authorizeRequests()
			.antMatchers("/login", "/sms/**",  "/static/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.failureHandler(new RedirectUrlAuthenticationFailureHandler("/login"))
			.defaultSuccessUrl("/index")
			.permitAll()
			.and()
			.logout()
			.logoutSuccessUrl("/login");
		
		http.addFilterBefore(smsCodeAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
	}


}
