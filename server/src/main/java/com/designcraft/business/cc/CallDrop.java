package com.designcraft.business.cc;

public class CallDrop {
	private String ccNumber;
	private String recipient;
	
	public CallDrop(String ccNumber, String recipient) {
		this.ccNumber = ccNumber;
		this.recipient = recipient;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
