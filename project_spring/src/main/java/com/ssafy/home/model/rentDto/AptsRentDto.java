package com.ssafy.home.model.rentDto;

public class AptsRentDto {
	String sggCd;
	String umdNm;
	String aptNm;
	String jibun;
	String excluUseAr;
	String dealYear;
	String dealMonth;
	String dealDay;
	String deposit;
	String monthlyRent;
	String floor;
	String buildYear;
	
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
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(String monthlyRent) {
		this.monthlyRent = monthlyRent;
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
		return "aptsRent [sggCd=" + sggCd + ", umdNm=" + umdNm + ", aptNm=" + aptNm + ", jibun=" + jibun
				+ ", excluUseAr=" + excluUseAr + ", dealYear=" + dealYear + ", dealMonth=" + dealMonth + ", dealDay="
				+ dealDay + ", deposit=" + deposit + ", monthlyRent=" + monthlyRent + ", floor=" + floor
				+ ", buildYear=" + buildYear + "]";
	}
	
	
}
