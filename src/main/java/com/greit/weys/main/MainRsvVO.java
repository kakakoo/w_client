package com.greit.weys.main;

public class MainRsvVO {

	private int rsvId;
	private String rsvDt;
	private String rsvAmnt;
	private String storeNm;
	private String rsvSt;
	private String rsvForm;
	public int getRsvId() {
		return rsvId;
	}
	public void setRsvId(int rsvId) {
		this.rsvId = rsvId;
	}
	public String getRsvDt() {
		return rsvDt;
	}
	public void setRsvDt(String rsvDt) {
		this.rsvDt = rsvDt;
	}
	public String getRsvAmnt() {
		return rsvAmnt;
	}
	public void setRsvAmnt(String rsvAmnt) {
		this.rsvAmnt = rsvAmnt;
	}
	public String getStoreNm() {
		return storeNm;
	}
	public void setStoreNm(String storeNm) {
		this.storeNm = storeNm;
	}
	public String getRsvSt() {
		return rsvSt;
	}
	public void setRsvSt(String rsvSt) {
		this.rsvSt = rsvSt;
	}
	public String getRsvForm() {
		return rsvForm;
	}
	public void setRsvForm(String rsvForm) {
		this.rsvForm = rsvForm;
	}
	@Override
	public String toString() {
		return "MainRsvVO [rsvId=" + rsvId + ", rsvDt=" + rsvDt + ", rsvAmnt=" + rsvAmnt + ", storeNm=" + storeNm
				+ ", rsvSt=" + rsvSt + ", rsvForm=" + rsvForm + "]";
	}
}
