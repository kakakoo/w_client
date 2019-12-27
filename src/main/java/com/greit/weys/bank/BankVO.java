package com.greit.weys.bank;

public class BankVO {

	private String usrNm = "이동서";
	private String usrBirth = "19870525";
	private String usrSex = "1";
	private String usrEmail = "kk-koo@hanmail.net";
	private String usrTel = "01032255078";
	private String usrTelTp = "skt";
	private String usrBankNm;
	private String usrBankCd;
	private String usrBankNick;
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getUsrBirth() {
		return usrBirth;
	}
	public void setUsrBirth(String usrBirth) {
		this.usrBirth = usrBirth;
	}
	public String getUsrTel() {
		return usrTel;
	}
	public void setUsrTel(String usrTel) {
		this.usrTel = usrTel;
	}
	public String getUsrBankNm() {
		return usrBankNm;
	}
	public void setUsrBankNm(String usrBankNm) {
		this.usrBankNm = usrBankNm;
	}
	public String getUsrBankCd() {
		return usrBankCd;
	}
	public void setUsrBankCd(String usrBankCd) {
		this.usrBankCd = usrBankCd;
	}
	public String getUsrBankNick() {
		return usrBankNick;
	}
	public void setUsrBankNick(String usrBankNick) {
		this.usrBankNick = usrBankNick;
	}
	public String getUsrSex() {
		return usrSex;
	}
	public void setUsrSex(String usrSex) {
		this.usrSex = usrSex;
	}
	public String getUsrTelTp() {
		return usrTelTp;
	}
	public void setUsrTelTp(String usrTelTp) {
		this.usrTelTp = usrTelTp;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	@Override
	public String toString() {
		return "BankVO [usrNm=" + usrNm + ", usrBirth=" + usrBirth + ", usrSex=" + usrSex + ", usrEmail=" + usrEmail
				+ ", usrTel=" + usrTel + ", usrTelTp=" + usrTelTp + ", usrBankNm=" + usrBankNm + ", usrBankCd="
				+ usrBankCd + ", usrBankNick=" + usrBankNick + "]";
	}
	
}
