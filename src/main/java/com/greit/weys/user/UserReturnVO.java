package com.greit.weys.user;

public class UserReturnVO {

	private String tokenWeys;
	private String usrNm;
	private String usrAk;
	private String uuid;
	private int noticeId;
	public String getTokenWeys() {
		return tokenWeys;
	}
	public void setTokenWeys(String tokenWeys) {
		this.tokenWeys = tokenWeys;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getUsrAk() {
		return usrAk;
	}
	public void setUsrAk(String usrAk) {
		this.usrAk = usrAk;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	@Override
	public String toString() {
		return "UserReturnVO [tokenWeys=" + tokenWeys + ", usrNm=" + usrNm + ", usrAk=" + usrAk + ", uuid=" + uuid
				+ ", noticeId=" + noticeId + "]";
	}
	
}
