package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix{
	public static final int TOOK_EXPIRE =3600*24*2;	//有效时间，单位秒
	/*
	 * prefix	redis中key的名字
	 */
	private UserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	//缓存token
	public static UserKey token=new UserKey(TOOK_EXPIRE,"token:");
	//缓存user对象，有效期一般设置为永久有效
	public static UserKey getById= new UserKey(0, "id:");
}
