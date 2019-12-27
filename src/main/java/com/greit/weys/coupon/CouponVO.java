package com.greit.weys.coupon;

public class CouponVO {

	private String id;
	private String couponImg;
	private String couponTp;
	private String couponNm;
	private String couponSt;
	private String startDt;
	private String endDt;
	private int cost;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCouponImg() {
		return couponImg;
	}
	public void setCouponImg(String couponImg) {
		this.couponImg = couponImg;
	}
	public String getCouponTp() {
		return couponTp;
	}
	public void setCouponTp(String couponTp) {
		this.couponTp = couponTp;
	}
	public String getCouponSt() {
		return couponSt;
	}
	public void setCouponSt(String couponSt) {
		this.couponSt = couponSt;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getCouponNm() {
		return couponNm;
	}
	public void setCouponNm(String couponNm) {
		this.couponNm = couponNm;
	}
	@Override
	public String toString() {
		return "CouponVO [id=" + id + ", couponImg=" + couponImg + ", couponTp=" + couponTp + ", couponNm=" + couponNm
				+ ", couponSt=" + couponSt + ", startDt=" + startDt + ", endDt=" + endDt + ", cost=" + cost + "]";
	}
}
