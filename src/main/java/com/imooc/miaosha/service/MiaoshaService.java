package com.imooc.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.pojo.Goods;
import com.imooc.miaosha.pojo.OrderInfo;
import com.imooc.miaosha.pojo.User;
import com.imooc.miaosha.vo.GoodsVo;

@Service
public class MiaoshaService {
	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	@Transactional
	public OrderInfo miaosha(User user, GoodsVo goods) {
		//减库存	下订单	写入秒杀订单
		goodsService.reduceStock(goods);
		//order_info miaosha_order
		return orderService.createOrder(user,goods);
		
		
	}
	
}
