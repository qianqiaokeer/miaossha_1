package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.RedisService;
import com.imooc.miaosha.service.UserService;

@Controller
@RequestMapping("/hello")
public class DemoController {
	@Autowired
	private RedisService redisService;
	@Autowired
	private UserService userService;
	@RequestMapping("/world")
	@ResponseBody
	String home() {
		return "Hello World";
	}
	@RequestMapping("/msg")
	@ResponseBody
	public Result<String> message() {
		return Result.success("大牛");
	}
	@RequestMapping("/error")
	@ResponseBody
	public Result<String> error() {
		return Result.error(CodeMsg.GG_ERROR);
	}
	@RequestMapping("thymeleaf")
	public String thymeleaf(Model model) {
		model.addAttribute("name","thymeleaf");
		return "hello";
	}
	@RequestMapping("/get/{id}")
	@ResponseBody
	public Result<User> dbGet(@PathVariable int id){
		return Result.success(userService.getUserById(id));
	}
	@RequestMapping("/tx")
	@ResponseBody
	public Result<Boolean> dbTx(){
		boolean flag=userService.tx();
		return Result.success(flag);
	}
	@RequestMapping("/redis")
	@ResponseBody
	public Result<User> redisGet(){
		User user=redisService.get(UserKey.getById,""+2, User.class);
		return Result.success(user);
	}
	@RequestMapping("/redis/set")
	@ResponseBody
	public Result<Boolean> redisSet(){
		User user=new User();
		user.setId(2);
		user.setName("李四");
		redisService.set(UserKey.getById,""+2, user);
		return Result.success(true);
	}
}
