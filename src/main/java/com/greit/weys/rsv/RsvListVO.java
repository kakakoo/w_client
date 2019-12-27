package com.greit.weys.rsv;

public class RsvListVO {

	private int rsvId;
	private String rsvNo;
	private String rsvSt;
	private String unit;
	private int rsvAmnt;
	private int getAmnt;
	private String storeNm;
	private String rsvDt;
	private String rsvForm;
	private String regDttm;
	public int getRsvId() {
		return rsvId;
	}
	public void setRsvId(int rsvId) {
		this.rsvId = rsvId;
	}
	public String getRsvNo() {
		return rsvNo;
	}
	public void setRsvNo(String rsvNo) {
		this.rsvNo = rsvNo;
	}
	public String getRsvSt() {
		return rsvSt;
	}
	public void setRsvSt(String rsvSt) {
		this.rsvSt = rsvSt;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getRsvAmnt() {
		return rsvAmnt;
	}
	public void setRsvAmnt(int rsvAmnt) {
		this.rsvAmnt = rsvAmnt;
	}
	public String getRsvDt() {
		return rsvDt;
	}
	public void setRsvDt(String rsvDt) {
		this.rsvDt = rsvDt;
	}
	public String getStoreNm() {
		return storeNm;
	}
	public void setStoreNm(String storeNm) {
		this.storeNm = storeNm;
	}
	public int getGetAmnt() {
		return getAmnt;
	}
	public void setGetAmnt(int getAmnt) {
		this.getAmnt = getAmnt;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	public String getRsvForm() {
		return rsvForm;
	}
	public void setRsvForm(String rsvForm) {
		this.rsvForm = rsvForm;
	}
	@Override
	public String toString() {
		return "RsvListVO [rsvId=" + rsvId + ", rsvNo=" + rsvNo + ", rsvSt=" + rsvSt + ", unit=" + unit + ", rsvAmnt="
				+ rsvAmnt + ", getAmnt=" + getAmnt + ", storeNm=" + storeNm + ", rsvDt=" + rsvDt + ", rsvForm="
				+ rsvForm + ", regDttm=" + regDttm + "]";
	}
	
}
