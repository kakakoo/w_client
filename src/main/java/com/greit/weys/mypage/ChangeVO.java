package com.greit.weys.mypage;

public class ChangeVO {

	private String originPw;
	private String newPw;
	private String usrId;
	private String usrTel;
	private String nation;
	private String usrEmail;
	private String agree;
	private String encKey;
	private int unKey = 0;
	public String getOriginPw() {
		return originPw;
	}
	public void setOriginPw(String originPw) {
		this.originPw = originPw;
	}
	public String getNewPw() {
		return newPw;
	}
	public void setNewPw(String newPw) {
		this.newPw = newPw;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrTel() {
		return usrTel;
	}
	public void setUsrTel(String usrTel) {
		this.usrTel = usrTel;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getEncKey() {
		return encKey;
	}
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAgree() {
		return agree;
	}
	public void setAgree(String agree) {
		this.agree = agree;
	}
	public int getUnKey() {
		return unKey;
	}
	public void setUnKey(int unKey) {
		this.unKey = unKey;
	}
	@Override
	public String toString() {
		return "ChangeVO [originPw=" + originPw + ", newPw=" + newPw + ", usrId=" + usrId + ", usrTel=" + usrTel
				+ ", nation=" + nation + ", usrEmail=" + usrEmail + ", agree=" + agree + ", encKey=" + encKey + "]";
	}
}
