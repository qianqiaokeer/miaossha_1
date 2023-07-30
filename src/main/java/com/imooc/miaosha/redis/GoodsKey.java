package com.imooc.miaosha.redis;
/*
 * 商品缓存
 */
public class GoodsKey extends BasePrefix{

	private GoodsKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	//redis缓存，goodslist页面缓存,redis中的key名为:	GoodsKey:goodslist
	public static GoodsKey getGoodsList=new GoodsKey(60,"goodslist");
	//商品详情页面缓存
	public static GoodsKey getGoodsDetail=new GoodsKey(60,"goodsdetail");
	//
}
