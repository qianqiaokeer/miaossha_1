package com.imooc.miaosha.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.pojo.Login;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger log= LoggerFactory.getLogger(LoginController.class);
	@Autowired
	UserService userService;
	@RequestMapping("/to_login")
	public String toLogin() {
		return "login";
	}
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(HttpServletResponse response,@Valid Login login) {
		log.info(login.toString());
/*		String mobile = login.getMobile();
		String password = login.getPassword();
		//密码不能为空
		if(StringUtils.isEmpty(password)) {
			return Result.error(CodeMsg.PASSWORD_EMPTY);
		}
		//手机号不能为空
		if(StringUtils.isEmpty(mobile)) {
			return Result.error(CodeMsg.MOBILE_EMPTY);
		}
		//手机格式是否正确
		if(!ValidatorUtil.isMobile(mobile)) {
			return Result.error(CodeMsg.MOBILE_ERROR);
		}
 * 
 */
		//登录
		userService.login(response,login);
		return Result.success(true);
	}
}
