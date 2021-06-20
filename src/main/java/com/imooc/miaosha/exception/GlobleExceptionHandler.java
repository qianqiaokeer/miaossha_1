package com.imooc.miaosha.exception;

import java.net.BindException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
		if(e instanceof BindException) {
			BindException ex=(BindException)e;
			return Result.error(CodeMsg.GG_ERROR);
		}else {
			return null;
		}
	}
}
