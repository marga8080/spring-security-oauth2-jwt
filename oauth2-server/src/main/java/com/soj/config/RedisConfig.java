package com.soj.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 配置
 * 
 * 配置redis.host、redis.port、redis.password、redis.pool.max-idle、redis.pool.max-wait、redis.timeout
 * 
 * @author mawei
 *
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Value("${spring.redis.password:#{null}}")
	private String redisPwd;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.timeout}")
	private int timeout;
	
	@Value("${spring.redis.database:0}")
	private int database;

	@Bean
	public JedisPoolConfig redisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return config;
	}
	
	
	@Bean
	public JedisPool redisPool(JedisPoolConfig config) {
		logger.info("注入JedisPool ===>");
		logger.info("redis地址：" + redisHost + ":" + redisPort);
		if (StringUtils.isBlank(redisPwd)) {
			redisPwd = null;
		}
		JedisPool jedisPool = new JedisPool(config, redisHost, redisPort, timeout, redisPwd, database, null);
		return jedisPool;
	}

	
	
}
