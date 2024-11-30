package com.ssafy.home.model.buyDto;

public class AptsBuyDto {
	String sggCd;
	String umdNm;
	String aptNm;
	String jibun;
	String excluUseAr;
	String dealYear;
	String dealMonth;
	String dealDay;
	String dealAmount;
	String floor;
	String buildYear;
	String rgstDate;
	String aptDong;
	
	public String getSggCd() {
		return sggCd;
	}
	public void setSggCd(String sggCd) {
		this.sggCd = sggCd;
	}
	public String getUmdNm() {
		return umdNm;
	}
	public void setUmdNm(String umdNm) {
		this.umdNm = umdNm;
	}
	public String getAptNm() {
		return aptNm;
	}
	public void setAptNm(String aptNm) {
		this.aptNm = aptNm;
	}
	public String getJibun() {
		return jibun;
	}
	public void setJibun(String jibun) {
		this.jibun = jibun;
	}
	public String getExcluUseAr() {
		return excluUseAr;
	}
	public void setExcluUseAr(String excluUseAr) {
		this.excluUseAr = excluUseAr;
	}
	public String getDealYear() {
		return dealYear;
	}
	public void setDealYear(String dealYear) {
		this.dealYear = dealYear;
	}
	public String getDealMonth() {
		return dealMonth;
	}
	public void setDealMonth(String dealMonth) {
		this.dealMonth = dealMonth;
	}
	public String getDealDay() {
		return dealDay;
	}
	public void setDealDay(String dealDay) {
		this.dealDay = dealDay;
	}
	public String getDealAmount() {
		return dealAmount;
	}
	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getBuildYear() {
		return buildYear;
	}
	public void setBuildYear(String buildYear) {
		this.buildYear = buildYear;
	}
	public String getRgstDate() {
		return rgstDate;
	}
	public void setRgstDate(String rgstDate) {
		this.rgstDate = rgstDate;
	}
	public String getAptDong() {
		return aptDong;
	}
	public void setAptDong(String aptDong) {
		this.aptDong = aptDong;
	}
	
	@Override
	public String toString() {
		return "aptsBuyDto [sggCd=" + sggCd + ", umdNm=" + umdNm + ", aptNm=" + aptNm + ", jibun=" + jibun
				+ ", excluUseAr=" + excluUseAr + ", dealYear=" + dealYear + ", dealMonth=" + dealMonth + ", dealDay="
				+ dealDay + ", dealAmount=" + dealAmount + ", floor=" + floor + ", buildYear=" + buildYear
				+ ", rgstDate=" + rgstDate + ", aptDong=" + aptDong + "]";
	}
	
	
	
}
