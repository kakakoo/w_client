package com.greit.weys.rsv;

public class RsvUnitVO {

	private String unitCd;
	private String unitNm;
	private int unitSize;
	private int unitMin;
	private int unitMax;
	private String rsvSt;
	private double basicRateAir;
	private double basicRateBank;
	private double basicRateWeys;
	private double weysCommis;
	private double airCommis;
	private double unitCommis;
	private int usrBonus;
	private int limitAmnt;
	private String rateDttm;
	public String getUnitCd() {
		return unitCd;
	}
	public void setUnitCd(String unitCd) {
		this.unitCd = unitCd;
	}
	public String getUnitNm() {
		return unitNm;
	}
	public void setUnitNm(String unitNm) {
		this.unitNm = unitNm;
	}
	public int getUnitSize() {
		return unitSize;
	}
	public void setUnitSize(int unitSize) {
		this.unitSize = unitSize;
	}
	public int getUnitMin() {
		return unitMin;
	}
	public void setUnitMin(int unitMin) {
		this.unitMin = unitMin;
	}
	public int getUnitMax() {
		return unitMax;
	}
	public void setUnitMax(int unitMax) {
		this.unitMax = unitMax;
	}
	public String getRsvSt() {
		return rsvSt;
	}
	public void setRsvSt(String rsvSt) {
		this.rsvSt = rsvSt;
	}
	public double getBasicRateAir() {
		return basicRateAir;
	}
	public void setBasicRateAir(double basicRateAir) {
		this.basicRateAir = basicRateAir;
	}
	public double getBasicRateBank() {
		return basicRateBank;
	}
	public void setBasicRateBank(double basicRateBank) {
		this.basicRateBank = basicRateBank;
	}
	public double getBasicRateWeys() {
		return basicRateWeys;
	}
	public void setBasicRateWeys(double basicRateWeys) {
		this.basicRateWeys = basicRateWeys;
	}
	public double getWeysCommis() {
		return weysCommis;
	}
	public void setWeysCommis(double weysCommis) {
		this.weysCommis = weysCommis;
	}
	public double getAirCommis() {
		return airCommis;
	}
	public void setAirCommis(double airCommis) {
		this.airCommis = airCommis;
	}
	public double getUnitCommis() {
		return unitCommis;
	}
	public void setUnitCommis(double unitCommis) {
		this.unitCommis = unitCommis;
	}
	public String getRateDttm() {
		return rateDttm;
	}
	public void setRateDttm(String rateDttm) {
		this.rateDttm = rateDttm;
	}
	public int getUsrBonus() {
		return usrBonus;
	}
	public void setUsrBonus(int usrBonus) {
		this.usrBonus = usrBonus;
	}
	public int getLimitAmnt() {
		return limitAmnt;
	}
	public void setLimitAmnt(int limitAmnt) {
		this.limitAmnt = limitAmnt;
	}
	@Override
	public String toString() {
		return "RsvUnitVO [unitCd=" + unitCd + ", unitNm=" + unitNm + ", unitSize=" + unitSize + ", unitMin=" + unitMin
				+ ", unitMax=" + unitMax + ", rsvSt=" + rsvSt + ", basicRateAir=" + basicRateAir + ", basicRateBank="
				+ basicRateBank + ", basicRateWeys=" + basicRateWeys + ", weysCommis=" + weysCommis + ", airCommis="
				+ airCommis + ", unitCommis=" + unitCommis + ", usrBonus=" + usrBonus + ", limitAmnt=" + limitAmnt
				+ ", rateDttm=" + rateDttm + "]";
	}
}
