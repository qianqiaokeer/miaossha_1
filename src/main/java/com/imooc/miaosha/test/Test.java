package com.imooc.miaosha.test;

import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromGetterNameCandidates;

import com.imooc.miaosha.pojo.User;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		User user = new User();
		Class class01 = user.getClass();		System.out.println(class01.getName());
		System.out.println(class01.getSimpleName());
	}

}
