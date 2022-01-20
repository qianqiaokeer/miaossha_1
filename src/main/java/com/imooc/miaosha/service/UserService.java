package com.imooc.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.exception.GlobleException;
import com.imooc.miaosha.pojo.Login;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;


@Service
public class UserService {
	private static Logger log= LoggerFactory.getLogger(UserService.class);
	public static final String COOKIE_NAME_TOKEN="token";
	@Autowired
	private UserDao userDao;
	/**
	 * 将cookie引入Redis中
	 */
	@Autowired
	RedisService redisService;
	//根据id得到user
	public User getUserById(long id) {
		/*
		 * 添加redis缓存对象功能
		 */
		//先从redis中取缓存
		/*
		 * redisService.get()方法中的三个参数
		 * 1、redis库中的key主名
		 * 2、key的辅助名
		 * 3、redis库中的value值以User格式返回
		 */
		User user = redisService.get(UserKey.getById, ""+id, User.class);
		//判断user是否为空，不为空则返回user
		if (user != null) {
			return user;
		}
		//redis库中没有缓存，则到数据库中取
		user = userDao.getUserById(id);
		//将在数据库中查找的user缓存到redis库中，先判断user是否为空，不为空则放入redis库中
		if (user != null) {
			/*
			 * redisService.set()中的三个参数
			 * 1、redis库中的key主名
			 * 2、key的辅助名
			 * 3、存入redis库的值，这里存入user对象
			 */
			redisService.set(UserKey.getById, ""+id, user);
		}
		return user;
	}
	//用户修改密码
	public boolean updatePassword(String token,long id,String formPassword) {
		//获取user
		User user = getUserById(id);
		if(user==null) {
			//没找到对应的用户,抛出异常，用户不存在
			throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//更新数据库
		User updataUser=new User();
		updataUser.setId(id);
		updataUser.setPassword(MD5Util.formPassToDBPass(formPassword, user.getSalt()));
		userDao.update(updataUser);
		//处理redis缓存
		//删除user对象缓存
		redisService.delete(UserKey.getById,""+id);
		//跟新token
		user.setPassword(updataUser.getPassword());
		redisService.set(UserKey.token, token, user);
		return true;
	}
	public User getByToken(HttpServletResponse response,String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		User user= redisService.get(UserKey.token, token, User.class);
		//延长有效期
		if(user!=null) {
			addCookie(response,token,user);
		}
		return user;
	}
	//事务测试
	@Transactional
	public boolean tx() {
		User u1=new User();
		u1.setId(12233332222L);
		u1.setName("小武");
		userDao.insertUser(u1);
		User u2=new User();
		u2.setId(18099990000L);
		u2.setName("小李");
		userDao.insertUser(u2);
		return true;
	}
	public String login(HttpServletResponse response,Login login) {
		if(login==null) {
			throw new GlobleException(CodeMsg.SERVER_ERROR);
			//return CodeMsg.SERVER_ERROR;
		}
		String mobile = login.getMobile();
		String password = login.getPassword();
		//判断手机号是否存在
		User user=getUserById(Long.parseLong(mobile));
		if(user==null) {
			throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
			//return CodeMsg.MOBILE_NOT_EXIST;
		}
		//验证密码
		String dbPass= user.getPassword();
		String saltDB = user.getSalt();
		log.info("数据库加密密码："+dbPass);
		String calcPass = MD5Util.formPassToDBPass(password, saltDB);
		log.info("表单加密密码："+calcPass);
		if(!calcPass.equals(dbPass)) {
			throw new GlobleException(CodeMsg.PASSWORD_ERROR);
			//return CodeMsg.PASSWORD_ERROR;
		}
		//生成cookie
		String token = UUIDUtil.uuid();
		addCookie(response,token,user);
		return token;
	}
	//生成cookie
	private void addCookie(HttpServletResponse response,String token,User user) {
		//用户信息缓存redis
		redisService.set(UserKey.token,token,user);
		Cookie cookie=new Cookie(COOKIE_NAME_TOKEN, token);
		//设置cookie有效期
		cookie.setMaxAge(UserKey.token.expireSeconds());
		//设置cookie路径
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
