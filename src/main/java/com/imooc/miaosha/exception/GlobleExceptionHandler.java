package com.imooc.miaosha.exception;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
	private static Logger log= LoggerFactory.getLogger(GlobleExceptionHandler.class);
	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
		if(e instanceof GlobleException){
			GlobleException ex=(GlobleException)e;
			return Result.error(ex.getcMsg());
		}else if(e instanceof BindException) {
			BindException ex=(BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg=error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		}else {
			log.info(e.getMessage());
			return Result.error(CodeMsg.SERVER_ERROR);
			//return Result.success(CodeMsg.SUCCESS.getMsg());
			
		}
	}
}
