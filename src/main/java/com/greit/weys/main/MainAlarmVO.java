package com.greit.weys.main;

public class MainAlarmVO {

	private int armId;
	private String armTp;
	private String armSt;
	private String armTitle;
	private String armTarget;
	private String armVal;
	private String regDttm;
	public int getArmId() {
		return armId;
	}
	public void setArmId(int armId) {
		this.armId = armId;
	}
	public String getArmTp() {
		return armTp;
	}
	public void setArmTp(String armTp) {
		this.armTp = armTp;
	}
	public String getArmSt() {
		return armSt;
	}
	public void setArmSt(String armSt) {
		this.armSt = armSt;
	}
	public String getArmTitle() {
		return armTitle;
	}
	public void setArmTitle(String armTitle) {
		this.armTitle = armTitle;
	}
	public String getArmTarget() {
		return armTarget;
	}
	public void setArmTarget(String armTarget) {
		this.armTarget = armTarget;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	public String getArmVal() {
		return armVal;
	}
	public void setArmVal(String armVal) {
		this.armVal = armVal;
	}
	@Override
	public String toString() {
		return "MainAlarmVO [armId=" + armId + ", armTp=" + armTp + ", armSt=" + armSt + ", armTitle=" + armTitle
				+ ", armTarget=" + armTarget + ", armVal=" + armVal + ", regDttm=" + regDttm + "]";
	}
}
