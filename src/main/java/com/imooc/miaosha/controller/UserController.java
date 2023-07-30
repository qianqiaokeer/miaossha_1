package com.imooc.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.RedisService;
import com.imooc.miaosha.service.UserService;
@Controller
@RequestMapping("/user")
public class UserController {
	private static Logger log= LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	@RequestMapping("/info")
	@ResponseBody
	public Result<User> info(Model model,User user) 
	{
		return Result.success(user);
	}
}
