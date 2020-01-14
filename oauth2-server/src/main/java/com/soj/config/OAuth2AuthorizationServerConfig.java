package com.soj.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.soj.config.custom.CustomAccessTokenConverter;
import com.soj.config.custom.CustomPasswordEncoder;

/**
 * OAuth2服务器配置
 * 
 * @author mawei
 */
@EnableAuthorizationServer
@Configuration
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	// 注入认证管理器，复用 WebSecurityConfig 的 authenticationManager
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 客户端信息配置
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetails());
	}

	/**
	 * OAuth2 端点配置
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//指定认证管理器
		endpoints.authenticationManager(authenticationManager);
		//指定token存储位置
		endpoints.tokenStore(tokenStore());
		// 自定义token生成方式
		endpoints.tokenEnhancer(accessTokenConverter());
		// 支持post 和 get方法  默认只支持post
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);// add get method
		super.configure(endpoints);
	}
	
	
	/**
	 * 认证服务器的安全配置，即 /oauth/token 端点的安全配置。
	 * /oauth/authorize 端点同样需要安全配置，但不是在这里，而是像普通rest api一样进行处理即可。
	 * 
	 * @param security 安全配置器
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("permitAll()");
		security.tokenKeyAccess("permitAll()");
		security.allowFormAuthenticationForClients();
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}
	
	
	/**
	 * 客户端信息服务
	 */
	@Bean
	public ClientDetailsService clientDetails() { 
		// oauth_client_details
		return new JdbcClientDetailsService(dataSource);
	}

	/**
	 * token存储服务
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * JWT token转换器
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 生成JKS文件
		//keytool -genkeypair -alias lwuums -keyalg RSA -keypass uumspass -keystore lwuums.jks -storepass uumspass
		// 导出公钥
		// keytool -list -rfc --keystore lwuums.jks | openssl x509 -inform pem -pubkey
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("lwuums.jks"), "uumspass".toCharArray());
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair("lwuums"));
//		converter.setSigningKey("123");// 测试用,授权服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
		converter.setAccessTokenConverter(new CustomAccessTokenConverter());
		return converter;
	}
	
}
