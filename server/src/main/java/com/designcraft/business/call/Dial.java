package com.designcraft.business.call;

class Dial {
	private final String type = "dial"; 
	private String sender;
	
	public Dial(String sender) {
		this.sender = sender;
	}

	public String getType() {
		return type;
	}

	public String getSender() {
		return sender;
	}
}