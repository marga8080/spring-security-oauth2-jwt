package com.soj.sso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${security.oauth2.client.access-token-uri}")
    private String oauthHost;
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable(); // 关闭csrf保护功能（跨域访问）
    	
        http.antMatcher("/**")
          .authorizeRequests()
          .antMatchers("/login**")
          .permitAll()
          .anyRequest()
          .authenticated()
          //https://www.jianshu.com/p/1f0a5fea9d79
          .and()
          .logout()
          .permitAll()
          .logoutSuccessHandler(((request, response, authentication) -> {
                      response.sendRedirect(oauthHost.split("oauth")[0] + "logout?callback=http://" +  request.getHeader("Host") + "/index");
                  })
          );

    }
}

