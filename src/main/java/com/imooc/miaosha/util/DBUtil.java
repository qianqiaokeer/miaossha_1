package com.imooc.miaosha.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * 数据库工具类
 * @author ke
 *连接数据库
 */
public class DBUtil {
	private static Properties properties;
	
	static {
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();//创建对象，具有读取yml文件的对象
		yaml.setResources(new ClassPathResource("/application.yml"));//指定文件
		properties = yaml.getObject();
	}
	public static Connection getConnection() throws Exception {
		String url = properties.getProperty("spring.datasource.url");
		String username = properties.getProperty("spring.datasource.username");
		String password = properties.getProperty("spring.datasource.password");
		String driver = properties.getProperty("spring.datasource.driver-class-name");
		Class.forName(driver);
		return DriverManager.getConnection(url,username,password);
	}
	public static void main(String[] args) {
		LocalDateTime nowtime = LocalDateTime.now();
		long nano = nowtime.getNano();
		System.out.println(nano);
	}
}
