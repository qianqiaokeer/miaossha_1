package com.imooc.miaosha.redis;
/*
 * redis数据库
 * key的参数设置
 */
public interface KeyPrefix {
	//key的有效时间,单位秒
	public int expireSeconds();
	//redis中存储的key名
	public String getPrefix();
}
