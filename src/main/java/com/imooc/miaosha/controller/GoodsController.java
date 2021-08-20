package com.imooc.miaosha.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.vo.GoodsVo;
@Controller
@RequestMapping("/goods")
public class GoodsController {
	private static Logger log= LoggerFactory.getLogger(GoodsController.class);
	@Autowired
	UserService userService;
	@Autowired
	GoodsService goodsService;
	@RequestMapping("/to_list")
	public String toList(Model model,User user
			//@CookieValue(value=UserService.COOKIE_NAME_TOKEN,required=false) String cookieToken,
			//@RequestParam(value=UserService.COOKIE_NAME_TOKEN,required=false) String paramToken
			) {
		model.addAttribute("user", user);
		//查询商品列表
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList",goodsList);
		return "goods_list";
	}
	@RequestMapping("/to_detail/{goodsId}")
	public String detail(Model model,User user,
			@PathVariable("goodsId")long goodsId) {
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
		return "goods_detail";
	}
}
