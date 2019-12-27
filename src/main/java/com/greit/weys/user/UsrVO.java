package com.greit.weys.user;

public class UsrVO {

	private int usrId;
	private int unKey = 0;
	private String nation;
	private String usrTel;
	private String usrPw;
	private String uuid;
	private String os;
	private String usrNm;
	private String usrNmId;
	private String encKey;
	private String type;
	private String version;
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getUsrTel() {
		return usrTel;
	}
	public void setUsrTel(String usrTel) {
		if(usrTel.contains("-"))
			usrTel = usrTel.replaceAll("-", "");
		this.usrTel = usrTel;
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
	public String getEncKey() {
		return encKey;
	}
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm.replaceAll(" ", "");
	}
	public String getUsrNmId() {
		return usrNmId;
	}
	public void setUsrNmId(String usrNmId) {
		usrNmId = usrNmId.replaceAll(" ", "");
		if(usrNmId.length() == 13){
			usrNmId = usrNmId.substring(0, 6) + "-" + usrNmId.substring(6);
		}
		this.usrNmId = usrNmId;
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
	public int getUnKey() {
		return unKey;
	}
	public void setUnKey(int unKey) {
		this.unKey = unKey;
	}
	@Override
	public String toString() {
		return "UsrVO [usrId=" + usrId + ", unKey=" + unKey + ", nation=" + nation + ", usrTel=" + usrTel + ", usrPw="
				+ usrPw + ", uuid=" + uuid + ", os=" + os + ", usrNm=" + usrNm + ", usrNmId=" + usrNmId + ", encKey="
				+ encKey + ", type=" + type + ", version=" + version + "]";
	}
	public boolean checkTel(){
		if(nation == null
				|| usrTel == null
				|| type == null)
			return false;
		if(type.equals("L") || type.equals("J")){
			return true;
		}
		return false;
	}
	public boolean findPw(){
		if(nation == null
				|| usrTel == null)
			return false;
		return true;
	}
	public boolean checkLoginV7(){
		if(nation == null
				|| usrTel == null
				|| usrPw == null
				|| os == null
				|| version == null)
			return false;
		return true;
	}
	public boolean checkLogin(){
		if(nation == null
				|| unKey == 0
				|| usrTel == null
				|| usrPw == null
				|| os == null
				|| version == null)
			return false;
		return true;
	}
	public boolean checkSetPw() {
		if(nation == null
				|| usrTel == null
				|| usrPw == null)
			return false;
		return true;
	}
	public boolean checkUsrCertify() {
		if(usrNm == null
				|| usrNmId == null)
			return false;
		return true;
	}
}
