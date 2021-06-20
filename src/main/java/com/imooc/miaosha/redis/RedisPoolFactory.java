package com.imooc.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Service
public class RedisPoolFactory {
	@Autowired
	private RedisConfig redisConfig;
	@Bean
	public JedisPool jedisPoolFactory() {
		JedisPoolConfig jPoolConfig=new JedisPoolConfig();
		jPoolConfig.setMaxIdle(Integer.valueOf(redisConfig.getPool().get("maxIdle")));
		jPoolConfig.setMaxTotal(Integer.valueOf(redisConfig.getPool().get("maxTotal")));
		jPoolConfig.setMaxWaitMillis(Integer.valueOf(redisConfig.getPool().get("maxWait"))*1000);
		JedisPool jp=new JedisPool(jPoolConfig,redisConfig.getHost(),redisConfig.getPort(),
				redisConfig.getTimeout()*1000,redisConfig.getPassword(),0);
		return jp;
	}
}
