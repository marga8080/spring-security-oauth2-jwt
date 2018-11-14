package com.soj.config;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author 徐靖峰
 * Date 2018-04-19
 */
@Configuration
public class OAuth2ServerConfig {

//    private static final String DEMO_RESOURCE_ID = "order";
//
//    @Configuration
//    @EnableResourceServer
//    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//        @Override
//        public void configure(ResourceServerSecurityConfigurer resources) {
//            resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
//        }
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            http
//                .authorizeRequests()
//                    .antMatchers("/order/**").authenticated();//配置order访问控制，必须认证过后才可以访问
//
//        }
//    }


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        RedisConnectionFactory redisConnectionFactory;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

//        password 方案一：明文存储，用于测试，不能用于生产
//        String finalSecret = "123456";
//        password 方案二：用 BCrypt 对密码编码
//        String finalSecret = new BCryptPasswordEncoder().encode("123456");
            // password 方案三：支持多种编码，通过密码的前缀区分编码方式
            String finalSecret = "{bcrypt}"+new BCryptPasswordEncoder().encode("123456");
            //配置两个客户端,一个用于password认证一个用于client认证
            
//            Oauth支持的5类 grant_type 及说明：
//            authorization_code 授权码模式(即先登录获取code,再获取token)
//            password 密码模式(将用户名,密码传过去,直接获取token)
//            client_credentials 客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向’服务端’获取资源)
//            implicit 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)
//            refresh_token 刷新access_token
            clients.inMemory()
            		.withClient("client_1")
//                    .resourceIds(DEMO_RESOURCE_ID)
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("select")
                    .authorities("oauth2")
                    .secret(finalSecret)
                    .and()
                    .withClient("client_2")
//                    .resourceIds(DEMO_RESOURCE_ID)
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("select")
                    .authorities("oauth2")
                    .secret(finalSecret)
	            .and()
	            .withClient("client_3")
	            .secret(finalSecret)
	            .authorizedGrantTypes("authorization_code", "password")
	            .redirectUris("http://www.baidu.com")
	            .scopes("app");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory));
            endpoints.authenticationManager(authenticationManager);
            endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
         // 配置TokenServices参数
    		DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
    		tokenServices.setTokenStore(endpoints.getTokenStore());
    		tokenServices.setSupportRefreshToken(true);
    		tokenServices.setReuseRefreshToken(true);
    		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
    		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
    		tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1)); 	 // 1天
    		tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
    		endpoints.tokenServices(tokenServices);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            //允许表单认证
            oauthServer.allowFormAuthenticationForClients();
        }
        
    }

}
