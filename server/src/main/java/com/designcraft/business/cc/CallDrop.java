package com.designcraft.business.cc;

public class CallDrop {
	private String ccNumber;
	private String ip;
	
	public CallDrop(String ccNumber, String ip) {
		this.ccNumber = ccNumber;
		this.ip = ip;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
