package com.ssafy.home.model;

public class GugunDto {
	String gugun;

	public GugunDto(String gugun){
		this.gugun = gugun;
	}
	public String getGugun() {
		return gugun;
	}

	public void setGugun(String gugun) {
		this.gugun = gugun;
	}
	
    @Override
    public String toString() {
        return "GugunDto{" +
                "gugun='" + gugun + '\'' +
                '}';
    }
}
