package com.ssafy.home.model.buyDto;

public class VillaBuyDto {
	String sggCd;
	String umdNm;
	String mhouseNm;
	String jibun;
	String buildYear;
	String excluUseAr;
	String landAr;
	String dealYear;
	String dealMonth;
	String dealDay;
	String dealAmount;
	String floor;
	String rgstDate;
	
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
	public String getLandAr() {
		return landAr;
	}
	public void setLandAr(String landAr) {
		this.landAr = landAr;
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
	public String getRgstDate() {
		return rgstDate;
	}
	public void setRgstDate(String rgstDate) {
		this.rgstDate = rgstDate;
	}
	
	@Override
	public String toString() {
		return "villaBuyDto [sggCd=" + sggCd + ", umdNm=" + umdNm + ", mhouseNm=" + mhouseNm + ", jibun=" + jibun
				+ ", buildYear=" + buildYear + ", excluUseAr=" + excluUseAr + ", landAr=" + landAr + ", dealYear="
				+ dealYear + ", dealMonth=" + dealMonth + ", dealDay=" + dealDay + ", dealAmount=" + dealAmount
				+ ", floor=" + floor + ", rgstDate=" + rgstDate + "]";
	}
}
