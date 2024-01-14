package com.imooc.miaosha.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.alibaba.druid.util.StringUtils;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.RedisService;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger log= LoggerFactory.getLogger(GoodsController.class);
	@Autowired
	UserService userService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	RedisService redisService;
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	/**
	 * 
	 * @param model
	 * @param user
	 * @return
	 * jmeter压测
	 * 5000个线程，跑10次、
	 * 吞吐量QPS:4529
	 * 使用redis缓存页面后jmeter压测
	 * 5000个线程，跑10次、
	 * 吞吐量QPS:7799.1	
	 */
	@RequestMapping(value = "/to_list", produces = "text/html")	//produces以text/html的格式返回数据
	@ResponseBody
	public String toList(HttpServletRequest request,HttpServletResponse response, Model model,User user) {
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		//如果redis中有缓存
		if (!StringUtils.isEmpty(html)) {
			return html;
		}
		model.addAttribute("user", user);
		//查询商品列表
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList",goodsList);
		//如果redis中不存在缓存key
		//手动渲染，需要使用ThymeleafViewResolver
		/*
		 * WebContext
		 */
		WebContext ctx = new WebContext(request, response, request.getServletContext(),request.getLocale(),model.asMap());
		html=thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		//将缓存页面存入redis中
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}
	
	
	@RequestMapping(value = "/to_detail_01/{goodsId}",produces = "text/html")
	@ResponseBody
	public String detail(HttpServletRequest request,HttpServletResponse response, Model model,User user,
			@PathVariable("goodsId")long goodsId) {
		//先取缓存
		String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
		//判断缓存html是否为空，不为空则返回html
		if(!StringUtils.isEmpty(html)) {
			return html;
		}
		model.addAttribute("user", user);
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods",goods);
		//秒杀开始时间与结束时间
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int miaoshaStatus=0;
		int remainSeconds=0;
		if(now < startAt) {//秒杀未开始，倒计时
			miaoshaStatus=0;
			remainSeconds=(int)(startAt-now)/1000;
		}else if(now > endAt){//秒杀已结束
			miaoshaStatus=2;
			remainSeconds=-1;
		}else {//秒杀进行中
			miaoshaStatus=1;
			remainSeconds=0;
		}
		model.addAttribute("miaoshaStatus",miaoshaStatus);
		model.addAttribute("remainSeconds",remainSeconds);
		//redis中没有缓存页面则手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(),request.getLocale(),model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		//将html存入redis中
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, "_"+goodsId, html);
		}
		return html;
	}
	
	/**
	 * 页面静态化
	 * @param request
	 * @param response
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> static_detail(HttpServletRequest request,HttpServletResponse response, Model model,User user,@PathVariable("goodsId")long goodsId) {
		GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
		//秒杀开始时间与结束时间
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int miaoshaStatus=0;
		int remainSeconds=0;
		if(now < startAt) {//秒杀未开始，倒计时
			miaoshaStatus=0;
			remainSeconds=(int)(startAt-now)/1000;
		}else if(now > endAt){//秒杀已结束
			miaoshaStatus=2;
			remainSeconds=-1;
		}else {//秒杀进行中
			miaoshaStatus=1;
			remainSeconds=0;
		}
		GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
		goodsDetailVo.setGoods(goods);
		goodsDetailVo.setUser(user);
		goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
		goodsDetailVo.setRemainSeconds(remainSeconds);
		return Result.success(goodsDetailVo);
	}
}
