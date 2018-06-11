package com.designcraft.business.call;

class Dial {
	private String sender;
	private String address;
	
	public Dial(String sender, String address) {
		this.sender = sender;
		this.address = address;
	}

	public String getSender() {
		return sender;
	}
	
	public String getAddress() {
		return address;
	}
}