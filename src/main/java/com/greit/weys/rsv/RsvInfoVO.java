package com.greit.weys.rsv;

public class RsvInfoVO {

	private int storeId = 0;
	private int usrId;
	private int rsvId;
	private String rsvNo;
	private String rsvSt;
	private String rsvTp;
	private String rsvNm;
	private String rsvTel;
	private String rsvEmail;
	private String rsvNmId;
	private String rsvSign;
	private String rsvDt;
	private String rsvPaper = "R";
	private int rsvDay;
	private String rsvTm;
	private String unitCd;
	private double basicRateBank = 0.0;
	private double basicRateWeys = 0.0;
	private double basicRateUser = 0.0;
	private String rateDttm;
	private String cancelDttm;
	private String icmDttm;
	private String modDttm;

	private int rsvAmnt = 0;
	private int rsvAmntUser = 0;
	private int rsvAmntWeys = 0;
	private int getAmnt = 0;
	private int getAmntWeys = 0;
	private int bankAmnt = 0;
	private int rsvBenefit = 0;
	private int rsvCost = 0;
	
	private double weysCommis;
	private int weysCommisVal;
	private double weysBonus;
	private int weysBonusVal;
	private String bonusNm;
	
	private int cancelCms;
	private int cancelAmnt;

	private int rsvCommis;
	private int cms;
	private int orgCms;
	private int deliverTime;
	
	private String refId;
	private String tid;
	private String vbankNm;
	private String vbankCd;
	private String vbankHolder;
	private String vbankDue;
	
	private String retBankNm;
	private String retBankCd;
	
	private String rsvQr;
	private String rsvQrImg;
	private String mngNm;
	private String mngTel;
	private String storeNm;
	private String storeImg;
	private String rsvImg;
	private String storeUrl;
	private String storeAir;
	private String storeAddr;
	private String commisTxt;

	private String key;
	private String merchantUid;
	private String imp_uid;

	private int bonusId = 0;
	private int couponId = 0;
	private String rsvForm = "R";
	private String rsvAddr;
	private String rsvAddrDetail;
	private String roadAddr;
	private String zipCode;
	private String lon;
	private String lat;
	
	private String forign;
	private String nat;
	private String passNo;
	private String surNm;
	private String givNm;
	
	private String memo;
	private String storeInfo;
	private String payTp = "N";
	
	public String getRsvSt() {
		return rsvSt;
	}
	public void setRsvSt(String rsvSt) {
		this.rsvSt = rsvSt;
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
	public String getMngNm() {
		return mngNm;
	}
	public void setMngNm(String mngNm) {
		this.mngNm = mngNm;
	}
	public String getMngTel() {
		return mngTel;
	}
	public void setMngTel(String mngTel) {
		this.mngTel = mngTel;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getRsvTp() {
		return rsvTp;
	}
	public void setRsvTp(String rsvTp) {
		this.rsvTp = rsvTp;
	}
	public String getRsvNm() {
		return rsvNm;
	}
	public void setRsvNm(String rsvNm) {
		this.rsvNm = rsvNm;
	}
	public String getRsvDt() {
		return rsvDt;
	}
	public void setRsvDt(String rsvDt) {
		this.rsvDt = rsvDt;
	}
	public String getRsvTm() {
		return rsvTm;
	}
	public void setRsvTm(String rsvTm) {
		this.rsvTm = rsvTm;
	}
	public String getUnitCd() {
		return unitCd;
	}
	public void setUnitCd(String unitCd) {
		this.unitCd = unitCd;
	}
	public String getRateDttm() {
		return rateDttm;
	}
	public void setRateDttm(String rateDttm) {
		this.rateDttm = rateDttm;
	}
	public int getBankAmnt() {
		return bankAmnt;
	}
	public void setBankAmnt(int bankAmnt) {
		this.bankAmnt = bankAmnt;
	}
	public int getCms() {
		return cms;
	}
	public void setCms(int cms) {
		this.cms = cms;
	}
	public String getVbankNm() {
		return vbankNm;
	}
	public void setVbankNm(String vbankNm) {
		this.vbankNm = vbankNm;
	}
	public String getVbankCd() {
		return vbankCd;
	}
	public void setVbankCd(String vbankCd) {
		this.vbankCd = vbankCd;
	}
	public String getVbankHolder() {
		return vbankHolder;
	}
	public void setVbankHolder(String vbankHolder) {
		this.vbankHolder = vbankHolder;
	}
	public String getVbankDue() {
		return vbankDue;
	}
	public void setVbankDue(String vbankDue) {
		this.vbankDue = vbankDue;
	}
	public double getBasicRateWeys() {
		return basicRateWeys;
	}
	public void setBasicRateWeys(double basicRateWeys) {
		this.basicRateWeys = basicRateWeys;
	}
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	public String getMerchantUid() {
		return merchantUid;
	}
	public void setMerchantUid(String merchantUid) {
		this.merchantUid = merchantUid;
	}
	public double getBasicRateBank() {
		return basicRateBank;
	}
	public void setBasicRateBank(double basicRateBank) {
		this.basicRateBank = basicRateBank;
	}
	public String getRsvQr() {
		return rsvQr;
	}
	public void setRsvQr(String rsvQr) {
		this.rsvQr = rsvQr;
	}
	public String getRsvQrImg() {
		return rsvQrImg;
	}
	public void setRsvQrImg(String rsvQrImg) {
		this.rsvQrImg = rsvQrImg;
	}
	public String getStoreNm() {
		return storeNm;
	}
	public void setStoreNm(String storeNm) {
		this.storeNm = storeNm;
	}
	public String getRsvNmId() {
		return rsvNmId;
	}
	public void setRsvNmId(String rsvNmId) {
		this.rsvNmId = rsvNmId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getImp_uid() {
		return imp_uid;
	}
	public void setImp_uid(String imp_uid) {
		this.imp_uid = imp_uid;
	}
	public int getRsvId() {
		return rsvId;
	}
	public void setRsvId(int rsvId) {
		this.rsvId = rsvId;
	}
	public String getCancelDttm() {
		return cancelDttm;
	}
	public void setCancelDttm(String cancelDttm) {
		this.cancelDttm = cancelDttm;
	}
	public String getStoreImg() {
		return storeImg;
	}
	public void setStoreImg(String storeImg) {
		this.storeImg = storeImg;
	}
	public String getStoreUrl() {
		return storeUrl;
	}
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
	public int getRsvDay() {
		return rsvDay;
	}
	public void setRsvDay(int rsvDay) {
		this.rsvDay = rsvDay;
	}
	public int getRsvCommis() {
		return rsvCommis;
	}
	public void setRsvCommis(int rsvCommis) {
		this.rsvCommis = rsvCommis;
	}
	public double getBasicRateUser() {
		return basicRateUser;
	}
	public void setBasicRateUser(double basicRateUser) {
		this.basicRateUser = basicRateUser;
	}
	public String getStoreAir() {
		return storeAir;
	}
	public void setStoreAir(String storeAir) {
		this.storeAir = storeAir;
	}
	public String getRsvNo() {
		return rsvNo;
	}
	public void setRsvNo(String rsvNo) {
		this.rsvNo = rsvNo;
	}
	public String getRsvImg() {
		return rsvImg;
	}
	public void setRsvImg(String rsvImg) {
		this.rsvImg = rsvImg;
	}
	public String getRsvSign() {
		return rsvSign;
	}
	public void setRsvSign(String rsvSign) {
		this.rsvSign = rsvSign;
	}
	public String getIcmDttm() {
		return icmDttm;
	}
	public void setIcmDttm(String icmDttm) {
		this.icmDttm = icmDttm;
	}
	public String getRetBankNm() {
		return retBankNm;
	}
	public void setRetBankNm(String retBankNm) {
		this.retBankNm = retBankNm;
	}
	public String getRetBankCd() {
		return retBankCd;
	}
	public void setRetBankCd(String retBankCd) {
		this.retBankCd = retBankCd;
	}
	public String getStoreAddr() {
		return storeAddr;
	}
	public void setStoreAddr(String storeAddr) {
		this.storeAddr = storeAddr;
	}
	public int getCancelCms() {
		return cancelCms;
	}
	public void setCancelCms(int cancelCms) {
		this.cancelCms = cancelCms;
	}
	public int getCancelAmnt() {
		return cancelAmnt;
	}
	public void setCancelAmnt(int cancelAmnt) {
		this.cancelAmnt = cancelAmnt;
	}
	public String getRsvAddr() {
		return rsvAddr;
	}
	public void setRsvAddr(String rsvAddr) {
		this.rsvAddr = rsvAddr;
	}
	public String getRsvAddrDetail() {
		return rsvAddrDetail;
	}
	public void setRsvAddrDetail(String rsvAddrDetail) {
		this.rsvAddrDetail = rsvAddrDetail;
	}
	public int getRsvAmntUser() {
		return rsvAmntUser;
	}
	public void setRsvAmntUser(int rsvAmntUser) {
		this.rsvAmntUser = rsvAmntUser;
	}
	public int getRsvAmntWeys() {
		return rsvAmntWeys;
	}
	public void setRsvAmntWeys(int rsvAmntWeys) {
		this.rsvAmntWeys = rsvAmntWeys;
	}
	public String getRsvForm() {
		return rsvForm;
	}
	public void setRsvForm(String rsvForm) {
		this.rsvForm = rsvForm;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public int getGetAmntWeys() {
		return getAmntWeys;
	}
	public void setGetAmntWeys(int getAmntWeys) {
		this.getAmntWeys = getAmntWeys;
	}
	public String getRsvTel() {
		return rsvTel;
	}
	public void setRsvTel(String rsvTel) {
		this.rsvTel = rsvTel;
	}
	public String getRsvEmail() {
		return rsvEmail;
	}
	public void setRsvEmail(String rsvEmail) {
		this.rsvEmail = rsvEmail;
	}
	public String getRoadAddr() {
		return roadAddr;
	}
	public void setRoadAddr(String roadAddr) {
		this.roadAddr = roadAddr;
	}
	public String getModDttm() {
		return modDttm;
	}
	public void setModDttm(String modDttm) {
		this.modDttm = modDttm;
	}
	public int getRsvBenefit() {
		return rsvBenefit;
	}
	public void setRsvBenefit(int rsvBenefit) {
		this.rsvBenefit = rsvBenefit;
	}
	public int getRsvCost() {
		return rsvCost;
	}
	public void setRsvCost(int rsvCost) {
		this.rsvCost = rsvCost;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public int getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(int deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getRsvPaper() {
		return rsvPaper;
	}
	public void setRsvPaper(String rsvPaper) {
		this.rsvPaper = rsvPaper;
	}
	public double getWeysCommis() {
		return weysCommis;
	}
	public void setWeysCommis(double weysCommis) {
		this.weysCommis = weysCommis;
	}
	public int getWeysCommisVal() {
		return weysCommisVal;
	}
	public void setWeysCommisVal(int weysCommisVal) {
		this.weysCommisVal = weysCommisVal;
	}
	public double getWeysBonus() {
		return weysBonus;
	}
	public void setWeysBonus(double weysBonus) {
		this.weysBonus = weysBonus;
	}
	public int getWeysBonusVal() {
		return weysBonusVal;
	}
	public void setWeysBonusVal(int weysBonusVal) {
		this.weysBonusVal = weysBonusVal;
	}
	public String getBonusNm() {
		return bonusNm;
	}
	public void setBonusNm(String bonusNm) {
		this.bonusNm = bonusNm;
	}
	public int getBonusId() {
		return bonusId;
	}
	public void setBonusId(int bonusId) {
		this.bonusId = bonusId;
	}
	public String getNat() {
		return nat;
	}
	public void setNat(String nat) {
		this.nat = nat;
	}
	public String getPassNo() {
		return passNo;
	}
	public void setPassNo(String passNo) {
		this.passNo = passNo;
	}
	public String getSurNm() {
		return surNm;
	}
	public void setSurNm(String surNm) {
		this.surNm = surNm;
	}
	public String getGivNm() {
		return givNm;
	}
	public void setGivNm(String givNm) {
		this.givNm = givNm;
	}
	public String getForign() {
		return forign;
	}
	public void setForign(String forign) {
		this.forign = forign;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStoreInfo() {
		return storeInfo;
	}
	public void setStoreInfo(String storeInfo) {
		this.storeInfo = storeInfo;
	}
	public String getCommisTxt() {
		return commisTxt;
	}
	public void setCommisTxt(String commisTxt) {
		this.commisTxt = commisTxt;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPayTp() {
		return payTp;
	}
	public void setPayTp(String payTp) {
		this.payTp = payTp;
	}
	public int getOrgCms() {
		return orgCms;
	}
	public void setOrgCms(int orgCms) {
		this.orgCms = orgCms;
	}
	@Override
	public String toString() {
		return "RsvInfoVO [storeId=" + storeId + ", usrId=" + usrId + ", rsvId=" + rsvId + ", rsvNo=" + rsvNo
				+ ", rsvSt=" + rsvSt + ", rsvTp=" + rsvTp + ", rsvNm=" + rsvNm + ", rsvTel=" + rsvTel + ", rsvEmail="
				+ rsvEmail + ", rsvNmId=" + rsvNmId + ", rsvSign=" + rsvSign + ", rsvDt=" + rsvDt + ", rsvPaper="
				+ rsvPaper + ", rsvDay=" + rsvDay + ", rsvTm=" + rsvTm + ", unitCd=" + unitCd + ", basicRateBank="
				+ basicRateBank + ", basicRateWeys=" + basicRateWeys + ", basicRateUser=" + basicRateUser
				+ ", rateDttm=" + rateDttm + ", cancelDttm=" + cancelDttm + ", icmDttm=" + icmDttm + ", modDttm="
				+ modDttm + ", rsvAmnt=" + rsvAmnt + ", rsvAmntUser=" + rsvAmntUser + ", rsvAmntWeys=" + rsvAmntWeys
				+ ", getAmnt=" + getAmnt + ", getAmntWeys=" + getAmntWeys + ", bankAmnt=" + bankAmnt + ", rsvBenefit="
				+ rsvBenefit + ", rsvCost=" + rsvCost + ", weysCommis=" + weysCommis + ", weysCommisVal="
				+ weysCommisVal + ", weysBonus=" + weysBonus + ", weysBonusVal=" + weysBonusVal + ", bonusNm=" + bonusNm
				+ ", cancelCms=" + cancelCms + ", cancelAmnt=" + cancelAmnt + ", rsvCommis=" + rsvCommis + ", cms="
				+ cms + ", deliverTime=" + deliverTime + ", vbankNm=" + vbankNm + ", vbankCd=" + vbankCd
				+ ", vbankHolder=" + vbankHolder + ", vbankDue=" + vbankDue + ", retBankNm=" + retBankNm
				+ ", retBankCd=" + retBankCd + ", rsvQr=" + rsvQr + ", rsvQrImg=" + rsvQrImg + ", mngNm=" + mngNm
				+ ", mngTel=" + mngTel + ", storeNm=" + storeNm + ", storeImg=" + storeImg + ", rsvImg=" + rsvImg
				+ ", storeUrl=" + storeUrl + ", storeAir=" + storeAir + ", storeAddr=" + storeAddr + ", commisTxt="
				+ commisTxt + ", key=" + key + ", merchantUid=" + merchantUid + ", imp_uid=" + imp_uid + ", bonusId="
				+ bonusId + ", couponId=" + couponId + ", rsvForm=" + rsvForm + ", rsvAddr=" + rsvAddr
				+ ", rsvAddrDetail=" + rsvAddrDetail + ", roadAddr=" + roadAddr + ", zipCode=" + zipCode + ", lon="
				+ lon + ", lat=" + lat + ", forign=" + forign + ", nat=" + nat + ", passNo=" + passNo + ", surNm="
				+ surNm + ", givNm=" + givNm + ", memo=" + memo + ", storeInfo=" + storeInfo + "]";
	}

	public boolean insertRsvCheck(){
		/**
		 * 예약 입력시 필수 파라미터 체크
		 */
		if(storeId == 0
				|| rsvForm == null
				|| rsvDt == null
				|| rsvTm == null
				|| unitCd == null
				|| rateDttm == null
				|| rsvAmnt == 0
				|| basicRateWeys == 0.0
				|| weysCommis == 0.0
				|| weysCommisVal == 0
				|| getAmnt == 0
				|| vbankCd == null
				|| forign == null){
			return false;
		}
		return true;
	}

	public boolean insertRsvCheckV8(){
		/**
		 * 예약 입력시 필수 파라미터 체크
		 */
		if(storeId == 0
				|| rsvForm == null
				|| rsvDt == null
				|| rsvTm == null
				|| unitCd == null
				|| rateDttm == null
				|| rsvAmnt == 0
				|| basicRateWeys == 0.0
				|| weysCommis == 0.0
				|| weysCommisVal == 0
				|| getAmnt == 0
				|| vbankCd == null){
			return false;
		}
		return true;
	}
	
	public boolean certifyCheck(){
		if(rsvNm == null
				|| rsvNmId == null){
			return false;
		}
		return true;
	}
	
	public boolean checkForign(){
		if(nat == null
				|| passNo == null
				|| surNm == null
				|| givNm == null){
			return false;
		}
		return true;
	}
}
