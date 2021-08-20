package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

public class GlobleException extends RuntimeException{
	private CodeMsg cMsg;
	private static final long serialVersionUID = 1L;
	public GlobleException(CodeMsg cMsg) {
		super(cMsg.toString());
		this.cMsg=cMsg;
	}
	public CodeMsg getcMsg() {
		return cMsg;
	}
	public void setcMsg(CodeMsg cMsg) {
		this.cMsg = cMsg;
	}
	
}
