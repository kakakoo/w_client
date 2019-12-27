package com.greit.weys.user;

import java.util.Date;

public class UserDetailVO {

	private int usrId;
	private int unKey;
	private String usrAk;
	private String usrEmail;
	private String usrTel;
	private String nation;
	private String usrPw;
	private String uuid = "";
	private String os;
	private String usrSt;
	private String usrFrom;
	private String agree;
	private String usrNm;
	private String usrNmId;
	private String encKey;
	private String barcode;
	private String barcodeUrl;
	private String version;
	private String frdCd;
	private String tokenWeys;
	private Date tokenExpirationDttm;
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	public String getUsrAk() {
		return usrAk;
	}
	public void setUsrAk(String usrAk) {
		this.usrAk = usrAk;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getUsrTel() {
		return usrTel;
	}
	public void setUsrTel(String usrTel) {
		this.usrTel = usrTel;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getUsrPw() {
		return usrPw;
	}
	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getUsrSt() {
		return usrSt;
	}
	public void setUsrSt(String usrSt) {
		this.usrSt = usrSt;
	}
	public String getUsrFrom() {
		return usrFrom;
	}
	public void setUsrFrom(String usrFrom) {
		this.usrFrom = usrFrom;
	}
	public String getAgree() {
		return agree;
	}
	public void setAgree(String agree) {
		this.agree = agree;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getUsrNmId() {
		return usrNmId;
	}
	public void setUsrNmId(String usrNmId) {
		this.usrNmId = usrNmId;
	}
	public String getEncKey() {
		return encKey;
	}
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBarcodeUrl() {
		return barcodeUrl;
	}
	public void setBarcodeUrl(String barcodeUrl) {
		this.barcodeUrl = barcodeUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTokenWeys() {
		return tokenWeys;
	}
	public void setTokenWeys(String tokenWeys) {
		this.tokenWeys = tokenWeys;
	}
	public Date getTokenExpirationDttm() {
		return tokenExpirationDttm;
	}
	public void setTokenExpirationDttm(Date tokenExpirationDttm) {
		this.tokenExpirationDttm = tokenExpirationDttm;
	}
	public int getUnKey() {
		return unKey;
	}
	public void setUnKey(int unKey) {
		this.unKey = unKey;
	}
	public String getFrdCd() {
		return frdCd;
	}
	public void setFrdCd(String frdCd) {
		this.frdCd = frdCd;
	}
	@Override
	public String toString() {
		return "UserDetailVO [usrId=" + usrId + ", unKey=" + unKey + ", usrAk=" + usrAk + ", usrEmail=" + usrEmail
				+ ", usrTel=" + usrTel + ", nation=" + nation + ", usrPw=" + usrPw + ", uuid=" + uuid + ", os=" + os
				+ ", usrSt=" + usrSt + ", usrFrom=" + usrFrom + ", agree=" + agree + ", usrNm=" + usrNm + ", usrNmId="
				+ usrNmId + ", encKey=" + encKey + ", barcode=" + barcode + ", barcodeUrl=" + barcodeUrl + ", version="
				+ version + ", frdCd=" + frdCd + ", tokenWeys=" + tokenWeys + ", tokenExpirationDttm="
				+ tokenExpirationDttm + "]";
	}
	public boolean checkSignup() {

		if(usrEmail == null
				|| usrTel == null
				|| nation == null
				|| agree == null
				|| usrPw == null
				|| os == null)
			return false;

		if(usrEmail.equals("")
				|| usrTel.equals("")
				|| nation.equals("")
				|| agree.equals("")
				|| usrPw.equals("")
				|| os.equals(""))
			return false;
		return true;
	}
	public boolean checkUnknown(){

		if(version == null
				|| agree == null
				|| os == null)
			return false;
		return true;
	}
}
