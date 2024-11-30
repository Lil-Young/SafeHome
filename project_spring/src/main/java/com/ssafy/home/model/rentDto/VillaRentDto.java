package com.ssafy.home.model.rentDto;

public class VillaRentDto {
	String sggCd;
	String umdNm;
	String mhouseNm;
	String jibun;
	String buildYear;
	String excluUseAr;
	String dealYear;
	String dealMonth;
	String dealDay;
	String deposit;
	String monthlyRent;
	String floor;
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
	public String getMhouseNm() {
		return mhouseNm;
	}
	public void setMhouseNm(String mhouseNm) {
		this.mhouseNm = mhouseNm;
	}
	public String getJibun() {
		return jibun;
	}
	public void setJibun(String jibun) {
		this.jibun = jibun;
	}
	public String getBuildYear() {
		return buildYear;
	}
	public void setBuildYear(String buildYear) {
		this.buildYear = buildYear;
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
	
	@Override
	public String toString() {
		return "villaRentDto [sggCd=" + sggCd + ", umdNm=" + umdNm + ", mhouseNm=" + mhouseNm + ", jibun=" + jibun
				+ ", buildYear=" + buildYear + ", excluUseAr=" + excluUseAr + ", dealYear=" + dealYear + ", dealMonth="
				+ dealMonth + ", dealDay=" + dealDay + ", deposit=" + deposit + ", monthlyRent=" + monthlyRent
				+ ", floor=" + floor + "]";
	}
}
