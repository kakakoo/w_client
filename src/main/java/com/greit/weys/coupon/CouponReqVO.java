package com.greit.weys.coupon;

public class CouponReqVO {

	private String usrId;
	private String pageType;
	private String id;
	private String code;
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code.replaceAll(" ", "");
	}
	@Override
	public String toString() {
		return "CouponReqVO [usrId=" + usrId + ", pageType=" + pageType + ", id=" + id + ", code=" + code + "]";
	}
	public boolean checkCoupon(){
		if(code.equals("")){
			return false;
		}
		return true;
	}
}
