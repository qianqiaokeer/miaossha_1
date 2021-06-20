package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	//先设置一个salt
	private static final String salt="1a2b3c4d";
	/**
	 * 用户输入的密码即用户注册设置的密码，经过一次MD5加密,作为表单密码
	 */
	public static String inputPassToFormPass(String inputPass) {
		String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
		return md5(str);
	}
	/**
	 * 将表单传来的密码进行MD5加密，存入数据库
	 * @param formPass
	 * @return
	 */
	public static String formPassToDBPass(String formPass,String salt) {
		String str=""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
		return md5(str);
	}
	/**
	 * 好的，现在直接从注册密码到数据库
	 * @param
	 */
	public static String inputPassToDBPass(String inputPass,String salt) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, "keer1314");
		return md5(dbPass);
	}
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
		
	}
}
