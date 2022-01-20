package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.redis.KeyPrefix;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {
	
	@Autowired
	private JedisPool jedisPool;
	public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			//生成真正的key
			String realKey=keyPrefix.getPrefix()+key;
			String str=jedis.get(realKey);
			T t=stringToBean(str,clazz);
			return t;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		finally {
			returnToPool(jedis);
		}
		
	}
	/**
	 * 
	 * @param <T>
	 * @param keyPrefix	主名
	 * @param key	辅助名字
	 * @param value	要存储的内容
	 * @return
	 */
	public <T> boolean set(KeyPrefix keyPrefix,String key,T value) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			String str=beanToSting(value);
			if(str==null||str.length()<=0) {
				return false;
			}
			String realKey=keyPrefix.getPrefix()+key;
			int seconds=keyPrefix.expireSeconds();
			if(seconds<=0){
				jedis.set(realKey, str);
			}else {
				jedis.setex(realKey, seconds, str);
			}
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		finally {
			returnToPool(jedis);
		}
		
	}
	public <T> Long incr(KeyPrefix keyPrefix,String key) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			String realKey=keyPrefix.getPrefix()+key;
			return jedis.incr(realKey);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		finally {
			returnToPool(jedis);
		}
		
	}
	public <T> boolean exists(KeyPrefix keyPrefix,String key) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			String realKey=keyPrefix.getPrefix()+key;
			return jedis.exists(realKey);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		finally {
			returnToPool(jedis);
		}
		
	}
	/**
	 * 
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	public boolean delete(KeyPrefix keyPrefix,String key) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			//生成真正的redis库中key，主+辅
			String realKey=keyPrefix.getPrefix()+key;
			Long ret = jedis.del(realKey);
			return ret>0;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		finally {
			//回收jedis
			returnToPool(jedis);
		}
		
	}
	public <T> Long decr(KeyPrefix keyPrefix,String key) {
		Jedis jedis=null;
		try {
			jedis=jedisPool.getResource();
			String realKey=keyPrefix.getPrefix()+key;
			return jedis.decr(realKey);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		finally {
			returnToPool(jedis);
		}
		
	}
	private <T> String beanToSting(T value) {
		// 将Bean对象转化成String
		if(value==null) {
			return null;
		}
		Class<?> clazz=value.getClass();
		if(clazz==int.class||clazz==Integer.class) {
			return ""+value;
		}
		else if(clazz==long.class||clazz==Long.class){
			return ""+value;
		}
		else if(clazz==String.class) {
			return (String)value;
		}
		else {
			String str=JSON.toJSONString(value);
			return str;
		}
	}
	private <T> T stringToBean(String str,Class<T> clazz) {
		// 将字符串转化成Bean对象
		if(str==null||str.length()<=0||clazz==null) {
			return null;
		}
		if(clazz==int.class||clazz==Integer.class) {
			return (T)Integer.valueOf(str);
		}
		else if(clazz==long.class||clazz==Long.class){
			return (T)Long.valueOf(str);
		}
		else if(clazz==String.class) {
			return (T)str;
		}
		else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	private void returnToPool(Jedis jedis) {
		if(jedis!=null) {
			jedis.close();
		}
	}
	
}
