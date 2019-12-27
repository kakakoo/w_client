package com.greit.weys.rsv;

public class RsvCancelVO {

	private int rsvId;
	private int usrId;
	private String unit;
	private String rsvSt;
	private int rsvAmnt;
	private int getAmnt;
	private int cancelCms;
	private double cancelRate;
	private int cancelAmnt;
	private String rsvNm;
	private String bankNm;
	private String bankCd;
	private String encKey;
	private String commisTxt;
	private String cancelCmt;
	public int getRsvId() {
		return rsvId;
	}
	public void setRsvId(int rsvId) {
		this.rsvId = rsvId;
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
	public int getGetAmnt() {
		return getAmnt;
	}
	public void setGetAmnt(int getAmnt) {
		this.getAmnt = getAmnt;
	}
	public int getCancelCms() {
		return cancelCms;
	}
	public void setCancelCms(int cancelCms) {
		this.cancelCms = cancelCms;
	}
	public double getCancelRate() {
		return cancelRate;
	}
	public void setCancelRate(double cancelRate) {
		this.cancelRate = cancelRate;
	}
	public int getCancelAmnt() {
		return cancelAmnt;
	}
	public void setCancelAmnt(int cancelAmnt) {
		this.cancelAmnt = cancelAmnt;
	}
	public String getRsvNm() {
		return rsvNm;
	}
	public void setRsvNm(String rsvNm) {
		this.rsvNm = rsvNm;
	}
	public String getBankNm() {
		return bankNm;
	}
	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}
	public String getBankCd() {
		return bankCd;
	}
	public void setBankCd(String bankCd) {
		this.bankCd = bankCd;
	}
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	public String getRsvSt() {
		return rsvSt;
	}
	public void setRsvSt(String rsvSt) {
		this.rsvSt = rsvSt;
	}
	public String getEncKey() {
		return encKey;
	}
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	public String getCommisTxt() {
		return commisTxt;
	}
	public void setCommisTxt(String commisTxt) {
		this.commisTxt = commisTxt;
	}
	public String getCancelCmt() {
		return cancelCmt;
	}
	public void setCancelCmt(String cancelCmt) {
		this.cancelCmt = cancelCmt;
	}
	@Override
	public String toString() {
		return "RsvCancelVO [rsvId=" + rsvId + ", unit=" + unit + ", rsvAmnt=" + rsvAmnt + ", getAmnt=" + getAmnt
				+ ", cancelCms=" + cancelCms + ", cancelRate=" + cancelRate + ", cancelAmnt=" + cancelAmnt + ", rsvNm="
				+ rsvNm + ", bankNm=" + bankNm + ", bankCd=" + bankCd + "]";
	}
	public boolean checkCancel(){
		if(bankNm == null ||
				bankNm.equals("") ||
				bankCd == null ||
				bankCd.equals("")){
			return false;
		}
		return true;
	}
}
