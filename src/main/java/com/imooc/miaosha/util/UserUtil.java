package com.imooc.miaosha.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSOutput;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.miaosha.pojo.User;

/**
 * 写一个工具类，创建多个用户
 * 
 * @author ke
 *
 */
public class UserUtil {
	
	private static void CreateUser(int count) throws Exception {
		//第一步，生成用户
		List<User> users = new ArrayList<User>(count);
		for (int i = 0; i < count; i++) {
			User user = new User();
			//设置id
			user.setId(13800000000L+i);
			//设置登录次数
			user.setLoginCount(1);
			//设置登录名称
			user.setName("user"+i);
			//设置注册时间
			user.setRegisterDate(LocalDateTime.now());
			//设置salt
			user.setSalt("keer1314");
			//设置密码，使用md5加密技术
			user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
			//将创建好的user放入users列表中
			users.add(user);
		}
		System.out.println("正在创建用户中，请稍等...");
/*		
		//第二步，将user插入数据库
		//创建连接数据库
		Connection connection = DBUtil.getConnection();
		//写SQL，插入user表sql语句
		String sql ="INSERT INTO user (id, name, password, salt, register_date, login_count) VALUES (?,?,?,?,?,?)";
		//执行sql
		PreparedStatement pstmt = connection.prepareStatement(sql);
		for (int i = 0; i < users.size(); i++) {
			User user =users.get(i);
			//给sql语句中的?赋值
			pstmt.setLong(1, user.getId());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getSalt());
			pstmt.setTimestamp(5, new Timestamp(user.getRegisterDate().getNano()));
			pstmt.setInt(6, user.getLoginCount());
			//将sql语句存储，到时候批处理，这样效果更高
			pstmt.addBatch();
		}
		//执行插入sql语句
		pstmt.executeBatch();
		//关闭
		pstmt.close();
		connection.close();
		System.out.println("正在插入数据库，请稍等...");
*/
		//第三步，模拟登录，生成token
		String urlString = "http://localhost:8080/login/do_login";
		File file = new File("D:/UP/token.txt");	//将生成的token放入此文件中
		//判断是否存在此文件,如果存在则删除文件
		if(file.exists()) {
			file.delete();
		}
		RandomAccessFile raf = new RandomAccessFile(file, "rw");	//处理文件读写操作的工具类
		file.createNewFile();	//创建文件
		raf.seek(0);	//从文件那个位置开始读写，光标的意思，这里从0开始写入数据
		//开始使用用户模拟登录，生成token
		for (int i = 0; i < users.size(); i++) {
			User user =users.get(i);
			//访问网址
			URL url = new URL(urlString);
			HttpURLConnection openCo = (HttpURLConnection)url.openConnection();
			openCo.setRequestMethod("POST");	//设置请求方式
			openCo.setDoOutput(true);	//开启字节流输出
			OutputStream outputStream = openCo.getOutputStream();	//获取输出字节流
			//发送数据
			String params="mobile="+user.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
			outputStream.write(params.getBytes());
			outputStream.flush();	//刷新发送
			InputStream inputStream = openCo.getInputStream();	//获取输入字节流
			//创建本地比特输出流
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			//设置一个比特数组为单位，长度1024，用来读取输入流中的数据
			byte buff[]=new byte[1024];
			int len=0;
			while((len=inputStream.read(buff))>=0) {
				bout.write(buff,0,len);
			}
			inputStream.close();
			bout.close();
			String response =new String(bout.toByteArray());	//将输出流转化为数组，再转化为字符串
			System.out.println(response.toString());
			JSONObject jsons = JSON.parseObject(response);	//json格式
			String token = jsons.getString("data");	//拿到token
			System.out.println(user.getId()+"创建token");
			String row = user.getId()+","+token;	//写入token.txt文件的内容格式
			raf.seek(raf.length());	//从文件token.txt内容结尾继续写入
			raf.write(row.getBytes());	//写入
			raf.write("\r\n".getBytes()); //换行
			System.out.println("将"+user.getId()+"的token写入文件");
		}
		//关闭处理文件工具类
		raf.close();
		System.out.println("创建token完毕");
	}
	public static void main(String[] args) throws Exception {
		//使用方法创建5000个用户，并生成token
		CreateUser(5000);
	}
}
