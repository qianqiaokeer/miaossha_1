package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix{
	public static final int TOOK_EXPIRE =3600*24*2;
	private UserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	public static UserKey token=new UserKey(TOOK_EXPIRE,"tk");
}
