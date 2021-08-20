package com.imooc.miaosha.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.service.UserService;
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver{
	@Autowired
	UserService userService;
	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		HttpServletRequest request = arg2.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = arg2.getNativeResponse(HttpServletResponse.class);
		String paramToken=request.getParameter(UserService.COOKIE_NAME_TOKEN);
		String cookieToken=getCookieValue(request,UserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)) {
			return null;
		}
	    String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		User user=userService.getByToken(response,token);
		return user;
	}

	private String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter arg0) {
		Class<?> clazz = arg0.getParameterType();
		return clazz==User.class;
	}

}
