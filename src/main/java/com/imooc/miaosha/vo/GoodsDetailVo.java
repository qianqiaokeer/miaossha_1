package com.imooc.miaosha.vo;

import com.imooc.miaosha.pojo.User;
/**
 * 商品详情页面静态化
 * @author ke
 *
 */
public class GoodsDetailVo {
	//秒杀状态，用于判断该商品是否属于秒杀商品
	private int miaoshaStatus;
	//秒杀剩余时间
	private int remainSeconds;
	private GoodsVo goods;
	private User user;
	public int getMiaoshaStatus() {
		return miaoshaStatus;
	}
	public void setMiaoshaStatus(int miaoshaStatus) {
		this.miaoshaStatus = miaoshaStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
