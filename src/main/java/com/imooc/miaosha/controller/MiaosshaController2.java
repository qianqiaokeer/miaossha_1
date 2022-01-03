package com.imooc.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imooc.miaosha.pojo.MiaoshaOrder;
import com.imooc.miaosha.pojo.OrderInfo;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.service.UserService;
import com.imooc.miaosha.vo.GoodsVo;
@Controller
@RequestMapping("/miaosha")
public class MiaosshaController2 {
	private static Logger log= LoggerFactory.getLogger(MiaosshaController2.class);
	@Autowired
	UserService userService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	@Autowired
	MiaoshaService miaoshaService;
	
	
	/**
	 * 
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 * jmeter压测
	 * 5000个线程，跑10次、
	 * 吞吐量QPS:1717.2
	 */
	@RequestMapping("/do_miaosha")
	public String toList(Model model,User user,
			@RequestParam("goodsId") long goodsId) {
		model.addAttribute("user", user);
		if(user==null) {
			return "login";
		}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			model.addAttribute("error", CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}
		//判断是否已秒杀到了
		MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
		if(order != null) {
			model.addAttribute("error", CodeMsg.REPEATE_MIAOSHA.getMsg());
			return "miaosha_fail";
		}
		//减库存，下订单，写入秒杀订单
		OrderInfo orderInfo=miaoshaService.miaosha(user,goods);
		model.addAttribute("orderInfo",orderInfo);
		model.addAttribute("goods",goods);
		return "order_detail";
	}
}
