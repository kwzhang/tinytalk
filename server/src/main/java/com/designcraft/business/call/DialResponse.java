package com.designcraft.business.call;

class DialResponse {
	private String receiver;
	private String response;
	private String address;
	
	public DialResponse(String response, String address, String receiver) {
		this.response = response;
		this.address = address;
		this.receiver = receiver;
	}
	
	public String getReceiver() {
		return receiver;
	}
	public String getResponse() {
		return response;
	}
	public String getAddress() {
		return address;
	}
}
