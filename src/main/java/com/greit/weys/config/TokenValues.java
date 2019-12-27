package com.greit.weys.config;

public class TokenValues {

	private String userKey;
	private String userName;
	private String userEmail;
	
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	@Override
	public String toString() {
		return "TokenValues [userKey=" + userKey + ", userName=" + userName + ", userEmail=" + userEmail + "]";
	}
	
}
