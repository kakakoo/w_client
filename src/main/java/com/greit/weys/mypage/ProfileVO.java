package com.greit.weys.mypage;

public class ProfileVO {

	private String usrNm;
	private String agree;
	private String usrTel;
	private String usrEmail;
	private String nation;
	private String qrCode;
	private String qrImg;
	private String forign;
	private int couponCnt;
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getAgree() {
		return agree;
	}
	public void setAgree(String agree) {
		this.agree = agree;
	}
	public String getUsrTel() {
		return usrTel;
	}
	public void setUsrTel(String usrTel) {
		this.usrTel = usrTel;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getQrImg() {
		return qrImg;
	}
	public void setQrImg(String qrImg) {
		this.qrImg = qrImg;
	}
	public int getCouponCnt() {
		return couponCnt;
	}
	public void setCouponCnt(int couponCnt) {
		this.couponCnt = couponCnt;
	}
	public String getForign() {
		return forign;
	}
	public void setForign(String forign) {
		this.forign = forign;
	}
	@Override
	public String toString() {
		return "ProfileVO [usrNm=" + usrNm + ", agree=" + agree + ", usrTel=" + usrTel + ", usrEmail=" + usrEmail
				+ ", nation=" + nation + ", qrCode=" + qrCode + ", qrImg=" + qrImg + ", forign=" + forign
				+ ", couponCnt=" + couponCnt + "]";
	}
}
