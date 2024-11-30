package com.ssafy.member.model;

public class MemberDto {

	private String userId;
	private String userName;
	private String userPwd;
	private String emailId;
	private String emailDomain;
	private String joinDate;
	private String token;
	private String homeName;
	private String homeAddress;
	private String phone;
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
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
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
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
		return "MemberDto [userId=" + userId + ", userName=" + userName + ", userPwd=" + userPwd + ", emailId="
				+ emailId + ", emailDomain=" + emailDomain + ", joinDate=" + joinDate + ", token=" + token
				+ ", homeName=" + homeName + ", homeAddress=" + homeAddress + ", phone=" + phone + ", type=" + type
				+ "]";
	}
	
	

}
