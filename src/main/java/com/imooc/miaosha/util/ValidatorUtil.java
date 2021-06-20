package com.imooc.miaosha.util;
/**
 * 检查手机号码格式是否正确
 * @author kehui
 * @Description ValidatorUtil
 * @date 2021-05-03 20:22:02
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;

public class ValidatorUtil {
	private static final Pattern mobile_pattern=Pattern.compile("1\\d{10}");
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	public static void main(String[] args) {
		System.out.println(isMobile("18322983415"));
		System.out.println(isMobile("1832298341"));

	}
}
