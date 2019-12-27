package com.greit.weys.mypage;

public class AlrmExcVO {

	private int ueaId;
	private int usrId;
	private String unit;
	private double alrmRate;
	private double basicRate;
	private String alrmSt;
	public int getUeaId() {
		return ueaId;
	}
	public void setUeaId(int ueaId) {
		this.ueaId = ueaId;
	}
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getAlrmRate() {
		return alrmRate;
	}
	public void setAlrmRate(double alrmRate) {
		this.alrmRate = alrmRate;
	}
	public double getBasicRate() {
		return basicRate;
	}
	public void setBasicRate(double basicRate) {
		this.basicRate = basicRate;
	}
	public String getAlrmSt() {
		return alrmSt;
	}
	public void setAlrmSt(String alrmSt) {
		this.alrmSt = alrmSt;
	}
	@Override
	public String toString() {
		return "AlrmExcVO [ueaId=" + ueaId + ", usrId=" + usrId + ", unit=" + unit + ", alrmRate=" + alrmRate
				+ ", basicRate=" + basicRate + ", alrmSt=" + alrmSt + "]";
	}
	public boolean checkAlrm(){
		if(unit == null
				|| alrmRate == 0){
			return false;
		}
		return true;
	}
}
