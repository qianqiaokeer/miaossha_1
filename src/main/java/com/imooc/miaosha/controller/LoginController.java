package com.imooc.miaosha.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.pojo.Login;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.util.ValidatorUtil;
@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger log= LoggerFactory.getLogger(LoginController.class);
	@RequestMapping("/to_login")
	public String toLogin() {
		return "login";
	}
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<Boolean> doLogin(@Valid Login login) {
		log.info(login.toString());
//		String mobile = login.getMobile();
//		String password = login.getPassword();
//		//密码不能为空
//		if(StringUtils.isEmpty(password)) {
//			return Result.error(CodeMsg.PASSWORD_EMPTY);
//		}
//		//手机号不能为空
//		if(StringUtils.isEmpty(mobile)) {
//			return Result.error(CodeMsg.MOBILE_EMPTY);
//		}
//		//手机格式是否正确
//		if(!ValidatorUtil.isMobile(mobile)) {
//			return Result.error(CodeMsg.MOBILE_ERROR);
//		}
		//登录
		CodeMsg msg = UserService.login(login);
		if(msg.getCode()==200) {
			return Result.success(true);
		}else {
			return Result.error(msg);
		}
	}
}
