package com.greit.weys.main;

public class BannerVO {

	private int bannerId;
	private String bannerNm;
	private String bannerUrl;
	private String redirectUrl;
	private String redirectApp;
	private String target;
	
	private String cbNm;
	private String cbSImg;
	private String cbBImg;
	private String modalNm;
	private String btnNm;
	private String cbUrl;
	public int getBannerId() {
		return bannerId;
	}
	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}
	public String getBannerNm() {
		return bannerNm;
	}
	public void setBannerNm(String bannerNm) {
		this.bannerNm = bannerNm;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getRedirectApp() {
		return redirectApp;
	}
	public void setRedirectApp(String redirectApp) {
		this.redirectApp = redirectApp;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getCbNm() {
		return cbNm;
	}
	public void setCbNm(String cbNm) {
		this.cbNm = cbNm;
	}
	public String getCbSImg() {
		return cbSImg;
	}
	public void setCbSImg(String cbSImg) {
		this.cbSImg = cbSImg;
	}
	public String getCbBImg() {
		return cbBImg;
	}
	public void setCbBImg(String cbBImg) {
		this.cbBImg = cbBImg;
	}
	public String getCbUrl() {
		return cbUrl;
	}
	public void setCbUrl(String cbUrl) {
		this.cbUrl = cbUrl;
	}
	public String getModalNm() {
		return modalNm;
	}
	public void setModalNm(String modalNm) {
		this.modalNm = modalNm;
	}
	public String getBtnNm() {
		return btnNm;
	}
	public void setBtnNm(String btnNm) {
		this.btnNm = btnNm;
	}
	@Override
	public String toString() {
		return "BannerVO [bannerId=" + bannerId + ", bannerNm=" + bannerNm + ", bannerUrl=" + bannerUrl
				+ ", redirectUrl=" + redirectUrl + ", redirectApp=" + redirectApp + ", target=" + target + ", cbNm="
				+ cbNm + ", cbSImg=" + cbSImg + ", cbBImg=" + cbBImg + ", modalNm=" + modalNm + ", btnNm=" + btnNm
				+ ", cbUrl=" + cbUrl + "]";
	}
	
}
