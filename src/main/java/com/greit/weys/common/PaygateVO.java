package com.greit.weys.common;

public class PaygateVO {

	private String usrId;
	private String firstNm;
	private String lastNm;
	private String bankCd;
	private String bankNum;
	private String code;
	private String encKey;
	private String memGuid;
	private String authSt;
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getFirstNm() {
		return firstNm;
	}
	public void setFirstNm(String firstNm) {
		this.firstNm = firstNm;
	}
	public String getLastNm() {
		return lastNm;
	}
	public void setLastNm(String lastNm) {
		this.lastNm = lastNm;
	}
	public String getBankCd() {
		return bankCd;
	}
	public void setBankCd(String bankCd) {
		this.bankCd = bankCd;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEncKey() {
		return encKey;
	}
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	public String getMemGuid() {
		return memGuid;
	}
	public void setMemGuid(String memGuid) {
		this.memGuid = memGuid;
	}
	public String getAuthSt() {
		return authSt;
	}
	public void setAuthSt(String authSt) {
		this.authSt = authSt;
	}
	public boolean checkInfo(){
		if(firstNm == null
				|| lastNm == null
				|| bankCd == null
				|| bankNum == null)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PaygateVO [usrId=" + usrId + ", firstNm=" + firstNm + ", lastNm=" + lastNm + ", bankCd=" + bankCd
				+ ", bankNum=" + bankNum + ", code=" + code + ", encKey=" + encKey + ", memGuid=" + memGuid + "]";
	}
	
}
