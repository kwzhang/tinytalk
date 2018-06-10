package com.designcraft.business.call;

class DialResponse {
	private final String type = "dialResponse";
	private String response;
	private String ip;
	
	public DialResponse(String response, String ip) {
		this.response = response;
		this.ip = ip;
	}
	
	public String getType() {
		return type;
	}
	public String getResponse() {
		return response;
	}
	public String getIp() {
		return ip;
	}
}
