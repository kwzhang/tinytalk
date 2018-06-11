package com.designcraft.business.call;

class DialResponse {
	private String response;
	private String ip;
	
	public DialResponse(String response, String ip) {
		this.response = response;
		this.ip = ip;
	}
	
	public String getResponse() {
		return response;
	}
	public String getIp() {
		return ip;
	}
}
