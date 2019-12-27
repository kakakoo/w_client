package com.greit.weys.rsv;

import java.util.List;
import java.util.Map;

public class RsvStoreVO {

	private int storeId;
	private String storeNm;
	private String storeDesc;
	private String storeAddr;
	private String storeImg;
	private String rsvImg = "";
	private String storeUrl;
	private String storeSt;
	private String storeAir;
	private String storeExc;
	private String commisTitle;
	private String commisTxt;
	private String storeCenter;
	private String storeCmt;
	private String storetTimeTxt;
	private int rsvCommis;
	private int deliverCms;
	private int deliverTime;
	private String storeTimeTxt;
	private double lat;
	private double lon;
	private List<Map<String, Object>> rsvDate;
	private List<Map<String, Object>> breakList;
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getStoreNm() {
		return storeNm;
	}
	public void setStoreNm(String storeNm) {
		this.storeNm = storeNm;
	}
	public String getStoreImg() {
		return storeImg;
	}
	public void setStoreImg(String storeImg) {
		this.storeImg = storeImg;
	}
	public String getStoreUrl() {
		return storeUrl;
	}
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
	public List<Map<String, Object>> getRsvDate() {
		return rsvDate;
	}
	public void setRsvDate(List<Map<String, Object>> rsvDate) {
		this.rsvDate = rsvDate;
	}
	public String getStoreAddr() {
		return storeAddr;
	}
	public void setStoreAddr(String storeAddr) {
		this.storeAddr = storeAddr;
	}
	public String getStoreSt() {
		return storeSt;
	}
	public void setStoreSt(String storeSt) {
		this.storeSt = storeSt;
	}
	public String getRsvImg() {
		return rsvImg;
	}
	public void setRsvImg(String rsvImg) {
		this.rsvImg = rsvImg;
	}
	public String getStoreAir() {
		return storeAir;
	}
	public void setStoreAir(String storeAir) {
		this.storeAir = storeAir;
	}
	public int getRsvCommis() {
		return rsvCommis;
	}
	public void setRsvCommis(int rsvCommis) {
		this.rsvCommis = rsvCommis;
	}
	public int getDeliverCms() {
		return deliverCms;
	}
	public void setDeliverCms(int deliverCms) {
		this.deliverCms = deliverCms;
	}
	public int getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(int deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getStoreExc() {
		return storeExc;
	}
	public void setStoreExc(String storeExc) {
		this.storeExc = storeExc;
	}
	public String getCommisTitle() {
		return commisTitle;
	}
	public void setCommisTitle(String commisTitle) {
		this.commisTitle = commisTitle;
	}
	public String getCommisTxt() {
		return commisTxt;
	}
	public void setCommisTxt(String commisTxt) {
		this.commisTxt = commisTxt;
	}
	public String getStoreCenter() {
		return storeCenter;
	}
	public void setStoreCenter(String storeCenter) {
		this.storeCenter = storeCenter;
	}
	public String getStoreCmt() {
		return storeCmt;
	}
	public void setStoreCmt(String storeCmt) {
		this.storeCmt = storeCmt;
	}
	public String getStoretTimeTxt() {
		return storetTimeTxt;
	}
	public void setStoretTimeTxt(String storetTimeTxt) {
		this.storetTimeTxt = storetTimeTxt;
	}
	public String getStoreTimeTxt() {
		return storeTimeTxt;
	}
	public void setStoreTimeTxt(String storeTimeTxt) {
		this.storeTimeTxt = storeTimeTxt;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getStoreDesc() {
		return storeDesc;
	}
	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}
	public List<Map<String, Object>> getBreakList() {
		return breakList;
	}
	public void setBreakList(List<Map<String, Object>> breakList) {
		this.breakList = breakList;
	}
	@Override
	public String toString() {
		return "RsvStoreVO [storeId=" + storeId + ", storeNm=" + storeNm + ", storeAddr=" + storeAddr + ", storeImg="
				+ storeImg + ", rsvImg=" + rsvImg + ", storeUrl=" + storeUrl + ", storeSt=" + storeSt + ", storeAir="
				+ storeAir + ", storeExc=" + storeExc + ", commisTitle=" + commisTitle + ", commisTxt=" + commisTxt
				+ ", rsvCommis=" + rsvCommis + ", deliverCms=" + deliverCms + ", deliverTime=" + deliverTime
				+ ", rsvDate=" + rsvDate + "]";
	}
}
