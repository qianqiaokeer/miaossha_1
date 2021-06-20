package com.imooc.miaosha.result;


public class CodeMsg {
	private int code;
	private String msg;
	public static CodeMsg SUCCESS=new CodeMsg(200, "成功");
	public static CodeMsg SERVER_ERROR=new CodeMsg(500100,"服务端异常");
	public static CodeMsg GG_ERROR=new CodeMsg(500200, "普通异常");
	public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500211, "登录密码不能为空");
	public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212, "手机号不能为空");
	public static CodeMsg MOBILE_ERROR=new CodeMsg(500213, "手机号码格式错误");
	public static CodeMsg MOBILE_NOT_EXIST=new CodeMsg(500214, "手机号码不存在");
	public static CodeMsg PASSWORD_ERROR=new CodeMsg(500215, "密码错误");
	private CodeMsg(int i, String msg) {
		// TODO 自动生成的构造函数存根
		this.code=i;
		this.msg=msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
