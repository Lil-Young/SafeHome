package com.ssafy.home.model.buyDto;

public class OfficetelsBuyDto {
	String sggCd;
	String sggNm;
	String umdNm;
	String jibun;
	String offiName;
	String excluUseAr;
	String dealYear;
	String dealMonth;
	String dealDay;
	String dealAmount;
	String floor;
	String buildYear;
	
	public String getSggCd() {
		return sggCd;
	}
	public void setSggCd(String sggCd) {
		this.sggCd = sggCd;
	}
	public String getSggNm() {
		return sggNm;
	}
	public void setSggNm(String sggNm) {
		this.sggNm = sggNm;
	}
	public String getUmdNm() {
		return umdNm;
	}
	public void setUmdNm(String umdNm) {
		this.umdNm = umdNm;
	}
	public String getJibun() {
		return jibun;
	}
	public void setJibun(String jibun) {
		this.jibun = jibun;
	}
	public String getOffiName() {
		return offiName;
	}
	public void setOffiName(String offiName) {
		this.offiName = offiName;
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
	
	@Override
	public String toString() {
		return "officetelsBuyDto [sggCd=" + sggCd + ", sggNm=" + sggNm + ", umdNm=" + umdNm + ", jibun=" + jibun
				+ ", offiName=" + offiName + ", excluUseAr=" + excluUseAr + ", dealYear=" + dealYear + ", dealMonth="
				+ dealMonth + ", dealDay=" + dealDay + ", dealAmount=" + dealAmount + ", floor=" + floor
				+ ", buildYear=" + buildYear + "]";
	}
	
}
