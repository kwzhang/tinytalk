package com.designcraft.business.call;

class DialResponse {
	private String response;
	private String address;
	
	public DialResponse(String response, String address) {
		this.response = response;
		this.address = address;
	}
	
	public String getResponse() {
		return response;
	}
	public String getAddress() {
		return address;
	}
}
