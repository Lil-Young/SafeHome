package com.ssafy.member.model;

public class UserInfoDto {
	private String userId;
	private String userName;
	private String emailId;
	private String emailDomain;
	private String homeName; // 공인중개사만 필요
	private String homeAddress; // 공인중개사만 필요
	private String phone; // 공인중개사만 필요
	private int type;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getEmailDomain() {
		return emailDomain;
	}
	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}
	public String getHomeName() {
		return homeName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "UserInfoDto [userId=" + userId + ", userName=" + userName + ", emailId=" + emailId + ", emailDomain="
				+ emailDomain + ", homeName=" + homeName + ", homeAddress=" + homeAddress + ", phone=" + phone
				+ ", type=" + type + "]";
	}
}
