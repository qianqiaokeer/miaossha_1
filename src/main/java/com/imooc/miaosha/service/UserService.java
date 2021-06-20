package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.pojo.Login;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;


@Service
public class UserService {
	
	@Autowired
	private static UserDao userDao;
	//根据id得到user
	public static User getUserById(int id) {
		return userDao.getUserById(id);
	}
	//事务测试
	@Transactional
	public boolean tx() {
		User u1=new User();
		u1.setId(2);
		u1.setName("李四");
		userDao.insertUser(u1);
		User u2=new User();
		u2.setId(1);
		u2.setName("王五");
		userDao.insertUser(u2);
		return true;
	}
	public static CodeMsg login(Login login) {
		if(login==null) {
			return CodeMsg.SERVER_ERROR;
		}
		String mobile = login.getMobile();
		String password = login.getPassword();
		//判断手机号是否存在
		User user=getUserById((int) Long.parseLong(mobile));
		if(user==null) {
			return CodeMsg.MOBILE_NOT_EXIST;
		}
		//验证密码
		String dbPass= user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(password, saltDB);
		if(calcPass.equals(dbPass)) {
			return CodeMsg.PASSWORD_ERROR;
		}
		return CodeMsg.SUCCESS;
	}
}
