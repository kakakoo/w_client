package com.greit.weys.main;

public class MainReqVO {

	private int armId;
	private String pageType = "";
	private String usrId;
	private String type;
	private String version;
	private String isNew;
	public int getArmId() {
		return armId;
	}
	public void setArmId(int armId) {
		this.armId = armId;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	@Override
	public String toString() {
		return "MainReqVO [armId=" + armId + ", pageType=" + pageType + ", usrId=" + usrId + ", type=" + type + "]";
	}
	
}
